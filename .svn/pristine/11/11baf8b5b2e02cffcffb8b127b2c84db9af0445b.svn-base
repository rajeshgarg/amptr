/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author surendra.singh
 * 
 */
public enum DateCriteriaEnum {
	LAST_WEEK {

		@Override
		public String getDisplayName() {
			return "Last 7 Days";
		}

		@Override
		public int getDayDifferenceFormToDay() {
			return -7;
		}
	},
	
	DAY_BF_YESTERDAY {
		
		@Override
		public String getDisplayName() {
			return "Day Before Yesterday";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return -2;
		}
	},
	
	YESTERDAY {
		
		@Override
		public String getDisplayName() {
			return "Yesterday";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return -1;
		}
	},
	TO_DAY {
		
		@Override
		public String getDisplayName() {
			return "Today";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return 1;
		}
	},	
	TOMORROW {
		@Override
		public String getDisplayName() {
			return "Tomorrow";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return 2;
		}
	},
	
	DAY_AF_TOMORROW {
		
		@Override
		public String getDisplayName() {
			return "Day After Tomorrow";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return 3;
		}
	},
	
	THIS_WEEK {
		@Override
		public String getDisplayName() {
			return "This Week";
		}
		
		@Override
		public int getDayDifferenceFormToDay() {
			return 8;
		}
	};
	
	public abstract String getDisplayName();
	
	public abstract int getDayDifferenceFormToDay();
	
	public static DateCriteriaEnum findByName(final String criteriaName) {
		if (StringUtils.isNotBlank(criteriaName)) {
			for (DateCriteriaEnum criteria : DateCriteriaEnum.values()) {
				if (criteriaName.equalsIgnoreCase(criteria.name())) {
					return criteria;
				}
			}
		}
		throw new IllegalArgumentException("No Date Criteria found for given name: " + criteriaName);
	}
}
