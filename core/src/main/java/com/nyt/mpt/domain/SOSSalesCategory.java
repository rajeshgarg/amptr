/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import java.sql.Date;

/**
 * This class is used to provide SOS Sales Category
 * 
 * @author Pranay.Prabhat
 * 
 */
@Entity
@Immutable
@Table(name = "SALES_CATEGORY")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SOSSalesCategory {

	private long sosSalesCategoryId;

	private String name;

	private Date deleteDate;

	private String status;

	@Id
	@Column(name = "SOS_SALES_CATEGORY_ID", nullable = false)
	public long getSosSalesCategoryId() {
		return sosSalesCategoryId;
	}

	public void setSosSalesCategoryId(long sosSalesCategoryId) {
		this.sosSalesCategoryId = sosSalesCategoryId;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
}
