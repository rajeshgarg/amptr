/**
 * 
 */
package com.nyt.mpt.util;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.mail.MailException;

import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.ISalesForceProposalService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.DocumentTypeEnum;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.ws.ConnectionException;

/**
 * This <code>SalesforceProposalServiceHelper</code> class includes all the helper and utility methods which are being used in communicating salesforce to AMPT and vice-versa.
 * @author Gurditta.Garg
 */
public class SalesforceProposalServiceHelper {

	private static final Long ATTACHMENT_SIZE_LIMIT = 2L;
	private static final Long MEGABYTE = 1024L * 1024L;
	
	private MailUtil mailUtil;
	private ISOSService sosService;
	private IProposalSOSService proposalSOSService;
	private ISalesForceProposalService salesForceProposalService;
	private IProposalService proposalService;
	private IUserService userService;
	
	private String salesforceIntegrationEmailFrom;
	private String salesforceConnectionFailureEmailTo;
	private static AntiSamy antiSamy;

	private static final Logger LOGGER = Logger.getLogger(SalesforceProposalServiceHelper.class);
	
	/**
	 * Sets default values for {@link ProposalOption} and {@link ProposalVersion} when {@link Proposal} is created first time from salesforce to AMPT
	 * @param 	proposal
	 * 			{@link Proposal} object which has to be saved after setting the default and {@link Media_Plan__c} data 
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has data which will be used to set in the {@link Proposal} with Following information :
	 * <ul>
	 *	<li> {@code budget} is for the default option
	 * </ul>
	 */
	public void settingOptionsFirstTime(final Proposal proposal, final Media_Plan__c sfProposal) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("Setting Option and status for the salesforce proposal - " + sfProposal.getId());
		}
		if (proposal != null) {
			proposal.setProposalStatus(ProposalStatus.UNASSIGNED);
			final ProposalOption option = new ProposalOption();
			option.setProposal(proposal);
			option.setDefaultOption(true);
			option.setName(ConstantStrings.OPTION_NAME + 1);
			option.setOptionNo(1);
			option.setBudget(sfProposal.getBudget__c() == null ? 0 : (NumberUtil.round(sfProposal.getBudget__c()*proposal.getConversionRate(), 2)));
			proposal.addNewOption(option);

			final ProposalVersion version = new ProposalVersion();
			version.setProposalVersion(1L);
			version.setProposalOption(option);
			option.addNewVersion(version);
		}
	}

	/**
	 * Validates <code>sfProposal</code> and populates the same to AMPT {@link Media_Plan__c}. 
	 * If everything works well then the proposal will be saved otherwise a mail will go and all the data will be rolled back
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has all the data which has to be validated before saving it to AMPT database 
	 * @return
	 * 			Returns a validated AMPT {@link Media_Plan__c} which has to be saved in AMPT database as {@link Proposal}
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 * @throws ParseException Signals that an error has been reached unexpectedly while parsing.
	 */
	public Proposal populateProposalFromSF(final Media_Plan__c sfProposal) throws ConnectionException {
		final Proposal amptproposal = new Proposal();
		
		final String[] emptyArray = {};
		final String[] campaignObj = StringUtils.isNotBlank(sfProposal.getCampaign_Objective__c()) ? (String[]) sfProposal.getCampaign_Objective__c().split(";") : emptyArray;
		amptproposal.setName(generateProposalName(sfProposal));
		amptproposal.setCriticality(Criticality.findByDisplayName(sfProposal.getPriority__c()));
		amptproposal.setDueDate(DateUtil.mergeDateAndTimeString(sfProposal.getDue_On_Date__c() == null ? null : sfProposal.getDue_On_Date__c().getTime(), 
				StringUtils.isBlank(sfProposal.getDue_On_Time__c()) ? ConstantStrings.EMPTY_STRING : sfProposal.getDue_On_Time__c()));
		amptproposal.setCampaignName(replaceToUnderScore(sfProposal.getName()));
		amptproposal.setAgencyMargin(sfProposal.getAgency_Margin__c());
		amptproposal.setDateRequested(DateUtil.getCurrentDate());
		amptproposal.setCurrency(sfProposal.getProposal_Curency__c());
		amptproposal.setSalesforceID(sfProposal.getId().substring(0, 15));
		amptproposal.setSosSalesCategoryId(sosService.getSOSSalesCategories().get(sfProposal.getSales_Category__c()));
		amptproposal.setCampaignObjectiveSet(getCampaignObjectivesSet(campaignObj, proposalService.getCampaignObjectivesMap()));
		amptproposal.setPriceType(sfProposal.getPrice_Type__c());
		
		Long currencyId = setCurrencyAndValidate(sfProposal.getProposal_Curency__c());
		amptproposal.setCurrencyId(currencyId);
		
		amptproposal.setAccountManager(sfProposal.getOwner() == null ? ConstantStrings.EMPTY_STRING : sfProposal.getOwner().getName());
		amptproposal.setStartDate(sfProposal.getStart_Date__c() == null ? null : sfProposal.getStart_Date__c().getTime());
		amptproposal.setEndDate(sfProposal.getEnd_Date__c() == null ? null : sfProposal.getEnd_Date__c().getTime());
		
			if(currencyId != null){
				Map<String,Double> conversionRateMap = new HashMap<String, Double>();				
				try {
					conversionRateMap = proposalSOSService.getCurrencyConversionRate();
				} catch (ParseException e) {
					LOGGER.error(ConstantStrings.SOS_CONVERSION_RATE_ERROR);
					throw new RuntimeException(ConstantStrings.SOS_CONVERSION_RATE_ERROR, e);
				}
				amptproposal.setConversionRate(conversionRateMap.get(amptproposal.getCurrency()));
				amptproposal.setConversionRate_Euro(conversionRateMap.get("EUR"));
				amptproposal.setConversionRate_Yen(conversionRateMap.get("CNY"));
				
				
			}
		amptproposal.setAttachments(salesForceProposalService.fetchAttachmentsFromSalesforce(sfProposal.getId()));
		return amptproposal;
	}
	
	/**
	 * Validates <code>sfProposal</code> and shoots an email if the <code>sfProposal</code> invalidates
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has all the data which has to be validated
	 * @param 	amptproposal
	 * 			Populated AMPT {@link Proposal} which has all the data from Salesforce {@link Media_Plan__c}
	 * @return
	 * 			Validates <code>sfProposal</code> and shoots an email if the <code>sfProposal</code> invalidates
	 */
	public boolean validateProposalData(final Media_Plan__c sfProposal, final Proposal amptProposal) {
		final List<String> errors = validateSfProposal(sfProposal, amptProposal);
		if (errors != null && !errors.isEmpty()) {
			sendMail(amptProposal, sfProposal, errors);
			return true;
		}
		return false;
	}
	
	/**
	 * Validates <code>sfProposal</code>. Following parameters will be verified : 
	 *  <ul>
	 *    <li> check if proposal with same name exists
	 *    <li> check if proposal name contains any XSS content   
	 *    <li> check if sales category is valid
	 *    <li> check if currency is valid
	 *    <li> check if agency margin is valid
	 *    <li> check if end date is valid
	 *    <li> check if notes are valid
	 *    <li> check if campaign Objectives are valid
	 *    <li> Validates the related {@link Attachment}
	 *  </ul>
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has all the data which has to be validated
	 * @param 	amptproposal
	 * 			Populated AMPT {@link Proposal} which has all the data from Salesforce {@link Media_Plan__c}
	 * @return
	 * 			Returns List of errors if any otherwise return an empty list of String
	 */
	private List<String> validateSfProposal(final Media_Plan__c sfProposal, final Proposal amptproposal){
		final List<String> errors = new ArrayList<String>();
		final String[] emptyArray = {};
		final String[] campaignObj = StringUtils.isNotBlank(sfProposal.getCampaign_Objective__c()) ? (String[]) sfProposal.getCampaign_Objective__c().split(";") : emptyArray;
		final String noteDescription = StringUtils.isBlank(sfProposal.getNotes__c()) ? ConstantStrings.EMPTY_STRING : sfProposal.getNotes__c();
		final int count = proposalService.proposalCountWithSameName(amptproposal.getName(), 0L);
		// Checks if proposal with same name exists
		if (count > 0) {
			errors.add(ConstantStrings.PROPOSAL_NAME_FROM_SF_VALIDATION_ERROR);
		}
		if(performsXSS(amptproposal.getCampaignName()) || containsXSSAttacks(amptproposal.getCampaignName())) {
			errors.add(ConstantStrings.PROPOSAL_NAME_FROM_SF_HTML_TAGS_ERROR);
		}
		// Checks if sales category exits in sos.
		if (amptproposal.getSosSalesCategoryId() == null) {
			errors.add(ConstantStrings.SALES_CATEGORY_FROM_SF_VALIDATION_ERROR + amptproposal.getSosSalesCategoryId());
		}
		// Checks if currency exists in database
		if (amptproposal.getCurrencyId() == null) {
			errors.add(ConstantStrings.CURRENCY_FROM_SF_VALIDATION_ERROR + amptproposal.getCurrency());
		}
		// Checks agency margin validation and range
		if (amptproposal.getAgencyMargin() == null || amptproposal.getAgencyMargin() < 0 || amptproposal.getAgencyMargin() > 100) {
			errors.add(ConstantStrings.AGENCY_MARGIN_RANGE + amptproposal.getAgencyMargin());
		}
		// Checks is ends date is before start date
		if(amptproposal.getEndDate() != null && amptproposal.getStartDate() != null && amptproposal.getEndDate().before(amptproposal.getStartDate())) {
			errors.add(ConstantStrings.END_DATE_IS_GREATER + "StartDate(" + DateUtil.getGuiDateString(amptproposal.getStartDate()) + "), EndDate("
					+ DateUtil.getGuiDateString(amptproposal.getEndDate()) + ConstantStrings.CLOSING_BRACKET);
		}
		// Checks is notes are valid
		errors.addAll(validateNote(noteDescription));
		// Checks is campaign objectives exists 
		for (String objectives : campaignObj) {
			if (proposalService.getCampaignObjectivesMap().get(objectives) == null) {
				errors.add(ConstantStrings.CAMPAIGN_OBJECTIVES_FROM_SF_VALIDATION_ERROR + objectives);
			}
		}
		
		if (amptproposal.getAttachments() != null && !amptproposal.getAttachments().isEmpty()) {
			for (Attachment attachment : amptproposal.getAttachments()) {
				errors.addAll(validateAttachment(attachment));
			}
		}
		return errors;
	}

	/**
	 * Validates <code>sfRevision</code> and shoots an email if the <code>sfRevision</code> invalidates
	 * @param 	sfRevision
	 * 			{@link Media_Plan_Revision__c} object has all the data from salesforce which has to be validated against AMPT {@link Notes}
	 * @param 	attachments
	 * 			{@link Attachment} object has all the attachments from salesforce related to the <code>sfRevision</code> which has to be validated against AMPT {@link Document} and file system's {@link File}
	 * @return
	 * 			Returns {@code true} if, and only if, {@code sfRevision} invalidates and shoots an email
	 */
	public boolean validateRevisionData(final Media_Plan_Revision__c sfRevision, final List<Attachment> attachments) {
		final List<String> errors = validateSfRevision(sfRevision, attachments);
		if (errors != null && !errors.isEmpty()) {
			sendMail(sfRevision, errors, false, salesForceProposalService.getProposalBySalesforceId(sfRevision.getCampaign_Name__r().getId()));
			return true;
		}
		return false;
	}
	
	/**
	 * Validates <code>sfRevision</code> and it's related <code>attachments</code> and return a List of errors if any otherwise return an empty List
	 * @param 	sfRevision
	 * 			{@link Media_Plan_Revision__c} object has all the data from salesforce which has to be validated against AMPT {@link Notes}
	 * @param 	attachments
	 * 			{@link Attachment} object has all the attachments from salesforce related to the <code>sfRevision</code> which has to be validated against AMPT {@link Document} and file system's {@link File}
	 * @return
	 * 			Returns a List of errors if, and only if, {@code sfRevision} invalidates otherwise return an empty List
	 */
	private List<String> validateSfRevision(final Media_Plan_Revision__c sfRevision, final List<Attachment> attachments) {
		final String noteDescription = StringUtils.isBlank(sfRevision.getRevision_Note__c()) ? ConstantStrings.EMPTY_STRING : sfRevision.getRevision_Note__c() ;
		final List<String> errors = validateNote(noteDescription);
		if (attachments != null && !attachments.isEmpty()) {
			for (Attachment attachment : attachments) {
				errors.addAll(validateAttachment(attachment));
			}
		}
		return errors;
	}
	
	/**
	 * Validates <code>attachments</code> and return a List of errors if any otherwise return an empty List. These <code>attachments</code> are validated against AMPT {@link Document}
	 * with following parameters :
	 *  <ul>
	 *    <li> check if <code>attachment</code> name length should not exceed the limit
	 *    <li> check if <code>attachment</code> has valid extension which AMPT Supports  
	 *    <li> check if <code>attachment</code> has valid size which AMPT supports
	 *  </ul>
	 * @param 	attachment
	 * 			{@link Attachment} object has all the attachment from salesforce which has to be validated against AMPT {@link Document}
	 * @return
	 * 			Returns a List of errors if, and only if, {@code attachment} invalidates otherwise return an empty List
	 */
	private List<String> validateAttachment(final Attachment attachment) {
		final List<String> errors = new ArrayList<String>();
		if (attachment.getName().length() > 100) {
			errors.add("File name length exceeds the limit of 100 characters : "
					+ attachment.getName());
		}
		if (!DocumentTypeEnum.isAllowedDocumentType(attachment.getName())) {
			errors.add("AMPT supports - doc, docx, xls, xlsx, ppt, pptx, pdf, jpg, jpeg, png, "
					+ "gif and bmp file extentions, Please check your file extention : "
					+ attachment.getName());
		}
		final float attachmentSize = (float) NumberUtil.round(((float)attachment.getBodyLength() / MEGABYTE), 2);
		if (attachmentSize > ATTACHMENT_SIZE_LIMIT) {
			errors.add("AMPT supports attachments of size up to 2 MB. The attached file (" + attachment.getName() + 
					") size is " + attachmentSize + "MB please check.");
		}
		return errors;
	}

	/**
	 * Validates <code>noteDescription</code> and return a List of errors if any otherwise return an empty List. These <code>noteDescription</code> is validated against AMPT {@link Notes} with the following parameters :
	 *  <ul>
	 *    <li> check if <code>noteDescription</code> should not contain any XSS content
	 *    <li> check if <code>noteDescription</code> has valid length
	 *  </ul>
	 * @param 	noteDescription
	 * 			{@link Media_Plan_Revision__c} note description that is validated against AMPT {@link Notes} description
	 * @return
	 * 			Returns a List of errors if, and only if, {@code noteDescription} invalidates otherwise return an empty List
	 */
	private List<String> validateNote(final String noteDescription){
		final List<String> errors = new ArrayList<String>();
		if (containsXSSAttacks(noteDescription)) {
			errors.add(ConstantStrings.NOTE_HTML_TAGS_ERROR);
		}
		if(noteDescription.length() > 3072){
			errors.add(ConstantStrings.NOTE_LENGTH_ERROR + noteDescription.length());
		}
		return errors; 
	}
	
	/**
	 * Returns {@code true} if the {@code string} is not compliance with the given regular expression
	 * @param 	string
	 * 			To check if given {@code string} is compliance with the given regular expression or not
	 * @return
	 * 			Returns {@code true} if the {@code string} is not compliance with the given regular expression
	 */
	private static boolean performsXSS(String string) {
		return Pattern.compile("[^A-Za-z0-9.$'&@;,/ _()-]+").matcher(string).find();
	}
		
	/**
	 * Load AntySamy policy file and return instance of AntiSamy
	 * @return	Returns AntySamy instance by loading AntySamy policy file
	 * @throws 	PolicyException  This exception gets thrown when there is a problem validating or parsing the policy file. Any validation errors not caught by the XML validation will be thrown with this exception.
	 */
	private AntiSamy getAntiSamyInstance() throws PolicyException {
		if (antiSamy == null) {
			final URL policyFileUrl = Thread.currentThread().getContextClassLoader().getResource("antisamy_config.xml");
			antiSamy = new AntiSamy(Policy.getInstance(policyFileUrl));
		}
		return antiSamy;
	}

	/**
	 * This method is used to check cross-side scripting. Which checks if the {@code content} contains any HTML tags or JavaScript tags or not
	 * @param 	content
	 * 			The {@code content} which is to be checked against AntiSamy policy
	 * @return
	 * 			Returns {@code true} if the {@code content} is not compliance with the AntiSamy policy
	 */
	private boolean containsXSSAttacks(final String content) {
		try {
			final CleanResults result = getAntiSamyInstance().scan(content);
			return result.getErrorMessages().isEmpty() ? false : true;
		} catch (ScanException e) {
			LOGGER.warn(e.getMessage());
		} catch (PolicyException e) {
			LOGGER.warn(e.getMessage());
		}
		return true;
	}

	/**
	 * Sends email while validating the salesforce {@link Media_Plan__c} against the validation parameters that are implied by the AMPT {@link Proposal}
	 * @param 	amptproposal
	 * 			Populated AMPT {@link Proposal} which has all the data populated from Salesforce {@code sfProposal}
	 * @param 	sfProposal
	 * 			Salesforce {@link Media_Plan__c} object which is going to be saved in the AMPT database
	 * @param 	errors
	 * 			List of errors which came while validating the salesforce {@link Media_Plan__c} against AMPT {@link Proposal}
	 */
	private void sendMail(final Proposal amptproposal, final Media_Plan__c sfProposal, final List<String> errors) {
		try {
			final SalesforceProposalEmailFlag sfPropEmailFlag = salesForceProposalService.getEmailFlagBySalesforceId(sfProposal.getId());
			if (sfPropEmailFlag == null || sfPropEmailFlag.getSubmmitedDate().compareTo(sfProposal.getSubmitted_Date__c().getTime()) != 0) {
				final String subject = createSubjectForEmail(amptproposal == null ? sfProposal.getName() : amptproposal.getName(), false);
				final StringBuffer message = new StringBuffer(100);
				for (String string : errors) {
					message.append(ConstantStrings.NEW_AND_TAB_LINE).append(string);
				}
				final MimeMessage mailMessage = mailUtil.setMessageInfo(getMailInfo(sfProposal.getCreatedBy().getEmail(), subject, salesforceIntegrationEmailFrom,
						ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD + ConstantStrings.NEW_AND_TAB_LINE + ConstantStrings.NEW_LINE + message.toString()), null);
				mailUtil.sendMail(mailMessage);
				
				//Update flag on Database 
				saveOrUpdateEmailFlag(sfProposal.getId(), sfProposal.getSubmitted_Date__c().getTime());
			}
		} catch (MailException e) {
			throw new BusinessException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE, new CustomBusinessError(), e);
		}
	}

	/**
	 * Sends email while validating the salesforce {@link Media_Plan_Revision__c} against the validation parameters that are implied by the AMPT {@link Notes}
	 * @param 	amptproposal
	 * 			Populated AMPT {@link Proposal} which has all the data populated from Salesforce {@link Media_Plan__c}
	 * @param 	sfRevision
	 * 			Salesforce {@link Media_Plan_Revision__c} object which is going to be saved in the AMPT database
	 * @param 	errors
	 * 			List of errors which came while validating the salesforce {@link Media_Plan_Revision__c} against AMPT {@link Notes}
	 */
	public void sendMail(final Media_Plan_Revision__c sfRevision, final List<String> errors, final boolean isProposalStatusSold, final Proposal proposal) {
		try {
			final SalesforceProposalEmailFlag sfPropEmailFlag = salesForceProposalService.getEmailFlagBySalesforceId(sfRevision.getId());
			if (sfPropEmailFlag == null || sfPropEmailFlag.getSubmmitedDate().compareTo(sfRevision.getLastModifiedDate().getTime()) != 0) {
				final StringBuffer message = new StringBuffer(100);
				for (String string : errors) {
					message.append(ConstantStrings.NEW_AND_TAB_LINE).append(string);
				}
				mailUtil.sendMail(mailUtil.setMessageInfo(getMailInfo(sfRevision.getSubmitter_Email__c() , createSubjectForEmail(proposal != null ? proposal.getName() : "", isProposalStatusSold), 
						salesforceIntegrationEmailFrom, (isProposalStatusSold ? ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD_SOLD : ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD) + 
						ConstantStrings.NEW_AND_TAB_LINE + ConstantStrings.NEW_LINE + message.toString()), null));
				saveOrUpdateEmailFlag(sfRevision.getId(), sfRevision.getLastModifiedDate().getTime());
			}
		} catch (MailException e) {
			throw new BusinessException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE, new CustomBusinessError(), e);
		}
	}
	
	/**
	 * Sends mail if any fatal/unexpected error comes while communicating with Salesforce from AMPT or vice-versa
	 * @param 	errors
	 * 			List of errors which came while communicating with Salesforce from AMPT or vice-versa
	 * @param 	file
	 * 			This is the temporary file which gets attached while sending the mail whenever a fatal/unexpected error comes while communicating with the Salesforce or vice-versa 
	 * @param 	isError
	 * 			To see if its a fatal error or unexpected error
	 */
	public void sendMail(final String errors, final File file, boolean isError) {
		String emailSubject = ConstantStrings.SALESFORCE_JOB_FAILED_EMAIL_SUBJECT;
		String emailBody = ConstantStrings.SALESFORCE_CONFLUENCE_LINK + ConstantStrings.NEW_AND_TAB_LINE + ConstantStrings.NEW_LINE + ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD;
		if (isError && file != null) {
			emailSubject = ConstantStrings.SALESFORCE_GENERIC_EXCEPTION_EMAIL_SUBJECT;
			emailBody = ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD;
		} if (file == null) {
			 emailSubject = ConstantStrings.SALESFORCE_JOB_UP_EMAIL_SUBJECT;
			 emailBody = ConstantStrings.SALESFORCE_EMAIL_BODY_STANDARD_UP;
		}
		mailUtil.sendMail(mailUtil.setMessageInfo(getMailInfo(salesforceConnectionFailureEmailTo, emailSubject, salesforceIntegrationEmailFrom, emailBody + ConstantStrings.NEW_AND_TAB_LINE + ConstantStrings.NEW_LINE + errors), file));
	}
	
	/**
	 * Returns the subject of the email while sending the mail while validating the Salesforce {@link Media_Plan__c} or {@link Media_Plan_Revision__c}
	 * @param 	proposalName
	 * 			The Proposal Name which has be appended in the subject while sending the mail while validating the Salesforce {@link Media_Plan__c} or {@link Media_Plan_Revision__c}
	 * @param 	isProposalStatusSold
	 * 			Flag to change the subject if the Proposal has been mark sold or has been mark Expired and still having the sales_order id
	 * @return
	 * 			Returns the subject for the email while sending the mail while validating the Salesforce {@link Media_Plan__c} or {@link Media_Plan_Revision__c}
	 */
	private String createSubjectForEmail(final String proposalName, final boolean isProposalStatusSold) {
		final StringBuffer subject = new StringBuffer(isProposalStatusSold ? ConstantStrings.SALESFORCE_PROPOSAL_VALIDATION_SOLD : ConstantStrings.SALESFORCE_PROPOSAL_VALIDATION);
		if (StringUtils.isNotBlank(proposalName)) {
			subject.append(" Proposal ( ").append(proposalName).append(" )");
		}
		return subject.toString();
	}

	/**
	 * Saves/Updates the {@link SalesforceProposalEmailFlag} with the following information
	 * <ul>
	 *	<li> {@code submmitedDate} is the last submitted date when the {@link Media_Plan__c} was saved/updated from salesforce
	 *	<li> {@code emailFlag} is a flag which checks if the mail was sent or not
	 * </ul>
	 * @param 	salesforceId
	 * 			{@link Media_Plan__c} object's id which has relation to {@link SalesforceProposalEmailFlag} table in AMPT database
	 * @param 	submittedDate
	 * 			This is the date when {@link Media_Plan__c} was submitted to AMPT. This date will be updated every-time when user submit the {@link Media_Plan__c} to AMPT 
	 */
	private void saveOrUpdateEmailFlag(final String salesforceId, final Date submittedDate) {
		SalesforceProposalEmailFlag sfPropEmailFlag = salesForceProposalService.getEmailFlagBySalesforceId(salesforceId);
		if(sfPropEmailFlag == null) {
			sfPropEmailFlag = new SalesforceProposalEmailFlag();
			sfPropEmailFlag.setSalesforceId(salesforceId);
		}
		sfPropEmailFlag.setSubmmitedDate(submittedDate);
		sfPropEmailFlag.setEmailFlag("Y");
		salesForceProposalService.saveOrUpdateEmailFlag(sfPropEmailFlag);
	}

	/**
	 * Returns default note {@link Notes} for the {@link Proposal} when it is created first time from the salesforce
	 * @param 	proposal
	 * 			{@link Proposal} which is going to be saved for the first time in AMPT database
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} has all the information which will be set in {@link Notes} object
	 * @return
	 * 			Returns {@link Notes} which will be saved for the first time when the {@link Media_Plan__c} will be created in AMPT as {@link Proposal}
	 */
	public Notes getDefaultNote(final Proposal proposal, final Media_Plan__c sfProposal) {
		final String noteDescription = (StringUtils.isNotBlank(sfProposal.getNotes__c()) ? sfProposal.getNotes__c() : ConstantStrings.EMPTY_STRING);
		final StringBuffer noteString = new StringBuffer(200);
		noteString.append("Advertiser - ")
			.append(sfProposal.getAdvertiser_Name__r() == null ? ConstantStrings.EMPTY_STRING : sfProposal.getAdvertiser_Name__r().getName())
			.append(", Agency - ").append(sfProposal.getAgency__r() == null ? ConstantStrings.EMPTY_STRING : sfProposal.getAgency__r().getName())
			.append("</br>Notes - ").append(noteDescription);
		
		final Notes notes = new Notes();
		notes.setDescription(noteString.toString());
		notes.setProposalId(proposal.getId());
		notes.setCreatedByUserName(sfProposal.getCreatedBy().getName());
		notes.setRole(StringUtils.isNotBlank(sfProposal.getCreatedBy().getUserRole().getName()) ? 
				sfProposal.getCreatedBy().getUserRole().getName() : ConstantStrings.EMPTY_STRING);
		return notes;
	}

	/**
	 * Returns {@link Notes} for the {@link Proposal} when a {@link Media_Plan_Revision__c} is created every-time from salesforce and pushed to AMPT
	 * @param 	sfPropRevision
	 * 			{@link Media_Plan_Revision__c} has all the information which will be set in the {@link Notes} object
	 * @param 	proposal
	 * 			The {@link Notes} will be saved in {@code proposal} object which has one-to-many relation 
	 * @return
	 * 			Returns {@link Notes} for the {@link Proposal} when a {@link Media_Plan_Revision__c} is created.
	 * @throws 	BusinessException
	 * 			This Exception is thrown when there is any {@code NytRuntimeException} exception occurs
	 */
	public Notes getRevisionNote(final Media_Plan_Revision__c sfPropRevision, final Proposal proposal) throws BusinessException {
		if (sfPropRevision != null && proposal != null) {
			StringBuilder noteDescription = new StringBuilder();
			final Notes note = new Notes();
			if(ConstantStrings.SALES_ORDER.equals(sfPropRevision.getRevision_Type__c())) {
				noteDescription.append(createNoteForIORequest(sfPropRevision));
			}
			noteDescription.append(StringUtils.isBlank(sfPropRevision.getRevision_Note__c()) ? ConstantStrings.EMPTY_STRING : sfPropRevision.getRevision_Note__c());
			note.setDescription(noteDescription.toString());
			note.setSfProposalRevisionId(sfPropRevision.getId());
			note.setProposalId(proposal.getId());
			note.setCreatedByUserName(sfPropRevision.getRevision_Submitter__c());
			note.setRole(StringUtils.isNotBlank(sfPropRevision.getCreatedBy().getUserRole().getName()) 
					? sfPropRevision.getCreatedBy().getUserRole().getName() : ConstantStrings.EMPTY_STRING);
			return note;
		}
		return null;
	}

	private String createNoteForIORequest(final Media_Plan_Revision__c sfPropRevision) {
		StringBuilder noteDescription = new StringBuilder();
		noteDescription.append("IO Request").append(ConstantStrings.NEW_LINE).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Billing Contact's Name:</b> ").append(null == sfPropRevision.getBilling_Contact__r() ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getBilling_Contact__r().getFull_Name__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Billing Contact's Email:</b> ").append(StringUtils.isBlank(sfPropRevision.getBilling_Contact_Email__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getBilling_Contact_Email__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Billing Contact's Phone:</b> ").append(StringUtils.isBlank(sfPropRevision.getBilling_Contact_Phone__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getBilling_Contact_Phone__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Billing Address:</b> ").append(StringUtils.isBlank(sfPropRevision.getAdvertisers_Billing_Address__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getAdvertisers_Billing_Address__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Media Contact's Name:</b> ").append(null == sfPropRevision.getMedia_Contact__r() ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getMedia_Contact__r().getFull_Name__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Media Contact's Email:</b> ").append(StringUtils.isBlank(sfPropRevision.getMedia_Contact_Email__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getMedia_Contact_Email__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Media Contact's Phone:</b> ").append(StringUtils.isBlank(sfPropRevision.getMedia_Contact_Phone__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getMedia_Contact_Phone__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Client PO Number:</b> ").append(StringUtils.isBlank(sfPropRevision.getClient_PO_Number__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getClient_PO_Number__c()).append(ConstantStrings.NEW_LINE);
		noteDescription.append("<b>Geo Region:</b> ").append(StringUtils.isBlank(sfPropRevision.getGeo_Region__c()) ? 
				ConstantStrings.EMPTY_STRING : sfPropRevision.getGeo_Region__c()).append(ConstantStrings.NEW_LINE).append(ConstantStrings.NEW_LINE);
		return noteDescription.toString();
	}
	
	/**
	 * Updates the {@link Media_Plan_Revision__c} info with following attributes from AMPT to salesforce when {@link Media_Plan_Revision__c} has been created in the AMPT database :
	 * <ul>
	 *	<li> {@code createdDate} is the cretaed date when {@link Media_Plan_Revision__c} was created in AMPT database 
	 * </ul>
	 * @param 	revisionNote
	 * 			{@link Notes} object which has all the information which we set in the salesforce {@link Media_Plan_Revision__c} hence update salesforce from AMPT
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	public void setRevisionInfoForUpdateToSF(final Notes revisionNote) throws ConnectionException {
		final Media_Plan_Revision__c sfPropRevision = new Media_Plan_Revision__c();
		sfPropRevision.setId(revisionNote.getSfProposalRevisionId());
		sfPropRevision.setCreated_In_AMPT__c(DateUtil.dateTypeToCalendar(revisionNote.getCreatedDate()));
		salesForceProposalService.updateProposalRevisionToSalesforce(sfPropRevision);
	}

	/**
	 * Returns a temp {@link File} which has all the fatal Exceptions written occurred while communication with salesforce or vice-versa 
	 * @param 	exception
	 * 			{@link Exception} object which has all the Exceptions which occurred while communication with salesforce or vice-versa  
	 * @return
	 * 		 	Returns a temp {@link File} which has all the fatal Exceptions written occurred while communication with salesforce or vice-versa 
	 */
	public File tempFileForEmailAttachment(final Exception exception) {
		File file = null;
		PrintWriter printWriter = null;
		try {
			file = File.createTempFile("log", ".txt");
			printWriter = new PrintWriter(file);
		} catch (Exception e) {
			LOGGER.error(ConstantStrings.TEMP_FILE_CREATION_ERROR);
		}
		exception.printStackTrace(printWriter);
		printWriter.close();
		return file;
	}
	
	/**
	 * Returns {@code true} if, and only if, the Proposal Status is {@code PROPOSED} or {@code INPROGRESS} or {@code UNASSIGNED} or {@code EXPIRED}
	 * @param 	proposal
	 * 			{@link Proposal} object for which we have to check the current status
	 * @return Returns {@code true} if, and only if, the Proposal Status is following :
	 * 			<ul>
	 *             <li> {@code PROPOSED}
	 *             <li> {@code INPROGRESS}
	 *             <li> {@code UNASSIGNED}
	 *             <li> {@code EXPIRED}
	 *          </ul>
	 */
	public boolean proposalStatusExceptReadOnly(final Proposal proposal) {
		return (ProposalStatus.PROPOSED.name()).equals(proposal.getProposalStatus().name()) || (ProposalStatus.INPROGRESS.name()).equals(proposal.getProposalStatus().name())
				|| (ProposalStatus.UNASSIGNED.name()).equals(proposal.getProposalStatus().name()) || (ProposalStatus.EXPIRED.name()).equals(proposal.getProposalStatus().name());
	}

	/**
	 * Returns the full Proposal name which will be inserted in the AMPT database against {@link Proposal}'s  {@code name}
	 * @param 	sfProposal
	 * 			Salesforce {@link Media_Plan__c} which has all the information which will be used in generating the Proposal name
	 * @return
	 * 			Returns the full Proposal name when the {@link Media_Plan__c} is created first time in AMPT database as {@link Proposal}
	 */
	private String generateProposalName(final Media_Plan__c sfProposal) {
		final StringBuffer proposalname = new StringBuffer(100);
		final Date dueOnDate = sfProposal.getDue_On_Date__c() == null ? null : sfProposal.getDue_On_Date__c().getTime();
		final String dueTime = StringUtils.isBlank(sfProposal.getDue_On_Time__c()) ? ConstantStrings.EMPTY_STRING : sfProposal.getDue_On_Time__c();
		final String dueDate = DateUtil.getGuiDateString(DateUtil.mergeDateAndTimeString(dueOnDate, dueTime));
		
		if (StringUtils.isNotBlank(sfProposal.getName()) && StringUtils.isNotBlank(dueDate)) {
			if (StringUtils.isNotBlank(sfProposal.getSales_Category__c())) {
				proposalname.append(sfProposal.getSales_Category__c()).append(ConstantStrings.UNDER_SCORE).append(sfProposal.getName()).append(ConstantStrings.UNDER_SCORE).append(dueDate);
			}else{
				proposalname.append(sfProposal.getName()).append(ConstantStrings.UNDER_SCORE).append(dueDate);
			}
		}
		return replaceToUnderScore(proposalname.toString());
	}
	

	/**
	 * Returns the set of {@link CampaignObjective} which will be used to populate {@link Media_Plan__c} into AMPT {@link Proposal} while creating the proposal fist time from saleforce to AMPT
	 * @param 	campaignObj
	 * 			String array of all the campaign objective from which were selected while creating {@link Media_Plan__c} in salesforce
	 * @param 	campList
	 * 			All the campaign objective in AMPT database
	 * @return
	 * 			 Returns the set of {@link CampaignObjective} which will be used to populate {@link Media_Plan__c} into AMPT {@link Proposal}
	 */
	private Set<CampaignObjective> getCampaignObjectivesSet(final String[] campaignObj, final Map<String, Long> campList) {
		final Set<CampaignObjective> campObjectives = new HashSet<CampaignObjective>(campaignObj.length);
		for (String objectives : campaignObj) {
			final CampaignObjective campaignObjective = new CampaignObjective();
			campaignObjective.setCmpObjText(objectives);
			if (campList.get(objectives) != null) {
				campaignObjective.setCmpObjId(campList.get(objectives));
			}
			campObjectives.add(campaignObjective);
		}
		return campObjectives;
	}

	/**
	 * Returns the Long id of selected {@code selectedCurr} if it's available in the database otherwise return null
	 * @param 	selectedCurr
	 * 			Selected currency in salesforce while creating {@link Media_Plan__c} in salesforce and has been pushed to AMPT.
	 * @return
	 * 			Returns Id if, and only if, the selected {@code selectedCurr} exist in the SOS database otherwise return null
	 */
	private Long setCurrencyAndValidate(final String selectedCurr) {
		final Map<Long, String> currMap = proposalSOSService.getCurrencies();
		for (Long currId : currMap.keySet()) {
			if (currMap.get(currId).equals(selectedCurr)) {
				return currId;
			}
		}
		return null;
	}
	
	/**
	 * Returns the mail properties which will be used while sending the email
	 * @param 	mailTo
	 * 			To whom mail should be send
	 * @param 	subject
	 * 			Subject of the mail
	 * @param 	mailFrom
	 * 			From whom the mail will come
	 * @param 	textMsg
	 * 			Boby of the email
	 * @return
	 * 			Returns the mail properties which will be used while sending the email
	 */
	private Map<String, String> getMailInfo(final String mailTo, final String subject, final String mailFrom, final String textMsg) {
		final Map<String, String> mailProps = new HashMap<String, String>();
		mailProps.put("mailTo", mailTo);
		mailProps.put("subject", subject);
		mailProps.put("mailFrom", mailFrom);
		mailProps.put("textMsg", textMsg);
		mailProps.put("cc", emailIdOfUserBasedOnRole());
		return mailProps;
	}

	/**
	 * Returns the e-mail id of following roles which we keep in cc while sending email :
	 * 			<ul>
	 *             <li> {@code ADMIN}
	 *             <li> {@code MEDIA PLANNER}
	 *          </ul>
	 * @return
	 * Returns the e-mail ids of {@code ADMIN} and {@code MEDIA PLANNER} roles
	 */
	private String emailIdOfUserBasedOnRole() {
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(new String[] {SecurityUtil.ADMIN, SecurityUtil.MEDIA_PLANNER});
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (mail.length() > 0) {
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
		}
		return mail.toString();
	}
	
	/**
	 * Return underscored campaign name. Usually while creating the campaign name from salesforce there might be some special characters which we don't support in AMPT. Hence we replaced those special characters to underscores 
	 * @param 	string
	 * 			Campaign name which we want to underscored where needed
	 * @return
	 * 		Return underscored campaign name while creating {@link Proposal} from salesforce {@link Media_Plan__c} for the first time
	 */
	private String replaceToUnderScore (final String string) {
		return Pattern.compile("[^A-Za-z0-9.$'&@;,/ _()-]").matcher(string).replaceAll("_");
	}
	
	public void setMailUtil(final MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}

	public void setProposalSOSService(final IProposalSOSService propSOSService) {
		this.proposalSOSService = propSOSService;
	}

	public void setSalesForceProposalService(final ISalesForceProposalService sfPropService) {
		this.salesForceProposalService = sfPropService;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setSalesforceIntegrationEmailFrom(final String emailFrom) {
		this.salesforceIntegrationEmailFrom = emailFrom;
	}

	public void setSalesforceConnectionFailureEmailTo(String salesforceConnectionFailureEmailTo) {
		this.salesforceConnectionFailureEmailTo = salesforceConnectionFailureEmailTo;
	}
}