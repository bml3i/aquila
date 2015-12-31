package club.magicfun.aquila.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverUtility {

	public static void hideElement(WebDriver driver, String xpath) {

		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility='hidden'", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
