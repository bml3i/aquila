package club.magicfun.aquila.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import club.magicfun.aquila.model.Category;
import club.magicfun.aquila.model.Job;
import club.magicfun.aquila.model.Product;
import club.magicfun.aquila.model.RankSearchKeyword;
import club.magicfun.aquila.model.RankSearchType;
import club.magicfun.aquila.repository.JobRepository;
import club.magicfun.aquila.repository.ProductRepository;
import club.magicfun.aquila.repository.RankSearchKeywordRepository;
import club.magicfun.aquila.service.ProductService;

@Controller
public class DemoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RankSearchKeywordRepository rankSearchKeywordRepository;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private ProductService productService;
	
	
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public String indexHandler(Model model) {
		
		System.out.println("Class -  Name: " + this.getClass().getName());
    	System.out.println("Class - Simple Name: " + this.getClass().getSimpleName());
		
		List<Product> products = productRepository.findAll();
		
		if (products != null && products.size() > 0) {
			for(Product product : products) {
				logger.info(product.getId() + " - " + product.getProductName());
				
				Set<Category> categories = product.getCategories();
				
				for (Category category : categories) {
					logger.info(category.getCategoryName() + " - " + category.getCategoryPrice() + " - " + category.getCategoryStockNumber());
				}
			}
		}
		
		List<RankSearchKeyword> rankSearchKeywords = rankSearchKeywordRepository.findAll();
		
		if (rankSearchKeywords != null && rankSearchKeywords.size() > 0) {
			for(RankSearchKeyword rankSearchKeyword : rankSearchKeywords) {
				logger.info(rankSearchKeyword.getId() + " - " + rankSearchKeyword.getKeyword());
				
				Set<RankSearchType> rankSearchTypes = rankSearchKeyword.getRankSearchTypes();
				
				for (RankSearchType rankSearchType : rankSearchTypes) {
					logger.info(rankSearchType.getId() + " - " + rankSearchType.getName() + " - " + rankSearchType.getDescription());
				}
			}
		}
		
		String testJobClassName = "club.magicfun.aquila.job.QuartzJob";
		
		Job job = jobRepository.findByClassName(testJobClassName);
		
		if (job != null) {
			logger.info(job.getId() + " - " + job.getClassName() + " - " + job.getDescription());
		}
		
		
		Product product = productService.findProductById(123456l);
		
		if (product != null) {
			logger.info("product 123456 exists, will delete it soon. ");
			
			productService.deleteProduct(product);
			logger.info("product 123456 had been deleted. ");
		}
		
		
		
		
		
		return "demo/index";
	}

	@RequestMapping(value = "demo", method = RequestMethod.POST)
	public String indexSubmitHandler(Model model) {

		return "demo/index";
	}
}
