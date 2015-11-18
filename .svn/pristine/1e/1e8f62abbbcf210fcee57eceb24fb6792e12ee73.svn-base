/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This <code>ProposalHead</code> class includes all the attributes related to
 * Proposal Head and their getter and setter. The attributes have mapping with
 * <code>MP_PROPOSALS_HEAD</code> table in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_PROPOSALS_HEAD")
public class ProposalHead implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String displayName;
	private String headName;
	private boolean proposalType;

	private List<ProposalHeadAttributes> proposalHeadAttributes = new ArrayList<ProposalHeadAttributes>();

	@Id
	@Column(name = "HEAD_ID")
	public Long getId() {
		return id;
	}

	public void setId(final Long headId) {
		this.id = headId;
	}

	@Column(name = "DISPLAY_HEAD_NAME")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "HEAD_NAME")
	public String getHeadName() {
		return headName;
	}

	public void setHeadName(final String headName) {
		this.headName = headName;
	}

	@OneToMany(mappedBy = "proposalHead", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<ProposalHeadAttributes> getProposalHeadAttributes() {
		return proposalHeadAttributes;
	}

	public void setProposalHeadAttributes(final List<ProposalHeadAttributes> proposalHeadAttributes) {
		this.proposalHeadAttributes = proposalHeadAttributes;
	}

	@Column(name = "IS_PROPOSAL_TYPE")
	public boolean isProposalType() {
		return proposalType;
	}

	public void setProposalType(boolean proposalType) {
		this.proposalType = proposalType;
	}
}