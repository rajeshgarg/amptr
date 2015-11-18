/**
 * 
 */
package com.nyt.mpt.util.reservation;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * This <code>ReservationListViewDetailVO</code> class represent detail of a
 * particular day in calendar in a list view
 * 
 * @author manish.kesarwani
 */
public class ReservationListViewDetailVO implements Comparable<ReservationListViewDetailVO> {

	private Double sor;
	private Long lineItemId;
	private Long proposalId;
	private Long salesOrderId;
	private String proposalName;
	private String campaignName;
	private String accountManager;
	private String advertiserName;
	private String expirationDate;
	private String vulnerable;

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public String getProposalName() {
		return proposalName;
	}

	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}

	public Double getSor() {
		return sor;
	}

	public void setSor(Double sor) {
		this.sor = sor;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public Long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(Long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}
	
	public String getVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(String vulnerable) {
		this.vulnerable = vulnerable;
	}

	@Override
	public int compareTo(ReservationListViewDetailVO o) {
		if (o == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(o.getProposalId(), this.getProposalId()).toComparison();
		}
	}
}
