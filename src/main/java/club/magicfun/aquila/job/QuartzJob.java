package club.magicfun.aquila.job;

import java.util.List;
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
	
	@Scheduled(cron = "0/15 * * * * ? ")
    public void run(){
        System.out.println("Quartz执行的任务调度");
        
        WebDriver webDriver = new ChromeDriver();
        
        webDriver.get("https://item.taobao.com/item.htm?spm=a1z10.1-c.w10312587-8883412262.6.4dUlMo&id=41671173269"); 
        
        //webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        
        System.out.println("title: " + webDriver.getTitle());
  
        List<WebElement> colorCategoryList = webDriver.findElements(By.xpath("//*[@data-property='颜色分类']/li/a"));
        
        for (WebElement colorCategory : colorCategoryList) {
        	
        	// simulate choosing a color
        	colorCategory.click();
        	
        	WebElement selectedColorLi = webDriver.findElement(By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' tb-selected ')]"));
        	System.out.println("selectedLi: " + selectedColorLi.findElements(By.cssSelector("a > span")).get(0).getAttribute("innerHTML"));
        	
        	System.out.println("price: " + webDriver.findElement(By.id("J_PromoPriceNum")).getText());
        	System.out.println("stock: " + webDriver.findElement(By.id("J_SpanStock")).getText());
        	
        }
        
        webDriver.close();  
        webDriver.quit();
    }

}
