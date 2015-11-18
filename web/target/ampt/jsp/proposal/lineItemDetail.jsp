<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
	<tr>
		<td><label class="label-bold"><spring:message code="label.generic.lineItemName"/></label>@@@</td>
		<td colspan="2"><form:input path="name" maxlength="256" id="name" size="53"/></td>
	</tr>	
	<tr>
		<td><label class="label-bold"><spring:message code="label.generic.product"/></label></td>
		<td colspan="2">
			<form:select path="sosProductId" id="sosProductId" cssStyle="width: 250px;" >
				<option value=""><spring:message code="label.generic.blankSelectOption"/></option>		
				<form:options items="${allProducts}" />								
			</form:select>
		</td>	
	</tr>
	<tr>
		<td><label class="label-bold"><spring:message code="label.generic.salesTarget"/></label></td>
		<td colspan="2">
			<form:select path="sosSalesTargetId" id="sosSalesTargetId" cssStyle="width: 250px;" >
				<option value=""><spring:message code="label.generic.blankSelectOption"/></option>		
				<form:options items="${allSalesTarget}" />								
			</form:select>
		</td>
	</tr>
	<tr>
		<td><label class="label-bold">Start Date</label></td>	
		<td colspan="2"><form:input  id="startDate" path="startDate" readonly="true" size="12" maxlength="13" /></td>
	</tr>
	<tr>
		<td><label class="label-bold">End Date</label></td>	
		<td colspan="2"><form:input  id="endDate" path="endDate" readonly="true" size="12" maxlength="13" /></td>
	</tr>
	<tr>
		<td><label class="label-bold"><spring:message code="label.generic.flight"/></label></td>	
		<td colspan="2">
			<form:input path="flightUnit" maxlength="4" id="flightUnit" size="4" />
			<form:select path="flight"  id="flight" >
				<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
				<form:options items="${allFlightType}" />
			</form:select>
		</td>	
	</tr>					
	<tr id="tr_priceType">
		<td><label class="label-bold"><spring:message code="label.generic.priceType"/></label></td>	
		<td colspan="2">
			<form:radiobutton path="priceType"  id="cpm"  onclick="onClickPriceTypeRadio(this.value);" value="CPM"  /><spring:message code="label.generic.priceType.CPM"/><br>
			<form:radiobutton path="priceType"  id="fixed"  onclick="onClickPriceTypeRadio(this.value);" value="FIXED"  /><spring:message code="label.generic.priceType.FIXED"/>
		</td>
	</tr>
	<tr id="tr_rate">
		<td><label class="label-bold"><spring:message code="label.generic.netCPM"/></label></td>	
		<td colspan="2"><form:input path="rate" maxlength="500" id="rate"  /></td>
	</tr>
	<tr id="tr_impressionTotal">
		<td><label class="label-bold"><spring:message code="label.generic.impressionTotal"/></label></td>	
		<td colspan="2"><form:input path="impressionTotal" maxlength="500" id="impressionTotal" /></td>
	</tr>
	<tr id="tr_totalInvestment">
		<td><label class="label-bold"><spring:message code="label.generic.totalInvestment"/></label></td>	
		<td colspan="2"><form:input path="totalInvestment" maxlength="500" id="totalInvestment" />
			<form:hidden path="lineItemID" id="lineItemID"/>
		</td>
	</tr>
	<tr>
		<td style="vertical-align: middle;"><label class="label-bold"><spring:message code="label.generic.comments"/></label></td>	
		<td colspan="2"><form:textarea path="comments" cols="50" rows="5" id="comments"  /></td>
	</tr>
	<tr>
		<td colspan="3" align="center"><input type="button" id="lineItemSaveData" class="save-btn"/>&nbsp;&nbsp;<input type="button" id="lineItemResetData" class="reset-btn"/></td>
	</tr>
</table>