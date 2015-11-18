package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>Attribute</code> class includes all the attributes related to
 * Attribute and their getter and setter. The attributes have mapping with
 * <code>MP_ATTRIBUTES</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_ATTRIBUTES")
@SuppressWarnings("serial")
public class Attribute extends ChangeTrackedDomain {

	private long attributeId;

	private String attributeType;

	private String attributeKey;

	private String attributeName;

	private String attributeDescription;

	private String attributeOptionalValue;

	private boolean active = true;

	private int version;

	@Id
	@Column(name = "ATTRIBUTE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTRIBUTE_SEQUENCE")
	@SequenceGenerator(name = "ATTRIBUTE_SEQUENCE", sequenceName = "MP_ATTRIBUTE_SEQUENCE", allocationSize = 1)
	public long getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "TYPE", insertable = true, updatable = true, nullable = false)
	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	@Column(name = "KEY", nullable = false, length = 100)
	public String getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	@Column(name = "NAME", length = 256, nullable = false)
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Column(name = "DESCRIPTION", length = 500)
	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	@Column(name = "OPTIONAL_VALUE", length = 500)
	public String getAttributeOptionalValue() {
		return attributeOptionalValue;
	}

	public void setAttributeOptionalValue(String attributeOptionalValue) {
		this.attributeOptionalValue = attributeOptionalValue;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(attributeId).append(attributeKey).append(attributeName).hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			Attribute other = (Attribute) obj;
			return new EqualsBuilder().append(this.attributeId, other.getAttributeId()).append(this.attributeKey, other.getAttributeKey()).append(this.attributeName, other.getAttributeName()).isEquals();
		}
		return false;
	}
}
