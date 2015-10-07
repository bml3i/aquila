package club.magicfun.aquila.util;

import cn.edu.hfut.dmic.webcollector.model.Page;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class PageUtils {

    public static HtmlUnitDriver getDriver(Page page) {
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);
        driver.get(page.getUrl());
        return driver;
    }

    public static HtmlUnitDriver getDriver(Page page, BrowserVersion browserVersion) {
        HtmlUnitDriver driver = new HtmlUnitDriver(browserVersion);
        driver.setJavascriptEnabled(true);
        driver.get(page.getUrl());
        return driver;
    }

}
