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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>ProposalHeadAttributes</code> class includes all the attributes
 * related to Proposal Head Attributes and their getter and setter. The
 * attributes have mapping with <code>MP_PROPOSALS_HEAD_ATTRIBUTES</code> table
 * in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_PROPOSALS_HEAD_ATTRIBUTES")
public class ProposalHeadAttributes implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private ProposalHead proposalHead;
	private String displayAttributeName;
	private String attributeName;
	private String lookUpHead;
	private String autoConfigKey;

	@Id
	@Column(name = "ATTRIBUTE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HEAD_ATTRIBUTES_SEQUENCE")
	@SequenceGenerator(name = "HEAD_ATTRIBUTES_SEQUENCE", sequenceName = "MP_HEAD_ATTRIBUTES_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(final Long attributId) {
		this.id = attributId;
	}

	@Column(name = "DISPLAY_ATTRIBUTENAME")
	public String getDisplayAttributeName() {
		return displayAttributeName;
	}

	public void setDisplayAttributeName(final String attributeName) {
		this.displayAttributeName = attributeName;
	}

	@Column(name = "ATTRIBUTE_NAME")
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(final String attributeName) {
		this.attributeName = attributeName;
	}

	@ManyToOne
	@JoinColumn(name = "HEAD_ID", nullable = true, insertable = true, updatable = true)
	public ProposalHead getProposalHead() {
		return proposalHead;
	}

	public void setProposalHead(final ProposalHead proposalHead) {
		this.proposalHead = proposalHead;
	}

	@Column(name = "LOOKUP_HEAD")
	public String getLookUpHead() {
		return lookUpHead;
	}

	public void setLookUpHead(String lookUpHead) {
		this.lookUpHead = lookUpHead;
	}

	@Column(name = "AUTO_CONFIG_KEY")
	public String getAutoConfigKey() {
		return autoConfigKey;
	}

	public void setAutoConfigKey(String autoConfigKey) {
		this.autoConfigKey = autoConfigKey;
	}
}
