<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form action="../manageAttribute/saveAttribute.action" id="attributeForm"  name="attributeForm" method="post" modelAttribute="attributeForm">
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
					<td width="35%">
						<form:hidden path="forceUpdate" id="forceUpdate"/>
						<form:hidden path="version" id="version"/>
						<form:hidden path="attributeId" id="attributeId"/>
						<form:hidden path="attributeTypeStr" id="attributeTypeStr"/>
						<form:input maxlength="60" path="attributeName" id="attributeName" size="50" cssClass="textbox"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.name'/>" />
					</td>
				</tr>
				<tr>
					<td align="right"><span class="mandatory">*&nbsp;</span></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.type"/>:
						</label>
					</td>
					<td>
						<form:select path="attributeType" id="attributeType">
							 <form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>
							 <form:options items="${attributeForm.attributTypes}"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.optionalValue"/>:
						</label>
					</td>
					<td width="35%">
						<form:input maxlength="200" path="attributeOptionalValue" id="attributeOptionalValue" size="50" cssClass="textbox"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.value'/>" />
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td style="vertical-align: middle;">
						<label class="label-bold"><spring:message code="label.generic.description"/>:</label>
						<div id="charsRemaining" class="chars-remaining"></div>
					</td>
					<td>
						<form:textarea path="attributeDescription" rows="5" cols="100" id="attributeDescription" />
						<img src='../images/info-icon.png' class="top-170 tip-toolright" title="<spring:message code='help.attribute.description'/>" />
					</td>
				</tr>
				
			</table>
		</div>
		<div align="center">
			<table align="center" width="20%">
				<tr>
					<td><input type="button" value="SAVE" id="attributeSaveData" class="save-btn"/></td>					
					<td><input type="button" value="RESET" id="attributeResetData" class="reset-btn"/></td>
				</tr>	
			</table>	
		</div>
	</div>
</form:form>	
