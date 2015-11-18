/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.nyt.mpt.dao.IEmailScheduleDAO;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.service.IEmailScheduleService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>EmailScheduleService</code> contains all the methods for
 * the EmailSchedule related operations like : saving/deleting/updating
 * EmailSchedules, fetching EmailSchedule etc
 * @author Gurditta.Garg
 */

public class EmailScheduleService  implements IEmailScheduleService{
	
	private IEmailScheduleDAO emailScheduleDAO;
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#saveEmailSchedule(com.nyt.mpt.domain.EmailSchedule)
	 */
	@Override
	public EmailSchedule saveEmailSchedule(final EmailSchedule entity) {
		return emailScheduleDAO.saveEmailSchedule(entity);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#updateEmailSchedule(com.nyt.mpt.domain.EmailSchedule)
	 */
	@Override
	public EmailSchedule updateEmailSchedule(final EmailSchedule entity) {
		//fetching the objects form the database
		EmailSchedule emailScheduleDB = emailScheduleDAO.getEmailScheduleById(entity.getEmailScheduleId());
		EmailScheduleDetails emailDtls = entity.getEmailSchedules().get(0);
		//setting the values of the entity object
		if(emailDtls.getEmailScheduleDetailsId() == 0){
			emailDtls.setEmailSchedule(emailScheduleDB);
			emailScheduleDAO.saveEmailScheduleDetails(emailDtls);
		}else{
			for(EmailScheduleDetails emailScheduleDetails : emailScheduleDB.getEmailSchedules()){
				if(emailScheduleDetails.getEmailScheduleDetailsId() == emailDtls.getEmailScheduleDetailsId()){
					emailScheduleDetails.setStartDate(emailDtls.getStartDate());
					emailScheduleDetails.setEndDate(emailDtls.getEndDate());
					emailScheduleDetails.setForever(emailDtls.isForever());
					emailScheduleDetails.setWeekdays(emailDtls.getWeekdays());
					emailScheduleDetails.setFrequency(emailDtls.getFrequency());
					emailScheduleDAO.updateEmailScheduleDetails(emailScheduleDetails);
					break;
				}
			}
		}
		return entity;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#deleteEmailSchedule(com.nyt.mpt.domain.EmailSchedule)
	 */
	@Override
	public void deleteEmailSchedule(final Long emailDetailId) {		
		EmailScheduleDetails emailScheduleDetail = emailScheduleDAO.getEmailScheduleDetailsById(emailDetailId);
		emailScheduleDAO.deleteEmailScheduleDetails(emailScheduleDetail);		
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#checkDuplicateDataExists(com.nyt.mpt.domain.EmailSchedule)
	 */
	@Override
	public boolean findDuplicateDataExists(EmailSchedule emailSchedule) {
		EmailScheduleDetails emailScheduleDetail = emailSchedule.getEmailSchedules().get(0);
		return  emailScheduleDAO.findDuplicateDate(emailScheduleDetail.getStartDate(),emailScheduleDetail.getEndDate(), emailSchedule.getEmailScheduleId(), emailScheduleDetail.getEmailScheduleDetailsId());
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#getFilteredEmailDetailList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<EmailSchedule> getFilteredEmailDetailList(List<FilterCriteria> filterCriteriaLst, PaginationCriteria paginationCriteria,SortingCriteria sortingCriteria) {
		final List<EmailSchedule>  scheduleDetail = emailScheduleDAO.getFilteredEmailDetailList(filterCriteriaLst, paginationCriteria, sortingCriteria);
		for (EmailSchedule emailSchedule : scheduleDetail) {
			Hibernate.initialize(emailSchedule.getEmailSchedules());
		}
		return scheduleDetail; 
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#getFilteredEmailDetailListSize(java.util.List)
	 */
	@Override
	public int getFilteredEmailDetailListSize(List<FilterCriteria> filterCriteriaLst) {
		return emailScheduleDAO.getFilteredEmailDetailListSize(filterCriteriaLst);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IEmailScheduleService#getEmailScheduleByDate(java.lang.Long, java.lang.Long, java.util.Date)
	 */
	@Override
	public EmailSchedule getEmailScheduleByDate(final Long productId, final Long salesTargetId, final Date sendDate){
		return emailScheduleDAO.getEmailScheduleByDate(productId, salesTargetId, sendDate);
	}

	public void setEmailScheduleDAO(final IEmailScheduleDAO emailScheduleDAO) {
		this.emailScheduleDAO = emailScheduleDAO;
	}
}

