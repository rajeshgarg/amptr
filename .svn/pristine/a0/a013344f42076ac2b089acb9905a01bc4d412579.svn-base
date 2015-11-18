/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;
import java.util.Map;

import com.nyt.mpt.domain.CountryRegionMap;

/**
 * @author amandeep.singh
 *
 */
public interface ITargetingService {
	/**
	 * Return a map of SosAudienceTargetTypeId and  audienceTargetType Name. 
	 * @return Map<Long,String>
	 */
	Map<Long,String> getTargetTypeCriteria();
	
	/**
	 * This method is used to fetch target type with adx name
	 * @return Map<Long, String>
	 */
	Map<Long, String> getTargetTypeWithAdxNameCriteria();
	
	/**
	 * Return a map of SosAudienceTargetId and  audienceTarget Name based on targetTypeCriteriaID. 
	 * @param targetTypeCriteriaID
	 * @return Map<Long,String>
	 */
	Map<Long,String> getTargetTypeElement(long targetTypeCriteriaID);
	
	/**
	 * This method is used to fetch target type for countries
	 * @param targetTypeCriteriaID
	 * @return
	 */
	Map<Long, String> getTargetForCountries(long targetTypeCriteriaID);
	
	/**
	 * Return All Target Type Elements
	 * @return Map<Long, String>
	 */
	Map<Long, String> getAllTargetTypeElement();

	/**
	 * @return
	 */
	List<CountryRegionMap> getAllCountryRegionData();

	/**
	 * Get target element name by Id
	 * @param targetElementId
	 * @return
	 */
	String getTargetElementNameById(String targetElementId);	
}
