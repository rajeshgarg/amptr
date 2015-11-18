/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Garima.garg
 *
 */
public enum ProductClassEnum {

	HOMEPAGE {
		@Override
		public String getDisplayName() {
			return "Home Page";
		}

		@Override
		public String getOptionValue() {
			return "HOME PAGE";
		}
	},
	DISPLAYCROSSPLATFORM {
		@Override
		public String getDisplayName() {
			return "Display Cross Platform";
		}

		@Override
		public String getOptionValue() {

			return "Display Cross Platform";
		}
	};

	public abstract String getDisplayName();

	public abstract String getOptionValue();
	
	/**
	 * Return Map of price type from enum
	 * @return
	 */
	public static Map<String, String> getAllPriceType() {
		final Map<String, String> allPriceType = new LinkedHashMap<String, String>();
		for (LineItemPriceTypeEnum lineItemPriceType : LineItemPriceTypeEnum.values()) {
			allPriceType.put(lineItemPriceType.getOptionValue(), lineItemPriceType.getDisplayName());
		}
		return allPriceType;
	}
}
