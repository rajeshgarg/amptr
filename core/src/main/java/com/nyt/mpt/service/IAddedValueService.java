package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

public interface IAddedValueService {
	
	/**
	 * get length of filtered list of AddedValueBudget 
	 * @param filterCriteria
	 * @return
	 */
	int getFilteredPlanBudgetCount(final FilterCriteria filterCriteria);
	
	/**
	 * get filtered list of AddedValueBudget
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return
	 */
	List<AddedValueBudget>  getFilteredPlanBudgetList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria);
	
	/**
	 * save an AddedValueBudget object
	 * @param addedValueBudget
	 * @return
	 */
	AddedValueBudget createPlanBudget(AddedValueBudget addedValueBudget);
	
	/**
	 * update an AddedValueBudget object
	 * @param addedValueBudget
	 * @param forceUpdate
	 * @return
	 */
	AddedValueBudget updatePlanBudget(AddedValueBudget addedValueBudget, boolean forceUpdate); 
	
	/**
	 *delete an AddedValueBudget object
	 * @param planBudgetId
	 */
	void deleteAddedValuePlanBudget(long planBudgetId);
	
	/**
	 * check if totalInvestment value in AddedValueBudget database is duplicate
	 * @param investment
	 * @param id
	 * @return
	 */
	boolean isDuplicateInvestment(double investment, Long id);
	
	/**
	 * checks whether % entered is valid for given investment
	 * @param investment
	 * @param avPercentage
	 * @param id
	 * @return
	 */
	boolean getAvPercentValidForInvestment(double investment, double avPercentage, long id);
}
