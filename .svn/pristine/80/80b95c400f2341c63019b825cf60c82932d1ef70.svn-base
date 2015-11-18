<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="editProposalContainer edit-prop-cont">
	<%@ include file="proposalHeader.inc" %>	
	<div class="proposal-inner-container">
		<div id="buildProposalParentDiv">
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}">
			<input type="hidden" value="${readOnlyView}" id="isreadOnly" />
			<input id="proposalOwnerRole" type="hidden" value="${proposalOwnerRole}" />
			<c:set var="currentView" value="buildProposal" scope="request"></c:set>
			<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>
			<jsp:include page="optionStageNavigationBar.jsp"></jsp:include>
			<script type="text/javascript" src="<%= request.getContextPath() %>/js/proposal/lineItem.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
			<script type="text/javascript" src="./js/proposal/buildProposal.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
			<div id="buildProposalaccordion"  class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
				<div id="campaigndocumentaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
					<img class="accordian-icon-img" src='./images/Basic-info-proposalP.png'/>
						<span class="accordian-txt"><a href="#"><spring:message code="label.buildproposal.accordiontext.basicInfo"/></a></span>
					</h3>
					<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
						<%@ include file="basicInfo.inc" %>
						<input type="hidden" id="salescategory" value="${proposalForm.salescategory}"/> 
					</div>
				</div>
				<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
					<img class="accordian-icon-img" src='./images/line-item-iconP.png'/>
					<span id="lineItemSpan" class="accordian-txt"><a href="#"><spring:message code="label.buildproposal.accordiontext.lineitems"/></a></span>
				</h3>		
				<div class="tblbg">			
					<div id="proposalLineItemContainer">
						<div id="availStatuslink" style="display:none;"> 
							<c:if test="${!readOnlyView}">
								<a class="avail-status-helplink" id="availStatus" href="#" title='<spring:message code="label.lineitem.avails.status"/>' >
									<spring:message code="label.lineitem.avails.status"/>
		               			</a>
		   					</c:if>
		               		<div class="spacer7"></div>
		               	</div>
		               	<div class="spacer7"></div>
						<div id="availStatusSummary" title='<spring:message code="label.lineitem.avails.status"/>' style="display:none;overflow:hidden;"></div>
						<div id="lineItemGridHider">
							<table id="lineItemTable" class="childGridForBuildProposalLineItem"></table>
							<div id="lineItemPager"></div>
						</div>
						<div class="spacer7"></div>
						<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="lineItemSearchPanel">
							<select id="lineItemSearchOption">
								<option value="lineItemId"><spring:message code="label.generic.lineitemid"/></option>
								<option value="sosLineItemId"><spring:message code="label.lineItem.sosId"/></option>
							</select>
							<input id="lineItemSearchValue" type="text" size="20" class="search-icon search-input"/>
						</div>
						<div id="buildProposalTab" style="margin-top: 2px;">
							<ul>
							     <li id="buildProposalDetailFormTab-1"><a href="#lineItem"><spring:message code="tab.generic.lineItemDetails"/></a></li>					     
							     <li id="buildProposalDetailFormTab-4"><a href="#addExistinglineItem"><spring:message code="tab.generic.addExistinglineItems"/></a></li>
						    </ul>				    
							<div id="lineItem" class="tblbg">	
								<form:form action="./proposalWorkflow/editLineItemsOfProposal.action" id="lineItemdetailForm"  name="lineItemdetailForm" modelAttribute="proposalLineItemForm" >
									 <jsp:include page="../package/packageLineItems.jsp"></jsp:include>
									 <div class="save-buttons-div" style="width: 100%; margin-top: 10px; ">
										<table style="width: 100%; ">
											<tr id="buildProposal_buttons" >
												<td colspan="12" align="center">
													<c:choose>
														<c:when test="${readOnlyView}">			
															<input type="button"  value="SAVE" disabled="disabled" class="disabled-btn marg-top-6"/>
															<input type="button"  value ="RESET" disabled="disabled" class="disabled-btn marg-top-6"/>
														</c:when>
														<c:otherwise>
															<input type="button" id="lineItemSaveData" value="SAVE" class="save-btn marg-top-6"/>
															<input type="button" id="lineItemResetData" value ="RESET" class="reset-btn marg-top-6"/>
														</c:otherwise>	
													</c:choose>	
												</td>
											</tr>
										</table>
									</div>						
								</form:form>
							</div>				
							<div id="addExistinglineItem" style="margin-bottom: 9px;">
								<div id="buildProposalsearch">	
									<form:form action="./proposalWorkflow/addLineItemsToProposal.action" id="buildProposalSearchForm"  name="buildProposalSearchForm" modelAttribute="searchProposalForm"  >
										<%@include file="buildProposalSearch.jsp" %>			
									</form:form>	
								</div>
							</div>
						</div>								
					</div>	
					<div id = "searchProposalLineItemsDiv">							
						<div id="searchProposalLineItems" style="display: none" title="Line Items"></div>
					</div>
					<div id="availsLineItems" style="display: none" title="Avails Detail"></div>				
				</div>
			</div>
			<div id="multiLineItemDeleteContainer" title="Delete Line Items">
				<div id="multiLineItemDeleteConfirmation">
					<table cellpadding="5" cellspacing="5">
						<tr>
							<td><img src='./images/warning.png'></td>
							<td style='line-height:2; vertical-align: top;'>
								<spring:message code="label.multi.line.item.delete.confirmation"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="multiLineItemPriceUpdateContainer" title="<spring:message code="label.confirma.title.update.rate.card.price"/>">
				<div id="multiLineItemUpdateConfirmation">
					<table cellpadding="5" cellspacing="5">
						<tr>
							<td><img src='./images/warning.png'></td>
							<td style='line-height:2; vertical-align: top;'>
								<spring:message code="label.multi.line.item.update.confirmation"/>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div id="copyInOtherOptionParentDiv">
					<div id="copyPasteInOtherOptionDialogue" style="display: none"></div>
			</div>
			<div id="splitLineItemsContainer" title="Split Line Items">
				<div id="splitLineItemsConfirmation">
					<table cellpadding="5" cellspacing="5">
						<tr>
							<td><img src='./images/warning.png'></td>
							<td id="splitLineItemsConfirmationMsg" style='line-height:2; vertical-align: top;'>
								<spring:message code="label.line.items.split.confirmation"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>