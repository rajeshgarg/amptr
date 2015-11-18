/**
 *
 */
package com.nyt.mpt.util.intercepter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.nyt.mpt.util.ChangeTrackedDomain;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This class is used for NYT Intercepter
 *
 * @author surendra.singh
 *
 */
public class NYTDAOInterceptor extends EmptyInterceptor {

	private static final Logger LOGGER = Logger.getLogger(NYTDAOInterceptor.class);

	private static final long serialVersionUID = 1L;

	private static final String LAST_MODIFIED_DATE_ATTRIBUTE = "MODIFIEDDATE";

	private static final String CREATE_DATE_ATTRIBUTE = "CREATEDDATE";

	private static final String LAST_MODIFIED_BY_ATTRIBUTE = "MODIFIEDBY";

	private static final String CREATED_BY_ATTRIBUTE = "CREATEDBY";


	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(final Object entity, final Serializable entityId, final Object[] state, final String[] propertyNames, final Type[] types) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding new object of " + entity.getClass() + " with id: " + entityId);
		}
		final String userName = SecurityUtil.getUser().getLoginName();
		final Date date = DateUtil.getCurrentDate();
		if (entity instanceof ChangeTrackedDomain) {
			for (int i = 0; i < propertyNames.length; i++) {
				if (StringUtils.equalsIgnoreCase(propertyNames[i], CREATE_DATE_ATTRIBUTE)) {
					state[i] = date;
				} else if (StringUtils.equalsIgnoreCase(propertyNames[i], LAST_MODIFIED_DATE_ATTRIBUTE)) {
					state[i] = date;
				} else if (StringUtils.equalsIgnoreCase(propertyNames[i], CREATED_BY_ATTRIBUTE)) {
					state[i] = userName;
				} else if (StringUtils.equalsIgnoreCase(propertyNames[i], LAST_MODIFIED_BY_ATTRIBUTE)) {
					state[i] = userName;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(final Object entity, final Serializable entityId, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("Updating object of " + entity.getClass() + " with id: " + entityId);
		}
		if (entity instanceof ChangeTrackedDomain) {
			for (int i = 0; i < propertyNames.length; i++) {
				if (StringUtils.equalsIgnoreCase(propertyNames[i], LAST_MODIFIED_DATE_ATTRIBUTE)) {
					currentState[i] = DateUtil.getCurrentDate();
				}
				if (StringUtils.equalsIgnoreCase(propertyNames[i], LAST_MODIFIED_BY_ATTRIBUTE)) {
					currentState[i] = SecurityUtil.getUser().getLoginName();
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onCollectionRemove(java.lang.Object, java.io.Serializable)
	 */
	@Override
	public void onCollectionRemove(final Object collection, final Serializable key) throws CallbackException {

	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onCollectionRecreate(java.lang.Object, java.io.Serializable)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void onCollectionRecreate(final Object collection, final Serializable key) throws CallbackException {
		final String userName = SecurityUtil.getUser().getLoginName();
		final Date date = DateUtil.getCurrentDate();
		if (collection instanceof Collection) {
			final Collection col = (Collection) collection;
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if (object instanceof ChangeTrackedDomain) {
					ChangeTrackedDomain chngTrackDomain = (ChangeTrackedDomain) object;
					chngTrackDomain.setCreatedBy(userName);
					chngTrackDomain.setCreatedDate(date);
					chngTrackDomain.setModifiedBy(userName);
					chngTrackDomain.setModifiedDate(date);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onCollectionUpdate(java.lang.Object, java.io.Serializable)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void onCollectionUpdate(final Object collection, final Serializable key) throws CallbackException {
		final String userName = SecurityUtil.getUser().getLoginName();
		final Date date = DateUtil.getCurrentDate();
		if (collection instanceof Collection) {
			final Collection col = (Collection) collection;
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if (object instanceof ChangeTrackedDomain) {
					ChangeTrackedDomain chngTrackDomain = (ChangeTrackedDomain) object;
					chngTrackDomain.setModifiedBy(userName);
					chngTrackDomain.setModifiedDate(date);
				}
			}
		}
	}
}