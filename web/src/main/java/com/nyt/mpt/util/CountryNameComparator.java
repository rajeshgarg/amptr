/**
 * 
 */
package com.nyt.mpt.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author garima.garg
 *
 */
public class CountryNameComparator implements Comparator<KeyValuePairPojo>{
	
	@Override
	public int compare(final KeyValuePairPojo countryFormo1, final KeyValuePairPojo countryFormo2) {
		if (countryFormo1 == null && countryFormo2 == null) {
			return 0;
		} else if (countryFormo1 == null || countryFormo2 == null) {
			return -1;
		} else {
			return new CompareToBuilder().append(countryFormo1.getName(), countryFormo2.getName()).toComparison();
		}
	}
}
