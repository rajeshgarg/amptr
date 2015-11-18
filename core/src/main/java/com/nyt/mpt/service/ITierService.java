package com.nyt.mpt.service;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

public interface ITierService {

	/**
	 * Save tier information
	 * @param tier
	 * @return
	 */
	Tier saveTier(final Tier tier);

	/**
	 * Return list of tier based on filter, pagination, sorting criteria
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return
	 */
	List<Tier> getFilteredTierList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria);

	/**
	 * Return count of tier
	 * @param filterCriteria
	 * @return
	 */
	int getFilteredTierCount(final FilterCriteria filterCriteria);

	/**
	 * Delete tier
	 * @param tierId
	 * @return
	 */
	Long deleteTier(final long tierId);

	/**
	 * Check duplicate active tier Name
	 * Returns true if Name is duplicate
	 * @param tierName
	 * @param tierID
	 * @return
	 */
	boolean isDuplicateTierName(final String tierName, final Long tierID);

	/**
	 * Check duplicate active tier level
	 * Returns true if Level is duplicate
	 * @param tierName
	 * @param tierID
	 * @return
	 */
	boolean isDuplicateTierLevel(final long tierLevel, final Long tierID);

	/**
	 * Return list of premium based on filter, pagination, sorting criteria
	 * @param tierId
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return
	 */
	List<TierPremium> getFilteredTierPremiumList(Long tierId, final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria);


	/**
	 * Return count of premium
	 * @param tierId
	 * @param filterCriteria
	 * @return
	 */
	int getFilteredTierPremiumCount(final long tierId, final FilterCriteria filterCriteria);

	/**
	 * Save tier information
	 * @param premium
	 * @param tierId
	 * @return
	 */
	Tier saveTierPremium(TierPremium premium, long tierId);

	/**
	 * Delete premium
	 * @param premium
	 * @return
	 */
	Long deleteTierPremium(final long premiumId);

	/**
	 * Check the duplicacy of Target Type elements in a premium Object
	 * @param premiumTierId
	 * @param targetId
	 * @param id
	 * @return
	 */
	boolean isDuplicatePremiumElement(TierPremium tierPremium , StringBuilder duplicateElementsStr);

	/**
	 * To clone the premiums from the source tier to the target tier
	 * @param sourceTierId
	 * @param targetTierId
	 * @param targetTierName
	 */
	void createClonePremiumForTier(final long sourceTierId, final long targetTierId);
	
	/**
	 * Returns the tier object with the given tier Id
	 * @param tierId
	 * @return
	 */
	public Tier getTierById(final long tierId);
	
	/**
	 * Returns the premium object with the given premium Id
	 * @param tierId
	 * @return
	 */
	public TierPremium getPremiumById(final long premiumId);
	
	/**
	 * Returns the list of premiums of a tier with the given tierId
	 * @param tierId
	 * @return
	 */
	public List<TierPremium> getPremiumListByTierId(long tierId);

	/**
	 * Return list of Tier based on section Ids
	 * @param tokenSet
	 * @return
	 */
	List<Tier> getTierSectionAssocList(final Set<Long> sectionIDSet);

}
