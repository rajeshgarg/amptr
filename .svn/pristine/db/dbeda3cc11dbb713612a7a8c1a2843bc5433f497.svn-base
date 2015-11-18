/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ITemplateDAO;
import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateMetaDataAttributes;
import com.nyt.mpt.domain.TemplateSheetMetaData;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for Template related operation
 * @author manish.kesarwani
 */
public class TemplateDAO extends GenericDAOImpl implements ITemplateDAO {

	private static final String TEMPLATE_NAME = "templateName";

	private static final Logger LOGGER = Logger.getLogger(TemplateDAO.class);

	private static final Map<String, String> FIELD_DB_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_DB_COLUMN_MAP.put("templateId", "templateId");
		FIELD_DB_COLUMN_MAP.put(TEMPLATE_NAME, TEMPLATE_NAME);
		FIELD_DB_COLUMN_MAP.put("templateFileName", "templateFileName");
		FIELD_DB_COLUMN_MAP.put("updatedBy", "modifiedBy");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getMediaPlanTemplates()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<TemplateMetaData> getActiveMediaPlanTemplates() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching MediaPlan Templates");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.ne(TEMPLATE_NAME, ConstantStrings.CREATIVE_SPEC_TEMPALTE));
		criteria.addOrder(Order.asc(TEMPLATE_NAME));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getMediaPlanTemplate(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public TemplateMetaData getActiveMediaPlanTemplateById(final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching MediaPlan Template. TemplateId: " + templateId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq("id", templateId));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.setFetchMode("templateSheetList", FetchMode.JOIN);
		final List<TemplateMetaData> mediaTemplateList = findByCriteria(criteria);
		if (mediaTemplateList == null || mediaTemplateList.isEmpty()) {
			return null;
		} else {
			final TemplateMetaData tempalteMetaData = mediaTemplateList.get(0);
			for (TemplateSheetMetaData sheetMetaData : tempalteMetaData.getTemplateSheetList()) {
				final List<TemplateMetaDataAttributes> metaDataAttrList = sheetMetaData.getMediaPlanAttributes();
				for (TemplateMetaDataAttributes metaDataAttribute : metaDataAttrList) {
					Hibernate.initialize(metaDataAttribute);
				}
			}
			return tempalteMetaData;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getCreativeSpecTemplateMetaData(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public TemplateMetaData getMediaPlanTemplateById(final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching MediaPlan Template. TemplateId: " + templateId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq("id", templateId));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.setFetchMode("templateSheetList", FetchMode.JOIN);
		final List<TemplateMetaData> mediaTemplateList = findByCriteria(criteria);
		if (mediaTemplateList == null || mediaTemplateList.isEmpty()) {
			return null;
		} else {
			final TemplateMetaData tempalteMetaData = mediaTemplateList.get(0);
			for (TemplateSheetMetaData sheetMetaData : tempalteMetaData.getTemplateSheetList()) {
				final List<TemplateMetaDataAttributes> metaDataAttrList = sheetMetaData.getMediaPlanAttributes();
				for (TemplateMetaDataAttributes metaDataAttribute : metaDataAttrList) {
					Hibernate.initialize(metaDataAttribute);
				}
			}
			return tempalteMetaData;
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getMediaPlanTemplateByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public TemplateMetaData getMediaPlanTemplateByName(final String templateName) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq(TEMPLATE_NAME, templateName));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		
		final List<TemplateMetaData> mediaTemplateList = findByCriteria(criteria);

		if (mediaTemplateList == null || mediaTemplateList.isEmpty()) {
			return null;
		} else {
			for (TemplateMetaData templateMetaData : mediaTemplateList) {
				final List<TemplateSheetMetaData> templateSheetList = templateMetaData.getTemplateSheetList();
				for (TemplateSheetMetaData templateSheetMetaData : templateSheetList) {
					Hibernate.initialize(templateSheetMetaData);
					final List<TemplateMetaDataAttributes> metaDataAttrList = templateSheetMetaData.getMediaPlanAttributes();
					for (TemplateMetaDataAttributes metaDataAttribute : metaDataAttrList) {
						Hibernate.initialize(metaDataAttribute);
					}
				}
			}
			return mediaTemplateList.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getMediaPlanTemplates(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<TemplateMetaData> getMediaPlanTemplates(final FilterCriteria filterCriteria, final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching MediaPlan Templates");
		}
		final DetachedCriteria criteria = constructFilterCriteria(filterCriteria);

		if(sortingCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortingCriteria.setSortingField(FIELD_DB_COLUMN_MAP.get(sortingCriteria.getSortingField()));
			addSortingCriteria(criteria, sortingCriteria);
		}
		
		final List<TemplateMetaData> mediaTemplateList = findByCriteria(criteria, pgCriteria);
		if (mediaTemplateList != null && !mediaTemplateList.isEmpty()) {
			for (TemplateMetaData templateMetaData : mediaTemplateList) {
				Hibernate.initialize(templateMetaData.getTemplateSheetList());
			}
		} 
		return mediaTemplateList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getFilteredCreativeListCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getMediaPlanTemplatesCount(final FilterCriteria filterCriteria){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("fetching filtered creative list count. Filtercriteria: "+filterCriteria);
		}
		return getCount(constructFilterCriteria(filterCriteria));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProposalHeadDisplayName()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getProposalHeadDisplayName() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Data");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHead.class);
		criteria.addOrder(Order.asc("displayName"));
		final List<ProposalHead> proposalHeadLst = findByCriteria(criteria);
		if (proposalHeadLst != null && !proposalHeadLst.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(proposalHeadLst.size());
			for (ProposalHead proposalHead : proposalHeadLst) {
				returnMap.put(proposalHead.getId(), proposalHead.getDisplayName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;

	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProposalHeadAttributes()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalHeadAttributes> getProposalHeadAttributes() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Attributes");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHeadAttributes.class);
		criteria.addOrder(Order.asc("attributeName"));
		final List<ProposalHeadAttributes> headAttribute = findByCriteria(criteria);
		if (headAttribute != null && !headAttribute.isEmpty()) {
			for (ProposalHeadAttributes proposalHeadAttributes : headAttribute) {
				Hibernate.initialize(proposalHeadAttributes.getProposalHead());
			}
			return headAttribute;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProposalHeadAttributes(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getProposalHeadAttributes(final Long headID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Attributes");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHeadAttributes.class);
		criteria.add(Restrictions.eq("proposalHead.id", headID));
		criteria.addOrder(Order.asc("displayAttributeName"));
		final List<ProposalHeadAttributes> headAttribute = findByCriteria(criteria);
		if (headAttribute != null && !headAttribute.isEmpty()) {
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(headAttribute.size());
			for (ProposalHeadAttributes propoAttribute : headAttribute) {
				returnMap.put(propoAttribute.getId(), propoAttribute.getDisplayAttributeName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProposalHeadList()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalHead> getProposalHeadList() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Data");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHead.class);
		criteria.addOrder(Order.asc("headName"));
		final List<ProposalHead> proposalHead = findByCriteria(criteria);
		for (ProposalHead proposalhead : proposalHead) {
			Hibernate.initialize(proposalhead.getProposalHeadAttributes());
		}
		return proposalHead;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProHeadListByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalHead> getProHeadListByName(final String headName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching ProposalHead by head name" + headName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHead.class);
		criteria.createAlias("proposalHeadAttributes", "proposalHeadAttributes");
		criteria.add(Restrictions.eq("headName", headName));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#saveCustomTemplate(com.nyt.mpt.domain.TemplateMetaData)
	 */
	@Override
	public TemplateMetaData saveCustomTemplate(final TemplateMetaData templateMetaData) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save template meta data");
		}
		saveOrUpdate(templateMetaData);
		return templateMetaData;
	}

	/**
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteria(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing FilterCriteria. filterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.ne(TEMPLATE_NAME, ConstantStrings.CREATIVE_SPEC_TEMPALTE));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField()) && StringUtils.isNotBlank(filterCriteria.getSearchString())) {
			if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(FIELD_DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString().trim(), MatchMode.ANYWHERE));
			} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(FIELD_DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString().trim(), MatchMode.START));
			} else {
				criteria.add(Restrictions.ilike(FIELD_DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString().trim(), MatchMode.EXACT));
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#saveHeadAttributes(com.nyt.mpt.domain.ProposalHeadAttributes)
	 */
	@Override
	public ProposalHeadAttributes saveHeadAttributes(final ProposalHeadAttributes headAttributes) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Save head attributes data");
		}
		saveOrUpdate(headAttributes);
		return headAttributes;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getHeadAttributesByParameter(java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalHeadAttributes> getHeadAttributesByParameter(final String attributeName, final String headName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Attributes");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHeadAttributes.class);
		criteria.add(Restrictions.eq("displayAttributeName", attributeName));
		criteria.createAlias("proposalHead", "proposalHead");
		criteria.add(Restrictions.eq("proposalHead.headName", headName));
		final List<ProposalHeadAttributes> headAttribute = findByCriteria(criteria);
		if (headAttribute != null && !headAttribute.isEmpty()) {
			for (ProposalHeadAttributes proposalHeadAttributes : headAttribute) {
				Hibernate.initialize(proposalHeadAttributes.getProposalHead());
			}
			return headAttribute;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#deleteTemplateAttribute(com.nyt.mpt.domain.TemplateMetaDataAttributes)
	 */
	@Override
	public void deleteTemplateAttribute(final TemplateMetaDataAttributes templateAttribute) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting template attribute objects");
		}
		delete(templateAttribute);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getProposalHeadAttributes(java.util.Set)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProposalHeadAttributes> getProposalHeadAttributes(final Set<String> tokenSet) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Return list of ProposalHeadAttributes based on tokenSet");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProposalHeadAttributes.class);
		criteria.add(Restrictions.in("autoConfigKey", tokenSet));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#deleteCustomTemplate(com.nyt.mpt.domain.TemplateMetaData)
	 */
	@Override
	public void deleteCustomTemplate(final TemplateMetaData customTemplateDb) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Delete custom template for id: " + customTemplateDb.getTemplateId());
		}
		delete(customTemplateDb);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITemplateDAO#getTemplateIdByName(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Long getTemplateIdByName(final String templateName) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(TemplateMetaData.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq("templateName", templateName));
		criteria.setProjection(Projections.property("templateId"));
		List<Long> dataArray = findByCriteria(criteria);
		if (dataArray != null && !dataArray.isEmpty()) {
			return dataArray.get(0);
		}
		return null;
	}
}
