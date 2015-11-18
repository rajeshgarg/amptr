package com.nyt.mpt.audit;

import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Audit;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IAuditService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.CustomCheckedException;

/**
 * JUnit test for audit service
 * @author megha.vyas
 */
public class AuditServiceTest extends AbstractTest {
	@Autowired
	@Qualifier("auditService")
	private IAuditService auditService;

	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;

	private Proposal proposal = null;
	private ProposalOption proposalOption = null;
	private ProposalVersion proposalVersion = null;
	private User userobj = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		List<Proposal> proposalLst = proposalService.getProposalList(null, null, null);
		if (proposalLst.size() > 0) {
			proposal = proposalLst.get(0);
			userobj = proposal.getAssignedUser();
			SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
			for (Proposal proposal : proposalLst) {
				Set<ProposalOption> proposalOptionSet = proposal.getProposalOptions();
				for (ProposalOption proposalOptionDB : proposalOptionSet) {
					proposalOption = proposalOptionDB;
					proposalVersion = proposalOption.getLatestVersion();
					if (proposalVersion != null) {
						return;
					}
				}
			}
		}
	}

	/**
	 * Create a new Audit Log
	 */
	@Test
	public void testCreate() {
		Audit auditDB = new Audit(0, new Date(), "megha", "entityName", 101, 1001, "hello");
		Assert.assertEquals(auditService.create(auditDB), auditDB);
	}

	/**
	 * Create a new Audit Log while creating new Proposal
	 */
	@Test
	public void testCreateAuditForNewProposal() {
		if (proposal != null) {
			auditService.createAuditForNewProposal(proposal);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Create a new Audit Log while creating new Proposal Option
	 */
	@Test
	public void testCreateAuditForNewOption() {
		if (proposalOption != null) {
			auditService.createAuditForNewOption(proposalOption);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Create a new Audit Log while creating new Option Version
	 */
	@Test
	public void testCreateAuditForNewVersion() {
		if (proposalVersion != null) {
			auditService.createAuditForNewVersion(0L, proposalVersion);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Create a new Audit Log while Assigning Proposal to another User
	 */
	@Test
	public void testCreateAuditForAssigningProposal() {
		if (proposal != null && userobj != null) {
			auditService.createAuditForAssigningProposal(proposal, userobj);
			Assert.assertTrue(true);
			proposal.setAssignedUser(null);
			auditService.createAuditForAssigningProposal(proposal, userobj);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Create a new Audit Log while updating a Proposal
	 */
	@Test
	public void testCreateAuditForUpdateProposal() {
		if (proposal != null) {
			final Proposal proposalnew = new Proposal();
			proposalnew.setDueDate(new Date());
			auditService.createAuditForUpdateProposal(proposal, proposalnew);
			Assert.assertTrue(true);
		}
	}

	/**
	 * test for createAuditForProposalStaus that Create a new Audit Log while updating Proposal Status
	 */
	@Test
	public void testCreateAuditForProposalStaus() {
		if (proposal != null) {
			auditService.createAuditForProposalStaus(proposal.getId(), ProposalStatus.PROPOSED, ProposalStatus.SOLD);
			Assert.assertTrue(true);
		}
	}

	/**
	 * Get List of Audits by ProposalId
	 */
	@Test
	public void testGetAuditsByParentId() {
		if (proposal != null) {
			final List<Audit> auditlist = auditService.getAuditsByParentId(proposal.getId());
			Assert.assertTrue(auditlist.size() >= 0);
		}
	}

	/**
	 * Submit for pricing review when user is planner and proposal is 'In-Progress'
	 */
	@Test
	public void testCreateAuditForPricingReview() {
		if (proposal != null) {
			final Audit audit = auditService.createAuditForPricingReview(proposal);
			Assert.assertEquals("Proposal submitted for pricing review.", audit.getMessage());
		}
	}

	/**
	 * Submit back to planner when user is pricing admin and proposal is 'In-Progress'
	 */
	@Test
	public void testCreateAuditForProposalBackToPlanner() {
		if (proposal != null) {
			final Audit audit = auditService.createAuditForProposalBackToPlanner(proposal);
			Assert.assertEquals("Proposal submitted back to planner.", audit.getMessage());
		}
	}
	
	@Test
	public void testcreateAuditForNewSalesforceID() throws CustomCheckedException {
		if (proposal != null) {
			auditService.createAuditForNewSalesforceID(proposal);
		}
	}
	
	@Test
	public void testcreateAuditOldSalesforceProposal() throws CustomCheckedException {
		if (proposal != null) {
			auditService.createAuditOldSalesforceProposal("", proposal);
		}
	}
}
