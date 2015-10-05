<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<footer class="modal-footer">
	<div class="container">
		<p class="pull-left">
			<a href="#"><s:message code="global.info.terms_of_service" /></a> <span
				class="divider">/</span> <a href="#"><s:message
					code="global.info.about" /></a> <span class="divider">/</span> <a
				href="#"><s:message code="global.info.faq" /></a> <span
				class="divider">/</span>
			<s:message code="global.info.icons_from" />
			<a href="/demo/upload">Glyphicons</a> (
			<s:message code="global.info.online_user_number" />
			: ${onlineUserNum})
		</p>
		<p class="pull-right">
			<s:message code="global.info.copyright" />
		</p>
	</div>
</footer>