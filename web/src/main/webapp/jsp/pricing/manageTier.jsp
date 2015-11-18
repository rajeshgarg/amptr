<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/pricing/manageTier.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageTierContainer">
	<table id="manageTierTable" class="master"></table>
 	<div id="manageTierPager"></div>
	<div class="spacer7"></div>
	<div id="manageTierDetailForm">
		<ul>
			<li id="ManageTierDetailFormTab-1"><a href="#manageTier-tabs-1"><spring:message code="tab.generic.tierDetails"/></a></li>
			<li id="ManageTierDetailFormTab-2"><a href="#manageTier-tabs-2"><spring:message code="tab.generic.tierPremium"/></a></li>
		</ul>
		<div id="manageTier-tabs-1">
			<jsp:include page="tierDetails.jsp"></jsp:include>
		</div>
		<div id="manageTier-tabs-2">
			<jsp:include page="premiumDetails.jsp"></jsp:include>
		</div>
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="tierSearchPanel">
		<select id="tierSearchOption">
			<option value="tierName"><spring:message code="label.manageTier.name"/></option>
			<option value="sectionNames"><spring:message code="label.manageTier.tierSection"/></option>
			<option value="level"><spring:message code="label.manageTier.level"/></option>
		</select>
		<input id="tierSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
	<div style="display: none;" id="tierPremiumCloneForm" title="Clone Premium">
		<div class="spacer7"></div>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td width="30%"><label class="label-bold">From:</label></td>
				<td>						
					<label id="cloneTierFrom"></label>
					<input type="hidden" id="sourceTierId">
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td style="vertical-align: top"><label class="label-bold">To:</label></td>
				<td>						
					 <select name="targetTier" id="targetTier" style="width: 250px;"></select>
				</td>
			</tr>
		</table>
		<div style="display: none" id="targetTierPremiumExist">
			<table cellpadding="5" cellspacing="5">
				<tr>
					<td><img src='../images/warning.png'></td>
					<td style='line-height:2; vertical-align: top;'>
						Premiums Associated with <label id="selectedTier"></label> Tier will be overwritten with the premiums of the selected tier.<Br/></Br>Do you want to continue?
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>
