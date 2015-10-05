<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form id="form" class="form-horizontal" method="post"
	modelAttribute="departmentFormBean">
	<fieldset>
		<legend>
			<s:message code="departments.info.add_department" />
		</legend>

		<s:bind path="name">
			<div class="control-group ${status.error ? 'error' : '' }">
				<label class="control-label" for="name"><s:message
						code="departments.info.departments_name" /></label>
				<div class="controls">
					<form:input class="input-xlarge" path="name" tabindex="1"
						autofocus="autofocus" />
					<c:if test="${status.error}">
						<span class="help-inline">${status.errorMessage}</span>
					</c:if>
				</div>
			</div>
		</s:bind>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary" tabindex="2">
				<s:message code="global.info.btn.add" />
			</button>
		</div>
	</fieldset>
</form:form>