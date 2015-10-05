<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<form class="form-horizontal" action="j_spring_security_check"
	method="post">
	<fieldset>
		<legend>
			<s:message code="login.info.login" />
		</legend>
		<div class="control-group">
			<label class="control-label" for="j_username"><s:message
					code="login.info.userid" /></label>
			<div class="controls">
				<input type="text" class="input-xlarge" name="j_username"
					id="j_username" tabindex="1" autofocus>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="j_password"><s:message
					code="login.info.password" /></label>
			<div class="controls">
				<input type="password" class="input-xlarge" name="j_password"
					id="j_password" tabindex="2">
			</div>
		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-primary">
				<s:message code="login.info.btn.login" />
			</button>
		</div>
	</fieldset>
</form>