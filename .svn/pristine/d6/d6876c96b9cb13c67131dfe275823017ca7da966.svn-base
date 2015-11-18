package com.nyt.mpt.template;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nyt.mpt.domain.TemplateMetaDataAttributes;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This <code>TemplateAttributeVO</code> contains an attribute data and includes
 * those methods which gives details of <tt>Template</tt> cell.
 * 
 * @author manish.kesarwani
 */
public class TemplateAttributeVO implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(TemplateAttributeVO.class);

	private static final int rowDifference = 1;

	private static final int colDifference = 1;

	private static final long serialVersionUID = 1L;

	private String attributeName;

	private String attributeType;

	private String attributeTypeKey;

	private String attributeValue;

	private int rowNum;

	private int colNum;

	private String format;

	private Long fontSize;

	private Long fontWeight;

	private Long templateMetaDataAttributesID = 0L;

	private Long attributeTypeKeyId;

	private boolean proposalType;
	
	private Map<Long, String> displayAttributMap = new HashMap<Long, String>();

	/**
	 * Returns {@link TemplateAttributeVO}, {@link TemplateMetaDataAttributes}
	 * populates there data in {@link TemplateAttributeVO} i.e. row and column
	 * number, font size, format of cell, attribute name etc.
	 * 
	 * @param mediaPlanAttributeVo
	 *            {@link TemplateAttributeVO}
	 * @param mediaPlanAttributes
	 *            {@link TemplateMetaDataAttributes}
	 * @return {@link TemplateAttributeVO}, {@link TemplateMetaDataAttributes}
	 *         populates there data in {@link TemplateAttributeVO}.
	 */
	public TemplateAttributeVO populate(TemplateAttributeVO mediaPlanAttributeVo, TemplateMetaDataAttributes mediaPlanAttributes) {
		mediaPlanAttributeVo.setAttributeName(mediaPlanAttributes.getAttributeName());
		mediaPlanAttributeVo.setAttributeType(mediaPlanAttributes.getProposalHeadAttributes() == null ? ConstantStrings.EMPTY_STRING : mediaPlanAttributes.getProposalHeadAttributes()
				.getProposalHead().getHeadName());
		mediaPlanAttributeVo.setAttributeTypeKey(mediaPlanAttributes.getProposalHeadAttributes() == null ? ConstantStrings.EMPTY_STRING : mediaPlanAttributes.getProposalHeadAttributes().getAttributeName());
		mediaPlanAttributeVo.setRowNum(mediaPlanAttributes.getRowNum() - rowDifference);
		mediaPlanAttributeVo.setColNum(getCellNumberBasedOnCellName(mediaPlanAttributes.getColNum()) - colDifference);
		mediaPlanAttributeVo.setFormat(mediaPlanAttributes.getFormat());
		mediaPlanAttributeVo.setFontSize(mediaPlanAttributes.getFontSize());
		return mediaPlanAttributeVo;
	}

	/**
	 * Calculate cell number based on cell name of .xlsx file.
	 * 
	 * @param cellName
	 *            Name of the cell in .xlsx file.
	 * @return cell number of .xlsx file.
	 */
	public int getCellNumberBasedOnCellName(String cellName) {
		char[] characters = cellName.toUpperCase().toCharArray();
		int sum = 0;
		for (int i = 0; i < characters.length; i++) {
			sum *= 26;
			sum += (characters[i] - 'A' + 1);
		}
		return sum;
	}

	/**
	 * Returns value of @param key (attribute) from @param createdObject.
	 * 
	 * @param key
	 *            Name of the variable whose method want to invoke.
	 * @param className
	 *            Name of the class from where system want all the methods (e.g com.nyt.mpt.domain.Proposal).
	 * @param createdObject
	 *            This object contain all the data. (e.g {@link Proposal} or
	 *            {@link LineItem} etc})
	 * @return value of @param key (attribute) from @param createdObject.
	 */
	@SuppressWarnings("rawtypes")
	public String populateKeyValueFromObject(String key, String className, Object createdObject) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Populating Key Value From Object with Key: " + key + "Class Name: " + className + "Created Object: " + createdObject);
		}
		String value = ConstantStrings.EMPTY_STRING;
		try {
			/*Based on class name get class Object*/
			Class c = Class.forName(className);
			/*From the class object get all inside method*/
			Method[] methods = c.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				/*Check for getter method*/
				if (methods[i].getName().equalsIgnoreCase("get" + key)) {
					Object[] arguments = null;
					/*
					 * Invokes the underlying method represented by this Method
					 * object, on the specified object with the specified
					 * parameters. Individual parameters are automatically
					 * unwrapped to match primitive formal parameters, and both
					 * primitive and reference parameters are subject to method
					 * invocation conversions as necessary.
					 */
					Object obj = methods[i].invoke(createdObject, arguments);
					/*Set value of attribute*/
					if (obj != null) {
						value = obj.toString();
					} else {
						value = ConstantStrings.EMPTY_STRING;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception has occurred: " + e);
		}
		return value;
	}

	/**
	 * Calculate cell name based on cell number of .xlsx file.
	 * 
	 * @param colNum
	 *            Number of the cell in .xlsx file.
	 * @return Cell name of .xlsx file.
	 */
	public static String getCellNameFromCellNumber(int colNum) {
		int Base = 26;
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String colName = "";
		while (colNum > 0) {
			int position = colNum % Base;
			/*the char value at the specified index of this string. The first char value is at index 0. */
			colName = (position == 0 ? 'Z' : chars.charAt(position > 0 ? position - 1 : 0)) + colName;
			colNum = (colNum - 1) / Base;
		}
		return colName;
	}

	/**
	 * Returns the data type of the key. And this key is the part of @param
	 * className.
	 * 
	 * @param key
	 *            Name of the variable whose data type want to know.
	 * @param className
	 *            Name of the class who contain all the key.
	 * @return the data type of the key.
	 */
	@SuppressWarnings("rawtypes")
	public String populateKeyType(String key, String className) {
		String keyType = ConstantStrings.EMPTY_STRING;
		try {
			Class c = Class.forName(className);
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equalsIgnoreCase(key)) {
					Type modifierType = fields[i].getType();
					keyType = modifierType.toString();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception has occurred: " + e);
		}
		return keyType;
	}

	/**
	 * According to the data type method returns .xlsx cell format type.
	 * 
	 * @param format
	 *            Data type of attribute (i.e also called key)
	 * @return .xlsx cell format type based on data type.
	 */
	public String getFormat(String format) {
		String formatType = ConstantStrings.EMPTY_STRING;
		if (format.contains("Long") || format.contains("int") || format.contains("Integer")) {
			formatType = ConstantStrings.NUMBER;
		} else if (format.contains("Date")) {
			formatType = ConstantStrings.DATE;
		} else if (format.contains("Double") || format.contains("double")) {
			formatType = ConstantStrings.CURRENCY;
		} else {
			formatType = ConstantStrings.TEXT;
		}
		return formatType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName == null ? attributeName : attributeName.trim();
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeTypeKey() {
		return attributeTypeKey;
	}

	public void setAttributeTypeKey(String attributeTypeKey) {
		this.attributeTypeKey = attributeTypeKey;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	public Long getFontSize() {
		return fontSize;
	}

	public void setFontSize(Long fontSize) {
		this.fontSize = fontSize;
	}

	public Long getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(Long fontWeight) {
		this.fontWeight = fontWeight;
	}

	public Long getTemplateMetaDataAttributesID() {
		return templateMetaDataAttributesID;
	}

	public void setTemplateMetaDataAttributesID(final Long templateMetaDataAttributesID) {
		this.templateMetaDataAttributesID = templateMetaDataAttributesID;
	}

	public Long getAttributeTypeKeyId() {
		return attributeTypeKeyId;
	}

	public void setAttributeTypeKeyId(Long attributeTypeKeyId) {
		this.attributeTypeKeyId = attributeTypeKeyId;
	}

	public void setProposalType(boolean proposalType) {
		this.proposalType = proposalType;
	}

	public boolean isProposalType() {
		return proposalType;
	}

	/**
	 * @return the displayAttributMap
	 */
	public Map<Long, String> getDisplayAttributMap() {
		return displayAttributMap;
	}

	/**
	 * @param displayAttributMap the displayAttributMap to set
	 */
	public void setDisplayAttributMap(Map<Long, String> displayAttributMap) {
		this.displayAttributMap = displayAttributMap;
	}	
}
