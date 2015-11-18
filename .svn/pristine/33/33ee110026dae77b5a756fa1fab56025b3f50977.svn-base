/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.List;
import java.util.Set;
import com.nyt.mpt.dao.IMultipleCalendarDao;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.service.IMultipleCalendarService;

/**
 * This <code>MultipleCalendarService</code> includes all the methods to get the Multiple Calendar's related data
 * 
 * @author shipra.bansal
 */

public class MultipleCalendarService implements IMultipleCalendarService {

	private IMultipleCalendarDao multipleCalendarDao;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IMultipleCalendarService#getUserFiltersByType(java.lang.String, java.util.Set)
	 */
	@Override
	public List<UserFilter> getUserFiltersByType(final String filterType, final Set<Long> userIds) {
		return multipleCalendarDao.getUserFiltersByType(filterType, userIds);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IMultipleCalendarService#isDuplicateFilterName(java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean isDuplicateFilterName(final String filterName, final Long filterId,final long userId) {
		return multipleCalendarDao.isDuplicateName(filterName, filterId,userId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IMultipleCalendarService#saveFilter(com.nyt.mpt.domain.UserFilter)
	 */
	@Override
	public UserFilter saveFilter(final UserFilter filter) {
		if (filter.getFilterId() > 0) {
			UserFilter filterDb = getFiltersById(filter.getFilterId());
			filterDb.setCreatedBy(filter.getCreatedBy());
			filterDb.setCreatedDate(filter.getCreatedDate());
			filterDb.setFilterData(filter.getFilterData());
			filterDb.setFilterId(filter.getFilterId());
			filterDb.setFilterName(filter.getFilterName());
			filterDb.setModifiedBy(filter.getModifiedBy());
			filterDb.setModifiedDate(filter.getModifiedDate());
			filterDb.setFilterType(filter.getFilterType());
			filterDb.setUserId(filter.getUserId());
			filterDb.setVersion(filter.getVersion());
			return multipleCalendarDao.saveFilter(filterDb);
		}
		return multipleCalendarDao.saveFilter(filter);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IMultipleCalendarService#getFiltersById(long)
	 */
	public UserFilter getFiltersById(final long filterCriteriaId) {
		return multipleCalendarDao.getFilterById(filterCriteriaId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IMultipleCalendarService#deleteFilter(long)
	 */
	@Override
	public void deleteFilter(final long filterId) {
		multipleCalendarDao.deleteFilter(filterId);
	}

	public void setMultipleCalendarDao(IMultipleCalendarDao multipleCalendarDao) {
		this.multipleCalendarDao = multipleCalendarDao;
	}

}
