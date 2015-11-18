/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This <code>SalesTarget</code> class includes all the attributes related to
 * SalesTarget and their getter and setter. The attributes have mapping with
 * <code>SALES_TARGET</code> table in the SOS database.
 * 
 * Fetch sales target only if sales target type in ('Display ROS (16)', 'Display
 * Section (17)', 'Display Sub Section (18)', 'NYTd Email (19)');
 * 
 * @author amandeep.singh
 */
@Entity
@Immutable
@Table(name = "SALES_TARGET")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Where(clause = "TARGETTYPE_ID IN ('16', '17', '18', '19')")
public class SalesTarget {

	private long salesTargetId;

	private String salesTargetName;

	private String salesTargeDisplayName;

	private SalesTargetType salesTargetType;

	private boolean active;

	private Long parentSalesTargetId;

	@Id
	@Column(name = "SALESTARGET_ID")
	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Column(name = "SALESTARGET_NAME", nullable = false)
	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}

	@Column(name = "TARGET_DISPLAY_NAME", nullable = false)
	public String getSalesTargeDisplayName() {
		return salesTargeDisplayName;
	}

	public void setSalesTargeDisplayName(String salesTargeDisplayName) {
		this.salesTargeDisplayName = salesTargeDisplayName;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne(targetEntity = SalesTargetType.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "TARGETTYPE_ID", nullable = false, updatable = false, insertable = false)
	public SalesTargetType getSalesTargetType() {
		return salesTargetType;
	}

	public void setSalesTargetType(SalesTargetType salesTargetType) {
		this.salesTargetType = salesTargetType;
	}

	@Column(name = "PARENT_SALESTARGET", nullable = true)
	public Long getParentSalesTargetId() {
		return parentSalesTargetId;
	}

	public void setParentSalesTargetId(Long parentSalesTargetId) {
		this.parentSalesTargetId = parentSalesTargetId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Sales Targe Display Name", salesTargeDisplayName)
				.append("Sales Target Id", salesTargetId)
				.append("Sales Target Name", salesTargetName)
				.append("Sales Target Type", salesTargetType)
				.append("Status", active).toString();
	}
}
