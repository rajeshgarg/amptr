/**
 * 
 */
package com.nyt.mpt.domain;

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


import java.sql.Date;

/**
 * Class is used for getting Sales Target from SOS Database 
 * 
 * Fetch sales target only if sales target type in ('Display ROS (16)', 'Display Section (17)',
 * 'Display Sub Section (18)', 'NYTd Email (19)');
 * 
 * @author amandeep.singh 
 */
@Entity
@Immutable
@Table(name = "SALES_TARGET")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Where(clause = "SOS_SALES_TARGET_TYPE_ID IN ('16', '17', '18', '19') AND DELETE_DATE is null")
public class SalesTarget {
	
	private long salesTargetId;
	
	private String salesTargetName;
	
	private String salesTargeDisplayName;
	
	private SalesTargetType salesTargetType;
	
	private String status;
	
	private Date deleteDate;
	
	private Long parentSalesTargetId;

	@Id
	@Column(name = "SOS_SALES_TARGET_ID")
	public long getSalesTargetId() {
		return salesTargetId;
	}

	public void setSalesTargetId(long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}

	@Column(name = "NAME", nullable = false)
	public String getSalesTargetName() {
		return salesTargetName;
	}

	public void setSalesTargetName(String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}

	@Column(name = "DISPLAY_NAME", nullable = false)
	public String getSalesTargeDisplayName() {
		return salesTargeDisplayName;
	}

	public void setSalesTargeDisplayName(String salesTargeDisplayName) {
		this.salesTargeDisplayName = salesTargeDisplayName;
	}

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(targetEntity = SalesTargetType.class, fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "SOS_SALES_TARGET_TYPE_ID", nullable = false, updatable = false, insertable = false)
	public SalesTargetType getSalesTargetType() {
		return salesTargetType;
	}

	public void setSalesTargetType(SalesTargetType salesTargetType) {
		this.salesTargetType = salesTargetType;
	}
	
	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	@Column(name = "SOS_PARENT_SALES_TARGET_ID", nullable = true)
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
				.append("Status", status).toString();
	}
}
