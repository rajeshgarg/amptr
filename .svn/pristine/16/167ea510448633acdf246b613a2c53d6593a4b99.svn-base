/**
 * 
 */
package com.nyt.mpt.util;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * This is base class for Change Tracked Domain.
 * 
 * @author surendra.singh
 * 
 */
@MappedSuperclass
public abstract class ChangeTrackedDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date createdDate;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", updatable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "created_by", updatable = false)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
