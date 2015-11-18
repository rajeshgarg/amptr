package com.nyt.mpt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.nyt.mpt.util.ChangeTrackedDomain;
import org.hibernate.annotations.Type;

/**
 * This <code>UserFilter</code> class includes all the attributes related to
 * UserFilter and their getter and setter. The attributes have mapping with
 * <code>MP_USER_FILTER</code> table in the AMPT database
 * 
 * @author surendra.singh
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "MP_USER_FILTER")
public class UserFilter extends ChangeTrackedDomain {

	private long filterId;

	private long userId;

	private String filterName;

	private String filterData;

	private int version;

	private String filterType;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILTER_SEQUENCE")
	@SequenceGenerator(name = "FILTER_SEQUENCE", sequenceName = "MP_FILTER_SEQUENCE", allocationSize = 1)
	public long getFilterId() {
		return filterId;
	}

	public void setFilterId(long filterId) {
		this.filterId = filterId;
	}

	@Column(name = "USER_ID")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Column(name = "FILTER_NAME")
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name = "FILTER_DATA")
	public String getFilterData() {
		return filterData;
	}

	public void setFilterData(String filterData) {
		this.filterData = filterData;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "FILTER_TYPE")
	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(final String filterType) {
		this.filterType = filterType;
	}

}
