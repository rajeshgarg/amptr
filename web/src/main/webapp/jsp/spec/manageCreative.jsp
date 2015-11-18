<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/spec/manageCreative.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageCreativeContainer">		
	<table id="creativeTable" class="master"></table>
	<div id="creativePager"></div>	
	<div class="spacer7"></div>
	<div id="creativeTab" style="display: none;">
		<ul>
		     <li id="detailFormTab-1"><a href="#creative-tabs-1"><spring:message code="tab.generic.creativeDetails"/></a></li>
	    	 <li id="creativeAttributeTab-1"><a href="#creative-tabs-2"><spring:message code="tab.generic.manageAttribute"/></a></li>
	    	 <li id="creativeDocumentTab-1"><a href="#creative-tabs-3"><spring:message code="tab.generic.manageDocument"/></a></li>
	    </ul>
	    <div id="creative-tabs-1">
			<jsp:include page="creativeDetail.jsp"></jsp:include>
		</div>	
		<div id="creative-tabs-2">
			<jsp:include page="creativeAttributeMap.jsp"></jsp:include>
		</div>
		<div id="creative-tabs-3">
			<div id="creativeAttachement"></div>
		</div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px;" id="creativeSearchPanel">
		<select id="creativeSearchOption">
			<option value="name"><spring:message code="label.generic.name"/></option>
			<option value="typeStr"><spring:message code="label.generic.type"/></option>
			<option value="width"><spring:message code="label.generic.width"/></option>
			<option value="height"><spring:message code="label.generic.height"/></option>
			<option value="width2"><spring:message code="label.generic.width2"/></option>
			<option value="height2"><spring:message code="label.generic.height2"/></option>
		</select>
		<input id="creativeSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>											
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>