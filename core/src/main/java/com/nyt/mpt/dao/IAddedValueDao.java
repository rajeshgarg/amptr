package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

public interface IAddedValueDao {

	/**
	 * get count of filtered list of AddedValueBudget objects
	 * @param filterCriteria
	 * @return
	 */
	int getFilteredPlanBudgetCount(FilterCriteria filterCriteria);
	
	/**
	 * get filtered list of AddedValueBudget objects
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return
	 */
	List<AddedValueBudget> getFilteredPlanBudgetList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria);
	
	/**
	 * save AddedValueBudget object in database
	 * @param addedValueBudget
	 * @return
	 */
	AddedValueBudget saveAddedValueBudget(AddedValueBudget addedValueBudget);

	/**
	 * get AddedValueBudget object for an id passed as an argument
	 * @param addedValueBudgetid
	 * @return
	 */
	AddedValueBudget getAddedValueBudget(long addedValueBudgetid); 
	
	/**
	 * delete the AddedValueBudget object from database passed as argument
	 * @param addedValueBudget
	 */
	void deleteAddedValueBudget(AddedValueBudget addedValueBudget);

	/**
	 * @param investment
	 * @param id
	 * @return
	 */
	boolean isDuplicateInvestment(double investment, Long id);

	/**
	 * Returns List of AddedValueBudget which is just higher and just lower than the investment passed
	 * @param investment
	 * @param id
	 * @return
	 */
	List<AddedValueBudget> getNearByAvDiscountPercent(Double investment, long id);

}
