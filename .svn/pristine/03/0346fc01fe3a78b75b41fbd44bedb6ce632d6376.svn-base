/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Map;

import com.nyt.mpt.domain.CountryRegionMap;

/**
 * @author amandeep.singh
 *
 */
public interface ITargetingDAO {
	
	/**
	 * Return a Map of sosAudienceTargetTypeId and audienceTargetType name.
	 *
	 * @return Map<Long,String>
	 */
	Map<Long, String> getTargetTypeCriteria();
	
	/**
	 * This method is used to fetch target type with adx name
	 *
	 * @return Map<Long, String>
	 */
	Map<Long, String> getTargetTypeWithAdxNameCriteria();
	
	/**
	 * Return a Map of sosAudienceTargetId and audienceTarget name.
	 *
	 * @param criteriaId
	 * @return Map<Long,String>
	 */

	Map<Long, String> getTargetTypeElement(long criteriaId);

	/**
	 * This method is used to fetch target type for countries
	 *
	 * @param criteriaId
	 * @return
	 */
	Map<Long, String> getTargetForCountries(long criteriaId);
	
	/**
	 * Return All Target Type Elements
	 *
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
