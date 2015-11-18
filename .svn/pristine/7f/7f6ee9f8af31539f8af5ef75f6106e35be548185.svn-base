/**
 *
 */
package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This <code>CronJobConfiguration</code> class includes all the attributes
 * related to CronJob Configuration and their getter and setter. The attributes
 * have mapping with <code>MP_JOB_CONFIG</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@Table(name = "MP_JOB_CONFIG")
public class CronJobConfiguration {

	private long id;
	private String name;
	private String hostName;
	private boolean active;
	private Integer recordFetchDurationInMin;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "JOB_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SERVER_HOST_NAME")
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Column(name = "STATUS")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "RECORD_FETCH_DURATION_IN_MIN")
	public Integer getRecordFetchDurationInMin() {
		return recordFetchDurationInMin;
	}

	public void setRecordFetchDurationInMin(Integer recordFetchDurationInMin) {
		this.recordFetchDurationInMin = recordFetchDurationInMin;
	}
}
