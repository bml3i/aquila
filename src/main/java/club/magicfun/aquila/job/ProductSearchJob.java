package club.magicfun.aquila.job;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.model.ProductSearchQueue;
import club.magicfun.aquila.service.ProductService;
import club.magicfun.aquila.service.ScheduleService;

@Component
public class ProductSearchJob {

	private static final Logger logger = LoggerFactory.getLogger(ProductSearchJob.class);
	
	private static final String PRODUCT_CATEGORY_URL_TEMPLATE = "http://item.taobao.com/item.htm?id={PRODUCTID}";


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
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			List<ProductSearchQueue> productSearchQueues = productService.findAllProductSearchQueues();
			
			if (productSearchQueues != null) {
				
				logger.info("Product Search queue size = " + productSearchQueues.size());
				
				WebDriver webDriver = new ChromeDriver();
				
				for (ProductSearchQueue productSearchQueue : productSearchQueues) {
					logger.info("Dealing with Product Id: " + productSearchQueue.getProductId());
					
					String url = PRODUCT_CATEGORY_URL_TEMPLATE.replaceFirst("\\{PRODUCTID\\}", productSearchQueue.getProductId().toString());
					
					webDriver.get(url);
					
					System.out.println("Current URL: " + webDriver.getCurrentUrl());
					
					
					
					
				}
				
				webDriver.close();  
				webDriver.quit();
			}
			
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
	
}
