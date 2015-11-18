/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IRateProfileDAO;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.service.IRateProfileService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.annotation.Validate;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This service class is used for List Rate Profile operation.
 * @author sachin.ahuja
 * 
 */
public class RateProfileService implements IRateProfileService {

	private static final Logger LOGGER = Logger.getLogger(RateProfileService.class);
	
	private IRateProfileDAO rateProfileDAO;
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#createListRateProfile(com.nyt.mpt.domain.ListRateProfile)
	 */
	@Override
	public RateProfile createRateProfile(RateProfile rateProfile) {
		return rateProfileDAO.saveRateProfile(rateProfile);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#getListRateProfile(long)
	 */
	public RateProfile getRateProfileById(long rateProfileId) {
		return rateProfileDAO.getRateProfile(rateProfileId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#updateListRateProfile(com.nyt.mpt.domain.ListRateProfile, boolean)
	 */
	@Validate
	public RateProfile updateRateProfile(RateProfile rateProfile, boolean forceUpdate) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Updating list rate profile: Id = " + rateProfile.getProfileId());
		}
		RateProfile rateProfileDb = getRateProfileById(rateProfile.getProfileId());
		
		rateProfileDb.setSalesCategoryId(rateProfile.getSalesCategoryId());
		rateProfileDb.setSalesCategoryName(rateProfile.getSalesCategoryName());
		rateProfileDb.setProductId(rateProfile.getProductId());
		rateProfileDb.setProductName(rateProfile.getProductName());
		
		// Clear the sales target association and put new list
		final Set<RateConfig> listRateConfig = rateProfileDb.getRateConfigSet();
		for (RateConfig listRateConfigAssoc : listRateConfig) {
			rateProfileDAO.deleteRateConfig(listRateConfigAssoc);
		}
		rateProfileDb.setRateConfigSet(rateProfile.getRateConfigSet());
		
		rateProfileDb.setBasePrice(rateProfile.getBasePrice());
		rateProfileDb.setNotes(rateProfile.getNotes());		
		rateProfileDb.setVersion(rateProfile.getVersion());
		rateProfileDb.setSectionNames(rateProfile.getSectionNames());
		rateProfileDb.setRateCardNotRounded(rateProfile.isRateCardNotRounded());
		
		final List<RateProfileSeasonalDiscounts> discountsLst = rateProfileDb.getSeasonalDiscountsLst();
		for (RateProfileSeasonalDiscounts rateProfileSeasonalDiscounts : discountsLst) {
			rateProfileDAO.deleteSeasonalDiscount(rateProfileSeasonalDiscounts);
		}
		rateProfileDb.setSeasonalDiscountsLst(rateProfile.getSeasonalDiscountsLst());
		return rateProfileDAO.saveRateProfile(rateProfileDb);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#deleteListRateProfile(com.nyt.mpt.domain.ListRateProfile)
	 */
	@Validate
	public long deleteRateProfile(RateProfile rateProfile){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Deleting List Rate Profile: Id = " + rateProfile.getProfileId());
		}
		RateProfile rateProfileDb = getRateProfileById(rateProfile.getProfileId());
		rateProfileDb.setActive(false);
		return rateProfileDAO.saveRateProfile(rateProfileDb).getProfileId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#createRateProfileCloneByID(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void createCloneRateProfileForSalesCategory(final Long salesCategoryId, final Long targetSalesCategoryId, final String targetSalesCategoryName) {
		final List<RateProfile> targetRateProfiles = rateProfileDAO.getRateProfilesBySalesCategory(targetSalesCategoryId);
		for (RateProfile rateProfile : targetRateProfiles) {
			rateProfile.setActive(false);
			rateProfileDAO.saveRateProfile(rateProfile);
		}
		
		final List<RateProfile> rateProfileLst = rateProfileDAO.getRateProfilesBySalesCategory(salesCategoryId);
		for (RateProfile rateProfileDb : rateProfileLst) {
			final RateProfile rateProfile = new RateProfile();
			rateProfile.setSalesCategoryId(targetSalesCategoryId);
			rateProfile.setSalesCategoryName(targetSalesCategoryName);
			rateProfile.setProductId(rateProfileDb.getProductId());
			rateProfile.setProductName(rateProfileDb.getProductName());
			
			final Set<RateConfig> listRateConfig = rateProfileDb.getRateConfigSet();
			for (RateConfig rateConfigDb : listRateConfig) {
				final RateConfig rateConfig = new RateConfig();
				rateConfig.setSalesTargetId(rateConfigDb.getSalesTargetId());
				rateConfig.setSalesTargetName(rateConfigDb.getSalesTargetName());
				rateProfile.addListRateConfig(rateConfig);
			}
			rateProfile.setBasePrice(rateProfileDb.getBasePrice());
			rateProfile.setNotes(rateProfileDb.getNotes());		
			rateProfile.setVersion(0);
			rateProfile.setSectionNames(rateProfileDb.getSectionNames());
			final List<RateProfileSeasonalDiscounts> discountsLst = rateProfileDb.getSeasonalDiscountsLst();
			for (RateProfileSeasonalDiscounts discount : discountsLst) {
				final RateProfileSeasonalDiscounts seasonalDiscount = new RateProfileSeasonalDiscounts();
				seasonalDiscount.setDiscount(discount.getDiscount());
				seasonalDiscount.setStartDate(discount.getStartDate());
				seasonalDiscount.setEndDate(discount.getEndDate());
				seasonalDiscount.setNot(discount.isNot());
				seasonalDiscount.setRateProfile(rateProfile);
				rateProfile.addListRateDiscount(seasonalDiscount);
			}
			rateProfileDAO.saveRateProfile(rateProfile);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#getRateProfilesBySalesCategory(java.lang.Long)
	 */
	@Override
	public List<RateProfile> getRateProfilesBySalesCategory(final Long salesCategoryId){
		return rateProfileDAO.getRateProfilesBySalesCategory(salesCategoryId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#getFilteredListRateProfileList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	public List<RateProfile> getFilteredRateProfileList(List<FilterCriteria> filterCriteria, 
			PaginationCriteria pgCriteria, SortingCriteria sortingCriteria) {
		return rateProfileDAO.getFilteredRateProfileList(filterCriteria, pgCriteria, sortingCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IListRateProfileService#getFilteredListRateProfileListCount(java.util.List)
	 */
	public int getFilteredRateProfileListCount(List<FilterCriteria> filterCriteria) {
		return rateProfileDAO.getFilteredRateProfileCount(filterCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#isDuplicateRateProfile(com.nyt.mpt.domain.RateProfile, java.util.List)
	 */
	@Override
	public boolean isDuplicateRateProfile(final RateProfile rateProfile, final Long[] salesTargetIds) {
		return rateProfileDAO.isDuplicateRateProfile(rateProfile, salesTargetIds);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#getProfileSeasonalDiscountLst(long)
	 */
	@Override
	public List<RateProfileSeasonalDiscounts> getProfileSeasonalDiscountLst(long rateProfileId ){
		return rateProfileDAO.getProfileSeasonalDiscountLst(rateProfileId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#getRateProfilesBySalesCategoryAndProduct(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<RateProfile> getRateProfilesBySalesCategoryAndProduct(final Long salesCategoryId , final Long productId){
		return rateProfileDAO.getRateProfilesBySalesCategoryAndProduct(salesCategoryId, productId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#createCopySeasonalDiscounts(java.lang.Long[], java.lang.Long[])
	 */
	@Override
	public void createCopySeasonalDiscounts(final Long[] discountIds , final Long[] targetProfileIds){
		List<RateProfileSeasonalDiscounts> discountList = rateProfileDAO.getSeasonalDiscountByIds(discountIds);
		List<RateProfile> targetProfileLst = rateProfileDAO.getRateProfileLstByIds(targetProfileIds);
		for (RateProfile rateProfile : targetProfileLst) {
			for (RateProfileSeasonalDiscounts rateProfileSeasonalDiscounts : rateProfile.getSeasonalDiscountsLst()) {
				rateProfileDAO.deleteSeasonalDiscount(rateProfileSeasonalDiscounts);
			}
			for (RateProfileSeasonalDiscounts discounts : discountList) {
				final RateProfileSeasonalDiscounts seasonalDiscount = new RateProfileSeasonalDiscounts();
				seasonalDiscount.setDiscount(discounts.getDiscount());
				seasonalDiscount.setStartDate(discounts.getStartDate());
				seasonalDiscount.setEndDate(discounts.getEndDate());
				seasonalDiscount.setNot(discounts.isNot());
				seasonalDiscount.setRateProfile(rateProfile);
				rateProfile.addListRateDiscount(seasonalDiscount);
			}
			rateProfileDAO.saveRateProfile(rateProfile);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IRateProfileService#getRateProfileLstByIds(java.lang.Long[])
	 
	@Override
	public List<RateProfile> getRateProfileLstByIds(final Long[] profileIdLst){
		return rateProfileDAO.getRateProfileLstByIds(profileIdLst);
	}*/
	
	public void setRateProfileDAO(IRateProfileDAO rateProfileDAO) {
		this.rateProfileDAO = rateProfileDAO;
	}
}