/**
 *
 */
package com.nyt.mpt.util.security;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.impl.UserService;

/**
 *
 * This class provide AMPT Authentication.
 *
 * @author surendra.singh
 *
 */
public class AMPTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static final Logger LOGGER = Logger.getLogger(AMPTAuthenticationProvider.class);

	private UserService userService;

	private List<IAuthenticator> authenticators;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
		UserDetails loadedUser = null;
		try {
			LOGGER.info("Authorizing user : " + username);
			loadedUser = userService.loadUserByUsername(username);
			LOGGER.info("User successfully authorized : " + username);
		} catch (UsernameNotFoundException unf) {
			LOGGER.warn("User not found in the data base : -------------- ");
		}
		return loadedUser;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Authentication authenticate(Authentication authentication) {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, messages.getMessage(
				"AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported"));

		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		final String userName = token.getPrincipal().toString();
		final String password = token.getCredentials().toString();
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			return null;
		}

		boolean authenticated = false;
		for (IAuthenticator authenticator : authenticators) {
			authenticated = authenticator.authenticate(userName, password);
		}
		if (!authenticated) {
			return null;
		}
		UserDetails user = retrieveUser(userName, (UsernamePasswordAuthenticationToken) authentication);

		/**
		 * If user is disabled in the DB it should not be allowed to access the
		 * application.
		 */
		if (user == null || !user.isEnabled()) {
			user = new User();
			((User) user).setUserRoles(Collections.EMPTY_SET);
		}
		return createSuccessAuthentication(user, authentication, user);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
		// TODO
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAuthenticators(List<IAuthenticator> authenticators) {
		this.authenticators = authenticators;
	}
}
