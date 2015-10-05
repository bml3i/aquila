function showDepartments(pageNumber) {
	$.ajax({
		url : '/ajax/departments/show_departments',
		data : {
			pageNumber : pageNumber == null ? 1 : pageNumber
		},
		type : 'get',
		cache : false,
		success : function(response, textStatus, xhr) {
			$('#departments').html(response);
		}
	});
};


function deleteDepartment(deleteId) {
	$.ajax({
		url : '/rest/departments/delete_department',
		data : {
			deleteId : deleteId
		},
		type : 'post',
		cache : false,
		success : function(response, textStatus, xhr) {
			var obj = jQuery.parseJSON(xhr.responseText);
			if (obj.resultType == 'SUCCESS') {
				showDepartments();
			} else if (obj.resultType == 'ERROR') {
				$('#ajax_error').html(obj.errorMessage).show();
			}
		}
	});
};