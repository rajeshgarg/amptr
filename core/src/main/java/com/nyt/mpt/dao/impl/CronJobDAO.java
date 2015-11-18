/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.ICronJobDAO;
import com.nyt.mpt.domain.CronJobConfiguration;
import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * @author surendra.singh
 *
 */
public class CronJobDAO extends GenericDAOImpl implements ICronJobDAO {

	private static final Logger LOGGER = Logger.getLogger(CronJobDAO.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICronJobDAO#isJobActiveOnServer(com.nyt.mpt.util.enums.CronJobNameEnum, java.lang.String)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CronJobConfiguration> getActiveJobConfig(CronJobNameEnum jobName, String hostName) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(CronJobConfiguration.class);
		criteria.add(Restrictions.eq(ConstantStrings.NAME, jobName.name()));
		criteria.add(Restrictions.eq("hostName", hostName));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		List list = findByCriteria(criteria);
		LOGGER.info("Active '"+ jobName.name() +"' cron jobs on server - '" + hostName + "' are - " + list.size());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICronJobDAO#getRecordFetchDuration(java.lang.String)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public CronJobConfiguration getRecordFetchDuration(String jobName) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(CronJobConfiguration.class);
		criteria.add(Restrictions.eq(ConstantStrings.NAME, jobName));
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		List list = findByCriteria(criteria);
		return list.isEmpty() ? null : (CronJobConfiguration) list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICronJobDAO#addNewJobSchedule(com.nyt.mpt.domain.CronJobSchedule)
	 */
	@Override
	public void addNewJobSchedule(CronJobSchedule jobSchedule) {
		save(jobSchedule);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICronJobDAO#getScheduledJob(com.nyt.mpt.domain.CronJobSchedule)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public CronJobSchedule getScheduledJob(CronJobSchedule jobSchedule) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(CronJobSchedule.class);
		criteria.add(Restrictions.eq("jobName", jobSchedule.getJobName()));
		criteria.add(Restrictions.eq("scheduleDate", jobSchedule.getScheduleDate()));
		List list = findByCriteria(criteria);
		return list.isEmpty() ? null : (CronJobSchedule) list.get(0);
	}
}
