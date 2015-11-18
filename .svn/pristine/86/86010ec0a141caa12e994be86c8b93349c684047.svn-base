/**
 * 
 */
package com.nyt.mpt.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Provide FilterCriteria for Sorting
 * 
 * @author surendra.singh
 * 
 */
public class SortingCriteria {

	/**
	 * Sorting order (asc or desc)
	 */
	private String sortingOrder;

	/**
	 * Sorting field name
	 */
	private String sortingField;

	public SortingCriteria(String sortingField, String sortingOrder) {
		this.sortingField = sortingField;
		this.sortingOrder = sortingOrder;
	}

	public String getSortingOrder() {
		return sortingOrder;
	}

	public void setSortingOrder(String sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	public String getSortingField() {
		return sortingField;
	}

	public void setSortingField(String sortingField) {
		this.sortingField = sortingField;
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new CacheSupportToStringStyle()).toString();
	}
}
