/**
 *
 */
package com.nyt.mpt.controller;

import static com.nyt.mpt.util.Constants.REQUIRES_AUTH;
import static com.nyt.mpt.util.Constants.REQUIRES_AUTH_VALUE;

import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.NYTReloadableResourceBundleMessageSource;
import com.nyt.mpt.service.IDatabaseHeartBeatService;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>HomePageController</code> class contain methods related to home
 * page and login information
 * 
 * @author Shishir.Srivastava
 */
@Controller
public class HomePageController {

	private static final Logger LOGGER = Logger.getLogger(HomePageController.class);

	private static final String AMPT_ALIVE = "ampt is alive";

	private static final String AMPT_DEAD = "ampt is dead";

	private NYTReloadableResourceBundleMessageSource messageSource;

	private IDatabaseHeartBeatService databaseHeartBeatService;

	/**
	 * Returns the ModelAndView for the first time when the applications loads
	 * @return
	 */
	@RequestMapping("/homepage/viewdetails")
	public ModelAndView displayPage() {
		final ModelAndView view = new ModelAndView("homePage");
		final User user = SecurityUtil.getUser();
		final Collection<GrantedAuthority> authSet = user.getAuthorities();
		for (Role role : user.getUserRoles()) {
			if (authSet.contains(new GrantedAuthorityImpl(role.getDefaultPage()))) {
				view.addObject("defaultTab", role.getDefaultPageName());
				view.addObject("defaultTabUrl", role.getDefaultPageUrl());
				break;
			}
		}
		return view;
	}

	/**
	 * Returns Login Page View
	 * @return
	 */
	@RequestMapping("/loginpage")
	public ModelAndView showLoginPage() {
		return new ModelAndView("loginPage");
	}

	/**
	 * Returns Login Page Failed View
	 * @return
	 */
	@RequestMapping("/loginfailed")
	public ModelAndView loginFailed() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Login Failed");
		}
		return new ModelAndView("loginErrorPage");
	}

	/**
	 * Returns Login Page Access Denied View
	 * @return
	 */
	@RequestMapping("/accessdenied")
	public ModelAndView accessDenied() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Access Denied");
		}
		return new ModelAndView("accessDeniedPage");
	}

	/**
	 * Returns ModelAndView when session time outs
	 * @param response
	 * @return
	 */
	@RequestMapping("/invalidSession")
	public ModelAndView invalidSession(final HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Invalid Session");
		}
		response.addHeader(REQUIRES_AUTH, REQUIRES_AUTH_VALUE);
		return showLoginPage();
	}

	/**
	 * This Method loads the resourceBundle and returns the key value pair to JS
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/homepage/initResourceBundle")
	public Properties initResourceBundle(final HttpServletRequest request) {
		return messageSource.getMergedPropertiesForLocale(RequestContextUtils.getLocale(request));
	}

	/**
	 * Checks if the the ampt is alive or dead
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/healthCheck")
	public String healthCheckDB() {
		if (databaseHeartBeatService.executeHealthStatus()) {
			return AMPT_ALIVE;
		} else {
			return AMPT_DEAD;
		}
	}

	public void setMessageSource(final NYTReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setDatabaseHeartBeatService(final IDatabaseHeartBeatService dbHeartBeatService) {
		this.databaseHeartBeatService = dbHeartBeatService;
	}
}