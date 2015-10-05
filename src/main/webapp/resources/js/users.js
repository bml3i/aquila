function showUsers(pageNumber) {
	$.ajax({
		url : '/ajax/users/show_users',
		data : {
			pageNumber : pageNumber == null ? 1 : pageNumber
		},
		type : 'get',
		cache : false,
		success : function(response, textStatus, xhr) {
			$('#users').html(response);
		}
	});
};

function changeUserStatus(updateId, activeFlag, currPageNum) {
	$.ajax({
		url : '/rest/users/update_active_flag',
		data : {
			updateId : updateId,
			activeFlag : activeFlag
		},
		type : 'post',
		cache : false,
		success : function(response, textStatus, xhr) {
			var obj = jQuery.parseJSON(xhr.responseText);
			if (obj.resultType == 'SUCCESS') {
				showUsers(currPageNum == null ? 1 : currPageNum);
			} else if (obj.resultType == 'ERROR') {
				showUsers(currPageNum == null ? 1 : currPageNum);
				$('#ajax_error').html(obj.errorMessage).show();
			}
		}
	});
};