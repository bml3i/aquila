package club.magicfun.aquila.crawler;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import club.magicfun.aquila.util.PageUtils;
import cn.edu.hfut.dmic.webcollector.crawler.DeepCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequesterImpl;

public class DemoJSCrawler extends DeepCrawler {

	public DemoJSCrawler(String crawlPath) {
		super(crawlPath);
		
		//HttpRequesterImpl requester = (HttpRequesterImpl) this.getHttpRequester();
	}

	@Override
	public Links visitAndGetNextLinks(Page page) {
		 /*HtmlUnitDriver可以抽取JS生成的数据*/
        //HtmlUnitDriver driver=PageUtils.getDriver(page,BrowserVersion.CHROME);
        HtmlUnitDriver driver=PageUtils.getDriver(page, BrowserVersion.CHROME);
        
        System.out.println("Title:" + driver.getTitle());
        
        /*HtmlUnitDriver也可以像Jsoup一样用CSS SELECTOR抽取数据
          关于HtmlUnitDriver的文档请查阅selenium相关文档*/
        //List<WebElement> divInfos=driver.findElementsByCssSelector("span.title");
        List<WebElement> divInfos=driver.findElementsByCssSelector("#J_ShopAsynSearchURL");
        
        System.out.println("divInfos.size: " + divInfos.size());
        
        for(WebElement divInfo:divInfos){
            System.out.println(divInfo.getAttribute("value"));
        }
        return null;
	}

	public static void main(String[] args) throws Exception {
		DemoJSCrawler crawler = new DemoJSCrawler("~/temp");
		
		for (int page = 1; page <= 1; page++) {
			crawler.addSeed("https://magicfun.taobao.com/search.htm?search=y&orderType=hotsell_desc&pageNo=" + page);
		}
			
		crawler.start(1);
	}

}
