package club.magicfun.aquila.job;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Agent;
import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.service.AgentService;
import club.magicfun.aquila.service.ScheduleService;

@Component
public class BaiduAutoClickJob {
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduAutoClickJob.class);
	
	private static final String BAIDU_SEARCH_URL = "http://www.baidu.com/s?wd={KEYWORD}&rsv_iqid＝{RANDOM}"; 
	
	private static final int AGENT_NUMBER_PER_TIME = 5; 
	
	private static final int WEBDRIVER_PAGE_TIMEOUT = 30; 
	
	private static final String searchKeyword = "吕记汤包";
	private static final String targetLinkPartialText = "吕记包子速冻包子 知名品牌速冻包子";
	
	//private static final String searchKeyword = "包子培训";
	//private static final String targetLinkPartialText = "天津大帅包餐饮管理有限公司";
	
	public static final long SLEEP_TIME = 1000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AgentService agentService;
	
	public BaiduAutoClickJob() {
		super();
	}

	@Scheduled(cron = "0/15 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			List<Agent> activeAgents = agentService.findFewRecentActiveAgents(AGENT_NUMBER_PER_TIME);
			
			for (Agent agent: activeAgents) {
				boolean errorFlag = false; 
				WebDriver webDriver = null; 
				
				try {
					logger.info("using agent:" + agent.getIPAndPort());
					
					Proxy proxy = new Proxy();
			        proxy.setProxyType(ProxyType.MANUAL);
			        proxy.setHttpProxy(agent.getIPAndPort());
			        
					DesiredCapabilities capabilities = DesiredCapabilities.chrome();
					capabilities.setCapability(CapabilityType.PROXY, proxy);
					
					webDriver = new ChromeDriver(capabilities);
					
					webDriver.manage().timeouts().pageLoadTimeout(WEBDRIVER_PAGE_TIMEOUT, TimeUnit.SECONDS);
					
					int maxRetryCount = 10; 
					
					for (int retryIndex = 1; retryIndex < maxRetryCount; retryIndex++) {
						
						String random1 = String.valueOf(Math.random());
						String url = BAIDU_SEARCH_URL.replaceFirst("\\{KEYWORD\\}", searchKeyword).replaceFirst("\\{RANDOM\\}", random1);
						
						webDriver.get(url);
						
						try {
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'ERR_PROXY_CONNECTION_FAILED')]")).getText().length() > 0) {
								logger.info("contains 'ERR_PROXY_CONNECTION_FAILED', break!");
								errorFlag = true; 
								break;
							}
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'Could not connect')]")).getText().length() > 0) {
								logger.info("contains 'Could not connect', break!");
								errorFlag = true; 
								break;
							}
						} catch (NoSuchElementException ex) {
							break; 
						}
						
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						try {
					        WebElement targetLinkElement = webDriver.findElement(By.partialLinkText(targetLinkPartialText));
					        targetLinkElement.click();
					        
							logger.info("Successful click through proxy: " + agent.getIPAndPort());
							
							try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							break; 
						
						} catch (NoSuchElementException nsex) {
							//nsex.printStackTrace();
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
		        
				} catch (Exception ex) {
					logger.info("Error when using proxy: " + agent.getIPAndPort());
					
					//ex.printStackTrace();
				} finally {
			        webDriver.quit();
			        webDriver = null; 
				}
			
				// update agent
				if (errorFlag) {
					agent.setRetryCount(agent.getRetryCount() + 1);
				}
				
				if (agent.getRetryCount() >= 3) {
					agent.setActiveFlag(false);
				}
				
				agentService.persist(agent);
				
			}
	        
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
