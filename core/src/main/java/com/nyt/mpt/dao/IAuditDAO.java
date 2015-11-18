/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Audit;

/**
 * @author amandeep.singh
 *
 */
public interface IAuditDAO {
	
	/**
	 * Create a new Audit Log
	 *
	 * @param audit
	 * @return
	 */
	Audit create(Audit audit);

	/**
	 * Get List of Audits by ProposalId
	 * @param parentId
	 * @return
	 */
	List<Audit> getAuditsByParentId(long parentId);

}
