/**
 * 
 */
package com.nyt.mpt.domain.sos;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This <code>TargetType</code> class includes all the attributes related to
 * TargetType and their getter and setter. The attributes have mapping with
 * <code>TAR_TYPE</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "TAR_TYPE")
public class TargetType {

	private long typeId;

	private String name;

	private List<TargetElement> targetElements;

	@Id
	@Column(name = "TYPE_ID", updatable = false, insertable = false)
	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	@Column(name = "NAME", updatable = false, insertable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "targetType")
	public List<TargetElement> getTargetElements() {
		return targetElements;
	}

	public void setTargetElements(List<TargetElement> targetElements) {
		this.targetElements = targetElements;
	}
}
