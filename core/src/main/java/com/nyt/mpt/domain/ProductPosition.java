/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * Used to capture product position information
 * 
 * @author manish.kesarwani
 * 
 */
@Entity
@Immutable
@Table(name = "PRODUCT_POSITION")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductPosition implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProductPositionId productPositionId;

	private Date createdDate;

	private Date deletedDate;

	private Product product;

	private Position position;

	public ProductPosition() {
		super();
	}

	@EmbeddedId
	@AttributeOverrides( { @AttributeOverride(name = "sosProductId", column = @Column(name = "SOS_PRODUCT_ID")),
			@AttributeOverride(name = "position", column = @Column(name = "POSITION")) })
	public ProductPositionId getProductPositionId() {
		return productPositionId;
	}

	public void setProductPositionId(ProductPositionId productPositionId) {
		this.productPositionId = productPositionId;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	@ManyToOne
	@JoinColumn(name = "SOS_PRODUCT_ID", insertable = false, updatable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	@JoinColumn(name = "ADX_POSITION_ID", insertable = false, updatable = false)
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}