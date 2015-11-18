/**
 * 
 */
package com.nyt.mpt.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Where;

/**
 * This class is used to capture position for a product
 * 
 * @author rakesh.tewari
 * 
 */
@Entity
@Immutable
@Table(name = "POSITION")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Position implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long adxPositionID;
	
	private String name;

	@Id
	@Column(name = "ADX_POSITION_ID")
	public Long getAdxPositionID() {
		return adxPositionID;
	}

	public void setAdxPositionID(Long adxPositionID) {
		this.adxPositionID = adxPositionID;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
