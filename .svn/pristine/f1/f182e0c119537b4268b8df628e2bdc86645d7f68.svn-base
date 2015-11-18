/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>Authority</code> class includes all the attributes related to
 * Authority and their getter and setter. The attributes have mapping with
 * <code>MP_AUTHORITIES</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_AUTHORITIES")
public class Authority extends ChangeTrackedDomain implements Serializable, Comparable<Authority> {

	private static final long serialVersionUID = 1L;

	private long authorityId;

	private String displayName;

	private String value;

	private boolean active = true;

	private int version;

	@Id
	@Column(name = "AUTHORITY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORITIES_SEQUENCE")
	@SequenceGenerator(name = "AUTHORITIES_SEQUENCE", sequenceName = "MP_AUTHORITIES_SEQUENCE", allocationSize = 1)
	public long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(long id) {
		this.authorityId = id;
	}

	@Column(name = "DISPLAY_NAME", nullable = false)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "VALUE", nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(authorityId).append(value).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			Authority other = (Authority) obj;
			return new EqualsBuilder().append(this.authorityId, other.authorityId).append(this.value, other.value).isEquals();
		}
		return false;
	}

	@Override
	public int compareTo(Authority o) {
		return new CompareToBuilder().append(this.value, o.value).append(this.authorityId, o.authorityId).toComparison();
	}
}
