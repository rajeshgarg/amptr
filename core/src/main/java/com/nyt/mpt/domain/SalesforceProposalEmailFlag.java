/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This <code>SalesforceProposalEmailFlag</code> class includes all the
 * attributes related to Salesforce Proposal Email Flag and their getter and
 * setter. The attributes have mapping with
 * <code>MP_SALESFORCE_PROPOSAL_EMAIL</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_SALESFORCE_PROPOSAL_EMAIL")
public class SalesforceProposalEmailFlag implements Serializable {

	private static final long serialVersionUID = 1L;
	private String salesforceId;
	private String emailFlag;
	private Date submmitedDate;

	@Id
	@Column(name = "SALESFORCE_ID")
	public String getSalesforceId() {
		return salesforceId;
	}

	public void setSalesforceId(final String salesforceId) {
		this.salesforceId = salesforceId;
	}

	@Column(name = "IS_EMAIL_SENT")
	public String getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(final String emailFlag) {
		this.emailFlag = emailFlag;
	}

	@Column(name = "SUBMITTED_AMPT_DATE")
	public Date getSubmmitedDate() {
		return submmitedDate;
	}

	public void setSubmmitedDate(final Date submmitedDate) {
		this.submmitedDate = submmitedDate;
	}
}