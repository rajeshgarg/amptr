<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="pricingSummary" style="height: 300px; width: 650px; overflow: auto;">
	<c:choose>
	<c:when test="${not empty calculatorSummary && calculatorSummary.basePrice != null }">
		<c:choose>
			<c:when test="${not empty calculatorSummary.rateProfileSummary}">
				<table id="pricing-calc-table" class="table-layout" width="100%" cellpadding="3" cellspacing="1">
					<tr>
						<th align="center" class="help-row-bg" rowspan=2 width="10%"><label class="label-bold"><spring:message code="label.generic.product" /></label></th>
						<th align="center" class="help-row-bg" rowspan=2 width="10%"><label class="label-bold"><spring:message code="label.pricingrule.salesTarget" /></label></th>
						<th align="center" class="help-row-bg" rowspan=2 width="5%"><label class="label-bold"><spring:message code="label.selected.salesTarget.weight" /></label></th>
						<th align="center" class="help-row-bg" Colspan = "2" width="40%"><label class="label-bold"><spring:message code="label.pricingrule.seasonal.discount" /></label></th>
						<th align="center" class="help-row-bg" Colspan = "2" width="35%"><label class="label-bold"><spring:message code="label.pricingrule.price" /></label></th>
					</tr>
					<tr>
						<th align="center" class="help-row-bg"  width="5%"><label class="label-bold"><spring:message code="label.pricingrule.default" /></label></th>
						<th align="center" class="help-row-bg"  width="5%"><label class="label-bold"><spring:message code="label.pricingrule.category" /></label></th>
						<th align="center" class="help-row-bg"  width="5%"><label class="label-bold"><spring:message code="label.pricingrule.default" /></label></th>
						<th align="center" class="help-row-bg"  width="5%"><label class="label-bold"><spring:message code="label.pricingrule.category" /></label></th>
					</tr>
					<c:forEach var="rateProfileSummary"
						items="${calculatorSummary.rateProfileSummary}" varStatus="loop">
						<tr>
							<td>${rateProfileSummary.productName}</td>
							<td>${rateProfileSummary.salesTarget}</td>
							<td align="right">${rateProfileSummary.weight}</td>
							
							<td align="right">${rateProfileSummary.defaultDiscount}</td>
							<td align="right">${rateProfileSummary.salesCategoryDiscount}</td>
							<td align="right">${rateProfileSummary.defaultBasePrice}</td>
							<td align="right">${rateProfileSummary.salesCategoryBasePrice}</td>
						</tr>
					</c:forEach>
				</table>
				<c:choose>
					<c:when test="${not empty calculatorSummary.tierSummary}">
						<table id="pricing-calc-table" class="table-layout" width="100%" cellpadding="3" cellspacing="1">
							<tr class="ui-widget-header">
								<td align="left" colspan="4" style="padding: 5px;">
									<label style="padding-right: 5px;" class="label-bold"><spring:message code="label.generic.tier" />:</label>
									<span style="padding-right: 5px;">${calculatorSummary.tier}</span> | 
									<label style="padding:0 5px;" class="label-bold"><spring:message code="label.generic.level" />:</label>
									<span>${calculatorSummary.level}</span>
								</td>
							</tr>
							<tr>
								<th align="center" class="help-row-bg"  width="25%"><label class="label-bold"><spring:message code="label.generic.target.type" /></label></th>
								<th align="center" class="help-row-bg"  width="25%"><label class="label-bold"><spring:message code="label.generic.premium" /></label></th>
							</tr>
							<c:forEach var="tierSummary"
								items="${calculatorSummary.tierSummary}" varStatus="loop">
								<tr>
									<td>${tierSummary.targetType}</td>
									<td align="right">${tierSummary.premium}</td>
								</tr>
							</c:forEach>
						</table>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				<table class="pricing-summary" width="100%" cellpadding="3" cellspacing="1">
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.sales.category" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${calculatorSummary.salesCategory}</td>
						<td width="40%">&nbsp;</td>
					</tr>
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.weighted.base.price" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${calculatorSummary.weightedBasePrice}</td>
						<td width="40%">&nbsp;</td>
					</tr>
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.applied.premium" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${calculatorSummary.premium}</td>
						<td width="40%">&nbsp;</td>
					</tr>
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.calculated.agencyMargin" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${agencyMargin}</td>
						<td width="40%"></td>
					</tr>
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.calculated.fiveCentsRule" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${calculatorSummary.appliedFiveCentsRule}</td>
						<td width="40%"></td>
					</tr>
					<tr>
						<td class="bg-head" width="40%"><label class="label-bold"><spring:message code="label.generic.calculated.newPrice" /></label></td>
						<td>&nbsp;</td>
						<td width="20%">${calculatorSummary.price}</td>
						<td width="40%"></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<div class="pricing-msg"> <spring:message code="label.generic.base.price.not.available" /></div>
	</c:otherwise>
</c:choose></div>