/**
 * 
 */
package com.nyt.mpt.util.intercepter;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.impl.ProposalService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.ProposalAccessException;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * To use this intercepter every method which changes the database State of a Proposal
 * Should have first argument as ProposalID and that method should be annotated
 * with @validateproposal
 *
 * @author amandeep.singh
 */

@Aspect
public class ProposalAccessValidatorInterceptor extends AbstractValidatorInterceptor {

	private static final Logger LOGGER = Logger.getLogger(ProposalService.class);
	private IProposalService proposalService;

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Before("execution(@com.nyt.mpt.util.annotation.ValidateProposal * *(..))")
	public void checkUserAuthentication(final JoinPoint joinPoint) {
		Assert.isTrue(joinPoint.getArgs().length > 0);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Intercepting the  request for check simultaneous access of a proposal");
		}
		final Long proposalId = (Long) joinPoint.getArgs()[0];
		final Proposal proposal = proposalService.getProposalAndAssgndUsr(proposalId);
		if (proposal != null) {
			final User assignedUser = proposal.getAssignedUser();
			final User loggedInUser = SecurityUtil.getUser();
			if (assignedUser != null && assignedUser.getUserId() != loggedInUser.getUserId()) {
				LOGGER.info("Proposal with Id: " + proposalId + ", is assigned to user with Id: " + assignedUser.getUserId()
						+ ". Current user with Id: " + loggedInUser.getUserId() + " cannot change it.");
				throw new ProposalAccessException(getCustomeBusinessError(ErrorCodes.proposalNotAssigned, ErrorMessageType.PROPOSAL_ERROR));
			}
		}
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Before("execution(@com.nyt.mpt.util.annotation.ValidateProposalDocument * *Document(..))")
	public void checkUserAuthenticationForProposalDocument(final JoinPoint joinPoint) {
		Assert.isTrue(joinPoint.getArgs().length > 0);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Intercepting the  request for check simultaneous Document access of a proposal");
		}
		final Document document = (Document) joinPoint.getArgs()[0];
		if (document != null && ConstantStrings.PROPOSAL.equals(document.getDocumentFor())) {
			final Long proposalId = document.getComponentId();
			final Proposal proposal = proposalService.getProposalAndAssgndUsr(proposalId);
			if (proposal != null) {
				final User assignedUser = proposal.getAssignedUser();
				final User loggedInUser = SecurityUtil.getUser();
				if (assignedUser != null && assignedUser.getUserId() != loggedInUser.getUserId()) {
					LOGGER.info("Proposal with Id: " + proposalId + ", is assigned to user with Id: " + assignedUser.getUserId()
							+ ". Current user with Id: " + loggedInUser.getUserId() + " cannot change it.");
					throw new ProposalAccessException(getCustomeBusinessError(ErrorCodes.proposalNotAssigned, ErrorMessageType.PROPOSAL_ERROR));
				}
			}
		}
	}

	public IProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}
}
