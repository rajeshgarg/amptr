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

/**
 * This <code>TierSectionAssoc</code> class includes all the attributes related
 * to Tier Section and their getter and setter. The attributes have mapping with
 * <code>MP_TIER_SECTION_ASSOC</code> table in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_TIER_SECTION_ASSOC")
public class TierSectionAssoc {

	private Long id;

	private Long sectionId;

	private String sectionName;

	private Tier tier;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIER_SECTION_ASSOC_SEQ")
	@SequenceGenerator(name = "TIER_SECTION_ASSOC_SEQ", sequenceName = "MP_TIER_SECTION_ASSOC_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(final Long assocId) {
		id = assocId;
	}

	@Column(name = "SECTION_ID")
	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(final Long sectionId) {
		this.sectionId = sectionId;
	}

	@Column(name = "SECTION_NAME")
	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(final String sectionName) {
		this.sectionName = sectionName;
	}

	@ManyToOne
	@JoinColumn(name = "TIER_ID", nullable = true, insertable = true, updatable = true)
	public Tier getTier() {
		return tier;
	}

	public void setTier(final Tier tier) {
		this.tier = tier;
	}
}
