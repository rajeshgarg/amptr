/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ISalesTargetDAO;
import com.nyt.mpt.domain.Country;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.util.ConstantStrings;

/**
 *
 * This DAO level class is used for Sales Target related operation
 *
 * @author surendra.singh
 *
 */
public class SalesTargetDAO extends GenericDAOImpl implements ISalesTargetDAO {

	private static final String SALES_TARGET_ID = "salesTargetId";

	private static final String ST_DISPLAY_NAME = "salesTargeDisplayName";

	private static final Logger LOGGER = Logger.getLogger(SalesTargetDAO.class);

	private static final Map<String, String> DB_COLUMN_MAP = new HashMap<String, String>();

	static {
		DB_COLUMN_MAP.put(SALES_TARGET_ID, SALES_TARGET_ID);
		DB_COLUMN_MAP.put("salesTargetName", "salesTargetName");
		DB_COLUMN_MAP.put(ST_DISPLAY_NAME, ST_DISPLAY_NAME);
		DB_COLUMN_MAP.put(ConstantStrings.STATUS, ConstantStrings.STATUS);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getSalesTargetListByIDs(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTargetListByIDs(final Long[] ids) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Sales Target List by Ids. Ids: " + ids);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.setFetchMode("salesTargetType", FetchMode.JOIN);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.in(SALES_TARGET_ID, ids));
		criteria.addOrder(Order.asc(ST_DISPLAY_NAME).ignoreCase());
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getSalesTarget()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTarget() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of all active sales targets.");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.addOrder(Order.asc(ST_DISPLAY_NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getActiveInActiveSalesTargetBySalesTargetIds(java.util.List)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getActiveInActiveSalesTargetBySalesTargetIds(final List<Long> salesTargetIdList) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.in(SALES_TARGET_ID, salesTargetIdList));
		criteria.addOrder(Order.asc(ST_DISPLAY_NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getActiveSalesTargetBySTTypeId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getActiveSalesTargetBySTTypeId(final List<Long> salesTargetTypeIdLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of active sales targets by salesTargetTypeId ");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.createAlias("salesTargetType", "salesTargetType");
		criteria.add(Restrictions.in("salesTargetType.salestargetTypeId", salesTargetTypeIdLst));
		criteria.addOrder(Order.asc(ST_DISPLAY_NAME).ignoreCase());
		return findByCriteria(criteria);
	}
	
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public String[] getCountryCodeByRegions(String[] regions) {
		String[] countryCodeArray = null;
		final DetachedCriteria criteria = DetachedCriteria.forClass(Country.class);
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		
		Disjunction or = Restrictions.disjunction();
		for (String region : regions) {
			or.add(Restrictions.ilike("region", region));
		}
		criteria.add(or);
		List<Country> countryList = findByCriteria(criteria);
		if (countryList != null && !countryList.isEmpty()) {
			countryCodeArray = new String[countryList.size()];
			int i = 0;
			for (Country country : countryList) {
				countryCodeArray[i++] = country.getCode();
			}
		}
		return countryCodeArray;
	}
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getSalesTargetParentOrChild(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Long> getSalesTargetParentOrChild(final List<Long> salesTargetIds) {
		final List<Long> salesTargetIdList = new ArrayList<Long>();
		salesTargetIdList.addAll(salesTargetIds);
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.in(SALES_TARGET_ID, salesTargetIds));
		
		List<SalesTarget> parentSalesTargets = findByCriteria(criteria);
		for (SalesTarget parentSalesTarget : parentSalesTargets) {
			if(parentSalesTarget !=null && parentSalesTarget.getParentSalesTargetId() !=null && parentSalesTarget.getParentSalesTargetId()>0){
				salesTargetIdList.add(parentSalesTarget.getParentSalesTargetId());
			}
		}
		final DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesTarget.class);
		detachedCriteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		detachedCriteria.add(Restrictions.in("parentSalesTargetId", salesTargetIds));
		final List<SalesTarget> salesTargetList = findByCriteria(detachedCriteria);
		for (SalesTarget salesTarget : salesTargetList) {
			salesTargetIdList.add(salesTarget.getSalesTargetId());
		}
		return salesTargetIdList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAO#getInactiveSalesTargetById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public SalesTarget getInactiveSalesTargetById(final Long salesTargetTypeId) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all inactive or deleted Sales targets from sos");
		}
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query query = session.createSQLQuery("select * from {h-schema}SALES_TARGET where DELETE_DATE is not null or STATUS = 'Inactive' and SOS_SALES_TARGET_ID = " + salesTargetTypeId).addEntity(SalesTarget.class);
		final List<SalesTarget> salesTargetlist = query.list();
		if(salesTargetlist != null && !salesTargetlist.isEmpty()){
			return (SalesTarget) query.list().get(0);
		}
		return null;		
	}
}
