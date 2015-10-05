function showGroups(pageNumber) {
	$.ajax({
		url : '/ajax/groups/show_groups',
		data : {
			pageNumber : pageNumber == null ? 1 : pageNumber
		},
		type : 'get',
		cache : false,
		success : function(response, textStatus, xhr) {
			$('#groups').html(response);
		}
	});
};

function showGroupRoles(groupId) {
	$.ajax({
		url : '/ajax/groups/show_group_roles',
		data : {
			groupId : groupId
		},
		type : 'get',
		cache : false,
		success : function(response, textStatus, xhr) {
			$('#groupRoles').html(response);
		}
	});
};

function deleteGroup(deleteId) {
	$.ajax({
		url : '/rest/groups/delete_group',
		data : {
			deleteId : deleteId
		},
		type : 'post',
		cache : false,
		success : function(response, textStatus, xhr) {
			var obj = jQuery.parseJSON(xhr.responseText);
			if (obj.resultType == 'SUCCESS') {
				showGroups();
			} else if (obj.resultType == 'ERROR') {
				$('#ajax_error').html(obj.errorMessage).show();
			}
		}
	});
};