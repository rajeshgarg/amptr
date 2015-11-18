/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This <code>Customer</code> class includes all the attributes related to
 * Customer and their getter and setter. The attributes have mapping with
 * <code>CRM_CUSTOMER</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "CRM_CUSTOMER")
public class Customer {

	private long customerId;

	private String customerName;

	@Id
	@Column(name = "CUSTOMER_ID", updatable = false, insertable = false)
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CUSTOMER_NAME", updatable = false, insertable = false)
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
