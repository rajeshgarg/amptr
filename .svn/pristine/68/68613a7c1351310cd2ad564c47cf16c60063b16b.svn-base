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
 * This <code>LineItemTarget</code> class includes all the attributes related to
 * LineItem Targets and their getter and setter. The attributes have mapping
 * with <code>MP_LINE_ITEM_TARGET_INFO</code> table in the AMPT database
 * 
 * @author amandeep.singh
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "MP_LINE_ITEM_TARGET_INFO")
public class LineItemTarget extends ChangeTrackedDomain {

	private Long id;

	private Long sosTarTypeId;

	private String sosTarTypeElementId;

	private boolean active = true;

	private LineItem proposalLineItem;

	private Double Premium;

	private String operation;

	private String negation;

	private String segmentLevel;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEO_TARGET_SEQ")
	@SequenceGenerator(name = "GEO_TARGET_SEQ", sequenceName = "MP_LINE_ITEM_TARGET_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SOS_TAR_TYPE_ID")
	public Long getSosTarTypeId() {
		return sosTarTypeId;
	}

	public void setSosTarTypeId(Long sosTarTypeId) {
		this.sosTarTypeId = sosTarTypeId;
	}

	@Column(name = "SOS_TAR_TYPE_ELEMENT_ID")
	public String getSosTarTypeElementId() {
		return sosTarTypeElementId;
	}

	public void setSosTarTypeElementId(String sosTarTypeElementId) {
		this.sosTarTypeElementId = sosTarTypeElementId;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne
	@JoinColumn(name = "LINEITEM_ID", nullable = false, insertable = true, updatable = false)
	public LineItem getProposalLineItem() {
		return proposalLineItem;
	}

	public void setProposalLineItem(LineItem proposalLineItem) {
		this.proposalLineItem = proposalLineItem;
	}

	@Column(name = "PREMIUM")
	public Double getPremium() {
		return Premium;
	}

	public void setPremium(Double premium) {
		Premium = premium;
	}

	@Column(name = "OPERATION")
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Column(name = "NEGATION")
	public String getNegation() {
		return negation;
	}

	public void setNegation(String negation) {
		this.negation = negation;
	}

	@Column(name = "REVENUE_SEGMENT_LEVEL")
	public String getSegmentLevel() {
		return segmentLevel;
	}

	public void setSegmentLevel(String segmentLevel) {
		this.segmentLevel = segmentLevel;
	}
}
