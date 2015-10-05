package club.magicfun.aquila.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import club.magicfun.aquila.common.ResultType;
import club.magicfun.aquila.common.ValidationResult;
import club.magicfun.aquila.form.GroupFormBean;
import club.magicfun.aquila.form.GroupFormBeanValidator;
import club.magicfun.aquila.model.Group;
import club.magicfun.aquila.model.Role;
import club.magicfun.aquila.service.AuthorityService;

@Controller
public class GroupController extends BaseController {

	@Autowired
	MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private GroupFormBeanValidator groupFormBeanValidator;
	
	@InitBinder("groupFormBean")
	protected void initGroupFormBeanBinder(WebDataBinder binder) {
		binder.setValidator(groupFormBeanValidator);
	}
	
	@ModelAttribute("roles")
	public List<Role> getAllRoles() {
		return authorityService.findAllRoles();
	}

	@RequestMapping("groups")
	public String groupsIndexHandler() {

		logger.debug("GroupController.groupsIndexHandler is invoked.");

		return "groups/index";
	}

	@RequestMapping("ajax/groups/show_groups")
	public String showGroupsAjaxHandler(Model model, @RequestParam("pageNumber") Integer pageNumber) {

		logger.debug("GroupController.showGroupsAjaxHandler is invoked.");

		Page<Group> groupPage = authorityService.findPageableGroups(pageNumber - 1);
		List<Group> groups = groupPage.getContent();

		model.addAttribute("groups", groups);
		// add pagination attributes
		model.addAttribute("showRecordsJSFunc", "showGroups");
		model.addAllAttributes(GlobalManager.getGlobalPageableMap(groupPage));

		return "ajax/groups/show_groups";
	}

	@RequestMapping("group/{groupId}")
	public String selectGroupHandler(Model model, @PathVariable Integer groupId) {

		logger.debug("GroupController.selectGroupHandler is invoked.");

		model.addAttribute("groupId", groupId);

		return "groups/select_group";
	}

	@RequestMapping("ajax/groups/show_group_roles")
	public String showGroupRolesAjaxHandler(Model model, @RequestParam(required = false) Integer groupId) {

		logger.debug("GroupController.showGroupRolesAjaxHandler is invoked.");

		Set<Role> roles = authorityService.findRolesByGroupId(groupId);

		model.addAttribute("roles", roles);

		return "ajax/groups/show_group_roles";
	}

	@RequestMapping(value = "groups/create", method = RequestMethod.GET)
	public String addGroupGetHandler(Model model) {

		logger.debug("GroupController.addGroupGetHandler is invoked.");

		model.addAttribute("groupFormBean", new GroupFormBean());

		return "groups/add_group";
	}

	@RequestMapping(value = "groups/create", method = RequestMethod.POST)
	public String addGroupSubmitHandler(Model model, Locale locale,
			@Valid @ModelAttribute("groupFormBean") GroupFormBean groupFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("GroupController.addGroupSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "groups/add_group";
		} else {
			Group savedGroup = authorityService.saveNewGroup(groupFormBean);

			redirectAttributes.addFlashAttribute("info", messageSource.getMessage("groups.info.add_group_success",
					new String[] { savedGroup.getName() }, locale));
			return "redirect:/groups";
		}
	}
	
	@RequestMapping(value = "group/edit/{editId}", method = RequestMethod.GET)
	public String editGroupGetHandler(Model model, @PathVariable Integer editId) {

		logger.debug("GroupController.editGroupGetHandler is invoked.");

		Group editGroup = authorityService.findGroupById(editId);

		GroupFormBean groupFormBean = new GroupFormBean();
		groupFormBean.setId(editGroup.getId());
		groupFormBean.setName(editGroup.getName());
		groupFormBean.setDescription(editGroup.getDescription());
		groupFormBean.setRoleIds(editGroup.getRoleIds());

		model.addAttribute("groupFormBean", groupFormBean);

		return "groups/edit_group";
	}
	
	@RequestMapping(value = "group/edit/{editId}", method = RequestMethod.POST)
	public String editGroupSubmitHandler(Model model, Locale locale, @PathVariable Integer editId,
			@Valid @ModelAttribute("groupFormBean") GroupFormBean groupFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("GroupController.editGroupSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "groups/edit_group";
		} else {
			Set<Role> selectedRoles = new HashSet<Role>();

			for (Integer roleId : groupFormBean.getRoleIds()) {
				Role role = authorityService.findRoleById(roleId);
				selectedRoles.add(role);
			}

			Group group = authorityService.findGroupById(editId);
			group.setName(groupFormBean.getName());
			group.setDescription(groupFormBean.getDescription());
			group.setRoles(selectedRoles);

			authorityService.persist(group);

			redirectAttributes.addFlashAttribute("info", messageSource.getMessage("groups.info.edit_group_success",
					new String[] { group.getName() }, locale));
			return "redirect:/groups";
		}
	}

	@RequestMapping(value = "/rest/groups/delete_group", method = RequestMethod.POST)
	public @ResponseBody
	ResponseResult deleteGroupRestHandler(Locale locale, @RequestParam("deleteId") Integer deleteId) {

		logger.debug("GroupController.deleteGroupRestHandler is invoked.");

		ValidationResult validationResult = authorityService.validateBeforeDeleteGroup(deleteId, locale);

		if (ResultType.SUCCESS == validationResult.getResultType()) {
			authorityService.deleteGroup(deleteId);
		}

		ResponseResult responseResult = new ResponseResult(validationResult);
		return responseResult;
	}

}
