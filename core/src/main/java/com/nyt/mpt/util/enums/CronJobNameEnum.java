/**
 *
 */
package com.nyt.mpt.util.enums;

/**
 * @author amandeep.singh
 *
 */
public enum CronJobNameEnum {

	SECTION_WEIGHT_CALCULATION {

		/**
		 * Job to calculate section weight
		 */
		@Override
		public String getName() {
			return "Section Weight Calculation";
		}
	},
	UPDATE_EXPIRED_PROPOSAL_STATUS {

		/**
		 * Job to calculate section weight
		 */
		@Override
		public String getName() {
			return "Mark Proposals As Expired";
		}
	},
	SALESFORCE_PROPOSAL_INTEGRATION_JOB {
		/**
		 * Job to integrate with Salesforce
		 */
		@Override
		public String getName() {
			return "Salesforce Proposal Integration";
		}
	},
	CALENDAR_RESERVATION_EXPIRY_JOB{
		/**
		 * Sent Mail for Reservation Expiration Info
		 */
		@Override
		public String getName() {
			return "Sent Mail for Reservation Expiry";
		}
	},
	VULNERABLE_HOME_PAGE_RESERVATIONS_JOB{
		/**
		 * Send Mail to Admin for vulnerable home page Reservations
		 */
		@Override
		public String getName() {
			return "Sent Mail for vulnerable home page Reservations";
		}
	},
	VULNERABLE_PROPOSAL_HOME_PAGE_RESERVATION_JOB{
		/**
		 * Send Mail to Owner of proposal which  line item are  vulnerable
		 */
		@Override
		public String getName() {
			return "Send Mail to Owner of proposal for its vulnerable home page reservations";
		}
	},
	DELETE_INACTIVE_PRODUCT_ASSOC_JOB{
		/** 
		 * Delete inactive product assoc from AMPT if product is inactive in ods
		 */
		@Override
		public String getName() {
			return "Delete Inactive Product Assoc";
		}

	};

	public abstract String getName();
}
