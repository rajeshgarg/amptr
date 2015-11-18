<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fieldset style="padding:5px 15px;margin-top:5px;border-top:1px solid #ccc;">
	<legend style="padding:0 5px;font-size:12px;font-weight:700;">Proposal Details</legend>
	<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
		<tbody>
			<tr>
				<td width="3%">&nbsp;</td>
				<td width="19%"><label class="label-bold"><spring:message code="label.generic.proposalName"/>:</label></td>
				<td width="30%" class="fval content-wrap"><span class="prop-name-capt" style="width: 250px" title="${proposalForm.proposalName}">${proposalForm.proposalName}</span></td>
				<td width="20%"><label class="label-bold"><spring:message code="label.basicinfo.campaignName"/>:</label></td>
				<td width="20%" class="fval"><span class="prop-name-capt" style="width: 250px" title="${proposalForm.campaignName}">${proposalForm.campaignName}</span></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td width="19%"><label class="label-bold"><spring:message code="label.basicinfo.advertisername"/>:</label></td>
				<td width="30%" class="fval">
					<span class="prop-name-capt">${proposalForm.summaryAdvertiserName}</span>
					<c:if test="${proposalForm.specialAdvertiser}">
						<span id="specialAdvertiserImg" class="special-advertiser tip-tool" title="Special Advertiser" style="margin-left:7px;position:absolute"></span>
					</c:if>
				</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.accountManager"/>:</label></td>
				<td class="fval content-wrap" id="proposalHeaderStatus"><span class="prop-name-capt" style="width: 250px" title="${proposalForm.accountManager}">${proposalForm.accountManager}</span></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.agencyMargin"/>:</label></td>
				<td class="fval" id="propAgencyMargin">${proposalForm.agencyMargin}</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.salescategory"/>:</label></td>
				<td class="fval">${proposalForm.salesCategoryDisplayName}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.requestedon"/>:</label></td>
				<td class="fval">${proposalForm.requestedOn}</td>		
				<td><label class="label-bold"><spring:message code="label.basicinfo.dueon"/>:</label></td>
				<td class="fval">${proposalForm.dueOn}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>			
				<td><label class="label-bold"><spring:message code="label.generic.startDate"/>:</label></td>
				<td class="fval" id="proposalStartDate">${proposalForm.startDate}</td>	
				<td><label class="label-bold"><spring:message code="label.generic.endDate"/>:</label></td>
				<td class="fval" id="proposalEndDate">${proposalForm.endDate}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.currency"/>:</label></td>
				<td class="fval">${proposalForm.currency}</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.pricetype"/>:</label></td>
				<td class="fval" id="propPriceType">${proposalForm.priceType}</td>
			</tr>
		</tbody>
	</table>
</fieldset>
<fieldset style="padding:5px 15px;margin-top:5px;border-top:1px solid #ccc;">
	<legend style="padding:0 5px;font-size:12px;font-weight:700;">Financial Details</legend>
	<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
		<tbody>
			<tr>
				<td width="3%">&nbsp;</td>			
				<td width="19%"><label class="label-bold"><spring:message code="label.basicinfo.netImpressions"/>:</label></td>
				<td width="30%" class="fval content-wrap" id="proposalNetImpression"><span class="prop-name-capt" style="width: 250px">${proposalForm.netImpressions}</span></td>
				<td width="20%"><label class="label-bold"><spring:message code="label.basicinfo.offeredBudget"/>:</label></td>
				<td width="20%" class="fval" id="proposalOfferedBudget"><span class="prop-name-capt" style="width: 250px">${proposalForm.offeredBudget}</span></td>
			</tr>		
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.budget"/>:</label></td>
				<td class="fval">${proposalForm.budget}</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.netCpm"/>:</label></td>
				<td class="fval" id="proposalNetCpm">${proposalForm.netCpm}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.remBudget"/>:</label></td>
				<td class="fval" id="proposalRemainingBudget">${proposalForm.remBudget}</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.thresholdLimit"/>:</label></td>
				<td class="fval">${proposalForm.thresholdLimit}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><label class="label-bold"><spring:message code="label.basicinfo.pricingReviewed"/>:</label></td>
				<td class="fval">${proposalForm.lastPricingReviewedDate}</td>
			</tr>
		</tbody>
	</table>
</fieldset>