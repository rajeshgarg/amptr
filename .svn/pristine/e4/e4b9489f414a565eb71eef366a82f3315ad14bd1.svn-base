/**
 * 
 */
package com.nyt.mpt.util.reservation;

import java.util.Date;
import java.util.Set;

import com.nyt.mpt.domain.LineItemTarget;

/**
 * This <code>ReservationTO</code> class is used to transfer data between
 * various application layers
 * 
 * @author surendra.singh
 */
public class ReservationTO {
	private long proposalId;
	private long lineItemId;
	private long productId;
	private long salesTargetId;
	private Date startDate;
	private Date endDate;
	private boolean calculateSOR;
	private String productType;
	private Set<LineItemTarget> lineItemTargeting;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getProposalId() {
		return proposalId;
	}

	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public Set<LineItemTarget> getLineItemTargeting() {
		return lineItemTargeting;
	}

	public void setLineItemTargeting(Set<LineItemTarget> lineItemTargeting) {
		this.lineItemTargeting = lineItemTargeting;
	}

	public boolean isCalculateSOR() {
		return calculateSOR;
	}

	public void setCalculateSOR(boolean calculateSOR) {
		this.calculateSOR = calculateSOR;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	@Override
	public String toString() {
		return "CalendarVO [startDate=" + this.startDate + ", endDate=" + this.endDate + ", proposalId=" + this.proposalId + ", lineItemId=" 
										+ this.lineItemId + ", productId=" + this.productId + ", salesTargetId=" + this.salesTargetId
										+ ", productType=" + this.productType + "]";
	}
}
