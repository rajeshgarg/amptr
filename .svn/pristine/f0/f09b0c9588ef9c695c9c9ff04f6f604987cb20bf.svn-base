/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.Audit;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.security.SecurityUtil;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;

/**
 * @author amandeep.singh
 *
 */
public interface IAuditService {
	
	/**
	 * Create a new Audit Log
	 *
	 * @param audit
	 * @return
	 */
	Audit create(Audit audit);

	/**
	 * Create a new Audit Log while creating new Proposal
	 *
	 * @param proposal
	 */
	void createAuditForNewProposal(Proposal proposal);
	

	/**
	 * Create a new Audit Log while creating new Proposal Option
	 *
	 * @param propOption
	 */
	void createAuditForNewOption(ProposalOption propOption);
	
	/**
	 * Create a new Audit Log while creating new Option Version
	 * @param proposalId 
	 *
	 * @param propVersion
	 */
	void createAuditForNewVersion(Long proposalId, ProposalVersion propVersion);
	
	/**
	 * Create a new Audit Log while Assigning Proposal to another User
	 *
	 * @param proposal
	 * @param fromUser 
	 */
	void createAuditForAssigningProposal(Proposal proposal, User fromUser);
	
	/**
	 * Create a new Audit Log while updating a Proposal
	 *
	 * @param proposalOld
	 * @param proposalNew
	 */
	void createAuditForUpdateProposal(Proposal proposalOld, Proposal proposalNew);
	
	/**
	 * Create a new Audit Log while updating Proposal Status
	 * @param proposalId
	 * @param fromStatus
	 * @param toStatus
	 */
	void createAuditForProposalStaus(long proposalId ,ProposalStatus fromStatus ,ProposalStatus toStatus);
	
	/**
	 * Get List of Audits by ProposalId
	 * @param parentId
	 * @return
	 */
	List<Audit> getAuditsByParentId(long parentId);

	/**
	 * Submit for pricing review when user is planner and proposal is 'In-Progress'
	 * @param proposal
	 */
	Audit createAuditForPricingReview(Proposal proposal);
	
	/**
	 * Submit back to planner when user is pricing admin and  proposal is 'In-Progress'
	 * @param proposal
	 */
	Audit createAuditForProposalBackToPlanner(Proposal proposal);
	
	/**
	 * Create a new Audit Log while deleting Proposal Option
	 *
	 * @param propOption
	 */
	void createAuditForDeleteOption(ProposalOption propOption);
	/**
	 * audit log when new proposal is created from salesforce to ampt
	 * 
	 * @param proposal
	 * @param sfProposal
	 */
	void createAuditForNewProposalFromSalesforce(Proposal proposal, Media_Plan__c sfProposal);
	
	/**
	 * audit log when a revision is added from salesforce to ampt
	 * @param proposal
	 * @param proposalRevision
	 */
	void createAuditForNewProposalRevisionFromSalesforce(Proposal proposal, Media_Plan_Revision__c proposalRevision);

	/**
	 * Create audit log in SalesForce
	 * @param proposalDb
	 * @param message
	 * @throws CustomCheckedException 
	 */
	void salesforceAuditLog(final Proposal proposalDb, final StringBuilder message) throws CustomCheckedException;

	/**
	 * Create audits of new ampt proposal for the new salesforce mapping key
	 * @param proposal
	 * @throws CustomCheckedException
	 */
	void createAuditForNewSalesforceID(Proposal proposal) throws CustomCheckedException;

	/**
	 * Create audits of old ampt proposal for the new salesforce mapping key
	 * @param newSalesforceId
	 * @param proposal
	 * @throws CustomCheckedException
	 */
	void createAuditOldSalesforceProposal(String newSalesforceId, Proposal proposal) throws CustomCheckedException;
	
	/**
	 * Create audits of old home page line items for deleting their reservation
	 * @param lineItem
	 * @throws CustomCheckedException
	 */
	void createAuditMessageForHomePageResrvtn(LineItem lineItem, String msg)
			throws CustomCheckedException;
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAuditService#createAuditForPricingReview(com.nyt.mpt.domain.Proposal)
	 */
	Audit createAuditForVulnerableProposalHomePageReservation(Proposal proposal,String lineItemIds, String productClass) throws CustomCheckedException;

}
