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

/**
 * This <code>LineItemSalesTargetAssoc</code> class includes all the attributes
 * related to LineItem Sales Targets and their getter and setter. The attributes
 * have mapping with <code>MP_LINE_SALES_TARGET_ASSOC</code> table in the AMPT
 * database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_LINE_SALES_TARGET_ASSOC")
public class LineItemSalesTargetAssoc {

	private Long id;
	private Long sosSalesTargetId;
	private LineItem proposalLineItem;
	private String sosSalesTargetName;

	@Id
	@Column(name = "ASSOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LISALES_TARGET_SEQ")
	@SequenceGenerator(name = "LISALES_TARGET_SEQ", sequenceName = "MP_LINE_ITEM_SALES_TRGT_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "LINE_ITEM_ID", nullable = false, insertable = true, updatable = false)
	public LineItem getProposalLineItem() {
		return proposalLineItem;
	}

	public void setProposalLineItem(LineItem proposalLineItem) {
		this.proposalLineItem = proposalLineItem;
	}

	@Column(name = "SALES_TARGET_ID", nullable = false)
	public Long getSosSalesTargetId() {
		return sosSalesTargetId;
	}

	public void setSosSalesTargetId(Long sosSalesTargetId) {
		this.sosSalesTargetId = sosSalesTargetId;
	}

	@Column(name = "SALES_TARGET_DISPLAY_NAME")
	public String getSosSalesTargetName() {
		return sosSalesTargetName;
	}

	public void setSosSalesTargetName(String sosSalesTargetName) {
		this.sosSalesTargetName = sosSalesTargetName;
	}
}
