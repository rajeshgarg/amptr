/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>IDashboardDAO</code> includes all the declarations for the methods including functionalities like : fetching Proposal's data for both graph and grid in the dash-board view, all CRUD operations for 
 * {@link UserFilter} in the dash-board screen.
 * 
 * @author surendra.singh
 */

public interface IDashboardDAO {

	/**
	 * Returns Proposal's data to be displayed on the graph in the dash-board view
	 * @param 	filterCriteriaList
	 * 			List {@link FilterCriteria} for which we need Proposal's data to be displayed on the graph
	 * @return
	 * 			Returns Proposal's data to be displayed on the graph
	 */
	List<Proposal> getProposalDashboardData(final List<FilterCriteria> filterCriteriaList);
	
	/**
	 * Return List of {@link Proposal} for DashBoard's grid and it's data based on filter criteria, proposal with latest version will be fetched
	 * @param 	filterCriteriaLst
	 * 			List of {@link FilterCriteria} for which we need Proposal's data
	 * @param 	pgCriteria
	 * 			{@link PaginationCriteria}
	 * @param 	sortCriteria
	 * 			{@link SortingCriteria}
	 * @return
	 * 			Return proposal DashBoard Grid Data
	 */
	List<Proposal> getProposalList(final List<FilterCriteria> filterCriteriaLst, final PaginationCriteria pgCriteria,
			final SortingCriteria sortCriteria);
	
	/**
	 * Return a Proposal DashboardGrid List count based on filter criteria
	 * @param 	filterCriteriaLst
	 * 			List of {@link FilterCriteria} for which we need the count for the Proposal's grid in the dash-board view
	 * @return
	 * 			Returns the Proposals count integer value to be used in the grid
	 */
	int getProposalCount(final List<FilterCriteria> filterCriteriaLst);
	
	/**
	 * Saves {@link UserFilter} in the AMPT database
	 * @param 	filter
	 * 			{@link UserFilter} to be saved in the database requested by the user
	 * @return
	 * 			Returns saved {@link UserFilter}
	 */
	UserFilter saveDashboardFilter(final UserFilter filter);
	
	/**
	 * Returns the List of {@link UserFilter} from the AMPT database corresponding to current Logged in user
	 * @param 	userId
	 * 			Current LoggedIn user's Id
	 * @return
	 * 			Returns the List of {@link UserFilter} from the AMPT database
	 */
	List<UserFilter> getUserDashboardFilterList(long userId);

	/**
	 * Deletes the {@link UserFilter} from the AMPT database
	 * @param 	filterId
	 * 			{@link UserFilter}'s Id to be deleted requested by the user
	 */
	void deleteDashboardFilter(long filterId);

	/**
	 * Returns {@link UserFilter} from the AMPT database corresponding to the requested filter
	 * @param 	filterId
	 * 			{@link UserFilter}'s Id to be fetched from the AMPT database
	 * @return
	 * 		Returns {@link UserFilter} from the AMPT database	
	 */
	UserFilter getDashboardFilterById(long filterId);
}
