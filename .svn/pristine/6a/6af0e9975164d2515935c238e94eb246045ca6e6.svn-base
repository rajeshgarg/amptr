/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 *This class is used for providing  Product Type
 * @author amandeep.singh
 *
 */
@Entity
@Immutable
@Table(name = "PRODUCT_TYPE")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long productTypeId;
	
	private String productTypeName;
	
	private Date deleteDate;

	@Id
	@Column(name = "SOS_PRODUCT_TYPE_ID")
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
	
	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			ProductType other = (ProductType) obj;
			return new EqualsBuilder()
					.append(this.productTypeId, other.productTypeId).append(
							this.productTypeName, other.productTypeName).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(productTypeId).append(
				productTypeName).hashCode();
	}
}
