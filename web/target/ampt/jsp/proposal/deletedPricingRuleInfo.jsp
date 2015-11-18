<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
	ul.deletedRules li{
	list-style-type:disc;
	padding:0px;
}
</style>
<table width="100%" cellpadding="2" cellspacing="2" border="1">
	<c:choose>
		<c:when test="${not empty deletedRuleIdLst}">
			<tr>
				<td class="label-dialog"><label><spring:message code="label.generic.pricingRule.name" /></label></td>
			</tr>
			<tr>
				<td>
					<ul class="deletedRules">
						<c:forEach var="pricingRuleObj" items="${deletedRuleIdLst}" varStatus="loop">
							<li>${pricingRuleObj.name}</li>
						</c:forEach>
					</ul>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<td><label><spring:message code="label.targeting.no.pricing.rule" /></label></td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>
