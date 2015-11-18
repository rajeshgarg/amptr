/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * This enum is used to generate Line Item Exception
 * 
 * @author amandeep.singh
 * 
 */
public enum LineItemExceptionEnum {
	/**
	 * Represent line item exception when CPM Price is less than the Rate Card
	 * Price
	 */
	PRICING {
		@Override
		public String getDisplayName() {
			return ErrorCodes.pricingException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Pricing Rule is not defined
	 */
	NOPRICING {
		@Override
		public String getDisplayName() {
			return ErrorCodes.noPricingException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Rich Media has been selected for CPM <
	 * 20$
	 */
	RICHMEDIAWITHLESSCPM {
		@Override
		public String getDisplayName() {
			return ErrorCodes.richMediaWithLessCPM.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Copied from expired Proposal
	 */
	EXPIREDPROPOSAL {
		@Override
		public String getDisplayName() {
			return ErrorCodes.expiredProposalException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Copied from expired Package
	 */
	EXPIREDPACKAGE {
		@Override
		public String getDisplayName() {
			return ErrorCodes.expiredPackageException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Copied from unbreakable Package
	 */
	UNBREAKABLEPACKAGE {
		@Override
		public String getDisplayName() {
			return ErrorCodes.unbreakablePackageException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Product has been expired
	 */
	EXPIREDPRODUCT {
		@Override
		public String getDisplayName() {
			return ErrorCodes.expiredProductException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Sales Target has been expired
	 */
	EXPIREDSALESTARGET {
		@Override
		public String getDisplayName() {
			return ErrorCodes.expiredSalesTargetException.getResourceName();
		}
	},
	/**
	 * Represent line item exception when Sales Target & Product has been
	 * expired
	 */
	EXPIREDSALESTARGETPRODUCT {
		@Override
		public String getDisplayName() {
			return ErrorCodes.expiredSalesTargetProductException.getResourceName();
		}
	},
	SOVISGREATERTHANHUNDRED {
		@Override
		public String getDisplayName() {
			return ErrorCodes.sovGreaterThanHundred.getResourceName();
		}
	},
	/**
	 * Represent The total investment exception
	 * expired
	 */
	TOTALINVESTMENT{
		@Override
		public String getDisplayName() {
			return ErrorCodes.totalInvestmentException.getResourceName();
		}
	},
	/**
	 * Represents the SOR is blank 
	 */
	NOSOR{
		@Override
		public String getDisplayName() {
			return ErrorCodes.nosor.getResourceName();
		}
	};

	public abstract String getDisplayName();

	public static LineItemExceptionEnum findByName(String lineItemException) {
		if (StringUtils.isNotBlank(lineItemException)) {
			for (LineItemExceptionEnum type : LineItemExceptionEnum.values()) {
				if (lineItemException.equals(type.name())) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No lineItem Exception enum found for given Status name: " + lineItemException);
	}
}
