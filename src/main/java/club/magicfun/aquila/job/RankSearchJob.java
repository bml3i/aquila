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
import club.magicfun.aquila.model.RankSearchDetail;
import club.magicfun.aquila.model.RankSearchKeyword;
import club.magicfun.aquila.model.RankSearchType;
import club.magicfun.aquila.service.RankingService;
import club.magicfun.aquila.service.ScheduleService;
import club.magicfun.aquila.util.HtmlUtility;

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

	@Scheduled(cron = "0/20 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "," + job.getClassName() + "] is started.");
			
			List<RankSearchKeyword> rankSearchKeywords = rankingService.findAllRankSearchKeywords();
			
			if (rankSearchKeywords != null) {
				logger.info("Rank Search Keywords count = " + rankSearchKeywords.size());
				
				for (RankSearchKeyword rankSearchKeyword : rankSearchKeywords) {
					logger.info("Dealing with Rank Search Keywords: " + rankSearchKeyword.getKeyword());
					
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
								
								/*
								System.out.println("Item Rank index: " + rankIndex);
								System.out.println("item product id: " + itemProductId);
								System.out.println("item product name: " + itemProductName);
								System.out.println("item product price: " + itemProductPrice);
								System.out.println("shop name: " + itemShopName);
								System.out.println("------------------------");
								*/
								
								RankSearchDetail rankSearchDetail = new RankSearchDetail();
								rankSearchDetail.setRankSearchKeyword(rankSearchKeyword);
								rankSearchDetail.setRankSearchType(rankSearchType);
								rankSearchDetail.setRankNumber(rankIndex);
								rankSearchDetail.setProductId(Long.parseLong(itemProductId));
								rankSearchDetail.setProductName(itemProductName);
								rankSearchDetail.setProductPrice(Double.parseDouble(itemProductPrice));
								rankSearchDetail.setDealCount(0);
								rankSearchDetail.setShopName(itemShopName);
								rankSearchDetail.setCreateDatetime(new Date());
								
								rankingService.persist(rankSearchDetail);
							}
							
						}
						
						webDriver.close();  
						webDriver.quit();
					}
				}
			}
			
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is inactive/still in-processing.");
		}
	}
}
