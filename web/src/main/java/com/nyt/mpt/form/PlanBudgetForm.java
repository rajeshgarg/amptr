package com.nyt.mpt.form;

import java.io.Serializable;

import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.util.NumberUtil;

/**
 * This <code>PlanBudgetForm</code> has all the attribute related to Plan Budget
 * 
 * @author gurditta.garg
 */
public class PlanBudgetForm extends BaseForm<AddedValueBudget> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long planBudgetId;
	private String totalInvestment;
	private String avPercentage;
	private String avNotes;

	public long getPlanBudgetId() {
		return planBudgetId;
	}

	public void setPlanBudgetId(long planBudgetId) {
		this.planBudgetId = planBudgetId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(final String totalInvestment) {
		this.totalInvestment = totalInvestment == null ? totalInvestment : totalInvestment.trim();
	}

	public String getAvPercentage() {
		return avPercentage;
	}

	public void setAvPercentage(final String avPercentage) {
		this.avPercentage = avPercentage;
	}

	public String getAvNotes() {
		return avNotes;
	}

	public void setAvNotes(final String avNotes) {
		this.avNotes = avNotes;
	}

	public boolean isActive() {
		return super.isActive();
	}

	public void setActive(final boolean active) {
		super.setActive(active);
	}

	@Override
	public void populateForm(final AddedValueBudget bean) {
		this.setPlanBudgetId(bean.getId());
		this.setTotalInvestment(NumberUtil.formatDouble(bean.getTotalInvestment(), true));
		this.setAvPercentage(NumberUtil.formatDouble(bean.getAvPercentage(), true));
		this.setAvNotes(bean.getNotes());
	}

	@Override
	public AddedValueBudget populate(final AddedValueBudget bean) {
		bean.setId(this.getPlanBudgetId());
		bean.setTotalInvestment(NumberUtil.doubleValue(this.getTotalInvestment()));
		bean.setAvPercentage(NumberUtil.doubleValue(this.getAvPercentage()));
		bean.setNotes(this.getAvNotes());
		return bean;
	}
}
