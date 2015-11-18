/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.nyt.mpt.dao.ICalendarReservationDao;
import com.nyt.mpt.dao.IProposalDAO;
import com.nyt.mpt.dao.ITargetingDAO;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.sos.Countries;
import com.nyt.mpt.domain.sos.LineItemTargeting;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.Region;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.domain.sos.TargetElement;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IEmailScheduleService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationDetails;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationListViewDetailVO;
import com.nyt.mpt.util.reservation.ReservationListViewVO;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarDetailVO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarViewVO;

/**
 * This <code>CalendarReservationService</code> includes all the methods to get the reservation and it's related data to display in the calendar
 * 
 * @author surendra.singh
 */
public class CalendarReservationService implements ICalendarReservationService {

	private static final String ADX_DMA = "Adx DMA";

	private static final String TARGET_REGION = "Target Region";

	private static final String COUNTRIES = "Countries";

	private static final String US = "US";

	private ICalendarReservationDao reservationDao;
	
	private IProposalDAO proposalDao;

	private ITargetingDAO targetingDao;
	
	private IEmailScheduleService emailScheduleService;
	
	private IProductService productService;
	
	private ISOSService sosService;
	
	private static final Logger LOGGER = Logger.getLogger(CalendarReservationService.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getSalesOrderForCalendar()
	 */
	@Override
	public List<ReservationInfo> getReservationDetailForCalendar(final ReservationTO calendarVO) {
		final Map<Long, String> targetElementMap = targetingDao.getAllTargetTypeElement();
		final Map<Long, String> targetTypeMap = targetingDao.getTargetTypeCriteria();
		final List<Region> regions = reservationDao.getRegionList();
		
		LOGGER.info("Loading Sales Order & Proposals for calendar - " + calendarVO);
		final List<SalesOrder> salesOrderList = reservationDao.getSalesOrderForCalendar(calendarVO);
		LOGGER.info("Sales Order count for calendar - " + salesOrderList.size());
		if (!salesOrderList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromSalesOrder(calendarVO.getLineItemTargeting(), salesOrderList, targetTypeMap, regions);
		}

		final List<Proposal> proposalList = proposalDao.getReservedProposalsForCalendar(calendarVO);
		LOGGER.info("Proposal count for calendar - " + proposalList.size());
		if (!proposalList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromProposal(calendarVO.getLineItemTargeting(), proposalList, targetTypeMap, targetElementMap);
		}
		
		Date currentDate = calendarVO.getStartDate();
		final List<ReservationInfo> reservationList = new ArrayList<ReservationInfo>();
		
		final List<Date> bookingAllowedDates = new ArrayList<Date>();
		if (StringUtils.isNotBlank(calendarVO.getProductType()) && "EMAIL".equals(calendarVO.getProductType())) {
			getEmailAvailableDates(calendarVO, bookingAllowedDates);
		}		 
		
		/**
		 * Calculate Orders, Proposals  count and Booking % for each days between start and end date
		 */		
		while (currentDate.before(calendarVO.getEndDate()) || currentDate.equals(calendarVO.getEndDate())) {
			int orders = 0, proposals = 0; 	
			final List<LineItem> proposalLineItem = new ArrayList<LineItem>();
			final List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
			final ReservationInfo reservationDetail = new ReservationInfo();
			for (SalesOrder salesOrder : salesOrderList) {
				boolean flag = false;
				for (OrderLineItem lineItem : salesOrder.getLineItem()) {
					if(lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
						orderLineItems.add(lineItem);
						if (!flag) {
							flag = true;
							orders++;
						}
					}
				}
			}
			reservationDetail.setOrders(orders);
			for (Proposal proposal : proposalList) {
				boolean flag = false;
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion proposalVersion = options.getLatestVersion();
					for (LineItem lineItem : proposalVersion.getProposalLineItemSet()) {
						if(lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
							/**
							 * Added condition for Calculate Minimum Available Booking % - Needs to ignore for current line item from calculation
							 */
							if (calendarVO.isCalculateSOR() && lineItem.getLineItemID() == calendarVO.getLineItemId()) {
								continue;
							} else {
								proposalLineItem.add(lineItem);
								if (!flag) {
									flag = true;
									proposals++;
									/**
									 * To show check icon on calendar view, represent proposal has booking on a particular date 
									 */
									if (proposal.getId() == calendarVO.getProposalId()) {
										reservationDetail.setBookedForCurrenProposal(true);
									}
								}
							}
						}
					}
				}
			}
			if (StringUtils.isNotBlank(calendarVO.getProductType()) && "EMAIL".equals(calendarVO.getProductType())) {
				reservationDetail.setBookingAllowed(bookingAllowedDates.contains(currentDate));
			} else {
				reservationDetail.setBookingAllowed(true);
			}
			reservationDetail.setProposals(proposals);
			reservationDetail.setSor(calculateSor(orderLineItems, proposalLineItem, targetTypeMap, regions, targetElementMap));
			reservationDetail.setStart(DateUtil.getYieldexDateString(currentDate));
			
			reservationDetail.setOrderLineItems(orderLineItems);
			reservationDetail.setProposalLineItems(proposalLineItem);
			
			reservationList.add(reservationDetail);
			currentDate = DateUtils.addDays(currentDate, 1);
		}
		return reservationList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getSalesOrderForCalendar()
	 */
	@Override
	public List<ReservationListViewVO> getReservationDetailForListView(final ReservationTO calendarVO) {
		final Map<Long, String> targetElementMap = targetingDao.getAllTargetTypeElement();
		final Map<Long, String> targetTypeMap = targetingDao.getTargetTypeCriteria();
		final List<Region> regions = reservationDao.getRegionList();
		
		LOGGER.info("Loading Sales Order & Proposals for calendar - " + calendarVO);
		final List<SalesOrder> salesOrderList = reservationDao.getSalesOrderForCalendar(calendarVO);
		LOGGER.info("Sales Order count for calendar - " + salesOrderList.size());
		if (!salesOrderList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromSalesOrder(calendarVO.getLineItemTargeting(), salesOrderList, targetTypeMap, regions);
		}

		final List<Proposal> proposalList = proposalDao.getReservedProposalsForCalendar(calendarVO);
		LOGGER.info("Proposal count for calendar - " + proposalList.size());
		if (!proposalList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromProposal(calendarVO.getLineItemTargeting(), proposalList, targetTypeMap, targetElementMap);
		}

		Date currentDate = calendarVO.getStartDate();
		final List<ReservationListViewVO> reservationList = new ArrayList<ReservationListViewVO>();

		while (currentDate.before(calendarVO.getEndDate()) || currentDate.equals(calendarVO.getEndDate())) {
			final List<LineItem> proposalLineItem = new ArrayList<LineItem>();
			final List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
			
			final ReservationListViewVO listViewVo = new ReservationListViewVO();			
			listViewVo.setViewDate(DateUtil.formatDateForListView(currentDate));

			for (SalesOrder salesOrder : salesOrderList) {
				for (OrderLineItem lineItem : salesOrder.getLineItem()) {
					if (lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
						final ReservationListViewDetailVO saleOrderVo = new ReservationListViewDetailVO();
						if (salesOrder.getAdvertiser() != null) {
							saleOrderVo.setAdvertiserName(salesOrder.getAdvertiser().getCustomerName());
						}
						saleOrderVo.setCampaignName(salesOrder.getCampaignName());
						saleOrderVo.setLineItemId(lineItem.getLineItemId());
						saleOrderVo.setSalesOrderId(salesOrder.getSalesOrderId());
						saleOrderVo.setSor(lineItem.getShareOfReservation());
						listViewVo.addReservationSalesOrder(saleOrderVo);
						
						orderLineItems.add(lineItem);
					}
				}
			}

			for (Proposal proposal : proposalList) {
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion proposalVersion = options.getLatestVersion();
					for (LineItem lineItem : proposalVersion.getProposalLineItemSet()) {
						if (lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
							final ReservationListViewDetailVO proposalVO = new ReservationListViewDetailVO();
							proposalVO.setAccountManager(proposal.getAccountManager());
							proposalVO.setAdvertiserName(proposal.getSosAdvertiserName());
							proposalVO.setProposalId(proposal.getId());
							proposalVO.setProposalName(proposal.getName());
							proposalVO.setLineItemId(lineItem.getLineItemID());
							proposalVO.setSor(lineItem.getSor());
							proposalVO.setExpirationDate((lineItem.getReservation() != null && lineItem.getReservation().getExpirationDate() != null ) ? String.valueOf(DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())) : ConstantStrings.EMPTY_STRING );
							Calendar todayCal = Calendar.getInstance();
							todayCal.set(Calendar.HOUR_OF_DAY, todayCal.getActualMinimum(Calendar.HOUR_OF_DAY));
							todayCal.set(Calendar.MINUTE, todayCal.getActualMinimum(Calendar.MINUTE));
							todayCal.set(Calendar.SECOND, todayCal.getActualMinimum(Calendar.SECOND));
							todayCal.set(Calendar.MILLISECOND, todayCal.getActualMinimum(Calendar.MILLISECOND));
							proposalVO.setVulnerable(((lineItem.getStartDate().getTime() - todayCal.getTimeInMillis()) / (1000 * 60 * 60 * 24) <= 30) ? ConstantStrings.VULNERABLE : ConstantStrings.EMPTY_STRING);
							listViewVo.addReservationProposal(proposalVO);

							proposalLineItem.add(lineItem);
						}
					}
				}
			}

			listViewVo.setTotalSOR(calculateSor(orderLineItems, proposalLineItem, targetTypeMap, regions, targetElementMap));
			reservationList.add(listViewVo);
			currentDate = DateUtils.addDays(currentDate, 1);
		}
		return reservationList;
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservationDetailForSalesCalendar(com.nyt.mpt.util.reservation.SalesCalendarReservationDTO)
	 */
	@Override
	public List<SalesReservationCalendarViewVO> getReservationDetailForSalesCalendar(final SalesCalendarReservationDTO calendarVO) {
		final Map<Long, String> targetElementMap = targetingDao.getAllTargetTypeElement();
		final Map<Long, String> targetTypeMap = targetingDao.getTargetTypeCriteria();
		final List<Region> regions = reservationDao.getRegionList();
		
		LOGGER.info("Loading Sales Order & Proposals for calendar - " + calendarVO);
		final List<SalesOrder> salesOrderList = reservationDao.getSalesOrderForSalesCalendar(calendarVO);
		LOGGER.info("Sales Order count for calendar - " + salesOrderList.size());
		if (!salesOrderList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromSalesOrder(calendarVO.getLineItemTargeting(), salesOrderList, targetTypeMap, regions);
		}
		
		final List<Proposal> proposedProposalList = proposalDao.getProposedReservationsForCalendar(calendarVO);
		LOGGER.info("Proposed Proposal count for calendar - " + proposedProposalList.size());
		if (!proposedProposalList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromProposal(calendarVO.getLineItemTargeting(), proposedProposalList, targetTypeMap, targetElementMap);
		}

		final List<Proposal> proposalList = proposalDao.getReservedProposalsForSalesCalendar(calendarVO);
		LOGGER.info("Proposal count for calendar - " + proposalList.size());
		if (!proposalList.isEmpty()) {
			filterMutuallyExclusivelyLineItemFromProposal(calendarVO.getLineItemTargeting(), proposalList, targetTypeMap, targetElementMap);
		}
		
		final Map<Long, String> productMap = new LinkedHashMap<Long, String>();
		Map<Long, String> salesCategoryMap = new LinkedHashMap<Long, String>();
		if (!proposalList.isEmpty() || !salesOrderList.isEmpty()){
			for (Product product : productService.getAllReservableProducts(true)) {
				productMap.put(product.getId(), product.getDisplayName());
			}
			salesCategoryMap = sosService.getSalesCategories();
		}

		Date currentDate = calendarVO.getStartDate();
		int proposedLineItemCount = 0;
		final List<SalesReservationCalendarViewVO> reservationList = new ArrayList<SalesReservationCalendarViewVO>();

		while (currentDate.before(calendarVO.getEndDate()) || currentDate.equals(calendarVO.getEndDate())) {
			proposedLineItemCount = 0;
			final List<LineItem> proposalLineItem = new ArrayList<LineItem>();
			final List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
			
			final SalesReservationCalendarViewVO listViewVo = new SalesReservationCalendarViewVO();			
			listViewVo.setViewDate(DateUtil.formatDateForListView(currentDate));

			for (SalesOrder salesOrder : salesOrderList) {
				for (OrderLineItem lineItem : salesOrder.getLineItem()) {
					if (lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
						final SalesReservationCalendarDetailVO saleOrderVo = new SalesReservationCalendarDetailVO();
						saleOrderVo.setAdvertiserName(salesOrder.getAdvertiser()==null ? ConstantStrings.EMPTY_STRING : salesOrder.getAdvertiser().getCustomerName());
						saleOrderVo.setCampaignName(salesOrder.getCampaignName());
						saleOrderVo.setLineItemId(lineItem.getLineItemId());
						saleOrderVo.setSalesOrderId(salesOrder.getSalesOrderId());
						saleOrderVo.setSor(lineItem.getShareOfReservation());
						saleOrderVo.setStatus("Sold");
						saleOrderVo.setAccountManager((salesOrder.getOwner() == null) ? ConstantStrings.EMPTY_STRING : salesOrder.getOwner().getFirstName() + ConstantStrings.SPACE + salesOrder.getOwner().getLastName());
						saleOrderVo.setSalesCategoryName(salesCategoryMap.containsKey(salesOrder.getTerritoryId()) ? salesCategoryMap.get(salesOrder.getTerritoryId()) : ConstantStrings.EMPTY_STRING);
						saleOrderVo.setProductName(productMap.containsKey(lineItem.getProductId()) ? productMap.get(lineItem.getProductId()) : ConstantStrings.EMPTY_STRING);
						listViewVo.addReservationSalesOrder(saleOrderVo);
						
						orderLineItems.add(lineItem);
					}
				}
			}

			for (Proposal proposal : proposalList) {
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion proposalVersion = options.getLatestVersion();
					for (LineItem lineItem : proposalVersion.getProposalLineItemSet()) {
						if (lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
							final SalesReservationCalendarDetailVO proposalVO = new SalesReservationCalendarDetailVO();
							String proposalPlanner = (proposal.getAssignedUser() == null) ? ConstantStrings.EMPTY_STRING : proposal.getAssignedUser().getFullName();
							proposalVO.setAccountManager(StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : proposalPlanner);
							proposalVO.setAdvertiserName(StringUtils.isNotBlank(proposal.getSosAdvertiserName()) ? proposal.getSosAdvertiserName() : ConstantStrings.EMPTY_STRING);
							proposalVO.setProposalId(proposal.getId());
							proposalVO.setProposalName(proposal.getName());
							proposalVO.setLineItemId(lineItem.getLineItemID());
							proposalVO.setSor(lineItem.getSor());
							proposalVO.setExpirationDate(lineItem.getReservation() != null ?  lineItem.getReservation().getExpirationDate() : null );
							Calendar cal = Calendar.getInstance();
							if(lineItem.getReservation() != null){
								cal.setTime(lineItem.getReservation().getExpirationDate());
								proposalVO.setStatus("Reserved till " + DateUtil.getSalesCalendarDateString(proposalVO.getExpirationDate()));
							}
							Calendar todayCal = Calendar.getInstance();
							todayCal.set(Calendar.HOUR_OF_DAY, todayCal.getActualMinimum(Calendar.HOUR_OF_DAY));
							todayCal.set(Calendar.MINUTE, todayCal.getActualMinimum(Calendar.MINUTE));
							todayCal.set(Calendar.SECOND, todayCal.getActualMinimum(Calendar.SECOND));
							todayCal.set(Calendar.MILLISECOND, todayCal.getActualMinimum(Calendar.MILLISECOND));
							proposalVO.setDaysToExpire((cal.getTimeInMillis() - todayCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
							proposalVO.setDaysLeftInFlight((lineItem.getStartDate().getTime() - todayCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
							proposalVO.setProductName(productMap.containsKey(lineItem.getSosProductId()) ? productMap.get(lineItem.getSosProductId()) : ConstantStrings.EMPTY_STRING);
							proposalVO.setSalesCategoryName(salesCategoryMap.containsKey(proposal.getSosSalesCategoryId()) ? salesCategoryMap.get(proposal.getSosSalesCategoryId()) : ConstantStrings.EMPTY_STRING);
							listViewVo.addReservationProposal(proposalVO);

							proposalLineItem.add(lineItem);
						}
					}
				}
			}
			
			for (Proposal proposal : proposedProposalList) {
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion proposalVersion = options.getLatestVersion();
					for (LineItem lineItem : proposalVersion.getProposalLineItemSet()) {
						if (lineItem.getStartDate().getTime() <= currentDate.getTime() && lineItem.getEndDate().getTime() >= currentDate.getTime()) {
							proposedLineItemCount ++;
						}
					}
				}
			}

			listViewVo.setTotalSOR(calculateSor(orderLineItems, proposalLineItem, targetTypeMap, regions, targetElementMap));
			int availableSOR = (int) (100 - listViewVo.getTotalSOR());
			if(availableSOR >= 0){
				listViewVo.setAvailableSOR(availableSOR + "% Available");
			}else {
				listViewVo.setAvailableSOR("OVERBOOKED");
			}
			listViewVo.setProposedLineItemsCount(proposedLineItemCount);
			reservationList.add(listViewVo);
			currentDate = DateUtils.addDays(currentDate, 1);
		}
		return reservationList;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservationDetails(com.nyt.mpt.util.reservation.ReservationTO)
	 */
	@Override
	public ReservationDetails getReservationDetails(final ReservationTO reservationTo) {
		final Map<Long, String> targetElementMap = targetingDao.getAllTargetTypeElement();
		final Map<Long, String> targetTypeMap = targetingDao.getTargetTypeCriteria();
		final List<Region> regions = reservationDao.getRegionList();
		
		final List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();		
		final List<SalesOrder> salesOrderList = reservationDao.getSalesOrderForCalendar(reservationTo);
		filterMutuallyExclusivelyLineItemFromSalesOrder(reservationTo.getLineItemTargeting(), salesOrderList, targetTypeMap, regions);
		for (SalesOrder salesOrder : salesOrderList) {
			for (OrderLineItem orderLineItem : salesOrder.getLineItem()) {
				Hibernate.initialize(orderLineItem.getLineItemTargeting());
			}
			orderLineItems.addAll(salesOrder.getLineItem());
		}

		final List<LineItem> proposalLineItem = new ArrayList<LineItem>();
		final List<Proposal> proposalList = proposalDao.getReservedProposalsForCalendar(reservationTo);
		filterMutuallyExclusivelyLineItemFromProposal(reservationTo.getLineItemTargeting(), proposalList, targetTypeMap, targetElementMap);
		for (Proposal proposal : proposalList) {
			for (ProposalOption option : proposal.getProposalOptions()) {
				proposalLineItem.addAll(option.getLatestVersion().getProposalLineItemSet());
			}
		}
		final ReservationDetails reservation = new ReservationDetails();
		reservation.setProposalList(proposalList);
		reservation.setSalesOrderList(salesOrderList);
		reservation.setTotalSOR(calculateSor(orderLineItems, proposalLineItem, targetTypeMap, regions, targetElementMap));
		return reservation;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getProposedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@Override
	public List<Proposal> getProposedProposalsForCalendar(final ReservationTO calendarVO) {
		final List<Proposal> proposalList = proposalDao.getProposedProposalsForCalendar(calendarVO);
		
		/**
		 * Remove mutually exclusive line items
		 */
		filterMutuallyExclusivelyLineItemFromProposal(calendarVO.getLineItemTargeting(), proposalList, 
				targetingDao.getTargetTypeCriteria(), targetingDao.getAllTargetTypeElement());

		return proposalList;
	}
	
	/**
	 * Removes mutually exclusive Line Items from List of {@link SalesOrder}
	 * @param 	lineItemTargeting
	 * 			Set of {@link LineItemTarget}
	 * @param 	salesOrderList
	 * 			List of {@link SalesOrder}
	 * @param 	targetTypeMap
	 * 			Map of target types
	 * @param 	regions
	 * 			List of {@link Region}
	 */
	private void filterMutuallyExclusivelyLineItemFromSalesOrder(final Set<LineItemTarget> lineItemTargeting,
			final List<SalesOrder> salesOrderList, final Map<Long, String> targetTypeMap, final List<Region> regions) {
		if (lineItemTargeting == null || lineItemTargeting.isEmpty()) {
			return;
		} else {
			for (LineItemTarget lineItemTarget : lineItemTargeting) {
				if (COUNTRIES.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					final Map<Long, String> countryTocheck = getCountryCodeMap(lineItemTarget.getSosTarTypeElementId());
					if (!countryTocheck.isEmpty()) {
						filterSalesOrderLineItemForCountry(countryTocheck, salesOrderList, regions);
					}
				} else if (TARGET_REGION.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					final Map<Long, String> countryTocheck = getCountryCodeMapForRegion(lineItemTarget.getSosTarTypeElementId());
					if (!countryTocheck.isEmpty()) {
						filterSalesOrderLineItemForCountry(countryTocheck, salesOrderList, regions);
					}
				} else if (ADX_DMA.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					filterSalesOrderLineItemForDMA(lineItemTarget.getSosTarTypeElementId(), salesOrderList);
				}
			}
		}
	}
	
	/**
	 * Removes mutually exclusive Line Items from List of {@link Proposal}
	 * @param 	lineItemTargeting
	 * 			Set of {@link LineItemTarget}
	 * @param 	proposalList
	 * 			List of {@link Proposal}
	 * @param 	targetTypeMap
	 * 			Map of target types
	 * @param 	targetElementMap
	 * 			Map of target elements
	 */
	private void filterMutuallyExclusivelyLineItemFromProposal(final Set<LineItemTarget> lineItemTargeting,
			final List<Proposal> proposalList, final Map<Long, String> targetTypeMap, final Map<Long, String> targetElementMap) {
		if (lineItemTargeting == null || lineItemTargeting.isEmpty()) {
			return;
		} else {
			for (LineItemTarget lineItemTarget : lineItemTargeting) {
				if (COUNTRIES.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					final Map<Long, String> countryTocheck = getCountryCodeMap(lineItemTarget.getSosTarTypeElementId());
					if (!countryTocheck.isEmpty()) {
						filterProposalLineItemsForCountry(countryTocheck, proposalList, targetTypeMap, targetElementMap);
					}
				} else if (TARGET_REGION.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					final Map<Long, String> countryTocheck = getCountryCodeMapForRegion(lineItemTarget.getSosTarTypeElementId());
					if (!countryTocheck.isEmpty()) {
						filterProposalLineItemsForCountry(countryTocheck, proposalList, targetTypeMap, targetElementMap);
					}
				} else if (ADX_DMA.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
					filterProposalLineItemsForDMA(lineItemTarget.getSosTarTypeElementId(), proposalList, targetTypeMap, targetElementMap);
				}
			}
		}
	}

	/**
	 * Filters Line Items from List of {@link SalesOrder} as per the <code>DMA</code>
	 * @param dmas
	 * @param salesOrderList
	 */
	private void filterSalesOrderLineItemForDMA(final String dmas, final List<SalesOrder> salesOrderList) {
		final Map<Long, String> targetElementMap = targetingDao.getAllTargetTypeElement();
		for (SalesOrder salesOrder : salesOrderList) {
			for (final Iterator<OrderLineItem> iterator = salesOrder.getLineItem().iterator(); iterator.hasNext();) {
				final OrderLineItem lineItem = iterator.next();
				if (lineItem.getLineItemTargeting() == null || lineItem.getLineItemTargeting().isEmpty()) {
					continue;
				} else {
					boolean found = false; boolean geoGraphicTargeted = false;
					for (LineItemTargeting lineItemTarget : lineItem.getLineItemTargeting()) {
						if (COUNTRIES.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							if ("United States".equalsIgnoreCase(lineItemTarget.getTargetElement().getElementName())) {
								found = true;
								break;
							}
						} else if (TARGET_REGION.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							/**
							 * SOS allows following region targeting :- Europe, Asia-Pacific, Africa, Middle East, Latin America 
							 * 
							 * None of them contain United States. So if we find any reservation line item in SOS which is targeted to any region,
							 * we can safely assume that it is **not** targeted to US and therefore won't conflict with any DMA based targeting.
							 */
						} else if (ADX_DMA.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							for (final String sourceDma : dmas.split(ConstantStrings.COMMA)) {
								if (targetElementMap.get(Long.valueOf(sourceDma)) != null
										&& targetElementMap.get(Long.valueOf(sourceDma)).equalsIgnoreCase(lineItemTarget.getTargetElement().getElementName())) {
									found = true;
									break;
								}
							}
						}
						if (found) {
							break;
						}
					}
					if (!found && geoGraphicTargeted) {
						iterator.remove();
					}
				}
			}
		}
	}

	/**
	 * Filters Line Items from List of {@link SalesOrder} as per the <code>countryTocheck</code> and <code>regions</code>
	 * @param countryTocheck
	 * @param salesOrderList
	 * @param regions
	 */
	private void filterSalesOrderLineItemForCountry(final Map<Long, String> countryTocheck, final List<SalesOrder> salesOrderList, final List<Region> regions) {
		for (SalesOrder salesOrder : salesOrderList) {
			for (final Iterator<OrderLineItem> iterator = salesOrder.getLineItem().iterator(); iterator.hasNext();) {
				final OrderLineItem lineItem = iterator.next();
				if (lineItem.getLineItemTargeting() == null || lineItem.getLineItemTargeting().isEmpty()) {
					continue;
				} else {
					boolean found = false; boolean geoGraphicTargeted = false;
					for (LineItemTargeting lineItemTarget : lineItem.getLineItemTargeting()) {
						if (COUNTRIES.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							final String country = lineItemTarget.getTargetElement().getCode();
							if (countryTocheck.values().contains(country)) {
								found = true;
								break;
							}
						} else if (TARGET_REGION.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							final Set<Countries> countriesList = getCountryListForRegion(regions, lineItemTarget.getTargetElement());
							for (Countries countries : countriesList) {
								if(countryTocheck.values().contains(countries.getCode())) {
									found = true;
									break;
								}
							}
						} else if (ADX_DMA.equalsIgnoreCase(lineItemTarget.getTargetElement().getTargetType().getName())) {
							geoGraphicTargeted = true;
							/**
							 * SOS allows following region targeting :- Europe, Asia-Pacific, Africa, Middle East, Latin America 
							 * 
							 * None of them contain United States. So if we find any reservation line item in SOS which is targeted to any region,
							 * we can safely assume that it is **not** targeted to US and therefore won't conflict with any DMA based targeting.
							 */
							if (countryTocheck.values().contains(US)) {
								found = true;
								break;
							}
						}
						if (found) {
							break;
						}
					}
					if (!found && geoGraphicTargeted) {
						iterator.remove();
					}
				}
			}
		}
	}

	/**
	 * Returns the unique set of {@link Countries} by <code>regionList</code> and <code>targetElement</code>
	 * @param regionList
	 * @param targetElement
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Set<Countries> getCountryListForRegion(final List<Region> regionList, final TargetElement targetElement) {
		for (Region region : regionList) {
			if (targetElement.getElementName().equalsIgnoreCase(region.getName())) {
				return region.getCountries();
			}
		}
		return Collections.EMPTY_SET;
	}

	/**
	 * Filters LineItems from the List of {@link Proposal}
	 * @param countryTocheck
	 * @param proposalList
	 * @param targetTypeMap
	 * @param targetElementMap
	 */
	@SuppressWarnings("unchecked")
	private void filterProposalLineItemsForCountry(final Map<Long, String> countryTocheck, 
			final List<Proposal> proposalList, final Map<Long, String> targetTypeMap, final Map<Long, String> targetElementMap) {
		for (Proposal proposal : proposalList) {
			for (ProposalOption options : proposal.getProposalOptions()) {
				final ProposalVersion versions = options.getLatestVersion();
				for (final Iterator<LineItem> iterator = versions.getProposalLineItemSet().iterator(); iterator.hasNext();) {
					final LineItem lineItem = iterator.next();
					if (lineItem.getGeoTargetSet() == null || lineItem.getGeoTargetSet().isEmpty()) {
						continue;
					} else {
						boolean found = false; boolean geoGraphicTargeted = false;
						for (LineItemTarget lineItemTarget : lineItem.getGeoTargetSet()) {
							Map<Long, String> countryMap = Collections.EMPTY_MAP;
							if (COUNTRIES.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								countryMap = getCountryCodeMap(lineItemTarget.getSosTarTypeElementId());
							} else if (TARGET_REGION.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								countryMap = getCountryCodeMapForRegion(lineItemTarget.getSosTarTypeElementId());
							} else if (ADX_DMA.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								countryMap = new HashMap<Long, String>();
								for (Long elementId : targetElementMap.keySet()) {
									if ("United States".equalsIgnoreCase(targetElementMap.get(elementId))) {
										countryMap.put(elementId, US);
									}
								}
							}
							for (Long sourceCountryId : countryTocheck.keySet()) {
								if (countryMap.containsKey(sourceCountryId)) {
									found = true;
									break;
								}
							}
							if (found) {
								break;
							}
						}
						if (!found && geoGraphicTargeted) {
							iterator.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * In case of DMA targeting in source line item, proposal line items having either
	 * any matching DMA or country targeted as US or target region is North America are not mutually exclusive.
	 * @param dma
	 * @param proposalList
	 * @param targetTypeMap
	 * @param targetElementMap
	 */
	private void filterProposalLineItemsForDMA(final String dma, final List<Proposal> proposalList,
			final Map<Long, String> targetTypeMap, final Map<Long, String> targetElementMap) {
		for (Proposal proposal : proposalList) {
			for (ProposalOption options : proposal.getProposalOptions()) {
				final ProposalVersion versions = options.getLatestVersion();
				for (final Iterator<LineItem> iterator = versions.getProposalLineItemSet().iterator(); iterator.hasNext();) {
					final LineItem lineItem = iterator.next();
					if (lineItem.getGeoTargetSet() == null || lineItem.getGeoTargetSet().isEmpty()) {
						continue;
					} else {
						boolean found = false; boolean geoGraphicTargeted = false;
						for (LineItemTarget lineItemTarget : lineItem.getGeoTargetSet()) {
							if (COUNTRIES.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								final Map<Long, String> countryMap = new HashMap<Long, String>();
								for (Long elementId : targetElementMap.keySet()) {
									if ("United States".equalsIgnoreCase(targetElementMap.get(elementId))) {
										countryMap.put(elementId, US);
									}
								}
								for (String country : lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
									if (countryMap.containsKey(Long.valueOf(country))) {
										found = true;
										break;
									}
								}
							} else if (TARGET_REGION.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								for (String region : lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
									if ("North America".equalsIgnoreCase(targetElementMap.get(Long.valueOf(region)))) {
										found = true;
										break;
									}
								}
							} else if (ADX_DMA.equalsIgnoreCase(targetTypeMap.get(lineItemTarget.getSosTarTypeId()))) {
								geoGraphicTargeted = true;
								for (String lineItemDma : lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
									for (String sourceDma : dma.split(ConstantStrings.COMMA)) {
										if (sourceDma.equalsIgnoreCase(lineItemDma)) {
											found = true;
											break;
										}
									}
								}
							}
							if (found) {
								break;
							}
						}
						if (!found && geoGraphicTargeted) {
							iterator.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * Returns the Map of Country codes by <code>countryIds</code>
	 * @param 	countryIds
	 * @return
	 * 			Returns the Map of Country codes
	 */
	private Map<Long, String> getCountryCodeMap(final String countryIds) {
		final List<CountryRegionMap> countryRegionMap = targetingDao.getAllCountryRegionData();
		final Map<Long, String> countryMap = new HashMap<Long, String>();
		if (StringUtils.isNotBlank(countryIds)) {
			for (String countryId : countryIds.split(ConstantStrings.COMMA)) {
				for (CountryRegionMap countryRegion : countryRegionMap) {
					if (countryRegion.getId().getCountryId() == Long.valueOf(countryId)) {
						countryMap.put(countryRegion.getId().getCountryId(), countryRegion.getCountryCode());
						break;
					}
				}
			}
		}
		return countryMap;
	}

	/**
	 * Returns the Map of Country codes by <code>regionIds</code>
	 * @param 	regionIds
	 * @return
	 * 			Returns the Map of Country codes
	 */
	private Map<Long, String> getCountryCodeMapForRegion(final String regionIds) {
		final List<CountryRegionMap> countryRegionMap = targetingDao.getAllCountryRegionData();
		final Map<Long, String> countryMap = new HashMap<Long, String>();
		if (StringUtils.isNotBlank(regionIds)) {
			for (String regionId : regionIds.split(ConstantStrings.COMMA)) {
				for (CountryRegionMap countryRegion : countryRegionMap) {
					if (countryRegion.getId().getRegionId() == Long.valueOf(regionId)) {
						countryMap.put(countryRegion.getId().getCountryId(), countryRegion.getCountryCode());
					}
				}
			}
		}
		return countryMap;
	}
	
	/**
	 * Returns the SOR
	 * @param orderLineItems
	 * @param proposalLineItem
	 * @param targetTypeMap
	 * @param regions
	 * @param targetElementMap
	 * @return
	 */
	private double calculateSor(final List<OrderLineItem> orderLineItems, final List<LineItem> proposalLineItem,
			final Map<Long, String> targetTypeMap, final List<Region> regions, final Map<Long, String> targetElementMap) {
		final Map<String, Double> dmaMap = new HashMap<String, Double>();
		final Map<String, Double> countryMap = new HashMap<String, Double>();
		double sor = 0.0;
		for (OrderLineItem lineItem : orderLineItems) {
			if(lineItem.getLineItemTargeting() == null || lineItem.getLineItemTargeting().isEmpty()){
				sor += lineItem.getShareOfReservation();
			} else {
				double tmpSor = 0; boolean geoGraphicTargeted = false;
				for (LineItemTargeting targeting : lineItem.getLineItemTargeting()) {
					if (COUNTRIES.equalsIgnoreCase(targeting.getTargetElement().getTargetType().getName())) {
						geoGraphicTargeted = true;
						final String countryCode = targeting.getTargetElement().getCode();
						if (countryMap.get(countryCode) == null) {
							countryMap.put(countryCode, lineItem.getShareOfReservation());
						} else {
							countryMap.put(countryCode, countryMap.get(countryCode) + lineItem.getShareOfReservation());
						}
					} else if (TARGET_REGION.equalsIgnoreCase(targeting.getTargetElement().getTargetType().getName())) {
						geoGraphicTargeted = true;
						final Set<Countries> countriesList = getCountryListForRegion(regions, targeting.getTargetElement());			
						for (Countries country : countriesList) {
							if (countryMap.get(country.getCode()) == null) {
								countryMap.put(country.getCode(), lineItem.getShareOfReservation());
							} else {
								countryMap.put(country.getCode(), countryMap.get(country.getCode()) + lineItem.getShareOfReservation());
							}
						}						
					} else if (ADX_DMA.equalsIgnoreCase(targeting.getTargetElement().getTargetType().getName())) {
						geoGraphicTargeted = true;
						final String dma = targetElementMap.get(targeting.getTargetElement().getTargetElementId());
						if (dmaMap.get(dma) == null) {
							dmaMap.put(dma, lineItem.getShareOfReservation());
						} else {
							dmaMap.put(dma, dmaMap.get(dma) + lineItem.getShareOfReservation());
						}
					} else {
						tmpSor = lineItem.getShareOfReservation();
					}
				}
				if (!geoGraphicTargeted) {
					sor += tmpSor;
				}
			}
		}
		
		for (LineItem lineItem : proposalLineItem) {
			if(lineItem.getGeoTargetSet() == null || lineItem.getGeoTargetSet().isEmpty()){
				sor += lineItem.getSor();
			} else {
				double tmpSor = 0; boolean geoGraphicTargeted = false;
				for (LineItemTarget targeting : lineItem.getGeoTargetSet()) {
					if (COUNTRIES.equalsIgnoreCase(targetTypeMap.get(targeting.getSosTarTypeId()))) {
						geoGraphicTargeted = true;
						for (String countryCode : getCountryCodeMap(targeting.getSosTarTypeElementId()).values()) {
							if (countryMap.get(countryCode) == null) {
								countryMap.put(countryCode, lineItem.getSor());
							} else {
								countryMap.put(countryCode, countryMap.get(countryCode) + lineItem.getSor());
							}
						}
					} else if (TARGET_REGION.equalsIgnoreCase(targetTypeMap.get(targeting.getSosTarTypeId()))) {
						geoGraphicTargeted = true;
						for (String countryCode : getCountryCodeMapForRegion(targeting.getSosTarTypeElementId()).values()) {
							if (countryMap.get(countryCode) == null) {
								countryMap.put(countryCode, lineItem.getSor());
							} else {
								countryMap.put(countryCode, countryMap.get(countryCode) + lineItem.getSor());
							}
						}
					} else if (ADX_DMA.equalsIgnoreCase(targetTypeMap.get(targeting.getSosTarTypeId()))) {
						geoGraphicTargeted = true;
						for (final String dmaId : targeting.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
							final String dma = targetElementMap.get(Long.valueOf(dmaId));
							if (dmaMap.get(dma) == null) {
								dmaMap.put(dma, lineItem.getSor());
							} else {
								dmaMap.put(dma, dmaMap.get(dma) + lineItem.getSor());
							}
						}
					} else {
						tmpSor = lineItem.getSor();
					}
				}
				if (!geoGraphicTargeted) {
					sor += tmpSor;
				}
			}
		}
		countryMap.put(US, (countryMap.get(US) == null ? 0 : countryMap.get(US)) + (dmaMap.isEmpty() ? 0 : Collections.max(dmaMap.values())));
		return sor + (countryMap.isEmpty() ? 0 : Collections.max(countryMap.values()));
	}
	
	/**
	 * Returns the List of {@link Date} where Email dates are available
	 * @param calendarVO
	 * @param datesList
	 * @return
	 */
	private List<Date> getEmailAvailableDates(final ReservationTO calendarVO, final List<Date> datesList) {
		final List<FilterCriteria> filterCriteriaLst = new ArrayList<FilterCriteria>();
		filterCriteriaLst.add(new FilterCriteria("productId", String.valueOf(calendarVO.getProductId()), SearchOption.EQUAL.toString()));
		filterCriteriaLst.add(new FilterCriteria("salesTargetId", String.valueOf(calendarVO.getSalesTargetId()), SearchOption.EQUAL.toString()));
		
		final List<EmailSchedule> emailScheduleLst = emailScheduleService.getFilteredEmailDetailList(filterCriteriaLst, null, null);
		for (EmailSchedule emailSchedule : emailScheduleLst) {
			for (EmailScheduleDetails scheduleDetails : emailSchedule.getEmailSchedules()) {
				datesList.addAll(DateUtil.getAvailableDates(scheduleDetails));
			}
		}
		return datesList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@Override
	public List<OrderLineItem> getSalesOrderForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria){
		return reservationDao.getSalesOrderForReservationSearch(criteriaLst,pgCriteria,sortCriteria );
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@Override
	public int getSalesOrderForReservationSearchCount(final List<RangeFilterCriteria> criteriaLst){
		return reservationDao.getSalesOrderForReservationSearchCount(criteriaLst);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@Override
	public List<LineItem> getProposalsForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria){
		return proposalDao.getProposalsForReservationSearch(criteriaLst,pgCriteria,sortCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getReservedProposalsForCalendar(com.nyt.mpt.domain.sos.CalendarVO)
	 */
	@Override
	public int getProposalsForReservationSearchCount(final List<RangeFilterCriteria> criteriaLst){
		return proposalDao.getProposalsForReservationSearchCount(criteriaLst);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ICalendarReservationService#getProposedProposalsForSalesCalendar(com.nyt.mpt.util.reservation.SalesCalendarReservationDTO)
	 */
	@Override
	public List<Proposal> getProposedProposalsForSalesCalendar(SalesCalendarReservationDTO salesCalendarReservationDTO) {
		List<Proposal> proposalList =  proposalDao.getProposedReservationsForCalendar(salesCalendarReservationDTO);
		if(!proposalList.isEmpty()){
			filterMutuallyExclusivelyLineItemFromProposal(salesCalendarReservationDTO.getLineItemTargeting(), proposalList, 
					targetingDao.getTargetTypeCriteria(), targetingDao.getAllTargetTypeElement());
		}
		return proposalList;
	}
	
	public void setReservationDao(ICalendarReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}

	public void setProposalDao(IProposalDAO proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setTargetingDao(ITargetingDAO targetingDao) {
		this.targetingDao = targetingDao;
	}

	public void setEmailScheduleService(IEmailScheduleService emailScheduleService) {
		this.emailScheduleService = emailScheduleService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public void setSosService(ISOSService sosService) {
		this.sosService = sosService;
	}
}
