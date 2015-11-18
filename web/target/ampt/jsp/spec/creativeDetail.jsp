<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form action="../manageCreative/savecreative.action" id="creativeForm" name="creativeForm" method="post" modelAttribute="creativeForm">
	<div>
		<div id="messageHeaderDiv" class="error"></div>
		<div>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td width="5%" align="right"><span class="mandatory">*&nbsp;</span></td>
					<td width="10%">
						<label class="label-bold">
							<spring:message code="label.generic.name"/>:
						</label>
					</td>
					<td width="35%" colspan="4">
						<form:hidden path="typeStr" id="typeStr"/>
						<form:hidden path="version" id="version"/>
						<form:hidden path="forceUpdate" id="forceUpdate"/>
						<form:hidden path="creativeId" id="creativeId"/>
						<form:input maxlength="60" path="name" id="name" size="50"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.creative.name'/>" />
					</td>
				</tr>
				<tr>
					<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.type"/>:</label>
					</td>
					<td colspan="4">
						<form:select path="type" id="type">
							<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
							<form:options items="${creativeForm.creativeTypesMap}"/>
						</form:select>
					</td>
				</tr>
				<tr>
				<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.width"/>:
						</label>
					</td>
					<td>
						<form:input path="width" id="width" size="10" cssClass="numeric"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.width'/>" />
					</td>
					<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.height"/>:
						</label>
					</td>
					<td>
						<form:input path="height" id="height" size="10" cssClass="numeric"/>
						<img src='../images/info-icon.png' class="tip-toolright" title="<spring:message code='help.attribute.height'/>" />
					</td>
				</tr>
				<tr>
					<td align="right"><span class="mandatory">&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.width2"/>:
						</label>
					</td>
					<td>
						<form:input path="width2" id="width2" size="10" cssClass="numeric"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.width'/>" />
					</td>
					<td align="right"><span class="mandatory">&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.height2"/>:
						</label>
					</td>
					<td>
						<form:input path="height2" id="height2" size="10" cssClass="numeric"/>
						<img src='../images/info-icon.png' class="tip-toolright" title="<spring:message code='help.attribute.height'/>" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td style="vertical-align: middle;">
						<label class="label-bold"><spring:message code="label.generic.description"/>:</label>
						<div id="charsRemaining" class="chars-remaining"></div>
					</td>
					<td colspan="4">
						<form:textarea rows="5" cols="100" path="description" id="description"/>
						<img src='../images/info-icon.png' class="top-170 tip-toolright" title="<spring:message code='help.creative.description'/>" />
					</td>
				</tr>
			</table>
		</div>
		<div align="center">
			<table align="center" width="20%">
				<tr>
					<td><input value="SAVE" type="button" id="creativeSaveData" class="save-btn"/></td>					
					<td><input value="RESET" type="button" id="creativeResetData" class="reset-btn"/></td>
				</tr>	
			</table>	
		</div>
	</div>
</form:form>	
