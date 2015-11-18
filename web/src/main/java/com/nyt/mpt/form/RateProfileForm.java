/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.NumberUtil;

/**
 * This form bean is used for list rate information
 * 
 * @author sachin.ahuja
 * 
 */
@SuppressWarnings("serial")
public class RateProfileForm extends BaseForm<RateProfile> implements Serializable {

	private long profileId;
	private String salesCategoryId;
	private String salesCategoryName;
	private String productId;
	private String productName;
	private String basePrice;
	private String[] salesTargetId;
	private String salesTargetNamesStr;
	private String salesTargetIdStr;
	private String notes;
	private String sectionNames;
	private String rateProfileDiscountData;
	private String discountSalesCategoryId;
	private String discountProductId;
	private boolean rateCardNotRounded;

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(final long profileId) {
		this.profileId = profileId;
	}

	public String getSalesCategoryId() {
		return salesCategoryId;
	}

	public void setSalesCategoryId(final String salesCategoryId) {
		this.salesCategoryId = salesCategoryId;
	}

	public String getSalesCategoryName() {
		return salesCategoryName;
	}

	public void setSalesCategoryName(final String salesCategoryName) {
		this.salesCategoryName = salesCategoryName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(final String productName) {
		this.productName = productName;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(final String basePrice) {
		this.basePrice = basePrice == null ? basePrice : basePrice.trim();
	}

	public String[] getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(final String[] salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes == null ? notes : notes.trim();
	}

	public String getSalesTargetNamesStr() {
		return salesTargetNamesStr;
	}

	public void setSalesTargetNamesStr(final String salesTargetNamesStr) {
		this.salesTargetNamesStr = salesTargetNamesStr;
	}

	public String getSalesTargetIdStr() {
		return salesTargetIdStr;
	}

	public void setSalesTargetIdStr(final String salesTargetIdStr) {
		this.salesTargetIdStr = salesTargetIdStr;
	}

	public String getSectionNames() {
		return sectionNames;
	}

	public void setSectionNames(final String sectionNames) {
		this.sectionNames = sectionNames;
	}

	public String getRateProfileDiscountData() {
		return rateProfileDiscountData;
	}

	public void setRateProfileDiscountData(final String rateProfileDiscountData) {
		this.rateProfileDiscountData = rateProfileDiscountData;
	}

	public String getDiscountSalesCategoryId() {
		return discountSalesCategoryId;
	}

	public void setDiscountSalesCategoryId(final String discountSalesCategoryId) {
		this.discountSalesCategoryId = discountSalesCategoryId;
	}

	public String getDiscountProductId() {
		return discountProductId;
	}

	public void setDiscountProductId(final String discountProductId) {
		this.discountProductId = discountProductId;
	}

	public boolean isRateCardNotRounded() {
		return rateCardNotRounded;
	}

	public void setRateCardNotRounded(boolean rateCardNotRounded) {
		this.rateCardNotRounded = rateCardNotRounded;
	}

	@Override
	public RateProfile populate(final RateProfile bean) {
		bean.setProfileId(this.getProfileId());
		bean.setProductId(NumberUtil.longValue(this.getProductId()));
		bean.setProductName(this.getProductName());
		bean.setBasePrice(NumberUtil.doubleValue(this.getBasePrice()));

		if (StringUtils.isNotBlank(this.getSalesCategoryId())) {
			bean.setSalesCategoryId(NumberUtil.longValue(this.getSalesCategoryId()));
		}

		if (StringUtils.isNotBlank(this.getSalesCategoryName())) {
			bean.setSalesCategoryName(this.getSalesCategoryName());
		}

		bean.setNotes(this.getNotes());
		bean.setVersion(getVersion());
		bean.setRateCardNotRounded(this.isRateCardNotRounded());
		return bean;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	public void populateForm(final RateProfile bean) {
		this.setProfileId(bean.getProfileId());
		this.setProductId(String.valueOf(bean.getProductId()));
		this.setProductName(bean.getProductName());
		this.setBasePrice(NumberUtil.formatDouble(bean.getBasePrice(), true));
		if (bean.getRateConfigSet() != null && !bean.getRateConfigSet().isEmpty()) {
			final StringBuffer salesTargetNames = new StringBuffer(30);
			final StringBuffer salesTargetIds = new StringBuffer(30);
			for (RateConfig rateConfig : bean.getRateConfigSet()) {
				salesTargetNames.append(rateConfig.getSalesTargetName()).append(ConstantStrings.COMMA).append(ConstantStrings.SPACE);
				salesTargetIds.append(rateConfig.getSalesTargetId()).append(ConstantStrings.COMMA);
			}

			String salesTargetIdStr = salesTargetIds.toString();
			String salesTargetNamesStr = salesTargetNames.toString();
			salesTargetIdStr = salesTargetIdStr.substring(0, salesTargetIds.lastIndexOf(ConstantStrings.COMMA));
			salesTargetNamesStr = salesTargetNamesStr.substring(0, salesTargetNames.lastIndexOf(ConstantStrings.COMMA));

			this.setSalesTargetIdStr(salesTargetIdStr);
			this.setSalesTargetNamesStr(salesTargetNamesStr);
		}
		this.setNotes(bean.getNotes());
		this.setVersion(bean.getVersion());
		this.setSectionNames(bean.getSectionNames());
		this.setRateCardNotRounded(bean.isRateCardNotRounded());
	}
}
