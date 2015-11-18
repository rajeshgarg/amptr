/**
 * 
 */
package com.nyt.mpt.domain;

/**
 * @author amandeep.singh
 *
 */
public class SosIntegrationPojo {
	
	private String proposalId;
	private String advertiserId;
	private String advertiser;
	private String billingAddress;
	private String[] billingContact;
	private String billingMethod;
	private String paymentMethod;
	private String agencyname;
	private String agencyId;
	private String billto;
	
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getBillto() {
		return billto;
	}

	public void setBillto(String billto) {
		this.billto = billto;
	}

	public String getAgencyname() {
		return agencyname;
	}

	public void setAgencyname(String agencyname) {
		this.agencyname = agencyname;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String[] getBillingContact() {
		return billingContact;
	}

	public void setBillingContact(String[] billingContact) {
		this.billingContact = billingContact;
	}

	public String getBillingMethod() {
		return billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
