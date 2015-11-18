/**
 * JavaScript for fetch avails screen
 *
 * @author manish.kesarwani
 * @version 2.0
 */

var dailyDetails;

jQuery(document).ready(function() {
	
	$("#fetchAvailsSOSProductId", "#manageFetchAvails").select2();
	
	$("#fetchAvailspriceType", "#manageFetchAvails").select2();
	
	 $("input[class=numeric]", "#manageFetchAvails").numeric({
	        negative: false
	    });
	
	$("#fetchAvailsSOSSalesTargetId", "#manageFetchAvails").multiselect({
		selectedList: 2,
		click: function(event, ui) {
							
        }
	}).multiselectfilter();
	
	$("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").multiselect({
		selectedList: 2,
	}).multiselectfilter();
		
	$("#fetchAvailsSOSProductId", "#manageFetchAvails").change(function(){
		$("#productAvailType","#manageFetchAvails").val('S');
		if ($("#availSystemType_d", "#manageFetchAvails").is(':checked') || $("#availSystemType_b", "#manageFetchAvails").is(':checked')) {	 
			checkProductIsReservable($("#fetchAvailsSOSProductId", "#manageFetchAvails").val());
		}
	 	setManageAvailsSalesTargetByProdId($("#fetchAvailsSOSProductId", "#manageFetchAvails").val());
		
	});  
	
	 var datesProposalLineItem = jQuery('#fetchAvailsStartDate, #fetchAvailsEndDate', "#manageFetchAvails").datepicker({
        autoSize: true,
        showOn: "both",
        buttonText: "Select Date",
        buttonImage: '../images/calendar.gif',
        buttonImageOnly: true,
        numberOfMonths: 3,
        onSelect: function(selectedDate){			
        	date = $.datepicker.parseDate($(this).data("datepicker").settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, $(this).data("datepicker").settings);
        }
    });
	var today = new Date();
	$("#fetchAvailsStartDate").datepicker("setDate", new Date(today.getFullYear(), today.getMonth() + 1, 1));
	$("#fetchAvailsEndDate").datepicker("setDate", new Date(today.getFullYear(), today.getMonth() + 1, getNumberOfDaysInNextMonth()));
	
	$("#startFromReset", "#manageFetchAvails").bind("click", function(event, ui){
        $("#fetchAvailsStartDate", "#manageFetchAvails").val("");
    });
	
    $("#endFromReset", "#manageFetchAvails").bind("click", function(event, ui){
        $("#fetchAvailsEndDate", "#manageFetchAvails").val("");
    });
	
	 $("#fetchAvailsSOSTarTypeId", "#manageFetchAvails").change(function(){
        fillTargetTypeElementForFetchAvails();
    });
	
	/**
     * Call check Avails Validation of availsCheck  
     */
    $('#manageFetchAvailsReset', "#manageFetchAvails").click(function(){
    	$("#availSystemType_r").attr('checked', 'checked');
    	$('#fetchAvailsHeaderDiv', "#manageFetchAvails").html('');
    	changeAvailSystemType('R', true);
    	resetFetchAvailsData();
    });
	
	/**
     * Call check Avails Validation of availsCheck  
     */
    $('#manageFetchAvailsCheck', "#manageFetchAvails").click(function(){
        if (validateManageFetchAvailsTargeting()) {
            populateManageAvailsFromYieldex();
        }
    });
	
	 /**
     * Add dialog box for loading msg
     */ 
    $("#manageFetchAvailsProgress").dialog({
        dialogClass: 'alert',
        title: 'Fetching Avails...',
        autoOpen: false,
        resizable: false,
        showTitlebar: true,
        modal: true,
        height: 100,
        width: 225
    });
    
    $("#availSystemType_r").attr('checked', 'checked');
    changeAvailSystemType('R', true);
});

function resetFetchAvailsData(){
	 var today = new Date();
	 clearCSSErrors("#manageFetchAvailsForm", "#manageFetchAvails");
 	 jQuery("#fetchAvailsSOSProductId", "#manageFetchAvails").select2("val", "").change();
	 jQuery('#fetchAvailsStartDate, #fetchAvailsEndDate', "#manageFetchAvails").val("");
		
	 $("#fetchAvailsStartDate").datepicker("setDate", new Date(today.getFullYear(), today.getMonth() + 1, 1));
	 $("#fetchAvailsEndDate").datepicker("setDate", new Date(today.getFullYear(), today.getMonth() + 1, getNumberOfDaysInNextMonth()));
		
	 $.each($("#fetchAvailsGeoTargetsummary > tbody > tr", "#manageFetchAvails"), function(i, row) {
		if("geoTargetsummaryHeader" != $(row).attr("id")) {
			$(row).remove();
		}
	 }); 
	 resetTargetingFieldsManageFetchAvails();
	 $('#viewRowId', "#manageFetchAvails").hide();
	 $('#searchAvailsList', "#manageFetchAvails").hide();
	 $('#yieldexURLTB', "#manageFetchAvails").hide();
	 $('#dfpDetails', "#manageFetchAvails").hide();
}

function getNumberOfDaysInNextMonth() {
	var date = new Date();
	var month = date.getMonth() + 2;
	if (month === 2) {
	      if (new Date(date.getFullYear(), 1, 29 ).getMonth() === 1) {
	            daysInMonth = 29;
	      } else {
	      		daysInMonth = 28;
	      }
	} else if (month === 4 || month === 6 ||
	      month === 9 || month === 11) {
	      daysInMonth = 30;
	} else {
	      daysInMonth = 31;
	}
	return daysInMonth;
}

function populateManageAvailsFromYieldex(){
	 $('#viewRowId', "#manageFetchAvails").hide();
	 $('#searchAvailsList', "#manageFetchAvails").hide();
	 $('#yieldexURLTB', "#manageFetchAvails").hide();
	 $('#dfpDetails', "#manageFetchAvails").hide();
	 $('#consolidatedDetails', "#manageFetchAvails").hide();
	$('#yieldexURL', "#manageFetchAvails").html("");
   	$('#fetchAvailsHeaderDiv', "#manageFetchAvails").html('');
    $("#manageFetchAvailsProgress").dialog("open");
	addManageFetchingAvailsText();
    
    var fetchAvailsForm = new FormManager();
    fetchAvailsForm.formName = 'manageFetchAvailsForm';
    var populateAvailsFrom = new JQueryAjaxForm(fetchAvailsForm);
    $("#manageFetchAvailsForm", "#manageFetchAvails").attr("action", "../avails/fetchAvailsFromYieldex.action?manageAvailsOption=Y");
   
    populateAvailsFrom.doCustomProcessingAfterFormSucsesJson = function(jsonResponse, XMLHTTPRequest){
    	var yieldexError = jsonResponse.objectMap.yieldexError;
    	if($("#availType","#manageFetchAvails").val() != 'D'){
    		$('#dfpDetails', "#manageFetchAvails").hide();
			if (yieldexError != "") {
				$('#fetchAvailsHeaderDiv', "#manageFetchAvails").html(resourceBundle['error.generic.yieldex.error'] + yieldexError).addClass('error');
				$('#fetchAvailsHeaderDiv', "#manageFetchAvails").show();
				$('#viewRowId', "#manageFetchAvails").hide();
				$('#searchAvailsList', "#manageFetchAvails").hide();
			} else {
				$('#fetchAvailsHeaderDiv', "#manageFetchAvails").html(yieldexError).removeClass('error');
				$('#fetchAvailsHeaderDiv', "#manageFetchAvails").hide();
							
				dailyDetails = jsonResponse.objectMap.dailyDetails;
				if($("#availType","#manageFetchAvails").val() == 'R'){
					$('#viewRowId', "#manageFetchAvails").show();
				}
				$('#searchAvailsList', "#manageFetchAvails").show();
				$('input:radio[id=radio_summary]', "#manageFetchAvails").attr('checked', true);			
				if($("#availType","#manageFetchAvails").val() == 'B'){
					getConsolidatedView(jsonResponse.objectMap.dfpResponse);
				}
				getSummaryView();
			}
			var url = jsonResponse.objectMap.yieldexURL;
			$('#yieldexURL', "#manageFetchAvails").html("<a href=" + encodeURI(url) + " target='_blank' class='yieldex-url'>" + url + "</a>");
			$('#yieldexURLTB', "#manageFetchAvails").show();
    	}
    	if(yieldexError == "" && ($("#availType","#manageFetchAvails").val() == 'D' || $("#availType","#manageFetchAvails").val() == 'B')){
    		var dfpDetails = jsonResponse.objectMap.dfpResponse;
    		if(dfpDetails.available != ""){
    			fillDfpDetails(dfpDetails);    			
    		}
    	}
    	 $("#manageFetchAvailsProgress").dialog("close");
    };
	
    populateAvailsFrom.doCustomProcessingAfterValidationFailedJson = function(jsonResponse, XMLHTTPRequest){
    	$("#manageFetchAvailsProgress").dialog("close");
	    var errorArray = jsonResponse.errorList;
        jQuery.each(errorArray, function(i, error){
			if (error.field == "fetchAvailsStartDate" || error.field == "fetchAvailsStartDate" || error.field == "fetchAvailsSOSSalesTargetId_custom" || error.field == "fetchAvailsSOSProductId") {
                jQuery("#" + error.field, "#" + fetchAvailsForm.formName).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            }
        });
        $('.errortip', "#" + fetchAvailsForm.formName).qtip({
            style: {
                classes: 'ui-tooltip-red'
            },
            position: {
                my: 'bottom center',
                at: 'top center'
            }
        });
    };
	
    populateAvailsFrom.error = function(XMLHTTPRequest, textStatus, errorThrown){
		$("#manageFetchAvailsProgress").dialog("close");
        handleManageYieldexAvailsExceptionOnServer(XMLHTTPRequest);
    };
    getManageDataForTargetingToSave();
    populateAvailsFrom.submit();
}

function getConsolidatedView(dfpDetailsObj){
	$('#consolidatedDetails', "#manageFetchAvails").show();
	$("#consolidatedAvailsList", "#manageFetchAvails").children().remove();
	$("#consolidatedAvailsList", "#manageFetchAvails").append('<tr><td colspan="4" class="normalHead"><strong style="margin-top: 3px;display: inline-block;">Consolidated Search Result</strong></td></tr>');
	$("#consolidatedAvailsList", "#manageFetchAvails").append('<tr><th class="label-bold">Available</th><th class="label-bold">Capacity</th><th class="label-bold">Start Date</th><th class="label-bold">End Date</th></tr>');
	var consolidatedAvailability = 0;
	var consolidatedcapacity = 0;
	for (var i=0;i<dailyDetails.length;i++) { 
		consolidatedAvailability = consolidatedAvailability + parseInt(dailyDetails[i].available);
		consolidatedcapacity = consolidatedcapacity + parseInt(dailyDetails[i].capacity);
 	}
	consolidatedAvailability = consolidatedAvailability + parseInt(dfpDetailsObj.available);
	consolidatedcapacity = consolidatedcapacity + parseInt(dfpDetailsObj.capacity);
	var rowString = rowString + "<tr>";
	rowString = rowString + "<td style='text-align: right'>" + formatNumber(consolidatedAvailability) + "</td>";
	rowString = rowString + "<td style='text-align: right'>" + formatNumber(consolidatedcapacity) + "</td>";
	rowString = rowString + "<td>" + dfpDetailsObj.startDate + "</td>";
	rowString = rowString + "<td>" + dfpDetailsObj.endDate + "</td>";
	rowString = rowString + "</tr>";
	$('#consolidatedAvailsList', "#manageFetchAvails").append(rowString);
}

function fillDfpDetails(dfpDetailsObj){
	$('#dfpDetails', "#manageFetchAvails").show();
	$("#searchDfpAvailsList", "#manageFetchAvails").children().remove();
	$("#searchDfpAvailsList", "#manageFetchAvails").append('<tr><td colspan="4" class="normalHead"><strong style="margin-top: 3px;display: inline-block;">Search Result of Dfp</strong></td></tr>');
	$("#searchDfpAvailsList", "#manageFetchAvails").append('<tr><th class="label-bold">Available</th><th class="label-bold">Capacity</th><th class="label-bold">Start Date</th><th class="label-bold">End Date</th></tr>');

	var rowString = rowString + "<tr>";
	rowString = rowString + "<td style='text-align: right'>" + formatNumber(dfpDetailsObj.available) + "</td>";
	rowString = rowString + "<td style='text-align: right'>" + formatNumber(dfpDetailsObj.capacity) + "</td>";
	rowString = rowString + "<td>" + dfpDetailsObj.startDate + "</td>";
	rowString = rowString + "<td>" + dfpDetailsObj.endDate + "</td>";
	rowString = rowString + "</tr>";
	$('#searchDfpAvailsList', "#manageFetchAvails").append(rowString);
	$('#dfpInputJson', "#manageFetchAvails").empty();
	var sjonObj = jQuery.parseJSON(dfpDetailsObj.inputJson);
	$.each(sjonObj, function(key, value) {
		if (value == "" || value == null) {
			delete sjonObj[key];
		}
	});
	$('#dfpInputJson', "#manageFetchAvails").append(JSON.stringify(sjonObj).replace(/"/g, "").replace(/,/g, ", "));
}
/*
 * Function to represent the avails in multiple views
 */
function generateView() {
   var val = $('input:radio[name=availsView]:checked', "#manageFetchAvails").val();
   if(val=="week") {
   		getWeekViewDayBased();
   }else if(val=="summary") {
   		getSummaryView();
   }else if(val=="day") {
   		getDayView();
   }else if(val=="month") {
   		getMonthView();
   }
}

/*
 * This method is used to generate summary view
 */
function getSummaryView() {
	var dataRowList = new Array();
	var rowData = new Object();
	var availability=0;
	var capacity=0;
	var startDate;
	var endDate;
	
	for (var i=0;i<dailyDetails.length;i++) { 
		if(i==0) {
			startDate=dailyDetails[i].startDate;
		}
		availability = availability + parseInt(dailyDetails[i].available);
		capacity = capacity + parseInt(dailyDetails[i].capacity);
		endDate = dailyDetails[i].endDate;
 	}
		
	rowData.available = availability;
	rowData.capacity = capacity;
	rowData.startDate = startDate;
	rowData.endDate = endDate;
	
	dataRowList[0] = rowData;
	appendRow(dataRowList);	
}

/*
 * This method is used to generate weekly view
 */
function getWeekView() {
	var dataRowList = new Array();
	var rowData;
	
	var availability=0;
	var capacity=0;
	var startDate;
	var endDate;
	
	var i,j,weekArray,chunk = 7;
	var l = 0;	
	for (var i=0,j=dailyDetails.length; i<j; i+=chunk) { 
		weekArray = dailyDetails.slice(i,i+chunk);
		rowData = new Object();
		for(var k=0; k<weekArray.length; k++) {
			if(k==0) {
				startDate = weekArray[k].startDate;
			}
			availability = availability + parseInt(weekArray[k].available);
			capacity = capacity + parseInt(weekArray[k].capacity);
			endDate = weekArray[k].endDate;
		}
		rowData.available = availability;
		rowData.capacity = capacity;
		rowData.startDate=startDate;
		rowData.endDate=endDate;
				
		availability = 0;
		capacity = 0;
		
		dataRowList[l]=rowData;
		l++;
 	}
	appendRow(dataRowList);	
}

/*
 * This method is used to generate daily view
 */
function getDayView() {
	var dataRowList = new Array();
	var rowData;
	
	for(var i=0;i<dailyDetails.length; i++) {
		rowData = new Object();
		rowData.available = dailyDetails[i].available;
		rowData.capacity = dailyDetails[i].capacity;
		rowData.startDate=dailyDetails[i].startDate;
		rowData.endDate=dailyDetails[i].endDate;
		dataRowList[i] = rowData;
 	}
	appendRow(dataRowList);	
}



/*
 * This method is used to generate month view
 */
function getMonthView() {
	var dataRowList = new Array();
	var rowData;
	var availability=0;
	var capacity=0;
	var endDate;
	var startDate;
	var monthCount = -1;
	var k=0;
	for (var i=0;i<dailyDetails.length;i++) { 
		var returnDate = new Date(dailyDetails[i].startDate);
		var returnMonth = returnDate.getMonth();
		if(i==0) {
			monthCount = returnMonth;
			startDate = dailyDetails[i].startDate;
		}
		if(monthCount==returnMonth) {
			availability = availability + parseInt(dailyDetails[i].available);
			capacity = capacity + parseInt(dailyDetails[i].capacity);
			endDate = dailyDetails[i].endDate;
		}else {
			rowData = new Object();
			monthCount=returnMonth;
			rowData.available = availability;
			rowData.capacity = capacity;
			rowData.startDate = startDate;
			rowData.endDate = endDate;
			dataRowList[k++] = rowData;
			availability = 0;
			capacity = 0;
			startDate = dailyDetails[i].startDate;
			availability = availability + parseInt(dailyDetails[i].available);
			capacity = capacity + parseInt(dailyDetails[i].capacity);
			endDate = dailyDetails[i].endDate;
		}
 	} 
	
	//Adding last month data
	rowData = new Object();
	rowData.available = availability;
	rowData.capacity = capacity;
	rowData.startDate = startDate;
	rowData.endDate = endDate;
	dataRowList[k] = rowData;
	
	appendRow(dataRowList);	
}


/*
 * This method is used to generate week view from Monday - Sunday
 */
function getWeekViewDayBased() {
	var dataRowList = new Array();
	var rowData;
	var availability=0;
	var capacity=0;
	var endDate;
	var startDate;
	var k=0;
	for (var i=0;i<dailyDetails.length;i++) { 
		var returnDate = new Date(dailyDetails[i].startDate);
		var returnDay = returnDate.getDay();
				
		if(i==0) {
			startDate = dailyDetails[i].startDate;
			endDate = dailyDetails[i].endDate;
		}
		if(returnDay!=1 || (i==0 && returnDay==1)) {
			availability = availability + parseInt(dailyDetails[i].available);
			capacity = capacity + parseInt(dailyDetails[i].capacity);
			endDate = dailyDetails[i].endDate;
		}else {
			rowData = new Object();
			rowData.available = availability;
			rowData.capacity = capacity;
			rowData.startDate = startDate;
			rowData.endDate = endDate;
			dataRowList[k++] = rowData;
			availability = 0;
			capacity = 0;
			startDate = dailyDetails[i].startDate;
			availability = availability + parseInt(dailyDetails[i].available);
			capacity = capacity + parseInt(dailyDetails[i].capacity);
			endDate = dailyDetails[i].endDate;
		}
 	} 
	
	//Adding last week data
	rowData = new Object();
	rowData.available = availability;
	rowData.capacity = capacity;
	rowData.startDate = startDate;
	rowData.endDate = endDate;
	dataRowList[k] = rowData;
	
	appendRow(dataRowList);	
}


/*
 * This method is used to append row in search Results
 */
function appendRow(dataRowList) {
	$("#searchAvailsList", "#manageFetchAvails").children().remove();
	$("#searchAvailsList", "#manageFetchAvails").append('<tr><td colspan="4" class="normalHead"><strong style="margin-top: 3px;display: inline-block;">Search Result for Adx</strong></td></tr>');
	$("#searchAvailsList", "#manageFetchAvails").append('<tr><th class="label-bold">Available</th><th class="label-bold">Capacity</th><th class="label-bold">Start Date</th><th class="label-bold">End Date</th></tr>');
	
	var rowString = "";
	for(var i=0; i<dataRowList.length; i++) {
		var rowData = dataRowList[i];
		rowString = rowString + "<tr>";
		rowString = rowString + "<td style='text-align: right'>" + formatNumber(rowData.available) + "</td>";
		rowString = rowString + "<td style='text-align: right'>" + formatNumber(rowData.capacity) + "</td>";
		rowString = rowString + "<td>" + rowData.startDate + "</td>";
		rowString = rowString + "<td>" + rowData.endDate + "</td>";
		rowString = rowString + "</tr>";
	}
	$("#searchAvailsList", "#manageFetchAvails").append(rowString);
}

function handleManageYieldexAvailsExceptionOnServer(XMLHTTPRequest){
	var errorObj = eval('(' + XMLHTTPRequest.responseText + ')').errorObject;
	jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, width: 370, height: 220, message: errorObj.errorMessage});
}

/**
 * Function creates a JsonString to pass to server
 */
function getManageDataForTargetingToSave(){
	var lineItemTargetings = new Array();
	var objId ="";
	var action = "";
	var not = "";
	var rowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").attr("id");
	
	$("#fetchAvailsTargetingData", "#manageFetchAvails").val("");
    $("#fetchAvailsGeoTargetsummary tr", "#manageFetchAvails").each(function(){
		objId = $(this).attr("id");
        if (objId != "geoTargetsummaryHeader") {
			 var object = new Object();
			 var level="";
			 if(rowId != objId){
				 action = $('#targetAction_' +objId + " option:selected", "#manageFetchAvails").val();
			 }else{
				 action = "";
			 }
			 object.action = action;
		     object.targetTypeId = $("#" + objId + " td:nth-child(3)", "#manageFetchAvails").html();
		     object.targetTypeElement = $("#" + objId + " td:nth-child(5)", "#manageFetchAvails").html();
		     if ($('#not_' +objId, "#manageFetchAvails").is(':checked')) {
					not = "Not";
		     }else{
		     	not = "";
		     }
		     
		     object.not = not;
		     if("Behavioral" == $("#" + objId + " td:nth-child(2)", "#manageFetchAvails").html()){
		    	 level = $('#revenueScienceSegId_' +objId + " option:selected", "#manageFetchAvails").val();
		     }
		     object.segmentLevel = level;
		     lineItemTargetings.push(object);
            $("#fetchAvailsTargetingData", "#manageFetchAvails").val(JSON.stringify(lineItemTargetings));
        }
    });
}



function addManageFetchingAvailsText(){
    $('#manageFetchAvailsProgress').empty('');
    var currentDate = $.datepicker.formatDate('mm/dd/yy', new Date());
    var selectedDate = $('#fetchAvailsStartDate', "#manageFetchAvails").val();
    if (Date.parse(selectedDate) >= Date.parse(currentDate)) {
        $('#manageFetchAvailsProgress').append('<p style="padding:8px;margin:10px 0 0"><span class="fetching-avails">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span>' + resourceBundle['label.fetching.avails'] + '</span></p>');
    } else if(selectedDate=='' || $('#fetchAvailsEndDate', "#manageFetchAvails").val()==''){
   	 $('#manageFetchAvailsProgress').empty('');
	 $("#manageFetchAvailsProgress").dialog("close");
    }else {
        $('#manageFetchAvailsProgress').append('<p style="padding:8px;margin:10px 0 0"><span class="warning-dialog">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span style="white-space:nowrap">' + resourceBundle['label.fetching.past.date.avails'] + '</span></p>');
    }
}

/**
 * Validates if the operation preceeding the Frequency Cap targeting is 'AND' operation or not.
 * @returns {Boolean} Returns true, if the preceeing operation is 'AND' else returns false.
 */
function validateManageFetchAvailsTargeting(){
	var returnFlag = true;
	var rowCount = $('#fetchAvailsGeoTargetsummary tr', "#manageFetchAvails").length;
	if(rowCount > 2){
		var lastRowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").attr("id");
		if("Frequency Cap" == $("#" + lastRowId + " td:nth-child(2)", "#manageFetchAvails").html()){
			var prevRowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").prev('tr').attr("id");
			if($('#targetAction_' +prevRowId, "#manageFetchAvails").val() == "or"){
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.operation.preceeding.frequencyCap.targeting']});
				returnFlag = false;
			}
		}
	}
	
	return returnFlag;
}



/**
 * Fill target type element
 */
function fillTargetTypeElementForFetchAvails(elementsId){
	var url = "";
	var tarTypeElem = "";
	var flag = false;
	clearCSSErrors("#fetchAvailsTargetingTable", "#manageFetchAvails");
	$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").hide();
	if ($('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").val() == "") {
		$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").show();
		$('#fetchAvailsSOSTarTypeElement >option', "#manageFetchAvails").remove();
		$("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").val('');
		$("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").multiselect("refresh");
	}else if("Zip Codes" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() || "Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()){
		$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").show().focus().val("");
		$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").hide();
	}else {
		$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").show();
		var ajaxReq = new AjaxRequest();
		ajaxReq.url = $("#contextPath").val() +"/proposalWorkflow/getTargetTypeElements.action?sosTarTypeId=" + $('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").val();
		ajaxReq.success = function(result, status, xhr){	
			var returnedId = result.objectMap.gridKeyColumnValue;
			var select = $("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails");
			$('option', select).remove();
			sortList(returnedId, select);
			if(elementsId != null){
				var selectedElements = elementsId.split(",");
				$(select).val(selectedElements);
			}
			$('#fetchAvailsSOSTarTypeElement', "#manageFetchAvails").animate({
				scrollTop: $('#fetchAvailsSOSTarTypeElement', "#manageFetchAvails").offset(0)
			}, 'fast');
			$(select).multiselect("refresh");
		};
		ajaxReq.submit();
	}
}


/**
 * Function for adding the new targeting's to the Target table.
 */
function addTargetsToManageFetchAvails(){
	if (validateAddTargetsManageFetchAvails()) {
		var valueToPrint = "";
		var selectedElements = [];
		var level = "--";
		var not= "--";
		var operation = "--";
		var previousRowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").attr("id");
		var rowBeforeLastRowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").prev('tr').attr("id");
		var rowName = "fetchAvailsGeoTargetsummary";	
		if($('#fetchAvailsGeoTargetsummary tr', "#manageFetchAvails").length == 1){
			var rowCount = 1;
		}else{
			var rowCount = Number(previousRowId.substring(rowName.length)) + 1;
			var prevRowCount = Number(rowBeforeLastRowId.substring(rowName.length)) + 1;
			if(prevRowCount > rowCount){
				rowCount = prevRowCount;
			}
		}
			
		var rowId = "fetchAvailsGeoTargetsummary" + rowCount;
			
		if("Frequency Cap" == $('#' + previousRowId + " td:nth-child(2)", "#manageFetchAvails").html()){
			operation = "<select id='targetAction_" + rowId + "'><option value='and'>And</option> <option value='or'>Or</option></select>";
			$('#targetAction_' +rowId, "#manageFetchAvails").val("and");
		}else{
			var prevRowOperation = "<select id='targetAction_" + previousRowId + "'><option value='and'>And</option> <option value='or'>Or</option></select>";
			$('#targetAction_' +previousRowId, "#manageFetchAvails").val("and");
			$('#' + previousRowId + " td:nth-child(7)", "#manageFetchAvails").html(prevRowOperation);
		}
			
		if ("Frequency Cap" != $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			not = "<input type='checkbox' id='not_" + rowId + "' >";	
		}
			
		if("Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()){
			var level = "<select id='revenueScienceSegId_" + rowId + "' size='1'><option value='LEVEL1'>1</option> <option value='LEVEL2'>2</option></select>"
			$('#revenueScienceSegId_' +rowId, "#manageFetchAvails").val("LEVEL1");
		}
			
		$("#fetchAvailsSOSTarTypeElement :selected", "#manageFetchAvails").each(function(){
			selectedElements.push($(this).text());
		});
			
		valueToPrint = "<tr id='" + rowId + "'><td width='12%'>" + not + "</td><td width='18%'>" + $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() + "</td><td style='display : none'>" + $("#fetchAvailsSOSTarTypeId", "#manageFetchAvails").val();
		if ("Zip Codes" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() || "Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			valueToPrint = valueToPrint + "</td><td width='50%' title='" + $("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val() + "' style=' white-space: nowrap;  overflow: hidden; text-overflow: ellipsis;'>" + $("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val() + "</td><td style='display : none'>" + $("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val() + "</td>";
		}else {
			valueToPrint = valueToPrint + "</td><td width='50%' title='" + selectedElements.join(", ") + "' style=' white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + selectedElements.join(", ") + "</td><td style='display : none'>" + $("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").val() + "</td>";
		}
			
		valueToPrint = valueToPrint + "<td width='10%'>" + level + "</td>" + "</td><td width='10%'>" +  operation +"</td>" + "<td width='10%'>" + "<a style='float:left;margin-left:3px' title='Edit' onClick=\"editFetchAvailsTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/></a> <a style='float:left;margin-left:3px' title='Delete' onClick=\"deleteFetchAvailsTarget('" + rowId + "')\"> <span class='ui-icon ui-icon-trash' style='cursor:pointer;'/></a></td></tr>";
			
		if("Frequency Cap" == $('#' + previousRowId + " td:nth-child(2)", "#manageFetchAvails").html()){
			$("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").before(valueToPrint);
		}else{
			$('#fetchAvailsGeoTargetsummary', "#manageFetchAvails").append(valueToPrint);
		}
			
		resetTargetingFieldsManageFetchAvails();
		
		if ($("#lineItemType_r", "#manageFetchAvails").is(':checked')) {
			$('#fetchAvailsGeoTargetsummary tbody tr', "#manageFetchAvails").each(function(){
				$('td:eq(0),td:eq(5),td:eq(6)', this).hide();
			});
		}
	}
}




/**
 * Function Edits the row of a Target Table
 */
function editFetchAvailsTarget(trId){
	$("#fetchAvailsTargetResetData", "#manageFetchAvails").val('CANCEL');
	$("#fetchAvailsSOSTarTypeId", "#manageFetchAvails").val($("#" + trId + " td:nth-child(3)", "#manageFetchAvails").html());
	$('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").attr('disabled', true);
	if ("Zip Codes" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() || "Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
		$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").show();
		$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").hide();
		$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val($("#" + trId + " td:nth-child(5)", "#manageFetchAvails").text());
	}
	else {
		$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").hide();
		$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").show();
		fillTargetTypeElementForFetchAvails($("#" + trId + " td:nth-child(5)", "#manageFetchAvails").html());
	}
	$("#addTargetToFetchAvails", "#manageFetchAvails").hide();
	$("#updateTargetToFetchAvails", "#manageFetchAvails").show();
	$("#updateTargetToFetchAvails", "#manageFetchAvails").attr("onclick","updateTargetsToFetchAvails('"+trId+"')")
}



/**
 * Function resets the Targetting fields
 */
function resetTargetingFieldsManageFetchAvails(){
	clearCSSErrors("#fetchAvailsTargetingTable", "#manageFetchAvails");
	$("#fetchAvailsSOSTarTypeId", "#manageFetchAvails").val("");
	$('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").attr('disabled', false);
	$('option', $("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails")).remove();
	$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").hide();
	$("#fetchAvailsSOSTarTypeElement_custom", "#manageFetchAvails").show();
	$("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val("");
	$("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").val('');
	$("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").multiselect("refresh");
	$("#addTargetToFetchAvails", "#manageFetchAvails").show();
	$("#updateTargetToFetchAvails", "#manageFetchAvails").hide();
	$("#fetchAvailsTargetResetData", "#manageFetchAvails").val('RESET');
	if($("#manageFetchAvails").find(".errortip").length > 0 ){
    	$('#messageHeaderDiv', '#manageFetchAvails').html(resourceBundle['validation.error']).addClass('error');
    }else if($("#manageFetchAvails").find(".errortip").length == 0){
    	$('#messageHeaderDiv', '#manageFetchAvails').html('');
    }
}

/**
 * Update the value in The Target Table based on rowId
 */
function updateTargetsToFetchAvails(trId){
	if (validateAddTargetsManageFetchAvails()) {
		var not = "--";
		var selectedElements = [];
		$('#' + trId + " td:nth-child(2)", "#manageFetchAvails").html($("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text());
		$('#' + trId + " td:nth-child(3)", "#manageFetchAvails").html($("#fetchAvailsSOSTarTypeId", "#manageFetchAvails").val());
	
		if ("Zip Codes" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() || "Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			$('#' + trId + " td:nth-child(4)", "#manageFetchAvails").attr("title", $("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val());
			$('#' + trId + " td:nth-child(4)", "#manageFetchAvails").html($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val());
			$('#' + trId + " td:nth-child(5)", "#manageFetchAvails").html($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val());
		}
		else {
			$("#fetchAvailsSOSTarTypeElement :selected", "#manageFetchAvails").each(function(){
				selectedElements.push($(this).text());
			});
			$('#' + trId + " td:nth-child(4)", "#manageFetchAvails").attr("title", selectedElements.join(", "));
			$('#' + trId + " td:nth-child(4)", "#manageFetchAvails").html(selectedElements.join(", "));
			$('#' + trId + " td:nth-child(5)", "#manageFetchAvails").html($("#fetchAvailsSOSTarTypeElement", "#manageFetchAvails").val().join(","));
		}
		resetTargetingFieldsManageFetchAvails();
	}
}


/**
 * Function dynamically delete rows to the Target Table
 */
function deleteFetchAvailsTarget(trId){
	var lastRowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").attr("id");
	if(trId == lastRowId){
		var rowId = $("#fetchAvailsGeoTargetsummary" , "#manageFetchAvails").find("tr:last").prev('tr').attr("id");
		$('#' + rowId + " td:nth-child(7)", "#manageFetchAvails").html('--');
	}
	var targetTypeText = $('#' + trId + " td:nth-child(2)", "#manageFetchAvails").html();
	$('#' + trId, "#manageFetchAvails").remove();
	resetTargetingFieldsManageFetchAvails();
}

/**
 * It validates the targeting details Entered by user
 */
function validateAddTargetsManageFetchAvails(){
     var returnFlag = true;
     clearCSSErrors("#fetchAvailsTargetingTable", "#manageFetchAvails");
    	if ($('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").val() == "") {
			$('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Target Type is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
			renderErrorQtip("#manageFetchAvails");
			returnFlag = false;
		}
		
		if ($('#fetchAvailsSOSTarTypeElement', "#manageFetchAvails").val() == null && "Zip Codes" != $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text() && "Behavioral" != $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			$('#fetchAvailsSOSTarTypeElement_custom', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
			renderErrorQtip("#manageFetchAvails");
			$('#fetchAvailsSOSTarTypeElement_custom', "#manageFetchAvails").focus();
			returnFlag = false;
		}
		
		if ("Zip Codes" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			if ($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val() == "") {
				$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
				renderErrorQtip("#manageFetchAvails");
				returnFlag = false;
			}
			else {
				var regexp = /^[0-9]+([\,\-\][0-9]+)?$/g;
				var result = regexp.test($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val());
				if (!result) {
					$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Only numbers and [ ,  - ] are allowed." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#manageFetchAvails");
					returnFlag = false;
				}
			}
			if($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val().length > 4000){
				$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Only 4000 characters are allowed." + '<BR><b>Help:</b> ' + "Please enter valid data in the field.");
				renderErrorQtip("#manageFetchAvails");
				returnFlag = false;
			}
		}
		
		if ("Behavioral" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			if ($.trim($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val()) == "") {
				$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
				renderErrorQtip("#manageFetchAvails");
				returnFlag = false;
			}
			else {
				var regexp = /^([A-Za-z0-9.$@;,\/ _%()-]+)?$/g;
				var result = regexp.test($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val());
				if (!result) {
					$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Only alphabets, numbers, spaces and [ . ; , / _ - @ $ % ( )] are allowed." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#manageFetchAvails");
					returnFlag = false;
				}
			}
			if($("#fetchAvailsTarTypeElementText", "#manageFetchAvails").val().length > 4000){
				$('#fetchAvailsTarTypeElementText', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Only 4000 characters are allowed." + '<BR><b>Help:</b> ' + "Please enter valid data in the field.");
				renderErrorQtip("#manageFetchAvails");
				returnFlag = false;
			}
		}
		
		if ("Frequency Cap" == $("#fetchAvailsSOSTarTypeId option:selected", "#manageFetchAvails").text()) {
			var lastRowId = $("#fetchAvailsGeoTargetsummary", "#manageFetchAvails").find("tr:last").attr("id");
			if ("Frequency Cap" == $('#' + lastRowId + " td:nth-child(2)", "#manageFetchAvails").html() && $('#fetchAvailsSOSTarTypeId', "#manageFetchAvails").is(':disabled') != true) {
				resetTargetingFieldsManageFetchAvails();
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: resourceBundle['error.adding.frequencyCap.targeting']});
				returnFlag = false;
			}
			else {
				var selectedElements = [];
				$("#fetchAvailsSOSTarTypeElement :selected", "#manageFetchAvails").each(function(){
					selectedElements.push($(this).text());
				});
				
				if (selectedElements.length > 1) {
					$('#fetchAvailsSOSTarTypeElement_custom', "#manageFetchAvails").addClass("errorElement errortip").attr("title", "Multiple elements cannot be selected for the Frequency Cap Targeting." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
					renderErrorQtip("#manageFetchAvails");
					$('#fetchAvailsSOSTarTypeElement_custom', "#manageFetchAvails").focus();
					returnFlag = false;
				}
			}
		}
		
		if(!returnFlag){
			$('#messageHeaderDiv', '#manageFetchAvails').html(resourceBundle['validation.error']).addClass('error');
		}else{
			$('#messageHeaderDiv', '#manageFetchAvails').html('');
		}
		
		return returnFlag;
}

/**
 * Fetch sales target from selected product
 * @param {Object} selectedProduct
 */
function setManageAvailsSalesTargetByProdId(selectedProduct) {
    var salesTarget = $("#fetchAvailsSOSSalesTargetId", "#manageFetchAvails");
	
	var ajaxReq = new AjaxRequest();
    ajaxReq.url = "../managepackage/getSalesTargetForProduct.action?productID=" + selectedProduct;
    ajaxReq.cache = false;
    ajaxReq.success = function(result, status, xhr){
		var salesTargetValues = result.objectMap.gridKeyColumnValue;
        $("option", salesTarget).remove();
        sortList(salesTargetValues, salesTarget);
		$("#fetchAvailsSOSSalesTargetId", "#manageFetchAvails").multiselect("refresh");
    };
    ajaxReq.submit();
}

function changeAvailSystemType(type, isAvailSystemTypeChange){
	/**
	 * Code to add selection arrow on selected avail system type 
	 */
	if(isAvailSystemTypeChange && $("#availType","#manageFetchAvails").val() != type ){
		resetFetchAvailsData();
		$('option', $("#fetchAvailsSOSProductId","#fetchAvailsfieldsContainer")).remove();
		var optString = "<option value=''></option>";		
		$.each($('#allFetchAvailsProducts optgroup'), function(i, optgroup){
			optString = optString + '<optgroup  label="' + $(optgroup).attr('label') +'">';		
			$.each($(optgroup).find(' option[data-type=\"'+type +'"]') , function(i, option){			
				optString = optString + '<option value="' + $(option).attr('value') + '" >' + $(option).text() + '</option>';
			});
			optString = optString + '</optgroup>';
		});
		$("#fetchAvailsSOSProductId","#fetchAvailsfieldsContainer").append(optString);	
	}
	$("#availType","#manageFetchAvails").val(type);
	if(type == 'D' || type== 'B'){
		$("#dfpRow","#manageFetchAvails").show();
		jQuery("#fetchAvailspriceType", "#manageFetchAvails").select2("val", "CPM").change();
		$("#infoIconFetchAvails","#fetchAvailsTargetingTable").show();
	}else{
		$("#dfpRow","#manageFetchAvails").hide();
		$("#infoIconFetchAvails","#fetchAvailsTargetingTable").hide();
	}
}

function checkProductIsReservable(prodId){
	var isprodReservable = 'N';
	$.each($('#allFetchAvailsProducts optgroup'), function(i, optgroup){
		$.each($(optgroup).find(' option[value=\"'+prodId +'"]') , function(i, option){		
			isprodReservable = $(option).attr('data-reserveable');
		});
	});
	if(isprodReservable == 'Y'){
		$("#productAvailType","#manageFetchAvails").val('R');
	}
}