/**
 * @author surendra.singh
 */
$(document).ready(function(){
	$("#closeReservationDetail", "#reservationDetail").live("click", function () {
		$("#reservationDetail").hide("slow");
	});

    $("body").bind("ajaxComplete", function(event, request, settings){
        if (request.getResponseHeader("'REQUIRES_AUTH") === '1') {
            $("#sessionTimeout").dialog("open");
        }
    }).bind("ajaxStart", function(event, request, settings){
        $.blockUI($('#loader'));
    }).bind("ajaxStop", function(event, request, settings){
        $.unblockUI();
    });
	
	$("#close-button").click(function () {
		window.close();
	});
	
	var viewStatus = 0;
	
	var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
	
	var startDate = $("#startDate", "#lineItemDetail").val().split("/");
	var startMonth = startDate[0] - 1; var startYear = startDate[2];

	var endDate = $("#endDate", "#lineItemDetail").val().split("/");
	var endMonth = endDate[0] - 1; var endYear = endDate[2];

 	$("#calendar", "#calendatContainer").fullCalendar({
        weekMode: 'variable', month: startMonth, year: startYear,
		header: {
            right: 'prev,next'
        },
		events: {
			type: "POST",
			endParam: "endDate",
			startParam: "startDate",
			url : "../reservations/monthDetail.action",
            data: {
				productId: $("#productId", "#lineItemDetail").val(),
				proposalId: $("#proposalId", "#lineItemDetail").val(),
				lineItemId: $("#lineItemId", "#lineItemDetail").val(),
				salesTargetId: $("#salesTargetId", "#lineItemDetail").val(),
				productType: $("#productType", "#lineItemDetail").val(),
				lineItemTargetingData: $("#lineItemTargetingData", "#lineItemDetail").val()
			},
			error : function () {
				jQuery("#runtimeDialogDiv").model({theme: 'Error', autofade: false, message: "Exceptional Behaviour occurred please consult your system administrator."});
			}
        },
		eventRender: function(event, element) {
            $(element).html('').removeClass('fc-event');
			if (event.orders > 0) {
				$(element).append("<span class='cal-date-data'>Order(s): " + event.orders +"</span>");
			}
			if (event.proposals > 0) {
				$(element).append("<span class='cal-date-data'>Proposal(s): " + event.proposals + "</span>");
			}
			if (event.sor > 0) {
				var sorString = (Math.round(event.sor * 100) / 100) + "% Booked";
				if ($("#productType", "#lineItemDetail").val() == "EMAIL") {
					sorString = getEmailProductSorString(event); 
				}
				$(element).append("<span class='cal-date-data'>" + sorString + "</span>").bind("click", function () {
					var ajaxReq = new AjaxRequest ();
					ajaxReq.dataType = 'html';
					ajaxReq.type = "POST";
					ajaxReq.url = "../reservations/monthDayDetail.action";
                    ajaxReq.data = {
						startDate: $.fullCalendar.formatDate(event.start, "MM/dd/yyyy"),
						endDate: $.fullCalendar.formatDate(event.start, "MM/dd/yyyy"),
						productId: $("#productId", "#lineItemDetail").val(),
						proposalId: $("#proposalId", "#lineItemDetail").val(),
						salesTargetId: $("#salesTargetId", "#lineItemDetail").val(),
						lineItemTargetingData: $("#lineItemTargetingData", "#lineItemDetail").val()
                    };
					ajaxReq.success = function(result, status, xhr) {
						$("#reservationDetail").html(result).height($("#calendatContainer").height()-20).show("slow"); // Set Reservation Detail height
						$(".scroll-accord-content").height($("#calendatContainer").height()-45); // Set scroll content height
					};
					ajaxReq.submit();
				});
				$(element).css("cursor", "pointer");
			}
			if (!event.bookingAllowed) {
				$(element).append("<span class='cal-email-chk' title='Booking for Product/Sales Traget combination not Allowed for the day'></span>");
			}
			if (event.bookedForCurrenProposal) {
				$(element).append("<span class='cal-date-chk' title='Current proposal has reservations on this date'></span>");
			}
			if (event.sor >= 100) {
                $('td[data-date=\"' + $.fullCalendar.formatDate(event.start, 'yyyy-MM-dd') + '"]').css("background", "#ffd6e4");
			}
	    }
	});
	
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
	
	var currentDate = $("#calendar").fullCalendar('getDate');
	var currentCalYear = currentDate.getFullYear();
	var currentCalMonth = currentDate.getMonth();
	var currentCalFirstDay = new Date(currentCalYear, currentCalMonth, 1);
	var currentCalLastDay = new Date(currentCalYear, currentCalMonth + 1, 0);
	var toggleStartDate = $.fullCalendar.formatDate(currentCalFirstDay, "MM/dd/yyyy");
	var toggleEndDate = $.fullCalendar.formatDate(currentCalLastDay, "MM/dd/yyyy");
		
	modifyCalendarOption();
	
	$('#previousMonth').click(function() {
   	 	if(viewStatus == 0) {
			$('#calendar').fullCalendar('prev');
			var date = $("#calendar").fullCalendar('getDate');
			var calYear = date.getFullYear();
			var calMonth = date.getMonth();
			var calFirstDay = new Date(calYear, calMonth, 1);
			var calLastDay = new Date(calYear, calMonth + 1, 0);
			toggleStartDate = $.fullCalendar.formatDate(calFirstDay, "MM/dd/yyyy");
	 		toggleEndDate = $.fullCalendar.formatDate(calLastDay, "MM/dd/yyyy");
			
		}else {
			var tSDate = $.fullCalendar.parseDate(toggleStartDate);
			var calYear = tSDate.getFullYear();
			var calMonth = tSDate.getMonth();
			
			var tSNextMonthDate = new Date(calYear,calMonth-1, 1);
			
			var calYearPre = tSNextMonthDate.getFullYear();
			var calMonthPre = tSNextMonthDate.getMonth();
			
			var calFirstDayPre = new Date(calYearPre, calMonthPre, 1);
			var calLastDayPre = new Date(calYearPre, calMonthPre + 1, 0);
			
			toggleStartDate = $.fullCalendar.formatDate(calFirstDayPre, "MM/dd/yyyy");
	 		toggleEndDate = $.fullCalendar.formatDate(calLastDayPre, "MM/dd/yyyy");
			
			$('#calendatContainer table .fc-header-title h2').text(monthNames[calMonthPre] +' ' + calYearPre);
			
			displayListView(toggleStartDate,toggleEndDate);
		}
	});
	
	$('#nextMonth').click(function() {
		if(viewStatus == 0) {
			$('#calendar').fullCalendar('next');
			var date = $("#calendar").fullCalendar('getDate');
			var calYear = date.getFullYear();
			var calMonth = date.getMonth();
			var calFirstDay = new Date(calYear, calMonth, 1);
			var calLastDay = new Date(calYear, calMonth + 1, 0);
			toggleStartDate = $.fullCalendar.formatDate(calFirstDay, "MM/dd/yyyy");
	 		toggleEndDate = $.fullCalendar.formatDate(calLastDay, "MM/dd/yyyy");
			
		}else {
			var tSDate = $.fullCalendar.parseDate(toggleStartDate);
			var calYear = tSDate.getFullYear();
			var calMonth = tSDate.getMonth();
			
			var tSNextMonthDate = new Date(calYear,calMonth+1, 1);
			
			var calYearPre = tSNextMonthDate.getFullYear();
			var calMonthPre = tSNextMonthDate.getMonth();
			
			var calFirstDayPre = new Date(calYearPre, calMonthPre, 1);
			var calLastDayPre = new Date(calYearPre, calMonthPre + 1, 0);
			
			toggleStartDate = $.fullCalendar.formatDate(calFirstDayPre, "MM/dd/yyyy");
	 		toggleEndDate = $.fullCalendar.formatDate(calLastDayPre, "MM/dd/yyyy");
			
			$('#calendatContainer table .fc-header-title h2').text(monthNames[calMonthPre] +' ' + calYearPre);
			
			displayListView(toggleStartDate,toggleEndDate);
		}
	});
	
	$('#calendarView').click(function() {
		viewStatus = 0;
		$(".select-cal-view").css("background-image","url('../images/gridview-selected.png')");
		$(".select-list-view").css("background-image","url('../images/listview-normal.png')");
    	resetCalendar(toggleStartDate,toggleEndDate);
	});
	
	$('#ListView').click(function() {
		viewStatus = 1;
		$(".select-list-view").css("background-image","url('../images/listview-selected.png')");
		$(".select-cal-view").css("background-image","url('../images/gridview-normal.png')");
    	displayListView(toggleStartDate,toggleEndDate);
	});
});

function modifyCalendarOption() {
	var calendarDiv = $("#calendarOptionMenu").html();
	$('#calendatContainer table .fc-header-right').empty().html(calendarDiv);

}

function resetCalendar(toggleStartDate,toggleEndDate) {
	$('#calendatContainer .fc-content').show();
	var splitStartDate = toggleStartDate.split("/");
	$('#calendar').fullCalendar( 'gotoDate', splitStartDate[2], splitStartDate[0]-1, splitStartDate[1]);
	$('#listViewData').hide();
}

function displayListView(toggleStartDate,toggleEndDate) {
	var calendarInstance = $('#calendatContainer .fc-content').css('display','none');
	var productId = $("#productId", "#lineItemDetail").val();
	var proposalId = $("#proposalId", "#lineItemDetail").val();
	var salesTargetId = $("#salesTargetId", "#lineItemDetail").val();
	
	var ajaxReq = new AjaxRequest();
	ajaxReq.dataType = 'html';
	ajaxReq.type = "POST";
	ajaxReq.url = "../reservations/listView.action";
	
	 ajaxReq.data = {
			startDate: toggleStartDate,
			endDate: toggleEndDate,
			productId: $("#productId", "#lineItemDetail").val(),
			proposalId: $("#proposalId", "#lineItemDetail").val(),
			salesTargetId: $("#salesTargetId", "#lineItemDetail").val(),
			lineItemTargetingData: $("#lineItemTargetingData", "#lineItemDetail").val()
     };
    
	ajaxReq.success = function(result, status, xhr){
		$('#listViewData').show();
		$('#listViewData').html(result);
	}
	ajaxReq.submit();
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
			var url =  sosURL + "/cgi-bin/sos/sales/calc.pl?SALESORDER_ID="+orderId; 
			var width = screen.width - 100;
		    var height = screen.height - 100;
			window.open(url, "order" + orderId, 'left=50,top=0,width=' + width + ',height=' + height + ',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
	    }
}
