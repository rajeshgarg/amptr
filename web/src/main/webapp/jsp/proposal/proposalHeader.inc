<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="propHeader" class="prop-header">
	<div class="name">Proposal Owner:&nbsp;&nbsp;${proposalForm.userName}</div>
	<input id="propVersionStaleObject" type="hidden" value="${proposalForm.version}" />
	<c:choose>
		<c:when test="${proposalForm.id > 0}">
			<div class="assign">Assign To:
                  	<select id="assignTo" name="assignTo" style="width:200px;margin-top:-1px;">
                  		<c:forEach  items="${roleUserMap}" var="entry">
                          	<optgroup label="${entry.key}">
                              	<c:forEach  items="${entry.value}" var="values">
                              		<c:choose>
										<c:when test="${logedInUser.userId == values.key}">
                                			<option selected value="${values.key}" >${values.value}</option>
                                		</c:when>
                                		<c:otherwise>
                                			<option value="${values.key}" >${values.value}</option>
                                		</c:otherwise>	
                                	</c:choose>	
                                </c:forEach>
                            </optgroup>
                      	</c:forEach>  
                      </select>
				<input type="button" class="save-btn reset-save-btn" value="Go" style="margin-top: 0;" onclick="assignToUser('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'${proposalForm.userName}')">
			</div>
			<div class="status-header">
				<span class="capt">Status:</span><span id="proposalStatusHeader" class="val"> ${proposalForm.proposalStatusDisplayName}
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS' && proposalOwnerRole == 'PAN'}">
						<span id ='pricingReviewMsg'>(Under pricing review)</span>
					</c:if>
				</span>
			</div>			
			<div class="clone">
				<!--Mark as In Progress-->
				<sec:authorize access="hasRole('AOP')"> 
					<c:if test="${proposalForm.proposalStatus == 'REJECTED_BY_CLIENT' || proposalForm.proposalStatus == 'EXPIRED' || proposalForm.proposalStatus == 'REVIEW' || proposalForm.proposalStatus == 'READY_TO_BRIDGE'}">
						<div style ="float: left;" class="inprogress-ico" title="Mark as In Progress" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'INPROGRESS','confirm.update.proposal.status.inProgress','message.generic.proposal.stausChanged.inprogress')"></div>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('ADM','POW')"> 
					<c:if test="${proposalForm.proposalStatus == 'PROPOSED' || proposalForm.proposalStatus == 'REVIEW' || proposalForm.proposalStatus == 'UNASSIGNED' || proposalForm.proposalStatus == 'REJECTED_BY_CLIENT' || proposalForm.proposalStatus == 'READY_TO_BRIDGE' || proposalForm.proposalStatus == 'EXPIRED'}">
						<div style ="float: left;" class="inprogress-ico" title="Mark as In Progress" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'INPROGRESS','confirm.update.proposal.status.inProgress','message.generic.proposal.stausChanged.inprogress')"></div>
					</c:if>					
				</sec:authorize>
				<sec:authorize access="hasAnyRole('PLR', 'EPM')"> 
					<c:if test="${proposalForm.proposalStatus == 'PROPOSED' || proposalForm.proposalStatus == 'UNASSIGNED' || proposalForm.proposalStatus == 'REVIEW' || proposalForm.proposalStatus == 'REJECTED_BY_CLIENT' || proposalForm.proposalStatus == 'EXPIRED'}">
						<div style ="float: left;" class="inprogress-ico" title="Mark as In Progress" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'INPROGRESS','confirm.update.proposal.status.inProgress','message.generic.proposal.stausChanged.inprogress')"></div>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasRole('PAN')"> 
					<c:if test="${proposalForm.proposalStatus == 'REJECTED_BY_CLIENT' || proposalForm.proposalStatus == 'EXPIRED'}">
						<div style ="float: left;" class="inprogress-ico" title="Mark as In Progress" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'INPROGRESS','confirm.update.proposal.status.inProgress','message.generic.proposal.stausChanged.inprogress')"></div>
					</c:if>					
				</sec:authorize>
				
				<!--Mark as Deleted-->
				<sec:authorize access="hasAnyRole('ADM', 'PLR', 'EPM', 'POW')">
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS' || proposalForm.proposalStatus == 'UNASSIGNED'}">
						<div style ="float: left;" class="deleted-prop-ico" title="Mark as Deleted" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'DELETED','confirm.update.proposal.status.deleted','message.generic.proposal.stausChanged.deleted')"></div>
					</c:if>
				</sec:authorize>
				<sec:authorize access="hasRole('PAN')">
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS'}">
						<div style ="float: left;" class="deleted-prop-ico" title="Mark as Deleted" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'DELETED','confirm.update.proposal.status.deleted','message.generic.proposal.stausChanged.deleted')"></div>
					</c:if>
				</sec:authorize>
				
				<!--Submit for pricing review-->
				<sec:authorize access="hasAnyRole('PLR', 'ADM', 'EPM', 'POW')">	
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS'&& proposalOwnerRole != 'PAN' }">
						<div style ="float: left;" class="submit-for-pricing-review-ico" title="Submit for Pricing Review" onclick="submitForPricingReview('${flowExecutionUrl}', 'eventViewEditBasicInfo', ${proposalForm.id})"></div>
					</c:if>
				</sec:authorize>
				
				<!--Submit back to planner-->
				<sec:authorize access="hasRole('PAN')">	
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS' && proposalOwnerRole == 'PAN'}">
						<div style ="float: left;" class="submit-back-to-planner-ico" title="Submit Back To Planner" onclick="submitBackToPlanner('${flowExecutionUrl}', 'eventViewEditBasicInfo', ${proposalForm.id})"></div>
					</c:if>
				</sec:authorize>
				
				<!--Mark as Proposed-->
				<sec:authorize access="hasAnyRole('ADM', 'PLR', 'PAN', 'EPM', 'POW')">
					<c:if test="${proposalForm.proposalStatus == 'INPROGRESS'}">
						<div style ="float: left;" class="proposed-ico" title="Mark as Proposed" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'PROPOSED','confirm.update.proposal.status.proposed','message.generic.proposal.stausChanged.proposed')"></div>
					</c:if>
				</sec:authorize>
				
				<!--Mark as Rejected By Client-->
				<sec:authorize access="hasAnyRole('ADM', 'PLR', 'AOP', 'EPM', 'POW')">
					<c:if test="${proposalForm.proposalStatus == 'PROPOSED'}">								
						<div style ="float: left;" class="rejected-ico" title="Mark as Rejected By Client" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'REJECTED_BY_CLIENT','confirm.update.proposal.status.rejected','message.generic.proposal.stausChanged.rejected')"></div>
					</c:if>	
				</sec:authorize>
				
				<!--Mark as Under Review-->
				<sec:authorize access="hasAnyRole('ADM', 'PLR', 'AOP', 'EPM', 'POW')">
					<c:if test="${proposalForm.proposalStatus == 'PROPOSED'}">
						<div style ="float: left;" class="review-ico" title="Mark as Under Review" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'REVIEW','confirm.update.proposal.status.review','message.generic.proposal.stausChanged.review')"></div>
					</c:if>
				</sec:authorize>
				
				<!--Mark as Ready To Bridge-->
				<sec:authorize access="hasAnyRole('ADM', 'PLR', 'AOP', 'EPM', 'POW')">	
					<c:if test="${proposalForm.proposalStatus == 'REVIEW'}">
						<div style ="float: left;" class="sold-ico" title="Mark as Sold" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'SOLD','confirm.update.proposal.status.sold','message.generic.proposal.stausChanged.sold')"></div>
					</c:if>
				</sec:authorize>
				
				<!--Blank Separator-->
				<div class="blank-separator" style="float:left;display:inline-block;width:25px;height:24px"></div>

				<!--Clone Proposal-->
				<sec:authorize access="hasAnyRole('PLR', 'ADM', 'EPM', 'POW')">				
					<div style="float: left; " title="Clone Proposal" class="clone-ico" onclick="cloneProposal('${proposalForm.id}');"></div>  
				</sec:authorize>
				
				<!--Copy URL-->
				<div style="float:left;" title="Copy URL"><span id="copyUrl" class="copy-url-ico" onclick="copyUrl(${proposalForm.id})"></span></div>
				
				<!--Mark as Unassigned-->
				<c:if test="${proposalAccessMap['UNASSIGNED']}">
					<div style ="float: left;" class="unassigned-ico" title="Mark as Unassigned" onclick="updateProposalStatus('${flowExecutionUrl}','eventViewEditBasicInfo',${proposalForm.id},'UNASSIGNED')"></div>
				</c:if>
			</div>	
		</c:when>
	</c:choose>
</div>
<script>
$(document).ready(function () {
	if ($("#propHeader #assignTo").length > 0) {
		$("#propHeader #assignTo").select2(); // Select2 plug-in called on assign to field
	}
});
</script>