/**
 *
 */
package com.nyt.mpt.form;

import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;

/**
 * @author rakesh.tewari
 * 
 */
public class SalesTargetAmptForm extends BaseForm<SalesTargetAmpt> {

	private String salesTargetId;

	private String salesTargetName;

	private String salesTargeDisplayName;

	private String weight;

	private String capacity;

	private String modifiedDate;

	@Override
	public SalesTargetAmpt populate(final SalesTargetAmpt bean) {
		return bean;
	}

	@Override
	public void populateForm(final SalesTargetAmpt bean) {
		this.setSalesTargetId(String.valueOf(bean.getSalesTargetId()));
		this.setCapacity((bean.getCapacity() == null) ? ConstantStrings.NA : NumberUtil.formatLong(bean.getCapacity(), true));
		this.setSalesTargeDisplayName(String.valueOf(bean.getSalesTargeDisplayName()));
		this.setWeight((bean.getWeight() == null) ? ConstantStrings.NA : NumberUtil.formatDouble(bean.getWeight(), true));
		this.setModifiedDate((bean.getModifiedDate() == null) ? ConstantStrings.EMPTY_STRING : DateUtil.getGuiDateString(bean.getModifiedDate()));
	}

	/**
	 * @return the salesTargetId
	 */
	public String getSalesTargetId() {
		return salesTargetId;
	}

	/**
	 * @param salesTargetId the salesTargetId to set
	 */
	public void setSalesTargetId(final String salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	/**
	 * @return the salesTargetName
	 */
	public String getSalesTargetName() {
		return salesTargetName;
	}

	/**
	 * @param salesTargetName the salesTargetName to set
	 */
	public void setSalesTargetName(final String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}

	/**
	 * @return the salesTargeDisplayName
	 */
	public String getSalesTargeDisplayName() {
		return salesTargeDisplayName;
	}

	/**
	 * @param salesTargeDisplayName the salesTargeDisplayName to set
	 */
	public void setSalesTargeDisplayName(final String salesTargeDisplayName) {
		this.salesTargeDisplayName = salesTargeDisplayName;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(final String weight) {
		this.weight = weight;
	}

	/**
	 * @return the capacity
	 */
	public String getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(final String capacity) {
		this.capacity = capacity;
	}

	public void setModifiedDate(final String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}
}
