/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * API to be implemented by ComponentHandler classes. Get HttpRequest and other
 * params from the JSP Tag and return the model Map required by the tag to
 * render.
 * 
 * @author surendra.singh
 */
public interface ComponentHandler {

	/**
	 * API to be implemented by ComponentHandler classes.
	 * Get HttpRequest and other params from the JSP Tag and return the model Map required by the tag to render.
	 * 
	 * @param request HttpRequest in scope
	 * @param params Any additional params required by the handler to fetch the model map required by the tag
	 * @return
	 */
	Map<String, Object> handleRequest(HttpServletRequest request, Map<String, Object> params);

}
