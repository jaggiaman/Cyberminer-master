var searchTxt;
var searchType;
var sortType;
var pageSize;
var currPage;
var searchTextChangedCounter = 0;

$(document).ready(function() {
	$(".loader").hide();
	$("#noResultsContainer").hide();
	$("#numResultsContainer").hide();
	$("#resultsContainer").hide();
	$("#pagingContainer").hide();
	
	$("#searchTxt").val("");
	$("#searchTxt").focus();
	
	$("#searchBtn").click(function() {
		searchTxt = $("#searchTxt").val();
		searchType = $("input[name=searchRadio]:checked").val();
		sortType = $("input[name=sortRadio]:checked").val();
		pageSize = $("#pageSize").val();
		
		search();
	});
	
	$("#searchTxt").change(function() {
        $("#searchTxt").removeClass("invalidField");
		$("#searchTxt").addClass("border border-primary");
    });
	
	$('#searchTxt').on('input', searchTxtChanged);
	
	$("#searchTxt").keypress(function (e) {
	    if (e.keyCode == 13) {
	    	searchTxt = $("#searchTxt").val();
			searchType = $("input[name=searchRadio]:checked").val();
			sortType = $("input[name=sortRadio]:checked").val();
			pageSize = $("#pageSize").val();
			
	    	search();
	    }
	});
	
	$("#paging ul li").click(pagingClicked);
});

function disableSearch() {
	$("#searchTxt").attr("disabled", true);
	$("#orRadio").attr("disabled", true);
	$("#andRadio").attr("disabled", true);
	$("#notRadio").attr("disabled", true);
	$("#alphaRadio").attr("disabled", true);
	$("#accessRadio").attr("disabled", true);
	$("#paymentRadio").attr("disabled", true);
	$("#pageSize").attr("disabled", true);
	$("#searchBtn").attr("disabled", true);
}

function enableSearch() {
	$("#searchTxt").attr("disabled", false);
	$("#orRadio").attr("disabled", false);
	$("#andRadio").attr("disabled", false);
	$("#notRadio").attr("disabled", false);
	$("#alphaRadio").attr("disabled", false);
	$("#accessRadio").attr("disabled", false);
	$("#paymentRadio").attr("disabled", false);
	$("#pageSize").attr("disabled", false);
	$("#searchBtn").attr("disabled", false);
}

function searchTxtChanged() {
	if($('#searchTxt').val()) {
		searchTextChangedCounter++;
		
		var requestData =
		{
			"searchTxt": $('#searchTxt').val(),
			"num": 5,
			"counter": searchTextChangedCounter,
		};
	  
		$.ajax({
			type: "POST",
			url: "/suggest",
			dataType: "json",
			contentType: 'application/json',
	        data: JSON.stringify(requestData),
			success: function(response) {
				var counter = response.counter;
				
				if(counter == searchTextChangedCounter) {
					var suggestions = response.suggestions;
					
					$("#suggList").empty();
					
					for(var i = 0; i < suggestions.length; i++) {
						$("#suggList").append("<option>" + suggestions[i].suggestion + "</option>");
					}
				}
			}
		});
	} else {
		$("#suggList").empty();
	}
}

function pagingClicked(event) {
	var element = event.target;

	if(!element.classList.contains("disabled")) {
		var elementText = element.childNodes[0].textContent;

		if(elementText == "Previous") {
			search(currPage - 1);
		} else if (elementText == "Next") {
			search(currPage + 1);
		} else if (Number(elementText) != currPage) {
			search(Number(elementText));
		}
	}
}

function search(page) {
	if(!page || !Number.isInteger(page)) {
		page = 1;
	}
	
	currPage = page;
	
	$("#noResultsContainer").hide();
	$("#numResultsContainer").hide();
	$("#resultsContainer").hide();
	$("#pagingContainer").hide();
	
	$("#resultsContainer").empty();
	
	if(!$("#searchTxt").val()) {
        $("#searchTxt").addClass("invalidField");
		$("#searchTxt").removeClass("border border-primary");
		return;
    }
	
	disableSearch();
	$(".loader").show();
	
	var requestData =
	{
		"searchTxt": searchTxt,
		"type": searchType,
		"sort": sortType,
		"page": currPage,
		"pageSize": pageSize
	};
  
	$.ajax({
		type: "POST",
		url: "/search",
		dataType: "json",
		contentType: 'application/json',
        data: JSON.stringify(requestData),
		success: function(response) {
			handleResponse(response);
			enableSearch();
			$(".loader").hide();
		},
		error: function() {
			enableSearch();
			$(".loader").hide();
		}
	});
}

function handleResponse(response) {
	var resultSet = response.lines;
	var numOfResults = response.numOfResults;
	
	if(numOfResults > 0) {
		$("#numResultsTxt").text("Total Results: " + numOfResults);
		$("#pageSizeTxt").text(pageSize + " Results Per Page");
		
		for(var i = 0; i < resultSet.length; i++) {
			$("#resultsContainer").append(
				"<div class='row justify-content-center border-top col-md-8 mx-auto p-2 align-items-center'>" +
					"<div class='col-md-12'>" +
						"<h6>" + resultSet[i].descriptor + "</h6>" +
						"<a id='link-" + resultSet[i].id + "' href='" + resultSet[i].url + "' target='_blank'>" + resultSet[i].url + "</a>" +
					"</div>" +
				"</div>"
			);
		}
		
		$('[id*="link-"]').click(linkClicked);
		
		handlePaging(numOfResults);
	
		$("#numResultsContainer").show();
		$("#resultsContainer").show();
		$("#pagingContainer").show();
	} else {
		$("#noResultsContainer").show();
	}
}

function linkClicked() {
	var lineId = $(this).attr("id").substring(5);
  
	var requestData =
	{
		"lineId": lineId
	};
  
	$.ajax({
		type: "POST",
		url: "/update",
		dataType: "text",
		contentType: 'application/json',
        data: JSON.stringify(requestData)
	});
	
	return true;
}

function handlePaging(numOfResults) {
	var totalPages = 1;
	
	if(numOfResults % pageSize == 0) {
		totalPages = numOfResults/pageSize;
	} else {
		totalPages = Math.floor(numOfResults/pageSize) + 1;
	}

	$("#previous").removeClass("disabled");
	$("#next").removeClass("disabled");
	$("#first").show();
	$("#second").show();
	$("#third").show();
	$("#fourth").show();
	$("#fifth").show();
	$("#first").removeClass("active");
	$("#second").removeClass("active");
	$("#third").removeClass("active");
	$("#fourth").removeClass("active");
	$("#fifth").removeClass("active");

	if(currPage == 1) {
		$("#previous").addClass("disabled");
	}

	if(currPage == totalPages) {
		$("#next").addClass("disabled");
	}

	if(totalPages == 1) {
		$("#first a").text("1");

		$("#second").hide();
		$("#third").hide();
		$("#fourth").hide();
		$("#fifth").hide();

		$("#first").addClass("active");
	} else if (totalPages == 2) {
		$("#first a").text("1");
		$("#second a").text("2");

		$("#third").hide();
		$("#fourth").hide();
		$("#fifth").hide();

		if(currPage == 1) {
			$("#first").addClass("active");
		} else if (currPage == 2) {
			$("#second").addClass("active");
		}
	} else if (totalPages == 3) {
		$("#first a").text("1");
		$("#second a").text("2");
		$("#third a").text("3");

		$("#fourth").hide();
		$("#fifth").hide();

		if(currPage == 1) {
			$("#first").addClass("active");
		} else if (currPage == 2) {
			$("#second").addClass("active");
		} else if (currPage == 3) {
			$("#third").addClass("active");
		}
	} else if (totalPages == 4) {
		$("#first a").text("1");
		$("#second a").text("2");
		$("#third a").text("3");
		$("#fourth a").text("4");

		$("#fifth").hide();

		if(currPage == 1) {
			$("#first").addClass("active");
		} else if (currPage == 2) {
			$("#second").addClass("active");
		} else if (currPage == 3) {
			$("#third").addClass("active");
		} else if (currPage == 4) {
			$("#fourth").addClass("active");
		}
	} else if (currPage == 1) {
		$("#first a").text(1);
		$("#second a").text(2);
		$("#third a").text(3);
		$("#fourth a").text(4);
		$("#fifth a").text(5);

		$("#first").addClass("active");
	} else if (currPage == 2) {
		$("#first a").text(1);
		$("#second a").text(2);
		$("#third a").text(3);
		$("#fourth a").text(4);
		$("#fifth a").text(5);

		$("#second").addClass("active");
	} else if (currPage == totalPages) {
		$("#first a").text(currPage - 4);
		$("#second a").text(currPage - 3);
		$("#third a").text(currPage - 2);
		$("#fourth a").text(currPage - 1);
		$("#fifth a").text(currPage);

		$("#fifth").addClass("active");
	} else if (currPage == totalPages - 1) {
		$("#first a").text(currPage - 3);
		$("#second a").text(currPage - 2);
		$("#third a").text(currPage - 1);
		$("#fourth a").text(currPage);
		$("#fifth a").text(currPage + 1);

		$("#fourth").addClass("active");
	} else {
		$("#first a").text(currPage - 2);
		$("#second a").text(currPage - 1);
		$("#third a").text(currPage);
		$("#fourth a").text(currPage + 1);
		$("#fifth a").text(currPage + 2);

		$("#third").addClass("active");
	}
}