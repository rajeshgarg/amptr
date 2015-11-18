/**
 * 
 */
package com.nyt.mpt.util.reservation;

import java.util.Date;

/**
 * @author amandeep.singh
 *
 */
public class SalesReservationCalendarDetailVO implements Comparable<SalesReservationCalendarDetailVO> {
	private Double sor;
	private Long lineItemId;
	private Long proposalId;
	private Long salesOrderId;
	private String proposalName;
	private String campaignName;
	private String accountManager;
	private String advertiserName;
	private Date expirationDate;
	private String status;
	private String productName;
	private String salesCategoryName;
	private Long daysToExpire;
	private Long daysLeftInFlight;
	
	public Double getSor() {
		return sor;
	}
	public void setSor(Double sor) {
		this.sor = sor;
	}
	public Long getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(Long lineItemId) {
		this.lineItemId = lineItemId;
	}
	public Long getProposalId() {
		return proposalId;
	}
	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}
	public Long getSalesOrderId() {
		return salesOrderId;
	}
	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSalesCategoryName() {
		return salesCategoryName;
	}
	public void setSalesCategoryName(String salesCategoryName) {
		this.salesCategoryName = salesCategoryName;
	}
	public Long getDaysToExpire() {
		return daysToExpire;
	}
	public void setDaysToExpire(Long daysToExpire) {
		this.daysToExpire = daysToExpire;
	}
	public Long getDaysLeftInFlight() {
		return daysLeftInFlight;
	}
	public void setDaysLeftInFlight(Long daysLeftInFlight) {
		this.daysLeftInFlight = daysLeftInFlight;
	}
	@Override
	public int compareTo(SalesReservationCalendarDetailVO obj) {
		if (obj == null) {
			return -1;
		} else {
			return this.getExpirationDate().compareTo(obj.getExpirationDate());
		}
	}
}
