/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ISalesTargetAmptDAO;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This DAO level interface is used for Sales Target related operation and which
 * MP_Sales_Target data is fetched by corn job
 * 
 * @author rakesh.tewari
 */
public class SalesTargetAmptDAO extends GenericDAOImpl implements ISalesTargetAmptDAO {

	private static final Logger LOGGER = Logger.getLogger(SalesTargetAmpt.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetAmptDAO#getSalesTarget(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTargetAmpt> getSalesTarget(Long[] ids) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Sales Target List by Ids. Ids: " + ids);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTargetAmpt.class).addOrder(Order.asc("salesTargeDisplayName"));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.in("salesTargetId", ids));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetAmptDAO#saveSalesTargets(java.util.List)
	 */
	@Override
	public void saveSalesTargets(List<SalesTargetAmpt> salesTargetList) {
		if(salesTargetList != null && !salesTargetList.isEmpty()){
			for (SalesTargetAmpt salesTargetAmpt : salesTargetList) {
				saveOrUpdate(salesTargetAmpt);
			}
		}
	}
}
