/**
 * 
 */
package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.nyt.mpt.util.enums.MessageType;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class can be used to send xml response back to client. Though
 * AjaxFormSubmitResponse class is also used for the same purpose but this class
 * should be used for xml response.
 * @author shishir.srivastava
 * 
 */
@XmlRootElement
public class XMLResponseForAjaxCall {

	/**
	 * Must be present for automatic marshelling. unmarshelling through JAXB.
	 */
	public XMLResponseForAjaxCall() {

	}

	/**
	 * @param messageSource
	 */
	public XMLResponseForAjaxCall(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private UIStatus uiStatus = new UIStatus();

	@XmlTransient
	private MessageSource messageSource;

	private List<ObjectError> errorList = new ArrayList<ObjectError>();

	@XmlElement
	public boolean getValidationFailed() {
		return !getErrorList().isEmpty();
	}

	@XmlElement
	public UIStatus getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(final UIStatus uiStatus) {
		this.uiStatus = uiStatus;
	}

	@XmlTransient
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setErrorList(List<ObjectError> allErrors) {
		this.errorList = allErrors;

	}

	@XmlElement
	public List<CustomError> getErrorList() {
		final List<CustomError> customErrList = new ArrayList<CustomError>();
		for (Object object : errorList) {
			if (object instanceof FieldError) {
				final CustomFieldError fieldError = (CustomFieldError) object;
				final CustomError error = new CustomError();
				error.setUiFieldIdentifier(fieldError.getUiFieldIdentifier());
				error.setErrorMessageForUI(messageSource.getMessage(fieldError, null));
				error.setErrorHelpMessageForUI(messageSource.getMessage(fieldError.getHelpCode().getResourceName(), new Object[] {}, null));
				customErrList.add(error);
			}
		}
		if (!errorList.isEmpty()) {
			uiStatus.setStatus(messageSource.getMessage(UserHelpCodes.DefaultValidationErrorHeaderMessage.getResourceName(), new Object[] {}, null));
			uiStatus.setMessageType(MessageType.Error);
		}
		return customErrList;
	}
}

/**
 * This class will hold an instance of validation error.
 * 
 * @author shishir.srivastava
 * 
 */
final class CustomError {

	private String uiFieldIdentifier;

	private String errorHelpMessageForUI;

	private String errorMessageForUI;

	private String inputField;

	public String getUiFieldIdentifier() {
		return uiFieldIdentifier;
	}

	public void setUiFieldIdentifier(final String uiFieldIdentifier) {
		this.uiFieldIdentifier = uiFieldIdentifier;
	}

	public String getErrorHelpMessageForUI() {
		return errorHelpMessageForUI;
	}

	public void setErrorHelpMessageForUI(final String errorHelpMessageForUI) {
		this.errorHelpMessageForUI = errorHelpMessageForUI;
	}

	public String getErrorMessageForUI() {
		return errorMessageForUI;
	}

	public void setErrorMessageForUI(final String errorMessageForUI) {
		this.errorMessageForUI = errorMessageForUI;
	}

	public String getInputField() {
		return inputField;
	}

	public void setInputField(final String inputField) {
		this.inputField = inputField;
	}
}
