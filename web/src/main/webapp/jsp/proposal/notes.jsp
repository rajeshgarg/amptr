<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div id="proposalNotesContainer">
	<div id="proposalNotes">
		<div id="proposalNotesAccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
			<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
				<span title="Close" class="open-section" id="notesID" onclick="toggleContent(this.id, 'proposalNoteDetails');"></span>
				<span id="notesSpan" class="accordian-txt"><a><spring:message code="label.generic.proposalNotes" /></a></span>
			</h3>
		
			<div id="proposalNoteDetails" style="padding-left : 15px;"  class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active" >
				<div id="notesOperator">
				<textarea path="notesDescription"style="width:97%;height:80px" id="writeNotes"></textarea>
				<img src='./images/info-icon.png' style="position:relative;top:-68px" class="top-170 tip-toolright"	title="<spring:message code='help.proposal.notes'/>" /> 
				<input type="button" value="SAVE" id="notesSaveData" class="save-btn marg-top-6" onclick="saveUpdateNotesData(false)" style="width:90px !important"/> 
				<input type="button" value="UPDATE" id="notesUpdateData" class="reset-btn marg-top-6" style="display: none;width:90px !important" onclick="saveUpdateNotesData(false)" />
				<input type="button" value="PUSH TO SALES FORCE" id="saveNotesInSalesForce" class="reset-btn marg-top-6" onclick="saveUpdateNotesData(true)" />
				<input type="button" value="RESET" id="notesResetData" class="reset-btn marg-top-6"	onclick="resetNotesFields()" style="width:90px !important" /> 
				<input type="button" value="CANCEL" id="notesCancelData" class="reset-btn marg-top-6" style="display: none;width:90px !important" onclick="resetNotesFields()" />
				<div id="charsRemainingProposalNotes" class="chars-remaining" style="display:inline-block;margin-right:20px"></div>
				</div>
				<table  cellpadding="2" cellspacing="2"  style=" width:100%; border:0px;">
					<tr>
						<input id="notesId" value='0' style="display:none;"/>
						<input id="proposalId" style="display:none;"/>
					</tr>
				</table>
				<div id="displayNotesContainer" style="margin: 10px 10px 0px 0px; border-radius: 5px;">
					<c:choose>
						<c:when test="${proposalNotesLst != null}">
							<c:forEach var="notes" items="${proposalNotesLst}" varStatus="loop">
								<div id='displayContainer_${notes.notesId}'>
									<div class='display-notes'>
										<div id='displayNotes_${notes.notesId}' class='notes-added-by'>${notes.notesSectionHeader}
										</div>
										<div id="notesAction" class='action-icons'>
											<c:if test="${!notes.pushedInSalesforce}">
												<sec:authorize access="hasAnyRole('ADM', 'PLR', 'EPM')">
													<span id = 'deleteNotes_${notes.notesId}' class='notes-delete-ico' title='Delete selected note' onclick='deleteNotes(${notes.notesId})'></span>
													<span id = 'editNotes_${notes.notesId}' class='notes-edit-ico' title='Edit selected note' onclick='editNotes(${notes.notesId})'></span>
												</sec:authorize>
											</c:if>
											<input type="hidden" id='pushedInSalesforce_${notes.notesId}' value='${notes.pushedInSalesforce}'></input>
										</div>
										<div style="clear:both"></div>
										<div class='notes-content' id='notesDetails_${notes.notesId}'>${notes.notesDescription}</div>
									</div>
									<div class='spacer7'></div>
								</div>
							</c:forEach>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>