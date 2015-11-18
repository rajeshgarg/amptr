/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.sos.Product;
import com.nyt.mpt.domain.sos.ProductPlacement;

/**
 * @author amandeep.singh
 *
 */
public interface IProductDAOSOS {

	/**
	 * Return list of product for given product ids
	 *
	 * @param ids
	 * @return List<Product>
	 */
	List<Product> getProductListByIDs(final Long[] ids);
	
	/**
	 * Method to check whether product/sales target combination is active in SOS
	 *
	 * @param productId
	 * @param salesTargetID
	 * @return boolean
	 */
	List<ProductPlacement> getActiveProductPlacement(long productId, Long[] salesTargetID);
}
