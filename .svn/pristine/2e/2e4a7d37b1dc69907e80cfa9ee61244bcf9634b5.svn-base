/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.dao.ISalesForceProposalDAO;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.service.IAuditService;
import com.nyt.mpt.service.ICronJobService;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.ISalesForceProposalService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.SalesforceProposalServiceHelper;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.enums.DocumentTypeEnum;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.SfProposalRevisionTypeEnum;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan_Option__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

/**
 * This {@code SalesForceProposalService} class has all the implementations of the interface {@link ISalesForceProposalService}.
 * The class {@code SalesForceProposalService} includes the methods for creating {@link Proposal} from salesforce's {@link Media_Plan__c}, for creating {@link Media_Plan_Revision__c} as {@link Notes} in AMPT database,
 * for creating and updating {@link Media_Plan_Option__c} as {@link ProposalOption} in AMPT and vice-versa, for saving the {@link Attachment} in AMPT's {@link Document} in file system as {@link File}
 * @author Gurditta.Garg
 * 
 */
public class SalesForceProposalService implements ISalesForceProposalService {

	private static final int BYTES_IN_KILO_BYTE = 1024;
	
	private String applicationURL;
	private ISalesForceProposalDAO sfProposalDAO;
	private ICronJobService cronJobService;
	private IDocumentService documentService;
	private SalesforceProposalServiceHelper sfProposalHelper;
	private IAuditService auditService;
	private IProposalSOSService proposalSOSService;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#saveProposalToAMPT(com.nyt.mpt.domain.Proposal, com.sforce.soap.enterprise.sobject.Media_Plan__c)
	 */
	@Override
	public Proposal saveSfProposalToAMPT(final Proposal proposal, final Media_Plan__c sfProposal) throws IOException {
		if (proposal != null && sfProposal != null) {
			sfProposalHelper.settingOptionsFirstTime(proposal, sfProposal);
			sfProposalDAO.saveProposalSF(proposal);
			auditService.createAuditForNewProposalFromSalesforce(proposal, sfProposal);
			final Notes notes = sfProposalHelper.getDefaultNote(proposal, sfProposal);
			if (notes != null) {
				sfProposalDAO.saveNotesSF(notes);
			}
			saveAttachments(proposal.getId(), proposal.getAttachments());
		}
		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#saveRevisionsToAMPT(com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c)
	 */
	@Override
	public void saveSfRevisionsToAMPT(final Media_Plan_Revision__c sfRevision, final List<Attachment> attachments) throws IOException, ConnectionException {
		final Proposal proposal = getProposalBySalesforceId(sfRevision.getCampaign_Name__r().getId().substring(0,15));
		if (proposal != null) {
			if (sfProposalHelper.proposalStatusExceptReadOnly(proposal)) {
				if ((ProposalStatus.EXPIRED.name()).equals(proposal.getProposalStatus().name())) {
					if (proposal.getSosOrderId() != null && proposal.getSosOrderId() > 0) {
						sfProposalHelper.sendMail(sfRevision, emailBodyForSoldStatus(sfRevision, proposal), false, proposal);
						return;
					}
					proposal.setLastProposedDate(null);
				}
				saveAttachments(proposal.getId(), attachments);
				proposal.setProposalStatus(ProposalStatus.UNASSIGNED);
				proposal.setAssignedUser(null);
				proposal.setWithPricing(false);
				if (ConstantStrings.SALES_ORDER.equals(sfRevision.getRevision_Type__c())) {
					proposal.setSfRevisionType(SfProposalRevisionTypeEnum.SALESORDER.name());
				} else {
					proposal.setSfRevisionType(SfProposalRevisionTypeEnum.REVISION.name());
				}
				final Notes note = sfProposalHelper.getRevisionNote(sfRevision, proposal);
				if (note != null) {
					sfProposalDAO.saveNotesSF(note);
					sfProposalHelper.setRevisionInfoForUpdateToSF(note);
				}
				auditService.createAuditForNewProposalRevisionFromSalesforce(proposal, sfRevision);
			} else {
				boolean standardBodyFlag = false;
				final List<String> message = new ArrayList<String>();
				if(ProposalStatus.SOLD.name().equals(proposal.getProposalStatus().name())) {
					standardBodyFlag = true;
					message.addAll(emailBodyForSoldStatus(sfRevision, proposal));
				} else {
					message.add("Exception in saving Revision ( " + sfRevision.getName() + " ) for the Proposal ( " + proposal.getName() + " ) as proposal status is in Read only Mode");
				}
				sfProposalHelper.sendMail(sfRevision, message, standardBodyFlag, proposal);
			}
		}
	}

	/**
	 * Saves salesforce {@link Attachment} to AMPT {@link Document} and as {@link File} in the file system
	 * @param 	proposalid
	 * 			This is the {@code componentId} of the {@link Document} in MP_DOCUMENTS table in AMPT
	 * @param 	attachments
	 * 			List of {@link Attachment} which are going to saved in AMPT {@link Document} and as {@link File} in the file system
	 * @throws 	IOException
	 * 			This exception occur while creating {@link File} in the file system
	 */
	private void saveAttachments(final long proposalid, final List<Attachment> attachments) throws IOException {
		if (!attachments.isEmpty()) {
			for (Attachment attachment : attachments) {
				final Document document = new Document();
				final DocumentTypeEnum docTypeEnum = DocumentTypeEnum.getDocType(attachment.getName());
				document.setFileName(attachment.getName());
				document.setFileType(docTypeEnum == null ? ConstantStrings.NA : docTypeEnum.name());
				document.setDescription(attachment.getDescription());
				document.setDocumentFor(DocumentForEnum.PROPOSAL.name());
				document.setComponentId(proposalid);
				document.setFileSize(attachment.getBodyLength() / BYTES_IN_KILO_BYTE);
				documentService.saveDocumentAndFile(document, attachment.getBody());
			}
		}
	}

	/**
	 * Returns the list of errors while saving the {@link Media_Plan_Revision__c} in AMPT if, and only if, the {@link Proposal} has been marked {@code SOLD} or {@code EXPIRED}
	 * @param 	sfRevision
	 * 			{@link Media_Plan_Revision__c} which has all the information which will be used while sending the mail if any error occur
	 * @param 	proposal
	 * 			{@link Proposal} has {@code proposalName} which is being used in email body
	 * @return
	 * 			Returns the list of errors while saving the {@link Media_Plan_Revision__c} in AMPT
	 */
	private List<String> emailBodyForSoldStatus(final Media_Plan_Revision__c sfRevision, final Proposal proposal) {
		final List<String> message = new ArrayList<String>();
		message.add("A new Sales Order ( " + sfRevision.getName() + " ) has been requested for Proposal ( " + proposal.getName() + " ). "
				+ "This Proposal has already been marked as sold in AMPT and bridged to SOS, so any revisions must be handled as post-sale changes and in accordance with IO terms and conditions.");
		message.add(ConstantStrings.NEW_AND_TAB_LINE);
		message.add("The requestor included the following notes:-");
		final String noteDescription = StringUtils.isBlank(sfRevision.getRevision_Note__c()) ? ConstantStrings.EMPTY_STRING : sfRevision.getRevision_Note__c();
		message.add(noteDescription);
		return message;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#getSalesForceProposals()
	 */
	@Override
	public List<Media_Plan__c> getSalesForceProposals() throws ConnectionException {
		final int recordFetchDuration = cronJobService.getRecordFetchDuration(CronJobNameEnum.SALESFORCE_PROPOSAL_INTEGRATION_JOB.name()).getRecordFetchDurationInMin();
		return sfProposalDAO.getSalesForceProposals(recordFetchDuration);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#getSalesForceProposalRevisions()
	 */
	@Override
	public List<Media_Plan_Revision__c> getSalesForceProposalRevisions() throws ConnectionException {
		final int recordFetchDuration = cronJobService.getRecordFetchDuration(CronJobNameEnum.SALESFORCE_PROPOSAL_INTEGRATION_JOB.name()).getRecordFetchDurationInMin();
		return sfProposalDAO.fetchProposalsRevisions(recordFetchDuration);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#saveOrUpdateEmailFlag(com.nyt.mpt.domain.SalesforceProposalEmailFlag)
	 */
	@Override
	public SalesforceProposalEmailFlag saveOrUpdateEmailFlag(final SalesforceProposalEmailFlag sfPropEmailFlag) {
		return sfProposalDAO.saveOrUpdateEmailFlag(sfPropEmailFlag);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#updateProposalToSalesforce(com.sforce.soap.enterprise.sobject.Media_Plan__c)
	 */
	@Override
	public void updateProposalToSalesforce(final Proposal amptProposal, String action) throws ConnectionException, CustomCheckedException {
		final StringBuffer url = new StringBuffer();
		url.append(this.applicationURL).append(ConstantStrings.PROPOSAL_URL).append(amptProposal.getId());
		final Media_Plan__c sfProposal = new Media_Plan__c();
		sfProposal.setId(amptProposal.getSalesforceID());
		sfProposal.setAMPT_Key__c(ConstantStrings.AMPT_KEY + String.valueOf(amptProposal.getId()));
		sfProposal.setAMPT_Link__c(url.toString());
		sfProposal.setCreated_in_AMPT__c(DateUtil.dateTypeToCalendar(amptProposal.getCreatedDate()));
		sfProposalDAO.updateMediaPlanToSalesforce(sfProposal);

		final Ad_Systems_Workflow_Update__c eventAuditLog = new Ad_Systems_Workflow_Update__c();
		eventAuditLog.setAction__c(action);
		eventAuditLog.setDate__c(DateUtil.dateTypeToCalendar(DateUtil.getCurrentDate()));
		eventAuditLog.setUser__c(SecurityUtil.getUser().getFullName());
		eventAuditLog.setProposal__c(amptProposal.getSalesforceID());
		saveProposalAuditLogInSalesforce(eventAuditLog);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#updateProposalRevisionToSalesforce(com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c)
	 */
	@Override
	public void updateProposalRevisionToSalesforce(final Media_Plan_Revision__c sFPropRevision) throws ConnectionException {
		sfProposalDAO.updateProposalRevisionToSalesforce(sFPropRevision);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#getEmailFlagBySalesforceId(java.lang.String)
	 */
	@Override
	public SalesforceProposalEmailFlag getEmailFlagBySalesforceId(final String salesforceId) throws BusinessException {
		return sfProposalDAO.getEmailFlagBySalesforceId(salesforceId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#updateMediaPlanToSalesforce(com.sforce.soap.enterprise.sobject.Media_Plan__c)
	 */
	@Override
	public void updateMediaPlanToSalesforce(final Media_Plan__c sfProposal) throws CustomCheckedException{
		sfProposalDAO.updateMediaPlanToSalesforce(sfProposal);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#fetchAttachmentsFromSalesforce(java.lang.String)
	 */
	@Override
	public List<Attachment> fetchAttachmentsFromSalesforce(final String proposalId) throws ConnectionException {
		return sfProposalDAO.fetchAttachmentsFromSalesforce(proposalId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#createOptionsInSf(java.util.Set)
	 */
	@Override
	public void createOptionsInSf(final Proposal proposal) throws CustomCheckedException {
		final List<Media_Plan_Option__c> options = sfProposalDAO.getOptionDetailsFromSf(proposal.getSalesforceID());
		int i = 0;
		if (!options.isEmpty()) {
			final String[] optionIds = new String[options.size()];
			for (Media_Plan_Option__c proposalOptionC : options) {
				optionIds[i++] = proposalOptionC.getId();
			}
			sfProposalDAO.deleteSalesforceObjects(optionIds);
		}
		i = 0;
		final SObject[] sfOptionsArr = new SObject[proposal.getProposalOptions().size()];
		for (ProposalOption propOption : proposal.getProposalOptions()) {
			Media_Plan_Option__c sfOption = new Media_Plan_Option__c();
			sfOption.setOption_Name__c(propOption.getName());
			sfOption.setCurrencyIsoCode(proposal.getCurrency());
			sfOption.setDefault__c(propOption.isDefaultOption());
			sfOption.setProposed_Value__c(getOfferedBudget(propOption, proposal.getConversionRate()));
			sfOption.setMedia_Plan__c(proposal.getSalesforceID());
			sfOption.setAgency_Margin__c(proposal.getAgencyMargin());
			sfOption.setPrice_Type__c(proposal.getPriceType());
			sfOption.setLast_Modified_by_AMPT_Date__c(DateUtil.dateTypeToCalendar(DateUtil.getCurrentDate()));
			sfOptionsArr[i++] = sfOption;
		}
		sfProposalDAO.createSalesforceObjects(sfOptionsArr);
	}
	
	/**
	 * Returns Offered Budget which will be used in {@link Media_Plan_Option__c} to set {@code Proposed_Value__c}
	 * @param 	propOption
	 * 			{@link ProposalOption} which has {@link ProposalVersion} which gives a set of Proposed {@link LineItem}
	 * @param 	conversionRate
	 * 			The conversion rate in {@link Proposal}
	 * @return
	 * 			Returns Offered Budget which will be used to set {@code Proposed_Value__c} in salesforce {@link Media_Plan_Option__c}
	 */
	private Double getOfferedBudget(final ProposalOption propOption, final Double conversionRate) {
		Double offeredBudget = 0.00;
		for (LineItem lineItem : propOption.getLatestVersion().getProposalLineItemSet()) {
			if (!LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue().equalsIgnoreCase(lineItem.getPriceType())) {
				if (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equalsIgnoreCase(lineItem.getPriceType())) {
					offeredBudget = offeredBudget + NumberUtil.round((lineItem.getTotalInvestment() != null ? lineItem.getTotalInvestment()	: 0)
													/ (conversionRate > 0 ? conversionRate : 1), 2);
				} else {
					Double rate = NumberUtil.round((lineItem.getRate() != null ? lineItem.getRate() : 0)	/ (conversionRate > 0 ? conversionRate : 1), 2);
					offeredBudget = offeredBudget + NumberUtil.round((rate * lineItem.getImpressionTotal()) / 1000, 2);
				}
			}
		}
		return offeredBudget;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#updateOptionsInSf(com.nyt.mpt.domain.ProposalOption, java.util.List)
	 */
	@Override
	public void updateOptionsInSf(final ProposalOption defaultOption, final List<Media_Plan_Option__c> options) throws CustomCheckedException {
		final List<SObject> sfOptionslst = new ArrayList<SObject>();
		if (!options.isEmpty()) {
			for (Media_Plan_Option__c proposalOptionC : options) {
				if (proposalOptionC.getOption_Name__c().equals(defaultOption.getName())) {
					Media_Plan_Option__c sfOption = new Media_Plan_Option__c();
					sfOption.setId(proposalOptionC.getId());
					sfOption.setDefault__c(defaultOption.isDefaultOption());
					sfOption.setSold_Value__c(getOfferedBudget(defaultOption, defaultOption.getProposal().getConversionRate()));
					sfOption.setLast_Modified_by_AMPT_Date__c(DateUtil.dateTypeToCalendar(DateUtil.getCurrentDate()));
					sfOptionslst.add(sfOption);
				} else if (proposalOptionC.getDefault__c()) {
					Media_Plan_Option__c sfOption = new Media_Plan_Option__c();
					sfOption.setId(proposalOptionC.getId());
					sfOption.setDefault__c(false);
					sfOption.setLast_Modified_by_AMPT_Date__c(DateUtil.dateTypeToCalendar(DateUtil.getCurrentDate()));
					sfOptionslst.add(sfOption);
				}
			}
		}
		final SObject[] sObjectArr = new SObject[sfOptionslst.size()];
		int i = 0;
		for (SObject sObject : sfOptionslst) {
			sObjectArr[i++] = sObject;
		}
		sfProposalDAO.updateSalesforceObjects(sObjectArr);
	}
	

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#updateOptionsInSalesForce(com.nyt.mpt.domain.Proposal, java.lang.String)
	 */
	@Override
	public void updateOptionsInSalesForce(final Proposal proposal, final String salesforceId) throws ParseException, CustomCheckedException {
		final List<Media_Plan_Option__c> options = sfProposalDAO.getOptionDetailsFromSf(salesforceId);
		final List<SObject> sfOptionslst = new ArrayList<SObject>();
		Set<ProposalOption> proposalOptionsSet = proposal.getProposalOptions();
		Map<String, ProposalOption> proposalOptionsMap = new HashMap<String, ProposalOption>(proposalOptionsSet.size());
		for (ProposalOption proposalOption : proposalOptionsSet) {
			proposalOptionsMap.put(proposalOption.getName(), proposalOption);
		}

		if (!options.isEmpty()) {
			if (checkIsPricingDataChanged(proposal, options.get(0))) {
				for (Media_Plan_Option__c proposalOptionSF : options) {
					Media_Plan_Option__c sfOption = new Media_Plan_Option__c();
					ProposalOption propOption = proposalOptionsMap.get(proposalOptionSF.getOption_Name__c());
					if (propOption != null) {
						sfOption.setId(proposalOptionSF.getId());
						sfOption.setDefault__c(propOption.isDefaultOption());

						if (propOption.isDefaultOption()) {
							sfOption.setSold_Value__c(getOfferedBudget(propOption, proposal.getConversionRate()));
						}

						Double proposedValue = proposalOptionSF.getProposed_Value__c();
						boolean priceTypeChanged = (proposalOptionSF.getPrice_Type__c().equalsIgnoreCase(proposal.getPriceType())) ? false : true;

						/*
						 * Converting the proposed value accordingly in case the
						 * agency margin has been changed and price type
						 * remained unchanged.
						 */
						if (!(proposalOptionSF.getAgency_Margin__c().equals(proposal.getAgencyMargin()))) {
							if (!priceTypeChanged && PriceType.Gross.name().equals(proposal.getPriceType())) {
								proposedValue = proposedValue * ((100 - proposalOptionSF.getAgency_Margin__c()) / (100 - proposal.getAgencyMargin()));
							}
						}

						/*
						 * Converting the proposed value in case the price type
						 * changes taking into consideration any agency margin
						 * changes automatically
						 */
						if (priceTypeChanged && PriceType.Gross.name().equalsIgnoreCase(proposal.getPriceType())) {
							proposedValue = proposedValue * (100 / (100 - proposal.getAgencyMargin()));
						} else if (priceTypeChanged && PriceType.Net.name().equalsIgnoreCase(proposal.getPriceType())) {
							proposedValue = proposedValue * ((100 - proposalOptionSF.getAgency_Margin__c()) / 100);
						}

						/*
						 * Converting the proposed value in case the currency
						 * changes.
						 */
						if (!(proposalOptionSF.getCurrencyIsoCode().equalsIgnoreCase(proposal.getCurrency()))) {
							String optionCurrency = proposalOptionSF.getCurrencyIsoCode();
							Map<Long, String> currenyMap = proposalSOSService.getCurrencies();
							for (Long currencyId : currenyMap.keySet()) {
								String currencyCode = currenyMap.get(currencyId);
								if (!optionCurrency.equalsIgnoreCase(currencyCode)) {
									if (currencyCode.equalsIgnoreCase(proposal.getCurrency())) {										
										if (!optionCurrency.equalsIgnoreCase("USD")) {
											double conversionRate = proposalSOSService.getCurrencyConversionRate().get(optionCurrency);
											proposedValue = proposedValue * conversionRate;
										}
										if (!currencyCode.equalsIgnoreCase("USD")) {
											double targetConversionRate = proposalSOSService.getCurrencyConversionRate().get(optionCurrency);
											proposedValue = proposedValue / targetConversionRate;
										}
									}
								}
							}
						}

						sfOption.setProposed_Value__c(proposedValue);
						sfOption.setCurrencyIsoCode(proposal.getCurrency());
						sfOption.setAgency_Margin__c(proposal.getAgencyMargin());
						sfOption.setPrice_Type__c(proposal.getPriceType());
						sfOption.setLast_Modified_by_AMPT_Date__c(DateUtil.dateTypeToCalendar(DateUtil.getCurrentDate()));
						sfOptionslst.add(sfOption);
					}
				}
			} else {
				updateOptionsInSf(proposal.getDefaultOption(), options);
			}
		}
		final SObject[] sObjectArr = new SObject[sfOptionslst.size()];
		int i = 0;
		for (SObject sObject : sfOptionslst) {
			sObjectArr[i++] = sObject;
		}
		sfProposalDAO.updateSalesforceObjects(sObjectArr);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ISalesForceProposalService#getMediaPlanBySfId(java.lang.String)
	 */
	@Override
	public List<Media_Plan__c> getMediaPlanBySfId(String salesforceId) throws ConnectionException, CustomCheckedException {
		return sfProposalDAO.getMediaPlanBySfId(salesforceId);
	}
	
	/**
	 * Returns {@code true} if, and only if, pricing data has been changed
	 * @param 	proposal
	 * 			{@link Proposal} has all following information which will used to check if any pricing data has changed or not
	 * @param 	optionSF
	 * 			{@link Media_Plan_Option__c} has all the information which will be used to compare with the pricing information {@link Proposal}
	 * @return
	 * 			Returns {@code true} if, and only if, pricing data has been changed otherwise return {@code false}
	 */
	private boolean checkIsPricingDataChanged(final Proposal proposal, final Media_Plan_Option__c optionSF) {
		boolean result = false;
		if (!(optionSF.getAgency_Margin__c().equals(proposal.getAgencyMargin())) || !(optionSF.getPrice_Type__c().equalsIgnoreCase(proposal.getPriceType()))
				|| !(optionSF.getCurrencyIsoCode().equalsIgnoreCase(proposal.getCurrency()))) {
			result = true;
		}
		return result;
	}
		
	@Override
	public Proposal getProposalBySalesforceId(final String salesforceID) {
		return sfProposalDAO.getProposalBySalesforceId(salesforceID);
	}

	@Override
	public void saveProposalAuditLogInSalesforce(final Ad_Systems_Workflow_Update__c ad_Systems_Workflow_Update__c) throws CustomCheckedException {
		sfProposalDAO.saveProposalAuditLogInSalesforce(ad_Systems_Workflow_Update__c);
	}
	
	public void setSfProposalDAO(final ISalesForceProposalDAO sfProposalDAO) {
		this.sfProposalDAO = sfProposalDAO;
	}

	public void setCronJobService(final ICronJobService cronJobService) {
		this.cronJobService = cronJobService;
	}

	public void setDocumentService(final IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setSfProposalHelper(final SalesforceProposalServiceHelper sfProposalHelper) {
		this.sfProposalHelper = sfProposalHelper;
	}

	public void setAuditService(final IAuditService auditService) {
		this.auditService = auditService;
	}

	public void setProposalSOSService(IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setApplicationURL(final String applicationURL) {
		this.applicationURL = applicationURL;
	}
}