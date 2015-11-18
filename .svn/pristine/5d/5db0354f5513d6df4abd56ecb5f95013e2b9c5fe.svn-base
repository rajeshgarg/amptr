package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import java.sql.Date;

/**
 * This <code>AudienceTargetType</code> class includes all the attributes
 * related to Audience Target Type and their getter and setter. The attributes
 * have mapping with <code>MP_AUDIENCE_TARGET_TYPE</code> table in the AMPT
 * database
 * 
 * @author Pranay.Prabhat
 */
@Entity
@Immutable
@Table(name = "MP_AUDIENCE_TARGET_TYPE")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AudienceTargetType {

	private long sosAudienceTargetTypeId;

	private String name;

	private Date deleteDate;

	private String adxName;

	private String yieldexName;

	@Id
	@Column(name = "SOS_AUD_TARGET_TYPE_ID", nullable = false)
	public long getSosAudienceTargetTypeId() {
		return sosAudienceTargetTypeId;
	}

	public void setSosAudienceTargetTypeId(long sosAudienceTargetTypeId) {
		this.sosAudienceTargetTypeId = sosAudienceTargetTypeId;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@Column(name = "ADX_NAME")
	public String getAdxName() {
		return adxName;
	}

	public void setAdxName(String adxName) {
		this.adxName = adxName;
	}

	@Column(name = "YIELDEX_NAME")
	public String getYieldexName() {
		return yieldexName;
	}

	public void setYieldexName(String yieldexName) {
		this.yieldexName = yieldexName;
	}

}
