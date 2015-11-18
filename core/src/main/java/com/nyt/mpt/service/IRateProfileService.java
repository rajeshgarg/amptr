/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare List Rate Profile Service level methods
 * @author sachin.ahuja
 * 
 */
public interface IRateProfileService {
		
	/**
	 * Return total number of Rate Profile in system as per filter criteria
	 * @param filterCriteria
	 * @return int
	 */
	public int getFilteredRateProfileListCount(List<FilterCriteria> filterCriteria);
	
	/**
	 * Return list of all rate profiles in system as per filter, paging and sorting criteria.
	 * @param filterCriteria
	 * @param pgCriteria
	 * @param sortCriteria
	 * @return List<ListRateProfile>
	 */
	public List<RateProfile> getFilteredRateProfileList(List<FilterCriteria> filterCriteria, 
			PaginationCriteria pgCriteria, SortingCriteria sortCriteria);
	
	/**
	 * Create new Rate Profile in system
	 * @param rateProfile
	 * @return long
	 */
	public RateProfile createRateProfile(RateProfile rateProfile);
	
	/**
	 * Return the Rate Profile for given Id
	 * @param rateProfileId
	 * @return ListRateProfile
	 */
	public RateProfile getRateProfileById(long rateProfileId);
	
	/**
	 * Update existing Rate Profile
	 * @param rateProfile
	 * @param forceUpdate
	 * @return long
	 */
	public RateProfile updateRateProfile(RateProfile rateProfile, boolean forceUpdate);

	/**
	 * Delete Rate Profile in database
	 * @param rateProfile
	 * @return long
	 */
	public long deleteRateProfile(RateProfile rateProfile);
	
	/**
	 * Check Product and Sales Target combination for the selected Sales Category already exist
	 * @param salesCategoryId
	 * @param productId
	 * @param salesTargetIds
	 * @return
	 */
	boolean isDuplicateRateProfile(RateProfile rateProfile, Long[] salesTargetIds);

	/**
	 * Copy Rate Profile(s) from source Sales Category to target Sales Category
	 * @param salesCategoryId
	 * @param targetSalesCategoryId
	 * @param targetSalesCategoryName
	 */
	void createCloneRateProfileForSalesCategory(Long salesCategoryId, Long targetSalesCategoryId, String targetSalesCategoryName);

	/**
	 * Return Rate Profiles for given Sales Category Id
	 * @param salesCategoryId
	 * @return
	 */
	List<RateProfile> getRateProfilesBySalesCategory(Long salesCategoryId);
	
	/**
	 * Returns the list of all the seasonal discounts of a given List Rate Profile.
	 * @param rateProfileId
	 * @return
	 */
	List<RateProfileSeasonalDiscounts> getProfileSeasonalDiscountLst(long rateProfileId);
	
	/**
	 * Returns the list of rate profiles for given sales category and product.
	 * @param salesCategoryId
	 * @param productId
	 * @return
	 */
	public List<RateProfile> getRateProfilesBySalesCategoryAndProduct(final Long salesCategoryId , final Long productId);
	
	
	/**
	 * Creates the clone of given discount ids into the target profile ids.
	 * @param discountIds
	 * @param targetProfileIds
	 */
	public void createCopySeasonalDiscounts(final Long[] discountIds , final Long[] targetProfileIds);
	
	/**
	 * returns the list of rate profiles with the given list if Id's.
	 * @param profileIdLst
	 * @return
	 *//*
	public List<RateProfile> getRateProfileLstByIds(final Long[] profileIdLst);*/
}
