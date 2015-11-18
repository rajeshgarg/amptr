/**
 *
 */
package com.nyt.mpt.dao.impl;

import com.nyt.mpt.dao.IODSDatabaseHeartBeatSchedularDAO;

/**
 * This DAO level class is used for checking database heart beat
 *
 * @author pranay.prabhat
 *
 */
public class ODSDatabaseHeartBeatSchedularDAO extends GenericDAOImpl implements IODSDatabaseHeartBeatSchedularDAO {
	
	 /* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IODSDatabaseHeartBeatSchedularDAO#fetchSalesCategoryFromODS()
	 */
	@Override
	public void fetchSalesCategoryFromODS() {
		getHibernateTemplate().find("from SOSSalesCategory where SOS_SALES_CATEGORY_ID = 1");
	}	
}
