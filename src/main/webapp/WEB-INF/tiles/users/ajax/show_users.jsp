<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${fn:length(users) == 0}">
	<div class="alert alert-info">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<s:message code="global.info.no_records" />
	</div>
</c:if>
<c:if test="${fn:length(users) > 0}">
	<table class="table table-hover">
		<thead>
			<tr>
				<th><s:message code="users.info.users_userid" /></th>
				<th><s:message code="users.info.users_name" /></th>
				<th><s:message code="users.info.users_group" /></th>
				<th><s:message code="users.info.users_department" /></th>
				<th><s:message code="users.info.users_create_date" /></th>
				<th><s:message code="users.info.users_status" /></th>
				<th><s:message code="users.info.users_operation" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.userId}</td>
					<td>${user.name}</td>
					<td>${user.group.description}</td>
					<td>${user.department.name}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd"
							value="${user.createDate}" /></td>
					<td><c:if test="${user.activeFlag}">
							<s:message code="global.info.active" />
						</c:if> <c:if test="${not user.activeFlag}">
							<s:message code="global.info.inactive" />
						</c:if></td>
					<td><c:if test="${user.activeFlag}">
							<button class="btn btn-danger btn-mini"
								onclick="changeUserStatus(${user.id}, false, ${currentIndex});">
								<s:message code="global.info.btn.deactivate" />
							</button>
						</c:if> <c:if test="${not user.activeFlag}">
							<button class="btn btn-success btn-mini"
								onclick="changeUserStatus(${user.id}, true, ${currentIndex});">
								<s:message code="global.info.btn.activate" />
							</button>
						</c:if>
						<button class="btn btn-link btn-mini"
							onclick="location.href='/user/edit/${user.id}'">
							<s:message code="global.info.btn.edit" />
						</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<jsp:include page="/WEB-INF/tiles/common/pagination.jsp" />
</c:if>