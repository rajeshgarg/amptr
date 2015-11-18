/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.nyt.mpt.dao.IGenericDAO;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;

/**
 * This base class is used by all dao level classes
 * 
 * @author surendra.singh
 * 
 */
abstract class GenericDAOImpl extends HibernateDaoSupport implements IGenericDAO {

	private static final Logger LOGGER = Logger.getLogger(GenericDAOImpl.class);

	@Override
	protected void initDao() throws Exception {
		super.initDao();
		getHibernateTemplate().setFlushMode(HibernateAccessor.FLUSH_COMMIT);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Object object) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving or Updating the object: " + object);
		}
		getHibernateTemplate().getSessionFactory().getCurrentSession().evict(object);
		getHibernateTemplate().saveOrUpdate(object);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#get(long)
	 */
	public <T> T load(Class<T> entityClass, Serializable entityId) {
		return getHibernateTemplate().load(entityClass, entityId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#save(java.lang.Object)
	 */
	public void save(Object object) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving object: " + object);
		}
		getHibernateTemplate().save(object);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#update(java.lang.Object)
	 */
	public void update(Object object) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating object: " + object);
		}
		getHibernateTemplate().getSessionFactory().getCurrentSession().evict(object);
		getHibernateTemplate().update(object);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#delete(java.lang.Object)
	 */
	public void delete(Object object) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting object: " + object);
		}
		getHibernateTemplate().delete(object);
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.util.dao.IGenericDAO#deleteAll(java.util.List)
	 */
	@SuppressWarnings({ "rawtypes" })
	public void deleteAll(List objectList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting List of objects. ObjectList: " + objectList);
		}
		getHibernateTemplate().deleteAll(objectList);
	}

	/**
	 * Find the entity of a specified class with the specified id
	 * @param entityClass
	 * @param entityId
	 * @return the entity instance
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	Object findById(Class entityClass, Serializable entityId) {
		return getHibernateTemplate().get(entityClass, entityId);
	}

	@SuppressWarnings("rawtypes")
	List findByCriteria(final DetachedCriteria detachedCriteria) {
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	/**
	 * This method will add the clause of limiting result if PaginationCriteria
	 * criteria is supplied. NOTE: pg.getFirstResultPosition()-1 : "-1" is done
	 * as first result is equal to 0 index in resultset. Caution: Addition of
	 * pagination could result in problem if result transformer is used in
	 * Criteria
	 * 
	 * @see( 
	 *       http://blog.xebia.com/2008/12/sorting-and-pagination-with-hibernate-
	 *       criteria-how-it-can-go-wrong-with-joins/ )
	 * 
	 * @param criteria
	 * @param pageCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List findByCriteria(DetachedCriteria criteria, PaginationCriteria pageCriteria) {
		if (pageCriteria == null) {
			return getHibernateTemplate().findByCriteria(criteria);
		} else {
			return getHibernateTemplate().findByCriteria(criteria, pageCriteria.getFirstResultPosition() - 1, pageCriteria.getMaxNoOfRecordsToBeFetch());
		}
	}

	/**
	 * @param criteria
	 * @param sortingCriteria
	 */
	protected void addSortingCriteria(final DetachedCriteria criteria, final SortingCriteria sortingCriteria) {
		if (sortingCriteria == null) {
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding sorting criteria " + sortingCriteria);
		}
		final CustomDbOrder order = new CustomDbOrder();
		order.setAscending(sortingCriteria.getSortingOrder().equals(CustomDbOrder.ASCENDING));
		order.setFieldName(sortingCriteria.getSortingField());
		criteria.addOrder(order.getOrder());
	}

	/**
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	Integer getCount(final DetachedCriteria criteria) {
		criteria.setProjection(Projections.rowCount());
		return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session) {
				return ((Long) criteria.getExecutableCriteria(session).uniqueResult()).intValue();
			}
		});
	}
}
