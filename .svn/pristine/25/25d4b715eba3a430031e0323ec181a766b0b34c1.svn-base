<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="buildProposalContainer">
	<div id="messageHeaderDiv"></div>
	<table id="packageContainer" width="100%" cellpadding="2" cellspacing="2" class="search-proposal-input basic-content-align">
		<tr>
			<td width="5%">&nbsp;</td>
			<td width="16%">
				<label class="label-bold"><spring:message code="label.generic.packageName" />:</label>
			</td>
			<td width="25%">
				<form:input path="packageName" id="packageName"	cssStyle="width: 220px;" />
			</td>
			<td width="11%">
				<label class="label-bold"><spring:message code="label.generic.proposalBudget" /> ($)</label>
			</td>
			<td width="1%">&nbsp;</td>
			<td width="4%" align="right">
				<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
			</td>
			<td width="1%">&nbsp;</td>
			<td width="14%" align="left">
				<input type="text" id="packageBudgetFrom" size="10" class="numeric" maxlength="10" />
			</td>
			<td width="1%">&nbsp;</td>
			<td width="6%" align="left">
				<label class="label-bold"><spring:message code="label.generic.rangeto" />:</label>
			</td>
			<td width="1%">&nbsp;</td>
			<td>
				<input type="text" id="packageBudgetTo" size="10" class="numeric" maxlength="10" />
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<label class="label-bold"><spring:message code="label.generic.startDate" />:</label>
			</td>
			<td>
				<form:input maxlength="13" path="validFrom" id="validFrom" readonly="true" size="12" />
				<a href="javascript:void(0)" id="startFromSearchReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></a>
			</td>
			<td>
				<label class="label-bold"><spring:message code="label.generic.endDate" />:</label>
			</td>
			<td width="1%">&nbsp;</td>
			<td width="1%">&nbsp;</td>
			<td width="1%">&nbsp;</td>
			<td colspan="1">
				<form:input maxlength="13" path="validTo" id="validTo" readonly="true" size="12" />
				<a href="javascript:void(0)" id="startToSearchReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></a>
			</td>
			<td width="1%">&nbsp;</td>
			<td width="5%">
				<label class="label-bold"><spring:message code="label.generic.ExpiredPackage" />:</label>
			</td>
			<td width="1%">&nbsp;</td>
			<td>
				<form:checkbox path="expiredPackages" id="expiredPackages" value="Expired" />
			</td>
		</tr>
		<tr>
			<td align="center" width="100%" colspan="12">
				<input type="button" id="searchproposal" onclick="searchProposal()" class="save-btn" value="Search" />
				<input type="button" id="resetProposal" value="Reset" onclick="resetSearchFields();" class="reset-btn" />
				<form:hidden id="optionId" path="optionId" value="${proposalForm.optionId}" />
				<form:hidden id="proposalversion" path="proposalversion" value="${proposalForm.proposalVersion}" /> 
				<form:hidden id="proposalID" path="proposalID" value="${proposalForm.id}" />
			</td>
		</tr>
	</table>
	<div id="searchPackageGridContainer" style="display: none; padding-top: 7px;">
		<table id="searchPackageTable" class="secondChildGrid"></table>
		<div id="searchPackagePager"></div>
	</div>
	<div id="addToProposalContainer" style="display: none;">
		<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
			<tr>
				<td align="center">
					<input type="button" id="addproposalLi"	value="Add to proposal" onclick="addToproposal()" />
				</td>
			</tr>
		</table>
	</div>
</div>