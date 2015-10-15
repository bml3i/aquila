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
		System.setProperty("webdriver.chrome.driver", dir + File.separator + "drivers" + File.separator + "chromedriver");
        
        WebDriver webDriver = new ChromeDriver();
        
        //webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        webDriver.get("http://list.jd.com/9987-653-655-0-0-0-0-0-0-0-1-1-1-1-1-72-4137-33.html");  
  
        WebElement webElement = webDriver.findElement(By.xpath("//div[@id='plist']"));  
  
        System.out.println(webElement.getAttribute("outerHTML"));  
  
        WebElement li=webElement.findElement(By.xpath("//li[@index='1']"));  
  
        String name=li.findElement(By.xpath("//li[@index='1']//div[@class='p-name']/a")).getText();  
        System.out.println("商品名:"+name);  
  
        String price=li.findElement(By.xpath("//li[@index='1']//div[@class='p-price']/strong")).getText();  
        System.out.println("价格:"+price);  
  
        String eva=li.findElement(By.xpath("//li[@index='1']//span[@class='evaluate']/a[@target='_blank']")).getText();  
        System.out.println("评价:"+eva);  
        
        webDriver.close();  
        webDriver.quit();
    }

}
