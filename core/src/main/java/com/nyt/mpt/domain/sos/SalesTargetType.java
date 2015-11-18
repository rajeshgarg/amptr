/**
 * 
 */
package com.nyt.mpt.domain.sos;

import java.util.List;

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

/**
 * This <code>SalesTargetType</code> class includes all the attributes related
 * to SalesTargetType and their getter and setter. The attributes have mapping
 * with <code>TARGET_TYPE</code> table in the SOS database
 * 
 * @author amandeep.singh
 */
@Entity
@Immutable
@Table(name = "TARGET_TYPE")
@Where(clause = "DIVISION_ID = 2")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class SalesTargetType {

	private long salestargetTypeId;

	private String salesTargetTypeName;

	private boolean active;

	private List<SalesTarget> salesTargetList;

	@Id
	@Column(name = "TARGETTYPE_ID")
	public long getSalestargetTypeId() {
		return salestargetTypeId;
	}

	public void setSalestargetTypeId(long salestargetTypeId) {
		this.salestargetTypeId = salestargetTypeId;
	}

	@Column(name = "TARGETTYPE_NAME")
	public String getSalesTargetTypeName() {
		return salesTargetTypeName;
	}

	public void setSalesTargetTypeName(String salesTargetTypeName) {
		this.salesTargetTypeName = salesTargetTypeName;
	}

	@Column(name = "STATUS", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
					.append("Sales Target Type Name", salesTargetTypeName)
					.append("Sales Target Type Id", salestargetTypeId).toString();
	}

	@OneToMany(mappedBy = "salesTargetType", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<SalesTarget> getSalesTargetList() {
		return salesTargetList;
	}

	public void setSalesTargetList(List<SalesTarget> salesTargetList) {
		this.salesTargetList = salesTargetList;
	}
}
