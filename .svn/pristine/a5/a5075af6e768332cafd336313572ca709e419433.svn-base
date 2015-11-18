/**
 * 
 */
package com.nyt.mpt.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import com.nyt.mpt.dao.ISosIntegrationDao;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.domain.sos.SosNotes;

/**
 * @author amandeep.singh
 *
 */
public class SosIntegrationDao extends GenericDAOImpl implements ISosIntegrationDao {
	
	private static final Logger LOGGER = Logger.getLogger(SosIntegrationDao.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISosIntegrationDao#saveOrder(com.nyt.mpt.domain.sos.SalesOrder)
	 */
	@Override
	public Long saveOrder(SalesOrder order) {
		saveOrUpdate(order);
		return order.getSalesOrderId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISosIntegrationDao#getLineItemId()
	 */
	@Override
	public Long getLineItemId() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Getting lineItem Id through Sequence S_LINEITEM_ID");
		}
		final String queryString = "Select S_LINEITEM_ID.nextval AS LINEITEMID from DUAL";
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final SQLQuery query = session.createSQLQuery(queryString);
		query.addScalar("LINEITEMID", StandardBasicTypes.LONG);
		return (Long) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISosIntegrationDao#saveNotes(com.nyt.mpt.domain.sos.SosNotes)
	 */
	@Override
	public void saveNotes(SosNotes notes) {
		save(notes);		
	}
}
