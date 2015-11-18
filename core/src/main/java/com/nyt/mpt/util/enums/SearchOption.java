/**
 * 
 */
package com.nyt.mpt.util.enums;

/**
 * JQGrid Search Options for data filtering
 * 
 * @author surendra.singh
 * 
 */
public enum SearchOption {
	/**
	 * Search data in JQGrid based on filter option "CONTAIN"
	 */
	CONTAIN {
		@Override
		public String toString() {
			return "cn";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "Equal to"
	 */
	EQUAL {
		@Override
		public String toString() {
			return "eq";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "not equal to"
	 */
	NOTEQUAL {
		@Override
		public String toString() {
			return "ne";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "greater than"
	 */
	GREATER {
		@Override
		public String toString() {
			return "gt";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "less than"
	 */
	LESS {
		@Override
		public String toString() {
			return "lt";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "greater then equal to"
	 */
	GREATER_EQUAL {
		@Override
		public String toString() {
			return "ge";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "less than equal to"
	 */
	LESS_EQUAL {
		@Override
		public String toString() {
			return "le";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "begins with"
	 */
	BEGINS_WITH {
		@Override
		public String toString() {
			return "bw";
		}
	},
	/**
	 * Search data in JQGrid based on filter option "between"
	 */
	BETWEEN {
		@Override
		public String toString() {
			return "btw";
		}
	},
	IN {
		@Override
		public String toString() {
			return "in";
		}
	};
}
