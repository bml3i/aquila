package club.magicfun.aquila.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.magicfun.aquila.util.HtmlUtility;
import club.magicfun.aquila.util.StringUtility;

public class TestWebDriver3 {

	private static final Logger logger = LoggerFactory.getLogger(TestWebDriver3.class);

	private static final String PRODUCT_CATEGORY_URL_TEMPLATE = "http://item.taobao.com/item.htm?id={PRODUCTID}";

	private static final String SHOP_TYPE_TMALL = "tmall";
	private static final String SHOP_TYPE_TAOBAO = "taobao";
	
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
		
		WebDriver webDriver = new ChromeDriver();
        
		List<Long> productIds = new ArrayList<Long>();
		productIds.add(21429655813l);
		productIds.add(37802140119l);
		productIds.add(44860074108l);
		productIds.add(520680685488l);
		
		for (Long productId : productIds) {
			
			String shopType = null; 
			String productName = null;
			
			logger.info("Dealing with Product Id: " + productId);
			
			String url = PRODUCT_CATEGORY_URL_TEMPLATE.replaceFirst("\\{PRODUCTID\\}", productId.toString());
			
			
			webDriver.get(url);
			
			System.out.println("Current URL: " + webDriver.getCurrentUrl());
			
			if (StringUtility.containsAny(webDriver.getCurrentUrl(), "detail.tmall.com")) {
				shopType = SHOP_TYPE_TMALL;
			} else if (StringUtility.containsAny(webDriver.getCurrentUrl(), "item.taobao.com")) {
				shopType = SHOP_TYPE_TAOBAO;
			}
			
			logger.info("shop type = " + shopType);
			
			// get product name
			
			if (SHOP_TYPE_TAOBAO.equalsIgnoreCase(SHOP_TYPE_TAOBAO)) {
				productName = webDriver.findElement(By.xpath("//div[@id='J_Title']/h3[@class='tb-main-title']")).getText();
			} else if (SHOP_TYPE_TAOBAO.equalsIgnoreCase(SHOP_TYPE_TMALL)) {
				productName = webDriver.findElement(By.xpath("//div[@class='tb-detail-hd']/h1")).getText();
			}
			
			logger.info("productName: " + productName);
			
		}
		
        webDriver.close();  
        webDriver.quit();
	}

}
