/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.List;
import java.util.Map;

import com.nyt.mpt.dao.ITargetingDAO;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.service.ITargetingService;

/**
 * @author amandeep.singh
 *
 */
public class TargetingService implements ITargetingService {
	
	private ITargetingDAO targetingDao;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getAllTargetTypeElement()
	 */
	@Override
	public Map<Long, String> getAllTargetTypeElement() {
		return targetingDao.getAllTargetTypeElement();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getTargetForCountries(long)
	 */
	@Override
	public Map<Long, String> getTargetForCountries(long targetTypeCriteriaID) {
		return targetingDao.getTargetForCountries(targetTypeCriteriaID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getTargetTypeCriteria()
	 */
	@Override
	public Map<Long, String> getTargetTypeCriteria() {
		return targetingDao.getTargetTypeCriteria();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getTargetTypeElement(long)
	 */
	@Override
	public Map<Long, String> getTargetTypeElement(long targetTypeCriteriaID) {
		return targetingDao.getTargetTypeElement(targetTypeCriteriaID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getTargetTypeWithAdxNameCriteria()
	 */
	@Override
	public Map<Long, String> getTargetTypeWithAdxNameCriteria() {
		return targetingDao.getTargetTypeWithAdxNameCriteria();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getAllCountryRegionData()
	 */
	@Override
	public List<CountryRegionMap> getAllCountryRegionData() {
		return targetingDao.getAllCountryRegionData();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITargetingService#getTargetElementNameById(java.lang.String)
	 */
	@Override
	public String getTargetElementNameById(final String targetElementId) {
		return targetingDao.getTargetElementNameById(targetElementId);
	}
	
	public void setTargetingDao(ITargetingDAO targetingDao) {
		this.targetingDao = targetingDao;
	}
}
