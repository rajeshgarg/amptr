/**
 * 
 */
package com.nyt.mpt.util.exception;

import com.nyt.mpt.util.CustomBusinessError;

/**
 * Exception class to handle all Proposal Access related exception
 * 
 * @author amandeep.singh
 * 
 */
public class ProposalAccessException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ProposalAccessException(CustomBusinessError error) {
		super(error);
	}
}