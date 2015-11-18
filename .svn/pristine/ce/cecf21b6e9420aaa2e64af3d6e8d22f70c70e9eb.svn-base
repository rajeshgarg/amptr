/**
 *
 */
package com.nyt.mpt.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.ProposalForm;
import com.nyt.mpt.form.SearchProposalForm;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.template.ReferenceDataMap;
import com.nyt.mpt.util.enums.CreativeType;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemViewableCriteriaEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This class is used as proposal work flow helper for proposal work flow related operations
 * @author Shishir.Srivastava
 */
public class ProposalWorkflowHandler extends FormAction {

	private static final String PROPOSAL_FORM = "proposalForm";

	private static final String RENDER_FRAGMENT = "renderFragment";

	private static final String RENDER_FRAGMENT_ID = "renderFragmentId";

	private static final String LINE_ITEM_SEQ = "lineItemSequence";
	
	private static final Logger LOGGER = Logger.getLogger(ProposalWorkflowHandler.class);

	private IProposalService proposalService;

	private ProposalHelper proposalHelper;

	private IUserService userService;

	private IProposalSOSService proposalSOSService;

	private String sosURL;
	
	/**
	 * Initialized work flow for a proposal
	 * @param proposalId
	 * @param proposalVersion
	 * @param context
	 * @return
	 */
	public ProposalForm initProposal(final long proposalId, final RequestContext context) {
		final ProposalForm pro = new ProposalForm();
		pro.setId(proposalId);
		return pro;
	}

	/**
	 * Setup form for display basic info screen
	 */
	@Override
	public Event setupForm(final RequestContext context) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting up proposal form");
		}
		setFormObjectName(PROPOSAL_FORM);
		setFormObjectClass(ProposalForm.class);
		return super.setupForm(context);
	}

	/**
	 * Setup drop down values used in proposal
	 * @param context
	 * @return
	 */
	public Event setupDropdownValues(final RequestContext context) {
		context.getRequestScope().put("allSalesCategories", getUserSpecificSortedList(proposalHelper.getSalesCategory()));
		context.getRequestScope().put("allPriceTypes", PriceType.getPriceTypeMap());
		context.getRequestScope().put("allCriticalities", Criticality.getCriticalityMap());
		context.getRequestScope().put("allActiveProducts", proposalHelper.getAllProducts());
		context.getRequestScope().put("allCampainObjective", proposalHelper.getCampainObjectives());
		context.getRequestScope().put("currency", proposalSOSService.getCurrencies());
		return success();
	}

	/**
	 * Set data for create new proposal
	 * 
	 * @param context
	 * @return
	 * @throws ParseException 
	 */
	public Event setupNewProposalData(final RequestContext context) throws ParseException {
		final ProposalForm form = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		form.setProposalStatus(ProposalStatus.INPROGRESS.name());
		form.setRequestedOn(DateUtil.getGuiDateTimeString(DateUtil.getCurrentDateAndUserTime(DateUtil.getCurrentDateTimeString("HH:mm"))));
		form.setDueOn(DateUtil.getGuiDateTimeString(DateUtil.getCurrentDateAndUserTime("23:59")));
		form.setUserName(SecurityUtil.getUser().getFullName());
		form.setConversionRate("1.0");
		form.setCurrencyId(1l);
		form.setProposalTitle("New Proposal");
		return success();
	}

	/**
	 * Set proposal and proposal version data to the form for display in UI
	 * @param context
	 * @return
	 */
	public Event setupProposalData(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting up Proposal Data for edit proposal");
		}
		if (context.getFlowScope().contains(PROPOSAL_FORM)) {
			final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
			proposalForm.setOptionName("");
			final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());

			/**
			 * Setting up proposal options data
			 */
			final Map<Long, String> optionMap = new LinkedHashMap<Long, String>(proposal.getProposalOptions().size());
			for (ProposalOption option : proposal.getProposalOptions()) {
				optionMap.put(option.getId(), option.getName());

			}
			final Map<Long, String> optionReverseMap = new LinkedHashMap<Long, String>(proposal.getProposalOptions().size());
			for (int i = optionMap.size() - 1; i >= 0; i--) {
				optionReverseMap.put((Long) optionMap.keySet().toArray()[i], (String) optionMap.values().toArray()[i]);
			}
			context.getRequestScope().put("proposalOptions", optionReverseMap);
			context.getRequestScope().put("proposalDefaultOption", proposal.getDefaultOption().getId());
			context.getRequestScope().put("currentDate", DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
			context.getRequestScope().put("applicationURL", proposalHelper.getApplicationURL());
			proposalForm.populateForm(proposal);
			if (proposal.getClonedFromProposalVersion() != null) {
				final ProposalVersion proposalVersion = proposalService.getProposalVersion(proposal.getClonedFromProposalVersion());
				if (proposalVersion != null) {
					proposalForm.setClonedFormProposal(proposalVersion.getProposalOption().getProposal().getName());
				}
			}

			proposalForm.setSalesCategoryDisplayName(proposalHelper.getSalesCategory().get(proposal.getSosSalesCategoryId()));
			if (proposalHelper.getAdvertiser().get(proposal.getSosAdvertiserId()) == null) {
				proposalForm.setSummaryAdvertiserName(proposalForm.getNewAdvertiserName());
			} else {
				proposalForm.setSummaryAdvertiserName(proposalHelper.getAdvertiser().get(proposal.getSosAdvertiserId()));
			}
			proposalForm.setSummaryAgencyName(proposalHelper.getAgency().get(proposal.getSosAgencyId()));
			proposalForm.setCampaignObjective(proposalHelper.getCampaignObjectivesByProposalId(proposal.getId()));

			context.getRequestScope().put("logedInUser", SecurityUtil.getUser());
			if (ProposalStatus.REVIEW.equals(proposal.getProposalStatus())) {
				context.getRequestScope().put("roleUserMap", proposalHelper.getProposalReviewer(SecurityUtil.getProposalReviewers()));
			} else {
				context.getRequestScope().put("roleUserMap", proposalHelper.getProposalReviewer(SecurityUtil.getProposalAssignToList()));
			}

			context.getRequestScope().put("allVersions", proposalHelper.getVersions(((ProposalForm) context.getFlowScope().get(PROPOSAL_FORM)).getOptionId()));
			context.getRequestScope().put("proposalAccessMap", ProposalStatus.getProposalStatusAccessMap(proposal.getProposalStatus()));
			if (proposal.getAssignedUser() != null) {
				final User user = (User) userService.loadUserByUsername(proposal.getAssignedUser().getLoginName());
				context.getRequestScope().put("proposalOwnerRole", user.getUserRoles().iterator().next().getRoleName());
			}
			if (StringUtils.isNotBlank(proposal.getSalesforceID())) {
				proposalForm.setSalesForceSearchKey(ConstantStrings.AMPT_KEY + String.valueOf(proposal.getId()));
			} else {
				proposalForm.setSalesForceSearchKey(null);
			}
			proposalForm.setSosURL(sosURL);
		}
		return success();
	}

	/**
	 * Set data for display only on summary screen
	 * @param context
	 * @return
	 */
	public Event setupSummaryData(final RequestContext context) {
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		LOGGER.info("Setting up summary data getting Line Item Details for ProposalID: " + proposalForm.getId() + " And Proposal Version: " + proposalForm.getProposalVersion());

		final List<LineItemForm> lineItemFormList = new ArrayList<LineItemForm>();
		final SortingCriteria SortingCriteria = new SortingCriteria(LINE_ITEM_SEQ, CustomDbOrder.ASCENDING);
		final List<LineItem> lineItemLst = proposalService.getProposalLineItems(proposalForm.getOptionId(), Long.parseLong(proposalForm.getProposalVersion()), null, null, SortingCriteria);

		final ReferenceDataMap referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemAssocList(lineItemLst);
		final Map<Long, Product> productMap = referenceDataMap.getProductMap();
		final Map<Long, SalesTarget> salesTargetMap = referenceDataMap.getSalesTargetMap();

		// Setting discount in proposal form
		final Double discountValue = proposalHelper.getProposalLevelDiscount(lineItemLst);
		proposalForm.setDiscount((discountValue != null && discountValue > 0) ? NumberUtil.formatDouble(discountValue, true) : ConstantStrings.NA);
		for (LineItem lineItem : lineItemLst) {
			final LineItemForm form = new LineItemForm();
			form.populateForm(lineItem);
			if (lineItem.getRate() == null || lineItem.getRateCardPrice() == null) {
				form.setDiscount(ConstantStrings.NA);
			} else {
				if (lineItem.getRateCardPrice() >= lineItem.getRate() && lineItem.getRateCardPrice() > 0) {
					final Double discount = ((lineItem.getRateCardPrice() - NumberUtil.round(lineItem.getRate(), 2)) / lineItem.getRateCardPrice()) * 100;
					form.setDiscount(NumberUtil.formatDouble(discount, true));
				} else {
					form.setDiscount(ConstantStrings.NA);
				}
			}
			if (lineItem.getSor() == null) {
				form.setSor(ConstantStrings.NA);
			}
			if (!productMap.containsKey(lineItem.getSosProductId()) || !Constants.ACTIVE_STATUS.equalsIgnoreCase(productMap.get(lineItem.getSosProductId()).getStatus())
					|| productMap.get(lineItem.getSosProductId()).getDeleteDate() != null) {
				form.setProduct_active(Constants.NO);
			}
			// Setting status of sales target
			if (!proposalHelper.getSalesTargetStatusFromSalesTargetAssocList(salesTargetMap, lineItem)) {
				form.setSalesTarget_active(Constants.NO);
			}

			lineItemFormList.add(form);
		}
		context.getRequestScope().put("proposalLineItems", lineItemFormList);

		final Map<Long, String> objectiveMap = proposalHelper.getCampainObjectives();
		final Map<Long, String> propObjMap = new TreeMap<Long, String>();
		proposalForm.setCampaignObjective(proposalHelper.getCampaignObjectivesByProposalId(proposalForm.getId()));
		for (long objectiveId : proposalForm.getCampaignObjective()) {
			propObjMap.put(objectiveId, objectiveMap.get(objectiveId));
		}
		context.getRequestScope().put("proposalCampainObjective", propObjMap);
		return success();
	}

	/**
	 * Update proposalId in case of new proposal
	 * @param context
	 * @return
	 */
	public ProposalForm updateProposalId(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating render fragment id");
		}
		LOGGER.info("Updating render fragment id");
		long proposalId = 0;
		if (StringUtils.isNotBlank(context.getRequestParameters().get("proposalId"))) {
			proposalId = Long.valueOf(context.getRequestParameters().get("proposalId"));
		}
		context.getRequestScope().put("proposalId", proposalId);
		context.getFlowScope().put("proposalId", proposalId);
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		proposalForm.setId(proposalId);
		return proposalForm;
	}

	/**
	 * Update OptionId in case of Opening a proposal Option
	 * @param context
	 * @return
	 */
	public ProposalForm updateOptionId(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Option id");
		}
		long optionId = 0;
		if (StringUtils.isNotBlank(context.getRequestParameters().get("proposalOption"))) {
			optionId = Long.valueOf(context.getRequestParameters().get("proposalOption"));
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		proposalForm.setOptionId(optionId);
		// So that latest version will be displayed when clicked on option Tab
		proposalForm.setProposalVersion(null);
		return proposalForm;
	}

	/**
	 * Update OptionVersion in case of Opening a OlderVersion
	 * @param context
	 * @return
	 */
	public ProposalForm updateOptionVersion(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Option id");
		}
		String optionVersion = "1";
		if (StringUtils.isNotBlank(context.getRequestParameters().get("proposalVersion"))) {
			optionVersion = context.getRequestParameters().get("proposalVersion");
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		proposalForm.setProposalVersion(optionVersion);
		return proposalForm;
	}

	public Event setupOptionVersionData(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setup Option version data");
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		final ProposalOption option = proposalService.getOptionbyId(proposalForm.getOptionId());
		ProposalVersion proposalVersion = null;
		LOGGER.info("setup Option version data for optionId: " + proposalForm.getOptionId());
		if (StringUtils.isBlank(proposalForm.getProposalVersion())) {
			proposalVersion = option.getLatestVersion();
		} else {
			for (ProposalVersion propversion : option.getProposalVersions()) {
				if (propversion.getProposalVersion().equals(Long.valueOf(proposalForm.getProposalVersion()))) {
					proposalVersion = propversion;
				}
			}
		}
		proposalForm.populateOptionVersionData(option, proposalVersion);
		return success();
	}

	public Event resetOptionVersion(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setup Option version data");
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		proposalForm.setProposalVersion(null);
		return success();
	}

	/**
	 * Check whether proposal is assigned to current user
	 * 
	 * @param context
	 * @return
	 */
	private Boolean proposalAssignedToCurrentUser(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check whether proposal is assigned to current user");
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		final User user = SecurityUtil.getUser();
		LOGGER.info("Check whether proposal is assigned to current user for ProposalID: " + proposalForm.getId());
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		if (proposal.getAssignedUser() != null && proposal.getAssignedUser().getUserId() == user.getUserId()) {
			context.getRequestScope().put("assignedToCurrentUser", Boolean.TRUE);
			return Boolean.TRUE;
		}
		context.getRequestScope().put("assignedToCurrentUser", Boolean.FALSE);
		return Boolean.FALSE;
	}

	/**
	 * Check whether proposal is editable for current status
	 * 
	 * @param context
	 * @return
	 */
	private Boolean isProposalEditableForStatus(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check whether proposal is editable for current user");
		}
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		if (proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.EXPIRED.getDisplayName())
				|| proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.PROPOSED.getDisplayName())
				|| proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.SOLD.getDisplayName())
				|| proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.DELETED.getDisplayName())
				|| proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.REJECTED_BY_CLIENT.getDisplayName())) {
			return Boolean.TRUE;
		} else if (proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.INPROGRESS.getDisplayName())) {
			if (SecurityUtil.SALES_GENERAL.equals(SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName()) || SecurityUtil.AD_OPS.equals(SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName())) {
				return Boolean.TRUE;
			}
		} else if (proposal.getProposalStatus().getDisplayName().equals(ProposalStatus.REVIEW.getDisplayName())) {
			if (SecurityUtil.SALES_GENERAL.equals(SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName()) || SecurityUtil.PRICING_ADMIN.equals(SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Setup data for display in template page
	 * @param context
	 * @return
	 */
	public Event setupTemplateData(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting template data");
		}
		context.getRequestScope().put("allProposalTemplate", proposalHelper.getProposalTemplate());
		return success();
	}

	/**
	 * Setup data for display search page while building proposal
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Event setupSearchData(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("setting up SearchData");
		}
		final SearchProposalForm searchPropForm = new SearchProposalForm();
		final long proposalId = ((ProposalForm) context.getFlowScope().get(PROPOSAL_FORM)).getId();
		searchPropForm.setProposalversion(((ProposalForm) context.getFlowScope().get(PROPOSAL_FORM)).getProposalVersion());
		searchPropForm.setProposalID(String.valueOf(proposalId));
		final LineItemForm lineItemForm = new LineItemForm();
		context.getRequestScope().put("proposalLineItemForm", lineItemForm);
		context.getRequestScope().put("searchProposalForm", searchPropForm);
		context.getRequestScope().put("allProducts", proposalHelper.getAllProductsByClassMap());
		context.getRequestScope().put("targetTypeCriteria", proposalHelper.getTargetTypeCriteria());
		context.getRequestScope().put("allPriceType", LineItemPriceTypeEnum.getAllPriceType());
		context.getRequestScope().put("lineItemSpecType", CreativeType.getSpecTypeMap());
		context.getRequestScope().put("isViewableOptions", LineItemViewableCriteriaEnum.getAllValuesMap());
		return success();
	}

	/**
	 * Event to Change renderFragment after new version created
	 * @param context
	 * @return
	 */
	public Event createVersion(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating version");
		}
		if (context.getFlowScope().contains(PROPOSAL_FORM)) {
			final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
			final ProposalOption option = proposalService.getOptionbyId(proposalForm.getOptionId());
			proposalForm.setPreviousProposalVersion(String.valueOf(option.getLatestVersion().getProposalVersion()));
			proposalForm.setProposalVersion(String.valueOf(option.getLatestVersion().getProposalVersion()));
			context.getFlowScope().put(RENDER_FRAGMENT_ID, RENDER_FRAGMENT + ConstantStrings.HYPHEN + proposalForm.getId() + ConstantStrings.HYPHEN + option.getLatestVersion().getProposalVersion());
		}
		return success();
	}

	/**
	 * Check for read only view of proposal
	 * @param context
	 * @return
	 */
	public boolean readOnlyView(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoking readOnlyView");
		}
		boolean readOnlyView = !proposalAssignedToCurrentUser(context);
		if (!readOnlyView) {
			readOnlyView = isProposalEditableForStatus(context);
		}
		return readOnlyView;
	}

	/**
	 * Check for read only view of a option if not latest version
	 * @param context
	 * @return
	 */
	public boolean isLatestOptionVersion(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoking isLatestOptionVersion");
		}
		boolean readOnlyView = (Boolean) context.getViewScope().get("readOnlyView");
		if (!readOnlyView) {
			final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
			final ProposalOption option = proposalService.getOptionbyId(proposalForm.getOptionId());
			LOGGER.info("Check whether working on latest version of a proposal option with version No: " + proposalForm.getProposalVersion());
			final boolean latestPropVersion = Long.valueOf(proposalForm.getProposalVersion()).equals(option.getLatestVersion().getProposalVersion());
			if (!latestPropVersion) {
				readOnlyView = true;
			}
		}
		return readOnlyView;
	}

	/**
	 * Check for read only view of Option if under Review
	 * @param context
	 * @return
	 */
	public boolean isOptionUnderReview(final RequestContext context) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invoking isLatestOptionVersion");
		}
		boolean readOnlyView = (Boolean) context.getViewScope().get("readOnlyView");
		if (!readOnlyView) {
			final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
			final ProposalOption option = proposalService.getOptionbyId(proposalForm.getOptionId());
			LOGGER.info("Check whether proposal is under review for proposalId: " + proposalForm.getId());
			if (ProposalStatus.REVIEW.name().equals(proposalForm.getProposalStatus()) && !option.isDefaultOption()) {
				readOnlyView = true;
			}
		}
		return readOnlyView;
	}

	/**
	 * Method tells whether logged in user can change the status of Proposal
	 * @param context
	 * @return
	 */
	public boolean isProposalCompletedAndAssigned(final RequestContext context) {
		final ProposalForm proposalForm = (ProposalForm) context.getFlowScope().get(PROPOSAL_FORM);
		final Proposal proposal = proposalService.getProposalbyId(proposalForm.getId());
		final boolean latestPropVersion = Long.valueOf(proposalForm.getProposalVersion()).equals(proposal.getDefaultOption().getLatestVersion().getProposalVersion());
		if (proposal.getAssignedUser() != null && proposal.getAssignedUser().getUserId() == SecurityUtil.getUser().getUserId()
				&& !ProposalStatus.PROPOSED.getDisplayName().equals(proposal.getProposalStatus().getDisplayName()) && latestPropVersion) {
			return true;
		}
		return false;
	}

	/**
	 * @param salesCategoryMap
	 * @return
	 */
	public Map<Long, String> getUserSpecificSortedList(final Map<Long, String> salesCategoryMap) {
		final Map<Long, String> sortedSalesCategory = new LinkedHashMap<Long, String>();
		final User user = SecurityUtil.getUser();
		final List<SalesCategory> salesCategories = userService.getUserSalesCategories(user.getUserId());
		if (salesCategories != null && !salesCategories.isEmpty()) {
			for (Iterator<Long> iterator = salesCategoryMap.keySet().iterator(); iterator.hasNext();) {
				final Long salesCategoryId = (Long) iterator.next();
				for (SalesCategory salesCategory : salesCategories) {
					if (salesCategoryId == salesCategory.getSosSalesCategoryId()) {
						sortedSalesCategory.put(salesCategoryId, salesCategoryMap.get(salesCategoryId));
						iterator.remove();
					}
				}
			}
		}
		sortedSalesCategory.putAll(salesCategoryMap);
		return sortedSalesCategory;
	}

	@Override
	protected Object createFormObject(final RequestContext context) {
		return new ProposalForm();
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setProposalSOSService(final IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}

}
