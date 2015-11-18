/**
 * 
 */
package com.nyt.mpt.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class is used for sorting Component.
 * 
 * @author surendra.singh
 * @param <T>
 */
public class SortingUtil<T> {

	private static final Logger logger = Logger.getLogger(SortingUtil.class);

	private static final String DESC_ORDER = "DESC";

	public void sortComponent(final List<T> list, final String sortingString, final String sortingOrder) {
		sortComponent(list, sortingString, sortingOrder, false);
	}

	public void sortComponent(final List<T> list, final String sortingString, final String sortingOrder, final boolean isDateSort) {
		final int position = 0;
		if (StringUtils.isEmpty(sortingString)) {
			return;
		}
		if (list == null) {
			return;
		}
		if (list.isEmpty()) {
			return;
		}

		Collections.sort(list, new Comparator<T>() {
			@SuppressWarnings("unchecked")
			public int compare(final T component1, final T component2) {
				int result = 0;
				try {
					final String getterMethod = "get" + startCharInUpperCase(sortingString, position);
					final Method methods1 = component1.getClass().getMethod(getterMethod);
					final Method methods2 = component2.getClass().getMethod(getterMethod);
					if (isDateSort) {
						final String secondParam = (String) methods2.invoke(component2);
						final String firstParam = (String) methods1.invoke(component1);
						Date firstDate = null;
						Date secondDate = null;
						if (StringUtils.isNotEmpty(secondParam) && StringUtils.isNotEmpty(firstParam)) {
							firstDate = DateUtil.parseToDate(firstParam);
							secondDate = DateUtil.parseToDate(secondParam);

							if (sortingOrder != null && sortingOrder.toUpperCase().startsWith(DESC_ORDER)) {
								result = secondDate.compareTo(firstDate);
							} else {
								result = firstDate.compareTo(secondDate);
							}
						} else if (StringUtils.isNotEmpty(secondParam) && sortingOrder != null && sortingOrder.toUpperCase().startsWith(DESC_ORDER)) {
							return 1;
						} else if (StringUtils.isNotEmpty(secondParam)) {
							return -1;
						} else if (StringUtils.isNotEmpty(firstParam) && sortingOrder != null && sortingOrder.toUpperCase().startsWith(DESC_ORDER)) {
							return -1;
						} else if (StringUtils.isNotEmpty(firstParam)) {
							return 1;
						}
					} else {
						if (sortingOrder != null && sortingOrder.toUpperCase().startsWith(DESC_ORDER)) {
							final Comparable<T> comparable = (Comparable<T>) methods2.invoke(component2);
							result = comparable.compareTo((T) methods1.invoke(component1));

							if (comparable.getClass().equals(java.lang.String.class)) {
								final String param2 = (String) methods2.invoke(component2);
								final String param1 = (String) methods1.invoke(component1);
								result = param2.toLowerCase().compareTo(param1.toLowerCase());
							}
						} else {
							final Comparable<T> comparable = (Comparable<T>) methods1.invoke(component1);
							result = comparable.compareTo((T) methods2.invoke(component2));
							if (comparable.getClass().equals(java.lang.String.class)) {
								final String param2 = (String) methods2.invoke(component2);
								final String param1 = (String) methods1.invoke(component1);
								result = param1.toLowerCase().compareTo(param2.toLowerCase());
							}
						}
					}
					return result;
				} catch (Exception e) {
					logger.error("Exception:", e);
					new RuntimeException(e);
				}
				return result;
			}
		});
	}

	public static String startCharInUpperCase(final String s, final int pos) {
		char c = s.charAt(pos);
		c = Character.toUpperCase(c);
		return c + s.substring(pos + 1);
	}
}