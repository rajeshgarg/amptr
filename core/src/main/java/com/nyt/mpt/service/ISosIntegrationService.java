/**
 * 
 */
package com.nyt.mpt.service;

import java.util.Map;
import java.util.Set;

import com.nyt.mpt.domain.LineItemTarget;


/**
 * @author amandeep.singh
 *
 */
public interface ISosIntegrationService {
	/**
	 * Create SOS Order from Proposal and returns Order Id
	 * @param proposalId
	 * @return
	 */
	Map<String, Set<LineItemTarget>> createOrder(Long proposalId);

}