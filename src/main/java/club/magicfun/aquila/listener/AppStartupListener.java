package club.magicfun.aquila.listener;

import java.io.File;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class AppStartupListener implements ApplicationListener<ContextRefreshedEvent> {

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
			
		}
	}

}
