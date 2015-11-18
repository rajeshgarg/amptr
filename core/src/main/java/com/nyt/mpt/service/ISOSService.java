/**
 * 
 */
package com.nyt.mpt.service;

import java.util.Map;

/**
 * This interface declare SOS Service level  methods
 * @author amandeep.singh
 *
 */
public interface ISOSService {
		
	/**
	 * Return a map of SosSalesCategoryId and  salesCategory getName.
	 * @return Map<Long,String>
	 */
	Map<Long,String> getSalesCategories();
	/**
	 * Return a map of salesCategory getName and SosSalesCategoryId.
	 * @return Map<String, Long>
	 */
	Map<String, Long> getSOSSalesCategories();
	
	/**
	 * Return Sales Category based on salesCategoryId
	 * @param salesCategoryId
	 * @return Map<Long,String>
	 */
	Map<Long, String> getSalesCategoryById(long salesCategoryId);
}
