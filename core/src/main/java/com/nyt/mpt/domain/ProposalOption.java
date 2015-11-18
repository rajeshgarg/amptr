/**
 * 
 */
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>ProposalOption</code> class includes all the attributes related to
 * ProposalOption and their getter and setter. The attributes have mapping with
 * <code>MP_PROPOSAL_OPTIONS</code> table in the AMPT database
 * 
 * @author amandeep.singh
 */

@Entity
@Table(name = "MP_PROPOSAL_OPTIONS")
public class ProposalOption extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Proposal proposal;

	private Set<ProposalVersion> proposalVersions = new LinkedHashSet<ProposalVersion>();

	// This should be used for concurrency check.
	private int version;

	private boolean active = true;

	private boolean defaultOption = false;

	private Double budget;

	private Integer optionNo;

	private Double thresholdLimit;

	private Date lastReviewedDate;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSAL_OPTION")
	@SequenceGenerator(name = "PROPOSAL_OPTION", sequenceName = "MP_PROPOSAL_OPTION_SEQ", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "PROPOSAL_ID", nullable = false, insertable = true, updatable = false)
	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	@Column(name = "VERSION", nullable = false)
	public int getVersion() {
		return version;
	}

	@OneToMany(mappedBy = "proposalOption", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderBy("proposalVersion DESC")
	public Set<ProposalVersion> getProposalVersions() {
		return proposalVersions;
	}

	public void setProposalVersions(Set<ProposalVersion> proposalVersions) {
		this.proposalVersions = proposalVersions;
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

	@Column(name = "IS_DEFAULT")
	public boolean isDefaultOption() {
		return defaultOption;
	}

	@Column(name = "BUDGET", nullable = true)
	public Double getBudget() {
		return budget;
	}

	@Column(name = "OPTION_NO")
	public Integer getOptionNo() {
		return optionNo;
	}

	public void setOptionNo(Integer optionNo) {
		this.optionNo = optionNo;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public void setDefaultOption(boolean defaultOption) {
		this.defaultOption = defaultOption;
	}

	public void addNewVersion(ProposalVersion proposalversion) {
		proposalVersions.add(proposalversion);
		proposalversion.setProposalOption(this);
	}

	@Column(name = "THRESHOLD_LIMIT", nullable = true)
	public Double getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(Double thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTREVIEWED_DATE")
	public Date getLastReviewedDate() {
		return lastReviewedDate;
	}

	public void setLastReviewedDate(Date lastReviewedDate) {
		this.lastReviewedDate = lastReviewedDate;
	}

	@Transient
	public ProposalVersion getLatestVersion() {
		if (!proposalVersions.isEmpty()) {
			return proposalVersions.iterator().next();
		} else {
			return null;
		}
	}

}
