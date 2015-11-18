/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>RateConfig</code> class includes all the attributes related to
 * Rate Profile Configurations and their getter and setter. The attributes have
 * mapping with <code>MP_RATE_PROFILE_ASSOC</code> table in the AMPT database
 * 
 * @author Sachin.Ahuja
 */
@Entity
@Table(name = "MP_RATE_PROFILE_ASSOC")
@SuppressWarnings("serial")
public class RateConfig extends ChangeTrackedDomain {

	private long configId;

	private RateProfile rateProfile;

	private long salesTargetId;

	private String salesTargetName;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RATE_CONFIG")
	@SequenceGenerator(name = "RATE_CONFIG", sequenceName = "MP_RATE_PROFILE_ASSOC_SEQUENCE", allocationSize = 1)
	public long getConfigId() {
		return configId;
	}

	public void setConfigId(long configId) {
		this.configId = configId;
	}

	@ManyToOne
	@JoinColumn(name = "PROFILE_ID", nullable = false, insertable = true, updatable = false)
	public RateProfile getRateProfile() {
		return rateProfile;
	}

	public void setRateProfile(RateProfile rateProfile) {
		this.rateProfile = rateProfile;
	}

	@Column(name = "SALES_TARGET_ID")
	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Column(name = "SALES_TARGET_NAME")
	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append(this.salesTargetId).append(this.salesTargetName).toString();
	}
}