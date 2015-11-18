package com.nyt.mpt.service;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Creative Service level methods
 * @author amandeep.singh
 * 
 */
public interface ICreativeService {

	/**
	 * Add a new creative to the database
	 * @param adCreative
	 * @return long
	 */
	long createCreative(Creative adCreative);
	
	/**
	 * Update a existing creative.
	 * @param adCreative
	 * @param forceUpdate
	 * @return long
	 */
	long updateCreative(Creative adCreative, boolean forceUpdate);
	
	/**
	 * Load the creative from database.
	 * @param creativeId
	 * @return Creative
	 */
	Creative getCreative(long creativeId);

	/**
	 * Delete creative from database
	 * @param creative
	 * @return long
	 */
	long deleteCreative(Creative creative);

	/**
	 * Return list of creative based on status (active/inactive)
	 * @param active
	 * @return List<Creative>
	 */
	List<Creative> getCreativeList(boolean active);

	/**
	 * Add a new attribute to the creative with given attribute value.
	 * @param creativeId
	 * @param adAttribute
	 * @param attributeValue
	 * @return long
	 */
	long addCreativeAttribute(long creativeId, Attribute adAttribute, String attributeValue);

	/**
	 * Update attribute value for given creative 
	 * @param creativeId
	 * @param adAttribute
	 * @param attributeValue
	 * @return long
	 */
	long updateCreativeAttribute(long creativeId, Attribute adAttribute, String attributeValue);
	
	/**
	 * Delete attribute for given creative
	 * @param creativeId
	 * @param adAttribute
	 * @return int
	 */
	int deleteCreativeAttribute(long creativeId, Attribute adAttribute);
	
	/**
	 * Return the list of attribute for given creative
	 * @param creativeId
	 * @return Set<CreativeAttributeValue>
	 */
	Set<CreativeAttributeValue> getCreativeAttribute(long creativeId);
	
	/**
	 * Return list of creative based on search parameters
	 * @param filterCriteria
	 * @param paginationCriteria
	 * @param sortingCriteria
	 * @return List<Creative>
	 */
	List<Creative> getFilteredCreativeList(FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);
	
	/**
	 * Return total creative count for given filter criteria
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredCreativeListCount(FilterCriteria filterCriteria);
	
	
	/**
	 * get filtered creative attribute list
	 * @param creativeId
	 * @param filterCriteria
	 * @param paginationCriteria
	 * @param sortingCriteria
	 * @return List<CreativeAttributeValue>
	 */
	List<CreativeAttributeValue>getFilteredCreativeAttributeList(long creativeId, FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);
	
	/**
	 * return creative attribute count for filter criteria
	 * @param creativeId
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredCreativeAttributeListCount(long creativeId, FilterCriteria filterCriteria);
	
	/**
	 * check duplicate creative name
	 * @param creativeName
	 * @param cretiveID
	 * @return boolean
	 */
	boolean isDuplicateCreativeName(String creativeName, long cretiveID );
	
	/**
	 * Check for creative attribute association 
	 * @param creativeId
	 * @param attributeId
	 * @return boolean
	 */
	boolean isDuplicateCreativeAttributeAssocExist(long creativeId, long attributeId );

	/**
	 * Find creative name value and ID associated with attributeId    
	 * @param attributeId
	 * @return List<CreativeAttributeValue>
	 */
	List<CreativeAttributeValue> getCreativeAttrAssocListByAttributeId(long attributeId);
}
