/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This class is used for providing  Product Placement
 * @author amandeep.singh
 *
 */
@Entity
@Immutable
@Table(name = "PRODUCT_PLACEMENT")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductPlacement implements Serializable {

	private static final long serialVersionUID = 1L;

	private long placementId;
	
	private String status;
	
	private SalesTarget salesTarget;
	
	private Product product;
	
	private Date deleteDate;
	
	@Id
	@Column(name = "SOS_PLACEMENT_ID")
	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@ManyToOne(targetEntity = SalesTarget.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "SOS_SALES_TARGET_ID", nullable = false, updatable = false, insertable = false)
	public SalesTarget getSalesTarget() {
		return salesTarget;
	}
	
	public void setSalesTarget(SalesTarget salesTarget) {
		this.salesTarget = salesTarget;
	}

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "SOS_PRODUCT_ID", nullable = false, updatable = false, insertable = false)
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
