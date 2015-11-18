/**
 * 
 */
package com.nyt.mpt.form;

import com.nyt.mpt.domain.ProductCreativeAssoc;

/**
 * This form bean is used for product creativeAssoc information
 * 
 * @author surendra.singh
 * 
 */
public class ProductCreativeAssocForm extends BaseForm<ProductCreativeAssoc> {

	private String associationId;

	private String productId;

	private String creativeId;

	private String creativeName;

	private String creativeDescription;

	public String getAssociationId() {
		return associationId;
	}

	public void setAssociationId(final String associationId) {
		this.associationId = associationId != null ? associationId.trim() : associationId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId != null ? productId.trim() : productId;
	}

	public String getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(final String creativeId) {
		this.creativeId = creativeId != null ? creativeId.trim() : creativeId;
	}

	public String getCreativeName() {
		return creativeName;
	}

	public void setCreativeName(final String creativeName) {
		this.creativeName = creativeName != null ? creativeName.trim() : creativeName;
	}

	public String getCreativeDescription() {
		return creativeDescription;
	}

	public void setCreativeDescription(final String creativeDescription) {
		this.creativeDescription = creativeDescription != null ? creativeDescription.trim() : creativeDescription;
	}

	@Override
	public ProductCreativeAssoc populate(final ProductCreativeAssoc bean) {
		bean.setAssociationId(Long.valueOf(this.associationId));
		return bean;
	}

	@Override
	public void populateForm(final ProductCreativeAssoc bean) {
		this.associationId = String.valueOf(bean.getAssociationId());
		this.productId = String.valueOf(bean.getProductId());
		this.creativeId = String.valueOf(bean.getCreative().getCreativeId());
		this.creativeName = String.valueOf(bean.getCreative().getName());
		this.creativeDescription = bean.getCreative().getDescription();
	}
}
