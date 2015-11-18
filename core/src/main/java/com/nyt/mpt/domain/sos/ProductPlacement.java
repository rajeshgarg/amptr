package com.nyt.mpt.domain.sos;

import java.io.Serializable;

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

/**
 * This <code>ProductPlacement</code> class includes all the attributes related
 * to ProductPlacement and their getter and setter. The attributes have mapping
 * with <code>PRODUCT_PLACEMENT</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Immutable
@Table(name = "PRODUCT_PLACEMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductPlacement implements Serializable {

	private static final long serialVersionUID = 1L;

	private long placementId;

	private boolean active;

	private SalesTarget salesTarget;

	private Product product;

	@Id
	@Column(name = "PLACEMENT_ID")
	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne(targetEntity = SalesTarget.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "SALESTARGET_ID", nullable = false, updatable = false, insertable = false)
	public SalesTarget getSalesTarget() {
		return salesTarget;
	}

	public void setSalesTarget(SalesTarget salesTarget) {
		this.salesTarget = salesTarget;
	}

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PRODUCT_ID", nullable = false, updatable = false, insertable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}