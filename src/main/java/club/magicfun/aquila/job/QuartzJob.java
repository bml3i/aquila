package club.magicfun.aquila.job;

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
	
	@Scheduled(cron = "0/30 * *  * * ? ")
    public void run(){
        System.out.println("Quartz执行的任务调度");
        
        WebDriver webDriver = new ChromeDriver();
        
        //webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        webDriver.get("https://item.taobao.com/item.htm?id=43462858898"); 
  
        System.out.println("title: " + webDriver.getTitle());
        
        webDriver.close();  
        webDriver.quit();
    }

}
