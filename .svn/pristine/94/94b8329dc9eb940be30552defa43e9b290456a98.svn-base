package com.nyt.mpt.domain;

import org.apache.commons.lang.StringUtils;

/**
 * This class is used for providing Attribute Type info 
 * @author surendra.singh
 *
 */
public enum AttributeType {
	/** 
	 * Used for Creative Attribute type 
	 */
	CREATIVE {
		@Override
		public String getDisplayName() {
			return "Creative";
		}
	}, 
	/** 
	 * Used for Product Attribute type 
	 */
	PRODUCT {
		@Override
		public String getDisplayName() {
			return "Product";
		}
	};

	public abstract String getDisplayName();
	
	public static AttributeType findByName(String attributeType) {
		if(StringUtils.isNotBlank(attributeType)){
			for (AttributeType type : AttributeType.values()) {
				if(attributeType.equals(type.name())){
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No Attribute Type enum found for given Status name: "+attributeType);
	}
}
