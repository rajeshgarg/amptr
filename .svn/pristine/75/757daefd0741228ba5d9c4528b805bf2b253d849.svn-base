/**
 * 
 */
package com.nyt.mpt.service;

import java.util.Set;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.util.enums.PricingStatus;

/**
 * @author garima.garg
 *
 */
public interface IPricingStatusCalculatorService {

	/**
	 * Calculates the pricing status of the given line item.
	 * @param lineitem
	 * @param isSpecialAdvertiser
	 * @return
	 */
	PricingStatus getPricingStatus (LineItem lineitem, boolean isSpecialAdvertiser);
	
	/**
	 * Returns the Total Investment of qualifying Line Items
	 * @param lineitems
	 * @return
	 */
	Double getInvstmntOfQulfyngLI(Set<LineItem> lineitems, boolean isThresholdCheck);
	
	/**
	 * Executes the Threshold Check
	 * @param ProposalId
	 */
	void addThreshHoldCheck(Long ProposalId);
	
	/**
	 * checks the status of Added Value LI on change in proposal status
	 * @param ProposalId
	 */
	void addAddedValueCheck(Long ProposalId);
	
	/**
	 * Mark all Unapproved Line Item as Pricing Approved
	 * @param proposalId
	 * @return
	 */
	Long updatePricingStatusAsPricingApproved(Long proposalId, Long[] optionIds);

	/**
	 * Executes the Threshold Check for given option 
	 * @param option
	 */
	void addThreshHoldCheckForOption(final ProposalOption option);
	
	/**
	 * Checks the status of Added Value LI for given option
	 * @param option
	 */
	void addAddedValueCheckForOption(ProposalOption option);
	
	/**
	 * to get possible offered impressions which can be given for a LineItem
	 * @param rateCardPrice
	 * @param optionId
	 * @param lineItemId
	 * @return
	 */
	double getOffImpressions(double  rateCardPrice , Long optionId , Long lineItemId);
	
	/**
	 * Returns the value of the THRESHOLD Percent being configured.
	 * @return
	 */
	String getTHRESHOLD_PERCENT();
}
