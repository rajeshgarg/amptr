
$(document).ready(function() {
	initalizeResourceBundleForSalesCalendar();
	setInitialData('H');	
	$("#productMultiSelect","#salesCalendarForm").multiselect({
		selectedList: 1,
		position: {
		      my: 'bottom',
		      at: 'top'
		   }
	}).multiselectfilter();
	
	$("#salesTargetMultiSelect" , "#salesCalendarForm").multiselect({
		selectedList: 1,
		position: {
		      my: 'bottom',
		      at: 'top'
		   },
		uncheckAll: function(){
			enableDisabledChildSaleTargetsForCrossPlatforms();
		   },
		click: function(event, ui){
			enableDisabledChildSaleTargetsForCrossPlatforms();
		   }
	}).multiselectfilter();
	
	$("#countriesMultiSelect").multiselect({
		selectedList: 1,
		position: {
		      my: 'bottom',
		      at: 'top'
		   }
	}).multiselectfilter();
	$("#filterCalendarMultiSelect").multiselect({
		selectedList: 1,
		position: {
		      my: 'bottom',
		      at: 'top'
		   },
		   checkAll: function(){
			   filterCalendar();
		   },
		   uncheckAll: function(){
			   filterCalendar();
		   }
	}).multiselectfilter();
	$("#dateMultiSelect","#salesCalendarForm").select2();
	$("#weekdayMultiSelect","#salesCalendarForm").select2();
	$("#goButton",  "#salesCalendarForm" ).bind( "click", function(event, ui) {
		$("#dateString","#salesCalendarForm").val($("#dateMultiSelect" , "#salesCalendarForm").val());
		$("#filterCalendarMultiSelect").multiselect("uncheckAll");
		getCalendarData();		
	});
});
$(document).ajaxComplete(function(event, request, settings) {
	if (request.getResponseHeader("'REQUIRES_AUTH") === '1') {
		$("#sessionTimeout").dialog("open");
	}
});
$(document).ajaxStart(function(){
	 $.blockUI($('#loader'));
});
$(document).ajaxStop(function(){
	 $.unblockUI();
});

var globalMonthArr = new Array();
		globalMonthArr[0] = "January";
		globalMonthArr[1] = "February";
		globalMonthArr[2] = "March";
		globalMonthArr[3] = "April";
		globalMonthArr[4] = "May";
		globalMonthArr[5] = "June";
		globalMonthArr[6] = "July";
		globalMonthArr[7] = "August";
		globalMonthArr[8] = "September";
		globalMonthArr[9] = "October";
		globalMonthArr[10] = "November";
		globalMonthArr[11] = "December";

 function closeMe(){
	 window.close();
 };
 
function setInitialData(type){
	/**
	 *select all products of class Home Page 
	 */
	$("#productType_"+type, "#salesCalendarForm").attr('checked','checked');
	changeProductType(type);
	$("#productMultiSelect" , "#salesCalendarForm").find("option").each(function(){
	     $(this).attr('selected','selected');	     
	});
	$("#productMultiSelect","#salesCalendarForm").multiselect('refresh');
	if(type == 'H'){
		$("#productMultiSelect_custom" , "#salesCalendarForm").show();
		$("#productLabel" , "#salesCalendarForm").show();
		$("#salesTargetMultiSelect_custom" , "#salesCalendarForm").hide();
		$("#salesTargetLabel" , "#salesCalendarForm").hide();
		$("option", $("#salesTargetMultiSelect" , "#salesCalendarForm")).remove();
	}else{
		$("#productMultiSelect_custom" , "#salesCalendarForm").hide();
		$("#productLabel" , "#salesCalendarForm").hide();
		$("#salesTargetMultiSelect_custom" , "#salesCalendarForm").show();
		$("#salesTargetLabel" , "#salesCalendarForm").show();
		setsosSalesTargets($("#productMultiSelect" , "#salesCalendarForm").val());
	}
	
	/**
	 * Set country as US 
	 */
	
	$("#countriesMultiSelect" , "#salesCalendarForm").find("option").each(function(){
	    if($(this).attr("value") == 283){
	    	$(this).attr('selected','selected');
	    }else{
	    	$(this).attr('selected',false);
	    }
	});
	$("#countriesMultiSelect","#salesCalendarForm").multiselect('refresh');
	
	/**
	 *Set month as current Month 
	 */
	$("#dateMultiSelect" , "#salesCalendarForm").find("option").each(function(){
		var d = new Date();
		var currentMonthYear = globalMonthArr[d.getMonth()] + " " + d.getFullYear(); 
	    if($(this).attr("value") == currentMonthYear){
	    	$(this).attr('selected','selected').trigger("change");
	    }
	});
	$("#weekdayMultiSelect" , "#salesCalendarForm").val("").trigger("change");
	$("#dateString","#salesCalendarForm").val($("#dateMultiSelect" , "#salesCalendarForm").val());
	$("#filterCalendarMultiSelect").multiselect('uncheckAll');
	getCalendarData();
};


function setsosSalesTargets(selectedProduct) {
    var salesTargetIdMulti = $("#salesTargetMultiSelect" , "#salesCalendarForm");
	var actionURL = "../managepackage/getSalesTargetForProduct.action?productID=" + selectedProduct;
	dynaLoadSalesTargetsForCrossPlatform(salesTargetIdMulti, actionURL);
}

/**
 * Dynamic load sales target based on productId
 * @param {Object} select
 * @param {Object} actionURL
 */
function dynaLoadSalesTargetsForCrossPlatform(multiSelectBox, actionURL){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = actionURL;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
        $("option", multiSelectBox).remove();
        sortListForSalesTargets(result.objectMap.gridKeyColumnValue, multiSelectBox, result.objectMap.childSaleTargetIds);
        $(multiSelectBox).multiselect("refresh");			
    };
    ajaxReq.submit();
}

function sortListForSalesTargets(result, select, childSaleTargetIds){
    var arrVal = sortMapDataByValue(result);
    var optString = "";
	
    for (var i = 0; i < arrVal.length; i++) {
        var parentValue = 'NA';
        $.each(childSaleTargetIds, function(j, value){
            if (j == arrVal[i][1]) {
                parentValue = value;
            }
        });
        optString = optString + '<option value="' + arrVal[i][1] + '" parent="' + parentValue + '">' + arrVal[i][0] + '</option>';
    }
    $(select).append(optString);
}


function enableDisabledChildSaleTargetsForCrossPlatforms() {
    var salesTargetIdCombo = $("#salesTargetMultiSelect" , "#salesCalendarForm");
    $(salesTargetIdCombo).multiselect("widget").find(":checkbox").each(function(){
		$(this).attr("disabled", false);
    });
	var salesTargetIds = $(salesTargetIdCombo).multiselect("getChecked").map(function(){
        return this.value;
    }).get();
	if (salesTargetIds.length > 0) {
		for (var i = 0; i < salesTargetIds.length; i++) {
			enableDisableSalesTargetForProposal(salesTargetIdCombo, getChildSalesTargetForCrossPlatform(salesTargetIdCombo, salesTargetIds[i]));
		}
	}
}

function enableDisableSalesTargetForProposal(salesTargetIdCombo, salesTargetIds) {
	if (salesTargetIds.length > 0) {
		for (var i = 0; i < salesTargetIds.length; i++) {
			enableDisableSalesTargetForProposal(salesTargetIdCombo, getChildSalesTargetForCrossPlatform(salesTargetIdCombo, salesTargetIds[i]));
            $("#salesTargetMultiSelect option[value="+salesTargetIds[i]+"]").attr('selected', false);
		   	$(salesTargetIdCombo).multiselect("widget").find(":checkbox[value=" + salesTargetIds[i] + "]").attr('checked', false).attr('disabled','disabled');
		}
	} else {
		return;
	}	
}

function getChildSalesTargetForCrossPlatform(salesTargetIdCombo, salesTargetId) {
	return $(salesTargetIdCombo).find("option[parent='" + salesTargetId + "']").map(function(){
        return this.value;
    });
}


/**
 * To arrange JSON response in weekly manner for display  
 */
function displayWeeklyData(jsonResponse){
	var data = jsonResponse.objectMap.gridKeyColumnValue;
	var currentDate = data[0].viewDate;
	var month = currentDate.substring(currentDate.indexOf(',')+2,currentDate.lastIndexOf(',')-2) + currentDate.substring(currentDate.lastIndexOf(',')+1);
	document.getElementById('monthName').innerHTML = month;
	var viewToDisplay = '';
	var oldDate = "";
	var counter = 0;
	for(var i=0; i<data.length; i++){
		if(isNewWeek(oldDate,data[i].viewDate) || isNewMonth(oldDate, data[i].viewDate) || $('#weekdayMultiSelect').val()!=""){
			if(isNewMonth(oldDate, data[i].viewDate) && counter > 0){
				var currentMonth = data[i].viewDate.substring(data[i].viewDate.indexOf(',')+2,data[i].viewDate.lastIndexOf(',')-2) + data[i].viewDate.substring(data[i].viewDate.lastIndexOf(',')+1);
				viewToDisplay = viewToDisplay + "<div style='float:left;margin-top:40px;width:100%'><label class='label-bold' style='font-size:16px;'>"+ currentMonth +"</label></div><div style='clear:both'></div>";
			}
			var calendarHeader = '<div id="calendarHeader"><table><tr>' + 
				'<td style="width:4%"><label class="label-bold"></label></td>' +
				'<td style="width:8%"><label class="label-bold">Application ID</label></td>' +
				'<td style="width:14%"><label class="label-bold">Advertiser</label></td>' +
				'<td style="width:7%"><label class="label-bold">SOV</label></td>' +
				'<td style="width:14%"><label class="label-bold">Status</label></td>' +
				'<td style="width:14%"><label class="label-bold">Sales</label></td>'+
				'<td style="width:20%"><label class="label-bold">Configuration</label></td>' +
				'<td style="width:19%"><label class="label-bold">Category</label></td>' +
				'</tr></table></div>'+
				'<div style="clear:both"></div>';

			viewToDisplay = viewToDisplay + calendarHeader + '<div id="weeklyCalendar">';;
			counter = 0;
		}
		oldDate = data[i].viewDate;
		var vulnerableLI;
		var reservationSummary = '<div id="dayDetails">'+
									'<div id="reservationSummary">'+
										'<label class="label-bold"><span style="float:left">'+data[i].viewDate +'</span>'+
										'<span class="proposalCountLink" onClick=getListOfProposedLineItems("'+ encodeURIComponent(data[i].viewDate) +'")>';
			if(data[i].proposedLineItemsCount > 0){							
				reservationSummary = reservationSummary +'<span>( Proposed Line Items: </span>'+
														 '<span>'+data[i].proposedLineItemsCount +' )</span>';
				}else{
				reservationSummary = reservationSummary +'<span></span><span></span>';
				}
										reservationSummary = reservationSummary + '</span><span style="float:right;margin-right:15px;"><label class="label-bold" id="toggleExpansion" onClick="toggleExpansion(this)">[ - ]</label></span>'+
										'<span style="float:right;margin-right:15px;" id="sorValue">'+data[i].availableSOR +'</span></label>'+
									'</div>'+
									'<div style="clear:both"></div>'+
									'<div class="reservationDetail" style=""><table style="padding-left:0px;">';
		viewToDisplay = viewToDisplay + reservationSummary;
		if(data[i].salesOrderList){
			for(var j=0;j<data[i].salesOrderList.length;j++){				
				var dailyDetailData = '<tr>' +
										'<td style="width:4%"><span class="flightIn30days"></span></td>' +
										'<td style="width:8%"><p title="'+data[i].salesOrderList[j].salesOrderId+'">'+data[i].salesOrderList[j].salesOrderId  +'</p></td>' +    
										'<td style="width:14%"><p title="'+data[i].salesOrderList[j].advertiserName+'">'+data[i].salesOrderList[j].advertiserName  +'</p></td>'+   
										'<td style="width:7%"><p title="'+data[i].salesOrderList[j].sor+'">'+data[i].salesOrderList[j].sor  +'%</p></td>'+   			
										'<td style="width:14%"><p title="'+data[i].salesOrderList[j].status+'">'+data[i].salesOrderList[j].status  +'</p></td>'+  			
										'<td style="width:14%"><p title="'+data[i].salesOrderList[j].accountManager+'">'+data[i].salesOrderList[j].accountManager  +'</p></td>'+   
										'<td style="width:20%"><p title="'+data[i].salesOrderList[j].productName+'">'+ data[i].salesOrderList[j].productName +'</p></td>'+ 
										'<td style="width:19%"><p title="'+data[i].salesOrderList[j].salesCategoryName+'">'+ data[i].salesOrderList[j].salesCategoryName +'</p></td>'+ 
									'</tr>';
				viewToDisplay = viewToDisplay + dailyDetailData;
			}
		}
		
		if(data[i].proposalList){
			for(var j=0;j<data[i].proposalList.length;j++){
				var highlightStyle;
				if(data[i].proposalList[j].daysToExpire <= 1){
					highlightStyle = "color:red;";
				}
				else{
					highlightStyle = "";
				}
				if(data[i].proposalList[j].daysLeftInFlight <= 30){
					vulnerableLI = "color:red;display:block";
				}
				else{
					vulnerableLI = "display:none";
				}
				var dailyDetailData = '<tr>' +
										'<td style="width:4%"><span class="flightIn30days" style="' + vulnerableLI + '">Vulnerable</span></td>' +
										'<td style="width:8%"><p title="'+data[i].proposalList[j].proposalId+'">'+data[i].proposalList[j].proposalId  +'</p></td>' +  
										'<td style="width:14%"><p title="'+data[i].proposalList[j].advertiserName+'">'+data[i].proposalList[j].advertiserName  +'</p></td>'+ 
										'<td style="width:7%"><p title="'+data[i].proposalList[j].sor+'">'+data[i].proposalList[j].sor  +'%</p></td>'+ 
										'<td style="width:14%"><p style="' + highlightStyle + '" title="'+data[i].proposalList[j].status+'">' + data[i].proposalList[j].status  +'</p></td>'+ 
										'<td style="width:14%"><p title="'+data[i].proposalList[j].accountManager+'">'+data[i].proposalList[j].accountManager  +'</p></td>'+ 
										'<td style="width:20%"><p title="'+data[i].proposalList[j].productName+'">'+ data[i].proposalList[j].productName +'</p></td>'+ 
										'<td style="width:19%"><p title="'+data[i].proposalList[j].salesCategoryName+'">'+ data[i].proposalList[j].salesCategoryName +'</p></td>'+ 
									'</tr>';
				viewToDisplay = viewToDisplay + dailyDetailData;
			}
		}
		
		viewToDisplay = viewToDisplay + '</table></div></div>';
		counter = counter + 1;
	}
	viewToDisplay = viewToDisplay + '</div>';	
	document.getElementById('completeCalendar').innerHTML = viewToDisplay;
	var res = $("#monthName","#monthDetail").text().split(" "); 
	if(globalMonthArr.indexOf(res[0]) == new Date().getMonth() && res[1] == new Date().getFullYear()){
		$('#previousMonth').css({'opacity' : 0.7,'cursor':'default'});
	}
	else{
		$('#previousMonth').css({'opacity' : 1,'cursor':'pointer'});
	}
}

/**
 * To check for new month while display  
 */
function isNewMonth(oldDate, newDate){
	if(oldDate != ""){
		var oldMonth = $.trim(oldDate.split(",")[1]);
		oldMonth = oldMonth.split(" ")[0];
		var newMonth = $.trim(newDate.split(",")[1]);
		newMonth = newMonth.split(" ")[0];
		return oldMonth != newMonth ;
	} else{
		return true;
	}
}
/**
 * To check for new week while display
 */
function isNewWeek(oldDate, newDate){
	if(oldDate !=""){
		var newDay = $.trim(newDate.split(",")[0]);
		newDay = newDay.split(" ")[0];
		return newDay=="Monday"?true : false;
	}else{
		return true;
	}
}
var calendarData;
/**
 * To navigate between quaterly calendar data from backend
 */
function getCalendarData(){
	var instanceFormManager = new FormManager();
	instanceFormManager.formName = 'salesCalendarForm';
	var getCalendarDataForm = new JQueryAjaxForm(instanceFormManager);
	getCalendarDataForm.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
		document.getElementById('monthDetail').style.display = "none";
		document.getElementById('completeCalendar').innerHTML = "";
	};
	getCalendarDataForm.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
		document.getElementById('monthDetail').style.display = "block";
		calendarData = jsonResponse;
		displayWeeklyData(jsonResponse);
	};
	getCalendarDataForm.submit();
}

/**
 * To navigate in calendar for next or previous month  
 */
function getDataByNavigation(howManyMonths){
	var currentDateDisplayed = $("#monthName","#monthDetail").text();
	var res = currentDateDisplayed.split(" "); 
	var monthNo = globalMonthArr.indexOf(res[0]);
	if(howManyMonths < 0 && monthNo <= new Date().getMonth() && res[1] == new Date().getFullYear()){
		return;
	}
	var yearToDisplay = parseInt(res[1]);
	var monthToDisplay = parseInt(monthNo) + parseInt(howManyMonths);
	if(monthToDisplay > 11){
		monthToDisplay = monthToDisplay - 12;
		yearToDisplay = yearToDisplay + 1;
	}else if(monthToDisplay < 0){
		monthToDisplay = monthToDisplay + 12;
		yearToDisplay = yearToDisplay - 1;
	}
	var dateTosend = globalMonthArr[monthToDisplay] + " " + yearToDisplay;
	$("#dateMultiSelect" , "#salesCalendarForm").val(dateTosend).trigger("change");
	$("#dateString","#salesCalendarForm").val(dateTosend);
	$("#filterCalendarMultiSelect").multiselect("uncheckAll");
	getCalendarData();	
}

/**
 * To expand or collapse indivisual rows of week  
 */
function toggleExpansion(self){
	$(self.parentElement.parentNode.parentElement.parentElement.getElementsByClassName('reservationDetail')).toggle();
	var labelNode = $(self.parentNode).find('#toggleExpansion');
	if($(labelNode).html()=='[ + ]')
		$(labelNode).html('[ - ]');
	else
		$(labelNode).html('[ + ]');
};
/**
 * To toggle between all the available dates for reservation  
 */
function showAvailableDates(){
	var rowSummary = $("span[id^='sorValue']");
	if(rowSummary.length == 0){
		return;
	}
	calendarData.objectMap.gridKeyColumnValue.forEach(function(value,i){
		$(rowSummary[i].parentElement.parentElement.parentElement.getElementsByClassName('reservationDetail')).hide();
		var labelNode = $(rowSummary[i].parentNode).find('#toggleExpansion');
		$(labelNode).html('[ + ]');
	});
	if(filteredData && filteredData.length>0){
		filteredData.forEach(function(value,i){
			var index = $.inArray(value, tempCalendarData) ;
			$(rowSummary[index].parentElement.parentElement.parentElement.getElementsByClassName('reservationDetail')).show();
			var labelNode = $(rowSummary[index].parentNode).find('#toggleExpansion');
			$(labelNode).html('[ - ]');
		});
	}
};

/**
 * Initialised resource bundle and store in java script
 */
function initalizeResourceBundleForSalesCalendar(){
	var ajaxReq = new AjaxRequest();
	ajaxReq.url = "../homepage/initResourceBundle.action";
	ajaxReq.success = function(result, status, xhr) {
		/**
		 * Setting a Global array variable named "resourceBunldle" to access
		 * constants which is defined in messages.properties 
		 */
		resourceBundle = result;
	};
	ajaxReq.submit();
};

function changeProductType(type){
	$('option', $("#productMultiSelect","#salesCalendarForm")).remove();
	$(".productRadioButton").find("i").removeClass("active-line-item");
	$("#productType_"+ type).siblings("i").addClass("active-line-item");
	var optString = "";		
	$.each($('#allProducts optgroup'), function(i, optgroup){
		$.each($(optgroup).find(' option[data-type=\"'+type +'"]') , function(i, option){			
			optString = optString + '<option value="' + $(option).attr('value') + '" >' + $(option).text() + '</option>';
		});
	});
	$("#productMultiSelect","#salesCalendarForm").append(optString);
};

/**
 *Return list of Proposed Line Items 
 * @param {Object} forDate
 */
function getListOfProposedLineItems(forDate){
 	var countriesString = $("#countriesMultiSelect" , "#salesCalendarForm").val() == null ? "" : $("#countriesMultiSelect" , "#salesCalendarForm").val().toString();
 	var productString = $("#productMultiSelect" , "#salesCalendarForm").val() == null ? "" : $("#productMultiSelect" , "#salesCalendarForm").val().toString();
 	if(productString == "" ){
 		jQuery("#runtimeDialogDiv").model({theme: 'Error', message: 'Product is mandatory'});
 	}else{
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = "../reservations/getProposedLineItems.action";
		ajaxReq.type = 'POST';
		ajaxReq.data = { "dateString": decodeURIComponent(forDate) , "countries": countriesString, "productIds": productString };
	    ajaxReq.cache = false;
		ajaxReq.success = function(result, status, xhr) {
			var proposedLineItems = [];
			for(var i=0; i<result.length; i++){
				for(var j=0;j<result[i].lineItems.length;j++){//Each iteration of a loop represents one row in a new Modal Dialogue				
					proposedLineItems.push({'applicationId':result[i].proposalId,'advertiser':result[i].advertiserName,'SOV':result[i].lineItems[j].sor,'sales':result[i].accountManager,'configuration':result[i].lineItems[j].productName,'category':result[i].campaignName});
				}
			}
			if(proposedLineItems.length > 0){
				displayProposedLineItems(proposedLineItems);
			}
		};
		ajaxReq.submit();
	}
}

/**
 * Modal Dialog to display proposed line items
 * @param [array] data to render in dialog
 */
function displayProposedLineItems(data){
	var proposedLineItemHTML = '';
	var proposedLineItemHeader = '<div id="proposedLineItemCalendarHeader"><table><tr id="calendarHeader">' + 
			'<td><label class="label-bold">Application ID</label></td>' +
			'<td><label class="label-bold">Advertiser</label></td>' +
			'<td><label class="label-bold">SOV (%)</label></td>' + 
			'<td><label class="label-bold">Sales</label></td>'+ 
			'<td style="width:25%;"><label class="label-bold">Configuration</label></td>' +
			'<td style="width:25%;"><label class="label-bold">Category</label></td>' +
			'</tr></div>';

	proposedLineItemHTML = proposedLineItemHTML + proposedLineItemHeader ;
	var rowData = '';
	data.forEach(function(value,i){
		rowData = rowData + '<tr style="line-height: 2;"><td style="text-align:right"><p class="proposalCalendarDetails" title="'+value.applicationId+'">'+value.applicationId+'</p></td>'+
							'<td style="text-align:left"><p class="proposalCalendarDetails" title="'+value.advertiser+'">'+value.advertiser+'</p></td>'+
							'<td style="text-align:right"><p class="proposalCalendarDetails" title="'+value.SOV+'">'+value.SOV + '</p></td>'+
							'<td style="text-align:left"><p class="proposalCalendarDetails" title="'+value.sales+'">'+value.sales+'</p></td>'+
							'<td style="text-align:left"><p class="proposalCalendarDetails" title="'+value.configuration+'">'+value.configuration+'</p></td>'+
							'<td style="text-align:left"><p class="proposalCalendarDetails" title="'+value.category+'">'+value.category+'</tr>';
	});
	proposedLineItemHTML = proposedLineItemHTML + rowData + '</table>';
	jQuery('#proposed-lineItems').html(proposedLineItemHTML); 
	var proposedLineItemsDialog = new ModalDialog();
	proposedLineItemsDialog.height = 250;
	proposedLineItemsDialog.width = '80%';
	proposedLineItemsDialog.draggable = false;
	proposedLineItemsDialog.resizable = false;
	proposedLineItemsDialog.buttons = [];
	jQuery('#proposed-lineItems').dialog(proposedLineItemsDialog);
	jQuery('#proposed-lineItems').dialog("open");
}

var filteredData = [];
var tempCalendarData=[];
/**
 * Filters for Calendar Screen
 * @param String option selected to display filtered data
 */
function filterCalendar(){
	var appliedFilters = $("#filterCalendarMultiSelect").val();
	tempCalendarData = $.extend(true,{}, calendarData.objectMap).gridKeyColumnValue; 
	if(appliedFilters==null || appliedFilters.length == 0){
		filteredData = tempCalendarData; 
	}
	else{
		var calendarToDisplayArr=[];
		var calendarToDisplay = {};
		filteredData = [];
		tempCalendarData.forEach(function(value,i){
			if($.inArray('Sold', appliedFilters) > -1){
				if(value.salesOrderList.length>0){
					if($.inArray(value, filteredData) < 0){
						filteredData.push(value);
					}
				}
			}
			if($.inArray('Hold', appliedFilters) > -1){
				if(value.proposalList.length>0){
					if($.inArray(value, filteredData) < 0){
						filteredData.push(value);
					}
				}
			}
			if($.inArray('AvailableDates', appliedFilters) > -1){
				if(value.totalSOR<100){
					if($.inArray(value, filteredData) < 0){
						filteredData.push(value);
					}
				}
			}
			if($.inArray('Vulnerable', appliedFilters) > -1){
				value.proposalList.forEach(function(proposal,k){
					if(proposal.daysLeftInFlight<30){
						if($.inArray(value, filteredData) < 0){
							filteredData.push(value);
						}
					}
				});
			}
			if($.inArray('Overbooked', appliedFilters) > -1){
				if(value.totalSOR>100){
					if($.inArray(value, filteredData) < 0){
						filteredData.push(value);
					}
				}
			}
		});
	}
	showAvailableDates();	
};