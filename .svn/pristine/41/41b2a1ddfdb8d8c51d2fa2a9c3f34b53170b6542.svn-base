/**
 * 
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.form.PlanBudgetForm;
import com.nyt.mpt.service.impl.AddedValueService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.PlanBudgetValidator;

/**
 * This <code>ManageAddedValueController</code> class contains all the methods related to AddedValue screen
 * 
 * @author gurditta.garg
 */
@Controller
@RequestMapping("/manageAddedValue/*")
public class ManageAddedValueController extends AbstractBaseController {

	private static final String TOTAL_INVESTMENT = "totalInvestment";
	private static final String PLAN_BUDGET_FORM = "planBudgetForm";
	private static final String GRID_DATA = "planBudgetGridData";
	private AddedValueService addedValueService;
	
	/**
	 * Returns the ModelAndView for the AddedValue screen for the first time it loads
	 * @param planBudgetForm
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute(PLAN_BUDGET_FORM) final PlanBudgetForm planBudgetForm) {
		final ModelAndView view = new ModelAndView("addedValueRule");
		view.addObject(PLAN_BUDGET_FORM, planBudgetForm);
		return view;
	}

	/**
	 * Loads the Added Value Budget data in the grid
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadPlanBudgetGridData(@ModelAttribute final TableGrid<PlanBudgetForm> tblGrid) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setplanBudgetDataToGrid(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Sets the Added Value Budget list data to grid
	 * @param tblGrid
	 */
	private void setplanBudgetDataToGrid(final TableGrid<PlanBudgetForm> tblGrid) {
		final List<AddedValueBudget> planBudgetLst = addedValueService.getFilteredPlanBudgetList(tblGrid.getFilterCriteria(), 
				tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!planBudgetLst.isEmpty()) {
			final int count = addedValueService.getFilteredPlanBudgetCount(tblGrid.getFilterCriteria());
			tblGrid.setGridData(convertPlanBudgetDtoToForm(planBudgetLst), count);
		}
	}

	/**
	 * Converts List of {@link AddedValueBudget} to List of {@link PlanBudgetForm}
	 * @param planBudgetLst
	 * @return
	 */
	private List<PlanBudgetForm> convertPlanBudgetDtoToForm(final List<AddedValueBudget> planBudgetLst) {
		final List<PlanBudgetForm> planBudgetformLst = new ArrayList<PlanBudgetForm>();
		for (AddedValueBudget avbudget : planBudgetLst) {
			final PlanBudgetForm planBudgetForm = new PlanBudgetForm();
			planBudgetForm.populateForm(avbudget);
			planBudgetformLst.add(planBudgetForm);
		}
		return planBudgetformLst;
	}

	/**
	 * Saves/Updates {@link AddedValueBudget} object
	 * @param response
	 * @param planBudgetForm
	 * @return
	 */
	@RequestMapping("/saveAddedValuePlanBudget")
	@ResponseBody
	public AjaxFormSubmitResponse saveAddedValuePlanBudget(final HttpServletResponse response, @ModelAttribute(PLAN_BUDGET_FORM) final PlanBudgetForm planBudgetForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(PLAN_BUDGET_FORM, planBudgetForm);
		new PlanBudgetValidator().validate(planBudgetForm, results);
		try {
			boolean flag = addedValueService.isDuplicateInvestment(NumberUtil.doubleValue(planBudgetForm.getTotalInvestment()), planBudgetForm.getPlanBudgetId());
			if (flag) {
				results.rejectValue(TOTAL_INVESTMENT, ErrorCodes.DuplicateInvestmentHelp, TOTAL_INVESTMENT, new Object[] { "Total Investment" }, UserHelpCodes.DuplicateInvestmentHelp);
			} else {
				if (!(NumberUtil.doubleValue(planBudgetForm.getAvPercentage()) <= 0.00 || NumberUtil.doubleValue(planBudgetForm.getAvPercentage()) >= 100.00)) {
					if (!addedValueService.getAvPercentValidForInvestment(NumberUtil.doubleValue(planBudgetForm.getTotalInvestment()), 
							NumberUtil.doubleValue(planBudgetForm.getAvPercentage()), planBudgetForm.getPlanBudgetId())) {
						results.rejectValue("avPercentage", ErrorCodes.InvalidAvPercentageRange, "avPercentage", new Object[] { "Percentage" }, UserHelpCodes.AvPercentageHelp);
					}
				}
			}
		} catch (NumberFormatException e) {
			// Only numeric digits allowed
			results.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigit, TOTAL_INVESTMENT, new Object[] { "Total Investment" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			AddedValueBudget bean = planBudgetForm.populate(new AddedValueBudget());
			if (bean.getId() == 0) {
				bean = addedValueService.createPlanBudget(bean);
			} else {
				bean = addedValueService.updatePlanBudget(bean, planBudgetForm.isForceUpdate());
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, bean.getId());
		}
		return ajaxResponse;
	}

	/**
	 * Deletes an AddedValueBudget object by <code>planBudgetId</code>
	 * @param planBudgetId
	 */
	@RequestMapping("/deleteAddedValuePlanBudget")
	@ResponseBody
	public void deleteAddedValuePlanBudget(@RequestParam final long planBudgetId) {
		addedValueService.deleteAddedValuePlanBudget(planBudgetId);
	}

	public void setAddedValueService(final AddedValueService addedValueService) {
		this.addedValueService = addedValueService;
	}
}