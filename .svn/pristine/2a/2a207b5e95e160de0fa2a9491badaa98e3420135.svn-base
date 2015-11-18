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

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * This <code>CronJobSchedule</code> class includes all the attributes related
 * to Cron Jobs Scheduling and their getter and setter. The attributes have
 * mapping with <code>MP_JOB_SCHEDULE</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MP_JOB_SCHEDULE")
public class CronJobSchedule extends ChangeTrackedDomain {

	private long id;
	private String jobName;
	private String scheduledBy;
	private Date scheduleDate;

	public CronJobSchedule() {
		super();
	}

	public CronJobSchedule(String jobName, Date scheduleDate) {
		this.jobName = jobName;
		this.scheduleDate = scheduleDate;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_SCHEDULE_SEQUENCE")
	@SequenceGenerator(name = "JOB_SCHEDULE_SEQUENCE", sequenceName = "MP_JOB_SCHEDULE_SEQUENCE", allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "JOB_NAME")
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "SCHEDULE_BY")
	public String getScheduledBy() {
		return scheduledBy;
	}

	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}

	@Column(name = "SCHEDULE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
}
