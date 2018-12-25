$(document).ready(function() {
	$(".loader").hide();
	
	$("#descriptor").val("");
	$("#url").val("");
	$("#payment").val("");
	
	$("#descriptor").focus();
	
	$("#addBtn").click(add);
	
	$("#descriptor").change(function() {
        $("#descriptor").removeClass("invalidField");
    });
	
	$("#url").change(function() {
        $("#url").removeClass("invalidField");
    });
	
	$("#payment").change(function() {
        $("#payment").removeClass("invalidField");
    });
});

function disableAdd() {
	$("#descriptor").attr("disabled", true);
	$("#url").attr("disabled", true);
	$("#payment").attr("disabled", true);
	$("#addBtn").attr("disabled", true);
}

function enableAdd() {
	$("#descriptor").attr("disabled", false);
	$("#url").attr("disabled", false);
	$("#payment").attr("disabled", false);
	$("#addBtn").attr("disabled", false);
}

function add() {
	var formValid = true;
	
	if(!$("#descriptor").val()) {
        $("#descriptor").addClass("invalidField");
		formValid = false;
    }
	
	if(!$("#url").val()) {
        $("#url").addClass("invalidField");
		formValid = false;
    }
	
	if(!$("#payment").val()) {
        $("#payment").addClass("invalidField");
		formValid = false;
    }
	
	if(!formValid) {
		return;
	}
	
	disableAdd();
	$(".loader").show();
	
	var requestData =
	{
		"descriptor": $("#descriptor").val(),
		"url": $("#url").val(),
		"payment": parseInt($("#payment").val())
	};
  
	$.ajax({
		type: "POST",
		url: "/add",
		dataType: "text",
		contentType: 'application/json',
        data: JSON.stringify(requestData),
		success: function(response) {
			$(".loader").hide();
			alert(response);
			enableAdd();
		},
		error: function(xhr, status, error) {
			$(".loader").hide();
			alert(error);
			enableAdd();
		}
	});
	
	$("#descriptor").val("");
	$("#url").val("");
	$("#payment").val("");
}