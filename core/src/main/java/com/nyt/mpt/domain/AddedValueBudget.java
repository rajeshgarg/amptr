package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>AddedValueBudget</code> class includes all the attributes related to
 * Added Value Budget and their getter and setter. The attributes have mapping with
 * <code>MP_ADDED_VALUE_BUDGET</code> table in the AMPT database
 * 
 * @author Gurditta.Garg
 */
@Entity
@Table(name = "MP_ADDED_VALUE_BUDGET")
@SuppressWarnings("serial")
public class AddedValueBudget extends ChangeTrackedDomain {
	private long id;
	private double totalInvestment;
	private String notes;
	private double avPercentage;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MP_ADDED_VALUE_BUDGET_SEQUENCE")
	@SequenceGenerator(name = "MP_ADDED_VALUE_BUDGET_SEQUENCE", sequenceName = "MP_ADDED_VALUE_BUDGET_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "TOTAL_INVESTMENT", insertable = true, updatable = true, nullable = false)
	public double getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(double totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	@Column(name = "AV_PERCENTAGE", insertable = true, updatable = true, nullable = false)
	public double getAvPercentage() {
		return avPercentage;
	}

	public void setAvPercentage(double avPercentage) {
		this.avPercentage = avPercentage;
	}

	@Column(name = "NOTES", insertable = true, updatable = true, nullable = false)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
