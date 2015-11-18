<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title><spring:message code="title.generic.calendarTitle"/></title>
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" />
		<link rel="stylesheet" href="../css/fullcalendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/jquery.ui.base.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/reservation.calendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<script type="text/javascript" src="../js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery/jquery-ui-1.8.7.js"></script>
		<script type="text/javascript" src="../js/plugins/fullcalendar/fullcalendar.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../js/common/base-templates.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>	
		<script type="text/javascript" src="../js/reservation/reservationCalendar.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/plugins/jquery.qtip.js"></script>
	</head>
	<body>
		<div id="edit-proposal-container">
			<div id="header">		
				<dl>
					<dt class="hdr-lt logo-nyt">
						<div class="logo"></div>
					</dt>
					<dt class="hdr-rt">						
						<span class="close" id="close-button" title="Close Calendar">
							<a title="Close Calendar"></a>
						</span>
					</dt>
				</dl>				
			</div>
			<div style="clear:both"></div>
			<h1>Reservation Calendar</h1>
			<div id="lineItemDetail" class="calendar-lineItem-detail">
				<c:if test="${isProductInactive == true}">
					<div class="error">The Product ${productInactiveName} is inactive.</div>
				</c:if>
				<c:if test="${isSalesTargetInactive ==  true}">
					<div class="error">The Sales Target ${salesTargetInactiveName} is inactive.</div>
				</c:if>
				<p style="margin-bottom:6px">
					<label class="label-bold" style="margin-right:5px">Product:</label>
					<span title="${productName}" class="label-value">
						<label>${productName}</label>
						<input type="hidden" id="productType" value="${productType}" />
						<input type="hidden" id="productId" value="${calendarForm.productId}" />
						<input type="hidden" id="proposalId" value="${calendarForm.proposalId}" />
						<input type="hidden" id="lineItemId" value="${calendarForm.lineItemId}" />
						<input type="hidden" id="reservationContextPath" value="<%= request.getContextPath() %>" />
					</span>
					<label class="label-bold" style="margin-right:5px">Sales Target:</label>
					<span title="${salesTargetName}" class="label-value">
						<label>${salesTargetName}</label>
						<input type="hidden" id="salesTargetId" value="${calendarForm.salesTargetId}" />
					</span>
					<label class="label-bold" style="margin-right:5px">Flight:</label>
					<span title="${calendarForm.startDate} - ${calendarForm.endDate}">
						<label>${calendarForm.startDate} - ${calendarForm.endDate}</label>
						<input type="hidden" id="startDate" value="${calendarForm.startDate}" />
						<input type="hidden" id="endDate" value="${calendarForm.endDate}" />
					</span>
				</p>
				<p>
					<label style="vertical-align:4px \0/IE;margin-right:5px" class="label-bold">Targeting String:</label>
					<span class="target-string-label" title="${targetingString}">${targetingString}</span>
					<input type="hidden" id="lineItemTargetingData" value="${calendarForm.lineItemTargetingData}" />
				</p>
				<div class="clear"></div>
			</div>
			<div style="clear:both"></div>
			<!-- Reservation Detail : contains reservation detail for a particular day -->
			<div id="reservationDetail" class="reservation-detail" style="display:none;width:93%;"></div>
			
			<!-- Calendar -->
			<div id="calendatContainer" class="calendar-container">
				<div id="calendar" class="calendar-view"></div>
			</div>	
			
			<div id="listViewData" style="display:none">
			
			</div>
			
			<div id="calendarOptionMenu" style='display:none'>
				<div class="toggle-views">
					<span id="ListView" title='<spring:message code="title.generic.listView"/>' class="fc-button fc-button-prev select-list-view fc-corner-left" unselectable="on" style="-moz-user-select: none;"></span>
					<span id="calendarView" title='<spring:message code="title.generic.calendarView"/>' class="fc-button fc-button-next select-cal-view fc-corner-right" unselectable="on" style="-moz-user-select: none;"></span>
				</div>
				<div class="toggle-month">
					<span id="previousMonth" class="fc-button fc-button-prev fc-state-default fc-corner-left" unselectable="on" style="-moz-user-select: none;">
						<span class="fc-text-arrow">‹</span>
					</span>
					<span id="nextMonth" class="fc-button fc-button-next fc-state-default fc-corner-right" unselectable="on" style="-moz-user-select: none;">
						<span class="fc-text-arrow">›</span>	
					</span>
				</div>
			</div>					
			
			<!-- Utility Section : contains element for session timeout, Block UI for AJAX request -->
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
	</body>
</html>
