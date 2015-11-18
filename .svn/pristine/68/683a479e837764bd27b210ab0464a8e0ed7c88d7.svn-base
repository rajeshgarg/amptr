/**
 * 
 */
package com.nyt.mpt.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ReservationStatus;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>LineItemReservations</code> class includes all the attributes
 * related to LineItem Reservations and their getter and setter. The attributes
 * have mapping with <code>MP_LINE_ITEMS_RESERVATION</code> table in the AMPT
 * database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_LINE_ITEMS_RESERVATION")
public class LineItemReservations {

	private Long id;

	private LineItem proposalLineItem;

	private ReservationStatus status;

	private Date expirationDate;

	private Date lastRenewedOn;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private String createdBy;
	
	private String modifiedBy;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESERVATION_SEQ")
	@SequenceGenerator(name = "RESERVATION_SEQ", sequenceName = "MP_LINE_ITEM_RESERVATION_SEQ", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINEITEM_ID", nullable = false, insertable = true, updatable = false)
	public LineItem getProposalLineItem() {
		return proposalLineItem;
	}

	public void setProposalLineItem(LineItem proposalLineItem) {
		this.proposalLineItem = proposalLineItem;
	}

	@Column(name = "STATUS", columnDefinition = "string", insertable = true, updatable = true, nullable = false)
	@Type(type = "com.nyt.mpt.domain.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "com.nyt.mpt.util.enums.ReservationStatus"),
			@Parameter(name = "identifierMethod", value = "name"), @Parameter(name = "valueOfMethod", value = "findByName") })
	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRATION_DATE")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	@Column(name = "LAST_RENEWED_ON")
	public Date getLastRenewedOn() {
		return lastRenewedOn;
	}

	public void setLastRenewedOn(Date lastRenewedOn) {
		this.lastRenewedOn = lastRenewedOn;
	}

	@Column(name = "created_date")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = (createdDate == null) ? DateUtil.getCurrentDate() : createdDate;
	}

	@Column(name = "modified_date")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = DateUtil.getCurrentDate();
	}

	@Column(name = "created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = (createdBy == null) ? SecurityUtil.getUser().getLoginName() : createdBy;
	}

	@Column(name = "modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = (modifiedBy == null) ? SecurityUtil.getUser().getLoginName() : modifiedBy;
	}
}
