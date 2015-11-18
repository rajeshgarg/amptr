package com.nyt.mpt.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.ICreativeDAO;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.annotation.Validate;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This service class is used for Creative operation.
 * @author amandeep.singh
 * 
 */
public class CreativeService implements ICreativeService {
	private static final Logger logger = Logger.getLogger(CreativeService.class);
	private ICreativeDAO creativeDAO;
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#addCreative(com.nyt.mpt.spec.AdCreative)
	 */
	public long createCreative(Creative creative) {
		if(logger.isDebugEnabled()){
			logger.debug("Creating Creative with id:"+creative.getCreativeId());
		}
		return creativeDAO.saveCreative(creative);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#updateCreative(com.nyt.mpt.spec.AdCreative)
	 */
	@Validate
	public long updateCreative(Creative creative, boolean forecupdate) {
		if(logger.isDebugEnabled()){
			logger.debug("Updating Creative. Creative id: "+creative.getCreativeId());
		}
		Creative creativeDb = getCreative(creative.getCreativeId());
		creativeDb.setName(creative.getName());
		creativeDb.setType(creative.getType());
		creativeDb.setWidth(creative.getWidth());
		creativeDb.setHeight(creative.getHeight());
		creativeDb.setDescription(creative.getDescription());
		creativeDb.setWidth2(creative.getWidth2());
		creativeDb.setHeight2(creative.getHeight2());
		return creativeDAO.saveCreative(creativeDb);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#deleteCreative(long)
	 */
	@Validate
	public long deleteCreative(Creative creative) {
		if(logger.isDebugEnabled()){
			logger.debug("Deleting Creative. Creative id: "+creative.getCreativeId());
		}
		Creative creativeDb = getCreative(creative.getCreativeId());
		creativeDb.setActive(false);
		removeCreativeAttribute(creativeDb.getAttributeValues());
		creativeDb.setAttributeValues(null);
		return creativeDAO.saveCreative(creativeDb);
	}
	
	private void removeCreativeAttribute(Set<CreativeAttributeValue> creativeAttribute) {
		if(logger.isDebugEnabled()){
			logger.debug("Removing creative attribute. Creative Attributes: "+creativeAttribute);
		}
		creativeDAO.removeCreativeAttribute(creativeAttribute);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#getCreative(long)
	 */
	public Creative getCreative(long creativeId) {
		return creativeDAO.getCreative(creativeId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#getCreativeList(boolean)
	 */
	public List<Creative> getCreativeList(boolean active) {
		List<Creative> adCreativeLst = creativeDAO.getCreativeList(active);
		return adCreativeLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#addCreativeAttribute(long, com.nyt.mpt.spec.AdAttribute, java.lang.String)
	 */
	public long addCreativeAttribute(long creativeId, Attribute adAttribute, String attributeValue) {
		if(logger.isDebugEnabled()){
			logger.debug("Adding creative attribute. CreativeId: "+creativeId+",Attribute:"+adAttribute+",Attribute Value:"+attributeValue);
		}
		Creative creativeDb = getCreative(creativeId);
		CreativeAttributeValue creativeAttrVal 
			= new CreativeAttributeValue(creativeDb, adAttribute, true, attributeValue);
		creativeDb.getAttributeValues().add(creativeAttrVal);
		return creativeDAO.saveCreative(creativeDb);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#updateCreativeAttribute(long, com.nyt.mpt.spec.AdAttribute, java.lang.String)
	 */
	public long updateCreativeAttribute(long creativeId, Attribute attribute, String attributeValue) {
		if(logger.isDebugEnabled()){
			logger.debug("Updating creative attribute. CreativeId:"+creativeId+",Attribute:"+attribute+",Attribute Value:"+attributeValue);
		}
		Creative creativeDb = getCreative(creativeId);
		for (CreativeAttributeValue attrVal : creativeDb.getAttributeValues()) {
			if ((attrVal.getAttribute().getAttributeId() == attribute.getAttributeId())
					&& (attrVal.getCreative().getCreativeId() == creativeId)) {
				attrVal.setAttributeValue(attributeValue);
				break;
			}
		}
		return creativeDAO.saveCreative(creativeDb);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#deleteCreativeAttribute(long, com.nyt.mpt.spec.AdAttribute)
	 */
	public int deleteCreativeAttribute(long creativeId, Attribute attribute){
		if(logger.isDebugEnabled()){
			logger.debug("Deleting creative Attribute. Creative Id: "+creativeId+",Attribute:"+attribute);
		}
		Creative adCreative = getCreative(creativeId);
		CreativeAttributeValue attrToDelete = null;
		for (CreativeAttributeValue attrVal  : adCreative.getAttributeValues()) {
			if ((attrVal.getAttribute().getAttributeId() == attribute.getAttributeId())
					&& (attrVal.getCreative().getCreativeId() == creativeId)) {
				attrToDelete = attrVal;
				break;
			}
		}
		if (attrToDelete == null) {
			logger.error("invalid attribute value for delete called.");
			throw new RuntimeException("invalid attribute value for delete called.");
		}
		adCreative.getAttributeValues().remove(attrToDelete);
		creativeDAO.deleteAttributeValue(attrToDelete);
		return (int) creativeDAO.saveCreative(adCreative);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#getCreativeAttribute(long)
	 */
	@SuppressWarnings("unchecked")
	public Set<CreativeAttributeValue> getCreativeAttribute(long creativeId){
		Creative adCreative = getCreative(creativeId);
		if (adCreative != null) {
			return adCreative.getAttributeValues();
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#getFilteredCreativeList(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public List<Creative> getFilteredCreativeList(
			FilterCriteria filterCriteria, PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria) {
		return creativeDAO.getFilteredCreativeList(filterCriteria, paginationCriteria, sortingCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICreativeService#getFilteredCreativeListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredCreativeListCount(FilterCriteria filterCriteria){
		return creativeDAO.getFilteredCreativeListCount(filterCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#getFilteredCreativeAttribute(com.nyt.mpt.util.FilterCriteria, long)
	 */
	@Override
	public List<CreativeAttributeValue> getFilteredCreativeAttributeList(long creativeId, FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria) {
		return creativeDAO.getFilteredCreativeAttributeList(creativeId, filterCriteria, paginationCriteria, sortingCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICreativeService#getFilteredCreativeAttributeListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredCreativeAttributeListCount(long creativeId, FilterCriteria filterCriteria){
		return creativeDAO.getFilteredCreativeAttributeListCount(creativeId, filterCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.service.IAdCreativeService#isDuplicateCreativeName(java.lang.String)
	 */
	public boolean isDuplicateCreativeName(String creativeName, long creativeID) {
		if(logger.isDebugEnabled()){
			logger.debug("Checking for duplicate creative name. Creative Name:"+creativeName+",CreativeId:"+creativeID);
		}
		return creativeDAO.isDuplicateCreativeName(creativeName, creativeID);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICreativeService#isDuplicateCreativeAttributeAssocExist(long, long)
	 */
	@Override
	public boolean isDuplicateCreativeAttributeAssocExist(long creativeId, long attributeId) {
		if(logger.isDebugEnabled()){
			logger.debug("Checking for duplicate creative attribute association. Creative Id: "+creativeId+",AttributeID: "+attributeId);
		}
		return creativeDAO.isDuplicateCreativeAttributeAssocExist(creativeId,attributeId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICreativeService#getCreativeAttrAssocListByAttributeId(long)
	 */
	public List<CreativeAttributeValue> getCreativeAttrAssocListByAttributeId(long attributeId){
		logger.info("Search Creative Associated with AttributeId:"+attributeId);
		return creativeDAO.getCreativeAttrAssocListByAttributeId(attributeId);		
	}
	
	public void setCreativeDAO(ICreativeDAO creativeDAO) {
		this.creativeDAO = creativeDAO;
	}
}
