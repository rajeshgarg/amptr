/**
 * 
 */
package com.nyt.mpt.form;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * This <code>DashboardForm</code> bean is being used for showing information in the dash-board graphs
 * 
 * @author abhijeet.jethalia
 */

public class DashboardForm implements Comparable<DashboardForm> {

	private long userId;

	private String userName;

	private int redCount;

	private int greenCount;

	public long getUserId() {
		return userId;
	}

	public void setUserId(final long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName != null ? userName.trim() : userName;
	}

	public int getRedCount() {
		return redCount;
	}

	public void setRedCount(final int redCount) {
		this.redCount = redCount;
	}

	public int getGreenCount() {
		return greenCount;
	}

	public void setGreenCount(final int greenCount) {
		this.greenCount = greenCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final DashboardForm o) {
		return new CompareToBuilder().append(this.userName, o.userName).toComparison();
	}
}
