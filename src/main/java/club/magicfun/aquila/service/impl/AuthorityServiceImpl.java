package club.magicfun.aquila.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.magicfun.aquila.common.GlobalManager;
import club.magicfun.aquila.common.ResultType;
import club.magicfun.aquila.common.ValidationResult;
import club.magicfun.aquila.form.GroupFormBean;
import club.magicfun.aquila.model.Group;
import club.magicfun.aquila.model.Role;
import club.magicfun.aquila.model.User;
import club.magicfun.aquila.repository.GroupRepository;
import club.magicfun.aquila.repository.RoleRepository;
import club.magicfun.aquila.repository.UserRepository;
import club.magicfun.aquila.service.AuthorityService;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private GroupRepository groupRepository;
	
	@Override
	public User findUserById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public Role findRoleById(Integer id) {
		return roleRepository.findOne(id);
	}

	@Override
	public Group findGroupById(Integer id) {
		return groupRepository.findOne(id);
	}

	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public User persist(User user) {
		if (user.getId() != null) {
			user.setUpdateDateTime(new Date());
		}
		return userRepository.save(user);
	}

	@Override
	public Page<User> findPageableUsers(Integer pageNumber) {
		PageRequest pageRequest = new PageRequest(pageNumber, GlobalManager.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id");
		return userRepository.findAll(pageRequest);
	}
	
	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public ValidationResult updateUserActiveFlag(Integer updateId, boolean activeFlag, Locale locale) {

		ValidationResult result = new ValidationResult(ResultType.SUCCESS);

		User targetUser = userRepository.findOne(updateId);

		if (targetUser == null || targetUser.getActiveFlag().booleanValue() == activeFlag) {
			result.setResultType(ResultType.ERROR);
			result.addErrorMessage(messageSource.getMessage("global.error.unknown_error", null, locale));
			return result;
		} else {
			targetUser.setActiveFlag(activeFlag);
			this.persist(targetUser);
			return result;
		}
	}

	@Override
	public User findUserByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}

	@Override
	public Set<Role> findRolesByGroupId(Integer groupId) {
		return groupRepository.findOne(groupId).getRoles();
	}

	@Override
	public List<Group> findAllGroups() {
		return groupRepository.findAll();
	}

	@Override
	public Group persist(Group group) {
		return groupRepository.save(group);
	}

	@Override
	public void deleteGroup(Integer groupId) {
		groupRepository.delete(groupId);
	}

	@Override
	public Group saveNewGroup(GroupFormBean groupFormBean) {

		Group group = new Group();

		group.setName(groupFormBean.getName());
		group.setDescription(groupFormBean.getDescription());
		group.setRoles(null);

		Group savedGroup = this.persist(group);

		Set<Role> selectedRoles = new HashSet<Role>();

		for (Integer roleId : groupFormBean.getRoleIds()) {
			Role role = this.findRoleById(roleId);
			selectedRoles.add(role);
		}

		savedGroup.setRoles(selectedRoles);
		return this.persist(savedGroup);
	}

	@Override
	public ValidationResult validateBeforeDeleteGroup(Integer groupId, Locale locale) {

		ValidationResult result = null;

		List<User> groupAssociatedUsers = userRepository.findByGroup_Id(groupId);

		if (groupAssociatedUsers != null && groupAssociatedUsers.size() > 0) {
			result = new ValidationResult(ResultType.ERROR);
			result.addErrorMessage(messageSource.getMessage("global.error.data_in_use", null, locale));
		} else {
			result = new ValidationResult(ResultType.SUCCESS);
		}

		return result;
	}

	@Override
	public Group findGroupByName(String name) {
		return groupRepository.findByName(name);
	}

	@Override
	public Page<Group> findPageableGroups(Integer pageNumber) {
		PageRequest pageRequest = new PageRequest(pageNumber, GlobalManager.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id");
		return groupRepository.findAll(pageRequest);
	}

}
