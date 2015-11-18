/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This enum provide price type
 * 
 * @author pranay.prabhat
 * 
 */
public enum PriceType {
	/**
	 * Represent proposal order Price in Net
	 */
	Net {

		@Override
		public String getDisplayName() {
			return "Net";
		}
	},
	/**
	 * Represent proposal order Price in Gross
	 */
	Gross {

		@Override
		public String getDisplayName() {
			return "Gross";
		}
	};

	public abstract String getDisplayName();

	/**
	 * @return
	 */
	public static Map<String, String> getPriceTypeMap() {
		final Map<String, String> priceTypeMap = new LinkedHashMap<String, String>(PriceType.values().length);
		for (PriceType priceType : PriceType.values()) {
			priceTypeMap.put(priceType.name(), priceType.getDisplayName());
		}
		return priceTypeMap;
	}
}
