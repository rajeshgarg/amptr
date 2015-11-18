package com.nyt.mpt.template;

import org.apache.commons.lang.StringUtils;

/**
 * Represent excel cell value format
 * 
 * @author manish.kesarwani
 *
 */
public enum TemplateValueFormateType {
	/** 
	 * Represent excel template's cell type is Currency
	 */
	CURRENCY {
		@Override
		public String getDisplayName() {
			return "Currency";
		}
		
		@Override
		public String getFormat() {
			return "#,##0.00";
		}

		@Override
		public boolean isDateType() {
			return false;
		}

		@Override
		public boolean isNumericType() {
			return true;
		}
	},
	/** 
	 * Represent excel template's cell type is Date
	 */
	DATE {
		@Override
		public String getDisplayName() {
			return "Date";
		}
		
		@Override
		public String getFormat() {
			return "MM/dd/yyyy";
		}
		
		@Override
		public boolean isDateType() {
			return true;
		}

		@Override
		public boolean isNumericType() {
			return false;
		}
	},
	/** 
	 * Represent excel template's cell type is Number
	 */
	NUMBER {
		@Override
		public String getDisplayName() {
			return "Number";
		}
		
		@Override
		public String getFormat() {
			return "#,##0";
		}
		
		@Override
		public boolean isDateType() {
			return false;
		}

		@Override
		public boolean isNumericType() {
			return true;
		}
	},
	/** 
	 * Represent excel template's cell type is Text
	 */
	TEXT {
		@Override
		public String getDisplayName() {
			return "Text";
		}
		
		@Override
		public String getFormat() {
			return "Text";
		}
		
		@Override
		public boolean isDateType() {
			return false;
		}

		@Override
		public boolean isNumericType() {
			return false;
		}
	};

	public abstract String getDisplayName();
	
	public abstract String getFormat();
	
	public abstract boolean isNumericType();
	
	public abstract boolean isDateType();
	
	public static TemplateValueFormateType findByName(String formatType) {
		if(StringUtils.isNotBlank(formatType)){
			for (TemplateValueFormateType type : TemplateValueFormateType.values()) {
				if(formatType.equalsIgnoreCase(type.name())){
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No Format Type enum found for given format name: "+formatType);
	}
}
