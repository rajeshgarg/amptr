<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
	.disable-column a.anchor-color{color:#D9DEE4 !important}
	.disable-column a.anchor-color:hover{color:#D9DEE4 !important}
</style>

<c:if test="${readOnlyView}">
	<div class="warning-info">
    	<h3>Proposal is open in read only mode.</h3>
	</div>
</c:if>

<div id="statusContainer" class="status-container">
	<table cellpadding="2" cellspacing="2" style="width:100%">
		<tr id="proposalNavigatorRow">
			<c:choose>
				<c:when test="${currentView == 'basicInfo'}">
					<td class="selected-current-status">
						<p><a class="nav-anchor">Basic Setup</a></p>
						<span class="active-menu"></span>
					</td>
				</c:when>
				<c:otherwise>
					<td class="selected-status">
						<p><a class="Fleft nav-anchor" href="#" onClick="stateChangeRequest('${flowExecutionUrl}', 'eventViewEditBasicInfo')">
							Basic Setup</a></p>
							<span></span>
					</td>
				</c:otherwise>
			</c:choose>
			<c:if test="${proposalForm.id > 0}">
				<c:choose>
					<c:when test="${currentView == 'proposalReservation'}">
						<td class="selected-current-status">
							<p><a class="nav-anchor">Reservation</a></p>
							<span class="active-menu"></span>
						</td>
					</c:when>
					<c:otherwise>
						<td class="selected-status">
							<p>
								<a class="Fleft nav-anchor" href="#" onClick="stateChangeRequest('${flowExecutionUrl}', 'eventProposalReservation')">Reservation</a>
							</p>
							<span></span>
						</td>						
					</c:otherwise>
				</c:choose>	
				
				<td class="bod-sep"> | </td>		
			</c:if>
 			<c:forEach var="options" items="${proposalOptions}">
				<c:choose>
					<c:when test="${options.value == proposalForm.optionName}">
						<td id="${options.key}" class="main-prop-buttons selected-prop-but">
							<p><a class="nav-anchor" title="${options.value}" >${options.value}</a></p>
							<span class="active-menu"></span>
							<c:if test="${options.key == proposalDefaultOption}">
								<span class="option-default-icon"></span>
							</c:if>
						</td>
						
					</c:when>
					<c:otherwise>
						<td id="${options.key}" class="main-prop-buttons">
							<p><a class="nav-anchor" title="${options.value}" onClick="loadOption('${flowExecutionUrl}', 'eventShowProposalOptionData', ${proposalForm.id}, ${options.key})">${options.value}</a></p>							
							<span></span>
							<c:if test="${options.key == proposalDefaultOption}">
								<span class="option-default-icon"></span>
							</c:if>
						</td>
					</c:otherwise>
				</c:choose>								   
			</c:forEach>
			<td></td>			
		</tr>
	</table>
	<div class="proposal-id-nav">
		<c:if test="${proposalForm.sosOrderId != null}">
			<span>SOS Order ID<br /><strong>${proposalForm.sosOrderId}</strong></span>
		</c:if>
		<c:if test="${proposalForm.salesForceSearchKey != null}">
			<span>SalesForce Search Key<br /><strong>${proposalForm.salesForceSearchKey}</strong></span>
		</c:if>
		<c:if test="${proposalForm.id > 0}">
			<span>Proposal Id<br /><strong>${proposalForm.id}</strong></span>
		</c:if>
	</div>
</div>
