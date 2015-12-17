package club.magicfun.aquila.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.service.ScheduleService;

@Component
public class RankSearchJob {
	
	private static final Logger logger = LoggerFactory.getLogger(RankSearchJob.class);
	
	@Autowired
	private ScheduleService scheduleService;
	
	public RankSearchJob() {
		super();
	}

	@Scheduled(cron = "0/15 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job job = scheduleService.findJobByClassName(className);
		
		// determine if to run this job
		if (job != null && job.getActiveFlag() && "C".equalsIgnoreCase(job.getRunStatus())) {
			
			job = scheduleService.startJob(job);
			logger.info("Job [" + job.getId() + "|" + job.getClassName() + "] is started.");
			
			
			
			
			job = scheduleService.completeJob(job);
		} else {
			logger.warn("Job has not been configured for " + className + " or the job is inactive/still in-processing.");
		}
	}
}
