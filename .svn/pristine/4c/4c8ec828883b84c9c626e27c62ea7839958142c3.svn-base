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
 * This <code>ProductAttributeAssoc</code> class includes all the attributes
 * related to ProductAttribute and their getter and setter. The attributes have
 * mapping with <code>MP_PRODUCT_ATTRIBUTE_ASSOC</code> table in the AMPT
 * database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_PRODUCT_ATTRIBUTE_ASSOC")
public class ProductAttributeAssoc extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long associationId;

	private long productId;

	private Long salesTargetId;

	private String attributeValue;

	private Attribute attribute;

	private Product product;

	private SalesTarget salesTarget;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_ATTRIBUTE_ASSOC_SEQ")
	@SequenceGenerator(name = "PRODUCT_ATTRIBUTE_ASSOC_SEQ", sequenceName = "MP_PRODUCT_ATTRIBUTE_ASSOC_SEQ", allocationSize = 1)
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

	@Column(name = "ATTRIBUTE_VALUE", nullable = false)
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTRIBUTE_ID", nullable = false, insertable = true, updatable = true)
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(name = "SOS_SALES_TARGT_ID", nullable = true)
	public Long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(Long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Transient
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transient
	public SalesTarget getSalesTarget() {
		return salesTarget;
	}

	public void setSalesTarget(SalesTarget salesTarget) {
		this.salesTarget = salesTarget;
	}
}
