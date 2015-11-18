package com.nyt.mpt.util.intercepter;

import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;

/**
 * Abstract Class defined to define the common methods of Interceptor
 *
 * @author amandeep.singh
 */
public abstract class AbstractValidatorInterceptor {

	public CustomBusinessError getCustomeBusinessError(final ErrorCodes error, final ErrorMessageType messageType) {
		final CustomBusinessError custBusinessErr = new CustomBusinessError();
		custBusinessErr.setMessageType(messageType.name());
		custBusinessErr.setErrorKey(error.getResourceName());
		return custBusinessErr;
	}
}
