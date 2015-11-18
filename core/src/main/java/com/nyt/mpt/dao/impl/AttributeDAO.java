/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IAttributeDAO;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for Attribute related operation
 *
 * @author surendra.singh
 *
 */
@SuppressWarnings(ConstantStrings.UNCHECKED)
public class AttributeDAO extends GenericDAOImpl implements IAttributeDAO {

	private static final String ATTRIBUTE_NAME = "attributeName";

	private static final String ATTRIBUTE_ID = "attributeId";

	private static final Logger LOGGER = Logger.getLogger(AttributeDAO.class);

	/**
	 * Map for attribute field form and entity map
	 */
	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_COLUMN_MAP.put(ATTRIBUTE_ID, ATTRIBUTE_ID);
		FIELD_COLUMN_MAP.put(ATTRIBUTE_NAME, ATTRIBUTE_NAME);
		FIELD_COLUMN_MAP.put("attributeDescription", "attributeDescription");
		FIELD_COLUMN_MAP.put("attributeKey", "attributeKey");
		FIELD_COLUMN_MAP.put("attributeTypeStr", "attributeType");
		FIELD_COLUMN_MAP.put("attributeValue", "attributeOptionalValue");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdAttributeDAO#addAttribute(com.nyt.mpt.spec.AdAttribute)
	 */
	public Attribute saveAttribute(final Attribute adAttribute) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving attribute with Id: " + adAttribute.getAttributeId());
		}
		saveOrUpdate(adAttribute);
		return adAttribute;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdAttributeDAO#getAttributeList(boolean)
	 */
	public List<Attribute> getAttributeList(final boolean active, final AttributeType attrType) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching active attribute list with AttributeType: " + attrType.getDisplayName());
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Attribute.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, active));
		criteria.add(Restrictions.eq("attributeType", attrType.name()));
		criteria.addOrder(Order.asc(ATTRIBUTE_NAME).ignoreCase());
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdAttributeDAO#getAttribute(long)
	 */
	public Attribute getAttribute(final long attributeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching attribute with AttributeId: " + attributeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Attribute.class);
		criteria.add(Restrictions.eq(ATTRIBUTE_ID, attributeId));
		return (Attribute) findByCriteria(criteria).get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAttributeDAO#isDuplicateAttributeName(java.lang.String, long, java.lang.String)
	 */
	@Override
	public boolean isDuplicateAttributeName(final String attributeName, final long attributeId, final String attributeType) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Checking Duplicate Attribute Name. AttributeName: " + attributeName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Attribute.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq("attributeType", attributeType));
		if (attributeId == 0) {
			criteria.add(Restrictions.eq(ATTRIBUTE_NAME, attributeName).ignoreCase());
		} else {
			criteria.add(Restrictions.ne(ATTRIBUTE_ID, attributeId));
			criteria.add(Restrictions.eq(ATTRIBUTE_NAME, attributeName).ignoreCase());
		}
		final List<Attribute> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Attribute count with attribute Name: '" + attributeName + "' is: " + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAttributeDAO#getFilteredAttributeList(com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<Attribute> getFilteredAttributeList(final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered attribute list with filterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = constructFilterCriteriaForAttribute(filterCriteria);
		
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pageCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAttributeDAO#getFilteredAttributeListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredAttributeListCount(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered attribute list count with filterCriteria: " + filterCriteria);
		}
		return getCount(constructFilterCriteriaForAttribute(filterCriteria));
	}

	/**
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForAttribute(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for attribute. Filter Criteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Attribute.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.ANYWHERE));
			} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.START));
			} else {
				criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.EXACT));
			}
		}
		return criteria;
	}
}
