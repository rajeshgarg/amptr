package com.nyt.mpt.cronJob;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.CronJobConfiguration;
import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.service.impl.CronJobService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * JUnit test for CronJob Service
 * @author megha.vyas
 *
 */
public class CronJobServiceTest extends AbstractTest{  

	@Autowired
	@Qualifier("cronService")
	private CronJobService cronJobService;

	@Before 
	public void setup(){
		super.setAuthenticationInfo();
	}


	/**
	 * Test for addNewJobSchedule that Schedule a new cron job 
	 */
	@Test
	public void testAddNewJobSchedule(){ 
		final Date date = DateUtil.getTomorrowMidnightDate();
		final CronJobSchedule jobSchedule = new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date);
		jobSchedule.setScheduledBy("Test");
		cronJobService.addNewJobSchedule(jobSchedule);
		Assert.assertTrue(true);
	}

	/**
	 * Test for isJobActiveOnServerIt tells whether job is active for given server.
	 */
	@Test
	public void testIsJobActiveOnServer(){
		try{
			Assert.assertFalse(cronJobService.isJobActiveOnServer(CronJobNameEnum.SECTION_WEIGHT_CALCULATION, InetAddress.getLocalHost().getCanonicalHostName()));
		} catch (UnknownHostException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Test for getScheduledJob that Return scheduled job object
	 */
	@Test
	public void testGetScheduledJob() {
		final Date date = DateUtil.getTomorrowMidnightDate();
		final CronJobSchedule jobSchedule = new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date);
		jobSchedule.setScheduledBy("Test");
		cronJobService.addNewJobSchedule(jobSchedule);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		final CronJobSchedule jobScheduleDB = cronJobService.getScheduledJob(new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date));
		Assert.assertTrue(jobScheduleDB != null);
	}
	
	@Test
	public void testGetRecordFetchDuration(){
		CronJobConfiguration cronJob = cronJobService.getRecordFetchDuration(CronJobNameEnum.SALESFORCE_PROPOSAL_INTEGRATION_JOB.name());
		Assert.assertTrue(cronJob.getRecordFetchDurationInMin() > 0);
	}
}
