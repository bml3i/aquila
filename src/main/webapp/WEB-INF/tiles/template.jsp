<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html>
<html>
<head>
<title><s:message code="global.info.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="/resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen" />
<link href="/resources/css/core.css" rel="stylesheet" media="screen" />
<link href="/resources/css/datetimepicker.css" rel="stylesheet" media="screen" />
<link href="/resources/css/bootstrap-fileupload.min.css" rel="stylesheet" media="screen" />
<link rel="shortcut icon" href="/resources/img/favicon.ico" />
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
</style>
<script src="/resources/js/jquery.js"></script>
<script src="/resources/js/jquery.alert_cleaner.js"></script>
<script src="/resources/js/jquery.typeahead_binder.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>
<script src="/resources/js/bootstrap-datetimepicker.min.js"
	charset="utf-8"></script>
<script src="/resources/js/locales/bootstrap-datetimepicker.zh-CN.js"
	charset="utf-8"></script>
<script src="/resources/js/bootstrap-fileupload.min.js"></script>
<script src="/resources/js/bootbox.min.js"></script>
<script src="/resources/js/main.js"></script>
</head>
<body>
	<tiles:insertAttribute name="menu" />
	<div id="container" class="container">

		<div id="ajax_success" class="alert alert-success" style="display: none;"></div>
		<div id="ajax_error" class="alert alert-error" style="display: none;"></div>

		<input type="hidden" id="current_locale" name="current_locale"
			value='<s:message code="global.config.locale"/>' />

		<c:if test="${not empty info}">
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				${info}
			</div>
		</c:if>

		<c:if test="${not empty error}">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				${error}
			</div>
		</c:if>

		<tiles:insertAttribute name="body" />
	</div>
	<tiles:insertAttribute name="footer" />
</body>

<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$("#container").bindAlertCleanerClassName();
		
		// Set locale for bootbox
		bootbox.setLocale(getCurrentLocale());
	});
</script>

</html>