/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.SalesTarget;


/**
 * This interface declare Sales Target related methods
 *
 * @author amandeep.singh
 *
 */
public interface ISalesTargetDAO {

	/**
	 * Return SalesTarget based on Id
	 *
	 * @param salesTargetIds
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTargetListByIDs(Long[] salesTargetIds);

	/**
	 * Returns the map of SalesTarget from SOS
	 *
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTarget();
	
	/**
	 * Return both Active and Inactive sales Targets by sales target IDs
	 *
	 * @param salesTargetIdList
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getActiveInActiveSalesTargetBySalesTargetIds(List<Long> salesTargetIdList);

	/**
	 * Get salestarget list based on salestarget Type id
	 * @param salesTargetTypeIdLst
	 * @return
	 */
	List<SalesTarget> getActiveSalesTargetBySTTypeId(List<Long> salesTargetTypeIdLst);
	
	/**
	 * This method is used to get country code for regions
	 * @param regions
	 * @return
	 */
	String[] getCountryCodeByRegions(String[] regions);

	/**
	 * This method returns the parnt and the childs of the selected salesTargetId.
	 * @param salesTargetId
	 * @return
	 */
	List<Long> getSalesTargetParentOrChild(List<Long> salesTargetId);
	
	/**
	 * @param salesTargetTypeId
	 * @return
	 */
	SalesTarget getInactiveSalesTargetById(final Long salesTargetTypeId);
}
