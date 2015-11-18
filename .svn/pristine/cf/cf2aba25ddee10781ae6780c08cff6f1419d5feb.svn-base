/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This <code>EmailScheduleFrequency</code> enum contains the frequency for EmailSchedule
 * 
 * @author suvigya
 */

public enum EmailScheduleFrequency {
	WEEKLY {
		@Override
		public String getDisplayName() {
			return "Weekly";
		}	
	},
	BIWEEKLY {
		@Override
		public String getDisplayName() {
			return "Biweekly";
		}
	};
	public abstract String getDisplayName();

	public static EmailScheduleFrequency findByName(final String emailScheduleFrequency) {
		if (StringUtils.isNotBlank(emailScheduleFrequency)) {
			for (EmailScheduleFrequency frequency : EmailScheduleFrequency.values()) {
				if (emailScheduleFrequency.equals(frequency.name())) {
					return frequency;
				}
			}
		}
		throw new IllegalArgumentException("No Frequency enum found for given Frequency display name: " + emailScheduleFrequency);
	}

	public static Map<String, String> getFrequencyMap() {
		final Map<String, String> frequencyMap = new LinkedHashMap<String, String>();
		for (EmailScheduleFrequency emailScheduleFrequency : EmailScheduleFrequency.values()) {
			frequencyMap.put(emailScheduleFrequency.name(), emailScheduleFrequency.getDisplayName());
		}
		return frequencyMap;
	}

}
