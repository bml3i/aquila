package club.magicfun.aquila.job;

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
	
	private static final String PROXY_EXTRACT_URL = "http://xvre.daili666api.com/ip/?tid=557510611046590&num=1&delay=1&category=2&foreign=none&exclude_ports=8090,8123&filter=on";
	
	private static final String searchKeyword = "吕记汤包";
	//private static final String targetLinkPartialText = "吕记包子吕记汤包 知名品牌吕记汤包";
	private static final String targetLinkPartialText = "包子加盟_包子连锁_灌汤包加盟-天津吕记包子加盟连锁";
	
	
	public static final long SLEEP_TIME = 1000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
	public BaiduAutoClickJob() {
		super();
	}

	@Scheduled(cron = "0/10 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			WebDriver webDriver0 = new ChromeDriver();
			String proxyRow = null; 
			try {
				webDriver0.get(PROXY_EXTRACT_URL);
				proxyRow = webDriver0.findElement(By.xpath("//body/pre")).getText().trim();
				//logger.info("proxyRow:" + proxyRow);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				webDriver0.close();  
				webDriver0.quit();
			}
			
			Proxy proxy = new Proxy();
	        proxy.setProxyType(ProxyType.MANUAL);
	        
	        logger.info("proxyRow:" + proxyRow);
	        if(proxyRow != null && proxyRow.length() > 0) {
	        	proxy.setHttpProxy(proxyRow);
	        } else {
	        	proxy.setHttpProxy("127.0.0.1:80");
	        }
	        
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.PROXY, proxy);
			
			WebDriver webDriver = new ChromeDriver(capabilities);
			
			int maxRetryCount = 20; 
			boolean foundOutFlag = false; 
			
			for (int i = 1; i < maxRetryCount; i++) {
				
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
					
					logger.info("found out! click now! ");
					foundOutFlag = true; 
					targetLink.click();
					
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					break; 
				
				} catch (NoSuchElementException nsex) {
					// do nothing
				}
				
			}
	        
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
