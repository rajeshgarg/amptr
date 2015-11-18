/**
 * 
 */
package com.nyt.mpt.util.exception;

/**
 * this is used for generating run time exception
 * 
 * @author shishir.srivastava
 * 
 */
public class NytRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	NytRuntimeException(String string) {
		super(string);
	}

	public NytRuntimeException(Exception ex) {
		super(ex);
	}

	NytRuntimeException(String message, Exception ex) {
		super(message, ex);
	}

}
