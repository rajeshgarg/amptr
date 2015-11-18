/**
 * 
 */
package com.nyt.mpt.util.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import com.nyt.mpt.util.security.IAuthenticator;

/**
 * @author amandeep.singh
 * 
 */
public class LdapAuthenticator implements IAuthenticator {

	private static final Log LOGGER = LogFactory.getLog(LdapAuthenticator.class);

	private LdapTemplate ldapTemplate;

	private static final String PERSON = "Person";

	private static final String UID = "uid";

	private static final String OBJECT_CLASS = "objectclass";

	/* (non-Javadoc)
	 * @see com.nyt.mpt.util.security.IAuthenticator#authenticate(java.lang.String, java.lang.String)
	 */
	public boolean authenticate(String usrName, String usrPass) {
		boolean isAuthenticated = false;
		try {
			LOGGER.info("Authenticating user : " + usrName);
			AndFilter filter = new AndFilter();
			filter.and(new EqualsFilter(OBJECT_CLASS, PERSON)).and(new EqualsFilter(UID, usrName));
			isAuthenticated = ldapTemplate.authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), usrPass);
		} catch (Exception e) {
			LOGGER.error("Context creation failed - authentication did not succeed : ", e);
			isAuthenticated = false;
		}
		return isAuthenticated;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
}
