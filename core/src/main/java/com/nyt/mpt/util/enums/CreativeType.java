/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * This enum is used for Creative Type enum
 * 
 * @author amandeep.singh
 * 
 */
public enum CreativeType {
	/**
	 * Creative Type is HTML5
	 */
	HTML5 {
		@Override
		public String getDisplayValue() {
			return "HTML5";
		}
	},
	/**
	 * Creative Type is Rich Media
	 */
	RICH_MEDIA {
		@Override
		public String getDisplayValue() {
			return "Rich Media";
		}
	},
	/**
	 * Creative Type is Text
	 */
	TEXT {
		@Override
		public String getDisplayValue() {
			return "Text";
		}
	},
	/**
	 * Creative Type is Video
	 */
	VIDEO {
		@Override
		public String getDisplayValue() {
			return "Video";
		}
	},
	/**
	 * Creative Type is Standard
	 */
	STANDARD {
		@Override
		public String getDisplayValue() {
			return "Standard";
		}
	};

	public abstract String getDisplayValue();

	public static CreativeType findByName(String creativeType) {
		if (StringUtils.isNotBlank(creativeType)) {
			for (CreativeType type : CreativeType.values()) {
				if (creativeType.equalsIgnoreCase(type.name())) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No Creative Type enum found for given Status name: " + creativeType);
	}
	
	/**
	 * Return map of Spec Type
	 * @return Map<String, String>
	 */
	public static Map<String, String> getSpecTypeMap() {
		final Map<String, String> specTypeMap = new TreeMap<String, String>();
		for (CreativeType creativeType : CreativeType.values()) {
			specTypeMap.put(creativeType.name(), creativeType.getDisplayValue());
		}
		return specTypeMap;
	}
}
