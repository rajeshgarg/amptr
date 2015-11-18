<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="proposalOptionThresholdValueListing" style="height:185px;">
<table width="100%" cellpadding="2" cellspacing="2" class="table-layout" id="optionsThresholdValueTable">
	<tr id="optionsThresholdValueHeader">
		<td class="help-row-bg" style="width:5%;" align="center" >
			<%-- <label class="label-bold"><spring:message code="label.generic.options.reviewed"/></label> --%>
			<input type='checkbox' id="reviewedOption_Header" onClick="customReviewedOptionSelect(this);">
		</td>
		<td class="help-row-bg" align="left" width="5%" style="display:none;"><label class="label-bold"><spring:message code="label.generic.option.id"/></label></td>		 
		<td class="help-row-bg" align="center" ><label class="label-bold" style="width:25%"><spring:message code="label.basicinfo.optionName"/></label></td>
		<td class="help-row-bg" align="center"><label class="label-bold"  style="width:25%"><spring:message code="label.option.qualifing.added.value.worth"/></label></td>
		<td class="help-row-bg" align="center"><label class="label-bold" style="width:20%"><spring:message code="label.option.threshold.value"/></label></td>
		<td class="help-row-bg" align="center" ><label class="label-bold" style="width:20%"><spring:message code="label.basicinfo.pricingReviewed"/></label></td>
		<td class="help-row-bg" align="left" width="5%" style="display:none;"><label class="label-bold"><spring:message code="label.option.threshold.value"/></label></td>
	</tr>
	<c:forEach var="option" items="${proposalOptions}" varStatus="loop"> 	
	<tr id="${option.optionId}">
		<td style="width:5%;" align="center">
			<input type='checkbox' id="reviewedOption_${option.optionId}" name='customReviewedOption' onClick="selectUnselectReviewOptHeaderChkBox(this);" value="${option.optionId}">
		</td>
		<td id="optionId_${option.optionId}" align="right" style="padding-right:3px ;display:none;" >${option.optionId}</td>
		<td id="optionName_${option.optionId}" style="padding: 0 0 0 5px;width:20%;">${option.optionName}
			 <c:if test="${option.defaultOption == true}">
			 	<span class="pop-default-icon"></span>
			 </c:if>
		 </td>
		 <td id="totalInvestment_${option.optionId}" align="right" style="padding-right:2px">${option.qualifingLineItemInvestment}</td>
		 <td align="center" style="padding-right:1px"><input id="Threshold_${option.optionId}" style="width:100px;" type="text" readonly="true" class="input-textbox-readonly" /></td>
		 <td id="lastReviewedOn_${option.optionId}" style="padding: 0 0 0 5px;width:20%;">${option.lastPricingReviewedDate}</td>
		 <td id="thresholdValue_${option.optionId}" align="right" style="padding-right:3px ;display:none;" >${option.thresholdLimit}</td>
	</tr>
	</c:forEach>
</table>
</div>
<strong>Note:</strong>
<ul style="margin:0">
	<li>Select the check boxes to mark the required options as Pricing Reviewed.</li>
	<li>Total Investment displayed here is the cumulative total investment of the qualifying lines only.</li>
	<li>The Threshold value should be greater than 0 and cannot go beyond Total Investment.</li>
</ul>