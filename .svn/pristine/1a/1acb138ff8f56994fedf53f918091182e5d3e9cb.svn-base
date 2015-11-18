/**
 * 
 */
package com.nyt.mpt.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ComponentHandler;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.NYTReloadableResourceBundleMessageSource;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * Class define Welcome String Component Handler
 * 
 * @author amandeep.singh
 */
public class WelcomeStringComponentHandler implements ComponentHandler {

	private NYTReloadableResourceBundleMessageSource messageSource;

	@Override
	public Map<String, Object> handleRequest(final HttpServletRequest request, final Map<String, Object> params) {
		final Map<String, Object> results = new HashMap<String, Object>();
		final User user = SecurityUtil.getUser();
		final StringBuilder welcomeStr = new StringBuilder(messageSource.getMessage("label.generic.welcome", null, "Welcome", null));
		welcomeStr.append(ConstantStrings.SPACE + user.getFirstName() + " | ");
		final Set<Role> setRoles = user.getUserRoles();
		for (Role role : setRoles) {
			welcomeStr.append(ConstantStrings.SPACE + role.getDisplayName() + " | ");
		}
		results.put("welcomeString", welcomeStr.toString());
		return results;
	}

	public void setMessageSource(final NYTReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
}