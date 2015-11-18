/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.CronJobConfiguration;
import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * @author surendra.singh
 *
 */
public interface ICronJobDAO {

	/**
	 * Return list of active job on given server.
	 * 
	 * @param name
	 * @param hostName
	 * @return
	 */
	List<CronJobConfiguration> getActiveJobConfig(CronJobNameEnum jobName, String hostName);

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
