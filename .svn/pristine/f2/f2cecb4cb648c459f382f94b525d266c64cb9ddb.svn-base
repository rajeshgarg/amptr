/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author amandeep.singh
 *
 */
public enum PricingStatus {
	/**
	 * Represent status of Line Item is "Pricing Approved"
	 */
	PRICING_APPROVED {

		@Override
		public String getDisplayName() {
			return "Pricing Approved";
		}
	},
	/**
	 * Represent status of Line Item is "Unapproved"
	 */
	UNAPPROVED {

		@Override
		public String getDisplayName() {
			return "Unapproved";
		}
	},
	/**
	 * Represent status of Line Item is "System Approved"
	 */
	SYSTEM_APPROVED {

		@Override
		public String getDisplayName() {
			return "System Approved";
		}
	};
	
	public abstract String getDisplayName();
	
	public static PricingStatus findByName(String pricingStatus) {
		if (StringUtils.isNotBlank(pricingStatus)) {
			for (PricingStatus pricingStat : PricingStatus.values()) {
				if (pricingStatus.equals(pricingStat.name())) {
					return pricingStat;
				}
			}
		}
		throw new IllegalArgumentException("No Status enum found for given Status name: " + pricingStatus);
	}
	
	public static PricingStatus findByDisplayName(String pricingStatus) {
		if (StringUtils.isNotBlank(pricingStatus)) {
			for (PricingStatus pricingStat : PricingStatus.values()) {
				if (pricingStatus.equals(pricingStat.getDisplayName())) {
					return pricingStat;
				}
			}
		}
		throw new IllegalArgumentException("No Status enum found for given Status display name: " + pricingStatus);
	}
}
