package club.magicfun.aquila.listener;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.service.ScheduleService;

@Service
public class AppStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ScheduleService scheduleService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			
			String dir = System.getProperty("user.dir");
			String osName = System.getProperty("os.name");
			
			// load the appropriate chrome drivers according to the OS type
			if (osName.toLowerCase().indexOf("windows") > -1) {
				System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "win"
						+ File.separator + "chromedriver.exe");
			} else if (osName.toLowerCase().indexOf("mac") > -1) {
				System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "mac"
						+ File.separator + "chromedriver");
			}
			
			// update job run_status to "C" when application starts
			List<Job> jobs = scheduleService.findAllJobs();
			
			if (jobs != null) {
				for (Job job : jobs) {
					job.setRunStatus("C");
					scheduleService.persist(job);
				}
			}
			
		}
	}

}
