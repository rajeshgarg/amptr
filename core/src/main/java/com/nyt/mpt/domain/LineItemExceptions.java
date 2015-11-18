/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This <code>LineItemExceptions</code> class includes all the attributes
 * related to LineItem Exceptions and their getter and setter. The attributes
 * have mapping with <code>MP_LINE_ITEMS_EXCEPTIONS</code> table in the AMPT
 * database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_LINE_ITEMS_EXCEPTIONS")
public class LineItemExceptions implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private LineItem lineitemId;

	private String lineItemException;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXCEPTION_SEQUENCE")
	@SequenceGenerator(name = "EXCEPTION_SEQUENCE", sequenceName = "MP_LINE_ITEMS_EXCEPTIONS_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "LINEITEM_ID", nullable = true, insertable = true, updatable = false)
	public LineItem getLineitemId() {
		return lineitemId;
	}

	public void setLineitemId(LineItem lineitemId) {
		this.lineitemId = lineitemId;
	}

	@Column(name = "LINEITEM_EXCEPTION")
	public String getLineItemException() {
		return lineItemException;
	}

	public void setLineItemException(String lineItemException) {
		this.lineItemException = lineItemException;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(lineItemException).hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			LineItemExceptions other = (LineItemExceptions) obj;
			return new EqualsBuilder().append(this.id, other.getId()).append(this.lineItemException, other.getLineItemException()).isEquals();
		}
		return false;
	}

}
