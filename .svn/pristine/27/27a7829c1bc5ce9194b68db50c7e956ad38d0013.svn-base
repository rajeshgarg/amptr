package com.nyt.mpt.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.nyt.mpt.util.ChangeTrackedDomain;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This <code>User</code> class includes all the attributes related to User and
 * their getter and setter. The attributes have mapping with
 * <code>MP_USERS</code> table in the AMPT database. Since there are not a lot
 * of users in the system, all user associations are eager fetched
 * 
 * @author surendra.singh
 */

@Entity
@Table(name = "MP_USERS")
public class User extends ChangeTrackedDomain implements UserDetails {

	private static final long serialVersionUID = 1L;

	private long userId;

	private String loginName;

	private String firstName;

	private String lastName;

	private String email;

	private boolean active = true;

	private boolean global;

	private String address1;

	private String address2;

	private String city;

	private String state;

	private String zip;

	private String telephone;

	private String mobile;

	private String faxNo;

	private int version;

	private Set<SalesCategory> salesCategories = new HashSet<SalesCategory>();

	private Set<Role> userRoles = new LinkedHashSet<Role>();

	private boolean displayOnWarRoom = false;

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQUENCE")
	@SequenceGenerator(name = "USER_SEQUENCE", sequenceName = "MP_USER_SEQUENCE", allocationSize = 1)
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.ALL })
	public Set<SalesCategory> getSalesCategories() {
		return salesCategories;
	}

	public void setSalesCategories(Set<SalesCategory> salesCategories) {
		this.salesCategories = salesCategories;
	}

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = "MP_USER_ROLE_ASSOC", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	public Set<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}

	public void addRole(Role role) {
		if (userRoles == null) {
			userRoles = new LinkedHashSet<Role>();
		}
		userRoles.add(role);
	}

	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "EMAIL_ID", unique = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_GLOBAL")
	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Column(name = "LOGIN_NAME", nullable = false)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "FAX", nullable = true)
	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	@Column(name = "ADDRESS_1", nullable = true)
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "ADDRESS_2", nullable = true)
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "CITY", nullable = true)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "STATE", nullable = true)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "ZIP", nullable = true)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "TELEPHONE", nullable = true)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "MOBILE", nullable = true)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	@Transient
	public String getPassword() {
		return null;
	}

	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		/**
		 * This is only for access the application.
		 */
		if (this.userRoles != null && !this.userRoles.isEmpty()) {
			authSet.add(new GrantedAuthorityImpl("USER"));
		}
		for (Role roles : this.getUserRoles()) {
			for (Authority authority : roles.getAuthorities()) {
				authSet.add(new GrantedAuthorityImpl(authority.getValue()));
			}
			authSet.add(new GrantedAuthorityImpl(roles.getRoleName()));
		}
		return authSet;
	}

	@Override
	@Transient
	public String getUsername() {
		return loginName;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return this.active;
	}

	@Transient
	public String getFullName() {
		return this.lastName == null ? this.firstName : this.firstName + ConstantStrings.SPACE + this.lastName;
	}

	@Column(name = "IS_DISPLAY_ON_WARROOM")
	public boolean isDisplayOnWarRoom() {
		return displayOnWarRoom;
	}

	public void setDisplayOnWarRoom(boolean displayOnWarRoom) {
		this.displayOnWarRoom = displayOnWarRoom;
	}
}
