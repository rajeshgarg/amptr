/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.reservation.ReservationTO;

/**
 * This <code>CalendarForm</code> has all the attributes related to Calendar Reservation to be shown on the UI
 * 
 * @author surendra.singh
 */

@SuppressWarnings("serial")
public class CalendarForm extends BaseForm<ReservationTO> implements Serializable {

	private long proposalId;
	private long lineItemId;
	private long productId;
	private long salesTargetId;

	private String endDate;
	private String startDate;
	private String targetingString;
	private String lineItemTargetingData;
	private String productType;
	
	private boolean calculateSOR = false;

	public long getProductId() {
		return productId;
	}

	public void setProductId(final long productId) {
		this.productId = productId;
	}

	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(final long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public String getTargetingString() {
		return targetingString;
	}

	public void setTargetingString(final String targetingString) {
		this.targetingString = targetingString;
	}

	public long getProposalId() {
		return proposalId;
	}

	public void setProposalId(final long proposalId) {
		this.proposalId = proposalId;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(final long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItemTargetingData() {
		return lineItemTargetingData;
	}

	public void setLineItemTargetingData(final String lineItemTargetingData) {
		this.lineItemTargetingData = lineItemTargetingData == null
				? ConstantStrings.EMPTY_STRING : lineItemTargetingData.trim().replaceAll("\"", "'");
	}

	public boolean isCalculateSOR() {
		return calculateSOR;
	}

	public void setCalculateSOR(final boolean calculateSOR) {
		this.calculateSOR = calculateSOR;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(final String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(final String startDate) {
		this.startDate = startDate;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public ReservationTO populate(final ReservationTO bean) {
		bean.setProposalId(this.proposalId);
		bean.setLineItemId(this.lineItemId);
		bean.setProductId(this.productId);
		bean.setSalesTargetId(this.salesTargetId);
		bean.setCalculateSOR(this.calculateSOR);
		bean.setProductType(this.getProductType());
		if (StringUtils.isNotBlank(this.lineItemTargetingData)) {
			bean.setLineItemTargeting(TargetJsonConverter.convertJsonToObject(this.lineItemTargetingData.replaceAll("'", "\""), null));
		}
		/**
		 * To set start and end date for calendar view only
		 */
		bean.setStartDate(DateUtil.parseToDate(this.startDate));
		bean.setEndDate(DateUtil.parseToDate(this.endDate));
		return bean;
	}

	@Override
	public void populateForm(ReservationTO bean) {

	}
}
