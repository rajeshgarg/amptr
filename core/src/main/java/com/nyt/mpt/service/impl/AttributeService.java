package com.nyt.mpt.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IAttributeDAO;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.annotation.Validate;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This service class is used for Attribute operation.
 * @author amandeep.singh
 * 
 */
public class AttributeService implements IAttributeService {

	private static final Logger logger = Logger.getLogger(AttributeService.class);
	private IAttributeDAO attributeDAO;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAttributeService#createAttribute(com.nyt.mpt.domain.Attribute)
	 */
	@Override
	public Attribute createAttribute(Attribute attribute) {
		return attributeDAO.saveAttribute(attribute);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAttributeService#addAttribute(com.nyt.mpt.spec.AdAttribute)
	 */
	@Validate
	public Attribute updateAttribute(Attribute attribute, boolean forceUpdate) {
		if(logger.isDebugEnabled()){
			logger.debug("Updating Attribute: Id="+attribute.getAttributeId());
		}
		Attribute attributeDb = getAttribute(attribute.getAttributeId());
		//attributeDb.setAttributeType(attribute.getAttributeType());
		attributeDb.setAttributeName(attribute.getAttributeName());
		attributeDb.setAttributeKey(attribute.getAttributeKey());
		attributeDb.setAttributeDescription(attribute.getAttributeDescription());
		attributeDb.setAttributeOptionalValue(attribute.getAttributeOptionalValue());
		attributeDb.setVersion(attribute.getVersion());
		return attributeDAO.saveAttribute(attributeDb);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAttributeService#deleteAttribute(com.nyt.mpt.domain.Attribute)
	 */
	@Validate
	public Attribute deleteAttribute(Attribute attribute){
		if(logger.isDebugEnabled()){
			logger.debug("Deleting Attribute: Id="+attribute.getAttributeId());
		}
		Attribute attributeDb = getAttribute(attribute.getAttributeId());
		attributeDb.setActive(false);
		return attributeDAO.saveAttribute(attributeDb);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAttributeService#getAttributeList(boolean)
	 */
	public List<Attribute> getAttributeList(boolean active, AttributeType attrType) {
		return attributeDAO.getAttributeList(active, attrType);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAttributeService#getAttribute(long)
	 */
	public Attribute getAttribute(long attributeId) {
		return attributeDAO.getAttribute(attributeId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAttributeService#isDuplicateAttributeName(java.lang.String, long, java.lang.String)
	 */
	public boolean isDuplicateAttributeName(String attributeName, long attributeId , String attributeType) {
		if(logger.isDebugEnabled()){
			logger.debug("Checking for duplicate attribute name.Attribute name: "+attributeName+" AttributeId: "+attributeId);
		}
		return attributeDAO.isDuplicateAttributeName(attributeName, attributeId, attributeType);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAttributeService#getFilteredAttributeList(com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	public List<Attribute> getFilteredAttributeList(FilterCriteria filterCriteria, 
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria) {
		return attributeDAO.getFilteredAttributeList(filterCriteria, paginationCriteria, sortingCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAttributeService#getFilteredAttributeListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	public int getFilteredAttributeListCount(FilterCriteria filterCriteria) {
		return attributeDAO.getFilteredAttributeListCount(filterCriteria);
	}

	public void setAttributeDAO(IAttributeDAO attributeDAO) {
		this.attributeDAO = attributeDAO;
	}
}