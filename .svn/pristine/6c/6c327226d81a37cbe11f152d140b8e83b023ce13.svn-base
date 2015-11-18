/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import java.io.Serializable;

/**
 * This class is used for providing AMPT Display Lists info 
 * @author pranay.prabhat
 * 
 */
@Entity
@Immutable
@SuppressWarnings("serial")
@Table(name = "AMPT_DISPLAY_LISTS")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AMPTDisplayLists implements Serializable {
	
	private String sourceTable;
	
	private long sourceId;
	
	protected String displayFlag;
	
	@Id
	@Column(name = "SOURCE_TABLE", insertable = false, updatable = false)
	public String getSourceTable() {
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	@Column(name = "SOURCE_ID", insertable = false, updatable = false)
	public long getSourceId() {
		return sourceId;
	}

	public void setSourceId(long sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "DISPLAY_FLAG", nullable = false)
	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
}
