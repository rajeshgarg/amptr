<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="./js/proposal/proposalReservation.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>
<div class="editProposalContainer edit-prop-cont">
	<%@ include file="proposalHeader.inc"%>
	<div class="proposal-inner-container">
		<div id="reservationParentDiv">
		
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}"/> 
			<c:set var="currentView" value="proposalReservation" scope="request"></c:set>
			<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}"/>
			<input type="hidden" value="${readOnlyView}" id="isreadOnly" />
			<div class="marg-top-basic" style="margin-left:5px;">
				<table id="reservedLineItemTable" class="master"></table>
				<div id="reservedLineItemPager"></div>
			</div>
			<input type="hidden" id="proposalID" value="${proposalForm.id}" />
			<input type="hidden" id="proposalOwnerRole" value="${proposalOwnerRole}"/>
			<input type="hidden" id="readOnlyView" value="${readOnlyView}"/>
			<input type="hidden" id="proposalStatus" value="${proposalForm.proposalStatus}"/>
			<input type="hidden" id="defaultOption" value="${proposalDefaultOption}"/>
			<input type="hidden" id="currentDate" value="${currentDate}"/>
			<input type="hidden" id="proposalAssignedToCurrentUsr" value="${assignedToCurrentUser}"/>
			<div id="moveReservedDataDialogue" style="display: none"></div>
			
			<div id="renewExpirationDateContainer"  title="<spring:message code="label.title.renew.expiration.date"/>" style="display: none">
				<div id="renewLineItemsExpirationDateConfirmation">
				<table cellpadding="5" cellspacing="5">
					<tr>
						<td style="vertical-align:middle;">&nbsp;&nbsp;<label class="label-bold">Expiration Date:</label></td>
						<td>
							<input id="reservationExpiryDate" onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" style="width: 60%; margin-left: 1px; vertical-align: middle;" />
							&nbsp;&nbsp;
							<input type="hidden" id="lineItemID" />
						</td>
					</tr>
					<tr>
						<td  style="vertical-align: middle;" >&nbsp;&nbsp;<label class="label-bold">Renew Date:</label></td>
						<td>
							<input id="renewDate" onfocus="this.blur()" tabindex="-1" class="input-textbox-readonly" style="width: 60%; margin-left: 1px; vertical-align: middle;" readonly="readonly"/>
							&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				</div>
			</div>
			
		</div>
	</div>
</div>
