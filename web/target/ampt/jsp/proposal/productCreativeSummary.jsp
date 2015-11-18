<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="creativeSummary" style="height: 200px; width: 550px; overflow: auto;">
	<c:choose>
	<c:when test="${not empty productCreativeLst }">
			<table id="pricing-calc-table" class="table-layout" width="100%" cellpadding="3" cellspacing="1">
					<tr>
						<th align="center" class="help-row-bg"  width="50%"><label class="label-bold"><spring:message code="label.creative" /></label></th>
						<th align="center" class="help-row-bg"  width="10%"><label class="label-bold"><spring:message code="label.generic.type" /></label></th>
						<th align="center" class="help-row-bg"  width="10%"><label class="label-bold"><spring:message code="label.generic.width" /></label></th>
						<th align="center" class="help-row-bg"  width="10%"><label class="label-bold"><spring:message code="label.generic.height" /></label></th>
						<th align="center" class="help-row-bg"  width="10%"><label class="label-bold"><spring:message code="label.generic.width2" /></label></th>
						<th align="center" class="help-row-bg"  width="10%"><label class="label-bold"><spring:message code="label.generic.height2" /></label></th>
					</tr>
					
					<c:forEach var="creativeData" items="${productCreativeLst}" varStatus="loop">
						<tr>
							<td>${creativeData.name}</td>
							<td>${creativeData.type}</td>
							<td align="center">${creativeData.width}</td>
							<td align="center">${creativeData.height}</td>
							<td align="center"><c:out value="${not empty creativeData.width2 ? creativeData.width2 : 'NA'}"/></td>
							<td align="center"><c:out value="${not empty creativeData.height2 ? creativeData.height2 : 'NA'}"/></td>
						</tr>
					</c:forEach>
				</table>
	</c:when>
	<c:otherwise>
		<div class="pricing-msg"> <spring:message code="label.generic.no.creative.associated" /></div>
	</c:otherwise>
</c:choose></div>