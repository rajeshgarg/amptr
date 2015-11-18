/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ISalesTargetDAOSOS;
import com.nyt.mpt.domain.sos.SalesTarget;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author amandeep.singh
 *
 */
public class SalesTargetDAOSOS extends GenericDAOImpl implements ISalesTargetDAOSOS {
	
	private static final String SALES_TARGET_ID = "salesTargetId";

	private static final String ST_DISPLAY_NAME = "salesTargeDisplayName";

	private static final Logger LOGGER = Logger.getLogger(SalesTargetDAOSOS.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesTargetDAOSOS#getSalesTargetListByIDs(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTargetListByIDs(Long[] ids) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Sales Target List by Ids. Ids: " + ids);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.setFetchMode("salesTargetType", FetchMode.JOIN);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.in(SALES_TARGET_ID, ids));
		criteria.addOrder(Order.asc(ST_DISPLAY_NAME).ignoreCase());
		return findByCriteria(criteria);
	}

}
