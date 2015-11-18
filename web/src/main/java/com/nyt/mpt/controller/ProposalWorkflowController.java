/**
 *
 */
package com.nyt.mpt.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.AudienceTarget;
import com.nyt.mpt.domain.Audit;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.domain.PricingCalculatorSummary;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.RateProfileSummary;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.Product;
import com.nyt.mpt.domain.sos.ProductPlacement;
import com.nyt.mpt.domain.sos.SalesTarget;
import com.nyt.mpt.form.AuditForm;
import com.nyt.mpt.form.AvailData;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.NotesForm;
import com.nyt.mpt.form.ProposalForm;
import com.nyt.mpt.form.SalesTargetAmptForm;
import com.nyt.mpt.form.SearchProposalForm;
import com.nyt.mpt.service.IAuditService;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IEmailScheduleService;
import com.nyt.mpt.service.IPackageService;
import com.nyt.mpt.service.IPricingCalculator;
import com.nyt.mpt.service.IPricingStatusCalculatorService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISalesForceProposalService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ISosIntegrationService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.service.impl.ProposalUtilService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.KeyValuePairPojo;
import com.nyt.mpt.util.LineItemUtil;
import com.nyt.mpt.util.MailUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.OptionNameComparator;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.SalesTargeDisNameComparator;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.TemplateGenerator;
import com.nyt.mpt.util.TemplateWorkBook;
import com.nyt.mpt.util.enums.EmailReservationStatus;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.LineItemExceptionEnum;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.LineItemViewableCriteriaEnum;
import com.nyt.mpt.util.enums.PricingStatus;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.security.SecurityUtil;
import com.nyt.mpt.validator.PackageLineItemsFormValidator;
import com.nyt.mpt.validator.ProposalValidator;
import com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.ws.ConnectionException;

/**
 * Controller class for handling proposal and their in page action.
 *
 * @author kapil.malhotra
 *
 */
@Controller
@RequestMapping("/proposalWorkflow/*")
public class ProposalWorkflowController extends AbstractBaseController {

	private static final String ZERO = "0.00";

	private static final String PROPOSAL = "Proposal";

	private static final String ZIP_CODES = "Zip Codes";

	private static final String BEHAVIOURAL = "Behavioral";

	private IProposalService proposalService;

	private ProposalHelper proposalHelper;

	private LineItemUtil lineItemUtil;

	private TargetJsonConverter targetJsonConverter;

	private IProposalSOSService proposalSOSService;

	private IPackageService packageService;

	private IPricingCalculator pricingCalculator;

	private IAuditService auditService;

	private MailUtil mailUtil;

	private IUserService userService;

	private ISosIntegrationService sosIntegrationService;

	private ISalesTargetService salesTargetService;

	private IProductService productService;

	private TemplateGenerator templateGenerator;

	private ITemplateService templateService;

	private IPricingStatusCalculatorService pricingStatusCalculatorService;

	private ICalendarReservationService reservationService;
	
	private ISalesForceProposalService salesForceProposalService;
	
	private ProposalUtilService proposalUtilService;
	
	private static final Logger LOGGER = Logger.getLogger(ProposalWorkflowController.class);

	private String emailFrom;

	private IEmailScheduleService emailScheduleService;

	/**
	 * This method is used for loading the data in the options grid for a given {@link Proposal}.
	 * @param tblGrid 
	 * @param proposalId - The id of the proposal whose options grid data has to be populated.
	 * @return It returns an object of ModelAndView with the option's grid data associated to it, as its attribute.
	 */
	@RequestMapping("/loadOptionGridData")
	public ModelAndView loadOptionGridData(@ModelAttribute final TableGrid<ProposalForm> tblGrid, @RequestParam("proposalId") final Long proposalId){
		final ModelAndView view = new ModelAndView(ConstantStrings.GRID_DATA);
		this.setOptionDataToGrid(tblGrid, proposalId);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Fetches the options data of the given {@link Proposal} and sets the option data being fetched into the {@link TableGrid} object passed as an argument. 
	 * @param tblGrid
	 * @param proposalId - The id of the proposal whose options data has to be fetched and populated into the TableGrid object.
	 */
	private void setOptionDataToGrid(final TableGrid<ProposalForm> tblGrid, final Long proposalId) {
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalId);
		Date startDate = null;
		Date endDate = null;
		int maxOption = 0;
		for (ProposalOption option : optionLst) {
			if (maxOption < option.getOptionNo()) {
				maxOption = option.getOptionNo();
			}
		}
		final String maxOptionNo = String.valueOf(maxOption);
		final List<ProposalForm> proposalFormLst = new ArrayList<ProposalForm>(optionLst.size());
		for (ProposalOption option : optionLst) {
			startDate = null;
			endDate = null;
			Boolean isLineItemSetViewable = false;
			final ProposalForm proposalForm = new ProposalForm();
			proposalForm.setDefaultOption(Boolean.toString(option.isDefaultOption()));
			proposalForm.setOptionId(option.getId());
			proposalForm.setOptionName(option.getName());
			proposalForm.setProposalVersion(String.valueOf(option.getLatestVersion().getProposalVersion()));
			proposalForm.setBudget(option.getBudget() == null ? ZERO : NumberUtil.formatDouble(option.getBudget(), true));
			proposalForm.setNetCpm(option.getLatestVersion().getEffectiveCpm() == null ? ZERO : NumberUtil.formatDouble(option.getLatestVersion().getEffectiveCpm(), true));
			proposalForm.setOfferedBudget(option.getLatestVersion().getOfferedBudget() == null ? ZERO : NumberUtil.formatDouble(option.getLatestVersion().getOfferedBudget(), true));
			//proposalForm.setNetImpressions(option.getLatestVersion().getImpressions() == null ? ZERO : NumberUtil.formatLong(option.getLatestVersion().getImpressions(), true));
			for (LineItem lineItem : option.getLatestVersion().getProposalLineItemSet()) {
				if (startDate == null || (lineItem.getStartDate() != null && startDate.after(lineItem.getStartDate()))) {
					startDate = lineItem.getStartDate();
				}
				if (endDate == null || (lineItem.getEndDate() != null && endDate.before(lineItem.getEndDate()))) {
					endDate = lineItem.getEndDate();
				}
				if(lineItem.getViewabilityLevel() != 0 && !isLineItemSetViewable){
					isLineItemSetViewable = true;
				}
			}
			proposalForm.setStartDate((startDate == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(startDate));
			proposalForm.setEndDate((endDate == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(endDate));
			proposalForm.setMaxOptionNo(maxOptionNo);
			proposalForm.setThresholdLimit(option.getThresholdLimit() == null ? ConstantStrings.EMPTY_STRING : NumberUtil.formatDouble(option.getThresholdLimit(), true));
			proposalForm.setLastPricingReviewedDate(option.getLastReviewedDate() == null ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateTimeString(option.getLastReviewedDate()));
			proposalForm.setIsOptionViewable(isLineItemSetViewable ? "Yes" : "No");
			proposalFormLst.add(proposalForm);
		}
		Collections.sort(proposalFormLst, new OptionNameComparator());
		tblGrid.setGridData(proposalFormLst, proposalFormLst.size());
	}

	/**
	 * Creates a new {@link ProposalOption} if it doesn't exists or update the existing {@link ProposalOption} after validating its data successfully. In case of 
	 * validation failure respective error codes will be returned.
	 * @param response
	 * @param form - The {@link ProposalForm} object containing the option data to be saved.
	 * @return Returns the data of the {@link ProposalOption} being saved as an AJAX response.
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/saveOption")
	public AjaxFormSubmitResponse saveOption(final HttpServletResponse response, final ProposalForm form) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("proposalForm", form);
		final Proposal proposaldb = proposalService.getProposalbyId(form.getId());
		// to assign optionId =-1 when there are already 5 options for particular Proposal
		if (proposaldb.getProposalOptions().size() == 5 && form.getOptionId() == 0) {
			form.setOptionId(-1L);
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, form);
			return ajaxResponse;
		}

		if (proposaldb.getProposalStatus().getDisplayName().equalsIgnoreCase("Under Review") && proposaldb.getDefaultOption().getId() != form.getOptionId()) {
			results.rejectValue("budget", ErrorCodes.OptionNotEditableForProposalUnderReview, "budget", new Object[] {"budget" }, UserHelpCodes.HelpSelectDefaultOption);
			return constructResponse(response, ajaxResponse, results);
		}

		new ProposalValidator().validateBudget(form, results);
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			ProposalOption option = new ProposalOption();
			final Proposal proposal = new Proposal();
			proposal.setId(form.getId());
			option.setBudget(StringUtils.isNotBlank(form.getBudget()) ? NumberUtil.doubleValue(form.getBudget()) : 0);
			option.setId(form.getOptionId());
			option.setProposal(proposal);
			if (form.getOptionId() == 0) {
				option.setOptionNo(Integer.valueOf(form.getMaxOptionNo()) + 1);
				option.setName(ConstantStrings.OPTION_NAME + (Integer.valueOf(form.getMaxOptionNo()) + 1));
				final ProposalVersion proposalVersion = new ProposalVersion();
				proposalVersion.setProposalVersion(1L);
				proposalVersion.setProposalOption(option);
				final Set<ProposalVersion> proposalVersions = new HashSet<ProposalVersion>();
				proposalVersions.add(proposalVersion);
				option.setProposalVersions(proposalVersions);
			}
			option = proposalService.saveOption(form.getId(), option);
			form.setOptionId(option.getId());
			form.setOptionName(option.getName());
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, form);
			return ajaxResponse;
		}
	}

	/**
	 * Soft deletes the data of a {@link ProposalOption} with the given ID.
	 * @param optionId - Id of the {@link ProposalOption} whose data has to be deleted. 
	 * @param proposalId - Id of the {@link Proposal} whose option data has to be deleted.
	 * @return Returns the Id of the {@link ProposalOption} being deleted as an AJAX response.
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/deleteOption")
	public AjaxFormSubmitResponse deleteOption(@RequestParam("optionId") Long optionId, @RequestParam("proposalId") Long proposalId) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		ProposalOption option = proposalService.getOptionbyId(optionId);
		// We set optionId as -1 so as to identify in basic-info.js that this cannot to be deleted
		if (option.isDefaultOption()) {
			optionId = -1L;
		} else {
			option.setActive(false);
		}
		option = proposalService.saveOption(proposalId, option);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, optionId);
		return ajaxResponse;
	}

	/**
	 * Marks a given {@link ProposalOption} as a default option of its proposal.
	 * @param form
	 * @return Returns the Id of the {@link ProposalOption} being marked as default option, as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/markOptionDefault")
	public AjaxFormSubmitResponse markOptionDefault(final ProposalForm form) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		proposalService.updateOptionAsDefault(form.getId(), form.getOptionId());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, form.getOptionId());
		return ajaxResponse;
	}

	/**
	 * Updates the status of the given {@link Proposal} as 'Under Review' and marks the {@link ProposalOption} selected by the user, as its default Option. 
	 * Creates a new {@link ProposalVersion} for the option marked as default. Also if there are reservations being created in any other option, other than the 
	 * option being marked as default, will be deleted. If the given {@link Proposal} has been created by the SalesForce, creates an audit entry in {@link Ad_Systems_Workflow_Update__c} 
	 * signifying 'Proposal is submitted for Ad Ops Review'. 
	 * @param 	form 
	 * 			{@link ProposalForm} object containing the information of the {@link Proposal} to be marked as under review and the selected {@link ProposalOption} 
	 * 			to be marked as default.
	 * @return 	Returns the {@code Id} of the {@link ProposalOption} being marked as default option, as an AJAX response.
	 * @throws 	CustomCheckedException 
	 * 			If any error occurs while establishing connection with SalesForce. Error can occur if any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username/password} for SalesForce is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code SalesForce account} has been expired due to any reason
	 *           </ul>
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/markOptionForReview")
	public AjaxFormSubmitResponse markOptionForReview(final HttpServletRequest request, final ProposalForm form) throws CustomCheckedException, ParseException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		proposalService.updateProposalStatus(form.getId(), ProposalStatus.REVIEW, form.getVersion());
		final ProposalOption option = proposalService.getOptionbyId(form.getOptionId());
		if (!option.isDefaultOption()) {
			proposalService.updateOptionAsDefault(form.getId(), form.getOptionId());
		}
		proposalService.createVersion(form.getId(), form.getOptionId(), option.getLatestVersion().getProposalVersion());
		//Find Reservations in Options other than default and delete it
		List<LineItem> lineItemsLst = proposalService.deleteReservationsOfNonDefaultOptions(form.getId());
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for(LineItem lineItem : lineItemsLst){
			if( productClassIdLst.contains(lineItem.getSosProductClass())){
				proposalUtilService.sendMailForHomeResrvtn(lineItem, option.getProposal(), ConstantStrings.DELETED);
			}	
		}
		
		Map<String, String> mailMap = proposalUtilService.sendMailForDeleteReservation(lineItemsLst, proposalHelper.getSalesCategory());
		proposalUtilService.sendMail(mailMap);
		/*Sent mail to Planners when proposal is picked for Ad Ops Review*/
		final List<String> roles = new ArrayList<String>(2);
		final Proposal proposal = proposalService.getProposalbyId(form.getId());
		final StringBuffer subject = new StringBuffer(Constants.WORKFLOW_EMAIL_KEYWORD);
		subject.append(" Proposal '");
		subject.append(proposal.getName());
		subject.append("' (Sales Category: ");
		subject.append(proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()));
		subject.append(") under Ad Ops Review");
		String textMsg = "Proposal Id - " + proposal.getId() + "\t\n";
		textMsg += creatTextMsg(proposal, request);
		roles.add(SecurityUtil.AD_OPS);
		sendMailToUser(proposal, subject.toString(), textMsg, roles);
		if (StringUtils.isNotBlank(proposal.getSalesforceID())) {
			StringBuilder message = new StringBuilder(ConstantStrings.EMPTY_STRING);
			message.append("Proposal submitted for Ad Ops Review");
			auditService.salesforceAuditLog(proposal, message);
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, form.getOptionId());
		return ajaxResponse;
	}

	/**
	 * Creates the copy of the given {@link ProposalOption} only if already 5 options have not been created in the {@link Proposal}.
	 * @param proposalForm - {@link ProposalForm} object containing the data of the proposal and its option to be cloned.
	 * @return Returns the {@link ProposalForm} object populated with the data of the new {@link ProposalOption} being created after cloning as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/cloneOption")
	public AjaxFormSubmitResponse createOptionClone(final ProposalForm proposalForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		LOGGER.info("Option to be cloned Id: " + proposalForm.getOptionId());
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		if (proposal.getProposalOptions().size() < 5) {
			final Long clonnedId = proposalService.createOptionClone(proposalForm.getId(), proposalForm.getOptionId(), Long.valueOf(proposalForm.getProposalVersion()), (Integer.valueOf(proposalForm.getMaxOptionNo()) + 1));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Option clone has been created. New option Id: " + clonnedId);
			}
			pricingStatusCalculatorService.addAddedValueCheck(proposalForm.getId());
			pricingStatusCalculatorService.addThreshHoldCheck(proposalForm.getId());
			proposalForm.setOptionId(clonnedId);
			proposalForm.setOptionName(ConstantStrings.OPTION_NAME + (Integer.valueOf(proposalForm.getMaxOptionNo()) + 1));
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalForm);
		}
		return ajaxResponse;
	}

	/**
	 * Creates a new {@link Proposal} if it doesn't exists or update the existing {@link Proposal} after validating its data successfully. In case of validation 
	 * failure respective error codes will be returned and proposal data will not be saved 
	 * @param 	response
	 * @param 	form
	 * @return 	Returns the {@code Id} of the {@link Proposal} being created or updated along with its version.
	 * @throws 	CustomCheckedException 
	 * 			If any error occurs while establishing connection with SalesForce. Occurs only while updating the due date and sales category of an existing 
	 * 			{@link Proposal} created from SalesForce {@link Media_Plan__c}.
	 */
	@ResponseBody
	@RequestMapping("/saveProposalDetail")
	public AjaxFormSubmitResponse saveProposalData(final HttpServletResponse response, @ModelAttribute("proposalForm") final ProposalForm form) throws CustomCheckedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Proposal with id: " + form.getId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("proposalForm", form);
		new ProposalValidator().validate(form, results);
		
		/* Checking if any proposal with the same name already exists or not */
		final int count = proposalService.proposalCountWithSameName(form.getProposalName(), form.getId());
		if (count > 0) {
			results.rejectValue("proposalName", ErrorCodes.DuplicateProposalName, "proposalName", UserHelpCodes.DuplicateProposalName);
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}

		LOGGER.info("Saving Proposal with id: " + form.getId());
		final Proposal proposal = new Proposal();
		form.populate(proposal);
		if (StringUtils.isNotBlank(form.getAdvertiserName())) {
			proposal.setSosAdvertiserName(proposalHelper.getAdvertiser().get(Long.valueOf(form.getAdvertiserName())));
		}
		final Long returnId = proposalService.saveProposal(form.getId(), proposal);

		final Proposal returnProposal = proposalService.getProposalForClone(returnId);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnId);
		ajaxResponse.getObjectMap().put(Constants.VERSION, returnProposal.getVersion());

		return ajaxResponse;
	}

	/**
	 * Updates the {@Code Assigned to user} of the {@link Proposal} to the given user.
	 * @param 	proposalId - the id of the {@link Proposal} whose assign to user has to be updated.
	 * @param 	userId -  Id of the user to be set as the assigned to user of the given proposal.
	 * @return 	The Id of the {@link Proposal} whose assigned to user has been updated as AJAX response.
	 * @throws 	CustomCheckedException
	 * 			If any error occurs while establishing connection with SalesForce. Occurs only while updating the proposal created from SalesForce {@link Media_Plan__c}.
	 */
	@ResponseBody
	@RequestMapping("/updateAssignToUser")
	public AjaxFormSubmitResponse updateAssignToUser(@RequestParam("proposalId") final Long proposalId, @RequestParam("userId") final Long userId) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final User usr = new User();
		usr.setUserId(userId);
		final long returnId = proposalService.updateAssignToUser(proposalId, usr);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnId);
		return ajaxResponse;
	}

	/**
	 * Fetches all the active {@link AudienceTarget} elements of a given target type.
	 * @param lineItemForm
	 * @return Returns the Map of the {@link AudienceTarget} elements with the key value pair representing its Id and name respectively, as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/getTargetTypeElements")
	public AjaxFormSubmitResponse getTargetTypeElements(final LineItemForm lineItemForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		LOGGER.info("Getting Target Type Element for Target Type ID: " + lineItemForm.getSosTarTypeId());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE,
				proposalHelper.getTargetTypeElement(lineItemForm.getSosTarTypeId()));
		return ajaxResponse;
	}

	/**
	 * Used to copy all the selected package {@link LineItem} into the given {@link Proposal}.
	 * @param searchForm - 
	 * @param copiedLineItemIds - A comma separated string of line items Id's to be copied.
	 * @param lineitemNumbers
	 * @param priceType - {@code Price type} of the {@link Package} whose line items has to be copied.
	 * @return Returns false if the line items are copied successfully else returns true.
	 */
	@ResponseBody
	@RequestMapping("/addCopiedLineItemsToProposal")
	public AjaxFormSubmitResponse addCopiedLineItemsToProposal(final SearchProposalForm searchForm, @RequestParam("copiedLineItemIds") final String copiedLineItemIds,
			@RequestParam("lineitemNumbers") final int lineitemNumbers, @RequestParam("priceType") final String priceType) {
		String poposalPriceType = ConstantStrings.EMPTY_STRING;
		Double propAgencyMargin = 0.0;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding copied Line Items To Proposal. copiedLineItemIds: " + copiedLineItemIds);
		}
		boolean flag = false;
		LOGGER.info("adding copied Line Items To Proposal. copiedLineItemIds: " + copiedLineItemIds);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		if (StringUtils.isNotBlank(copiedLineItemIds)) {
			final String[] lineItem = copiedLineItemIds.split(ConstantStrings.COMMA);
			final Long[] lineItems = new Long[lineItem.length];
			for (int i = 0; i < lineItem.length; i++) {
				lineItems[i] = Long.valueOf(lineItem[i]);
			}

			if ("Net".equals(priceType)) {
				poposalPriceType = "Net";
				propAgencyMargin = 0.0;
			} else {
				Proposal proposalSourceDBValue = proposalService.getProposalbyId(Long.valueOf(searchForm.getProposalID()));
				poposalPriceType = proposalSourceDBValue.getPriceType();
				propAgencyMargin = proposalSourceDBValue.getAgencyMargin();
			}

			flag = proposalService.saveCopiedLineItemsToProposal(Long.valueOf(searchForm.getProposalID()), searchForm.getOptionId(), Long.valueOf(searchForm.getProposalversion()), searchForm
					.getSearchType(), lineItems, searchForm.isPartiallyCopiedUnbreakPackage(), searchForm.isCopiedFromExpired(), lineitemNumbers, poposalPriceType, false, propAgencyMargin);

			if (!flag) {
				proposalService.updateAllLineItemPricingStatus(searchForm.getOptionId());
				proposalService.updateProposalVersionNetImpressionAndCPM(searchForm.getOptionId(), Long.valueOf(searchForm.getProposalversion()));
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, flag);
		return ajaxResponse;
	}

	/**
	 * Creates a new {@link LineItem} if it doesn't exists or update the existing {@link LineItem} after validating its data successfully. In case of validation failure respective 
	 * error codes will be returned. Updates the CPM, Net impressions and Offered budget of the proposal option whose line item is created/updated. Also re-calculates 
	 * the pricing status of all the line items of the {@link ProposalOption} whose {@link LineItem} has been updated/created.
	 * @param response
	 * @param lineItemForm
	 * @return Returns an object of {@link LineItemForm} populated with the data of the {@link LineItem} being saved as an AJAX response.
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/editLineItemsOfProposal")
	public AjaxFormSubmitResponse editLineItemsOfProposal(
			final HttpServletResponse response, final  HttpServletRequest request, @ModelAttribute(ConstantStrings.PROPOSAL_LINE_ITEM_FORM) final LineItemForm lineItemForm) throws CustomCheckedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Editing line Items of proposal. LineItemid: " + lineItemForm.getLineItemID());
		}
		LOGGER.info("Editing line Items of proposal. LineItemid: " + lineItemForm.getLineItemID());
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final LineItem lineItemOld = proposalService.getLineItemById(lineItemForm.getLineItemID());
		if(!lineItemForm.getSorExceededHidden()) {
			if(StringUtils.isNotBlank(lineItemForm.getSosProductId())){
				lineItemForm.setAvailSystemId(productService.getProductById(NumberUtil.longValue(lineItemForm.getSosProductId())).getAvailsSytemId());
			}
			final CustomBindingResult results = new CustomBindingResult("lineItemForm", lineItemForm);
			new PackageLineItemsFormValidator().validate(lineItemForm, results);
			new PackageLineItemsFormValidator().validateReservations(lineItemForm, results);
			if ((lineItemOld != null && PricingStatus.PRICING_APPROVED.equals(lineItemOld.getPricingStatus())) && LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue().equals(lineItemOld.getPriceType())) {
				try {
					if (StringUtils.isNotBlank(lineItemForm.getImpressionTotal()) && lineItemOld.getImpressionTotal() < NumberUtil.longValue(lineItemForm.getImpressionTotal())) {
						results.rejectValue("impressionTotal", ErrorCodes.OfferedImpressionsIncreased, "impressionTotal", new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
					}
				} catch (NumberFormatException e) {
					// Only numeric digits allowed in {Offered Impression}.
				}
			}
			if (lineItemForm.getProductType().equals(LineItemProductTypeEnum.EMAIL.getShortName())) {
				if (StringUtils.isNotBlank(lineItemForm.getStartDate()) && !proposalHelper.isValidStartDate(lineItemForm)) {
					results.rejectValue("startDate", ErrorCodes.LineItemEmailReservationInvalidDate, "startDate", new Object[] {Constants.SEND_DATE},
							UserHelpCodes.LineItemsendDateHelp);
				}
			}
			if (results.hasErrors()) {
				return constructResponse(response, ajaxResponse, results);
			} else if ((LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) || LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType())) && 
					lineItemForm.getReservationExpiryDate() != null && !ConstantStrings.EMPTY_STRING.equals(lineItemForm.getReservationExpiryDate())) {
				final String calculateSOR = calculateSOR(lineItemForm, lineItemForm.getProductType());
				if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) ) {
					if(NumberUtil.doubleValue(lineItemForm.getSor()) > NumberUtil.doubleValue(calculateSOR)) {
						results.rejectValue("sorExceeded", ErrorCodes.sorExceededForReservable, "sorExceeded", new Object[] {"reserved"}, UserHelpCodes.sorExceededForReservable);
						return constructResponse(response, ajaxResponse, results);
					}
				} else if(LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType()) ) {
					if(EmailReservationStatus.RESERVED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.OVERBOOKED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.SOLD.getDisplayName().equals(calculateSOR)) {
						results.rejectValue("sorExceeded", ErrorCodes.sorExceededForEmail, "sorExceeded", new Object[] {calculateSOR}, UserHelpCodes.sorExceededForEmail);
						return constructResponse(response, ajaxResponse, results);
					}
				}
			}
		}
		Long returnId;
		final LineItem proposalLineItem = new LineItem();
		lineItemForm.populate(proposalLineItem);
		lineItemForm.populateSalestargetAssoc(proposalLineItem, salesTargetService.getSalesTargetListByIDs(proposalHelper.getSalesTragetIdsArray(lineItemForm.getSosSalesTargetId())));
		Map<String, Long> productsClassMap = proposalHelper.getProductsClassMap();
		proposalLineItem.setSosProductClass(productsClassMap.get(lineItemForm.getSosProductClassName()));
		boolean isNewLineItem = true;
		if (proposalLineItem.getLineItemID() == 0) {
			returnId = proposalService.saveLineItemsOfProposal(Long.valueOf(lineItemForm.getProposalID()), Long.valueOf(lineItemForm.getOptionId()), proposalLineItem, Long
					.valueOf(lineItemForm.getProposalversion()));
		} else {
			returnId = proposalService.updateLineItemsOfProposal(Long.valueOf(lineItemForm.getProposalID()), proposalLineItem);
			isNewLineItem = false;
		}
		proposalService.updateAllLineItemPricingStatus(Long.valueOf(lineItemForm.getOptionId()));

		proposalService.updateProposalVersionNetImpressionAndCPM(Long.valueOf(lineItemForm.getOptionId()),
				Long.valueOf(lineItemForm.getProposalversion()));

		final LineItem lineItem = proposalService.getLineItemById(returnId);
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if (!isNewLineItem) {
			final List<LineItem> lineItemsLst =  proposalService.getLineItemsList(lineItemForm.getGridLineItemIds());
			final HashMap<Long, String> gridLineItemDataMap = new HashMap<Long, String>();
			if (lineItemsLst != null && lineItemsLst.size() > 0) {
				for (LineItem lineItemObj : lineItemsLst) {
					gridLineItemDataMap.put(lineItemObj.getLineItemID(), lineItemObj.getPricingStatus().getDisplayName());
				}
				ajaxResponse.getObjectMap().put("gridLineItemsPricingData", gridLineItemDataMap);
			}
			// Sending mail for home page reservation creation.
			boolean newReservation = false;
			if (lineItem.getReservation() != null) {
				newReservation = isReservationNew(lineItem, lineItemOld);
			}
			Date oldExpirationDate = null;
			if(lineItemOld.getReservation() != null){
				oldExpirationDate = lineItemOld.getReservation().getExpirationDate();
			}
			if(lineItem.getReservation() != null && productClassIdLst.contains(lineItem.getSosProductClass())){
				if(newReservation || oldExpirationDate == null) {
					sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.CREATED, lineItemForm.getSosProductClassName());
				}
			}
			if((newReservation && oldExpirationDate != null && productClassIdLst.contains(lineItem.getSosProductClass())) || 
					(oldExpirationDate != null && lineItem.getReservation() == null) && productClassIdLst.contains(lineItem.getSosProductClass())){
				sendMailForHomeResrvtn(lineItemOld, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED, lineItemForm.getSosProductClassName());
			}
		}else{
			if(lineItem.getReservation() != null && productClassIdLst.contains(lineItem.getSosProductClass())){
				sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.CREATED, lineItemForm.getSosProductClassName());
			}
		}

		
		proposalHelper.sendMailForReservation(request, lineItemOld, lineItem);
		lineItemForm.populateForm(lineItem);
		if ((LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) || LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType())) && 
				lineItemForm.getReservationExpiryDate() != null && !ConstantStrings.EMPTY_STRING.equals(lineItemForm.getReservationExpiryDate())) {
			Map<String,Object> sorMap = calculateSORWithLineItems(lineItemForm, lineItemForm.getProductType());
			 final String calculateSOR = sorMap.containsKey("SOR") ? (String)sorMap.get("SOR") : "100";
			if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) ) {
				if(NumberUtil.doubleValue(lineItemForm.getSor()) > NumberUtil.doubleValue(calculateSOR)) {					
					proposalHelper.sendMailForOverBooking(lineItem, sorMap);
				}
			}else if(LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType()) ) {
				if(EmailReservationStatus.RESERVED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.OVERBOOKED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.SOLD.getDisplayName().equals(calculateSOR)) {
					proposalHelper.sendMailForOverBooking(lineItem, sorMap);
				}
			}
		}
		
		if (lineItem.getPackageObj() != null) {
			final Package packageObj = packageService.getPackageById(lineItem.getPackageObj().getId());
			lineItemForm.setPackageName(packageObj.getName());
		} else {
			lineItemForm.setPackageName(ConstantStrings.EMPTY_STRING);
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, lineItemForm);
		return ajaxResponse;
	}

	/**
	 * 
	 * @param lineItemForm
	 * @param type
	 * @return
	 */
	private String calculateSOR(final LineItemForm lineItemForm, final String type) {
		double sor = 0.0D;
		final ReservationTO reservationTO = lineItemForm.populateToReservationVo((new ReservationTO()));
		reservationTO.setStartDate(DateUtil.parseToDate(lineItemForm.getStartDate()));
		reservationTO.setEndDate(DateUtil.parseToDate(lineItemForm.getEndDate()));
		ReservationInfo reservationInfo = null; 
		for (ReservationInfo reservationDetail : reservationService.getReservationDetailForCalendar(reservationTO)) {
			if (sor < reservationDetail.getSor()) {
				sor = reservationDetail.getSor();
			}
			reservationInfo = reservationDetail;
		}
		if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(type)) {
			return NumberUtil.formatDouble(sor > 100 ? 0 : 100 - sor, true);
		} else {
			return getReservationStatus(reservationInfo);
		}
	}
	
	/**
	 * 
	 * @param lineItemForm
	 * @param type
	 * @return
	 */
	private Map<String,Object> calculateSORWithLineItems(final LineItemForm lineItemForm, final String type) {
		Map <String,Object> sorMap = new HashMap<String, Object>();
		List<LineItem> lineItemLst = new ArrayList<LineItem>();
		List<OrderLineItem> orderLineItemLst = new ArrayList<OrderLineItem>();
		double sor = 0.0D;
		final ReservationTO reservationTO = lineItemForm.populateToReservationVo((new ReservationTO()));
		reservationTO.setStartDate(DateUtil.parseToDate(lineItemForm.getStartDate()));
		reservationTO.setEndDate(DateUtil.parseToDate(lineItemForm.getEndDate()));
		ReservationInfo reservationInfo = null; 
		for (ReservationInfo reservationDetail : reservationService.getReservationDetailForCalendar(reservationTO)) {
			if (sor < reservationDetail.getSor()) {
				sor = reservationDetail.getSor();
			}
			lineItemLst.addAll(reservationDetail.getProposalLineItems());
			orderLineItemLst.addAll(reservationDetail.getOrderLineItems());
			reservationInfo = reservationDetail;
		}
		if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(type)) {
			sorMap.put("SOR" , NumberUtil.formatDouble(sor > 100 ? 0 : 100 - sor, true));
		} else {
			sorMap.put("SOR" , getReservationStatus(reservationInfo));
		}
		if(!lineItemLst.isEmpty()){
			sorMap.put("PROPOSALLI" , lineItemLst);
		}
		if(!orderLineItemLst.isEmpty()){
			sorMap.put("ORDERLI" , orderLineItemLst);
		}
		return sorMap;
	}
	
	/**
	 * Returns the reservation status for email products
	 * @param 	reservationInfo
	 * 			{@link ReservationInfo} has all the reservation info needed to calculate the reservation status
	 * @return
	 * 			Returns the reservation status
	 */
	private String getReservationStatus(final ReservationInfo reservationInfo) {
		String status = EmailReservationStatus.AVAILABLE.getDisplayName();
		if (reservationInfo != null) {
			if (reservationInfo.getSor() > 100) {
				status = EmailReservationStatus.OVERBOOKED.getDisplayName();
			} else if (reservationInfo.getSor() == 100) {
				status = (reservationInfo.getProposals() > 0) ? EmailReservationStatus.RESERVED.getDisplayName() : EmailReservationStatus.SOLD.getDisplayName();
			}
		}
		return status;
	}
	
	/**
	 * Returns the next sequence No. to be generated for the {@link LineItem}.
	 * @param lineItemForm
	 * @return Returns the sequence no. generated as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/getNextSequenceNo")
	public AjaxFormSubmitResponse getNextSequenceNo(final LineItemForm lineItemForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final ProposalVersion proposalVersion = proposalService.getproposalVersions(Long.valueOf(lineItemForm.getOptionId()),
				Long.valueOf(lineItemForm.getProposalversion())).get(0);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE,
				proposalService.getNextSequenceNoForLineItem(proposalVersion.getId()));
		return ajaxResponse;
	}

	/**
	 * Used to soft delete Proposal {@link LineItem}(s) whose id's has been passed as an argument and updates the CPM, Net impressions and Offered budget of the 
	 * proposal option whose line item(s) are deleted. Also re-calculates the pricing status of all the line items of the option whose line item has been deleted.
	 * @param 	lineItemForm
	 * @param 	lineItemIDs - A comma separated string of {@link LineItem} Id's to be deleted. 
	 * @return
	 * @throws CustomCheckedException 
	 * @throws NumberFormatException 
	 */
	@ResponseBody
	@RequestMapping("/deleteProposalLineItems")
	public AjaxFormSubmitResponse deleteProposalLineItems(@ModelAttribute(ConstantStrings.PROPOSAL_LINE_ITEM_FORM) final LineItemForm lineItemForm, @RequestParam(required = true) final String lineItemIDs) throws NumberFormatException, CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("deleting line Items of proposal. LineItemIds: " + lineItemIDs);
		}
		final List<LineItem> lineItemLst = proposalService.getLineItems(lineItemIDs);
		LOGGER.info("deleting line Items of proposal. LineItemIds: " + lineItemIDs);

		proposalService.deleteProposalLineItem(Long.valueOf(lineItemForm.getProposalID()), lineItemIDs);

		proposalService.updateAllLineItemPricingStatus(Long.valueOf(lineItemForm.getOptionId()));

		proposalService.updateProposalVersionNetImpressionAndCPM(Long.valueOf(lineItemForm.getOptionId()),
				Long.valueOf(lineItemForm.getProposalversion()));
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for (LineItem lineItem : lineItemLst) {
			if (lineItem.getReservation() != null) {
				if(productClassIdLst.contains(lineItem.getSosProductClass())){
					lineItemForm.setSosProductClassName((lineItem.getSosProductClass() == 28) ? ConstantStrings.PRODUCT_CLASS_HOME_PAGE : ConstantStrings.PRODUCT_CLASS_DISPLAY_CROSS_PLATFORM);//Its set explicitly bcz calling method needs it
					sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED, lineItemForm.getSosProductClassName());
				}
			}
		}
		return ajaxResponse;
	}

	/**
	 * Used to fetch the entire set of data of the given {@link LineItem} including its Exceptions, Targeting data and Pricing steps.
	 * @param lineItemId - Id of the {@link LineItem} whose data has to be fetched.
	 * @param productId - Id of the {@link Product} being set in the line item whose data has to be fetched.
	 * @return Returns the entire set of data of the {@link LineItem} as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/getLineItemsConsolidatedData")
	public AjaxFormSubmitResponse getLineItemsConsolidatedData(@RequestParam("lineItemId") final Long lineItemId, @RequestParam("productID") final Long productId){
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final LineItemForm lineItemForm = new LineItemForm();
		final LineItem lineItem = proposalService.getLineItemById(lineItemId);
		lineItemForm.populateForm(lineItem);
		if (lineItem.getPackageObj() != null) {
			final Package packageObj = packageService.getPackageById(lineItem.getPackageObj().getId());
			lineItemForm.setPackageName(packageObj.getName());
		}
		ajaxResponse.getObjectMap().put("lineItemDetailData", lineItemForm);
		final Map<Object, Object> salesTargetProductMap = getSalesTargetFromProductID(productId);
		ajaxResponse.getObjectMap().put("productSaleTargets", salesTargetProductMap.get("productSaleTargets"));
		if (salesTargetProductMap.containsKey("childSaleTargetIds")) {
			ajaxResponse.getObjectMap().put("childSaleTargetIds", salesTargetProductMap.get("childSaleTargetIds"));
		}
		ajaxResponse.getObjectMap().put("lineItemExceptions", getLineItemsExceptions(lineItemId).getObjectMap().get(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE));
		ajaxResponse.getObjectMap().put("lineItemTargets", getLineItemTargetData(lineItemId));
		ajaxResponse.getObjectMap().put("lineItemPricingSteps", getPricingCalculationJsonVal(lineItem));
		return ajaxResponse;
	}

	/**
	 * Used to fetch the data of all the {@link SalesTarget} along with its child Sales Target associated with a given product.
	 * @param productID - Id of the {@link Product} whose Sales Target data has to be fetched.
	 * @return Returns the Map of the Sales Target with the key value pair representing its Id and display name respectively, as an AJAX response.
	 */
	private Map<Object, Object> getSalesTargetFromProductID(final Long productID) {
		final Map<Object, Object> returnMap = new HashMap<Object, Object>();
		final Map<Long, String> allSalesTarTypMap = proposalHelper.getAllSalesTargetForProductID(productID);
		returnMap.put("productSaleTargets" , allSalesTarTypMap);
		final Set<Long> sosSaleTarIdKey = allSalesTarTypMap.keySet();
		final List<Long> saleTargetTypeLst = new ArrayList<Long>();
		for (Long targetTypeId : sosSaleTarIdKey) {
			saleTargetTypeLst.add(Long.valueOf(targetTypeId));
		}
		if (allSalesTarTypMap != null && !allSalesTarTypMap.isEmpty()) {
			final Map<Long, Long> childSaleTargtLst = proposalHelper.getParentSalesTargetId(saleTargetTypeLst);
			returnMap.put("childSaleTargetIds", childSaleTargtLst);
		}
		return returnMap;
	}

	/**
	 * Fetches the entire set of {@link LineItemExceptions} associated with a given {@link LineItem}.
	 * @param 	lineItemId - Id of the line item whose exceptions has to be fetched.
	 * @return 	Returns the list of String containing the display names of the {@link LineItemExceptions}.
	 */
	@ResponseBody
	@RequestMapping("/getLineItemsExceptions")
	public AjaxFormSubmitResponse getLineItemsExceptions(@RequestParam("lineItemID") final Long lineItemId) {
		final List <String> exceptionMessages = new ArrayList<String>();
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		LOGGER.info("LineItem Business Exceptions for LineItemID: " + lineItemId);
		final List<LineItemExceptions> lineItemExceptions = proposalService.getLineItemExceptions(lineItemId);
		if (lineItemExceptions != null) {
			for (LineItemExceptions lineItemException : lineItemExceptions) {
				exceptionMessages.add(LineItemExceptionEnum.findByName(lineItemException.getLineItemException()).getDisplayName());
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, exceptionMessages);
		return ajaxResponse;
	}

	/**
	 * Fetches the entire set of {@link LineItemTarget} data associated with a given {@link LineItem}.
	 * @param 	lineItemId - Id of the line item whose targeting data has to be fetched.
	 * @return 	Returns the targeting data of a {@link LineItem} as a JSON String.
	 */
	private String getLineItemTargetData(@RequestParam("lineItemId") final Long lineItemId) {
		LOGGER.info("Getting Line Item Targets for LineItemID: " + lineItemId);
		return targetJsonConverter.convertObjectToJson(proposalService.getProposalGeoTargets(lineItemId));
	}

	
	/**
	 * Fetches the pricing calculation steps of a given {@link LineItem}.
	 * @param lineItem - Id of the line item whose pricing steps has to be fetched.
	 * @return Returns the pricing calculation steps of a {@link LineItem} as a JSON String.
	 */
	private String getPricingCalculationJsonVal(final LineItem lineItem) {
		String priceCalSummary = ConstantStrings.EMPTY_STRING;
		if (lineItem != null) {
			if (StringUtils.isNotBlank(lineItem.getPriceCalSummary())
				&& (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE
						.equals(lineItem.getPriceType())) && lineItem.getRateCardPrice() > 0) {
				priceCalSummary = lineItem.getPriceCalSummary();
			}
		}
		return priceCalSummary;
	}

	/**
	 * Fetches the data of a particular option {@link ProposalVersion} of a given {@link Proposal}.
	 * @param 	proposalForm - {@link ProposalForm} object containing the information of the proposal, its options and version whose data has to be fetched.
	 * @return 	Returns the {@link ProposalForm} object populated with the {@link ProposalVersion} data as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/getproposalVersion")
	public AjaxFormSubmitResponse getproposalVersion(final ProposalForm proposalForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		LOGGER.info("Fetching ProposalVersion Details for proposalID: "
				+ proposalForm.getId() + " and proposalVersion: " + proposalForm.getProposalVersion());
		final ProposalVersion proposalVersion = proposalService.getproposalVersions(
				proposalForm.getOptionId(), Long.valueOf(proposalForm.getProposalVersion())).get(0);
		proposalForm.setNetCpm(proposalVersion.getEffectiveCpm() == null ? "0" : NumberUtil.formatDouble(proposalVersion.getEffectiveCpm(), false));
		proposalForm.setNetImpressions(proposalVersion.getImpressions() == null ? "0" : proposalVersion.getImpressions().toString());
		proposalForm.setOfferedBudget(proposalVersion.getOfferedBudget() == null
				? "0" : NumberUtil.formatDouble(proposalVersion.getOfferedBudget(), false));
		//Setting remaining budget
		proposalForm.setBudget(proposalVersion.getProposalOption().getBudget() == null ? "0" : NumberUtil.formatDouble(proposalVersion
				.getProposalOption().getBudget(), false));

		final double remainingBudget = NumberUtil.doubleValue(proposalForm.getBudget()) - NumberUtil.doubleValue(proposalForm.getOfferedBudget());
		proposalForm.setRemBudget(NumberUtil.formatDouble(remainingBudget, true));

		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalForm);
		return ajaxResponse;
	}

	/**
	 * 
	 * @param proposalLineItemForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLineItem")
	public AjaxFormSubmitResponse getLineItem(@ModelAttribute(ConstantStrings.PROPOSAL_LINE_ITEM_FORM) final LineItemForm proposalLineItemForm) {
		final LineItemForm lineItemForm = new LineItemForm();
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		LOGGER.info("LineItem Details for Line Item ID: " + proposalLineItemForm.getLineItemID());
		final LineItem lineItem = proposalService.getLineItemById(proposalLineItemForm.getLineItemID());
		lineItemForm.populateForm(lineItem);

		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, lineItemForm);
		return ajaxResponse;
	}

	/**
	 * Creates a new {@link ProposalVersion} of a particular {@link ProposalOption} of the given {@link Proposal}.
	 * @param proposalForm
	 * @return Returns the id of the {@link Proposal} whose {@link ProposalVersion} has been created as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/createVersion")
	public AjaxFormSubmitResponse createVersion(final ProposalForm proposalForm){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating version");
		}
		LOGGER.info("Creating version for ProposalID: " + proposalForm.getId());
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final long proposalId = proposalService.createVersion(proposalForm.getId(), proposalForm.getOptionId(), Long.valueOf(proposalForm.getProposalVersion()));

		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalId);
		return ajaxResponse;
	}

	/**
	 * Method to update proposal staus
	 * @param proposalForm
	 * @return
	 * @throws CustomCheckedException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/updateProposalStatus")
	public AjaxFormSubmitResponse updateProposalStatus(final ProposalForm proposalForm) throws CustomCheckedException, ParseException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating ProposalStatus");
		}
		LOGGER.info("Updating status for  ProposalID: " + proposalForm.getId());
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		boolean returnFlag = false;
		boolean isUnapprovedViewableLineItem =  false;
		if(ProposalStatus.PROPOSED.name().equalsIgnoreCase(proposalForm.getProposalStatus())){
			for (ProposalOption proposalOption : proposal.getProposalOptions()) {
				for (LineItem lineItem : proposalOption.getLatestVersion().getProposalLineItemSet()) {
					if(lineItem.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue() && lineItem.getPricingStatus().name().equalsIgnoreCase(PricingStatus.UNAPPROVED.name())){
						isUnapprovedViewableLineItem = true;
						break;
					}
				}
			}
		}
		if (proposal.getAssignedUser() == null && !(ProposalStatus.INPROGRESS.name().equalsIgnoreCase(proposalForm.getProposalStatus()) || ProposalStatus.DELETED.name().equalsIgnoreCase(proposalForm.getProposalStatus()))) {
			returnFlag = true;
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_ADDITIONAL_VALUE, returnFlag);
		}else {
			if(isUnapprovedViewableLineItem){
				ajaxResponse.getObjectMap().put(Constants.UNAPPROVED_VIEWABLE_LINE_ITEM, isUnapprovedViewableLineItem);
			}else{
				if(proposal.getVersion() != proposalForm.getVersion()) {
					throw new HibernateOptimisticLockingFailureException (new StaleObjectStateException(PROPOSAL, proposal.getId()));
				}
				final long proposalId = proposalService.updateProposalStatus(proposalForm.getId(), ProposalStatus.findByName(proposalForm.getProposalStatus()), proposalForm.getVersion());
				ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalId);
				ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_ADDITIONAL_VALUE, returnFlag);
			}
		}
		if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
			proposal.setWithPricing(false);
			proposalService.saveProposal(proposal.getId(), proposal);
		}
		return ajaxResponse;
	}

	/**
	 * Re-calculates the pricing status of all the line items of the given proposal in case proposal is not in the deleted and rejected by client status. Then checks 
	 * if there are any line items with 'Unapproved' status or not, in the default option in case the proposal is having Sold status otherwise all the options of the
	 * proposal will be checked.   
	 * @param proposalForm
	 * @return Returns true in case there will be line item(s) with Unapproved status
	 *         Returns false in case there won't be any line item with Unapproved status
	 */
	@ResponseBody
	@RequestMapping("/checkPricingStatus")
	public AjaxFormSubmitResponse checkPricingStatus(final ProposalForm proposalForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		if (proposal.getAssignedUser() == null && !(ProposalStatus.INPROGRESS.name().equalsIgnoreCase(proposalForm.getProposalStatus()) || ProposalStatus.DELETED.name().equalsIgnoreCase(proposalForm.getProposalStatus()))) {
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_ADDITIONAL_VALUE, true);
			return ajaxResponse;
		}
		boolean unapprovedFlag = false;
		if (StringUtils.isBlank(proposalForm.getProposalStatus())
				|| !(ProposalStatus.DELETED.equals(ProposalStatus.findByName(proposalForm.getProposalStatus())) || 
						ProposalStatus.REJECTED_BY_CLIENT.equals(ProposalStatus.findByName(proposalForm.getProposalStatus())))) {
			pricingStatusCalculatorService.addThreshHoldCheck(proposalForm.getId());
			pricingStatusCalculatorService.addAddedValueCheck(proposalForm.getId());
			if (StringUtils.isNotBlank(proposalForm.getProposalStatus()) && ProposalStatus.SOLD.equals(ProposalStatus.findByName(proposalForm.getProposalStatus()))) {
				/*Chaeck Unapproved Line Items in Default option for SOLD Proposal*/
				for (LineItem lineItem : proposal.getDefaultOption().getLatestVersion().getProposalLineItemSet()) {
					if (PricingStatus.UNAPPROVED.equals(lineItem.getPricingStatus())) {
						unapprovedFlag = true;
						break;
					}
				}
			} else {
				for (ProposalOption option : proposal.getProposalOptions()) {
					for (LineItem lineItem : option.getLatestVersion().getProposalLineItemSet()) {
						if (PricingStatus.UNAPPROVED.equals(lineItem.getPricingStatus())) {
							unapprovedFlag = true;
							break;
						}
					}
					if (unapprovedFlag) {
						break;
					}
				}
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, unapprovedFlag);
		return ajaxResponse;
	}

	
	/**
	 * Copies the list of {@link LineItem}(passed as an argument as a comma separated String of line Item Id's) of a given {@link ProposalOption} into the same {@link ProposalOption} 
	 * and also from a given {@link Package} into the same {@link Package}. Updates the CPM, Net impressions and Offered budget of the proposal option in which the 
	 * line items are copied. Also re-calculates the pricing status of all the {@link LineItem}(s) of that {@link ProposalOption}.
	 * @param lineItemForm
	 * @param lineItemIds - Comma separated list of id's of the {@link LineItem} to be copied.
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyLineItem")
	public AjaxFormSubmitResponse copyLineItem(final LineItemForm lineItemForm , @RequestParam(required = true) final String lineItemIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding copied Line Items To Proposal. copiedLineItemIds: " + lineItemIds);
		}
		LOGGER.info("adding copied Line Items To Proposal. copiedLineItemIds: " + lineItemIds);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal proposalDBValue = proposalService.getProposalbyId(NumberUtil.longValue(lineItemForm.getProposalID()));
		final String[] lineItems = lineItemIds.split(",");
		proposalService.saveCopiedLineItemsToProposal(Long.valueOf(lineItemForm.getProposalID()), Long.valueOf(lineItemForm.getOptionId()), Long.valueOf(lineItemForm.getProposalversion()), PROPOSAL,
				StringUtil.convertStringArrayToLongArray(lineItems), false, false, 1, proposalDBValue.getPriceType(), true, proposalDBValue.getAgencyMargin());

		proposalService.updateAllLineItemPricingStatus(Long.valueOf(lineItemForm.getOptionId()));

		// updating total budget and impressions.
		proposalService.updateProposalVersionNetImpressionAndCPM(Long.valueOf(lineItemForm.getOptionId()), Long.valueOf(lineItemForm.getProposalversion()));
		return ajaxResponse;
	}

	/**
	 * Re-calculates and update the base price of the list of {@link LineItem}(passed as an argument as a comma separated String of line Item Id's) of a given {@link ProposalOption}. 
	 * Then re-calculates the pricing status of all the line items of the {@link ProposalOption} whose line item(s) base price is updated..
	 * @param proposalID - Id of the proposal whose line items base price has to be recalculated.
	 * @param lineItemIds - Comma separated list of id's of the {@link LineItem} whose base price has to be recalculated.
	 * @param salesCategoryId - Id of the {@Code Sales Category} of the {@link Proposal}
	 * @param proposalPriceType - {@code Price type} of the {@link Proposal}
	 * @param propAgencyMargin - {@code AgencyMargin %} of the {@link Proposal}
	 * @param optionId - Id of the {@link ProposalOption} whose line items base price has to be recalculated.
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAllLineItemPrice")
	public AjaxFormSubmitResponse updateAllLineItemPrice(@RequestParam("proposalID") final Long proposalID, @RequestParam(required = true) final String lineItemIds,
			@RequestParam(ConstantStrings.SALE_SCATEGORY_ID) final Long salesCategoryId,
			@RequestParam(ConstantStrings.PRO_PRICE_TYPE) final String proposalPriceType,
			@RequestParam(ConstantStrings.PRO_AGENCY_MARGIN) final String propAgencyMargin,
			@RequestParam("optionId") final Long optionId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final String[] lineItems = lineItemIds.split(ConstantStrings.COMMA);
		proposalService.updateAllLineItemsPrice(proposalID, StringUtil.convertStringArrayToLongArray(lineItems), salesCategoryId, proposalPriceType, Double.valueOf(propAgencyMargin));
		proposalService.updateAllLineItemPricingStatus(optionId);
		return ajaxResponse;
	}

	/**
	 * Get selected sales target summary
	 * @param salesTargetId
	 * @return
	 */
	@RequestMapping("/getSTWeightSummary")
	public ModelAndView getSTWeightSummary(@RequestParam("sosSalesTargetId") final Long[] salesTargetId) {
		final ModelAndView view = new ModelAndView(ConstantStrings.SALES_TARGET_WEIGHT_SUMMARY);
		LOGGER.info("Getting sales target weight summary for id's : " + StringUtils.join(salesTargetId, ConstantStrings.COMMA));
		if (salesTargetId != null && salesTargetId.length > 0) {
			final List<SalesTargetAmpt> salesTargetLst = proposalHelper.getSalesTarget(salesTargetId);
			final List<SalesTargetAmptForm> lineItemFormList = convertSalesTargetAmptToForm(salesTargetLst);
			if (lineItemFormList != null && !lineItemFormList.isEmpty()) {
				Collections.sort(lineItemFormList, new SalesTargeDisNameComparator());
				view.addObject("salesTargetLst", lineItemFormList);
			}
		} else {
			view.addObject("showMsg", "noSalSalesTargetSelected");
		}
		return view;
	}

	/**
	 * Splits the line items month wise based on its flight dates, only if the value of the {@code total impressions} is not too low in any of the line item to be split so 
	 * that in the line items created after splitting its impressions count is coming as zero. The impressions of the line item to be split will be divided among 
	 * the new line items created after splitting on the basis of impressions per day and the number of days which span the flight of the newly created line item.
	 * @param lineItemForm
	 * @param lineItemIds - Comma separated list of id's of the line items to be split.
	 * @return Returns true in case the line items impressions value is too low for splitting else returns false as an AJAX response.
	 * @throws CustomCheckedException 
	 * @throws NumberFormatException 
	 */
	@ResponseBody
	@RequestMapping("/splitLineItems")
	public AjaxFormSubmitResponse splitLineItems(final LineItemForm lineItemForm , @RequestParam(required = true) final String lineItemIds) throws NumberFormatException, CustomCheckedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Splitting the Line Items with the ids: " + lineItemIds);
		}
		LOGGER.debug("Splitting the Line Items with the ids: " + lineItemIds);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		boolean lineItemsImpressionsTooLow = false;
		
		lineItemsImpressionsTooLow = proposalService.createLineItemsBySpliting(Long.valueOf(lineItemForm.getProposalID()), Long.valueOf(lineItemForm.getOptionId()), 
				Long.valueOf(lineItemForm.getProposalversion()),lineItemIds);
		
		/* updating total budget and impressions, if the lines items have been split successfully. */
		if(!lineItemsImpressionsTooLow){
			proposalService.updateProposalVersionNetImpressionAndCPM(Long.valueOf(lineItemForm.getOptionId()), Long.valueOf(lineItemForm.getProposalversion()));
		}
		
		ajaxResponse.getObjectMap().put("lineItemsImpressionsTooLow", lineItemsImpressionsTooLow);
		return ajaxResponse;
	}
	
	/**
	 * Validates if the list of {@link LineItem} can be split successfully or not.
	 * @param lineItemForm
	 * @param lineItemIds - Comma separated list of id's of the {@link LineItem} to be validated for splitting
	 * @return Returns a flag with its value as true if the {@link LineItem}(s) can be split successfully else its value will be false.
	 * 		   Returns another flag with its value as true in case the flight date of any of the {@link LineItem} to be split falls in the same month else its value 
	 * 		   will be false, as an AJAX response.
	 */
	@ResponseBody
	@RequestMapping("/validateSplitLineItemsData")
	public AjaxFormSubmitResponse validateSplitLineItemsData(@RequestParam(required = true) final String lineItemIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Splitting the Line Items with the ids: " + lineItemIds);
		}
		LOGGER.debug("Splitting the Line Items with the ids: " + lineItemIds);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		boolean islineItemsSplittingAllowed =  true;
		boolean lineItemFlightsSpanSameMon = false;
		
		List<LineItem> lineItemLst = proposalService.getLineItems(lineItemIds);
		for (LineItem lineItem : lineItemLst) {
			if( !LineItemProductTypeEnum.STANDARD.equals(lineItem.getProductType()) || 
					(LineItemProductTypeEnum.STANDARD.equals(lineItem.getProductType()) && LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())) ||
					(lineItem.getStartDate() == null && lineItem.getEndDate() == null)){
				islineItemsSplittingAllowed = false;
				continue;
			}
			
			/* Checking if the flight of the line item falls in the same month or not */
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(lineItem.getStartDate());
			
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(lineItem.getEndDate());
		
			long noOfFlightMonths = DateUtil.monthsBetween(startDate, endDate);
			if(noOfFlightMonths == 1){
				lineItemFlightsSpanSameMon = true;
			}
		}
		
		ajaxResponse.getObjectMap().put("islineItemsSplittingAllowed", islineItemsSplittingAllowed);
		ajaxResponse.getObjectMap().put("lineItemFlightsSpanSameMon", lineItemFlightsSpanSameMon);
		return ajaxResponse;
	}
	
	/**
	 * Used to create list of {@link SalesTargetAmpt} objects from the corresponding {@link SalesTarget} bean class.
	 * @param salesTargetList
	 * @return
	 */
	private List<SalesTargetAmptForm> convertSalesTargetAmptToForm(final List<SalesTargetAmpt> salesTargetList) {
		final List<SalesTargetAmptForm> lineItemFormList = new ArrayList<SalesTargetAmptForm>();
		for (SalesTargetAmpt salesTargetAmpt : salesTargetList) {
			final SalesTargetAmptForm salesTargetAmptForm = new SalesTargetAmptForm();
			salesTargetAmptForm.populateForm(salesTargetAmpt);
			if (salesTargetAmpt.getSalesTargetId() == -1L) {
				salesTargetAmptForm.setWeight("0.00");
				salesTargetAmptForm.setCapacity("-");
				salesTargetAmptForm.setModifiedDate("-");
			}
			lineItemFormList.add(salesTargetAmptForm);
		}
		return lineItemFormList;
	}

	/**
	 * Validates the data of the given {@link Proposal} to check if the proposal can be marked as sold or not. In case of validation failure returns the appropriate validation error codes 
	 * and messages.
	 * @param 	proposalId - Id of the {@link Proposal} whose data has to be validated.
	 * @return	Returns the validation type as "ERROR" or "CUSTOM_ERROR" and appropriate validation message in case any of the below mentioned condition is true otherwise returns validation 
	 * type as "NO ERROR" and an empty String as a validation message:
	 * <ul>
	 *		<li> There are no line items present in the default option of the proposal.
	 *      <li> If any inactive product or sales target is selected in any of the line item present in the default option of the proposal.
	 *      <li> If the combination of product and sales target(s) selected in any the line item present in the default option of the proposal is inactive.
	 *      <li> If the Start/End date of any of the line item present in the default option of the proposal is null.
	 * </ul>
	 */
	@ResponseBody
	@RequestMapping("/validateProposalForBridging")
	@SuppressWarnings("unchecked")
	public AjaxFormSubmitResponse validateProposalForBridging(@RequestParam("proposalId") final Long proposalId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal proposal = proposalService.getProposalbyIdForSosIntegration(proposalId);
		final Set<LineItem> proposalLineItems = (proposal != null) ? proposal.getDefaultOption().getLatestVersion().getProposalLineItemSet() : Collections.EMPTY_SET;
		String validationType = "NO ERROR";
		String message = ConstantStrings.EMPTY_STRING;
		if (proposalLineItems.isEmpty()) {
			validationType = "ERROR";
			message = "error.no.lineItems";
		} else {
			final Map<String, List<String>> inactiveProdSTLst = new HashMap<String, List<String>>();
			final List<String> inActiveProdutPlacements = new ArrayList<String>();
			final List<Long> lineItems = new ArrayList<Long>();
			if(!validateHomePageReservation(proposalLineItems, lineItems)){
				validationType = "CUSTOM_ERROR";
				message = createMessageForNonRsrvdHomePageLnItms(lineItems);
			}
			else if (!isAllProductSalesTargetActive(proposalLineItems, inactiveProdSTLst)) {
				validationType = "CUSTOM_ERROR";
				message = createMessageForInactiveProSat(inactiveProdSTLst);
			} else if (!isProductPlacementActive(proposalLineItems, inActiveProdutPlacements)) {
				validationType = "CUSTOM_ERROR";
				message = createMessageForInactiveProdPlcmnt(inActiveProdutPlacements);
			} else if (!validateLineItemsDate(proposalLineItems, lineItems)) {
				validationType = "CUSTOM_ERROR";
				message = createMessageForLineItemDates(lineItems);
			}else if(!validateHomePageReservation(proposalLineItems, lineItems)){
				validationType = "CUSTOM_ERROR";
				message = createMessageForNonRsrvdHomePageLnItms(lineItems);
			}
		}
		ajaxResponse.getObjectMap().put("VALIDATION_TYPE", validationType);
		ajaxResponse.getObjectMap().put("MESSAGE", message);
		return ajaxResponse;
	}

	/**
	 * Generates the custom error message for Line Item Dates validation done while bridging {@link Proposal} to SOS.
	 * @param lineItems - List of {@link LineItem} for whom date validation has failed while bridging {@link Proposal} to SOS.
	 * @return	Returns the custom error message generated for the Line Item Dates validation as a String.
	 */
	private String createMessageForLineItemDates(final List<Long> lineItems) {
		final StringBuilder message = new StringBuilder();
		message.append("The proposal cannot be bridged to SOS as some of the line items in default option have actual flight dates missing :");
		message.append("<br/>");
		for (Long lineItemId : lineItems) {
			message.append(lineItemId).append("<br/>");
		}
		return message.toString();
	}
	
	

	/**
	 * Validates whether all the {@link LineItem} in the given list contains Start date and End date.
	 * @param 	proposalLineItems - List of {@link LineItem} to be validated.
	 * @param 	lineItems - List of Long values to be populated with the Id's of the {@link LineItem} for whom Date validation will be failed. 
	 * @return	Returns true in case all line items are validated successfully else returns false.
	 */
	private boolean validateHomePageReservation(final Set<LineItem> proposalLineItems, final List<Long> lineItems) {
		boolean returnFlag = true;
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for (LineItem lineItem : proposalLineItems) {
			if(productClassIdLst.contains(lineItem.getSosProductClass()) && (lineItem.getReservation()==null || ReservationStatus.RELEASED.name().equals(lineItem.getReservation().getStatus().name()))){
				lineItems.add(lineItem.getLineItemID());
				returnFlag = false;
			}
		}
		return returnFlag;
	}
	
	/**
	 * Generates the custom error message for non reserved home page line items while bridging {@link Proposal} to SOS.
	 * @param lineItems - List of non reserved home page {@link LineItem} for whom reservation for home page validation has failed while bridging {@link Proposal} to SOS.
	 * @return	Returns the custom error message generated for the Line Item Dates validation as a String.
	 */
	private String createMessageForNonRsrvdHomePageLnItms(final List<Long> lineItems) {
		final StringBuilder message = new StringBuilder();
		message.append("The proposal cannot be bridged to SOS as some of the home page/cross platform line items are not reserved:");
		message.append("<br/>");
		for (Long lineItemId : lineItems) {
			message.append(lineItemId).append("<br/>");
		}
		return message.toString();
	}
	
	/**
	 * Validates whether all the {@link LineItem} in the given list are not non-reserved home page line items.
	 * @param 	proposalLineItems - List of {@link LineItem} to be validated.
	 * @param 	lineItems - List of Long values to be populated with the Id's of the {@link LineItem} which are non-reserved home page line items. 
	 * @return	Returns true in case all line items are validated successfully else returns false.
	 */
	private boolean validateLineItemsDate(final Set<LineItem> proposalLineItems, final List<Long> lineItems) {
		boolean returnFlag = true;
		for (LineItem lineItem : proposalLineItems) {
			if (lineItem.getStartDate() == null || lineItem.getEndDate() == null) {
				lineItems.add(lineItem.getLineItemID());
				returnFlag = false;
			}
		}
		return returnFlag;
	}

	/**
	 * Generates the custom error message for inactive product placements validation 
	 * @param 	inActiveProdutPlacements - List of the inactive Product and Sales Target combinations for whom the error message has to be generated. 
 	 * @return	Returns the custom error message generated for the inactive product placements validation as a String.
	 */
	private String createMessageForInactiveProdPlcmnt(final List<String> inActiveProdutPlacements) {
		final StringBuilder message = new StringBuilder();
		message.append("Following Product Placements are inactive in SOS:");
		message.append("<br/>");
		for (String productPlacement : inActiveProdutPlacements) {
			message.append(productPlacement).append("<br/>");
		}
		return message.toString();
	}

	/**
	 * Generates the custom error message for the given list of inactive {@link Product} and {@link SalesTarget} 
	 * @param 	inactiveProdSTLst - Map of the inactive Products and Sales Targets elements with its key value pair representing the type of the list of values as Product/Sales Target and
	 * the list of the names of the inactive Products/SalesTarget respectively. 
	 * @return	Returns the custom error message generated for the inactive Products and Sales Targets as a String.
	 */
	private String createMessageForInactiveProSat(final Map<String, List<String>> inactiveProdSTLst) {
		final StringBuilder message = new StringBuilder();
		message.append("Following Product/SalesTagets are inactive in SOS:");
		message.append("<br/>");
		if (inactiveProdSTLst.containsKey(Constants.PRODUCT)) {
			message.append("<b>" + Constants.PRODUCT + "</b>").append("<br/>");
			for (String productName : inactiveProdSTLst.get(Constants.PRODUCT)) {
				message.append(productName).append("<br/>");
			}
		}
		if (inactiveProdSTLst.containsKey(Constants.SALES_TARGET)) {
			message.append("<b>" + Constants.SALES_TARGET + "</b>").append("<br/>");
			for (String salesTargetName : inactiveProdSTLst.get(Constants.SALES_TARGET)) {
				message.append(salesTargetName).append("<br/>");
			}
		}
		return message.toString();
	}

	/**
	 * Checks whether the combination of the Product and Sales Target i.e. Product Placement selected in the given Set of {@link LineItem} is active in SOS or not.
	 * @param	proposalLineItems - Set of {@link LineItem} whose Product Placements has to be validated.
	 * @param 	inActiveProdutPlacements - List of String values to be populated with the inactive Product Placements found in the given list of {@link LineItem}.
	 * @return	Returns true if all the Product Placements are active otherwise returns false.
	 */
	private boolean isProductPlacementActive(final Set<LineItem> proposalLineItems, final List<String> inActiveProdutPlacements) {
		boolean returnFlag = true;
		boolean recordExist = false;
		for (LineItem lineItem : proposalLineItems) {
			final List<ProductPlacement> productPlacements = proposalSOSService.getActiveProductPlacement(lineItem.getSosProductId(), proposalHelper.convertSalesTargetAssocsToIds(lineItem
					.getLineItemSalesTargetAssocs()));
			if (productPlacements == null || productPlacements.size() != lineItem.getLineItemSalesTargetAssocs().size()) {
				returnFlag = false;
				if (productPlacements == null) {
					for (LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()) {
						inActiveProdutPlacements.add(lineItem.getProductName() + ConstantStrings.COMMA + lineItemSalesTarget.getSosSalesTargetName());
					}
				} else {
					for (LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()) {
						for (ProductPlacement productPlacement : productPlacements) {
							if (productPlacement.getProduct().getId() == lineItem.getSosProductId()
									&& productPlacement.getSalesTarget().getSalesTargetId() == lineItemSalesTarget.getSosSalesTargetId()) {
								recordExist = true;
								break;
							}
						}
						if (!recordExist) {
							inActiveProdutPlacements.add(lineItem.getProductName() + ConstantStrings.COMMA + lineItemSalesTarget.getSosSalesTargetName());
						}
						recordExist = false;
					}
				}
			}
		}
		return returnFlag;
	}

	/**
	 * Checks whether all the {@link Product} and {@link SalesTarget} selected in the given Set of {@link LineItem} is active in SOS or not.
	 * @param 	proposalLineItems - Set of {@link LineItem} whose {@link Product} and {@link SalesTarget} has to be validated.
	 * @param 	inactiveProdLst - Map to be populated with the inactive {@link Product} and {@link SalesTarget} elements with its key value pair representing the type of the list of values 
	 * as Product/Sales Target and the list of the names of the inactive Products/SalesTarget respectively.
	 * @return	Returns true if all the {@link Product} and {@link SalesTarget} are active otherwise returns false.
	 */
	private boolean isAllProductSalesTargetActive(final Set<LineItem> proposalLineItems, final Map<String, List<String>> inactiveProdSTLst) {
		boolean returnFlag = false;
		final Set<Long> productIds = new HashSet<Long>(proposalLineItems.size());
		final Set<Long> salesTargetIds = new HashSet<Long>();
		for (LineItem lineItem : proposalLineItems) {
			productIds.add(lineItem.getSosProductId());
			for (LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()) {
				salesTargetIds.add(lineItemSalesTarget.getSosSalesTargetId());
			}
		}
		final List<com.nyt.mpt.domain.sos.Product> products = productService.getSOSProductListByIDs(productIds.toArray(new Long[0]));
		final List<com.nyt.mpt.domain.sos.SalesTarget> salesTargets = salesTargetService.getSOSSalesTargetListByIDs(salesTargetIds.toArray(new Long[0]));
		if (products.size() == productIds.size()) {
			if (salesTargets.size() == salesTargetIds.size()) {
				returnFlag = true;
			}
		}
		final Map<Long, String> productMap = new HashMap<Long, String>(products.size());
		final Map<Long, String> salesTargetMap = new HashMap<Long, String>(salesTargets.size());
		for (Product product : products) {
			productMap.put(product.getId(), product.getDisplayName());
		}
		for (SalesTarget salesTarget : salesTargets) {
			salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargetName());
		}
		for (LineItem lineItem : proposalLineItems) {
			if (!productMap.containsKey(lineItem.getSosProductId())) {
				if (inactiveProdSTLst.containsKey(Constants.PRODUCT)) {
					if(!inactiveProdSTLst.get(Constants.PRODUCT).contains(lineItem.getProductName())){
						inactiveProdSTLst.get(Constants.PRODUCT).add(lineItem.getProductName());
					}
				} else {
					final List<String> prodNames = new ArrayList<String>();
					prodNames.add(lineItem.getProductName());
					inactiveProdSTLst.put(Constants.PRODUCT, prodNames);
				}
			}
			for (LineItemSalesTargetAssoc lineItemSalesTarget : lineItem.getLineItemSalesTargetAssocs()) {
				if (!salesTargetMap.containsKey(lineItemSalesTarget.getSosSalesTargetId())) {
					if (inactiveProdSTLst.containsKey(Constants.SALES_TARGET)) {
						if(!inactiveProdSTLst.get(Constants.SALES_TARGET).contains(lineItemSalesTarget.getSosSalesTargetName())){
							inactiveProdSTLst.get(Constants.SALES_TARGET).add(lineItemSalesTarget.getSosSalesTargetName());
						}
					} else {
						final List<String> salstargtNames = new ArrayList<String>();
						salstargtNames.add(lineItemSalesTarget.getSosSalesTargetName());
						inactiveProdSTLst.put(Constants.SALES_TARGET, salstargtNames);
					}
				}
			}
		}
		return returnFlag;
	}

	/**
	 * Used to update avails of all package line item
	 * @param proposalID
	 * @param optionID
	 * @param proposalVersion
	 * @param availsJsonData
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateAvails")
	public AjaxFormSubmitResponse updateAvails(@RequestParam("proposalID") final long proposalID, @RequestParam("optionID") final long optionID, @RequestParam("proposalVersion") final long proposalVersion,
			@RequestParam("availsJsonData") final String availsJsonData) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final ObjectMapper objectMapper = new ObjectMapper();
		List<LinkedHashMap<String, String>> resultLst = new ArrayList<LinkedHashMap<String, String>>();
		Long capacity = 0L;
		Double sov = 0D;
		try {
			resultLst = objectMapper.readValue(availsJsonData, resultLst.getClass());

		} catch (JsonParseException e) {
			LOGGER.error("Error during Parsing JSON - " + e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error during Mapping JSON - " + e);
		} catch (IOException e) {
			LOGGER.error("Error during Converting JSON to CampaignPacingObject - " + e);
		}
		final List<LineItem> lineItemLst = proposalService.getProposalLineItems(optionID, proposalVersion, null, null, null);
		final Map<Long, AvailData> lineItemAvailsMap = convertAvailsLstToMap(resultLst);
		final List<LineItem> lineItemLstToSave = new ArrayList<LineItem>(lineItemAvailsMap.size());
		for (LineItem lineItem : lineItemLst) {
			if (lineItemAvailsMap.containsKey(lineItem.getLineItemID())) {
				if (!StringUtils.equals(ConstantStrings.NA, lineItemAvailsMap.get(lineItem.getLineItemID()).getAvail())) {
					lineItem.setAvails(NumberUtil.doubleValue(lineItemAvailsMap.get(lineItem.getLineItemID()).getAvail()));
				}
				if (!StringUtils.equals(ConstantStrings.NA, lineItemAvailsMap.get(lineItem.getLineItemID()).getCapacity())) {
					capacity = NumberUtil.longValue(lineItemAvailsMap.get(lineItem.getLineItemID()).getCapacity());
					lineItem.setTotalPossibleImpressions(capacity);
				}
				if (capacity > 0) {
					sov = (double) (((lineItem.getImpressionTotal()) * 100) / capacity);
					lineItem.setSov(NumberUtil.round(sov, 2));
				}
				lineItem.setAvailsPopulatedDate(DateUtil.getCurrentDate());
				lineItemLstToSave.add(lineItem);
			}
		}
		proposalService.updateLineItemsAvails(proposalID, lineItemLstToSave);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, 1);
		return ajaxResponse;
	}

	/**
	 * Convert JSON Object 'availLst' to Map
	 * @param availLst
	 * @return
	 */
	private Map<Long, AvailData> convertAvailsLstToMap(final List<LinkedHashMap<String, String>> availLst) {
		final Map<Long, AvailData> lineItemMap = new HashMap<Long, AvailData>(availLst.size());
		AvailData availData = null;
		for (LinkedHashMap<String, String> linkedHashMap : availLst) {
			availData = new AvailData();
			availData.setAvail(linkedHashMap.get(ConstantStrings.AVAILS));
			availData.setCapacity(linkedHashMap.get(ConstantStrings.CAPACITY));
			availData.setLineItemID(NumberUtil.longValue(linkedHashMap.get(ConstantStrings.LINITEMID)));
			lineItemMap.put(availData.getLineItemID(), availData);
		}
		return lineItemMap;
	}

	/**
	 * To create Proposal Clone page.
	 * @param proposalForm
	 * @return
	 */
	@RequestMapping("/getProposalCloneDisplayPage")
	public ModelAndView getProposalCloneDisplayPage(final ProposalForm proposalForm) {
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalForm.getId());
		final List<ProposalForm> proposalFormLst = new ArrayList<ProposalForm>(optionLst.size());
		for (ProposalOption option : optionLst) {
			final ProposalForm propForm = new ProposalForm();
			propForm.setDefaultOption(Boolean.toString(option.isDefaultOption()));
			propForm.setOptionId(option.getId());
			propForm.setOptionName(option.getName());
			proposalFormLst.add(propForm);
		}

		final ModelAndView view = new ModelAndView(ConstantStrings.CLONE_PROPOSAL_PAGE);
		Collections.sort(proposalFormLst, new OptionNameComparator());
		view.addObject("proposalOptions", proposalFormLst);
		return view;
	}

	/**
	 * Returns the reviewStatusPage.jsp as a HTML 
	 * @param proposalForm
	 * @return
	 */
	@RequestMapping("/getReviewStatusPage")
	public ModelAndView getReviewStatusPage(final ProposalForm proposalForm) {
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalForm.getId());
		final List<ProposalForm> proposalFormLst = new ArrayList<ProposalForm>(optionLst.size());
		int reservationCount = 0;
		for (ProposalOption option : optionLst) {
			final ProposalForm propForm = new ProposalForm();
			propForm.setDefaultOption(Boolean.toString(option.isDefaultOption()));
			propForm.setOptionId(option.getId());
			propForm.setOptionName(option.getName());
			propForm.setBudget(option.getBudget() == null ? ZERO : NumberUtil.formatDouble(option.getBudget(), true));
			propForm.setOfferedBudget(option.getLatestVersion().getOfferedBudget() == null ? ZERO : NumberUtil.formatDouble(option.getLatestVersion().getOfferedBudget(), true));
			propForm.setNetCpm(option.getLatestVersion().getEffectiveCpm() == null ? ZERO : NumberUtil.formatDouble(option.getLatestVersion().getEffectiveCpm(), true));
			propForm.setNetImpressions(option.getLatestVersion().getImpressions() == null ? ZERO : NumberUtil.formatLong(option.getLatestVersion().getImpressions(), true));
			reservationCount = 0;
			for (LineItem lineItem : option.getLatestVersion().getProposalLineItemSet()) {
				if (lineItem.getReservation() != null && !ReservationStatus.RELEASED.name().equals(lineItem.getReservation().getStatus().name())) {
					reservationCount++;
				}
			}
			propForm.setNoOfReservations(NumberUtil.formatLong(reservationCount, true));
			proposalFormLst.add(propForm);
		}

		final ModelAndView view = new ModelAndView(ConstantStrings.REVIEW_PAGE);
		Collections.sort(proposalFormLst, new OptionNameComparator());
		view.addObject("proposalOptions", proposalFormLst);
		return view;
	}

	/**
	 * Creates the clone of the given proposal. Copies only the {@link ProposalOption} whose Id's being passed as an argument.
	 * @param 	proposalId - Id of the {@link Proposal} whose clone has to be created.
	 * @param 	optionIds - Comma separated list of Id's of the {@link ProposalOption} of the given proposal to be copied.
	 * @return	Returns the Id of the new {@link Proposal} being created after cloning.
	 */
	@ResponseBody
	@RequestMapping("/getProposalClone")
	public AjaxFormSubmitResponse createProposalClone(@RequestParam("proposalId") final long proposalId, @RequestParam("optionIds") final String optionIds) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal prop = proposalService.createClone(proposalId , optionIds);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Proposal clone has been created. New proposal Id: " + prop.getId());
		}
		pricingStatusCalculatorService.addAddedValueCheck(prop.getId());
		pricingStatusCalculatorService.addThreshHoldCheck(prop.getId());
		LOGGER.info("Proposal clone has been created. New proposal Id: " + prop.getId());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, prop.getId());
		return ajaxResponse;
	}

	/**
	 * Calculates Rate Card Price for a given {@link lineItem}.
	 * @param 	response
	 * @param 	form - {@link LineItemForm} object containing the details of the line item whose rate card price has to be calculated.
	 * @param 	salesCategoryId - Id of the Sales Category of the {@link Proposal} of the given line item.
	 * @param 	proposalPriceType - {@code Price Type} of the {@link Proposal} of the given line item.
	 * @param 	propAgencyMargin - {@code Agency Margin(%)} of the {@link Proposal} of the given line item.
	 * @return	Returns the rate card price calculated and the Summary of the Price Calculation Steps as an AJAX Response.
	 */
	@ResponseBody
	@RequestMapping("/calculateBasePrice")
	public AjaxFormSubmitResponse calculateBasePrice(final HttpServletResponse response,
			@ModelAttribute(ConstantStrings.PROPOSAL_LINE_ITEM_FORM) final LineItemForm form,
			@RequestParam(ConstantStrings.SALE_SCATEGORY_ID) final long salesCategoryId,
			@RequestParam(ConstantStrings.PRO_PRICE_TYPE) final String proposalPriceType,
			@RequestParam(ConstantStrings.PRO_AGENCY_MARGIN) final String propAgencyMargin) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("lineItemForm", form);
		new PackageLineItemsFormValidator().pricingCalculatorValidate(form, results);
		Double price = null;
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			final LineItem lineItem = new LineItem();
			form.populate(lineItem);
			form.populateSalestargetAssoc(lineItem, salesTargetService.getSalesTargetListByIDs(proposalHelper.getSalesTragetIdsArray(form.getSosSalesTargetId())));
			if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItem.getPriceType())
					|| ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(lineItem.getPriceType())) {
				final Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(lineItem, salesCategoryId, proposalPriceType);
				if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
					price = (Double) pricingCalculatorMap.get("price");
					if (price != null && price > 0.0) {
						// Apply Gross Margin and half cent formated rule
						if ("Gross".equalsIgnoreCase(proposalPriceType)) {
							price = proposalService.applyAgencyMargin(proposalPriceType, price, Double.valueOf(propAgencyMargin));
						} else {
							boolean israteCardRounded = (Boolean) pricingCalculatorMap.get("rateCardRounded");
							if(!israteCardRounded){
								price = NumberUtil.getHalfCentFormatedValue(price);
							}
						}
					}
					final String formatPrice = (price == null || price == 0.0) ? null : NumberUtil.formatDouble(price, true);
					ajaxResponse.getObjectMap().put(ConstantStrings.PRICE, formatPrice);
					final String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null || formatPrice == null 
							? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
					ajaxResponse.getObjectMap().put(ConstantStrings.PRICE_CAL_SUMMARY, priceCalSummary);
				}
			}
		}
		return ajaxResponse;
	}

	/**
	 * Fetches the pricing calculation steps of the given {@link LineItem} and returns a view named "priceCalSummaryStep.jsp" associated with a ModelAndView Object for showing the pricing 
	 * steps on UI as a HTML response. Also associates the summary of the Pricing Calculation Steps and agency margin to the ModelAndView object as its attribute.
	 * @param 	priceCalSummary - String representing the pricing calculation summary of the given {@link LineItem}
	 * @param 	lineItemId - Id of the {@link LineItem} whose pricing calculation steps has to be calculated.
	 * @param 	rateCardPrice - {@code Rate Card Price} of the {@link LineItem} whose pricing calculation steps has to be calculated.
	 * @param 	proposalPriceType - {@code Price Type} of the {@link Proposal} of the given {@link LineItem}
	 * @param 	propAgencyMargin - {@code Agency Margin(%)} of the {@link Proposal} of the given line item.
	 * @return	Returns a ModelAndView object with a view named "priceCalSummaryStep.jsp" associated with it.
	 */
	@RequestMapping("/getPricingCalculationStep")
	public ModelAndView getPricingCalculationStep(@RequestParam("priceCalSummary") String priceCalSummary,
			@RequestParam("lineItemId") final long lineItemId, @RequestParam("rateCardPrice") final String rateCardPrice,
			@RequestParam(ConstantStrings.PRO_PRICE_TYPE) final String proposalPriceType,
			@RequestParam(ConstantStrings.PRO_AGENCY_MARGIN) final String propAgencyMargin) {
		final ModelAndView view = new ModelAndView(ConstantStrings.PRICE_CAL_SUMMARY_VIEW);
		PricingCalculatorSummary pricingCalculatorStep = null;
		if (StringUtils.isNotBlank(rateCardPrice) && !StringUtils.equals(rateCardPrice, ConstantStrings.NOT_DEFINED)
				&& !StringUtils.equals(rateCardPrice, ConstantStrings.NA)) {
			if (StringUtils.isNotBlank(priceCalSummary)) {
				pricingCalculatorStep = convertJsonToObject(priceCalSummary, proposalPriceType, Double.valueOf(propAgencyMargin));
			} else if (lineItemId > 0) {
				final LineItem lineItem = proposalService.getLineItemById(lineItemId);
				if (lineItem != null) {
					priceCalSummary = lineItem.getPriceCalSummary();
					if (StringUtils.isNotBlank(priceCalSummary)) {
						pricingCalculatorStep = convertJsonToObject(priceCalSummary, proposalPriceType, Double.valueOf(propAgencyMargin));
					}
				}
			}
		}
		if (pricingCalculatorStep == null) {
			view.addObject("showMsg", "stepNotFound");
		} else {
			view.addObject("calculatorSummary", pricingCalculatorStep);
			if (ConstantStrings.GROSS.equalsIgnoreCase(proposalPriceType)) {
				view.addObject("agencyMargin", propAgencyMargin);
			} else {
				view.addObject("agencyMargin", ConstantStrings.NA);
			}
		}
		return view;
	}

	/**
	 * Fetches the details of all the creative(s) associated with the given {@link Product} and returns a view associated with the "productCreativeSummary.jsp" for showing the creative(s)
	 * summary on UI as HTML.
	 * @param 	sosProductId - Id of the {@link Product} whose creative(s) summary has to be shown.
	 * @return	
	 */
	@RequestMapping("/showProductCreativesSummary")
	public ModelAndView showProductCreativesSummary(@RequestParam("sosProductId") final long sosProductId) {
		final ModelAndView view = new ModelAndView("productCreativeSummary");
		final Map<String, Creative> productCreativeMap = new TreeMap<String, Creative>();
		if (sosProductId > 0) {
			final List<ProductCreativeAssoc> productCreativeAssocLst = productService.getProductCreatives(sosProductId);
			for (ProductCreativeAssoc productCreativeAssoc : productCreativeAssocLst) {
				productCreativeMap.put(productCreativeAssoc.getCreative().getName().toLowerCase(), productCreativeAssoc.getCreative());
			}
		}

		view.addObject("productCreativeLst", productCreativeMap.values());
		return view;
	}

	/**
	 * Set json data into PricingCalculatorSummary
	 * @param jsonString
	 * @param propPriceType
	 * @param propAgencyMargin
	 * @return
	 */
	private PricingCalculatorSummary convertJsonToObject(final String jsonString, final String propPriceType, final double propAgencyMargin) {
		final PricingCalculatorSummary pricingCalculatorStep = getNode(jsonString);
		if (pricingCalculatorStep != null) {
			if (pricingCalculatorStep.getBasePrice() != null && pricingCalculatorStep.getBasePrice() >= 0.0) {
				/*if ("Net".equalsIgnoreCase(propPriceType)) {
					pricingCalculatorStep.setPrice(NumberUtil.formatDouble(NumberUtil.getHalfCentFormatedValue(pricingCalculatorStep.getBasePrice()), true));
					pricingCalculatorStep.setAppliedFiveCentsRule(ConstantStrings.YES);
				}*/ if ("Gross".equalsIgnoreCase(propPriceType)) {
					Double basePrice = proposalService.applyAgencyMargin(ConstantStrings.GROSS, pricingCalculatorStep.getBasePrice(), propAgencyMargin);
					pricingCalculatorStep.setPrice(NumberUtil.formatDouble(basePrice, true));
					pricingCalculatorStep.setAppliedFiveCentsRule(ConstantStrings.NO);
				}
			}
			if (StringUtils.isNotBlank(pricingCalculatorStep.getWeightedBasePrice())) {
				pricingCalculatorStep.setWeightedBasePrice(NumberUtil.formatDouble(Double.valueOf(pricingCalculatorStep.getWeightedBasePrice()), true));
			}
			setRateProfileSummary(pricingCalculatorStep);
		}
		return pricingCalculatorStep;
	}

	/**
	 * Set rate profile summary data
	 * @param pricingCalculatorStep
	 */
	private void setRateProfileSummary(final PricingCalculatorSummary pricingCalculatorStep) {
		final List<RateProfileSummary> rateProfileSummary = pricingCalculatorStep.getRateProfileSummary();
		for (RateProfileSummary rateProfile : rateProfileSummary) {
			if (StringUtils.isNotBlank(rateProfile.getBasePrice())) {// For old line item when discount functionality is not available
				rateProfile.setDefaultBasePrice(getFormatedValue(ConstantStrings.EMPTY_STRING));
				rateProfile.setDefaultDiscount(getFormatedValue(ConstantStrings.EMPTY_STRING));
				rateProfile.setDefaultdiscountBP(getFormatedValue(ConstantStrings.EMPTY_STRING));
				rateProfile.setSalesCategoryBasePrice(getFormatedValue(ConstantStrings.EMPTY_STRING));
				rateProfile.setSalesCategoryDiscount(getFormatedValue(ConstantStrings.EMPTY_STRING));
				rateProfile.setSalesCategorydiscountBP(getFormatedValue(rateProfile.getBasePrice()));
			} else {
				rateProfile.setDefaultBasePrice(getFormatedValue(rateProfile.getDefaultBasePrice()));
				rateProfile.setDefaultDiscount(getFormatedValue(rateProfile.getDefaultDiscount()));
				rateProfile.setDefaultdiscountBP(getFormatedValue(rateProfile.getDefaultdiscountBP()));
				rateProfile.setSalesCategoryBasePrice(getFormatedValue(rateProfile.getSalesCategoryBasePrice()));
				rateProfile.setSalesCategoryDiscount(getFormatedValue(rateProfile.getSalesCategoryDiscount()));
				rateProfile.setSalesCategorydiscountBP(rateProfile.getSalesCategorydiscountBP());
			}
		}
	}

	/**
	 * Set json data into PricingCalculatorSummary
	 * @param json
	 * @return
	 */
	private PricingCalculatorSummary getNode(final String json) {
		PricingCalculatorSummary pricingCalculatorSummary = null;
		try {
			if (StringUtils.isNotBlank(json)) {
				pricingCalculatorSummary = new ObjectMapper().readValue(json , PricingCalculatorSummary.class);
			}
		} catch (JsonProcessingException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing jsonString of pricing calculation step summary");
			}
		} catch (IOException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing jsonString of pricing calculation step summary");
			}
		}
		return pricingCalculatorSummary;
	}

	/**
	 * @param value
	 * @return
	 */
	private String getFormatedValue(final String value) {
		String formatedValue = ConstantStrings.NA;
		if (StringUtils.isNotBlank(value)) {
			formatedValue = NumberUtil.formatDouble(Double.valueOf(value), true);
		}
		return formatedValue;
	}

	/**
	 * @param currencyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getConversionRate")
	public AjaxFormSubmitResponse getCurrencyConversionRate() {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		Map<String,Double> conversionRateMap = new HashMap<String, Double>();
		try {
			conversionRateMap = proposalSOSService.getCurrencyConversionRate();
		} catch (ParseException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing the start/end date calculated for fetching the period id for currency conversion");
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, conversionRateMap);
		return ajaxResponse;
	}

	/**
	 * @param proposalId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/refreshConversionRate")
	public AjaxFormSubmitResponse refreshCurrencyConversionRate(@RequestParam("proposalId") final long proposalId){
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		Map<String,Double> conversionRateMap = new HashMap<String, Double>();
		try {
			conversionRateMap = proposalSOSService.getCurrencyConversionRate();
		} catch (ParseException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing the start/end date calculated for fetching the period id for currency conversion");
			}
		}
		Proposal proposal = proposalService.updateProposalConversionRate(proposalId, conversionRateMap);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, conversionRateMap.get(proposal.getCurrency()));
		ajaxResponse.getObjectMap().put(Constants.VERSION, proposal.getVersion());
		return ajaxResponse;
	}

	/**
	 * Get List of Audits by ProposalId
	 * @param proposalId
	 * @return
	 */
	@RequestMapping("/getAuditHistory")
	public ModelAndView getAuditHistory(@RequestParam("proposalId") final long proposalId) {
		final ModelAndView view = new ModelAndView("manageProposalAuditHistory");
		final List<AuditForm> auditHistoryLst = new ArrayList<AuditForm>();
		final List<Audit> auditLst = auditService.getAuditsByParentId(proposalId);
		for (Audit audit : auditLst) {
			AuditForm auditForm = new AuditForm();
			auditForm.populateForm(audit);
			auditHistoryLst.add(auditForm);
		}
		view.addObject("auditHistoryLst", auditHistoryLst);
		return view;
	}

	/**
	 * Saves the notes of the proposals.
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveNotes")
	public AjaxFormSubmitResponse saveNotes(final NotesForm form) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		if (new ProposalValidator().validateProposalNotes(form.getNotesDescription(), ajaxResponse)) {
			return ajaxResponse;
		}
		final Notes notes = new Notes();
		form.populate(notes);
		final Notes returnedNotes = proposalService.saveNotes(notes.getProposalId(), notes.getId(), notes);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnedNotes);
		ajaxResponse.getObjectMap().put("notesHeader", "Notes updated on " + DateUtil.getGuiDateTimeString(returnedNotes.getModifiedDate().getTime()) + 
				" by " + returnedNotes.getCreatedByUserName() + " (" + returnedNotes.getRole() + ")");
		return ajaxResponse;
	}

	/**
	 * Returns the list of all the active notes of a given proposal
	 * @param form
	 * @return
	 */
	@RequestMapping("/getProposalNotes")
	public ModelAndView getProposalNotes(@RequestParam("proposalId") final long proposalId) {
		final ModelAndView view = new ModelAndView("manageProposalNotes");
		final List<NotesForm> proposalNotesLst = new ArrayList<NotesForm>();
		final List<Notes> notesLst = proposalService.getProposalNotes(proposalId);
		for (Notes notes : notesLst) {
			NotesForm proposalNotesForm = new NotesForm();
			proposalNotesForm.populateForm(notes);
			proposalNotesLst.add(proposalNotesForm);
		}
		view.addObject("proposalNotesLst", proposalNotesLst);
		return view;
	}

	/**
	 * Deletes the notes with the given Id.
	 * @param notesId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteNotes")
	public AjaxFormSubmitResponse deleteNotes(@RequestParam("proposalId") final long proposalId, @RequestParam("notesId") final long notesId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final long id = proposalService.deleteNotes(proposalId, notesId);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, id);
		return ajaxResponse;
	}

	/**
	 * Submit for pricing review when user is planner and proposal is 'In-Progress'
	 * @param proposalId
	 * @return
	 * @throws CustomCheckedException
	 */
	@ResponseBody
	@RequestMapping("/submitForPricingReview")
	public AjaxFormSubmitResponse submitProposalForPricingReview(final HttpServletRequest request, @RequestParam("proposalId") final long proposalId, @RequestParam("pricingNotes") final String pricingNotes) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal proposal = proposalService.getProposalbyId(proposalId);
		final Audit audit = auditService.createAuditForPricingReview(proposal);
		final ArrayList<String> roles = new ArrayList<String>();
		if (proposal.getAssignedUser() == null && !(ProposalStatus.DELETED.name().equalsIgnoreCase(proposal.getProposalStatus().getDisplayName()))) {
			ajaxResponse.getObjectMap().put("isUserNullAndStatusNotDeleted", true);
			return ajaxResponse;
		} else {
			ajaxResponse.getObjectMap().put("isUserNullAndStatusNotDeleted", false);
		}
		if (audit != null && audit.getId() > 0) {
			final StringBuffer subject = new StringBuffer(Constants.WORKFLOW_EMAIL_KEYWORD);
			subject.append(" Proposal '");
			subject.append(proposal.getName());
			subject.append("' (Sales Category '");
			subject.append(proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()));
			subject.append("') sent for Pricing Review");
			String textMsg = "Proposal Id - " + proposal.getId() + "\t\n";
			textMsg += creatTextMsg(proposal, request);
			if (StringUtils.isNotBlank(pricingNotes)) {
				textMsg += "\t\n\n\nAdditional Notes : " + pricingNotes;
			}
			roles.add(SecurityUtil.PRICING_ADMIN);
			sendMailToUser(proposal, subject.toString(), textMsg, roles);
		}
		proposal.setWithPricing(true);
		proposal.setPricingSubmittedDate(DateUtil.getCurrentDate());
		proposalService.saveProposal(proposal.getId(), proposal);
		return ajaxResponse;
	}

	/**
	 * Submit back to planner when user is pricing admin and  proposal is 'In-Progress'
	 * @param proposalId
	 * @return
	 * @throws IOException
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/submitBackToPlanner")
	public AjaxFormSubmitResponse submitProposalBackToPlanner(final HttpServletRequest request, final HttpServletResponse response, @RequestParam("proposalId") final long proposalId,
			@RequestParam("reviewedOptionsIds") final String reviewedOptionsIds, final ProposalForm proposalForm) throws IOException, CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		Map<Long, ProposalForm> proposalOptionsMap = new HashMap<Long, ProposalForm>();
		final CustomBindingResult results = new CustomBindingResult("proposalForm" , proposalForm);
		if (StringUtils.isNotBlank(proposalForm.getOptionThresholdValueString())) {
			proposalOptionsMap = convertJsonToObject(proposalForm.getOptionThresholdValueString());
			new ProposalValidator().validateOptionsThresholdValue(proposalOptionsMap, results);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}

		final Proposal proposal = proposalService.getProposalbyId(proposalId);
		final Set<ProposalOption> optionsSet =  proposal.getProposalOptions();
		if (!(optionsSet.isEmpty())) {
			for (ProposalOption proposalOption : optionsSet) {
				if (proposalOptionsMap.containsKey(proposalOption.getId())) {
					proposalOption.setThresholdLimit(Double.valueOf(proposalOptionsMap.get(proposalOption.getId()).getThresholdLimit().replaceAll(",","")));
				}
			}
		}

		proposalService.saveOptionsThresholdValue(optionsSet);
		final Audit audit = auditService.createAuditForProposalBackToPlanner(proposal);
		final List<String> roles = new ArrayList<String>();
		final String[] optionIds = reviewedOptionsIds.split(ConstantStrings.COMMA);
		if (audit != null && audit.getId() > 0) {
			StringBuffer subject = new StringBuffer(Constants.WORKFLOW_EMAIL_KEYWORD);
			subject.append(" Proposal '");
			subject.append(proposal.getName());
			subject.append("' (Sales Category '");
			subject.append(proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()));
			subject.append("') submitted back from Pricing");
			String textMsg = "Proposal Id - " + proposal.getId() + "\t\n";
			textMsg += creatTextMsg(proposal, request);
			roles.add(SecurityUtil.MEDIA_PLANNER);
			roles.add(SecurityUtil.ADMIN);
			//Creating template workbook and attaching with mail
			sendMailToUser(proposal, subject.toString(), textMsg, roles, 
					new ByteArrayDataSource(createZipOfWorkbooks(getWorkbookForEmail(optionIds)), "application/zip"), proposal.getName().replaceAll("/", "_") + ".zip");
		}
		
		if (!optionsSet.isEmpty()) {
			for (ProposalOption proposalOption : optionsSet) {
				if (proposalOptionsMap.containsKey(proposalOption.getId())) {
					pricingStatusCalculatorService.addThreshHoldCheck(proposalId);
					pricingStatusCalculatorService.addAddedValueCheck(proposalId);
				}
			}
		}
		pricingStatusCalculatorService.updatePricingStatusAsPricingApproved(proposalId, StringUtil.convertStringArrayToLongArray(optionIds));
		proposal.setWithPricing(false);
		proposalService.saveProposal(proposal.getId(), proposal);
		return ajaxResponse;
	}

	/**
	 * @param workBookList
	 * @return
	 * @throws IOException
	 */
	private byte[] createZipOfWorkbooks(final Map<TemplateWorkBook, String> workBookList) throws IOException {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final ZipOutputStream zipOutputStream = new ZipOutputStream(bout);
		try {
			for (final Map.Entry<TemplateWorkBook, String> entry : workBookList.entrySet()) {
				zipOutputStream.putNextEntry(new ZipEntry(entry.getValue() + entry.getKey().getTemplateFileName().substring(entry.getKey().getTemplateFileName().lastIndexOf(ConstantStrings.DOT))));
				final ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					entry.getKey().getTemplateWorkBook().write(bos);
				} finally {
				    bos.close();
				}
				zipOutputStream.write(bos.toByteArray());
			}
		} finally {
			zipOutputStream.close();
		}
		return bout.toByteArray();
	}

	/**
	 * @param optionIds
	 * @return
	 */
	private Map<TemplateWorkBook, String> getWorkbookForEmail(final String[] optionIds) {
		final Long templateId = templateService.getTemplateIdByName(ConstantStrings.NYT_TIMES_TEMPLATE);
		final Map<TemplateWorkBook, String> templateWorkBooks = new HashMap<TemplateWorkBook, String>();
		final List<ProposalOption> optionLists = proposalService.getOptionLstbyIds(StringUtil.convertStringArrayToLongArray(optionIds));
		for (final ProposalOption propOption : optionLists) {
			if (templateId != null) {
				final TemplateWorkBook templateWorkBook =  templateGenerator.generateTemplate(propOption.getId(), propOption.getLatestVersion().getProposalVersion(), templateId);
				if (templateWorkBook != null) {
					if(propOption.isDefaultOption()) {
						templateWorkBooks.put(templateWorkBook, propOption.getName() + "-Default");
					} else {
						templateWorkBooks.put(templateWorkBook, propOption.getName());
					}
				}
			}
		}
		return templateWorkBooks;
	}

	/**
	 * @param proposal
	 * @return
	 */
	private String creatTextMsg(final Proposal proposal, final HttpServletRequest request) {
		final StringBuffer textMsg = new StringBuffer();
		final StringBuffer requestedUrl = request.getRequestURL();
		final String servletPath = request.getServletPath();
		final String url = "/manageProposal.action?_flowId=workOnProposal&proposalId=" + proposal.getId();
		textMsg.append("\n\n" + requestedUrl.substring(0, requestedUrl.indexOf(servletPath)) + url);
		return textMsg.toString();
	}

	/**
	 * @param proposal
	 * @param subject
	 * @param textMsg
	 */
	private void sendMailToUser(final Proposal proposal, final String subject, final String textMsg, final List<String> roles) {
		final User user = proposal.getAssignedUser();
		final String mailFrom = user.getEmail();
		final StringBuffer mail = new StringBuffer();
		mail.append(mailFrom);
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[0]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (!userDB.getEmail().equals(mailFrom)) {
					mail.append(ConstantStrings.COMMA);
					mail.append(userDB.getEmail());
				}
			}
			final Map<String, String> mailProps = getMailInfo(mail.toString(), subject, mailFrom, textMsg);
			mailUtil.sendMail(mailUtil.setMessageInfo(mailProps));
		}
	}

	/**
	 * @param proposal
	 * @param subject
	 * @param textMsg
	 * @param roles
	 * @param ds
	 * @param attachmentName
	 */
	private void sendMailToUser(final Proposal proposal, final String subject, final String textMsg, final List<String> roles, final DataSource dataSource, final String attachmentName) {
		final User user = proposal.getAssignedUser();
		final String mailFrom = user.getEmail();
		final StringBuffer mail = new StringBuffer();
		mail.append(mailFrom);
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[0]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if (!userDB.getEmail().equals(mailFrom)) {
					mail.append(ConstantStrings.COMMA);
					mail.append(userDB.getEmail());
				}
			}
			final Map<String, String> mailProps = getMailInfo(mail.toString(), subject, mailFrom, textMsg);
			mailUtil.sendMail(mailUtil.setMessageInfo(mailProps, dataSource, attachmentName));
		}
	}

	/**
	 * @param mailTo
	 * @param subject
	 * @param mailFrom
	 * @param textMsg
	 * @return
	 */
	private Map<String, String> getMailInfo(final String mailTo, final String subject, final String mailFrom, final String textMsg) {
		final Map<String, String> mailProps = new HashMap<String, String>();
		mailProps.put("mailTo", mailTo);
		mailProps.put("subject", subject);
		mailProps.put("mailFrom", mailFrom);
		mailProps.put("textMsg", textMsg);
		mailProps.put("cc", SecurityUtil.getUser().getEmail());
		return mailProps;
	}

	/**
	 * Fetching LineItems for given proposal Id
	 * @param proposalId
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/getAllReservedLineItemsForProposal")
	public ModelAndView getAllReservedLineItemsForProposal(@RequestParam("proposalId") final long proposalId, @ModelAttribute final TableGrid<LineItemForm> tblGrid) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching LineItems for proposal with Id: " + proposalId);
		}
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalId);
		final List<Long> proposalVersionIdLst = new ArrayList<Long>(optionLst.size());
		for (ProposalOption option : optionLst) {
			proposalVersionIdLst.add(option.getLatestVersion().getId());
		}
		final List<LineItem> lineItemLst = proposalService.getAllReservedLineItemsForProposal(proposalId, proposalVersionIdLst, tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid
				.getSortingCriteria());
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			Map<Long,String> productMap = proposalHelper.getReservableProducts(true);
			Map<Long,String> salesTargetMap = proposalHelper.getSalesTarget();
			Map<Long, String> productsClassMap = proposalHelper.getProductClass();
			final List<LineItemForm> lineItemFormLst = new ArrayList<LineItemForm>(lineItemLst.size());
			for (LineItem proposalLineItem : lineItemLst) {
				final LineItemForm lineItemForm = new LineItemForm();
				lineItemForm.populateForm(proposalLineItem);
				lineItemForm.setOptionName(proposalLineItem.getProposalVersion().getProposalOption().getName());
				lineItemForm.setOptionId(String.valueOf(proposalLineItem.getProposalVersion().getProposalOption().getId()));
				lineItemForm.setProduct_active(productMap.containsKey(proposalLineItem.getSosProductId()) ? Constants.YES : Constants.NO);
				lineItemForm.setSalesTarget_active(salesTargetMap.containsKey(proposalLineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId()) ? Constants.YES : Constants.NO);
				final List<LineItemTarget> lineItemTargetLst = new ArrayList<LineItemTarget>(proposalLineItem.getGeoTargetSet());
				lineItemForm.setLineItemTargetingData(targetJsonConverter.convertObjectToJson(lineItemTargetLst));
				lineItemForm.setSosProductClassName(productsClassMap.get(proposalLineItem.getSosProductClass()));
				lineItemFormLst.add(lineItemForm);
			}
			final int lineItemCount = proposalService.getReservedLineItemsCount(proposalId, proposalVersionIdLst);
			tblGrid.setGridData(lineItemFormLst, lineItemCount);
		}
		final ModelAndView view = new ModelAndView(ConstantStrings.PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}


	/**
	 * Update line item reservations data
	 * @param request
	 * @param proposalID
	 * @param lineItemID
	 * @param expiryDate
	 * @return
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/updateReservationsExpiryDate")
	public AjaxFormSubmitResponse updateReservationsExpiryDate(final HttpServletResponse response, final HttpServletRequest request, @RequestParam("proposalID") final long proposalID, @RequestParam("lineItemID") final long lineItemID,
			@RequestParam("expiryDate") final String expiryDate) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final LineItem oldLineItem = proposalService.getLineItemById(lineItemID);
		List<Long> pastReservationLineItems = new ArrayList<Long>();
		final CustomBindingResult results = new CustomBindingResult("renewDateValidation", pastReservationLineItems);
		new PackageLineItemsFormValidator().validateReservationsRenewDate(DateUtil.getGuiDateString(oldLineItem.getStartDate()), expiryDate,oldLineItem.getProductType().getShortName(),results);
		if(results.hasErrors()){
			ajaxResponse.getObjectMap().put(Constants.RESERVATIONS, pastReservationLineItems);
			return constructResponse(response, ajaxResponse, results);
		}
		LineItem newLineItem = null;
		LineItemReservations reservation = null;
		if (oldLineItem != null) {
			reservation = proposalService.updateReservations(proposalID, lineItemID, DateUtil.parseToDate(expiryDate));
			newLineItem = proposalService.getLineItemById(lineItemID);
			proposalHelper.sendMailForReservation(request, oldLineItem, newLineItem);
			
			LineItemForm lineItemForm = new LineItemForm();
			lineItemForm.populateForm(newLineItem);
			lineItemForm.setProposalID(String.valueOf(proposalID));
			Map<String,Object> sorMap = calculateSORWithLineItems(lineItemForm, lineItemForm.getProductType());
			 final String calculateSOR = sorMap.containsKey("SOR") ? (String)sorMap.get("SOR") : "100";
			if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) ) {
				if(NumberUtil.doubleValue(lineItemForm.getSor()) > NumberUtil.doubleValue(calculateSOR)) {					
					proposalHelper.sendMailForOverBooking(newLineItem, sorMap);
				}
			}else if(LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType()) ) {
				if(EmailReservationStatus.RESERVED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.OVERBOOKED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.SOLD.getDisplayName().equals(calculateSOR)) {
					proposalHelper.sendMailForOverBooking(newLineItem, sorMap);
				}
			}
			
			
		}
		ajaxResponse.getObjectMap().put("lineItemID", lineItemID);
		ajaxResponse.getObjectMap().put("lastRenewedOn", DateUtil.getGuiDateString((reservation != null) ? reservation.getLastRenewedOn() : DateUtil.getCurrentDate()));
		return ajaxResponse;
	}

	/**
	 * Get reservation data by lineItemID
	 * @param proposalID
	 * @param optionID
	 * @return
	 */
	@RequestMapping("/getProposalReservationOption")
	public ModelAndView getProposalReservationOption(@RequestParam("proposalID") final long proposalID, @RequestParam("optionID") final long optionID) {		
		final ModelAndView view = new ModelAndView("proposalReservationOption");
		view.addObject("proposalOptions", getOptionData(proposalID));
		view.addObject("currentOption", optionID);
		return view;
	}

	/**
	 * Get all option details in a proposal
	 * @param proposalID
	 * @param optionID
	 * @return
	 */
	@RequestMapping("/getProposalOptionDetails")
	public ModelAndView getProposalOptionDetails(@RequestParam("proposalID") final long proposalID, @RequestParam("optionID") final long optionID) {
		final ModelAndView view = new ModelAndView("proposalReservationOption");
		view.addObject("proposalOptions", getOptionData(proposalID));
		view.addObject("currentOption", optionID);
		view.addObject("copyInOtherOption", true);
		return view;
	}

	/**
	 * Get proposal option data
	 * @param proposalID
	 * @return
	 */
	private List<ProposalForm> getOptionData(final long proposalID) {
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalID);
		final List<ProposalForm> proposalFormLst = new ArrayList<ProposalForm>(optionLst.size());
		for (ProposalOption option : optionLst) {
			final ProposalForm propForm = new ProposalForm();
			propForm.setDefaultOption(Boolean.toString(option.isDefaultOption()));
			propForm.setOptionId(option.getId());
			propForm.setOptionName(option.getName());
			propForm.setBudget(option.getBudget() == null ? ZERO : NumberUtil.formatDouble(option.getBudget(), true));
			propForm.setNetCpm(option.getLatestVersion().getEffectiveCpm() == null ? ZERO : NumberUtil.formatDouble(option.getLatestVersion().getEffectiveCpm(), true));
			propForm.setNetImpressions(option.getLatestVersion().getImpressions() == null ? ZERO : NumberUtil.formatLong(option.getLatestVersion().getImpressions(), true));
			proposalFormLst.add(propForm);
		}
		Collections.sort(proposalFormLst, new OptionNameComparator());
		return proposalFormLst;
	}

	/**
	 * Move line item reservation data into given option and delete from existing line item
	 * @param newOptionId
	 * @param lineItemID
	 * @return
	 * @throws CustomCheckedException 
	 */
	@ResponseBody
	@RequestMapping("/moveReservationData")
	public AjaxFormSubmitResponse moveReservationData(@RequestParam("proposalID") final long proposalID, @RequestParam("optionId") final long newOptionId,
			@RequestParam("lineItemId") final long lineItemID) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		proposalService.createAndMoveReservationData(proposalID, lineItemID, newOptionId);
		proposalService.updateAllLineItemPricingStatus(newOptionId);
		ProposalOption proposalOption = proposalService.getOptionbyId(newOptionId);
		proposalService.updateProposalVersionNetImpressionAndCPM(newOptionId, proposalOption.getLatestVersion().getProposalVersion());
		ajaxResponse.getObjectMap().put("lineItemID", lineItemID);
		return ajaxResponse;
	}

	/**
	 * @param proposalId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/createOrder")
	public AjaxFormSubmitResponse createOrder(@RequestParam("proposalId") final Long proposalId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating Order");
		}
		LOGGER.info("Creating Order for ProposalID: " + proposalId);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Map<String, Set<LineItemTarget>> missedTargetingMap = sosIntegrationService.createOrder(proposalId);
		final Proposal returnProposal = proposalService.getProposalForClone(proposalId);
		final User user = SecurityUtil.getUser();
		if (!missedTargetingMap.isEmpty()) {
			String mailSubject = "SOS ORDER CREATED FROM AMPT : Order Id - " + returnProposal.getSosOrderId() + ", Proposal Id - " + proposalId;
			Map<String, String> mailProps = getMailInfo(user.getEmail(), mailSubject , emailFrom, CreateTextMsgForSosIntegration(missedTargetingMap));
			mailUtil.sendMail(mailUtil.setMessageInfo(mailProps));
		}
		ajaxResponse.getObjectMap().put(Constants.VERSION, returnProposal.getVersion());
		ajaxResponse.getObjectMap().put(ConstantStrings.ALL_TARGETING_PUSHED, missedTargetingMap.isEmpty());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalId);
		return ajaxResponse;
	}

	/**
	 * @param advertiserName
	 * @param page_limit
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAdvertiser")
	public List<KeyValuePairPojo> getAdvertiser(@RequestParam("name") final String advertiserName, @RequestParam("page_limit") final int page_limit) {
		return proposalHelper.getAdvertiserByName(advertiserName);
	}

	/**
	 * @param advertiserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAdvertiserById")
	public KeyValuePairPojo getAdvertiserById(@RequestParam("id") final Long advertiserId) {
		final Advertiser advertiser = proposalSOSService.getAdvertiserById(advertiserId);
		return new KeyValuePairPojo(advertiserId, (advertiser == null) ? ConstantStrings.EMPTY_STRING : advertiser.getName());
	}

	/**
	 * Get key advertiser for given advertiser id
	 * @param advertiserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/isSpecialAdvertiser")
	public AjaxFormSubmitResponse getAdvertiserTypeById(@RequestParam("advertiserId") final Long advertiserId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Advertiser advertiser = proposalSOSService.getAdvertiserById(advertiserId);
		boolean returnVal = false;
		if (advertiser != null) {
			if ('Y' == advertiser.getSpecialAdvertiser() && isAdvertiserNotExpired(advertiser)) {
				returnVal = true;
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnVal);
		return ajaxResponse;
	}

	/**
	 * @param advertiser
	 * @return
	 */
	private boolean isAdvertiserNotExpired(final Advertiser advertiser) {
		boolean isNotExpired = false;
		if (advertiser.getSpecialExpiryDate() == null) {
			isNotExpired = true;
		} else if (advertiser.getSpecialExpiryDate().getTime() > DateUtil
					.getCurrentDate().getTime()) {
				isNotExpired = true;
		}
		return isNotExpired;
	}

	/**
	 * @param agencyName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAgency")
	public List<KeyValuePairPojo> getAgency(@RequestParam(ConstantStrings.NAME) final String agencyName) {
		return proposalHelper.getAgencyByName(agencyName);
	}

	/**
	 * @param agencyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAgencyById")
	public KeyValuePairPojo getAgencyById(@RequestParam("id") final Long agencyId) {
		final Agency agency = proposalSOSService.getAgencyById(agencyId);
		return new KeyValuePairPojo(agencyId, (agency == null) ? ConstantStrings.EMPTY_STRING : agency.getName());
	}

	/**
	 * to get maximum Offered Impression that can be given
	 * @param lineItemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOffImpressions")
	public AjaxFormSubmitResponse getOffImpressions(@RequestParam("rateCardPrice")  final String rateCardPrice , @RequestParam("optionId") final Long optionId , @RequestParam("lineItemId") final Long lineItemId){
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE,
				NumberUtil.formatLong((long) (Math.floor(pricingStatusCalculatorService.getOffImpressions(NumberUtil.doubleValue(rateCardPrice), optionId, lineItemId))), true));
		return  ajaxResponse;
	}

	/**
	 * To Review Proposal options.
	 * @param proposalForm
	 * @return
	 */
	@RequestMapping("/getOptionThresHoldValuePage")
	public ModelAndView getOptionThresHoldValuePage(final ProposalForm proposalForm) {
		final List<ProposalOption> optionLst = proposalService.getProposalOptionsById(proposalForm.getId());
		final List<ProposalForm> proposalFormLst = new ArrayList<ProposalForm>(optionLst.size());
		Double totalInvestment = 0D;
		for (ProposalOption option : optionLst) {
			final ProposalForm propForm = new ProposalForm();
			propForm.setDefaultOption(Boolean.toString(option.isDefaultOption()));
			propForm.setOptionId(option.getId());
			propForm.setOptionName(option.getName());
			final Set<LineItem> lineItems = option.getLatestVersion().getProposalLineItemSet();
			totalInvestment = pricingStatusCalculatorService.getInvstmntOfQulfyngLI(lineItems , true);
			propForm.setQualifingLineItemInvestment(String.valueOf(NumberUtil.formatDouble(totalInvestment, true)));
			Double thresholdValue = totalInvestment - ((totalInvestment * Double.valueOf(pricingStatusCalculatorService.getTHRESHOLD_PERCENT()))/100);
			propForm.setThresholdLimit(String.valueOf(NumberUtil.formatDouble(thresholdValue, true)));
			propForm.setLastPricingReviewedDate(option.getLastReviewedDate() == null ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateTimeString(option.getLastReviewedDate()));
			proposalFormLst.add(propForm);
		}

		final ModelAndView view = new ModelAndView(ConstantStrings.OPTION_THRESHOLD_VALUE_PAGE);
		Collections.sort(proposalFormLst, new OptionNameComparator());
		view.addObject("proposalOptions", proposalFormLst);
		return view;
	}


	/**
	 * Move line item reservation data into given option and delete from existing line item
	 * @param newOptionId
	 * @param lineItemID
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping("/saveOptionThresholdValueData")
	public AjaxFormSubmitResponse saveOptionThresholdValueData(HttpServletResponse response, ProposalForm proposalForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		Set<ProposalForm> proposalOptionsSet = null;
		final CustomBindingResult results = new CustomBindingResult("proposalForm" , proposalForm);
		if(StringUtils.isNotBlank(proposalForm.getOptionThresholdValueString())){
			//proposalOptionsSet = convertJsonToObject(proposalForm.getOptionThresholdValueString());
			//new ProposalValidator().validateOptionsThresholdValue(proposalOptionsSet, results);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}
		return ajaxResponse;
	}*/

	/**
	 * Converts the JSON String of the discount into the discount form object.
	 * @param jsonString
	 * @return
	 */
	public static Map<Long, ProposalForm> convertJsonToObject(final String jsonString) {
		final Map<Long, ProposalForm> proposalOptionMap = new HashMap<Long, ProposalForm>();
		final ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing discounts of a List Rate Profile");
			}
		} catch (IOException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing Targeting of a List Rate Profile");
			}
		}
		for (JsonNode discount : node) {
			ProposalForm proposalOption = new ProposalForm();
			proposalOption.setOptionId(Long.valueOf(discount.findValue("optionId").getTextValue()));
			proposalOption.setQualifingLineItemInvestment(discount.findValue("qualifingLineItemInvestment").getTextValue());
			proposalOption.setThresholdLimit(discount.findValue("thresholdLimit").getTextValue());
			proposalOptionMap.put(Long.valueOf(discount.findValue("optionId").getTextValue()), proposalOption);
		}
		return proposalOptionMap;
	}

	/**
	 * Save notes in sales forces as well as in ampt
	 * @param form
	 * @return
	 * @throws CustomCheckedException
	 */
	@ResponseBody
	@RequestMapping("/saveNotesInSalesForce")
	public AjaxFormSubmitResponse saveNotesInSalesForce(final NotesForm form) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		if (new ProposalValidator().validateProposalNotes(form.getNotesDescription(), ajaxResponse)) {
			return ajaxResponse;
		}
		Notes notes = new Notes();
		form.populate(notes);
		Proposal proposal = proposalService.getProposalbyId(notes.getProposalId());
		if (StringUtils.isNotBlank(proposal.getSalesforceID())) {
			auditService.salesforceAuditLog(proposal, new StringBuilder(notes.getDescription()));
			Notes returnedNotes = proposalService.saveNotes(notes.getProposalId(), notes.getId(), notes);
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnedNotes);
			ajaxResponse.getObjectMap().put("notesHeader", "Notes updated on " + DateUtil.getGuiDateTimeString(returnedNotes.getModifiedDate().getTime()) + " by " + returnedNotes.getCreatedByUserName() + " (" + returnedNotes.getRole()
							+ ")" + " | Pushed to SalesForce.");
		}
		return ajaxResponse;
	}

	/**
	 * @param proposalForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAllLineItemPricingStatus")
	public AjaxFormSubmitResponse updateAllLineItemPricingStatus(final ProposalForm proposalForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Update all line item pricing status for selected option latest version");
		}
		LOGGER.info("Update pricing status for : " + proposalForm.getOptionId());
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		proposalService.updateAllLineItemPricingStatus(proposalForm.getOptionId());
		return ajaxResponse;
	}

	/**
	 * @param packageId
	 * @param positionToMove
	 * @param lineItemsToMove
	 */
	@ResponseBody
	@RequestMapping("/arrangeLineItemSequence")
	public void arrangeLineItemSequence(@RequestParam(required = true) final long optionId, @RequestParam(required = true) final long proposalVersion,
			@RequestParam(required = true) final int positionToMove, @RequestParam(required = true) final String lineItemsToMove) {
		List <LineItem> lineItemLst =  proposalService.getProposalLineItems(optionId, proposalVersion, null, null, null);
		proposalHelper.arrangeLineItemSequence(lineItemLst, StringUtil.convertStringToLongList(lineItemsToMove), positionToMove);
		proposalService.updateLineItemsOfProposal(lineItemLst);
	}

	/**
	 * @param sosProductId
	 * @param sosSalesTargetId
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/getEmailAvailableDates")
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<String> getEmailAvailableDates(final String sosProductId, final String sosSalesTargetId) {
		if (StringUtils.isNotBlank(sosProductId) && StringUtils.isNotBlank(sosSalesTargetId)) {
			final List<FilterCriteria> filterCriteriaLst = new ArrayList<FilterCriteria>();
			filterCriteriaLst.add(new FilterCriteria("productId", sosProductId, SearchOption.EQUAL.toString()));
			filterCriteriaLst.add(new FilterCriteria("salesTargetId", sosSalesTargetId, SearchOption.EQUAL.toString()));

			final List<Date> datesList = new ArrayList<Date>();
			final List<EmailSchedule> emailScheduleLst = emailScheduleService.getFilteredEmailDetailList(filterCriteriaLst, null, null);
			for (EmailSchedule emailSchedule : emailScheduleLst) {
				for (EmailScheduleDetails scheduleDetails : emailSchedule.getEmailSchedules()) {
					datesList.addAll(DateUtil.getAvailableDates(scheduleDetails));
				}
			}
			return DateUtil.convertDatesToStringFormat(datesList);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Sends Email for targeting not pushed to SOS
	 * @param missedTargetingMap
	 * @return
	 */
	private String CreateTextMsgForSosIntegration(final Map<String, Set<LineItemTarget>> missedTargetingMap) {
		LOGGER.info("Creating text message for Target types not pushed to SOS");
		final StringBuffer textMsg = new StringBuffer();
		final Map<Long, String> targetTypeMap = proposalHelper.getTargetTypeCriteria();
		for (Iterator<String> iterator = missedTargetingMap.keySet().iterator(); iterator.hasNext();) {
			String uniqueId = (String) iterator.next();
			String ids[] = uniqueId.split("~");
			textMsg.append("AMPT LineItemId : ");
			textMsg.append(ids [0]);
			textMsg.append("\t\n");
			textMsg.append("SOS LineItemId : ");
			textMsg.append(ids [1]);
			textMsg.append("\t\n\t\n");
			String notTargetTypes = ConstantStrings.EMPTY_STRING;
			String targetTypes = ConstantStrings.EMPTY_STRING;
			Map<Long, String> formatEmail = new LinkedHashMap<Long, String>();
			for (LineItemTarget targetType : missedTargetingMap.get(uniqueId)) {
				if (ConstantStrings.NOT.equalsIgnoreCase(targetType.getNegation())) {
					notTargetTypes = ConstantStrings.NOT + ConstantStrings.SPACE + targetTypeMap.get(targetType.getSosTarTypeId()) + ConstantStrings.SPACE + ConstantStrings.COLON
							+ ConstantStrings.SPACE;
					if (ZIP_CODES.equals(targetTypeMap.get(targetType.getSosTarTypeId())) || BEHAVIOURAL.equals(targetTypeMap.get(targetType.getSosTarTypeId()))) {
						notTargetTypes = notTargetTypes + targetType.getSosTarTypeElementId();
					} else {
						notTargetTypes = notTargetTypes + lineItemUtil.getTarTypeElementName(targetType);
					}
					if (formatEmail.containsKey(2L)) {
						notTargetTypes = formatEmail.get(2L) + "\t\n" + notTargetTypes;

					}
					formatEmail.put(2L, notTargetTypes);
				} else if (targetType.getSosTarTypeId() == 33 || targetType.getSosTarTypeId() == 36 || targetType.getSosTarTypeId() == 37 || targetType.getSosTarTypeId() == 38
						|| targetType.getSosTarTypeId() == 40 || targetType.getSosTarTypeId() == 41 || targetType.getSosTarTypeId() == 42) {
					targetTypes =  targetTypeMap.get(targetType.getSosTarTypeId()) + ConstantStrings.SPACE + ConstantStrings.COLON + ConstantStrings.SPACE;
					if (ZIP_CODES.equals(targetTypeMap.get(targetType.getSosTarTypeId())) || BEHAVIOURAL.equals(targetTypeMap.get(targetType.getSosTarTypeId()))) {
						targetTypes = targetTypes + targetType.getSosTarTypeElementId();
					} else {
						targetTypes = targetTypes + lineItemUtil.getTarTypeElementName(targetType);
					}
					if (formatEmail.containsKey(1L)) {
						targetTypes = formatEmail.get(1L) + "\t\n" + targetTypes;

					}
					formatEmail.put(1L, targetTypes);
				}

				if (ConstantStrings.OR.equalsIgnoreCase(targetType.getOperation())) {
					String targetString = targetType.getProposalLineItem().getTargetingString();
					targetString = targetString.replaceAll(ConstantStrings.SPACE + ConstantStrings.OR + ConstantStrings.SPACE, "\t\n"+ConstantStrings.OR+"\t\n");
					formatEmail.put(3L, targetString);
					break;
				}
			}
			if (formatEmail.containsKey(1L)) {
				textMsg.append("Following are the Targets which were not pushed to SOS :");
				textMsg.append("\t\n\t\n");
				textMsg.append(formatEmail.get(1L));
				textMsg.append("\t\n\t\n");
			}
			if (formatEmail.containsKey(2L)) {
				textMsg.append("Following are the  EXCLUSION Targets which were not pushed to SOS :");
				textMsg.append("\t\n\t\n");
				textMsg.append(formatEmail.get(2L));
				textMsg.append("\t\n\t\n");
			}
			if (formatEmail.containsKey(3L)) {
				textMsg.append("The line item uses OR in the targeting expression. Anything after OR has not been bridged to SOS :");
				textMsg.append("\t\n\t\n");
				textMsg.append("Targeting String :- ");
				textMsg.append("\t\n");
				textMsg.append(formatEmail.get(3L));
				textMsg.append("\t\n\t\n");
			}
			textMsg.append("\t\n\t\n");
		}
		return textMsg.toString();
	}

	@ResponseBody
	@RequestMapping("/getProductCreativesSpecTypeLst")
	public AjaxFormSubmitResponse getProductCreativesSpecTypeLst(@RequestParam(required = true) final long productId){
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final List<ProductCreativeAssoc> productCreativesLst = productService.getProductCreatives(productId);
		final String[] productCreativeSpecTypeArr = new String[productCreativesLst.size()];
		int index = 0;
		for (ProductCreativeAssoc productCreativeAssoc : productCreativesLst) {
			productCreativeSpecTypeArr[index] = productCreativeAssoc.getCreative().getType();
			index++;
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, productCreativeSpecTypeArr);
		return ajaxResponse;
	}

	/**
	 * Adding copied Line Items To other option
	 * @param lineItemForm
	 * @param fromOptionId
	 * @param toOptionId
	 * @param lineItemIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyAndPasteLineItem")
	public AjaxFormSubmitResponse copyAndPasteLineItem(final LineItemForm lineItemForm, @RequestParam("fromOptionId") final long fromOptionId, @RequestParam("toOptionId") final String toOptionId,
			@RequestParam("lineItemId") final String lineItemIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding copied Line Items To other option. copiedLineItemIds: " + lineItemIds);
		}
		LOGGER.info("adding copied Line Items To ther option. copiedLineItemIds: " + lineItemIds);
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final String[] toOptionIds = toOptionId.split(ConstantStrings.COMMA);
		final List<String> toOptionList = Arrays.asList(toOptionIds);
		final String[] lineItems = lineItemIds.split(ConstantStrings.COMMA);
		final Proposal proposalDBValue = proposalService.getProposalbyId(NumberUtil.longValue(lineItemForm.getProposalID()));
		proposalService.createLineItemToOtherOption(proposalDBValue, fromOptionId, toOptionList, StringUtil.convertStringArrayToLongArray(lineItems));
		for (String optionId : toOptionList) {
			for (ProposalOption proposalOption : proposalDBValue.getProposalOptions()) {
				if (Long.valueOf(optionId) == proposalOption.getId()) {
					proposalService.updateAllLineItemPricingStatus(Long.valueOf(optionId));
					proposalService.updateProposalVersionNetImpressionAndCPM(Long.valueOf(optionId), proposalOption.getLatestVersion().getProposalVersion());
				}
			}

		}
		return ajaxResponse;
	}

	@ResponseBody
	@RequestMapping("/mapSalesforceId")
	public AjaxFormSubmitResponse mapSalesforceId(@RequestParam("newSalesforceId") final String newSalesforceId, @RequestParam("proposalId") final long proposalId) throws CustomCheckedException,
			ConnectionException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		if (!new ProposalValidator().validateNewSalesforceMapping(newSalesforceId, ajaxResponse)) {
			final List<Media_Plan__c> mediaPlanLst = salesForceProposalService.getMediaPlanBySfId(newSalesforceId);
			if (mediaPlanLst.isEmpty()) {
				ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "sfid.doesnt.exist");
			} else if (mediaPlanLst.get(0).getSubmitted_Date__c() == null) {
				ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "sf.mediaplan.not.submitted");
			}
		}
		if (!ajaxResponse.getObjectMap().containsKey(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE)) {
			proposalService.saveNewSalesfoceId(proposalId, newSalesforceId);
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "SUCCESS");
		}
		return ajaxResponse;
	}
	
	@ResponseBody
	@RequestMapping("/sendReqMailForHomePageResrvtn")
	public AjaxFormSubmitResponse sendReqMailForHomePageResrvtn(final Long lineItemId) throws CustomCheckedException{
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final LineItem lineItem = proposalService.getLineItemById(lineItemId);
		Map<String, String> mailMap = proposalUtilService.sendMailForHomePageResrvtnRequest(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal());
		proposalUtilService.sendMail(mailMap);
		auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.REQUESTED);
		return ajaxResponse;
	}
	
	private boolean isReservationNew(LineItem lineItem_New, LineItem lineItem_Old ){
		if(lineItem_Old.getStartDate() == null || lineItem_Old.getEndDate() == null){
			return true;
		}else{
			Calendar c = Calendar.getInstance();
		    c.setTime(lineItem_New.getStartDate());
		    long newStartTime = c.getTimeInMillis();
			c.setTime(lineItem_New.getEndDate());
			long newEndTime = c.getTimeInMillis();
		    c.setTime(lineItem_Old.getStartDate());
			long oldStartTime = c.getTimeInMillis();
			c.setTime(lineItem_Old.getEndDate());
			long oldEndTime = c.getTimeInMillis();
			Long newSalesTargetId = lineItem_New.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId();
			Long oldSalesTargetId = lineItem_Old.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId();
			Long newProductId = lineItem_New.getSosProductId();
			Long oldProductId = lineItem_Old.getSosProductId();
			if((newStartTime != oldStartTime) || (oldEndTime != newEndTime) || !( newProductId.equals(oldProductId)) || !(newSalesTargetId.equals(oldSalesTargetId))){
				return true;
			}else{
				return false;
			}
		}
	}

	public void sendMailForHomeResrvtn(LineItem lineItem, Proposal proposal, String resrvtnOperation, String productClassName){
		String proposalOwnerRole = SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName();
		ReservationTO reservationVO = new ReservationTO();
		reservationVO.setEndDate(lineItem.getEndDate());
		reservationVO.setStartDate(lineItem.getStartDate());
		reservationVO.setProductId(lineItem.getSosProductId());
		reservationVO.setSalesTargetId(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId());
		reservationVO.setLineItemTargeting(lineItem.getGeoTargetSet());
		List<Proposal> proposalLst = reservationService.getProposedProposalsForCalendar(reservationVO);
		Set<String> toUserSet = new HashSet<String>();
		StringBuffer mailTo =  new StringBuffer();
		for (Proposal proposalObj : proposalLst) {
			if(proposalObj.getAssignedUser() != null && proposalObj.getAssignedUser().isActive() && !(ProposalStatus.DELETED.name().equals(proposalObj.getProposalStatus().name()) || ProposalStatus.EXPIRED.name().equals(proposalObj.getProposalStatus().name()))
				&& proposalObj.getId() != proposal.getId()){
				toUserSet.add(proposalObj.getAssignedUser().getEmail());
			}
		}
		for (String userId : toUserSet) {
			if(mailTo.length() > 0){
				mailTo.append(ConstantStrings.COMMA);
			}
			mailTo.append(userId);
		}
		final String textMsg = proposalUtilService.createTextMsgForHomePageResrvtnCreated(proposal, lineItem).toString();
		Map<String, String> mailMap = new HashMap<String, String>();
		String reservationType = (ConstantStrings.PRODUCT_CLASS_HOME_PAGE.equalsIgnoreCase(productClassName) ? ConstantStrings.PRODUCT_CLASS_HOME_PAGE : ConstantStrings.PRODUCT_CLASS_DISPLAY_CROSS_PLATFORM);
		if(resrvtnOperation.equals(ConstantStrings.CREATED) && (proposalOwnerRole.equals(SecurityUtil.ADMIN) || proposalOwnerRole.equals(SecurityUtil.PRODUCT_OWNER))){
			if(!toUserSet.isEmpty()){
				final String subject = reservationType+ " Reservation Created" + ConstantStrings.COLON + ConstantStrings.SPACE + proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()) +  
					ConstantStrings.COLON + ConstantStrings.SPACE+ proposal.getName();
				final String mailcc = SecurityUtil.getUser().getEmail();
				mailMap = proposalUtilService.getMailInfoForHomePageReservation(mailcc, subject, mailTo.toString(), textMsg);
			}
		}else if(resrvtnOperation.equals(ConstantStrings.DELETED)){
			final String subject = reservationType + " Reservation Deleted" + ConstantStrings.COLON + ConstantStrings.SPACE + proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()) +  
					ConstantStrings.COLON + ConstantStrings.SPACE+ proposal.getName();
			final String mailcc = SecurityUtil.getUser().getEmail();
			mailMap = proposalUtilService.getMailInfoForHomePageReservation(mailTo.toString(), subject, mailcc, textMsg);
		}
		proposalUtilService.sendMail(mailMap);
	}

	public void setProposalSOSService(final IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setPackageService(final IPackageService packageService) {
		this.packageService = packageService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setTargetJsonConverter(final TargetJsonConverter targetJsonConverter) {
		this.targetJsonConverter = targetJsonConverter;
	}

	public void setPricingCalculator(final IPricingCalculator pricingCalculator) {
		this.pricingCalculator = pricingCalculator;
	}

	public void setAuditService(final IAuditService auditService) {
		this.auditService = auditService;
	}

	public void setMailUtil(final MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setSosIntegrationService(final ISosIntegrationService sosIntegrationService) {
		this.sosIntegrationService = sosIntegrationService;
	}

	public void setTemplateGenerator(final TemplateGenerator templateGenerator) {
		this.templateGenerator = templateGenerator;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}

	public void setPricingStatusCalculatorService(final IPricingStatusCalculatorService pricingStatusCalculatorService) {
		this.pricingStatusCalculatorService = pricingStatusCalculatorService;
	}

	public void setEmailFrom(final String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public void setLineItemUtil(final LineItemUtil lineItemUtil) {
		this.lineItemUtil = lineItemUtil;
	}

	public void setEmailScheduleService(final IEmailScheduleService emailScheduleService) {
		this.emailScheduleService = emailScheduleService;
	}

	public void setReservationService(ICalendarReservationService reservationService) {
		this.reservationService = reservationService;
	}

	public void setSalesForceProposalService(ISalesForceProposalService salesForceProposalService) {
		this.salesForceProposalService = salesForceProposalService;
	}

	public void setProposalUtilService(ProposalUtilService proposalUtilService) {
		this.proposalUtilService = proposalUtilService;
	}
}