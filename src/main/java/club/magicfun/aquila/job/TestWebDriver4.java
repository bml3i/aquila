package club.magicfun.aquila.job;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import club.magicfun.aquila.util.StringUtility;
import club.magicfun.aquila.util.WebDriverUtility;

public class TestWebDriver4 {

	private static final Logger logger = LoggerFactory.getLogger(TestWebDriver4.class);
	
	private static final String PRODUCT_CATEGORY_URL_TEMPLATE = "http://item.taobao.com/item.htm?id={PRODUCTID}";
	
	private static final String SHOP_TYPE_TMALL = "tmall";
	private static final String SHOP_TYPE_TAOBAO = "taobao";
	
	public static final int MAX_EXTRACT_PAGE_COUNT = 1000; 
	
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
		
		// variables
		String shopType = null;
		
		Long productId = 522818128551l; //taobao
		//Long productId = 17382419997l; //tmall
		
		WebDriver webDriver = new ChromeDriver();
		
		//webDriver.manage().window().maximize();
		
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); 
        
		String url = PRODUCT_CATEGORY_URL_TEMPLATE.replaceFirst("\\{PRODUCTID\\}", productId.toString());
		
		webDriver.get(url);
		
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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
	        
	        while (targetPageIndex <= MAX_EXTRACT_PAGE_COUNT) {
	        	
	        	logger.info("targetPageIndex: " + targetPageIndex);
	        	
	        	if (targetPageIndex > 1) {
	        		
	        		WebElement nextPageLink = null; 
	        		
	        		try {
	        			nextPageLink = webDriver.findElement(By.xpath("//div[@id='deal-record']/div[@class='tb-pagination']//a[@class='J_TAjaxTrigger page-next']"));
		        		nextPageLink.click();
		        		
						try {
							Thread.sleep(SLEEP_TIME);
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

			WebElement dealCountButton = webDriver.findElement(By.xpath("//div[@id='J_TabBarBox']//a[@href='#J_DealRecord']"));
	        
			logger.info("dealCountButton location: " + dealCountButton.getLocation());
			
	        dealCountButton.click();
	        
	        WebDriverUtility.hideElement(webDriver, "//div[@id='J_MUIMallbar']");
	        
	        int targetPageIndex = 1; 
	        int currentPageIndex = 1; 
	        
	        while (targetPageIndex <= MAX_EXTRACT_PAGE_COUNT) {
	        	
	        	logger.info("targetPageIndex: " + targetPageIndex);
	        	
	        	if (targetPageIndex > 1) {
	        		
	        		WebElement nextPageLink = null; 
	        		
	        		try {
	        			nextPageLink = webDriver.findElement(By.xpath("//div[@id='J_showBuyerList']/div[@class='pagination']//a[@class='J_TAjaxTrigger page-next'][last()]"));
		        		
	        			logger.info("nextPageLink location: " + nextPageLink.getLocation());
	        			logger.info("nextPageLink text: " + nextPageLink.getText());
	        			
	        			nextPageLink.click();
		        		
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	        			
	        		} catch (NoSuchElementException ex) {
	        			// do nothing
					} catch (StaleElementReferenceException sre) {
						// do nothing
					}
	        	}
	        	
	        	currentPageIndex = Integer.parseInt(webDriver.findElement(By.xpath("//div[@id='J_showBuyerList']/div[@class='pagination']//span[@class='page-cur']")).getText());
	        	
	        	logger.info("currentPageIndex: " + currentPageIndex);
	        	
	        	if (currentPageIndex < targetPageIndex) {
	        		logger.info("break the while block because currentPageIndex (" + currentPageIndex + ") < targetPageIndex (" + targetPageIndex + "). ");
	        		break;
	        	}
	        	
	        	List<WebElement> dealRecordElements = webDriver.findElements(By.xpath("//div[@id='J_showBuyerList']/table/tbody/tr[not(@class='tb-change-info') and position()>1]"));
		        
		        for (WebElement dealRecord : dealRecordElements) {
		        	String dealDateTime = dealRecord.findElement(By.xpath("td[@class='dealtime']")).getText().trim().replaceAll("\n", " ");
		        	String dealAmount = dealRecord.findElement(By.xpath("td[@class='quantity']")).getText().trim();
		        	String dealSku = dealRecord.findElement(By.xpath("td[@class='cell-align-l style']")).getText().replaceAll(" 颜色分类:", "");
					
		        	logger.info(">>> " + dealDateTime + "|" + dealAmount + "|" + dealSku);
		        }
		        
		        targetPageIndex++; 
	        }
			
		}
        
        webDriver.close();  
        webDriver.quit();
        
		
	}

}
