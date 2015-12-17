package club.magicfun.aquila.job;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import club.magicfun.aquila.util.HtmlUtility;

public class TestWebDriver2 {

	public static void main(String[] args) {
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
		
		WebDriver webDriver = new ChromeDriver();
        
        webDriver.get("http://s.taobao.com/search?q=水果刀&sort=default&style=list");
        
		List<WebElement> prodItemDivs = webDriver.findElements(By.xpath("//*[@class='list']/div/div[contains(concat(' ', normalize-space(@class), ' '), ' item ')]"));
		
		int itemRank = 0; 
		
		for (WebElement prodItemDiv : prodItemDivs) {
			
			System.out.println("webElement " + prodItemDiv.toString());
			
			itemRank++;
			
			WebElement itemTitleLink = prodItemDiv.findElement(By.xpath("div[@class='col col-2']/p/a"));
			
			String itemProductId = itemTitleLink.getAttribute("data-nid");
			
			String itemProductName = HtmlUtility.removeHtmlTags(itemTitleLink.getText());
			
			String itemShopName = HtmlUtility.removeHtmlTags(prodItemDiv.findElement(By.xpath("div[@class='col col-2']/div/div[@class='shop']")).getText());
			
			String itemPrice = prodItemDiv.findElement(By.xpath("div[@class='col col-3']/div/span/strong")).getText();
			
			System.out.println("Item Rank: " + itemRank);
			System.out.println("item product id: " + itemProductId);
			System.out.println("item product name: " + itemProductName);
			System.out.println("item product price: " + itemPrice);
			System.out.println("shop name: " + itemShopName);
			System.out.println("------------------------");
		}
        
        webDriver.close();  
        webDriver.quit();
	}

}
