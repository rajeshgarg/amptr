/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 
 * Used for Reload Resource Bundle Message Source
 * 
 * @author surendra.singh
 * 
 */
public class NYTReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	/**
	 * @param locale
	 * @return
	 */
	public Properties getMergedPropertiesForLocale(Locale locale) {
		return super.getMergedProperties(locale).getProperties();
	}
}
