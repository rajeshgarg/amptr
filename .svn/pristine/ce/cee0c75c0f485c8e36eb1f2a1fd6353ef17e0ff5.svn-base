/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import com.nyt.mpt.dao.IRoleDAO;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.util.ConstantStrings;

/**
 *
 * This DAO level class is used for user role related operation
 *
 * @author surendra.singh
 *
 */
public class RoleDAO extends GenericDAOImpl implements IRoleDAO {

	private static final Logger LOGGER = Logger.getLogger(RoleDAO.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.security.dao.IUsertDAO#createUser(com.nyt.mpt.security.User)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Role> getAllAssignableRole() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Assignable Role List");
		}
		return findByCriteria(DetachedCriteria.forClass(Role.class).addOrder(Order.asc("displayName").ignoreCase()));
	}
}
