
package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>EmailScheduleDetails</code> class includes all the attributes related to
 * EmailScheduleDetails and their getter and setter. The attributes have mapping with
 * <code>MP_EMAIL_SCHEDULE_DETAILS</code> table in the AMPT database
 * 
 * @author Gurditta.Garg
 */

@Entity
@Table(name = "MP_EMAIL_SCHEDULE_DETAILS")
public class EmailScheduleDetails extends ChangeTrackedDomain  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long emailScheduleDetailsId;
	
	private boolean forever;
	
	private String frequency;
	
	private String weekdays;
	
	private Date startDate;
	
	private Date endDate;
	
	private EmailSchedule emailSchedule;

	@Id
	@Column(name = "EMAIL_SCHEDULE_DETAILS_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAILS_SEQUENCE")
	@SequenceGenerator(name = "EMAILS_SEQUENCE", sequenceName = "MP_EMAIL_SCHEDULE_DETAILS_SEQ", allocationSize = 1)
	public long getEmailScheduleDetailsId() {
		return emailScheduleDetailsId;
	}
	
	public void setEmailScheduleDetailsId(final long emailScheduleDetailsId) {
		this.emailScheduleDetailsId = emailScheduleDetailsId;
	}

	@ManyToOne(targetEntity = EmailSchedule.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_SCHEDULE_ID")
	public EmailSchedule getEmailSchedule() {
		return emailSchedule;
	}

	public void setEmailSchedule(final EmailSchedule emailSchedule) { 
		this.emailSchedule = emailSchedule;
	}

	@Column(name = "FOREVER")
	public boolean isForever() {
		return forever;
	}
	
	public void setForever(final boolean forever) {
		this.forever = forever;
	}
	
	@Column(name = "RECURRENCE_FREQUENCY")		
	public String getFrequency() {
		return frequency;
	}
	
	public void setFrequency(final String frequency) {
		this.frequency = frequency;
	}
	
	@Column(name = "WEEKDAY")
	public String getWeekdays() {
		return weekdays;
	}
	
	public void setWeekdays(final String weekdays) {
		this.weekdays = weekdays;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}
	
}
