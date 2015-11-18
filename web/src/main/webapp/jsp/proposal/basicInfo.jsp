<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="./js/proposal/basicInfo.js?appVersion=<spring:eval expression="@applicationVersion.getProperty('appVersion')" />"></script>

<div class="editProposalContainer edit-prop-cont">
	<%@ include file="proposalHeader.inc" %>	
	<div class="proposal-inner-container">
		<div id="basicInfoParentDiv">
			<input type="hidden" id="flowExecutionUrl" value="${flowExecutionUrl}">
			<form:form id="basicForm" action="${flowExecutionUrl}" modelAttribute="proposalForm">
				<c:set var="currentView" value="basicInfo" scope="request"></c:set>
				<jsp:include page="proposalStageNavigationBar.jsp"></jsp:include>
				<div id="newProposalaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons marg-top-basic">
					<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">						
						<span class="accordian-txt">
							<span class="open-section" id="basicInfoID" onclick="toggleContent(this.id, 'propoalDetails');"></span>
							<a href="#"><spring:message code="label.basicinfo.accordiontext.basicInfo"/></a>
						</span>												
					</h3>
					<div id="propoalDetails">
						<div class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">		
							<div id="messageHeaderDiv" class="error" style="margin-left:25px;margin-top:10px;"></div>
							<table width="100%" cellpadding="2" cellspacing="2" class="basic-content-align">
								<tbody>
									<tr>
										<td width="5%">&nbsp;</td>
										<td width="14%">
											<label class="label-bold">
												<spring:message code="label.basicinfo.advertisername"/>:
											</label>
										</td>
										<td width="30%" class="basic-advertiser-column">
											<form:hidden path="advertiserName" id="advertiserName" cssStyle="width: 292px;" onchange="setAdvertiserName(); generateProposalName();setSpecialAdvertiserImg();" />
											<span id="specialAdvertiserImg" class="special-advertiser tip-tool" title="Special Advertiser" style="top:5px;position:relative;display:none;"></span>
											<form:hidden path="specialAdvertiser"/>
											<form:hidden path="withPricing"/>
										</td>
										<td width="4%">&nbsp;</td>
										<td width="14%">
											<label class="label-bold">
												<spring:message code="label.basicinfo.agencyname"/>:
											</label>
										</td>
										<td width="30%" class="basic-advertiser-column">
											<form:hidden path="agencyName" id="agencyName" cssStyle="width: 292px;"  />												
										</td>
										<td width="5%">&nbsp;</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.newAdvertisername"/>:
											</label>
										</td>
										<td>				
											<form:input  id="newAdvertiserName" path="newAdvertiserName" cssStyle="width: 287px;"  maxlength="50" onblur="generateProposalName()"/>
											<img src='./images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.proposal.newadvertisername'/>" />
										</td>
										<td>&nbsp;</td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.pricetype"/>:
											</label>
										</td>
										<td>
											<form:select path="priceType" id="priceType" cssStyle="width: 292px;"  >
												<form:options items="${allPriceTypes}" />								
											</form:select>
											<img src='./images/info-icon.png' class="top-170 tip-toolright" title="<spring:message code='help.proposal.priceType'/>" />
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right"><span class="mandatory">*&nbsp;</span></td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.requestedon"/>:
											</label>
										</td>
										<td>
											<form:input id="requestedOn" style='margin-left: 0px;' path="requestedOn" readonly="true" size="13" />
											<a style='cursor:pointer;' id="requestedOnReset">
												<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
											</a>
										</td>
										<td align="right"><span class="mandatory">*&nbsp;</span></td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.dueon"/>:
											</label>
										</td>
										<td>
											<form:input id="dueOn" style='margin-left: 0px;' path="dueOn" readonly="true" size="13" onchange="generateProposalName()"/>
											<a style='cursor:pointer;' id="dueOnReset">
												<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
											</a>
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right" ><span class="mandatory">*&nbsp;</span></td>
										<td  nowrap="nowrap">
											<label class="label-bold">
												<spring:message code="label.basicinfo.campaignName"/>:
											</label>
										</td>
										<td>
											<form:input  id="campaignName" path="campaignName" cssStyle="width: 287px;"  maxlength="60" readonly="${readonly}" cssClass="${readonlyclass}" onblur="generateProposalName()" />
											<img src='./images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.proposal.campaignname'/>" />
										</td>										
										<td align="right">&nbsp;</td>
										<td nowrap="nowrap">
											<label class="label-bold">
												<spring:message code="label.basicinfo.proposalName"/>:
											</label>
										</td>
										<td >
											<form:input  id="proposalName" path="proposalName" cssStyle="width: 287px;" readonly="true" cssClass="input-textbox-readonly" maxlength="60"  />
											<img src='./images/info-icon.png' class="top-170 tip-toolright" title="<spring:message code='help.proposal.proposalname'/>" />
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right"><span class="mandatory">*&nbsp;</span></td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.salescategory"/>:
											</label>
										</td>
										<td class="basic-advertiser-column">
											<form:select path="salescategory" id="salescategory" cssStyle="width: 293px;" onchange="generateProposalName();" >
												<option value=""></option>	
												<form:options items="${allSalesCategories}" />								
											</form:select>
											<img src='./images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.proposal.salescategory'/>" />
										</td>
										<td align="right"><span class="mandatory">*&nbsp;</span></td>
										<td>
											<label class="label-bold">
												<spring:message code="label.generic.proposalpriorities"/>:
											</label>
										</td>
										<td>
											<form:select path="criticality" id="criticality" cssStyle="width: 293px;">
												<option value=""><spring:message code="label.generic.blankSelectOption"/></option>		
												<form:options items="${allCriticalities}" />								
											</form:select>
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>							
										<td>&nbsp;</td>
										<td>
											<label class="label-bold">
												<spring:message code="label.generic.startDate"/>:
											</label>
										</td>
										<td>
											<form:input id="startDate" style='margin-left: 0px;' path="startDate" readonly="true" size="12" />
											<a style='cursor:pointer;' id="startDateReset">
												<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'></span>
											</a>
										</td>		
										<td>&nbsp;</td>
										<td>
											<label class="label-bold">
												<spring:message code="label.generic.endDate"/>:
											</label>
										</td>
										<td>
											<form:input id="endDate" style='margin-left: 0px;' path="endDate" readonly="true" size="12" />
											<a style='cursor:pointer;' id="endDateReset">
												<span class="reset-decoration" style="vertical-align:-2px !important;" title='<spring:message code="label.generic.clear"/>'/></span>
											</a>
										</td>	
										<td>&nbsp;</td>																							
									</tr>
									<tr id="assignedByRow">
										<td align="right"><span class="mandatory">&nbsp;</span></td>
										<td style="vertical-align: middle;" >
											<label class="label-bold">
												<spring:message code="label.basicinfo.assignedBy"/>:
											</label>
										</td>
										<td>
											<form:input cssClass="input-textbox-readonly" id="assignedBy" readonly="true"  path="assignedBy" cssStyle="width: 288px;" />											
										</td>
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.accountManager"/>:
											</label>
										</td>
										<td>				
											<form:input  id="accountManager" path="accountManager" cssStyle="width: 287px;"  maxlength="60"/>
										</td>
										<td>&nbsp;</td>	
									</tr>
									<tr>			
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold"><spring:message code="label.basicinfo.campaignObjective"/>:</label>
										</td>
										<td class="multiselect-sales-target">
											<div id="campaignObjective_custom" class="multi-select-box" style="width:294px">
												<form:select path="campaignObjective" id="campaignObjective" multiple="multiple" cssStyle="width:79.5%;" size="5">
													<form:options items="${allCampainObjective}" />		
												</form:select>
											</div>
										</td> 
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.clonedfrom"/>:
											</label>
										</td>
										<td>
											<form:input cssClass="input-textbox-readonly" id="clonedFormProposal" readonly="true"  path="clonedFormProposal" cssStyle="width: 287px;"  title="${proposalForm.clonedFormProposal}" />
										</td>
										<td>&nbsp;</td>																	
									</tr>
									<tr>
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.currency"/>:
											</label>
										</td>											
										<td>
											<form:select path="currencyId" id="currencyId" cssStyle="width: 292px;" onchange="getCurrencyConversionRate()">
												<form:options items="${currency}" />
											</form:select>
										</td>
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.ConversionRate"/>:
											</label>
										</td>
										<td>
											<form:input cssClass="input-textbox-readonly" path="conversionRate" id="conversionRate" readonly="true"   cssStyle="width: 287px;" />											
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right"><span class="mandatory">*&nbsp;</span></td>
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.agencyMargin"/>:
											</label>
										</td>
										<td>
											<form:input id="agencyMargin" path="agencyMargin" cssStyle="width: 288px;" cssClass="numericdecimal" maxlength="6" />
											<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.pricing.rule.price'/>" />
										</td>
										<td>&nbsp;</td>	
										<td>
											<label class="label-bold">
												<spring:message code="label.basicinfo.lastProposedDate"/>:
											</label>
										</td>
										<td>
											<form:input cssClass="input-textbox-readonly" path="lastProposedDate" id="lastProposedDate" readonly="true"   cssStyle="width: 287px;" />											
										</td>
										<td>&nbsp;</td>
									</tr>
									<sec:authorize access="hasAnyRole('ADM')">
									<c:choose>
										<c:when test="${empty proposalForm.salesforceID && assignedToCurrentUser && proposalForm.id > 0 && 
														(proposalForm.proposalStatus == 'INPROGRESS' || proposalForm.proposalStatus == 'PROPOSED' || proposalForm.proposalStatus == 'REVIEW')}">
											<tr>
												<td>&nbsp;</td>
												<td>
													<label class="label-bold">
														<spring:message code="label.basicinfo.salesforceID"/>:
													</label>
												</td>
												<td>
													<form:input id="modifySalesforceId" path="salesforceID" cssStyle="width: 208px;margin-right: 5px;" cssClass="input-textbox-readonly" readonly="true"/>
													<a href="javascript:void(0)" id="sfIdEdit" class="modifySalesforceId-icon" title="Edit" onclick="showHideField('EDIT',this)">
														<span class="ui-icon ui-icon-pencil"></span>
													</a>
													<a href="javascript:void(0)" id="sfIdSave" class="modifySalesforceId-icon" title="Save" onclick="mapSalesforceId()" style="display: none;">
														<span class="ui-icon ui-icon-disk"></span>
													</a>
													<a href="javascript:void(0)" id="sfIdClose" class="modifySalesforceId-icon" title="Cancel" onclick="showHideField('CANCEL',this)" style="display: none;">
														<span class="ui-icon ui-icon-close"></span>
													</a>
													<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-tool" title="<spring:message code='help.salesforce.map.new.id'/>" />
												</td>
												<td>&nbsp;</td>
											</tr>
										</c:when>
									</c:choose>
									</sec:authorize>
									<tr>
										<td>&nbsp;</td>
										<td style="vertical-align:middle;">
											<label class="label-bold"><spring:message code="label.basicinfo.reservationEmails"/>:</label>
											<div id="charsRemainingReservationMails" class="chars-remaining"></div>
										</td>
										<td colspan="4">
											<form:textarea path="reservationEmails" id="reservationEmails" cssStyle="width:97%;height:45px"></form:textarea>
											<img src='<%= request.getContextPath() %>/images/info-icon.png' class="tip-toolright" style="top:-35px;position:relative;" title="<spring:message code='help.reservation.emails'/>" />
										</td>
										<td>&nbsp;</td>
									</tr>
									<tr style="display:none;" id="SOSURLRow">
										<td>&nbsp;</td>
										<td>
											<label class="label-bold"><spring:message code="label.basicinfo.SOSURL"/>:</label>
										</td>
										<td colspan="4" style="vertical-align:top;"><a href="javascript:void(0)" id="SOSURL" class="app-anchor" onClick="openCreatedOrder()"></a></td>
										<td>&nbsp;</td>
									</tr>
								</tbody>
							</table>
							<div class="spacer7"></div>
							<div class="save-buttons-div">
								<table style="width: 100%;">
									<tr>
										<td align="center" colspan="3">
											<c:choose>
												<c:when test="${readOnlyView}">
													<input class="disabled-btn marg-top-6"  type="button" value="SAVE PROPOSAL" id="saveProposal" />													
												</c:when>
												<c:otherwise>
													<input class="save-btn marg-top-6"  type="button" value="SAVE PROPOSAL" id="saveProposal" onClick="saveProposalData('basicForm', '${flowExecutionUrl}', 'eventViewEditBasicInfo','noEvent');" />
												</c:otherwise>	
											</c:choose>
											<input type="hidden" value="${readOnlyView}" id="isAssigned" />
											<input type="hidden" id="assignedToCurrentUser" value="${assignedToCurrentUser}">
											<input type="hidden" value="eventViewEditLineItemDetail" id="_eventId" />
											<form:hidden id="id" path="id" />
											<form:hidden id="salesforceID" path="salesforceID"/>
											<form:hidden path="proposalStatus" id="proposalStatus" />	
											<form:hidden id="sosOrderId" path="sosOrderId" cssStyle="width: 288px;" readonly="true" cssClass="input-textbox-readonly" maxlength="100"/>
											<form:hidden path="currency" id="currency" />
											<form:hidden path="sosURL"  id="sosURL" />
											<form:hidden path="conversionRate_Euro" id="conversionRate_Euro" />
											<form:hidden path="conversionRate_Yen"  id="conversionRate_Yen" />
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</form:form>
				<div id="optionsGridContainer">
					<div id="optionsGrid">
					<div id="campaigndocumentsaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
							<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
								<span title="Close" class="open-section" id="optionsGridID" onclick="toggleContent(this.id, 'optionsGridDetails');"></span>
								<span id="proposalOptionSpan" class="accordian-txt"><a ><spring:message	code="label.generic.proposalOptions" /></a></span>								
							</h3>
							<div id="optionsGridDetails" class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">		
								<table id="optionsTable" class="optionsGrid"></table>
								<div id="optionsPager"></div>
							</div>
						</div>
					</div>
				</div>
				<div id="proposalDocumentsContainer">
					<div id="proposalDocuments">
						<div id="campaigndocumentsaccordion" class="ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
							<h3 class="proposal-headr accordian-header ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top">
								<span title="Close" class="open-section" id="documentsID" onclick="toggleContent(this.id, 'documentsDetails');"></span>
								<span id="uploadDocumentSpan" class="accordian-txt"><a><spring:message	code="label.generic.proposalUploadCampaignDocuments" /></a></span>								
							</h3>	
							<div id="documentsDetails" class="proposal-accordion-content ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active">	
								<div id="proposalDocumentsGrid">
									<div>
										<table id="proposalDocumentsGridTable" class='childGrid'></table>
										<div id="proposalDocumentsGridPager"></div>
									</div>
									<div style="display: none" id="proposalDocumentsGridDialog" title="Upload File">
										<form id="proposalDocumentsGridForm" enctype="multipart/form-data" action="./document/savedocument.action" method="POST">
											<div id='messageHeaderDiv'> </div>
											<table width="100%" align="center" class="table-layout-fixed">
												<tr>
													<td width="7%" class="align-right"><span class="mandatory">*&nbsp;</span></td>
													<td width="13%"> <label class="label-bold">Document:</label></td>
													<td width="80%">
														<input name="file" id="attachement" type="file" size="80"/>&nbsp;
														<img src='/ampt/images/info-icon.png' class='tip-tool' title='Only PDF, DOC, DOCX, PPT, PPTX, PNG, JPG, GIF, BMP, XLSX and XLS file formats <br>having size lesser than 1 MB are supported for upload.' />
													</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td><label class="label-bold">Purpose:</label></td>
													<td><input name="description" id="description" type="text" style="width:90%" maxlength="500"/></td>
												</tr>
												<tr>
													<td colspan="3"><div class="spacer7"></div></td>
												</tr>
												<tr>
													<td colspan="3" align="center">
														<div id="proposalDocumentsGridMessage" align="left"></div>
														<input type="hidden" name="id" id="id" />
														<input type="hidden" name="documentFor" id="documentFor" value="PROPOSAL" />
														<input type="hidden" name="componentId" id="componentId"/>
														<input type="hidden" name="fileName" id="fileName"/>
													</td>
												</tr>
											</table>
										</form>
									</div>						
								</div>
							</div>
						</div>
					</div>
				</div>
			<div id="notesContainer"></div>
			<div id="auditHistoryContainer"></div>
		</div>
		<!-- Create/Edit Option -->	
		<div style="display: none;" id="optionDialogue" title="Option" >
			<form:form  id="optionForm" name="optionForm" action="./proposalWorkflow/saveOption.action" method="post" modelAttribute="proposalForm" >
				<div id="optionContainer">
					<div id="messageHeaderDiv" class="error"></div>
					<div class="spacer7"></div>
					<table  cellpadding="2" cellspacing="2"  style=" width:100%; border:0px;">
						<tr>
							<td style="padding-left:100px;height:25px;" width="30%"><label class="label-bold"><spring:message code="label.basicinfo.optionName"/>:</label></td>
							<td>						
								<label id="optionName"></label>
								<form:hidden path="optionId" id="optionId" />
								<form:hidden path="maxOptionNo" id="maxOptionNo" />
							</td>
						</tr>
						
						<tr>
							<td style="padding-left:100px;"><label class="label-bold"><spring:message code="label.basicinfo.budget"/>:</label></td>
							<td>						
								<form:input  path="budget" id="budget" maxlength="10" cssStyle="width: 241px;" cssClass="numericdecimal" />
								<img src='./images/info-icon.png' class="top-170 tip-tool" title="<spring:message code='help.proposal.budget'/>" />
							</td>
						</tr>			
					</table>
				</div>
				<div style="display:none">
					<input type="text" value="">
				</div>
			</form:form>	
		</div>			
	</div>
</div>
<script>
	$('.tip-tool[title]').qtip({position:{my:  'left center',at: 'right center'}, style: {classes: 'ui-tooltip-blue'} });
	$('.tip-toolright[title]').qtip({position:{my:  'right center',at: 'left center'},style: {classes: 'ui-tooltip-blue'}});
</script>