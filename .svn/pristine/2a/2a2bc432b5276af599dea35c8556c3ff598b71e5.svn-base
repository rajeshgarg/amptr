package com.nyt.mpt.PricingCalculator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.service.IPricingCalculator;
import com.nyt.mpt.service.IPricingStatusCalculatorService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.IRateProfileService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.PricingStatus;

/**
 * JUnit test for calculate base price for a line item
 * @author rakesh.tewari
 *
 */
public class PricingCalculatorTest extends AbstractTest {

	@Autowired
	@Qualifier("pricingCalculator")
	private IPricingCalculator pricingCalculator;
	
	@Autowired
	@Qualifier("rateProfileService")	
	private IRateProfileService rateProfileService;
	
	@Autowired
	@Qualifier("sosService")
	private ISOSService sosService;
	
	@Autowired
	@Qualifier("pricingStatusCalculatorService")
	private IPricingStatusCalculatorService pricingStatusCalculatorService;
	
	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
	}

	/**
	 * Calculate base price when line item and sales category both are null
	 */
	@Test
	public void testPC_ForNull() {// pass
		Double baseprice = null;
		baseprice = pricingCalculator.getLineItemBasePrice(null, null);
		Assert.assertTrue(baseprice == null);
	}
	
	@Test
	public void testLineItemPriceFromDefaultCategoryWithSeasonalDiscount(){
		Long productId = null;
		String productName = ConstantStrings.EMPTY_STRING;
		Map<Long,String> salesCategoryMap = sosService.getSalesCategories();
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = new ArrayList<LineItemSalesTargetAssoc>();
		List<RateProfile> defaultRateProfiles = rateProfileService.getRateProfilesBySalesCategory(null);
		for (RateProfile rateProfile : defaultRateProfiles) {
			if(rateProfile.getSeasonalDiscountsLst() != null && !rateProfile.getSeasonalDiscountsLst().isEmpty()){
				productId = rateProfile.getProductId();
				productName = rateProfile.getProductName();
				for (RateConfig rateConfig : rateProfile.getRateConfigSet()){
					LineItemSalesTargetAssoc lineItemSalesTargetAssoc = new LineItemSalesTargetAssoc();
					lineItemSalesTargetAssoc.setSosSalesTargetId(rateConfig.getSalesTargetId());
					lineItemSalesTargetAssoc.setSosSalesTargetName(rateConfig.getSalesTargetName());
					lineItemSalesTargetAssocsList.add(lineItemSalesTargetAssoc);				
				}
				break;
			}			
		}
		LineItem lineItem = getDummyLineItem(productId,lineItemSalesTargetAssocsList,productName,LineItemPriceTypeEnum.CPM.getOptionValue());
		Map<String, Object> priceObject = pricingCalculator.getLineItemPrice(lineItem, salesCategoryMap.keySet().iterator().next(),ConstantStrings.NET);
		Assert.assertTrue((Double)priceObject.get("price") > 0D);		
	}
	
	@Test
	public void testLineItemPriceFromDefaultCategoryWithOutSeasonalDiscount(){
		Long productId = null;
		String productName = ConstantStrings.EMPTY_STRING;
		Map<Long,String> salesCategoryMap = sosService.getSalesCategories();
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = new ArrayList<LineItemSalesTargetAssoc>();
		List<RateProfile> defaultRateProfiles = rateProfileService.getRateProfilesBySalesCategory(null);
		for (RateProfile rateProfile : defaultRateProfiles) {
			if(rateProfile.getSeasonalDiscountsLst() == null || rateProfile.getSeasonalDiscountsLst().isEmpty()){
				productId = rateProfile.getProductId();
				productName = rateProfile.getProductName();
				for (RateConfig rateConfig : rateProfile.getRateConfigSet()){
					LineItemSalesTargetAssoc lineItemSalesTargetAssoc = new LineItemSalesTargetAssoc();
					lineItemSalesTargetAssoc.setSosSalesTargetId(rateConfig.getSalesTargetId());
					lineItemSalesTargetAssoc.setSosSalesTargetName(rateConfig.getSalesTargetName());
					lineItemSalesTargetAssocsList.add(lineItemSalesTargetAssoc);				
				}
				break;
			}			
		}
		LineItem lineItem = getDummyLineItem(productId,lineItemSalesTargetAssocsList,productName,LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue());
		Map<String, Object> priceObject = pricingCalculator.getLineItemPrice(lineItem, salesCategoryMap.keySet().iterator().next(), ConstantStrings.NET);
		Assert.assertTrue((Double)priceObject.get("price") > 0D);		
	}
	
	@Test
	public void testLineItemPriceFromCategory(){
		Long productId = null;
		Long salesCategoryId = null;
		String productName = ConstantStrings.EMPTY_STRING;
		List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList = new ArrayList<LineItemSalesTargetAssoc>();
		List<RateProfile> rateProfileLst = rateProfileService.getFilteredRateProfileList(null, null, null);
		for (RateProfile rateProfile : rateProfileLst) {
			if(rateProfile.getSalesCategoryId() != null){
				salesCategoryId = rateProfile.getSalesCategoryId();
				productId = rateProfile.getProductId();
				productName = rateProfile.getProductName();
				for (RateConfig rateConfig : rateProfile.getRateConfigSet()){
					LineItemSalesTargetAssoc lineItemSalesTargetAssoc = new LineItemSalesTargetAssoc();
					lineItemSalesTargetAssoc.setSosSalesTargetId(rateConfig.getSalesTargetId());
					lineItemSalesTargetAssoc.setSosSalesTargetName(rateConfig.getSalesTargetName());
					lineItemSalesTargetAssocsList.add(lineItemSalesTargetAssoc);				
				}
				break;
			}			
		}
		LineItem lineItem = getDummyLineItem(productId,lineItemSalesTargetAssocsList,productName,LineItemPriceTypeEnum.CPM.getOptionValue());
		Map<String, Object> priceObject = pricingCalculator.getLineItemPrice(lineItem, salesCategoryId, ConstantStrings.NET);
		Assert.assertTrue((Double)priceObject.get("price") > 0D);		
	}
	
	
	@Test
	public void testGetOffImpressions(){
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 100);
		Proposal proposal = null;
		Set <LineItem> lineItemSet = null;
		Double basePrice = null;
		final List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, pgCriteria, null);
		if (proposalLst.size() > 0 ) {
			for (Proposal proposalDB : proposalLst) {
				lineItemSet = proposalDB.getDefaultOption().getLatestVersion().getProposalLineItemSet();
				if(lineItemSet.size() > 1){
					for(LineItem lineItem : lineItemSet){
						if(lineItem.getRateCardPrice() != null && lineItem.getRateCardPrice() > 0){
							basePrice = lineItem.getRateCardPrice();
							break;
						}
					}
				}
				if(basePrice != null && basePrice > 0){
					proposal = proposalDB;
					break;
				}
			}
		}
		if(basePrice != null && proposal!= null && lineItemSet!= null) {
			double offeredImpressions = pricingStatusCalculatorService.getOffImpressions(basePrice, proposal.getDefaultOption().getId(), lineItemSet.iterator().next().getLineItemID());
			Assert.assertTrue(offeredImpressions >= 0);
		}
	}
	
	@Test
	public void testUpdatePricingStatusAsPricingApproved(){		
		PaginationCriteria pgCriteria = new PaginationCriteria(1, 2);
		final List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, pgCriteria, null);
		Long optionIds [] = new Long[proposalLst.get(0).getProposalOptions().size()];
		int i=0;
		for(ProposalOption option : proposalLst.get(0).getProposalOptions()){
			optionIds[i++] = option.getId();
		}
		Long proposalID = pricingStatusCalculatorService.updatePricingStatusAsPricingApproved(proposalLst.get(0).getId(), optionIds);
		Assert.assertTrue(proposalID == proposalLst.get(0).getId());
	}
	
	@Test
	public void testGetTHRESHOLD_PERCENT(){
		Assert.assertNotNull(pricingStatusCalculatorService.getTHRESHOLD_PERCENT());
	}
	
	private LineItem getDummyLineItem(Long productId, List<LineItemSalesTargetAssoc> lineItemSalesTargetAssocsList, String productName, String priceType) {
		LineItem returnLineItem = new LineItem();
		returnLineItem.setPriceType(priceType);
		returnLineItem.setLineItemSalesTargetAssocs(lineItemSalesTargetAssocsList);

		returnLineItem.setSosProductClass(0L);
		returnLineItem.setSosProductId(productId);
		returnLineItem.setProductName(productName);
		returnLineItem.setTargetingString("Test Targeting String");
		returnLineItem.setTotalInvestment(0.01);
		returnLineItem.setPricingStatus(PricingStatus.SYSTEM_APPROVED);
		returnLineItem.setProductType(LineItemProductTypeEnum.STANDARD);
		returnLineItem.setStartDate(DateUtil.getPriorDateFromCurrentDate(365));
		returnLineItem.setEndDate(DateUtil.getCurrentDate());

		Set<LineItemTarget> geoTargetSet = new LinkedHashSet<LineItemTarget>();
		LineItemTarget lineItemTarget = new LineItemTarget();
		lineItemTarget.setActive(true);
		lineItemTarget.setProposalLineItem(returnLineItem);
		lineItemTarget.setSosTarTypeElementId("101,2,3");
		lineItemTarget.setSosTarTypeId(1L);
		lineItemTarget.setPremium(150D);
		
		LineItemTarget lineItemTargetRegion = new LineItemTarget();
		lineItemTargetRegion.setActive(true);
		lineItemTargetRegion.setProposalLineItem(returnLineItem);
		lineItemTargetRegion.setSosTarTypeElementId("4600,4601,4602,4603,4604,5143,5144,5145");
		lineItemTargetRegion.setSosTarTypeId(35L);
		lineItemTargetRegion.setPremium(150D);
		
		LineItemTarget lineItemTargetAudience = new LineItemTarget();
		lineItemTargetAudience.setActive(true);
		lineItemTargetAudience.setProposalLineItem(returnLineItem);
		lineItemTargetAudience.setSosTarTypeElementId("dsfsf");
		lineItemTargetAudience.setSosTarTypeId(33L);
		lineItemTargetAudience.setSegmentLevel("LEVEL1");
		
		LineItemTarget lineItemTargetCountry = new LineItemTarget();
		lineItemTargetCountry.setActive(true);
		lineItemTargetCountry.setProposalLineItem(returnLineItem);
		lineItemTargetCountry.setSosTarTypeElementId("300,299,298,297,296,295,294,293,292,291,290,289");
		lineItemTargetCountry.setSosTarTypeId(8L);
		lineItemTargetCountry.setPremium(150D);
		
		
		geoTargetSet.add(lineItemTargetAudience);
		geoTargetSet.add(lineItemTargetRegion);
		geoTargetSet.add(lineItemTarget);
		geoTargetSet.add(lineItemTargetCountry);
		returnLineItem.setGeoTargetSet(geoTargetSet);
		return returnLineItem;
	}
}