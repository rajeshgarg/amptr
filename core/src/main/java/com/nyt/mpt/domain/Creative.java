package com.nyt.mpt.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.nyt.mpt.util.ChangeTrackedDomain;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This <code>Creative</code> class includes all the attributes related to
 * Creative and their getter and setter. The attributes have mapping with
 * <code>MP_CREATIVES</code> table in the AMPT database
 * 
 * @author amandeep.singh
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MP_CREATIVES")
public class Creative extends ChangeTrackedDomain implements Comparable<Creative> {

	private long creativeId;

	private String name;

	private int width;

	private int height;

	private String type;

	private int version;

	private String description;

	private boolean active = true;

	private Set<CreativeAttributeValue> attributeValues = new HashSet<CreativeAttributeValue>();

	private Integer width2;

	private Integer height2;

	@Id
	@Column(name = "CREATIVE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREATIVE_SEQUENCE")
	@SequenceGenerator(name = "CREATIVE_SEQUENCE", sequenceName = "MP_CREATIVES_SEQUENCE", allocationSize = 1)
	public long getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "CREATIVE_TYPE", insertable = true, updatable = true, nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "CREATIVE_WIDTH")
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Column(name = "CREATIVE_HEIGHT")
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@OneToMany(mappedBy = "creative", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public Set<CreativeAttributeValue> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(Set<CreativeAttributeValue> attributesSet) {
		this.attributeValues = attributesSet;
	}

	@Column(name = "CREATIVE_WIDTH2")
	public Integer getWidth2() {
		return width2;
	}

	public void setWidth2(Integer width2) {
		this.width2 = width2;
	}

	@Column(name = "CREATIVE_HEIGHT2")
	public Integer getHeight2() {
		return height2;
	}

	public void setHeight2(Integer height2) {
		this.height2 = height2;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("Name", name)
				.append("Description", description)
				.append("Width", width)
				.append("Height", height)
				.append("Type", type)
				.append("Width2", width2)
				.append("Height2", height2)
				.append("Creative Id", creativeId).toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(creativeId).append(name).append(type).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			Creative other = (Creative) obj;
			return new EqualsBuilder().append(this.creativeId, other.creativeId).append(this.name, other.name).append(this.type, other.getType()).isEquals();
		}
		return false;
	}

	@Override
	public int compareTo(Creative o) {
		return new CompareToBuilder().append(this.name, o.name).toComparison();
	}

	@Transient
	public String getShortDescription() {
		if (this.name == null) {
			return ConstantStrings.EMPTY_STRING;
		} else {
			if (this.height2 != null && this.width2 != null) {
				return this.name + ConstantStrings.SPACE + ConstantStrings.OPENING_BRACKET + this.width + ConstantStrings.SPACE + ConstantStrings.CROSS_SIGN 
						+ ConstantStrings.SPACE + this.height + ConstantStrings.SPACE + ConstantStrings.OR
						+ ConstantStrings.SPACE + this.width2 + ConstantStrings.SPACE + ConstantStrings.CROSS_SIGN 
						+ ConstantStrings.SPACE + this.height2 + ConstantStrings.CLOSING_BRACKET;
			} else {
				return this.name + ConstantStrings.SPACE + ConstantStrings.OPENING_BRACKET + this.width + ConstantStrings.SPACE 
						+ ConstantStrings.CROSS_SIGN + ConstantStrings.SPACE + this.height + ConstantStrings.CLOSING_BRACKET;
			}
		}
	}

	@Transient
	public String getShortDescriptionForSOS() {
		if (this.name == null) {
			return ConstantStrings.EMPTY_STRING;
		} else {
			return this.width + ConstantStrings.CROSS_SIGN + this.height;
		}
	}
}
