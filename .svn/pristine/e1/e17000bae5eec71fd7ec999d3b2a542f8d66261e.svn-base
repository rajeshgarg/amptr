package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * This class is used to fetch country information
 * @author manish.kesarwani
 *
 */
@Entity
@Immutable
@Table(name = "COUNTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	
	private String odsName;
	
	private String adxName;
	
	private String region;

	@Id
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "ODS_NAME")
	public String getOdsName() {
		return odsName;
	}

	public void setOdsName(String odsName) {
		this.odsName = odsName;
	}

	@Column(name = "ADX_NAME")
	public String getAdxName() {
		return adxName;
	}

	public void setAdxName(String adxName) {
		this.adxName = adxName;
	}

	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}	


}
