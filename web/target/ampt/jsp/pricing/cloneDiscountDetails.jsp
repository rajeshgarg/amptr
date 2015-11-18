<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
$(function () { 
	$('.allRateProfiles').click(function () {
		$(this).parents('div').find(':checkbox').filter(function() { return !this.disabled; }).attr('checked', this.checked);
	});
});

function selectUnselectProfile(obj){
	if(!$(obj).is(':checked')){
		$("#selectAllProfiles").attr('checked', false);
  	}
	if($('[name=rateProfile]:not(:checked)',"#rateProfileSummary").size() == 0){
  		$("#selectAllProfiles").attr("checked", true);
  	}	
}
</script>
<div id=cloneDiscountDiv >
	<div id="rateProfileSummary">
		<div class="summary-table-header" style="position:static;width:1062px;">
			<table style="width:100%" cellpadding="2" cellspacing="2" class="availsSummary-line-table">
				<tr>
					<th width="3%">
						<input type="checkbox" id="selectAllProfiles"  class="allRateProfiles" >
					</th>
					<th width="15%">
						<label class="label-bold"><spring:message code="label.generic.productName" /></label>
					</th>
					<th width="15%">
						<label class="label-bold"><spring:message code="label.generic.section" /></label>
					</th>
					<th width="30%">
						<label class="label-bold"><spring:message code="label.generic.salesTarget" /></label>
					</th>
					<th width="7%">
						<label class="label-bold"><spring:message code="label.pricingrule.price" /></label>
					</th>
					<th width="10%">
						<label class="label-bold"><spring:message code="label.clone.discount.lookup.discount" /></label>
					</th>
				</tr>
			</table>
		</div>
		<div style="height:200px;position:static;overflow-y:auto;width:1080px;">
			<table style="width:100%" cellpadding="2" cellspacing="2" id="rateProfileDetailsTable" class="availsSummary-line-table">
			<c:choose>
				<c:when test="${not empty rateProfileList}">
						<c:forEach var="rateProfileObj" items="${rateProfileList}" varStatus="loop">
							<tr id="${rateProfileObj.profileId}" class="availsSummary-table-records">
								<td class="fval" style="vertical-align: middle;" width="3%">
									<input type="checkbox" name="rateProfile" value=${rateProfileObj.profileId} onclick="selectUnselectProfile(this)" style="margin:0 auto;display:block" />
								</td>
								<td class="fval" style="text-align: left; vertical-align: middle!important;" width="15%">${rateProfileObj.productName}</td>
								<td class="fval" style="text-align: left; vertical-align: middle!important;" width="15%">${rateProfileObj.sectionNames}</td>
								<td class="fval" style="text-align:left;" width="30%" title="${rateProfileObj.salesTargetNamesStr}">
									<span style="width:100%;display:inline-block;word-break:break-all;">${rateProfileObj.salesTargetNamesStr}</span>
								</td>
								<td class="fval" style="text-align: right; vertical-align: middle!important;" width="7%">${rateProfileObj.basePrice}</td>
								<td class="fval" style="text-align: center; vertical-align: middle!important;" width="10%">
									<a href="javascript:void(0)" id="lookUpDiscounts_${rateProfileObj.profileId}" class="grid-lookup" onClick="getDiscountData(${rateProfileObj.profileId})" title="Lookup"></a>
								</td>
								<td id="rateProfileId_${rateProfileObj.profileId}" class="fval" style="display: none" width="15%">${rateProfileObj.profileId}</td>
							</tr>
						</c:forEach>
				</c:when>
				<c:otherwise>
	   				<tr>
	   					<%-- <div style="text-align: center;"> <spring:message code="label.generic.salesTarget.no.data.available" /></div> --%>
	   					<td colspan="7"><spring:message code="label.generic.salesTarget.no.data.available" /></td>
	   				</tr>
				</c:otherwise>
			</c:choose>
			</table>
		</div>	
	</div>
</div>
<div class="spacer7"></div>
<div>
	<img src='../images/warning.png'>
	<strong style="vertical-align:8px;">Existing seasonal discounts associated with the selected Rate Profiles will be removed.</strong>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>
	