package club.magicfun.aquila.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.model.Rank;
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

	private static final int RANK_SEARCH_QUEUE_NUMBER_PER_TIME = 5;
	
	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private RankingService rankingService;

	public RankSearchJob() {
		super();
	}

	@Scheduled(cron = "0/30 * * * * ? ")
	public void run() {

		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		Date currentDate = new Date();

		// determine if to run this job
		if (job != null && job.isJobReadyToRun()) {

			job = scheduleService.startJob(job);
			logger.debug("Job [" + job.getId() + "," + job.getClassName() + "] is started.");

			List<RankSearchQueue> rankSearchQueues = rankingService.findFewActivePendingRankSearchQueues(RANK_SEARCH_QUEUE_NUMBER_PER_TIME);

			if (rankSearchQueues != null && rankSearchQueues.size() > 0) {
				logger.info("Rank Search Queues count = " + rankSearchQueues.size());

				for (RankSearchQueue rankSearchQueue : rankSearchQueues) {
					logger.info("Dealing with Rank Search Queues: " + rankSearchQueue.getKeyword());
					
					// delete ranks first by rank search queue id
					List<Rank> originalRanks = rankingService.findAllRanksByRankSearchQueueId(rankSearchQueue.getId());
					if (originalRanks != null && originalRanks.size() > 0) {
						rankingService.deleteRanksInBatch(originalRanks); 
						logger.info(originalRanks.size() + " old ranks had been deleted.");
					}
					
					boolean containsError = false; 

					Set<RankSearchType> rankSearchTypes = rankSearchQueue.getRankSearchTypes();

					if (rankSearchTypes != null) {

						try {

							WebDriver webDriver = new PhantomJSDriver();

							for (RankSearchType rankSearchType : rankSearchTypes) {
								logger.info("Dealing with Rank Search Type: " + rankSearchType.getName());

								String url = null;
								try {
									url = RANK_SEARCH_URL_TEMPLATE
											.replaceFirst("\\{KEYWORD\\}", URLEncoder.encode(rankSearchQueue.getKeyword(), "utf-8"))
											.replaceFirst("\\{SORTTYPE\\}", rankSearchType.getSortType());
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								logger.info("URL: " + url);

								webDriver.get(url);

								List<WebElement> prodItemDivs = webDriver.findElements(By.xpath(
										"//*[@class='list']/div/div[contains(concat(' ', normalize-space(@class), ' '), ' item ')]"));

								int rankIndex = 0;

								for (WebElement prodItemDiv : prodItemDivs) {

									// System.out.println("webElement " +
									// prodItemDiv.toString());

									rankIndex++;

									WebElement itemTitleLink = prodItemDiv
											.findElement(By.xpath("div[@class='col col-2']/p/a"));

									String itemProductId = itemTitleLink.getAttribute("data-nid");

									String itemProductName = HtmlUtility.removeHtmlTags(itemTitleLink.getText());

									String itemShopName = HtmlUtility.removeHtmlTags(prodItemDiv
											.findElement(By.xpath("div[@class='col col-2']/div/div[@class='shop']"))
											.getText());

									String itemProductPrice = prodItemDiv
											.findElement(By.xpath("div[@class='col col-3']/div/span/strong")).getText();

									String itemProductDealCount = StringUtility.extractFirstFewDigits(prodItemDiv
											.findElement(By.xpath("div[@class='col col-4']/p[@class='deal-cnt']"))
											.getText());

									Rank rank = new Rank();
									rank.setRankSearchQueue(rankSearchQueue);
									rank.setRankSearchType(rankSearchType);
									rank.setRankNumber(rankIndex);
									rank.setProductId(Long.parseLong(itemProductId));
									rank.setProductName(itemProductName);
									rank.setProductPrice(Double.parseDouble(itemProductPrice));
									rank.setDealCount(Integer.parseInt(itemProductDealCount));
									rank.setShopName(itemShopName);
									rank.setCutoffDate(currentDate);
									rank.setCreateDatetime(new Date());

									rankingService.persist(rank);
								}

							}

							webDriver.close();
							webDriver.quit();
						} catch (NoSuchElementException ex) {
							containsError = true; 
						}
					}
					
					if (!containsError) {
						rankSearchQueue.setCutoffDate(new Date());
					} else {
						rankSearchQueue.setRetryCount(rankSearchQueue.getRetryCount() + 1);
						
						// update active flag if retry_cnt >= 2
						if (rankSearchQueue.getRetryCount() >= 2) {
							rankSearchQueue.setActiveFlag(false);
						}
					}
					
					rankingService.persist(rankSearchQueue);
				}
			}

			job = scheduleService.completeJob(job);
		} else {
			logger.debug("Job has not been configured for " + className + " or the job is not ready to run.");
		}
	}
}
