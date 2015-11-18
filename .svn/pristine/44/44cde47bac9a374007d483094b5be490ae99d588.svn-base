<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/spec/manageAttribute.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageAttributeContainer">		
	<table id="manageAttributeTable" class="master"></table>
	<div id="manageAttributePager"></div>	
	<div class="spacer7"></div>
	<div id="manageAttributeDetailContainer" style="display: none;">
		<div id="manageAttributeTab">
			<ul>
			     <li id="attributeDetailFormTab-1"><a href="#attribute-tabs-1"><spring:message code="tab.generic.attributeDetails"/></a></li>
		    </ul>
		    <div id="attribute-tabs-1">
				<jsp:include page="attributeDetails.jsp"></jsp:include>
			</div>	
		</div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="attributSearchPanel">
		<select id="attributSearchOption">
			<option value="attributeName"><spring:message code="label.generic.name"/></option>
			<option value="attributeTypeStr"><spring:message code="label.generic.type"/></option>
		</select>
		<input id="attributSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>