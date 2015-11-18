package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This <code>CampaignObjective</code> class includes all the attributes related
 * to Campaign Objectives and their getter and setter. The attributes have
 * mapping with <code>MP_CAMPAIGN_OBJECTIVE</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_CAMPAIGN_OBJECTIVE")
public class CampaignObjective implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long cmpObjId;

	private String cmpObjText;

	private String cmpObjDescription;

	@Id
	@Column(name = "ID")
	public Long getCmpObjId() {
		return cmpObjId;
	}

	public void setCmpObjId(Long cmpObjId) {
		this.cmpObjId = cmpObjId;
	}

	@Column(name = "OBJECTIVE")
	public String getCmpObjText() {
		return cmpObjText;
	}

	public void setCmpObjText(String cmpObjText) {
		this.cmpObjText = cmpObjText;
	}

	@Column(name = "DESCRIPTION")
	public String getCmpObjDescription() {
		return cmpObjDescription;
	}

	public void setCmpObjDescription(String cmpObjDescription) {
		this.cmpObjDescription = cmpObjDescription;
	}
}
