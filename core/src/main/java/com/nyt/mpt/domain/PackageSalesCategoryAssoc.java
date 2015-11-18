/**
 * 
 */
package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * This <code>PackageSalesCategoryAssoc</code> class includes all the attributes
 * related to Package SalesCategory Association and their getter and setter. The
 * attributes have mapping with <code>MP_PACKAGE_SALESCATEGORY_ASSOC</code>
 * table in the AMPT database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_PACKAGE_SALESCATEGORY_ASSOC")
public class PackageSalesCategoryAssoc {

	private long id;
	private long sosSalesCategoryId;
	private String sosSalesCategoryName;
	private Package packageObject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PACKAGEID", nullable = false, insertable = true, updatable = false)
	public Package getPackageObject() {
		return packageObject;
	}

	public void setPackageObject(Package packageObject) {
		this.packageObject = packageObject;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PACSALES_CATEGORY_SEQ")
	@SequenceGenerator(name = "PACSALES_CATEGORY_SEQ", sequenceName = "MP_PACKAGE_SALES_CAT_SEQ", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "SOS_SALES_CATEGORY_ID", nullable = false)
	public long getSosSalesCategoryId() {
		return sosSalesCategoryId;
	}

	public void setSosSalesCategoryId(long sosSalesCategoryId) {
		this.sosSalesCategoryId = sosSalesCategoryId;
	}

	@Column(name = "SOS_SALES_CATEGORY_NAME")
	public String getSosSalesCategoryName() {
		return sosSalesCategoryName;
	}

	public void setSosSalesCategoryName(String sosSalesCategoryName) {
		this.sosSalesCategoryName = sosSalesCategoryName;
	}
}
