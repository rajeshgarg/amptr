/**
 * 
 */
package com.nyt.mpt.proposal;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.CampaignObjective;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for Build Proposal Service
 * @author amandeep.singh
 */
public class BuildProposalTest extends AbstractTest {

	@Autowired
	@Qualifier("proposalService")
	private IProposalService proposalService;

	private Proposal proposal = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		final List<Proposal> proposalLst = proposalService.getProposalList(null, null, null);
		if (proposalLst.size() > 0) {
			proposal = proposalLst.get(0);
			final User userobj = proposal.getAssignedUser();
			SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
		}
	}

	/**
	 * Return count of Option line items based on Option Id, proposalversion and filter criteria.
	 */
	@Test
	public void testGetFilteredPackageLineItemsCount() {
		if (proposal != null) {
			final long proposalId = proposal.getId();
			final long proposalversion = proposal.getVersion();
			final FilterCriteria filterCriteria = null;
			final int count = proposalService.getFilteredPackageLineItemsCount(proposalId, proposalversion, filterCriteria);
			Assert.assertTrue(count >= 0);
		}
	}

	/**
	 * This method is used to fetch all campaign objectives from database
	 */
	@Test
	public void testGetCampaignObjectives() {
		final List<CampaignObjective> comObject = proposalService.getCampaignObjectives();
		if (comObject != null) {
			Assert.assertTrue(comObject.size() >= 0);
		}
	}

	/**
	 * Check for proposal with same name based on proposalName and proposalId.
	 */
	@Test
	public void testProposalCountWithSameName() {
		if (proposal != null) {
			final String proposalName = proposal.getName();
			final long proposalId = proposal.getId();
			final int count = proposalService.proposalCountWithSameName(proposalName, proposalId);
			Assert.assertTrue(count >= 0);
		}

	}

	/**
	 * Return list of linrItrmException based on lineItem Id.
	 */
	@Test
	public void testGetProposalListCountHavingLineItems() {
		final int count = proposalService.getProposalListCount(null);
		Assert.assertTrue(count >= 0);
	}
}
