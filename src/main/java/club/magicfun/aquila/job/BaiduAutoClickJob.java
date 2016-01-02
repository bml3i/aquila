package club.magicfun.aquila.job;

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

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.service.ScheduleService;

@Component
public class BaiduAutoClickJob {
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduAutoClickJob.class);
	
	private static final String BAIDU_SEARCH_URL = "http://www.baidu.com/s?wd={KEYWORD}&rsv_iqid＝{RANDOM}"; 
	
	private static final int PROXY_NUM = 5; 
	
	private static final String PROXY_EXTRACT_URL_00 = "http://xvre.daili666api.com/ip/?tid=557510611046590&num={PROXYNUM}&delay=1&category=2&foreign=none&exclude_ports=8090,8123&filter=on";
	private static final String PROXY_EXTRACT_URL_01 = "http://xvre.daili666api.com/ip/?tid=557510611046590&num={PROXYNUM}&operator=2&delay=1&category=2&foreign=none&filter=on&area=山东";
	
	
	private static final String searchKeyword = "吕记汤包";
	private static final String targetLinkPartialText = "吕记包子吕记汤包 知名品牌吕记汤包";
	//private static final String targetLinkPartialText = "包子加盟_包子连锁_灌汤包加盟-天津吕记包子加盟连锁";
	
	
	public static final long SLEEP_TIME = 1000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
	public BaiduAutoClickJob() {
		super();
	}

	@Scheduled(cron = "0/30 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			WebDriver webDriver0 = new ChromeDriver();
			String proxyResult = null; 
			try {
				webDriver0.get(PROXY_EXTRACT_URL_01.replaceFirst("\\{PROXYNUM\\}", String.valueOf(PROXY_NUM)));
				proxyResult = webDriver0.findElement(By.xpath("//body/pre")).getText().trim();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				webDriver0.close();  
				webDriver0.quit();
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
					webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
					
					// get ip information
					webDriver.get("http://1212.ip138.com/ic.asp");
					String ipString = webDriver.findElement(By.xpath("//body/center")).getText().trim();
					logger.info("IP info: " + ipString);
					
					int maxRetryCount = 10; 
					boolean foundOutFlag = false; 
					
					for (int retryIndex = 1; retryIndex < maxRetryCount; retryIndex++) {
						
						String random1 = String.valueOf(Math.random());
						String url = BAIDU_SEARCH_URL.replaceFirst("\\{KEYWORD\\}", searchKeyword).replaceFirst("\\{RANDOM\\}", random1);
						
						webDriver.get(url);
						
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						try {
						
							WebElement targetLink = webDriver.findElement(By.partialLinkText(targetLinkPartialText));
							
							foundOutFlag = true; 
							targetLink.click();
							
							logger.info("Successful click through IP: " + proxyRow);
							
							try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							break; 
						
						} catch (NoSuchElementException nsex) {
						}
						
					}
		        
				} catch (Exception ex) {
					logger.info("Error when using proxy: " + proxyRow);
					
					//ex.printStackTrace();
				} finally {
					webDriver.close();  
			        webDriver.quit();
				}
				
			}
	        
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
