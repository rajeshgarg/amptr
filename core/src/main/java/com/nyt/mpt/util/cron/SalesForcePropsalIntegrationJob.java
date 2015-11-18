/**
 * 
 */
package com.nyt.mpt.util.cron;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.service.ISalesForceProposalService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.SalesforceProposalServiceHelper;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.ws.ConnectionException;

/**
 * This <code>SalesForcePropsalIntegrationJob</code> class includes the cron job implementation. It includes how and when the system would communicate with salesforce to AMPT and vice-versa.
 * @author Gurditta.Garg
 */

public class SalesForcePropsalIntegrationJob extends AbstractCronJob {
	
	private ISalesForceProposalService sfProposalService;

	private SalesforceProposalServiceHelper sfProposalHelper;

	private static final Logger LOGGER = Logger.getLogger(SalesForcePropsalIntegrationJob.class);
	
	private static boolean sfConnectionExceptionEmailFlag = false;
	
	private static boolean genericExceptionEmailFlag = true;
	
	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!isJobActiveOnServer(CronJobNameEnum.SALESFORCE_PROPOSAL_INTEGRATION_JOB)) {
			return;
		}
		super.setUserForCronJobs();
		try {
			for (Media_Plan__c sfProposal : sfProposalService.getSalesForceProposals()) {
				final Proposal amptProposal = sfProposalHelper.populateProposalFromSF(sfProposal);
				if (sfProposalHelper.validateProposalData(sfProposal, amptProposal)) {
					continue;
				}
				if (amptProposal != null) {
					sfProposalService.saveSfProposalToAMPT(amptProposal, sfProposal);
					sfProposalService.updateProposalToSalesforce(amptProposal, ConstantStrings.PROPOSAL_CREATED_IN_AMPT);
				}
			}
			for (Media_Plan_Revision__c sfRevision : sfProposalService.getSalesForceProposalRevisions()) {
				final List<Attachment> attachments = sfProposalService.fetchAttachmentsFromSalesforce(sfRevision.getId());
				if (sfProposalHelper.validateRevisionData(sfRevision, attachments)) {
					continue;
				}
				sfProposalService.saveSfRevisionsToAMPT(sfRevision, attachments);
			}
			if(sfConnectionExceptionEmailFlag) {
				sfProposalHelper.sendMail(ConstantStrings.EMPTY_STRING, null, false);
				sfConnectionExceptionEmailFlag = false;
			}
			genericExceptionEmailFlag = true;
		} catch (ConnectionException exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE, exception);
			if (!sfConnectionExceptionEmailFlag) {
				final File file = sfProposalHelper.tempFileForEmailAttachment(exception);
				sfProposalHelper.sendMail(exception.getMessage(), file, false);
				file.deleteOnExit();
			}
			sfConnectionExceptionEmailFlag = true;
		} catch (Exception exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR, exception);
			if(genericExceptionEmailFlag) {
				final File file = sfProposalHelper.tempFileForEmailAttachment(exception);
				sfProposalHelper.sendMail(exception.getMessage(), file, true);
				file.deleteOnExit();
			}
			genericExceptionEmailFlag = false;
		}
	}

	public void setSfProposalService(final ISalesForceProposalService sfProposalService) {
		this.sfProposalService = sfProposalService;
	}

	public void setSfProposalHelper(final SalesforceProposalServiceHelper sfProposalHelper) {
		this.sfProposalHelper = sfProposalHelper;
	}
}