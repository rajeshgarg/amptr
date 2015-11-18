/**
 * 
 */
package com.nyt.mpt.dao;

import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.domain.sos.SosNotes;

/**
 * @author amandeep.singh
 *
 */
public interface ISosIntegrationDao {

	/**
	 * Create a order in SOS
	 * @param order
	 * @return
	 */
	Long saveOrder(SalesOrder order);

	/**
	 * Generate lineItem Id from sequence
	 * @return
	 */
	Long getLineItemId();

	/**
	 * Save Notes in SOS
	 * @param notes
	 */
	void saveNotes(SosNotes notes);
}
