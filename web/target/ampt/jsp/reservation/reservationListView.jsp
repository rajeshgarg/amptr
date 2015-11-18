<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a id="closeReservationDetail" class="close-detail" title="Close" style="display:none"></a>
<div id="reservationListViewContainer">
	<c:forEach var="reservation" items="${reservationList}">
		<table id="reservationListView">
		<tbody>
			<tr class="date-header">
				<td colspan="5" class="label-bold">${reservation.viewDate}</td>
				<td colspan="2" class="label-bold" align="right">${reservation.totalSOR}% Booked</td>
			</tr>
			
			<c:choose>
				<c:when test="${reservation.totalSOR != 0.0}">
					<tr class="table-name-header">
						<td colspan="7" class="label-bold">ORDERS</td>
					</tr>
					<tr class="table-col-header">
						<td>ORDER ID</td>
						<td colspan="2">CAMPAIGN NAME</td>
						<td align="center">SOR</td>
						<td>ADVERTISER NAME</td>
						<td colspan="2">LINE ITEM ID</td>
					</tr>
					<c:choose>
						<c:when test="${not empty reservation.salesOrderList}">
							<c:forEach  var="salesOrder" items="${reservation.salesOrderList}">
								<tr>
									<td>${salesOrder.salesOrderId}</td>
									<td colspan="2">${salesOrder.campaignName}</td>
									<td align="center">${salesOrder.sor}</td>
									<td>${salesOrder.advertiserName}</td>
									<td colspan="2">${salesOrder.lineItemId}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7" style="text-align:center;font-size:11px;"><spring:message code="label.generic.emptymessagesearchgrid" /></td>
							</tr>
						</c:otherwise>
					</c:choose>
					<tr class="table-name-header">
						<td class="label-bold" colspan="7">RESERVATIONS</td>
					</tr>
					<tr class="table-col-header">
						<td>PROPOSAL ID</td>
						<td style="width:250px">PROPOSAL NAME</td>
						<td align="center">SOR</td>
						<td style="width:90px">ADVERTISER NAME</td>
						<td>LINE ITEM ID</td>
						<td style="width:140px">ACCOUNT MANAGER</td>
						<td>Expiry Date</td>
					</tr>
					<c:choose>
						<c:when test="${not empty reservation.proposalList}">
							<c:forEach var="proposal" items="${reservation.proposalList}">
								<tr>
									<td>${proposal.proposalId}</td>
									<td>${proposal.proposalName}</td>
									<td align="center">${proposal.sor}</td>
									<td>${proposal.advertiserName}</td>
									<td>${proposal.lineItemId}</td>
									<td>${proposal.accountManager}</td>
									<td>${proposal.expirationDate} <span style ="color:red;">${proposal.vulnerable}</span></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7" style="text-align:center;font-size:11px;"><spring:message code="label.generic.emptymessagesearchgrid" /></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="7">&nbsp;</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
		</table>
	</c:forEach>
</div>