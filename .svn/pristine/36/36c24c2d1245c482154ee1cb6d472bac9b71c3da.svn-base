package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This class is used as composite key for Cluster Sales Target
 * @author manish.kesarwani
 *
 */
@Embeddable
public class ClusterSalesTargetId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long sosClusterSalesTargetId;
	
	private long sosSalesTargetId;
	
	@Column(name = "SOS_CLUSTER_SALES_TARGET_ID")
	public long getSosClusterSalesTargetId() {
		return sosClusterSalesTargetId;
	}
	
	public void setSosClusterSalesTargetId(long sosClusterSalesTargetId) {
		this.sosClusterSalesTargetId = sosClusterSalesTargetId;
	}
	
	@Column(name = "SOS_SALES_TARGET_ID")
	public long getSosSalesTargetId() {
		return sosSalesTargetId;
	}
	
	public void setSosSalesTargetId(long sosSalesTargetId) {
		this.sosSalesTargetId = sosSalesTargetId;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(sosClusterSalesTargetId).append(sosSalesTargetId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			ClusterSalesTargetId other = (ClusterSalesTargetId) obj;
			return new EqualsBuilder().append(this.sosClusterSalesTargetId, other.sosClusterSalesTargetId).append(this.sosSalesTargetId, other.sosSalesTargetId).isEquals();
		}
		return false;
	}
}
