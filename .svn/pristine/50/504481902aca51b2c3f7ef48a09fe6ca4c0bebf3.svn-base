package com.nyt.mpt.util.exception;

import com.nyt.mpt.util.CustomBusinessError;


/**
 * For salesforce connection exception
 * @author rakesh.tewari
 *
 */
public class CustomCheckedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Error Object where your error and it's type like Warning,error will be
	 * stored
	 */
	private CustomBusinessError customBusinessError;
	
	/**
	 * Create a new Exception with the given error, but without any context
	 * data.
	 *
	 * @param error
	 */
	public CustomCheckedException(final CustomBusinessError error) {
		super(error.toString());
		this.customBusinessError = error;
	}

	public CustomBusinessError getCustomBusinessError() {
		return customBusinessError;
	}

	public void setCustomBusinessError(CustomBusinessError customBusinessError) {
		this.customBusinessError = customBusinessError;
	}
}
