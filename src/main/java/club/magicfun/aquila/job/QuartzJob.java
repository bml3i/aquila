package club.magicfun.aquila.job;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuartzJob  {

	public QuartzJob() {
		System.out.println("Quartzjob创建成功");
	}
	
	@Scheduled(cron = "0/10 * *  * * ? ")
    public void run(){
        System.out.println("Quartz执行的任务调度");
        
		String dir = System.getProperty("user.dir");
		String osName = System.getProperty("os.name");
		
		// windows
		if (osName.toLowerCase().indexOf("windows") > -1) {
			System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "win"
					+ File.separator + "chromedriver.exe");
		} else if (osName.toLowerCase().indexOf("mac") > -1) {
			System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "mac"
					+ File.separator + "chromedriver");
		}
        
        WebDriver webDriver = new ChromeDriver();
        
        //webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        webDriver.get("https://item.taobao.com/item.htm?id=39647261724");  
  
        System.out.println("title: " + webDriver.getTitle());
        
        webDriver.close();  
        webDriver.quit();
    }

}
