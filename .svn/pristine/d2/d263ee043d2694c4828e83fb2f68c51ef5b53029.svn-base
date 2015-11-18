/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IMultipleCalendarDao;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;

/**
 * This <code>MultipleCalendarDao</code> includes all the methods to get the Multiple Calendar's related data
 * 
 * @author shipra.bansa;
 */

public class MultipleCalendarDao extends GenericDAOImpl implements IMultipleCalendarDao {

	private static final Logger LOGGER = Logger.getLogger(MultipleCalendarDao.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IMultipleCalendarDao#getallFilters(java.lang.String, java.util.Set)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<UserFilter> getUserFiltersByType(final String filterType, final Set<Long> userIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Filters for the filterType:" + filterType + " and for the users having the ids:"+userIds);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(UserFilter.class).addOrder(Order.asc("filterName").ignoreCase());
		criteria.add(Restrictions.eq("filterType", filterType));
		criteria.add(Restrictions.in("userId", userIds));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IMultipleCalendarDao#deleteFilter(long)
	 */
	@Override
	public void deleteFilter(final long filterId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Filters for the filter id" + filterId);
		}
		final UserFilter filter = load(UserFilter.class, filterId);
		delete(filter);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IMultipleCalendarDao#getFiltersById(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public UserFilter getFilterById(final long filterCriteriaId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getting filtter by id" + filterCriteriaId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(UserFilter.class);
		criteria.add(Restrictions.eq("filterId", filterCriteriaId));
		final List<UserFilter> filtersLst = findByCriteria(criteria);
		return filtersLst.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IMultipleCalendarDao#saveFilter(com.nyt.mpt.domain.DataFilters)
	 */
	@Override
	public UserFilter saveFilter(final UserFilter filter) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saving a filter");
		}
		saveOrUpdate(filter);
		return filter;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IMultipleCalendarDao#findByFiltername(java.lang.String, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean isDuplicateName(final String filterName, final Long filterId, final long userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("finding if duplicate filter name exist for multiple calendar with name -" + filterName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(UserFilter.class);
		if (filterId > 0) {
			criteria.add(Restrictions.ne("filterId", filterId));
		}
		criteria.add(Restrictions.eq("userId", userId));
		criteria.add(Restrictions.eq("filterName", filterName).ignoreCase());
		criteria.add(Restrictions.eq("filterType", UserFilterTypeEnum.MULTIPLE_CALENDAR.name()));
		final List<UserFilter> list = findByCriteria(criteria);
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}
}
