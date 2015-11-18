/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IProposalDAO;
import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.service.IAddedValueService;
import com.nyt.mpt.service.IPricingStatusCalculatorService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemViewableCriteriaEnum;
import com.nyt.mpt.util.enums.PriceType;
import com.nyt.mpt.util.enums.PricingStatus;

/**
 * @author garima.garg
 *
 */
public class PricingStatusCalculatorService implements IPricingStatusCalculatorService{

	private String TOLERANCE_PERCENT;
	private String THRESHOLD_PERCENT;
	private IProposalService proposalService;
	private IProposalDAO proposalDao;
	private IAddedValueService addedValueService;
	private static final Logger LOGGER = Logger.getLogger(PricingStatusCalculatorService.class);
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#getPricingStatus(com.nyt.mpt.domain.LineItem, boolean)
	 */
	@Override
	public PricingStatus getPricingStatus(LineItem lineItem , boolean isSpecialAdvertiser) {
		PricingStatus pricingStatus;
		if(isSpecialAdvertiser || lineItem.getPriceType().equals(LineItemPriceTypeEnum.FLATRATE.getOptionValue())){
			pricingStatus= PricingStatus.SYSTEM_APPROVED;
		}else if(lineItem.getRateCardPrice().equals(0.0)){
			pricingStatus= PricingStatus.UNAPPROVED;
		}else{
			double difference = NumberUtil.round(((lineItem.getRateCardPrice() - NumberUtil.round(lineItem.getRate(), 2)) * 100) / lineItem.getRateCardPrice(), 2);
	        
		    if(difference <= Double.valueOf(TOLERANCE_PERCENT)){
		    	pricingStatus= PricingStatus.SYSTEM_APPROVED;
		    }else{
		    	pricingStatus= PricingStatus.UNAPPROVED;
		    }
		}
		lineItem.setPackageObj(null);
        return pricingStatus;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#getInvstmntOfQulfyngLI(java.util.Set)
	 */
	@Override
	public Double getInvstmntOfQulfyngLI(Set<LineItem> lineitems , boolean isThresholdCheck) {
		Double totalInvestment = 0D;
		LOGGER.info("Calculating Total Investment of Qualifying Line Items");
		Map<String,String> qualifyngTypeLI = proposalService.getQualifyingLineItems();
		if(isThresholdCheck){
			qualifyngTypeLI.put(LineItemPriceTypeEnum.PREEMPTIBLE.getOptionValue(), LineItemPriceTypeEnum.PREEMPTIBLE.getOptionValue());
		}
		for (LineItem lineItem : lineitems) {
			if(qualifyngTypeLI.containsKey(lineItem.getPriceType())){
				if(lineItem.getPriceType().equals(LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue())){
					if(lineItem.getRateCardPrice() != null){
						totalInvestment = totalInvestment + ((lineItem.getRateCardPrice() * lineItem.getImpressionTotal())/1000);
					}
				}else{
					totalInvestment = totalInvestment + lineItem.getTotalInvestment();
				}
			}
		}
		return totalInvestment;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#executeThreshHoldCheck(java.lang.Long)
	 */
	@Override
	public void addThreshHoldCheck(Long ProposalId) {
		LOGGER.info("Executing threshold check for pricing approved Line Items");
		Proposal proposal = proposalService.getProposalbyId(ProposalId);
		for(ProposalOption option : proposal.getProposalOptions()){
			addThreshHoldCheckForOption(option);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#addThreshHoldCheckForOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public void addThreshHoldCheckForOption(ProposalOption option) {
		Set<LineItem> lineItems = option.getLatestVersion().getProposalLineItemSet();
		if(lineItems != null &&  !lineItems.isEmpty()){
			Double totalInvestment = getInvstmntOfQulfyngLI(lineItems , true);
			if (option.getThresholdLimit() != null && option.getThresholdLimit() > totalInvestment) {
				for (LineItem lineItem : lineItems) {
					if (PricingStatus.PRICING_APPROVED.equals(lineItem.getPricingStatus()) && lineItem.getViewabilityLevel() != LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()) {
						lineItem.setPricingStatus(getPricingStatus(lineItem, option.getProposal().isSpecialAdvertiser()));
						proposalDao.editLineItemsOfProposal(lineItem);
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#executeAddedValueCheck(java.lang.Long)
	 */
	@Override
	public void addAddedValueCheck(Long ProposalId) {
		LOGGER.info("Executing Added value check");
		Proposal proposal = proposalService.getProposalbyId(ProposalId);
		for (ProposalOption option : proposal.getProposalOptions()) {
			addAddedValueCheckForOption(option);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#addAddedValueCheckForOption(com.nyt.mpt.domain.ProposalOption)
	 */
	@Override
	public void addAddedValueCheckForOption(ProposalOption option){
		Set<LineItem> lineItems = option.getLatestVersion().getProposalLineItemSet();
		if(lineItems != null &&  !lineItems.isEmpty()){
			Double totalInvestment = getInvstmntOfQulfyngLI(lineItems , false);
			Double allowedPercentage = getAddedValueAllowed(totalInvestment,option.getProposal().getPriceType(), option.getProposal().getAgencyMargin());
			Double allowedDiscount = (totalInvestment * allowedPercentage) / 100;
			Double totalDiscountOffrd = getTotalDiscountOffered(lineItems);
			for (LineItem lineItem : lineItems) {
				if (!PricingStatus.PRICING_APPROVED.equals(lineItem.getPricingStatus())
						&& lineItem.getPriceType().equals(LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue()) && lineItem.getViewabilityLevel() != LineItemViewableCriteriaEnum.VEIWABLE.getViewableValue()) {
					lineItem.setPricingStatus((allowedDiscount < totalDiscountOffrd) ? getPricingStatus(lineItem, option.getProposal().isSpecialAdvertiser())
							: PricingStatus.SYSTEM_APPROVED);
					proposalDao.editLineItemsOfProposal(lineItem);
				}
			}
		}
	}
	
	/**
	 * Method returns the added value worth in % that can be given for a option
	 * If no Added value rule is configured then we consider as 0% discount/allowed
	 * @param totalInvestment
	 * @param priceType
	 * @param agencyMargin
	 * @return
	 */
	private Double getAddedValueAllowed(Double totalInvestment, String priceType, Double agencyMargin) {
		Double addedValueInvestment = 0D;
		Double allowedPercentage = 0D;
		List<AddedValueBudget> addedValueList = addedValueService.getFilteredPlanBudgetList(null, null, new SortingCriteria("totalInvestment", "asc"));
		for (AddedValueBudget addedValueBudget : addedValueList) {
			addedValueInvestment = addedValueBudget.getTotalInvestment();
			if (PriceType.Gross.name().equals(priceType)) {
				addedValueInvestment = addedValueInvestment * (100 / (100 - agencyMargin));
			}
			if (totalInvestment >= addedValueInvestment) {
				allowedPercentage = addedValueBudget.getAvPercentage();
			}
		}
		return allowedPercentage;
	}
	
	
	/**
	 * Method returns how much discount is already offered
	 * @param lineItems
	 * @return
	 */
	private Double getTotalDiscountOffered(Set<LineItem> lineItems) {
		LOGGER.info("Calculating Total Discount Offered");
		Double totalDiscountOffrd = 0D;
		Map<String,String> qualifyngTypeLI = proposalService.getQualifyingLineItems();
		for (LineItem lineItem : lineItems) {
			if(qualifyngTypeLI.containsKey(lineItem.getPriceType()) || LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue().equals(lineItem.getPriceType())){
				if(lineItem.getPriceType().equals(LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue())){
					if(lineItem.getRateCardPrice() != null){
						totalDiscountOffrd = totalDiscountOffrd + ((lineItem.getRateCardPrice() * lineItem.getImpressionTotal())/1000);
					}
				}else{
					if(lineItem.getRateCardPrice() > lineItem.getRate()){
						totalDiscountOffrd = totalDiscountOffrd + ((lineItem.getRateCardPrice() - NumberUtil.round(lineItem.getRate(), 2)) * lineItem.getImpressionTotal())/1000;
					}
				}
			}
		}
		return totalDiscountOffrd;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#updatePricingStatusAsPricingApproved(java.lang.Long)
	 */
	@Override
	public Long updatePricingStatusAsPricingApproved(Long proposalId, Long[] optionIds) {
		List<ProposalOption> proposalOptionsLst = proposalService.getOptionLstbyIds(optionIds);
		for(ProposalOption option : proposalOptionsLst){
			for(LineItem lineItem : option.getLatestVersion().getProposalLineItemSet()){
				if(PricingStatus.UNAPPROVED.equals(lineItem.getPricingStatus())){
					lineItem.setPricingStatus(PricingStatus.PRICING_APPROVED);
					proposalDao.editLineItemsOfProposal(lineItem);
				}
			}
			option.setLastReviewedDate(DateUtil.getCurrentDate());
			proposalDao.saveOption(option);
		}
		return proposalId;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingStatusCalculatorService#getOffImpressions(double, java.lang.Long, java.lang.Long)
	 */
	@Override
	public double getOffImpressions(double  rateCardPrice , Long optionId, Long lineItemId){
		ProposalOption option =  proposalService.getOptionbyId(optionId);
		Set<LineItem> lineItems = option.getLatestVersion().getProposalLineItemSet();
		/**
		 * Following piece of code will remove the current Line Item	*/
		if(lineItemId > 0){
			Iterator<LineItem> iterator= lineItems.iterator();
			while(iterator.hasNext()) {
				LineItem lineItem= iterator.next();
				if(lineItem.getLineItemID().equals(lineItemId)){
					iterator.remove();
				}
			}
		}
		Double totalInvestment = getInvstmntOfQulfyngLI(lineItems , false);
		Double allowedPercentage = getAddedValueAllowed(totalInvestment, option.getProposal().getPriceType(), option.getProposal().getAgencyMargin());
		Double allowedDiscount = (totalInvestment * allowedPercentage) / 100;
		
		Double totalDiscountOffrd = getTotalDiscountOffered(lineItems);
		double offeredImpressions = ((allowedDiscount - totalDiscountOffrd)*1000 / rateCardPrice);
		return (offeredImpressions > 0) ? offeredImpressions : 0 ;
	}
	
	public String getTHRESHOLD_PERCENT() {
		return THRESHOLD_PERCENT;
	}

	public void setTOLERANCE_PERCENT(String tOLERANCE_PERCENT) {
		TOLERANCE_PERCENT = tOLERANCE_PERCENT;
	}

	public void setTHRESHOLD_PERCENT(String tHRESHOLD_PERCENT) {
		THRESHOLD_PERCENT = tHRESHOLD_PERCENT;
	}

	public void setProposalDao(IProposalDAO proposalDao) {
		this.proposalDao = proposalDao;
	}
	
	public void setProposalService(IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setAddedValueService(IAddedValueService addedValueService) {
		this.addedValueService = addedValueService;
	}

}
