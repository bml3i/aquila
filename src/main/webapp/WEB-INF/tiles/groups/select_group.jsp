<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<input type="hidden" name="groupId" value="${groupId}" />

<div class="page-header">
	<h4>
		<s:message code="global.info.group_setting" />
	</h4>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span8">
			<div id="groupRoles" class="panel"></div>
		</div>
		<div class="span4"></div>
	</div>
</div>

<script src="/resources/js/groups.js"></script>
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		var groupId = $('input[name=groupId]').val();
		showGroupRoles(groupId);
	});
</script>
