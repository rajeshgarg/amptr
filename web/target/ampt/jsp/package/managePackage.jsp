<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="static com.nyt.mpt.util.enums.DocumentForEnum.PACKAGE" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript">
	var DOCUMENT_TYPE = '<%= PACKAGE.name() %>';
</script>
<script type="text/javascript" src="../js/package/managePackage.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/proposal/lineItem.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="managePackageContainer">
	<table id="packageMasterGrid" class="master"></table>
	<div id="packageMasterPager"></div>
	<div class="spacer7"></div>
	<div id="packageTab">
		<ul>
			<li id="package-tabs-1-lbl">
				<a href="#package-tabs-1"><spring:message code="tab.generic.packageDetails"/></a></li>
			<li id="package-tabs-2-lbl">
				<a href="#package-tabs-2"><spring:message code="tab.generic.manageLineItem"/></a></li>
			<li id="package-tabs-3-lbl">
				<a href="#package-tabs-3"><spring:message code="tab.generic.manageDocument"/></a></li>
		</ul>
		<div id="package-tabs-1">
			<jsp:include page="packageDetail.jsp"></jsp:include>
		</div>
		<div id="package-tabs-2">
			<div id="packageAvailStatuslink" style="display:none;"> 
				<a class="avail-status-helplink" id="packageLineItemsAvailStatus" style="vertical-align: middle;" href="#" title='<spring:message code="label.lineitem.avails.status"/>' >
					<spring:message code="label.lineitem.avails.status"/>
           		 </a>
           		  <div class="spacer7"></div>
            </div>
           
            <div class="spacer7"></div>
			<div id="packageLineItemsAvailStatusSummary" title='<spring:message code="label.lineitem.avails.status"/>' style="display:none;overflow:hidden;">
			</div>
			<div id="packageLineItemShowHide">
				<div>
					<table id="packageLineItemsTable" class="childGrid"></table>
				</div>
				<div id="packageLineItemsPager"></div>	
				<div id="packageLineItemsForm" title="Line Items">
					<form:form action="../packagelineitems/saveLineItems.action" id="packageLineItems" name="packageLineItems" method="post" modelAttribute="lineItemForm">
						<jsp:include page="packageLineItems.jsp"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
		<div id="package-tabs-3">
			<div id="packageAttachement"></div>
		</div>
	</div>
	<div id="PackagelineItemTarget" title="Line Item target" style="display:none;">
		<div id="availsLineItems" style="display: none" title="Avails Detail"></div>   
	</div>
	<div class="ui-jqgrid-titlebar-close" style="right: 0px; display: none" id="managePackageSearchPanel">
		<select id="packageSearchOption">
			<option value="packageSalescategoryName"><spring:message code="label.generic.packageSalesCategory"/></option>
			<option value="packageName"><spring:message code="label.generic.packageName"/></option>
			<option value="packageOwner"><spring:message code="label.generic.OwnerFirstName"/></option>
			<option value="validFrom"><spring:message code="label.generic.startDate"/></option>
			<option value="validTo"><spring:message code="label.generic.endDate"/></option>
			<option value="breakableStr"><spring:message code="label.generic.breakable"/></option>
			<option value="budget"><spring:message code="label.generic.budget"/></option>
			<option value="expired">Expired</option>
		</select>
		<input id="packageSearchValue" type="text" size="20" class="search-icon search-input"/>
	</div>
	<div id="multiPkgLineItemDeleteContainer" title="Delete Line Items">
		<div id="multiPkgLineItemDeleteConfirmation">
			<table cellpadding="5" cellspacing="5">
				<tr>
					<td><img src='../images/warning.png'></td>
					<td style='line-height:2; vertical-align: top;'>
						<spring:message code="label.multi.line.item.delete.confirmation"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div id="multiPkgLineItemPriceUpdateContainer" title="<spring:message code="label.confirma.title.update.rate.card.price"/>">
		<div id="multiPkgLineItemUpdateConfirmation"> 
			<table cellpadding="5" cellspacing="5">
				<tr>
					<td><img src='../images/warning.png'></td>
					<td style='line-height: 2; vertical-align: top;'>
						<spring:message code="label.package.multi.line.item.update.confirmation" />
					</td>
				</tr>
			</table>
		</div>
	</div>
	<sec:authorize access="hasAnyRole('PLR','POW')">
		<input type="hidden" id="hasReadOnlyRights" />
	</sec:authorize>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>