/**
 * 
 */
package com.nyt.mpt.util;

import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class will be used as an extension of BindingResult, Method of these
 * class will be called fromv various validator used for validating form input.
 * 
 * @author Shishir.Srivastava
 * 
 */
@SuppressWarnings("serial")
public class CustomBindingResult extends BeanPropertyBindingResult {

	private static final Logger LOGGER = Logger.getLogger(CustomBindingResult.class);

	public CustomBindingResult(String objectName, Object target) {
		super(target, objectName);
	}

	/**
	 * @param field
	 *            : name of the form input
	 * @param errorCode
	 *            : error code should be defined in message.properties file
	 * @param uiFieldIdentifier
	 *            : UI element id of a form which is to be validated.
	 */
	public void rejectValue(final String field, final ErrorCodes errorCode, final String uiFieldIdentifier) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("rejecting value: " + field);
		}
		final String fixedField = fixedField(field);
		final FieldError fe = new CustomFieldError(getObjectName(), fixedField, resolveMessageCodes(errorCode.getResourceName(), field), uiFieldIdentifier);

		addError(fe);
	}

	/**
	 * @param field
	 *            : name of the form input
	 * @param errorCode
	 *            : error code should be defined in message.properties file
	 * @param uiFieldIdentifier
	 *            : UI element id of a form which is to be validated.
	 */
	public void rejectValue(final String field, final ErrorCodes errorCode, final String uiFieldIdentifier, final Object[] arguments) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("rejecting value: " + field);
		}
		final String fixedField = fixedField(field);
		final FieldError fe = new CustomFieldError(getObjectName(), fixedField, resolveMessageCodes(errorCode.getResourceName(), field), uiFieldIdentifier, arguments);

		addError(fe);
	}

	/**
	 * @param field
	 *            : name of the form input
	 * @param errorCode
	 *            : error code should be defined in message.properties file
	 * @param uiFieldIdentifier
	 *            : UI element id of a form which is to be validated.
	 */
	public void rejectValue(final String field, final ErrorCodes errorCode, final String uiFieldIdentifier, final UserHelpCodes errorHelpCode) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("rejecting value: " + field);
		}
		final String fixedField = fixedField(field);
		final FieldError fe = new CustomFieldError(getObjectName(), fixedField, resolveMessageCodes(errorCode.getResourceName(), field), uiFieldIdentifier, errorHelpCode);

		addError(fe);
	}

	/**
	 * @param field
	 *            : name of the form input
	 * @param errorCode
	 *            : error code should be defined in message.properties file
	 * @param defaultMessage
	 * @param uiFieldIdentifier
	 *            : UI element id of a form which is to be validated.
	 * @param errorHelpCode
	 *            : provide help code if required.
	 */
	public void rejectValue(final String field, final ErrorCodes errorCode, final String uiFieldIdentifier, final Object[] arguments, final UserHelpCodes errorHelpCode) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("rejecting value: " + field);
		}
		final String fixedField = fixedField(field);
		final FieldError fe = new CustomFieldError(getObjectName(), fixedField, resolveMessageCodes(errorCode.getResourceName(), field), uiFieldIdentifier, arguments, errorHelpCode);

		addError(fe);
	}
}
