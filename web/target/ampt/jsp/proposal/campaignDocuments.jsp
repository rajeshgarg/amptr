<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="./js/proposal/campaignDocuments.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id=${renderFragmentId}>
	<!-- <div id="renderFragmentId" style="display:none;">${renderFragmentId}</div> -->
	<input type="hidden" id="renderFragmentId" value="${renderFragmentId}">
	<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}">
	<c:set var="currentView" value="campaignDocument" scope="request"></c:set>
	<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>	
	<div id="campaigndocumentaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
		<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
		<img class="accordian-icon-img" src='./images/Basic-info-proposalP.png'/>
			<span class="accordian-txt"><a href="#"><spring:message code="label.campaigndocument.accordiontext.basicInfo"/></a></span>
		</h3>
		<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
			<%@ include file="basicInfo.inc" %>
		</div>
	</div>	
	<div id="campaigndocumentsaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
		<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
		<img class="accordian-icon-img" src='./images/uplaod-proposalP1.png'/>
			<span id="uploadDocumentSpan" class="accordian-txt"><a href="#"><spring:message	code="label.generic.proposalUploadCampaignDocuments" /></a></span>
		</h3>	
		<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">	
			<div id="${renderFragmentId}proposalDocumentsGrid"></div>
		</div>
	</div>
	<div class="save-buttons-div">
		<table width="100%">
			<tr>
				<td colspan="2" align="center">
					<input type="hidden" id="proposalId" value=${proposalForm.id}>
					<input type="button" class="reset-btn marg-top-6" id="backToBuildProposal" name="backToBuildProposal" value="Back" onClick="stateChangeRequest('${flowExecutionUrl}','eventBuildProposal')"  />
					<input type="button" class="reset-btn marg-top-6" id="campaignToSummary" name="campaignToSummary" value="Next" onClick="stateChangeRequest('${flowExecutionUrl}','eventGenerateMediaTemplate')" />
				</td>
			</tr>
		</table>
	</div>	
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>