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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
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
	
	private static final int WEBDRIVER_PAGE_TIMEOUT_SHORT = 10; 
	private static final int WEBDRIVER_PAGE_TIMEOUT_LONG = 20; 
	
	private static final String PROXY_EXTRACT_URL_00 = "http://xvre.daili666api.com/ip/?tid=557510611046590&num={PROXYNUM}&delay=1&category=2&foreign=none&exclude_ports=8090,8123&filter=on";
	private static final String PROXY_EXTRACT_URL_01 = "http://xvre.daili666api.com/ip/?tid=557510611046590&num={PROXYNUM}&operator=1,2,3&delay=1&category=2&foreign=none&filter=on";
	
	
	//private static final String searchKeyword = "吕记汤包";
	//private static final String targetLinkPartialText = "吕记包子吕记汤包 知名品牌吕记汤包";
	
	private static final String searchKeyword = "包子培训";
	private static final String targetLinkPartialText = "天津大帅包餐饮管理有限公司";
	
	public static final long SLEEP_TIME = 1000l; 
	
	@Autowired
	private ScheduleService scheduleService;
	
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
					
					// get ip information
					/*
					webDriver.manage().timeouts().pageLoadTimeout(WEBDRIVER_PAGE_TIMEOUT_SHORT, TimeUnit.SECONDS);
					webDriver.get("http://1212.ip138.com/ic.asp");
					String ipString = webDriver.findElement(By.xpath("//body/center")).getText().trim();
					logger.info("IP info: " + ipString);
					*/
					
					//webDriver.manage().timeouts().pageLoadTimeout(WEBDRIVER_PAGE_TIMEOUT_LONG, TimeUnit.SECONDS);
					
					int maxRetryCount = 10; 
					boolean foundOutFlag = false; 
					
					for (int retryIndex = 1; retryIndex < maxRetryCount; retryIndex++) {
						
						String random1 = String.valueOf(Math.random());
						String url = BAIDU_SEARCH_URL.replaceFirst("\\{KEYWORD\\}", searchKeyword).replaceFirst("\\{RANDOM\\}", random1);
						
						webDriver.get(url);
						
						try {
							// ERR_PROXY_CONNECTION_FAILED
							// Could not connect
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'ERR_PROXY_CONNECTION_FAILED')]")).getText().length() > 0) {
								logger.info("contains 'ERR_PROXY_CONNECTION_FAILED', break!");
								break;
							}
							
							if (webDriver.findElement(By.xpath("//*[contains(text(), 'Could not connect')]")).getText().length() > 0) {
								logger.info("contains 'Could not connect', break!");
								break;
							}
						} catch (NoSuchElementException ex) {
							// do nothing
						}
						
						
						try {
							WebDriverWait wait = new WebDriverWait(webDriver, WEBDRIVER_PAGE_TIMEOUT_SHORT);
					        wait.until(new ExpectedCondition<WebElement>(){  
					            @Override  
					            public WebElement apply(WebDriver d) {  
					                return d.findElement(By.partialLinkText(targetLinkPartialText));  
					        }}).click(); 
						
							foundOutFlag = true; 
							
							logger.info("Successful click through IP: " + proxyRow);
							
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
					logger.info("Error when using proxy: " + proxyRow);
					
					//ex.printStackTrace();
				} finally {
			        webDriver.quit();
				}
				
			}
	        
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
