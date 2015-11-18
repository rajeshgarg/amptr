/**
 * 
 */
package com.nyt.mpt.form;

import java.io.Serializable;

import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>MultipleCalendarForm</code> has all the attributes related to Multiple Calendar to be shown on the UI
 * 
 * @author shipra.bansal
 */

public class MultipleCalendarForm extends BaseForm<UserFilter> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long filterId;

	private String userName;

	private String filterName;

	private String filterData;

	public long getFilterId() {
		return filterId;
	}

	public void setFilterId(final long filterId) {
		this.filterId = filterId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(final String filterName) {
		this.filterName = filterName;
	}

	public String getFilterData() {
		return filterData;
	}

	public void setFilterData(final String filterData) {
		this.filterData = filterData;
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	public void populateForm(final UserFilter bean) {
		this.setFilterData(bean.getFilterData());
		this.setFilterId(bean.getFilterId());
		this.setFilterName(bean.getFilterName());
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public UserFilter populate(final UserFilter bean) {
		bean.setFilterData(this.getFilterData());
		bean.setFilterName(this.getFilterName());
		bean.setFilterId(this.getFilterId());
		bean.setUserId(SecurityUtil.getUser().getUserId());
		bean.setFilterType(UserFilterTypeEnum.MULTIPLE_CALENDAR.name());
		return bean;
	}
}
