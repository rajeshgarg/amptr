/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.Region;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;

/**
 * This <code>ICalendarReservationDao</code> interface includes all declarations for the methods to get the reservation data for calendar
 * 
 * @author surendra.singh
 */

public interface ICalendarReservationDao {

	/**
	 * Returns the List of {@link SalesOrder} to display it in the calendar
	 * @param 	reservationTO
	 * 			{@link ReservationTO}
	 * @return
	 * 			Returns the List of {@link SalesOrder}
	 */
	List<SalesOrder> getSalesOrderForCalendar(ReservationTO reservationTO);

	/**
	 * Returns the List of ids of Products from <code>PRODUCT_BASE</code> table in SOS
	 * @param productIds
	 * @return
	 */
	List<Long> getConflictingProductIdList(List<Long> productIds);

	/**
	 * Returns the list of SOS {@link OrderLineItem} fulfilling the <code>criteriaLst</code>, <code>pgCriteria</code>, <code>sortCriteria</code>
	 * @param 	criteriaLst
	 * 			List of {@link RangeFilterCriteria}
	 * @param 	pgCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 */
	List<OrderLineItem> getSalesOrderForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria);

	/**
	 * Returns the count of the SOS Orders fulfilling the {@link RangeFilterCriteria}
	 * @param 	criteriaLst
	 * 			List of {@link RangeFilterCriteria}
	 * @return
	 * 			Returns the count of SOS orders as an int value
	 */
	int getSalesOrderForReservationSearchCount(final List<RangeFilterCriteria> criteriaLst);

	/**
	 * Returns the List of {@link Region} from SOS database
	 * @return
	 */
	List<Region> getRegionList();

	/**
	 * Returns the reserved Orders for sales calendar
	 * @param calendarVO
	 * @return
	 */
	List<SalesOrder> getSalesOrderForSalesCalendar(	SalesCalendarReservationDTO calendarVO);
}
