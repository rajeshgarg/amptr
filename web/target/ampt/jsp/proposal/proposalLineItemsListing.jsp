<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
$(function () { // this line makes sure this code runs on page load
	$('.checkall').click(function () {
		//$(this).parents('div').find(':checkbox').attr('checked', this.checked);
		$(this).parents('div').find(':checkbox').filter(function() { return !this.disabled; }).attr('checked', this.checked);
	});
});


function selectUnselect(obj){
	if(!$(obj).is(':checked')){		
  	  	$("#selectAllLineItems","#searchProposalLineItems").attr('checked', false);
  	}  	
  	if($('[name=lookupLineItemId]:not(:checked)',"#searchProposalLineItems").size() == 0){
  		$("#selectAllLineItems","#searchProposalLineItems").attr('checked', 'checked');
  	}
}
</script>

<div id="proposalLineItemListing" >

<table width="100%" cellpadding="2" cellspacing="2" >
	<tr>
		<td align="center" width="2%"><input type="checkbox" id="selectAllLineItems"  class="checkall" ></input></td>		
		<td align="center" width="20%"><label class="label-bold"><spring:message code="label.generic.productName"/></label></td>		
		<td align="center" width="20%"><label class="label-bold"><spring:message code="label.generic.salesTarget"/></label></td>
		<td align="center" width="15%"><label class="label-bold"><spring:message code="label.generic.placementName"/></label></td>
		<td align="center" width="5%"><label class="label-bold"><spring:message code="label.generic.cpm"/></label></td>		
		<td align="center" width="11%"><label class="label-bold"><spring:message code="label.generic.impressionTotal"/></label></td>
		<td align="center" width="6%"><label class="label-bold"><spring:message code="label.generic.totalInvestment"/></label></td>		
		<td align="center" width="10%" ><label class="label-bold"><spring:message code="label.generic.salesTargetActive"/></label></td>
		<td align="center" width="10%" ><label class="label-bold"><spring:message code="label.generic.productActive"/></label></td>
	</tr>
<c:forEach var="lineItem" items="${allLineItems}" varStatus="loop"> 	
	<c:choose>
		<c:when test="${lineItem.salesTarget_active == 'No' || lineItem.product_active == 'No' || lineItem.productSalesTargetCombo_active == 'No'}">
			<tr class="expire-package">
			<td><input type="checkbox" disabled="disabled" name="lookupLineItemId" id="lookupLineItemId" onclick="selectUnselect(this)" value="${lineItem.lineItemID}"></input></td>
		</c:when>
		<c:otherwise>
			<tr>
			<td><input type="checkbox" name="lookupLineItemId" id="lookupLineItemId" onclick="selectUnselect(this)" value="${lineItem.lineItemID}"></input></td>
		</c:otherwise>
	</c:choose>
							
								
				<td>${lineItem.productName}</td>
				<td>${lineItem.sosSalesTargetName}</td>
				<td><span style="word-wrap:break-word;width:240px;display:inline-block;">${lineItem.placementName}</span></td>
				<td style="text-align:right">${lineItem.rate}</td>
				<td style="text-align:right">${lineItem.impressionTotal}</td>
				<td style="text-align:right">${lineItem.totalInvestment}</td>
				<td>${lineItem.salesTarget_active}</td>
				<td>${lineItem.product_active}</td>
			</tr>
	</c:forEach>
</table>
</div>
<span style="display:none" id="lineItemMultipierLabel"><spring:message code="label.generic.lineItemMultiplier"/></span>