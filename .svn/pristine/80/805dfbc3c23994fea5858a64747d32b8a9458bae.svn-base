package com.nyt.mpt.domain.sos;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * This <code>Product</code> class includes all the attributes related to
 * Product and their getter and setter. The attributes have mapping with
 * <code>PRODUCT_BASE</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Immutable
@Table(name = "PRODUCT_BASE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private ProductType typeName;

	private String name;

	private ProductClass className;

	private String note;

	private String description;

	private String displayName;

	private boolean active;

	private String reservable;

	private Long vendorid;

	@Id
	@Column(name = "PRODUCT_ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRODUCT_NOTE", nullable = false)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "DESCRIPTION", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DISPLAY_NAME", nullable = false)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne(targetEntity = ProductType.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PRODUCTTYPE_ID", nullable = false, updatable = false, insertable = false)
	public ProductType getTypeName() {
		return typeName;
	}

	public void setTypeName(ProductType typeName) {
		this.typeName = typeName;
	}

	@ManyToOne(targetEntity = ProductClass.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PRODUCTCLASS_ID", nullable = false, updatable = false, insertable = false)
	public ProductClass getClassName() {
		return className;
	}

	public void setClassName(ProductClass className) {
		this.className = className;
	}

	@Column(name = "IS_RESERVABLE")
	public String getReservable() {
		return reservable;
	}

	public void setReservable(String reservable) {
		this.reservable = reservable;
	}

	@Column(name = "DEFAULTVENDOR_ID")
	public Long getVendorid() {
		return vendorid;
	}

	public void setVendorid(Long vendorid) {
		this.vendorid = vendorid;
	}
}