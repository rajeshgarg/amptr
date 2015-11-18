/**
 * 
 */
package com.nyt.mpt.util.exception;

import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;

/**
 * @author Gurditta.Garg
 *
 */
@SuppressWarnings("serial")
public class DFPWrapperExcepion extends BusinessException {

	/**
	 * @param error
	 */
	public DFPWrapperExcepion(CustomBusinessError error) {
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
