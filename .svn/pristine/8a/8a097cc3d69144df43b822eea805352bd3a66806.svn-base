/**
 * 
 */
package com.nyt.mpt.domain.sos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This <code>Countries</code> class includes all the attributes related to
 * Countries and their getter and setter. The attributes have mapping with
 * <code>COUNTRIES</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "COUNTRIES")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Countries {

	private long id;

	private String name;

	private String code;

	@Id
	@Column(name = "ID", updatable = false, insertable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME", updatable = false, insertable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TWO_LETTER_CODE", updatable = false, insertable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
