/**
 * 
 */
package com.nyt.mpt.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This Class is used to throw Exception from Controller to UI
 * 
 * @author amandeep.singh
 * 
 */
public class CustomBusinessError {

	/*
	 * Key Defined in the properties File
	 */
	private String errorKey;

	/*
	 * Error message shown at UI It replaces the place Holders if provided
	 */
	private String errorMessage;

	/*
	 * Arguments to be replaced in PlaceHolder
	 */
	private Object[] arguments;

	/*
	 * Message Type i.e., Warning,Error etc
	 */
	private String messageType;

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("Error Key", errorKey).append("Error Message", errorMessage).append(
				"Arguments", arguments).toString();
	}
}
