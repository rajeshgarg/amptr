<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="../js/pricing/manageRateProfile.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageListRateContainer">
	<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
		<tr>
			<td width="5%">&nbsp;</td>
			<td width="10%"><label class="label-bold"><spring:message code="label.generic.salesCategory"/>:</label></td>
			<td width="35%">						
				<select name="allSalesCategory" id="allSalesCategory" style="width: 350px">
					<option value=""><spring:message code="label.generic.default" /></option>
					<c:forEach items="${allSalesCategory}" var="entry">
						<option value="${entry.key}">${entry.value}</option>
					</c:forEach>	
				</select>
			</td>
			<td><input type="button" value="CLONE" id="rateProfileCloneBtn" class="reset-btn" style="margin-top: 0px !important;"/></td>								
			<td style="vertical-align: bottom;">
				<a class="cron-job-helplink" href="#" onclick="checkScheduledSectionWeightCalJob()">Trigger Section weight calculation</a>
			</td>
		</tr>
	</table>
	<div class="spacer7"></div>
	<table id="manageListRateTable" class="master"></table>
	<div id="manageListRatePager"></div>
	<div class="spacer7"></div>
	<div id="listRateDetailForm">
		<ul>
			<li id="listRateDetailFormTab-1"><a href="#rateProfile-tabs-1"><spring:message code="tab.generic.rateProfileDetails"/></a></li>
		</ul>
		<div id="rateProfile-tabs-1">
			<jsp:include page="rateProfileDetails.jsp"></jsp:include>
		</div>		
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="rateProfileSearchPanel">
		<select id="rateProfileSearchOption">
			<option value="productName"><spring:message code="label.generic.productName"/></option>
			<option value="sectionNames"><spring:message code="label.generic.section"/></option>
			<option value="salesTargetNamesStr"><spring:message code="label.generic.salesTarget"/></option>
			<option value="basePrice"><spring:message code="label.pricingrule.price"/></option>
		</select>
		<input id="rateProfileSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
	<div style="display: none" id="sectionWeightCalJobInfo" title="Job Schedule">
		<table cellpadding="5" cellspacing="5">
			<tr>
				<td><img src='../images/warning.png'></td>
				<td style='line-height:2; vertical-align: top;'><label id="weightCalJobMessage"></label></td>
			</tr>
		</table>
	</div>	
	<div style="display: none" id="sectionWeightCalJobConfirm" title="Job Schedule">
		<table cellpadding="5" cellspacing="5">
			<tr>
				<td><img src='../images/warning.png'></td>
				<td style='line-height:2; vertical-align: top;'>
					Job will scheduled at <label id="nextFireTime"></label>.<Br/>Do you want to continue?
				</td>
			</tr>
		</table>
	</div>	
	<div style="display: none;" id="rateProfileCloneForm" title="Clone Rate Profile">
		<div class="spacer7"></div>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td width="30%"><label class="label-bold">From:</label></td>
				<td>						
					<label id="cloneSalesCategoryFrom"></label>
					<input type="hidden" id="sourceSalesCategoryId">
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td style="vertical-align: top"><label class="label-bold">To:</label></td>
				<td>						
					 <select name="targetSalesCategory" id="targetSalesCategory" style="width: 250px;"></select>
				</td>
			</tr>
		</table>
		<div style="display: none" id="targetRateProfileExist">
			<table cellpadding="5" cellspacing="5">
				<tr>
					<td><img src='../images/warning.png'></td>
					<td style='line-height:2; vertical-align: top;'>
						Existing Rate Profiles Associated with <label id="selectedSalesCategory"></label> Sales Category will be removed.<Br/></Br>Do you want to continue?
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>