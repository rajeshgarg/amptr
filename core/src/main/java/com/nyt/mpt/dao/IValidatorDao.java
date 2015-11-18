/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

/**
 * This interface declare Validation related methods
 *
 * @author surendra.singh
 *
 */
public interface IValidatorDao {

	/**
	 * Validating Association for associationValidator
	 *
	 * @param entityId
	 * @param validator
	 * @return boolean
	 */
	boolean validateAssociation(long entityId, List<String> validator);
}
