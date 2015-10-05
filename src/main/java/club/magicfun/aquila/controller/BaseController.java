package club.magicfun.aquila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {

	@Autowired
	SessionRegistry sessionRegistry;

	@ModelAttribute("onlineUserNum")
	public int getOnlineUserNumber() {
		return sessionRegistry.getAllPrincipals().size();
	}
}
