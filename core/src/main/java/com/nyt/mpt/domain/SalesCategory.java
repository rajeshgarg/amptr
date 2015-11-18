package com.nyt.mpt.domain;

import java.io.Serializable;
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
 * This <code>SalesCategory</code> class includes all the attributes related to
 * SalesCategory and their getter and setter. The attributes have mapping with
 * <code>MP_USER_SALES_CATEGORIES</code> table in the AMPT database
 * 
 * @author Shishir.Srivastava
 */
@Entity
@Table(name = "MP_USER_SALES_CATEGORIES")
public class SalesCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;

	private long sosSalesCategoryId;

	private User user;

	@Id
	@Column(name = "CATEGORY_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SALES_CATEGORY_SEQ")
	@SequenceGenerator(name = "USER_SALES_CATEGORY_SEQ", sequenceName = "MP_USER_SALES_CATEGORY_SEQ", allocationSize = 1)
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

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
