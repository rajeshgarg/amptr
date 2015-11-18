package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>TierPremium</code> class includes all the attributes related to
 * TierPremium and their getter and setter. The attributes have mapping with
 * <code>MP_TIER_PREMIUM</code> table in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_TIER_PREMIUM")
public class TierPremium {

	private Long id;

	private AudienceTargetType targetType;

	private String tarTypeElementId;

	private Double premium;

	private Tier tierObj;

	private boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREMIUM_SEQ")
	@SequenceGenerator(name = "PREMIUM_SEQ", sequenceName = "MP_TIER_PREMIUM_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(insertable = true, updatable = true, nullable = false, name = "TARGET_TYPE_ID")
	public AudienceTargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(AudienceTargetType targetType) {
		this.targetType = targetType;
	}

	@Column(name = "ELEMENT_ID")
	public String getTarTypeElementId() {
		return tarTypeElementId;
	}

	public void setTarTypeElementId(final String tarTypeElementId) {
		this.tarTypeElementId = tarTypeElementId;
	}

	@Column(name = "PREMIUM")
	public Double getPremium() {
		return premium;
	}

	public void setPremium(final Double premium) {
		this.premium = premium;
	}

	@ManyToOne
	@JoinColumn(name = "TIER_ID", nullable = true, insertable = true, updatable = false)
	public Tier getTierObj() {
		return tierObj;
	}

	public void setTierObj(final Tier tier) {
		this.tierObj = tier;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}
}
