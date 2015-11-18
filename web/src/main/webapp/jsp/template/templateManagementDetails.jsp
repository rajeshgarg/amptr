<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div>
	<div id="messageHeaderDiv" class="error"></div>
	<div id="helpContent" style="float: right; margin-top: -12px;">
		<a class="anchor-link" id="templatesHelp" onclick="viewTemplateHelp();" style="" href="#" aria-describedby="ui-tooltip-14">Help</a>
	</div>
	<div id="templateUploadDiv" class="browse-div float-none" style="display: none">
		<table cellpadding="2" cellspacing="2" width="100%">
			<tr>
				<td class="align-right"><span class="mandatory">*&nbsp;</span></td>
				<td>
					<label class="label-bold"><spring:message code="bulk.upload.select.file" /></label>
				</td>
				<td>
					<input name="customTemplateFile" id="customTemplateFile" type="file" size="50" />&nbsp;&nbsp;&nbsp;
					<img src='../images/info-icon.png' class='tip-tool' title="<spring:message code="message.tiptext.template.file.supported.document" />"/>
				</td>
			</tr>
			<tr>
				<td colspan="3" align='center'>
					<input type="button" name="templateUpload" id="templateUpload" value="upload" onclick="validateCustomTemplate()" class="save-btn"/>
				 	<input type="button" name="templateCancel" id="templateCancel" value="cancel" onclick="cancelCustomTemplate()" 	class="save-btn"/>
				</td>
			</tr>
		</table>
	</div>
	<div id="templateTableDiv" class="template-table-div float-none">
		<div id="messageHeaderDiv" style="width: 95%;"></div>
	</div>
</div>

