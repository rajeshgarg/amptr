/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationDetails;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationListViewVO;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarViewVO;

/**
 * This <code>ICalendarReservationService</code> interface includes all declarations for the methods to get the reservation data for calendar
 * 
 * @author surendra.singh
 */

public interface ICalendarReservationService {

	/**
	 * Returns List of {@link ReservationInfo} for particular month to display in calendar
	 * @param 	reservationTO
	 * 			{@link ReservationTO}
	 * @return
	 * 			Returns List of {@link ReservationInfo}
	 */
	List<ReservationInfo> getReservationDetailForCalendar (ReservationTO reservationTO);
	
	/**
	 * Returns List of {@link Proposal}, which are proposed but not reserved.
	 * @param reservationTO
	 * @return
	 */
	List<Proposal> getProposedProposalsForCalendar (ReservationTO reservationTO);
	
	/**
	 * Returns the List of {@link OrderLineItem} fulfilling the search criteria.
	 * @param 	criteriaLst
	 * 			List of {@link RangeFilterCriteria}
	 * @param 	pgCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 * 			Returns the List of {@link OrderLineItem}
	 */
	List<OrderLineItem> getSalesOrderForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria);
	
	/**
	 * Returns the List of {@link LineItem} fulfilling the search criteria.
	 * @param 	criteriaLst
	 * 			List of {@link RangeFilterCriteria}
	 * @param 	pgCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 * 			Returns the List of {@link LineItem}
	 */
	List<LineItem> getProposalsForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria);
	
	/**
	 * Returns the count of the AMPT proposals fulfilling the search criteria
	 * @param 	criteriaLst
	 * 			List of {@link RangeFilterCriteria}
	 * @return
	 */
	int getProposalsForReservationSearchCount(final List<RangeFilterCriteria> criteriaLst);

	/**
	 * Returns the count of the AMPT sales orders for reservation fulfilling the search criteria
	 * @param criteriaLst
	 * @return
	 */
	int getSalesOrderForReservationSearchCount(List<RangeFilterCriteria> criteriaLst);
	
	/**
	 * Returns the List of {@link ReservationListViewVO} for the list view for the particular day in a month
	 * @param 	reservationTO
	 * 			{@link ReservationTO}
	 */
	List<ReservationListViewVO> getReservationDetailForListView(final ReservationTO reservationTO);

	/**
	 * Returns the {@link ReservationDetails}
	 * @param 	reservationTO
	 * 			{@link ReservationTO}
	 */
	ReservationDetails getReservationDetails(final ReservationTO reservationTO);
	
	/**
	 * @param calendarVO
	 * @return
	 */
	List<SalesReservationCalendarViewVO> getReservationDetailForSalesCalendar(final SalesCalendarReservationDTO calendarVO);

	/**
	 * returns list of proposal containing proposed Line Items
	 * @param salesCalendarReservationDTO
	 * @return
	 */
	List<Proposal> getProposedProposalsForSalesCalendar(SalesCalendarReservationDTO salesCalendarReservationDTO); 
}
