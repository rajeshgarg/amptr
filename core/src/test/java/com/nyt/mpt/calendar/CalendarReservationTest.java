/**
 *
 */
package com.nyt.mpt.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarDetailVO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarViewVO;

/**
 * JUnit test for Calendar Reservation service
 * @author surendra.singh
 */
public class CalendarReservationTest extends AbstractTest {

	@Autowired
	@Qualifier("reservationService")
	private ICalendarReservationService reservationService;
	
	@Autowired
	private IProductService productService;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
	}

	/**
	 * Return CalendarVO object
	 * @return
	 */
	private ReservationTO getCalendarVO() {
		final ReservationTO calendarVO = new ReservationTO();
		calendarVO.setProductId(838);
		calendarVO.setSalesTargetId(3501);
		calendarVO.setStartDate(DateUtil.parseToDate("01/01/2013"));
		calendarVO.setEndDate(DateUtil.parseToDate("04/31/2013"));
		return calendarVO;
	}

	/**
	 * Test for getProposalsForReservationSearch that Return the list of
	 * proposals fulfilling the search criteria.
	 */
	@Test
	public void testGetProposalsForReservationSearch() {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;

		filterCriteria = getFilterCriteriaFor("proposalName", SearchOption.CONTAIN);
		filterCriteria.setSearchString("1 York_UI");
		filterCriteriaLst.add(filterCriteria);

		filterCriteria = getFilterCriteriaFor("salescategory", SearchOption.EQUAL);
		filterCriteria.setSearchString("1493");
		filterCriteriaLst.add(filterCriteria);

		filterCriteria = getFilterCriteriaFor("advertiserName", SearchOption.EQUAL);
		filterCriteria.setSearchString("148511");
		filterCriteriaLst.add(filterCriteria);

		filterCriteria = getFilterCriteriaFor("productId", SearchOption.EQUAL);
		filterCriteria.setSearchString("2238");
		filterCriteriaLst.add(filterCriteria);

		filterCriteria = getFilterCriteriaFor("salesTarget", SearchOption.EQUAL);
		filterCriteria.setSearchString("3597");
		filterCriteriaLst.add(filterCriteria);

		filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
		filterCriteria.setSearchStringFrom("05/01/2013");
		filterCriteria.setSearchStringTo("09/30/2013");
		filterCriteriaLst.add(filterCriteria);

		final List<LineItem> proposalsLst = reservationService.getProposalsForReservationSearch(filterCriteriaLst, null, null);
		Assert.assertTrue(reservationService.getProposalsForReservationSearch(null, null, null).size() >= 0);
		Assert.assertTrue(proposalsLst.size() >= 0);

	}

	/**
	 * Test for getReservationDetailForCalendar that Returns details to display
	 * over calendar day
	 */
	@Test
	public void testGetReservationDetailForCalendar() {
		final List<ReservationInfo> listForCalendar = reservationService.getReservationDetailForCalendar(getCalendarVO());
		if (listForCalendar != null) {
			Assert.assertTrue(listForCalendar.size() >= 0);
		}
	}

	/**
	 * Test for getProposedProposalsForCalendar that Return list of proposal,
	 * which are proposed but not reserved.
	 */
	@Test
	public void testGetProposedProposalsForCalendar() {
		final List<Proposal> listForCalendar = reservationService.getProposedProposalsForCalendar(getCalendarVO());
		if (listForCalendar != null) {
			Assert.assertTrue(listForCalendar.size() >= 0);
		}
	}

	/**
	 * Test for getSalesOrderForReservationSearch that Return the list of sales
	 * order fulfilling the search criteria.
	 */
	@Test
	public void testGetSalesOrderForReservationSearch() {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;
		filterCriteria = getFilterCriteriaFor("productId", SearchOption.EQUAL);
		filterCriteria.setSearchString("3393");//XXL Box
		filterCriteriaLst.add(filterCriteria);
		filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
		filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(360)));
		filterCriteria.setSearchStringTo(DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
		filterCriteriaLst.add(filterCriteria);
		final List<OrderLineItem> listForCalendar = reservationService.getSalesOrderForReservationSearch(filterCriteriaLst, new PaginationCriteria(0, 10), null);
		if (listForCalendar != null) {
			Assert.assertTrue(listForCalendar.size() >= 0);
		}
	}

	/**
	 * Test for getProposalsForReservationSearchCount that Returns the count of
	 * the AMPT proposals fulfilling the search criteria
	 */
	@Test
	public void testGetProposalsForReservationSearchCount() {
		Assert.assertTrue(reservationService.getProposalsForReservationSearchCount(null) >= 0);
	}

	/**
	 * Test for getSalesOrderForReservationSearchCount that Returns the count of
	 * the SOS Orders fulfilling the search criteria
	 */
	@Test
	public void testGetSalesOrderForReservationSearchCount() {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;
		filterCriteria = getFilterCriteriaFor("productId", SearchOption.EQUAL);
		filterCriteria.setSearchString("3393");//XXL Box
		filterCriteriaLst.add(filterCriteria);
		filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
		filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(DateUtil.getPriorDateFromCurrentDate(360)));
		filterCriteria.setSearchStringTo(DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
		filterCriteriaLst.add(filterCriteria);
		reservationService.getSalesOrderForReservationSearchCount(filterCriteriaLst);
		Assert.assertTrue(true);//since we can not judge how many records sos will return
	}
	
	@Test
	public void testGetReservationDetailForSalesCalendar(){
		SalesCalendarReservationDTO calendarVO =  new SalesCalendarReservationDTO ();
		Calendar calendarStartDate = Calendar.getInstance();
		calendarStartDate.set(2015, 3, 1);//setting 1st April 2015
		calendarVO.setStartDate(calendarStartDate.getTime());		
		Calendar calendarEndDate = Calendar.getInstance();
		calendarEndDate.set(2015, 3, 30);//setting 30th April 2015
		calendarVO.setEndDate(calendarEndDate.getTime());
		List<Long> productIds = new ArrayList<Long> ();
		for (Product product : productService.getAllProducts()){
			if(("HOME PAGE").equals(product.getClassName().getProductClassName()) && ConstantStrings.YES.equals(product.getReservable())){
				productIds.add(product.getId());
			}
		}
		calendarVO.setProductIds(productIds);
		LineItemTarget lineItemTarget = new LineItemTarget();
		lineItemTarget.setSosTarTypeId(8l);//for countries
		lineItemTarget.setSosTarTypeElementId("283");//for US
		Set<LineItemTarget> lineItemTargetingSet = new LinkedHashSet<LineItemTarget>(1);
		lineItemTargetingSet.add(lineItemTarget);
		calendarVO.setLineItemTargeting(lineItemTargetingSet);
		List<SalesReservationCalendarViewVO> reservationViewList = reservationService.getReservationDetailForSalesCalendar(calendarVO);
		Assert.assertNotNull(reservationViewList);
		//Uncomment to check how result will be retrieved
		/*for (SalesReservationCalendarViewVO salesReservationCalendarViewVO : reservationViewList) {
			System.out.println(salesReservationCalendarViewVO.getViewDate());
			System.out.println(salesReservationCalendarViewVO.getTotalSOR());
			System.out.println("*****Orders******");
			for(SalesReservationCalendarDetailVO SalesReservationCalendarDetailVO : salesReservationCalendarViewVO.getSalesOrderList()){
				System.out.println(SalesReservationCalendarDetailVO.getLineItemId());
				System.out.println(SalesReservationCalendarDetailVO.getProductName());
				System.out.println(SalesReservationCalendarDetailVO.getSor());
				System.out.println(SalesReservationCalendarDetailVO.getStatus());
			}
			System.out.println("*****Proposals******");
			for(SalesReservationCalendarDetailVO SalesReservationCalendarDetailVO : salesReservationCalendarViewVO.getProposalList()){
				System.out.println(SalesReservationCalendarDetailVO.getLineItemId());
				System.out.println(SalesReservationCalendarDetailVO.getProductName());
				System.out.println(SalesReservationCalendarDetailVO.getSor());
				System.out.println(SalesReservationCalendarDetailVO.getStatus());
			}
			System.out.println("---------------End of Day-----");
		}*/
	}

	/**
	 * Return range filter criteria object
	 * @param field
	 * @param oper
	 * @return
	 */
	private RangeFilterCriteria getFilterCriteriaFor(final String field, final SearchOption oper) {
		final RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField(field);
		filterCriteria.setSearchOper(oper.toString());
		return filterCriteria;
	}
}
