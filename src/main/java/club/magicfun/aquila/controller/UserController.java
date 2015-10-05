package club.magicfun.aquila.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import club.magicfun.aquila.common.GlobalManager;
import club.magicfun.aquila.common.ResponseResult;
import club.magicfun.aquila.common.ValidationResult;
import club.magicfun.aquila.form.UserFormBean;
import club.magicfun.aquila.form.UserFormBeanValidator;
import club.magicfun.aquila.model.Department;
import club.magicfun.aquila.model.Group;
import club.magicfun.aquila.model.User;
import club.magicfun.aquila.service.AuthorityService;

@Controller
public class UserController extends BaseController {

	@Autowired
	MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserFormBeanValidator userFormBeanValidator;
	
	
	@InitBinder("userFormBean")
	protected void initUserFormBeanBinder(WebDataBinder binder) {
		binder.setValidator(userFormBeanValidator);
	}
	
	@ModelAttribute("groups")
	public List<Group> getAllGroups() {
		return authorityService.findAllGroups();
	}
	
	@ModelAttribute("departments")
	public List<Department> getAllDepartments() {
		return authorityService.findAllDepartments();
	}

	@RequestMapping("users")
	public String usersIndexHandler() {

		logger.debug("UserController.usersIndexHandler is invoked.");

		return "users/index";
	}

	@RequestMapping("ajax/users/show_users")
	public String showUsersAjaxHandler(Model model, @RequestParam("pageNumber") Integer pageNumber) {

		logger.debug("UserController.showUsersAjaxHandler is invoked.");

		Page<User> userPage = authorityService.findPageableUsers(pageNumber - 1);
		List<User> users = userPage.getContent();

		model.addAttribute("users", users);
		// add pagination attributes
		model.addAttribute("showRecordsJSFunc", "showUsers");
		model.addAllAttributes(GlobalManager.getGlobalPageableMap(userPage));

		return "ajax/users/show_users";
	}

	@RequestMapping(value = "users/create", method = RequestMethod.GET)
	public String addUserGetHandler(Model model) {

		logger.debug("UserController.addUserGetHandler is invoked.");

		model.addAttribute("userFormBean", new UserFormBean());

		return "users/add_user";
	}

	@RequestMapping(value = "user/edit/{editId}", method = RequestMethod.GET)
	public String editUserGetHandler(Model model, @PathVariable Integer editId) {

		logger.debug("UserController.editUserGetHandler is invoked.");

		User editUser = authorityService.findUserById(editId);

		UserFormBean userFormBean = new UserFormBean();
		userFormBean.setId(editUser.getId());
		userFormBean.setUserId(editUser.getUserId());
		userFormBean.setName(editUser.getName());
		userFormBean.setPassword(editUser.getPassword());
		userFormBean.setEmail(editUser.getEmail());
		userFormBean.setGroupId(editUser.getGroup().getId());
		
		if (editUser.getDepartment() != null) {
			userFormBean.setDepartmentId(editUser.getDepartment().getId());
		}

		model.addAttribute("userFormBean", userFormBean);

		return "users/edit_user";
	}

	@RequestMapping(value = "users/edit_my_account", method = RequestMethod.GET)
	public String editMyAccountGetHandler(Model model, Principal principal) {

		logger.debug("UserController.editMyAccountGetHandler is invoked.");

		User editUser = authorityService.findUserByUserId(principal.getName());
		Group editUserGroup = editUser.getGroup();
		Department editDepartment = editUser.getDepartment();

		UserFormBean userFormBean = new UserFormBean();
		userFormBean.setId(editUser.getId());
		userFormBean.setUserId(editUser.getUserId());
		userFormBean.setName(editUser.getName());
		userFormBean.setPassword(editUser.getPassword());
		userFormBean.setEmail(editUser.getEmail());
		userFormBean.setGroupId(editUser.getGroup().getId());
		
		if (editUser.getDepartment() != null) {
			userFormBean.setDepartmentId(editUser.getDepartment().getId());
		}

		model.addAttribute("userFormBean", userFormBean);
		model.addAttribute("editUserGroup", editUserGroup);
		model.addAttribute("editDepartment", editDepartment);

		return "users/edit_my_account";
	}

	@RequestMapping(value = "users/create", method = RequestMethod.POST)
	public String addUserSubmitHandler(Model model, Locale locale,
			@Valid @ModelAttribute("userFormBean") UserFormBean userFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("UserController.addUserSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "users/add_user";
		} else {
			User user = new User();
			user.setUserId(userFormBean.getUserId());
			user.setName(userFormBean.getName());
			user.setPassword(userFormBean.getPassword());
			user.setEmail(userFormBean.getEmail());
			user.setGroup(authorityService.findGroupById(userFormBean.getGroupId()));
			
			if (userFormBean.getDepartmentId() != null && userFormBean.getDepartmentId() > 0) {
				user.setDepartment(authorityService.findDepartmentById(userFormBean.getDepartmentId()));
			} else {
				user.setDepartment(null);
			}
			
			user.setActiveFlag(true);
			user.setCreateDate(new Date());

			authorityService.persist(user);

			redirectAttributes.addFlashAttribute("info",
					messageSource.getMessage("users.info.add_user_success", new String[] { user.getUserId() }, locale));
			return "redirect:/users";
		}
	}

	@RequestMapping(value = "user/edit/{editId}", method = RequestMethod.POST)
	public String editUserSubmitHandler(Model model, Locale locale, @PathVariable Integer editId,
			@Valid @ModelAttribute("userFormBean") UserFormBean userFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("UserController.editUserSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "users/edit_user";
		} else {
			User user = authorityService.findUserById(editId);
			user.setUserId(userFormBean.getUserId());
			user.setName(userFormBean.getName());
			user.setPassword(userFormBean.getPassword());
			user.setEmail(userFormBean.getEmail());
			user.setGroup(authorityService.findGroupById(userFormBean.getGroupId()));

			if (userFormBean.getDepartmentId() != null && userFormBean.getDepartmentId() > 0) {
				user.setDepartment(authorityService.findDepartmentById(userFormBean.getDepartmentId()));
			} else {
				user.setDepartment(null);
			}
			
			authorityService.persist(user);

			redirectAttributes
					.addFlashAttribute("info", messageSource.getMessage("users.info.edit_user_success",
							new String[] { user.getUserId() }, locale));
			return "redirect:/users";
		}
	}

	@RequestMapping(value = "users/edit_my_account", method = RequestMethod.POST)
	public String editMyAccountSubmitHandler(Model model, Locale locale,
			@Valid @ModelAttribute("userFormBean") UserFormBean userFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes, Principal principal) {

		logger.debug("UserController.editMyAccountSubmitHandler is invoked.");

		User editUser = authorityService.findUserByUserId(principal.getName());

		if (result.hasErrors()) {
			Group editUserGroup = editUser.getGroup();
			model.addAttribute("editUserGroup", editUserGroup);

			return "users/edit_my_account";
		} else {
			editUser.setName(userFormBean.getName());
			editUser.setPassword(userFormBean.getPassword());
			editUser.setEmail(userFormBean.getEmail());

			authorityService.persist(editUser);

			redirectAttributes.addFlashAttribute("info", messageSource.getMessage("users.info.edit_user_success",
					new String[] { editUser.getUserId() }, locale));
			return "redirect:/";
		}
	}

	@RequestMapping("/rest/users/update_active_flag")
	public @ResponseBody
	ResponseResult updateActiveFlagRestHandler(Locale locale, @RequestParam("updateId") Integer updateId,
			@RequestParam("activeFlag") boolean activeFlag) {

		logger.debug("UserController.updateActiveFlagRestHandler is invoked.");

		ValidationResult validationResult = authorityService.updateUserActiveFlag(updateId, activeFlag, locale);

		ResponseResult responseResult = new ResponseResult(validationResult);

		return responseResult;
	}
	
}
