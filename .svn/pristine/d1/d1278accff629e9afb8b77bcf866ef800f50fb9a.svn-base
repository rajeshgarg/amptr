/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>LineItemTargeting</code> class includes all the attributes related
 * to LineItem Targeting and their getter and setter. The attributes have
 * mapping with <code>TAR_LINEITEM_ELEMENTS</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "TAR_LINEITEM_ELEMENTS")
public class LineItemTargeting {

	private long lineItemElementId;
	private TargetElement targetElement;
	private OrderLineItem orderLineItem;
	private Long sosUserId;
	private Long statusId = 1L;

	@Id
	@Column(name = "LINEITEMELEMENT_ID", updatable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINEITEM_TARGET_SEQUENCE")
	@SequenceGenerator(name = "LINEITEM_TARGET_SEQUENCE", sequenceName = "S_LINEITEMELEMENTS_ID", allocationSize = 1)
	public long getLineItemElementId() {
		return lineItemElementId;
	}

	public void setLineItemElementId(long lineItemElementId) {
		this.lineItemElementId = lineItemElementId;
	}

	@ManyToOne
	@JoinColumn(name = "LINEITEM_ID", nullable = false, updatable = false)
	public OrderLineItem getOrderLineItem() {
		return orderLineItem;
	}

	public void setOrderLineItem(OrderLineItem lineItem) {
		this.orderLineItem = lineItem;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ELEMENT_ID", nullable = false, updatable = false)
	public TargetElement getTargetElement() {
		return targetElement;
	}

	public void setTargetElement(TargetElement targetElement) {
		this.targetElement = targetElement;
	}

	@Column(name = "SOS_USER_ID", updatable = false)
	public Long getSosUserId() {
		return sosUserId;
	}

	public void setSosUserId(Long sosUserId) {
		this.sosUserId = sosUserId;
	}

	@Column(name = "STATUS", updatable = false)
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
}
