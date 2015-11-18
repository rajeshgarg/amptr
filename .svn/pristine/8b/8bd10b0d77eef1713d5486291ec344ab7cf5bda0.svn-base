<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
var parentContainer="";
var formObj="";
var attrType='${attributeType}';
</script>
<script type="text/javascript" src="../js/spec/attribute.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
<script type="text/javascript" src="../js/common/infoTip.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
<form:form id="attributeassoc${attributeType}" name="attributeassoc${attributeType}" action="../manageAttribute/saveAttributeFromAssoc.action" modelAttribute="attributeAssocForm" >
	<fieldset id="attributeAssocContainer">
	   	<div id="messageHeaderDiv"></div>
		<table width="100%" cellpadding="2" cellspacing="2">
			<tr id="attributeAction">
				<td><span class="mandatory">*&nbsp;</span></td>
				<td><label class="label-bold"><spring:message code="label.generic.action"/>:</label>
				</td>
				<td>								
					<form:radiobutton path="action" id="assocaction" value="AssociateAttribute" onclick="setAction('associate')"/>
					<label class="label-bold"><spring:message code="label.generic.AssociateAttribute"/></label>
					&nbsp;&nbsp;					
					<form:radiobutton path="action" id="addaction" value="AddNew" onclick="setAction('add')"/>
					<label class="label-bold"><spring:message code="label.generic.AddNewAttribute"/></label>	
				</td>	
			</tr>
			<tr>
				<td colspan="3">&nbsp;</td>
			</tr>
			<tr>
				<td><span class="mandatory">*&nbsp;</span></td>
				<td><label class="label-bold"><spring:message code="label.generic.name"/>:</label>
				</td>	
				<td id="attributeNameLabel" style="display:none;">
					<form:input path="attributeName" maxlength="60" size="50" id="attributeName"/>	
					<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.name'/>" />			
				</td>	
				<td id="attributeSelectLabel" >
					<form:select path="attributeId" id="attributeId" onchange="fillAttributeDetails(this)" >
						<form:option value=""><spring:message code="label.generic.blankSelectOption"/></form:option>				
						<form:options items="${allAttributes}" />								
					</form:select>
					<span class="attributeassoc">	
					<a id="attributeAssocDetails_${attributeType}" href="#" style="display:none;" onclick="attributeAssocDetails();">
						&nbsp;Associated Values
					</a>	
					</span>			
				</td>	
			</tr>			
			<tr>
				<td><span class="mandatory">*&nbsp;</span></td>
				<td><label class="label-bold"><spring:message code="label.generic.optionalValue"/>:</label>
				</td>	
				<td>
					<form:input path="attributeValue" maxlength="200" size="30" id="attributeValue"/>
					<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.attribute.value'/>" />	
					<form:hidden path="attributeType" id="attributeType"/>
					<form:hidden path="operation" id="operation"/>
					<form:hidden path="id" id="id"/>
					<form:hidden path="hidAttributeId" id="hidAttributeId"/>
					<form:hidden path="salestargetId" id="salestargetId"/>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td style="vertical-align: middle;"><label class="label-bold"><spring:message code="label.generic.description"/>:</label>
				<div id="charsRemainingCreativeAttr" class="chars-remaining"></div>
				</td>	
				<td>
					<form:textarea path="attributeDescription" rows="4" cols="60" id="attributeDescription"  disabled="true"/>		
					<img src='../images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.attribute.description'/>" />			
				</td>
			</tr>		
		</table>				
	</fieldset>
</form:form>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>