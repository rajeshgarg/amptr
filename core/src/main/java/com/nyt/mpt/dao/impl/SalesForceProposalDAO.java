/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.nyt.mpt.dao.ISalesForceProposalDAO;
import com.nyt.mpt.domain.Notes;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.SalesforceProposalEmailFlag;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.fault.ExceptionCode;
import com.sforce.soap.enterprise.fault.UnexpectedErrorFault;
import com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c;
import com.sforce.soap.enterprise.sobject.Attachment;
import com.sforce.soap.enterprise.sobject.Media_Plan__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Option__c;
import com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;

/**
 * This <code>SalesForceProposalDAO</code> class has all the CRUD i.e. create, read, update and delete operations. All operations are done in salesforce's
 * database which are done through salesforce's SOQL i.e. salesforce object query language queries. This <code>SalesForceProposalDAO</code> class includes methods for saving {@link Media_Plan__c}, {@link Media_Plan_Revision__c},
 * {@link Media_Plan_Option__c} in AMPT and also updating in salesforce from AMPT, for fetching all the {@link Media_Plan__c}, {@link Media_Plan_Revision__c}, {@link Media_Plan_Option__c} and {@link Attachment} from salesforce. This class also includes
 * a method to get {@link EnterpriseConnection} to communicate from AMPT to salesforce and vice-versa.
 * @author Gurditta.Garg
 */
public class SalesForceProposalDAO extends GenericDAOImpl implements ISalesForceProposalDAO, ApplicationContextAware {
	
	private EnterpriseConnection connection;
	
	private ApplicationContext applicationContext;
	
	private static final Logger LOGGER = Logger.getLogger(SalesForceProposalDAO.class);

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#getSalesForceProposals(int)
	 */
	@Override
	public List<Media_Plan__c> getSalesForceProposals(final int fetchDurationInMinutes) throws ConnectionException {
		QueryResult queryResults = null;
		final String queryString = "SELECT Id, Name, CreatedBy.UserRole.Name, CreatedBy.Name, CreatedBy.Email, Campaign_Objective__c, Due_On_Date__c, Due_On_Time__c,"
				+ "Advertiser_Name__r.Name, Owner.Name, Agency__r.Name, Price_Type__c, Budget__c, End_Date__c, Start_Date__c, Submitted_Date__c, Priority__c, Notes__c,"
				+ "Proposal_Curency__c, Sales_Category__c, Agency_Margin__c FROM Media_Plan__c WHERE AMPT_Key__c = '' AND AMPT_Link__c = '' AND Submitted_Date__c >= "
				+ DateUtil.convertToSFDateFormatForFetching(fetchDurationInMinutes);
		try {
			queryResults = getConnection().query(queryString);
		} catch (ConnectionException exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE, exception);
			throw new ConnectionException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
		} catch (Exception exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR);
			throw new BusinessException(ConstantStrings.SALESFORCE_GENERIC_ERROR, new CustomBusinessError(), exception);
		}
		LOGGER.info("Queried Number of Proposals from Salesforce : " + queryResults.getSize());
		
		final List<Media_Plan__c> sfProposalList = new ArrayList<Media_Plan__c>(queryResults.getSize());
		populateData(queryResults, sfProposalList);
		return sfProposalList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#fetchAttachmentsFromSalesforce(java.lang.String)
	 */
	@Override
	public List<Attachment> fetchAttachmentsFromSalesforce(final String parentId) throws ConnectionException {
		QueryResult queryResults = null;
		final String queryString = "SELECT Name, Body, ContentType, Description, BodyLength FROM Attachment WHERE ParentId in ('" + parentId + "')";
		try {
			queryResults = getConnection().query(queryString);
		} catch (ConnectionException exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
			throw new ConnectionException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
		} catch (Exception exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR);
			throw new BusinessException(ConstantStrings.SALESFORCE_GENERIC_ERROR, new CustomBusinessError(), exception);
		}

		final List<Attachment> attachmentList = new ArrayList<Attachment>(queryResults.getSize());
		populateData(queryResults, attachmentList);
		return attachmentList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#fetchProposalsRevisions(int)
	 */
	@Override
	public List<Media_Plan_Revision__c> fetchProposalsRevisions(final int fetchDurationInMinutes) throws ConnectionException {
		QueryResult queryResults = null;
		final String queryString = "SELECT Name, Id, CreatedBy.UserRole.Name, Revision_Type__c, LastModifiedDate, CreatedBy.Name, Campaign_Name__r.id, Revision_Note__c, Version_Number__c,Advertisers_Billing_Address__c,"
				+ "Billing_Contact_Email__c, Billing_Contact_Phone__c, Billing_Contact__r.Full_Name__c, Media_Contact__r.Full_Name__c, Media_Contact_Email__c, Media_Contact_Phone__c, Geo_Region__c, Client_PO_Number__c,"
				+ "Revision_Submitter__c, Submitter_Email__c, CreatedDate FROM Media_Plan_Revision__c WHERE Created_In_AMPT__c = null AND Date_Submitted__c != null AND LastModifiedDate >= " 
				+ DateUtil.convertToSFDateFormatForFetching(fetchDurationInMinutes);
		try {
			queryResults = getConnection().query(queryString);
		} catch (ConnectionException exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
			throw new ConnectionException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
		} catch (Exception exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR);
			throw new BusinessException(ConstantStrings.SALESFORCE_GENERIC_ERROR, new CustomBusinessError(), exception);
		}
		LOGGER.info("Queried Number of Proposals Revisions from Salesforce: " + queryResults.getSize());
		
		final List<Media_Plan_Revision__c> revisionList = new ArrayList<Media_Plan_Revision__c>(queryResults.getSize());
		populateData(queryResults, revisionList);
		return revisionList;
	}

	/**
	 * This method populates the salesforce {@link QueryResult} to the {@code list} of type which we pass as an argument
	 * @param 	queryResults
	 * 			When a SOQL query is executed to salesforce's database.com, it returns {@link QueryResult}
	 * @param 	list
	 * 			Generic {@code list} which we want to get populated from the {@code queryResults}
	 */
	@SuppressWarnings("unchecked")
	private <T> void populateData(QueryResult queryResults, final List<T> list) {
		if (queryResults.getSize() > 0) {
			boolean done = false;
			while (!done) {
				final SObject[] records = queryResults.getRecords();
	            for (int i = 0; i < records.length; i++) {
	            	list.add((T) queryResults.getRecords()[i]);
	            }
				try {
					if (queryResults.isDone()) {
						done = true;
					} else {
						queryResults = connection.queryMore(queryResults.getQueryLocator());
					}
				} catch (ConnectionException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#updateProposalRevisionToSalesforce(com.sforce.soap.enterprise.sobject.Media_Plan_Revision__c)
	 */
	@Override
	public void updateProposalRevisionToSalesforce(final Media_Plan_Revision__c sfProposalRevision) throws ConnectionException {
		try {
			getConnection().update(new SObject[] { sfProposalRevision });
		} catch (ConnectionException exception) {
			LOGGER.error(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
			throw new ConnectionException(ConstantStrings.SALESFORCE_CONNECTION_ERROR_FETCH_UPDATE);
		} catch (Exception e) {
			LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR);
			throw new BusinessException(ConstantStrings.SALESFORCE_GENERIC_ERROR, new CustomBusinessError(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#getEmailFlagBySalesforceId(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public SalesforceProposalEmailFlag getEmailFlagBySalesforceId(final String salesforceId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesforceProposalEmailFlag.class);
		criteria.add(Restrictions.eq("salesforceId", salesforceId));
		final List<SalesforceProposalEmailFlag> flagsList = findByCriteria(criteria);
		if (flagsList != null && !flagsList.isEmpty()) {
			return flagsList.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#saveProposalSF(com.nyt.mpt.domain.Proposal)
	 */
	@Override
	public Proposal saveProposalSF(final Proposal proposal) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Proposal with proposalId: " + proposal.getId());
		}
		saveOrUpdate(proposal);
		return proposal;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#saveNotesSF(com.nyt.mpt.domain.Notes)
	 */
	@Override
	public long saveNotesSF(final Notes note) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Proposal notes with note Id: " + note.getId());
		}
		saveOrUpdate(note);
		return note.getId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#saveOrUpdateEmailFlag(com.nyt.mpt.domain.SalesforceProposalEmailFlag)
	 */
	@Override
	public SalesforceProposalEmailFlag saveOrUpdateEmailFlag(final SalesforceProposalEmailFlag emailFlag) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving salesforce Proposal Email Flag with salesforceId: " + emailFlag.getSalesforceId());
		}
		saveOrUpdate(emailFlag);
		return emailFlag;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#getProposalBySalesforceId(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Proposal getProposalBySalesforceId(final String salesforceID) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.eq("salesforceID", salesforceID));
		final List<Proposal> proposalList = findByCriteria(criteria);
		if (proposalList != null && !proposalList.isEmpty()) {
			return proposalList.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#saveProposalAuditLogInSalesforce(com.sforce.soap.enterprise.sobject.Ad_Systems_Workflow_Update__c)
	 */
	@Override
	public void saveProposalAuditLogInSalesforce(final Ad_Systems_Workflow_Update__c ad_Systems_Workflow_Update__c) throws CustomCheckedException {
		try {
			getConnection().create(new SObject[] { ad_Systems_Workflow_Update__c });
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#updateMediaPlanToSalesforce(com.sforce.soap.enterprise.sobject.Media_Plan__c)
	 */
	@Override
	public void updateMediaPlanToSalesforce(final Media_Plan__c sfProposal) throws CustomCheckedException {
		try {
			getConnection().update(new SObject[] { sfProposal });
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#getOptionDetailsFromSf(java.lang.String)
	 */
	@Override
	public List<Media_Plan_Option__c> getOptionDetailsFromSf(String salesforceId) throws CustomCheckedException {
		QueryResult queryResults = null;
		final String queryString = "SELECT  CurrencyIsoCode, Default__c, IsDeleted,  Media_Plan__c, Option_Name__c, Name, Proposed_Value__c, Id, Sold_Value__c , Agency_Margin__c, Price_Type__c" +
				" FROM Media_Plan_Option__c WHERE IsDeleted= false and Media_Plan__c = '" + salesforceId + "'";
		try {
			queryResults = getConnection().query(queryString);
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
		LOGGER.info("Queried Number of Proposals Options from Salesforce: " + queryResults.getSize());
		final List<Media_Plan_Option__c> optionList = new ArrayList<Media_Plan_Option__c>(queryResults.getSize());
		populateData(queryResults, optionList);
		return optionList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#getMediaPlanBySfId(java.lang.String)
	 */
	@Override
	public List<Media_Plan__c> getMediaPlanBySfId(String salesforceId) throws CustomCheckedException {
		QueryResult queryResults = null;
		final String queryString = "SELECT Id, Submitted_Date__c FROM Media_Plan__c WHERE Id = '" + salesforceId + "'";
		try {
			queryResults = getConnection().query(queryString);
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
		final List<Media_Plan__c> optionList = new ArrayList<Media_Plan__c>(queryResults.getSize());
		populateData(queryResults, optionList);
		return optionList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#updateOptionDetailsInSalesforce(com.sforce.soap.enterprise.sobject.SObject[])
	 */
	@Override
	public void updateSalesforceObjects(SObject [] sObjects) throws CustomCheckedException {
		try {
			getConnection().update(sObjects);
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#deleteOptionsInSf(java.lang.String[])
	 */
	@Override
	public void deleteSalesforceObjects(String[] ids) throws CustomCheckedException {
		try {
			getConnection().delete(ids);
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ISalesForceProposalDAO#createOptionsInSF(com.sforce.soap.enterprise.sobject.Proposal_Option__c)
	 */
	@Override
	public void createSalesforceObjects(SObject[] sObjects) throws CustomCheckedException {
		try {
			getConnection().create(sObjects);
		} catch (Exception e) {
			createCustomCheckedException(e);
		}
	}
	
	/**
	 * To create a {@link CustomCheckedException} from {@link Exception}. 
	 * This is to create a generic SALESFORCE_CONNECTION_ERROR exception so that a customised messages could be used throughout the application where a  SALESFORCE_CONNECTION_ERROR occurs
	 * @param 	exception
	 * 			{@link Exception}
	 * @throws CustomCheckedException
	 */
	private void createCustomCheckedException(Exception exception) throws CustomCheckedException {
		LOGGER.error(ConstantStrings.SALESFORCE_GENERIC_ERROR, exception);
		final CustomBusinessError customBusinessError = new CustomBusinessError();
		customBusinessError.setErrorKey(ErrorCodes.salesforceConnectionError.getResourceName());
		customBusinessError.setMessageType("SALESFORCE_CONNECTION_ERROR");
		throw new CustomCheckedException(customBusinessError);
	}
	
	/**
	 * Returns the {@link EnterpriseConnection} to communicate with salesforce from AMPT or vice-versa. 
	 * Whenever any CRUD operation we do from AMPT to salesforce or vice-versa it requires {@link EnterpriseConnection} to do that. 
	 * @return 	Returns the {@link EnterpriseConnection} to communicate with salesforce from AMPT or vice-versa. 
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
	private EnterpriseConnection getConnection() throws ConnectionException {
		try {
			this.connection.getUserInfo();
		} catch (UnexpectedErrorFault uef) {
			if (ExceptionCode.INVALID_SESSION_ID.equals(uef.getExceptionCode())) {
				this.connection = this.applicationContext.getBean("enterPriseConnection", EnterpriseConnection.class);
			}
		}
		return this.connection;
	}
	
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}	
	
	public void setConnection(final EnterpriseConnection connection) {
		this.connection = connection;
	}
}