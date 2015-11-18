<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style>
	.disable-column a.anchor-color{color:#D9DEE4 !important}
	.disable-column a.anchor-color:hover{color:#D9DEE4 !important}
</style>
<div id="optionStatusContainer" class="">
	<table cellpadding="2" cellspacing="2" style="float:left;">
		<tr id="optionNavigatorRow">
			
			<c:choose>
				<c:when test="${currentView == 'buildProposal'}">
					<td>
						<p><a class="selected-current-link nav-anchor pad-left-0">Line Item</a></p>
					</td>
				</c:when>
				<c:otherwise>
					<td>
						<p>
							<a class="inactive-sub-menu-link nav-anchor pad-left-0" href="#" onClick="stateChangeRequest('${flowExecutionUrl}', 'eventBuildProposal')">Line Item</a>
						</p>
					</td>						
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${currentView == 'proposalTemplate'}">
					<td>
						<p><a class="selected-current-link nav-anchor">Generate Media Plan</a></p>
					</td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${proposalForm.id == 0}">
							<td style="letter-spacing: 0.2px">
								<p><a class="inactive-sub-menu-link disable-column nav-anchor anchor-color">Generate Media Plan</a></p>
							</td>
						</c:when>
						<c:otherwise>
							<td class="">
								<p><a class="inactive-sub-menu-link nav-anchor" href="#" onClick="stateChangeRequest('${flowExecutionUrl}', 'eventGenerateMediaTemplate')">
									Generate Media Plan</a></p>
							</td>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${currentView == 'summary'}">
					<td>
						<p><a class="selected-current-link nav-anchor bod-rt-0">Summary</a></p>
					</td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${proposalForm.id == 0}">
							<td style="letter-spacing: 0.2px">
								<p><a class="inactive-sub-menu-link disable-column nav-anchor anchor-color bod-rt-0">Summary</a></p>
							</td>
						</c:when>
						<c:otherwise>
							<td class="">
								<p><a class="inactive-sub-menu-link nav-anchor bod-rt-0" href="#" onClick="stateChangeRequest('${flowExecutionUrl}', 'eventShowSummary')">
									Summary</a></p>
							</td>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>		
		</tr>
	</table>
	<table style="float: right; font-size: 12px; ">
		<tr>
			<td class="version-title">Version:</td>
			<td>
				<select id="currentVersion" name="currentVersion" onchange="toggleCreateVersion('${allVersions}');">
					<c:forEach  items="${allVersions}" var="entry">
						<c:choose>
							<c:when test="${proposalForm.previousProposalVersion == entry.value}">
               	    			<option selected value="${entry.key}">${entry.value}</option>
               	    		</c:when>
               	    		<c:otherwise>
               	    			<option value="${entry.key}">${entry.value}</option>
               	    		</c:otherwise>
               	    	</c:choose>	
                   	</c:forEach>  
				</select>
			</td>
			<td>
				<input type="button" class="save-btn reset-save-btn" value="Go" style="margin: -4px 0 0 7px;" onClick="showPreviousRevision('${flowExecutionUrl}','eventShowPreviousVersionData','${proposalForm.id}','${proposalForm.optionId}')"></input>
			</td>
			<td>
				<c:if test="${!(readOnlyView) && (proposalForm.proposalStatus != 'REVIEW')}">
					<sec:authorize access="hasAnyRole('PLR', 'ADM', 'PAN', 'EPM', 'POW')">	
						<a id="createVersion" class="anchor-link" style="margin: -2px 0 0 10px;vertical-align: top;font-weight:700;" onclick="createVersionConfirmationDialog('${flowExecutionUrl}', 'eventCreateNewVersion','${proposalForm.id}','${proposalForm.optionId}')">Create Version</a>
					</sec:authorize>
				</c:if>
			</td>
		</tr>
	</table>
</div>