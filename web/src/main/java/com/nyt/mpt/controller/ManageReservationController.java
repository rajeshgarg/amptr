/**
 */
package com.nyt.mpt.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemReservations;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.MultipleCalendarForm;
import com.nyt.mpt.form.ProductForm;
import com.nyt.mpt.form.ReservationForm;
import com.nyt.mpt.service.ICalendarReservationService;
import com.nyt.mpt.service.IMultipleCalendarService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.service.impl.ProposalUtilService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.enums.EmailReservationStatus;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationInfo;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.security.SecurityUtil;
import com.nyt.mpt.validator.MultipleCalendarValidator;

/**
 * This <code>ManageReservationController</code> class includes all the methods to manage the reservations
 * 
 * @author garima.garg
 */

@Controller
@RequestMapping("/manageReservation/*")
public class ManageReservationController extends AbstractBaseController {

	private static final String PRO_GRID_DATA = "manageAmptReservationData";
	private static final String ZERO = "0";
	private ICalendarReservationService reservationService;
	private ProposalHelper proposalHelper;
	private IProductService productService;
	private TargetJsonConverter targetJsonConverter;
	private IProposalService proposalService;
	private String sosURL;
	private IUserService userService;
	private IMultipleCalendarService multipleCalendarService;
	private ProposalUtilService proposalUtilService;

	/**
	 * Return model and view for Manage Reservation tab
	 * @param reservationForm
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute("reservationForm") final ReservationForm reservationForm) {
		final ModelAndView view = new ModelAndView("manageReservationPage");
		view.addObject("reservationForm", reservationForm);
		final Map<String, List<ProductForm>> productMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllReservableProducts(true)) {
			final ProductForm prForm = new ProductForm();
			prForm.setProductId(product.getId());
			prForm.setDisplayName(product.getDisplayName());
			prForm.setReservable("Y");
			if (productMap.containsKey(product.getClassName().getProductClassName())) {
				productMap.get(product.getClassName().getProductClassName()).add(prForm);
			} else {
				final List<ProductForm> productFormList = new ArrayList<ProductForm>();
				productFormList.add(prForm);
				productMap.put(product.getClassName().getProductClassName(), productFormList);
			}
		}
		view.addObject("allProducts", productMap);
		view.addObject("allSalesTarget", getSalesTarget());
		view.addObject("allReservationStatus", getReservationStatus());
		view.addObject("allSalesCategories", proposalHelper.getSalesCategory());
		view.addObject("currentDate", DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
		return view;
	}

	/**
	 * Returns the {@link AjaxFormSubmitResponse} for the saved filter on ViewMultipleCalendars screen
	 * @param filterId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getFilteredData")
	public AjaxFormSubmitResponse getFilteredData(@RequestParam("filterId") final long filterId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final UserFilter userFilter = multipleCalendarService.getFiltersById(filterId);
		ajaxResponse.getObjectMap().put("userFilter", userFilter.getFilterData());
		return ajaxResponse;
	}

	/**
	 * Returns the {@link ModelAndView} for the ViewMultipleCalendars
	 * @param 	calendarForm
	 * 			{@link MultipleCalendarForm} has all the information related create the screen for the Multiple Calendars
	 * @return
	 */
	@RequestMapping("/viewMultipleCalendar")
	public ModelAndView displayCalendar(@ModelAttribute("multipleCalendarForm") final MultipleCalendarForm calendarForm) {
		final Map<String, List<MultipleCalendarForm>> filterMap = new TreeMap<String, List<MultipleCalendarForm>>();
		final User loggedUser = SecurityUtil.getUser();
		final List<User> users = userService.getUserBasedOnRoleList(new String[] { SecurityUtil.ADMIN });
		if (!users.contains(loggedUser)) {
			users.add(loggedUser);
		}
		final Map<Long, String> userMap = new LinkedHashMap<Long, String>();
		for (User userValue : users) {
			userMap.put(userValue.getUserId(), userValue.getFullName());
		}
		for (UserFilter filterCriteria : multipleCalendarService.getUserFiltersByType(UserFilterTypeEnum.MULTIPLE_CALENDAR.name(), userMap.keySet())) {
			final MultipleCalendarForm form = new MultipleCalendarForm();
			form.setFilterId(filterCriteria.getFilterId());
			form.setFilterData(filterCriteria.getFilterData());
			form.setFilterName(filterCriteria.getFilterName());
			form.setUserName(userMap.get(filterCriteria.getUserId()));
			if (filterMap.containsKey(userMap.get(filterCriteria.getUserId()))) {
				filterMap.get(userMap.get(filterCriteria.getUserId())).add(form);
			} else {
				final List<MultipleCalendarForm> calendarFormList = new ArrayList<MultipleCalendarForm>();
				calendarFormList.add(form);
				filterMap.put(userMap.get(filterCriteria.getUserId()), calendarFormList);
			}
		}
		final ModelAndView view = new ModelAndView("multipleCalendar");
		view.addObject("allFilters", filterMap);
		view.addObject("userFullName", loggedUser.getFullName());
		return view;
	}

	/**
	 * Returns the {@link ModelAndView} for the ViewMultipleCalendars
	 * @param 	numberOfCalendars
	 * 			This the integer value which denotes the number of calendar to be present on the screen
	 * @param 	reservationForm
	 * 			{@link ReservationForm} has all the information related to the Calendar Reservation
	 * @return
	 */
	@RequestMapping("/viewMultipleCalendars")
	public ModelAndView displayCalendars(@RequestParam("selectedNumberOfCalendars") final int numberOfCalendars,
			@ModelAttribute("reservationForm") ReservationForm reservationForm) {
		final ModelAndView view = new ModelAndView("multipleCalendars");
		final Map<String, List<ProductForm>> productClassMap = new TreeMap<String, List<ProductForm>>();
		for (Product product : productService.getAllReservableProducts(true)) {
			final ProductForm prForm = new ProductForm();
			prForm.setProductId(product.getId());
			prForm.setDisplayName(product.getDisplayName());
			prForm.setProductType((product.getAvailsSytemId().longValue() == 13) ? "EMAIL" : "RESERVABLE");
			if (productClassMap.containsKey(product.getClassName().getProductClassName())) {
				productClassMap.get(product.getClassName().getProductClassName()).add(prForm);
			} else {
				final List<ProductForm> productFormList = new ArrayList<ProductForm>();
				productFormList.add(prForm);
				productClassMap.put(product.getClassName().getProductClassName(), productFormList);
			}
		}
		final List<Integer> randomIdArray = new ArrayList<Integer>(numberOfCalendars);
		for (int i = 0; i < numberOfCalendars; i++) {
			randomIdArray.add(NumberUtil.genRandom(1, 10000));
		}
		view.addObject("randomIdArray", randomIdArray);
		view.addObject("allProducts", productClassMap);
		view.addObject("allSalesTarget", getSalesTarget());
		view.addObject("targetTypeCriteria", getTargetTypeCriteria());
		return view;
	}

	/**
	 * Returns the targetType.
	 * @return
	 */
	private Map<Long, String> getTargetTypeCriteria() {
		final Map<Long, String> criteria = new TreeMap<Long, String>();
		criteria.put(Long.valueOf(5), proposalHelper.getTargetTypeCriteria().get(Long.valueOf(5)));
		criteria.put(Long.valueOf(8), proposalHelper.getTargetTypeCriteria().get(Long.valueOf(8)));
		criteria.put(Long.valueOf(40), proposalHelper.getTargetTypeCriteria().get(Long.valueOf(40)));
		criteria.put(Long.valueOf(35), proposalHelper.getTargetTypeCriteria().get(Long.valueOf(35)));
		return criteria;
	}

	/**
	 * Returns the targetType Elements on the basis of the targetType selected.
	 * @param sosTarTypeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTargetTypeElements")
	public AjaxFormSubmitResponse getTargetTypeElements(final String sosTarTypeId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalHelper.getTargetTypeElement(sosTarTypeId));
		return ajaxResponse;
	}

	/**
	 * Returns the Map of Sales Target and products
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllSalesTargetAndProduct")
	public Map<Long, Map<Long, String>> getSalesTargetFromProductID() {
		return productService.getAllSalesTargetsAndProducts();
	}

	/**
	 * @param productID
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getSalesTargetForProduct")
	public AjaxFormSubmitResponse getSalesTargetFromProductID(@RequestParam("productID") final String productIDs) {
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		List<Long> productIdLst = StringUtil.convertStringToLongList(productIDs);
		final Map<Long, String> allSalesTarTypMap = proposalHelper.getAllSalesTargetForMultiProduct(productIdLst);
		ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, allSalesTarTypMap);
		return ajxFormSubmtResp;
	}

	/**
	 * Returns the Reservation data
	 * @param reservationForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/getAmptReservationData")
	public ModelAndView getAmptReservationData(final ReservationForm reservationForm, @ModelAttribute final TableGrid<ReservationForm> tblGrid) {
		final List<RangeFilterCriteria> criteriaList = getAMPTFilterCriteriaList(reservationForm);
		final List<LineItem> lineItemsLst = reservationService.getProposalsForReservationSearch(criteriaList,
				tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		final List<ReservationForm> reservationList = new ArrayList<ReservationForm>();
		if (lineItemsLst != null && !lineItemsLst.isEmpty()) {
			Map<Long,String> productMap = proposalHelper.getReservableProducts(true);
			Map<Long,String> salesTargetMap = proposalHelper.getSalesTarget();
			for (LineItem lineItem : lineItemsLst) {
				final ReservationForm form = new ReservationForm();
				final Proposal proposal = lineItem.getProposalVersion().getProposalOption().getProposal();
				form.setAdvertiserName(proposal.getSosAdvertiserName());
				form.setProposalId(String.valueOf(proposal.getId()));
				form.setProposalName(proposal.getName());
				form.setProposalStatus(proposal.getProposalStatus().name());
				form.setOptionName(lineItem.getProposalVersion().getProposalOption().getName());
				form.setLineItemId(lineItem.getLineItemID());
				form.setProductName(lineItem.getProductName());
				form.setSor(lineItem.getSor());
				form.setSalesTarget(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetName());
				form.setProductId((lineItem.getSosProductId() == null) ? ZERO : String.valueOf(lineItem.getSosProductId()));
				form.setSalesTargetId(String.valueOf(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId()));
				form.setTargetingString(lineItem.getTargetingString());
				form.setSosProductClassId(lineItem.getSosProductClass());
				final List<LineItemTarget> lineItemTargets = new ArrayList<LineItemTarget>(lineItem.getGeoTargetSet());
				form.setLineItemTargetingData(targetJsonConverter.convertObjectToJson(lineItemTargets));
				form.setProposalAssignedToUserId(String.valueOf((proposal.getAssignedUser() == null) ? ConstantStrings.EMPTY_STRING : proposal.getAssignedUser().getUserId()));
				if (lineItem.getStartDate() != null && lineItem.getEndDate() != null) {
					form.setStartDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
					form.setEndDate(DateUtil.getGuiDateString(lineItem.getEndDate()));
				}
				final LineItemReservations reservation = lineItem.getReservation();
				if (reservation != null) {
					if (reservation.getExpirationDate() != null) {
						form.setExpiryDate(DateUtil.getGuiDateString(reservation.getExpirationDate()));
					}
					form.setDisplayReservationStatus(reservation.getStatus().getDisplayName());
				}
				form.setProduct_active(productMap.containsKey(lineItem.getSosProductId()) ? Constants.YES : Constants.NO);
				form.setSalesTarget_active(salesTargetMap.containsKey(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId()) ? Constants.YES : Constants.NO);
				
				reservationList.add(form);
			}
			tblGrid.setGridData(reservationList, reservationService.getProposalsForReservationSearchCount(criteriaList));
		}

		final ModelAndView view = new ModelAndView(PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Fetching the SOS Reservation search data
	 * @param reservationForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/getSosReservationData")
	public ModelAndView getSosReservationData(final ReservationForm reservationForm, @ModelAttribute final TableGrid<ReservationForm> tblGrid) {
		final List<RangeFilterCriteria> filterCriterias = getSOSFilterCriteriaList(reservationForm);
		final List<OrderLineItem> sosLineItemLst = reservationService.getSalesOrderForReservationSearch(filterCriterias,
				tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		final List<ReservationForm> reservationList = new ArrayList<ReservationForm>();

		if (sosLineItemLst != null && !sosLineItemLst.isEmpty()) {
			final Map<Long, String> salesTargetMap = getSalesTarget();
			final Map<Long, String> productMap = new LinkedHashMap<Long, String>();
			for (Product product : productService.getAllReservableProducts(true)) {
				productMap.put(product.getId(), product.getDisplayName());
			}

			for (OrderLineItem lineItem : sosLineItemLst) {
				final ReservationForm form = new ReservationForm();
				final SalesOrder order = lineItem.getSalesOrder();
				form.setSosOrderId(String.valueOf(order.getSalesOrderId()));
				if (order.getAdvertiser() != null) {
					form.setAdvertiserName(order.getAdvertiser().getCustomerName());
				}
				form.setSalesCategoryId((order.getTerritoryId() == null) ? ZERO : String.valueOf(order.getTerritoryId()));
				form.setLineItemId(lineItem.getLineItemId());
				form.setProductId(String.valueOf(lineItem.getProductId()));
				if (productMap.containsKey(lineItem.getProductId())) {
					form.setProductName(productMap.get(lineItem.getProductId()));
				}
				form.setSalesTargetId(String.valueOf(lineItem.getSalesTargetId()));
				if (salesTargetMap.containsKey(lineItem.getSalesTargetId())) {
					form.setSalesTarget(salesTargetMap.get(lineItem.getSalesTargetId()));
				}
				form.setSor(lineItem.getShareOfReservation());
				if (lineItem.getStartDate() != null && lineItem.getEndDate() != null) {
					form.setStartDate(DateUtil.getGuiDateString(lineItem.getStartDate()));
					form.setEndDate(DateUtil.getGuiDateString(lineItem.getEndDate()));
				}
				form.setTargetingString(StringUtil.getTargetingStringForOrder(lineItem));
				form.setSosURL(sosURL);
				reservationList.add(form);
			}
			tblGrid.setGridData(reservationList, reservationService.getSalesOrderForReservationSearchCount(filterCriterias));
		}

		final ModelAndView view = new ModelAndView(PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Create list of search criteria for searching the reservation from SOS
	 * @param searchForm
	 * @return
	 */
	private List<RangeFilterCriteria> getSOSFilterCriteriaList(final ReservationForm searchForm) {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;

		if (StringUtils.isNotBlank(searchForm.getAdvertiserId())) {
			filterCriteria = getFilterCriteriaFor("advertiserName", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getAdvertiserId());
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getProductIdLst() != null && searchForm.getProductIdLst().length > 0) {
			filterCriteria = getFilterCriteriaFor("productId", SearchOption.IN);
			String productIds = "";
			for(String productId : searchForm.getProductIdLst()){
				productIds = productIds + productId + ConstantStrings.COMMA;
			}
			productIds = productIds.trim().substring(0, productIds.length()-1);
			filterCriteria.setSearchString(productIds);
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getSalesTargetIdLst() != null &&searchForm.getSalesTargetIdLst().length > 0) {
			filterCriteria = getFilterCriteriaFor("salesTargetId", SearchOption.IN);
			String salesTargetIds = "";
			for(String salesTargetId : searchForm.getSalesTargetIdLst()){
				salesTargetIds = salesTargetIds + salesTargetId + ConstantStrings.COMMA;
			}
			salesTargetIds = salesTargetIds.trim().substring(0, salesTargetIds.length()-1);
			filterCriteria.setSearchString(salesTargetIds);
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getSosOrderId())) {
			filterCriteria = getFilterCriteriaFor("sosOrderId", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getSosOrderId());
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getSalesCategoryId())) {
			filterCriteria = getFilterCriteriaFor("salesCategoryId", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getSalesCategoryId());
			filterCriteriaLst.add(filterCriteria);
		}
		if(StringUtils.isNotBlank(searchForm.getSosLineItemId())){
			filterCriteria = getFilterCriteriaFor("sosLineItemId", SearchOption.EQUAL);
			filterCriteria.setSearchString(String.valueOf(searchForm.getSosLineItemId()));
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getStartDate()) || StringUtils.isNotBlank(searchForm.getEndDate())) {
			filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
			if(StringUtils.isNotBlank(searchForm.getStartDate())){
				filterCriteria.setSearchStringFrom(searchForm.getStartDate());
			}
			if(StringUtils.isNotBlank(searchForm.getEndDate())){
				filterCriteria.setSearchStringTo(searchForm.getEndDate());
			}
			filterCriteriaLst.add(filterCriteria);
		}else{
			filterCriteria = getFilterCriteriaFor("date", SearchOption.GREATER_EQUAL);
			Date currentDate = new Date();
			filterCriteria.setSearchStringFrom(DateUtil.getGuiDateString(currentDate));
			filterCriteriaLst.add(filterCriteria);
		}
		return filterCriteriaLst;
	}

	/**
	 * Create list of search criteria for searching the reservation From AMPT
	 * @param searchForm
	 * @return
	 */
	private List<RangeFilterCriteria> getAMPTFilterCriteriaList(final ReservationForm searchForm) {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;
		if (StringUtils.isNotBlank(searchForm.getProposalName())) {
			filterCriteria = getFilterCriteriaFor("proposalName", SearchOption.CONTAIN);
			filterCriteria.setSearchString(searchForm.getProposalName().trim());
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getProposalId())) {
			filterCriteria = getFilterCriteriaFor("proposalId", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getProposalId().trim());
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getReservationStatus() == null || searchForm.getReservationStatus().length == 0) {
			filterCriteria = getFilterCriteriaFor(ConstantStrings.STATUS, SearchOption.EQUAL);
			String resStatus = ConstantStrings.EMPTY_STRING;
			for (ReservationStatus status : ReservationStatus.values()) {
				if (!ReservationStatus.DELETED.name().equalsIgnoreCase(status.name())) {
					resStatus += status.name() + ConstantStrings.COMMA;
				}
			}
			filterCriteria.setSearchString(resStatus.substring(0, resStatus.length() - 1));
			filterCriteriaLst.add(filterCriteria);
		} else {
			filterCriteria = getFilterCriteriaFor(ConstantStrings.STATUS, SearchOption.EQUAL);
			filterCriteria.setSearchString(StringUtils.join(searchForm.getReservationStatus(), ConstantStrings.COMMA));
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getSalesCategoryId())) {
			filterCriteria = getFilterCriteriaFor("salescategoryId", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getSalesCategoryId());
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getAdvertiserId())) {
			filterCriteria = getFilterCriteriaFor("advertiserId", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getAdvertiserId());
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getProductIdLst() != null &&searchForm.getProductIdLst().length > 0) {
			filterCriteria = getFilterCriteriaFor("productId", SearchOption.IN);
			String productIds = "";
			for(String productId : searchForm.getProductIdLst()){
				productIds = productIds + productId + ConstantStrings.COMMA;
			}
			productIds = productIds.trim().substring(0, productIds.length()-1);
			filterCriteria.setSearchString(productIds);
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getSalesTargetIdLst() != null &&searchForm.getSalesTargetIdLst().length > 0) {
			filterCriteria = getFilterCriteriaFor("salesTargetId", SearchOption.IN);
			String salesTargetIds = "";
			for(String salesTargetId : searchForm.getSalesTargetIdLst()){
				salesTargetIds = salesTargetIds + salesTargetId + ConstantStrings.COMMA;
			}
			salesTargetIds = salesTargetIds.trim().substring(0, salesTargetIds.length()-1);
			filterCriteria.setSearchString(salesTargetIds);
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getStartDate()) || StringUtils.isNotBlank(searchForm.getEndDate())) {
			filterCriteria = getFilterCriteriaFor("date", SearchOption.BETWEEN);
			if(StringUtils.isNotBlank(searchForm.getStartDate())){
				filterCriteria.setSearchStringFrom(searchForm.getStartDate());
			}
			if(StringUtils.isNotBlank(searchForm.getEndDate())){
				filterCriteria.setSearchStringTo(searchForm.getEndDate());
			}
			filterCriteriaLst.add(filterCriteria);
		}
		if (searchForm.getLineItemId() != null) {
			filterCriteria = getFilterCriteriaFor("amptLineItemId", SearchOption.EQUAL);
			filterCriteria.setSearchString(String.valueOf(searchForm.getLineItemId()));
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getMyReservation()) && "true".equalsIgnoreCase(searchForm.getMyReservation())) {
			final String userId = String.valueOf(SecurityUtil.getUser().getUserId());
			filterCriteria = getFilterCriteriaFor("assignedUser", SearchOption.EQUAL);
			filterCriteria.setSearchString(userId);
			filterCriteriaLst.add(filterCriteria);
			// now check for reservation status as "HOLD" or "RE_NEW"
			filterCriteria = getFilterCriteriaFor("myReservationStatus", SearchOption.EQUAL);
			// Create a search string for proposal status as IN clause
			filterCriteria.setSearchString(ReservationStatus.HOLD.name() + ConstantStrings.COMMA + ReservationStatus.RE_NEW.name());
			filterCriteriaLst.add(filterCriteria);
		}
		if (StringUtils.isNotBlank(searchForm.getDaysToExpire())) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf(searchForm.getDaysToExpire()));
			filterCriteria = getFilterCriteriaFor("daysToExpire", SearchOption.LESS_EQUAL);
			filterCriteria.setSearchString(DateUtil.getGuiDateTimeString(cal.getTimeInMillis()));
			filterCriteriaLst.add(filterCriteria);
		}
		return filterCriteriaLst;
	}

	/**
	 * Returns {@link RangeFilterCriteria} for setting the attribute field and operator
	 * @param 	field
	 * 			This is the attribute, will be used when the query will be formed
	 * @param 	oper
	 * 			Query operator to be applied
	 * @return
	 */
	private RangeFilterCriteria getFilterCriteriaFor(final String field, final SearchOption oper) {
		final RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField(field);
		filterCriteria.setSearchOper(oper.toString());
		return filterCriteria;
	}

	/**
	 * Returns the Map of Proposal Id and Proposal Name also the <code>pastEndDate</code> flag for the LineItem
	 * @param proposalId
	 * @param lineItemId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProposalsList")
	public AjaxFormSubmitResponse getProposalsList(@RequestParam("proposalId") final long proposalId, @RequestParam("lineItemId") final long lineItemId) {
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		final LineItem lineItem = proposalService.getLineItemById(lineItemId);
		final Date endDate = lineItem.getEndDate();
		final Date currentDate = DateUtil.getCurrentMidnightDate();
		boolean isPastEndDate = false;
		if (lineItem.getEndDate() != null && endDate.before(currentDate)) {
			isPastEndDate = true;
		} else {
			final List<Proposal> proposalLst = proposalService.getAllProposalList();
			final Map<Long, String> proposalsMap = new HashMap<Long, String>();
			if (proposalLst != null && !proposalLst.isEmpty()) {
				for (Proposal proposal : proposalLst) {
					if (proposal.getId() != proposalId) {
						proposalsMap.put(proposal.getId(), proposal.getName());
					}
				}
			}
			ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalsMap);
		}
		ajxFormSubmtResp.getObjectMap().put("pastEndDate", isPastEndDate);
		return ajxFormSubmtResp;
	}

	/**
	 * Move LineItem Reservation data into given option and delete from existing line item
	 * @param targetProposalId
	 * @param lineItemID
	 * @param srcProposalId
	 * @param srcAssignedToUsr
	 * @param expiryDate
	 * @return
	 * @throws CustomCheckedException
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/moveReservationData")
	public AjaxFormSubmitResponse moveReservationData(final HttpServletResponse response, @RequestParam("targetProposalId") final long targetProposalId,
			@RequestParam("lineItemId") final long lineItemID, @RequestParam("srcProposalId") final long srcProposalId,
			@RequestParam("srcAssignedToUsr") final Long srcAssignedToUsr, @RequestParam("expiryDate") final String expiryDate) throws CustomCheckedException, ParseException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		List<LineItemReservations> reservationLst = proposalService.getReservationsBylineItemIDs(StringUtil.convertStringToLongList(String.valueOf(lineItemID)));
		List<Long> pastReservationLineItems = new ArrayList<Long>();
		final CustomBindingResult results = new CustomBindingResult("expiryDateValidation", pastReservationLineItems);
		pastReservationLineItems.addAll(validateReservationExpiry(reservationLst, expiryDate, results,"moveReservationExpiryDate"));
		if(results.hasErrors()){
			ajaxResponse.getObjectMap().put(Constants.RESERVATIONS, pastReservationLineItems);
			return constructResponse(response, ajaxResponse, results);
		}
		
		final Proposal proposal = proposalService.getProposalbyId(targetProposalId);
		final User loggedInUsr = SecurityUtil.getUser();
		if (srcAssignedToUsr == null || srcAssignedToUsr != loggedInUsr.getUserId()) {
			proposalService.updateAssignToUser(srcProposalId, loggedInUsr);
		}
		if (proposal.getAssignedUser() == null || proposal.getAssignedUser().getUserId() != loggedInUsr.getUserId()) {
			proposalService.updateAssignToUser(targetProposalId, loggedInUsr);
		}
		proposalService.saveReservationDataFrmProposalToAnother(lineItemID, proposal.getDefaultOption().getId(), expiryDate);
		
		proposalService.updateAllLineItemPricingStatus(proposal.getDefaultOption().getId());

		proposalService.updateProposalVersionNetImpressionAndCPM(proposal.getDefaultOption().getId(), proposal.getDefaultOption().getLatestVersion().getProposalVersion());
		ajaxResponse.getObjectMap().put("lineItemID", lineItemID);
		return ajaxResponse;
	}

	/**
	 * Deletes Line Item's Reservation
	 * @param lineItemID
	 * @param proposalId
	 * @return
	 * @throws CustomCheckedException
	 */
	@ResponseBody
	@RequestMapping("/deleteReservedLineItem")
	public AjaxFormSubmitResponse deleteLineItemReservation(@RequestParam("lineItemId") final long lineItemID,
			@RequestParam("proposalId") final long proposalId) throws CustomCheckedException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Proposal proposal = proposalService.getProposalbyId(proposalId);
		final User currentLoggedInUsr = SecurityUtil.getUser();
		if (proposal.getAssignedUser() == null || proposal.getAssignedUser().getUserId() != currentLoggedInUsr.getUserId()) {
			proposalService.updateAssignToUser(proposalId, currentLoggedInUsr);
		}
		LineItem lineItem = proposalService.getLineItemById(lineItemID);
		final boolean returnFlag = proposalService.deleteReservationByLineItemId(lineItemID);
		List<Long> productClassIdLst = productService.getProductClassIdLstByDisplayName();
		if(lineItem.getReservation()!= null &&  productClassIdLst.contains(lineItem.getSosProductClass())){
			proposalUtilService.sendMailForHomeResrvtn(lineItem, lineItem.getProposalVersion().getProposalOption().getProposal(), ConstantStrings.DELETED);
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnFlag);
		return ajaxResponse;
	}

	/**
	 * Returns all the Advertiser's Map
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAdvertiserList")
	public Map<Long, String> getAdvertiser() {
		return proposalHelper.getAdvertiser();
	}

	/**
	 * Returns the Map of {@link ReservationStatus}
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getReservationStatus")
	public Map<String, String> getReservationStatus() {
		final Map<String, String> statusMap = new TreeMap<String, String>();
		for (ReservationStatus reservationStatus : ReservationStatus.values()) {
			if (!ReservationStatus.DELETED.name().equalsIgnoreCase(reservationStatus.name())) {
				statusMap.put(reservationStatus.name(), reservationStatus.getDisplayName());
			}
		}
		return statusMap;
	}

	/**
	 * Returns the Map of Sales Targets
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getSalesTarget")
	public Map<Long, String> getSalesTarget() {
		return proposalHelper.getSalesTarget();
	}

	/**
	 * Saves the filter data
	 * @param response
	 * @param calendarForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveFilterData")
	public AjaxFormSubmitResponse saveFilterData(final HttpServletResponse response, @ModelAttribute("multipleCalendarForm") final MultipleCalendarForm calendarForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("MultipleCalendarForm", calendarForm);
		new MultipleCalendarValidator().validate(calendarForm, results);
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else if (StringUtils.isNotBlank(calendarForm.getFilterName())) {
			final boolean isDuplicatename = multipleCalendarService.isDuplicateFilterName(calendarForm.getFilterName(),
					calendarForm.getFilterId(), SecurityUtil.getUser().getUserId());
			if (isDuplicatename) {
				results.rejectValue("filterText", ErrorCodes.DuplicateFilterName, "filterText",
						new Object[] { calendarForm.getFilterName() }, UserHelpCodes.HelpDuplicateTierName);
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			final UserFilter filter = calendarForm.populate(new UserFilter());
			multipleCalendarService.saveFilter(filter);
			ajaxResponse.getObjectMap().put("filterId", filter.getFilterId());
			return ajaxResponse;
		}
	}

	/**
	 * Deletes the saved filter
	 * @param filterId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteFilter")
	public AjaxFormSubmitResponse deleteMultipleCalendarFilter(@RequestParam("filterId") final long filterId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		multipleCalendarService.deleteFilter(filterId);
		ajaxResponse.getObjectMap().put("Status", "SUCCESS");
		return ajaxResponse;
	}
	
	/**
	 * Returns the {@link AjaxFormSubmitResponse} for the validate bulk reservation screen
	 * @param filterId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateReservation")
	public AjaxFormSubmitResponse validateReservation(@RequestParam("lineItemIds") final String lineItemIds) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		List<LineItem> lineItemLst = proposalService.getLineItems(lineItemIds);
		List<Long> lineTemsWithPastFlight =  new ArrayList<Long>();
		List<Long> lineTemsWithInactiveProduct =  new ArrayList<Long>();
		Map<Long,String> productMap = proposalHelper.getReservableProducts(true);
		Map<Long,String> salesTargetMap = proposalHelper.getSalesTarget();
		for (LineItem lineItem : lineItemLst) {
			if(productMap.containsKey(lineItem.getSosProductId()) && salesTargetMap.containsKey(lineItem.getLineItemSalesTargetAssocs().get(0).getSosSalesTargetId())){
				if(lineItem.getStartDate().before(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))){
					lineTemsWithPastFlight.add(lineItem.getLineItemID());
				}
			}else{
				lineTemsWithInactiveProduct.add(lineItem.getLineItemID());
			}
		}
		ajaxResponse.getObjectMap().put("FLIGHT", lineTemsWithPastFlight);
		ajaxResponse.getObjectMap().put("INACTIVE", lineTemsWithInactiveProduct);
		return ajaxResponse;
	}
	
	/**
	 * Returns the {@link AjaxFormSubmitResponse} for the saved filter on ViewMultipleCalendars screen
	 * @param filterId
	 * @return
	 * @throws CustomCheckedException 
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping("/updateExpiryDate")
	public AjaxFormSubmitResponse updateExpiryDate(final HttpServletResponse response,@RequestParam("lineItemIds") final String lineItemIds, @RequestParam("proposalIds") final String proposalIds, @RequestParam("expiryDate") final String expiryDate) throws CustomCheckedException, ParseException {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		List<LineItemReservations> reservationLst = proposalService.getReservationsBylineItemIDs(StringUtil.convertStringToLongList(lineItemIds));
		List<Long> pastReservationLineItems = new ArrayList<Long>();
		final CustomBindingResult results = new CustomBindingResult("expiryDateValidation", pastReservationLineItems);
		pastReservationLineItems.addAll(validateReservationExpiry(reservationLst, expiryDate, results, "renewReservationExpiryDate"));
		if(results.hasErrors()){
			ajaxResponse.getObjectMap().put(Constants.RESERVATIONS, pastReservationLineItems);
			return constructResponse(response, ajaxResponse, results);
		}
		for (LineItemReservations reservation : reservationLst) {
			reservation.setLastRenewedOn(DateUtil.getCurrentDate());
			reservation.setExpirationDate(DateUtil.parseToDate(expiryDate));
			reservation.setStatus(ReservationStatus.RE_NEW);
		}
		final User currentLoggedInUsr = SecurityUtil.getUser();
		List<Proposal> proposalLst = proposalService.getProposalsAndAssgndUsrs(StringUtil.convertStringToLongList(proposalIds));
		for (Proposal proposal : proposalLst) {
			if (proposal.getAssignedUser() == null || proposal.getAssignedUser().getUserId() != currentLoggedInUsr.getUserId()) {
				proposalService.updateAssignToUser(proposal.getId(), currentLoggedInUsr);
			}
		}
		proposalService.updateAllLineItemReservations(reservationLst);
		proposalHelper.sendMailForBulkRenewReservation(reservationLst, proposalLst);
		
		/*
		 * Code to send email for Overbooking
		 */
		for (LineItemReservations reservation : reservationLst) {
			LineItemForm lineItemForm = new LineItemForm();
			lineItemForm.populateForm(reservation.getProposalLineItem());
			lineItemForm.setProposalID(String.valueOf(reservation.getProposalLineItem().getProposalId()));
			Map<String,Object> sorMap = calculateSORWithLineItems(lineItemForm, lineItemForm.getProductType());
			 final String calculateSOR = sorMap.containsKey("SOR") ? (String)sorMap.get("SOR") : "100";
			if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType()) ) {
				if(NumberUtil.doubleValue(lineItemForm.getSor()) > NumberUtil.doubleValue(calculateSOR)) {					
					proposalHelper.sendMailForOverBooking( reservation.getProposalLineItem(), sorMap);
				}
			}else if(LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType()) ) {
				if(EmailReservationStatus.RESERVED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.OVERBOOKED.getDisplayName().equals(calculateSOR) || EmailReservationStatus.SOLD.getDisplayName().equals(calculateSOR)) {
					proposalHelper.sendMailForOverBooking(reservation.getProposalLineItem(), sorMap);
				}
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "SUCCESS");
		return ajaxResponse;
	}
	
	/**
	 * 
	 * @param lineItemForm
	 * @param type
	 * @return
	 */
	private Map<String,Object> calculateSORWithLineItems(final LineItemForm lineItemForm, final String type) {
		Map <String,Object> sorMap = new HashMap<String, Object>();
		List<LineItem> lineItemLst = new ArrayList<LineItem>();
		List<OrderLineItem> orderLineItemLst = new ArrayList<OrderLineItem>();
		double sor = 0.0D;
		final ReservationTO reservationTO = lineItemForm.populateToReservationVo((new ReservationTO()));
		reservationTO.setStartDate(DateUtil.parseToDate(lineItemForm.getStartDate()));
		reservationTO.setEndDate(DateUtil.parseToDate(lineItemForm.getEndDate()));
		ReservationInfo reservationInfo = null; 
		for (ReservationInfo reservationDetail : reservationService.getReservationDetailForCalendar(reservationTO)) {
			if (sor < reservationDetail.getSor()) {
				sor = reservationDetail.getSor();
			}
			lineItemLst.addAll(reservationDetail.getProposalLineItems());
			orderLineItemLst.addAll(reservationDetail.getOrderLineItems());
			reservationInfo = reservationDetail;
		}
		if(LineItemProductTypeEnum.RESERVABLE.getShortName().equals(type)) {
			sorMap.put("SOR" , NumberUtil.formatDouble(sor > 100 ? 0 : 100 - sor, true));
		} else {
			sorMap.put("SOR" , getReservationStatus(reservationInfo));
		}
		if(!lineItemLst.isEmpty()){
			sorMap.put("PROPOSALLI" , lineItemLst);
		}
		if(!orderLineItemLst.isEmpty()){
			sorMap.put("ORDERLI" , orderLineItemLst);
		}
		return sorMap;
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
		if (reservationInfo != null) {
			if (reservationInfo.getSor() > 100) {
				status = EmailReservationStatus.OVERBOOKED.getDisplayName();
			} else if (reservationInfo.getSor() == 100) {
				status = (reservationInfo.getProposals() > 0) ? EmailReservationStatus.RESERVED.getDisplayName() : EmailReservationStatus.SOLD.getDisplayName();
			}
		}
		return status;
	}
	
	public List<Long> validateReservationExpiry(List<LineItemReservations> reservationLst,String expiryDate,final Errors errors, String targetElement) throws ParseException{
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		Date expiration = format.parse(expiryDate);
		List<Long> pastReservations = new ArrayList<Long>();
		Calendar cal = Calendar.getInstance();
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		for (LineItemReservations reservation : reservationLst) {
			if(reservation.getProposalLineItem().getStartDate().compareTo(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))< 0){
				pastReservations.add(reservation.getProposalLineItem().getLineItemID());
			}
		}
		if (!pastReservations.isEmpty()) {
			customErrors.rejectValue(targetElement, ErrorCodes.PastStartDateError, targetElement, new Object[] { Constants.START_DATE },
					UserHelpCodes.HelpPastStartDate);
		}
		
		if (pastReservations.isEmpty()) {//check for expiry date should be in future 
			if (expiration.before(DateUtil.parseToDate(DateUtil.getGuiDateString(cal.getTime())))) {
				pastReservations.add(111L);
			}
			if (!pastReservations.isEmpty()) {
					customErrors.rejectValue(targetElement,ErrorCodes.LineItemExpiryBeforeCurrent,targetElement, new Object[] {},UserHelpCodes.HelpExpiryDateBeforeCurrent);
			}
		}
		if (pastReservations.isEmpty()) {//check for expiry date should be less than 1 year
			cal.add(Calendar.MONTH, 12);
			if (expiration.after(DateUtil.parseToDate(DateUtil.getGuiDateString(cal.getTime())))) {
				pastReservations.add(111L);
			}
			if (!pastReservations.isEmpty()) {
					customErrors.rejectValue(targetElement,ErrorCodes.LineItemExpiryBeyondOeYear,targetElement, new Object[] {},UserHelpCodes.HelpExpiryDateBeyondOneYear);
			}
		}

		return pastReservations;
	}
	public void setReservationService(final ICalendarReservationService service) {
		this.reservationService = service;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setTargetJsonConverter(final TargetJsonConverter jsonConverter) {
		this.targetJsonConverter = jsonConverter;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setMultipleCalendarService(final IMultipleCalendarService calendarService) {
		this.multipleCalendarService = calendarService;
	}

	public void setProposalUtilService(ProposalUtilService proposalUtilService) {
		this.proposalUtilService = proposalUtilService;
	}
	
}
