/**
 * 
 */
package com.nyt.mpt.util.cron;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.enums.ProposalStatus;

/**
 * Expired proposal status update scheduler to fetch all the proposals to be expired and mark them as expired.
 *
 * @author garima.garg
 *
 */
public class ExpiredProposalStatusUpdateJob extends AbstractCronJob {

	private IProposalService proposalService;

	private static final Logger LOGGER = Logger.getLogger(ExpiredProposalStatusUpdateJob.class);

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!isJobActiveOnServer(CronJobNameEnum.UPDATE_EXPIRED_PROPOSAL_STATUS)) {
			return;
		}

		LOGGER.info("Job for marking the proposal status as expired started - " + DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate().getTime()));
		super.setUserForCronJobs();

		final List<ProposalStatus> statusLst = new ArrayList<ProposalStatus>(3);
		statusLst.add(ProposalStatus.REJECTED_BY_CLIENT);
		statusLst.add(ProposalStatus.PROPOSED);

		final List<Proposal> proposalLst =  proposalService.getProposalsForUpdation(DateUtil.parseToDate(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(60))), statusLst);
		if (proposalLst != null && !proposalLst.isEmpty()) {
			LOGGER.info("Proposal's count to be marked as expired - " + proposalLst.size());
			proposalService.updateProposalStatus(proposalLst, ProposalStatus.EXPIRED);
			LOGGER.info("Proposal's count marked as expired - " + proposalLst.size());
		}
		LOGGER.info("Job for marking the proposal status as expired completed - " + DateUtil.getGuiDateTimeString(DateUtil.getCurrentDate().getTime()));
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}
}
