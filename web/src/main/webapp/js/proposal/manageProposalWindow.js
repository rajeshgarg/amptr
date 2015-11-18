/**
 * @author amandeep.singh
 */

$(document).ready(function () {
	
	 window.onbeforeunload = function(){
	        if (childwindow) {
	            var proposalIds = childwindow.split(",");
	            $.each(proposalIds, function(index, value){
	                if (value != ',') {
	                    window[value].close();
	                }
	            });
	        }
			  if (calendarwindow) {
	            var winId = calendarwindow.split(",");
	            $.each(winId, function(index, value){
	                if (value != ',') {
	                    window[value].close();
	                }
	            });
	        }
			if (orderChildwindow) {
				var orderIds = orderChildwindow.split(",");
				$.each(orderIds, function(index, value){
					if (value != ',') {
						window[value].close();
					}
				});
			}
			if (windowObjectReference) {
				windowObjectReference.close();
			}
	    };
});

/**
 * Opens a window with multiple calendars.
 */
var windowObjectReference = null;
function viewMultipleCalendarsWindow() {
	if (windowObjectReference == null || windowObjectReference.closed) {
		var url = "../manageReservation/viewMultipleCalendar.action";
		var width = screen.width - 100;
		var height = screen.height - 100;
		windowObjectReference = window.open(url, "multipleCalendarsLink", 'left=50,top=0,width=' + width + ',height=' + height
			+ ',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
	} else {
		windowObjectReference.focus();
	}
}
/**
 * opens a new window or opens the already opened window.
 * @param id
 * @return
 */
var childwindow = "";
var isFocus = false;
function openProposal(proposalID){
	var flag = false;
    // opens a new window 
    if (childwindow == "") {
        childwindow += "proposal" + proposalID + ",";
        // stores the window properties in the dynamic variable
        window["proposal" + proposalID] = openNewWindow(proposalID);
        childwindow = childwindow.substring(0, childwindow.lastIndexOf(","));
    }
    else {
        try {
            var abc = "";
            var proposalIds = childwindow.split(",");
            $.each(proposalIds, function(index, value){
                if (value != ',') {
                    // if window is already opened set flag to true
                    if (value == ("proposal" + proposalID)) {
                        flag = true;
                    }
                }
            });
            // if the window is not opened , opens a new window
            if (flag == false) {
                childwindow += ",proposal" + proposalID + ",";
                window["proposal" + proposalID] = openNewWindow(proposalID);
                childwindow = childwindow.substring(0, childwindow.lastIndexOf(","));
            }
            else {
                // if the window has been closed after opening 
                if (window["proposal" + proposalID].closed) {
                    var values = childwindow.split(",");
                    $.each(values, function(index, value){
                        if (value != ',') {
                            if (value != ("proposal" + proposalID)) {
                                abc += value + ",";
                                
                            }
                            else {
                                abc += "proposal" + proposalID + ",";
                                window["proposal" + proposalID] = openNewWindow(proposalID);
                                window["proposal" + proposalID].focus();
                            }
                        }
                    });
                    abc = abc.substring(0, abc.lastIndexOf(","));
                    childwindow = abc;
                }
                else {
                    // reopens the already opened window
                    window["proposal" + proposalID].focus();
                    isFocus = true;
                }
            }
        } 
        catch (e) {
        	openNewWindow(proposalID);
        }
    }
}

/**
 * Opens a proposal in new Window
 * 
 * @param {Object} proposalID
 */
function openNewWindow(proposalID){
	var url =  $("#contextPath").val() + "/manageProposal.action?_flowId=workOnProposal&proposalId="+proposalID;
	var width = screen.width - 100;
	var height = screen.height - 100;
	return window.open(url, "proposal"+proposalID, 'left=50,top=0,width='+width+',height='+height+',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
}

/**
 * Open Calendar Window
 */
var calendarwindow = "";
function viewCalanderWindow (params) {
	var width = screen.width - 150;
	var height = screen.height - 150;
	calendarwindow= "ReservationCalendar";
    var win = window.open($("#contextPath").val() + "/reservations/viewCalendar.action?" + params, "ReservationCalendar", "left=50,top=0,width=" + width + ",height=" + height + ",toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no");
	win.focus();
	window["ReservationCalendar"] = win;
}

/**
 * opens a new window or opens the already opened window.
 * @param orderID
 * @return
 */
var orderChildwindow = "";
var isInFocus = false;
function openOrder(orderID, sosURL){
	var flag = false;
    // opens a new window 
    if (orderChildwindow == "") {
    	orderChildwindow += "order" + orderID + ",";
        // stores the window properties in the dynamic variable
        window["order" + orderID] = openNewOrderWindow(orderID, sosURL);
        orderChildwindow = orderChildwindow.substring(0, orderChildwindow.lastIndexOf(","));
    }
    else {
        try {
            var abc = "";
            var proposalIds = orderChildwindow.split(",");
            $.each(proposalIds, function(index, value){
                if (value != ',') {
                    // if window is already opened set flag to true
                    if (value == ("order" + orderID)) {
                        flag = true;
                    }
                }
            });
            // if the window is not opened , opens a new window
            if (flag == false) {
            	orderChildwindow += ",order" + orderID + ",";
                window["order" + orderID] = openNewOrderWindow(orderID, sosURL);
                orderChildwindow = orderChildwindow.substring(0, orderChildwindow.lastIndexOf(","));
            }
            else {
                // if the window has been closed after opening 
                if (window["order" + orderID].closed) {
                    var values = orderChildwindow.split(",");
                    $.each(values, function(index, value){
                        if (value != ',') {
                            if (value != ("order" + orderID)) {
                                abc += value + ",";
                                
                            }
                            else {
                                abc += "order" + orderID + ",";
                                window["order" + orderID] = openNewOrderWindow(orderID, sosURL);
                                window["order" + orderID].focus();
                            }
                        }
                    });
                    abc = abc.substring(0, abc.lastIndexOf(","));
                    orderChildwindow = abc;
                }
                else {
                    // reopens the already opened window
                    window["order" + orderID].focus();
                    isInFocus = true;
                }
            }
        } 
        catch (e) {
        	openNewOrderWindow(orderID, sosURL);
        }
    }
}

/**
 * Opens a order in new Window
 * 
 * @param {Object} orderId
 */
function openNewOrderWindow(orderId, sosURL){
	var url =  sosURL + "/cgi-bin/sos/sales/calc.pl?SALESORDER_ID="+orderId;
	var width = screen.width - 100;
	var height = screen.height - 100;
	return window.open(url, "order"+orderId, 'left=50,top=0,width='+width+',height='+height+',toolbar=0,resizable=1,menubar=0,scrollbars=1,location=no');
}