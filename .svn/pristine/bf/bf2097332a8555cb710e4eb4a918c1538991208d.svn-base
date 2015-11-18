<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="./js/proposal/proposalTemplate.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div class="editProposalContainer edit-prop-cont">
	<%@ include file="proposalHeader.inc" %>
	<div class="proposal-inner-container">
		<div id="generateTemplateParentDiv">
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}">
			<c:set var="currentView" value="proposalTemplate" scope="request"></c:set>
			<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>
			<jsp:include page="optionStageNavigationBar.jsp"></jsp:include>
			<form:form id="proposalTemplateForm" action="${flowExecutionUrl}" modelAttribute="proposalForm">
				<div id="campaigndocumentaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
					<img class="accordian-icon-img" src='./images/Basic-info-proposalP.png'/>
						<span class="accordian-txt"><a href="#"><spring:message code="label.proposaltemplate.accordiontext.basicInfo"/></a></span>
					</h3>
					<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
						<%@ include file="basicInfo.inc" %>
					</div>
				</div>
				<div id="newProposalaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
					<img class="accordian-icon-img" src='./images/templateP.png'/>
						<span class="accordian-txt"><a href="#"><spring:message code="label.proposaltemplate.accordiontext.selectTemplate"/></a></span>
					</h3>
					<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
						<div id="messageHeaderDiv" class="error"></div>
						<div class="radio-button-header">
							<input type="checkbox" id="proposalTemplateCheck" value="proposalTemplate" checked="checked" onClick = "showHideExportOptions()"><label class="first">Proposal template</label>
							<input type="checkbox" id="creativeSpecsCheck" value="creativeSpecs" checked="checked" onClick = "showHideExportOptions()"><label class="second">Creative Specs</label>
							<input type="checkbox" id="allOptionCheck" value="allOption" checked="checked" onClick = "showHideExportOptions()"><label class="second">Export All Options</label>
						</div>
						<div class="generate-media-content">
							<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align" id="proposalTemplateValues">
								<tbody>
									<tr>
										<td align="right" width="240px" style="padding-right:4px">
											<label class="label-bold">
												<spring:message code="label.proposalTemplate.proposalTemplate"/>:
											</label>
										</td>
										<td>									
											<form:select path="proposalTemplate" id="proposalTemplate" cssStyle="width: 250px;" >
												<form:options items="${allProposalTemplate}" />								
											</form:select>
										</td>
									</tr>
								</tbody>
							</table>
							<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align specsNote" id="creativeSpecsNote" style="display:none">
								<tbody>
									<tr>
										<td align="center">
											<label class="label-bold">
												<spring:message code="label.generic.note"/>:
											</label>
											All the creative information will be exported.
										</td>
									</tr>
								</tbody>
							</table>
							<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align specsNote" id="selectOne" style="display:none">
								<tbody>
									<tr>
										<td align="center">
											<label class="label-bold checkbox-warning">
												<spring:message code="label.generic.warning"/>:
											</label>
											<span>
												<spring:message code="label.generic.selectOption"/>
											</span>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="save-buttons-div">
						<table width="100%">
							<tr>
								<td align="center">
									<input type="button" class="save-btn marg-top-6" onClick="generateMediaTemplate()" id="generateMediaExport" value="Export" />
									<input type="hidden" value="eventViewEditLineItemDetail" id="_eventId" />
									<form:hidden id="id" path="id" />
									<form:hidden id="proposalVersion" path="proposalVersion" />
									<form:hidden id="optionId" path="optionId" />
								</td>
							</tr>
						</table>
					</div>	
				</div>	
			</form:form>
		</div>
	</div>
</div>
