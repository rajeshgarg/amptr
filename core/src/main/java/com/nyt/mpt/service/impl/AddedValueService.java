package com.nyt.mpt.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IAddedValueDao;
import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.service.IAddedValueService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * Service class for Added Value Rule 
 * @author megha.vyas
 *
 */
public class AddedValueService implements IAddedValueService {
	
	private IAddedValueDao addedValueDao;
	
	private static final Logger logger = Logger.getLogger(AddedValueService.class);
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#getFilteredPlanBudgetCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredPlanBudgetCount(FilterCriteria filterCriteria) {
		if (logger.isDebugEnabled()) {
			logger.debug("get count of filtered list of AddedValueBudget objects");
		}
		return addedValueDao.getFilteredPlanBudgetCount(filterCriteria);
	}

	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#getFilteredPlanBudgetList(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<AddedValueBudget> getFilteredPlanBudgetList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (logger.isDebugEnabled()) {
			logger.debug("getting filtered list of AddedValueBudget objects");
		}
		return addedValueDao.getFilteredPlanBudgetList(filterCriteria, pageCriteria, sortCriteria);
	}
	
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#createPlanBudget(com.nyt.mpt.domain.AddedValueBudget)
	 */
	@Override
	public AddedValueBudget createPlanBudget(AddedValueBudget addedValueBudget){
		if (logger.isDebugEnabled()) {
			logger.debug("Saving a new AddedValueBudget Object");
		}
		return addedValueDao.saveAddedValueBudget(addedValueBudget);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#updatePlanBudget(com.nyt.mpt.domain.AddedValueBudget, boolean)
	 */
	@Override 
	public AddedValueBudget updatePlanBudget(AddedValueBudget addedValueBudget, boolean forceUpdate){
		if (logger.isDebugEnabled()) {
			logger.debug("Updating a AddedValueBudget Object with id:" + addedValueBudget.getId());
		}
		AddedValueBudget addedValueBudgetDb = getAddedValueBudget(addedValueBudget.getId());
		addedValueBudgetDb.setTotalInvestment(addedValueBudget.getTotalInvestment());
		addedValueBudgetDb.setAvPercentage(addedValueBudget.getAvPercentage());
		addedValueBudgetDb.setNotes(addedValueBudget.getNotes());
		return addedValueDao.saveAddedValueBudget(addedValueBudgetDb);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#deleteAddedValuePlanBudget(long)
	 */
	@Override
	public void deleteAddedValuePlanBudget(long planBudgetId){
		if(logger.isDebugEnabled()){
			logger.debug("Deleting an AdddValuePlanBudget with id:"+planBudgetId);
		}
		AddedValueBudget addedValueBudgetDb = getAddedValueBudget(planBudgetId);
		addedValueDao.deleteAddedValueBudget(addedValueBudgetDb);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#isDuplicateInvestment(double, java.lang.Long)
	 */
	@Override
	public boolean isDuplicateInvestment(double investment, Long id){
		if(logger.isDebugEnabled()){
			logger.debug("checking for duplicacy of investment value:"+ investment +"while saving or updating AddedValueBudget object");
		}
		return addedValueDao.isDuplicateInvestment(investment,id);
	}
	
	/**
	 * get AddedValueBudget object for an id passed as an argument 
	 * @param addedValueBudgetid
	 * @return
	 */
	public AddedValueBudget getAddedValueBudget(long addedValueBudgetid){
		return addedValueDao.getAddedValueBudget(addedValueBudgetid);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IAddedValueService#getAvPercentValidForInvestment(double, double, long)
	 */
	@Override
	public boolean getAvPercentValidForInvestment(double investment, double avPercentage, long id) {
		boolean returnflag = false;
		List<AddedValueBudget> avPercentageLSt = addedValueDao.getNearByAvDiscountPercent(investment,id);
		switch (avPercentageLSt.size()) {
		case 1:
			if (investment > avPercentageLSt.get(0).getTotalInvestment() && avPercentage > avPercentageLSt.get(0).getAvPercentage()
					|| investment < avPercentageLSt.get(0).getTotalInvestment() && avPercentage < avPercentageLSt.get(0).getAvPercentage()) {
				returnflag = true;
			}
			break;
		case 2:
			if (avPercentage < avPercentageLSt.get(0).getAvPercentage() && avPercentage > avPercentageLSt.get(1).getAvPercentage()) {
				returnflag = true;
			}
			break;
		default:
			returnflag = true;
			break;
		}
		return returnflag;
	}
	
	public void setAddedValueDao(IAddedValueDao addedValueDao) {
		this.addedValueDao = addedValueDao;
	}
}
