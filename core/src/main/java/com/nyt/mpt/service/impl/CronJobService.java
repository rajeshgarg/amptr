/**
 * 
 */
package com.nyt.mpt.service.impl;

import com.nyt.mpt.dao.ICronJobDAO;
import com.nyt.mpt.domain.CronJobConfiguration;
import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.service.ICronJobService;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * @author surendra.singh
 *
 */
public class CronJobService implements ICronJobService {

	private ICronJobDAO cronDAO; 
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICronJobService#isJobActiveOnServer(com.nyt.mpt.util.enums.CronJobNameEnum, java.lang.String)
	 */
	@Override
	public boolean isJobActiveOnServer(CronJobNameEnum jobName, String hostName) {
		return cronDAO.getActiveJobConfig(jobName, hostName).isEmpty() ? false : true;
	}

	public void setCronDAO(ICronJobDAO cronDAO) {
		this.cronDAO = cronDAO;
	}
	
	@Override
	public CronJobConfiguration getRecordFetchDuration(String jobName) {
		return cronDAO.getRecordFetchDuration(jobName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICronJobService#addNewJobSchedule()
	 */
	@Override
	public void addNewJobSchedule(CronJobSchedule jobSchedule) {
		cronDAO.addNewJobSchedule(jobSchedule);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICronJobService#getScheduledJob(com.nyt.mpt.domain.CronJobSchedule)
	 */
	@Override
	public CronJobSchedule getScheduledJob(CronJobSchedule jobSchedule) {
		return cronDAO.getScheduledJob(jobSchedule);
	}	
}
