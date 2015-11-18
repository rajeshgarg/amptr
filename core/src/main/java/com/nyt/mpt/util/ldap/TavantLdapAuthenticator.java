/**
 * 
 */
package com.nyt.mpt.util.ldap;

import javax.naming.directory.DirContext;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;

import com.nyt.mpt.util.security.IAuthenticator;

/**
 * 
 * This class provide TavantLdap Authentication.
 * 
 * @author surendra.singh
 * 
 */
public class TavantLdapAuthenticator implements IAuthenticator {

	private static final Logger LOGGER = Logger.getLogger(LdapAuthenticator.class);

	private LdapTemplate ldapTemplate;

	private String domainName = "IN\\";

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.security.IAuthenticator#authenticate(java.lang.String, java.lang.String)
	 */
	public boolean authenticate(String usrName, String usrPass) {
		DirContext ctx = null;
		boolean isAuthenticated = false;
		try {
			ctx = ldapTemplate.getContextSource().getContext(domainName + usrName, usrPass);
			isAuthenticated = true;
		} catch (Exception e) {
			LOGGER.error("Context creation failed - authentication did not succeed : ", e);
			isAuthenticated = false;
		} finally {
			LdapUtils.closeContext(ctx);
		}
		return isAuthenticated;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
}
