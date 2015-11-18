/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Attribute related methods
 *
 * @author surendra.singh
 *
 */
public interface IAttributeDAO {

	/**
	 * Add a new Attribute to database
	 *
	 * @param adAttribute
	 * @return Attribute
	 */
	Attribute saveAttribute(Attribute adAttribute);

	/**
	 * Return a List of all Attribute as per AttributeType
	 *
	 * @param active
	 * @param attrType
	 * @return List
	 */
	List<Attribute> getAttributeList(boolean active, AttributeType attrType);

	/**
	 * Return the Attribute for given Id
	 *
	 * @param attributeId
	 * @return Attribute
	 */
	Attribute getAttribute(long attributeId);

	/**
	 * Check attribute name is already exist in system
	 *
	 * @param attributeName
	 * @param attributeId
	 * @param attributeType
	 * @return boolean
	 */
	boolean isDuplicateAttributeName(String attributeName, long attributeId, String attributeType);

	/**
	 * Return list of all attributes in system as per filter, paging and sorting criteria.
	 *
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<Attribute>
	 */
	List<Attribute> getFilteredAttributeList(FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortCriteria);

	/**
	 * Return total number of attribute in system as per filter criteria
	 *
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredAttributeListCount(FilterCriteria filterCriteria);
}
