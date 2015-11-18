<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<form:form action="../managepackage/savepackage.action" id="ManagePackage" name="ManagePackage" method="post" modelAttribute="packageForm">
	<div>
		<div id="messageHeaderDiv" class="error"></div>
		<div>
			<table align="center" width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
				<tr>
					<td width="4%" align="right"><span class="mandatory">*&nbsp;</span></td>
					<td width="10%">
						<label class="label-bold">
							<spring:message code="label.generic.packageName"/>:
						</label>
					</td>
					<td width="29%">
						<form:hidden id="packageId" path="packageId"/>
						<form:hidden id="packageSalescategoryName" path="packageSalescategoryName"/>
						<form:input maxlength="150" path="packageName" id="packageName" size="50" cssClass="textbox"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.package.name'/>" />
					</td>
					<td width="2%" align="right"><span class="mandatory">*&nbsp;</span></td>
					<td width="11%">
						<label class="label-bold">
							<spring:message code="label.generic.packageOwner"/>:
						</label>
					</td>
					<td width="35%">
						<form:hidden id="packageOwner" path="packageOwner"/>
						<form:select path="ownerId" id="ownerId">
							<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
							<form:options items="${allUsersList}" />
						</form:select>
					</td>
				</tr>				
				<tr>
					<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.budget"/>:
						</label>
					</td>
					<td>
						<form:input maxlength="16" path="budget" id="budget" cssClass="numericdecimal" size="15"/>
					</td>
					<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.breakable"/>:
						</label>
					</td>
					<td>
						<form:hidden id="breakableStr" path="breakableStr"/>
						<select name="breakable" id="breakable">
							<option value=""><spring:message code="label.generic.blankSelectOption"/></option>
							<option value="true"><spring:message code="label.generic.yes"/></option>
							<option value="false"><spring:message code="label.generic.no"/></option>
						</select>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.startDate"/>:
						</label>
					</td>
					<td>
						<form:input maxlength="13" path="validFrom" id="validFrom" readonly="true" size="12"/>
						<span id="validFromReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>
					<td align="right">&nbsp;</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.endDate"/>:
						</label>
					</td>
					<td>
						<form:input maxlength="13" path="validTo" id="validTo" readonly="true" size="12"/>
						<span id="validToReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
						<form:hidden path="expired" id="expired"/>
					</td>
				</tr>
				<tr>
				<td>&nbsp;</td>
					<td style="vertical-align: middle;">
							<label class="label-bold">
									<spring:message code="label.basicinfo.salescategory"/>:
								</label>
					</td>
					<td class="basic-advertiser-column">
						<div id="packageSalescategory_custom" class="multi-select-box">   
	               			<form:select path="packageSalescategory" id="packageSalescategory" multiple="multiple" size="5">
	               				<form:options items="${allSalesCategories}" />	
	               			</form:select>
						</div>
							</td>
					
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td style="vertical-align: middle;">
						<label class="label-bold">
							<spring:message code="label.generic.comments"/>:
						</label>
						<div id="charsRemaining" class="chars-remaining"></div>
					</td>
					<td colspan="5">
						<form:textarea path="comments" id="comments" rows="5" cols="100"/>
						<img src='../images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.package.comment'/>" />
					</td>
				</tr>
			</table>
		</div>
		<div align="center">
			<table align="center" width="20%">
				<tr>
					<td><input type="button" value="SAVE" id="packageSaveDataBtn" class="save-btn"/></td>					
					<td><input type="button" value="RESET" id="packageResetDataBtn" class="reset-btn"/></td>
				</tr>	
			</table>	
		</div>
	</div>
</form:form>	
