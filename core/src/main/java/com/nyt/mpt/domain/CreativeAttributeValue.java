/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>CreativeAttributeValue</code> class includes all the attributes
 * related to CreativeAttributeValue and their getter and setter. The attributes
 * have mapping with <code>MP_CREATIVE_ATTRIBUTE_ASSOC</code> table in the AMPT
 * database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_CREATIVE_ATTRIBUTE_ASSOC")
public class CreativeAttributeValue extends ChangeTrackedDomain implements Serializable {
	/**
	 * This class is used for providing Creative ID
	 * @author surendra.singh
	 */
	@Embeddable
	public static class Id implements Serializable {
		public Id() {
		}

		public Id(long creativeId, long attributeId) {
			this.creativeId = creativeId;
			this.attributeId = attributeId;
		}

		private static final long serialVersionUID = 1L;
		private long creativeId;
		private long attributeId;

		/**
		 * @return the creativeId
		 */
		@Column(name = "CREATIVE_ID")
		public long getCreativeId() {
			return creativeId;
		}

		/**
		 * @param 	creativeId
		 * 			the creativeId to set
		 */
		public void setCreativeId(long creativeId) {
			this.creativeId = creativeId;
		}

		/**
		 * @return the attributeId
		 */
		@Column(name = "ATTRIBUTE_ID")
		public long getAttributeId() {
			return attributeId;
		}

		/**
		 * @param 	attributeId
		 * 			the attributeId to set
		 */
		public void setAttributeId(long attributeId) {
			this.attributeId = attributeId;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 31).append(attributeId).append(creativeId).hashCode();
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
				Id other = (Id) obj;
				return new EqualsBuilder().append(this.attributeId, other.attributeId).append(this.creativeId, other.creativeId).isEquals();
			}
			return false;
		}
	}

	private Id id;

	private static final long serialVersionUID = 1L;

	private String attributeValue;

	private Attribute attribute;

	private Creative creative;

	private int version;

	public CreativeAttributeValue() {
	}

	public CreativeAttributeValue(Creative creative, Attribute attribute, boolean active, String attributeValue) {
		this.id = new Id(creative.getCreativeId(), attribute.getAttributeId());
		this.creative = creative;
		this.attribute = attribute;
		this.attributeValue = attributeValue;
	}

	/**
	 * @return the id
	 */
	@EmbeddedId
	public Id getId() {
		return id;
	}

	/**
	 * @param 	id
	 *			the id to set
	 */
	public void setId(Id id) {
		this.id = id;
	}

	@Column(name = "ATTRIBUTE_VALUE")
	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@ManyToOne
	@JoinColumn(name = "ATTRIBUTE_ID", nullable = false, insertable = false, updatable = false)
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@ManyToOne
	@JoinColumn(name = "CREATIVE_ID", nullable = false, insertable = false, updatable = false)
	public Creative getCreative() {
		return creative;
	}

	public void setCreative(Creative creative) {
		this.creative = creative;
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
		return new HashCodeBuilder(17, 31).append(this.attribute).append(this.attributeValue).append(this.creative).hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			CreativeAttributeValue other = (CreativeAttributeValue) obj;
			return new EqualsBuilder().append(this.attribute, other.getAttribute()).append(this.attributeValue, other.getAttributeValue()).append(this.creative, other.getCreative()).isEquals();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("Creative Id", creative.getCreativeId()).append("Attribute id", attribute.getAttributeId()).append("Value", attributeValue).toString();
	}

}
