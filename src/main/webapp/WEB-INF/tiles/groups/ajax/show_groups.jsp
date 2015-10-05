<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<table class="table table-hover">
	<thead>
		<tr>
			<th><s:message code="groups.info.groups_name" /></th>
			<th><s:message code="groups.info.groups_description" /></th>
			<th><s:message code="groups.info.groups_operation" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="group" items="${groups}">
			<tr>
				<td>${group.name}</td>
				<td>${group.description}</td>
				<td>
					<button class="btn btn-link btn-mini"
						onclick="location.href='/group/edit/${group.id}'">
						<s:message code="global.info.btn.edit" />
					</button>
					<button data-delete-group-id="${group.id}"
						class="btn btn-link btn-mini confirm_delete_group">
						<s:message code="global.info.btn.delete" />
					</button>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<jsp:include page="/WEB-INF/tiles/common/pagination.jsp" />

<script type="text/javascript" charset="utf-8">
	$(".confirm_delete_group").on("click", function(e) {
		var deleteGorupId = $(this).data('deleteGroupId');
		var confirmsMessage = "<s:message code='global.alert.are_you_sure'/>";

		bootbox.confirm(confirmsMessage, function(result) {
			if (result == true) {
				deleteGroup(deleteGorupId);
			}
		});
	});
	
</script>