<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div>
	<a id="closeReservationDetail" class="close-detail" title="Close"></a>
	<table width="100%" cellpadding="2" cellspacing="2">
		<tr>
			<td>
				<label class="label-bold">${currentDate}</label>
			</td>
			<td align="right">
				<label class="label-bold">${booked}% Booked</label>
			</td>
		</tr>
	</table>
	<div class="scroll-accord-content">
		<c:if test="${not empty salesOrderList}">
			<div class="ui-accordion">
				<h3 class="reservation-detail-headr ui-state-active">
					<span><label class="label-bold">SOS Order</label></span>								
				</h3>
				<div class="proposal-accordion-content ui-accordion-content ui-accordion-content-active">
					<c:forEach items="${salesOrderList}" var="form">
						<div style="margin: 5px;">
						<div class="proposal-primary-info">
							<p>
								<dfn style="width:15%"><label class="label-bold">Order Id:</label>
									<span>
										<a onclick="openOrderInWindow('${form.salesOrderId}','${form.sosURL}')" title="${form.salesOrderId}">${form.salesOrderId}</a>
									</span>
								</dfn>
								<dfn style="width:38%"><label class="label-bold">Campaign Name:</label><span title="${form.campaignName}">${form.campaignName}</span></dfn>
								<dfn style="width:30%;margin-left:20px;"><label class="label-bold">Advertiser Name:</label><span title="${form.advertiserName}">${form.advertiserName}</span></dfn>
							</p>
						</div>
						<table cellpadding="2" cellspacing="2" width="100%" class="reservation-table">
							<thead>
								<tr>
									<th width="10%">Line Item Id</th>
									<th width="22%">Product</th>
									<th width="22%">Sales Target</th>
									<th width="23%">Targeting String</th>
									<th width="15%">Flight</th>
									<th width="10%">Booking (%)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${form.lineItems}" var="lineItem">
									<tr>
										<td title="${lineItem.lineItemID}">${lineItem.lineItemID}</td>
										<td title="${lineItem.productName}">${lineItem.productName}</td>
										<td title="${lineItem.salesTargetName}">${lineItem.salesTargetName}</td>
										<td title="${lineItem.targetingString}">${lineItem.targetingString}</td>
										<td title="${lineItem.startDate} - ${lineItem.endDate}">${lineItem.startDate} - ${lineItem.endDate}</td>
										<td title="${lineItem.sor}">${lineItem.sor}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
		<c:if test="${not empty reservedProposalList}">
			<div class="ui-accordion">
				<h3 class="reservation-detail-headr ui-state-active">
					<span><label class="label-bold">AMPT Reservations</label></span>								
				</h3>
				<div class="proposal-accordion-content ui-accordion-content ui-accordion-content-active">
					<c:forEach items="${reservedProposalList}" var="form">
						<div style="margin: 5px;">
						<div class="proposal-primary-info">
							<p>
								<dfn style="width:15%">
									<c:if test="${form.proposalId == calendarForm.proposalId}">
										<img src="../images/success.png" width="16px" height="16px"></img>
									</c:if>
									<label class="label-bold">Proposal Id:</label><span title="${form.proposalId}">${form.proposalId}</span>
								</dfn>
								<dfn style="width:38%"><label class="label-bold">Proposal Name:</label><span><a onclick="openProposalInWindow('${form.proposalId}')" title="${form.campaignName}">${form.campaignName}</a></span></dfn>						
								<dfn style="width:20%;margin-left:20px;"><label class="label-bold">Advertiser Name:</label><span title="${form.advertiserName}">${form.advertiserName}</span></dfn>
								<dfn style="width:21%;margin-left:20px;"><label class="label-bold">Account Manager:</label><span title="${form.accountManager}">${form.accountManager}</span></dfn>
							</p>
						</div>
						<table cellpadding="2" cellspacing="2" width="100%" class="reservation-table">
							<thead>
								<tr>
									<th width="10%">Line Item Id</th>
									<th width="16%">Product</th>
									<th width="16%">Sales Target</th>
									<th width="16%">Targeting String</th>
									<th width="7%">Option</th>
									<th width="15%">Flight</th>
									<th width="8%">Booking (%)</th>
									<th width="12%">Expiry Date</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${form.lineItems}" var="lineItem">
									<tr>
										<td title="${lineItem.lineItemID}">${lineItem.lineItemID}</td>
										<td title="${lineItem.productName}">${lineItem.productName}</td>
										<td title="${lineItem.salesTargetName}">${lineItem.salesTargetName}</td>
										<td title="${lineItem.targetingString}">${lineItem.targetingString}</td>
										<td title="${lineItem.optionName}">${lineItem.optionName}</td>
										<td title="${lineItem.startDate} - ${lineItem.endDate}">${lineItem.startDate} - ${lineItem.endDate}</td>
										<td title="${lineItem.sor}">${lineItem.sor}</td>
										<td title="${lineItem.expirationDate}">${lineItem.expirationDate} <span style ="color:red;">${lineItem.vulnerable}</span></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
		<c:if test="${not empty proposedProposalList}">
			<div class="ui-accordion">
				<h3 class="reservation-detail-headr ui-state-active">
					<span><label class="label-bold">AMPT Reservable Line Items</label></span>
					<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" height="12px" width="12px" title="<spring:message code='help.ampt.reservable.lineItem'/>" style="margin-left:5px;" />								
				</h3>
				<div class="proposal-accordion-content ui-accordion-content ui-accordion-content-active">
					<c:forEach items="${proposedProposalList}" var="form">
						<div style="margin: 5px;">
						<div class="proposal-primary-info">
							<p>
								<dfn style="width:15%">
									<c:if test="${form.proposalId == calendarForm.proposalId}">
										<img src="../images/success.png" width="16px" height="16px"></img>
									</c:if>
									<label class="label-bold">Proposal Id:</label><span title="${form.proposalId}">${form.proposalId}</span>
								</dfn>
								<dfn style="width:38%"><label class="label-bold">Proposal Name:</label><span><a onclick="openProposalInWindow('${form.proposalId}')" title="${form.campaignName}">${form.campaignName}</a></span></dfn>						
								<dfn style="width:20%;margin-left:20px;"><label class="label-bold">Advertiser Name:</label><span title="${form.advertiserName}">${form.advertiserName}</span></dfn>
								<dfn style="width:21%;margin-left:20px;"><label class="label-bold">Account Manager:</label><span title="${form.accountManager}">${form.accountManager}</span></dfn>
							</p>
						</div>
						<table cellpadding="2" cellspacing="2" width="100%" class="reservation-table">
							<thead>
								<tr>
									<th width="8%">Line Item Id</th>
									<th width="22%">Product</th>
									<th width="22%">Sales Target</th>
									<th width="20%">Targeting String</th>
									<th width="10%">Option</th>
									<th width="20%">Flight</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${form.lineItems}" var="lineItem">
									<tr>
										<td title="${lineItem.lineItemID}">${lineItem.lineItemID}</td>
										<td title="${lineItem.productName}">${lineItem.productName}</td>
										<td title="${lineItem.salesTargetName}">${lineItem.salesTargetName}</td>
										<td title="${lineItem.targetingString}">${lineItem.targetingString}</td>
										<td title="${lineItem.optionName}">${lineItem.optionName}</td>
										<td title="${lineItem.startDate} - ${lineItem.endDate}">${lineItem.startDate} - ${lineItem.endDate}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
	</div>
</div>

<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });	
</script>