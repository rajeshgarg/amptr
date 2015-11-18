<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="attributeAssocListing" style="height: 135px; overflow: auto;">
	<c:choose>
		<c:when test="${empty assocList}">
			<div style="text-align: center;"> Attribute is not attached to any ${type}</div>
		</c:when>
		<c:otherwise>
			<table width="100%" cellpadding="2" cellspacing="2">
				<tr>
					<th align="center" width="30%"><label class="label-bold">${type} Name</label></th>
					<c:if test="${type == 'Product'}">
						<td width="10%">&nbsp;</td>
						<th align="center" width="15%"><label class="label-bold"><spring:message code="label.generic.salesTarget"/></label></th>
					</c:if>
					<td width="10%">&nbsp;</td>
					<th align="center" width="35%"><label class="label-bold">Value</label></th>
				</tr>
				<c:forEach var="attrAssocList" items="${assocList}" varStatus="loop"> 	
					<tr>						
						<td style="border-bottom: 1px solid #9FC1F8;">${attrAssocList.name}</td>
						<c:if test="${type == 'Product'}">
							<td>&nbsp;</td>
							<td style="border-bottom: 1px solid #9FC1F8;">${attrAssocList.salesTargetName}</td>
						</c:if>
						<td>&nbsp;</td>
						<td style="border-bottom: 1px solid #9FC1F8;">${attrAssocList.value}</td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
</div>