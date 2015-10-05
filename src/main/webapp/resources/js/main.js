// Clean Ajax Messages
function cleanAjaxMessage() {
	$('#ajax_success').html('').hide();
	$('#ajax_error').html('').hide();
};

// Get currentLocale
function getCurrentLocale() {
	var currentLocale = $("#current_locale").val();
	var localeArray = [ "en", "zh-CN" ];
	
	if (jQuery.inArray(currentLocale, localeArray) == -1) {
		currentLocale = 'en';
	}
	
	return currentLocale;
}

// Bind DateTimePicker
function bindDateTimePicker() {
	var currentLocale = $("#current_locale").val();
	var localeArray = [ "en", "zh-CN" ];
	
	if (jQuery.inArray(currentLocale, localeArray) == -1) {
		currentLocale = 'en';
	}
	
	$('.form_datetime').datetimepicker({
		language : currentLocale,
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		showMeridian : 1
	});
};