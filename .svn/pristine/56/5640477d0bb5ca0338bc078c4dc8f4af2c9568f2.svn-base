/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.List;

import com.nyt.mpt.dao.IValidatorDao;
import com.nyt.mpt.service.IValidatorService;

/**
 * Provide Validation services 
 * @author surendra.singh
 *
 */
public class ValidatorService implements IValidatorService {

	private IValidatorDao validatorDao;

	@Override
	public boolean validateAssociation(long id, List<String> associationValidator) {
		return validatorDao.validateAssociation(id, associationValidator);
	}

	public void setValidatorDao(IValidatorDao validatorDao) {
		this.validatorDao = validatorDao;
	}
}
