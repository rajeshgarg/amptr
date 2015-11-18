/**
 * 
 */
package com.nyt.mpt.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nyt.mpt.util.ChangeTrackedDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * This <code>Package</code> class includes all the attributes related to
 * Package and their getter and setter. The attributes have mapping with
 * <code>MP_PACKAGE</code> table in the AMPT database
 * 
 * @author surendra.singh
 */

@Entity
@Table(name = "MP_PACKAGE")
public class Package extends ChangeTrackedDomain {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private User owner;

	private Date validFrom;

	private Date validTo;

	private boolean active = true;

	private boolean breakable;

	private double budget;

	private String comments;

	private Set<LineItem> packagelineItemSet = new LinkedHashSet<LineItem>();

	private List<PackageSalesCategoryAssoc> packageSalesCategoryAssoc = new ArrayList<PackageSalesCategoryAssoc>();

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PACKAGE_SEQUENCE")
	@SequenceGenerator(name = "PACKAGE_SEQUENCE", sequenceName = "MP_PACKAGE_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "OWNER_ID", nullable = false, updatable = true, insertable = true)
	public User getOwner() {
		return owner;
	}

	public void setOwner(final User owner) {
		this.owner = owner;
	}

	public void setValidTo(final Timestamp validTo) {
		this.validTo = validTo;
	}

	@Column(name = "VALID_FROM")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(final Date validFrom) {
		this.validFrom = validFrom;
	}

	@Column(name = "VALID_TO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(final Date validTo) {
		this.validTo = validTo;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@Column(name = "IS_BREAKABLE")
	public boolean isBreakable() {
		return breakable;
	}

	public void setBreakable(final boolean breakable) {
		this.breakable = breakable;
	}

	@Column(name = "BUDGET")
	public double getBudget() {
		return budget;
	}

	public void setBudget(final double budget) {
		this.budget = budget;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@OneToMany(mappedBy = "packageObject", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<PackageSalesCategoryAssoc> getPackageSalesCategoryAssoc() {
		return packageSalesCategoryAssoc;
	}

	public void setPackageSalesCategoryAssoc(List<PackageSalesCategoryAssoc> packageSalesCategoryAssocs) {
		packageSalesCategoryAssoc = packageSalesCategoryAssocs;
	}

	@OneToMany(mappedBy = "packageObj", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@Where(clause = "PROPOSAL_ID is NULL AND IS_ACTIVE = 1")
	@OrderBy(clause = "LINEITEM_ID")
	public Set<LineItem> getPackagelineItemSet() {
		return packagelineItemSet;
	}

	public void setPackagelineItemSet(final Set<LineItem> packagelineItemSet) {
		this.packagelineItemSet = packagelineItemSet;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("Id", id).append("Name", name).append("Owner", owner).append(
				"Valid From", validFrom).append("Valid To", validTo).append("Active", active).append("Breakable", breakable).append("Budget", budget)
				.append("Line Item Set", packagelineItemSet).toString();
	}

	/**
	 * @param salesCategoryAssoc
	 */
	public void addSalesCategoryAssoc(final PackageSalesCategoryAssoc salesCategoryAssoc) {
		if (packageSalesCategoryAssoc == null) {
			packageSalesCategoryAssoc = new ArrayList<PackageSalesCategoryAssoc>();
		}
		salesCategoryAssoc.setPackageObject(this);
		packageSalesCategoryAssoc.add(salesCategoryAssoc);
	}
}
