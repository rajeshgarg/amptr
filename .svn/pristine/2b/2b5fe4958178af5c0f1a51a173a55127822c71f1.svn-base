<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript" src="../js/reservation/manageReservation.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div id="manageReservationsContainer">
	<div class="searchProposalContainer">
		<div class="search-params reserv float-left">
			<select id="reservationSearchOptions">
				<option value="proposalName"><spring:message code="label.basicinfo.proposalName"/></option>
				<option value="proposalId"><spring:message code="label.basicinfo.proposalID"/></option>
				<option value="sosLineItemId"><spring:message code="label.basicinfo.sosLineItemId"/></option>
			</select>
			<input type="text" class="pad-rt-25 search-icon search-input" size="30" name="reservationSearchOptionValue" id="reservationSearchOptionValue"/>
			<input type="checkbox" checked="checked" id="myReservation" name="myReservation" value="true" style="vertical-align:-2px;">
			<label class="label-bold" for="myReservation"><spring:message code="label.generic.myReservation" /></label>
			<img src='../images/info-icon.png' class="tip-tool" title="<spring:message code='help.myReservation'/>" />
			<label class="label-bold" style="margin-left:20px"><spring:message code="label.reservation.daysToExpire" /></label>
			<select id="noOfExpiryDays" style="margin:0">
				<option value=""></option>
				<c:forEach var="i" begin="1" end="7">
   					<option value="${i}">${i}</option>
				</c:forEach>
			</select>
		</div>
		<div class="label-bold adv-search viewCalLink" id="viewMultipleCalendar" style="padding-bottom:5px;width:100px;margin-left:0">
			<span><a href="javascript:void(0)" class="anchor-link" target="multipleCalendarsLink" onclick="viewMultipleCalendarsWindow(); return false;">View Calendars</a></span>
		</div>
		<div class="label-bold adv-search" id="searchReservationCont" onclick="showReservationSearch();">
			<span><spring:message code="label.generic.advanced.search"/></span>
			<span class="adv-search-ico"></span>
		</div>
	</div>
	<div id="searchFieldsContainer" style="display:none;" class="search-prop-cont">
		<div class="spacer7"></div>
		<div id="messageHeaderDiv" class="error"></div>
		<div id="searchFieldErrorContainer">
			<img id="exptimgId" src="<%= request.getContextPath() %>/css/images/plus.png" /><strong><spring:message code="label.generic.notes"/>:</strong>
			<div id="searchFieldErrorShowhide" style="display:none;">
				<ol id="reservationSearchErrors" type="disc">
					<li><spring:message code="error.reservation.search.mandatory"/></li>
				</ol>
			</div>
		</div>
		<div class="spacer7"></div>
		<form:form id="manageReservationForm" name="manageReservationForm" action="../manageReservation/searchReservation.action" modelAttribute="reservationForm">
			<table width="100%" cellpadding="2" cellspacing="2" class="" id="reservationSearchCriteria">
				<tr>
					<td width="5%"></td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.startDate"/>:</label>
					</td>
					<td>
						<form:input path="startDate" id="reservationStartDate" size="12" onfocus="this.blur()" readonly="true" class="input-textbox-readonly"/>
						<span id="reservationStartDateReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>
					<td width="5%"></td>
					<td>
						<label class="label-bold"><spring:message code="label.generic.endDate"/>:</label>
					</td>
					<td>
						<form:input path="endDate" id="reservationEndDate" size="12" onfocus="this.blur()" readonly="true" class="input-textbox-readonly"/>
						<span id="reservationEndDateReset" class="reset-decoration" title='<spring:message code="label.generic.clear"/>'></span>
					</td>
				</tr>
				<tr>
					<td width="5%"></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.product"/>:
						</label>
					</td>
					<td>				
						<div id="sosProductIdDiv">
							<div id="reservationProductId_custom" class="multi-select-box" style="width:330px">
							  	<form:select multiple="true" path="productIdLst" id="reservationProductId" cssStyle="width: 90%;">
									<%-- <form:option value=""></form:option> --%>	
									<c:forEach  items="${allProducts}"  var="entry" >
										<optgroup  label="${entry.key}"> 	
										<c:forEach  var="product" items="${entry.value}">
											<form:option value="${product.productId}">${product.displayName}</form:option>
										</c:forEach>	
										</optgroup>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</td>
					<td width="5%"></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.salesTarget"/>:
						</label>
					</td>
					<td>
						<div id="reservationSalesTarget_custom" class="multi-select-box" style="width:330px">				
							<form:select multiple="true" path="salesTargetIdLst" id="reservationSalesTarget" cssStyle="width: 90%;">
								<%-- <form:option value=""></form:option> --%>
								<c:forEach  var="salesTarget" items="${allSalesTarget}">
									<form:option value="${salesTarget.key}">${salesTarget.value}</form:option>
								</c:forEach>
								<%-- <form:options items="${allSalesTarget}" /> --%>	
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td width="5%"></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.manageReservation.advertiser"/>:
						</label>
					</td>
					<td>				
						<form:hidden path="advertiserId" id="advId" cssStyle="width: 330px;"></form:hidden>
					</td>
					<td width="5%"></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.generic.salesCategory"/>:
						</label>
					</td>
					<td>				
						<form:select path="salesCategoryId" id="reservationSalesCategoryId" cssStyle="width: 330px;" >
							<form:option value=""></form:option>
							<form:options items="${allSalesCategories}" />								
						</form:select>
					</td>		
				</tr>
				<tr>
					<td width="5%"></td>
					<td>
						<label class="label-bold">
							<spring:message code="label.manageReservation.status"/>:
						</label>
					</td>
					<td>
						<div id="reservationStatus_custom" class="multi-select-box" style="width:330px">			
							<form:select  path="reservationStatus" id="reservationStatus" cssStyle="width: 90%;" multiple="multiple">
								<form:options items="${allReservationStatus}" />
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="10" align="center">
						<input class="reset-btn" value="Search" type="button" id="searchReservation" onclick="searchReservations();" />
						<input class="reset-btn" value="Reset" type="button" id="resetReservation" onclick="resetSearchFields();" />
					</td>
				</tr>
			</table>
		</form:form>
	</div>
	<div id="amptResrvDataGridContainer" class="search-reserv-cont">
		<table id="amptResrvtnDataTable"></table>
		<div id="amptResrvtnDataPager"></div>	
	</div>
	<div id="sosResrvDataGridContainer" class="search-reserv-cont">
		<table id="sosResrvtnDataTable"></table>
		<div id="sosResrvtnDataPager"></div>	
	</div>
	
	<div style="display: none;" id="moveReservationsDialogue" title="<spring:message code='title.move.reservation.data'/>">
		<div class="spacer7"></div>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>	
				<td colspan="2">
					<label class="label-bold" style="padding-right:3px;"><spring:message code="label.reservation.source.expiryDate"/>:</label>
					<input id="moveReservationSrcExpiryDate" onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" readonly="readonly" style="width:85px;height:18px;margin-right:10px"></input>
					<label class="label-bold" style="padding-right:3px;"><spring:message code="label.reservation.target.expiryDate"/>:</label>
					<input id="moveReservationExpiryDate" onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" readonly="readonly" style="width:85px;height:18px;"></input>
				</td>
			</tr>
			<tr>
				<td style="height:5px"></td>
			</tr>
			<tr>
				<td style="vertical-align: middle">
					<label class="label-bold"><spring:message code="label.generic.proposals"/>: </label>
				</td>
				<td>						
					 <select name="targetProposal" id="targetProposal" style="margin-left:4px;width:400px;"></select>
					 <%-- <img src='<%= request.getContextPath() %>/images/info-icon.png' class="top-169 tip-toolright" title="<spring:message code='help.attribute.placementname'/>" style="position:relative;top:4px;" /> --%>
				</td>
				<td>
					<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='message.move.reservation.data'/>" style="position:relative;top:4px;" />
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;" id="renewReservationsValidationDialogue" title="<spring:message code='label.title.renew.expiration.date'/>">
	<div class="spacer7"></div>
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>	
				<td colspan="2">
					<label class="label-bold" style="padding-right:3px;"><spring:message code="label.reservation.target.expiryDate"/>:</label>
					<input id="renewReservationExpiryDate" onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" readonly="readonly" style="width:85px;height:18px;"></input>
				</td>
			</tr>
		</table>
	</div>
	<input type="hidden" id="currentDate" value="${currentDate}"/>
	<sec:authorize access="hasAnyRole('ADM','POW')">
		<input type="hidden" id="hasAdminRole" />
	</sec:authorize>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>