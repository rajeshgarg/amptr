package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>SalesTargetAmpt</code> class includes all the attributes related
 * to SalesTargets and their getter and setter. The attributes have mapping with
 * <code>MP_SALES_TARGET</code> table in the AMPT database
 * 
 * @author rakesh.tewari
 */
@Entity
@Table(name = "MP_SALES_TARGET")
@SuppressWarnings("serial")
public class SalesTargetAmpt extends ChangeTrackedDomain {

	private long salesTargetId;

	private String salesTargetName;

	private String salesTargeDisplayName;

	private boolean active = true;

	private int version;

	private Double weight;

	private Long capacity;

	public void setSalesTargetId(final Long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Id
	@Column(name = "SALES_TARGET_ID")
	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetName(final String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}

	@Column(name = "NAME")
	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargeDisplayName(final String salesTargeDisplayName) {
		this.salesTargeDisplayName = salesTargeDisplayName;
	}

	@Column(name = "DISPLAY_NAME")
	public String getSalesTargeDisplayName() {
		return salesTargeDisplayName;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setWeight(final Double weight) {
		this.weight = weight;
	}

	@Column(name = "WEIGHT")
	public Double getWeight() {
		return weight;
	}

	public void setCapacity(final Long capacity) {
		this.capacity = capacity;
	}

	@Column(name = "CAPACITY")
	public Long getCapacity() {
		return capacity;
	}
}
