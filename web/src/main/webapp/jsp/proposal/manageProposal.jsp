<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript" src="../js/proposal/manageProposal.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageProposalContainer">
	<div class="searchProposalContainer">
		<div class="search-params float-left">
			<select id="proposalSearchOptions">
				<option value="proposalname"><spring:message code="label.basicinfo.proposalName"/></option>
				<option value="proposalID"><spring:message code="label.basicinfo.proposalID"/></option>
				<option value="sosOrderId"><spring:message code="label.basicinfo.orderid"/></option>
				<option value="sosLineItemId"><spring:message code="label.basicinfo.sosLineItemId"/></option>
			</select>
			<input type="text" class="pad-rt-25 search-icon search-input" size="30" name="proposalSearchOptionValue" id="proposalSearchOptionValue"/>
			<input type="checkbox" checked="checked" id="myProposal" name="myProposal" value="true" style="vertical-align:-2px;">
			<label class="label-bold" for="myProposal"><spring:message code="label.generic.myProposal" /></label>
			<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.myProposal'/>" />
		</div>
		<sec:authorize access="hasAuthority('CNP')">
			<div class="label-bold adv-search" style="width:140px;padding:3px 20px 0 10px;">
				<input class="red-btn" value='New Proposal' type="button" id="createproposal" onclick="openProposal('0')" />
			</div>
		</sec:authorize>
		<div class="label-bold adv-search" id="searchPropCont" onclick="showProposalSearch();" style="margin-left:10px;">
			<span>Advanced Search</span>
			<span class="adv-search-ico"></span>
		</div>
	</div>
	<div class="search-prop-cont" style="display:none; height:270px; ">
		<div class="spacer7"></div>
		<form:form modelAttribute="proposalForm" id="manageProposalForm" name="manageProposalForm">
			<table width="100%" cellpadding="2" cellspacing="2" class="" id="proposalSearchCriteria">
				<tr>
					<td width="1%">&nbsp;</td>
					<td width="15%">
						<div id="proposalST">
							<label class="label-bold" >
								<spring:message code="label.generic.proposalStatus"/>:
							</label>
						</div>
					</td>
					<td width="26%">
						<form:select path="proposalStatus" id="proposalStatus" cssStyle="width: 250px;"
							onclick="loadData(this, '../manageProposal/getProposalStatus.action')">
							<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
						</form:select>
					</td>	
					<td width="15%">
						<label class="label-bold"><spring:message code="label.generic.proposalCPM" /> </label>
					</td>
					<td align="right" width="4%">
						<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
					</td>
					<td width="1%">&nbsp;</td>
					<td width="17%"> 
						<input type="text" id="cpmFrom" size="12" class="numeric" maxlength="10"/>
					</td>
					<td align="right" width="4%"> 
						<label class="label-bold"><spring:message code="label.generic.rangeto" />:</label>
					</td>
					<td width="1%">&nbsp;</td>
					<td width="17%">
						<input type="text" id="cpmTo" size="12" class="numeric" maxlength="10"/>
					</td>			
				</tr>		
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold" id="userIdLevel">
							<spring:message code="label.basicinfo.assignto"/>:
						</label>
					</td>
					<td>
						<form:select path="userId" id="userId" cssStyle="width: 250px;" onclick="loadData(this, '../manageProposal/getUsersBasedOnRole.action')">
							<option value=""><spring:message code="label.generic.blankSelectOption"/></option>	
						</form:select>
					</td>	
					<td>
						<label class="label-bold"><spring:message code="label.generic.proposalBudget" /> ($)</label>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="budgetFrom" size="12" class="numeric" maxlength="10"/>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangeto" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="budgetTo" size="12" class="numeric" maxlength="10"/>
					</td>			
				</tr>		
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.proposalAgency" />:</label>
					</td>
					<td>
						<form:hidden path="agencyName" id="agencyName" cssStyle="width: 250px;"/>
					</td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.proposalImpressions" /></label>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="impressionFrom" size="12" class="numeric" maxlength="10"/>
					</td>
					<td align="right"> 
						<label class="label-bold"><spring:message	code="label.generic.rangeto" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="impressionTo" size="12" class="numeric" maxlength="10"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.salesCategory" />:</label></td>
					<td>
						<form:select path="salescategory" id="salescategory" cssStyle="width: 250px;" onclick="loadData(this, '../manageProposal/getSalesCategoriesList.action')">
							<option value=""><spring:message code="label.generic.blankSelectOption" /></option>
						</form:select>
					</td>
					<td>
						<label class="label-bold"><spring:message code="label.basicinfo.dueon" /> </label>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="_dueOnFrom"  size="12" readonly="readonly"/>
						<span  id="dueOnFrom" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangeto" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="_dueOnTo" size="12" readonly="readonly"/>
						<span id="dueOnTo" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>			
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.proposalAdvertiser"/>:</label>
					</td>
					<td>
						<form:hidden path="advertiserName" id="advertiserName" cssStyle="width: 250px;"/>
					</td>			
					<td>
						<label class="label-bold"><spring:message code="label.basicinfo.requestedon" /> </label>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangefrom" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="_requestedOnFrom" size="12" readonly="readonly"/>
						<span id="requestedOnFrom" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>
					<td align="right">
						<label class="label-bold"><spring:message code="label.generic.rangeto" />:</label>
					</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" id="_requestedOnTo"  size="12" readonly="readonly"/>
						<span id="requestedOnTo" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>													
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>
						<label class="label-bold" id="campaignObjectiveObjLevel">
							<spring:message code="label.basicinfo.campaignObjective"/>:
						</label>
					</td>
					<td colspan="8">
						<select name="campaignObjectiveObj" id="campaignObjectiveObj" style="width: 250px;"
							onclick="loadData(this, '../manageProposal/getCampaignObjectives.action')">
							<option value=""><spring:message code="label.generic.blankSelectOption"/></option>	
						</select>
					</td>	
				</tr>
				<tr>
					<td colspan="10" align="center">
						<input class="reset-btn" value="Search" type="button" id="searchproposal" onclick="gridReload()" style="margin-top:18px;" />
						<input class="reset-btn" value="Reset" type="button" id="resetProposal" onclick="resetProposalsearchFields();gridReload();" style="margin-top:18px;" />
					</td>
				</tr>
			</table>
		</form:form>
	</div>
	<div id="manageProposalGridContainer">
		<table id="manageProposalTable"></table>
		<div id="manageProposalPager"></div>	
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'bottom center',at: 'top center'}, style: {classes: 'ui-tooltip-blue'} });
</script>