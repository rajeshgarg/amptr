package com.nyt.mpt.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.PricingCalculatorSummary;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.domain.RateProfileSummary;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.TierSummary;
import com.nyt.mpt.service.IPricingCalculator;
import com.nyt.mpt.service.IRateProfileService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.ITierService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;

/**
 * Used for calculate base price for a line item
 * @author rakesh.tewari
 */
public class PricingCalculator implements IPricingCalculator {

	private static final String DEFAULT = "DEFAULT";

	private static final String CATEGORY = "CATEGORY";

	private static final Logger LOGGER = Logger.getLogger(PricingCalculator.class);

	private IRateProfileService rateProfileService;
	private ITierService tierService;
	private ISalesTargetService salesTargetService;
	private ITargetingService targetingService;
	private ISOSService sosService;

	private static final String STATES = "States";
	private static final String ADXDMA = "Adx DMA";
	private static final String COUNTRIES = "Countries";
	private static final String TARGET_REDION = "Target Region";

	private static final String BEHAVIORAL = "Behavioral";

	private static final long ROS_ID = 16;
	private static final long SECTION_ID = 17;
	private static final long SUBSECTION_ID = 18;
	private static final long NYTD_EMAIL_ID = 19;

	private PricingCalculatorSummary pricingCalculatorStep = null;
	private Map<String,Boolean> rateCardRoundMap = null;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingCalculator#getLineItemPrice(com.nyt.mpt.domain.LineItem, java.lang.Long)
	 */
	@Override
	public Map<String, Object> getLineItemPrice(final LineItem lineItem, final Long salesCategoryId, String priceType) {
		//getSalesTarget(lineItem);
		Double primiunm = null;
		Double price = null;
		String jsonString = ConstantStrings.EMPTY_STRING;
		Map<String, Object> pricingCalculatorMap = new HashMap<String, Object>();
		pricingCalculatorStep = new PricingCalculatorSummary();
		rateCardRoundMap = new HashMap<String, Boolean>();
		Double basePrice = getLineItemBasePrice(lineItem, salesCategoryId);
		if(rateCardRoundMap != null &&  !rateCardRoundMap.isEmpty()){
			pricingCalculatorMap.put("rateCardRounded", rateCardRoundMap.get((rateCardRoundMap.containsKey(CATEGORY)) ? CATEGORY : DEFAULT));
		}
		if (basePrice != null) {
			primiunm = getLineItemPremium(lineItem);
		    price = calculatePrice(basePrice, primiunm);
		    if ("Net".equalsIgnoreCase(priceType)) {
		    	boolean israteCardRounded = (Boolean) pricingCalculatorMap.get("rateCardRounded");
				if(!israteCardRounded){
					pricingCalculatorStep.setAppliedFiveCentsRule(ConstantStrings.YES);
					pricingCalculatorStep.setPrice(NumberUtil.formatDouble(NumberUtil.getHalfCentFormatedValue(price),true));
				}else{
					pricingCalculatorStep.setAppliedFiveCentsRule(ConstantStrings.NO);
					pricingCalculatorStep.setPrice(NumberUtil.formatDouble(price, true));
				}
		    }
		    jsonString = convertToJsonObject();
		}
		pricingCalculatorMap.put("price", price);
		pricingCalculatorMap.put("calculatorStep", jsonString);
		return pricingCalculatorMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingCalculator#getLineItemBasePrice(com.nyt.mpt.domain.LineItem, java.lang.Long)
	 */
	@Override
	public Double getLineItemBasePrice(final LineItem lineItem, final Long salesCategoryId) {
		if (lineItem != null) {
			final List<RateProfile> defaultRateProfiles = rateProfileService.getRateProfilesBySalesCategory(null);
			List<RateProfile> salesCategoryRateProfiles = null;
			if (salesCategoryId != null && !LineItemPriceTypeEnum.ADDEDVALUE.getOptionValue().equals(lineItem.getPriceType())) {
				salesCategoryRateProfiles = rateProfileService.getRateProfilesBySalesCategory(salesCategoryId);
				/* 
				 * Set sales category name in pricing Calculation Step
				 */
				final Map<Long, String> salesCategories = sosService.getSalesCategoryById(salesCategoryId);
				if (salesCategories.get(salesCategoryId) != null) {
					pricingCalculatorStep.setSalesCategory(salesCategories.get(salesCategoryId));
				}
			}
			if ((defaultRateProfiles != null && !defaultRateProfiles.isEmpty()) || (salesCategoryRateProfiles != null && !salesCategoryRateProfiles.isEmpty())) {
				final Set<Long> salesTargetSet = getSalesTargetSet(lineItem.getLineItemSalesTargetAssocs());

				final Map<Long, Double> salesTargetPriceMap = new HashMap<Long, Double>();
				for (Long salesTargetId : salesTargetSet) {
					/* Creat rate profile summary object */
					final RateProfileSummary rateProfileSummary = new RateProfileSummary(); 
					Double salesTargetPrice = getProductSalesTargetPrice(rateProfileSummary, salesCategoryRateProfiles, salesTargetId, lineItem , true);
					Double defaultSalesTargetPrice = getProductSalesTargetPrice(rateProfileSummary, defaultRateProfiles, salesTargetId, lineItem, false);
					/* Set base price summary for default and sales target */
					if(rateProfileSummary.getSalesTargetId() != null){
						pricingCalculatorStep.getRateProfileSummary().add(rateProfileSummary);
					}
					if (salesTargetPrice == null && defaultSalesTargetPrice == null) {
						return null;
					} else if (salesTargetPrice == null && defaultSalesTargetPrice != null) {
						salesTargetPrice = defaultSalesTargetPrice;
					} else if (salesTargetPrice != null && defaultSalesTargetPrice != null) {
							/*
							 * When list and category rate are available then select lowest value from them
							 */
						salesTargetPrice = (salesTargetPrice < defaultSalesTargetPrice) ? salesTargetPrice : defaultSalesTargetPrice;
						rateCardRoundMap.remove((salesTargetPrice < defaultSalesTargetPrice) ? DEFAULT : CATEGORY);
					}

					if (salesTargetPrice != null) {
						salesTargetPriceMap.put(salesTargetId, salesTargetPrice);
					}

				}
				final List<SalesTargetAmpt> salesTarAmptLst = salesTargetService.getSalesTarget(salesTargetSet.toArray(new Long[0]));
				return weightedBasePrice(salesTarAmptLst, salesTargetPriceMap);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IPricingCalculator#getLineItemPremium(com.nyt.mpt.domain.LineItem)
	 */
	@Override
	public Double getLineItemPremium(final LineItem lineItem) {
		if (lineItem == null || lineItem.getGeoTargetSet().isEmpty()) {
			return null;
		} else {
			final Set<Long> salesTargetSet = getSalesTargetSet(lineItem.getLineItemSalesTargetAssocs());
			final Tier highestLevelTier = getTierForSectionsIDSet(salesTargetSet);
			if (highestLevelTier == null) {
				return null;
			} else {
				return getPremiumFromTier(highestLevelTier, lineItem.getGeoTargetSet());
			}
		}
	}

	/**
	 * Filter a premium for a set of {@link LineItemTarget} form highest level
	 * tier.
	 * <p>
	 * For state, dma, country and region target type system takes minimum
	 * premium and for other system takes maximum premium
	 * 
	 * @param tier
	 *            Highest level {@link Tier}
	 * @param targetSet
	 *            A set of {@link LineItemTarget}
	 * @return Maximum premium value either audience type is behavioral, state,
	 *         dma, country, region or any other.
	 */
	private Double getPremiumFromTier(Tier tier, Set<LineItemTarget> targetSet) {
		Double maxPremium = 0.0D;
		Double minPremium = -1.0D;
		Double premium = 0.0;
		String audience = ConstantStrings.EMPTY_STRING;
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
		for (LineItemTarget lineItemTarget : targetSet) {
			/* Return list of TierPremium if based on line item target type. */
			final List<TierPremium> premiumLst = getTierPremium(tier.getTierPremiumLst(), lineItemTarget);
			if (!premiumLst.isEmpty()) {
				audience = targetTypeMap.get(lineItemTarget.getSosTarTypeId());
				TierSummary tierSummary = setTierValue(tier, audience);
				/* Check condition for revsci*/
				if (BEHAVIORAL.equalsIgnoreCase(audience)) {
					tierSummary.setTargetType(tierSummary.getTargetType() + ConstantStrings.HYPHEN + lineItemTarget.getSegmentLevel());
					maxPremium = checkRevenueSegments(premiumLst, lineItemTarget, targetTypeMap, maxPremium, tierSummary);
					premium = maxPremium;
				} else {
					final List<String> elementLst = convertStringToList(lineItemTarget.getSosTarTypeElementId());
					if (elementLst != null && !elementLst.isEmpty()) {
						/* Check condition for state, dma, country, region */
						if (checkPremiumRestrictions(lineItemTarget.getSosTarTypeId(), targetTypeMap)) {
							minPremium = getMinPremium(premiumLst, minPremium, elementLst, tierSummary);
							premium = minPremium;
						} else {
							maxPremium = getMaxPremium(premiumLst, maxPremium, elementLst, tierSummary);
							premium = maxPremium;
						}
					}
				}

				premium = 0.0;
			}
		}
		if (maxPremium > minPremium) {
			premium = maxPremium;
		} else {
			premium = minPremium;
		}
		if (premium == -1.0) {
			premium = null;
		}
		setHighestLevelTierValue(tier, premium);
		return premium;
	}

	/**
	 * Apply a premium on calculated base price of a line item.
	 * <P>
	 * Then roundoff the calculated value of two decimal place.
	 * <p>
	 * Formula to apply a premium on calculated base price
	 * <p>
	 * Base Price = basePrice + (basePrice * premium) / 100;
	 * 
	 * @param basePrice
	 *            Base price of a line item.
	 * @param premium
	 *            Applied premium based on target types.
	 * @return Base price after apply a premium.
	 */
	private Double calculatePrice(final Double basePrice, Double premium) {
		Double price = null;
		if (basePrice != null) {
			if (premium == null) {
				premium = 0.0;
			}
			price = basePrice + (basePrice * premium) / 100;
			price = NumberUtil.round(price, 2);
		}
		pricingCalculatorStep.setBasePrice(price);
		return price;
	}

	/**
	 * Creates a set of sos sales target id's from List of
	 * {@link LineItemSalesTargetAssoc}.
	 * 
	 * @param salesTargetList
	 *            List of {@link LineItemSalesTargetAssoc}.
	 * @return A set of sos sales target id's
	 */
	private Set<Long> getSalesTargetSet(final List<LineItemSalesTargetAssoc> salesTargetList) {
		final Set<Long> salesTargetSet = new HashSet<Long>();
		for (LineItemSalesTargetAssoc salesTargetAssoc : salesTargetList) {
			salesTargetSet.add(salesTargetAssoc.getSosSalesTargetId());
		}
		return salesTargetSet;
	}

	/**
	 * Return base price for product and sales target combination after apply
	 * seasonal discount.
	 * <p>
	 * System apply discount only if 75% of discount's start date and end date
	 * covers line item start date and end date.
	 * <p>
	 * Formula to apply a seasonal discount
	 * <p>
	 * Base Price = basePrice - (basePrice * appliedDiscount / 100);
	 * 
	 * @param rateProfileSummary
	 *            On this summary data to be populated.
	 * @param rateProfileList
	 *            List of {@link RateProfile}
	 * @param salesTargetId
	 *            Id of a sales target and this is a part of a line item.
	 * @param lineItem
	 *            {@link LineItem}
	 * @param isCategory
	 *            If flag is true populate category rate profile data otherwise
	 *            populates default rate profile data.
	 * @return Base price for product and sales target combination after apply
	 *         seasonal discount.
	 */
	private Double getProductSalesTargetPrice(RateProfileSummary rateProfileSummary, final List<RateProfile> rateProfileList, long salesTargetId, final LineItem lineItem, boolean isCategory) {
		Double discount = null;
		Double appliedDiscount = null;
		Double basePrice = null;
		if (rateProfileList != null && !rateProfileList.isEmpty()) {
			for (RateProfile rateProfile : rateProfileList) {
				if (rateProfile.getProductId() == lineItem.getSosProductId()) {
					for (RateConfig rateConfig : rateProfile.getRateConfigSet()) {
						if (rateConfig.getSalesTargetId() == salesTargetId) {
							if (lineItem.getStartDate() != null && lineItem.getEndDate() != null) {
								/* Apply seasonal discount */
								for (RateProfileSeasonalDiscounts seasonaldiscount : rateProfile.getSeasonalDiscountsLst()) {
									discount = getSeasonalDiscount(seasonaldiscount, lineItem);
									if (appliedDiscount == null && discount != null) {
										appliedDiscount = discount;
									} else {
										appliedDiscount = (discount != null && discount < appliedDiscount) ? discount : appliedDiscount;
									}
								}
								/**/
								basePrice = rateProfile.getBasePrice();
								if (appliedDiscount != null) {
									basePrice = basePrice - (basePrice * appliedDiscount / 100);
								}
							} else {
								basePrice = rateProfile.getBasePrice();
							}
							setRateProfileData(rateProfileSummary, rateConfig, rateProfile, appliedDiscount, basePrice, isCategory);
							if(isCategory){
								rateCardRoundMap.put(CATEGORY, rateProfile.isRateCardNotRounded());
							}else{
								rateCardRoundMap.put(DEFAULT, rateProfile.isRateCardNotRounded());
							}
							return basePrice;
						}
					}
				}
			}
		}
		return basePrice;
	}

	/**
	 * Below are the different scenarios to apply a discount or not. System
	 * apply discount only if 75% of discount's start date and end date covers
	 * line item start date and end date.
	 * <p>
	 * .............LSD---------------------LED
	 * <p>
	 * 1 DSD-------DED
	 * <p>
	 * 2 ...DSD---------------DED
	 * <p>
	 * 3 ............................DSD-----------------DED
	 * <p>
	 * 4 .................DSD-------------DED
	 * <p>
	 * 5 DSD---------------------------------------------DED
	 * <p>
	 * 6 ....................................................DSD----------DED
	 * <p>
	 * 7 DSD----------------------------------DED(LED==DED)
	 * <p>
	 * 8 ................DSD-----------------------------------DED(DSD==LSD)
	 * <p>
	 * 9 ................DSD---------------------DED
	 * <p>
	 * 10 ................DSD---------------DED
	 * <p>
	 * 11 .....................DSD---------------DED
	 * <p>
	 * 12 ...................................DSD-DED
	 * <p>
	 * 13 ................DSD-DED
	 * 
	 * <p>
	 * <ul>
	 * <li>LSD = Line item start date</li>
	 * <li>LED = Line item end date</li>
	 * <li>DSD = Discount start date</li>
	 * <li>DED = Discount end date</li>
	 * </ul>
	 * 
	 * @param seasonaldiscount {@link RateProfileSeasonalDiscounts} is seasonal discount, and this is configured with a rate profile
	 * @param lineItem {@link LineItem}
	 * @return Discount if applicable base on line item's and discount's start date and end date.
	 */
	private Double getSeasonalDiscount(final RateProfileSeasonalDiscounts seasonaldiscount, final LineItem lineItem) {
		Double discount = null;
		final long liStartDate = lineItem.getStartDate().getTime();
		final long liEndDate = lineItem.getEndDate().getTime();
		final long discStartDate = seasonaldiscount.getStartDate().getTime();
		final long discEndDate = seasonaldiscount.getEndDate().getTime();
		final long percentageLIFlight = ((liEndDate - liStartDate) * 75) / 100;
		long percentageOfDiscountFlight = 0;
		boolean isSeasonalDiscount = false;
		if (discStartDate > liEndDate || discEndDate < liStartDate) {
			/* This above condition for 1 and 6 */
			if (seasonaldiscount.isNot()) {
				/*
				 * When discount flight is out side of line item flight and with
				 * not
				 */
				isSeasonalDiscount = true;
			}
		} else if (discStartDate <= liStartDate && discEndDate >= liEndDate) {
			/*
			 * 	This above condition for 5, 9 (both flight are same)
			 *	When 'discount flight' (with extra cornar flight ) cover 'line item flight' and without not
			 * */
			if (!seasonaldiscount.isNot()) {
				isSeasonalDiscount = true;
			}
		} else if (discStartDate >= liStartDate && discEndDate <= liEndDate) {
			/* This above condition for 4, 11, 10 , 12, 13
			 * When 'line item flight' (with extra cornar flight ) cover 'discount flight'
			 */
			if (seasonaldiscount.isNot()) {
				/* With not */
				long disStatLIStartDiff = discStartDate - liStartDate;
				long liEndDisEndDiff = liEndDate - discEndDate;
				percentageOfDiscountFlight = disStatLIStartDiff + liEndDisEndDiff;
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			} else {
				/* Without not */
				percentageOfDiscountFlight = discEndDate - discStartDate;
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			}
		} else if (discStartDate >= liStartDate && discEndDate > liEndDate && discStartDate < liEndDate) {
			/*
			 * This above condition for 3, 8 When discount start date greater then or
			 * equal line item start date but less then line item end date and
			 * discount end date greater then line item end date
			 */
			if (seasonaldiscount.isNot()) {
				/* With not */
				percentageOfDiscountFlight = discStartDate - liStartDate;
				/* if discount DSD and line item LED date diff is greater than LSD and LED diff */
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			} else {
				/* Without not */
				percentageOfDiscountFlight = liEndDate - discStartDate;
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			}
		} else if (discStartDate < liStartDate && discEndDate <= liEndDate && discEndDate > liStartDate) {
			/* This above condition for 2, 7
			 * When discount start date less then line item start date and discount end date less then or equal line item end date
			 * but discount end date greater then line item start date
			 * */
			if (seasonaldiscount.isNot()) {
				/* With not */
				percentageOfDiscountFlight = liEndDate - discEndDate;
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			} else {
				/* Without not */
				percentageOfDiscountFlight = discEndDate - liStartDate;
				if (percentageOfDiscountFlight >= percentageLIFlight) {
					isSeasonalDiscount = true;
				}
			}
		}
		if (isSeasonalDiscount) {
			discount = seasonaldiscount.getDiscount();
		}
		return discount;
	}

	/**
	 * Calculate weighted base price for selected sales target. <li>Formula for
	 * calculating a base price</li> </ul>
	 * 
	 * <p>
	 * If sum of (ST1W1+----STnWn) is not zero
	 * <ul>
	 * <li>Base Price = (BP1* ST1w1+-----+BPn*STnWn)/(ST1W1+----STnWn)</li>
	 * <li>Where</li>
	 * <li>BP1 = Base Price of rate profile</li>
	 * <li>ST1w1 = Sales target weight</li>
	 * </ul>
	 * 
	 * <p>
	 * If sum of (ST1W1+----STnWn) is zero but base price defined in rate
	 * profile is not zero
	 * <ul>
	 * <li>Base Price = (BP1* ST1w1+-----+BPn*STnWn)/(Total number of Sales
	 * target)</li>
	 * <li>Where</li>
	 * <li>BP1 = Base Price of rate profile</li>
	 * </ul>
	 * 
	 * @param salesTargetLst
	 *            List of {@link SalesTargetAmpt}
	 * @param basePriceMap
	 *            Map of sales target id and it's weight.
	 * @return Calculate weighted base price for selected sales target.
	 */
	private Double weightedBasePrice(final List<SalesTargetAmpt> salesTargetLst, final Map<Long, Double> basePriceMap) {
		Double weightedPrice = 0.0;
		Double sumOfweight = 0.0;
		double sumOfBasePrice = 0;
		Long totalCount = 0L;
		Double weight = 0.0;
		for (Long salesTargetId : basePriceMap.keySet()) {
			weight = 0.0;
			for (SalesTargetAmpt salesTargetAmpt : salesTargetLst) {
				if (salesTargetId.longValue() == salesTargetAmpt.getSalesTargetId()) {
					weight = salesTargetAmpt.getWeight();
					setWeightOfSalesTarget(weight, salesTargetId);
				}
			}
			weightedPrice += weight * basePriceMap.get(salesTargetId);
			sumOfweight = sumOfweight + weight;
			sumOfBasePrice = sumOfBasePrice + basePriceMap.get(salesTargetId);
			totalCount = totalCount + 1;
		}
		setWeightedBasePriceValue(weightedPrice, sumOfweight, sumOfBasePrice, totalCount);
		if (sumOfweight == 0.0) {
			weightedPrice = sumOfBasePrice / totalCount;
		} else {
			weightedPrice = weightedPrice / sumOfweight;
		}
		pricingCalculatorStep.setWeightedBasePrice(String.valueOf(weightedPrice));
		return weightedPrice;
	}

	/**
	 * Method gets a List of {@link SalesTarget} for a set of sos sales target
	 * id's after that set of parent sales target id for line item sales target
	 * 
	 * @param salesTargetIdSet
	 *            Set of sales target id
	 * @return A highest level tier.
	 */
	private Tier getTierForSectionsIDSet(final Set<Long> salesTargetIdSet) {
		List<Tier> allTierLst = new ArrayList<Tier>();
		/*Fetching */
		final List<SalesTarget> salesTargetLst = salesTargetService.getSalesTargetListByIDs(salesTargetIdSet.toArray(new Long[0]));
		for (SalesTarget salesTarget : salesTargetLst) {
			final Set<Long> sectionsIDSet = new LinkedHashSet<Long>();
			long targetTypeId = salesTarget.getSalesTargetType().getSalestargetTypeId();
			if (targetTypeId == ROS_ID || targetTypeId == SECTION_ID) {
				sectionsIDSet.add(salesTarget.getSalesTargetId());
				getTierForSalesTargetId(sectionsIDSet, allTierLst);
			} else if (targetTypeId == SUBSECTION_ID && salesTarget.getParentSalesTargetId() != null) {
				sectionsIDSet.add(salesTarget.getParentSalesTargetId());
				getTierForSalesTargetId(sectionsIDSet, allTierLst);
			} else if ((targetTypeId == SUBSECTION_ID && salesTarget.getParentSalesTargetId() == null) || targetTypeId == NYTD_EMAIL_ID) {
				getTierForROS(allTierLst);
			}
		}
		return getHighestLevelTier(allTierLst);
	}

	/**
	 * <p>
	 * Adds all the {@link Tier} in parameter List whose sales target type id is
	 * 16. And all these tiers are called ROS tier.
	 * 
	 * @param allTierLst
	 *            List of {@link Tier}.
	 */
	private void getTierForROS(List<Tier> allTierLst) {
		final List<Long> salesTargetTypeIdLst = new ArrayList<Long>();
		salesTargetTypeIdLst.add(ROS_ID);
		Set<Long> sectionIdSet = getSectionsBySalesTargetTypeId(salesTargetTypeIdLst);
		if (!sectionIdSet.isEmpty()) {
			/* Gets all the Tier for those sales target whose parent type is ROS */
			final List<Tier> tierLst = tierService.getTierSectionAssocList(sectionIdSet);
			if (tierLst != null && !tierLst.isEmpty()) {
				for (Tier tier : tierLst) {
					if (!allTierLst.contains(tier)) {
						allTierLst.add(tier);
					}
				}
			}
		}
	}

	/**
	 * <p>For List of sales target type id method fetches a List of
	 * {@link SalesTarget}. And form this List method creates a Set of sales
	 * target id's.
	 * 
	 * @param salesTargetTypeIdLst List of {@link SalesTarget}.
	 * @return A Set of sales target type id's.
	 */
	private Set<Long> getSectionsBySalesTargetTypeId(List<Long> salesTargetTypeIdLst) {
		final Set<Long> sectionIdSet = new LinkedHashSet<Long>();
		/*Fetching a list of active sales targets*/
		final List<SalesTarget> salesTargetList = salesTargetService.getActiveSalesTargetBySTTypeId(salesTargetTypeIdLst);
		for (SalesTarget salesTarget : salesTargetList) {
			sectionIdSet.add(salesTarget.getSalesTargetId());
		}
		return sectionIdSet;
	}

	/**
	 * <p>
	 * For a Set of sos sales target id's, method fetching a List of
	 * {@link Tier} from data base and add these {@link Tier} Object in the
	 * parameter List Get tier for salesTargetId
	 * 
	 * @param sectionsIDSet
	 *            Set of sections ID
	 * @param allTierLst
	 *            List of all {@link Tier}.
	 */
	private void getTierForSalesTargetId(final Set<Long> sectionsIDSet, List<Tier> allTierLst) {
		final List<Tier> tierLst = tierService.getTierSectionAssocList(sectionsIDSet);
		if (tierLst != null && !tierLst.isEmpty()) {
			for (Tier tier : tierLst) {
				if (!allTierLst.contains(tier)) {
					allTierLst.add(tier);
				}
			}
		} else {
			getTierForROS(allTierLst);
		}
	}

	/**
	 * Get highest level tier from list of tier
	 * @param sectionsIDSet
	 * @return Highest level tier 
	 */
	private Tier getHighestLevelTier(final List<Tier> allTierLst) {
		Tier highestLevelTier = null;
		if (allTierLst != null && !allTierLst.isEmpty()) {
			for (Tier tier : allTierLst) {
				if (highestLevelTier == null || highestLevelTier.getTierLevel() < tier.getTierLevel()) {
					highestLevelTier = tier;
				}
			}
		}
		return highestLevelTier;
	}

	/**
	 * Returns list of TierPremium if based on line item target type.
	 * @param tierPremiumLst
	 * @param lineItemTarget
	 * @return List of {@link TierPremium}
	 */
	private List<TierPremium> getTierPremium(final List<TierPremium> tierPremiumLst, final LineItemTarget lineItemTarget) {
		final List<TierPremium> premiumLst = new ArrayList<TierPremium>();
		if (tierPremiumLst != null && !tierPremiumLst.isEmpty()) {
			for (TierPremium tierPremium : tierPremiumLst) {
				if (tierPremium.getTargetType() != null && lineItemTarget.getSosTarTypeId() == tierPremium.getTargetType().getSosAudienceTargetTypeId()) {
					premiumLst.add(tierPremium);
				}
			}
		}
		return premiumLst;
	}

	/**
	 * Converts comma separated string to List
	 * @param elementId
	 * @return List of Id
	 */
	private List<String> convertStringToList(final String elementId) {
		String[] elementArray = null;
		List<String> premiumElementLst = new ArrayList<String>();
		if (StringUtils.isNotBlank(elementId)) {
			elementArray = elementId.split(ConstantStrings.COMMA);
			premiumElementLst = Arrays.asList(elementArray);
		}
		return premiumElementLst;
	}

	/**
	 * Check revenue segments
	 * @param premiumLst
	 * @param lineItemTarget
	 * @param targetTypeMap
	 * @param maxPremium
	 * @return
	 */
	private Double checkRevenueSegments(List<TierPremium> premiumLst, LineItemTarget lineItemTarget, Map<Long, String> targetTypeMap, Double maxPremium, TierSummary tierSummary) {
		TierPremium defaultPremium = null;
		boolean checkInDefault = true;
		Double premium = maxPremium;
		Double maxTierPremium = 0.0D;
		boolean isElementAvailable = false;
		/* premiumLst contain TierPremium data for particular Target type */
		for (TierPremium tierPremium : premiumLst) {
			/* default premium for for particular Target type */
			if (StringUtils.isBlank(tierPremium.getTarTypeElementId())) {
				defaultPremium = tierPremium;
			} else {
				final List<String> premiumElementLst = convertStringToList(tierPremium.getTarTypeElementId());
				if (premiumElementLst != null && !premiumElementLst.isEmpty()) {
					/* Check either Level of line item target type is available in tier premium or not */
					if (premiumElementLst.contains(lineItemTarget.getSegmentLevel())) {
						premium = getMaxValue(maxPremium, tierPremium.getPremium());
						checkInDefault = false;
						/* set max Premium if multiple premiums are available in on tier */
						maxTierPremium = getMaxValue(maxTierPremium, tierPremium.getPremium());
						isElementAvailable = true;
					}
				}
			}
		}
		if (defaultPremium != null && checkInDefault) {
			premium = getMaxValue(maxPremium, defaultPremium.getPremium());
			/* set max Premium if multiple premiums are available in on tier */
			maxTierPremium = getMaxValue(maxTierPremium, defaultPremium.getPremium());
			isElementAvailable = true;
		}
		/* target type is available in LI as well as tier */
		if (isElementAvailable && maxTierPremium > 0.0) {
			tierSummary.setPremium(maxTierPremium);
			pricingCalculatorStep.getTierSummary().add(tierSummary);
		}
		return premium;
	}

	/**
	 * This method  maximum premium (not for state, dma, country, region)
	 * @param premiumLst
	 * @param maxPremium
	 * @param elementLst
	 * @return
	 */
	private Double getMaxPremium(final List<TierPremium> premiumLst, Double maxPremium, final List<String> elementLst, TierSummary tierSummary) {
		TierPremium defaultPremium = null;
		boolean checkInDefault = true;
		Double premium = maxPremium;
		Double maxTierPremium = 0.0D;
		boolean isElementAvailable = false;
		for (String element : elementLst) {
			checkInDefault = true;
			/* premiumLst contain TierPremium data for particular Target type */
			for (TierPremium tierPremium : premiumLst) {
				/* default premium for for particular Target type */
				if (StringUtils.isBlank(tierPremium.getTarTypeElementId())) {
					defaultPremium = tierPremium;
				} else {
					final List<String> premiumElementLst = convertStringToList(tierPremium.getTarTypeElementId());
					if (premiumElementLst != null) {
						/* Check either element of line item is available in tier premium or not */
						if (premiumElementLst.contains(element)) {
							premium = getMaxValue(maxPremium, tierPremium.getPremium());
							/* set max Premium if multiple premiums are available in on tier */
							maxTierPremium = getMaxValue(maxTierPremium, tierPremium.getPremium());
							checkInDefault = false;
							isElementAvailable = true;
						}
					}
				}
			}
			/* Find premium from default premium    */
			/* if element of line item is not available in tier premium than use default premium if exist */
			if (defaultPremium != null && checkInDefault) {
				premium = getMaxValue(maxPremium, defaultPremium.getPremium());
				/* set max Premium if multiple premiums are available in on tier */
				maxTierPremium = getMaxValue(maxTierPremium, defaultPremium.getPremium());
				isElementAvailable = true;
			}
			/* Set  maxPremium for a condition when we find new premium and more other element has corresponding premium */
			maxPremium = premium;
		}
		/* target type is available in LI as well as tier */
		if (isElementAvailable && maxTierPremium > 0.0) {
			tierSummary.setPremium(maxTierPremium);
			pricingCalculatorStep.getTierSummary().add(tierSummary);
		}
		return premium;
	}

	/**
	 * <p>
	 * For states, Adx DMA, Countries and Target Region method returns minimum
	 * premium. 
	 * 
	 * @param premiumLst
	 *            List of {@link TierPremium}.
	 * @param minPremium
	 *            Old premium.
	 * @param elementLst
	 *            List of {@link LineItemTarget} id.
	 * @param tierSummary
	 *            Summary of tier's which takes participate in pricing
	 *            calculation.
	 * @return Premium for {@link PricingCalculator.STATES},
	 *         {@link PricingCalculator.ADXDMA},
	 *         {@link PricingCalculator.COUNTRIES} and
	 *         {@link PricingCalculator.TARGET_REDION}
	 */
	private Double getMinPremium(final List<TierPremium> premiumLst, Double minPremium, final List<String> elementLst, TierSummary tierSummary) {
		TierPremium defaultPremium = null;
		boolean checkInDefault = true;
		Double premium = minPremium;
		Double minTierPremium = -1.0D;
		boolean isElementAvailable = false;
		for (String element : elementLst) {
			checkInDefault = true;
			/* premiumLst contain TierPremium data for particular Target type */
			for (TierPremium tierPremium : premiumLst) {
				if (StringUtils.isBlank(tierPremium.getTarTypeElementId())) { // default premium for for particular Target type
					defaultPremium = tierPremium;
				} else {
					final List<String> premiumElementLst = convertStringToList(tierPremium.getTarTypeElementId());
					if (premiumElementLst != null) {
						/* Check either element of line item is available in tier premium */
						if (premiumElementLst.contains(element)) {
							premium = getMinValue(minPremium, tierPremium.getPremium());
							checkInDefault = false;
							/* set min Premium if multiple premiums are available within tier */
							minTierPremium = getMinValue(minTierPremium, tierPremium.getPremium());
							isElementAvailable = true;
						}
					}
				}
			}
			/* Find premium from default premium  */
			/* if element of line item is not available in tier premium than use default premium if exist */
			if (defaultPremium != null && checkInDefault) {
				premium = getMinValue(minPremium, defaultPremium.getPremium());
				/* set min Premium if multiple premiums are available in on tier */
				minTierPremium = getMinValue(minTierPremium, defaultPremium.getPremium());
				isElementAvailable = true;
			}
			/* Set  minPremium for a condition when we find new premium and more other element has corresponding premium */
			minPremium = premium;
		}
		if (isElementAvailable && minTierPremium > -1.0D) {
			tierSummary.setPremium(minTierPremium);
			pricingCalculatorStep.getTierSummary().add(tierSummary);
		}
		return premium;
	}

	/**
	 * Method calculates minimum premium value from the given two premium.
	 * 
	 * @param minPremium
	 *            Old premium value
	 * @param tierPremium
	 *            New premium value
	 * @return Minimum Value from the given two premium.
	 */
	private Double getMinValue(final Double minPremium, final Double tierPremium) {
		Double premium = null;
		if (minPremium == -1.0D) {
			premium = tierPremium;
		} else if (tierPremium <= minPremium) {
			premium = tierPremium;
		} else if (tierPremium > minPremium) {
			premium = minPremium;
		}
		return premium;
	}

	/**
	 * Method calculates maximum premium value from the given two premium.
	 * 
	 * @param maxPremium
	 *            Old premium value
	 * @param tierPremium
	 *            New premium value
	 * @return Maximum Value from the given two premium.
	 */
	private Double getMaxValue(final Double maxPremium, final Double tierPremium) {
		Double premium = null;
		if (maxPremium == 0.0) {
			premium = tierPremium;
		} else if (tierPremium >= maxPremium) {
			premium = tierPremium;
		} else {
			premium = maxPremium;
		}
		return premium;
	}

	/**
	 * Return true if target types are from {@link PricingCalculator.STATES}
	 * or {@link PricingCalculator.ADXDMA} or
	 * {@link PricingCalculator.COUNTRIES} or
	 * {@link PricingCalculator.TARGET_REDION}
	 * 
	 * @param targetTypeId
	 *            Id of a target type
	 * @param targetTypeMap
	 *            Map of target type name and id. These are the used target type
	 *            in a line item.
	 * @return true if target type belongs to {@link PricingCalculator.STATES}
	 *         or {@link PricingCalculator.ADXDMA} or
	 *         {@link PricingCalculator.COUNTRIES} or
	 *         {@link PricingCalculator.TARGET_REDION} Otherwise returns false.
	 */
	private boolean checkPremiumRestrictions(final Long targetTypeId, final Map<Long, String> targetTypeMap) {
		boolean hasAudience = false;
		if (targetTypeMap.containsKey(targetTypeId)) {
			final String audience = targetTypeMap.get(targetTypeId);
			if (StringUtils.isNotBlank(audience)
					&& (STATES.equalsIgnoreCase(audience) || ADXDMA.equalsIgnoreCase(audience) || COUNTRIES.equalsIgnoreCase(audience) || TARGET_REDION.equalsIgnoreCase(audience))) {
				hasAudience = true;
			}
		}
		return hasAudience;
	}

	/**
	 * Populates the highest level of tier value in {@link PricingCalculatorSummary}.
	 * @param tier
	 *            Value of {@link Tier} based on the target type which used in
	 *            line item.
	 * @param premium
	 *            Value of applied premium.
	 */
	private void setHighestLevelTierValue(final Tier tier, final Double premium) {
		pricingCalculatorStep.setTier(tier.getTierName());
		pricingCalculatorStep.setPremium(premium);
		pricingCalculatorStep.setLevel(String.valueOf(tier.getTierLevel()));
	}

	/**
	 * Populates {@link Tier} data in {@link TierSummary} based on the target
	 * type which used in line item.
	 * 
	 * @param tier
	 *            Value of {@link Tier} based on the target type which used in
	 *            line item.
	 * @param targetType
	 *            Name of the target type used in a line item.
	 * @return Summary of tier based on applied target type in line item.
	 */
	private TierSummary setTierValue(final Tier tier, final String targetType) {
		final TierSummary tierSummary = new TierSummary();
		tierSummary.setLevel(tier.getTierLevel());
		tierSummary.setTier(tier.getTierName());
		tierSummary.setPremium(null);
		tierSummary.setTargetType(targetType);
		return tierSummary;
	}

	/**
	 * Set those data in {@link PricingCalculatorSummary} which are used in
	 * pricing calculation.
	 * 
	 * @param weightedPrice
	 *            Final weighted base price.
	 * @param sumOfweight
	 *            Sum of all the sales target weight.
	 * @param sumOfBasePrice
	 *            Sum of multiplied values of sales target weight and base price
	 *            defined in rate profile.
	 * @param totalCount
	 *            Number of sales target taking participate in base price
	 *            calculation.
	 */
	private void setWeightedBasePriceValue(final Double weightedPrice, final Double sumOfweight, final double sumOfBasePrice, final Long totalCount) {
		pricingCalculatorStep.setSumOfWeight(sumOfweight);
		pricingCalculatorStep.setSumOfBasePrice(sumOfBasePrice);
		pricingCalculatorStep.setTotalCount(totalCount);
		pricingCalculatorStep.setWeightedPrice(weightedPrice);
	}

	/**
	 * <p>
	 * Method populates sales target name, product name and sales target id in
	 * {@link RateProfileSummary} form {@link RateProfile} and
	 * {@link RateConfig}. This method also populates applied discount and base
	 * price after discount.
	 * 
	 * <p>
	 * This method populates default or category rate profile data based on flag
	 * value.
	 * 
	 * @param rateProfileSummary
	 *            On this summary data to be populated.
	 * @param rateConfig
	 *            Contains sales target data.
	 * @param rateProfile
	 *            Contains rate profile data.
	 * @param appliedDiscount
	 *            Discount that is applicable for a time period.
	 * @param discountedBP
	 *            Base price after applying discount.
	 * @param isCategory
	 *            If flag is true populate category rate profile data otherwise
	 *            populates default rate profile data.
	 */
	private void setRateProfileData(RateProfileSummary rateProfileSummary, final RateConfig rateConfig, final RateProfile rateProfile, Double appliedDiscount, Double discountedBP, boolean isCategory) {
		/*
		 * First time this method is colled when system calculate base price
		 * form sales category and set Sales Target Id But in second time system
		 * set data for default rate category that is else part
		 */
		rateProfileSummary.setSalesTarget(rateConfig.getSalesTargetName());
		rateProfileSummary.setProductName(rateProfile.getProductName());
		rateProfileSummary.setSalesTargetId(rateConfig.getSalesTargetId());
		
		if(isCategory){
			setCategoryRateProfileData(rateProfileSummary, rateProfile, appliedDiscount, discountedBP);
		} else {
			setDefaultRateProfileData(rateProfileSummary, rateProfile, appliedDiscount, discountedBP);
		}
	}

	/**
	 * <p>Method populates base price and sales category name in
	 * {@link RateProfileSummary} form {@link RateProfile}. This method also
	 * populates applied discount and base price after discount.
	 * 
	 * <p>This method sets category rate profile data.
	 * 
	 * @param rateProfileSummary
	 *            On this summary data to be populated.
	 * @param rateProfile
	 *            From where summary data to be taken.
	 * @param appliedDiscount
	 *            Discount that is applicable for a time period.
	 * @param discountedBP
	 *            Base price after applying discount.
	 */
	private void setCategoryRateProfileData(RateProfileSummary rateProfileSummary, final RateProfile rateProfile, Double appliedDiscount, Double discountedBP) {
		rateProfileSummary.setSalesCategoryBasePrice(String.valueOf(rateProfile.getBasePrice()));
		rateProfileSummary.setSalesCategoryDiscount((appliedDiscount == null || appliedDiscount == 0.0)? null :String.valueOf(appliedDiscount));
		rateProfileSummary.setSalesCategorydiscountBP((discountedBP == null || discountedBP == 0.0)? null : String.valueOf(discountedBP));
		rateProfileSummary.setSalesCategoryName(rateProfile.getSalesCategoryName());
		if(appliedDiscount == null || appliedDiscount == 0.0){
			rateProfileSummary.setSalesCategorydiscountBP(String.valueOf(rateProfile.getBasePrice()));
		}
	}

	/**
	 * <p>Method populates base price in {@link RateProfileSummary} form
	 * {@link RateProfile}. This method also populates applied discount and base
	 * price after discount.
	 * 
	 * <p>This method sets default rate profile data.
	 * 
	 * @param rateProfileSummary
	 *            On this summary data to be populated.
	 * @param rateProfile
	 *            From where summary data to be taken.
	 * @param appliedDiscount
	 *            Discount that is applicable for a time period.
	 * @param discountedBP
	 *            Base price after applying discount.
	 */
	private void setDefaultRateProfileData(RateProfileSummary rateProfileSummary, final RateProfile rateProfile, Double appliedDiscount, Double discountedBP) {
		rateProfileSummary.setDefaultBasePrice(String.valueOf(rateProfile.getBasePrice()));
		rateProfileSummary.setDefaultDiscount((appliedDiscount == null || appliedDiscount == 0.0)? null : String.valueOf(appliedDiscount));
		rateProfileSummary.setDefaultdiscountBP((discountedBP == null || discountedBP == 0.0)? null : String.valueOf(discountedBP));
		if (rateProfileSummary.getDefaultDiscount() == null) {
			/* If base price is not available then set only base price */
			rateProfileSummary.setDefaultdiscountBP(String.valueOf(rateProfile.getBasePrice()));
		}
	}

	/**
	 * Method gets a list of {@link RateProfileSummary} from
	 * {@link PricingCalculatorSummary}. After getting
	 * {@link RateProfileSummary} form the list, method sets sales target weight
	 * for given sales target id.
	 * 
	 * @param weight
	 *            Weight of the sales target
	 * @param salesTargetId
	 *            Id of sales target
	 */
	private void setWeightOfSalesTarget(final Double weight, final Long salesTargetId) {
		List<RateProfileSummary> rateProfileLst = pricingCalculatorStep.getRateProfileSummary();
		if (rateProfileLst != null && !rateProfileLst.isEmpty()) {
			for (RateProfileSummary rateProfileJSON : rateProfileLst) {
				if (salesTargetId.equals(rateProfileJSON.getSalesTargetId())) {
					rateProfileJSON.setWeight(weight);
				}
			}
		}
	}

	/**
	 * <p>
	 * Method that can be used to serialize any Java value as a String.
	 * Functionally equivalent to calling writeValue(Writer, Object) with
	 * java.io.StringWriter and constructing String, but more efficient.
	 * <p>
	 * Creates a JSON string from {@link PricingCalculatorSummary}.
	 * {@link PricingCalculatorSummary} contains all the pricing calculation
	 * steps.
	 * 
	 * @return A JSON string. It contains contains all the pricing calculation
	 *         steps.
	 */
	private String convertToJsonObject() {
		String returnString = ConstantStrings.EMPTY_STRING;
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			returnString = objectMapper.writeValueAsString(pricingCalculatorStep);
		} catch (JsonGenerationException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while creating Json for pricing calculator");
			}
		} catch (JsonMappingException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while creating Json for pricing calculator");
			}
		} catch (IOException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while creating Json for pricing calculator");
			}
		}
		return returnString;
	}
	public void setTierService(final ITierService tierService) {
		this.tierService = tierService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setRateProfileService(final IRateProfileService rateProfileService) {
		this.rateProfileService = rateProfileService;
	}

	public void setTargetingService(final ITargetingService targetingService) {
		this.targetingService = targetingService;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}
}
