package club.magicfun.aquila.job;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.model.RankSearchHistory;
import club.magicfun.aquila.model.RankSearchQueue;
import club.magicfun.aquila.model.RankSearchType;
import club.magicfun.aquila.service.RankingService;
import club.magicfun.aquila.service.ScheduleService;
import club.magicfun.aquila.util.HtmlUtility;
import club.magicfun.aquila.util.StringUtility;

@Component
public class RankSearchJob {
	
	private static final Logger logger = LoggerFactory.getLogger(RankSearchJob.class);
	
	private static final String RANK_SEARCH_URL_TEMPLATE = "http://s.taobao.com/search?q={KEYWORD}&sort={SORTTYPE}&style=list";
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private RankingService rankingService;
	
	public RankSearchJob() {
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
			
			List<RankSearchQueue> rankSearchQueues = rankingService.findAllRankSearchQueues();
			
			if (rankSearchQueues != null) {
				logger.info("Rank Search Queues count = " + rankSearchQueues.size());
				
				for (RankSearchQueue rankSearchKeyword : rankSearchQueues) {
					logger.info("Dealing with Rank Search Queues: " + rankSearchKeyword.getKeyword());
					
					Set<RankSearchType> rankSearchTypes = rankSearchKeyword.getRankSearchTypes();
					
					if (rankSearchTypes != null) {
						
						WebDriver webDriver = new ChromeDriver();
						
						for (RankSearchType rankSearchType : rankSearchTypes) {
							logger.info("Dealing with Rank Search Type: " + rankSearchType.getName());
							
							String url = RANK_SEARCH_URL_TEMPLATE.replaceFirst("\\{KEYWORD\\}",
									rankSearchKeyword.getKeyword()).replaceFirst("\\{SORTTYPE\\}", rankSearchType.getSortType());
							logger.info("URL: " + url);
							
							webDriver.get(url);
							
							List<WebElement> prodItemDivs = webDriver.findElements(By.xpath("//*[@class='list']/div/div[contains(concat(' ', normalize-space(@class), ' '), ' item ')]"));
							
							int rankIndex = 0; 
							
							for (WebElement prodItemDiv : prodItemDivs) {
								
								//System.out.println("webElement " + prodItemDiv.toString());
								
								rankIndex++;
								
								WebElement itemTitleLink = prodItemDiv.findElement(By.xpath("div[@class='col col-2']/p/a"));
								
								String itemProductId = itemTitleLink.getAttribute("data-nid");
								
								String itemProductName = HtmlUtility.removeHtmlTags(itemTitleLink.getText());
								
								String itemShopName = HtmlUtility.removeHtmlTags(prodItemDiv.findElement(By.xpath("div[@class='col col-2']/div/div[@class='shop']")).getText());
								
								String itemProductPrice = prodItemDiv.findElement(By.xpath("div[@class='col col-3']/div/span/strong")).getText();
								
								String itemProductDealCount = StringUtility.extractFirstFewDigits(prodItemDiv.findElement(By.xpath("div[@class='col col-4']/p[@class='deal-cnt']")).getText());
								
								/*
								System.out.println("Item Rank index: " + rankIndex);
								System.out.println("item product id: " + itemProductId);
								System.out.println("item product name: " + itemProductName);
								System.out.println("item product price: " + itemProductPrice);
								System.out.println("item product deal count: " + itemProductDealCount);
								System.out.println("shop name: " + itemShopName);
								System.out.println("------------------------");
								*/
								
								RankSearchHistory rankSearchHistory = new RankSearchHistory();
								rankSearchHistory.setRankSearchKeyword(rankSearchKeyword);
								rankSearchHistory.setRankSearchType(rankSearchType);
								rankSearchHistory.setRankNumber(rankIndex);
								rankSearchHistory.setProductId(Long.parseLong(itemProductId));
								rankSearchHistory.setProductName(itemProductName);
								rankSearchHistory.setProductPrice(Double.parseDouble(itemProductPrice));
								rankSearchHistory.setDealCount(Integer.parseInt(itemProductDealCount));
								rankSearchHistory.setShopName(itemShopName);
								rankSearchHistory.setCreateDatetime(new Date());
								
								rankingService.persist(rankSearchHistory);
							}
							
						}
						
						webDriver.close();  
						webDriver.quit();
					}
				}
			}
			
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
