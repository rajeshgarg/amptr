/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This enum is used for criticality enum
 * 
 * @author amandeep.singh
 * 
 */
public enum Criticality {
	/**
	 * Represent Priority of proposal is low
	 */
	REGULAR {

		@Override
		public String getDisplayName() {
			return "Regular";
		}
	},
	/**
	 * Represent Priority of proposal is normal
	 */
	URGENT {

		@Override
		public String getDisplayName() {
			return "Urgent";
		}
	};
	

	public abstract String getDisplayName();

	/**
	 * @param currency
	 * @return
	 */
	public static Criticality findByName(String criticality) {
		if (StringUtils.isNotBlank(criticality)) {
			for (Criticality crit : Criticality.values()) {
				if (criticality.equals(crit.name())) {
					return crit;
				}
			}
		}
		throw new IllegalArgumentException("No criticallity enum found for given criticallity name: " + criticality);
	}

	/**
	 * @param currency
	 * @return
	 */
	public static Criticality findByDisplayName(String criticality) {
		if (StringUtils.isNotBlank(criticality)) {
			for (Criticality crit : Criticality.values()) {
				if (criticality.equals(crit.getDisplayName())) {
					return crit;
				}
			}
		}
		throw new IllegalArgumentException("No criticallity enum found for given criticallity display name: " + criticality);
	}
	
	/**
	 * @return
	 */
	public static Map<String, String> getCriticalityMap() {
		final Map<String, String> criticalityMap = new LinkedHashMap<String, String>();
		for (Criticality criticality : Criticality.values()) {
			criticalityMap.put(criticality.name(), criticality.getDisplayName());
		}
		return criticalityMap;
	}
}
