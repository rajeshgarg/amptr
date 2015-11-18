/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * Used to capture primary page group information for a sales target
 * @author manish.kesarwani
 *
 */
@Entity
@Immutable
@Table(name = "PRIMARY_PAGE_GROUP")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PrimaryPageGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long adxPageGroupId;
	
	private String primaryPageGroup;
	
	private List<SalesTarget> salesTargets;
	
	@Id
	@Column(name = "ADX_PPG_ID")
	public long getAdxPageGroupId() {
		return adxPageGroupId;
	}
	public void setAdxPageGroupId(long adxPageGroupId) {
		this.adxPageGroupId = adxPageGroupId;
	}
	
	@Column(name = "PRIMARY_PAGE_GROUP")
	public String getPrimaryPageGroup() {
		return primaryPageGroup;
	}
	public void setPrimaryPageGroup(String primaryPageGroup) {
		this.primaryPageGroup = primaryPageGroup;
	}
	
	@OneToMany()
	@JoinColumn(name = "ADX_PPG_ID")
	public List<SalesTarget> getSalesTargets() {
		return salesTargets;
	}
	public void setSalesTargets(List<SalesTarget> salesTargets) {
		this.salesTargets = salesTargets;
	}	
}
