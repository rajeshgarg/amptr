<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="salesTargetListing" style="height: 150px;width: 350px; overflow: auto;">
	<c:choose>
		<c:when test="${not empty salesTargetLst}">
			<table width="100%" cellpadding="3" cellspacing="3" >
				<tr>
					<th width="40%">
						<label class="label-bold"><spring:message code="label.generic.salesTarget" /></label>
					</th>
					<td>&nbsp;</td>
					<th width="20%">
						<label class="label-bold"><spring:message code="label.selected.salesTarget.weight" /></label>
					</th>
					<td>&nbsp;</td>
					<th width="15%">
						<label class="label-bold"><spring:message code="label.selected.salesTarget.capacity" /></label>
					</th>
					<td>&nbsp;</td>
					<th width="25%">
						<label class="label-bold"><spring:message code="label.selected.salesTarget.updateDate" /></label>
					</th>
				</tr>
				<c:forEach var="salesTargetObj" items="${salesTargetLst}" varStatus="loop">
					<tr>
						<td>${salesTargetObj.salesTargeDisplayName}  </td>
						<td>&nbsp;</td>
						<td>${salesTargetObj.weight}</td>
						<td>&nbsp;</td>
						<c:choose>
      						<c:when test="${salesTargetObj.salesTargetId=='-1'}">
       							<td style="text-align: center;">${salesTargetObj.capacity}</td>
								<td>&nbsp;</td>
								<td style="text-align: center;">${salesTargetObj.modifiedDate}</td>
       						</c:when>
	     					<c:otherwise>
	     						<td>${salesTargetObj.capacity}</td>
								<td>&nbsp;</td>
								<td>${salesTargetObj.modifiedDate}</td>
						    </c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<c:choose>
  				<c:when test="${showMsg == 'noSalSalesTargetSelected'}">
  					<div style="text-align: center;"> <spring:message code="label.generic.no.salesTarget.selected" /></div>
  				</c:when>
  				<c:otherwise>
   					<div style="text-align: center;"> <spring:message code="label.generic.salesTarget.no.data.available" /></div>
  				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</div>
