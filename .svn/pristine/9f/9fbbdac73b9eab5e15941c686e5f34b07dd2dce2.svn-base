/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This <code>TargetElement</code> class includes all the attributes related to
 * Target Element and their getter and setter. The attributes have mapping with
 * <code>TAR_ELEMENTS</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "TAR_ELEMENTS")
public class TargetElement {

	private long targetElementId;

	private String elementName;

	private String code;

	private TargetType targetType;

	@Id
	@Column(name = "ELEMENT_ID", updatable = false, insertable = false)
	public long getTargetElementId() {
		return targetElementId;
	}

	public void setTargetElementId(long targetElementId) {
		this.targetElementId = targetElementId;
	}

	@Column(name = "NAME", updatable = false, insertable = false)
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elamentName) {
		this.elementName = elamentName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TYPE_ID", nullable = false, insertable = false, updatable = false)
	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	@Column(name = "LOOK_UP", updatable = false, insertable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
