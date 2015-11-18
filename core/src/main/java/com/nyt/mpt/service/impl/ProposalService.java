/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.CloneTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Hibernate;

import com.nyt.mpt.dao.IProposalDAO;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.PricingCalculatorSummary;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IAuditService;
import com.nyt.mpt.service.IPricingCalculator;
import com.nyt.mpt.service.IPricingStatusCalculatorService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.ISalesForceProposalService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.annotation.ValidateProposal;
import com.nyt.mpt.util.enums.LineItemExceptionEnum;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemViewableCriteriaEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.PricingStatus;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.ws.ConnectionException;

/**
 * @author amandeep.singh
 *
 */
public class ProposalService  implements IProposalService{
	
	private static final Logger LOGGER = Logger.getLogger(ProposalService.class);
	private IProposalDAO proposalDao;
	private ProposalUtilService proposalUtilService;
	private IAuditService auditService;
	private IProposalSOSService proposalSOSService;
	private ISOSService sosService;
	private IPricingCalculator pricingCalculator;
	private IPricingStatusCalculatorService pricingStatusCalculatorService;
	private ISalesForceProposalService salesForceProposalService;
	private UserService userService;
	private String sosURL;
	private IProductService productService;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<Proposal> getProposalList(final List<RangeFilterCriteria> filterCriteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		final List<Proposal> proposalList = proposalDao.getProposalList(filterCriteriaLst, pgCriteria, sortingCriteria);
		for (Proposal proposal : proposalList) {
			Hibernate.initialize(proposal.getAssignedByUser());
			Hibernate.initialize(proposal.getAssignedUser());
			Hibernate.initialize(proposal.getProposalOptions());
			for (ProposalOption option : proposal.getProposalOptions()) {
				Hibernate.initialize(option.getProposalVersions());
			}
		}
		return proposalList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getAllProposalList()
	 */
	@Override
	public List<Proposal> getAllProposalList(){
		return proposalDao.getAllProposalList();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalListCount(java.util.List)
	 */
	public int getProposalListCount(final List<RangeFilterCriteria> filterCriteriaLst) {
		return proposalDao.getProposalListCount(filterCriteriaLst);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveProposal(long, com.nyt.mpt.domain.Proposal)
	 */
	@Override
	@ValidateProposal
	public long saveProposal(final long proposalId, final Proposal proposal) throws CustomCheckedException {
		long propId = 0;
		if (proposal.getId() == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Saving New Proposal with ProposalId: " + proposal.getId());
			}
			LOGGER.info("Saving New Proposal with ProposalId: " + proposal.getId());
			proposal.setAssignedByUser(SecurityUtil.getUser());
			proposal.setAssignedUser(SecurityUtil.getUser());
			propId = proposalDao.addProposal(proposal);
			auditService.createAuditForNewProposal(proposal);//To audit Proposal Cloning
		} else {
			final Proposal proposalDb = proposalDao.getProposalbyId(proposal.getId());
			auditService.createAuditForUpdateProposal(proposalDb, proposal);
			updatePricing(proposalDb, proposal);
			boolean isNewAdvertiser = isNewAdvertiser(proposalDb, proposal);
			boolean isSalesCategoryChanged = isSalesCategoryChanged(proposalDb, proposal);
			boolean isDueDateChanged = isDueDateChanged(proposalDb, proposal);
			proposalUtilService.updateProposal(proposalDb, proposal);
			propId =  proposalDao.saveProposal(proposalDb).getId();
			updateProposalVersionData(proposalDb);
			if(isNewAdvertiser || isSalesCategoryChanged){
				pricingStatusCalculatorService.addThreshHoldCheck(proposalDb.getId());
				pricingStatusCalculatorService.addAddedValueCheck(proposalDb.getId());
			}
			if(StringUtils.isNotBlank(proposalDb.getSalesforceID()) && ( isDueDateChanged || isSalesCategoryChanged) ){
				// Whenever planner change due date and sales category from proposal basic info and this proposal should be created from sales force 
				auditService.salesforceAuditLog(proposalDb, createAuditLogMessage(proposalDb, isDueDateChanged, isSalesCategoryChanged));
			}
		}
		return propId;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveNewSalesfoceId(java.lang.String, long)
	 */
	@Override
	@ValidateProposal
	public void saveNewSalesfoceId(final long proposalId, final String newSalesforceId) throws CustomCheckedException, ConnectionException {
		Proposal proposalOld =  salesForceProposalService.getProposalBySalesforceId(newSalesforceId);
		if(proposalOld != null) {
			proposalOld.setSalesforceID(null);
			Proposal propOld = proposalDao.saveProposal(proposalOld);
			auditService.createAuditOldSalesforceProposal(newSalesforceId, propOld);
		}
		final Proposal proposalDb = proposalDao.getProposalbyId(proposalId);
		proposalDb.setSalesforceID(newSalesforceId);
		Proposal proposalNew = proposalDao.saveProposal(proposalDb);
		auditService.createAuditForNewSalesforceID(proposalNew);
		salesForceProposalService.updateProposalToSalesforce(proposalNew, ConstantStrings.MEDIA_PLAN_LINKED + ConstantStrings.AMPT_KEY + String.valueOf(proposalDb.getId()));
	}

	/**
	 * Update line item and option level data based on basic info changed
	 * @param proposalDb
	 * @param proposal
	 */
	private void updatePricing(final Proposal proposalDb, final Proposal proposal){
		boolean salesCategoryChanged = isSalesCategoryChanged(proposalDb, proposal);
		boolean priceTypeChanged = isPriceTypeChanged(proposalDb, proposal);
		boolean advertiserChanged = isAdvertiserChanged(proposalDb, proposal);
		
		if(advertiserChanged && !priceTypeChanged && !salesCategoryChanged ){//100
			/**
			 * update all line item status as system approve for all option latest version if Key advertiser.
			 */
			updatePricingStatusBasedOnAdvertiserOrSCChange(proposalDb, proposal, salesCategoryChanged);
		} else if (!advertiserChanged && priceTypeChanged && !salesCategoryChanged ){//010
			/**
			 * update all line item CPM, Rate card price and apply goal seek feature for all option all version.
			 *  Also update threshold value and Budget at option level. 
			 */
			applyAgencyMarginOnPriceTypeChange(proposalDb, proposal);
		} else if ((!advertiserChanged && !priceTypeChanged && salesCategoryChanged) || (advertiserChanged && !priceTypeChanged && salesCategoryChanged )){//001 && 101
			updatePricingStatusBasedOnAdvertiserOrSCChange(proposalDb, proposal, salesCategoryChanged);
		} else if (advertiserChanged && priceTypeChanged && !salesCategoryChanged ){//110
			applyAgencyMarginOnPriceTypeChange(proposalDb, proposal);
			updatePricingStatusBasedOnAdvertiserOrSCChange(proposalDb, proposal, salesCategoryChanged);
		} else if ((!advertiserChanged && priceTypeChanged && salesCategoryChanged ) || (advertiserChanged && priceTypeChanged && salesCategoryChanged )){//011 && 111
			applyAgencyMarginOnPriceTypeChange(proposalDb, proposal);
			updatePricingStatusBasedOnAdvertiserOrSCChange(proposalDb, proposal, salesCategoryChanged);
		}
	}
	
	/**
	 * Update pricing and status based on advertiser or sales category change
	 * @param proposalDb
	 * @param proposal
	 * @param isSalesCategoryChanged
	 */
	private void updatePricingStatusBasedOnAdvertiserOrSCChange(final Proposal proposalDb, final Proposal proposal, final boolean isSalesCategoryChanged){
		for (ProposalOption option : proposalDb.getProposalOptions()) {
			Set<LineItem> lineItemSet = option.getLatestVersion().getProposalLineItemSet();
			for (LineItem proposalDbLineItem : lineItemSet) {
				if(isSalesCategoryChanged){
					if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalDbLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalDbLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(proposalDbLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(proposalDbLineItem.getPriceType())) {
						Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(proposalDbLineItem, proposal.getSosSalesCategoryId(), proposalDb.getPriceType());
						if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
							Double price = (Double) pricingCalculatorMap.get("price");
							if(PriceType.Gross.name().equalsIgnoreCase(proposal.getPriceType())) {
								price = (price == null || price == 0.0) ? 0 : applyAgencyMargin(proposal.getPriceType(),price, proposal.getAgencyMargin());
							}else {
								boolean israteCardRounded = pricingCalculatorMap.containsKey("rateCardRounded") ? (Boolean) pricingCalculatorMap.get("rateCardRounded") : true;
								if(!israteCardRounded){
									price = (price == null || price == 0.0) ? 0 : NumberUtil.getHalfCentFormatedValue(price);
								}else{
									price = (price == null || price == 0.0) ? 0 : price;
								}
							}
							proposalDbLineItem.setRateCardPrice(price);
							String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
							proposalDbLineItem.setPriceCalSummary(priceCalSummary);
							proposalDbLineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptions(proposalDbLineItem));
						}
					} else {
						proposalDbLineItem.setRateCardPrice(0D);
					}
				} 
				proposalDbLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalDbLineItem, proposal.isSpecialAdvertiser()));
			}
		}
	}

	/**
	 * Apply agency margin on price type changes from gross to net and net to gross in line item and option
	 * @param proposalDb
	 * @param proposal
	 */
	private void applyAgencyMarginOnPriceTypeChange(final Proposal proposalDb, final Proposal proposal){
		for (ProposalOption option : proposalDb.getProposalOptions()) {
			for (ProposalVersion proposalVersion : option.getProposalVersions()) {
				for (LineItem proposalDbLineItem : proposalVersion.getProposalLineItemSet()) {
					if(PriceType.Gross.name().equalsIgnoreCase(proposal.getPriceType())) {
						// when price type changed from net to gross
						proposalDbLineItem = updateLineItemsNetToGross(proposalDbLineItem, proposal.getAgencyMargin(), proposalDb.getAgencyMargin());
					}else {
						proposalDbLineItem = updateLineItemsGrossToNet(proposalDbLineItem, proposal.getAgencyMargin(), proposalDb.getAgencyMargin());
						if (!LineItemPriceTypeEnum.FLATRATE.getOptionValue().equalsIgnoreCase(proposalDbLineItem.getPriceType())) {
							PricingCalculatorSummary pricingCalculatorSummary = getNode(proposalDbLineItem.getPriceCalSummary());
							if(pricingCalculatorSummary == null || pricingCalculatorSummary.getAppliedFiveCentsRule() == null || ConstantStrings.EMPTY_STRING.equals(pricingCalculatorSummary.getAppliedFiveCentsRule()) || ConstantStrings.YES.equals(pricingCalculatorSummary.getAppliedFiveCentsRule())){
								applyHalfCentFormatedRule(proposalDbLineItem);
							}
						}
					}
				}
			}
			updateOptionData(option, proposalDb, proposal);
		}
	}

	/**
	 * @param json
	 * @return
	 */
	private PricingCalculatorSummary getNode(final String json) {
		PricingCalculatorSummary pricingummary = null;
		try {
			if (StringUtils.isNotBlank(json)) {
				pricingummary = new ObjectMapper().readValue(json, PricingCalculatorSummary.class);
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
		return pricingummary;
	}

	/**
	 * @param proposalDb
	 */
	private void updateProposalVersionData(final Proposal proposalDb) {
		for (ProposalOption option : proposalDb.getProposalOptions()) {
			for (ProposalVersion proposalVersion : option.getProposalVersions()) {
				updateProposalVersionNetImpressionAndCPM(option.getId(), proposalVersion.getProposalVersion());
			}
		}
	}

	/**
	 * Apply agency margin on price type changes from gross to net and net to gross for budget and threshold limit
	 * @param option
	 * @param proposalDb
	 * @param proposal
	 */
	private void updateOptionData(final ProposalOption option, final Proposal proposalDb, final Proposal proposal) {
		if (PriceType.Gross.name().equalsIgnoreCase(proposal.getPriceType())) {
			updateThresholdLimitNetToGross(option, proposalDb, proposal);
			updateBudgetNetToGross(option, proposalDb, proposal);
		} else {
			updateThresholdLimitGrossToNet(option, proposalDb, proposal);
			updateBudgetGrossToNet(option, proposalDb, proposal);
		}
	}
	
	/**
	 * Apply agency margin in threshold limit on price type changes from gross to net
	 * @param option
	 * @param proposalDb
	 * @param proposal
	 */
	private void updateThresholdLimitGrossToNet(final ProposalOption option, final Proposal proposalDb, final Proposal proposal){
		if(option.getThresholdLimit() != null){
			option.setThresholdLimit(NumberUtil.getHalfCentFormatedValue(option.getThresholdLimit() * ((100 - proposal.getAgencyMargin()) / 100)));
		}
	}
	
	/**
	 * Apply agency margin in budget on price type changes from gross to net
	 * @param option
	 * @param proposalDb
	 * @param proposal
	 */
	private void updateBudgetGrossToNet(final ProposalOption option, final Proposal proposalDb, final Proposal proposal) {
		if (option.getBudget() != null) {
			option.setBudget(NumberUtil.getHalfCentFormatedValue(option.getBudget()	* ((100 - proposal.getAgencyMargin()) / 100)));
		}
	}
	
	/**
	 * Apply agency margin in threshold limit on price type changes from net to gross
	 * @param option
	 * @param proposalDb
	 * @param proposal
	 */
	private void updateThresholdLimitNetToGross(final ProposalOption option, final Proposal proposalDb, final Proposal proposal){
		if(option.getThresholdLimit() != null){
			option.setThresholdLimit(option.getThresholdLimit() * (100 / (100 - proposal.getAgencyMargin())));
		}
	}
	
	/**
	 * Apply agency margin in budget on price type changes from net to gross
	 * @param option
	 * @param proposalDb
	 * @param proposal
	 */
	private void updateBudgetNetToGross(final ProposalOption option, final Proposal proposalDb, final Proposal proposal){
		if(option.getBudget() != null){
			option.setBudget(option.getBudget() * (100 / (100 - proposal.getAgencyMargin())));
		}
	}
	
	/**
	 * Apply half cent rule when price type changed from gross to net
	 * @param proposalDbLineItem
	 */
	private void applyHalfCentFormatedRule(LineItem proposalDbLineItem) {
		proposalDbLineItem.setRateCardPrice(NumberUtil.getHalfCentFormatedValue(proposalDbLineItem.getRateCardPrice()));
		proposalDbLineItem.setRate(NumberUtil.getHalfCentFormatedValue(proposalDbLineItem.getRate()));
		proposalDbLineItem.setTotalInvestment(NumberUtil.getHalfCentFormatedValue(proposalDbLineItem.getTotalInvestment()));
	}
	
	
	/**
	 * Check either sales category changed or not
	 * @param proposalDb
	 * @param proposal
	 * @return
	 */
	private boolean isSalesCategoryChanged(final Proposal proposalDb, final Proposal proposal) {
		boolean flag = true;
		if(proposalDb.getSosSalesCategoryId() != null && proposalDb.getSosSalesCategoryId().equals(proposal.getSosSalesCategoryId())){
			flag = false;
		}
		return flag;
	}
	
	/**
	 * Check either 'DueDate' changed or not
	 * @param proposalDb
	 * @param proposal
	 * @return
	 */
	private boolean isDueDateChanged(final Proposal proposalDb, final Proposal proposal) {
		boolean flag = true;
		if(proposalDb.getDueDate().getTime() == proposal.getDueDate().getTime()){
			flag = false;
		}
		return flag;
	}

	/**
	 * Check either price type changed or not
	 * @param proposalDb
	 * @param proposal
	 * @return
	 */
	private boolean isPriceTypeChanged(final Proposal proposalDb, final Proposal proposal) {
		boolean flag = true;
		if(proposalDb.getPriceType() != null && proposalDb.getPriceType().equalsIgnoreCase(proposal.getPriceType())){
			flag = false;
		} 
		return flag;
	}

	/**
	 * Check either advertiser changed or not
	 * @param proposalDb
	 * @param proposal
	 * @return
	 */
	private boolean isAdvertiserChanged(final Proposal proposalDb, final Proposal proposal) {
		boolean flag = true;
		if((proposalDb.getSosAdvertiserId() != null && proposalDb.getSosAdvertiserId().equals(proposal.getSosAdvertiserId())) ||
				(proposalDb.isSpecialAdvertiser() == proposal.isSpecialAdvertiser())){
			/**
			 *  When advertiser is not changed or if changed form KA to KA or NKA to NKA then no changes are applicable
			 */
				flag = false;
		}
		return flag;
	}
	
	private boolean isNewAdvertiser(final Proposal proposalDb, final Proposal proposal) {
		boolean flag = true;
		if((proposalDb.getSosAdvertiserId() != null && proposalDb.getSosAdvertiserId().equals(proposal.getSosAdvertiserId()))){
			/** When advertiser is not changed */
				flag = false;
		}
		return flag;
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceNew#getProposalbyId(java.lang.Long)
	 */
	@Override
	public Proposal getProposalbyId(final Long proposalId) {
		return proposalDao.getProposalbyId(proposalId);
	}
	
	
	@Override
	public Proposal getProposalForClone(final Long proposalId) {
		return proposalDao.getProposalForClone(proposalId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceNew#getOptionbyId(java.lang.Long)
	 */
	@Override
	public ProposalOption getOptionbyId(final Long optionId) {
		return proposalDao.getOptionbyId(optionId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveLineItemsOfProposal(java.lang.Long, long, com.nyt.mpt.domain.LineItem, long)
	 */
	@Override
	@ValidateProposal
	public Long saveLineItemsOfProposal(final Long proposalID, final long OptionId, final LineItem proposalLineItem, final long proposalversion) throws CustomCheckedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Line Items Of Option with optionId: " + OptionId);
		}
		LOGGER.info("Saving Line Items Of Option with optionId: " + OptionId);
		final ProposalVersion proposalVersionDB = getproposalVersions(OptionId, proposalversion).get(0);
		proposalLineItem.setProposalVersion(proposalVersionDB);
		proposalLineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptions(proposalLineItem));
		Proposal proposal = proposalDao.getProposalForClone(proposalLineItem.getProposalId());
		if(proposalLineItem.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
			proposalLineItem.setPricingStatus(PricingStatus.UNAPPROVED);
		}else{
			proposalLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalLineItem , proposal.isSpecialAdvertiser()));
		}
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		Long lineItemId = proposalDao.addLineItemsOfProposal(proposalLineItem);
		if(proposalLineItem.getReservation() != null &&  productClassIdLst.contains(proposalLineItem.getSosProductClass()) ){
				auditService.createAuditMessageForHomePageResrvtn(proposalLineItem,ConstantStrings.CREATED);
		}
		return lineItemId;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateLineItemsOfProposal(java.lang.Long, com.nyt.mpt.domain.LineItem, boolean)
	 */
	@Override
	@ValidateProposal
	public Long updateLineItemsOfProposal(final Long proposalID, final LineItem bean) throws CustomCheckedException {
		LineItem proLineItemDB;
		LineItem prevHomePageLnItm;
		Long returnid = 0L;
		final List<LineItem> proLineItemLst = proposalDao.getLineItems(String.valueOf(bean.getLineItemID()));
		if (proLineItemLst != null && !proLineItemLst.isEmpty()) {
			proLineItemDB = proLineItemLst.get(0);
			prevHomePageLnItm = proposalUtilService.createNewLineItemFromExisting(proLineItemDB, proLineItemDB.getProposalVersion().getProposalOption().getProposal().getId());
			prevHomePageLnItm.setLineItemID(proLineItemDB.getLineItemID());
			boolean newReservation = false;
			if (bean.getReservation() != null) {
				newReservation = isReservationNew(bean, proLineItemDB);
			}
			Date oldExpirationDate = null;
			if(proLineItemDB.getReservation() != null){
				oldExpirationDate = proLineItemDB.getReservation().getExpirationDate();
			}
			boolean isViewablityFlagChanged = false;
			if(proLineItemDB.getViewabilityLevel() != bean.getViewabilityLevel()){
				isViewablityFlagChanged = true;
			}
			Double existingPrice = proLineItemDB.getRateCardPrice();
			Double existingCPM = (proLineItemDB.getRate() == null) ? 0d : proLineItemDB.getRate();
			Double currentCPM = (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(bean.getPriceType())) ? 0d : bean.getRate();
			proLineItemDB.setComments(bean.getComments());
			proLineItemDB.setEndDate(bean.getEndDate());
			proLineItemDB.setFlight(bean.getFlight());
			proLineItemDB.setImpressionTotal(bean.getImpressionTotal());
			proLineItemDB.setPriceType(bean.getPriceType());
			proLineItemDB.setRate(bean.getRate());
			proLineItemDB.setSosProductClass(bean.getSosProductClass());
			proLineItemDB.setSosProductId(bean.getSosProductId());
			proLineItemDB.setProductName(bean.getProductName());
			proLineItemDB.setTargetingString(bean.getTargetingString());
			// Clear the line items sales target association and put new list
			final List<LineItemSalesTargetAssoc> salesTargetAssocs = proLineItemDB.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : salesTargetAssocs) {
				proposalDao.deleteLineItemSalesTargetAssoc(lineItemSalesTargetAssoc);
			}
			final Set<LineItemTarget> geoTargetAssocs = proLineItemDB.getGeoTargetSet();
			for (LineItemTarget lineItemTarget : geoTargetAssocs) {
				proposalDao.deleteProposalGeoTargets(lineItemTarget);
			}
			proLineItemDB.setLineItemSalesTargetAssocs(bean.getLineItemSalesTargetAssocs());
			proLineItemDB.setGeoTargetSet(bean.getGeoTargetSet());
			proLineItemDB.setStartDate(bean.getStartDate());
			proLineItemDB.setTotalInvestment(bean.getTotalInvestment());
			proLineItemDB.setAvails(bean.getAvails());
			proLineItemDB.setPlacementName(bean.getPlacementName());
			proLineItemDB.setSov(bean.getSov());
			//proLineItemDB.setSegmentCode(bean.getSegmentCode());
			proLineItemDB.setRateCardPrice(bean.getRateCardPrice());
			proLineItemDB.setTotalPossibleImpressions(bean.getTotalPossibleImpressions());
			proLineItemDB.setSpecType(bean.getSpecType());
			proLineItemDB.setAvailsPopulatedDate(bean.getAvailsPopulatedDate());
			proLineItemDB.setLineItemSequence(bean.getLineItemSequence());
			proLineItemDB.setPriceCalSummary(bean.getPriceCalSummary());
			proLineItemDB.setSor(bean.getSor());
			proLineItemDB.setProductType(bean.getProductType());
			proLineItemDB.setRatex(bean.isRatex());
			proLineItemDB.setViewabilityLevel(bean.getViewabilityLevel());
			if (bean.getOrderNumber() != null) {
				proLineItemDB.setOrderNumber(bean.getOrderNumber());
			}
			if (!LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(bean.getPriceType())) {
					proLineItemDB.setRateCardPrice(bean.getRateCardPrice());
			} else {
				proLineItemDB.setRateCardPrice(0D);
			}
			proposalUtilService.updateReservation(proLineItemDB,bean);
			
			proLineItemDB.setLineItemExceptions(proposalUtilService.getLineItemExceptions(proLineItemDB));
			if(proLineItemDB.getPackageObj() != null  && (!(existingPrice.equals(bean.getRateCardPrice())) || !(existingCPM.equals(currentCPM)))){
				if(proLineItemDB.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
					proLineItemDB.setPricingStatus(PricingStatus.UNAPPROVED);
					proLineItemDB.setPackageObj(null);
				}else{
					Proposal proposal = proposalDao.getProposalForClone(proLineItemDB.getProposalId());
					proLineItemDB.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proLineItemDB , proposal.isSpecialAdvertiser()));
				}
			}else if( !PricingStatus.PRICING_APPROVED.equals(proLineItemDB.getPricingStatus()) && proLineItemDB.getPackageObj() == null){
				if(proLineItemDB.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
					proLineItemDB.setPricingStatus(PricingStatus.UNAPPROVED);
				}else{
					Proposal proposal = proposalDao.getProposalForClone(proLineItemDB.getProposalId());
					proLineItemDB.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proLineItemDB , proposal.isSpecialAdvertiser()));
				}
			}
			if(proLineItemDB.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue() && (isViewablityFlagChanged || !(existingPrice.equals(bean.getRateCardPrice())) || !(existingCPM.equals(currentCPM)))){
				proLineItemDB.setPricingStatus(PricingStatus.UNAPPROVED);
			}
			if(isViewablityFlagChanged && proLineItemDB.getViewabilityLevel() != LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
				Proposal proposal = proposalDao.getProposalForClone(proLineItemDB.getProposalId());
				proLineItemDB.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proLineItemDB , proposal.isSpecialAdvertiser()));
			}
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Updating Line Items with id: " + proLineItemDB.getLineItemID());
			}
			LOGGER.info("Updating Line Items with id: " + proLineItemDB.getLineItemID());
			returnid = proposalDao.editLineItemsOfProposal(proLineItemDB);
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			if(bean.getReservation() != null && productClassIdLst.contains(bean.getSosProductClass()) ){
				if(newReservation || oldExpirationDate == null) {
					auditService.createAuditMessageForHomePageResrvtn(proLineItemDB,ConstantStrings.CREATED);
				}else if(oldExpirationDate.compareTo(bean.getReservation().getExpirationDate()) != 0){
					auditService.createAuditMessageForHomePageResrvtn(bean,ConstantStrings.RENEWED);
				}
			}
			if(newReservation && oldExpirationDate != null && productClassIdLst.contains(prevHomePageLnItm.getSosProductClass()) || 
					(oldExpirationDate != null && proLineItemDB.getReservation() == null) && productClassIdLst.contains(prevHomePageLnItm.getSosProductClass())){
				auditService.createAuditMessageForHomePageResrvtn(prevHomePageLnItm,ConstantStrings.DELETED);
			}
			/*if (prevHomePageLnItm.getReservation() != null && prevHomePageLnItm.getSosProductClass() == Long.parseLong(ConstantStrings.HOME_PAGE_PRODUCT_ID)&& (isReservationNew(prevHomePageLnItm, bean))) {
				auditService.createAuditMessageForHomePageResrvtn(prevHomePageLnItm,ConstantStrings.DELETED);
			}*/
		}
		return returnid;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceNew#getLineItems(java.lang.Long)
	 */
	@Override
	public List<LineItem> getLineItems(final String lineItemId) {
		return proposalDao.getLineItems(lineItemId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceNew#getProposalLineItems(java.lang.Long, java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<LineItem> getProposalLineItems(final Long optionID, final Long proposalVersion, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		return proposalDao.getFilteredProposalLineItems(optionID, proposalVersion, filterCriteria, pgCriteria, sortingCriteria);
	}
	

	@Override
	public int getFilteredPackageLineItemsCount(final Long optionID, final Long proposalVersion, final FilterCriteria criteria) {
		return proposalDao.getFilteredProposalLineItemsCount(optionID, proposalVersion, criteria);
	}

	@Override
	@ValidateProposal
	public Long createVersion(final Long proposalId, final Long optionId, final Long optionversion) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating Version in createVersion()");
		}
		LOGGER.info("Creating version for OptionID: " + optionId);
		ProposalOption OptionDb = getOptionbyId(optionId);
		Transformer transformer = CloneTransformer.getInstance();
		ProposalVersion propVersionDB =  new ProposalVersion();
		for (ProposalVersion proposalVersion : OptionDb.getProposalVersions()) {
			if(proposalVersion.getProposalVersion().equals(optionversion)){
				propVersionDB = proposalVersion;
			}
		}
		ProposalVersion propVersion = (ProposalVersion) transformer.transform(propVersionDB);
		try {
			Ognl.setValue("proposalVersion", propVersion, OptionDb.getProposalVersions().size() + 1);
			Ognl.setValue("id", propVersion, 0);
			Ognl.setValue("version", propVersion, 1);
			Ognl.setValue("proposalLineItemSet", propVersion, null);
			propVersion.setProposalOption(OptionDb);
			propVersion = proposalDao.addOptionVersion(propVersion);
			
			SortingCriteria sortCriteria = new SortingCriteria("lineItemID", CustomDbOrder.ASCENDING);
			Set<LineItem> proposalLineItemSet = proposalUtilService.createNewLineItemAssocSetForVersionClone(
					getProposalLineItems(optionId, optionversion, null, null, sortCriteria), propVersion , true);
			if (proposalLineItemSet != null && !proposalLineItemSet.isEmpty()) {
				for (LineItem proposalLineItem : proposalLineItemSet) {
					proposalDao.addLineItemsOfProposal(proposalLineItem);
				}
			}
			
		} catch (OgnlException e) {
			LOGGER.error("OgnlException:" + e);
			throw new BusinessException(new CustomBusinessError(), e);
		}
		OptionDb.addNewVersion(propVersion);
		auditService.createAuditForNewVersion(proposalId, propVersion);
		return propVersion.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createOptionClone(java.lang.Long, java.lang.Long, java.lang.Long, int)
	 */
	@Override
	@ValidateProposal
	public Long createOptionClone(final Long proposalId, final Long optionId, final Long optionversion,  int maxOptionNo){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating Clone of a option in createOptionClone()");
		}
		LOGGER.info("Creating version for OptionID: " + optionId);
		ProposalOption OptionDb = getOptionbyId(optionId);
		Transformer transformer = CloneTransformer.getInstance();
		ProposalVersion propVersionDB =  new ProposalVersion();
		for (ProposalVersion proposalVersion : OptionDb.getProposalVersions()) {
			if(proposalVersion.getProposalVersion() == optionversion){
				propVersionDB = proposalVersion;
			}
		}
		ProposalOption propOption = (ProposalOption) transformer.transform(OptionDb);
		ProposalVersion propVersion = (ProposalVersion) transformer.transform(propVersionDB);
		try {
			String newOptionName = (ConstantStrings.OPTION_NAME + maxOptionNo);
			Ognl.setValue("id", propOption, 0);
			Ognl.setValue("defaultOption", propOption, false);
			Ognl.setValue("name", propOption, newOptionName);
			Ognl.setValue("optionNo", propOption, maxOptionNo);
			Ognl.setValue("version", propOption, 1);
			Ognl.setValue("thresholdLimit", propOption, null);
			Ognl.setValue("lastReviewedDate", propOption, null);
			Ognl.setValue("proposalVersion", propVersion, 1L);
			Ognl.setValue("id", propVersion, 0);
			Ognl.setValue("version", propVersion, 1);
			Ognl.setValue("proposalLineItemSet", propVersion, null);
			Set<ProposalVersion> proposalVersionSet = new LinkedHashSet<ProposalVersion>();
			proposalVersionSet.add(propVersion);
			propVersion.setProposalOption(propOption);
			propOption.setProposalVersions(proposalVersionSet);
			propOption = proposalDao.addOption(propOption);
			SortingCriteria sortCriteria = new SortingCriteria("lineItemID", CustomDbOrder.ASCENDING);
			Set<LineItem> proposalLineItemSet = proposalUtilService.createNewLineItemAssocSet(
					getProposalLineItems(optionId, optionversion, null, null, sortCriteria), propVersion, false);
			if (proposalLineItemSet != null && !proposalLineItemSet.isEmpty()) {
				for (LineItem proposalLineItem : proposalLineItemSet) {
					if(proposalLineItem.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
						proposalLineItem.setPricingStatus(PricingStatus.UNAPPROVED);
					}else{
						proposalLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalLineItem,OptionDb.getProposal().isSpecialAdvertiser()));
					}
					proposalDao.addLineItemsOfProposal(proposalLineItem);
				}
			}
		} catch (OgnlException e) {
			LOGGER.error("OgnlException:" + e);
			throw new BusinessException(new CustomBusinessError(), e);
		}
		auditService.createAuditForNewOption(propOption);
		return propOption.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createClone(java.lang.Long, java.lang.String)
	 */
	@Override
	public Proposal createClone(final Long proposalId, final String optionIds){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Proposal Clone Process Started");
		}
		LOGGER.info("Creating Clone for proposalID: " + proposalId);
		Transformer transformer = CloneTransformer.getInstance();
		Proposal proposalDb = proposalDao.getProposalForClone(proposalId);
		String [] optionIdsArr = optionIds.split(",");
		Proposal proposal = new Proposal();
		for(int i=0; i< optionIdsArr.length; i++){
			ProposalOption optionDb = getOptionbyId(Long.valueOf((optionIdsArr[i])));
			ProposalVersion propVersionDB = optionDb.getLatestVersion();
			int maxOptionNo = (i + 1);
			if(i == 0){
				proposal = (Proposal) transformer.transform(proposalDb);				
				String campaignName = StringUtil.generateCampaignName(proposalDb.getCampaignName());				
				String proposalName = generateProposalName(proposalDb,campaignName);				
				Long advertiserId= proposalDb.getSosAdvertiserId();
				if(advertiserId != null && advertiserId >0){
					final Advertiser advertiser = proposalSOSService.getAdvertiserById(advertiserId);
					if(advertiser != null){
						if ('Y' == advertiser.getSpecialAdvertiser() && (advertiser.getSpecialExpiryDate() == null || advertiser.getSpecialExpiryDate().getTime() > DateUtil
					.getCurrentDate().getTime())){
							proposal.setSpecialAdvertiser(true);
						}else{
							proposal.setSpecialAdvertiser(false);
						}
					}else{
						proposal.setSpecialAdvertiser(false);
						proposal.setSosAdvertiserId(null);
					}
				}
				try {
					Ognl.setValue("id", proposal, 0);
					Ognl.setValue("campaignName", proposal, campaignName);
					Ognl.setValue(ConstantStrings.NAME, proposal, proposalName);
					Ognl.setValue("clonedFromProposalVersion", proposal, propVersionDB.getId());
					Ognl.setValue("version", proposal, 1);
					Ognl.setValue("sosOrderId", proposal, null);
					Ognl.setValue("salesforceID", proposal, ConstantStrings.EMPTY_STRING);
					Ognl.setValue("proposalStatus", proposal, ProposalStatus.INPROGRESS);
					Ognl.setValue("dueDate", proposal, DateUtil.getCurrentDateAndUserTime("23:59"));
					Ognl.setValue("dateRequested", proposal, DateUtil.getCurrentDateAndUserTime(DateUtil.getCurrentDateTimeString("HH:mm")));
					User user = SecurityUtil.getUser();
					Ognl.setValue("assignedUser", proposal, user);
					Ognl.setValue("assignedByUser", proposal, user);
					Ognl.setValue("salesforceID", proposal, null);
					Ognl.setValue("sfRevisionType", proposal, null);
					Ognl.setValue("lastProposedDate", proposal, null);
					Ognl.setValue("pricingSubmittedDate", proposal, null);
					proposal = cloneOptionVersionData(proposal,optionDb,propVersionDB,transformer,maxOptionNo);
				} catch (OgnlException e) {
					LOGGER.error("OgnlException:" + e);
					throw new BusinessException(new CustomBusinessError(), e);
				}
			}else{
				cloneOptionVersionData(proposal,optionDb,propVersionDB,transformer,maxOptionNo);
			}
		}		
		auditService.createAuditForNewProposal(proposal);//To audit Proposal Cloning
		return proposal;
	}

	/**
	 * Generates the proposal Name
	 * @param proposalDb
	 * @param campaignName
	 * @return
	 */
	private String generateProposalName(Proposal proposalDb, String campaignName) {
		Long advertiserId= proposalDb.getSosAdvertiserId();
		String advertiserName = ConstantStrings.EMPTY_STRING;
		if(advertiserId != null && advertiserId >0){
			advertiserName = proposalSOSService.getAdvertiserById(advertiserId).getName();
		}else{
			advertiserName = proposalDb.getSosAdvertiserName();
		}
		String salesCategoryName= sosService.getSalesCategoryById(proposalDb.getSosSalesCategoryId()).get(proposalDb.getSosSalesCategoryId());
		return StringUtil.generateProposalName(campaignName,advertiserName,DateUtil.getCurrentDate(),salesCategoryName);
	}	
	
	
	
	 /** Clone the option and version Data along with line Items
	 * @param proposal
	 * @param optionDb
	 * @param propVersionDB
	 * @param transformer 
	 * @param optionId 
	 * @throws CustomCheckedException 
	 */
	private Proposal  cloneOptionVersionData(Proposal proposal, ProposalOption optionDb, ProposalVersion propVersionDB, Transformer transformer,int maxOptionNo) {
		ProposalOption propOption = (ProposalOption) transformer.transform(optionDb);
		ProposalVersion propVersion = (ProposalVersion) transformer.transform(propVersionDB);
		String optionName = ConstantStrings.OPTION_NAME + maxOptionNo;
		try {
			Ognl.setValue("id", propOption, 0);
			Ognl.setValue("version", propOption, 1);
			Ognl.setValue("name", propOption, optionName);
			Ognl.setValue("optionNo", propOption, maxOptionNo);
			Ognl.setValue("proposalVersion", propVersion, 1L);
			Ognl.setValue("id", propVersion, 0);
			Ognl.setValue("version", propVersion, 1);
			Ognl.setValue("thresholdLimit", propOption, null);
			Ognl.setValue("lastReviewedDate", propOption,null);

			Set<ProposalOption> proposalOptionSet = new LinkedHashSet<ProposalOption>();
			proposalOptionSet.add(propOption);
			propOption.setProposal(proposal);			
			proposal.setProposalOptions(proposalOptionSet);
			Set<ProposalVersion> proposalVersionSet = new LinkedHashSet<ProposalVersion>();
			proposalVersionSet.add(propVersion);
			propVersion.setProposalOption(propOption);
			propOption.setProposalVersions(proposalVersionSet);
			if(proposal.getId() == 0){
				propOption.setDefaultOption(true);
				proposalDao.addProposal(proposal) ;
			}else{
				propOption.setDefaultOption(false);
				proposalDao.addOption(propOption);
			}
			
			SortingCriteria sortCriteria = new SortingCriteria("lineItemID", CustomDbOrder.ASCENDING);
			Set<LineItem> proposalLineItemSet = proposalUtilService.createNewLineItemAssocSet(
					getProposalLineItems(optionDb.getId(), propVersionDB.getProposalVersion(), null, null, sortCriteria), propVersion, false);
			if (proposalLineItemSet != null && !proposalLineItemSet.isEmpty()) {
				for (LineItem proposalLineItem : proposalLineItemSet) {
					if(proposalLineItem.getViewabilityLevel() ==  LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
						proposalLineItem.setViewabilityLevel(LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue());
					}
					Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(proposalLineItem, proposal.getSosSalesCategoryId(), proposal.getPriceType());
					if(!LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(proposalLineItem.getPriceType())){
						if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
							Double price = (Double) pricingCalculatorMap.get("price");
							if(PriceType.Gross.name().equals(proposal.getPriceType())){
								price = (price == null || price == 0.0) ? 0 : applyAgencyMargin(proposal.getPriceType(), price, proposal.getAgencyMargin());
							}else{
								boolean israteCardRounded = pricingCalculatorMap.containsKey("rateCardRounded") ? (Boolean) pricingCalculatorMap.get("rateCardRounded") : true;
								if(!israteCardRounded){
									price = (price == null || price == 0.0) ? 0 : NumberUtil.getHalfCentFormatedValue(price);
								}else{
									price = (price == null || price == 0.0) ? 0 : price;
								}
							}
							proposalLineItem.setRateCardPrice(price);
							String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
							proposalLineItem.setPriceCalSummary(priceCalSummary);
							if (proposalLineItem.getRateCardPrice() > 0.00 && proposalLineItem.getViewabilityLevel() !=  LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()) {
								proposalLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalLineItem, proposal.isSpecialAdvertiser()));
							} else {
								proposalLineItem.setPricingStatus(PricingStatus.UNAPPROVED);
							}
						}
					}
					proposalLineItem.setPackageObj(null);
					proposalLineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptionsWhileCopyingLineItem("Proposal", false, false, proposalLineItem, proposalLineItem));
					proposalDao.addLineItemsOfProposal(proposalLineItem);
				}
			}
		} catch (OgnlException e) {
			LOGGER.error("OgnlException:" + e);
			throw new BusinessException(new CustomBusinessError(), e);
		}
		return proposal;
	}
		
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#deleteProposalLineItem(java.lang.Long, java.lang.String)
	 */
	@Override
	@ValidateProposal
	public List<Long> deleteProposalLineItem(final Long proposalId, final String lineItemId) throws CustomCheckedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting proposal Line Items in deleteProposalLineItem()");
		}
		LOGGER.info("Deleting proposal Line Items in deleteProposalLineItem()");
		Long returnId = 0L;
		final List<LineItem> lineItemLst = proposalDao.getLineItems(lineItemId);
		Map<String, String> mailMap = proposalUtilService.sendMailForDeleteReservation(lineItemLst, sosService.getSalesCategories());
		List<Long> deletedLineItemIds = new ArrayList<Long>();
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for (LineItem proposalLineItemDB : lineItemLst) {
			
			LineItem prevHomePageLnItm = proposalUtilService.createNewLineItemFromExisting(proposalLineItemDB, proposalLineItemDB.getProposalId());
			prevHomePageLnItm.setLineItemID(proposalLineItemDB.getLineItemID());
			/* Deleting sales target type association from database */
			List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocs = proposalLineItemDB.getLineItemSalesTargetAssocs();
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocs) {
				proposalDao.deleteLineItemSalesTargetAssoc(lineItemSalesTargetAssoc);
			}
			proposalLineItemDB.setLineItemSalesTargetAssocs(null);
			/* Deleting Geo target type association from database */
			Set<LineItemTarget> lineItemTargetSet = proposalLineItemDB.getGeoTargetSet();
			for (LineItemTarget lineItemTarget : lineItemTargetSet) {
				proposalDao.deleteProposalGeoTargets(lineItemTarget);
			}
			proposalLineItemDB.setGeoTargetSet(null);
			proposalLineItemDB.setActive(false);
			Set<LineItemExceptions> lineItemsExceptionSet = proposalLineItemDB.getLineItemExceptions();
			for (LineItemExceptions lineItemExceptions : lineItemsExceptionSet) {
				proposalDao.deleteLineItemException(lineItemExceptions);
			}
			proposalLineItemDB.setLineItemExceptions(null);
			if (proposalLineItemDB.getReservation() != null) {
				proposalDao.deleteReservation(proposalLineItemDB.getReservation());
				proposalLineItemDB.setReservation(null);
				if(productClassIdLst.contains(proposalLineItemDB.getSosProductClass())){
					auditService.createAuditMessageForHomePageResrvtn(prevHomePageLnItm, ConstantStrings.DELETED);
				}
			}
			returnId = proposalDao.editLineItemsOfProposal(proposalLineItemDB);
			deletedLineItemIds.add(returnId);
		}
		proposalUtilService.sendMail(mailMap);
		return deletedLineItemIds;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveCopiedLineItemsToProposal(java.lang.Long, java.lang.Long, long, java.lang.String, java.lang.Long[], boolean, boolean, int, java.lang.String, boolean, double)
	 */
	@Override
	@ValidateProposal
	public boolean saveCopiedLineItemsToProposal(final Long proposalID, final Long optionId, final long proposalVersion, final String type, final Long[] lineItems,
			boolean partiallyCopiedUnbreakPackage, boolean isCopiedFromExpired, int copiedLineItemIds, final String sourcePriceType, final boolean iscopiedFromGrid, final double sourceAgencyMargin) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Copying LineItems and Adding to Option in addCopiedLineItemsToProposal()");
		}
		LOGGER.info("Copying LineItems and Adding to Option with ID: " + optionId);
		Proposal proposalDBValue = proposalDao.getProposalbyId(proposalID);
		ProposalVersion proposalversion = getproposalVersions(optionId, proposalVersion).get(0);
		List<LineItem> lineItemLst = proposalDao.getLineItemsOnBaseOfId(lineItems);
		int sequenceNo = getNextSequenceNoForLineItem(proposalversion.getId());
		List<LineItem> newLineItemLst = new ArrayList<LineItem>();
		boolean flag = false;
		for (LineItem lineItem : lineItemLst) {
			for (int i = 0; i < copiedLineItemIds; i++) {
				LineItem lineItemNew = proposalUtilService.createNewLineItemFromExisting(lineItem, proposalID);
				if (ConstantStrings.PROPOSAL.equalsIgnoreCase(type)) {
					lineItemNew.setPackageObj(null);
				}
				lineItemNew.setProposalVersion(proposalversion);
				lineItemNew.setLineItemSequence((sequenceNo == 999) ? sequenceNo : sequenceNo++);
				lineItemNew.setPartiallyCopiedUnbreakPackage(partiallyCopiedUnbreakPackage);

				if (iscopiedFromGrid) {
					lineItemNew.setRateCardPrice(lineItem.getRateCardPrice());
					if(lineItemNew.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
						lineItemNew.setPricingStatus(PricingStatus.UNAPPROVED);
					}else{
						lineItemNew.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(lineItemNew , proposalDBValue.isSpecialAdvertiser()));
					}
				} else {
					boolean israteCardRounded = true;
					if (ConstantStrings.PROPOSAL.equalsIgnoreCase(type)) {
						lineItemNew.setRateCardPrice(lineItem.getRateCardPrice());
					}
					
					if (lineItem.isRatex()) {
						lineItemNew = restrictCopy(lineItemNew, proposalDBValue.getSosSalesCategoryId(), proposalDBValue.getPriceType());
					}else if (!LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())) {
						Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(lineItemNew, proposalDBValue.getSosSalesCategoryId(), proposalDBValue.getPriceType());
						if (pricingCalculatorMap == null) {
							flag = true;
						} else if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
							Double price = (Double) pricingCalculatorMap.get("price");
							israteCardRounded = pricingCalculatorMap.containsKey("rateCardRounded") ? (Boolean) pricingCalculatorMap.get("rateCardRounded") : israteCardRounded;
							//if (! (price == null || price == 0.0)) {
								lineItemNew.setRateCardPrice(price);
								String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
								lineItemNew.setPriceCalSummary(priceCalSummary);
						//	}
						}
					}
					if (lineItemNew == null) {
						flag = true;
						break;
					}else {
						if ("Gross".equals(proposalDBValue.getPriceType()) && "Net".equals(sourcePriceType)) {
							updateLineItemsNetToGross(lineItemNew, proposalDBValue.getAgencyMargin(), sourceAgencyMargin);
						} else if ("Net".equals(proposalDBValue.getPriceType()) && "Net".equals(sourcePriceType)) {
							updateLineItemsNetToNet(lineItemNew, israteCardRounded);
						}
					}
					lineItemNew.setPricingStatus(PricingStatus.SYSTEM_APPROVED);
				}
				lineItemNew.setLineItemExceptions(proposalUtilService.getLineItemExceptionsWhileCopyingLineItem(type, partiallyCopiedUnbreakPackage, isCopiedFromExpired, lineItem, lineItemNew));
				newLineItemLst.add(lineItemNew);
			}
		}
		if (!flag) {
			for (LineItem lineItem : newLineItemLst) {
				proposalDao.addLineItemsOfProposal(lineItem);
			}
		}
		return flag;
	}

	/**
	 * This method updates the CPM , rate card price and total investment
	 * when a line item is copied from gross to net.
	 * @param lineItem
	 * @param agencyMarginTarget
	 * @param agencyMarginSource
	 * @return
	 */
	private LineItem updateLineItemsGrossToNet(LineItem lineItem, double agencyMarginTarget, double agencyMarginSource) {
		if (lineItem.getRateCardPrice() != null && lineItem.getRateCardPrice() > 0) {
			Double basePrice = lineItem.getRateCardPrice() * ((100 - agencyMarginSource) / 100);
			lineItem.setRateCardPrice(basePrice);
		}
		applyAgencyMarginGrossToNet(lineItem, agencyMarginTarget, agencyMarginSource);
		return lineItem;
	}

	/**
	 * @param lineItem
	 * @param agencyMarginTarget
	 * @param agencyMarginSource
	 */
	private void applyAgencyMarginGrossToNet(LineItem lineItem, double agencyMarginTarget, double agencyMarginSource) {
		if (lineItem.getRate() != null && lineItem.getRate() > 0) {
			Double calculation = 0.0;
			Double rate = NumberUtil.round(lineItem.getRate(), 2) * ((100 - agencyMarginSource) / 100);
			lineItem.setRate(NumberUtil.round(rate, 2));
			if (lineItem.getImpressionTotal() != null && lineItem.getTotalInvestment() == 0.0) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() == null && lineItem.getRate() > 0.0) {
				calculation = (lineItem.getTotalInvestment() * 1000) / lineItem.getRate();
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() != null) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			}
		}else if (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())){
			lineItem.setTotalInvestment( lineItem.getTotalInvestment() * ((100 - agencyMarginTarget) / 100));
		}
	}
	
	/**
	 * @param lineItem
	 * @param agencyMarginTarget
	 * @param agencyMarginSource
	 */
	private void applyAgencyMarginGrossToGross(LineItem lineItem, double agencyMarginTarget, double agencyMarginSource) {
		if (lineItem.getRate() != null && lineItem.getRate() > 0) {
			Double calculation = 0.0;
			Double rate = NumberUtil.round(lineItem.getRate(), 2) * ((100 - agencyMarginSource) / (100 - agencyMarginTarget));
			lineItem.setRate(NumberUtil.round(rate, 2));
			if (lineItem.getImpressionTotal() != null && lineItem.getTotalInvestment() == 0.0) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() == null && lineItem.getRate() > 0.0) {
				calculation = (lineItem.getTotalInvestment() * 1000) / lineItem.getRate();
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() != null) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			}
		}else if (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())){
			lineItem.setTotalInvestment( lineItem.getTotalInvestment() * ((100 - agencyMarginSource) / (100 - agencyMarginTarget)));
		}
	}
	
	/**
	 * This method updates the CPM , rate card price and total investment
	 * when a line item is copied from net to gross.
	 * @param lineItem
	 * @param agencyMarginTarget
	 * @param agencyMarginSource
	 * @return
	 */
	private LineItem updateLineItemsNetToGross(LineItem lineItem, double agencyMarginTarget, double agencyMarginSource) {
		if (lineItem.getRateCardPrice() != null && lineItem.getRateCardPrice() > 0) {
			Double basePrice = lineItem.getRateCardPrice() * (100 / (100 - agencyMarginTarget));
			lineItem.setRateCardPrice(basePrice);
		}
		applyAgencyMarginNetToGross(lineItem, agencyMarginTarget, agencyMarginSource);
		return lineItem;
	}
	
	/**
	 * @param lineItem
	 * @param agencyMarginTarget
	 * @param agencyMarginSource
	 */
	private void applyAgencyMarginNetToGross(LineItem lineItem, double agencyMarginTarget, double agencyMarginSource) {
		if (lineItem.getRate() != null && lineItem.getRate() > 0) {
			Double calculation = 0.0;
			Double rate = NumberUtil.round(lineItem.getRate(), 2) * (100 / (100 - agencyMarginTarget));
			lineItem.setRate(NumberUtil.round(rate, 2));
			if (lineItem.getImpressionTotal() != null && lineItem.getTotalInvestment() == 0.0) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() == null && lineItem.getRate() > 0.0) {
				calculation = (lineItem.getTotalInvestment() * 1000) / lineItem.getRate();
				lineItem.setTotalInvestment(calculation);
			} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() != null) {
				calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
				lineItem.setTotalInvestment(calculation);
			}
		}else if (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())){
			lineItem.setTotalInvestment( lineItem.getTotalInvestment() * (100 / (100 - agencyMarginTarget)));
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateAllLineItemsPrice(java.lang.Long, java.lang.Long[], java.lang.Long, java.lang.String, double)
	 */
	@Override
	@ValidateProposal
	public void updateAllLineItemsPrice(final Long proposalID, final Long[] lineItems, final Long salesCategoryId, String propPriceType, double propAgencyMargin) {
		if (lineItems != null) {
			Proposal proposal = proposalDao.getProposalForClone(proposalID);
			List<LineItem> lineItemLst = proposalDao.getLineItemsOnBaseOfId(lineItems);
			for (LineItem lineItem : lineItemLst) {
				if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(lineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(lineItem.getPriceType())) {
					Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(lineItem, salesCategoryId, propPriceType);
					if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
						Double price = (Double) pricingCalculatorMap.get("price");
						if("Gross".equalsIgnoreCase(propPriceType)) {
							price = (price == null || price == 0.0) ? 0 : applyAgencyMargin(propPriceType,price, propAgencyMargin);
						}else {
							boolean israteCardRounded = pricingCalculatorMap.containsKey("rateCardRounded") ? (Boolean) pricingCalculatorMap.get("rateCardRounded") : true;
							if(!israteCardRounded){
								price = (price == null || price == 0.0) ? 0 : NumberUtil.getHalfCentFormatedValue(price);
							}else{
								price = (price == null || price == 0.0) ? 0 : price;
							}
						}
						Double existingPrice = lineItem.getRateCardPrice();
						lineItem.setRateCardPrice(price);
						String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
						lineItem.setPriceCalSummary(priceCalSummary);
						lineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptions(lineItem));
						if((!PricingStatus.PRICING_APPROVED.equals(lineItem.getPricingStatus()) || lineItem.getPackageObj() != null ) && !(existingPrice.equals(price))){
							if(lineItem.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
								lineItem.setPricingStatus(PricingStatus.UNAPPROVED);
							}else{
								lineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(lineItem, proposal.isSpecialAdvertiser()));
							}
						}
						proposalDao.editLineItemsOfProposal(lineItem);
					}
				}
			}
		}
	}
	
	@Override
	public int getNextSequenceNoForLineItem(final long versionId) {
		int maxSequence = proposalDao.getMaxSequenceNoForLineItem(versionId);
		return (maxSequence == 999) ? maxSequence : maxSequence + 1;
	}
	
	@Override
	public List<ProposalVersion> getproposalVersions(final Long optionId, final Long proposalversion) {
		return proposalDao.getproposalVersions(optionId, proposalversion);
	}
	
	@Override
	public ProposalVersion getProposalVersion(final long proposalversionId) {
		return proposalDao.getproposalVersion(proposalversionId);
	}
	
	@Override
	public Set<CampaignObjective> getCampaignObjectivesByProposalId(final long proposalId) {
		return proposalDao.getCampaignObjectivesByProposalId(proposalId);
	}
	
	@Override
	public Map<String, Long> getCampaignObjectivesMap() {
		List<CampaignObjective> campObjectiveList = proposalDao.getCampaignObjectives();
		Map<String, Long> campList = new HashMap<String, Long>();
		for (CampaignObjective objective : campObjectiveList) {
			campList.put(objective.getCmpObjText(), objective.getCmpObjId());
		}
		return campList;
	}
	
	@Override
	public List<CampaignObjective> getCampaignObjectives() {
		return proposalDao.getCampaignObjectives();
	}

	@Override
	public int proposalCountWithSameName(final String proposalName, final Long proposalId) {
		return proposalDao.proposalCountWithSameName(proposalName, proposalId);
	}

	@Override
	public List<LineItemExceptions> getLineItemExceptions(final Long lineItemID) {
		return proposalDao.getLineItemExceptions(lineItemID);
	}

	@Override
	public Long updateProposalVersionNetImpressionAndCPM(final Long optionId, final Long proposalVersion) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calculate Net Cpm, Net Impressions of a Proposal updateProposalVersionNetImpressionAndCPM()");
		}
		long netImpressions = 0;
		double netCpm = 0;
		double offerdBudget = 0;
		List<LineItem> proposalLineItemLst = proposalDao.getFilteredProposalLineItems(optionId, proposalVersion, null, null, null);
		if (proposalLineItemLst != null) {
			for (LineItem lineItem : proposalLineItemLst) {
				netImpressions = (long) (netImpressions + lineItem.getImpressionTotal());
				offerdBudget = offerdBudget + lineItem.getTotalInvestment();
			}
			if (netImpressions > 0) {
				netCpm = (offerdBudget / netImpressions) * 1000;
				netCpm = NumberUtil.round(netCpm, 2);
			}
		}
		ProposalVersion proposalversion=new ProposalVersion();
		if(proposalDao.getproposalVersions(optionId, proposalVersion) !=null && proposalDao.getproposalVersions(optionId, proposalVersion).size()>0){
		proposalversion = (ProposalVersion) proposalDao.getproposalVersions(optionId, proposalVersion).get(0);
		}
		proposalversion.setImpressions(netImpressions);
		proposalversion.setEffectiveCpm(netCpm);
		proposalversion.setOfferedBudget(offerdBudget);
		return proposalDao.updateProposalVersion(proposalversion);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalGeoTargets(java.lang.Long)
	 */
	@Override
	public List<LineItemTarget> getProposalGeoTargets(Long elementId) {
		return proposalDao.getProposalGeoTargets(elementId);
	}	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateLineItemsAvails(java.lang.Long, java.util.List)
	 */
	@Override
	@ValidateProposal
	public void updateLineItemsAvails(Long proposalId, List<LineItem> lineItemLst) {
		for (LineItem lineItem : lineItemLst) {
			lineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptions(lineItem));
			proposalDao.editLineItemsOfProposal(lineItem);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	@ValidateProposal
	public ProposalOption saveOption(Long proposalId, ProposalOption option) throws CustomCheckedException {
		if(option.getId() == 0){
			ProposalOption propOption =  proposalDao.addOption(option);
			auditService.createAuditForNewOption(propOption);
			return propOption;
		}else{
			ProposalOption optionDB = proposalDao.getOptionbyId(option.getId());
			optionDB.setBudget(option.getBudget());
			optionDB.setActive(option.isActive());
			if(!option.isActive() && optionDB.getLatestVersion().getProposalLineItemSet() != null){				
				//Set<LineItem> lineItemSet = optionDB.getLatestVersion().getProposalLineItemSet();
				List<LineItem> lineItemsLst = new ArrayList<LineItem>(optionDB.getLatestVersion().getProposalLineItemSet());
				List<LineItem> lineItemLst = new ArrayList<LineItem>(lineItemsLst.size());
				List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
				for (LineItem lineItem : lineItemsLst) {
					lineItemLst.add(lineItem);
					if(lineItem.getReservation() != null &&  productClassIdLst.contains(lineItem.getSosProductClass())){
						auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.DELETED);
						proposalUtilService.sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED);
					}
				}
				Map<String, String> mailMap = proposalUtilService.sendMailForDeleteReservation(lineItemLst, sosService.getSalesCategories());
				proposalUtilService.sendMail(mailMap);
				
			}
			optionDB =  proposalDao.saveOption(optionDB);
			if(!option.isActive()){
				auditService.createAuditForDeleteOption(optionDB);
			}
			return optionDB;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#markOptionDefault(long, long)
	 */
	@Override
	@ValidateProposal
	public void updateOptionAsDefault(long proposalId, long optionId) {
		Proposal proposal = getProposalbyId(proposalId);
		for (ProposalOption option : proposal.getProposalOptions()) {
			if (option.getId() == optionId) {
				option.setDefaultOption(true);
				proposalDao.saveOption(option);
			} else if (option.isDefaultOption()) {
				option.setDefaultOption(false);
				proposalDao.saveOption(option);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalServiceNew#getLineItems(java.lang.Long)
	 */
	@Override
	public LineItem getLineItemById(final Long lineItemId) {
		return proposalDao.getLineItemById(lineItemId);
	}	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateAssignToUser(java.lang.Long, com.nyt.mpt.domain.User)
	 */
	@Override
	public long updateAssignToUser(Long proposalId, User usr) throws CustomCheckedException {
		Proposal proposal = getProposalbyId(proposalId);
		User fromUser = proposal.getAssignedUser();
		User toUser = proposal.getAssignedByUser();
		ProposalStatus fromStatus = proposal.getProposalStatus();
		proposal.setAssignedUser(usr);
		proposal.setSfRevisionType(null);
		proposal.setAssignedByUser(SecurityUtil.getUser());
		if(ProposalStatus.UNASSIGNED.name().equals(proposal.getProposalStatus().name())){
			proposal.setProposalStatus(ProposalStatus.INPROGRESS);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Assign to User for proposal with id" + proposal.getId());
		}
		proposal.setWithPricing(SecurityUtil.isUserPricingAdmin(userService.getUserById(usr.getUserId())));
		long id = proposalDao.saveProposal(proposal).getId();
		if(ProposalStatus.UNASSIGNED.name().equals(fromStatus.name()) && fromUser == null){
			auditService.createAuditForProposalStaus(proposalId, fromStatus, proposal.getProposalStatus());// To audit proposal status
		}
		auditService.createAuditForAssigningProposal(proposal, fromUser);
		createAssignmentLogInSalesForce(proposal, fromUser, toUser, usr);
		return id;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalOptionsById(java.lang.Long)
	 */
	@Override
	public List<ProposalOption> getProposalOptionsById(Long proposalId) {
		return proposalDao.getProposalOptionsById(proposalId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateProposalStatus(long, com.nyt.mpt.util.enums.ProposalStatus)
	 */
	@Override
	public long updateProposalStatus(long proposalId, ProposalStatus status, int version) throws CustomCheckedException, ParseException {
		Proposal proposal = proposalDao.getProposalbyId(proposalId);
		proposal.setVersion(version);
		Map<String, String> mailMap = null;
		if(ProposalStatus.DELETED.getDisplayName().equals(status.getDisplayName())){
			List<LineItem> lineItemLst = new ArrayList<LineItem>();
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			for (ProposalOption option : proposal.getProposalOptions()) {
				List<LineItem> lineItemsLst = new ArrayList<LineItem>(option.getLatestVersion().getProposalLineItemSet());
				for (LineItem lineItem : lineItemsLst){
					lineItemLst.add(lineItem);
					if(lineItem.getReservation() != null &&  productClassIdLst.contains(lineItem.getSosProductClass())){
						auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.DELETED);
						proposalUtilService.sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED);
					}
				}
			}
			mailMap = proposalUtilService.sendMailForDeleteReservation(lineItemLst, sosService.getSalesCategories());
			
		}
		ProposalStatus fromStatus = proposal.getProposalStatus();
		User fromUser = proposal.getAssignedUser();
		User toUser = proposal.getAssignedByUser();
		proposal.setProposalStatus(status);
		if (ProposalStatus.INPROGRESS.getDisplayName().equals(status.getDisplayName())
				&& SecurityUtil.AD_OPS.equals(SecurityUtil.getUser().getUserRoles().iterator().next().getRoleName())) {
			proposal.setAssignedUser(null);
		}else{
			proposal.setAssignedUser(SecurityUtil.getUser());
			proposal.setAssignedByUser(SecurityUtil.getUser());
		}
		proposal.setSfRevisionType(null);
		if(ProposalStatus.PROPOSED.name().equals(proposal.getProposalStatus().name())){
			proposal.setLastProposedDate( DateUtil.parseToDateTime(DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate())));
		}
		
		long returnId = proposalDao.saveProposal(proposal).getId();
		auditService.createAuditForProposalStaus(proposalId, fromStatus, status);// To audit proposal status
		if(fromUser == null || fromUser.getUserId() != SecurityUtil.getUser().getUserId()){
			auditService.createAuditForAssigningProposal(proposal, fromUser);
		}
		if(ProposalStatus.DELETED.getDisplayName().equals(status.getDisplayName())){
			proposalUtilService.sendMail(mailMap);
		}
		if(StringUtils.isNotBlank(proposal.getSalesforceID())){
			if(ProposalStatus.SOLD.name().equals(proposal.getProposalStatus().name())){
				StringBuilder message = new StringBuilder(ConstantStrings.EMPTY_STRING);
				message.append("Proposal bridged to SOS.").append("\t\n");
				message.append(sosURL).append("/cgi-bin/sos/sales/calc.pl?SALESORDER_ID=").append(proposal.getSosOrderId());
				auditService.salesforceAuditLog(proposal, message);
				
				/*Update order id in SalesForce */
				Media_Plan__c sfProposal = new Media_Plan__c();
				sfProposal.setId(proposal.getSalesforceID());
				sfProposal.setSOSOrderId__c(String.valueOf(proposal.getSosOrderId()));
				salesForceProposalService.updateMediaPlanToSalesforce(sfProposal);
				salesForceProposalService.updateOptionsInSalesForce(proposal, proposal.getSalesforceID());
			}
			if(ProposalStatus.PROPOSED.name().equals(proposal.getProposalStatus().name())){
				auditService.salesforceAuditLog(proposal, new StringBuilder("Proposal status changed to 'Proposed'."));
				salesForceProposalService.createOptionsInSf(proposal);
			}
		}

		createAssignmentLogInSalesForce(proposal, fromUser, toUser,  proposal.getAssignedUser());
		return returnId;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateProposalConversionRate(long, double)
	 */
	@Override
	public Proposal updateProposalConversionRate(long proposalId, Map<String,Double> conversionRateMap) {
		Proposal proposal = proposalDao.getProposalForClone(proposalId);
		proposal.setConversionRate(conversionRateMap.get(proposal.getCurrency()));
		proposal.setConversionRate_Euro(conversionRateMap.get("EUR"));
		proposal.setConversionRate_Yen(conversionRateMap.get("CNY"));
		return proposalDao.saveProposal(proposal);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateProposalOrderId(long, java.lang.String)
	 */
	@Override
	public long updateProposalOrderId(long proposalId, Long sosOrderId) {
		Proposal proposal = proposalDao.getProposalForClone(proposalId);
		proposal.setSosOrderId(sosOrderId);
		return proposalDao.saveProposal(proposal).getId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveProposal(java.lang.Long, com.nyt.mpt.domain.Proposal)
	 */
	@Override
	@ValidateProposal
	public Notes saveNotes(final long proposalId, final long notesId, final Notes notes) {
		//long notesID = 0;
		Notes returnedNotes = new Notes();
		if (notes.getId() == 0) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Saving the notes: " + notes.getId());
			}
			returnedNotes = proposalDao.addNotes(notes);
		} else {
			Notes notesDb = proposalDao.getNotesById(notesId);
			notesDb.setDescription(notes.getDescription());
			notesDb.setCreatedByUserName(notes.getCreatedByUserName());
			notesDb.setRole(notes.getRole());
			notesDb.setPushedInSalesforce(notes.isPushedInSalesforce());
			returnedNotes = proposalDao.saveNotes(notesDb);
		}
		return returnedNotes;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveProposal(java.lang.Long, com.nyt.mpt.domain.Proposal)
	 */
	@Override
	@ValidateProposal
	public Long deleteNotes(final long proposalId, final long notesId) {
		Notes notesDb = proposalDao.getNotesById(notesId);
		notesDb.setActive(false);
		long id = proposalDao.saveNotes(notesDb).getId();
		return id;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveProposal(java.lang.Long, com.nyt.mpt.domain.Proposal)
	 */
	@Override
	public List<Notes> getProposalNotes(final long proposalId) {
		return proposalDao.getProposalNotes(proposalId);	
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveProposal(java.lang.Long, com.nyt.mpt.domain.Proposal)
	 */
	@Override
	public List<Proposal> getProposalsForUpdation(Date expiryProposedDate, List<ProposalStatus> statusLst) {
		return proposalDao.getProposalsForUpdation(expiryProposedDate, statusLst);	
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateProposalStatus(long, com.nyt.mpt.util.enums.ProposalStatus)
	 */
	@Override
	public List<Proposal> updateProposalStatus(List<Proposal> proposalLst , ProposalStatus newStatus) {
		List<Proposal> expiredProposalLst = new ArrayList<Proposal>();
		for (Proposal proposal : proposalLst) {
			Proposal proposalDb = proposalDao.getProposalForClone(proposal.getId());			
			ProposalStatus fromStatus = proposal.getProposalStatus();
			proposalDb.setProposalStatus(newStatus);
			Proposal updatedProposal = proposalDao.saveProposal(proposalDb);
			
			//To audit Proposal Status
			auditService.createAuditForProposalStaus(proposal.getId(), fromStatus, newStatus);			
			expiredProposalLst.add(updatedProposal);
		}
		
		return expiredProposalLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#deleteReservationByLineItemId(java.lang.Long)
	 */
	@Override
	public boolean deleteReservationByLineItemId(Long lineItemId) throws CustomCheckedException {
		boolean returnFlag = false;
		LineItem lineItem = proposalDao.getLineItemById(lineItemId);
		if(lineItem.getReservation() != null){
			Map<String, String> mailProps = new HashMap<String, String>();
			mailProps = proposalUtilService.sendMailForDeletedReservationStatus(lineItem, sosService.getSalesCategories());		
			proposalDao.deleteReservation(lineItem.getReservation());
			lineItem.setReservation(null);
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			if(productClassIdLst.contains(lineItem.getSosProductClass())){
				auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.DELETED);
			}
			proposalDao.editLineItemsOfProposal(lineItem);
			proposalUtilService.sendMail(mailProps);
			returnFlag = true;
		}
		return returnFlag;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getAllReservedLineItemsForProposal(java.lang.Long, java.util.List, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<LineItem> getAllReservedLineItemsForProposal(final Long proposalId, final List<Long> proposalVersionIdLst, final FilterCriteria filterCriteria, final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		return proposalDao.getAllReservedLineItemsForProposal(proposalId, proposalVersionIdLst, filterCriteria, pgCriteria, sortingCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getReservedLineItemsCount(java.lang.Long, java.util.List)
	 */
	@Override
	public int getReservedLineItemsCount(final Long proposalId, final List<Long> proposalVersionIdLst) {
		return proposalDao.getReservedLineItemsCount(proposalId, proposalVersionIdLst);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getReservationBylineItemID(java.lang.Long)
	 */
	@Override
	public List<LineItemReservations> getReservationBylineItemID(final Long lineItemID){
		return proposalDao.getReservationBylineItemID(lineItemID);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getReservationsBylineItemIDs(java.util.List)
	 */
	@Override
	public List<LineItemReservations> getReservationsBylineItemIDs(final List<Long> lineItemIDs){
		List<LineItemReservations>  reservationLst = proposalDao.getReservationsBylineItemIDs(lineItemIDs);
		for (LineItemReservations lineItemReservations : reservationLst) {
			LineItem lineItem = lineItemReservations.getProposalLineItem();
			Hibernate.initialize(lineItem.getLineItemSalesTargetAssocs());
		}
		return reservationLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateLineItemReservations(com.nyt.mpt.domain.LineItemReservations)
	 */
	@Override
	public LineItemReservations updateLineItemReservations(LineItemReservations lineItemReservations) {
		return  proposalDao.updateLineItemReservations(lineItemReservations);
	}


	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateReservations(long, long, java.util.Date)
	 */
	@Override
	@ValidateProposal
	public LineItemReservations updateReservations(long proposalID, long lineItemID, Date expiryDate) throws CustomCheckedException {
		List<LineItemReservations> lineItemReservations = getReservationBylineItemID(lineItemID);
		if (lineItemReservations != null && !lineItemReservations.isEmpty()) {
			lineItemReservations.get(0).setLastRenewedOn(DateUtil.getCurrentDate());
			lineItemReservations.get(0).setExpirationDate(expiryDate);
			lineItemReservations.get(0).setStatus(ReservationStatus.RE_NEW);
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			if(lineItemReservations.get(0) != null && productClassIdLst.contains(lineItemReservations.get(0).getProposalLineItem().getSosProductClass()) ){
				auditService.createAuditMessageForHomePageResrvtn(lineItemReservations.get(0).getProposalLineItem(),ConstantStrings.RENEWED);
			}
			
		}
		return  proposalDao.updateLineItemReservations(lineItemReservations.get(0));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createAndMoveReservationData(long, long, long)
	 */
	@Override
	@ValidateProposal
	public Set<LineItem> createAndMoveReservationData(long proposalID, long lineItemID, long newOptionId) {
		LineItem lineItemDB = getLineItemById(lineItemID);
		ProposalOption proposalOption = getOptionbyId(newOptionId);
		List<LineItem> proposalLineItems = new ArrayList<LineItem>();
		proposalLineItems.add(lineItemDB);
		Set<LineItem> lineItemSet = proposalUtilService.createNewLineItemAssocSet(proposalLineItems, proposalOption.getLatestVersion(), true);
		if (lineItemSet != null && !lineItemSet.isEmpty()) {
			for (LineItem proposalLineItem : lineItemSet) {
				if(proposalLineItem.getViewabilityLevel() !=  LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
					proposalLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalLineItem, getOptionbyId(newOptionId).getProposal().isSpecialAdvertiser()));
				}
				proposalLineItem.setLineItemSequence(getNextSequenceNoForLineItem(proposalLineItem.getProposalVersion().getId()));
				proposalDao.addLineItemsOfProposal(proposalLineItem);
			}
		}
		return lineItemSet;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#applyAgencyMargin(java.lang.String, double)
	 */
	@Override
	public double applyAgencyMargin(String priceType,double basePrice, double propAgencyMargin){
		if(StringUtils.isNotBlank(priceType) && PriceType.Gross.name().equals(priceType)){
			if(basePrice !=0){
				basePrice=basePrice*100/(100-propAgencyMargin);
				basePrice = NumberUtil.round(basePrice, 2);
			}
		}
		return basePrice;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalsForReservationSearch(java.util.List)
	 */
	@Override
	public List<LineItem> getProposalsForReservationSearch(final List<RangeFilterCriteria> criteriaLst) {
		return proposalDao.getProposalsForReservationSearch(criteriaLst,null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createAndMoveReservationData(long, long, long)
	 */
	@Override
	public Set<LineItem> saveReservationDataFrmProposalToAnother(long lineItemID, long newOptionId, String expirationDate) throws CustomCheckedException {
		Map<String, String> mailProps = new HashMap<String, String>();
		LineItem lineItemDB = getLineItemById(lineItemID);
		ProposalOption proposalOption = getOptionbyId(newOptionId);
		List<LineItem> proposalLineItems = new ArrayList<LineItem>(1);
		proposalLineItems.add(lineItemDB);
		
		LineItem prevHomePageLnItm = proposalUtilService.createNewLineItemFromExisting(lineItemDB, lineItemDB.getProposalVersion().getProposalOption().getProposal().getId());
		prevHomePageLnItm.setLineItemID(lineItemDB.getLineItemID());
		LineItemReservations reservationOld = lineItemDB.getReservation();
		LineItemReservations reservationNew = new LineItemReservations();
		reservationNew.setStatus(reservationOld.getStatus());
		reservationNew.setExpirationDate(reservationOld.getExpirationDate());
		reservationNew.setProposalLineItem(prevHomePageLnItm);
		reservationNew.setCreatedDate(reservationOld.getCreatedDate());
		reservationNew.setCreatedBy(reservationOld.getCreatedBy());
		reservationNew.setLastRenewedOn(reservationOld.getLastRenewedOn());
		prevHomePageLnItm.setReservation(reservationNew);
		
		Set<LineItem> lineItemSet = proposalUtilService.createNewLineItemAssocSet(proposalLineItems, proposalOption.getLatestVersion(), true);
		Proposal targetProposal = proposalOption.getProposal();
		Proposal sourceProposal = getProposalForClone(lineItemDB.getProposalId());
		if (lineItemSet != null && !lineItemSet.isEmpty()) {
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			for (LineItem proposalLineItem : lineItemSet) {
				
				Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(proposalLineItem, proposalOption.getProposal().getSosSalesCategoryId(), targetProposal.getPriceType());
				
				if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
					Double price = (Double) pricingCalculatorMap.get("price");		
					price = (price == null) ? 0.0 : price;
					if(PriceType.Gross.name().equals(targetProposal.getPriceType())) {
						price = applyAgencyMargin(targetProposal.getPriceType(), price, Double.valueOf(targetProposal.getAgencyMargin()));
					}else {
						boolean israteCardRounded = pricingCalculatorMap.containsKey("rateCardRounded") ? (Boolean) pricingCalculatorMap.get("rateCardRounded") : true;
						if(!israteCardRounded){
							price = NumberUtil.getHalfCentFormatedValue(price);
						}
					}
					proposalLineItem.setRateCardPrice(price);
					String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
					proposalLineItem.setPriceCalSummary(priceCalSummary);
				}
				proposalLineItem.setLineItemSequence(getNextSequenceNoForLineItem(proposalLineItem.getProposalVersion().getId()));
				LineItemReservations reservation = proposalLineItem.getReservation();
				if(reservation != null){
					reservation.setStatus(ReservationStatus.HOLD);
					reservation.setExpirationDate(DateUtil.parseToDate(expirationDate.trim()));
					// set created date when moving reservation from one proposal to another
					reservation.setCreatedDate(DateUtil.getCurrentDate());
				}
				proposalLineItem.setReservation(reservation);				
				if (PriceType.Gross.name().equals(targetProposal.getPriceType()) && PriceType.Gross.name().equals(sourceProposal.getPriceType())) {
					applyAgencyMarginGrossToGross(proposalLineItem, targetProposal.getAgencyMargin(), sourceProposal.getAgencyMargin());
				} else if (PriceType.Gross.name().equals(targetProposal.getPriceType()) && PriceType.Net.name().equals(sourceProposal.getPriceType())) {
					applyAgencyMarginNetToGross(proposalLineItem, targetProposal.getAgencyMargin(), sourceProposal.getAgencyMargin());
				} else if (PriceType.Net.name().equals(targetProposal.getPriceType()) && PriceType.Gross.name().equals(sourceProposal.getPriceType())) {
					applyAgencyMarginGrossToNet(proposalLineItem, targetProposal.getAgencyMargin(), sourceProposal.getAgencyMargin());
				}
				if(proposalLineItem.getViewabilityLevel() == LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()){
					proposalLineItem.setPricingStatus(PricingStatus.UNAPPROVED);
				}else{
					proposalLineItem.setPricingStatus(pricingStatusCalculatorService.getPricingStatus(proposalLineItem, proposalOption.getProposal().isSpecialAdvertiser()));
				}
				proposalLineItem.setLineItemExceptions(proposalUtilService.getLineItemExceptionsWhileCopyingLineItem("Proposal", false, false, proposalLineItem, proposalLineItem));
				proposalDao.addLineItemsOfProposal(proposalLineItem);	
				if(proposalLineItem.getReservation() != null &&  productClassIdLst.contains(proposalLineItem.getSosProductClass())){
						auditService.createAuditMessageForHomePageResrvtn(proposalLineItem, ConstantStrings.CREATED);
						auditService.createAuditMessageForHomePageResrvtn(proposalLineItems.get(0),ConstantStrings.DELETED);
						proposalUtilService.sendMailForHomeResrvtn(prevHomePageLnItm, sourceProposal, ConstantStrings.DELETED);
						proposalUtilService.sendMailForHomeResrvtn(proposalLineItem, targetProposal, ConstantStrings.CREATED);
				}
				/*Send mail when reservations moved from one proposal to another*/
				mailProps = proposalUtilService.sendMailWhenMoveReservationFrmProposalToAnother(lineItemDB, proposalLineItem);
				proposalUtilService.sendMail(mailProps);
			}
		}
		return lineItemSet;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#deleteReservationsOfNonDefaultOptions(long)
	 */
	@Override
	public List<LineItem> deleteReservationsOfNonDefaultOptions(long proposalId) throws CustomCheckedException {
		Proposal proposal = proposalDao.getProposalbyId(proposalId);
		List<LineItem> lineItemLst = new ArrayList<LineItem>();
		for(ProposalOption option : proposal.getProposalOptions()){
			if(!option.isDefaultOption()){
				lineItemLst.addAll(option.getLatestVersion().getProposalLineItemSet());
			}
		}
		List<LineItem> lineItemsToBeDeletedLst= new ArrayList<LineItem>();
		/*Map<String, String> mailMap = proposalUtilService.sendMailForDeleteReservation(lineItemLst, sosService.getSalesCategories());*/
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for(ProposalOption option : proposal.getProposalOptions()){
			if(!option.isDefaultOption()){
				for(LineItem lineItem : option.getLatestVersion().getProposalLineItemSet()){
					if(lineItem.getReservation() != null){
						LineItem prevHomePageLnItm = proposalUtilService.createNewLineItemFromExisting(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal().getId());
						prevHomePageLnItm.setLineItemID(lineItem.getLineItemID());
						LineItemReservations reservation = lineItem.getReservation();
						LineItemReservations reservationNew = new LineItemReservations();
						reservationNew.setStatus(reservation.getStatus());
						reservationNew.setExpirationDate(reservation.getExpirationDate());
						reservationNew.setProposalLineItem(prevHomePageLnItm);
						reservationNew.setCreatedDate(reservation.getCreatedDate());
						reservationNew.setCreatedBy(reservation.getCreatedBy());
						reservationNew.setLastRenewedOn(reservation.getLastRenewedOn());
						prevHomePageLnItm.setReservation(reservationNew);
						lineItemsToBeDeletedLst.add(prevHomePageLnItm);
						
						proposalDao.deleteReservation(lineItem.getReservation());
						lineItem.setReservation(null);
						proposalDao.editLineItemsOfProposal(lineItem);
						if( productClassIdLst.contains(lineItem.getSosProductClass())){
							auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.DELETED);
						}
					}
				}
			}
		}
		/*proposalUtilService.sendMail(mailMap);*/
		return lineItemsToBeDeletedLst;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalAndAssgndUsr(java.lang.Long)
	 */
	@Override
	public Proposal getProposalAndAssgndUsr(Long proposalId) {
		Proposal proposal = proposalDao.getProposalForClone(proposalId);
		if(proposal != null){
			Hibernate.initialize(proposal.getAssignedUser());
		}
		return proposal;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalsAndAssgndUsrs(java.util.List)
	 */
	@Override
	public List<Proposal> getProposalsAndAssgndUsrs(List<Long> proposalId) {
		List<Proposal> proposalLst = proposalDao.getProposalsByID(proposalId);
		for (Proposal proposal : proposalLst) {
			Hibernate.initialize(proposal.getAssignedUser());
		}
		return proposalLst;
	}
	

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalbyIdForSosIntegration(java.lang.Long)
	 */
	@Override
	public Proposal getProposalbyIdForSosIntegration(Long proposalId) {
		Proposal prop =  proposalDao.getProposalbyIdForSosIntegration(proposalId);
		if(prop != null){
			for (LineItem lineItem : prop.getDefaultOption().getLatestVersion().getProposalLineItemSet()) {
				Hibernate.initialize(lineItem.getGeoTargetSet());
				Hibernate.initialize(lineItem.getLineItemSalesTargetAssocs());
			}
		}
		return prop;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getQualifyingLineItems()
	 */
	@Override
	public Map<String, String> getQualifyingLineItems() {
		return proposalDao.getQualifyingLineItems();
	}

	/**
	 * Copy package line item when rate x is checked
	 * @param lineItem
	 * @param salesCategoryId
	 * @param priceType 
	 * @return
	 */
	private LineItem restrictCopy(LineItem lineItem, final Long salesCategoryId, String priceType) {
		boolean flag = false;
		Map<String, Object> pricingCalculatorMap = pricingCalculator.getLineItemPrice(lineItem, salesCategoryId, priceType);
		if (pricingCalculatorMap == null) {
			flag = true;
		} else if (pricingCalculatorMap != null && !pricingCalculatorMap.isEmpty()) {
			Double price = (Double) pricingCalculatorMap.get("price");
			if (price == null || price == 0.0) {
				flag = true;
			} else {
				lineItem.setRateCardPrice(price);
				lineItem.setRate(price);
				String priceCalSummary = pricingCalculatorMap.get("calculatorStep") == null ? ConstantStrings.EMPTY_STRING : pricingCalculatorMap.get("calculatorStep").toString();
				lineItem.setPriceCalSummary(priceCalSummary);
			}
		}

		if (flag) {
			lineItem = null;
		}
		return lineItem;
	}

	/**
	 * Method convert rates from Net to Net
	 * @param lineItem
	 * @param israteCardRounded 
	 * @return
	 */
	private LineItem updateLineItemsNetToNet(LineItem lineItem, boolean israteCardRounded) {
		if (lineItem != null) {
			if (lineItem.getRateCardPrice() != null && lineItem.getRateCardPrice() > 0) {
				if(!israteCardRounded){
					lineItem.setRateCardPrice(NumberUtil.getHalfCentFormatedValue(lineItem.getRateCardPrice()));
					lineItem.setRate(NumberUtil.getHalfCentFormatedValue(lineItem.getRate()));
				}
				Double calculation = 0.0;
				if (lineItem.getImpressionTotal() != null && lineItem.getTotalInvestment() == 0.0) {
					calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
					lineItem.setTotalInvestment(calculation);
				} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() == null && lineItem.getRate() > 0.0) {
					calculation = (lineItem.getTotalInvestment() * 1000) / lineItem.getRate();
					lineItem.setTotalInvestment(calculation);
				} else if (lineItem.getTotalInvestment() != null && lineItem.getImpressionTotal() != null) {
					calculation = (lineItem.getRate() * lineItem.getImpressionTotal()) / 1000;
					lineItem.setTotalInvestment(calculation);
				}
			}
		}
		return lineItem;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getLineItemsOnBaseOfId(java.lang.Long[])
	 */
	@Override
	public List<LineItem> getLineItemsOnBaseOfId(Long[] lineitems ){
		return proposalDao.getLineItemsOnBaseOfId(lineitems );
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateAllLineItemPricingStatus(java.lang.Long)
	 */
	@Override
	public void updateAllLineItemPricingStatus(final Long optionID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Update all line item's pricing status for selected option latest version");
		}
		ProposalOption optionDb = getOptionbyId(optionID);
		pricingStatusCalculatorService.addThreshHoldCheckForOption(optionDb);
		pricingStatusCalculatorService.addAddedValueCheckForOption(optionDb);
	}

	/**
	 * Create audit log message when due date and sales category are changed
	 * @param proposalDb
	 * @param isDueDateChanged
	 * @param isSalesCategoryChanged
	 * @return
	 */
	private StringBuilder createAuditLogMessage(Proposal proposalDb, final boolean isDueDateChanged, final boolean isSalesCategoryChanged){
		StringBuilder message = new StringBuilder(ConstantStrings.EMPTY_STRING);
		message = message.append("Proposal");
		message.append(ConstantStrings.SPACE);
		if(isDueDateChanged){
			message = message.append("Due Date changed to").append(ConstantStrings.SPACE).append(ConstantStrings.SINGLE_QUOTE).append(DateUtil.getGuiDateTimeString(proposalDb.getDueDate())).append(ConstantStrings.SINGLE_QUOTE);
		}
		if (isSalesCategoryChanged) {
			String salesCategoryName= sosService.getSalesCategoryById(proposalDb.getSosSalesCategoryId()).get(proposalDb.getSosSalesCategoryId());
			if(StringUtils.isNotBlank(salesCategoryName)){
				if(isDueDateChanged){
					message.append(ConstantStrings.SPACE);
					message.append("and").append(ConstantStrings.SPACE);
				}
				message = message.append("Sales Category changed to").append(ConstantStrings.SPACE).append(ConstantStrings.SINGLE_QUOTE).append(salesCategoryName).append(ConstantStrings.SINGLE_QUOTE);	
			}
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#saveOptionsThresholdValue(Set<ProposalOption> optionSet)
	 */
	@Override
	public void saveOptionsThresholdValue(Set<ProposalOption> optionSet){
		for (ProposalOption proposalOption : optionSet) {
			proposalDao.saveOption(proposalOption);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateLineItemsOfProposal(java.util.List)
	 */
	@Override
	public void updateLineItemsOfProposal(List<LineItem> lineItemLst){
		for (LineItem lineItem : lineItemLst) {
			 proposalDao.editLineItemsOfProposal(lineItem);
		}
	}

	/**
	 * Create log when proposal created from sales force and assign to any user
	 * @param proposal
	 * @param fromUser
	 * @param toUser
	 * @param currentUser
	 * @throws CustomCheckedException 
	 */
	private void createAssignmentLogInSalesForce(Proposal proposal, User fromUser, User toUser, User currentUser) throws CustomCheckedException{
		if(StringUtils.isNotBlank(proposal.getSalesforceID()) && fromUser == null && toUser == null){
			//When proposal created from sales force and not assign to any user
			 StringBuilder message = new StringBuilder(ConstantStrings.EMPTY_STRING);
			toUser = userService.getUserById(currentUser.getUserId());
			message.append("Proposal assigned to").append(ConstantStrings.SPACE).append(toUser.getFullName()).append(".");
			auditService.salesforceAuditLog(proposal, message);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getLineItemsList(java.lang.String)
	 */
	@Override
	public List<LineItem> getLineItemsList(final String lineItemId){
		return proposalDao.getLineItemsList(lineItemId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getOptionLstbyIds(java.lang.Long[])
	 */
	@Override
	public List<ProposalOption> getOptionLstbyIds(final Long[] optionIds) {
		return proposalDao.getOptionLstbyIds(optionIds);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createLineItemsBySpliting(java.lang.Long, java.lang.Long, long, java.util.List)
	 */
	@Override
	public boolean createLineItemsBySpliting(final Long proposalID, final Long optionId, final long proposalVersion, final String lineItemIds) throws CustomCheckedException{
		List<LineItem> lineItemLst = proposalDao.getLineItems(lineItemIds);
		boolean lineItemsImpressionsTooLow = false;
		ProposalVersion proposalversion = getproposalVersions(optionId, proposalVersion).get(0);
		int sequenceNo = getNextSequenceNoForLineItem(proposalversion.getId());
		List<LineItem> newLineItemLst = new ArrayList<LineItem>();
		List<LineItem> splitlineItemsLst = new ArrayList<LineItem>();
		
		for (LineItem lineItem : lineItemLst) {
			Calendar startDate = Calendar.getInstance();
			startDate.setTime(lineItem.getStartDate());
			
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(lineItem.getEndDate());
		
			long noOfFlightMonths = DateUtil.monthsBetween(startDate, endDate);
			long noOfFlightDays = DateUtil.daysBetween(startDate, endDate);
			double impressionsPerDay =  (double)lineItem.getImpressionTotal()/noOfFlightDays;
			
			if(noOfFlightMonths > 1){
				for(int i = 0 ; i < noOfFlightMonths ; i ++){
					LineItem lineItemNew = proposalUtilService.createNewLineItemFromExisting(lineItem, proposalID);
					
					lineItemNew.setProposalVersion(proposalversion);
					lineItemNew.setLineItemSequence((sequenceNo == 999) ? sequenceNo : sequenceNo++);
					
					Calendar newEndDate =  Calendar.getInstance();
					if( i != 0){
						startDate.add(Calendar.MONTH, 1);
						startDate.set(Calendar.DATE, 1);
					}if(i != (noOfFlightMonths - 1)){
						newEndDate.set(Calendar.MONTH, startDate.get(Calendar.MONTH));
						newEndDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR));
						newEndDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
					}else{
						newEndDate = endDate;
					}
					long totalNoOfFlightDays = DateUtil.daysBetween(startDate, newEndDate);
					lineItemNew.setStartDate(startDate.getTime());
					lineItemNew.setEndDate(newEndDate.getTime());
					lineItemNew.setImpressionTotal((long)(totalNoOfFlightDays * impressionsPerDay));
					
					if((long)lineItemNew.getImpressionTotal() == 0){
						lineItemsImpressionsTooLow = true;
						return lineItemsImpressionsTooLow;
					}
					
					if(!LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue().equals(lineItem.getPriceType())){
						lineItemNew.setTotalInvestment((lineItemNew.getRate() * lineItemNew.getImpressionTotal())/1000);
					}
					lineItemNew.setPricingStatus(lineItem.getPricingStatus());
					boolean isCopiedFromExpiredPkg = false;
					String type = "Proposal";
					Set<LineItemExceptions> lineItemExceptions = lineItem.getLineItemExceptions();
					for (LineItemExceptions lineItemExptn : lineItemExceptions) {
						if(LineItemExceptionEnum.EXPIREDPACKAGE.name().equalsIgnoreCase(lineItemExptn.getLineItemException())){
							isCopiedFromExpiredPkg = true;
							type = "package";
							break;
						}
					}
					lineItemNew.setLineItemExceptions(proposalUtilService.getLineItemExceptionsWhileCopyingLineItem(type, lineItemNew.isPartiallyCopiedUnbreakPackage(), isCopiedFromExpiredPkg, lineItem, lineItemNew));
					newLineItemLst.add(lineItemNew);
				}
				splitlineItemsLst.add(lineItem);
			}
		}
		
		for (LineItem lineItem : splitlineItemsLst) {
			deleteLineItem(lineItem);
		}
		
		for (LineItem lineItem : newLineItemLst) {
			proposalDao.addLineItemsOfProposal(lineItem);
		}
		
		return lineItemsImpressionsTooLow;
	}
	
	
	private void deleteLineItem(LineItem lineItem) throws CustomCheckedException{
		/* Deleting sales target type association from database */
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocs = lineItem.getLineItemSalesTargetAssocs();
		for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocs) {
			proposalDao.deleteLineItemSalesTargetAssoc(lineItemSalesTargetAssoc);
		}
		lineItem.setLineItemSalesTargetAssocs(null);
		/* Deleting Geo target type association from database */
		Set<LineItemTarget> lineItemTargetSet = lineItem.getGeoTargetSet();
		for (LineItemTarget lineItemTarget : lineItemTargetSet) {
			proposalDao.deleteProposalGeoTargets(lineItemTarget);
		}
		lineItem.setGeoTargetSet(null);
		lineItem.setActive(false);
		Set<LineItemExceptions> lineItemsExceptionSet = lineItem.getLineItemExceptions();
		for (LineItemExceptions lineItemExceptions : lineItemsExceptionSet) {
			proposalDao.deleteLineItemException(lineItemExceptions);
		}
		lineItem.setLineItemExceptions(null);
		if (lineItem.getReservation() != null) {
			proposalDao.deleteReservation(lineItem.getReservation());
			lineItem.setReservation(null);
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			if(productClassIdLst.contains(lineItem.getSosProductClass())){
				auditService.createAuditMessageForHomePageResrvtn(lineItem, ConstantStrings.DELETED);
			}
		}
		proposalDao.editLineItemsOfProposal(lineItem);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#createLineItemToOtherOption(com.nyt.mpt.domain.Proposal, java.lang.Long, java.util.List, java.lang.Long[])
	 */
	@Override
	public void createLineItemToOtherOption(final Proposal proposalDBValue, final Long fromOptionId, final List<String> toOptionList, final Long[] lineItemIds) {
		for (String optionId : toOptionList) {
			for (ProposalOption proposalOption : proposalDBValue.getProposalOptions()) {
				if (Long.valueOf(optionId) == proposalOption.getId()) {
					saveCopiedLineItemsToProposal(proposalDBValue.getId(), Long.valueOf(optionId), proposalOption.getLatestVersion().getProposalVersion(), "Proposal", lineItemIds, false, false, 1,
							proposalDBValue.getPriceType(), true, proposalDBValue.getAgencyMargin());
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalListhavingLineItems(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<Proposal> getProposalListhavingLineItems(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		return proposalDao.getProposalListHavingLineItems(criteriaLst, pgCriteria, sortCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#updateAllLineItemReservations(java.util.List)
	 */
	@Override
	public void updateAllLineItemReservations(List<LineItemReservations> reservationLst) throws CustomCheckedException {
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		for (LineItemReservations lineItemReservation : reservationLst) {
			 proposalDao.updateLineItemReservations(lineItemReservation);
				if(lineItemReservation != null && productClassIdLst.contains(lineItemReservation.getProposalLineItem().getSosProductClass()) ){
					auditService.createAuditMessageForHomePageResrvtn(lineItemReservation.getProposalLineItem(),ConstantStrings.RENEWED);
				}
		}
	}
	
 
	@Override
   public List<LineItem> getVulnerableHomepageReservations(Long productClassId){
		return proposalDao.getVulnerableHomepageReservations(productClassId);
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
	
	public void setProposalDao(final IProposalDAO proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setProposalUtilService(final ProposalUtilService proposalUtilService) {
		this.proposalUtilService = proposalUtilService;
	}

	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}

	public void setProposalSOSService(IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setSosService(ISOSService sosService) {
		this.sosService = sosService;
	}

	/**
	 * @param pricingCalculator the pricingCalculator to set
	 */
	public void setPricingCalculator(IPricingCalculator pricingCalculator) {
		this.pricingCalculator = pricingCalculator;
	}

	public void setPricingStatusCalculatorService(IPricingStatusCalculatorService pricingStatusCalculatorService) {
		this.pricingStatusCalculatorService = pricingStatusCalculatorService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSosURL(String sosURL) {
		this.sosURL = sosURL;
	}

	public void setSalesForceProposalService(ISalesForceProposalService salesForceProposalService) {
		this.salesForceProposalService = salesForceProposalService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
	
}
