/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * This <code>CountryRegionMap</code> class includes all the attributes related
 * to Country Region Mapping and their getter and setter. The attributes have
 * mapping with <code>MP_COUNTRY_REGION_MAPPING</code> table in the AMPT
 * database
 * 
 * @author surendra.singh
 */
@Entity
@Immutable
@Table(name = "MP_COUNTRY_REGION_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CountryRegionMap {

	private Id id;
	private String countryCode;
	private String countryName;
	private String regioName;

	@EmbeddedId
	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	@Column(name = "COUNTRY_CODE")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "REGION_NAME")
	public String getRegioName() {
		return regioName;
	}

	public void setRegioName(String regioName) {
		this.regioName = regioName;
	}

	@Embeddable
	@SuppressWarnings("serial")
	public static class Id implements Serializable {

		private long countryId;

		private long regionId;

		public Id() {
			super();
		}

		public Id(long countryId, long regionId) {
			this.countryId = countryId;
			this.regionId = regionId;
		}

		@Column(name = "COUNTRY_ID")
		public long getCountryId() {
			return countryId;
		}

		public void setCountryId(long countryId) {
			this.countryId = countryId;
		}

		@Column(name = "REGION_ID")
		public long getRegionId() {
			return regionId;
		}

		public void setRegionId(long regionId) {
			this.regionId = regionId;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 31).append(this.regionId).append(this.countryId).hashCode();
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
				final Id other = (Id) obj;
				return new EqualsBuilder().append(this.regionId, other.getRegionId()).append(this.countryId, other.getCountryId()).isEquals();
			}
			return false;
		}
	}
}
