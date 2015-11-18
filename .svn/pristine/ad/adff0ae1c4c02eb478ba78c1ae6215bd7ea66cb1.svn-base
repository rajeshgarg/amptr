/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.nyt.mpt.form.CalendarLineItemForm;

/**
 * @author surendra.singh
 */
public class CalendarLineItemComparator implements Comparator<CalendarLineItemForm> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(CalendarLineItemForm form1, CalendarLineItemForm form2) {
		if (form1 == null && form2 == null) {
			return 0;
		} else if (form1 == null || form2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(form1.getLineItemID(), form2.getLineItemID()).toComparison();
		}
	}
}
