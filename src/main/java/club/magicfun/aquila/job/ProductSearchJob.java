package club.magicfun.aquila.job;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Category;
import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.model.Product;
import club.magicfun.aquila.model.ProductSearchQueue;
import club.magicfun.aquila.service.ProductService;
import club.magicfun.aquila.service.ScheduleService;
import club.magicfun.aquila.util.HtmlUtility;
import club.magicfun.aquila.util.StringUtility;

@Component
public class ProductSearchJob {

	private static final Logger logger = LoggerFactory.getLogger(ProductSearchJob.class);
	
	private static final String PRODUCT_CATEGORY_URL_TEMPLATE = "http://item.taobao.com/item.htm?id={PRODUCTID}";

	private static final String SHOP_TYPE_TMALL = "tmall";
	private static final String SHOP_TYPE_TAOBAO = "taobao";
	
	private static final int PRODUCT_SEARCH_NUMBER_PER_TIME = 30; 

	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ProductService productService;
	
	public ProductSearchJob() {
		super();
	}
	
	@Scheduled(cron = "0/30 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		Date currentDate = new Date();
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.debug("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			List<ProductSearchQueue> productSearchQueues = productService.findFewActivePendingProductSearchQueues(PRODUCT_SEARCH_NUMBER_PER_TIME);
			
			if (productSearchQueues != null && productSearchQueues.size() > 0) {
				
				logger.info("Product Search queue size = " + productSearchQueues.size());
				
				WebDriver webDriver = new PhantomJSDriver();
				
				for (ProductSearchQueue productSearchQueue : productSearchQueues) {
					
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
					Product existProduct = productService.findProductByProductId(productSearchQueue.getProductId());
					
					if(existProduct != null) {
						logger.info("Deleting Product Id: " + productSearchQueue.getProductId());
						
						productService.deleteProduct(existProduct);
					}
					
					Product product = new Product();
					
					
					logger.info("Dealing with Product Id: " + productSearchQueue.getProductId());
					
					String url = PRODUCT_CATEGORY_URL_TEMPLATE.replaceFirst("\\{PRODUCTID\\}", productSearchQueue.getProductId().toString());
					
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
							//monthSaleAmount = webDriver.findElement(By.xpath("//em[@class='J_TDealCount']")).getText();
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
									// do not click if there is only one category, as it should have been selected by default. 
									if (categoryLinkElementList.size() >= 2) {
										categoryLinkElement.click();	
									}
									
									WebElement selectedCategoryLi = webDriver.findElement(By.xpath("//*[@data-property='颜色分类']/li[contains(concat(' ', normalize-space(@class), ' '), ' tb-selected ')]"));
									
									categoryName = selectedCategoryLi.findElements(By.cssSelector("a > span")).get(0).getAttribute("innerHTML");
						        	
									if (isPromotedFlag) {
										categoryPrice = webDriver.findElement(By.id("J_PromoPriceNum")).getText();
									} else {
										categoryPrice = StringUtility.extractFirstFewDigits(webDriver.findElement(By.id("J_StrPrice")).getText());
									}
									
									categoryStockNumber = webDriver.findElement(By.id("J_SpanStock")).getText();
									
									/*
									logger.info("categoryName = " + categoryName);
									logger.info("categoryPrice = " + categoryPrice);
									logger.info("categoryStockNumber = " + categoryStockNumber);
									*/
									
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
							
							product.setProductId(productSearchQueue.getProductId());
							product.setProductName(productName);
							//product.setMonthSaleAmount(new Integer(monthSaleAmount));
							
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
							//monthSaleAmount = webDriver.findElement(By.xpath("//div[@class='tm-indcon']/span[@class='tm-count']")).getText();
							favouriteCount = StringUtility.extractFirstFewDigits(webDriver.findElement(By.xpath("//span[@id='J_CollectCount']")).getText());
							shopName = HtmlUtility.removeHtmlTags(webDriver.findElement(By.xpath("//a[@class='slogo-shopname']")).getText());
							
							if (containsCategoryFlag) {
								
								categoryLinkElementList = webDriver.findElements(By.xpath("//*[@data-property='颜色分类']/li[not(contains(concat(' ', normalize-space(@class), ' '), ' tb-out-of-stock '))]/a"));
								
								for (WebElement categoryLinkElement : categoryLinkElementList) {
									
									Category category = new Category();
									
									String categoryName = null; 
									String categoryPrice = null; 
									String categoryStockNumber = null; 
									
									// simulate choosing a color category
									//if (categoryLinkElementList.size() >= 2) {
										categoryLinkElement.click();
									//}
									
									WebElement selectedCategoryLi = webDriver.findElement(By.xpath("//*[@data-property='颜色分类']/li[contains(concat(' ', normalize-space(@class), ' '), ' tb-selected ')]"));
									
									categoryName = selectedCategoryLi.findElements(By.cssSelector("a > span")).get(0).getAttribute("innerHTML");
						        	
									if (isPromotedFlag) {
										categoryPrice = StringUtility.extractFirstFewDigits(HtmlUtility.removeHtmlTags(webDriver.findElement(By.id("J_PromoPrice")).getText()));
									} else {
										categoryPrice = StringUtility.extractFirstFewDigits(HtmlUtility.removeHtmlTags(webDriver.findElement(By.id("J_StrPriceModBox")).getText()));
									}
									
									categoryStockNumber = StringUtility.extractFirstFewDigits(webDriver.findElement(By.id("J_EmStock")).getText());
									
									/*
									logger.info("categoryName = " + categoryName);
									logger.info("categoryPrice = " + categoryPrice);
									logger.info("categoryStockNumber = " + categoryStockNumber);
									*/
									
									category.setCategoryName(categoryName);
									
									if (categoryPrice != null && categoryPrice.length() > 0) {
										category.setCategoryPrice(new Double(categoryPrice));	
									} else {
										category.setCategoryPrice(0d);
									}
									
									category.setCategoryStockNumber(new Integer(categoryStockNumber));
									
									product.addProductCategory(category);
								}
							} else {
								if (isPromotedFlag) {
									productPrice = StringUtility.extractFirstFewDigits(HtmlUtility.removeHtmlTags(webDriver.findElement(By.id("J_PromoPrice")).getText()));
								} else {
									productPrice = StringUtility.extractFirstFewDigits(HtmlUtility.removeHtmlTags(webDriver.findElement(By.id("J_StrPriceModBox")).getText()));
								}
								
								String defaultStockNumber = StringUtility.extractFirstFewDigits(webDriver.findElement(By.id("J_EmStock")).getText());
								
								Category defaultCategory = new Category();
								defaultCategory.setCategoryName("默认款式");
								
								if (productPrice != null && productPrice.length() > 0) {
									defaultCategory.setCategoryPrice(new Double(productPrice));
								} else {
									defaultCategory.setCategoryPrice(0d);
								}
								
								defaultCategory.setCategoryStockNumber(new Integer(defaultStockNumber));
								
								product.addProductCategory(defaultCategory);
							}
							
							product.setProductId(productSearchQueue.getProductId());
							product.setProductName(productName);
							//product.setMonthSaleAmount(new Integer(monthSaleAmount));
							
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
							
						} catch (NoSuchElementException ex) {
							containsError = true; 
						}
					}
					
					/*
					logger.info("containsCategoryFlag = " + containsCategoryFlag);
					logger.info("isPromotedFlag: " + isPromotedFlag);
					
					logger.info("productName: " + productName);
					logger.info("monthSaleAmount: " + monthSaleAmount);
					logger.info("favouriteCount: " + favouriteCount);
					logger.info("shopName: " + shopName);
					
					logger.info("containsError: " + containsError);
					*/
					
					
					if (!containsError) {
						product.setActiveFlag(true);
						product.setCutoffDate(currentDate);
						
						logger.info("[product] - " + product.toString());
						
						product = productService.persist(product);
						
						logger.info("product " + product.getProductId() + " had been saved. ");
						
						productSearchQueue.setCutoffDate(currentDate);
						
					} else {
						logger.info("Failed to save product " + product.getProductId() + ".");
						
						// update retry_cnt
						productSearchQueue.setRetryCount(productSearchQueue.getRetryCount() + 1);
						
						// update active flag if retry_cnt >= 2
						if (productSearchQueue.getRetryCount() >= 2) {
							productSearchQueue.setActiveFlag(false);
						}
					}
					
					productService.persist(productSearchQueue);
					
				}
				
				webDriver.close();  
				webDriver.quit();
			}
			
			job = scheduleService.completeJob(job);
		} else {
			logger.debug("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
	
}
