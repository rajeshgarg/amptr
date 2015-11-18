/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.sos.LineItemTargeting;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.TargetElement;

/**
 * This class contains the String Utility
 *
 * @author amandeep.singh
 */
public final class StringUtil {

	private StringUtil() {
		super();
	}

	/**
	 * This method will escape the %, _ special characters and remove the ' with '' for SQL
	 *
	 * @param inputString
	 * @return
	 */
	public static String escapeSqlCharacters(final String inputString) {
		if (StringUtils.isBlank(inputString)) {
			return inputString;
		} else {
			final String string = StringEscapeUtils.escapeSql(inputString);
			if (string.contains("%")) {
				return string.replace("%", "\\%");
			}
			return string;
		}
	}

	/**
	 * This method will removed extra spaces exist before or after a semicolon
	 *
	 * @param inputString
	 * @return
	 */
	public static String trimSpacesForCommaSeperatedValues(final String inputString) {
		if (StringUtils.isBlank(inputString)) {
			return inputString;
		} else {
			return inputString.replaceAll(" *, *", ",");
		}
	}

	/**
	 * Generate Campaign name for a proposal using Time Stamp
	 *
	 * @param inputString
	 * @return
	 */
	public static String generateCampaignName(final String inputString) {
		if (StringUtils.isBlank(inputString)) {
			return String.valueOf(DateUtil.getCurrentDate().getTime());
		} else {
			if (inputString.length() > 46) {
				return inputString.substring(0, 46) + ConstantStrings.UNDER_SCORE + DateUtil.getCurrentDate().getTime();
			}
			return inputString + ConstantStrings.UNDER_SCORE + DateUtil.getCurrentDate().getTime();
		}
	}
	
	/**
	 * Generate proposal name while cloning using (Advertiser,sales category,due date)
	 * 
	 * @param CampaignName
	 * @param advertiserName
	 * @param dueDate
	 * @param salesCategoryName
	 * @return
	 */
	public static String generateProposalName(final String CampaignName, final String advertiserName, final Date dueDate, final String salesCategoryName){
		String proposalName=ConstantStrings.EMPTY_STRING;
		String newdueDate=DateUtil.getGuiDateString(dueDate);
		if(StringUtils.isNotBlank(advertiserName)){
		 proposalName= advertiserName + ConstantStrings.UNDER_SCORE + CampaignName + ConstantStrings.UNDER_SCORE +newdueDate ;
		}else{
		 proposalName= salesCategoryName + ConstantStrings.UNDER_SCORE + CampaignName + ConstantStrings.UNDER_SCORE + newdueDate ;
		}	
		return proposalName;		
	}

	/**
	 * Generate Package name from given name for cloning
	 * @param name
	 * @return
	 */
	public static String generatePackageName(final String name) {
		if (StringUtils.isBlank(name)) {
			return name;
		} else {
			if (name.length() > 46) {
				return name.substring(0, 46) + ConstantStrings.UNDER_SCORE + System.currentTimeMillis();
			}
			return name + ConstantStrings.UNDER_SCORE + DateUtil.getCurrentDate().getTime();
		}
	}
	
	/**
	 * Convert string array to long array
	 * @return
	 */
	public static Long[] convertStringArrayToLongArray(final String[] stringArr) {
		Long[] longArray = null;
		if(stringArr != null) {
			longArray = new Long[stringArr.length];
			for (int i = 0; i < stringArr.length; i++) {
				longArray[i] = Long.valueOf(stringArr[i]);
			}
		}
		return longArray;
	}
	
	/**
	 * Convert string to long array
	 * @return
	 */
	public static List<Long> convertStringToLongList(final String string) {
		final List<Long> itemList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(string)) {
			String[] itemArray = string.split(ConstantStrings.COMMA);
			for (int i = 0; i < itemArray.length; i++) {
				itemList.add(Long.valueOf(itemArray[i]));
			}
		}
		return itemList;
	}
	
	/**
	 * @param orderLineItem
	 * @return
	 */
	public static String getTargetingStringForOrder(final OrderLineItem orderLineItem) {
		if (orderLineItem.getLineItemTargeting() != null && !orderLineItem.getLineItemTargeting().isEmpty()) {
			final Map<String, String> targetingMap = new LinkedHashMap<String, String>();
			for (LineItemTargeting targeting : orderLineItem.getLineItemTargeting()) {
				final TargetElement element = targeting.getTargetElement();
				if (targetingMap.get(element.getTargetType().getName()) == null) {
					targetingMap.put(element.getTargetType().getName(), element.getElementName());
				} else {
					targetingMap.put(element.getTargetType().getName(), targetingMap.get(element.getTargetType().getName()) + ", " + element.getElementName());
				}
			}
			return getTargetingString(targetingMap);
		}
		return ConstantStrings.EMPTY_STRING;
	}
	
	/**
	 * @param targetingMap
	 * @return
	 */
	public static String getTargetingString(final Map<String, String> targetingMap) {
		int counter = 0;
		final StringBuilder builder = new StringBuilder();
		builder.append(ConstantStrings.OPENING_BRACKET);
		for (String tarElement : targetingMap.keySet()) {
			if (counter > 0) {
				builder.append(" with ");
				builder.append(tarElement).append(" in ").append(targetingMap.get(tarElement));
				counter++;
			} else {
				builder.append(tarElement).append(" in ").append(targetingMap.get(tarElement)).append(ConstantStrings.CLOSING_BRACKET);
				counter++;
			}
		}
		return builder.toString();
	}
	
	/**
	 * Create a new list by copying all item from array
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> List<T> getListFromArray(final T []array) {
		final List<T> list = new ArrayList<T>();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
		return list;
	}
}
