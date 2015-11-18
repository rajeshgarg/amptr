
function handleEnterKey(e) {
	if (null == e){
		e = window.event ;
	}
	if (e.keyCode == 13)  {
	    LoginMe();
	}
}

function LoginMe() {
	//alert("user name "+document.getElementById('j_username').value);
	if($.trim($('#j_username').val()) == '' && $.trim($('#j_password').val()) == '' ){
		$("#alerts div").text("Please enter username and password");
		document.getElementById('alerts').style.visibility = '';
		return;
	}
	if($.trim($('#j_username').val()) == ''){
		$("#alerts div").text("Enter your user id.");
		document.getElementById('alerts').style.visibility = '';
		return;
	}
	if($.trim($('#j_password').val()) == ''){
		$("#alerts div").text("Enter your password.");
		document.getElementById('alerts').style.visibility = '';
		return;
	}
	document.forms[0].submit();
}

function resetMe(){
	document.forms[0].reset();
	document.getElementById('alerts').style.visibility = 'hidden';
}

function showReservationCalendar(){
	var reservationCalendarWindow = window.open("./reservations/displayReservationCalendar.action");
}