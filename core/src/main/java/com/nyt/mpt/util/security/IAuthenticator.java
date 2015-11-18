/**
 * 
 */
package com.nyt.mpt.util.security;

/**
 * Authenticate given user against password from source.
 * 
 * @author surendra.singh
 * 
 */
public interface IAuthenticator {

	/**
	 * Authenticate given user against password from source.
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	boolean authenticate(String userName, String password);
}
