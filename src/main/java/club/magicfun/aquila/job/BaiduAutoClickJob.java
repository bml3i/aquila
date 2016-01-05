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
import club.magicfun.aquila.model.MyLog;
import club.magicfun.aquila.service.AgentService;
import club.magicfun.aquila.service.MyLogService;
import club.magicfun.aquila.service.ScheduleService;

@Component
public class BaiduAutoClickJob {
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduAutoClickJob.class);
	
	private static final String BAIDU_SEARCH_URL = "http://www.baidu.com/s?wd={KEYWORD}&rsv_iqid-{RANDOM}"; 
	
	private static final int AGENT_NUMBER_PER_TIME = 5; 
	
	private static final int WEBDRIVER_PAGE_TIMEOUT = 30; 
	
	//private static final String searchKeyword = "吕记汤包";
	//private static final String[] targetLinkPartialTexts = {"吕记包子速冻包子 知名品牌速冻包子", "包子加盟_包子连锁_灌汤包加盟-天津吕记包子加盟连锁", "包子培训汤包加盟灌汤包加盟狗不"};
	
	//private static final String searchKeyword = "包子培训";
	//private static final String[] targetLinkPartialTexts = {"天津包子培训基地", "大帅包"};
	
	private static final String searchKeyword = "包子加盟";
	private static final String[] targetLinkPartialTexts = {"全国包子加盟店店，吕记包子", "吕记包子包子加盟店","包子加盟店认准吕记包子"};
	
	//private static final String searchKeyword = "包子培训";
	//private static final String[] targetLinkPartialTexts = {"天津老卜家汤包", "老卜家汤包", "汤包培训班老卜家汤包-历史传承品牌"};
	
	public static final long SLEEP_TIME = 2000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private MyLogService myLogService;
	
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
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'ERR_CONNECTION_RESET')]")).getText().length() > 0) {
								logger.info("contains 'ERR_CONNECTION_RESET', break!");
								errorFlag = true; 
								break;
							}
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'Could not connect')]")).getText().length() > 0) {
								logger.info("contains 'Could not connect', break!");
								errorFlag = true; 
								break;
							}
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), '很抱歉，您要访问的页面不存在')]")).getText().length() > 0) {
								logger.info("contains '很抱歉，您要访问的页面不存在', break!");
								errorFlag = true; 
								break;
							}
							
						} catch (NoSuchElementException ex) {
							// do nothing
						}
						
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						boolean successFlag = false; 
						
						for (int targetTextIndex = 0; targetTextIndex < targetLinkPartialTexts.length; targetTextIndex++) {
							try {
						        WebElement targetLinkElement = webDriver.findElement(By.partialLinkText(targetLinkPartialTexts[targetTextIndex]));
						        targetLinkElement.click();
						        
						        successFlag = true; 
								logger.info("Successful click through proxy: " + agent.getIPAndPort());
								
								// get page title
								String pageTitle = webDriver.getTitle();
								
								MyLog myLog = new MyLog("BAIDU_CLICK_SUCCESS", agent.getIPAndPort() + "::" + pageTitle);
								myLogService.persist(myLog);
								
								try {
									Thread.sleep(10000l);
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
						
						if (successFlag) {
							logger.info("Break as it was already clicked successfully! ");
							break; 
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
				
				if (agent.getRetryCount() >= 2) {
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
