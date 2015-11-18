/**
 * 
 */
package com.nyt.mpt.util.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.enums.ErrorMessageType;

/**
 * Exception class to handle all SOS Violation Exceptions 
 * @author Manish.Kesarwani
 * 
 */
public class SOSViolationException extends BusinessException {
	
	private static final Logger LOGGER = Logger.getLogger(SOSViolationException.class);

	private static final long serialVersionUID = 1L;
	
	public SOSViolationException(List<CustomBusinessError> errorList) {
		super(errorList);	
	}

	/**
	 * @param errorMessages
	 * @return List of CustomeBusinessErrors
	 */
	public static List<CustomBusinessError> getCustomeBusinessErrors(Map<String, String> errorMessages) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("creating list of CustomeBusinessError");
		}
		
		List<CustomBusinessError> errorList = new ArrayList<CustomBusinessError>();
		CustomBusinessError customBusinessError = null;
		for(String key : errorMessages.keySet()) {
			Object[] obj = {errorMessages.get(key)};
			customBusinessError = new CustomBusinessError();
			customBusinessError.setMessageType(ErrorMessageType.SOS_VIOLATION.name());
			customBusinessError.setErrorKey(key);
			customBusinessError.setArguments(obj);
			errorList.add(customBusinessError);
		}
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Custome Business Error List: "  + errorList);
		}
		return errorList;
	}
}
