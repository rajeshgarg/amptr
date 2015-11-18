/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.Date;
import java.util.List;

import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>IEmailScheduleDAO</code> contains all the method declarations for
 * the EmailSchedule related operations like : saving/deleting/updating
 * EmailSchedules, fetching EmailSchedule etc
 * 
 * @author Gurditta.Garg
 */

public interface IEmailScheduleDAO {

	/**
	 * Saves {@link EmailSchedule} to the AMPT database
	 * @param 	entity
	 * 			{@link EmailSchedule} to be saved in the AMPT db
	 * @return
	 * 			Returns the saved {@link EmailSchedule}
	 */
	EmailSchedule saveEmailSchedule(EmailSchedule entity);

	/**
	 * Updates {@link EmailScheduleDetails} to the AMPT database
	 * @param 	entity
	 * 			{@link EmailScheduleDetails} to be updated in the AMPT database
	 */
	void updateEmailScheduleDetails(EmailScheduleDetails entity);

	/**
	 * Saves {@link EmailScheduleDetails} to the AMPT database
	 * @param 	entity
	 * 			{@link EmailScheduleDetails} to be saved in the AMPT database
	 */
	void saveEmailScheduleDetails(EmailScheduleDetails entity);

	/**
	 * Deletes {@link EmailScheduleDetails} from AMPT database
	 * @param 	entity
	 * 			{@link EmailScheduleDetails} to be deleted from the AMPT db
	 */
	void deleteEmailScheduleDetails(EmailScheduleDetails entity);

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
	List<EmailSchedule> getFilteredEmailDetailList(List<FilterCriteria> filterCriteriaLst, PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria);;

	/**
	 * Returns the count of the List of the filtered EmailDetailList by <code>filterCriteriaLst</code>
	 * @param 	filterCriteriaLst
	 * 			{@link FilterCriteria}
	 * @return
	 */
	int getFilteredEmailDetailListSize(List<FilterCriteria> filterCriteriaLst);

	/**
	 * Returns the {@link EmailSchedule} by <code>emailScheduleId</code>
	 * @param 	emailScheduleId
	 * 			This is the Id of email schedule for which we've to find {@link EmailSchedule}
	 * @return
	 * 			Returns the {@link EmailSchedule}
	 */
	EmailSchedule getEmailScheduleById(long emailScheduleId);

	/**
	 * Returns <code>true</code> if, and only if, there is any duplicate date
	 * @param startDate
	 * @param endDate
	 * @param emailScheduleId
	 * @param emailScheduleDetailsId
	 * @return
	 */
	boolean findDuplicateDate(Date startDate, Date endDate, Long emailScheduleId, Long emailScheduleDetailsId);

	/**
	 * Returns {@link EmailScheduleDetails} by <code>emailScheduleDetailsId</code>
	 * @param 	emailScheduleDetailsId
	 * 			This is the Id of email schedule details for which we've to find {@link EmailScheduleDetails}
	 * @return
	 * 			Returns {@link EmailScheduleDetails}
	 */
	EmailScheduleDetails getEmailScheduleDetailsById(long emailScheduleDetailsId);

	/**
	 * Returns {@link EmailSchedule} by <code>productId</code>, <code>salesTargetId</code>, <code>sendDate</code>
	 * @param productId
	 * @param salesTargetId
	 * @param sendDate
	 * @return
	 */
	EmailSchedule getEmailScheduleByDate(final Long productId, final Long salesTargetId, final Date sendDate);
}
