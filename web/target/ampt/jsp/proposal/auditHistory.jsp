<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<div id="auditHistoryAccordian" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
		<h3	class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
			<span title="Close" class="open-section" id="auditHistoryID" onclick="toggleContent(this.id, 'auditHistoryDetails');"></span>
			<span id="auditHistorySpan" class="accordian-txt"><a><spring:message code="label.generic.auditHistory" /></a></span>
		</h3>
		<div id="auditHistoryDetails"  class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">
			<table id="auditHistory">
				<tr style="background-color: lightblue;">
					<th style="width: 40%; text-align: left;">Action</th>
					<th style="width: 35%; text-align: left;">User</th>
					<th style="text-align: left;">Date</th>
				</tr>
				<c:choose>
						<c:when test="${auditHistoryLst != null}">
							<c:forEach var="auditHistory" items="${auditHistoryLst}" varStatus="loop"> 
								<tr>
									<td>${auditHistory.message}</td>
									<td>${auditHistory.who}</td>
									<td>${auditHistory.auditDate}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr><td colspan="3"></td></tr>
						</c:otherwise>
				</c:choose>						
			</table>
		</div>
	</div>