/**
 * 
 */
package com.nyt.mpt.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This <code>RateProfileSeasonalDiscounts</code> class includes all the
 * attributes related to Rate Profile Seasonal Discounts and their getter and
 * setter. The attributes have mapping with <code>MP_SEASONAL_DISCOUNT</code>
 * table in the AMPT database
 * 
 * @author garima.garg
 */
@Entity
@Table(name = "MP_SEASONAL_DISCOUNT")
public class RateProfileSeasonalDiscounts {

	private long discountId;

	private RateProfile rateProfile;

	private Date startDate;

	private Date endDate;

	private double discount;

	private boolean not = false;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEASONAL_DISCOUNT")
	@SequenceGenerator(name = "SEASONAL_DISCOUNT", sequenceName = "MP_SEASONAL_DISCOUNT_SEQ", allocationSize = 1)
	public long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(long discountId) {
		this.discountId = discountId;
	}

	@ManyToOne
	@JoinColumn(name = "PROFILE_ID", nullable = false, insertable = true, updatable = false)
	public RateProfile getRateProfile() {
		return rateProfile;
	}

	public void setRateProfile(RateProfile rateProfile) {
		this.rateProfile = rateProfile;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "DISCOUNT")
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	@Column(name = "IS_NOT")
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}
}
