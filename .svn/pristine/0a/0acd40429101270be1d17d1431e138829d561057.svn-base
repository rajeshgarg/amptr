/**
 * 
 */
package com.nyt.mpt.util;

import org.springframework.validation.FieldError;

import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class will be used as a wrapper over the validation error send by server to UI.
 * @author Shishir.Srivastava
 */
@SuppressWarnings("serial")
public class CustomFieldError extends FieldError {

	private String uiFieldIdentifier;
	/**
	 * This String will represent help message .Can be used in a UI to show what
	 * user can do to fix error.
	 */
	private UserHelpCodes helpCode;

	private String errorMessageForUI;

	private String errorHelpMessageForUI;

	public String getErrorMessageForUI() {
		return errorMessageForUI;
	}

	public void setErrorMessageForUI(final String errorMessageForUI) {
		this.errorMessageForUI = errorMessageForUI;
	}

	public String getErrorHelpMessageForUI() {
		return errorHelpMessageForUI;
	}

	public void setErrorHelpMessageForUI(final String errorHelpMessageForUI) {
		this.errorHelpMessageForUI = errorHelpMessageForUI;
	}

	private Object[] helpCodeArguments;

	/**
	 * @param objectName : name of the form 
	 * @param field : name of the form input
	 * @param codes	: error code should be defined in message.properties file 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 */
	public CustomFieldError(final String objectName, final String field, final String[] codes, final String uiFieldIdentifier) {
		super(objectName, field, null, false, codes, new Object[] {}, Constants.DEFAULT_VALIDATION_FAILED_MESSAGE);
		this.uiFieldIdentifier = uiFieldIdentifier;
	}

	/**
	 * @param objectName : name of the form 
	 * @param field : name of the form input
	 * @param codes	: error code should be defined in message.properties file 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 * @param arguments : if the error code has {} then should be defined 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 */
	public CustomFieldError(final String objectName, final String field, final String[] codes, final String uiFieldIdentifier, final Object[] arguments) {
		super(objectName, field, null, false, codes, arguments, Constants.DEFAULT_VALIDATION_FAILED_MESSAGE);
		this.uiFieldIdentifier = uiFieldIdentifier;
	}

	/**
	 * @param objectName : name of the form 
	 * @param field : name of the form input
	 * @param codes	: error code should be defined in message.properties file 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 * @param errorHelpCode : provide help code if required.
	 */
	public CustomFieldError(final String objectName, final String field, final String[] codes, final String uiFieldIdentifier, final UserHelpCodes helpCode) {
		super(objectName, field, null, false, codes, new Object[] {}, Constants.DEFAULT_VALIDATION_FAILED_MESSAGE);
		this.uiFieldIdentifier = uiFieldIdentifier;
		this.helpCode = helpCode;
	}

	/**
	 * @param objectName : name of the form 
	 * @param field : name of the form input
	 * @param codes	: error code should be defined in message.properties file 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 * @param arguments : if the error code has {} then should be defined 
	 * @param errorHelpCode : provide help code if required.
	 */
	public CustomFieldError(final String objectName, final String field, final String[] codes, final String uiFieldIdentifier, final Object[] arguments, final UserHelpCodes helpCode) {
		super(objectName, field, null, false, codes, arguments, Constants.DEFAULT_VALIDATION_FAILED_MESSAGE);
		this.uiFieldIdentifier = uiFieldIdentifier;
		this.helpCode = helpCode;
	}

	/**
	 * @param objectName : name of the form 
	 * @param field : name of the form input
	 * @param codes	: error code should be defined in message.properties file 
	 * @param uiFieldIdentifier : UI element id of a form which is to be validated.
	 * @param arguments : if the error code has {} then should be defined 
	 * @param errorHelpCode : provide help code if required.
	 * @param helpCodeArg : provide help code argument if required.
	 */
	public CustomFieldError(final String objectName, final String field, final String[] codes, final String uiFieldIdentifier, final Object[] arguments, 
			final UserHelpCodes helpCode, final Object[] helpCodeArg) {
		super(objectName, field, null, false, codes, arguments, Constants.DEFAULT_VALIDATION_FAILED_MESSAGE);
		this.uiFieldIdentifier = uiFieldIdentifier;
		this.helpCode = helpCode;
		this.helpCodeArguments = helpCodeArg;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final StringBuffer retValue = new StringBuffer();
		return retValue.append("CustomFieldError ( ").append(super.toString()).append(ConstantStrings.TAB).append("uiFieldIdentifier = ").append(this.uiFieldIdentifier)
				.append(ConstantStrings.TAB).append("helpCode = ").append(this.helpCode).append(ConstantStrings.TAB).append(" )").toString();
	}

	public String getUiFieldIdentifier() {
		return uiFieldIdentifier;
	}

	public void setUiFieldIdentifier(final String uiFieldIdentifier) {
		this.uiFieldIdentifier = uiFieldIdentifier;
	}

	public UserHelpCodes getHelpCode() {
		return helpCode;
	}

	public void setHelpCode(final UserHelpCodes helpCode) {
		this.helpCode = helpCode;
	}

	public Object[] getHelpCodeArguments() {
		return helpCodeArguments;
	}

	public void setHelpCodeArguments(final Object[] helpCodeArguments) {
		this.helpCodeArguments = helpCodeArguments;
	}
}
