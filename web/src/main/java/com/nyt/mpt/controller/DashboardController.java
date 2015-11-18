/**
 *
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.form.DashboardFilterForm;
import com.nyt.mpt.form.DashboardForm;
import com.nyt.mpt.form.ProposalForm;
import com.nyt.mpt.service.IDashboardService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.Criticality;
import com.nyt.mpt.util.enums.DateCriteriaEnum;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.SfProposalRevisionTypeEnum;
import com.nyt.mpt.util.filter.DateRangeFilterCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.InFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>DashboardController</code> includes all the methods to show the dash-board and all its related {@link Proposal} in the grid. 
 * This includes methods for dash-board for various views i.e. bar view, graph view and calendar view. This also include how the {@link UserFilter} will be saved in database and fetched when required
 * @author abhijeet.jethalia
 */

@Controller
@RequestMapping("/dashboard/*")
public class DashboardController extends AbstractBaseController {

	private static final String DUE_ON = "dueOn";

	private IDashboardService dashboardService;

	private ProposalHelper proposalHelper;

	private IUserService userService;

	/**
	 * Returns the {@link ModelAndView} when the dash-board loads for the first time the application starts
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage() {
		final ModelAndView view = new ModelAndView("dashboardDetailPage");
		view.addObject("savedFilter", getUserSavedFilter());
		view.addObject("salesCategory", proposalHelper.getSalesCategory());
		view.addObject("dateCriteria", getDateCriteria());
		view.addObject("priority", getPriorityMap());
		view.addObject("userMap", getUserMap());
		return view;
	}

	/**
	 * Returns {@link AjaxFormSubmitResponse} after the {@link DashboardFilterForm} has been submitted for saving {@link UserFilter} from the UI
	 * @param 	filterForm
	 * 			{@link DashboardFilterForm} has all the data from UI which will go for saving in the AMPT database
	 * @return
	 * 			Returns {@link AjaxFormSubmitResponse} after saving {@link UserFilter}
	 */
	@ResponseBody
	@RequestMapping("/saveFilterData")
	public AjaxFormSubmitResponse saveFilterData(@ModelAttribute("dashboardFilterForm") final DashboardFilterForm filterForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final UserFilter filter = filterForm.populate(new UserFilter());
		dashboardService.saveDashboardFilter(filter);
		ajaxResponse.getObjectMap().put("filterId", filter.getFilterId());
		return ajaxResponse;
	}

	/**
	 * Returns {@link AjaxFormSubmitResponse} when a user selects  to delete {@link UserFilter} from UI
	 * @param 	filterId
	 * 			{@link UserFilter}'s Id which has been selected from the UI for deletion from the AMPT database
	 * @return
	 * 			Returns {@link AjaxFormSubmitResponse} after deletion of {@link UserFilter}
	 */
	@ResponseBody
	@RequestMapping("/deleteFilter")
	public AjaxFormSubmitResponse deleteFilter(@RequestParam(required = true) final long filterId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		dashboardService.deleteDashboardFilter(filterId);
		ajaxResponse.getObjectMap().put("Status", "SUCCESS");
		return ajaxResponse;
	}

	/**
	 * Returns {@link AjaxFormSubmitResponse} when a user selects {@link UserFilter} from UI
	 * @param 	filterId
	 * 			{@link UserFilter}'s Id which has been selected from the UI for getting {@link DashboardFilterForm} from the AMPT database
	 * @return
	 * 			Returns {@link AjaxFormSubmitResponse} when a user selects {@link UserFilter}
	 */
	@ResponseBody
	@RequestMapping("/getFilterData")
	public AjaxFormSubmitResponse getFilterData(@RequestParam(required = true) final long filterId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final UserFilter filter = dashboardService.getDashboardFilterById(filterId);
		final DashboardFilterForm form = new DashboardFilterForm();
		form.populateForm(filter);
		ajaxResponse.getObjectMap().put("filter", form);
		return ajaxResponse;
	}

	/**
	 * @return
	 * 		Returns List of user's sales categories
	 */
	@ResponseBody
	@RequestMapping("/getUserSaleesCategory")
	public List<Long> getUserSaleesCategory(@ModelAttribute("dashboardFilterForm") final DashboardFilterForm filterForm) {
		final List<Long> salesCategoryList = new ArrayList<Long>();
		final List<SalesCategory> salesCategories = userService.getUserSalesCategories(SecurityUtil.getUser().getUserId());
		for (SalesCategory salesCategory : salesCategories) {
			salesCategoryList.add(salesCategory.getSosSalesCategoryId());
		}
		return salesCategoryList;
	}

	/**
	 * Loads dash-board's grid data whenever it'll be called explicitly, when user apply filters, when user clicks on graphs i.e. bars or pie chart 
	 * @param 	tblGrid
	 * 			{@link TableGrid} object which has information related to grid
	 * @param 	filterForm
	 * 			{@link DashboardFilterForm} object which has all the values of filter form if user applies any or whatever default values has in it
	 * @param 	graphFilter
	 * 			{@link DashboardGraphFilter} object which has all the information when user clicks on any graph in the dash-board
	 * @return
	 * 			Returns {@link ModelAndView} which has all the information for the requested {@link Proposal} List
	 */
	@RequestMapping("/loadGridData")
	public ModelAndView loadMasterGridData(@ModelAttribute final TableGrid<ProposalForm> tblGrid, @ModelAttribute final DashboardFilterForm filterForm, final DashboardGraphFilter graphFilter) {
		final ModelAndView view = new ModelAndView("dashboardGridData");
		final List<FilterCriteria> filterCriteria = getFilterCriteriaList(filterForm, graphFilter);
		final List<Proposal> proposalList = dashboardService.getProposalList(filterCriteria, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (proposalList != null && !proposalList.isEmpty()) {
			tblGrid.setGridData(convertToProposalForm(proposalList), dashboardService.getProposalCount(filterCriteria));
		}
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Returns the List of {@link ProposalForm} from List of {@code proposalList} to show in the UI
	 * @param 	proposalList
	 * 			List of {@code proposalList} for which we want the conversion to List of {@link ProposalForm}
	 * @return
	 */
	private List<ProposalForm> convertToProposalForm(final List<Proposal> proposalList) {
		final List<ProposalForm> proposalFormList = new ArrayList<ProposalForm>(proposalList.size());
		final Map<Long, String> advertiserMap = proposalHelper.getAdvertiser();
		final Map<Long, String> salescategoryMap = proposalHelper.getSalesCategory();
		for (Proposal proposal : proposalList) {
			final ProposalForm proposalForm = new ProposalForm();
			proposalForm.populateForm(proposal);
			proposalForm.setSalescategory(salescategoryMap.get(proposal.getSosSalesCategoryId()));
			if (StringUtils.isNotBlank(proposalForm.getAdvertiserName()) && "0".equals(proposalForm.getAdvertiserName())) {
				proposalForm.setAdvertiserName(proposalForm.getNewAdvertiserName());
			} else {
				proposalForm.setAdvertiserName(advertiserMap.get(proposal.getSosAdvertiserId()));
			}
			final ProposalOption option = proposal.getDefaultOption();
			proposalForm.setBudget((option.getBudget() == null) ? ConstantStrings.EMPTY_STRING : NumberUtil.formatDouble(option.getBudget(), true));
			proposalForm.setRequestedOn((proposal.getDateRequested() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateTimeString(proposal.getDateRequested().getTime()));
			proposalForm.setPricingSubmittedDate((proposal.getPricingSubmittedDate() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateTimeString(proposal.getPricingSubmittedDate().getTime()));
			proposalFormList.add(proposalForm);
		}
		return proposalFormList;
	}

	/**
	 * Returns the List of {@link FilterCriteria} which will be applied when the query will be executed to get the data from database
	 * @param 	filterForm
	 * 			{@link DashboardFilterForm} object has all the filter form data
	 * @param 	graphFilter
	 * 			{@link DashboardGraphFilter} object has all the information when user clicks any of the graph in the dash-board
	 * @return
	 */
	private List<FilterCriteria> getFilterCriteriaList(final DashboardFilterForm filterForm, final DashboardGraphFilter graphFilter) {
		final List<FilterCriteria> criteriaList = new ArrayList<FilterCriteria>();
		criteriaList.add(getProposalStatusFilterCriteria());

		if (filterForm.getSalesCategory() != null && filterForm.getSalesCategory().length > 0) {
			final InFilterCriteria<Long> criteria = new InFilterCriteria<Long>();
			criteria.setSearchField("salescategory");
			criteria.setSearchOper(SearchOption.EQUAL.name());
			criteria.setSearchvalues(Arrays.asList(StringUtil.convertStringArrayToLongArray(filterForm.getSalesCategory())));
			criteriaList.add(criteria);
		}

		if (graphFilter != null && StringUtils.isNotBlank(graphFilter.getUserId())) {
			final FilterCriteria criteria = new FilterCriteria();
			criteria.setSearchField("assignedUser");
			criteria.setSearchOper(SearchOption.EQUAL.name());
			criteria.setSearchString(graphFilter.getUserId());
			criteriaList.add(criteria);
		} else if (filterForm.getUser() != null && filterForm.getUser().length > 0) {
			final InFilterCriteria<Long> criteria = new InFilterCriteria<Long>();
			criteria.setSearchField("assignedUser");
			criteria.setSearchOper(SearchOption.EQUAL.name());
			criteria.setSearchvalues(Arrays.asList(StringUtil.convertStringArrayToLongArray(filterForm.getUser())));
			criteriaList.add(criteria);
		}

		if (filterForm.getPriority() != null && filterForm.getPriority().length > 0) {
			final InFilterCriteria<Criticality> criteria = new InFilterCriteria<Criticality>();
			criteria.setSearchField("criticality");
			criteria.setSearchOper(SearchOption.EQUAL.name());
			final List<Criticality> statusList = new ArrayList<Criticality>(2);
			for (final String criticality : filterForm.getPriority()) {
				statusList.add(Criticality.findByName(criticality));
			}
			criteria.setSearchvalues(statusList);
			criteriaList.add(criteria);
		}

		if (graphFilter != null) {
			if (StringUtils.isNotBlank(graphFilter.getFilterDate())) {
				final DateRangeFilterCriteria criteria = new DateRangeFilterCriteria();
				criteria.setSearchField(DUE_ON);
				criteria.setSearchOper(SearchOption.BETWEEN.name());

				final Date dateFrom = DateUtil.parseToDate(graphFilter.getFilterDate());
				criteria.setSearchStringFrom(DateUtil.convertToMidNightDate(dateFrom));
				criteria.setSearchStringTo(DateUtil.addDaysToDate(dateFrom, 1));
				criteriaList.add(criteria);
			} else if (StringUtils.isNotBlank(graphFilter.getStatus())) {
				if ("AHEAD".equalsIgnoreCase(graphFilter.getStatus())) {
					final DateRangeFilterCriteria criteria = new DateRangeFilterCriteria();
					criteria.setSearchField(DUE_ON);
					criteria.setSearchOper(SearchOption.GREATER.name());
					criteria.setSearchStringFrom(DateUtil.getCurrentMidnightDate());
					criteriaList.add(criteria);
				} else {
					final DateRangeFilterCriteria criteria = new DateRangeFilterCriteria();
					criteria.setSearchField(DUE_ON);
					criteria.setSearchOper(SearchOption.LESS.name());
					criteria.setSearchStringTo(DateUtil.getCurrentMidnightDate());
					criteriaList.add(criteria);
				}
			}
		}
		if (StringUtils.isNotBlank(filterForm.getDueDate())) {
			final DateCriteriaEnum dateCriteria = DateCriteriaEnum.findByName(filterForm.getDueDate());
			final Calendar calendar = Calendar.getInstance();

			final DateRangeFilterCriteria criteria = new DateRangeFilterCriteria();
			criteria.setSearchField(DUE_ON);
			criteria.setSearchOper(SearchOption.BETWEEN.name());
			if (dateCriteria.getDayDifferenceFormToDay() < 0) {
				calendar.add(Calendar.DAY_OF_MONTH, dateCriteria.getDayDifferenceFormToDay());
				criteria.setSearchStringFrom(DateUtil.convertToMidNightDate(calendar.getTime()));
				criteria.setSearchStringTo(DateUtil.getTomorrowMidnightDate());
			} else {
				if (DateCriteriaEnum.THIS_WEEK.name().equals(filterForm.getDueDate())) {
					calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - (calendar.get(Calendar.DAY_OF_WEEK) - 2));
					criteria.setSearchStringFrom(DateUtil.convertToMidNightDate(calendar.getTime()));
					calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
					criteria.setSearchStringTo(DateUtil.convertToMidNightDate(calendar.getTime()));
				} else {
					calendar.add(Calendar.DAY_OF_MONTH, dateCriteria.getDayDifferenceFormToDay());
					criteria.setSearchStringFrom(DateUtil.getCurrentMidnightDate());
					criteria.setSearchStringTo(DateUtil.convertToMidNightDate(calendar.getTime()));
				}
			}
			criteriaList.add(criteria);
		}
		return criteriaList;
	}

	private InFilterCriteria<ProposalStatus> getProposalStatusFilterCriteria() {
		final List<ProposalStatus> statusList = new ArrayList<ProposalStatus>(5);
		statusList.add(ProposalStatus.INPROGRESS);
		statusList.add(ProposalStatus.REVIEW);
		statusList.add(ProposalStatus.UNASSIGNED);

		final InFilterCriteria<ProposalStatus> criteria = new InFilterCriteria<ProposalStatus>();
		criteria.setSearchField("proposalStatus");
		criteria.setSearchOper(SearchOption.EQUAL.name());
		criteria.setSearchvalues(statusList);
		return criteria;
	}


	/**
	 * Returns List of {@link DashboardForm} which contains following information regarding the graphs : 
	 *           <ul>
	 *             <li> {@code userId} user Id of the particular bar
	 *             <li> {@code username} user name which is shown under the bar
	 *             <li> {@code redCount} number of red counts in the bar
	 *             <li> {@code greenCount} number of green counts in the bar
	 *           </ul> 
	 * @param 	filterForm
	 * 			{@link DashboardFilterForm} object has all the filter form data
	 * @return
	 * 			Returns List of {@link DashboardForm} which contains the graph related information
	 */
	@ResponseBody
	@RequestMapping("/getGraphData")
	public List<DashboardForm> getGraphData(@ModelAttribute final DashboardFilterForm filterForm) {
		final List<FilterCriteria> filterCriteria = getFilterCriteriaList(filterForm, null);
		return getDashboardFormList(dashboardService.getDashboardProposal(filterCriteria), filterForm.getUser());
	}

	/**
	 * Returns List of {@link DashboardForm} which has all the {@link User} mapped with the corresponding user and List of {@link Proposal}
	 * @param 	proposalList
	 * 			List of {@link Proposal} for the applied {@link FilterCriteria} which will give a map of user and corresponding List of {@link Proposal}
	 * @param 	filteredUser
	 * 			Array of users from {@link DashboardFilterForm}
	 * @return
	 * 			Returns List of {@link DashboardForm} which has all the {@link User} mapped
	 */
	private List<DashboardForm> getDashboardFormList(final List<Proposal> proposalList, final String[] filteredUser) {
		final List<DashboardForm> formList = new ArrayList<DashboardForm>();
		if (proposalList != null && !proposalList.isEmpty()) {
			final Map<User, List<Proposal>> userMap = getUserProposalMap(proposalList);
			DashboardForm unassignedForm = null, revisionForm = null, salesOrderForm = null, unassignedFormPM = null;
			for (User user : userMap.keySet()) {
				final DashboardForm statusForm = new DashboardForm();
				if (user == null) {
					statusForm.setUserId(0);
					statusForm.setUserName("Un-Assigned");
					unassignedForm = statusForm;
				} else if (user.getUserId() == -1L) {
					statusForm.setUserId(-1L);
					statusForm.setUserName(SfProposalRevisionTypeEnum.REVISION.getDisplayName());
					revisionForm = statusForm;
				} else if (user.getUserId() == -2L) {
					statusForm.setUserId(-2L);
					statusForm.setUserName(SfProposalRevisionTypeEnum.SALESORDER.getDisplayName());
					salesOrderForm = statusForm;
				} else if(user.getUserId() == -3L) {
					statusForm.setUserId(-3);
					statusForm.setUserName("Pending For Pricing Review");
					unassignedFormPM = statusForm;
				} else {
					if((filteredUser == null && SecurityUtil.getUser().getUserId() == user.getUserId()) || (filteredUser != null && filteredUser.length > 0 )) {
						statusForm.setUserId(user.getUserId());
						statusForm.setUserName(user.getFirstName());
						formList.add(statusForm);
					}
				}
				setProposalState(userMap.get(user), statusForm);
			}
			Collections.sort(formList);
				if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
					if (unassignedFormPM != null) {
						formList.add(0, unassignedFormPM);
					}
				}
				if (revisionForm != null) {
					formList.add(0, revisionForm);
				}
				if (salesOrderForm != null) {
					formList.add(0, salesOrderForm);
				}
				if (unassignedForm != null) {
					formList.add(0, unassignedForm);
				}
		}
		return formList;
	}

	/**
	 * Returns the Map of {@link User} and corresponding List of {@link Proposal}
	 * @param 	proposalList
	 * 			List of {@link Proposal} for the applied {@link FilterCriteria} which will give a map of user and corresponding List of {@link Proposal}
	 * @return
	 * 			Returns the Map of {@link User} and corresponding List of {@link Proposal}
	 */
	private Map<User, List<Proposal>> getUserProposalMap(final List<Proposal> proposalList) {
		final Map<User, List<Proposal>> userProposalMap = new LinkedHashMap<User, List<Proposal>>();
		User unAssignedUsr = null, salesOrderUsers = null, pricingUn = null;
		for (Proposal proposal : proposalList) {
			User user = proposal.getAssignedUser();
			if (user == null && SfProposalRevisionTypeEnum.REVISION.name().equals(proposal.getSfRevisionType())) {
				if (unAssignedUsr == null) {
					unAssignedUsr = new User();
					unAssignedUsr.setUserId(-1L);
				}
				user = unAssignedUsr;
			}
			if(user == null && SfProposalRevisionTypeEnum.SALESORDER.name().equals(proposal.getSfRevisionType())) {
				if (salesOrderUsers == null) {
					salesOrderUsers = new User();
					salesOrderUsers.setUserId(-2L);
				}
				user = salesOrderUsers;
			}
			if(proposal.isWithPricing() && !SecurityUtil.isUserPricingAdmin(user)) {
				if (pricingUn == null) {
					pricingUn = new User();
					pricingUn.setUserId(-3L);
				}
				user = pricingUn;
			}
			if (userProposalMap.containsKey(user)) {
				userProposalMap.get(user).add(proposal);
			} else {
				final List<Proposal> propsList = new ArrayList<Proposal>();
				propsList.add(proposal);
				userProposalMap.put(user, propsList);
			}
		}
		return userProposalMap;
	}

	/**
	 * Sets red and green counts in {@link DashboardForm} which says if the {@link Proposal} is before the schedule or ahead the schedule
	 * @param 	proposalList
	 * 			List of {@link Proposal} for the applied {@link FilterCriteria}
	 * @param 	dashboardForm
	 * 			{@link DashboardForm} which has information about all the counts for schedule ahead or before
	 */
	private void setProposalState(final List<Proposal> proposalList, final DashboardForm dashboardForm) {
		final Date currentDate = DateUtil.getCurrentMidnightDate();
		for (final Proposal proposal : proposalList) {
			final Date dueDate = proposal.getDueDate();
			if (dueDate.before(currentDate)) {
				dashboardForm.setRedCount(dashboardForm.getRedCount() + 1);
			} else {
				dashboardForm.setGreenCount(dashboardForm.getGreenCount() + 1);
			}
		}
	}

	/**
	 * Returns List of {@link DashboardCalendarVo}
	 * @param 	filterForm
	 * 			{@link DashboardFilterForm} object has all the filter form data
	 * @param 	startDate
	 * 			Initial start date of calendar from where calendar has to start picking data
	 * @param 	endDate
	 * 			End date of the calendar till when calendar has to pick data
	 * @return
	 * 			Returns List of {@link DashboardCalendarVo}
	 */
	@ResponseBody
	@RequestMapping("/getCalendarData")
	public List<DashboardCalendarVo> getCalendarData(@ModelAttribute final DashboardFilterForm filterForm, @RequestParam final String startDate, @RequestParam final String endDate) {
		final List<FilterCriteria> filterCriteria = getFilterCriteriaList(filterForm, null);
		return getDashboardCalendarData(dashboardService.getDashboardProposal(filterCriteria), startDate, endDate);
	}

	@SuppressWarnings("unchecked")
	private List<DashboardCalendarVo> getDashboardCalendarData(final List<Proposal> proposalList, final String startDate, final String endDate) {
		if (proposalList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Date currentDate = DateUtil.parseToDate(startDate);
		final Date finalDate = DateUtil.parseToDate(endDate);

		final List<DashboardCalendarVo> detailList = new ArrayList<DashboardCalendarVo>();
		while (currentDate.before(finalDate) || currentDate.equals(finalDate)) {
			int proposals = 0;
			final DashboardCalendarVo detailVo = new DashboardCalendarVo();
			for (Proposal proposal : proposalList) {
				if (DateUtil.parseToDate(DateUtil.getGuiDateString(proposal.getDueDate())).equals(currentDate)) {
					proposals++;
				}
			}
			detailVo.setStart(DateUtil.getYieldexDateString(currentDate));
			detailVo.setProposals(proposals);
			if (detailVo.getProposals() > 0) {
				detailList.add(detailVo);
			}
			currentDate = DateUtils.addDays(currentDate, 1);
		}
		return detailList;
	}

	/**
	 * Returns the Map of user's name and Ids based on following Roles :
	 *   <ul>
	 *     <li> {@code role PAN}  PRICING ADMIN
	 *     <li> {@code role PLR } MEDIA PLANNER
	 *   </ul>  
	 * @return
	 * 		Returns the Map of user's name and Ids
	 */
	private Map<Long, String> getUserMap() {
		if (SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
			final List<User> userList = userService.getUserBasedOnRoleList(new String[] { SecurityUtil.PRICING_ADMIN });
			final Map<Long, String> userMap = new LinkedHashMap<Long, String>(userList.size());
			if (!userList.isEmpty()) {
				for (User user : userList) {
					userMap.put(user.getUserId(), user.getFullName());
				}
			}
			return userMap;
		} else {
			final List<User> userList = userService.getUserBasedOnRoleList(new String[] { SecurityUtil.MEDIA_PLANNER });
			final Map<Long, String> userMap = new LinkedHashMap<Long, String>(userList.size());
			if (!userList.isEmpty()) {
				for (User user : userList) {
					if (user.isDisplayOnWarRoom()) {
						userMap.put(user.getUserId(), user.getFullName());
					}
				}
			}
			return userMap;
		}
	}

	/**
	 * Returns the Map of {@link Criticality}'s name and displayName to be used in {@link DashboardFilterForm}
	 */
	private Map<String, String> getPriorityMap() {
		final Map<String, String> criticalityMap = new TreeMap<String, String>();
		for (Criticality criticality : Criticality.values()) {
			criticalityMap.put(criticality.name(), criticality.getDisplayName());
		}
		return criticalityMap;
	}

	/**
	 * Returns the Map of {@link DateCriteriaEnum}'s name and displayName to be used in {@link DashboardFilterForm}
	 */
	private Map<String, String> getDateCriteria() {
		final Map<String, String> dateCriteriaMap = new LinkedHashMap<String, String>();
		for (DateCriteriaEnum dateCriteria : DateCriteriaEnum.values()) {
			dateCriteriaMap.put(dateCriteria.name(), dateCriteria.getDisplayName());
		}
		return dateCriteriaMap;
	}

	/**
	 * Returns the Map of {@link UserFilter}'s id and FilterName to be used in {@link DashboardFilterForm}
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, String> getUserSavedFilter() {
		Map<Long, String> filterMap = Collections.EMPTY_MAP;
		final List<UserFilter> filterList = dashboardService.getUserSaveFilterList(SecurityUtil.getUser().getUserId());
		if (filterList != null && !filterList.isEmpty()) {
			filterMap = new LinkedHashMap<Long, String>(filterList.size());
			for (UserFilter filter : filterList) {
				filterMap.put(filter.getFilterId(), filter.getFilterName());
			}
		}
		return filterMap;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setDashboardService(final IDashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public static class DashboardCalendarVo {
		private String start;

		private int proposals;

		public String getStart() {
			return start;
		}

		public void setStart(final String start) {
			this.start = start;
		}

		public int getProposals() {
			return proposals;
		}

		public void setProposals(final int proposals) {
			this.proposals = proposals;
		}

		public String getTitle() {
			return ConstantStrings.EMPTY_STRING;
		}
	}

	public static class DashboardGraphFilter {

		private String userId;

		private String status;

		private String filterDate;

		public String getUserId() {
			return userId;
		}

		public void setUserId(final String userId) {
			this.userId = userId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(final String status) {
			this.status = status;
		}

		public String getFilterDate() {
			return filterDate;
		}

		public void setFilterDate(final String filterDate) {
			this.filterDate = filterDate;
		}
	}
}