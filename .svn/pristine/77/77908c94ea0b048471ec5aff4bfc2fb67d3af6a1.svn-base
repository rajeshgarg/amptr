/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.nyt.mpt.form.CalendarDetailForm;

/**
 * @author surendra.singh
 */
public class CalendarDetailComparator implements Comparator<CalendarDetailForm> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final CalendarDetailForm form1, final CalendarDetailForm form2) {
		if (form1 == null && form2 == null) {
			return 0;
		} else if (form1 == null || form2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(form1.getProposalId(), form2.getProposalId()).toComparison();
		}
	}
}
