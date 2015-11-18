/**
 *
 */
package com.nyt.mpt.dao.impl;

import com.nyt.mpt.dao.IDatabaseHeartBeatSchedularDAO;
import java.util.List;

import org.hibernate.SQLQuery;

/**
 * This DAO level class is used for checking database heart beat
 *
 * @author pranay.prabhat
 *
 */
public class DatabaseHeartBeatSchedularDAO extends GenericDAOImpl implements IDatabaseHeartBeatSchedularDAO {

	/**
	 * Product form and entity map
	 */
	public void fetchAMPTRoles() {
		getHibernateTemplate().find("from Role where role_Id = 1");
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean healthCheck() {
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("SELECT * FROM DUAL WHERE ROWNUM <= 1");
			final List<Object> resultList = query.list();
			if (resultList.isEmpty() || resultList == null) {
				return false;
			}return true;
	}
}
