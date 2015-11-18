package com.nyt.mpt.domain.sos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This <code>ProductClass</code> class includes all the attributes related to
 * ProductClass and their getter and setter. The attributes have mapping with
 * <code>PRODUCT_CLASS</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Immutable
@Table(name = "PRODUCT_CLASS")
@Where(clause = "DIVISION_ID = 2")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ProductClass implements Serializable {

	private static final long serialVersionUID = 1L;

	private long productClassID;

	private String productClassName;

	@Id
	@Column(name = "PRODUCTCLASS_ID")
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

}