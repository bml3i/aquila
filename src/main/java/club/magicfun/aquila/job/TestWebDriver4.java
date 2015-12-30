package club.magicfun.aquila.job;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.magicfun.aquila.util.StringUtility;

public class TestWebDriver4 {

	private static final Logger logger = LoggerFactory.getLogger(TestWebDriver4.class);
	
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
		
		// variables
		String shopType = null;
		
		Long productId = 522818128551l;
		
		WebDriver webDriver = new ChromeDriver();
		
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
        
		String url = PRODUCT_CATEGORY_URL_TEMPLATE.replaceFirst("\\{PRODUCTID\\}", productId.toString());
		
		webDriver.get(url);
		
		System.out.println("Current URL: " + webDriver.getCurrentUrl());
		
		if (StringUtility.containsAny(webDriver.getCurrentUrl(), "detail.tmall.com")) {
			shopType = SHOP_TYPE_TMALL;
		} else if (StringUtility.containsAny(webDriver.getCurrentUrl(), "item.taobao.com")) {
			shopType = SHOP_TYPE_TAOBAO;
		}
		
		logger.info("shop type = " + shopType);
        
        
		if (SHOP_TYPE_TAOBAO.equalsIgnoreCase(shopType)) {
        
	        WebElement dealCountButton = webDriver.findElement(By.xpath("//a[@shortcut-label='查看成交记录查看成交记录']"));
	        
	        dealCountButton.click();
	        
	        int targetPageIndex = 1; 
	        int currentPageIndex = 1; 
	        
	        while (targetPageIndex <= 1000) {
	        	
	        	logger.info("targetPageIndex: " + targetPageIndex);
	        	
	        	// click next page link
	        	if (targetPageIndex > 1) {
	        		
	        		WebElement nextPageLink = null; 
	        		
	        		try {
	        			nextPageLink = webDriver.findElement(By.xpath("//div[@id='deal-record']/div[@class='tb-pagination']//a[@class='J_TAjaxTrigger page-next']"));
		        		nextPageLink.click();
		        		
		        		// wait for 1 second
						try {
							Thread.sleep(1000l);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	        			
	        		} catch (NoSuchElementException ex) {
					} 
	        	}
	        	
	        	currentPageIndex = Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='deal-record']/div[@class='tb-pagination']//span[@class='page-cur']")).getAttribute("page"));
	        	
	        	logger.info("currentPageIndex: " + currentPageIndex);
	        	
	        	if (currentPageIndex < targetPageIndex) {
	        		logger.info("break the while block because currentPageIndex (" + currentPageIndex + ") < targetPageIndex (" + targetPageIndex + "). ");
	        		break;
	        	}
	        	
	        	List<WebElement> dealRecordElements = webDriver.findElements(By.xpath("//div[@id='deal-record']/table/tbody/tr[not(@class='tb-change-info')]"));
		        
		        for (WebElement dealRecord : dealRecordElements) {
		        	String dealDateTime = dealRecord.findElement(By.xpath("td[@class='tb-start']")).getText().trim();
		        	String dealAmount = dealRecord.findElement(By.xpath("td[@class='tb-amount']")).getText().trim();
		        	String dealSku = dealRecord.findElement(By.xpath("td[@class='tb-sku']")).getText().replaceAll("颜色分类:", "");
					
		        	logger.info(">>> " + dealDateTime + "|" + dealAmount + "|" + dealSku);
		        }
		        
		        targetPageIndex++; 
		        
	        }
	        
		} else if (SHOP_TYPE_TMALL.equalsIgnoreCase(shopType)) {
			// TODO
		}
        
        webDriver.close();  
        webDriver.quit();
        
		
	}

}
