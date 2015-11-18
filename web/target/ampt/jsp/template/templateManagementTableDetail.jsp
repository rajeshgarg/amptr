<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<table id="uploadFileInfoTable" cellspacing="2" cellpadding="2" width="100%" style="table-layout: fixed;">
	<tr>
		<td align="right" width="2%">
			<span class="mandatory">*&nbsp;</span>
		</td>
		<td width="9%">
			<label class="label-bold"><spring:message code="label.generic.template.name" />:</label>
		</td>
		<td width="20%">
			<input type="hidden" id="tmpFile" name="tmpFile" value='${tmpFile}' /> 
			<input type="text" id="templateName" name="templateName" size="20" value='${templateName}' />
			<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.template.name.length'/>" />
		</td>
		<td width="10%">
			<label class="label-bold"><spring:message code="label.generic.use.existing.row" />:</label>
		</td>
		<td width="7%">
			<input type="checkbox" id="useExistingRowCB" style="vertical-align:-1px;" value="useExistingRowCB" <c:if test="${useExistingRow}">checked="checked"</c:if> />
			<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.template.use.existing.row'/>" />
		</td>
		<td width="6%">
			<label class="label-bold"><spring:message code="label.generic.file.name" />:</label>
		</td>
		<td width="18%">
			<input type="text" id="fileName" size="20" name="fileName" class="input-textbox-readonly" onfocus="this.blur()" value='${templateFileName}' />
		</td>
		<td width="10%">
			<div id="editUplodedTemplate" ><a class="anchor-link edit" onclick="editTemplate()" href="#">Edit Template</a></div>
		</td>
	</tr>

</table>
<div class="spacer7"></div>
<div>
	<c:forEach var="templateAttributesMap" items="${gridKeyColumnValue}" varStatus="loopCount">
		<c:choose>
			<c:when test="${'PROPOSAL_HEADER' == templateAttributesMap.key}">
				<fieldset style="border: 1px solid #9FC1F8;margin-bottom: 20px;padding-bottom: 15px;">
					<legend style="margin:0 0 0 10px;padding: 0 5px;" class="label-bold"><spring:message code="label.generic.proposal.configuration"/></legend>
					<table id="template_upload_table_pro" class="template-upload-doc-table" cellpadding="2" cellspacing="0">
						<tr>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.attribute.name" /></th>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.proposal.head" /></th>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.proposal.attribute" /></th>
							<th class="label-bold" width="10%"><spring:message code="label.generic.template.attribute.font" /></th>
						</tr>
						<c:forEach var="sheetData" items='${templateAttributesMap.value}' varStatus="loop">
							<tr id="template_upload_table_pro_${loop.count}">
								<td>
									<span class="mandatory">*&nbsp;</span> 
									<label id="templateAttribute_pro_${loop.count}" title="${sheetData.attributeName}">${sheetData.attributeName}</label>
									<input type="hidden" id="templateSheetRowNum_pro_${loop.count}" value="${sheetData.rowNum}" /> 
									<input type="hidden" id="templateSheetColNum_pro_${loop.count}" value="${sheetData.colNum}" /> 
									<input type="hidden" id="templateMetaDataAttributesID_pro_${loop.count}" value="${sheetData.templateMetaDataAttributesID}" />
								</td>
								<td>
									<select id="templateProposalName_pro_${loop.count}" onChange="getProposalAttribute('${loop.count}','','proposal')">
										<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
										<c:forEach var="ProposalHead" items="${ProposalHead}" varStatus="loopCount">
											<c:forEach var="ProposalHeadValue" items="${ProposalHead.value}" varStatus="loopCount">
												<c:choose>
													<c:when test="${sheetData.attributeType == ProposalHead.key}">
														<option value='${ProposalHead.key}' selected="selected">${ProposalHeadValue}</option>
													</c:when>
													<c:otherwise>
														<option value='${ProposalHead.key}'>${ProposalHeadValue}</option>
													</c:otherwise>
												</c:choose>
										 	</c:forEach>
										</c:forEach>
									</select>
								</td>
								<td>
									<select id="templateProposalAttribute_pro_${loop.count}" Style="width: 100%;">
										<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
										<c:forEach var="ProposalAttr" items="${sheetData.displayAttributMap}" varStatus="loopCount">
											<c:choose>
												<c:when	test="${sheetData.attributeTypeKeyId == ProposalAttr.key}">
													<option value='${ProposalAttr.key}' selected="selected">${ProposalAttr.value}</option>
												</c:when>
												<c:otherwise>
													<option value='${ProposalAttr.key}'>${ProposalAttr.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<td>
									<select id="templateAttributeFontSize_pro_${loop.count}" Style="width: 100%;">
										<option value=''></option>
										<c:forEach var="font" items="${fontsize}" varStatus="loopCount">
											<c:choose>
												<c:when test="${sheetData.fontSize == font}">
													<option value='${font}' selected="selected">${font}</option>
												</c:when>
												<c:otherwise>
													<option value='${font}'>${font}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</c:when>
			<c:when test="${'PROPOSAL_LINEITEM' == templateAttributesMap.key}">
				<fieldset style="border: 1px solid #9FC1F8; padding-bottom: 15px;">
					<legend class="label-bold" style="margin: 0 0 0 10px; padding: 0 5px;"><spring:message code="label.generic.line.item.configuration" /></legend>
					<table id="template_upload_table" class="template-upload-doc-table" cellpadding="2" cellspacing="0">
						<tr>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.attribute.name"/></th>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.proposal.head"/></th>
							<th class="label-bold" width="30%"><spring:message code="label.generic.template.proposal.attribute"/></th>
							<th class="label-bold" width="10%"><spring:message code="label.generic.template.attribute.font"/></th>
						</tr>
						<c:forEach var="sheetData" items='${templateAttributesMap.value}' varStatus="loop">
							<tr id="template_upload_table_${loop.count}">
								<td><span class="mandatory">*&nbsp;</span> 
									<label id="templateAttribute_${loop.count}" title="${sheetData.attributeName}">${sheetData.attributeName}</label>
									<input type="hidden" id="templateSheetRowNum_${loop.count}"	value="${sheetData.rowNum}" /> 
									<input type="hidden" id="templateSheetColNum_${loop.count}" value="${sheetData.colNum}" />
									<input type="hidden" id="templateMetaDataAttributesID_${loop.count}" value="${sheetData.templateMetaDataAttributesID}" />
								</td>
								<td>
									<select id="templateProposalName_${loop.count}" onChange="getProposalAttribute('${loop.count}','','')">
										<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
										<c:forEach var="ProposalLineItemHead" items="${ProposalLineItemHead}" varStatus="loopCount">
											<c:forEach var="ProposalLineItemHeadValue"	items="${ProposalLineItemHead.value}" varStatus="loopCount">
												<c:choose>
													<c:when	test="${sheetData.attributeType == ProposalLineItemHead.key}">
														<option value='${ProposalLineItemHead.key}'	selected="selected">${ProposalLineItemHeadValue}</option>
													</c:when>
												<c:otherwise>
													<option value='${ProposalLineItemHead.key}'>${ProposalLineItemHeadValue}</option>
												</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:forEach>
									</select>
								</td>
								<td>
									<select id="templateProposalAttribute_${loop.count}" Style="width: 100%;">
										<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
										<c:forEach var="ProposalLineItemAttr" items="${sheetData.displayAttributMap}" varStatus="loopCount">
											<c:choose>
												<c:when	test="${sheetData.attributeTypeKeyId == ProposalLineItemAttr.key}">
													<option value='${ProposalLineItemAttr.key}' selected="selected">${ProposalLineItemAttr.value}</option>
												</c:when>
												<c:otherwise>
													<option value='${ProposalLineItemAttr.key}'>${ProposalLineItemAttr.value}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<td>
									<select id="templateAttributeFontSize_${loop.count}" Style="width: 100%;">
										<option value=''></option>
										<c:forEach var="font" items="${fontsize}" varStatus="loopCount">
											<c:choose>
												<c:when test="${sheetData.fontSize == font}">
													<option value='${font}' selected="selected">${font}</option>
												</c:when>
												<c:otherwise>
													<option value='${font}'>${font}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
							</tr>
						</c:forEach>
					</table>
				</fieldset>
			</c:when>
		</c:choose>
	</c:forEach >
</div>
<div id="buttonDiv" class="button-div float-none">
<table style="margin: 0;">
	<tr>
		<td width="46%" class="align-right">
			<input type="button" value="SAVE" id="templateSaveData" onclick="submitForm()" class="save-btn" />
		</td>
		<td width="10%" class="align-left">
			<input type="button" value="RESET" id="templateResetData" onclick="resetUploadedTemplate()" class="save-btn" />
		</td>
		<td width="35%" class="align-left"></td>
	</tr>
</table>
</div>
<input type="hidden" id="hiddenSheetName" value='${templateSheetName}' />
<input type="hidden" id="hiddenSheetID" value='${templateSheetId}' />
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>
