/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.SalesTargetAmpt;

/**
 * This DAO level interface is used for Sales Target related operation and which
 * MP_Sales_Target data is fetched by corn job
 * 
 * @author rakesh.tewari
 */
public interface ISalesTargetAmptDAO {

	/**
	 * Return a list of Sales Target based on Ids
	 * @param ids
	 * @return
	 */
	List<SalesTargetAmpt> getSalesTarget(Long[] ids);

	/**
	 * Save or update all sales targets
	 * @param salesTargetList
	 */
	void saveSalesTargets(List<SalesTargetAmpt> salesTargetList);
}
