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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ISOSDao;
import com.nyt.mpt.domain.SOSSalesCategory;
import com.nyt.mpt.util.ConstantStrings;

/**
 *
 * This DAO level class is used for sos related operation
 *
 * @author amandeep.singh
 *
 */
public class SOSDao extends GenericDAOImpl implements ISOSDao {

	private static final Logger LOGGER = Logger.getLogger(SOSDao.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISOSDao#getSalesCategories()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getSalesCategories() {
		final String hqlQuery = "SELECT salesCategory from SOSSalesCategory salesCategory, AMPTDisplayLists ADL"
				+ " where salesCategory.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND salesCategory.status = :status ORDER BY LOWER(salesCategory.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.SALES_CATEGORY);
		hibernateQuery.setString(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS);
		hibernateQuery.setCacheable(true);
		final List<SOSSalesCategory> salesCategories = (List<SOSSalesCategory>) hibernateQuery.list();

		if (salesCategories != null && !salesCategories.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(salesCategories.size());
			for (SOSSalesCategory salesCategory : salesCategories) {
				returnMap.put(salesCategory.getSosSalesCategoryId(), salesCategory.getName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISOSDao#getSalesCategories()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<String, Long> getSOSSalesCategories() {
		final String hqlQuery = "SELECT salesCategory from SOSSalesCategory salesCategory, AMPTDisplayLists ADL"
				+ " where salesCategory.id = ADL.sourceId AND ADL.displayFlag = :displayFlag AND ADL.sourceTable = :sourceTable"
				+ " AND salesCategory.status = :status ORDER BY LOWER(salesCategory.name)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setString("displayFlag", ConstantStrings.YES);
		hibernateQuery.setString("sourceTable", ConstantStrings.SALES_CATEGORY);
		hibernateQuery.setString(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS);
		hibernateQuery.setCacheable(true);
		final List<SOSSalesCategory> salesCategories = (List<SOSSalesCategory>) hibernateQuery.list();

		if (salesCategories != null && !salesCategories.isEmpty()) {
			final Map<String, Long> returnMap = new LinkedHashMap<String, Long>(salesCategories.size());
			for (SOSSalesCategory salesCategory : salesCategories) {
				returnMap.put(salesCategory.getName(), salesCategory.getSosSalesCategoryId());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISOSDao#getSalesCategoryById(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getSalesCategoryById(long salesCategoryId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all sales categories for sales category id - " + salesCategoryId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SOSSalesCategory.class);
		criteria.add(Restrictions.eq("sosSalesCategoryId", salesCategoryId));
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.addOrder(Order.asc(ConstantStrings.NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<SOSSalesCategory> salesCategories = findByCriteria(criteria);
		if (salesCategories != null && !salesCategories.isEmpty()) {
			final Map<Long, String> returnMap = new LinkedHashMap<Long, String>(salesCategories.size());
			for (SOSSalesCategory salesCategory : salesCategories) {
				returnMap.put(salesCategory.getSosSalesCategoryId(), salesCategory.getName());
			}
			return returnMap;
		}
		return Collections.EMPTY_MAP;
	}
}
