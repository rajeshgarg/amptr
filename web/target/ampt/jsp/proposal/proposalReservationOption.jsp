<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div id="reservProposalOptionListing"  >
<table width="100%" cellpadding="2" cellspacing="2" class="table-layout">
	<tr>
		<td class="help-row-bg" align="left" ><label class="label-bold">&nbsp;</label></td>		 
		<td class="help-row-bg" align="center" ><label class="label-bold" style="width:30%"><spring:message code="label.basicinfo.optionName"/></label></td>
		<td class="help-row-bg" align="center"><label class="label-bold"><spring:message code="label.basicinfo.budgetname"/></label></td>
		<td class="help-row-bg" align="center"><label class="label-bold" style="width:30%"><spring:message code="label.basicinfo.offeredBudgetname"/></label></td>
		<td class="help-row-bg" align="center"><label class="label-bold" style="width:30%"><spring:message code="label.basicinfo.netCpmname"/></label></td>
	</tr>
	<c:forEach var="option" items="${proposalOptions}" varStatus="loop"> 	
	<tr>
	<c:choose>
		<c:when test="${copyInOtherOption == true}">
		 	<c:choose>
		 		<c:when test="${option.optionId == currentOption}">
					<td style="padding:5px;width:10%;"><input type="checkbox" name="proposalOptionId" id="proposalOptionId" disabled="disabled"></input></td>
				</c:when>
				<c:otherwise>
					<td style="padding:5px;width:10%;"><input type="checkbox" name="proposalOptionId" id="proposalOptionId" value="${option.optionId}"></input></td>
				</c:otherwise>
				</c:choose>
		 	</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${option.optionId == currentOption}">
					<td style="padding:5px;width:10%;" title="${copyInOtherOption}"><input type="radio" name="reservedOptionId" id="reservedOptionId" disabled="disabled"></input></td>
				</c:when>
				<c:otherwise>
					<td style="padding:5px;width:10%;"><input type="radio" name="reservedOptionId" id="reservedOptionId" value="${option.optionId}"></input></td>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>	
		<td style="padding:padding: 0 0 0 5px; width:40%;padding-left:15%">${option.optionName}
			 <c:if test="${option.defaultOption == true}">
			 	<span class="pop-default-icon"></span>
			 </c:if>
		 </td>
		 <td align="right">${option.budget}</td>
		 <td align="right" >${option.netImpressions} </td>
		 <td align="right">${option.netCpm} </td>
	</tr>
	</c:forEach>
</table>
</div>

