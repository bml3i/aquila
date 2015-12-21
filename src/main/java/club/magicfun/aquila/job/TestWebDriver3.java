package club.magicfun.aquila.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
        
		/*
		taobao:
		
		http://item.taobao.com/item.htm?id=44057413538
		http://item.taobao.com/item.htm?id=42453456250
		http://item.taobao.com/item.htm?id=41129702139
		http://item.taobao.com/item.htm?id=1771185060
		http://item.taobao.com/item.htm?id=37833049213
		http://item.taobao.com/item.htm?id=38403988528
		
			1) promote, no category
			44057413538

			2) promote, category, picture
			42453456250

			3) promote, category, no picture
			41129702139

			4) no promote, no category
			1771185060

			5) no promote, category, picture
			37833049213

			6)no promote, category, no picture
			38403988528

		tmall:
			1) promote, category, picture
			5968030997
			
			2) promote, category, no picture
			523767556371
			
			3) promote, no category
			524740490317
			
			4) no promote, category, no picture
			521299241246
			
			5) no promote, no category
			520878856645
			
			6) no promote, category, picture
			45855398633
		*/

		List<Long> productIds = new ArrayList<Long>();
		productIds.add(44057413538l);
		productIds.add(42453456250l);
		productIds.add(41129702139l);
		productIds.add(1771185060l);
		productIds.add(37833049213l);
		productIds.add(38403988528l);
		
		for (Long productId : productIds) {
			
			String shopType = null;
			boolean containsCategoryFlag = false; 
			boolean isPromotedFlag = false; 
			
			String productName = null;
			String monthSaleAmount = null;
			String favouriteCount = null;
			String shopName = null;
			
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
			
			if (SHOP_TYPE_TAOBAO.equalsIgnoreCase(shopType)) {
				try{
					if (webDriver.findElement(By.xpath("//div[@class='tb-skin']/dl/dt")).getText().equals("颜色分类")) {
						containsCategoryFlag = true; 
					}
				} catch (NoSuchElementException ex) {
					// do nothing here
				}
				
				try{
					if (webDriver.findElement(By.xpath("//*[@id='J_PromoPriceNum']")).getText().length() > 0) {
						isPromotedFlag = true; 
					}
				} catch (NoSuchElementException ex) {
					// do nothing here
				}
				
				productName = webDriver.findElement(By.xpath("//div[@id='J_Title']/h3[@class='tb-main-title']")).getText().trim();
				monthSaleAmount = webDriver.findElement(By.xpath("//em[@class='J_TDealCount']")).getText();
				favouriteCount = StringUtility.extractFirstFewDigits(webDriver.findElement(By.xpath("//em[@class='J_FavCount']")).getText());
				shopName = webDriver.findElement(By.xpath("//div[@class='tb-shop-name']//a")).getText();
				
				
			} else if (SHOP_TYPE_TMALL.equalsIgnoreCase(shopType)) {
				try{
					if (webDriver.findElement(By.xpath("//div[@class='tb-sku']/dl/dt")).getText().equals("颜色分类")) {
						containsCategoryFlag = true; 
					}
				} catch (NoSuchElementException ex) {
					// do nothing here
				}
				
				try{
					if (webDriver.findElement(By.xpath("//*[@id='J_PromoPrice']")).getText().length() > 0) {
						isPromotedFlag = true; 
					}
				} catch (NoSuchElementException ex) {
					// do nothing here
				}
				
				productName = webDriver.findElement(By.xpath("//div[@class='tb-detail-hd']/h1")).getText().trim();
				monthSaleAmount = webDriver.findElement(By.xpath("//div[@class='tm-indcon']/span[@class='tm-count']")).getText();
				favouriteCount = StringUtility.extractFirstFewDigits(webDriver.findElement(By.xpath("//span[@id='J_CollectCount']")).getText());
				shopName = HtmlUtility.removeHtmlTags(webDriver.findElement(By.xpath("//a[@class='slogo-shopname']")).getText());
				
			}
			
			logger.info("containsCategoryFlag = " + containsCategoryFlag);
			logger.info("isPromotedFlag: " + isPromotedFlag);
			
			logger.info("productName: " + productName);
			logger.info("monthSaleAmount: " + monthSaleAmount);
			logger.info("favouriteCount: " + favouriteCount);
			logger.info("shopName: " + shopName);
			
			
			
			
			
			
			logger.info("---------------------");
			
		}
		
        webDriver.close();  
        webDriver.quit();
	}

}
