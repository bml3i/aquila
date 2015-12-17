package club.magicfun.aquila.job;

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
import club.magicfun.aquila.model.RankSearchKeyword;
import club.magicfun.aquila.model.RankSearchType;
import club.magicfun.aquila.service.RankingService;
import club.magicfun.aquila.service.ScheduleService;

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

	@Scheduled(cron = "0/15 * * * * ? ")
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
							
							// TODO
							
//							List<WebElement> prodItemDivs = webDriver.findElements(By.xpath("//*[@class='list']/div/div[contains(concat(' ', normalize-space(@class), ' '), ' item ')]"));
//							
//							for (WebElement webElement : prodItemDivs) {
//								logger.info("webElement " + webElement.toString());
//							}
							
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
