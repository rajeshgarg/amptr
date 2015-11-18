<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id=discountDetailsDiv >
	<div id="discountDetails">
		<c:choose>
			<c:when test="${not empty discountList}">
				<div>
					<table style="width:320px;" class="availsSummary-line-table" id="discountTable">
						<tr id="discountDetailHeader">
							<th width="10%">
								<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount.Not" /></label>
							</th>
							<th width="15%">
								<label class="label-bold"><spring:message code="label.generic.startDate" /></label>
							</th>
							<th width="15%">
								<label class="label-bold"><spring:message code="label.generic.endDate" /></label>
							</th>
							<th width="15%">
								<label class="label-bold"><spring:message code="label.rateProfile.seasonalDiscount" /></label>
							</th>
						</tr>
						<c:forEach var="discountObj" items="${discountList}" varStatus="loop">
							<tr>
								<td class="fval">${discountObj.notChecked}</td>
								<td class="fval">${discountObj.startDate}</td>
								<td class="fval">${discountObj.endDate}</td>
								<td class="fval">${discountObj.discount}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:when>
			<c:otherwise>
	   			<div style="text-align: center;"> <spring:message code="label.generic.salesTarget.no.data.available" /></div>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>