package club.magicfun.aquila.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import club.magicfun.aquila.service.AuthorityService;

@Controller
public class DemoController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	MessageSource messageSource;
	
	@RequestMapping(value = "demo", method = RequestMethod.GET)
	public String indexHandler(Model model) {
		
		
		
		return "demo/index";
	}

	@RequestMapping(value = "demo", method = RequestMethod.POST)
	public String indexSubmitHandler(Model model) {

		return "demo/index";
	}
}
