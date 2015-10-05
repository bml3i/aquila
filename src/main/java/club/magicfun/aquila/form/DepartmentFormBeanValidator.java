package club.magicfun.aquila.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import club.magicfun.aquila.model.Department;
import club.magicfun.aquila.service.AuthorityService;

@Component
public class DepartmentFormBeanValidator implements Validator {

	@Autowired
	private AuthorityService authorityService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(DepartmentFormBean.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		// 1. Preparations
		DepartmentFormBean departmentFormBean = (DepartmentFormBean) target;

		// 2. Bean validations
		ValidationUtils.rejectIfEmpty(errors, "name", "DepartmentFormBean.name.NotEmpty");

		// 3. Business validations
		Department departmentFoundByName = authorityService.findDepartmentByName(departmentFormBean.getName());

		if (departmentFoundByName != null && !departmentFoundByName.getId().equals(departmentFormBean.getId())) {
			errors.rejectValue("name", "DepartmentFormBean.name.AlreadyExist");
		}

	}
}
