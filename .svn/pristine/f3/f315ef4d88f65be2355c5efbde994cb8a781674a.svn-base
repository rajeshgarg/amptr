
package com.nyt.mpt.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.nyt.mpt.dao.IEmailScheduleDAO;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>EmailScheduleDAO</code> contains all the methods for the EmailSchedule related operations like : saving/deleting/updating EmailSchedules, fetching EmailSchedule etc
 * 
 * @author Gurditta.Garg
 */

public class EmailScheduleDAO extends GenericDAOImpl implements IEmailScheduleDAO{

	private static final Logger LOGGER = Logger.getLogger(EmailSchedule.class); 
	
	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();
	
	static {
		FIELD_COLUMN_MAP.put("emailScheduleId", "emailScheduleId");
		FIELD_COLUMN_MAP.put("productId", "productId");
		FIELD_COLUMN_MAP.put("salesTargetId", "salesTargetId");
		FIELD_COLUMN_MAP.put("prodcutName", "prodcutName");
		FIELD_COLUMN_MAP.put("salesTargetName", "salesTargetName");
		FIELD_COLUMN_MAP.put("active", "active");
		FIELD_COLUMN_MAP.put("scheduleDetailId", "emailSchedules.emailScheduleDetailsId");
		FIELD_COLUMN_MAP.put("forever", "emailSchedules.forever");
		FIELD_COLUMN_MAP.put("frequencyStr", "emailSchedules.frequency");
		FIELD_COLUMN_MAP.put("weekDays", "emailSchedules.weekdays");
		FIELD_COLUMN_MAP.put("emailStartDate", "emailSchedules.startDate");
		FIELD_COLUMN_MAP.put("emailEndDate", "emailSchedules.endDate");
		FIELD_COLUMN_MAP.put("frequency", "emailSchedules.frequency");
	}
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#saveEmailSchedule(com.nyt.mpt.domain.ManageEmailSchedule)
	 */
	@Override
	public EmailSchedule saveEmailSchedule(final EmailSchedule entity) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving ManageEmailSchedule with id: " + entity.getEmailScheduleId());
		}
		save(entity);
		return entity;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#deleteEmailSchedule(com.nyt.mpt.domain.ManageEmailSchedule)
	 */
	@Override
	public void deleteEmailScheduleDetails(final EmailScheduleDetails entity) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting email schedule Details with id: " + entity.getEmailScheduleDetailsId());
		}
		delete(entity);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#getFilteredEmailDetailList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EmailSchedule> getFilteredEmailDetailList(final List<FilterCriteria> criteriaList, 
			final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Email schedule list for Filter Criteria: " + criteriaList);
		}
		final DetachedCriteria criteria = constructFilterCriteriaForEmailSchedule(criteriaList);
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}		
		return findByCriteria(criteria, pgCriteria);
	}

	/**
	 * Returns the {@link DetachedCriteria} from the List of {@link FilterCriteria}
	 * @param 	criteriaList
	 * 			List of {@link FilterCriteria}
	 * @return
	 * 			Returns the {@link DetachedCriteria}
	 */
	private DetachedCriteria constructFilterCriteriaForEmailSchedule(final List<FilterCriteria> criteriaList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for Email schedule : " + criteriaList);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(EmailSchedule.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias("emailSchedules", "emailSchedules", Criteria.LEFT_JOIN);
		if(criteriaList != null){
			for(FilterCriteria filterCriteria : criteriaList){
				if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())
						&& StringUtils.isNotBlank(filterCriteria.getSearchString())) {
					if (SearchOption.EQUAL.toString().equals(filterCriteria.getSearchOper())) {
						criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), 
								Long.valueOf(filterCriteria.getSearchString())));
					} else if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
						criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()),
								filterCriteria.getSearchString(), MatchMode.ANYWHERE));
					}
				}
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#getFilteredEmailDetailListSize(java.util.List)
	 */
	@Override
	public int getFilteredEmailDetailListSize(final List<FilterCriteria> criteriaList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Email list count for Filter Criteria: " + criteriaList);
		}
		return getCount(constructFilterCriteriaForEmailSchedule(criteriaList));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#findById(java.lang.Class, long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public EmailSchedule getEmailScheduleById(long emailScheduleId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finding by criteria for EmailSchedule : " + emailScheduleId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(EmailSchedule.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq("emailScheduleId", emailScheduleId));
		List<EmailSchedule>  emailSchedule = findByCriteria(criteria);
		return emailSchedule.get(0);
	}


	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#findBetweenDates(com.nyt.mpt.domain.EmailSchedule)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public boolean findDuplicateDate(final Date startDate,final Date endDate,final Long emailScheduleId,final Long emailScheduleDetailsId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finding by query for emailScheduleDetailsId: " + emailScheduleId);
		}
		final StringBuffer hqlQuery =
				new StringBuffer("select count(1) AS COUNT from {h-schema}MP_EMAIL_SCHEDULE_DETAILS esd, {h-schema}MP_EMAIL_SCHEDULE es")
				.append(" where  esd.EMAIL_SCHEDULE_DETAILS_ID != :emailScheduleDetailsId AND esd.email_schedule_id = :emailScheduleId AND " )
				.append ("((TO_DATE(:startDate,:dateFormat) BETWEEN esd.start_date AND esd.end_date OR TO_DATE(:endDate,:dateFormat) BETWEEN esd.start_date AND esd.end_date)")
				.append ("OR (esd.start_date BETWEEN TO_DATE(:startDate,:dateFormat) AND TO_DATE(:endDate,:dateFormat) OR esd.end_date BETWEEN TO_DATE(:startDate,:dateFormat)")
				.append ( "AND TO_DATE(:endDate,:dateFormat)))" );
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final SQLQuery query = hibernateSession.createSQLQuery(hqlQuery.toString());
		query.setLong("emailScheduleId", emailScheduleId);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		query.setLong("emailScheduleDetailsId", emailScheduleDetailsId);
		query.setString("dateFormat", "dd-mm-yy");
		//counting for size of array returned
		query.addScalar("COUNT", StandardBasicTypes.INTEGER);	
		final List<Object> result = query.list();
		int count = 0;
		for (Object object : result) {
			count = (Integer)object; 
		}
		return count > 0 ? true : false;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#updateEmailScheduleDetails(com.nyt.mpt.domain.EmailScheduleDetails)
	 */
	@Override
	public void updateEmailScheduleDetails(EmailScheduleDetails emailScheduleDetails) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating ManageEmailSchedule with id: " + emailScheduleDetails.getEmailScheduleDetailsId());
		}
		//hitting the database to update the object
		update(emailScheduleDetails);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#updateEmailScheduleDetails(com.nyt.mpt.domain.EmailScheduleDetails)
	 */
	@Override
	public void saveEmailScheduleDetails(EmailScheduleDetails emailScheduleDetails) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating ManageEmailSchedule with id: " + emailScheduleDetails.getEmailScheduleDetailsId());
		}
		//hitting the database to update the object
		save(emailScheduleDetails);
	}

	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public EmailScheduleDetails getEmailScheduleDetailsById(long emailScheduleDetailsId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finding by criteria for EmailScheduleDetails : " + emailScheduleDetailsId);
		}
		//detached criteria for the EmailScheduleDetails class
		final DetachedCriteria criteria = DetachedCriteria.forClass(EmailScheduleDetails.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("emailScheduleDetailsId", emailScheduleDetailsId));
		List<EmailScheduleDetails>  emailScheduleDetails = findByCriteria(criteria);
		//only one record returned
		return emailScheduleDetails.get(0) != null ?  emailScheduleDetails.get(0) : null ;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IEmailScheduleDAO#getEmailScheduleByDate(java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public EmailSchedule getEmailScheduleByDate(Long productId, Long salesTargetId, Date sendDate){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for Email schedule : " );
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(EmailSchedule.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias("emailSchedules", "emailSchedules",Criteria.LEFT_JOIN);
		// criteria for email schedule
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("salesTargetId", salesTargetId));
		// criteria for email schedule details
		criteria.add(Restrictions.le(FIELD_COLUMN_MAP.get("emailStartDate"), sendDate))
	            .add(Restrictions.ge(FIELD_COLUMN_MAP.get("emailEndDate"), sendDate));	
		//find by criteria
		List<EmailSchedule> emailScheduleDetailsList = findByCriteria(criteria);
		return emailScheduleDetailsList.size() > 0  ?  emailScheduleDetailsList.get(0): null;
	}
}