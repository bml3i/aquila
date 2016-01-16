<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form id="form" class="form-horizontal" method="post"
	modelAttribute="userFormBean">
	<fieldset>
		<legend>
			<s:message code="users.info.edit_my_account" />
		</legend>

		<input type="hidden" name="id" value="${userFormBean.id}" />
		<input type="hidden" name="groupId" value="${editUserGroup.id}" />

		<s:bind path="userId">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="userId"><s:message
						code="users.info.user_userid" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="userId" tabindex="1"
						readonly="true" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>
		<s:bind path="name">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="name"><s:message
						code="users.info.user_name" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="name" tabindex="2"
						autofocus="autofocus" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>
		<s:bind path="password">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="password"><s:message
						code="users.info.user_password" /></label>
				<div class="controls">
					<form:input type="password" class="input-xlarge" path="password"
						tabindex="3" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>
		<s:bind path="email">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="email"><s:message
						code="users.info.user_email" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="email" tabindex="4" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>

		<div class="control-group">
			<label class="control-label" for="groupDescription"><s:message
					code="users.info.user_group" /></label>
			<div class="controls">
				<input class="input-xlarge" type="text" name="groupDescription"
					value="${editUserGroup.description}" tabindex="5" readonly />
			</div>
		</div>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary" tabindex="7">
				<s:message code="global.info.btn.update" />
			</button>
			<button type="button" class="btn" tabindex="8"
				onclick="history.go(-1);">
				<s:message code="global.info.btn.cancel" />
			</button>
		</div>
	</fieldset>
</form:form>