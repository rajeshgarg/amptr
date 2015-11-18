/**
 * @author shipra.bansal
 */
var productSalesTarget;

jQuery(document).ready(function(){

    jQuery('body').bind('ajaxComplete', function(event, request, settings){
        if (request.getResponseHeader('REQUIRES_AUTH') === '1') {
            jQuery("#sessionTimeout").dialog("open");
        }
    });
    
    /**
     * Register progress message DIV on all AJAX call from the application
     */
    jQuery('body').bind('ajaxStart', function(event, request, settings){
        showLoader();
    });
    
    jQuery('body').bind('ajaxStop', function(event, request, settings){
        hideLoader();
    });
    
    jQuery("#sessionTimeout").dialog({
        autoOpen: false,
        height: 135,
        width: 400,
        modal: true,
        resizable: false,
        buttons: {
            Ok: function(){
                jQuery(this).dialog("close");
                window.location.reload();
            }
        },
        close: function(event, ui){
            window.location.reload();
        }
    });
    
    // initialise first time with only one calendar box
    $('#filterId', '#multipleCalParent').val('').select2();
    
    var ajaxReq = new AjaxRequest();
    ajaxReq.dataType = 'html';
    ajaxReq.url = '../manageReservation/viewMultipleCalendars.action?selectedNumberOfCalendars=' + 1;
    ajaxReq.success = function(result, status, xhr){
        $("#multipleCalendar", "#multipleCalParent").append(result);
        loadMultipleCalendars(1);
    };
    ajaxReq.submit();
    
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageReservation/getAllSalesTargetAndProduct.action';
    ajaxReq.success = function(result, status, xhr){
        productSalesTarget = result;
    };
    ajaxReq.submit();
    
    /**
     * closing the multiple Calendar window
     */
    $("#close-button", "#multipleCalParent").click(function(){
        window.close();
    });
});

/**
 * Populates the sales targets based on the selected product.
 */
function getSalesTargets(productId, randomId){
    var select = $('#sosSalesTargetId', "#calendarInstance" + randomId);
    $("option", select).remove();
    $.each(productSalesTarget, function(index, item){
        if (index == productId) {
            var optString = "<option value=''></option>";
            $.each(item, function(i, salesTarget){
                optString += "<option value='" + i + "'>" + salesTarget + "</option>";
            });
            $(select).append(optString).trigger("change");
        }
    });
}

/**
 *
 * add a single element of calendar and its related data
 */
function addCalendarBox(){
    var selectedNumberOfCalendars = $('#numberOfCalendarsToAdd', '#multipleCalParent').val();
    var ajaxRequest = new AjaxRequest();
    ajaxRequest.dataType = 'html';
    ajaxRequest.url = '../manageReservation/viewMultipleCalendars.action?selectedNumberOfCalendars=' + selectedNumberOfCalendars;
    ajaxRequest.success = function(result, status, xhr){
        $("#multipleCalendar", "#multipleCalParent").append(result);
        loadMultipleCalendars(selectedNumberOfCalendars);
    };
    ajaxRequest.submit();
    return false;
}

/**
 * sets the sales target randomly
 *
 * @param calendarBoxId
 */
function setSalesTargetRandom(calendarBoxId){
	$("#sosSalesTargetId", "#calendarInstance" + calendarBoxId).val('').trigger("change");
    if ($("#sosProductId", "#calendarInstance" + calendarBoxId).val() == "") {
        var select = $('#sosSalesTargetId', "#calendarInstance" + calendarBoxId);
    	$("option", select).remove();
    }
    else {
        getSalesTargets($("#sosProductId", "#calendarInstance" + calendarBoxId).val(), calendarBoxId);
    }
	if ($("#sosProductId option:selected", "#calendarInstance" + calendarBoxId).attr("data-productType") == "EMAIL") {
		$("input[id=sor], a[id=calculateSOR]", "#calendarInstance" + calendarBoxId).hide();
	} else {
		$("input[id=sor], a[id=calculateSOR]", "#calendarInstance" + calendarBoxId).show();
	}
}

/**
 * sets sales target for the selected div and selected product
 *
 * @param calendarBoxId
 */
function setSalesTarget(calendarBoxId){
    $("option", '#sosSalesTargetId', "#calendarInstance" + calendarBoxId).remove();
    loadData($("#sosSalesTargetId", "#calendarInstance" + calendarBoxId), '../manageReservation/getSalesTarget.action')
    $("#sosSalesTargetId", "#calendarInstance" + calendarBoxId).prepend("<option value='' selected='selected'></option>");
    $("#sosSalesTargetId", "#calendarInstance" + calendarBoxId).select2("val", "");
}

/**
 * Calculates SOR value
 *
 * @param calendarBoxId
 */
function calculateSORValue(calendarBoxId){
	clearCSSErrors("#calendarInstance" + calendarBoxId);
	resetTargetingFields(calendarBoxId);
    if ($("#sosProductId", "#calendarInstance" + calendarBoxId).val() != "" && ($('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val() != "" &&
    		$('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val() != null) && 
			$('#startDate' + calendarBoxId).val() != "" &&jQuery('#endDate' + calendarBoxId).val() != "") {
		getDataForTargetingToSave(calendarBoxId);
	    var ajaxReq = new AjaxRequest();
	    ajaxReq.type = 'POST';
	    ajaxReq.url = '../reservations/calculateSOR.action';
	    ajaxReq.data = {
	        calculateSOR: 'true',
	        productId: $("#sosProductId", "#calendarInstance" + calendarBoxId).val(),
	        salesTargetId: $('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val(),
	        startDate: jQuery('#startDate' + calendarBoxId).val(),
	        endDate: jQuery('#endDate' + calendarBoxId).val(),
	        proposalId: '0',
	        lineItemId: '0',
	        lineItemTargetingData: $("#targetingData", '#calendarInstance' + calendarBoxId).val()
	    };
	    ajaxReq.success = function(result, status, xhr){
	        clearCSSErrors("#calendarInstance" + calendarBoxId);
	        showHideErrorDiv();
	    	resetTargetingFields(calendarBoxId);

			var productType = $("#sosProductId option:selected", "#calendarInstance" + calendarBoxId).attr("data-productType"); 
			if (productType == 'EMAIL') {
				$("#sor", "#calendarInstance" + calendarBoxId).val(result.objectMap.reservationStatus);
			} else {
				$("#sor", "#calendarInstance" + calendarBoxId).val(result.objectMap.calculatedSOR);
			}	        
	    };
	    ajaxReq.submit();
	}
    else {
        renderError(calendarBoxId);
    }
}

/**
 * Function creates a JsonString to pass to server
 *
 * @param calendarBoxId
 */
function getDataForTargetingToSave(calendarBoxId){
    $("#targetingData", "#calendarInstance" + calendarBoxId).val('');
    var targetings = new Array();
    var objId = "";
    var action = "";
    var rowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").attr("id");
    $("#geoTargetsummary tr", "#calendarInstance" + calendarBoxId).each(function(){
        objId = $(this).attr("id");
        if (objId != "geoTargetsummaryHeader") {
            var object = new Object();
            var level = "";
            if (rowId != objId) {
                action = "and";
            }
            else {
                action = "";
            }
            object.action = action;
            object.targetTypeId = $("#" + objId + " td:nth-child(2)", "#calendarInstance" + calendarBoxId).html();
            object.targetTypeValue = $("#" + objId + " td:nth-child(1)", "#calendarInstance" + calendarBoxId).html();
            object.targetTypeElementValue = $("#" + objId + " td:nth-child(3)", "#calendarInstance" + calendarBoxId).html();
            object.targetTypeElement = $("#" + objId + " td:nth-child(4)", "#calendarInstance" + calendarBoxId).html();
            object.not = "";
            object.segmentLevel = "";
            targetings.push(object);
            $("#targetingData", "#calendarInstance" + calendarBoxId).val(JSON.stringify(targetings));
        }
    });
}

/**
 * Creates a calendar.
 *
 * @param calendarBoxId
 */
function viewCalander(calendarBoxId){
    clearCSSErrors("#calendarInstance" + calendarBoxId);
    showHideErrorDiv();
    if ($("#sosProductId", "#calendarInstance" + calendarBoxId).val() != "" && ($('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val() != "" &&
    		$('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val() != null) && jQuery('#startDate' + calendarBoxId).val() != "" && jQuery('#endDate' + calendarBoxId).val() != "") {
        var productId = $("#sosProductId", "#calendarInstance" + calendarBoxId).val();
        var salesTargetId = $('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val();
        getDataForTargetingToSave(calendarBoxId);
        var startDate = $("#startDate" + calendarBoxId).val().split("/");
        var startMonth = startDate[0] - 1;
        var startYear = startDate[2];
        var endDate = $("#endDate" + calendarBoxId).val().split("/");
        var endMonth = endDate[0] - 1;
        var endYear = endDate[2];
        $("#calendarDiv", '#calendarInstance' + calendarBoxId).html('');
        $("#calendarDiv", '#calendarInstance' + calendarBoxId).fullCalendar({
            weekMode: 'variable',
            month: startMonth,
            year: startYear,
            header: {
                right: 'prev,next'
            },
            events: {
                type: "POST",
                endParam: "endDate",
                startParam: "startDate",
                url: "../reservations/monthDetail.action",
                data: {
                    productId: productId,
                    proposalId: 0,
                    lineItemId: 0,
                    salesTargetId: salesTargetId,
                    productType: $("#sosProductId option:selected", "#calendarInstance" + calendarBoxId).attr("data-productType"),
                    lineItemTargetingData: $("#targetingData", '#calendarInstance' + calendarBoxId).val()
                },
                error: function(){
					jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: "Exceptional Behaviour occurred please consult your system administrator."});
                }
            },
            eventRender: function(event, element){
                $(element).html('').removeClass('fc-event');
                if (event.orders > 0) {
                    $(element).append("<span class='cal-date-data'>Order(s):" + event.orders + "</span>");
                }
                if (event.proposals > 0) {
                    $(element).append("<span class='cal-date-data'>Proposal(s):" + event.proposals + "</span>");
                }
                if (event.sor > 0) {
                    var sorString = (Math.round(event.sor * 100) / 100) + "% Booked";
					var productType = $("#sosProductId option:selected", "#calendarInstance" + calendarBoxId).attr("data-productType");
                    if (productType == "EMAIL") {
                        sorString = getEmailProductSorString(event);
                    }
                    $(element).append("<span class='cal-date-data'>" + sorString + "</span>").bind("click", function(){
                        var ajaxReq = new AjaxRequest();
                        ajaxReq.dataType = 'html';
                        ajaxReq.type = "POST";
                        ajaxReq.url = "../reservations/monthDayDetail.action";
                        ajaxReq.data = {
                            startDate: $.fullCalendar.formatDate(event.start, "MM/dd/yyyy"),
                            endDate: $.fullCalendar.formatDate(event.start, "MM/dd/yyyy"),
                            productId: productId,
                            proposalId: 0,
                            salesTargetId: salesTargetId,
                            lineItemTargetingData: $("#targetingData", '#calendarInstance' +
                            calendarBoxId).val()
                        };
                        ajaxReq.success = function(result, status, xhr){
                            $("#calendarDetailDiv" + calendarBoxId, '#calendarInstance' + calendarBoxId).html(result).height($(window).height() - 20).show("slow");
                            $(".scroll-accord-content").height($(".calendar-box").height() + 65);
                            if (result != null) {
                                $('body').addClass('hideScroll');
                            }
                        };
                        ajaxReq.submit();
                    });
					$(element).css("cursor", "pointer");
                }
				if (!event.bookingAllowed) {
					$(element).append("<span class='cal-email-chk' title='Booking for Product/Sales Traget combination not Allowed for the day'></span>");
				}
                if (event.sor >= 100) {
                    $('#calendarInstance' + calendarBoxId + ' td[data-date=\"' + $.fullCalendar.formatDate(event.start, 'yyyy-MM-dd') + '"]').css("background", "#ffd6e4");
                }
            }
        });
		modifyMultipleCalendarOption(calendarBoxId);
		resetTargetingFields(calendarBoxId);
    }
    else {
        renderError(calendarBoxId);
    }
	
	function getEmailProductSorString(reservationInfo) {
        var status = "Available";
        if (reservationInfo != null) {
            if (reservationInfo.sor > 100) {
                status = "Overbooked";
            }
            else if (reservationInfo.sor == 100) {
                status = (reservationInfo.proposals > 0) ? "Reserved" : "Sold";
            }
        }
        return status;
	}
}

/**
 * Used to provide list view option in multiple calendar screen
 * 
 * @param calendarBoxId
 */
function modifyMultipleCalendarOption(calendarBoxId) {
	var calendarDiv = $('#calendarInstance'+ calendarBoxId).html();
	$('#calendarInstance'+ calendarBoxId+ ' table .fc-header-right .fc-button-prev').before("<i class='select-list-view-btn' value='listView' title='List View' onclick=displayCalcListView(" + calendarBoxId + ") />");
}

/**
 * Used to display the list view in multiple calendar screen
 * 
 * @param calendarBoxId
 */
function displayCalcListView(calendarBoxId) {
	var productId = $("#sosProductId", "#calendarInstance" + calendarBoxId).val();
	var salesTargetId = $('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val();
	
	var date = $("#calendarDiv", "#calendarInstance" + calendarBoxId).fullCalendar('getDate');
	
	var calYear = date.getFullYear();
	var calMonth = date.getMonth();
	var calFirstDay = new Date(calYear, calMonth, 1);
	var calLastDay = new Date(calYear, calMonth + 1, 0);
	var firstDayOfMonth = $.fullCalendar.formatDate(calFirstDay, "MM/dd/yyyy");
	var latsDayOfMonth = $.fullCalendar.formatDate(calLastDay, "MM/dd/yyyy");	
	
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.type = "POST";
	ajaxReq.url = "../reservations/listView.action";
	
    ajaxReq.data = {
        startDate: firstDayOfMonth,
        endDate: latsDayOfMonth,
        productId: productId,
        proposalId: 0,
        salesTargetId: salesTargetId,
        lineItemTargetingData: $("#targetingData", '#calendarInstance' + calendarBoxId).val()
    };
    
	ajaxReq.success = function(result, status, xhr){
		$("#calendarDetailDiv" + calendarBoxId, '#calendarInstance' + calendarBoxId).html(result).height($(window).height() - 20).show("slow");
		$("#closeReservationDetail", '#calendarDetailDiv' + calendarBoxId).show();
		$(".scroll-accord-content").height($(".calendar-box").height() + 165);
		if (result != null) {
			$('body').addClass('hideScroll');
		}
	}
	ajaxReq.submit();
	
}


/**
 * Renders Error if either of productId, targetId, start date or end date has no
 * selected value.
 *
 * @param calendarBoxId
 */
function renderError(calendarBoxId){

    if ($("#sosProductId", "#calendarInstance" + calendarBoxId).val() == "") {
        $('#sosProductIdDiv', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
			.attr("title", "Product is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
        $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
        renderErrorQtip("#calendarInstance" + calendarBoxId);
    }
    if ($("#sosSalesTargetId", "#calendarInstance" + calendarBoxId).val() == "" ||
    $('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val() == null) {
        $('#sosSalesTargetIdDiv', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
			.attr("title", "Sales Target is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
        $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
        renderErrorQtip("#calendarInstance" + calendarBoxId);
    }
    if ($("#startDate" + calendarBoxId).val() == "") {
        $("#startDate" + calendarBoxId).addClass("errorElement errortip")
			.attr("title", "Start Date is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
        $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
        renderErrorQtip("#calendarInstance" + calendarBoxId);
    }
    if ($("#endDate" + calendarBoxId).val() == "") {
        $('#endDate' + calendarBoxId).addClass("errorElement errortip")
			.attr("title", "End Date is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
        $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
        renderErrorQtip("#calendarInstance" + calendarBoxId);
    }
}

/**
 * Renders Error if either of productId, targetId, start date or end date has no
 * selected value.
 *
 * @param calendarBoxId
 * @returns {String}
 */
function getParamsForSOR(calendarBoxId){
    var productId = $("#sosProductId", "#calendarInstance" + calendarBoxId).val();
    var salesTargetId = $('#sosSalesTargetId', '#calendarInstance' + calendarBoxId).val();
    var startDate = jQuery('#startDate' + calendarBoxId).val();
    var endDate = jQuery('#endDate' + calendarBoxId).val();
    getDataForTargetingToSave(calendarBoxId);
    var returnParams = "productId=" + productId + "&salesTargetId=" + salesTargetId + "&startDate=" 
		+ startDate + "&endDate=" + endDate + "&proposalId=0&lineItemId=0&lineItemTargetingData=" 
		+ $("#targetingData", '#calendarInstance' + calendarBoxId).val();
    return returnParams;
}

/**
 * Fill target type element
 *
 * @param elementsId
 * @param calendarBoxId
 */
function fillTargetTypeElement(elementsId, calendarBoxId){
    var url = "";
    var tarTypeElem = "";
    var flag = false;
    if ($('#sosTarTypeId', "#calendarInstance" + calendarBoxId).val() == "") {
        $('#sosTarTypeElement' + calendarBoxId + ' >option', "#calendarInstance" + calendarBoxId).remove();
        $('#sosTarTypeElement' + calendarBoxId , "#calendarInstance" + calendarBoxId).val('');
		$('#sosTarTypeElement' + calendarBoxId , "#calendarInstance" + calendarBoxId).multiselect("refresh");
    }
    else {
        var ajaxReq = new AjaxRequest();
        ajaxReq.url = "../manageReservation/getTargetTypeElements.action?sosTarTypeId=" +
        $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).val();
        ajaxReq.success = function(result, status, xhr){
            var returnedId = result.objectMap.gridKeyColumnValue;
            var select = $("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" + calendarBoxId);
            $('option', select).remove();
            sortList(returnedId, select);
            if (elementsId != null) {
                var selectedElements = elementsId.split(",");
                $(select).val(selectedElements);
            }
            $(select).multiselect("refresh");
        };
        ajaxReq.submit();
    }
}

/**
 * Fill target type element
 *
 * @param calendarBoxId
 */
function addTargets(calendarBoxId){
    if (validateAddTargets(calendarBoxId)) {
        var selectedElements = [];
        var previousRowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").attr("id");
        var rowBeforeLastRowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").prev('tr').attr("id");
        
        if ($('#geoTargetsummary tr', "#calendarInstance" + calendarBoxId).length == 1) {
            var rowCount = 1;
        }
        else {
            var rowCount = Number(previousRowId.substring(previousRowId.length - 1)) + 1;
            var prevRowCount = Number(rowBeforeLastRowId.substring(previousRowId.length - 1)) + 1;
            if (prevRowCount > rowCount) {
                rowCount = prevRowCount;
            }
        }
        
        var rowId = "geoTargetsummary" + rowCount;
        $("#sosTarTypeElement" + calendarBoxId + " :selected", "#calendarInstance" + calendarBoxId).each(function(){
            selectedElements.push($(this).text());
        });
        var sosTargetTypeText = $("#sosTarTypeId option:selected", "#calendarInstance" + calendarBoxId).text();
        var sosTarTypeId = $("#sosTarTypeId", "#calendarInstance" + calendarBoxId).val();
        var selectedElementsId = $("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" + calendarBoxId).val();
        displayTargets(rowId, sosTargetTypeText, sosTarTypeId, calendarBoxId, selectedElements.join(", "), selectedElementsId, previousRowId);
    }
}

/**
 * Function dynamically delete rows to the Target Table
 *
 * @param trId
 * @param calendarBoxId
 */
function deleteLineItemTarget(trId, calendarBoxId){
    var lastRowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").attr("id");
    if (trId == lastRowId) {
        var rowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").prev('tr').attr("id");
        $('#' + rowId + " td:nth-child(7)", "#calendarInstance" + calendarBoxId).html('--');
    }
    var targetTypeText = $('#' + trId + " td:nth-child(2)", "#calendarInstance" + calendarBoxId).html();
    $('#' + trId, "#calendarInstance" + calendarBoxId).remove();
    if ("geoTargetsummaryHeader" == $('#geoTargetsummaryContainer', "#calendarInstance" + calendarBoxId).find("tr:last").attr("id")) {
        $('#geoTargetsummaryContainer', "#calendarInstance" + calendarBoxId).hide();
    }
    resetTargetingFields(calendarBoxId);
    clearCSSErrors("#calendarInstance" + calendarBoxId);
    $('#messageHeaderDiv', '#multipleCalParent').html('');
}

/**
 * Function Edits the row of a Target Table
 *
 * @param trId
 * @param calendarBoxId
 */
function editLineItemTarget(trId, calendarBoxId){
    $("#targetResetData", "#calendarInstance" + calendarBoxId).val('CANCEL');
    $("#sosTarTypeId", "#calendarInstance" + calendarBoxId).val($("#" + trId + " td:nth-child(2)", "#calendarInstance" + calendarBoxId).html());
    $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).attr('disabled', true);
    $("#tarTypeElementText", "#calendarInstance" + calendarBoxId).hide();
    $("#sosTarTypeElement" + calendarBoxId + "_custom", "#calendarInstance" + calendarBoxId).show();
    fillTargetTypeElement($("#" + trId + " td:nth-child(4)", "#calendarInstance" + calendarBoxId).html(), calendarBoxId);
    $("#addTargetToLineItem", "#calendarInstance" + calendarBoxId).hide();
    $("#updateTargetToLineItem", "#calendarInstance" + calendarBoxId).show();
    $("#updateTargetToLineItem", "#calendarInstance" + calendarBoxId).attr("onclick", "updateTargetsToLineItem('" + trId + "','" + calendarBoxId + "')")
}

/**
 * Update the value in The Target Table based on rowId
 *
 * @param trId
 * @param calendarBoxId
 */
function updateTargetsToLineItem(trId, calendarBoxId){
    if (validateAddTargets(calendarBoxId)) {
        var not = "--";
        var selectedElements = [];
        $('#' + trId + " td:nth-child(1)", "#calendarInstance" + calendarBoxId).html($("#sosTarTypeId option:selected", "#calendarInstance" + calendarBoxId).text());
        $('#' + trId + " td:nth-child(2)", "#calendarInstance" + calendarBoxId).html($("#sosTarTypeId", "#calendarInstance" + calendarBoxId).val());
        $("#sosTarTypeElement" + calendarBoxId + " :selected", "#calendarInstance" + calendarBoxId).each(function(){
            selectedElements.push($(this).text());
        });
        $('#' + trId + " td:nth-child(3)", "#calendarInstance" + calendarBoxId).attr("title", selectedElements.join(", "));
        $('#' + trId + " td:nth-child(3)", "#calendarInstance" + calendarBoxId).html(selectedElements.join(", "));
        $('#' + trId + " td:nth-child(4)", "#calendarInstance" + calendarBoxId).html($("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" + calendarBoxId).val().join(","));
        resetTargetingFields(calendarBoxId);
    }
}

/**
 * Function resets the Targetting fields
 *
 * @param calendarBoxId
 */
function resetTargetingFields(calendarBoxId){
	var select = $("#targetingSection", "#calendarInstance" + calendarBoxId);
	clearCSSErrors(select);
    $("#sosTarTypeId", "#calendarInstance" + calendarBoxId).val("");
    $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).attr('disabled', false);
    $('option', $("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" +
    calendarBoxId)).remove();
    $("#sosTarTypeElement" + calendarBoxId + "_custom", "#calendarInstance" + calendarBoxId).show();
    $("#tarTypeElementText", "#calendarInstance" + calendarBoxId).val("");
    $("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" + calendarBoxId).val('');
    $("#sosTarTypeElement" + calendarBoxId, "#calendarInstance" + calendarBoxId).multiselect("refresh");
    $("#addTargetToLineItem", "#calendarInstance" + calendarBoxId).show();
    $("#updateTargetToLineItem", "#calendarInstance" + calendarBoxId).hide();
    $("#targetResetData", "#calendarInstance" + calendarBoxId).val('RESET');  
    if($("#multipleCalParent").find(".errortip").length > 0 ){
    	$('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
    }else if($("#multipleCalParent").find(".errortip").length == 0){
    	$('#messageHeaderDiv', '#multipleCalParent').html('');
    }
}

/**
 * validates targets
 *
 * @param calendarBoxId
 * @returns {Boolean}
 */
function validateAddTargets(calendarBoxId){
    var returnFlag = true;
    var select = $("#targetingSection", "#calendarInstance" + calendarBoxId);
	clearCSSErrors(select);
    $('#messageHeaderDiv', '#multipleCalParent').html('');
    if ($('#sosTarTypeId', "#calendarInstance" + calendarBoxId).val() == "") {
        $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip").attr("title", "Target Type is mandatory." + '<BR><b>Help:</b> ' +
        "Please select valid data in the field.");
        renderErrorQtip("#calendarInstance" + calendarBoxId);
        returnFlag = false;
    }
    
    if ($('#sosTarTypeElement' + calendarBoxId, "#calendarInstance" + calendarBoxId).val() == null) {
        $('#sosTarTypeElement' + calendarBoxId + '_custom', "#calendarInstance" + calendarBoxId)
			.addClass("errorElement errortip").attr("title", "Elements is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
        renderErrorQtip("#calendarInstance" + calendarBoxId);
        $('#sosTarTypeElement' + calendarBoxId + '_custom', "#calendarInstance" + calendarBoxId).focus();
        returnFlag = false;
    }
    
    if ("Frequency Cap" == $("#sosTarTypeId option:selected", "#calendarInstance" + calendarBoxId).text()) {
        var lastRowId = $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").attr("id");
        if ("Frequency Cap" == $('#' + lastRowId + " td:nth-child(1)", "#calendarInstance" + calendarBoxId).html() &&
        		$('#sosTarTypeId', "#calendarInstance" + calendarBoxId).is(':disabled') != true) {
            $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
				.attr("title", "Frequency Cap targeting cannot be added twice." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
            renderErrorQtip("#calendarInstance" + calendarBoxId);
            returnFlag = false;
        }
        else {
            var selectedElements = [];
            $("#sosTarTypeElement" + calendarBoxId + " :selected", "#calendarInstance" + calendarBoxId).each(function(){
                selectedElements.push($(this).text());
            });
            
            if (selectedElements.length > 1) {
                $('#sosTarTypeElement' + calendarBoxId + '_custom', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
					.attr("title", "Multiple elements cannot be selected for the Frequency Cap Targeting." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
                renderErrorQtip("#calendarInstance" + calendarBoxId);
                $('#sosTarTypeElement' + calendarBoxId + '_custom', "#calendarInstance" + calendarBoxId).focus();
                returnFlag = false;
            }
        }
    }
    
    if (returnFlag) {
        returnFlag = validateTargetsForReservation(calendarBoxId);
    }
    
    if(!returnFlag){
		$('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
	}else{
		$('#messageHeaderDiv', '#multipleCalParent').html('');
	}
    
    return returnFlag;
}

/**
 * only one Among DMA, Countries and Regions
 *
 * @param calendarBoxId
 * @returns {Boolean}
 */
function validateTargetsForReservation(calendarBoxId){
	var select = $("#targetingSection", "#calendarInstance" + calendarBoxId);
	clearCSSErrors(select);
	showHideErrorDiv();
    var returnFlag = true;
    if ("Frequency Cap" != $("#sosTarTypeId option:selected", "#calendarInstance" + calendarBoxId).text()) {
        $("#geoTargetsummary tr", "#calendarInstance" + calendarBoxId).each(function(){
            objId = $(this).attr("id");
            if (objId != "geoTargetsummaryHeader") {
                var targetType = $('#' + objId + " td:nth-child(1)","#calendarInstance" + calendarBoxId).html();
                if (("Adx DMA" == targetType || "Countries" == targetType ||"Target Region" == targetType) &&
                		$('#sosTarTypeId', "#calendarInstance" +calendarBoxId).is(':disabled') !=true) {
                    if (targetType == $("#sosTarTypeId option:selected", "#calendarInstance" + calendarBoxId).text()) {
                        $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
							.attr("title", targetType + " targeting cannot be added twice." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
                        renderErrorQtip("#calendarInstance" + calendarBoxId);
                    }
                    else {
                        $('#sosTarTypeId', "#calendarInstance" + calendarBoxId).addClass("errorElement errortip")
							.attr("title", "Only one targeting amongst Adx DMA, Countries and Target Regions is allowed." +
                        		'<BR><b>Help:</b> ' + "Please select valid data in the field.");
                        renderErrorQtip("#calendarInstance" + calendarBoxId);
                    }
                    returnFlag = false;
                }
            }
        });
    }
    return returnFlag;
}

/**
 * resetting the start date and calendar
 * @param calendarBoxId
 */
function resetStartDate(calendarBoxId){
    $("#startDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).val("");
    $("#endDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).datepicker("option", "minDate", $("#startDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).val());
}

/**
 * resetting the end date and calendar
 * @param calendarBoxId
 */
function resetEndDate(calendarBoxId){
    $("#endDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).val("");
    $("#startDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).datepicker("option", "maxDate", $("#endDate" + calendarBoxId, '#calendarInstance' + calendarBoxId).val());
}

/**
 *
 * @param {Object} proposalId
 */
function openProposalInWindow(proposalId){
    try {
        var myParentWindow = window.opener;
        myParentWindow.openProposal(proposalId);
    } 
    catch (e) {
        var url = $("#reservationContextPath").val() + "/manageProposal.action?_flowId=workOnProposal&proposalId=" + proposalId;
        var width = screen.width - 100;
        var height = screen.height - 100;
        window.open(url, "proposal" + proposalId, 'left=50,top=0,width=' + width + ',height=' + height + ',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
    }
}

/**
 *
 * @param {Object} orderId
 */
function openOrderInWindow(orderId, sosURL){
    try {
        var myParentWindow = window.opener;
        myParentWindow.openOrder(orderId, sosURL);
    } 
    catch (e) {
        var url = sosURL + "/cgi-bin/sos/sales/calc.pl?SALESORDER_ID=" + orderId;
        var width = screen.width - 100;
        var height = screen.height - 100;
        window.open(url, "order" + orderId, 'left=50,top=0,width=' + width + ',height=' + height + ',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
    }
}

/**
 * The function is called on the click of add and edit.
 * @param editOrAdd
 */
function addFilterCriteria(editOrAdd){
    clearCSSErrors("#multipleCalendarsForm");
    $('#messageHeaderDiv', '#multipleCalParent').html('');
    $('#editAndDelete', '#multipleCalParent').css("display", "none");
    $('#addFiltercriteria', '#multipleCalParent').css("display", "none");
    $('#saveAndCancel', '#multipleCalParent').css("display", "block");
    $('#filterPlugin', '#multipleCalParent').css("display", "none");
    $('#filtertextId', '#multipleCalParent').css("display", "block");
    if (editOrAdd == 'edit') {
        $('#filterText', '#multipleCalParent').val($('#filterId option:selected', '#multipleCalParent').text());
        $('#filterIdDiv', '#multipleCalParent').css("display", "inline-block");
        $('#filterPlugin', '#multipleCalParent').css("display", "none");
    }
    else {
		$('#hiddenFilterId','#multipleCalParent').val($('#filterId', '#multipleCalParent').val());
		$('#filterId', '#multipleCalParent').val('');
        $('#filterText', '#multipleCalParent').val('');
    }
}

/**
 * Resets filters on the click of cancel.
 * @param
 */
function resetFilters(){
    clearCSSErrors("#multipleCalendarsForm");
    $('#messageHeaderDiv', '#multipleCalParent').html('');
    $('#addFiltercriteria', '#multipleCalParent').css("display", "block");
    $('#saveAndCancel', '#multipleCalParent').css("display", "none");
    $('#filterPlugin', '#multipleCalParent').css("display", "block");
    $('#filtertextId', '#multipleCalParent').css("display", "none");
    if ($('#hiddenFilterId','#multipleCalParent').val() !='') {
		$('#filterId', '#multipleCalParent').val($('#hiddenFilterId','#multipleCalParent').val()).select2();
    }
	 if($('#userName', "#multipleCalParent").val() != $("#filterId option:selected").parent("optgroup").attr("label")){
		 $('#editAndDelete', '#multipleCalParent').css("display", "none");
	}
    else {
        $('#editAndDelete', '#multipleCalParent').css("display", "block");
    }
	$('#hiddenFilterId','#multipleCalParent').val('');
}

/**
 * saves a filter.
 * @param
 */
function saveFilters(){
	clearCSSErrors("#multipleCalendarsForm");
    $('#messageHeaderDiv', '#multipleCalParent').html('');
    var error = false;
    var noOfCalendarDivsPresent = $('#multipleCalendar').children(".calendar-box");
    var productId = "";
    var salesTarget = "";
    var startDate = "";
    var endDate = "";
    var obj = "";
    var targetings = new Array();
    var targetingdata = "";
	var scrollToError = true;
    for (var i = 0; i < noOfCalendarDivsPresent.length; i++) {
        var idOfInstance = (noOfCalendarDivsPresent[i].id).substr(16, (noOfCalendarDivsPresent[i].id).length);
        if ($("#sosProductId", "#" + noOfCalendarDivsPresent[i].id).val() != "" && ($("#sosSalesTargetId", "#" + noOfCalendarDivsPresent[i].id).val() != "" && $("#sosSalesTargetId", "#" + noOfCalendarDivsPresent[i].id).val() != null)) {
            var object = new Object();
            object.productId = $("#sosProductId", "#" + noOfCalendarDivsPresent[i].id).val();
            object.salestargetId = $("#sosSalesTargetId", "#" + noOfCalendarDivsPresent[i].id).val();
            getDataForTargetingToSave(idOfInstance);
            object.targetingdata = $("#targetingData", "#" + noOfCalendarDivsPresent[i].id).val();
            targetings.push(object);
        }
        else {
            error = true;
            if ($("#sosProductId", "#calendarInstance" + idOfInstance).val() == "") {
                $('#sosProductIdDiv', "#calendarInstance" + idOfInstance).addClass("errorElement errortip")
					.attr("title", "Product is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
                $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
				if(scrollToError){
					$("#calendarInstance" + idOfInstance)[0].scrollIntoView(true);
					scrollToError = false;
				}
                renderErrorQtip("#calendarInstance" + idOfInstance);
                
            }
            if ($("#sosSalesTargetId", "#calendarInstance" + idOfInstance).val() == "" ||
            $('#sosSalesTargetId', '#calendarInstance' + idOfInstance).val() == null) {
                $('#sosSalesTargetIdDiv', "#calendarInstance" + idOfInstance).addClass("errorElement errortip")
					.attr("title", "Sales Target is mandatory." + '<BR><b>Help:</b> ' + "Please select valid data in the field.");
                $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
                if(scrollToError){
					$("#calendarInstance" + idOfInstance)[0].scrollIntoView(true);
					scrollToError = false;
				}
                renderErrorQtip("#calendarInstance" + idOfInstance);
            }
			 if ($("#filterText", "#multipleCalParent").val() == "") {
                $('#filterText', "#multipleCalParent").addClass("errorElement errortip")
					.attr("title", "Filter Name is mandatory." + '<BR><b>Help:</b> ' + "Please provide valid data in the field.");
                $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
                renderErrorQtip("#multipleCalParent");
            }
        }
    }
    if (!error) {
        saveData(targetings);
    }
}

/**
 * displays the targeting values
 * @param
 */
function displayTargets(rowId, sosTarTypeText, sosTarTypeId, calendarBoxId, selectedElementsValue, sosTarTypeElementId, previousRowId){
    var valueToPrint = "<tr id='" + rowId + "'><td width='18%'>" + sosTarTypeText + "</td><td style='display : none'>" + sosTarTypeId;
    valueToPrint = valueToPrint + "</td><td width='50%' title='" + selectedElementsValue + "' style=' white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" +
    					selectedElementsValue + "</td><td style='display : none'>" + sosTarTypeElementId + "</td>";
    valueToPrint = valueToPrint + "</td><td width='10%'>" + "<a style='float:left;margin-left:25%' title='Edit' onClick=\"editLineItemTarget('" + rowId + "','" +
    					calendarBoxId + "')\"> <span class='ui-icon ui-icon-pencil' style='cursor:pointer;'/></a> <a style='float:left;margin-left:12px' title='Delete' onClick=\"deleteLineItemTarget('" +
    					rowId + "','" + calendarBoxId + "')\"> <span class='ui-icon ui-icon-trash' style='cursor:pointer;'/></a></td></tr>";
    $('#geoTargetsummaryContainer', "#calendarInstance" + calendarBoxId).css("display", "block");
    if ("Frequency Cap" == $('#' + previousRowId + " td:nth-child(1)", "#calendarInstance" + calendarBoxId).html()) {
        $("#geoTargetsummary", "#calendarInstance" + calendarBoxId).find("tr:last").before(valueToPrint);
    }
    else {
        $('#geoTargetsummary', "#calendarInstance" + calendarBoxId).append(valueToPrint);
    }
    resetTargetingFields(calendarBoxId);
}

/**
 * shows the filtered data.
 * @param
 */
function showfilteredData(){
    if ($('#userName', "#multipleCalParent").val() != $("#filterId option:selected").parent("optgroup").attr("label")) {
        $('#editAndDelete', '#multipleCalParent').css("display", "none");
    }
    else {
        $('#editAndDelete', '#multipleCalParent').css("display", "block");
        $('#addFiltercriteria', '#multipleCalParent').css("display", "block");
    }
    var noOfCalendarDivsPresent = $('#multipleCalendar').children(".calendar-box");
    for (var i = 0; i < noOfCalendarDivsPresent.length; i++) {
        removeSelectedCalendarDiv(noOfCalendarDivsPresent[i].id);
    }
    var ajaxReq = new AjaxRequest();
    ajaxReq.dataType = 'html';
    ajaxReq.url = '../manageReservation/viewMultipleCalendars.action?selectedNumberOfCalendars=1';
    ajaxReq.success = function(result, status, xhr){
        $("#multipleCalendar", "#multipleCalParent").append(result);
        loadMultipleCalendars(1);
        
        var filterId = 0;
        if ($('#filterId').val() != '') {
            filterId = $('#filterId').val();
        }
        if (filterId > 0) {
            var ajaxReq = new AjaxRequest();
            ajaxReq.type = "POST";
            ajaxReq.url = '../manageReservation/getFilteredData.action?filterId=' + filterId;
            ajaxReq.success = function(result, status, xhr){
                var targets = eval(result.objectMap.userFilter);
                var ajaxReq = new AjaxRequest();
                ajaxReq.dataType = 'html';
                ajaxReq.url = '../manageReservation/viewMultipleCalendars.action?selectedNumberOfCalendars=' + (targets.length - 1);
                ajaxReq.success = function(result, status, xhr){
                    $("#multipleCalendar", "#multipleCalParent").append(result);
                    loadMultipleCalendars(targets.length - 1);
                    $.each(targets, function(index, item){
                    
                        var noOfCalendarDivsPresent = $('#multipleCalendar').children(".calendar-box");
                        if (noOfCalendarDivsPresent.length > 0 && noOfCalendarDivsPresent.length > index) {
                            var idOfInstance = (noOfCalendarDivsPresent[index].id).substr(16, (noOfCalendarDivsPresent[index].id).length);
                            $("#sosProductId", "#" + noOfCalendarDivsPresent[index].id).val(item.productId).trigger("change");
                            $("#sosSalesTargetId", "#" + noOfCalendarDivsPresent[index].id).val(item.salestargetId).trigger("change");
                            var dateValue = new Date();
                            var startDate = dateValue.getMonth() + 1 + "/" + dateValue.getDate() + "/" + dateValue.getFullYear();
                            $("#startDate" + idOfInstance, '#' + noOfCalendarDivsPresent[index].id).val(startDate);
                            $("#endDate" + idOfInstance, '#' + noOfCalendarDivsPresent[index].id).val(startDate);
                            
                            var targetingInfo = eval(item.targetingdata);
                            if (targetingInfo != undefined) {
                                $.each(targetingInfo, function(i, value){
                                    var rowCount = $('#geoTargetsummary tr', '#' + noOfCalendarDivsPresent[index].id).length;
                                    var rowId = "geoTargetsummary" + rowCount;
                                    var sosTarTypeId = value.targetTypeId;
                                    var sosTarTypeText = $("#sosTarTypeId option[value=" + sosTarTypeId + "]").text();
                                    var selectedElementsValue = value.targetTypeElementValue;
                                    var sosTarTypeElementId = value.targetTypeElement;
                                    var sosTarTypeElementidArray = sosTarTypeElementId.split(',');
                                    var previousRowId = $("#geoTargetsummary", '#' + noOfCalendarDivsPresent[index].id).find("tr:last").attr("id");
                                    displayTargets(rowId, sosTarTypeText, sosTarTypeId, idOfInstance, selectedElementsValue, sosTarTypeElementId, previousRowId);
                                });
                            }
                        }
                        viewCalander(idOfInstance);
                    });
                };
                ajaxReq.submit();
            };
            ajaxReq.submit();
        }
    };
    ajaxReq.submit();
}

/**
 * deletes the filters
 * @param
 */
function deleteFilters(){
    var ajaxReq = new AjaxRequest();
    ajaxReq.url = '../manageReservation/deleteFilter.action?filterId=' + $('#filterId', '#multipleCalParent').val();
    ajaxReq.success = function(result, status, xhr){
        jQuery("#runtimeDialogDiv").model({theme: 'Success', message: 'Filter deleted successfully'});
        $("#filterId option[value='" + $('#filterId', '#multipleCalParent').val() + "']", '#multipleCalParent').remove();
        $("#filterId", '#multipleCalParent').select2("val", "");
        $('#editAndDelete', '#multipleCalParent').css("display", "none");
        $('#hiddenFilterId','#multipleCalParent').val('');
        resetFilters();
        if ($('#filterId  optgroup[label="' + $("#multipleCalParent #userName").val() + '"] option').size() == 0) {
            $('#filterId  optgroup[label="' + $("#multipleCalParent #userName").val() + '"]').remove();
        }
    };
    ajaxReq.submit();
}

/**
 * saves the filter Data
 * @param
 */
function saveData(targetings){
    var filterId = 0;
    if ($('#filterId', '#multipleCalParent').val() != "") {
        filterId = $('#filterId', '#multipleCalParent').val();
    }
    var filterName = $("#filterText", "#multipleCalParent").val();
    var ajaxReq = new AjaxRequest();
    ajaxReq.type = "POST";
    ajaxReq.url = '../manageReservation/saveFilterData.action';
    ajaxReq.data = {
        filterData: JSON.stringify(targetings),
        filterName: filterName,
        filterId: filterId
    };
    ajaxReq.success = function(result, status, xhr){
        clearCSSErrors("#multipleCalendarsForm");
        $('#messageHeaderDiv', '#multipleCalParent').html('');
        if (result.errorList.length > 0) {
            $('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
            jQuery.each(result.errorList, function(i, error){
                jQuery("#" + error.uiFieldIdentifier).addClass("errorElement errortip").attr("title", error.errorMessageForUI + '<BR><b>Help:</b> ' + error.errorHelpMessageForUI);
            });
            $('.errortip', "#multipleCalParent").qtip({
                style: {
                    classes: 'ui-tooltip-red'
                },
                position: {
                    my: 'bottom center',
                    at: 'top center'
                }
            });
        }
        else {
            $("#filterText", "#multipleCalParent").val(result.objectMap.filterId);
            jQuery("#runtimeDialogDiv").model({theme: 'Success', message: 'Filter saved successfully'});
            $("#multipleCalParent #filterId option[value='" + filterId + "']").remove();
            var isOptgroupPresent = false;
            $('#multipleCalParent #filterId ').find("optgroup").each(function(){
                var label = $(this).attr("label");
                if ($(this).attr("label") == $('#userName', "#multipleCalParent").val()) {
                    isOptgroupPresent = true;
                }
            });
            if (isOptgroupPresent) {
                $('#multipleCalParent #filterId optgroup[label="' + $("#multipleCalParent #userName").val() + '"]').append('<option value="' + result.objectMap.filterId + '">' + filterName + '</option>');
            }
            else {
                var optgroup = "<optgroup label='" + $('#userName', "#multipleCalParent").val() + "'><option value='" + result.objectMap.filterId + "'>" + filterName + "</option></optgroup>";
                $('#multipleCalParent #filterId').append(optgroup).select2();
            }
            $('#editAndDelete', '#multipleCalParent').css("display", "block");
            $('#addFiltercriteria', '#multipleCalParent').css("display", "block");
            $('#saveAndCancel', '#multipleCalParent').css("display", "none");
            $('#filterPlugin', '#multipleCalParent').css("display", "block");
            $('#filtertextId', '#multipleCalParent').css("display", "none");
            $('#filterId', '#multipleCalParent').val(result.objectMap.filterId).select2();
        }
    };
    ajaxReq.submit();
}

function showHideErrorDiv() {
	if($("#multipleCalParent").find(".errortip").length > 0 ) {
    	$('#messageHeaderDiv', '#multipleCalParent').html('An error has occurred. Please place/hover the cursor over highlighted field to see the details.').addClass('error');
    } else if ($("#multipleCalParent").find(".errortip").length == 0) {
    	$('#messageHeaderDiv', '#multipleCalParent').html('');
    }
}