/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IAuditDAO;
import com.nyt.mpt.domain.Audit;
import com.nyt.mpt.util.ConstantStrings;

/**
 * @author amandeep.singh
 *
 */
public class AuditDAO extends GenericDAOImpl implements IAuditDAO {
	
	private static final Logger LOGGER = Logger.getLogger(AuditDAO.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAuditDAO#create(com.nyt.mpt.domain.Audit)
	 */
	@Override
	public Audit create(Audit audit) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving audit with id: " + audit.getId());
		}
		save(audit);
		return audit;
	}

	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IAuditDAO#getAuditsByParentId(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Audit> getAuditsByParentId(long parentId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Audit.class);
		criteria.add(Restrictions.eq("parentEntityId", parentId));
		criteria.addOrder(Order.desc("id"));
		return findByCriteria(criteria);
	}
}
