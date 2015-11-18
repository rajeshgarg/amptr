/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Option__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

/**
 * This <code>ISalesForceProposalDAO</code> interface represent the CRUD i.e. create, read, update and delete operations. All operations are done in
 * salesforce's database which are done through salesforce's SOQL i.e. salesforce object query language queries. This <code>ISalesForceProposalDAO</code> interface includes declarations for saving {@link Media_Plan__c}, {@link Media_Plan_Revision__c},
 * {@link Media_Plan_Option__c} in AMPT and also updating in salesforce from AMPT, for fetching all the {@link Media_Plan__c}, {@link Media_Plan_Revision__c}, {@link Media_Plan_Option__c} and {@link Attachment} from salesforce.
 * @author Gurditta.Garg
 */
public interface ISalesForceProposalDAO {
	/**
	 * Returns the List of {@link Media_Plan__c} from salesforce which has been created within time frame of configured 
	 * <code>fetchDurationInMinutes</code> in the MP_JOB_CONFIG table for the SALESFORCE_PROPOSAL_INTEGRATION_JOB record
	 * @param 	fetchDurationInMinutes
	 * 			Duration in minutes which represent till what minutes before the {@link Media_Plan__c} has to be picked from salesforce to AMPT by cron job
	 * @return 	Returns the List of {@link Media_Plan__c} from salesforce within the configured time frame in <code>fetchDurationInMinutes</code>
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
	List<Media_Plan__c> getSalesForceProposals(int fetchDurationInMinutes) throws ConnectionException;

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
	 * Returns the List of {@link Media_Plan_Revision__c} from salesforce which has been created within time frame of configured 
	 * <code>fetchDurationInMinutes</code> in the MP_JOB_CONFIG table for the SALESFORCE_PROPOSAL_INTEGRATION_JOB record
	 * @param 	fetchDurationInMinutes
	 * 			Duration in minutes which represent till what minutes before the Media_Plan has to be picked from salesforce to AMPT by cron job
	 * @return 	Returns the List of {@link Media_Plan_Revision__c} from salesforce within the configured time frame in <code>fetchDurationInMinutes</code>
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
	List<Media_Plan_Revision__c> fetchProposalsRevisions(int fetchDurationInMinutes) throws ConnectionException;

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
	 * Saves populated salesforce {@link Media_Plan__c} to AMPT database as {@link Proposal}
	 * @param 	proposal
	 * 			{@link Proposal} object which will be saved in AMPT database
	 * @return
	 * 			Returns the saved {@link Proposal} object
	 */
	Proposal saveProposalSF(Proposal proposal);

	/**
	 * Saves populated salesforce {@link Media_Plan_Revision__c} to AMPT database as {@link Notes}
	 * @param 	note
	 * 			{@link Notes} object which will be saved in AMPT database
	 * @return
	 * 			Returns the saved {@link Notes} object's id
	 */
	long saveNotesSF(Notes note);

	/**
	 * Returns the {@link Proposal} by <code>salesforceID</code> which is {@link Media_Plan__c} object's id and has one-to-one relation with {@link Proposal}
	 * @param 	salesforceID
	 * 			{@link Media_Plan__c} object's id which has relation to {@link Proposal} table in AMPT database
	 * @return
	 * 			Returns the {@link Proposal} by passing the <code>salesforceID</code>
	 */
	Proposal getProposalBySalesforceId(String salesforceID);

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
	 * Returns the {@link SalesforceProposalEmailFlag} by <code>salesforceId</code> which is {@link Media_Plan__c} object's id and has one-to-one relation with {@link SalesforceProposalEmailFlag}
	 * @param 	salesforceId
	 * 			{@link Media_Plan__c} object's id which has relation to {@link SalesforceProposalEmailFlag} table in AMPT database
	 * @return
	 * 			Returns the {@link SalesforceProposalEmailFlag} by passing the <code>salesforceId</code>
	 */
	SalesforceProposalEmailFlag getEmailFlagBySalesforceId(String salesforceId);

	/**
	 * Updates the {@link Ad_Systems_Workflow_Update__c} from AMPT to salesforce
	 * @param 	ad_Systems_Workflow_Update__c
	 * 			{@link Ad_Systems_Workflow_Update__c} which has to be saved in the salesforce from AMPT
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	void saveProposalAuditLogInSalesforce(final Ad_Systems_Workflow_Update__c ad_Systems_Workflow_Update__c) throws CustomCheckedException;

	/**
	 * Updates the {@link Media_Plan_Option__c} from AMPT to salesforce
	 * @param 	optionArr
	 * 			Array of objects of {@link Media_Plan_Option__c} which has to be saved in the salesforce from AMPT
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	void updateSalesforceObjects(SObject[] optionArr) throws CustomCheckedException;

	/**
	 * Returns the List of {@link Media_Plan_Option__c} from salesforce by passing the <code>salesforceId</code> which is {@link Media_Plan__c} object's id and 
	 * which is having one-to-many relation with {@link Media_Plan_Option__c}
	 * @param 	salesforceId
	 * 			{@link Media_Plan_Revision__c} object's id which has relation to {@link Media_Plan_Option__c} table in salesforce database
	 * @return
	 * 			Returns the List of {@link Media_Plan_Option__c} by passing the <code>salesforceId</code>
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	List<Media_Plan_Option__c> getOptionDetailsFromSf(String salesforceId) throws CustomCheckedException;

	/**
	 * Deletes {@link Media_Plan_Option__c} from salesforce database by passing the array of <code>optionIds</code>
	 * @param 	optionIds
	 * 			Array of {@link Media_Plan_Option__c} object ids
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	void deleteSalesforceObjects(String[] optionIds) throws CustomCheckedException;

	/**
	 * Creates and saves {@link Media_Plan_Option__c} in salesforce from AMPT by passing <code>optionArr</code> which is the array of {@link Media_Plan_Option__c} objects
	 * @param 	optionArr
	 * 			Array of {@link Media_Plan_Option__c} object which has to be saved in the salesforce
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	void createSalesforceObjects(SObject[] optionArr) throws CustomCheckedException;

	/**
	 * Updates {@link Media_Plan__c} in salesforce from AMPT by passing <code>sfProposal</code> which is the {@link Media_Plan__c} object which has to be updated in the salesforce database
	 * @param 	sfProposal
	 * 			{@link Media_Plan__c} object which has to be updated in the salesforce database
	 * @throws 	CustomCheckedException
	 * 			This is to generate a customised messages throughout the application whenever SALESFORCE_CONNECTION_ERROR occurs
	 */
	void updateMediaPlanToSalesforce(final Media_Plan__c sfProposal) throws CustomCheckedException;
	
	/**
	 * @param salesforceId
	 * @return
	 * @throws CustomCheckedException
	 */
	List<Media_Plan__c> getMediaPlanBySfId(String salesforceId) throws CustomCheckedException;
}