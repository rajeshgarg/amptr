package com.nyt.mpt.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.util.ConstantStrings;

/**
 * This class is used for providing pricing calculation step detail  
 * @author rakesh.tewari
 *
 */
public class PricingCalculatorSummary {
	
	private List<TierSummary> tierSummary = new ArrayList<TierSummary>(); 
	
	private List<RateProfileSummary> rateProfileSummary = new ArrayList<RateProfileSummary>(); 
	
	private Double basePrice;
	
	private Double premium;
	
	private String tier;
	
	private String level;
	
	private Double sumOfWeight;
	
	private Double weightedPrice;
	
	private double sumOfBasePrice;
	
	private Long totalCount;
	
	private String price;
	
	private String weightedBasePrice;

	private Double agencyMargin;
	
	private String appliedFiveCentsRule;
	
	private String salesCategory;
	
	public List<TierSummary> getTierSummary() {
		return tierSummary;
	}

	public void setTierSummary(List<TierSummary> tierSummary) {
		this.tierSummary = tierSummary;
	}

	public List<RateProfileSummary> getRateProfileSummary() {
		return rateProfileSummary;
	}

	public void setRateProfileSummary(List<RateProfileSummary> rateProfileSummary) {
		this.rateProfileSummary = rateProfileSummary;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double primiunm) {
		this.premium = (primiunm == null) ? 0.0 : primiunm;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Double getSumOfWeight() {
		return sumOfWeight;
	}

	public void setSumOfWeight(Double sumOfWeight) {
		this.sumOfWeight = sumOfWeight;
	}

	/**
	 * @return the weightedPrice
	 */
	public Double getWeightedPrice() {
		return weightedPrice;
	}

	/**
	 * @param weightedPrice the weightedPrice to set
	 */
	public void setWeightedPrice(Double weightedPrice) {
		this.weightedPrice = weightedPrice;
	}

	/**
	 * @return the sumOfBasePrice
	 */
	public double getSumOfBasePrice() {
		return sumOfBasePrice;
	}

	/**
	 * @param sumOfBasePrice the sumOfBasePrice to set
	 */
	public void setSumOfBasePrice(double sumOfBasePrice) {
		this.sumOfBasePrice = sumOfBasePrice;
	}

	/**
	 * @return the totalCount
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public void setWeightedBasePrice(String weightedBasePrice) {
		this.weightedBasePrice = weightedBasePrice;
	}

	public String getWeightedBasePrice() {
		return weightedBasePrice;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public Double getAgencyMargin() {
		return agencyMargin;
	}

	public void setAgencyMargin(Double agencyMargin) {
		this.agencyMargin = agencyMargin;
	}

	public String getAppliedFiveCentsRule() {
		return appliedFiveCentsRule;
	}

	public void setAppliedFiveCentsRule(String appliedFiveCentsRule) {
		this.appliedFiveCentsRule = appliedFiveCentsRule;
	}

	public String getSalesCategory() {
		return salesCategory;
	}

	public void setSalesCategory(String salesCategory) {
		this.salesCategory = StringUtils.isBlank(salesCategory) ? ConstantStrings.SPACE : salesCategory;
	}
}
