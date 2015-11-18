/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import java.sql.Date;
import java.util.List;
/**
 * This class is used to provide Sales Target Type
 * @author amandeep.singh
 * 
 */
@Entity
@Immutable
@Table(name = "SALES_TARGET_TYPE")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SalesTargetType {

	private long salestargetTypeId;

	private String salesTargetTypeName;
	
	private Date deleteDate;
	
	private String status;
	
	private List<SalesTarget> salesTargetList;

	@Id
	@Column(name = "SOS_SALES_TARGET_TYPE_ID")
	public long getSalestargetTypeId() {
		return salestargetTypeId;
	}

	public void setSalestargetTypeId(long salestargetTypeId) {
		this.salestargetTypeId = salestargetTypeId;
	}

	@Column(name = "NAME")
	public String getSalesTargetTypeName() {
		return salesTargetTypeName;
	}

	public void setSalesTargetTypeName(String salesTargetTypeName) {
		this.salesTargetTypeName = salesTargetTypeName;
	}
	
	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Sales Target Type Name", salesTargetTypeName).append(
						"Sales Target Type Id", salestargetTypeId).toString();
	}

	@OneToMany(mappedBy = "salesTargetType", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<SalesTarget> getSalesTargetList() {
		return salesTargetList;
	}

	public void setSalesTargetList(List<SalesTarget> salesTargetList) {
		this.salesTargetList = salesTargetList;
	}
}