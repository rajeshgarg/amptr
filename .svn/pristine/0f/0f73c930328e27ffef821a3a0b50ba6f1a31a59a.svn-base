/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ITargetingDAO;
import com.nyt.mpt.domain.AudienceTarget;
import com.nyt.mpt.domain.AudienceTargetType;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author amandeep.singh
 *
 */
public class TargetingDAO extends GenericDAOImpl implements ITargetingDAO {
	
	private static final Logger LOGGER = Logger.getLogger(TargetingDAO.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getTargetTypeCriteria()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getTargetTypeCriteria() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all audience target type criteria");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTargetType.class).addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<AudienceTargetType> targetTypes = findByCriteria(criteria);
		if (targetTypes != null && !targetTypes.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(targetTypes.size());
			for (AudienceTargetType targetType : targetTypes) {
				returnMap.put(targetType.getSosAudienceTargetTypeId(), targetType.getName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getTargetTypeWithAdxNameCriteria()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getTargetTypeWithAdxNameCriteria() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all audience target type criteria");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTargetType.class).addOrder(Order.asc("yieldexName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<AudienceTargetType> targetTypes = findByCriteria(criteria);
		if (targetTypes != null && !targetTypes.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(targetTypes.size());
			for (AudienceTargetType targetType : targetTypes) {
				returnMap.put(targetType.getSosAudienceTargetTypeId(), targetType.getYieldexName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getTargetTypeElement(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getTargetTypeElement(final long targetTypeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching audiance target type for target type Id - " + targetTypeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq("audienceTargetType.sosAudienceTargetTypeId", targetTypeId));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<AudienceTarget> audienceTargets = findByCriteria(criteria);
		if (audienceTargets != null && !audienceTargets.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(audienceTargets.size());
			for (AudienceTarget audienceTarget : audienceTargets) {
				returnMap.put(audienceTarget.getSosAudienceTargetId(), audienceTarget.getName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getTargetForCountries(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getTargetForCountries(final long targetTypeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching audiance target type for target type Id - " + targetTypeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq("audienceTargetType.sosAudienceTargetTypeId", targetTypeId));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<AudienceTarget> audienceTargets = findByCriteria(criteria);
		if (audienceTargets != null && !audienceTargets.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(audienceTargets.size());
			for (AudienceTarget audienceTarget : audienceTargets) {
				returnMap.put(audienceTarget.getSosAudienceTargetId(), audienceTarget.getAdxName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getAllTargetTypeElement()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getAllTargetTypeElement() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all audience target type criteria");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTarget.class);
		final List<AudienceTarget> elementsList = findByCriteria(criteria);
		if (elementsList != null && !elementsList.isEmpty()) {
			final Map<Long, String> elementsMap = new LinkedHashMap<Long, String>(elementsList.size());
			for (AudienceTarget targetTypeElement : elementsList) {
				elementsMap.put(targetTypeElement.getSosAudienceTargetId(), targetTypeElement.getName());
			}
			return elementsMap;
		}
		return Collections.EMPTY_MAP;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getAllCountryRegionData()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<CountryRegionMap> getAllCountryRegionData() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all country - region data");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(CountryRegionMap.class);
		criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession()).setCacheable(true);
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITargetingDAO#getTargetElementNameById(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public String getTargetElementNameById(final String targetElementId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(AudienceTarget.class);
		criteria.add(Restrictions.eq("sosAudienceTargetId", Long.valueOf(targetElementId)));
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		final List<AudienceTarget> audienceTargets = findByCriteria(criteria);
		if(audienceTargets != null && !audienceTargets.isEmpty()) {
			return audienceTargets.get(0).getName();
		}
		return ConstantStrings.EMPTY_STRING;
	}
}
