/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.nyt.mpt.form.SalesTargetAmptForm;

/**
 * @author rakesh.tewari
 * 
 */
public class SalesTargeDisNameComparator implements Comparator<SalesTargetAmptForm> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final SalesTargetAmptForm form1, final SalesTargetAmptForm form2) {
		if (form1 == null && form2 == null) {
			return 0;
		} else if (form1 == null || form2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(form1.getSalesTargeDisplayName(), form2.getSalesTargeDisplayName()).toComparison();
		}
	}
}
