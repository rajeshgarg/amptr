package com.nyt.mpt.domain;

import java.util.ArrayList;
import java.util.List;

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

import org.hibernate.annotations.Where;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>Tier</code> class includes all the attributes related to Tier and
 * their getter and setter. The attributes have mapping with
 * <code>MP_TIER</code> table in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_TIER")
public class Tier extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private Long tierId;

	private String tierName;

	private Long tierLevel;

	private boolean active = true;

	private List<TierSectionAssoc> tierSectionAssocLst = new ArrayList<TierSectionAssoc>();

	private List<TierPremium> tierPremiumLst = new ArrayList<TierPremium>();

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIER_SEQUENCE")
	@SequenceGenerator(name = "TIER_SEQUENCE", sequenceName = "MP_TIER_SEQUENCE_SEQ", allocationSize = 1)
	public Long getTierId() {
		return tierId;
	}

	public void setTierId(final Long tierId) {
		this.tierId = tierId;
	}

	@Column(name = "NAME")
	public String getTierName() {
		return tierName;
	}

	public void setTierName(final String tierName) {
		this.tierName = tierName;
	}

	@Column(name = "TIER_LEVEL")
	public Long getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(final Long tierLevel) {
		this.tierLevel = tierLevel;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@OneToMany(mappedBy = "tier", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<TierSectionAssoc> getTierSectionAssocLst() {
		return tierSectionAssocLst;
	}

	public void setTierSectionAssocLst(final List<TierSectionAssoc> tierSectionAssocLst) {
		this.tierSectionAssocLst = tierSectionAssocLst;
	}

	@OneToMany(mappedBy = "tierObj", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Where(clause = "IS_ACTIVE = 1")
	public List<TierPremium> getTierPremiumLst() {
		return tierPremiumLst;
	}

	public void setTierPremiumLst(final List<TierPremium> premiumLst) {
		tierPremiumLst = premiumLst;
	}
}