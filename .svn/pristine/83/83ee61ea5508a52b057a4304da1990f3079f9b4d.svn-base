/**
 * 
 */
package com.nyt.mpt.domain.sos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This <code>OrderContactAssociation</code> class includes all the attributes
 * related to Order Contact and their getter and setter. The attributes have
 * mapping with <code>CRM_ORDER_CONTACT</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "CRM_ORDER_CONTACT")
public class OrderContactAssociation {

	@Embeddable
	public static class Id implements Serializable {

		public Id() {
			super();
		}

		public Id(SalesOrder order, Long contactId) {
			this.order = order;
			this.contactId = contactId;
		}

		private static final long serialVersionUID = 1L;
		private SalesOrder order;
		private Long contactId;

		@ManyToOne
		@JoinColumn(name = "SALESORDER_ID", nullable = false, insertable = false, updatable = false)
		public SalesOrder getOrder() {
			return order;
		}

		public void setOrder(SalesOrder order) {
			this.order = order;
		}

		@Column(name = "CONTACT_ID")
		public Long getContactId() {
			return contactId;
		}

		public void setContactId(Long contactId) {
			this.contactId = contactId;
		}
	}

	private Id id;

	public OrderContactAssociation() {
		super();
	}

	@EmbeddedId
	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}
}
