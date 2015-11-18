/**
 *
 */
package com.nyt.mpt.form;

import com.nyt.mpt.domain.Role;

/**
 * This form bean is used for user role information
 * 
 * @author Shishir.Srivastava
 * 
 */
public class RoleForm {

	private long roleId;

	private String roleName;

	private String description;

	private boolean active = true;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(final long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(final String roleName) {
		this.roleName = roleName != null ? roleName.trim() : roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description != null ? description.trim() : description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public void populateForm(final Role bean) {
		this.roleId = bean.getRoleId();
		this.roleName = bean.getRoleName();
	}
}
