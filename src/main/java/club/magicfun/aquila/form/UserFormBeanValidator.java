package club.magicfun.aquila.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import club.magicfun.aquila.model.User;
import club.magicfun.aquila.service.AuthorityService;

@Component
public class UserFormBeanValidator implements Validator {

	@Autowired
	private AuthorityService authorityService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(UserFormBean.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		// 1. Preparations
		UserFormBean userFormBean = (UserFormBean) target;

		// 2. Bean validations
		ValidationUtils.rejectIfEmpty(errors, "userId", "UserFormBean.userId.NotEmpty");
		ValidationUtils.rejectIfEmpty(errors, "name", "UserFormBean.name.NotEmpty");
		ValidationUtils.rejectIfEmpty(errors, "password", "UserFormBean.password.NotEmpty");
		ValidationUtils.rejectIfEmpty(errors, "email", "UserFormBean.email.NotEmpty");

		if (userFormBean.getGroupId() == null || userFormBean.getGroupId() == 0) {
			errors.rejectValue("groupId", "UserFormBean.groupId.IsRequired");
		}

		// 3. Business validations
		User userFoundByUserId = authorityService.findUserByUserId(userFormBean.getUserId());

		if (userFoundByUserId != null && !userFoundByUserId.getId().equals(userFormBean.getId())) {
			errors.rejectValue("userId", "UserFormBean.userId.AlreadyExist");
		}
	}

}
