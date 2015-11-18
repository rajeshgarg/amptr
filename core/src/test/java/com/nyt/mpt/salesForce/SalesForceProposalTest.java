package com.nyt.mpt.salesForce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.service.impl.SalesForceProposalService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.SalesforceProposalServiceHelper;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.soap.enterprise.sobject.UserRole;
import com.sforce.ws.ConnectionException;

/**
 * 
 * @author Gurditta.Garg
 * 
 */
public class SalesForceProposalTest extends AbstractTest {

	@Autowired
	@Qualifier("salesForceProposalService")
	private SalesForceProposalService salesForceProposalService;
	@Autowired
	private SalesforceProposalServiceHelper sfProposalHelper;

	private Media_Plan__c sfProposal = new Media_Plan__c();
	private Media_Plan_Revision__c revision = new Media_Plan_Revision__c();

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		createDummySfProposal();
		createDummySfRevision();
	}

	@Test
	public void testFetchAllProposal() throws BusinessException, ConnectionException {
		for (Media_Plan__c media_Plan__c : salesForceProposalService.getSalesForceProposals()) {
			Assert.notNull(media_Plan__c.getId());
			List<Attachment> attachments = salesForceProposalService.fetchAttachmentsFromSalesforce(media_Plan__c.getId());
			if (!attachments.isEmpty()) {
				Assert.isTrue(attachments.size() > 0);
			}
			break;
		}
		for (Media_Plan_Revision__c proposalC : salesForceProposalService.getSalesForceProposalRevisions()) {
			System.out.println(proposalC.getName());
			break;
		}
	}

	@Test
	public void testSaveSfProposal() throws ConnectionException, IOException {
		final Proposal amptProposal = sfProposalHelper.populateProposalFromSF(sfProposal);
		amptProposal.setAssignedByUser(SecurityUtil.getUser());
		sfProposalHelper.validateProposalData(sfProposal, amptProposal);
		salesForceProposalService.saveSfProposalToAMPT(amptProposal, sfProposal);
		salesForceProposalService.saveSfRevisionsToAMPT(revision, new ArrayList<Attachment>());
	}
	
	@Test
	public void testTempFileForEmailAttachment() {
		Assert.notNull(sfProposalHelper.tempFileForEmailAttachment(new Exception("Exception")));
	}
	
	@Test
	public void testProposalStatusExceptReadOnly() {
		Assert.notNull(sfProposalHelper.proposalStatusExceptReadOnly(createDummyProposal()));
	}
	
	@Test
	public void testGetRevisionNote() {
		Assert.notNull(sfProposalHelper.getRevisionNote(revision, createDummyProposal()));
	}
	
	@Test
	public void testValidateRevisionData() {
		Assert.notNull(sfProposalHelper.validateRevisionData(revision, null));
	}
	
	@Test
	public void testGetMediaPlanBySfId() throws ConnectionException, CustomCheckedException {
		Assert.notNull(salesForceProposalService.getMediaPlanBySfId(sfProposal.getId()));
	}
	
	@Test
	public void testGetRevisionNotes() {
		revision.setRevision_Type__c(ConstantStrings.SALES_ORDER);
		Assert.notNull(sfProposalHelper.getRevisionNote(revision, createDummyProposal()));
	}
	
	@Test
	public void testGetEmailFlagBySalesforceId() {
		setEmailFlag();
		Assert.notNull(salesForceProposalService.getEmailFlagBySalesforceId(sfProposal.getId()));
	}
	
	private void setEmailFlag() {
		SalesforceProposalEmailFlag sfPropEmailFlag = new SalesforceProposalEmailFlag();
		sfPropEmailFlag.setSalesforceId(sfProposal.getId());
		sfPropEmailFlag.setEmailFlag("Y");
		salesForceProposalService.saveOrUpdateEmailFlag(sfPropEmailFlag);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
	}
	
	/**
	 * Used to create dummy proposal
	 * 
	 * @return
	 */
	private void createDummySfProposal() {
		sfProposal.setName("jUnit Testing Sf");
		sfProposal.setPriority__c("Regular");
		sfProposal.setDue_On_Date__c(DateUtil.dateTypeToCalendar(DateUtil.parseToDate(DateUtil.getCurrentDateTimeString("MM/dd/yyyy"))));
		sfProposal.setDue_On_Time__c("11:00 AM ");
		sfProposal.setAgency_Margin__c(15D);
		sfProposal.setProposal_Curency__c("USD");
		sfProposal.setId("123456789qazwsx");
		sfProposal.setSales_Category__c("Advocacy");
		sfProposal.setCampaign_Objective__c("Mobile;Tablet");
		sfProposal.setPrice_Type__c("Net");
		sfProposal.setAdvertiser_Name__c("Advertiser_Name__c");
		sfProposal.setAttachments(new QueryResult());
		User sfuser = new User();
		sfuser.setName("junit User Test");
		UserRole role = new UserRole();
		role.setName("junit User Role Test");
		sfuser.setUserRole(role);
		sfProposal.setCreatedBy(sfuser);
	}

	private void createDummySfRevision() {
		revision.setCampaign_Name__r(sfProposal);
		revision.setRevision_Type__c("Revision");
		revision.setRevision_Note__c("Revision notes");
		User sfuser = new User();
		sfuser.setName("junit User Test");
		UserRole role = new UserRole();
		role.setName("junit User Role Test");
		sfuser.setUserRole(role);
		revision.setCreatedBy(sfuser);
	}

	private Proposal createDummyProposal() {
		Proposal proposalNew = new Proposal();
		proposalNew.setActive(true);
		proposalNew.setCriticality(Criticality.REGULAR);
		proposalNew.setDateRequested(DateUtil.getCurrentDate());
		proposalNew.setDueDate(DateUtil.getCurrentDate());
		proposalNew.setEndDate(DateUtil.getCurrentDate());
		proposalNew.setGlobal(false);
		proposalNew.setId(0);
		proposalNew.setName("ProposalByTestcase");
		proposalNew.setPriceType("Net");
		proposalNew.setProposalStatus(ProposalStatus.INPROGRESS);
		proposalNew.setSosAdvertiserId(0L);
		proposalNew.setSosAgencyId(0L);
		proposalNew.setSosSalesCategoryId(1748L);
		proposalNew.setStartDate(DateUtil.getCurrentDate());
		proposalNew.setVersion(1);
		proposalNew.setCampaignName("ProposalCampaignNameByTestcase");
		proposalNew.setAgencyMargin(20D);
		return proposalNew;
	}
}