/**
 *
 */
package com.nyt.mpt.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * This class is used for Component Handler
 * @author surendra.singh
 */
public class ComponentHandlerTag extends BodyTagSupport implements DynamicAttributes {

	private static final long serialVersionUID = 1L;

	private String bean;

	private Map<String, Object> params;

	private static final Logger LOGGER = Logger.getLogger(ComponentHandlerTag.class);

	public ComponentHandlerTag() {
		params = new HashMap<String, Object>();
	}

	public void setBean(final String bean) {
		this.bean = bean;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.DynamicAttributes#setDynamicAttribute(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setDynamicAttribute(final String uri, final String name, final Object value) throws JspException {
		if (value != null) {
			params.put(name, value);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		final WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		if (context != null) {
			if (context.containsBean(bean)) {
				final Object beanObj = context.getBean(bean);
				// if handler bean was found
				if (beanObj != null && beanObj instanceof ComponentHandler) {
					final ComponentHandler handler = (ComponentHandler) beanObj;
					Map<String, Object> results = null;
					try {
						results = handler.handleRequest(request, params);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
					if (results != null) {
						// add entries in map to request
						for (Entry<String, Object> result : results.entrySet()) {
							request.setAttribute(result.getKey(), result.getValue());
						}
					}
				} else {
					LOGGER.error(" Invalid bean definition for bean name " + bean + " in component handler. ");
					throw new RuntimeException(" Invalid bean definition for bean name " + bean + " in component handler. ");
				}
			}
		} else {
			LOGGER.error("context is null, cannot proceed.");
			throw new RuntimeException(" context is null, cannot proceed.");
		}
		return SKIP_BODY;
	}
}
