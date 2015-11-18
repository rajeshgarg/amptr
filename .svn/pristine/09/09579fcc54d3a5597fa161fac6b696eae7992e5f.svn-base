/**
 * 
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.form.CalendarDetailForm;
import com.nyt.mpt.form.CalendarForm;
import com.nyt.mpt.form.CalendarLineItemForm;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.CalendarDetailComparator;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.enums.EmailReservationStatus;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.reservation.ReservationDetails;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationListViewVO;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;
import com.nyt.mpt.util.reservation.SalesReservationCalendarViewVO;

/**
 * This <code>ReservationController</code> includes all the methods related to creating calendar and its related reservation for Reservable and Email line items. 
 * This also includes methods for month view, list view, month day view and also for calculating SOR.
 *  
 * @author surendra.singh
 */
@Controller
@RequestMapping("/reservations/*")
public class ReservationController extends AbstractBaseController {

	private static final String CALENDAR_FORM = "calendarForm";

	private ProposalHelper helper;

	private IProductService productService;

	private ITargetingService targetingService;

	private ISalesTargetService salesTargetService;

	private ICalendarReservationService reservationService;

	private String sosURL;

	/**
	 * Return Model and view to display reservation of a lineItem {@link LineItem}
	 * @param 	calendarForm
	 * 			{@link CalendarForm} has all the information related to creation of calendar reservation for both <code>RESERVABLE</code> and <code>EMAIL</code> line items
	 * @return
	 * 			 Return Model and view to display reservation
	 */
	@RequestMapping("/viewCalendar")
	public ModelAndView viewCalendar(@ModelAttribute(CALENDAR_FORM) final CalendarForm calendarForm) {
		final ModelAndView view = new ModelAndView("viewCalendar");
		final Product product = productService.getProductById(calendarForm.getProductId());
		if (product == null) {
			final Product productValue = productService.getInactiveProductById(calendarForm.getProductId());
			if (productValue != null) {
				view.addObject("productInactiveName", productValue.getDisplayName());
			}
			calendarForm.setProductId(0L);
			view.addObject("isProductInactive", true);
		} else {
			view.addObject("productName", product.getDisplayName());
			view.addObject("isProductInactive", false);
			view.addObject("productType", (product.getAvailsSytemId() != null && product.getAvailsSytemId().longValue() == 13) ? "EMAIL" : "RESERVABLE");
		}

		final List<SalesTarget> salesTargets = salesTargetService.getSalesTargetListByIDs(new Long[] { calendarForm.getSalesTargetId() });
		if (salesTargets != null && !salesTargets.isEmpty()) {
			view.addObject("salesTargetName", salesTargets.get(0).getSalesTargeDisplayName());
			view.addObject("isSalesTargetInactive", false);
		} else {
			final SalesTarget salesTarget = salesTargetService.getInactiveSalesTargetById(calendarForm.getSalesTargetId());
			if (salesTarget != null) {
				view.addObject("salesTargetInactiveName", salesTarget.getSalesTargeDisplayName());
			}
			calendarForm.setSalesTargetId(0L);
			view.addObject("isSalesTargetInactive", true);
		}

		if (StringUtils.isNotBlank(calendarForm.getLineItemTargetingData())) {
			view.addObject("targetingString", getTargetingStringForProposal(
				TargetJsonConverter.convertJsonToObject(calendarForm.getLineItemTargetingData().replaceAll("'", "\""), null)));
		}
		view.addObject(CALENDAR_FORM, calendarForm);
		return view;
	}

	/**
	 * Return reservation details for a particular month to display in calendar
	 * @param 	calendarForm
	 * 			{@link CalendarForm} has all the information related to creation of reservation calendar for a particular month
	 * @return
	 * 			Return reservation details for a particular month
	 */
	@ResponseBody
	@RequestMapping("/monthDetail")
	public List<ReservationInfo> getMonthReservationDetail(@ModelAttribute(CALENDAR_FORM) final CalendarForm calendarForm) {
		return reservationService.getReservationDetailForCalendar(calendarForm.populate(new ReservationTO()));
	}

	/**
	 * Return Model and view for reservation details in list view
	 * @param 	calendarForm
	 * 			{@link CalendarForm} has all the information related to creation of reservation calendar in a list view
	 * @return
	 * 			 Return reservation details in a list view
	 */
	@RequestMapping("/listView")
	public ModelAndView getListView(@ModelAttribute final CalendarForm calendarForm) {
		final ModelAndView view = new ModelAndView("reservationListView");
		final List<ReservationListViewVO> reservationListVO = reservationService.getReservationDetailForListView(calendarForm.populate(new ReservationTO()));
		view.addObject("reservationList", reservationListVO);
		return view;
	}

	/**
	 * Return Model and view for reservation details of a particular day
	 * @param 	calendarForm
	 * 			{@link CalendarForm} has all the information related to creation of reservation calendar for a particular day
	 * @return
	 * 			Return reservation details of a particular day
	 */
	@RequestMapping("/monthDayDetail")
	public ModelAndView getMonthDayDetail(@ModelAttribute(CALENDAR_FORM) final CalendarForm calendarForm) {
		final ReservationTO reservationTo = calendarForm.populate(new ReservationTO());
		final ReservationDetails reservationDetail = reservationService.getReservationDetails(reservationTo);
		
		final ModelAndView view = new ModelAndView("reservationDetail");		
		view.addObject("currentDate", DateUtil.getDayDateString(DateUtil.parseToDate(calendarForm.getStartDate()).getTime()));
		view.addObject("salesOrderList", convertSalesOrderToForm(reservationDetail.getSalesOrderList()));		
		view.addObject("reservedProposalList", convertProposalToForm(reservationDetail.getProposalList()));
		view.addObject("proposedProposalList", convertProposalToForm(reservationService.getProposedProposalsForCalendar(reservationTo)));
		view.addObject("booked", reservationDetail.getTotalSOR());
		return view;
	}

	/**
	 * Returns Ajax Response after calculating the SOR
	 * @param 	calendarForm
	 * 			{@link CalendarForm} has all the information related to calculate the SOR
	 * @return
	 * 			Return calculated SOR
	 */
	@ResponseBody
	@RequestMapping("/calculateSOR")
	public AjaxFormSubmitResponse calculateSOR(@ModelAttribute(CALENDAR_FORM) final CalendarForm calendarForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		double sor = 0.0D;
		final ReservationTO calendarVo = calendarForm.populate(new ReservationTO());
		calendarVo.setStartDate(DateUtil.parseToDate(calendarForm.getStartDate()));
		calendarVo.setEndDate(DateUtil.parseToDate(calendarForm.getEndDate()));
		ReservationInfo reservationInfo = null; 
		for (ReservationInfo reservationDetail : reservationService.getReservationDetailForCalendar(calendarVo)) {
			if (sor < reservationDetail.getSor()) {
				sor = reservationDetail.getSor();
			}
			reservationInfo = reservationDetail;
		}
		ajaxResponse.getObjectMap().put("calculatedSOR", NumberUtil.formatDouble(sor > 100 ? 0 : 100 - sor, true));
		ajaxResponse.getObjectMap().put("reservationStatus", getReservationStatus(reservationInfo));
		return ajaxResponse;
	}
	
	/**
	 * Returns empty model and view to Display Reservation Calendar to audit Manager. This method is only meant for redirecting call to <b>Reservation Calendar</b> screen.
	 * 
	 * @return instance of {@link ModelAndView} containing without any Audit data.
	 * @throws Exception
	 */
	@RequestMapping("/displayReservationCalendar")
	public ModelAndView displayReservationCalendar(@ModelAttribute("salesCalendarReservationDTO") final SalesCalendarReservationDTO salesCalendarReservationDTO){
		final ModelAndView modelAndView = new ModelAndView("abstract-reservation-calendar");
		modelAndView.addObject("productMap", helper.getSalesCalendarProductsMap());
		modelAndView.addObject("countryMap",helper.getCountriesBasedOnRegions());
		modelAndView.addObject("datesMap",helper.getDatesMap());
		modelAndView.addObject("weekdayMap",helper.getWeekdayMap());
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/getProposedLineItems")
	public List<CalendarDetailForm> getProposedLineItems(final SalesCalendarReservationDTO salesCalendarReservationDTO){
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		if(StringUtils.isNotBlank(salesCalendarReservationDTO.getDateString())){
			String [] dateArr = salesCalendarReservationDTO.getDateString().split(ConstantStrings.COMMA);
			String [] monthDayArr = dateArr[1].trim().split(ConstantStrings.SPACE);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, ArrayUtils.indexOf(monthNames, monthDayArr[0]));
			cal.set(Calendar.YEAR, Integer.valueOf(dateArr[2].trim()));
			cal.set(Calendar.DATE, Integer.valueOf(monthDayArr[1].trim()));
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
			salesCalendarReservationDTO.setStartDate(cal.getTime());
			salesCalendarReservationDTO.setEndDate(cal.getTime());
		}
		if(StringUtils.isNotBlank(salesCalendarReservationDTO.getCountries())){
			LineItemTarget lineItemTarget = new LineItemTarget();
			lineItemTarget.setSosTarTypeId(8l);//for countries
			lineItemTarget.setSosTarTypeElementId(salesCalendarReservationDTO.getCountries());
			Set<LineItemTarget> lineItemTargetingSet = new LinkedHashSet<LineItemTarget>(1);
			lineItemTargetingSet.add(lineItemTarget);
			salesCalendarReservationDTO.setLineItemTargeting(lineItemTargetingSet);
		}		
		return convertProposalToCalendarForm(reservationService.getProposedProposalsForSalesCalendar(salesCalendarReservationDTO));
	}
	
	@ResponseBody
	@RequestMapping("/getSalesCalendarResult")
	public AjaxFormSubmitResponse getSalesCalendarResult(final HttpServletResponse response, @ModelAttribute("salesCalendarReservationDTO") final SalesCalendarReservationDTO salesCalendarReservationDTO) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		/**
		 * Validation code starts
		 */
		
		final CustomBindingResult results = new CustomBindingResult("salesCalendarReservationDTO", salesCalendarReservationDTO);
		if(salesCalendarReservationDTO.getProductIds() == null || salesCalendarReservationDTO.getProductIds().isEmpty() ){
				results.rejectValue("productMultiSelect_custom", ErrorCodes.MandatoryInputMissing, "productMultiSelect_custom", new Object[] {Constants.PRODUCT},	UserHelpCodes.HelpMandatoryInputMissing);
		}
		if(StringUtils.isBlank(salesCalendarReservationDTO.getDateString())){
			results.rejectValue("dateMultiSelect_select2", ErrorCodes.MandatoryInputMissing, "dateMultiSelect_select2", new Object[] {"Date"}, UserHelpCodes.HelpMandatoryInputMissing);
		}
		
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}
		
		/**
		 * validation code ends
		 */		
		setStartAndEndDate(salesCalendarReservationDTO);
		if(StringUtils.isNotBlank(salesCalendarReservationDTO.getCountries())){
			LineItemTarget lineItemTarget = new LineItemTarget();
			lineItemTarget.setSosTarTypeId(8l);//for countries
			lineItemTarget.setSosTarTypeElementId(salesCalendarReservationDTO.getCountries());
			Set<LineItemTarget> lineItemTargetingSet = new LinkedHashSet<LineItemTarget>(1);
			lineItemTargetingSet.add(lineItemTarget);
			salesCalendarReservationDTO.setLineItemTargeting(lineItemTargetingSet);
		}
		List<SalesReservationCalendarViewVO> reservationList = reservationService.getReservationDetailForSalesCalendar(salesCalendarReservationDTO);
		List<SalesReservationCalendarViewVO> reservationUiList = new ArrayList<SalesReservationCalendarViewVO>();
		if(StringUtils.isNotBlank(salesCalendarReservationDTO.getWeekDay())){
			for (SalesReservationCalendarViewVO salesReservationCalendarViewVO : reservationList) {
				if(StringUtils.contains(salesReservationCalendarViewVO.getViewDate(), salesCalendarReservationDTO.getWeekDay())){
					reservationUiList.add(salesReservationCalendarViewVO);
				}
			}
		}else{
			reservationUiList.addAll(reservationList);
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, reservationUiList);
		return ajaxResponse;
	}


	private void setStartAndEndDate(SalesCalendarReservationDTO salesCalendarReservationDTO) {
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		if(StringUtils.isNotBlank(salesCalendarReservationDTO.getDateString())){
			String [] dateArr = salesCalendarReservationDTO.getDateString().split(ConstantStrings.SPACE);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, ArrayUtils.indexOf(monthNames, dateArr[0]));
			cal.set(Calendar.YEAR, Integer.valueOf(dateArr[1]));
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
			salesCalendarReservationDTO.setStartDate(cal.getTime());
			//cal.set(Calendar.MONTH, ArrayUtils.indexOf(monthNames, dateArr[0]) + 2);
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			salesCalendarReservationDTO.setEndDate(cal.getTime());
		}
	}

	/**
	 * Returns the reservation status for email products
	 * @param 	reservationInfo
	 * 			{@link ReservationInfo} has all the reservation info needed to calculate the reservation status
	 * @return
	 * 			Returns the reservation status
	 */
	private String getReservationStatus(final ReservationInfo reservationInfo) {
		String status = EmailReservationStatus.AVAILABLE.getDisplayName();
		if(reservationInfo != null){
			if(reservationInfo.getSor() > 100){
				status = EmailReservationStatus.OVERBOOKED.getDisplayName();
			}else if(reservationInfo.getSor() == 100){
				status = (reservationInfo.getProposals()>0) ? EmailReservationStatus.RESERVED.getDisplayName() : EmailReservationStatus.SOLD.getDisplayName();
			}
		}
		return status;
	}
	
	/**
	 * @param proposalList
	 * @return
	 */
	private List<CalendarDetailForm> convertProposalToCalendarForm(final List<Proposal> proposalList) {
		final List<CalendarDetailForm> formList = new ArrayList<CalendarDetailForm>();
		if (proposalList != null) {
			final Map<Long, String> productMap = helper.getAllProducts();
			final Map<Long, String> salesTargetMap = helper.getSalesTarget();
			final Map<Long, String> advertiserMap = helper.getAdvertiser();
			final Map<Long, String> salesCategoryMap = helper.getSalesCategory();
			for (Proposal proposal : proposalList) {
				final CalendarDetailForm form = new CalendarDetailForm();
				form.setCampaignName(salesCategoryMap.containsKey(proposal.getSosSalesCategoryId()) ? salesCategoryMap.get(proposal.getSosSalesCategoryId()) : ConstantStrings.EMPTY_STRING);//Using it for Sales Category Name
			//	String proposalPlanner = (proposal.getAssignedUser() == null) ? ConstantStrings.EMPTY_STRING : proposal.getAssignedUser().getFullName();
				form.setAccountManager(StringUtils.isNotBlank(proposal.getAccountManager()) ? proposal.getAccountManager() : ConstantStrings.EMPTY_STRING);
				form.setProposalId(proposal.getId());
				if (proposal.getSosAdvertiserId() != null) {
					form.setAdvertiserName(advertiserMap.containsKey(proposal.getSosAdvertiserId()) ? advertiserMap.get(proposal.getSosAdvertiserId()) : ConstantStrings.EMPTY_STRING);
				} else {
					form.setAdvertiserName(StringUtils.isNotBlank(proposal.getSosAdvertiserName()) ? proposal.getSosAdvertiserName() : ConstantStrings.EMPTY_STRING);
				}
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion version = options.getLatestVersion();
					for (LineItem lineItem : version.getProposalLineItemSet()) {
						final CalendarLineItemForm lineItemForm = new CalendarLineItemForm();
						lineItemForm.setOptionName(options.getName());
						lineItemForm.setLineItemID(lineItem.getLineItemID());
						lineItemForm.setProductName(productMap.get(lineItem.getSosProductId()));
						lineItemForm.setSalesTargetName(salesTargetMap.get(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId()));
						lineItemForm.setStartDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
						lineItemForm.setEndDate(DateUtil.getGuiDateString(lineItem.getEndDate()));
						lineItemForm.setTargetingString(lineItem.getTargetingString());
						lineItemForm.setSor(String.valueOf(lineItem.getSor()));
						lineItemForm.setExpirationDate((lineItem.getReservation() != null && lineItem.getReservation().getExpirationDate() != null ) ? String.valueOf(DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())) : ConstantStrings.EMPTY_STRING );
						form.addLineItem(lineItemForm);
					}
				}
				if (form.getLineItems() != null && form.getLineItems().size() > 0) {
					formList.add(form);
				}
			}
		}
		Collections.sort(formList, Collections.reverseOrder(new CalendarDetailComparator()));
		return formList;
	}


	/**
	 * Returns List of {@link CalendarDetailForm} from the List of {@link Proposal} o show information for the particular day in a month
	 * @param 	proposalList
	 * 			List of {@link Proposal} which has all the information related to convert it to {@link CalendarDetailForm} so that it can be shown in the day view
	 * @return
	 * 			 Returns List of {@link CalendarDetailForm} to show information for the particular day in a month
	 */
	private List<CalendarDetailForm> convertProposalToForm(final List<Proposal> proposalList) {
		final List<CalendarDetailForm> formList = new ArrayList<CalendarDetailForm>();
		if (proposalList != null) {
			final Map<Long, String> productMap = helper.getAllProducts();
			final Map<Long, String> salesTargetMap = helper.getSalesTarget();
			final Map<Long, String> advertiserMap = helper.getAdvertiser();
			for (Proposal proposal : proposalList) {
				final CalendarDetailForm form = new CalendarDetailForm();
				form.setCampaignName(proposal.getName());
				form.setAccountManager(proposal.getAccountManager());
				form.setProposalId(proposal.getId());
				if (proposal.getSosAdvertiserId() != null) {
					form.setAdvertiserName(advertiserMap.get(proposal.getSosAdvertiserId()));
				} else {
					form.setAdvertiserName(proposal.getSosAdvertiserName());
				}
				for (ProposalOption options : proposal.getProposalOptions()) {
					final ProposalVersion version = options.getLatestVersion();
					for (LineItem lineItem : version.getProposalLineItemSet()) {
						final CalendarLineItemForm lineItemForm = new CalendarLineItemForm();
						lineItemForm.setOptionName(options.getName());
						lineItemForm.setLineItemID(lineItem.getLineItemID());
						lineItemForm.setProductName(productMap.get(lineItem.getSosProductId()));
						lineItemForm.setSalesTargetName(salesTargetMap.get(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId()));
						lineItemForm.setStartDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
						lineItemForm.setEndDate(DateUtil.getGuiDateString(lineItem.getEndDate()));
						lineItemForm.setTargetingString(lineItem.getTargetingString());
						lineItemForm.setSor(String.valueOf(lineItem.getSor()));
						lineItemForm.setExpirationDate((lineItem.getReservation() != null && lineItem.getReservation().getExpirationDate() != null ) ? String.valueOf(DateUtil.getGuiDateString(lineItem.getReservation().getExpirationDate())) : ConstantStrings.EMPTY_STRING );
						Calendar todayCal = Calendar.getInstance();
						todayCal.set(Calendar.HOUR_OF_DAY, todayCal.getActualMinimum(Calendar.HOUR_OF_DAY));
						todayCal.set(Calendar.MINUTE, todayCal.getActualMinimum(Calendar.MINUTE));
						todayCal.set(Calendar.SECOND, todayCal.getActualMinimum(Calendar.SECOND));
						todayCal.set(Calendar.MILLISECOND, todayCal.getActualMinimum(Calendar.MILLISECOND));
						lineItemForm.setVulnerable(((lineItem.getStartDate().getTime() - todayCal.getTimeInMillis()) / (1000 * 60 * 60 * 24) <= 30) ? ConstantStrings.VULNERABLE : ConstantStrings.EMPTY_STRING);
						form.addLineItem(lineItemForm);
					}
				}
				if (form.getLineItems() != null && form.getLineItems().size() > 0) {
					formList.add(form);
				}
			}
		}
		Collections.sort(formList, Collections.reverseOrder(new CalendarDetailComparator()));
		return formList;
	}

	/**
	 * Returns List of {@link CalendarDetailForm} from the List of {@link SalesOrder} to show information for the particular day in a month
	 * @param 	salesOrderList
	 * 			List of {@link SalesOrder} which has all the information related to convert it to {@link CalendarDetailForm} so that it can be shown in the day view
	 * @return
	 * 			Returns List of {@link CalendarDetailForm} from the List of {@link SalesOrder}
	 */
	private List<CalendarDetailForm> convertSalesOrderToForm(final List<SalesOrder> salesOrderList) {
		final List<CalendarDetailForm> formList = new ArrayList<CalendarDetailForm>();
		if (salesOrderList != null) {
			final Map<Long, String> productMap = helper.getAllProducts();
			final Map<Long, String> salesTargetMap = helper.getSalesTarget();
			for (SalesOrder salesOrder : salesOrderList) {
				final CalendarDetailForm form = new CalendarDetailForm();
				form.setSalesOrderId(salesOrder.getSalesOrderId());
				form.setCampaignName(salesOrder.getCampaignName());
				if (salesOrder.getAdvertiser() != null) {
					form.setAdvertiserName(salesOrder.getAdvertiser().getCustomerName());
				}
				for (OrderLineItem orderLineItem : salesOrder.getLineItem()) {
					final CalendarLineItemForm lineItemForm = new CalendarLineItemForm();
					lineItemForm.setLineItemID(orderLineItem.getLineItemId());
					lineItemForm.setProductName(productMap.get(orderLineItem.getProductId()));
					lineItemForm.setSalesTargetName(salesTargetMap.get(orderLineItem.getSalesTargetId()));
					lineItemForm.setStartDate(DateUtil.getGuiDateString(orderLineItem.getStartDate()));
					lineItemForm.setEndDate(DateUtil.getGuiDateString(orderLineItem.getEndDate()));
					lineItemForm.setSor(String.valueOf(orderLineItem.getShareOfReservation()));
					lineItemForm.setTargetingString(StringUtil.getTargetingStringForOrder(orderLineItem));
					form.addLineItem(lineItemForm);
				}
				form.setSosURL(sosURL);
				if (form.getLineItems() != null && form.getLineItems().size() > 0) {
					formList.add(form);
				}
			}
		}
		return formList;
	}

	/**
	 * Returns targeting string from the set of {@link LineItemTarget}
	 * @param 	lineItemTargeting
	 * 			Set of {@link LineItemTarget} which has all the information to calculate the targeting string for Proposal
	 * @return
	 * 			Returns targeting string
	 */
	private String getTargetingStringForProposal(final Set<LineItemTarget> lineItemTargeting) {
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
		final Map<Long, String> targetElementMap = targetingService.getAllTargetTypeElement();
		if (lineItemTargeting != null && !lineItemTargeting.isEmpty()) {
			final Map<String, String> targetingMap = new LinkedHashMap<String, String>();
			for (LineItemTarget targeting : lineItemTargeting) {
				final String targetType = targetTypeMap.get(targeting.getSosTarTypeId());
				String targetedElement = ConstantStrings.EMPTY_STRING;
				int counter = 0;
				for (String targetElement : targeting.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
					if (counter > 0) {
						targetedElement += ConstantStrings.COMMA + ConstantStrings.SPACE;
					}
					targetedElement += targetElementMap.get(Long.valueOf(targetElement));
					counter++;
				}
				targetingMap.put(targetType, targetedElement);
			}
			return StringUtil.getTargetingString(targetingMap);
		}
		return ConstantStrings.EMPTY_STRING;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setReservationService(final ICalendarReservationService reservationService) {
		this.reservationService = reservationService;
	}

	public void setHelper(final ProposalHelper helper) {
		this.helper = helper;
	}

	public void setTargetingService(final ITargetingService targetingService) {
		this.targetingService = targetingService;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}
	
}
