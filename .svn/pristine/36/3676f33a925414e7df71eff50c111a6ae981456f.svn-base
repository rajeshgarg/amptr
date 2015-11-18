/**
 * 
 */
package com.nyt.mpt.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

/**
 * This class is used to provide Proposal client info
 * 
 * @author amandeep.singh
 * 
 */
@Entity
@Table(name = "CUSTOMER")
@Where(clause = "DELETE_DATE is null")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "CUSTOMER_TYPE")
public abstract class ProposalClients {

	private long id;

	private String name;

	private String typeID;

	private Date deleteDate;
	
	private char specialAdvertiser;
	
	private Date specialExpiryDate;

	@Id
	@Column(name = "SOS_CUSTOMER_ID", insertable = false, updatable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME", insertable = false, updatable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CUSTOMER_TYPE", nullable = false, insertable = false, updatable = false)
	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	@Column(name = "DELETE_DATE", nullable = false)
	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	@Column(name = "IS_SPECIAL")
	public char getSpecialAdvertiser() {
		return specialAdvertiser;
	}

	public void setSpecialAdvertiser(char specialAdvertiser) {
		this.specialAdvertiser = specialAdvertiser;
	}

	@Column(name = "IS_SPECIAL_EXPIRY_DATE")
	public Date getSpecialExpiryDate() {
		return specialExpiryDate;
	}

	public void setSpecialExpiryDate(Date specialExpiryDate) {
		this.specialExpiryDate = specialExpiryDate;
	}
}
