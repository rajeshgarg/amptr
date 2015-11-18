/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;

/**
 * This seasonal discount form bean is used for the List rate profile discount details.
 * @author garima.garg
 */
@SuppressWarnings("serial")
public class RateProfileDiscountsForm extends BaseForm<RateProfileSeasonalDiscounts> implements Serializable {

	private long discountId;

	private String startDate;

	private String endDate;

	private String discount;

	private boolean not;

	private String notChecked;

	private int rowIndex;

	private String discountSeqNo;

	public long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(final long discountId) {
		this.discountId = discountId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(final String endDate) {
		this.endDate = endDate;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(final String discount) {
		this.discount = discount;
	}

	public boolean getNot() {
		return not;
	}

	public void setNot(final boolean not) {
		this.not = not;
	}

	public String getNotChecked() {
		return notChecked;
	}

	public void setNotChecked(final String notChecked) {
		this.notChecked = notChecked;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(final int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getDiscountSeqNo() {
		return discountSeqNo;
	}

	public void setDiscountSeqNo(final String discountSeqNo) {
		this.discountSeqNo = discountSeqNo;
	}

	@Override
	public void populateForm(final RateProfileSeasonalDiscounts bean) {
		this.setDiscountId(bean.getDiscountId());
		this.setStartDate(DateUtil.getGuiDateString(bean.getStartDate()));
		this.setEndDate(DateUtil.getGuiDateString(bean.getEndDate()));
		this.setDiscount(String.valueOf(bean.getDiscount()));
		this.setNot(bean.isNot());
		if (bean.isNot()) {
			this.setNotChecked(ConstantStrings.YES);
		} else {
			this.setNotChecked(ConstantStrings.NO);
		}
	}

	@Override
	public RateProfileSeasonalDiscounts populate(final RateProfileSeasonalDiscounts bean) {
		bean.setDiscountId(this.getDiscountId());
		bean.setDiscount(Double.valueOf(this.getDiscount()));
		bean.setStartDate(DateUtil.parseToDate(this.getStartDate()));
		bean.setEndDate(DateUtil.parseToDate(this.getEndDate()));
		bean.setNot(this.getNot());
		return bean;
	}
}
