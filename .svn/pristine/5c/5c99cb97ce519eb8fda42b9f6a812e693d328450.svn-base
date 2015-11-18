<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="pricingLayeringRuleListing" >

<table width="100%" cellpadding="2" cellspacing="2" border="1">
	<tr>				
		<td><label class="label-bold"><spring:message code="label.generic.target.type"/></label></td>		
		<td><label class="label-bold"><spring:message code="label.generic.premium"/></label></td>
				
	</tr>
	<c:forEach var="pricingLayeringRule" items="${allLayeringRules}" varStatus="loop"> 
			<tr>				
				<td>${pricingLayeringRule.sosTarTypeValue}</td>
				<td>${pricingLayeringRule.premium}</td>
			</tr>
	</c:forEach>
</table>
</div>

