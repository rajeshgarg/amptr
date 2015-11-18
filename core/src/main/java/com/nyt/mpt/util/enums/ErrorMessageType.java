/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * This Enum is use to define error Type i.e. whether a Warning,Error,Confirm etc message need to shown to User
 * @author amandeep.singh
 * 
 */
public enum ErrorMessageType {
	/** 
	 * Represent message type as a warning 
	 */
	WARNING {
		@Override
		public String getDisplayName() {
			return "Warning";
		}
	}, 
	/** 
	 * Represent error message  
	 */
	ERROR {
		@Override
		public String getDisplayName() {
			return "Error";
		}
	}, 
	/** 
	 * Represent a Confirm message 
	 */
	CONFIRM {
		@Override
		public String getDisplayName() {
			return "Confirm";
		}
	},
	/** 
	 * Represent SOS Violation Error message 
	 */
	SOS_VIOLATION {
		@Override
		public String getDisplayName() {
			return "SOS Violation Error";
		}
	},
	/** 
	 * Represent SOS Violation Error message for template
	 */
	TEMPLATE_ERROR {
		@Override
		public String getDisplayName() {
			return "Template Error";
		}
	},	
	/** 
	 * Represent Yield-ex Error message for Avails
	 */
	YIELDEX_AVAILS_ERROR {
		@Override
		public String getDisplayName() {
			return "Yieldex Avails Error";
		}
	},
	/** 
	 * Represent Proposal Error message 
	 */
	PROPOSAL_ERROR {
		@Override
		public String getDisplayName() {
			return "Proposal Error";
		}
	},
	/** 
	 * Represent Proposal Error message 
	 */
	SOS_INTEGRATION_ERROR {
		@Override
		public String getDisplayName() {
			return "SOS Integration Error";
		}
	},
	/** 
	 * Represent DFP Error message 
	 */
	DFP_WRAPPER_EXCEPTION {
		@Override
		public String getDisplayName() {
			return "DFP Error";
		}
	};

	public abstract String getDisplayName();
	
	public static ErrorMessageType findByName(String messageType) {
		if(StringUtils.isNotBlank(messageType)){
			for (ErrorMessageType type : ErrorMessageType.values()) {
				if(messageType.equals(type.name())){
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No Message Type enum found for given Status name: "+messageType);
	}
}
