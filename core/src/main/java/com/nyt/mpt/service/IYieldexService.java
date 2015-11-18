package com.nyt.mpt.service;

import com.nyt.mpt.domain.InventoryDetail;
import com.nyt.mpt.util.exception.YieldexAvailsException;

/**
 * This interface is used to communicate with yield-ex web service
 * @author manish.kesarwani
 *
 */
public interface IYieldexService {
	
	/**
	 * Get Avails Inventory details from Yield-ex based on URL
	 * @param url
	 * @return InventoryDetail
	 * @throws YieldexAvailsException
	 */
	InventoryDetail getInventoryDetail(String url);
	
	/**
	 * This method is used to get Inventory with daily details information
	 * @param url
	 * @return
	 */
	InventoryDetail getInvWithDailyDetails(String url);
}
