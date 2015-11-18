/**
 * 
 */
package com.nyt.mpt.util.exception;

import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;

/**
 * 
 * this generate exception for Template. 
 * @author manish.kesarwani
 *
 */
public class TemplateGenerationException extends BusinessException {
	
	private static final long serialVersionUID = 1L;
	
	public TemplateGenerationException(CustomBusinessError error) {
		super(error);		
	}

	/**
	 * Return Custom Business Error
	 * @param error
	 * @param messageType
	 * @return
	 */
	public static CustomBusinessError getCustomeBusinessError(final ErrorCodes error, final ErrorMessageType messageType) {
		CustomBusinessError customBusinessError = new CustomBusinessError();
		customBusinessError.setMessageType(messageType.name());
		customBusinessError.setErrorKey(error.getResourceName());
		return customBusinessError;
	}
}
