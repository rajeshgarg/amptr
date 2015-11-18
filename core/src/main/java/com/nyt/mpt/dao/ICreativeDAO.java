/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Creative related methods
 *
 * @author surendra.singh
 *
 */
public interface ICreativeDAO {

	/**
	 * Update existing creative to database
	 *
	 * @param adCreative
	 * @return long
	 */
	long saveCreative(Creative creative);

	/**
	 * Return the creative for given creative Id.
	 *
	 * @param creativeId
	 * @return Creative
	 */
	Creative getCreative(long creativeId);

	/**
	 * Return list of creative based on creative status (active/inactive)
	 *
	 * @param active
	 * @return List<Creative>
	 */
	List<Creative> getCreativeList(boolean active);

	/**
	 * Return list of creative based on Search Parameters
	 *
	 * @param filterCriteria
	 * @return List<Creative>
	 */
	List<Creative> getFilteredCreativeList(FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortCriteria);

	/**
	 * Return total creative count for given filter criteria
	 *
	 * @param searchGridParams
	 * @return int
	 */
	int getFilteredCreativeListCount(FilterCriteria filterCriteria);

	/**
	 * Get filtered creative attribute list
	 *
	 * @param searchGridParams
	 * @param creativeId
	 * @return List<CreativeAttributeValue>
	 */
	List<CreativeAttributeValue> getFilteredCreativeAttributeList(long creativeId, FilterCriteria filterCriteria,
			PaginationCriteria pageCriteria, SortingCriteria sortCriteria);

	/**
	 * Return creative attribute count for filter criteria
	 *
	 * @param searchGridParams
	 * @return int
	 */
	int getFilteredCreativeAttributeListCount(long creativeId, FilterCriteria filterCriteria);

	/**
	 * Check duplicate creative name
	 *
	 * @param creativeName
	 * @return boolean
	 */
	boolean isDuplicateCreativeName(String creativeName, long creativeID);

	/**
	 * Delete creative attribute
	 *
	 * @param value
	 *            attribute value to delete
	 * @return void
	 */
	void deleteAttributeValue(CreativeAttributeValue value);

	/**
	 * Remove creative attribute association
	 *
	 * @param creativeAttribute
	 * @return boolean
	 */
	boolean removeCreativeAttribute(Set<CreativeAttributeValue> creativeAttribute);

	/**
	 * Check for creative attribute association
	 *
	 * @param creativeId
	 * @param attributeId
	 * @return boolean
	 */
	boolean isDuplicateCreativeAttributeAssocExist(long creativeId, long attributeId);

	/**
	 * Find creative name value and ID associated with attributeId
	 *
	 * @param attributeId
	 * @return List<CreativeAttributeValue>
	 */
	List<CreativeAttributeValue> getCreativeAttrAssocListByAttributeId(long attributeId);
}
