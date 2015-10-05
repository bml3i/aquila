package club.magicfun.aquila.controller;

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
import club.magicfun.aquila.common.ResultType;
import club.magicfun.aquila.common.ValidationResult;
import club.magicfun.aquila.form.DepartmentFormBean;
import club.magicfun.aquila.form.DepartmentFormBeanValidator;
import club.magicfun.aquila.model.Department;
import club.magicfun.aquila.service.AuthorityService;

@Controller
public class DepartmentController extends BaseController {

	@Autowired
	MessageSource messageSource;

	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private DepartmentFormBeanValidator departmentFormBeanValidator;
	
	@InitBinder("departmentFormBean")
	protected void initDepartmentFormBeanBinder(WebDataBinder binder) {
		binder.setValidator(departmentFormBeanValidator);
	}
	
	@RequestMapping("departments")
	public String departmentsIndexHandler() {

		logger.debug("DepartmentController.departmentsIndexHandler is invoked.");

		return "departments/index";
	}

	@RequestMapping("ajax/departments/show_departments")
	public String showDepartmentsAjaxHandler(Model model, @RequestParam("pageNumber") Integer pageNumber) {

		logger.debug("DepartmentController.showDepartmentsAjaxHandler is invoked.");

		Page<Department> departmentPage = authorityService.findPageableDepartments(pageNumber - 1);
		List<Department> departments = departmentPage.getContent();

		model.addAttribute("departments", departments);
		// add pagination attributes
		model.addAttribute("showRecordsJSFunc", "showDepartments");
		model.addAllAttributes(GlobalManager.getGlobalPageableMap(departmentPage));

		return "ajax/departments/show_departments";
	}

	@RequestMapping(value = "departments/create", method = RequestMethod.GET)
	public String addDepartmentGetHandler(Model model) {

		logger.debug("DepartmentController.addDepartmentGetHandler is invoked.");

		model.addAttribute("departmentFormBean", new DepartmentFormBean());

		return "departments/add_department";
	}

	@RequestMapping(value = "departments/create", method = RequestMethod.POST)
	public String addDepartmentSubmitHandler(Model model, Locale locale,
			@Valid @ModelAttribute("departmentFormBean") DepartmentFormBean departmentFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("DepartmentController.addDepartmentSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "departments/add_department";
		} else {
			Department savedDepartment = new Department();
			savedDepartment.setName(departmentFormBean.getName());

			savedDepartment = authorityService.persist(savedDepartment);

			redirectAttributes.addFlashAttribute(
					"info",
					messageSource.getMessage("departments.info.add_department_success",
							new String[] { savedDepartment.getName() }, locale));
			return "redirect:/departments";
		}
	}

	@RequestMapping(value = "department/edit/{editId}", method = RequestMethod.GET)
	public String editDepartmentGetHandler(Model model, @PathVariable Integer editId) {

		logger.debug("DepartmentController.editDepartmentGetHandler is invoked.");

		Department editDepartment = authorityService.findDepartmentById(editId);

		DepartmentFormBean departmentFormBean = new DepartmentFormBean();
		departmentFormBean.setId(editDepartment.getId());
		departmentFormBean.setName(editDepartment.getName());

		model.addAttribute("departmentFormBean", departmentFormBean);

		return "departments/edit_department";
	}

	@RequestMapping(value = "department/edit/{editId}", method = RequestMethod.POST)
	public String editDepartmentSubmitHandler(Model model, Locale locale, @PathVariable Integer editId,
			@Valid @ModelAttribute("departmentFormBean") DepartmentFormBean departmentFormBean, BindingResult result,
			final RedirectAttributes redirectAttributes) {

		logger.debug("DepartmentController.editDepartmentSubmitHandler is invoked.");

		if (result.hasErrors()) {
			return "departments/edit_department";
		} else {
			Department department = authorityService.findDepartmentById(editId);
			department.setName(departmentFormBean.getName());

			authorityService.persist(department);

			redirectAttributes.addFlashAttribute(
					"info",
					messageSource.getMessage("departments.info.edit_department_success",
							new String[] { department.getName() }, locale));
			return "redirect:/departments";
		}
	}
	
	@RequestMapping(value = "/rest/departments/delete_department", method = RequestMethod.POST)
	public @ResponseBody
	ResponseResult deleteDepartmentRestHandler(Locale locale, @RequestParam("deleteId") Integer deleteId) {

		logger.debug("DepartmentController.deleteDepartmentRestHandler is invoked.");

		ValidationResult validationResult = authorityService.validateBeforeDeleteDepartment(deleteId, locale);

		if (ResultType.SUCCESS == validationResult.getResultType()) {
			authorityService.deleteDepartment(deleteId);
		}

		ResponseResult responseResult = new ResponseResult(validationResult);
		return responseResult;
	}

}
