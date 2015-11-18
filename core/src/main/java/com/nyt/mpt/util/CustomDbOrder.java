/**
 * 
 */
package com.nyt.mpt.util;

import org.hibernate.criterion.Order;

/**
 * This class is used to create Order of customer.
 * 
 * @author surendra.singh
 * 
 */
public class CustomDbOrder {

	public static final String ASCENDING = "asc";

	public static final String DESCENDING = "desc";

	private String fieldName;

	private boolean ascending;

	private boolean ignoreCase = true;

	public CustomDbOrder() {
		super();
	}

	public Order getOrder() {
		Order order = null;
		if (ascending) {
			order = Order.asc(this.fieldName);
		} else {
			order = Order.desc(this.fieldName);
		}
		return ignoreCase ? order.ignoreCase() : order;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	@Override
	public String toString() {
		return " ORDER BY " + (ignoreCase ? "Lower(" + this.fieldName + ") " : this.fieldName) + " " + (ascending ? ASCENDING : DESCENDING);
	}
}
