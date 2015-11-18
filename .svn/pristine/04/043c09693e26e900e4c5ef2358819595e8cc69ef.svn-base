/**
 * 
 */
package com.nyt.mpt.service;

import com.nyt.mpt.domain.CronJobConfiguration;
import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * @author surendra.singh
 *
 */
public interface ICronJobService {

	/**
	 * It tells whether job is active for given server.
	 * 
	 * @param name
	 * @param hostName
	 * @return
	 */
	boolean isJobActiveOnServer(CronJobNameEnum jobName, String canonicalHostName);
	
	/**
	 * Schedule a new cron job 
	 * @return
	 */
	void addNewJobSchedule(CronJobSchedule jobSchedule);

	/**
	 * Return scheduled job object
	 * @param jobSchedule
	 * @return
	 */
	CronJobSchedule getScheduledJob(CronJobSchedule jobSchedule);
	
	/**
	 * Returns the fetch duration in mins
	 * @param jobName
	 * @return
	 */
	CronJobConfiguration getRecordFetchDuration(String jobName);
}
