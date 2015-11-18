<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form id="tierDetail" name="tierDetail" action="../manageTier/saveTier.action" method="post" modelAttribute="tierForm">
	<div>
		<div id="messageHeaderDiv" class="error"></div>
		<div>
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td width="5%" align="right"><span class="mandatory">*&nbsp;</span></td>
					<td width="5%">
						<label class="label-bold" style="width:10px;">
							<spring:message code="label.manageTier.name"/>:
						</label>
					</td>
					<td width="35%">
						<form:hidden path="tierId"  id="tierId" />
						<form:input path="tierName" id="tierName"  maxlength="60" size="40" />
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.tier.name'/>" />&nbsp; &nbsp;&nbsp; &nbsp;
					</td>
				</tr>
				<tr>
					<td width="5%" align="right"><span class="mandatory">*&nbsp;</span></td>
					<td width="5%">
						<label class="label-bold">
							<spring:message code="label.manageTier.level"/>:
						</label>
					</td>
					<td width="35%">
						<form:input path="level" id="level" cssClass="numeric" size="40" align="right"/>
						<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.tier.level'/>" />
					</td>
				</tr>
			</table>
		</div>
		<div>
			<table width="100%"  cellpadding="2" cellspacing="2" class="basic-content-align">
				<tbody>
					<tr>
						<td style="vertical-align: middle;" align="right" width="11%"><span class="mandatory">*&nbsp;</span></td>
						<td width="11%" style="vertical-align: middle;" >
							<label class="label-bold"> 
								<spring:message code="label.manageTier.tierSection" />:
							</label>
						</td>
						<td width="24%">
							<form:select path="unselectedSectionIds" id="availTierSections" multiple="multiple" cssStyle="width: 90%;" size="5"
							ondblclick="$('#tierSections', '#tierDetail').prepend($('#availTierSections option:selected', '#tierDetail'));">
							</form:select>
						</td>
						<td  width="10%" style="vertical-align: middle;">
							<div class="forward">
								<a id="addSecIt" class="fwd-btn-users" title="Select Sections" alt="Right" href="javascript:void('0');"
								onClick="$('#tierSections', '#tierDetail').prepend($('#availTierSections option:selected', '#tierDetail'));"></a>
								<a id="addAllSecIt" class="fwd-all-btn-users" title="Select All Sections" alt="Right" href="javascript:void('0');" 
								onClick="moveAllSelectBoxOptions('tierSections', 'availTierSections');" ></a>
							</div>
							<div class="forward"> 
								<a id="delSecIt" class="bckwrd-btn-users" title="Unselect Sections" alt="Left" href="javascript:void('0')"
								onClick="$('#availTierSections', '#tierDetail').prepend($('#tierSections option:selected', '#tierDetail'));"></a>
								<a id="delAllSecIt" class="bckwrd-all-btn-users" title="Unselect All Sections" alt="Left" href="javascript:void('0')" 
								onClick="moveAllSelectBoxOptions('availTierSections', 'tierSections');" ></a>
							</div>
						</td>
						<td style="vertical-align: middle;" width="42%">
							<form:select path="selectedSectionIds" id="tierSections" multiple="multiple" cssStyle="width: 52%;" size="5"
							ondblclick="$('#availTierSections', '#tierDetail').prepend($('#tierSections option:selected', '#tierDetail'));">
							</form:select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div align="center">
			<table align="center" width="15%">
				<tr>
					<td><input type="button" value="SAVE" id="listTierSaveData" class="save-btn"/></td>					
					<td><input type="button" value="RESET" id="listTierResetData" class="reset-btn"/></td>
				</tr>
			</table>
		</div>
	</div>
</form:form>
