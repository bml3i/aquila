<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div class="page-header">
	<h4>
		<s:message code="global.info.group_setting" />
	</h4>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span8">
			<div id="groups" class="panel"></div>
		</div>
		<div class="span4"></div>
	</div>
	<div class="row-fluid">
		<div class="span6">
			<button type="button" class="btn btn-primary"
				onclick="location.href='/groups/create'">
				<s:message code="global.info.btn.add" />
			</button>
		</div>
		<div class="span6"></div>
	</div>
</div>
<br/>

<script src="/resources/js/groups.js"></script>
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		showGroups();
	});
</script>
