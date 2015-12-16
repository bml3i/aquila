package club.magicfun.aquila.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.repository.JobRepository;

@Component
public class RankSearchJob {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(RankSearchJob.class);
	
	@Autowired
	private JobRepository jobRepository;
	
	public RankSearchJob() {
		super();
	}

	@Scheduled(cron = "0/15 * * * * ? ")
    public void run(){
		
		String className = this.getClass().getName();
		Job currentJob = jobRepository.findByClassName(className);
		
		// determine if to run this job
		if (currentJob != null && currentJob.getActiveFlag()) {
			System.out.println("job: " + currentJob.getId() + " - " + currentJob.getClassName());
			
			
		} else {
			logger.error("Job has not been configured or job is inactive.");
		}
	}
}
