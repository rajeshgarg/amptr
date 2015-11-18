/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.List;

import org.hibernate.Hibernate;

import com.nyt.mpt.dao.IDashboardDAO;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.service.IDashboardService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>DashboardService</code> includes all the implementations for the methods including functionalities like : fetching Proposal's data for both graph and grid in the dash-board view, all CRUD operations for 
 * {@link UserFilter} in the dash-board screen
 * 
 * @author surendra.singh
 */

public class DashboardService implements IDashboardService {
	
	private IDashboardDAO dashboardDao;
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDashboardService#saveDashboardFilter(com.nyt.mpt.domain.DashboardFilter)
	 */
	@Override
	public UserFilter saveDashboardFilter(final UserFilter filter) {
		return dashboardDao.saveDashboardFilter(filter);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDashboardService#deleteDashboardFilter(long)
	 */
	@Override
	public void deleteDashboardFilter(final long filterId) {
		dashboardDao.deleteDashboardFilter(filterId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDashboardService#getUserSaveFilterList(long)
	 */
	@Override
	public List<UserFilter> getUserSaveFilterList(long userId) {
		return dashboardDao.getUserDashboardFilterList(userId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IDashboardService#getDashboardFilterById(long)
	 */
	@Override
	public UserFilter getDashboardFilterById(final long filterId) {
		UserFilter filter = dashboardDao.getDashboardFilterById(filterId);
		Hibernate.initialize(filter);
		return filter;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalDashboardGridList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<Proposal> getProposalList(final List<FilterCriteria> filterList, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		final List<Proposal> proposalList = dashboardDao.getProposalList(filterList, pgCriteria, sortCriteria);
		for (Proposal proposal : proposalList) {
			Hibernate.initialize(proposal.getAssignedByUser());
			Hibernate.initialize(proposal.getProposalOptions());
			for (ProposalOption option : proposal.getProposalOptions()) {
				Hibernate.initialize(option.getProposalVersions());
			}
		}
		return proposalList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalDashboardGridListCount(java.util.List)
	 */
	@Override
	public int getProposalCount(final List<FilterCriteria> filterList) {
		return dashboardDao.getProposalCount(filterList);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProposalService#getProposalDashboardData(java.util.List)
	 */
	@Override
	public List<Proposal> getDashboardProposal(final List<FilterCriteria> filterCriteriaList) {
		List<Proposal> proposalList = dashboardDao.getProposalDashboardData(filterCriteriaList);
		return proposalList;
	}
	
	public void setDashboardDao(IDashboardDAO dashboardDAO) {
		this.dashboardDao = dashboardDAO;
	}
}
