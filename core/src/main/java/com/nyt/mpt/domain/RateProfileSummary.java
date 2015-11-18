package com.nyt.mpt.domain;

/**
 * This class is used for providing rate profile details when calculating rate card price  
 * @author rakesh.tewari
 *
 */
public class RateProfileSummary {

	private String salesCategoryName;

	private String productName;
	
	private String basePrice;

	private String defaultBasePrice;
	
	private String salesCategoryBasePrice;

	private String salesTarget;
	
	private Double weight;
	
	private Long salesTargetId;
	
	private String defaultDiscount;
	
	private String salesCategoryDiscount;
	
	private String defaultdiscountBP; // after discount
	
	private String salesCategorydiscountBP; // after discount

	public void setSalesCategoryName(String salesCategoryName) {
		this.salesCategoryName = salesCategoryName;
	}

	public String getSalesCategoryName() {
		return salesCategoryName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setSalesTarget(String salesTarget) {
		this.salesTarget = salesTarget;
	}

	public String getSalesTarget() {
		return salesTarget;
	}

	public void setWeight(Double weight) {
		this.weight = (weight == null) ? 0.0 : weight;
	}

	public Double getWeight() {
		return weight;
	}

	public void setSalesTargetId(Long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public Long getSalesTargetId() {
		return salesTargetId;
	}

	public String getDefaultBasePrice() {
		return defaultBasePrice;
	}

	public void setDefaultBasePrice(String defaultBasePrice) {
		this.defaultBasePrice = defaultBasePrice;
	}

	public String getSalesCategoryBasePrice() {
		return salesCategoryBasePrice;
	}

	public void setSalesCategoryBasePrice(String salesCategoryBasePrice) {
		this.salesCategoryBasePrice = salesCategoryBasePrice;
	}

	public String getDefaultDiscount() {
		return defaultDiscount;
	}

	public void setDefaultDiscount(String defaultDiscount) {
		this.defaultDiscount = defaultDiscount;
	}

	public String getSalesCategoryDiscount() {
		return salesCategoryDiscount;
	}

	public void setSalesCategoryDiscount(String salesCategoryDiscount) {
		this.salesCategoryDiscount = salesCategoryDiscount;
	}

	public String getDefaultdiscountBP() {
		return defaultdiscountBP;
	}

	public void setDefaultdiscountBP(String defaultdiscountBP) {
		this.defaultdiscountBP = defaultdiscountBP;
	}

	public String getSalesCategorydiscountBP() {
		return salesCategorydiscountBP;
	}

	public void setSalesCategorydiscountBP(String salesCategorydiscountBP) {
		this.salesCategorydiscountBP = salesCategorydiscountBP;
	}
}
