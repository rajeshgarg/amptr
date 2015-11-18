/**
 * 
 */
package com.nyt.mpt.service;

import java.util.Date;
import java.util.List;

import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>IEmailScheduleService</code> contains all the method declarations
 * for the EmailSchedule related operations like : saving/deleting/updating
 * EmailSchedules, fetching EmailSchedule etc
 * 
 * @author Gurditta.Garg
 */

public interface IEmailScheduleService {

	/**
	 * Saves {@link EmailSchedule} to the AMPT database
	 * @param 	entity
	 * 			{@link EmailSchedule} to be saved in the AMPT db
	 * @return
	 * 			Returns the saved {@link EmailSchedule}
	 */
	EmailSchedule saveEmailSchedule(EmailSchedule entity);

	/**
	 * Updates {@link EmailSchedule}
	 * @param 	entity
	 * 			{@link EmailSchedule} to be updated in the AMPT db
	 */
	EmailSchedule updateEmailSchedule(EmailSchedule entity);

	/**
	 * Deletes {@link EmailScheduleDetails} from AMPT database
	 * @param 	emailDetailId
	 * 			{@link EmailScheduleDetails}'s Id to be deleted from the AMPT db
	 */
	void deleteEmailSchedule(Long emailDetailId);

	/**
	 * Returns the List of {@link EmailSchedule} on the basis of <code>filterCriteriaLst</code>, <code>paginationCriteria</code>, <code>sortingCriteria</code>
	 * @param 	filterCriteriaLst
	 * 			{@link FilterCriteria}
	 * @param 	paginationCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortingCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 * 			Returns the List of {@link EmailSchedule}
	 */
	List<EmailSchedule> getFilteredEmailDetailList(List<FilterCriteria> filterCriteriaLst, PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);

	/**
	 * Returns the count of the List of the filtered EmailDetailList by <code>filterCriteriaLst</code>
	 * @param 	filterCriteriaLst
	 * 			{@link FilterCriteria}
	 * @return
	 */
	int getFilteredEmailDetailListSize(List<FilterCriteria> filterCriteriaLst);

	/**
	 * Returns <code>true</code> if, and only if, there is any duplicate data
	 * @param 	emailSchedule
	 * 			{@link EmailSchedule} has all the information to find out the duplicate data
	 * @return
	 * 			Returns <code>true</code> or <code>false</code>
	 */
	boolean findDuplicateDataExists(EmailSchedule emailSchedule);

	/**
	 * Returns {@link EmailSchedule} by <code>productId</code>, <code>salesTargetId</code>, <code>sendDate</code>
	 * @param productId
	 * @param salesTargetId
	 * @param sendDate
	 * @return
	 */
	EmailSchedule getEmailScheduleByDate(final Long productId, final Long salesTargetId, final Date sendDate);

}
