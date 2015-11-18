/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>Notes</code> class includes all the attributes related to Notes
 * and their getter and setter. The attributes have mapping with
 * <code>MP_NOTES</code> table in the AMPT database
 * 
 * @author garima.garg
 */
@Entity
@Table(name = "MP_NOTES")
public class Notes extends ChangeTrackedDomain implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	private String description;

	private Long proposalId;

	private String role;

	private String createdByUserName;

	private String sfProposalRevisionId;

	private boolean active = true;

	private boolean isPushedInSalesforce = false;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTES_SEQUENCE")
	@SequenceGenerator(name = "NOTES_SEQUENCE", sequenceName = "MP_NOTES_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PROPOSAL_ID")
	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	@Column(name = "ROLE")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "CREATED_BY_USER_NAME")
	public String getCreatedByUserName() {
		return createdByUserName;
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	@Column(name = "SF_PROPOSAL_REVISION_ID")
	public String getSfProposalRevisionId() {
		return sfProposalRevisionId;
	}

	public void setSfProposalRevisionId(String sfProposalRevisionId) {
		this.sfProposalRevisionId = sfProposalRevisionId;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_PUSHED_IN_SALES_FORCE")
	public boolean isPushedInSalesforce() {
		return isPushedInSalesforce;
	}

	public void setPushedInSalesforce(final boolean isPushedInSalesforce) {
		this.isPushedInSalesforce = isPushedInSalesforce;
	}
}
