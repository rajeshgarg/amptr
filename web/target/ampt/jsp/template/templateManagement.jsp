<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
 
<script type="text/javascript" src="../js/template/templateManagement.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="templateManagementContainer">		
	<table id="templateManagementTable" class="master"></table>
	<div id="templateManagementPager"></div>	
	<div class="spacer7"></div>
	<div id="templateManagementDetailContainer">
		<div id="templateManagementTab">
			<ul>
			     <li id="templateManagementDetailFormTab-1">
			     	<a href="#template-tabs-1">
			     		<spring:message code="tab.generic.manageTemplates" />
			     	</a>
			     </li>
		    </ul>
		    <div id="template-tabs-1" style="display: block;">
		    	<form:form id="templateManagementForm" name="templateManagementForm" modelAttribute="templateManagementForm" action="../templateManagement/uploadCustomTemplate.action" method="post">
					<jsp:include page="templateManagementDetails.jsp"></jsp:include>
					<form:hidden path="templateJsonData" id="templateJsonData" />
					<form:hidden path="sheetName" id="sheetName" />
					<form:hidden path="sheetID" id="sheetID" />
					<form:hidden path="templateId" id="templateId"/>
					<form:hidden path="editAction" id="editAction" />
					<form:hidden path="useExistingRow" id="useExistingRow" />
				</form:form>
			</div>	
		</div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px;  display: none" id="templateSearchPanel">
		<select id="templateSearchOption">
			<option value="templateName"><spring:message code="label.generic.file.name"/></option>
			<option value="templateFileName"><spring:message code="label.generic.template.name"/></option>
			<option value="updatedBy"><spring:message code="label.generic.template.updatedBy"/></option>
		</select>
		<input id="templateSearchValue" type="text" size="20" class="search-icon search-input" />
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>

