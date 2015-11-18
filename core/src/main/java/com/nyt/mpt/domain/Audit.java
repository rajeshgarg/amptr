/**
 * 
 */
package com.nyt.mpt.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This <code>Audit</code> class includes all the attributes related to Audit
 * and their getter and setter. The attributes have mapping with
 * <code>MP_AUDITS</code> table in the AMPT database
 * 
 * @author amandeep.singh
 */
@Entity
@Table(name = "MP_AUDITS")
public class Audit {

	private long id;

	private Date auditDate;

	private String who;

	private String entityName;

	private long entityId;

	private String message;

	private long parentEntityId;

	public Audit() {
		super();
	}

	public Audit(long id, Date auditDate, String who, String entityName, long entityId, long parentEntityId, String message) {
		super();
		this.id = id;
		this.auditDate = auditDate;
		this.who = who;
		this.entityName = entityName;
		this.entityId = entityId;
		this.message = message;
		this.parentEntityId = parentEntityId;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_SEQUENCE")
	@SequenceGenerator(name = "AUDIT_SEQUENCE", sequenceName = "MP_AUDITS_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_DATE")
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	@Column(name = "WHO")
	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	@Column(name = "ENTITY_NAME")
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Column(name = "ENTITY_ID")
	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "PARENT_ENTITY_ID")
	public long getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(long parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

}
