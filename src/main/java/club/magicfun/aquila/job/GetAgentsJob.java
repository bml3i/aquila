package club.magicfun.aquila.job;

import java.util.Date;
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
public class GetAgentsJob {
	
	private static final Logger logger = LoggerFactory.getLogger(GetAgentsJob.class);
	
	private static final int PROXY_EXTRACT_NUM = 25; 
	
	private static final int WEBDRIVER_PAGE_TIMEOUT = 10; 
	
	private static final int MIN_ACTIVE_PROXY_NUM = 5; 
	
	private static final String SHOW_IP_INFO_URL = "http://1212.ip138.com/ic.asp"; 
	private static final String PROXY_EXTRACT_URL = "http://xvre.daili666api.com/ip/?tid=557510611046590&num={PROXYNUM}&operator=1,2,3&delay=1&category=2&foreign=none&filter=on";
	
	private static final String TARGET_SITE_URL = "http://www.baidu.com";
	
	public static final long SLEEP_TIME = 2000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private MyLogService myLogService;
	
	public GetAgentsJob() {
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
			
			List<Agent> activeAgents = agentService.findFewRecentActiveAgents(MIN_ACTIVE_PROXY_NUM);
			
			if (activeAgents.size() < MIN_ACTIVE_PROXY_NUM) {
				
				WebDriver webDriver0 = new ChromeDriver();
				String proxyResult = null; 
				try {
					webDriver0.get(PROXY_EXTRACT_URL.replaceFirst("\\{PROXYNUM\\}", String.valueOf(PROXY_EXTRACT_NUM)));
					proxyResult = webDriver0.findElement(By.xpath("//body/pre")).getText().trim();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					webDriver0.quit();
					webDriver0 = null; 
				}
				
				String[] proxyArray = proxyResult.split("\n");
				
				for (int proxyIndex = 0; proxyIndex < proxyArray.length; proxyIndex++) {
					
					WebDriver webDriver = null; 
					String proxyRow = null;
					
					try {
						proxyRow = proxyArray[proxyIndex];
						logger.info("proxyRow:" + proxyRow);
						
						Proxy proxy = new Proxy();
				        proxy.setProxyType(ProxyType.MANUAL);
				        proxy.setHttpProxy(proxyRow);
				        
						DesiredCapabilities capabilities = DesiredCapabilities.chrome();
						capabilities.setCapability(CapabilityType.PROXY, proxy);
						
						webDriver = new ChromeDriver(capabilities);
						
						// get ip information
						webDriver.manage().timeouts().pageLoadTimeout(WEBDRIVER_PAGE_TIMEOUT, TimeUnit.SECONDS);
						
						Date beginTime = new Date();
						webDriver.get(SHOW_IP_INFO_URL);
						Date endTime = new Date();
						
						String ipInfo = webDriver.findElement(By.xpath("//body/center")).getText().trim();
						logger.info("IP info: " + ipInfo);
						
						// test accessing the target site
						//Date beginTime = new Date();
						//webDriver.get(TARGET_SITE_URL);
						//Date endTime = new Date();
						
						long timeDifference = endTime.getTime() - beginTime.getTime();
						
						logger.info("Delay: " + timeDifference);
						
						// ignore if delay > 5000 ms
						if (timeDifference <= 5000) {
							logger.info("Persist proxy: " + proxyRow);
							
							Agent agent = new Agent();
							agent.setIpAddress(proxyRow.split(":")[0]);
							agent.setPortNumber(proxyRow.split(":")[1]);
							agent.setDescription(ipInfo);
							agent.setActiveFlag(true);
							agent.setRetryCount(0);
							agent.setDelay(timeDifference);
							
							agentService.persist(agent); 
							
							MyLog myLog = new MyLog("PROXY_GET_SUCCESS", agent.getIPAndPort() + "::" + ipInfo);
							myLogService.persist(myLog);
						}
						
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							//e.printStackTrace();
						}
						
					} catch (Exception ex) {
						logger.info("Error when using proxy: " + proxyRow);
					} finally {
				        webDriver.quit();
				        webDriver = null; 
					}
				}
			}
	        
			job = scheduleService.completeJob(job);
		} else {
			logger.info("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
