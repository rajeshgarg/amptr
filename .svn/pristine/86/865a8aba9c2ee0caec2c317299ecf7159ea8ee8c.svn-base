/**
 * 
 */
package com.nyt.mpt.util.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author amandeep.singh
 *
 */
public enum LineItemProductTypeEnum {

	STANDARD{

		@Override
		public String getShortName() {
			return "S";
		}
	},
	RESERVABLE{

		@Override
		public String getShortName() {
			return "R";
		}
	},
	EMAIL{

		@Override
		public String getShortName() {
			return "E";
		}
	};
	public abstract String getShortName();
	
	/**
	 * Returns a LineItemProductTypeEnum based on short Name
	 * @param shortName
	 * @return
	 */
	public static LineItemProductTypeEnum findByShortName(String shortName){
		if (StringUtils.isNotBlank(shortName)) {
			for(LineItemProductTypeEnum lineItemProductTypeEnum :LineItemProductTypeEnum.values()){
				if(lineItemProductTypeEnum.getShortName().equals(shortName)){
					return lineItemProductTypeEnum;
				}
			}
		}
		throw new IllegalArgumentException("No Price Type enum found for given short name: " + shortName);
	}
	
	public static LineItemProductTypeEnum findByName(String productType) {
		if (StringUtils.isNotBlank(productType)) {
			for (LineItemProductTypeEnum lineItemProductType : LineItemProductTypeEnum.values()) {
				if (productType.equals(lineItemProductType.name())) {
					return lineItemProductType;
				}
			}
		}
		throw new IllegalArgumentException("No Status enum found for given Status name: " + productType);
	}
}
