/**
 * 
 */
var maxNumberOfCalendars = 10;

function loadMultipleCalendars(divCount) {
	$('#numberOfCalendarsToAdd', '#multipleCalParent').val('1');
	var noOfCalendarDivsPresent = $('#multipleCalendar').children(".calendar-box");
	if (noOfCalendarDivsPresent.length == 1) {
		$('.closeCal').hide();
	} else {
		$('.closeCal').show();
	}
	if (noOfCalendarDivsPresent.length == maxNumberOfCalendars) {
		$('#numberOfCalendarsToAdd', '#multipleCalParent').find('option').remove().end().append($('<option>', {value : (0)}).text(0));
		$('#numberOfCalendarsToAdd', '#multipleCalParent').attr("disabled","disabled");
		$('#addCalendarBox').hide();
	}
	var numberOfOptions = (maxNumberOfCalendars - noOfCalendarDivsPresent.length) - 1;
	$("#numberOfCalendarsToAdd option:gt(" + numberOfOptions + ")","#multipleCalParent").remove();
	$('#numberOfCalendarsToAdd', '#multipleCalParent').trigger('create');
	noOfCalendarDivsPresent = noOfCalendarDivsPresent.slice((noOfCalendarDivsPresent.length - divCount));
	for ( var i = 0; i < noOfCalendarDivsPresent.length; i++) {
		var idOfInstance = (noOfCalendarDivsPresent[i].id).substr(16,(noOfCalendarDivsPresent[i].id).length);
		$("#sosProductId", "#" + noOfCalendarDivsPresent[i].id).select2();
		$("#sosSalesTargetId", "#" + noOfCalendarDivsPresent[i].id).select2();
		$("#sosTarTypeElement" + idOfInstance).multiselect({selectedList : 2,}).multiselectfilter();
		var datesProposalLineItem = jQuery( '#startDate' + idOfInstance + ', #endDate' + idOfInstance ,'#'+ noOfCalendarDivsPresent[i].id).datepicker({
			autoSize : true,
			showOn : "both",
			buttonText : "Select Date",
			buttonImage : '../images/calendar.gif',
			buttonImageOnly : true,
			numberOfMonths : 3,
			onSelect : function(selectedDate) {
				var instanceId="";
				var datePickerId = "";
				if((this.id).indexOf('startDate') >=0){
					instanceId=this.id.substr(9,this.id.length);
					datePickerId = "endDate" + instanceId;
				}else{
					instanceId=this.id.substr(7,this.id.length);
					datePickerId = "startDate" + instanceId;
				}
				var option = this.id == "startDate"+ instanceId ? "minDate" : "maxDate", instance = $(this).data("datepicker");
				date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
				
				$("#" + datePickerId).datepicker( "option", option, date);
				if(this.id == "startDate"+ instanceId){
					$("#endDate"+ instanceId).val(selectedDate);
					$("#startDate"+ instanceId).datepicker( "option", "maxDate", date);
				}
			}
		});
	}
	$("#closeReservationDetail").live("click", function() {
		$(".reservation-detail").hide("slow");
		$('body').removeClass('hideScroll');
	});
	$("body").bind("ajaxComplete", function(event, request, settings) {
		if (request.getResponseHeader("'REQUIRES_AUTH") === '1') {
			$("#sessionTimeout").dialog("open");
		}
	}).bind("ajaxStart", function(event, request, settings) {
		$.blockUI($('#loader'));
	}).bind("ajaxStop", function(event, request, settings) {
		$.unblockUI();
	});
}

/**
 * Removes the current div and updates the select box
 *
 * @param divObj
 */
function removeSelectedCalendarDiv(divObj){
    var noOfCalendarDivsPresent = $('#multipleCalendar').children(".calendar-box").length;
    if (noOfCalendarDivsPresent == 2) {
        $('.closeCal').hide();
    }
    if (noOfCalendarDivsPresent == maxNumberOfCalendars) {
        $("#numberOfCalendarsToAdd", "#multipleCalParent").removeAttr("disabled");
        $("#numberOfCalendarsToAdd", "#multipleCalParent").find('option').remove().end().append($('<option>', { value: (1) }).text(1));
        $('#addCalendarBox').show();
    }
    else {
        var lastOptionValue = parseInt($('#numberOfCalendarsToAdd option:last', '#multipleCalParent').val());
        $('#numberOfCalendarsToAdd', '#multipleCalParent').append($('<option>', { value: (lastOptionValue + 1) }).text((lastOptionValue + 1)));
    }
    $("#" + divObj).remove();
    showHideErrorDiv();
}