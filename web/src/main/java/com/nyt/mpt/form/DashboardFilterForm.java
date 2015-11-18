/**
 * 
 */
package com.nyt.mpt.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>DashboardFilterForm</code> contains the information regarding the filter in the dash-board view
 * 
 * @author surendra.singh
 */

public class DashboardFilterForm extends BaseForm<UserFilter> {

	private static final String USER = "user";

	private static final String SALES_CATEGORY = "salesCategory";

	private long filterId;

	private String dashboardFilterName;

	private String dueDate;

	private String[] priority;

	private String[] salesCategory;

	private String[] user;

	private boolean mySalesCategory;

	private static final Logger LOGGER = Logger.getLogger(DashboardFilterForm.class);

	public String getDashboardFilterName() {
		return dashboardFilterName;
	}

	public void setDashboardFilterName(final String dashboardFilterName) {
		this.dashboardFilterName = dashboardFilterName != null ? dashboardFilterName.trim() : dashboardFilterName;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(final String dueDate) {
		this.dueDate = dueDate;
	}

	public String[] getPriority() {
		return priority;
	}

	public void setPriority(final String[] priority) {
		this.priority = priority;
	}

	public String[] getSalesCategory() {
		return salesCategory;
	}

	public void setSalesCategory(final String[] salesCategory) {
		this.salesCategory = salesCategory;
	}

	public String[] getUser() {
		return user;
	}

	public void setUser(final String[] user) {
		this.user = user;
	}

	public long getFilterId() {
		return filterId;
	}

	public void setFilterId(final long filterId) {
		this.filterId = filterId;
	}

	public boolean isMySalesCategory() {
		return mySalesCategory;
	}

	public void setMySalesCategory(final boolean mySalesCategory) {
		this.mySalesCategory = mySalesCategory;
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populate(java.lang.Object)
	 */
	@Override
	public UserFilter populate(final UserFilter bean) {
		bean.setFilterId(this.filterId);
		bean.setFilterName(this.dashboardFilterName);
		bean.setUserId(SecurityUtil.getUser().getUserId());
		bean.setFilterType(UserFilterTypeEnum.DASHBOARD.name());
		bean.setFilterData(convertFilterDataToJson());
		return bean;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.form.BaseForm#populateForm(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void populateForm(final UserFilter bean) {
		this.filterId = bean.getFilterId();
		this.dashboardFilterName = bean.getFilterName();

		final ObjectMapper mapper = new ObjectMapper();
		try {
			final Map<String, Object> dataMap = mapper.readValue(bean.getFilterData(), HashMap.class);
			if (dataMap.get(ConstantStrings.MY_SALES_CATEGORY) != null) {
				this.mySalesCategory = (Boolean) dataMap.get(ConstantStrings.MY_SALES_CATEGORY);
			}
			if (dataMap.get(SALES_CATEGORY) != null) {
				this.salesCategory = ((ArrayList<String>) dataMap.get(SALES_CATEGORY)).toArray(new String[0]);
			}
			if (dataMap.get(ConstantStrings.PRIORITY) != null) {
				this.priority = ((ArrayList<String>) dataMap.get(ConstantStrings.PRIORITY)).toArray(new String[0]);
			}
			if (dataMap.get(USER) != null) {
				this.user = ((ArrayList<String>) dataMap.get(USER)).toArray(new String[0]);
			}
			if (dataMap.get(ConstantStrings.DUE_DATE) != null) {
				this.dueDate = (String) dataMap.get(ConstantStrings.DUE_DATE);
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private String convertFilterDataToJson() {
		final Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put(ConstantStrings.MY_SALES_CATEGORY, this.mySalesCategory);
		dataMap.put(SALES_CATEGORY, this.salesCategory);
		dataMap.put(ConstantStrings.PRIORITY, this.priority);
		dataMap.put(ConstantStrings.DUE_DATE, this.dueDate);
		dataMap.put(USER, this.user);

		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(dataMap);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return ConstantStrings.EMPTY_STRING;
	}
}