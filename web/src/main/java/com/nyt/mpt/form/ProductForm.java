/**
 * 
 */
package com.nyt.mpt.form;

import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductClass;
import com.nyt.mpt.domain.ProductType;

/**
 * This form bean is used for product information
 * 
 * @author surendra.singh
 * 
 */
public class ProductForm extends BaseForm<Product> {

	private long productId;
	private String productName;
	private String productDescription;
	private String typeName;
	private String className;
	private String note;
	private String displayName;
	private long placementId;
	private long salestargetId;
	private String salestargetName;
	private String placementPosition;
	private boolean hasDocument;
	private String creativeName;
	private long lineItemID;
	private String validFor;
	private String guarnteedImpressions;
	private String sov;
	private String validForUnit;

	private String[] sosSalesTargetId;
	private String creatives;
	private long creativesLength;
	private String reservable;
	private String productType;
	private String isViewable;
	
	public long getCreativesLength() {
		return creativesLength;
	}

	public void setCreativesLength(final long creativesLength) {
		this.creativesLength = creativesLength;
	}

	public String getCreatives() {
		return creatives;
	}

	public void setCreatives(final String creatives) {
		this.creatives = creatives;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(final long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(final String productName) {
		this.productName = productName == null ? productName : productName.trim();
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(final String productDescription) {
		this.productDescription = productDescription == null ? productDescription : productDescription.trim();
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(final String typeName) {
		this.typeName = typeName == null ? typeName : typeName.trim();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className == null ? className : className.trim();
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note == null ? note : note.trim();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName == null ? displayName : displayName.trim();
	}

	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(final long placementId) {
		this.placementId = placementId;
	}

	public long getSalestargetId() {
		return salestargetId;
	}

	public void setSalestargetId(final long salestargetId) {
		this.salestargetId = salestargetId;
	}

	public String getSalestargetName() {
		return salestargetName;
	}

	public void setSalestargetName(final String salestargetName) {
		this.salestargetName = salestargetName == null ? salestargetName : salestargetName.trim();
	}

	public String getPlacementPosition() {
		return placementPosition;
	}

	public void setPlacementPosition(final String placementPosition) {
		this.placementPosition = placementPosition == null ? placementPosition : placementPosition.trim();
	}

	public String getCreativeName() {
		return creativeName;
	}

	public void setCreativeName(final String creativeName) {
		this.creativeName = creativeName;
	}

	public String getValidFor() {
		return validFor;
	}

	public void setValidFor(String validFor) {
		this.validFor = validFor;
	}

	public String getValidForUnit() {
		return validForUnit;
	}

	public void setValidForUnit(String validForUnit) {
		this.validForUnit = validForUnit;
	}

	public String getGuarnteedImpressions() {
		return guarnteedImpressions;
	}

	public void setGuarnteedImpressions(final String guarnteedImpressions) {
		this.guarnteedImpressions = guarnteedImpressions == null ? guarnteedImpressions : guarnteedImpressions.trim();
	}

	public String getSov() {
		return sov;
	}

	public void setSov(String sov) {
		this.sov = sov;
	}

	public int getVersion() {
		return super.getVersion();
	}

	public void setVersion(final int version) {
		super.setVersion(version);
	}

	@Override
	public boolean isForceUpdate() {
		return super.isForceUpdate();
	}

	@Override
	public void setForceUpdate(final boolean forceUpdate) {
		super.setForceUpdate(forceUpdate);
	}

	@Override
	public Product populate(final Product bean) {
		bean.setDescription(this.getProductDescription());
		bean.setName(this.getProductName());
		bean.setId(this.getProductId());
		final ProductType productType = new ProductType();
		productType.setProductTypeName(this.getTypeName());
		bean.setTypeName(productType);
		final ProductClass productClass = new ProductClass();
		productClass.setProductClassName(this.getClassName());
		bean.setClassName(productClass);
		bean.setNote(this.getNote());
		bean.setDisplayName(this.getDisplayName());
		bean.setIsViewable(this.getIsViewable());
		return bean;
	}

	@Override
	public void populateForm(final Product bean) {
		this.setProductDescription(bean.getDescription());
		this.setProductName(bean.getName());
		this.setProductId(bean.getId());
		if (bean.getTypeName() != null) {
			this.setTypeName(bean.getTypeName().getProductTypeName());
		}
		if (bean.getClassName() != null) {
			this.setClassName(bean.getClassName().getProductClassName());
		}
		this.setNote(bean.getNote());
		this.setDisplayName(bean.getDisplayName());
		this.setIsViewable(bean.getIsViewable());
	}

	public boolean isHasDocument() {
		return hasDocument;
	}

	public void setHasDocument(final boolean hasDocument) {
		this.hasDocument = hasDocument;
	}

	/**
	 * @return the sosSalesTargetId
	 */
	public String[] getSosSalesTargetId() {
		return sosSalesTargetId;
	}

	/**
	 * @param sosSalesTargetId the sosSalesTargetId to set
	 */
	public void setSosSalesTargetId(final String[] sosSalesTargetId) {
		this.sosSalesTargetId = sosSalesTargetId;
	}

	/**
	 * @return long
	 */
	public long getLineItemID() {
		return lineItemID;
	}

	/**
	 * @param lineItemID
	 */
	public void setLineItemID(final long lineItemID) {
		this.lineItemID = lineItemID;
	}

	public String getReservable() {
		return reservable;
	}

	public void setReservable(final String reservable) {
		this.reservable = reservable;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getIsViewable() {
		return isViewable;
	}

	public void setIsViewable(String isViewable) {
		this.isViewable = isViewable;
	}
	
	
}
