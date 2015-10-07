package club.magicfun.aquila.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuartzJob {

	public QuartzJob() {
		System.out.println("Quartzjob创建成功");
	}
	
	@Scheduled(cron = "0/10 * *  * * ? ")
    public void run(){
        System.out.println("Quartz执行的任务调度");
    }

}
