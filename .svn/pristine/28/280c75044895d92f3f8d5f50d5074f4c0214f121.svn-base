package com.nyt.mpt.domain.sos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * This <code>ProductType</code> class includes all the attributes related to
 * ProductType and their getter and setter. The attributes have mapping with
 * <code>PRODUCT_TYPE</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Immutable
@Table(name = "PRODUCT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductType implements Serializable {

	private static final long serialVersionUID = 1L;

	private long productTypeId;

	private String productTypeName;

	private boolean active;

	@Id
	@Column(name = "PRODUCTTYPE_ID")
	public long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}

	@Column(name = "NAME", nullable = false)
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			ProductType other = (ProductType) obj;
			return new EqualsBuilder().append(this.productTypeId, other.productTypeId).append(this.productTypeName, other.productTypeName).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(productTypeId).append(productTypeName).hashCode();
	}
}
