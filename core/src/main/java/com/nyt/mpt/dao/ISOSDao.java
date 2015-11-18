/**
 *
 */
package com.nyt.mpt.dao;

import java.util.Map;

/**
 * This interface declare SOS Audience Target related methods
 *
 * @author amandeep.singh
 *
 */
public interface ISOSDao {

	/**
	 * Return a Map of sosSalesCategoryId and salesCategory name.
	 *
	 * @return Map<Long, String>
	 */

	Map<Long, String> getSalesCategories();

	/**
	 * Return a Map of salesCategory name and sosSalesCategoryId.
	 * @return Map<String, Long>
	 */
	Map<String, Long> getSOSSalesCategories();
	
	/**
	 * Return Sales Category based on salesCategoryId
	 *
	 * @param salesCategoryId
	 * @return Map<Long, String>
	 */
	Map<Long, String> getSalesCategoryById(long salesCategoryId);

}
