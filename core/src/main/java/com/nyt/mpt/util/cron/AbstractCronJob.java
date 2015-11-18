/**
 * 
 */
package com.nyt.mpt.util.cron;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.ICronJobService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.enums.CronJobNameEnum;

/**
 * Base Class for All Cron Jobs
 *
 * @author amandeep.singh
 *
 */
public abstract class AbstractCronJob extends QuartzJobBean {

	private static final String SYSTEM_CRON = "System.Cron";

	private static final Logger LOGGER = Logger.getLogger(AbstractCronJob.class);

	private ICronJobService cronService;

	/**
	 * It tells whether job is active for given server.
	 * @param jobName
	 * @return
	 */
	protected boolean isJobActiveOnServer(final CronJobNameEnum jobName) {
		try {
			return cronService.isJobActiveOnServer(jobName, InetAddress.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
			LOGGER.info("UnknownHostException while retriving Host Name for server");
		}
		return false;
	}

	/**
	 * Set user for Create/Update auditing for cron jobs.
	 */
	protected void setUserForCronJobs() {
		final User user = new User();
		user.setFirstName(SYSTEM_CRON);
		user.setLastName(ConstantStrings.EMPTY_STRING);
		user.setLoginName(SYSTEM_CRON);
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, SYSTEM_CRON));
	}

	public ICronJobService getCronService() {
		return cronService;
	}

	public void setCronService(final ICronJobService cronService) {
		this.cronService = cronService;
	}
}
