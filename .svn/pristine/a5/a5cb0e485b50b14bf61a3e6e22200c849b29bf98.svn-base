<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript" 	src="../js/emailSchedule/manageEmailSchedule.js"></script>

<div id="manageEmailScheduleContainer">
	<table width="80%" cellpadding="2" cellspacing="2" align="center">
		<tr>
			<td width="15%">&nbsp;</td>
			<td>
				<label class="label-bold"> <spring:message	code="label.generic.product" />: </label>
			</td>
			<td> 
				<select name="product" id="product" style="width: 250px;">
					<option value=""></option>
					<c:forEach items="${productMap}" var="entry">
						<option value="${entry.key}">${entry.value}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<label class="label-bold"> <spring:message	code="label.generic.salesTarget" />: </label>
			</td>
			<td> 
				<select name="salesTarget" id="salesTarget" style="width: 250px;" ></select>
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="hidden" id="emailScheduleConfiguredDates">				
			</td>
		</tr>
	</table>
	<div class="spacer7"></div>
	<table id="manageEmailSendScheduleTable" class="master"></table>
	<div id="emailSendSchedulePager"></div>
	<div class="spacer7"></div>
	<div id="emailSendScheduleDetailsContainer" style="display: none;">
		<div id="emailSendScheduleTab" style="min-height:0 !important">
			<ul>
				<li id="emailSendScheduleFormTab-1">
					<a href="#emailSchedule-tabs-1">Email Send Schedule Details</a>
				</li>
			</ul>
			<div id="emailSchedule-tabs-1">
				<jsp:include page="manageEmailScheduleDetails.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<div width="20%" class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="emailScheduleSearchPanel">
		<select id="emailScheduleSearchOption">
			<option value="frequency">Frequency</option>
			<option value="weekDays">Weekday(s)</option>
		</select>
		<input id="emailScheduleSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
</div>