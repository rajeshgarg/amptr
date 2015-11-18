package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;





import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>EmailSchedule</code> class includes all the attributes related to
 * EmailSchedule and their getter and setter. The attributes have mapping with
 * <code>MP_EMAIL_SCHEDULE</code> table in the AMPT database
 * 
 * @author Gurditta.Garg
 */

@Entity
@Table(name = "MP_EMAIL_SCHEDULE")
public class EmailSchedule extends ChangeTrackedDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private long emailScheduleId;
	
	private Long productId;
	
	private Long salesTargetId;
	
	private String prodcutName;
	
	private String salesTargetName;
	
	private List<EmailScheduleDetails> emailSchedules;

	private boolean active = true;

	private int version;

	/**
	 * Gets the email schedule id.
	 *
	 * @return the email schedule id
	 */
	@Id
	@Column(name = "EMAIL_SCHEDULE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAIL_SEQUENCE")
	@SequenceGenerator(name = "EMAIL_SEQUENCE", sequenceName = "MP_EMAIL_SCHEDULE_SEQUENCE", allocationSize = 1)
	public long getEmailScheduleId() {
		return emailScheduleId;
	}
	

	/**
	 * Sets the email schedule id.
	 *
	 * @param emailScheduleId the new email schedule id
	 */
	public void setEmailScheduleId(final Long emailScheduleId) {
		this.emailScheduleId =emailScheduleId;
	}
	
	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	@Column(name = "PRODUCT_ID", nullable = false)
	public Long getProductId() {
		return productId;
	}	
	
	/**
	 * Sets the product id.
	 *
	 * @param productId the new product id
	 */
	public void setProductId(final Long productId) {
		this.productId = productId;
	}
	
	/**
	 * Gets the sales target id.
	 *
	 * @return the sales target id
	 */
	@Column(name = "SALES_TARGET_ID", nullable = false)
	public Long getSalesTargetId() {
		return salesTargetId;
	}
	
	/**
	 * Sets the sales target id.
	 *
	 * @param salesTargetId the new sales target id
	 */
	public void setSalesTargetId(final Long salesTargetId) {
		this.salesTargetId = salesTargetId;
	}
	
	/**
	 * Gets the prodcut name.
	 *
	 * @return the prodcut name
	 */
	@Column(name = "PRODUCT_NAME", nullable = false)
	public String getProdcutName() {
		return prodcutName;
	}
	
	/**
	 * Sets the prodcut name.
	 *
	 * @param prodcutName the new prodcut name
	 */
	public void setProdcutName(final String prodcutName) {
		this.prodcutName = prodcutName;
	}
	
	/**
	 * Gets the sales target name.
	 *
	 * @return the sales target name
	 */
	@Column(name = "SALES_TARGET_NAME")
	public String getSalesTargetName() {
		return salesTargetName;
	}
	
	/**
	 * Sets the sales target name.
	 *
	 * @param salesTargetName the new sales target name
	 */
	public void setSalesTargetName(final String salesTargetName) {
		this.salesTargetName = salesTargetName;
	}
	
	/**
	 * Gets the email schedules.
	 *
	 * @return the email schedules
	 */

	@OneToMany(mappedBy = "emailSchedule", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<EmailScheduleDetails> getEmailSchedules() {
		return emailSchedules;
	}
	
	/**
	 * Sets the email schedules.
	 *
	 * @param emailSchedules the new email schedules
	 */
	public void setEmailSchedules(final List<EmailScheduleDetails> emailSchedules) {
		this.emailSchedules = emailSchedules;
	}
	
	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(final int version) {
		this.version = version;
	}
}
