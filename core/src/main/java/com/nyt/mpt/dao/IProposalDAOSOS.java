/**
 *
 */
package com.nyt.mpt.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;

/**
 * This interface declare Proposal SOS related methods
 *
 * @author amandeep.singh
 *
 */
public interface IProposalDAOSOS {

	/**
	 * Returns the map of Agencies from SOS
	 *
	 * @return List<Agency>
	 */
	List<Agency> getAgency();

	/**
	 * Agency based on agencyId
	 *
	 * @param agencyId
	 * @return Agency
	 */
	Agency getAgencyById(long agencyId);

	/**
	 * Returns the map of Advertisers from SOS
	 *
	 * @return List<Advertiser>
	 */
	List<Advertiser> getAdvertiser();

	/**
	 * Return Advisor based on advertiser Id
	 *
	 * @return List of Advisor, irrespective of Status
	 */
	Advertiser getAdvertiserById(long advertiserId);

	/**
	 * Method to check whether product/sales target combination is active
	 *
	 * @param productId
	 * @param salesTargetID
	 * @return boolean
	 */
	boolean isProductPlacementActive(long productId, Long[] salesTargetID);
	
	/**
	 * Returns all currencies as Map with the currency Id as key and the Currency Name as value
	 * @return
	 */
	public Map<Long, String> getCurrencies();
	
	/**
	 * Returns the conversion Id for the given currency. 
	 * @param currencyId
	 * @return
	 */
	long getCurrencyConversionId(final long currencyId );
	
	/**
	 * Returns the period Id based on the given start and end dates.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	long getConversionPeriodId(Date startDate , Date endDate);
	
	/**
	 * Returns the conversion Rate for a specific currency based on its conversion and period id.
	 * @param CurrencyConversionId
	 * @param periodId
	 * @return
	 */
	double getCurrencyConversionRate(final long CurrencyConversionId , final long  periodId);

	/**
	 * Fetch all the advertisers based on name
	 * @param advertiserName
	 * @return
	 */
	List<Advertiser> getAdvertiserByName(String advertiserName);

	/**
	 * Fetch all the agency based on name
	 * @param agencyName
	 * @return
	 */
	List<Agency> getAgencyByName(String agencyName);
}
