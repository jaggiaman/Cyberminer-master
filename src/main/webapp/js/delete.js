$(document).ready(function() {
	$(".loader").hide();
	getAll();
});

function disableDelete() {
	$('[id*="delete-"]').attr("disabled", true);
}

function enableDelete() {
	$('[id*="delete-"]').attr("disabled", false);
}

function getAll() {
	$("#resultsContainer").empty();
	
	$(".loader").show();
	
	var requestData =
	{
		"searchTxt": "",
		"type": "",
		"sort": "alpha",
		"page": 0,
		"pageSize": 0
	};
  
	$.ajax({
		type: "POST",
		url: "/search",
		dataType: "json",
		contentType: 'application/json',
        data: JSON.stringify(requestData),
		success: function(response) {
		  handleResponse(response);
		  $(".loader").hide();
		},
		error: function() {
			$(".loader").hide();
		}
	});
}

function handleResponse(response) {
	var resultSet = response.lines;
	
	for(var i = 0; i < resultSet.length; i++) {
		$("#resultsContainer").append(
			"<div class='row justify-content-center border col-md-9 mx-auto p-2 mb-2 align-items-center'>" +
				"<div class='col-md-11'>" +
					"<h6>" + resultSet[i].descriptor + "</h6>" +
					"<a href='" + resultSet[i].url + "'>" + resultSet[i].url + "</a>" +
				"</div>" +
				"<div class='col-md-1 text-right'>" +
					"<i id='delete-" + resultSet[i].id + "' class='fas fa-trash text-primary'></i>" +
				"</div>" +
			"</div>"
		);
	}
	
	$('[id*="delete-"]').click(deleteClicked);
}

function deleteClicked() {
	disableDelete();
	$(".loader").show();
	
	var lineId = $(this).attr("id").substring(7);
  
	var requestData =
	{
		"lineId": lineId
	};
  
	$.ajax({
		type: "POST",
		url: "/delete",
		dataType: "text",
		contentType: 'application/json',
        data: JSON.stringify(requestData),
		success: function(response) {
			$(".loader").hide();
			alert(response);
			getAll();
		},
		error: function(xhr, exception) {
			$(".loader").hide();
			alert(exception);
			enableDelete();
		}
	});
}