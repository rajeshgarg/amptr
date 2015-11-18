/**
 * 
 */
package com.nyt.mpt.util.exception;

import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;

/**
 * Used to generate yield-ex avails exception
 *
 * @author manish.kesarwani
 *
 */
public class YieldexAvailsException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public YieldexAvailsException(final CustomBusinessError error) {
		super(error);
	}

	/**
	 * This method is used to create custom business error
	 *
	 * @param error
	 * @param messageType
	 * @return CustomBusinessError
	 */
	public static CustomBusinessError getCustomeBusinessError(final ErrorCodes error, final ErrorMessageType messageType, final String[] messageArgument) {
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setMessageType(messageType.name());
		custBusinessErr.setErrorKey(error.getResourceName());
		if (messageArgument != null && messageArgument.length > 0) {
			custBusinessErr.setArguments(messageArgument);
		}

		return custBusinessErr;
	}
}
