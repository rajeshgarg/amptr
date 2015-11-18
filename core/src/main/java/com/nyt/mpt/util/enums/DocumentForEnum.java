/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * This Enum is used For creative , product,  proposal and  Package type's for a document
 * 
 * @author surendra.singh
 */

public enum DocumentForEnum {
	/** 
	 * This is used for creative type document 
	 */
	CREATIVE {

		@Override
		public String getDisplayName() {
			return "Creative";
		}		
	},
	/** 
	 * This is used for Product type document 
	 */
	PRODUCT {

		@Override
		public String getDisplayName() {
			return "Product";
		}		
	}, 
	/** 
	 * This is used for Package type document 
	 */
	PACKAGE {

		@Override
		public String getDisplayName() {
			return "Package";
		}		
	},
	/** 
	 * This is used for Proposal type document 
	 */
	PROPOSAL {

		@Override
		public String getDisplayName() {
			return "Proposal";
		}		
	};
	
	public abstract String getDisplayName();
	
	public static DocumentForEnum findByName(String type) {
		if (StringUtils.isNotBlank(type)) {
			for (DocumentForEnum enm : DocumentForEnum.values()) {
				if (enm.name().equalsIgnoreCase(type)) {
					return enm;
				}
			}
		}
		throw new IllegalArgumentException("Document Type not found for given type: "+type);
	}
}
