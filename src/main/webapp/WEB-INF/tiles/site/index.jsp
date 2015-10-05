<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<c:set var="website_name">
	<s:message code="global.info.website_name" />
</c:set>

<div class="hero-unit">
	<h1>
		<s:message code="site.index.info.hero_unit_welcome"
			arguments="${website_name}" />
	</h1>
	<p>
		<s:message code="site.index.info.hero_unit_desc"
			arguments="${website_name}" />
	</p>
	<sec:authorize ifNotGranted="ROLE_USER">
		<p>
			<a class="btn btn-primary" href="/login"><s:message
					code="site.index.info.btn.log_in" /></a>
		</p>
	</sec:authorize>
</div>