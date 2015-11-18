/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

import com.nyt.mpt.dao.IValidatorDao;

/**
 * This class is used for validate data
 *
 * @author surendra.singh
 *
 */
public class ValidatorDao extends GenericDAOImpl implements IValidatorDao {

	private static final Logger LOGGER = Logger.getLogger(ValidatorDao.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IValidatorDao#validateAssociation(long, java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean validateAssociation(long entiryId, List<String> validator) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating Association for associationValidator:" + validator);
		}
		if (validator != null) {
			SQLQuery query = null;
			for (String queryString : validator) {
				query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryString);
				query.setLong(0, entiryId);
				query.addScalar("COUNT", StandardBasicTypes.LONG);
				final List<Object> resultList = query.list();
				if ((Long) resultList.get(0) > 0) {
					return false;
				}
			}
		}
		return true;
	}
}
