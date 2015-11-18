/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>ProductCreativeAssoc</code> class includes all the attributes
 * related to ProductCreative and their getter and setter. The attributes have
 * mapping with <code>MP_PRODUCT_CREATIVE_ASSOC</code> table in the AMPT
 * database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_PRODUCT_CREATIVE_ASSOC")
public class ProductCreativeAssoc extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long associationId;

	private long productId;

	private Creative creative;

	private Product product;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_CREATIVE_ASSOC_SEQ")
	@SequenceGenerator(name = "PRODUCT_CREATIVE_ASSOC_SEQ", sequenceName = "MP_PRODUCT_CREATIVE_ASSOC_SEQ", allocationSize = 1)
	public long getAssociationId() {
		return associationId;
	}

	public void setAssociationId(long associationId) {
		this.associationId = associationId;
	}

	@Column(name = "SOS_PRODUCT_ID", nullable = false)
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "creative_id", nullable = false, insertable = true, updatable = false)
	public Creative getCreative() {
		return creative;
	}

	public void setCreative(Creative creative) {
		this.creative = creative;
	}

	@Transient
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
