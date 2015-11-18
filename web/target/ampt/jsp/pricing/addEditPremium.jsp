<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

	<div id=addEditTierPremiumDiv >
	<form:form id="tierPremiumDetail" name="tierPremiumDetail" action="../manageTier/saveTierPremium.action" method="post" modelAttribute="tierPremiumForm">
		<div>
			<div id="messageHeaderDiv" class="error"></div>
			<div>
				<table width="100%" cellpadding="2" cellspacing="2" border="0">
					<tbody>
						<tr>
							<td width="5%" align="right" style="vertical-align: middle;"><span class="mandatory">*&nbsp;</span></td>
							<td style="vertical-align: middle;" >
								<label class="label-bold"> <spring:message code="label.manageTier.targetType" />: </label>
							</td>
							<td>
								<form:select path="targetTypeId" id="targetTypeId" cssStyle="width: 90%;" onChange="getSelectedTargetTypeElements(null,'')"  >
									<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
									<form:options items="${targetTypes}" />
								</form:select>
							</td>
						</tr>					
						<tr id="targetElementsInfo">
							<td align="right" style="vertical-align: middle;">&nbsp;</td>
							<td style="vertical-align: middle;">
								<label class="label-bold">
									<spring:message code="label.manageTier.targetElements"/>:
								</label>
							</td>
							<td class="multiselect-sales-target multiselect-sales-target-selectbox">
								<div id="targetTypeElements_custom" class="multi-select-box" style="width:350px">
									<form:select path="targetTypeElements" id="targetTypeElements" multiple="multiple" cssStyle="width: 90%;" size="5">
									</form:select>
								</div>
							</td>
						</tr>
						<tr>
							<td  align="right" style="vertical-align: middle;"><span class="mandatory">*&nbsp;</span></td>
							<td  style="vertical-align: middle;">
								<label class="label-bold">
									<spring:message code="label.generic.premium"/>:
								</label>
							</td>
							<td >
								<form:hidden path="hidTargetTypeId"  id="hidTargetTypeId" />
								<form:hidden path="premiumTierId"  id="premiumTierId" />
								<form:hidden path="id"  id="id" />
								<form:hidden path="targetName"  id="hidTargetName" />
								<form:input path="premium" id="premium" cssClass="numericdecimal" maxlength="6" size="40" align="right"/>
								<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.tier.premium'/>" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>		
		</form:form>
	</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>
	