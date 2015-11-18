<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="proposalReviewOptionListing" style="height:145px;">
<table width="100%" cellpadding="2" cellspacing="2" class="table-layout">
	<tr>
		<td class="help-row-bg" align="left" width="4%"><label class="label-bold">&nbsp;</label></td>		 
		<td class="help-row-bg" align="center" width="5%"><label class="label-bold"><spring:message code="label.basicinfo.optionName"/></label></td>
		<td class="help-row-bg" align="center" width="10%"><label class="label-bold"><spring:message code="label.pricingrule.budget"/></label></td>
		<td class="help-row-bg" align="center" width="16%"><label class="label-bold"><spring:message code="label.basicinfo.netImpressions"/></label></td>
		<td class="help-row-bg" align="center" width="14%"><label class="label-bold"><spring:message code="label.basicinfo.netCpmname"/></label></td>
		<td class="help-row-bg" align="center" width="19%"><label class="label-bold"><spring:message code="label.basicinfo.offeredBudget"/></label></td>
		<td class="help-row-bg" align="center" width="15%"><label class="label-bold"><spring:message code="label.basicinfo.reservation"/></label></td>
	</tr>
	<c:forEach var="option" items="${proposalOptions}" varStatus="loop"> 	
	<tr>
	<c:choose>
		<c:when test="${option.defaultOption == true}">
			<td><input type="radio"  checked="checked" name="reviewOptionId" id="reviewOptionId"  value="${option.optionId}"></input></td>
		</c:when>
		<c:otherwise>
			<td><input type="radio"   name="reviewOptionId" id="reviewOptionId" value="${option.optionId}"></input></td>
		</c:otherwise>
	</c:choose>	
		<td style="padding: 0 0 0 5px; vertical-align: top; width:20%;">${option.optionName}
			 <c:if test="${option.defaultOption == true}">
			 	<span class="pop-default-icon"></span>
			 </c:if>
		 </td>
		 <td align="right" style="padding-right:3px">${option.budget}</td>
		 <td align="right" style="padding-right:3px">${option.netImpressions} </td>
		 <td align="right" style="padding-right:3px">${option.netCpm} </td>
		 <td align="right" style="padding-right:3px">${option.offeredBudget} </td>
		 <td align="right" style="padding-right:3px">${option.noOfReservations} </td>
	</tr>
	</c:forEach>
</table>
</div>
<strong>Note:</strong>
<ul style="margin:0">
	<li>Selected option will be marked as default.</li>
	<li>Reservations of other options will be deleted.</li>
</ul>