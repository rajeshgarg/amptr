/**
 * 
 */
package com.nyt.mpt.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.service.IDashboardService;
import com.nyt.mpt.service.impl.UserService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.filter.DateRangeFilterCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.InFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * JUnit test for Dashboard service
 * 
 * @author surendra.singh
 */
public class DashboardTest extends AbstractTest {

	@Autowired
	@Qualifier("dashboardService")
	private IDashboardService dashboardService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
	}

	/**
	 * Return a Proposal DashboardGrid List count based on filter criteria
	 */
	@Test
	public void testGetProposalDashboardGridListCount() {
		int totalCount = dashboardService.getProposalCount(null);
		int count = 0;
		List<FilterCriteria> criterialList = new ArrayList<FilterCriteria>();
		DateRangeFilterCriteria dateRangeFilterCriteria= new DateRangeFilterCriteria();
		dateRangeFilterCriteria.setSearchField("dueOn");
		dateRangeFilterCriteria.setSearchStringTo(DateUtil.getCurrentDate());
		dateRangeFilterCriteria.setSearchOper(SearchOption.LESS.name());
		criterialList.add(dateRangeFilterCriteria);		
		count = dashboardService.getProposalCount(criterialList);
		Assert.assertTrue(totalCount >= count);
		
		criterialList.clear();
		dateRangeFilterCriteria.setSearchOper(SearchOption.GREATER.name());
		dateRangeFilterCriteria.setSearchStringFrom(DateUtil.getCurrentDate());
		criterialList.add(dateRangeFilterCriteria);
		count = dashboardService.getProposalCount(criterialList);
		Assert.assertTrue(totalCount >= count);
		
		
		criterialList.clear();
		dateRangeFilterCriteria.setSearchOper(SearchOption.BETWEEN.name());
		dateRangeFilterCriteria.setSearchStringFrom(DateUtil.getPriorDateFromCurrentDate(365));
		dateRangeFilterCriteria.setSearchStringTo(DateUtil.getCurrentDate());
		criterialList.add(dateRangeFilterCriteria);
		count = dashboardService.getProposalCount(criterialList);
		Assert.assertTrue(totalCount >= count);
	}

	/**
	 * return proposal DashBoard Grid Data based on filter criteria, proposal
	 * with latest version will be fetched
	 */
	@Test
	public void testGetProposalDashboardGridList() {
		List<FilterCriteria> criterialList = new ArrayList<FilterCriteria>();
		final List<ProposalStatus> statusList = new ArrayList<ProposalStatus>(5);
		statusList.add(ProposalStatus.INPROGRESS);
		statusList.add(ProposalStatus.REVIEW);
		statusList.add(ProposalStatus.UNASSIGNED);

		final InFilterCriteria<ProposalStatus> criteria = new InFilterCriteria<ProposalStatus>();
		criteria.setSearchField("proposalStatus");
		criteria.setSearchOper(SearchOption.EQUAL.name());
		criteria.setSearchvalues(statusList);
		criterialList.add(criteria);
		List<Proposal> proposalList = dashboardService.getProposalList(criterialList, new PaginationCriteria(1, 10), new SortingCriteria("proposalName", ""));
		int totalCount = dashboardService.getProposalCount(null);
		Assert.assertTrue(totalCount >= proposalList.size());
	}

	/**
	 * Return a list of Proposal based on filter criteria.
	 */
	@Test
	public void testGetProposalDashboardData() {
		List<Proposal> proposalLst = dashboardService.getDashboardProposal(null);
		if (proposalLst != null && !proposalLst.isEmpty()) {
			Assert.assertTrue(proposalLst.size() >= 0);
		}
	}
	
	@Test
	public void testSaveDashboardFilter(){
		UserFilter filter = dashboardService.saveDashboardFilter(getUserFilter());
		Assert.assertTrue(filter.getFilterId() > 0);
	}
	
	@Test
	public void testGetUserSaveFilterList(){
		UserFilter filter = dashboardService.saveDashboardFilter(getUserFilter());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertFalse(dashboardService.getUserSaveFilterList(filter.getUserId()).isEmpty());
	}
	
	@Test
	(expected = ObjectNotFoundException.class)
	public void testDeleteDashboardFilter(){
		UserFilter filter = dashboardService.saveDashboardFilter(getUserFilter());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		dashboardService.deleteDashboardFilter(filter.getFilterId());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		dashboardService.getDashboardFilterById(filter.getFilterId());
	}
	
	@Test
	public void testGetDashboardFilterById(){
		UserFilter filter = dashboardService.saveDashboardFilter(getUserFilter());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertNotNull(dashboardService.getDashboardFilterById(filter.getFilterId()));
	}
	
	@Test	
	public void testGetProposalAssignToList(){
		Assert.assertTrue(SecurityUtil.getProposalAssignToList().length > 0);
	}
	
	@Test	
	public void testGetProposalReviewers(){
		Assert.assertTrue(SecurityUtil.getProposalReviewers().length > 0);
	}
	
	@Test	
	public void testIsUserProposalPlanner(){
		FilterCriteria filterCriteria = new FilterCriteria("userRolesAsString", "Planner", SearchOption.CONTAIN.toString());
		List<User> userLst = userService.getFilteredUserList(filterCriteria, new PaginationCriteria(1, 2), null, false);
		if(!userLst.isEmpty()){
			Assert.assertTrue(SecurityUtil.isUserProposalPlanner(userLst.get(0)));
		}
	}
	
	
	private UserFilter getUserFilter(){
		List<User> userList = userService.getFilteredUserList(null, new PaginationCriteria(1, 2), null, false);
		UserFilter filter = new UserFilter();
		filter.setFilterId(0);
		filter.setFilterName("SalesCategories_Filter");
		filter.setUserId(userList.get(0).getUserId());
		filter.setFilterType(UserFilterTypeEnum.DASHBOARD.name());
		filter.setFilterData(getFilterData());
		return filter;
	}

	private String getFilterData() {
		return "{\"mySalesCategory\":false,\"priority\":null,\"salesCategory\":[\"1748\",\"1544\"],\"user\":[\"131\"],\"dueDate\":\"LAST_WEEK\"}";
	}
}
