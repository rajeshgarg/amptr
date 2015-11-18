<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
$(function () { // this line makes sure this code runs on page load
	$('.allPopupAvails').click(function () {
		//$(this).parents('div').find(':checkbox').attr('checked', this.checked);
		$(this).parents('div').find(':checkbox').filter(function() { return !this.disabled; }).attr('checked', this.checked);
	});
});


function selectUnselectAvails(obj){
	if(!$(obj).is(':checked')){	
		$("#selectAllAvails").attr('checked', false);
  	}  	
}
</script>


<div id="availsSummary">
	<c:choose>
		<c:when test="${not empty availsSummaryList}">
		<div style="height:45px;position:static;width:1062px;">
			<table width="100%" cellpadding="2" cellspacing="2" class="availsSummary-line-table th-flat-background">
				<tr style="background: url('<%= request.getContextPath() %>/images/inner-bg-tab-sub.png') repeat scroll 50% 50% #4A84CE;">
					<th style="width:28px;border-left:1px solid #9fc1f8">
						<input type="checkbox" id="selectAllAvails"  name="allLineitem" class="allPopupAvails" style="vertical-align: -13px;">
					</th>
					<th style="width:72px">
						<label class="label-bold"><spring:message code="label.generic.sequenceNo" /></label>
					</th>
					<th style="width:150px">
						<label style="vertical-align:-10px;" class="label-bold"><spring:message code="label.generic.productName" /></label>
					</th>
					<th style="width:150px">
						<label style="vertical-align:-10px;" class="label-bold"><spring:message code="label.generic.salesTarget" /></label>
					</th>
					<th style="width:100px">
						<label style="vertical-align:-10px;" class="label-bold"><spring:message code="label.generic.flightType" /></label>
					</th>
					<th style="width:80px">
						<label class="label-bold"><spring:message code="label.generic.projected.sov" /></label>
					</th>
					<th style="width:100px">
						<label class="label-bold"><spring:message code="label.generic.impressionTotal" /></label>
					</th>
					<th style="padding-right: 0;padding-left: 0;width:158px">
						<label class="label-bold">Line Item</label>
						<div style="border-top: 1px solid #9fc1f8;margin-top: 2px;">
							<span class="label-bold" style="width: 48%;display: inline-block;padding: 4px 0;border-right: 1px solid #9fc1f8;"><spring:message code="label.generic.avails" /></span>
							<span class="label-bold" style="width: 48%;display: inline-block;padding: 4px 0;">Capacity</span>
						</div>
					</th>
					<th style="padding-right: 0;padding-left: 0;width:158px">
						<label class="label-bold">Current</label>
						<div style="border-top: 1px solid #9fc1f8;margin-top: 2px;">
							<span class="label-bold" style="width: 48%;display: inline-block;padding: 4px 0;border-right: 1px solid #9fc1f8;"><spring:message code="label.generic.avails" /></span>
							<span class="label-bold" style="width: 48%;display: inline-block;padding: 4px 0;">Capacity</span>
						</div>
					</th>
				</tr>
			</table>
		</div>
		<div style="height:350px;position:static;overflow-y:scroll;width: 1079px;">
		<table width="100%" cellpadding="2" cellspacing="2" class="availsSummary-line-table">
				<c:forEach var="availsSummaryObj" items="${availsSummaryList}" varStatus="loop">
					<c:choose>
						<c:when test="${availsSummaryObj.offeredImpLessThanCurAvails == 'true'}">
							<tr class="availsSummary-table-records expire-package">
						</c:when>
						<c:otherwise>
   							<tr class="availsSummary-table-records"> 
						</c:otherwise>	
					</c:choose>
						<td class="fval" style="vertical-align: middle;width:28px">
							<c:choose>
								<c:when test="${(availsSummaryObj.startDate =='' && availsSummaryObj.endDate =='' ) || availsSummaryObj.currentAvails == 'NA' || availsSummaryObj.sov > 100 
													|| availsSummaryObj.currentTotalUICamparision < 0 || availsSummaryObj.currentTotalUICamparision == 'NA' 
													|| availsSummaryObj.currentAvailsUICamparision < 0 || availsSummaryObj.currentAvailsUICamparision == 'NA'}">
										<input type="checkbox" name="lineitem" class="lineitem" disabled="disabled" value=${availsSummaryObj.lineItemID} style="margin:0 auto;display:block"/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="lineitem" class="lineitem" value=${availsSummaryObj.lineItemID} onclick="selectUnselectAvails(this)" style="margin:0 auto;display:block" />
									<span id="avail_${availsSummaryObj.lineItemID}" style="display: none;">${availsSummaryObj.currentAvails}</span>
									<span id="capacity_${availsSummaryObj.lineItemID}" style="display: none;">${availsSummaryObj.currentTotalPossibleImpressions}</span>
									<span id="availsDate_${availsSummaryObj.lineItemID}" style="display: none;">${availsSummaryObj.availsPopulatedDate}</span>
								</c:otherwise>	
							</c:choose>
						</td>
						<td class="fval" style="text-align: right;width:72px">${availsSummaryObj.lineItemSequence}</td>
						<td class="fval" style="text-align: left;width:150px">${availsSummaryObj.productName}</td>
						<td class="fval" style="text-align:left;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;width:150px" title="${availsSummaryObj.sosSalesTargetName}" >${availsSummaryObj.sosSalesTargetName}</td>
						<td class="fval" style="text-align: center;width:100px">
							<c:choose>
								<c:when test="${availsSummaryObj.startDate !='' && availsSummaryObj.endDate !=''}">
									${availsSummaryObj.startDate} - ${availsSummaryObj.endDate} 
								</c:when>
								<c:otherwise>
   									${availsSummaryObj.flight} 
								</c:otherwise>	
							</c:choose>
						</td>
						<c:choose>
							<c:when test="${availsSummaryObj.sov > 100}">
								<td class="fval" style="text-align: right;color:red;width:80px">${availsSummaryObj.sov}</td>
							</c:when>
							<c:otherwise>
							<c:choose>
								<c:when test="${availsSummaryObj.productType == 'R' || availsSummaryObj.productType == 'E'}">
									<td class="fval" style="text-align: right;width:80px">NA</td>
								</c:when>
								<c:otherwise>
									<td class="fval" style="text-align: right;width:80px">${availsSummaryObj.sov}</td>
								</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						<td class="fval" style="text-align: right;width:100px">${availsSummaryObj.impressionTotal}</td>
						<td style="width:150px">
							<table width="100%">
								<tbody>
									<tr style="background:none;">
										<td width="50%" style="text-align: right;border:none">
											<label>${availsSummaryObj.avails}</label>
										</td>
										<td width="50%" style="text-align: right;border:none">
											<label>${availsSummaryObj.totalPossibleImpressions}</label>
										</td>
									</tr>
								</thead>
							</table>
						</td>
						<td style="width:150px">
							<table width="100%">
								<tbody>
									<tr style="background:none;">
										<td width="50%" style="text-align: right;border:none">
											<label>${availsSummaryObj.currentAvails}</label>
										</td>
										<td width="50%" style="text-align: right;border:none">
											<label>${availsSummaryObj.currentTotalPossibleImpressions}</label>
										</td>
									</tr>
								</thead>
							</table>
						</td>
					</tr>
				</c:forEach>
			</table>
			</div>
			<c:if test="${sovExceedFlag eq true}">
			  	<div id='staticNoteForAvailsDailogue' style='color:red'><spring:message code="error.lineitem.sov.greater.than.hundred" /></div>
			</c:if>
		</c:when>
		<c:otherwise>
   			<div style="text-align: center;"> <spring:message code="label.generic.salesTarget.no.data.available" /></div>
		</c:otherwise>
	</c:choose>
</div>