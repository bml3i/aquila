package club.magicfun.aquila.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import club.magicfun.aquila.model.Group;
import club.magicfun.aquila.service.AuthorityService;

@Component
public class GroupFormBeanValidator implements Validator {

	@Autowired
	private AuthorityService authorityService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(GroupFormBean.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		// 1. Preparations
		GroupFormBean groupFormBean = (GroupFormBean) target;
		
		// 2. Bean validations
		ValidationUtils.rejectIfEmpty(errors, "name", "GroupFormBean.name.NotEmpty");
		ValidationUtils.rejectIfEmpty(errors, "description", "GroupFormBean.description.NotEmpty");
		
		if(groupFormBean.getRoleIds() == null || groupFormBean.getRoleIds().size() == 0) {
			errors.rejectValue("roleIds", "GroupFormBean.roleIds.IsRequired");
		}
		
		// 3. Business validations
		Group groupFoundByName = authorityService.findGroupByName(groupFormBean.getName());

		if (groupFoundByName != null && !groupFoundByName.getId().equals(groupFormBean.getId())) {
			errors.rejectValue("name", "GroupFormBean.name.AlreadyExist");
		}
	}

}
