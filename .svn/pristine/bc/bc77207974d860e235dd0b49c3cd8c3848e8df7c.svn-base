/**
 * 
 */
package com.nyt.mpt.domain.sos;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

/**
 * This <code>Region</code> class includes all the attributes related to
 * Region and their getter and setter. The attributes have mapping with
 * <code>REGIONS</code> table in the SOS database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "REGIONS")
@Where(clause = "REGION_TYPE_ID = 6")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Region {

	private long id;

	private String name;

	private Set<Countries> countries = new LinkedHashSet<Countries>();

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

	@OneToMany(targetEntity = Countries.class, fetch = FetchType.LAZY)
	@JoinTable(name = "COUNTRIES_REGIONS", joinColumns = @JoinColumn(name = "REGION_ID"), inverseJoinColumns = @JoinColumn(name = "COUNTRY_ID"))
	public Set<Countries> getCountries() {
		return countries;
	}

	public void setCountries(Set<Countries> countries) {
		this.countries = countries;
	}
}
