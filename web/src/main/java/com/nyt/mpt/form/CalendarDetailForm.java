/**
 *
 */
package com.nyt.mpt.form;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import com.nyt.mpt.util.CalendarLineItemComparator;

/**
 * @author surendra.singh
 * 
 */
public class CalendarDetailForm {

	private long salesOrderId;

	private long proposalId;

	private String campaignName;

	private String advertiserName;

	private String accountManager;

	private Set<CalendarLineItemForm> lineItems;

	private String sosURL;

	public long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(final long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(final String campaignName) {
		this.campaignName = campaignName;
	}

	public Set<CalendarLineItemForm> getLineItems() {
		return lineItems;
	}

	public void setLineItems(final Set<CalendarLineItemForm> lineItems) {
		this.lineItems = lineItems;
	}

	public void addLineItem(final CalendarLineItemForm lineItemForm) {
		if (lineItems == null) {
			lineItems = new TreeSet<CalendarLineItemForm>(Collections.reverseOrder(new CalendarLineItemComparator()));
		}
		lineItems.add(lineItemForm);
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(final String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public long getProposalId() {
		return proposalId;
	}

	public void setProposalId(final long proposalId) {
		this.proposalId = proposalId;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(final String accountManager) {
		this.accountManager = accountManager;
	}

	public String getSosURL() {
		return sosURL;
	}

	public void setSosURL(final String sosURL) {
		this.sosURL = sosURL;
	}
}
