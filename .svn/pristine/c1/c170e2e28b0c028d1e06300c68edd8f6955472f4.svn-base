/**
 *
 */
package com.nyt.mpt.util.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author suvigya
 * 
 */
public enum Weekdays {
	SUNDAY {
		@Override
		public String getDisplayName() {
			return "Sun";
		}
		
		@Override
		public int getShortCode() {
			return 1;
		}
	},
	MONDAY {
		@Override
		public String getDisplayName() {
			return "Mon";
		}
		
		@Override
		public int getShortCode() {
			return 2;
		}
	},
	TUESDAY {
		@Override
		public String getDisplayName() {
			return "Tue";
		}
		
		@Override
		public int getShortCode() {
			return 3;
		}

	},
	WEDNESDAY {
		@Override
		public String getDisplayName() {
			return "Wed";
		}
		
		@Override
		public int getShortCode() {
			return 4;
		}
	},
	THURSDAY {
		@Override
		public String getDisplayName() {
			return "Thu";
		}
		
		@Override
		public int getShortCode() {
			return 5;
		}
	},
	FRIDAY {
		@Override
		public String getDisplayName() {
			return "Fri";
		}
		
		@Override
		public int getShortCode() {
			return 6;
		}
	},
	SATURDAY {
		@Override
		public String getDisplayName() {
			return "Sat";
		}
		
		@Override
		public int getShortCode() {
			return 7;
		}
	};

	public abstract String getDisplayName();
	public abstract int getShortCode();
	
	/**
	 * @param code
	 * @return
	 */
	public static Weekdays findByCode(final int code) {
		if (code > 0 && code < 8) {
			for (Weekdays wd : Weekdays.values()) {
				if (code == wd.getShortCode()) {
					return wd;
				}
			}
		}
		throw new IllegalArgumentException("No Weekday enum found for given Weekday code: " + code);
	}

	/**
	 * @param weekDay
	 * @return
	 */
	public static Weekdays findByName(final String weekDay) {
		if (StringUtils.isNotBlank(weekDay)) {
			for (Weekdays wd : Weekdays.values()) {
				if (weekDay.equals(wd.name())) {
					return wd;
				}
			}
		}
		throw new IllegalArgumentException("No Weekday enum found for given Weekday display name: " + weekDay);
	}

	/**
	 * @return
	 */
	public static Map<String, String> getWeekDayMap() {
		final Map<String, String> weekDays = new LinkedHashMap<String, String>();
		for (Weekdays weekday : Weekdays.values()) {
			weekDays.put(weekday.name(), weekday.getDisplayName());
		}
		return weekDays;
	}
}
