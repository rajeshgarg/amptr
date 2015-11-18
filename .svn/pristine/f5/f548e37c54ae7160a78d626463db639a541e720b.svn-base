package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Attribute Service level methods
 * @author amandeep.singh
 * 
 */
public interface IAttributeService {

	/**
	 * Create new attribute in system
	 * @param bean
	 * @return Attribute
	 */
	Attribute createAttribute(Attribute bean);
	
	/**
	 * Update existing attribute
	 * @param attribute
	 * @param forceUpdate
	 * @return Attribute
	 */
	Attribute updateAttribute(Attribute attribute, boolean forceUpdate);

	/**
	 * Delete attribute in database
	 * @param attribute
	 * @return Attribute
	 */
	Attribute deleteAttribute(Attribute attribute);
	
	/**
	 * Return list of all attributes for given type.
	 * @param active
	 * @param attrType
	 * @return List<Attribute>
	 */
	List<Attribute> getAttributeList(boolean active, AttributeType attrType);

	/**
	 * Return attribute for given Id
	 * @param attributeId
	 * @return Attribute
	 */
	Attribute getAttribute(long attributeId);

	/**
	 * Check attribute name is already exist in system
	 * @param attributeName
	 * @param attributeId
	 * @param attributeType
	 * @return boolean
	 */
	boolean isDuplicateAttributeName(String attributeName, long attributeId, String attributeType);

	/**
	 * Return list of all attributes in system as per filter, paging and sorting criteria.
	 * @param filterCriteria
	 * @param paginationCriteria
	 * @param sortingCriteria
	 * @return List<Attribute>
	 */
	List<Attribute> getFilteredAttributeList(FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);

	/**
	 * Return total number of attribute in system as per filter criteria
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredAttributeListCount(FilterCriteria filterCriteria);
}
