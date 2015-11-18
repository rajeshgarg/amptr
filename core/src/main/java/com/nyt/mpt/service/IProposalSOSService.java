/**
 * 
 */
package com.nyt.mpt.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.sos.ProductPlacement;
import com.nyt.mpt.template.ReferenceDataMap;

/**
 * @author amandeep.singh
 *
 */
public interface IProposalSOSService {
	
	/**
	 * Returns List of Agency from SOS
	 * @return List<Agency>
	 */
	List<Agency> getAgency();
	
	/**
	 * Return Agency based on agency Id
	 * @param agencyId
	 * @return Agency
	 */
	Agency getAgencyById(long agencyId);

	
	/**
	 * Returns a list of Advertisers from SOS
	 * @return List<Advertiser>
	 */
	List<Advertiser> getAdvertiser();

	
	/**
	 * Return Advisor based on advertiserId
	 * @param advertiserId
	 * @return Advertiser
	 */
	 Advertiser getAdvertiserById(long advertiserId);
	 
	 /**
	  * Method to check whether product/sales target combination  is active
	  * @param productId
	  * @param salesTargetID
	  * @return boolean
	  */
	boolean isProductPlacementActive(long productId, Long[] salesTargetID);
	
	/**
	  * Method to check whether product/sales target combination  is active in SOS
	  * @param productId
	  * @param salesTargetID
	  * @return boolean
	  */
	List<ProductPlacement> getActiveProductPlacement(long productId, Long[] salesTargetID);
	
	/**
	 *  Method to return reference data from SOS(Product,Sales Target)
	 * @param lineItemsList
	 * @return ReferenceDataMap
	 */
	ReferenceDataMap getReferenceDataMapFromLineItemList(List<LineItem> lineItemsList);
	
	/**
	 * Return ReferenceDataMap From LineItemAssocList based on ProposalLineItemAssoc List
	 * @param proposalLineItemLst
	 * @return ReferenceDataMap
	 */
	ReferenceDataMap getReferenceDataMapFromLineItemAssocList(List<LineItem> proposalLineItemLst);
	
	/**
	 * Returns all currencies as Map with the currency Id as key and the Currency Name as value
	 * @return
	 */
	Map<Long, String> getCurrencies();
	
	/**
	 * Returns the conversion rate for the given currency.
	 * @param currencyId
	 * @return
	 * @throws ParseException
	 */
	Map<String, Double> getCurrencyConversionRate() throws ParseException;

	/**
	 * Fetch all the advertiser based on name
	 * @param advertiserName
	 * @return
	 */
	List<Advertiser> getAdvertiserByName(String advertiserName);

	/**
	 * Fetch all the agency based on name
	 * @param agencyName
	 * @return
	 */
	List<Agency>  getAgencyByName(String agencyName);
}