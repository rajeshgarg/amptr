/**
 * 
 */
/**
 * 
 */
package com.nyt.mpt.util.exception;

import java.util.List;

import com.nyt.mpt.util.CustomBusinessError;

/**
 * This class used for user define Business Exception
 *
 * @author shishir.srivastava
 *
 */
public class BusinessException extends NytRuntimeException {

	private static final long serialVersionUID = 1L;

	/*
	 * Error Object where your error and it's type like Warning,error will be
	 * stored
	 */
	private CustomBusinessError customBusinessError;

	private List<CustomBusinessError> customBusinessErrors;

	/**
	 * Create a new Exception with the given error, but without any context
	 * data.
	 *
	 * @param error
	 */
	BusinessException(final CustomBusinessError error) {
		super(error.toString());
		this.customBusinessError = error;
	}

	public BusinessException(final CustomBusinessError error, final Exception rootCause) {
		super(rootCause);
		this.customBusinessError = error;
	}

	public BusinessException(final String message, final CustomBusinessError error, final Exception rootCause) {
		super(message, rootCause);
		this.customBusinessError = error;
	}

	BusinessException(final List<CustomBusinessError> errors) {
		super(errors.toString());
		this.customBusinessErrors = errors;
	}

	public CustomBusinessError getCustomBusinessError() {
		return customBusinessError;
	}

	public void setCustomBusinessError(final CustomBusinessError custBusinessErr) {
		this.customBusinessError = custBusinessErr;
	}

	public List<CustomBusinessError> getCustomBusinessErrors() {
		return customBusinessErrors;
	}

	public void setCustomBusinessErrors(final List<CustomBusinessError> custBusinessErr) {
		this.customBusinessErrors = custBusinessErr;
	}
}
