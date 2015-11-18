/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;



/**
 * This class is used for providing Line item trans period type details
 * @author Garima.garg
 *
 */
@Entity
@Immutable
@Table(name = "LINEITEM_TRANS_PERIOD_TYPE")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class lineItemTransPeriodType implements Serializable{

	private static final long serialVersionUID = 1L;

	private long id;
	
	private String name;
	
	private Date createDate;
	
	private Date updateDate;
	
	private Date deleteDate;
	
	private String description;
	
	@Id
	@Column(name = "SOS_PERIOD_TYPE_ID")
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setCreateDate(Date startDate) {
		this.createDate = startDate;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeleteDate() {
		return deleteDate;
	}

	/**
	 * @param deleteDate the deleteDate to set
	 */
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
