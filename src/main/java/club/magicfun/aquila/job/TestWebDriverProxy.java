package club.magicfun.aquila.job;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWebDriverProxy {

private static final Logger logger = LoggerFactory.getLogger(TestWebDriverProxy.class);
	
	private static final String BAIDU_SEARCH_URL = "http://www.baidu.com/s?wd={KEYWORD}&rsv_iqid＝{RANDOM}"; 
		
	public static final long SLEEP_TIME = 1000l; 
	
	public static void main(String[] args) {

		String dir = System.getProperty("user.dir");
		String osName = System.getProperty("os.name");
		
		// load the appropriate chrome drivers according to the OS type
		if (osName.toLowerCase().indexOf("windows") > -1) {
			System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "win"
					+ File.separator + "chromedriver.exe");
		} else if (osName.toLowerCase().indexOf("mac") > -1) {
			System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "mac"
					+ File.separator + "chromedriver");
		}
		
		String searchKeyword = "吕记汤包";
		String targetLinkPartialText = "吕记包子吕记汤包 知名品牌吕记汤包";
		
		Proxy proxy = new Proxy();
        proxy.setProxyType(ProxyType.MANUAL);
        proxy.setHttpProxy("180.97.29.57:80");
        //218.213.166.218:81
        
        /*
101.226.249.237:80
218.78.210.190:8080
116.246.6.52:80
101.231.48.200:3128
124.160.194.71:80
218.106.96.204:80
180.97.29.57:80
119.188.115.26:8088
182.18.19.219:3128
119.188.115.27:80
         * */
        
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		WebDriver webDriver = new ChromeDriver(capabilities);
		
		webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		webDriver.get("http://1212.ip138.com/ic.asp");
		
		String ipString = webDriver.findElement(By.xpath("//body/center")).getText().trim();
		logger.info("IP info: " + ipString);
		
		
		int maxRetryCount = 20; 
		
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
				// fount out, click now!
				logger.info("found out! click now! ");
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
		
		
		webDriver.close();  
        webDriver.quit();
	}

}