/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.nyt.mpt.dao.ICreativeDAO;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for Creative related operation
 *
 * @author amandeep.singh
 *
 */
public class CreativeDAO extends GenericDAOImpl implements ICreativeDAO {

	private static final String ATTRIBUTE = "attribute";

	private static final String CREATIVE = "creative";

	private static final String CREATIVE_ID = "creativeId";

	private static final Logger LOGGER = Logger.getLogger(CreativeDAO.class);

	private static final String CREATIVE_WITH_ATTRIBUTE_VALUES = "from Creative ac left join fetch ac.attributeValues where ac.creativeId = :creativeId";

	/**
	 * Map for creative form and entity mapping
	 */
	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_COLUMN_MAP.put(CREATIVE_ID, CREATIVE_ID);
		FIELD_COLUMN_MAP.put(ConstantStrings.NAME, ConstantStrings.NAME);
		FIELD_COLUMN_MAP.put("description", "description");
		FIELD_COLUMN_MAP.put("typeStr", "type");
		FIELD_COLUMN_MAP.put("width", "width");
		FIELD_COLUMN_MAP.put("height", "height");
		FIELD_COLUMN_MAP.put("width2", "width2");
		FIELD_COLUMN_MAP.put("height2", "height2");
	}

	/**
	 * Creative attribute form to entity mapping
	 */
	private static final Map<String, String> CREATIVE_ATTR_MAP = new HashMap<String, String>();

	static {
		CREATIVE_ATTR_MAP.put("attributeId", "attribute.attributeId");
		CREATIVE_ATTR_MAP.put("attributeName", "attribute.attributeName");
		CREATIVE_ATTR_MAP.put("attributeDescription", "attribute.attributeDescription");
		CREATIVE_ATTR_MAP.put("attributeKey", "attribute.attributeKey");
		CREATIVE_ATTR_MAP.put("attributeValue", "attributeValue");
		CREATIVE_ATTR_MAP.put("createdDate", "createdDate");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#AddCreative(com.nyt.mpt.spec.AdCreative)
	 */
	public long saveCreative(final Creative creative) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving creative with id: " + creative.getCreativeId());
		}
		saveOrUpdate(creative);
		return creative.getCreativeId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#getCreative(long)
	 */
	public Creative getCreative(final long creativeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching creative with id: " + creativeId);
		}
		return getHibernateTemplate().execute(new HibernateCallback<Creative>() {
			@Override
			public Creative doInHibernate(Session session) {
				return (Creative) session.createQuery(CREATIVE_WITH_ATTRIBUTE_VALUES).setLong(CREATIVE_ID, creativeId).uniqueResult();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#getCreativeList(boolean)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Creative> getCreativeList(final boolean active) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching active creative list");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Creative.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, active));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#getFilteredCreativeList(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Creative> getFilteredCreativeList(final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered creative list. FilterCriteria:" + filterCriteria);
		}
		DetachedCriteria criteria = null;
		try {
			 criteria = constructFilterCriteriaForCreative(filterCriteria);
		} catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		
		return findByCriteria(criteria, pageCriteria);
	}

	/**
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForCreative(final FilterCriteria filterCriteria){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for creative. FilterCriteria:" + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Creative.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if ("width".equals(filterCriteria.getSearchField()) || "height".equals(filterCriteria.getSearchField())
					|| "width2".equals(filterCriteria.getSearchField()) || "height2".equals(filterCriteria.getSearchField())) {
				try {
					if(StringUtils.isNotBlank(filterCriteria.getSearchString())) {
						criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
								Integer.valueOf(filterCriteria.getSearchString())));
					}
				} catch (NumberFormatException ex) {
					LOGGER.info("Invalid search input for " + FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()) + " - "
							+ filterCriteria.getSearchString());
					throw ex;
				}
			} else {
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
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#getFilteredCreativeListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredCreativeListCount(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("fetching filtered creative list count. Filtercriteria: " + filterCriteria);
		}
		try {
			return getCount(constructFilterCriteriaForCreative(filterCriteria));
		} catch (NumberFormatException ex) {
			return 0;
		}		
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#getFilteredCreativeAttribute(com.nyt.mpt.util.FilterCriteria, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<CreativeAttributeValue> getFilteredCreativeAttributeList(final long creativeId, final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("fetching filtered creative attribute list. FilterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = constructFilterCriteriaForCreativeAttribute(filterCriteria, creativeId);
		
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(CREATIVE_ATTR_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pageCriteria);
	}

	/**
	 * @param filterCriteria
	 * @param creativeId
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForCreativeAttribute(final FilterCriteria filterCriteria, final long creativeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filtered criteria for creative attribute. FilterCriteria: " + filterCriteria + " Creative id: " + creativeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(CreativeAttributeValue.class);
		criteria.createAlias(CREATIVE, CREATIVE);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq("creative.creativeId", creativeId));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#getFilteredCreativeAttributeListCount(long, com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredCreativeAttributeListCount(final long creativeId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered creative attribute list count. FilterCriteria: " + filterCriteria + " Creative id: " + creativeId);
		}
		return getCount(constructFilterCriteriaForCreativeAttribute(filterCriteria, creativeId));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdCreativeDAO#isDuplicateCreativeName(java.lang.String)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public boolean isDuplicateCreativeName(final String creativeName, final long creativeID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for Duplicate Creative Name. CreativeName: " + creativeName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Creative.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (creativeID == 0) {
			criteria.add(Restrictions.eq(ConstantStrings.NAME, creativeName).ignoreCase());
		} else {
			criteria.add(Restrictions.ne(CREATIVE_ID, creativeID));
			criteria.add(Restrictions.eq(ConstantStrings.NAME, creativeName).ignoreCase());
		}
		final List<Creative> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Count for creative name '" + creativeName + "' is: " + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#deleteAttributeValue(com.nyt.mpt.domain.CreativeAttributeValue)
	 */
	@Override
	public void deleteAttributeValue(CreativeAttributeValue value) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Attribute Value. CreativeAttributeValue: " + value);
		}
		delete(value);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#removeCreativeAttribute(java.util.Set)
	 */
	@Override
	public boolean removeCreativeAttribute(final Set<CreativeAttributeValue> attributeList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Remove creative Attributes. creativeAttributeList:" + attributeList);
		}
		getHibernateTemplate().deleteAll(attributeList);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#isDuplicateCreativeAttributeAssocExist(long, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public boolean isDuplicateCreativeAttributeAssocExist(final long creativeId, final long attributeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check whether Duplicate Creative Attribute Association Exists for CreativeID: "
					+ creativeId + " attributeId: "	+ attributeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(CreativeAttributeValue.class);
		criteria.createAlias(CREATIVE, CREATIVE);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq("creative.creativeId", creativeId));
		criteria.add(Restrictions.eq("attribute.attributeId", attributeId));
		final List<CreativeAttributeValue> list = findByCriteria(criteria);
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICreativeDAO#getCreativeAttrAssocListByAttributeId(long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<CreativeAttributeValue> getCreativeAttrAssocListByAttributeId(final long attributeId) {
		LOGGER.info("Search Creative which Associated with AttributeId: " + attributeId);
		final DetachedCriteria criteria = DetachedCriteria.forClass(CreativeAttributeValue.class);
		criteria.createAlias(CREATIVE, CREATIVE);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq("attribute.attributeId", attributeId));
		return findByCriteria(criteria);
	}
}
