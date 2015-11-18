/**
 * 
 */
package com.nyt.mpt.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.nyt.mpt.dao.impl.SalesForceProposalDAO;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan_Option__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.ws.ConnectionException;

/**
 * This <code>ISalesForceProposalService</code> interface helps in calling {@link SalesForceProposalDAO} functions directly from controller. This <code>ISalesForceProposalService</code> has declarations 
 * for fetching all the salesforce {@link Media_Plan__c} and {@link Media_Plan_Revision__c}, for creating {@link Proposal} from salesforce's {@link Media_Plan__c}, for creating {@link Media_Plan_Revision__c} as {@link Notes} in AMPT database,
 * for creating and updating {@link Media_Plan_Option__c} as {@link ProposalOption} in AMPT and vice-versa, for saving the {@link Attachment} in AMPT's {@link Document} in file system as {@link File}
 * @author Gurditta.Garg
 */
public interface ISalesForceProposalService {

	/**
	 * Returns the List of {@link Media_Plan__c} from salesforce which has been created within configured time frame in the MP_JOB_CONFIG table for the SALESFORCE_PROPOSAL_INTEGRATION_JOB record
	 * @return 	Returns the List of {@link Media_Plan__c} from salesforce within the configured time frame
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	List<Media_Plan__c> getSalesForceProposals() throws ConnectionException;

	/**
	 * Saves populated and validated salesforce {@link Media_Plan__c} to AMPT database as {@link Proposal}
	 * @param 	proposal
	 * 			AMPT {@link Proposal} which has to be saved in the AMPT database and has all data been set from salesforce {@link Media_Plan__c}
	 * @param 	sfProposal
	 * 			Salesforce {@link Media_Plan__c} which has all the data from salesforce which will set the data to AMPT {@link Proposal}
	 * @throws 	IOException If any exception comes while saving the {@link Attachment} from salesforce as {@link File} in file system
	 */
	Proposal saveSfProposalToAMPT(Proposal proposal, Media_Plan__c sfProposal) throws IOException;

	/**
	 * Returns the List of {@link Media_Plan_Revision__c} from salesforce which has been created within the configured time frame in the MP_JOB_CONFIG table for the SALESFORCE_PROPOSAL_INTEGRATION_JOB record
	 * @return 	Returns the List of {@link Media_Plan_Revision__c} from salesforce within the configured time frame
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	List<Media_Plan_Revision__c> getSalesForceProposalRevisions() throws ConnectionException;

	/**
	 * Saves/Updates the {@link SalesforceProposalEmailFlag} with the following information
	 * <ul>
	 *	<li> {@code submmitedDate} is the last submitted date when the {@link Media_Plan__c} was saved/updated from salesforce
	 *	<li> {@code emailFlag} is flag which represent if the mail was sent or not
	 * </ul>
	 * @param 	salesforceProposalEmailFlag
	 * 			{@link SalesforceProposalEmailFlag} object which will be saved in AMPT database
	 * @return
	 * 			Saves/Updates the {@link SalesforceProposalEmailFlag} in AMPT database
	 */
	SalesforceProposalEmailFlag saveOrUpdateEmailFlag(SalesforceProposalEmailFlag salesforceProposalEmailFlag);

	/**
	 * Updates the {@link Media_Plan__c} from AMPT to salesforce which includes following information:
	 * <ul>
	 * 	<li> {@code amptKey} is the Id of Proposal in AMPT
	 * 	<li> {@code amptLink} is Proposal link which will redirect to AMPT application
	 * 	<li> {@code createdDate} is the date when Proposal created in AMPT database
	 * 	<li> {@code eventAuditLog} is the auditing logs
	 * </ul>
	 * @param 	amptProposal
	 * 			{@link Proposal} which has all the data which has to be updated in salesforce {@link Media_Plan__c}
	 * @throws CustomCheckedException
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	void updateProposalToSalesforce(final Proposal amptProposal, String action) throws CustomCheckedException, ConnectionException;

	/**
	 * Updates the {@link Media_Plan_Revision__c} from AMPT to salesforce which includes following information:
	 * <ul>
	 *	<li> {@code createdDate} is created date of {@code Notes} in AMPT
	 * </ul>
	 * @param 	sfProposalRevision
	 * 			{@link Media_Plan_Revision__c} which we've to update in the salesforce from AMPT
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	void updateProposalRevisionToSalesforce(Media_Plan_Revision__c sfProposalRevision) throws ConnectionException;

	/**
	 * Returns the {@link SalesforceProposalEmailFlag} by <code>salesforceId</code> which is {@link Media_Plan__c} object's id and has one-to-one relation with {@link SalesforceProposalEmailFlag}
	 * @param 	salesforceId
	 * 			{@link Media_Plan__c} object's id which has relation to {@link SalesforceProposalEmailFlag} table in AMPT database
	 * @return
	 * 			Returns the {@link SalesforceProposalEmailFlag} by passing the <code>salesforceId</code>
	 */
	SalesforceProposalEmailFlag getEmailFlagBySalesforceId(String salesforceId);

	/**
	 * Saves populated salesforce {@link Media_Plan_Revision__c} to AMPT database as {@link Notes} and its related {@link Attachment} to the file system
	 * @param 	sfRevision
	 * 			{@link Media_Plan_Revision__c} object which has one-to-many relation with {@link Notes} table in AMPT database
	 * @param 	amptProposal
	 * 			{@link Proposal} object which has one-to-many relation with {@link Notes} table in AMPT database
	 * @throws IOException If any exception comes while saving the {@link Attachment} from salesforce as {@link File} in file system
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	void saveSfRevisionsToAMPT(final Media_Plan_Revision__c sfRevision, final List<Attachment> attachments) throws IOException, ConnectionException;

	/**
	 * Updates {@link Media_Plan__c} in salesforce from AMPT by passing <code>sfProposal</code> which is the {@link Media_Plan__c} object which has to be updated in the salesforce database
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has to be updated in the salesforce database
	 * @throws CustomCheckedException
	 */
	void updateMediaPlanToSalesforce(final Media_Plan__c sfProposal) throws CustomCheckedException;

	/**
	 * Returns the List of {@link Attachment} from salesforce for a specific parentId which may be the id of {@link Media_Plan__c} or {@link Media_Plan_Revision__c}
	 * @param 	parentId
	 * 			{@link Media_Plan__c} id or {@link Media_Plan_Revision__c} id which is parentId in {@link Attachment}
	 * @return 	Returns the List of attachments by passing the parentId
	 * @throws 	ConnectionException
	 * 			If any of the following is true:
	 *           <ul>
	 *             <li> {@code salesforceConnection} is null
	 *             <li> {@code username} is not correct
	 *             <li> {@code password} is not correct
	 *             <li> {@code end-point URl} is not correct
	 *             <li> {@code account} has been expired due to any reason
	 *           </ul>
	 */
	List<Attachment> fetchAttachmentsFromSalesforce(String parentId) throws ConnectionException;
	
	/**
	 * Creates and saves {@link Media_Plan_Option__c} in salesforce from AMPT by passing <code>proposal</code> which is {@link Proposal} object in AMPT
	 * @param 	proposal
	 * 			{@link Proposal} object which has all the information related to all the options 
	 * @throws CustomCheckedException
	 */
	void createOptionsInSf(Proposal proposal) throws CustomCheckedException;
	
	/**
	 * Updates the {@link Media_Plan_Option__c} from AMPT to salesforce when {@link Proposal} has been marked SOLD and pricing data has not been changed
	 * @param 	defaultOption
	 * 			This is the default {@link ProposalOption}
	 * @param 	options
	 * 			List of {@link Media_Plan_Option__c} which we have to update in salesforce from AMPT
	 * @throws CustomCheckedException
	 */
	void updateOptionsInSf(ProposalOption defaultOption, List<Media_Plan_Option__c> options) throws CustomCheckedException;
	
	/**
	 * Updates the {@link Media_Plan_Option__c} from AMPT to salesforce when {@link Proposal} has been marked SOLD and pricing data has been changed
	 * @param 	proposal
	 * 			{@link Proposal} object which has all the information related to all the options 
	 * @param 	salesforceId
	 * 			{@link Media_Plan__c} object's id which has to be updated in the salesforce database
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 * @throws CustomCheckedException
	 */
	void updateOptionsInSalesForce(final Proposal proposal, String salesforceId) throws ParseException, CustomCheckedException;
	
	/**
	 * Returns the {@link Proposal} by <code>salesforceID</code> which is {@link Media_Plan__c} object's id and has one-to-one relation with {@link Proposal}
	 * @param 	salesforceID
	 * 			{@link Media_Plan__c} object's id which has relation to {@link Proposal} table in AMPT database
	 * @return
	 * 			Returns the {@link Proposal} by passing the <code>salesforceID</code>
	 */
	Proposal getProposalBySalesforceId(String salesforceID);

	/**
	 * @param salesforceId
	 * @return
	 * @throws ConnectionException
	 * @throws CustomCheckedException
	 */
	List<Media_Plan__c> getMediaPlanBySfId(String salesforceId) throws ConnectionException, CustomCheckedException;

	/**
	 * @param ad_Systems_Workflow_Update__c
	 * @throws CustomCheckedException
	 */
	void saveProposalAuditLogInSalesforce(Ad_Systems_Workflow_Update__c ad_Systems_Workflow_Update__c) throws CustomCheckedException;
}