package club.magicfun.aquila.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import club.magicfun.aquila.repository.JobRepository;

@Component
public class RankSearchJob extends AbstractJob {
	
	@Autowired
	private JobRepository jobRepository;
	
	public RankSearchJob() {
		super();
	}

	@Scheduled(cron = "0/15 * * * * ? ")
    public void run(){
		
		if(job != null) {
			System.out.println("***** SUCCESS *****");
		}
	}

	@Override
	protected void lookupJob() {
		String className = this.getClass().getName();
		
		System.out.println("lookupJob() --> className: " + className);
		
		job = jobRepository.findByClassName(className);
	}
	
}
