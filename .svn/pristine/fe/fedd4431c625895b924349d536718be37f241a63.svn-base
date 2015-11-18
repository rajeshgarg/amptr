package com.nyt.mpt.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.nyt.mpt.util.ChangeTrackedDomain;

/**
 * 
 * This <code>Role</code> class includes all the attributes related to Role and
 * their getter and setter. The attributes have mapping with
 * <code>MP_ROLES</code> table in the AMPT database.
 * 
 * @author surendra.singh
 */

@Entity
@Table(name = "MP_ROLES")
public class Role extends ChangeTrackedDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private long roleId;

	private String roleName;

	private String description;

	private boolean active = true;

	private String displayName;

	private String defaultPage;

	private String defaultPageName;

	private String defaultPageUrl;

	private int version;

	private Set<Authority> authorities = new HashSet<Authority>();

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQUENCE")
	@SequenceGenerator(name = "ROLE_SEQUENCE", sequenceName = "MP_ROLE_SEQUENCE", allocationSize = 1)
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "NAME", nullable = false)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "DESCRIPTION", length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "IS_ACTIVE")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "DISPLAYNAME", nullable = false)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "MP_ROLE_AUTHORITIES_ASSOC", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHRITY_ID") })
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Column(name = "DEFAULT_PAGE")
	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	@Column(name = "DEFAULT_PAGE_NAME")
	public String getDefaultPageName() {
		return defaultPageName;
	}

	public void setDefaultPageName(String defaultPageName) {
		this.defaultPageName = defaultPageName;
	}

	@Column(name = "DEFAULT_PAGE_URL")
	public String getDefaultPageUrl() {
		return defaultPageUrl;
	}

	public void setDefaultPageUrl(String defaultPageUrl) {
		this.defaultPageUrl = defaultPageUrl;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(roleId).append(roleName).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			Role other = (Role) obj;
			return new EqualsBuilder().append(this.roleId, other.roleId).append(this.roleName, other.roleName).isEquals();
		}
		return false;
	}
}
