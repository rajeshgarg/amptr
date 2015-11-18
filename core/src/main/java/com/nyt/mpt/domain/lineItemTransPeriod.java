package com.nyt.mpt.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This class is used for providing Line Item transPeriod details
 * @author Garima.garg
 *
 */
@Entity
@Immutable
@Table(name = "LINEITEM_TRANS_PERIOD")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class lineItemTransPeriod implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long periodId;
	
	private Date deleteDate;
	
	private lineItemTransPeriodType transPeriodType;
	
	private Date startDate;
	
	private Date endDate;
	
	private String description;

	@Id
	@Column(name = "SOS_PERIOD_ID")
	public long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(long periodId) {
		this.periodId = periodId;
	}

	@Column(name = "DELETE_DATE")
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	@ManyToOne
	@JoinColumn(name="SOS_PERIOD_TYPE_ID", nullable = false, insertable = true, updatable = false)
	public lineItemTransPeriodType getTransPeriodType() {
		return transPeriodType;
	}

	public void setTransPeriodType(lineItemTransPeriodType transPeriodType) {
		this.transPeriodType = transPeriodType;
	}

	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
