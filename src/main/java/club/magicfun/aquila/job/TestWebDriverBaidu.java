package club.magicfun.aquila.job;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWebDriverBaidu {

	private static final Logger logger = LoggerFactory.getLogger(TestWebDriverBaidu.class);
	
	private static final String BAIDU_SEARCH_URL = "https://www.baidu.com/s?wd={KEYWORD}"; 
	
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
		
		WebDriver webDriver = new ChromeDriver();
		
		String url = BAIDU_SEARCH_URL.replaceFirst("\\{KEYWORD\\}", searchKeyword);
		
		int maxRetryCount = 20; 
		
		for (int i = 1; i < maxRetryCount; i++) {
			
			webDriver.get(url);
			
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
