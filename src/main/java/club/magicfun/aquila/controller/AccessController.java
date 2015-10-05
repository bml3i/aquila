package club.magicfun.aquila.controller;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccessController extends BaseController {

	@Autowired
	MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(AccessController.class);

	@RequestMapping("/login")
	public String loginHandler() throws UnsupportedEncodingException {

		logger.debug("AccessController.loginHandler is invoked.");

		return "access/login";
	}

	@RequestMapping(value = "/login/{status}")
	public String loginStatusHandler(Locale locale, @PathVariable String status,
			final RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

		logger.debug("AccessController.loginStatusHandler is invoked.");

		if ("success".equals(status)) {
			redirectAttributes.addFlashAttribute("info",
					messageSource.getMessage("login.info.login_success", null, locale));
			return "redirect:/";
		} else {
			redirectAttributes.addFlashAttribute("error",
					messageSource.getMessage("login.error.login_failure", null, locale));
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/denied")
	public String deniedHandler(Locale locale, final RedirectAttributes redirectAttributes)
			throws UnsupportedEncodingException {

		logger.debug("AccessController.deniedHandler is invoked.");

		redirectAttributes.addFlashAttribute("error",
				messageSource.getMessage("global.error.permission_denied", null, locale));

		return "redirect:/";
	}

}
