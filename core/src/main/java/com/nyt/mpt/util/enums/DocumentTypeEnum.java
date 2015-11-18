/**
 * 
 */
package com.nyt.mpt.util.enums;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * This <code>DocumentTypeEnum</code> is used for document types like doc, excel, ppt etc
 * 
 * @author surendra.singh
 */

public enum DocumentTypeEnum {
	/** 
	 * This is used for doc type document 
	 */
	DOC {

		@Override
		public String getDisplayName() {
			return "Doc";
		}
	},
	/** 
	 * This is used for excel type document  
	 */
	EXCEL {

		@Override
		public String getDisplayName() {
			return "Excel";
		}
	},
	/** 
	 * This is used for ppt type document 
	 */
	PPT {

		@Override
		public String getDisplayName() {
			return "PPT";
		}
	},
	/** 
	 * This is used for pdf type document 
	 */
	PDF {

		@Override
		public String getDisplayName() {
			return "PDF";
		}
	},
	/** 
	 * This is used for jpg type document 
	 */
	JPG {
		@Override
		public String getDisplayName() {
			return "Jpg";
		}
	},
	/** 
	 * This is used for png type document 
	 */
	PNG {
		@Override
		public String getDisplayName() {
			return "Png";
		}
	},
	/** 
	 * This is used for gif type document 
	 */
	GIF {
		@Override
		public String getDisplayName() {
			return "Gif";
		}
	},
	/** 
	 * This is used for bmp type document 
	 */
	BMP {
		@Override
		public String getDisplayName() {
			return "Bmp";
		}
	};

	private static final Map<String, DocumentTypeEnum> DOC_EXT_TYPE_MAP = new HashMap<String, DocumentTypeEnum>();
	
	static {
		DOC_EXT_TYPE_MAP.put(".doc", DocumentTypeEnum.DOC);
		DOC_EXT_TYPE_MAP.put(".docx", DocumentTypeEnum.DOC);
		DOC_EXT_TYPE_MAP.put(".xls", DocumentTypeEnum.EXCEL);
		DOC_EXT_TYPE_MAP.put(".xlsx", DocumentTypeEnum.EXCEL);
		DOC_EXT_TYPE_MAP.put(".ppt", DocumentTypeEnum.PPT);
		DOC_EXT_TYPE_MAP.put(".pptx", DocumentTypeEnum.PPT);
		DOC_EXT_TYPE_MAP.put(".pdf", DocumentTypeEnum.PDF);
		DOC_EXT_TYPE_MAP.put(".jpg", DocumentTypeEnum.JPG);
		DOC_EXT_TYPE_MAP.put(".jpeg", DocumentTypeEnum.JPG);
		DOC_EXT_TYPE_MAP.put(".png", DocumentTypeEnum.PNG);
		DOC_EXT_TYPE_MAP.put(".gif", DocumentTypeEnum.GIF);
		DOC_EXT_TYPE_MAP.put(".bmp", DocumentTypeEnum.BMP);
	}
	
	public abstract String getDisplayName();

	public static DocumentTypeEnum findByDisplayName(String displayName) {
		if (StringUtils.isNotBlank(displayName)) {
			for (DocumentTypeEnum enm : DocumentTypeEnum.values()) {
				if (enm.getDisplayName().equalsIgnoreCase(displayName)) {
					return enm;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the {@link DocumentTypeEnum} by <code>name</code>
	 * @param name
	 * @return
	 */
	public static DocumentTypeEnum findByName(String name) {
		if (StringUtils.isNotBlank(name)) {
			for (DocumentTypeEnum enm : DocumentTypeEnum.values()) {
				if (enm.name().equalsIgnoreCase(name)) {
					return enm;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns {@link DocumentTypeEnum} by <code>fileName</code> 
	 * @param 	fileName
	 * 			Name of the file for which we've to get the {@link DocumentTypeEnum}
	 * @return
	 */
	public static DocumentTypeEnum getDocType(final String fileName) {
		return DOC_EXT_TYPE_MAP.get(fileName.substring(fileName.lastIndexOf(".")).toLowerCase());
	}
	
	/**
	 * Returns <code>true</code> if, and only if, the <code>fileName</code> has the valid Extension
	 * @param 	fileName
	 * 			Name of the file for which we've to check the valid Extension
	 * @return
	 */
	public static boolean isAllowedDocumentType(final String fileName) {
		if (getDocType(fileName) == null) {
			return false;
		}
		return true;
	}
}
