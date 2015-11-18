/**
 * 
 */
package com.nyt.mpt.form;

import com.nyt.mpt.domain.SalesTarget;

/**
 * This form bean is used for Sales Target information
 * 
 * @author amandeep.singh
 * 
 */
public class SalesTargetForm extends BaseForm<SalesTarget> {
	private long salesTargetId;

	private String salesTargetName;

	private String salesTargeDisplayName;

	private String status;

	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(final long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(final String salesTargetName) {
		this.salesTargetName = salesTargetName != null ? salesTargetName.trim() : salesTargetName;
	}

	public String getSalesTargeDisplayName() {
		return salesTargeDisplayName;
	}

	public void setSalesTargeDisplayName(final String salesTargeDisplayName) {
		this.salesTargeDisplayName = salesTargeDisplayName != null ? salesTargeDisplayName.trim() : salesTargeDisplayName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Override
	public SalesTarget populate(final SalesTarget bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populateForm(final SalesTarget bean) {
		this.setSalesTargeDisplayName(bean.getSalesTargeDisplayName());
		this.setSalesTargetId(bean.getSalesTargetId());
		this.setSalesTargetName(bean.getSalesTargetName());
		this.setStatus(bean.getStatus());
	}
}
