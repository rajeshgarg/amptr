/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare rate profile related methods
 *
 * @author sachin.ahuja
 *
 */
public interface IRateProfileDAO {

	/**
	 * Return list of all rate profiles in system as per filter, paging and sorting criteria.
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<ListRateProfile>
	 */
	List<RateProfile> getFilteredRateProfileList(List<FilterCriteria> filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortCriteria);
	
	/**
	 * Return total number of Rate Profile in system as per filter criteria
	 * @param filterCriteria
	 * @return int
	 */
	public int getFilteredRateProfileCount(List<FilterCriteria> filterCriteria);
	
	/**
	 * Add a new Rate Profile to database
	 * @param rateProfile
	 * @return long
	 */
	public RateProfile saveRateProfile(RateProfile rateProfile);
	
	/**
	 * Return the Rate Profile for given Id
	 * @param rateProfileId
	 * @return ListRateProfile
	 */
	public RateProfile getRateProfile(long rateProfileId);

	/**
	 * Delete associated Sales Target for the selected Product
	 * @param rateConfig
	 */
	public void deleteRateConfig(RateConfig rateConfig);

	/**
	 * Check Product and Sales Target combination for the selected Sales Category already exist
	 * @param salesCategoryId
	 * @param productId
	 * @param salesTargetIds
	 * @return
	 */
	boolean isDuplicateRateProfile(RateProfile rateProfile, Long[] salesTargetIds);

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
	 * Deletes a seasonal discount
	 * @param listRateDiscount
	 */
	public void deleteSeasonalDiscount(RateProfileSeasonalDiscounts listRateDiscount);
	
	/**
	 * 
	 * @param salesCategoryId
	 * @param productId
	 * @return
	 */
	public List<RateProfile> getRateProfilesBySalesCategoryAndProduct(final Long salesCategoryId , final Long productId);
	
	/**
	 * Returns the seasonal discount with teh given id.
	 * @param discountIdLst
	 * @return
	 */
	public List<RateProfileSeasonalDiscounts> getSeasonalDiscountByIds(Long[] discountIdLst );
	
	/**
	 * returns the list of rate profiles with the given list if Id's.
	 * @param profileIdLst
	 * @return
	 */
	public List<RateProfile> getRateProfileLstByIds(final Long[] profileIdLst);
}
