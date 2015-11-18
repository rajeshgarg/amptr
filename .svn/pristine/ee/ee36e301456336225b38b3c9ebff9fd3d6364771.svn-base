/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.nyt.mpt.domain.Attribute;

/**
 * This class is used to compare attribute based on Id.
 * 
 * @author Manish.Kesarwani
 */
public class AttributeIdComparator implements Comparator<Attribute> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Attribute attribute1, final Attribute attribute2) {
		if (attribute1 == null && attribute2 == null) {
			return 0;
		} else if (attribute1 == null || attribute2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(attribute1.getAttributeId(), attribute2.getAttributeId()).toComparison();
		}
	}

}
