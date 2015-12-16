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
import club.magicfun.aquila.model.Product;
import club.magicfun.aquila.model.RankSearchKeyword;
import club.magicfun.aquila.model.RankSearchType;
import club.magicfun.aquila.repository.CategoryRepository;
import club.magicfun.aquila.repository.ProductRepository;
import club.magicfun.aquila.repository.RankSearchKeywordRepository;

@Controller
public class DemoController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private RankSearchKeywordRepository rankSearchKeywordRepository;
	
	
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public String indexHandler(Model model) {
		
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
		
		return "demo/index";
	}

	@RequestMapping(value = "demo", method = RequestMethod.POST)
	public String indexSubmitHandler(Model model) {

		return "demo/index";
	}
}
