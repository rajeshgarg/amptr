<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="./js/proposal/summaryProposal.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div class="editProposalContainer edit-prop-cont">
	<%@ include file="proposalHeader.inc" %>	
	<div class="proposal-inner-container">
		<div id="summaryParentDiv">
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}">	
			<form:form id="summaryForm" action="${flowExecutionUrl}" modelAttribute="proposalForm">
				<c:set var="currentView" value="summary" scope="request"></c:set>
				<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>
				<jsp:include page="optionStageNavigationBar.jsp"></jsp:include>
				<div id="basicInfoaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons summary-new">
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
						<img class="accordian-icon-img" src='./images/Basic-info-proposalP.png'/>
						<span class="accordian-txt"><a href="#"><spring:message code="label.summaryproposal.accordiontext.proposalInfo"/></a></span>
					</h3>
					<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
						<fieldset style="padding:5px 15px;margin-top:5px;border-top:1px solid #ccc;">
						<legend style="padding:0 5px;font-size:12px;font-weight:700;">Proposal Details</legend>
						<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align basic-info-summary-table">
							<tbody>
								<tr>
									<td width="5%">&nbsp;</td>
									<td width="15%">
										<label class="label-bold"><spring:message code="label.basicinfo.proposalName"/>:</label>
									</td>
									<td width="30%" id="clonedProposalName" class="fval content-wrap summary-top-row"><span class="prop-name-capt" title="${proposalForm.proposalName}">${proposalForm.proposalName}</span></td>
									<td width="15%">
										<label class="label-bold"><spring:message code="label.basicinfo.campaignName"/>:</label>
									</td>
									<td width="34%" id="clonedOptionName" class="fval content-wrap summary-top-row"><span class="prop-name-capt" title="${proposalForm.campaignName}">${proposalForm.campaignName}</span></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.generic.proposalAdvertiser"/>:</label>
									</td>
									<td id="advertiserName" class="fval content-wrap summary-top-row">
										<span class="prop-name-capt" title="${proposalForm.summaryAdvertiserName}">${proposalForm.summaryAdvertiserName}</span>
										<c:if test="${proposalForm.specialAdvertiser}">
											<span class="special-advertiser tip-tool" title="Special Advertiser" style="margin-left:7px;position:absolute"></span>
										</c:if>		
									</td>
									<td><label class="label-bold"><spring:message code="label.basicinfo.accountManager"/>:</label></td>
									<td class="fval content-wrap summary-top-row"><span class="prop-name-capt" title="${proposalForm.accountManager}">${proposalForm.accountManager}</span></td>
								</tr>
								<tr>
									<td width="2%">&nbsp;</td>
									<td width="15%"><label class="label-bold"><spring:message code="label.generic.proposalAgency"/>:</label></td>
									<td width="34%" class="fval content-wrap" ><span class="prop-name-capt" title="${proposalForm.summaryAgencyName}">${proposalForm.summaryAgencyName}</span></td>
									<td><label class="label-bold"><spring:message code="label.basicinfo.agencyMargin"/>:</label></td>
									<td>${proposalForm.agencyMargin}</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.generic.startDate"/>:</label>
									</td>
									<td class="fval">${proposalForm.startDate}</td>													
									<td>
										<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
									</td>
									<td class="fval">${proposalForm.endDate}</td>												
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.requestedon"/>:</label>
									</td>
									<td class="fval">${proposalForm.requestedOn}</td>	
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.dueon"/>:</label>
									</td>
									<td class="fval">${proposalForm.dueOn}</td>												
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.sosorderid"/>:</label>
									</td>
									<td class="fval content-wrap">${proposalForm.sosOrderId}</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.currency"/>:</label>
									</td>
									<c:choose>
										<c:when test="${proposalForm.currency == 'USD'}">
											<td class="fval">${proposalForm.currency}</td>
										</c:when>
										<c:otherwise>
											<td class="fval">${proposalForm.currency} 
												<span class="toggle-div"><label class="selected-curr" onclick="toggleCurrency(event, this);" title="Convert amounts to USD">USD (&#36;)</label>
												<c:choose>
													<c:when test="${proposalForm.currency == 'EUR'}">
														<label onclick="toggleCurrency(event, this);" title="Convert amounts to ${proposalForm.currency}">${proposalForm.currency} (&euro;)</label>
													</c:when>
													<c:when test="${proposalForm.currency == 'BEL'}">
														<label onclick="toggleCurrency(event, this);" title="Convert amounts to ${proposalForm.currency}">${proposalForm.currency} (&#8354;)</label>
													</c:when>
													<c:otherwise>
														<label onclick="toggleCurrency(event, this);" title="Convert amounts to ${proposalForm.currency}">${proposalForm.currency} (&#165;)</label>
													</c:otherwise>
												</c:choose>
												</span>
											</td>								
										</c:otherwise>
									</c:choose>
								</tr>					
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.pricetype"/>:</label>
									</td>
									<td class="fval">${proposalForm.priceType}</td>	
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.salescategory"/>:</label>
									</td>
									<td class="fval">${proposalForm.salesCategoryDisplayName}</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.generic.proposalpriorities"/>:</label>
									</td>
									<td class="fval">${proposalForm.criticalityDisplayName}</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.assignedBy"/>:</label>
									</td>
									<td class="fval">${proposalForm.assignedBy}</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.ConversionRate"/>:</label>
									</td>
									<td class="fval">${proposalForm.conversionRate}</td>
									<td>
										<label class="label-bold"><spring:message code="label.basicinfo.clonedfrom"/>:</label>
									</td>
									<td class="fval content-wrap summary-top-row"><span class="prop-name-capt" title="${proposalForm.clonedFormProposal}">${proposalForm.clonedFormProposal}</span></td>
								</tr>
								</tbody>
							</table>
						</fieldset>
						<fieldset style="padding:5px 15px;margin-top:5px;border-top:1px solid #ccc;">
						<legend style="padding:0 5px;font-size:12px;font-weight:700;">Financial Details</legend>
						<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align basic-info-summary-table">
							<tbody>
								<tr>
									<td width="5%">&nbsp;</td>
									<td width="15%">
										<label class="label-bold"><spring:message code="label.basicinfo.netImpressions"/>:</label>
									</td>	
									<td width="30%" class="fval">${proposalForm.netImpressions}</td>
									<td width="15%">
										<span><label class="label-bold"><spring:message code="label.basicinfo.budgetname"/></label></span><span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td width="34%" class="fval">
										<span id="proposalBudget" >${proposalForm.budget}</span>
										<span id="budgetInProposalCurrency" style="display:none;"></span>
									</td>
									<%-- <td>
										<label class="label-bold"><spring:message code="label.generic.proposalpriorities"/>:</label>
									</td>
									<td class="fval">${proposalForm.criticalityDisplayName}</td> --%>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<span><label class="label-bold"><spring:message code="label.basicinfo.offeredBudgetname"/></label></span><span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td class="fval">
										<span id="totalInvestment" >${proposalForm.offeredBudget}</span>
										<span id="InvestmentInProposalCurrency" style="display:none;"></span>
									</td>
									<td>
										<span><label class="label-bold"><spring:message code="label.basicinfo.netCpmname"/></label></span> <span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td class="fval">
										<span id="proposalCPM" >${proposalForm.netCpm}</span>
										<span id="cpmInProposalCurrency" style="display:none;"></span>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.generic.discount"/>:</label>
									</td>
									<td class="fval">${proposalForm.discount}</td>
									<td><label class="label-bold"><spring:message code="label.basicinfo.thresholdLimit"/>:</label></td>
									<td>${proposalForm.thresholdLimit}</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>
										<label class="label-bold"><spring:message code="label.generic.proposalCampaignObjectives"/>:</label>
									</td>
									<td class="fval">
										<ul>
											<c:forEach var="objective" items="${proposalCampainObjective}">
												<li>${objective.value}</li>
											</c:forEach>
										</ul>
									</td>
									<td><span><label class="label-bold"><spring:message code="label.summary.remBudget"/></label></span><span id="usdSymbol"> (<b>&#36;</b>)</span></td>
									<td class="fval">
										<span id="proposalRemBudget" >${proposalForm.remBudget}</span>
										<span id="remBudgetInProposalCurrency" style="display:none;"></span>
									</td>
								</tr>
								<tr style="display:none;">
									<td>
										<form:hidden path="currency"  id="proposalCurrency" />
										<form:hidden path="conversionRate"  id="proposalConversionRate" />
									</td>
								</tr>						
							</tbody>
						</table>
						</fieldset>
					</div>
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
						<img class="accordian-icon-img" src='./images/line-item-iconP.png'/>
						<span class="accordian-txt"><a href="#"><spring:message code="label.summaryproposal.accordiontext.lineitemInfo"/></a></span>
					</h3>
					<div id="LineItemAccordian" class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
						<table width="100%" cellpadding="4" cellspacing="0" style="table-layout:fixed" class="summary-line-table">
							<tbody>
								<tr class="text-align-center">
									<td width="4%" >
										<label class="label-bold"><spring:message code="label.generic.lineitemid" /></label>
									</td>
									<td width="10%">
										<label class="label-bold"><spring:message code="label.generic.product" /></label>
									</td>
									<td width="13%">
										<label class="label-bold"><spring:message code="label.generic.salesTarget" /></label>
									</td>
									<td width="7%">
										<label class="label-bold"><spring:message code="label.reservation.sor" /></label>
									</td>
									<td width="6%">
										<label class="label-bold"><spring:message code="label.lineitem.RateCardPrice" /></label><span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td width="6%">
										<label class="label-bold"><spring:message code="label.lineitem.cpm" /></label><span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td width="7%">
										<label class="label-bold"><spring:message code="label.generic.discount" /></label>
									</td>
									<td width="8%">
										<label class="label-bold" style="word-wrap:break-word;">
											<spring:message	code="label.generic.offeredImpression" />
										</label>
									</td>
									<td width="9%" nowrap="nowrap">
										<label class="label-bold"><spring:message code="label.lineitem.totalInvestment" /></label><span id="usdSymbol"> (<b>&#36;</b>)</span>
									</td>
									<td width="8%">
										<label class="label-bold"><spring:message code="label.generic.startDate" /></label>
									</td>
									<td width="8%">
										<label class="label-bold"><spring:message code="label.generic.endDate" /></label>
									</td>
									<td width="7%">
										<label class="label-bold"><spring:message code="label.generic.pricingStatus" /></label>
									</td>
									<td width="7%">
										<label class="label-bold"><spring:message code="label.generic.reserved" /></label>
									</td>
								</tr>
								<c:choose>
									<c:when test="${empty proposalLineItems}">
										<tr class="table-records">
											<td style="height: 25px; vertical-align: middle;" colspan="11" align="center">
												No record to view
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="lineItem" items="${proposalLineItems}">
											<tr class="table-records">
												<td class="fval" style="text-align:right;" >${lineItem.lineItemID}</td>
												<td class="fval" style="word-wrap:break-word">${lineItem.productName}</td>
												<td class="fval" style="word-wrap:break-word">${lineItem.sosSalesTargetName}</td>
												<td class="fval" align="right" style="word-wrap:break-word">${lineItem.sor}</td>
												<td class="fval" align="right"><span class="lineItemValueInDollar">${lineItem.rateCardPrice}</span>
											 	<span class="lineItemCurrencyClass" style="display:none;"></span>
											 	<span class="lineItemIDClass" style="display:none;">${lineItem.lineItemID}</span></td>
												<td class="fval" align="right"><span id=rate_${lineItem.lineItemID} class="lineItemValueInDollar">${lineItem.rate}</span>
												<span class="lineItemCurrencyClass" style="display:none;"></span></td>
												<td class="fval" align="right">${lineItem.discount}</td>
												<td class="fval content-margin" align="right"><span id=impressionTotal_${lineItem.lineItemID}>${lineItem.impressionTotal}</span></td>
												<td class="fval" align="right"><span class="totalInvestmentInDollar" id=totalInvestmentInDollar_${lineItem.lineItemID}>${lineItem.totalInvestment}</span>
												<span id=totalInvestmentInCurrency_${lineItem.lineItemID} class="totalInvestmentInCurrency" style="display:none;"></span>
												</td>
												<c:choose>
													<c:when test="${empty lineItem.startDate}">
														<td class="fval" align="center">${lineItem.flight}</td>
													</c:when>
													<c:otherwise>
														<td class="fval" align="center">${lineItem.startDate}</td>									
													</c:otherwise>
												</c:choose>
												<td class="fval" align="center">${lineItem.endDate}</td>
												<td class="fval" align="center">${lineItem.pricingStatus}</td>
												<c:choose>
													<c:when test="${lineItem.reserved}"><td class="fval" style="text-align:center">Yes</td></c:when>
													<c:otherwise><td class="fval" style="text-align:center">No</td></c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${lineItem.salesTarget_active == 'No' || lineItem.product_active == 'No'}">
														<td class="expire-package">
															<img src='./images/info-icon.png' class="tip-toolright" title="<spring:message code='label.summaryProposal.expireProdSt'/>" />
														</td>
													</c:when>
												</c:choose>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>								
							</tbody>
						</table>
					</div>
				</div>
				<input type="hidden" value="${proposalForm.id}" id="id" />
				<input type="hidden" value="${proposalForm.proposalVersion}" id="proposalVersion" />
				<input type="hidden" value="${proposalForm.optionId}" id="proposalOption" />
				<input type="hidden" value="${proposalOptionsCount}" id="optionsCount" />
				<input type="hidden" value="${proposalForm.conversionRate}" id="exchangeRate" />
			</form:form>
		</div>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>