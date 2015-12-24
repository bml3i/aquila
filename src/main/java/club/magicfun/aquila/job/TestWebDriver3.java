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

import club.magicfun.aquila.model.Category;
import club.magicfun.aquila.model.Product;
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
//		productIds.add(44057413538l);
//		productIds.add(42453456250l);
//		productIds.add(41129702139l);
		productIds.add(1771185060l);
//		productIds.add(37833049213l);
		productIds.add(38403988528l);
		
		for (Long productId : productIds) {
			
			boolean containsError = false; 
			
			String shopType = null;
			boolean containsCategoryFlag = false; 
			boolean isPromotedFlag = false; 
			
			String productName = null;
			String monthSaleAmount = null;
			String favouriteCount = null;
			String shopName = null;
			String productPrice = null;
			
			List<WebElement> categoryLinkElementList = null; 
			
			// Delete existing product if it exits.
			
			Product product = new Product();
			
			
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
				
				try{
					productName = webDriver.findElement(By.xpath("//div[@id='J_Title']/h3[@class='tb-main-title']")).getText().trim();
					monthSaleAmount = webDriver.findElement(By.xpath("//em[@class='J_TDealCount']")).getText();
					favouriteCount = StringUtility.extractFirstFewDigits(webDriver.findElement(By.xpath("//em[@class='J_FavCount']")).getText());
					shopName = webDriver.findElement(By.xpath("//div[@class='tb-shop-name']//a")).getText();
					
					
					if (containsCategoryFlag) {
						
						categoryLinkElementList = webDriver.findElements(By.xpath("//*[@data-property='颜色分类']/li/a"));
						
						for (WebElement categoryLinkElement : categoryLinkElementList) {
							
							Category category = new Category();
							
							String categoryName = null; 
							String categoryPrice = null; 
							String categoryStockNumber = null; 
							
							// simulate choosing a color category
							categoryLinkElement.click();
							
							WebElement selectedCategoryLi = webDriver.findElement(By.xpath("//*[@data-property='颜色分类']/li[contains(concat(' ', normalize-space(@class), ' '), ' tb-selected ')]"));
							
							categoryName = selectedCategoryLi.findElements(By.cssSelector("a > span")).get(0).getAttribute("innerHTML");
				        	
							if (isPromotedFlag) {
								categoryPrice = webDriver.findElement(By.id("J_PromoPriceNum")).getText();
							} else {
								categoryPrice = StringUtility.extractFirstFewDigits(webDriver.findElement(By.id("J_StrPrice")).getText());
							}
							
							categoryStockNumber = webDriver.findElement(By.id("J_SpanStock")).getText();
							
							logger.info("categoryName = " + categoryName);
							logger.info("categoryPrice = " + categoryPrice);
							logger.info("categoryStockNumber = " + categoryStockNumber);
							
							category.setCategoryName(categoryName);
							category.setCategoryPrice(new Double(categoryPrice));
							category.setCategoryStockNumber(new Integer(categoryStockNumber));
							
							product.addProductCategory(category);
						}
					} else {
						if (isPromotedFlag) {
							productPrice = webDriver.findElement(By.id("J_PromoPriceNum")).getText();
						} else {
							productPrice = StringUtility.extractFirstFewDigits(webDriver.findElement(By.id("J_StrPrice")).getText());
						}
						
						String defaultStockNumber = webDriver.findElement(By.id("J_SpanStock")).getText();
						
						Category defaultCategory = new Category();
						defaultCategory.setCategoryName("默认款式");
						defaultCategory.setCategoryPrice(new Double(productPrice));
						defaultCategory.setCategoryStockNumber(new Integer(defaultStockNumber));
						
						product.addProductCategory(defaultCategory);
					}
					
					product.setProductId(productId);
					product.setProductName(productName);
					product.setMonthSaleAmount(new Integer(monthSaleAmount));
					
					if (!containsCategoryFlag) {
						product.setProductPrice(new Double(productPrice));
					} else {
						product.setProductPrice(product.getMinCategoryPrice());
					}
					
					product.setShopName(shopName);
					
					if (favouriteCount != null && favouriteCount.trim().length() > 0) {
						product.setFavouriteCount(new Integer(favouriteCount));
					} else {
						product.setFavouriteCount(0);
					}
					
				} catch (Exception ex) {
					containsError = true; 
					ex.printStackTrace();
				}
				
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
				
				try{
					productName = webDriver.findElement(By.xpath("//div[@class='tb-detail-hd']/h1")).getText().trim();
					monthSaleAmount = webDriver.findElement(By.xpath("//div[@class='tm-indcon']/span[@class='tm-count']")).getText();
					favouriteCount = StringUtility.extractFirstFewDigits(webDriver.findElement(By.xpath("//span[@id='J_CollectCount']")).getText());
					shopName = HtmlUtility.removeHtmlTags(webDriver.findElement(By.xpath("//a[@class='slogo-shopname']")).getText());
				} catch (NoSuchElementException ex) {
					containsError = true; 
				}
			}
			
			logger.info("containsCategoryFlag = " + containsCategoryFlag);
			logger.info("isPromotedFlag: " + isPromotedFlag);
			
			logger.info("productName: " + productName);
			logger.info("monthSaleAmount: " + monthSaleAmount);
			logger.info("favouriteCount: " + favouriteCount);
			logger.info("shopName: " + shopName);
			
			logger.info("containsError: " + containsError);
			
			
			if (!containsError) {
				product.setActiveFlag(true);
				
				// save product here
			}
			
			logger.info("---------------------");
		}
		
        webDriver.close();  
        webDriver.quit();
	}

}
