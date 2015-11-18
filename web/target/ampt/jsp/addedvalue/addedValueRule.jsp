<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript" src="../js/addedvalue/addedValueRule.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
<div id="addedValueRuleContainer">
	<table id="planBudgetTable" class="master"></table>
 	<div id="planBudgetPager"></div>
	<div class="spacer7"></div>
	<div id="planBudgetDetailForm">
		<ul>
			<li id="PlanBudgetDetailFormTab-1"><a href="#planBudget-tabs-1"><spring:message code="tab.generic.tierDetails"/></a></li>
		</ul>
		<div id="planBudget-tabs-1">
			<form:form id="planBudgetDetail" name="planBudgetDetail" action="../manageAddedValue/saveAddedValuePlanBudget.action" method="post" modelAttribute="planBudgetForm">
				<div id="messageHeaderDiv" class="error"></div>
				<table style="width:90%;margin:0 auto">
					<tr>
						<td><span class="mandatory">*&nbsp;</span></td>
						<td><label class="label-bold">Total Investment:</label></td>
						<td>
							<form:hidden path="planBudgetId"  id="planBudgetId" />
							<form:input path="totalInvestment" id="totalInvestment" cssClass="numericdecimal"  maxlength="25" size="40"/>
							<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
						</td>
					</tr>
					<tr>
						<td><span class="mandatory">*&nbsp;</span></td>
						<td><label class="label-bold">Added Value Percentage:</label></td>
						<td>
							<form:input path="avPercentage" id="avPercentage" cssClass="numericdecimal" maxlength="6" size="40"/>
							<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
						</td>
					</tr>
					<tr>
						<td></td>
						<td style="vertical-align:middle;">
							<label class="label-bold">Notes:</label>
							<div id="charsRemaining" class="chars-remaining"></div>
						</td>
						<td>
							<form:textarea path="avNotes"  id="avNotes" rows="5" cols="100"/>
							<img src='<%= request.getContextPath() %>/images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.rateProfile.notes'/>" />
						</td>
					</tr>
				</table>
				<div align="center">
					<table width="15%">
						<tr>
							<td><input type="button" value="SAVE" id="planBudgetSaveData" class="save-btn"/></td>					
							<td><input type="button" value="RESET" id="planBudgetResetData" class="reset-btn"/></td>
						</tr>
					</table>
				</div>
			</form:form>
		</div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="addedValueSearchPanel">
		<select id="addedValueSearchOption" style="margin:0">
			<option value="totalInvestment"><spring:message code="label.generic.totalInvestment"/></option>
			<option value="avPercentage"><spring:message code="label.generic.percentage"/></option>
		</select>
		<input id="addedValueSearchValue" type="text" size="20" class="search-icon search-input" style="margin:0"/>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>