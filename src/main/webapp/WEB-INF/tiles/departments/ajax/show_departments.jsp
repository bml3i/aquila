<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${fn:length(departments) == 0}">
	<div class="alert alert-info">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<s:message code="global.info.no_records" />
	</div>
</c:if>

<c:if test="${fn:length(departments) > 0}">
	<table class="table table-hover">
		<thead>
			<tr>
				<th><s:message code="departments.info.departments_name" /></th>
				<th><s:message code="departments.info.departments_operation" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="department" items="${departments}">
				<tr>
					<td>${department.name}</td>
					<td>
						<button class="btn btn-link btn-mini"
							onclick="location.href='/department/edit/${department.id}'">
							<s:message code="global.info.btn.edit" />
						</button>
						<button data-delete-department-id="${department.id}"
							class="btn btn-link btn-mini confirm_delete_department">
							<s:message code="global.info.btn.delete" />
						</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<jsp:include page="/WEB-INF/tiles/common/pagination.jsp"/>
</c:if>

<script type="text/javascript" charset="utf-8">
	$(".confirm_delete_department").on("click", function(e) {
		var deleteDepartmentId = $(this).data('deleteDepartmentId');
		var confirmsMessage = "<s:message code='global.alert.are_you_sure'/>";

		bootbox.confirm(confirmsMessage, function(result) {
			if (result == true) {
				deleteDepartment(deleteDepartmentId);
			}
		});
	});
	
</script>