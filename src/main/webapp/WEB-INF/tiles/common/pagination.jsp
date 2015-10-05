<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<div class="pagination pagination-small">
	<c:url var="firstUrl" value="javascript:${showRecordsJSFunc}(${prefixFuncParams}1);" />
	<c:url var="lastUrl"
		value="javascript:${showRecordsJSFunc}(${prefixFuncParams}${totalPages});" />
	<c:url var="prevUrl"
		value="javascript:${showRecordsJSFunc}(${prefixFuncParams}${currentIndex - 1});" />
	<c:url var="nextUrl"
		value="javascript:${showRecordsJSFunc}(${prefixFuncParams}${currentIndex + 1});" />

	<c:set var="nav_first_page">
		<s:message code="global.info.page_first" />
	</c:set>
	<c:set var="nav_last_page">
		<s:message code="global.info.page_last" />
	</c:set>
	<c:set var="nav_prev_page">
		<s:message code="global.info.page_previous" />
	</c:set>
	<c:set var="nav_next_page">
		<s:message code="global.info.page_next" />
	</c:set>

	<ul>
		<c:choose>
			<c:when test="${currentIndex == 1}">
				<li class="disabled"><a href="#">${nav_first_page}</a></li>
				<li class="disabled"><a href="#">${nav_prev_page}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${firstUrl}">${nav_first_page}</a></li>
				<li><a href="${prevUrl}">${nav_prev_page}</a></li>
			</c:otherwise>
		</c:choose>
		<c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
			<c:url var="pageUrl" value="javascript:${showRecordsJSFunc}(${prefixFuncParams}${i});" />
			<c:choose>
				<c:when test="${i == currentIndex}">
					<li class="active"><a href="${pageUrl}"><c:out
								value="${i}" /></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${pageUrl}"><c:out value="${i}" /></a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${currentIndex == totalPages}">
				<li class="disabled"><a href="#">${nav_next_page}</a></li>
				<li class="disabled"><a href="#">${nav_last_page}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${nextUrl}">${nav_next_page}</a></li>
				<li><a href="${lastUrl}">${nav_last_page}</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>