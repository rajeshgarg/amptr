<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title><spring:message code="title.generic.calendarTitle"/></title>
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" /> 
		<link rel="stylesheet" href="../css/jquery.ui.base.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/jquery.multiselect.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/jquery.multiselect.filter.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/fullcalendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/reservation.calendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<script type="text/javascript" src="../js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery/jquery-ui-1.8.7.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../js/common/base-templates.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/common/common-form.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script> 		
 		<script type="text/javascript" src="../js/plugins/multi-select/jquery.multiselect.filter.js"></script>
		<script type="text/javascript" src="../js/plugins/multi-select/multi-select.js"></script>
		<script type="text/javascript" src="../js/reservation/abstract-reservation-calendar.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/plugins/jquery.form.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.qtip.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.select2.js"></script>
	</head>
	<body style="background-color: #ffffff;" class="abstract-reservation-calendar">
		<div id="header" class="pane ui-layout-north ui-layout-pane ui-layout-pane-north" style="display: block;  visibility: visible;  height: 45px !important;">
			<dl onClick="closeMe()">
				<dt class="hdr-lt"><img src="../images/logo.png" height="35"></dt>
				<dt class="calendar-hdr-rt">
					<span class="close" id="close-button"
						title="Close Calendar"> <a title="Close Calendar"></a>
					</span>
				</dt>
			</dl>
		</div>
		<div style="clear:both"></div>
		<div>
			<h1 class="pageHeader">Reservation Calendar</h1>
		</div>
		<div style="clear:both"></div>
		 <div id="filterCriteria">
		 	<form:form id="salesCalendarForm" name="salesCalendarForm" action="../reservations/getSalesCalendarResult.action" modelAttribute="salesCalendarReservationDTO">
		 		<div id="messageHeaderDiv" class="error" style="width : 60%; float : none;margin-bottom:15px;"></div>
		 		
		 				 		
		 		<div class="productRadioButton">
					<span class="item-std-reserve">
						<input type="radio" name="productType" id="productType_H" onclick="setInitialData('H')" checked="checked"></input>
						<label style="vertical-align:1px;" class="label-bold">Home Page</label>
						<i class=""></i>
					</span>	
					<span class="item-std-reserve">
						<input type="radio" name="productType" id="productType_C" onclick="setInitialData('C')"></input>
						<label style="vertical-align:1px;" class="label-bold">Cross Platform</label>
						<i class=""></i>
					</span>					
				</div>
				<div style="margin-left:2%">
					<span style="display: inline-block; vertical-align: middle;">
						<span id ="productLabel" class="mandatory">*  </span><label id ="productLabel" class="label-bold">Product</label>
						<span id ="salesTargetLabel" ></span><label id ="salesTargetLabel" class="label-bold">Sales Target</label>
					</span>
					<div id="productMultiSelect_custom" class="multi-select-box">
						 <form:select multiple="true" size="5" path="productIds" id="productMultiSelect">
						 	<form:option value=""></form:option>
						 </form:select>
					 </div>
					  <select  id="allProducts" style="display: none;">
						<c:forEach  items="${productMap}"  var="entry" >
							<optgroup  label="${entry.key}"> 	
								<c:forEach  var="product" items="${entry.value}">
									<c:choose>
										<c:when test="${entry.key == 'HOME PAGE'}">
											<option data-type="H" value="${product.productId}">${product.displayName}</option>
										</c:when>
										<c:otherwise>
											<option data-type="C" value="${product.productId}">${product.displayName}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>	
							</optgroup>
						</c:forEach>
					</select> 
					
					<div id="salesTargetMultiSelect_custom" class="multi-select-box">
						 <form:select multiple="true" size="5" path="salesTargetIds" id="salesTargetMultiSelect">
						 </form:select>
					 </div>
					 
					 
				</div>
				<div>
					<span style="display: inline-block;   vertical-align: middle;">
						<span class="mandatory"> </span><label class="label-bold">Countries</label>
					</span>
					<div id="countriesMultiSelect_custom" class="multi-select-box">
					<form:select multiple="true" size="5" path ="countries" id="countriesMultiSelect">
					    <c:forEach  items="${countryMap}"  var="entry" >
							<optgroup  label="${entry.key}"> 	
								<c:forEach  var="country" items="${entry.value}">
									<form:option value="${country.id}">${country.name}</form:option>
								</c:forEach>	
							</optgroup>
						</c:forEach>
					 </form:select> 
					 
					 </div>
				</div>
				<div>
					<span style="display: inline-block;   vertical-align: middle;">
						<span class="mandatory">*  </span><label class="label-bold">Month</label>
					</span>
					<select  size="5"  id="dateMultiSelect" style="width: 50%">
						<option value=""></option>
						<c:forEach  var="dates" items="${datesMap}">
							<option value="${dates.key}">${dates.value}</option>
						</c:forEach>
					</select>
					<form:hidden path = "dateString" id="dateString" />
				</div>
				<div>
					<span style="display: inline-block;  vertical-align: middle;">
						<span class="mandatory"></span><label class="label-bold">Weekday</label>
					</span>
					 <form:select  size="5" path ="weekDay" id="weekdayMultiSelect" cssStyle="width: 50%">
					       <form:option value=""></form:option>
							<form:options items="${weekdayMap}" />	
					 </form:select>
				</div>
				<div style="width:3%;">
					<input type="button" class="save-btn reset-save-btn" id="goButton" value="Go" >
				</div>
			</form:form>
		</div>
		<div style="clear:both"></div>
		<div id="monthDetail">
			<div style="float:left;margin-left:3%;"><label class="label-bold" style="font-size:16px;" id="monthName"></label></div>
			<div id="calendarOptionMenu">
				<div class="toggle-month">
					<span id="previousMonth" class="fc-button fc-button-prev fc-state-default fc-corner-left" unselectable="on" style="-moz-user-select: none;" onClick="getDataByNavigation(-1)">
						<span class="fc-text-arrow">‹</span>
					</span>
					<span id="nextMonth" class="fc-button fc-button-next fc-state-default fc-corner-right" unselectable="on" style="-moz-user-select: none;" onClick="getDataByNavigation(1)">
						<span class="fc-text-arrow">›</span>	
					</span>
				</div>
			</div>
			
			<div id="filterCalendarMultiSelect_custom" class="multi-select-box">
				<select multiple="multiple" id="filterCalendarMultiSelect"size="5" onChange="filterCalendar()">
					<option value="Sold">Sold</option>
					<option value="Hold">Reserved</option>
					<option value="AvailableDates">Available Dates</option>
					<option value="Vulnerable">Vulnerable</option>
					<option value="Overbooked">Overbooked</option>
				</select>
			</div>
			<span style="display: inline-block; vertical-align: middle;float:right;margin:2.3% 0.2% 0% 0%;">
				<label class="label-bold">Filters</label>
			</span>
		</div>
		<div style="clear:both"></div>
		<div style="clear:both" id="monthData"></div>
		
		<div id="completeCalendar">
		</div>
		<div style="display: none" id=sessionTimeout title="Session Timeout">
				<table cellpadding="2" cellspacing="2">
					<tr>
						<td>
							<img src='../images/sessiontimeout.png' height="32px" width="32px" />
						</td>
						<td style='line-height: 2; vertical-align: top;'>
							<spring:message	code="message.generic.sessionTimeout" />
						</td>
					</tr>
				</table>
			</div>
			<div id="loader" style="display: none" class="main_loader_sec">
				<div class="loader_block">				
					<div class="loader_text"><spring:message code="label.generic.loader"/></div>
				</div>
			</div>
			<div style="display: none" id="failedSavedDialogDiv" title="Error"></div>
			<input type="hidden" id="contextPath" value="<%= request.getContextPath() %>" />
		</div>
		
		<div id="proposed-lineItems"></div>
		<div style="display: none" id="runtimeDialogDiv"></div>
		
	</body>
</html>