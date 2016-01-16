<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form id="form" class="form-horizontal" method="post"
	modelAttribute="groupFormBean">
	<fieldset>
		<legend>
			<s:message code="groups.info.add_group" />
		</legend>

		<s:bind path="name">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="name"><s:message
						code="groups.info.groups_name" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="name" tabindex="1"
						autofocus="autofocus" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>

		<s:bind path="description">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="description"><s:message
						code="groups.info.groups_description" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="description" tabindex="2" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>

		<s:bind path="roleIds">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="roleIds"><s:message
						code="groups.info.group_role" /></label>
				<div class="controls">
					<form:select path="roleIds" multiple="true" tabindex="3">
						<form:options items="${roles}" itemValue="id"
							itemLabel="description" />
					</form:select>
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary" tabindex="4">
				<s:message code="global.info.btn.add" />
			</button>
			<button type="button" class="btn" tabindex="5"
				onclick="history.go(-1);">
				<s:message code="global.info.btn.cancel" />
			</button>
		</div>
	</fieldset>
</form:form>