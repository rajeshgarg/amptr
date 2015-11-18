/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used for price type In packageLineItems.jsp
 * 
 * @author rakesh.tewari
 * 
 */
public enum LineItemPriceTypeEnum {

	CPM {
		@Override
		public String getDisplayName() {
			return "CPM";
		}

		@Override
		public String getOptionValue() {
			return "CPM";
		}
	},
	FLATRATE {
		@Override
		public String getDisplayName() {
			return "Flat Rate";
		}

		@Override
		public String getOptionValue() {

			return "FLAT RATE";
		}
	},
	ADDEDVALUE {
		@Override
		public String getDisplayName() {
			return "Added Value";
		}

		@Override
		public String getOptionValue() {
			return "ADDED VALUE";
		}
	},
	CUSTOMUNIT {
		@Override
		public String getDisplayName() {
			return "Custom Unit";
		}

		@Override
		public String getOptionValue() {
			return "CUSTOM UNIT";
		}
	},
	PREEMPTIBLE {
		@Override
		public String getDisplayName() {
			return "Pre Emptible";
		}

		@Override
		public String getOptionValue() {
			return "PRE EMPTIBLE";
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
