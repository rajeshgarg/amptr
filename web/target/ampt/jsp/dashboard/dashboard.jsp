<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="../css/dashboard.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" />
<link rel="stylesheet" type="text/css" href="../css/jquery.jqplot.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" />
<link rel="stylesheet" type="text/css" href="../css/fullcalendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" />
<link rel="stylesheet" type="text/css" href="../css/reservation.calendar.css?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />" />

<script type="text/javascript" src="../js/plugins/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.cursor.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.dateAxisRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript" src="../js/plugins/jqplot/plugins/jqplot.pointLabels.min.js"></script>
<script type="text/javascript" src="../js/plugins/fullcalendar/fullcalendar.js"></script>
<script type="text/javascript" src="../js/dashboard/dashboard.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="dashboardContainer">
	<div class="dashboard-filter">
		<div class="apply-filter-header">
			<h4>Apply Filters</h4>
			<!--  <img id="show-hide" src="../images/arrow.png" class="open-close" title="Close"></img>  -->
		</div>
		<form:form action="../dashboard/saveFilterData.action" id="dashboardFilterForm"  name="dashboardFilterForm" method="post" modelAttribute="dashboardFilterForm">
			<table width="100%" cellpadding="3px">
				<tr class="saved-filter-row">
					<td>
						<select id="savedFilter" name="savedFilter" style="width:86%;float:left;">
							<option value=""></option>
							<c:forEach items="${savedFilter}" var="entry">
								<option value="${entry.key}">${entry.value}</option>
							</c:forEach>
						</select>
						<label class="filter-icon delete-filter-inactive" title="Delete" id="deleteFilter"></label>
					</td>
				</tr>
				<tr>
					<td><label class="label-bold">Sales Category:</label></td>
				</tr>
				<tr>
					<td>
						<div id="salesCategory_custom" class="multi-select-box" style="width:86%;float:left;">
							<select id="salesCategory" name="salesCategory" multiple="multiple">
								<c:forEach items="${salesCategory}" var="entry">
									<option value="${entry.key}">${entry.value}</option>
								</c:forEach>
							</select>							
						</div>
						<label class="filter-icon filter-sales" title="My Sales Category" id="mySalesCategory"></label>
					</td>
				</tr>
				<tr>
					<td><label class="label-bold">User:</label></td>
				</tr>
				<tr>
					<td>
						<div id="user_custom" class="multi-select-box">
							<select id="user" name="user" multiple="multiple">
								<c:forEach items="${userMap}" var="entry">
									<option value="${entry.key}">${entry.value}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td><label class="label-bold">Priority:</label></td>
				</tr>
				<tr>
					<td>
						<div id="priority_custom" class="multi-select-box">
							<select id="priority" name="priority" multiple="multiple">
								<c:forEach items="${priority}" var="entry">
									<option value="${entry.key}">${entry.value}</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td><label class="label-bold">Due Date:</label></td>
				</tr>
				<tr>
					<td>
						<select id="dueDate" name="dueDate" style="width: 100%">
							<option value="">&nbsp;</option>
							<c:forEach items="${dateCriteria}" var="entry">
								<option value="${entry.key}">${entry.value}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" value="Apply" class="dashboard-btn" id="applyDashboardFilterBtn"/>
						<input type="button" value="Save" class="dashboard-btn" id="saveDashboardFilterBtn"/>
						<input type="button" value="Reset" class="dashboard-btn" id="resetDashboardFilterBtn"/>
					</td>
				</tr>
			</table>
			<div class="save-filter-input" style="display: none;" id="dashboardFilterNameDiv">
				<span class="mandatory" style="float: left;">*&nbsp;</span>
				<label class="label-bold" style="float: left; margin-bottom: 8px">Filter Name:</label>
				<input type="text" maxlength="25" style="width:82%;float:left;padding:3px 2px;" id="dashboardFilterName" name="dashboardFilterName">
				<input type="hidden" id="filterId" name="filterId">
				<span class="filter-icon filter-save" title="Save Filter" id="saveFilterOk"></span>
				<div class="clearboth spacer6"></div>
				<span class="mandatory" id="saveErorr"></span>
			</div>
		</form:form>
	</div>
	<div class="dashboard-view">
		<ul class="dashboard-nav-bar">
			<li class="views selected-view">
				<div>
					<input type="radio" name="dashboardView" id="plannerView" checked="checked"/><label class="label-bold" for="plannerView">Bar View</label>
				</div>
				<i class="d-arrow"></i>
			</li>
			<li class="views">
				<div>
					<input type="radio" name="dashboardView" id="statusView" /><label class="label-bold" for="statusView">Status View</label>
				</div>
				<i class="d-arrow"></i>
			</li>
			<li class="views">
				<div>
					<input type="radio" name="dashboardView" id="calendarView" /><label class="label-bold" for="calendarView">Calendar View</label>
				</div>
				<i class="d-arrow"></i>
			</li>
			<li class="dashboard-legend">
				<div>
					<span class="dashboard-legend-container">
						<span class="legend-green"></span>
					</span>
					<label class="label-bold">Ahead Of Schedule</label>
				</div>
				<div>
					<span class="dashboard-legend-container">
						<span class="legend-red"></span>
					</span>
					<label class="label-bold">Behind Schedule</label>
				</div>
			</li>
			<li class="refresh-label">
				<div class="margin-0">
					<label class="label-bold legend-label">Auto refresh in </label>
					<span id="dashboardCounter">00 : 00</span>
				</div>	
			</li>
		</ul>
		<div id="dashboardChart" class="dashboard-status-chart"></div>
		<div class="dashboard-data-grid">
			<table id="dashboardDataGrid"></table>
			<div id="dashboardDataGridPager"></div>
		</div>	
	</div>
</div>