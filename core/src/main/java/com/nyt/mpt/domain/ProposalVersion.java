package com.nyt.mpt.domain;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>ProposalVersion</code> class includes all the attributes related
 * to ProposalVersion and their getter and setter. The attributes have mapping
 * with <code>MP_PROPOSAL_VERSIONS</code> table in the AMPT database
 * 
 * @author amandeep.singh
 */

@Entity
@Table(name = "MP_PROPOSAL_VERSIONS")
public class ProposalVersion extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long id;

	private ProposalOption proposalOption;

	private Double effectiveCpm;

	private Long impressions;

	// This should be used for maintaining history of proposal.
	private Long proposalVersion;

	// This should be used for concurrency check.
	private int version;

	private boolean active = true;

	private Date startDate;

	private Date endDate;

	private Double offeredBudget;

	private Set<LineItem> proposalLineItemSet = new LinkedHashSet<LineItem>();

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSAL_VERSION")
	@SequenceGenerator(name = "PROPOSAL_VERSION", sequenceName = "MP_PROPOSAL_VERSION_SEQ", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "OPTION_ID", nullable = false, insertable = true, updatable = false)
	public ProposalOption getProposalOption() {
		return proposalOption;
	}

	public void setProposalOption(ProposalOption proposalOption) {
		this.proposalOption = proposalOption;
	}

	@Column(name = "CPM", nullable = true)
	public Double getEffectiveCpm() {
		return effectiveCpm;
	}

	public void setEffectiveCpm(Double effectiveCpm) {
		this.effectiveCpm = effectiveCpm;
	}

	@Column(name = "IMPRESSIONS", nullable = true)
	public Long getImpressions() {
		return impressions;
	}

	public void setImpressions(Long impressions) {
		this.impressions = impressions;
	}

	@Column(name = "PROPOSAL_VERSION", nullable = false)
	public Long getProposalVersion() {
		return proposalVersion;
	}

	public void setProposalVersion(Long proposalVersion) {
		this.proposalVersion = proposalVersion;
	}

	@Column(name = "VERSION", nullable = false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "OFFERD_BUDGET")
	public Double getOfferedBudget() {
		return offeredBudget;
	}

	public void setOfferedBudget(Double offeredBudget) {
		this.offeredBudget = offeredBudget;
	}

	@OneToMany(mappedBy = "proposalVersion", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Where(clause = "IS_ACTIVE = 1")
	public Set<LineItem> getProposalLineItemSet() {
		return proposalLineItemSet;
	}

	public void setProposalLineItemSet(Set<LineItem> proposalLineItemSet) {
		this.proposalLineItemSet = proposalLineItemSet;
	}

}
