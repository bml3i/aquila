<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h1>This is a demo</h1>

<div id="demo_msg_box"></div>

<c:set var="now" value="<%=new java.util.Date()%>" />
<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${now}" />

<form:form id="form" class="form-horizontal" method="post">
	<button type="submit" class="btn btn-primary">Post Form</button>

	<input name="tag" class="input-xlarge typeahead"
		data-source='["中国","美国","china","BMW"]' />
</form:form>

<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$('#demo_msg_box').html('Aquila');

		$('.typeahead').bindCustomTypeAhead();
	});

	
</script>