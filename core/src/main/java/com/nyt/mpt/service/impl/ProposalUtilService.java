/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IProposalDAO;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemExceptions;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.MailUtil;
import com.nyt.mpt.util.enums.LineItemExceptionEnum;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * @author amandeep.singh
 *
 */
public class ProposalUtilService {
	
	private static final Logger LOGGER = Logger.getLogger(ProposalUtilService.class);
	private IProposalDAO proposalDao;
	private String applicationURL;	
	private MailUtil mailUtil;
	private IUserService userService;
	private AuditService auditService;
	private IProposalService proposalService;
	private ISOSService sosService;
	private ICalendarReservationService reservationService;
	private IProductService productService;
	
	/**
	 * Updates a proposal
	 * @param proposalDb
	 * @param proposal
	 */
	public void updateProposal(Proposal proposalDb, Proposal proposal){
		final Set<ProposalOption> proOptionSet = proposal.getProposalOptions();
		ProposalVersion proposalVersion = new ProposalVersion();
		ProposalOption proposalOption = new ProposalOption();
		for (ProposalOption proposalOption1 : proOptionSet) {
			proposalOption = proposalOption1;
		}
		final Set<ProposalVersion> proVersionSet = proposalOption.getProposalVersions();
		for (ProposalVersion proposalVersion2 : proVersionSet) {
			proposalVersion = proposalVersion2;
		}
		proposalDb.setName(proposal.getName());
		proposalDb.setCampaignName(proposal.getCampaignName());
		proposalDb.setReservationEmails(proposal.getReservationEmails());
		proposalDb.setCampaignObjectiveSet(proposal.getCampaignObjectiveSet());
		proposalDb.setAccountManager(proposal.getAccountManager());

		proposalDb.setSosOrderId(proposal.getSosOrderId());
		proposalDb.setCriticality(proposal.getCriticality());
		proposalDb.setSosSalesCategoryId(proposal.getSosSalesCategoryId());
		if (proposalDb.getAssignedUser().getUserId() != (SecurityUtil.getUser()).getUserId()) {
			proposalDb.setAssignedByUser(SecurityUtil.getUser());
		}
		//proposalDb.setAssignedUser(proposal.getAssignedUser());
		proposalDb.setSosAdvertiserId(proposal.getSosAdvertiserId());
		proposalDb.setSosAgencyId(proposal.getSosAgencyId());
		proposalDb.setDateRequested(proposal.getDateRequested());
		proposalDb.setDueDate(proposal.getDueDate());
		proposalDb.setStartDate(proposal.getStartDate());
		proposalDb.setEndDate(proposal.getEndDate());
		/* While updating a proposal, the price type field remains disabled ,so will not get updated in this scenario.*/
		//proposalDb.setPriceType(proposal.getPriceType());
		proposalDb.setSosAdvertiserName(proposal.getSosAdvertiserName());
		final Set<ProposalOption> proOptionSetDB = proposalDb.getProposalOptions();
		Set<ProposalVersion> proVersionSetDB = new LinkedHashSet<ProposalVersion>();
		for (ProposalOption proposalOption1 : proOptionSetDB) {
			if(proposalOption1.getId() == proposalOption.getId()){
				proposalOption1.setName(proposalOption.getName());
				proVersionSetDB = proposalOption1.getProposalVersions();
				break;
			}
		}
		for (ProposalVersion proposalVersionDB : proVersionSetDB) {
			if (proposalVersionDB.getProposalVersion() == proposalVersion.getProposalVersion()) {
				proposalVersionDB.setStartDate(proposalVersion.getStartDate());
				proposalVersionDB.setImpressions(proposalVersion.getImpressions());
				proposalVersionDB.setEffectiveCpm(proposalVersion.getEffectiveCpm());
				break;
			}
		}
		proposalDb.setCurrencyId(proposal.getCurrencyId());
		proposalDb.setConversionRate(proposal.getConversionRate());
		proposalDb.setConversionRate_Euro(proposal.getConversionRate_Euro());
		proposalDb.setConversionRate_Yen(proposal.getConversionRate_Yen());
		proposalDb.setCurrency(proposal.getCurrency());
		proposalDb.setSpecialAdvertiser(proposal.isSpecialAdvertiser());
		proposalDb.setPriceType(proposal.getPriceType());
		proposalDb.setWithPricing(proposal.isWithPricing());
		proposalDb.setPricingSubmittedDate((proposal.getPricingSubmittedDate() == null) ? proposalDb.getPricingSubmittedDate() : proposal.getPricingSubmittedDate());
	}	
	
	/*
	 * Method used for Checking the Pricing Exceptions while updating or adding
	 * a line Item to Proposal
	 */
	public Set<LineItemExceptions> getLineItemExceptions(final LineItem proposalLineItem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calculating Line Item Exceptions for lineItem with id: " + proposalLineItem.getLineItemID());
		}
		LOGGER.info("Calculating Line Item Exceptions for lineItem with id: " + proposalLineItem.getLineItemID());
		boolean flagPricing = false;
		boolean flagNoPricing = false;
		boolean flagRichMedia = false;
		boolean flagInvestment = false;
		final Set<LineItemExceptions> exceptionSet = new LinkedHashSet<LineItemExceptions>();
		if (proposalLineItem.getLineItemExceptions() != null && !proposalLineItem.getLineItemExceptions().isEmpty()) {
			final Set<LineItemExceptions> exceptionSetDB = proposalLineItem.getLineItemExceptions();
			exceptionSet.addAll(exceptionSetDB);
			for (LineItemExceptions lineItemExceptions : exceptionSetDB) {
				if (StringUtils.equals(lineItemExceptions.getLineItemException(), LineItemExceptionEnum.PRICING.name()) && !flagPricing) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Checking Line Items Pricing Exception for lineitem id: " + proposalLineItem.getLineItemID());
					}
					LOGGER.info("Checking Line Items Pricing Exception for lineitem id: " + proposalLineItem.getLineItemID());
					flagPricing = true;
					if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType())  ) {
						if ((proposalLineItem.getRateCardPrice() != null && proposalLineItem.getRate() != null)
								&& (proposalLineItem.getRateCardPrice() <= proposalLineItem.getRate())) {
							exceptionSet.remove(lineItemExceptions);
							proposalDao.deleteLineItemException(lineItemExceptions);
						}
					} else {
						exceptionSet.remove(lineItemExceptions);
						proposalDao.deleteLineItemException(lineItemExceptions);
					}
				}
				if (StringUtils.equals(lineItemExceptions.getLineItemException(), LineItemExceptionEnum.NOPRICING.name()) && !flagNoPricing) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Checking Line Items No Pricing Exception");
					}
					LOGGER.info("Checking Line Items No Pricing Exception");
					flagNoPricing = true;
					if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(proposalLineItem.getPriceType()) ) {
						if (proposalLineItem.getRateCardPrice() != null && proposalLineItem.getRateCardPrice() > 0) {
							exceptionSet.remove(lineItemExceptions);
							proposalDao.deleteLineItemException(lineItemExceptions);
						}
					} else {
						exceptionSet.remove(lineItemExceptions);
						proposalDao.deleteLineItemException(lineItemExceptions);
					}
				}
				if (StringUtils.equals(lineItemExceptions.getLineItemException(), LineItemExceptionEnum.RICHMEDIAWITHLESSCPM.name())) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Checking Line Items Rich Media Exception with line item id: " + proposalLineItem.getLineItemID());
					}
					LOGGER.info("Checking Line Items Rich Media Exception with line item id: " + proposalLineItem.getLineItemID());

					if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(proposalLineItem.getPriceType()) ) {
						flagRichMedia = true;
						if (isRichMediaAvailable(proposalLineItem.getSpecType())) {
							if ((proposalLineItem.getRate() != null) && ((proposalLineItem.getRate() >= 18))) {
								exceptionSet.remove(lineItemExceptions);
								proposalDao.deleteLineItemException(lineItemExceptions);
							}
						} else {
							exceptionSet.remove(lineItemExceptions);
							proposalDao.deleteLineItemException(lineItemExceptions);
						}

					} else {
						exceptionSet.remove(lineItemExceptions);
						proposalDao.deleteLineItemException(lineItemExceptions);
					}
				}
				if(StringUtils.equals(lineItemExceptions.getLineItemException(), LineItemExceptionEnum.TOTALINVESTMENT.name())){
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Checking Line Items for the investment: " + proposalLineItem.getLineItemID());
					}
					LOGGER.info("Checking Line Items  for the investment: " + proposalLineItem.getLineItemID());
					if (!ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(proposalLineItem.getPriceType())) {
						flagInvestment = true;
						Double totalInvestment = 0D;
						Proposal proposal = proposalLineItem.getProposalVersion().getProposalOption().getProposal();
						if (PriceType.Gross.name().equals(proposal.getPriceType())) {
							totalInvestment = proposalLineItem.getTotalInvestment() * ((100 - proposal.getAgencyMargin()) / 100);
						} else {
							totalInvestment = proposalLineItem.getTotalInvestment();
						}
						if (totalInvestment >= 1000) {
							exceptionSet.remove(lineItemExceptions);
							proposalDao.deleteLineItemException(lineItemExceptions);
						}
					} else {
						exceptionSet.remove(lineItemExceptions);
						proposalDao.deleteLineItemException(lineItemExceptions);
					}
				}
				
				if(StringUtils.equals(lineItemExceptions.getLineItemException(), LineItemExceptionEnum.NOSOR.name())) {
					if(proposalLineItem.getSor() != null && proposalLineItem.getSor() > 0) {
						exceptionSet.remove(lineItemExceptions);
						proposalDao.deleteLineItemException(lineItemExceptions);
					}
				}
			}
		}
		if (!flagPricing) {
			if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType()) ) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Calculating Line Items Pricing Exception in getLineItemExceptions()");
				}
				LOGGER.info("Calculating Line Items Pricing Exception in getLineItemExceptions()");
				if ((proposalLineItem.getRateCardPrice() != null && proposalLineItem.getRate() != null) && (proposalLineItem.getRateCardPrice() > proposalLineItem.getRate())) {
					final LineItemExceptions lineItemException = new LineItemExceptions();
					lineItemException.setLineitemId(proposalLineItem);
					lineItemException.setLineItemException(LineItemExceptionEnum.PRICING.name());
					exceptionSet.add(lineItemException);
				}
			}
		}
		if (!flagNoPricing) {
			if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(proposalLineItem.getPriceType()) ) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Calculating Line Items No Pricing Exception in getLineItemExceptions()");
				}
				LOGGER.info("Calculating Line Items No Pricing Exception in getLineItemExceptions()");
				if (proposalLineItem.getRateCardPrice() != null && proposalLineItem.getRateCardPrice() == 0) {
					final LineItemExceptions lineItemException = new LineItemExceptions();
					lineItemException.setLineitemId(proposalLineItem);
					lineItemException.setLineItemException(LineItemExceptionEnum.NOPRICING.name());
					exceptionSet.add(lineItemException);
				}
			}
		}

		//Adding exception in case of Rich Media and valid condition
		if (!flagRichMedia) {
		if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(proposalLineItem.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(proposalLineItem.getPriceType()) ) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Calculating Line Items RichMedia Exception in getLineItemExceptions()");
			}
			LOGGER.info("Calculating Line Items RichMedia Exception in getLineItemExceptions()");
			if ((proposalLineItem.getRate() != null) && ((proposalLineItem.getRate() < 18) && isRichMediaAvailable(proposalLineItem.getSpecType()))) {
				final LineItemExceptions lineItemException = new LineItemExceptions();
				lineItemException.setLineitemId(proposalLineItem);
				lineItemException.setLineItemException(LineItemExceptionEnum.RICHMEDIAWITHLESSCPM.name());
				exceptionSet.add(lineItemException);
			}
		}
		}
		if (!flagInvestment) {
			if (!ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(proposalLineItem.getPriceType())){
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Calculating Line Items for the investment");
				}
				LOGGER.info("Calculating Line Items for the investment");
				Double totalInvestment = 0D;
				Proposal proposal = proposalLineItem.getProposalVersion().getProposalOption().getProposal();
				if( PriceType.Gross.name().equals(proposal.getPriceType())){
					totalInvestment = proposalLineItem.getTotalInvestment() * ((100 - proposal.getAgencyMargin()) / 100);
				}else{
					totalInvestment = proposalLineItem.getTotalInvestment();
				}
				if (totalInvestment < 1000) {
					final LineItemExceptions lineItemException = new LineItemExceptions();
					lineItemException.setLineitemId(proposalLineItem);
					lineItemException.setLineItemException(LineItemExceptionEnum.TOTALINVESTMENT.name());
					exceptionSet.add(lineItemException);
				}
			}
			}		
		return exceptionSet;
	}
	
	/**
	 * Check "RICH_MEDIA" is available in string or not
	 * @param specType
	 * @return
	 */
	public boolean isRichMediaAvailable(final String specType) {
		if (specType != null && specType != ConstantStrings.EMPTY_STRING) {
			String[] specTypeArray = specType.split(ConstantStrings.COMMA);
			for (String specTypeVal : specTypeArray) {
				if (specTypeVal.equalsIgnoreCase(ConstantStrings.RICH_MEDIA)) {
					return true;
				}
			}
		}
		return false;
	}
	
		
	/*
	 * Method is used for creating a new Line Item from a Existing One
	 */
	public LineItem createNewLineItemFromExisting(final LineItem lineItem, final Long proposalID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generic Code which create New Line Item from Existing  LineItems in createNewLineItemFromExisting()");
		}
		Set<LineItemTarget> targetSetNew = new LinkedHashSet<LineItemTarget>();
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsNewList = new ArrayList<LineItemSalesTargetAssoc>();

		LineItem returnLineItem = new LineItem();
		returnLineItem.setComments(lineItem.getComments());
		returnLineItem.setFlight(lineItem.getFlight());
		returnLineItem.setImpressionTotal(lineItem.getImpressionTotal());
		returnLineItem.setPriceType(lineItem.getPriceType());
		returnLineItem.setRate(lineItem.getRate());
		returnLineItem.setSosProductId(lineItem.getSosProductId());
		returnLineItem.setProductName(lineItem.getProductName());
		returnLineItem.setTargetingString(lineItem.getTargetingString());
		returnLineItem.setTotalInvestment(lineItem.getTotalInvestment());
		returnLineItem.setLineItemSalesTargetAssocs(lineItem.getLineItemSalesTargetAssocs());
		returnLineItem.setSosProductClass(lineItem.getSosProductClass());
		returnLineItem.setProposalId(proposalID);
		returnLineItem.setPackageObj(lineItem.getPackageObj());
		returnLineItem.setAvails(lineItem.getAvails());
		returnLineItem.setAvailsPopulatedDate(lineItem.getAvailsPopulatedDate());
		returnLineItem.setPlacementName(lineItem.getPlacementName());
		returnLineItem.setSov(lineItem.getSov());
		//returnLineItem.setSegmentCode(lineItem.getSegmentCode());
		returnLineItem.setTotalPossibleImpressions(lineItem.getTotalPossibleImpressions());
		returnLineItem.setSpecType(lineItem.getSpecType());
		returnLineItem.setPartiallyCopiedUnbreakPackage(lineItem.isPartiallyCopiedUnbreakPackage());
		returnLineItem.setStartDate(lineItem.getStartDate());
		returnLineItem.setEndDate(lineItem.getEndDate());
		returnLineItem.setOrderNumber(System.currentTimeMillis());
		returnLineItem.setLineItemSequence(lineItem.getLineItemSequence());
		returnLineItem.setPriceCalSummary(lineItem.getPriceCalSummary());
		returnLineItem.setSor(lineItem.getSor());
		returnLineItem.setProductType(lineItem.getProductType());
		Set<LineItemTarget> targetSet = lineItem.getGeoTargetSet();
		if (targetSet != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Copying Targets of LineItems in createNewLineItemFromExisting()");
			}
			for (LineItemTarget lineItemTarget : targetSet) {
				LineItemTarget lineItemTargetNew = new LineItemTarget();
				lineItemTargetNew.setActive(lineItemTarget.isActive());
				lineItemTargetNew.setProposalLineItem(returnLineItem);
				lineItemTargetNew.setSosTarTypeElementId(lineItemTarget.getSosTarTypeElementId());
				lineItemTargetNew.setSosTarTypeId(lineItemTarget.getSosTarTypeId());
				lineItemTargetNew.setOperation(lineItemTarget.getOperation());
				lineItemTargetNew.setNegation(lineItemTarget.getNegation());
				lineItemTargetNew.setSegmentLevel(lineItemTarget.getSegmentLevel());
				targetSetNew.add(lineItemTargetNew);
			}
			returnLineItem.setGeoTargetSet(targetSetNew);
		}

		// Creating new line item sales target association list
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = lineItem.getLineItemSalesTargetAssocs();
		if (lineItemSalesTargetAssocsList != null) {
			for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSalesTargetAssocsList) {
				LineItemSalesTargetAssoc lineItemSalesTargetAssocNew = new LineItemSalesTargetAssoc();
				lineItemSalesTargetAssocNew.setSosSalesTargetId(lineItemSalesTargetAssoc.getSosSalesTargetId());
				lineItemSalesTargetAssocNew.setSosSalesTargetName(lineItemSalesTargetAssoc.getSosSalesTargetName());
				lineItemSalesTargetAssocNew.setProposalLineItem(returnLineItem);
				lineItemSalesTargetAssocsNewList.add(lineItemSalesTargetAssocNew);
			}
			returnLineItem.setLineItemSalesTargetAssocs(lineItemSalesTargetAssocsNewList);
		}

		if (LineItemPriceTypeEnum.FLATRATE.getOptionValue().equals(lineItem.getPriceType())) {
			returnLineItem.setRateCardPrice(0D);
		} else {
			returnLineItem.setRateCardPrice(lineItem.getRateCardPrice());
		}
		returnLineItem.setViewabilityLevel(lineItem.getViewabilityLevel());
		return returnLineItem;
	}
	
	/**
	 * Creating New proposal Line Item Assoc Set From Existing
	 * @param proposalLineItems
	 * @param propVersion
	 * @return
	 * @throws CustomCheckedException 
	 */
	public Set<LineItem> createNewLineItemAssocSet(final List<LineItem> proposalLineItems, final ProposalVersion propVersion, boolean isMoveReservation) {
		Set<LineItem> proposalLineItemSet = new LinkedHashSet<LineItem>(proposalLineItems.size());
		for (LineItem lineItemOld : proposalLineItems) {
			LineItem lineItem = createNewLineItemFromExisting(lineItemOld, (propVersion.getProposalOption().getProposal().getId()));
			lineItem.setPricingStatus(lineItemOld.getPricingStatus());
			lineItem.setLineItemExceptions(getLineItemExceptionsWhileCopyingLineItem("Proposal", false, false, lineItemOld, lineItem));
			/**
			 * Following code is executed when Line Item copied during Create Version for moving the reservation
			 */
			if(isMoveReservation){				
				moveReservation(lineItemOld, lineItem);
			}
			lineItem.setProposalVersion(propVersion);
			proposalLineItemSet.add(lineItem);
		}
		return proposalLineItemSet;
	}
	
	/**
	 * Creating New proposal Line Item Assoc Set From Existing
	 * @param proposalLineItems
	 * @param propVersion
	 * @return
	 */
	public Set<LineItem> createNewLineItemAssocSetForVersionClone(final List<LineItem> proposalLineItems, final ProposalVersion propVersion, boolean isMoveReservation){
		Set<LineItem> proposalLineItemSet = new LinkedHashSet<LineItem>(proposalLineItems.size());
		for (LineItem lineItemOld : proposalLineItems) {
			LineItem lineItem = createNewLineItemFromExisting(lineItemOld, (propVersion.getProposalOption().getProposal().getId()));
			lineItem.setPricingStatus(lineItemOld.getPricingStatus());
			boolean isCopiedFromExpiredPkg = false;
			String type = "Proposal";
			Set<LineItemExceptions> lineItemExceptions = lineItemOld.getLineItemExceptions();
			for (LineItemExceptions lineItemExptn : lineItemExceptions) {
				if(LineItemExceptionEnum.EXPIREDPACKAGE.name().equalsIgnoreCase(lineItemExptn.getLineItemException())){
					isCopiedFromExpiredPkg = true;
					type = "package";
					break;
				}
			}
			lineItem.setLineItemExceptions(getLineItemExceptionsWhileCopyingLineItem(type, lineItem.isPartiallyCopiedUnbreakPackage(), isCopiedFromExpiredPkg, lineItemOld, lineItem));
			/**
			 * Following code is executed when Line Item copied during Create Version for moving the reservation
			 */
			if(isMoveReservation){
				moveReservation(lineItemOld, lineItem);
			}
			lineItem.setProposalVersion(propVersion);
			proposalLineItemSet.add(lineItem);
		}
		return proposalLineItemSet;
	}
	
	private void moveReservation(LineItem lineItemOld , LineItem lineItem){
		if(lineItemOld.getReservation() != null){					
			LineItemReservations reservation = lineItemOld.getReservation();					
			LineItemReservations reservationNew = new LineItemReservations();
			reservationNew.setStatus(reservation.getStatus());
			reservationNew.setExpirationDate(reservation.getExpirationDate());
			reservationNew.setProposalLineItem(lineItem);
			reservationNew.setCreatedDate(reservation.getCreatedDate());
			reservationNew.setCreatedBy(reservation.getCreatedBy());
			reservationNew.setLastRenewedOn(reservation.getLastRenewedOn());
			lineItem.setReservation(reservationNew);
			lineItemOld.setReservation(null);
			proposalDao.deleteReservation(reservation);
		}
	}
	
	public Set<LineItemExceptions> getLineItemExceptionsWhileCopyingLineItem(final String type, final boolean partiallyCopiedUnbreakPackage, final boolean isCopiedFromExpired, final LineItem lineItem, final LineItem lineItemNew) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Copying LineItems and Adding to proposal in addCopiedLineItemsToProposal()");
		}
		Set<LineItemExceptions> lineItemsExceptionSet = new LinkedHashSet<LineItemExceptions>();
		if (partiallyCopiedUnbreakPackage) {
			LineItemExceptions lineItemException = new LineItemExceptions();
			lineItemException.setLineitemId(lineItemNew);
			lineItemException.setLineItemException(LineItemExceptionEnum.UNBREAKABLEPACKAGE.name());
			lineItemsExceptionSet.add(lineItemException);
		}
		if (isCopiedFromExpired) {
			LineItemExceptions lineItemException = new LineItemExceptions();
			lineItemException.setLineitemId(lineItemNew);
			if ("package".equals(type)) {
				lineItemException.setLineItemException(LineItemExceptionEnum.EXPIREDPACKAGE.name());
			} else {
				lineItemException.setLineItemException(LineItemExceptionEnum.EXPIREDPROPOSAL.name());
			}

			lineItemsExceptionSet.add(lineItemException);
		}		
		if ((ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItemNew.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItemNew.getPriceType())) && (lineItemNew.getRate() != null) && (lineItemNew.getRateCardPrice() > lineItemNew.getRate())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Setting Pricing Exception for  LineItems in addCopiedLineItemsToProposal()");
			}
			LineItemExceptions lineItemException = new LineItemExceptions();
			lineItemException.setLineitemId(lineItemNew);
			lineItemException.setLineItemException(LineItemExceptionEnum.PRICING.name());
			lineItemsExceptionSet.add(lineItemException);
		}
		if ((ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItemNew.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItemNew.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(lineItemNew.getPriceType()) ) && (lineItemNew.getRateCardPrice() == 0)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Setting No Pricing Exception for  LineItems in addCopiedLineItemsToProposal()");
			}
			LineItemExceptions lineItemException = new LineItemExceptions();
			lineItemException.setLineitemId(lineItemNew);
			lineItemException.setLineItemException(LineItemExceptionEnum.NOPRICING.name());
			lineItemsExceptionSet.add(lineItemException);
		}
		if ((ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equals(lineItemNew.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(lineItemNew.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(lineItemNew.getPriceType()) ) && (lineItemNew.getRate() < 18) && isRichMediaAvailable(lineItemNew.getSpecType())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Setting RichMedia Exception for  LineItems in addCopiedLineItemsToProposal()");
			}
			LineItemExceptions lineItemException = new LineItemExceptions();
			lineItemException.setLineitemId(lineItemNew);
			lineItemException.setLineItemException(LineItemExceptionEnum.RICHMEDIAWITHLESSCPM.name());
			lineItemsExceptionSet.add(lineItemException);
		}
		if ((!ConstantStrings.LINE_ITEM_PRICE_TYPE_ADDED_VALUE.equals(lineItemNew.getPriceType()))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Setting Total investment Exception for  LineItems in addCopiedLineItemsToProposal()");
			}
			Double totalInvestment = 0D;
			if (!"package".equals(type)) {
			Proposal proposal = lineItem.getProposalVersion().getProposalOption().getProposal();
			if( PriceType.Gross.name().equals(proposal.getPriceType())){
				totalInvestment = lineItemNew.getTotalInvestment() * ((100 - proposal.getAgencyMargin()) / 100);
			}else{
				totalInvestment = lineItemNew.getTotalInvestment();
			}
			}else{
				totalInvestment = lineItemNew.getTotalInvestment();
			}
			if (totalInvestment < 1000) {
				LineItemExceptions lineItemException = new LineItemExceptions();
				lineItemException.setLineitemId(lineItemNew);
				lineItemException.setLineItemException(LineItemExceptionEnum.TOTALINVESTMENT.name());
				lineItemsExceptionSet.add(lineItemException);
			}
			if (lineItem.getSor() == null && LineItemProductTypeEnum.RESERVABLE.name().equals(lineItem.getProductType().name())) {
				LineItemExceptions lineItemException = new LineItemExceptions();
				lineItemException.setLineitemId(lineItemNew);
				lineItemException.setLineItemException(LineItemExceptionEnum.NOSOR.name());
				lineItemsExceptionSet.add(lineItemException);
			}
		}
		return lineItemsExceptionSet;
	}

	/**
	 * Updates reservation of Line Item
	 * @param proLineItemDB
	 * @param bean
	 * @throws CustomCheckedException 
	 */
	public void updateReservation(LineItem proLineItemDB, LineItem bean) throws CustomCheckedException {
		if(proLineItemDB.getReservation() != null){
			if( bean.getReservation() != null){
				if(! DateUtils.isSameDay(bean.getReservation().getExpirationDate(),proLineItemDB.getReservation().getExpirationDate())){
					proLineItemDB.getReservation().setExpirationDate(bean.getReservation().getExpirationDate());
					proLineItemDB.getReservation().setStatus(ReservationStatus.RE_NEW);
					proLineItemDB.getReservation().setLastRenewedOn(DateUtil.getCurrentDate());
				}
				
			}else{
				proposalDao.deleteReservation(proLineItemDB.getReservation());
				proLineItemDB.setReservation(null);
				/*if(proLineItemDB.getSosProductClass()==Long.parseLong(ConstantStrings.HOME_PAGE_PRODUCT_ID)){
					auditService.createAuditMessageForHomePageResrvtn(proLineItemDB,ConstantStrings.DELETED);
					//sendMailForHomeResrvtn(proLineItemDB, proLineItemDB.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED);
				}*/
			}
		}else {
			if( bean.getReservation() != null){
				proLineItemDB.setReservation(bean.getReservation());
			}
		}
		
	}

	/**
	 * send Email when reservation is deleted
	 * @param lineItemLst
	 * @param salescategoryMap
	 * @return
	 */
	public Map<String, String> sendMailForDeleteReservation(List<LineItem> lineItemLst, Map<Long, String> salescategoryMap) {
		boolean flag = false;
		boolean emailFlag = false;
		boolean emailPOW = false;
		if(lineItemLst != null && !lineItemLst.isEmpty()){
			List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
			for (LineItem lineItem : lineItemLst) {
				if(lineItem.getReservation() != null){
					flag = true;
				}
				if (LineItemProductTypeEnum.EMAIL.name().equals(lineItem.getProductType().name())){
					emailFlag = true;
				}
				if(lineItem.getReservation() != null &&  productClassIdLst.contains(lineItem.getSosProductClass())){
					emailPOW = true;
				}
				if(flag && emailFlag){
					break;
				}
			}
			if(flag){
				final List<String> roles = new ArrayList<String>(3);
				roles.add(SecurityUtil.ADMIN);
				if(emailFlag){
					roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
				}
				if(emailPOW){
					roles.add(SecurityUtil.PRODUCT_OWNER);
				}
				final Proposal proposal = proposalDao.getProposalbyId(lineItemLst.get(0).getProposalId());
				return createMail(proposal, createSubject(ReservationStatus.DELETED, proposal, salescategoryMap), creatTextMsg(
						proposal, lineItemLst), roles);
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * send Email when reservation is deleted
	 * @param lineItemLst
	 * @param salescategoryMap
	 * @return
	 */
	public Map<String, String> sendMailForDeletedReservationStatus(LineItem lineItem, Map<Long, String> salescategoryMap) {
		final List<String> roles = new ArrayList<String>(3);
		roles.add(SecurityUtil.ADMIN);
		if (LineItemProductTypeEnum.EMAIL.name().equals(lineItem.getProductType().name())){
			roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
		}
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if((lineItem != null && lineItem.getReservation() != null &&  productClassIdLst.contains(lineItem.getSosProductClass()))){
			roles.add(SecurityUtil.PRODUCT_OWNER);
		}
		final Proposal proposal = proposalDao.getProposalbyId(lineItem.getProposalId());
		return createMail(proposal, createSubject(ReservationStatus.DELETED, proposal, salescategoryMap), creatTextMsgForReservation(
				proposal, lineItem, ReservationStatus.DELETED), roles);
	}
	/**
	 * create subject for a Email
	 * 
	 * @param status
	 * @param proposal
	 * @param salescategoryMap
	 * @return
	 */
	private String createSubject(final ReservationStatus status, final Proposal proposal, final Map<Long, String> salescategoryMap) {
		final StringBuffer subject = new StringBuffer(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
		subject.append("(S)");
		subject.append(ConstantStrings.SPACE);
		subject.append(status.getDisplayName());
		subject.append(ConstantStrings.COLON);
		subject.append(ConstantStrings.SPACE);
		subject.append(salescategoryMap.get(proposal.getSosSalesCategoryId()));
		subject.append(ConstantStrings.COLON);
		subject.append(ConstantStrings.SPACE);
		subject.append(proposal.getName());
		return subject.toString();
	}
	
	/**
	 * Create Email Body
	 * 
	 * @param proposal
	 * @param lineItemLst
	 * @return
	 */
	private String creatTextMsg(Proposal proposal, List<LineItem> lineItemLst) {
		final StringBuffer textMsg = new StringBuffer();
		for (LineItem lineItem : lineItemLst) {
			if(lineItem.getReservation() != null){
				textMsg.append("LineItemID: " + lineItem.getLineItemID());
				textMsg.append("\t\n" + "Ad Unit: " + lineItem.getProductName());
				textMsg.append("\t\n" + "Sales Target: " + lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
				textMsg.append("\t\n" + "Target: "
						+ ((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING));
				textMsg.append("\t\n" + "Flight: " + DateUtil.getGuiDateString(lineItem.getStartDate()) + " - "
						+ DateUtil.getGuiDateString(lineItem.getEndDate()));
				textMsg.append("\t\n"
						+ "Reservation Created On: "
						+ ((lineItem.getReservation() != null) ? DateUtil.getGuiDateString(lineItem.getReservation().getCreatedDate())
								: ConstantStrings.EMPTY_STRING));
				textMsg.append("\t\n" + "Account Manager: "
						+ (StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
				textMsg.append("\t\n\t\n");
			}
		}
		final String url = applicationURL + ConstantStrings.PROPOSAL_URL + proposal.getId();
		textMsg.append(url);
		return textMsg.toString();

	}
	
	/**
	 * Create text message for sending
	 * @param proposal
	 * @param request
	 * @param lineItem
	 * @param deleted 
	 * @return
	 */
	private String creatTextMsgForReservation(final Proposal proposal, final LineItem lineItem, final ReservationStatus status) {
		final StringBuffer textMsg = new StringBuffer();
		textMsg.append("LineItemID: "+ lineItem.getLineItemID());
		textMsg.append("\t\n" + "Ad Unit: " + StringUtils.trimToEmpty(lineItem.getProductName()));
		textMsg.append("\t\n" + "Sales Target: " + lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
		textMsg.append("\t\n" + "Target: " + ((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING));
		textMsg.append("\t\n" + "Flight: " + DateUtil.getGuiDateString(lineItem.getStartDate()) + " - " + DateUtil.getGuiDateString(lineItem.getEndDate()));
		textMsg.append("\t\n"	+ "Reservation Created On: " + ((lineItem.getReservation() != null) ? DateUtil.getGuiDateString(lineItem.getReservation().getCreatedDate())	: ConstantStrings.EMPTY_STRING));
		textMsg.append("\t\n" + "SOR: " + ((lineItem.getSor() != null) ? lineItem.getSor() : ConstantStrings.NA));
		textMsg.append("\t\n" + "Account Manager: " + (StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
		String url = applicationURL + ConstantStrings.PROPOSAL_URL + proposal.getId();
		textMsg.append("\t\n\t\n" +  url);
		return textMsg.toString();
	}
	
	/**
	 * Create a Email
	 * 
	 * @param proposal
	 * @param subject
	 * @param textMsg
	 * @param roles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> createMail(final Proposal proposal, final String subject, final String textMsg, final List<String> roles) {
		final User user = proposal.getAssignedUser();
		final String mailTo = (user == null) ? SecurityUtil.getUser().getEmail() : user.getEmail();
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[roles.size()]));
		if (userLst != null && !userLst.isEmpty()) {
			for (User userDB : userLst) {
				if(mail.length() > 0){
					mail.append(ConstantStrings.COMMA);
				}
				mail.append(userDB.getEmail());
			}
			if(StringUtils.isNotBlank(proposal.getReservationEmails())){
					mail.append(ConstantStrings.COMMA);
					mail.append(proposal.getReservationEmails());
			}
			return getMailInfoForReservation(mail.toString(), subject, mailTo, textMsg);			
		}
		return Collections.EMPTY_MAP;
	}

	/**
	 * get Mail content
	 * 
	 * @param mailcc
	 * @param subject
	 * @param mailTo
	 * @param textMsg
	 * @return
	 */
	private Map<String, String> getMailInfoForReservation(final String mailcc, final String subject, final String mailTo, final String textMsg) {
		final Map<String, String> mailProps = new HashMap<String, String>();
		mailProps.put("mailTo", mailTo);
		mailProps.put("subject", subject);
		mailProps.put("mailFrom", SecurityUtil.getUser().getEmail());
		mailProps.put("textMsg", textMsg);
		mailProps.put("cc", mailcc.trim());
		return mailProps;
	}
	
	/**
	 * sends a Mail
	 * @param mailMap
	 */
	public void sendMail(final Map<String, String> mailMap){
		if(mailMap != null && !mailMap.isEmpty()){
			mailUtil.sendMail(mailUtil.setMessageInfo(mailMap));
		}
	}

	/**
	 * Send mail when reservations moved from one proposal to another
	 * @param srcLineItem
	 * @param tarlineItem
	 * @return
	 */
	public Map<String, String> sendMailWhenMoveReservationFrmProposalToAnother(final LineItem srcLineItem, final LineItem tarlineItem) {
		final List<String> roles = new ArrayList<String>(3);
		roles.add(SecurityUtil.ADMIN);
		if (LineItemProductTypeEnum.EMAIL.name().equals(srcLineItem.getProductType().name())){
			roles.add(SecurityUtil.EMAIL_PRODUCT_MANAGER);
		}
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if((srcLineItem != null && srcLineItem.getReservation() != null &&  productClassIdLst.contains(srcLineItem.getSosProductClass())) || ((tarlineItem != null && tarlineItem.getReservation() != null &&  productClassIdLst.contains(tarlineItem.getSosProductClass())))){
			roles.add(SecurityUtil.PRODUCT_OWNER);
		}
		final String mailcc = emailIdOfUserBasedOnRole(roles);
		final String subject = createSubjectForMoveReservation(srcLineItem, tarlineItem);
		final String mailFrom = SecurityUtil.getUser().getEmail();
		final String textMsg = textMsgForMoveReservationFrmProposalToAnother(srcLineItem, tarlineItem).toString();
		return getMailInfoForReservation(mailcc, subject, mailFrom, textMsg);
	}

	/**
	 * Create text msg when reservations moved from one proposal to another
	 * @param srcLineItem
	 * @param tarlineItem
	 * @return
	 */
	private StringBuffer textMsgForMoveReservationFrmProposalToAnother(final LineItem srcLineItem, final LineItem tarlineItem) {
		final StringBuffer textMsg = new StringBuffer();
		final Proposal tarProposal = tarlineItem.getProposalVersion().getProposalOption().getProposal();
		textMsg.append("Source Proposal Line Item :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(srcLineItem.getLineItemID());
		textMsg.append("\t\nDestination Proposal Line Item :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(tarlineItem.getLineItemID());
		textMsg.append("\t\nAd Unit :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(tarlineItem.getProductName());
		textMsg.append("\t\nSales Target :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(tarlineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
		textMsg.append("\t\nTarget :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append((StringUtils.isNotBlank(tarlineItem.getTargetingString())) ? tarlineItem.getTargetingString() : ConstantStrings.EMPTY_STRING);
		textMsg.append("\t\nFlight :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(DateUtil.getGuiDateString(tarlineItem.getStartDate()) + " - " + DateUtil.getGuiDateString(tarlineItem.getEndDate()));
		textMsg.append("\t\nExpiry Date :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(DateUtil.getGuiDateString(tarlineItem.getReservation().getExpirationDate()));
		textMsg.append("\t\nAccount Manager :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(StringUtils.isNotBlank(tarProposal.getAccountManager()) ? tarProposal.getAccountManager() : ConstantStrings.EMPTY_STRING);
		String url = applicationURL + ConstantStrings.PROPOSAL_URL;
		textMsg.append("\t\n\t\nLink to Source Proposal :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(url);
		Proposal srcProposal = srcLineItem.getProposalVersion().getProposalOption().getProposal();
		textMsg.append(srcProposal.getId());
		textMsg.append("\t\n\t\nLink to Destination Proposal :");
		textMsg.append(ConstantStrings.SPACE);
		textMsg.append(url);
		textMsg.append(tarProposal.getId());
		return textMsg;
	}

	/**
	 * Create subject when reservations moved from one proposal to another
	 * @param srcLineItem 
	 * @param tarlineItem
	 * @return
	 */
	private String createSubjectForMoveReservation(final LineItem srcLineItem, final LineItem tarlineItem) {
		final StringBuffer subject = new StringBuffer(ConstantStrings.RESERVATION_EMAIL_KEYWORD);
		subject.append(ConstantStrings.SPACE);
		subject.append("MOVED: FROM");
		subject.append(ConstantStrings.SPACE);
		final Proposal srcProposal = srcLineItem.getProposalVersion().getProposalOption().getProposal();
		subject.append(srcProposal.getName());
		subject.append(ConstantStrings.SPACE);
		subject.append("TO");
		subject.append(ConstantStrings.SPACE);
		final Proposal tarProposal = tarlineItem.getProposalVersion().getProposalOption().getProposal();
		subject.append(tarProposal.getName());
		return subject.toString();
	}

	/**
	 * Return E-mail id of user, based user role
	 * @param roles
	 * @return
	 */
	public String emailIdOfUserBasedOnRole(final List<String> roles) {
		final StringBuffer mail = new StringBuffer();
		final List<User> userLst = userService.getUserBasedOnRoleList(roles.toArray(new String[roles.size()]));
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
	 * 
	 * @param proposal
	 * @param lineItemLst
	 * @return
	 */
	private String creatTextMsgForHomePageResrvtnRequest(Proposal proposal, LineItem lineItem, String productClass) {
		final StringBuffer textMsg = new StringBuffer();
			textMsg.append(SecurityUtil.getUser().getFullName());
			textMsg.append(" is requesting a " + productClass +" reservation for ");
			textMsg.append(proposal.getName());
			textMsg.append(" with the following details:");
			textMsg.append("\t\n\t\n");
			textMsg.append("LineItemID: " + lineItem.getLineItemID());
			textMsg.append("\t\n" + "Ad Unit: " + lineItem.getProductName());
			textMsg.append("\t\n" + "Sales Target: " + lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
			textMsg.append("\t\n" + "Target: "
					+ ((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING));
			if(lineItem.getStartDate() != null && lineItem.getEndDate() !=null){
				textMsg.append("\t\n" + "Flight: " + DateUtil.getGuiDateString(lineItem.getStartDate()) + " - "
					+ DateUtil.getGuiDateString(lineItem.getEndDate()));
			}else{
				textMsg.append("\t\n" + "Flight: " + lineItem.getFlight());
			}
			textMsg.append("\t\n" + "SOR: " + ((lineItem.getSor() != null) ? lineItem.getSor() : ConstantStrings.NA));
			textMsg.append("\t\n" + "Account Manager: "
					+ (StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
			textMsg.append("\t\n\t\n");
		final String url = applicationURL + ConstantStrings.PROPOSAL_URL + proposal.getId();
		textMsg.append(url);
		return textMsg.toString();

	}
	
	/**
	 * 
	 * @param srcLineItem
	 * @param tarlineItem
	 * @return
	 */
	public Map<String, String> sendMailForHomePageResrvtnRequest(final LineItem lineItem, final Proposal proposal) {
		final List<String> roles = new ArrayList<String>(2);
		roles.add(SecurityUtil.ADMIN);
		roles.add(SecurityUtil.PRODUCT_OWNER);
		Map<Long, String> productClassMap = productService.getProductClass();
		String productClass = (productClassMap.get(lineItem.getSosProductClass()).equals("HOME PAGE") ? ConstantStrings.PRODUCT_CLASS_HOME_PAGE :  ConstantStrings.PRODUCT_CLASS_DISPLAY_CROSS_PLATFORM);
		final String mailFrom = emailIdOfUserBasedOnRole(roles);
		final String subject = "Request for "+ productClass + " Reservation";
		final String mailcc = SecurityUtil.getUser().getEmail();
		final String textMsg = creatTextMsgForHomePageResrvtnRequest(proposal, lineItem, productClass).toString();
		return getMailInfoForReservation(mailcc, subject, mailFrom, textMsg);
	}
	
	/**
	 * 
	 * @param proposal
	 * @param lineItem
	 * @return
	 */
	public String createTextMsgForHomePageResrvtnCreated(final Proposal proposal, final LineItem lineItem) {
		final StringBuffer textMsg = new StringBuffer();
		textMsg.append("LineItemID: "+ lineItem.getLineItemID());
		textMsg.append("\t\n" + "Ad Unit: " + StringUtils.trimToEmpty(lineItem.getProductName()));
		textMsg.append("\t\n" + "Sales Target: " + lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
		textMsg.append("\t\n" + "Target: " + ((StringUtils.isNotBlank(lineItem.getTargetingString())) ? lineItem.getTargetingString() : ConstantStrings.EMPTY_STRING));
		textMsg.append("\t\n" + "Flight: " + DateUtil.getGuiDateString(lineItem.getStartDate()) + " - " + DateUtil.getGuiDateString(lineItem.getEndDate()));
		textMsg.append("\t\n"	+ "Reservation Created On: " + ((lineItem.getReservation() != null) ? DateUtil.getGuiDateString(lineItem.getReservation().getCreatedDate())	: ConstantStrings.EMPTY_STRING));
		textMsg.append("\t\n" + "Expiry Date: " + DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate()));
		textMsg.append("\t\n" + "SOR: " + ((lineItem.getSor() != null) ? lineItem.getSor() : ConstantStrings.NA));
		textMsg.append("\t\n" + "Account Manager: " + (StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : ConstantStrings.EMPTY_STRING));
		String url = applicationURL + ConstantStrings.PROPOSAL_URL + proposal.getId();
		textMsg.append("\t\n\t\n" +  url);
		return textMsg.toString();
	}
	
	/**
	 * 
	 * @param mailTo
	 * @param subject
	 * @param mailTo
	 * @param textMsg
	 * @return
	 */
	public Map<String, String> getMailInfoForHomePageReservation(final String mailCc, final String subject, final String mailTo, final String textMsg) {
		return getMailInfoForReservation(mailCc, subject, mailTo, textMsg);
	}
	
	public void sendMailForHomeResrvtn(LineItem lineItem, Proposal proposal, String resrvtnOperation){
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
		final String textMsg = createTextMsgForHomePageResrvtnCreated(proposal, lineItem).toString();
		Map<String, String> mailMap = new HashMap<String, String>();
		
		Map<Long, String> productClassMap = productService.getProductClass();
		String productClass = (productClassMap.get(lineItem.getSosProductClass()).equals("HOME PAGE") ? ConstantStrings.PRODUCT_CLASS_HOME_PAGE :  ConstantStrings.PRODUCT_CLASS_DISPLAY_CROSS_PLATFORM);
		if(resrvtnOperation.equals(ConstantStrings.CREATED) && (proposalOwnerRole.equals(SecurityUtil.ADMIN) || proposalOwnerRole.equals(SecurityUtil.PRODUCT_OWNER))){
			if(!toUserSet.isEmpty()){
				final String subject = productClass + " Reservation Created" + ConstantStrings.COLON + ConstantStrings.SPACE + sosService.getSalesCategories().get(proposal.getSosSalesCategoryId()) +  
						ConstantStrings.COLON + ConstantStrings.SPACE+ proposal.getName();
				final String mailcc = SecurityUtil.getUser().getEmail();
						
				mailMap = getMailInfoForHomePageReservation(mailcc, subject, mailTo.toString(), textMsg);
			}
		}else if(resrvtnOperation.equals(ConstantStrings.DELETED)){
			final String subject = productClass + " Reservation Deleted" + ConstantStrings.COLON + ConstantStrings.SPACE + sosService.getSalesCategories().get(proposal.getSosSalesCategoryId()) +  
					ConstantStrings.COLON + ConstantStrings.SPACE+ proposal.getName();
			final String mailcc = SecurityUtil.getUser().getEmail();
			mailMap = getMailInfoForHomePageReservation(mailTo.toString(), subject, mailcc, textMsg);
		}
		sendMail(mailMap);
	}
	public void setProposalDao(IProposalDAO proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setApplicationURL(String applicationURL) {
		this.applicationURL = applicationURL;
	}
	
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}

	public void setProposalService(IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setSosService(ISOSService sosService) {
		this.sosService = sosService;
	}

	public void setReservationService(ICalendarReservationService reservationService) {
		this.reservationService = reservationService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}
}