package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class is used as composite key for Product Position
 * @author manish.kesarwani
 *
 */
@Embeddable
public class ProductPositionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long sosProductId;
	
	private String position;
	
	public ProductPositionId() {
		
	}
	
	@Column(name = "SOS_PRODUCT_ID")
	public long getSosProductId() {
		return sosProductId;
	}
	public void setSosProductId(long sosProductId) {
		this.sosProductId = sosProductId;
	}
	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(position).append(sosProductId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			ProductPositionId other = (ProductPositionId) obj;
			return new EqualsBuilder().append(this.position, other.position).append(this.sosProductId, other.sosProductId).isEquals();
		}
		return false;
	}
}
