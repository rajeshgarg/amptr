package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetAmpt;
/**
 * This interface declare Sales Target Service level  methods
 * @author amandeep.singh
 *
 */
public interface ISalesTargetService {

	/**
	 * Return a list of Sales Target based on  Ids
	 * @param id
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTargetListByIDs(Long[] id);
	
	/**
	 * Return a list of Sales Target based on  Ids
	 * @param id
	 * @return List<SalesTarget>
	 */
	List<com.nyt.mpt.domain.sos.SalesTarget> getSOSSalesTargetListByIDs(Long[] id);

	/**
	 * Return a list of Sales Target based on Ids
	 * @param ids
	 * @return
	 */
	List<SalesTargetAmpt> getSalesTarget(Long[] ids);
	
	/**
	 * Save or update all sales targets
	 * @param salesTargetList
	 */
	void saveSalesTargets(List<SalesTargetAmpt> salesTargetList);
	
	/**
	 * returns List of SalesTarget from SOS
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTarget();

	/**
	 * Get salestarget list based on salestarget Type id
	 * @param salesTargetTypeIdLst
	 * @return
	 */
	List<SalesTarget> getActiveSalesTargetBySTTypeId(List<Long> salesTargetTypeIdLst);
	
	/**
	 * This is used to get the country code for regions
	 * @param regions
	 * @return
	 */
	String[] getCountryCodeByRegions(String[] regions);
	/**
	 * @param salesTargetTypeId
	 * @return
	 */
	SalesTarget getInactiveSalesTargetById(final Long salesTargetTypeId);
}
