/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This class is used to capture cluster sales target
 * @author manish.kesarwani
 *
 */
@Entity
@Immutable
@Table(name = "CLUSTER_SALES_TARGET")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ClusterSalesTarget implements Serializable {

	private static final long serialVersionUID = 1L;

	private ClusterSalesTargetId clusterSalesTargetId;

	public ClusterSalesTarget() {
		super();
	}

	@EmbeddedId
	@AttributeOverrides( { @AttributeOverride(name = "sosClusterSalesTargetId", column = @Column(name = "SOS_CLUSTER_SALES_TARGET_ID")),
			@AttributeOverride(name = "sosSalesTargetId", column = @Column(name = "SOS_SALES_TARGET_ID")) })
	public ClusterSalesTargetId getClusterSalesTargetId() {
		return clusterSalesTargetId;
	}

	public void setClusterSalesTargetId(ClusterSalesTargetId clusterSalesTargetId) {
		this.clusterSalesTargetId = clusterSalesTargetId;
	}
}
