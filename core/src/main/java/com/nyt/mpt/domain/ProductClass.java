/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

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
 * This class is used for providing Product class details
 * 
 * @author amandeep.singh
 * 
 */
@Entity
@Immutable
@Table(name = "PRODUCT_CLASS")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductClass implements Serializable {

	private static final long serialVersionUID = 1L;

	private long productClassID;

	private String productClassName;

	private Date deleteDate;

	@Id
	@Column(name = "SOS_PRODUCT_CLASS_ID")
	public long getProductClassID() {
		return productClassID;
	}

	public void setProductClassID(long productClassID) {
		this.productClassID = productClassID;
	}

	@Column(name = "NAME", nullable = false)
	public String getProductClassName() {
		return productClassName;
	}

	public void setProductClassName(String productClassName) {
		this.productClassName = productClassName;
	}

	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
}
