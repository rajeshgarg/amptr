/**
 * 
 */
package com.nyt.mpt.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>RateProfile</code> class includes all the attributes related to
 * RateProfile and their getter and setter. The attributes have mapping with
 * <code>MP_RATE_PROFILE</code> table in the AMPT database
 * 
 * @author Sachin.Ahuja
 */
@Entity
@Table(name = "MP_RATE_PROFILE")
@SuppressWarnings("serial")
public class RateProfile extends ChangeTrackedDomain {

	private long profileId;

	private Long salesCategoryId;

	private String salesCategoryName;

	private long productId;

	private String productName;

	private double basePrice;

	private Set<RateConfig> rateConfigSet = new LinkedHashSet<RateConfig>();

	private String notes;

	private boolean active = true;

	private int version;

	private String sectionNames;

	private List<RateProfileSeasonalDiscounts> seasonalDiscountsLst = new ArrayList<RateProfileSeasonalDiscounts>();
	
	private boolean rateCardNotRounded = false;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RATE_PROFILE")
	@SequenceGenerator(name = "RATE_PROFILE", sequenceName = "MP_RATE_PROFILE_SEQUENCE", allocationSize = 1)
	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	@Column(name = "SALES_CATEGORY_ID")
	public Long getSalesCategoryId() {
		return salesCategoryId;
	}

	public void setSalesCategoryId(Long salesCategoryId) {
		this.salesCategoryId = salesCategoryId;
	}

	@Column(name = "SALES_CATEGORY_NAME")
	public String getSalesCategoryName() {
		return salesCategoryName;
	}

	public void setSalesCategoryName(String salesCategoryName) {
		this.salesCategoryName = salesCategoryName;
	}

	@Column(name = "PRODUCT_ID")
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_DISPLAY_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@OneToMany(mappedBy = "rateProfile", cascade = { CascadeType.ALL })
	@OrderBy(clause = "SALES_TARGET_NAME")
	public Set<RateConfig> getRateConfigSet() {
		return rateConfigSet;
	}

	public void setRateConfigSet(Set<RateConfig> listRateConfig) {
		this.rateConfigSet = listRateConfig;
	}

	public void addListRateConfig(RateConfig rateConfig) {
		if (this.rateConfigSet == null) {
			this.rateConfigSet = new LinkedHashSet<RateConfig>();
		}
		rateConfig.setRateProfile(this);
		this.rateConfigSet.add(rateConfig);
	}

	@Column(name = "BASE_PRICE")
	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	@Column(name = "NOTES")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "SECTIONS_NAME")
	public String getSectionNames() {
		return sectionNames;
	}

	public void setSectionNames(String sectionNames) {
		this.sectionNames = sectionNames;
	}

	@OneToMany(mappedBy = "rateProfile", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderBy(clause = "ID")
	public List<RateProfileSeasonalDiscounts> getSeasonalDiscountsLst() {
		return seasonalDiscountsLst;
	}

	public void setSeasonalDiscountsLst(List<RateProfileSeasonalDiscounts> seasonalDiscountsLst) {
		this.seasonalDiscountsLst = seasonalDiscountsLst;
	}

	@Column(name = "IS_RATECARDROUNDED")
	public boolean isRateCardNotRounded() {
		return rateCardNotRounded;
	}

	public void setRateCardNotRounded(boolean rateCardNotRounded) {
		this.rateCardNotRounded = rateCardNotRounded;
	}

	public void addListRateDiscount(RateProfileSeasonalDiscounts seasonalDiscount) {
		if (this.seasonalDiscountsLst == null) {
			this.seasonalDiscountsLst = new ArrayList<RateProfileSeasonalDiscounts>();
		}
		seasonalDiscount.setRateProfile(this);
		this.seasonalDiscountsLst.add(seasonalDiscount);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append(this.productName).append(this.salesCategoryName).toString();
	}
}