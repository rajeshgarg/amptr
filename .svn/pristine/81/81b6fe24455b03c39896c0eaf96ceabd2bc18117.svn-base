package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

import java.sql.Date;

/**
 * This <code>AudienceTarget</code> class includes all the attributes related to
 * Audience Target and their getter and setter. The attributes have mapping with
 * <code>MP_AUDIENCE_TARGET</code> table in the AMPT database
 * 
 * @author Pranay.Prabhat
 */
@Entity
@Immutable
@Table(name = "MP_AUDIENCE_TARGET")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AudienceTarget {

	private long sosAudienceTargetId;

	private String name;

	private String adxName;

	private Date deleteDate;

	private String status;

	private AudienceTargetType audienceTargetType;

	@Id
	@Column(name = "SOS_AUD_TARGET_ID", nullable = false)
	public long getSosAudienceTargetId() {
		return sosAudienceTargetId;
	}

	public void setSosAudienceTargetId(long sosAudienceTargetId) {
		this.sosAudienceTargetId = sosAudienceTargetId;
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

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name = "SOS_AUD_TARGET_TYPE_ID", nullable = false, insertable = false, updatable = false)
	public AudienceTargetType getAudienceTargetType() {
		return audienceTargetType;
	}

	public void setAudienceTargetType(AudienceTargetType audienceTargetType) {
		this.audienceTargetType = audienceTargetType;
	}

	@Column(name = "ADX_LOOKUP")
	public String getAdxName() {
		return adxName;
	}

	public void setAdxName(String adxName) {
		this.adxName = adxName;
	}
}
