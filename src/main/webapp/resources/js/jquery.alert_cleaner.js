(function($) {
	$.fn.bindAlertCleanerClassName = function(className) {
		var targetSelector;

		if (className == null || className == "") {
			targetSelector = "*";
		} else {
			targetSelector = "." + className;
		}

		$(this).delegate(targetSelector, "click", function() {
			$(".alert").hide();
		});
	};
})(jQuery);