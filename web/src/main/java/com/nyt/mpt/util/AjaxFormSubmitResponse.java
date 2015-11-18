package com.nyt.mpt.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * This class can be used to send AJAX response
 * 
 * @author shishir.srivastava
 * 
 */
@SuppressWarnings("serial")
public class AjaxFormSubmitResponse implements Serializable {

	private Model model;

	private MessageSource messageSource;

	private Map<Object, Object> objectMap = new HashMap<Object, Object>();

	private List<ObjectError> errorList = new ArrayList<ObjectError>();

	private final UIStatus uiStatus = new UIStatus();

	public UIStatus getUiStatus() {
		return uiStatus;
	}

	public List<ObjectError> getErrorList() {
		for (Object object : errorList) {
			if (object instanceof FieldError) {
				final CustomFieldError fieldError = (CustomFieldError) object;
				/*
				 * Retrieve exact message from message source and set in the
				 * list.
				 */
				fieldError.setErrorMessageForUI(messageSource.getMessage(fieldError, null));
				fieldError.setErrorHelpMessageForUI(messageSource.getMessage(fieldError.getHelpCode().getResourceName(), new Object[] {}, null));
			}
		}
		return errorList;

	}

	public AjaxFormSubmitResponse(final MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setErrorList(final List<ObjectError> errorList) {
		this.errorList = errorList;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(final Model model) {
		this.model = model;
	}

	public Map<Object, Object> getObjectMap() {
		return objectMap;
	}

	public void setObjectMap(final Map<Object, Object> objectMap) {
		this.objectMap = objectMap;
	}

	@JsonIgnore
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public boolean getValidationFailed() {
		return !getErrorList().isEmpty();
	}

}
