<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<link rel="shortcut icon" href="http://css.nyt.com/images/icons/nyt.ico" /> 
		<link rel="stylesheet" href="../css/jquery.ui.base.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/override.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/fullcalendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		<link rel="stylesheet" href="../css/reservation.calendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" type="text/css" />
		
		<script type="text/javascript" src="../js/plugins/jquery/jquery-1.6.1.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery/jquery-ui-1.8.7.js"></script>
		<script type="text/javascript" src="../js/plugins/multi-select/jquery.multiselect.filter.js"></script>
		<script type="text/javascript" src="../js/plugins/multi-select/multi-select.js"></script>
		<script type="text/javascript" src="../js/plugins/fullcalendar/fullcalendar.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.qtip.js"></script>
		<script type="text/javascript" src="../js/plugins/jquery.select2.js"></script>
		<script type="text/javascript" src="../js/common/base-templates.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/common/common-form.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/proposal/manageProposalWindow.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/reservation/viewMultipleCalendar.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
		<script type="text/javascript" src="../js/reservation/singleCalendarInstance.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
	</head>
	<body style="overflow-x: hidden;">
		<div id="multipleCalParent" class="multipleCalDesign">
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
			<form:form id="multipleCalendarsForm" name="multipleCalendarsForm" modelAttribute="multipleCalendarForm">
				<input type="hidden" value="${userFullName}" id="userName"/>
				<div style="display: none" id="runtimeDialogDiv"></div>
				<input type="hidden" id="contextPath" value="<%= request.getContextPath() %>" />
				<input type="hidden" id="hiddenFilterId" />
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
				<div id="messageHeaderDiv" class="error" style="margin-left:25px;margin-top:12px;"></div>
				<div class="multiple-calendar-menu">
					<div style="float:left;margin-right:5px;" id="filterPlugin">
						<label class="label-bold" style="vertical-align:-2px;margin-right:47px;display: inline-block;width: 70px;">Filter List:</label>  
						<span id="filterIdDiv">
							<form:select  id="filterId" path ="filterId" cssStyle="width:384px;" onchange="showfilteredData()">
								<form:option value=""></form:option>	
								<c:forEach  items="${allFilters}"  var="entry" >
									<optgroup  label="${entry.key}"> 	
									<c:forEach  var="filters" items="${entry.value}">
										<form:option value="${filters.filterId}">${filters.filterName}</form:option>
									</c:forEach>	
									</optgroup>
								</c:forEach>
							</form:select>
						</span>
					</div>
					<div style="float:left;margin-right:5px;display:none;" id="filtertextId">
				<label class="label-bold" style="margin-right:47px;display: inline-block;width: 70px;">Filter Name:</label>
					<input type="text" id="filterText" style="width:378px;padding:3px 0;" /></div>
						<span class="multiple-calendar-icons" id="addFiltercriteria">
							<a href="javascript:void(0)" class="ui-icon ui-icon-plus" title="Add filter"  onclick="addFilterCriteria('add')"></a>
						</span>
						<span id="editAndDelete" style="display: none;">
							<span class="multiple-calendar-icons" id="editFiltercriteria">
								<a href="javascript:void(0)" class="ui-icon ui-icon-pencil" title="Edit filter" onclick="addFilterCriteria('edit')"></a>
							</span>
							<span class="multiple-calendar-icons" id="deleteFiltercriteria">
								<a href="javascript:void(0)" class="ui-icon ui-icon-trash" title="Delete filter" onclick="deleteFilters()"></a>
							</span>
						</span>
						<span id="saveAndCancel" style="display: none;">
							<span class="multiple-calendar-icons" id="saveFiltercriteria">
								<a href="javascript:void(0)" class="ui-icon ui-icon-disk" title="Save filter" onclick="saveFilters()"></a>
							</span>
							<span class="multiple-calendar-icons" id="cancelFiltercriteria">
								<a href="javascript:void(0)" class="ui-icon ui-icon-close" title="Cancel" onclick="resetFilters()"></a>
							</span>
						</span>
					<div style="float:right">
						<label class="label-bold">Number of Calendars : </label> 
						<select id="numberOfCalendarsToAdd" name="inputs" style="width: 60px;">
							<c:forEach var="i" begin="1" end="9">
								<option value="${i}">${i}</option>
							</c:forEach>
						</select>
						<a id="addCalendarBox" class="save-btn" href="javascript:void(0)" onclick="addCalendarBox()">Add</a>
					</div>
					</div>
				<div id="multipleCalendar"></div>
			</form:form>
		</div>
	</body>
</html>