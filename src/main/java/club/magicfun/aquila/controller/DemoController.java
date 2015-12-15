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
import club.magicfun.aquila.repository.CategoryRepository;
import club.magicfun.aquila.repository.ProductRepository;

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
		
		return "demo/index";
	}

	@RequestMapping(value = "demo", method = RequestMethod.POST)
	public String indexSubmitHandler(Model model) {

		return "demo/index";
	}
}
