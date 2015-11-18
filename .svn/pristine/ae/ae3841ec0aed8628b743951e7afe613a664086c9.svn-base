/**
 *
 */
package com.nyt.mpt.form;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ConstantStrings;

/**
 * This form bean is used for User information
 * 
 * @author Shishir.Srivastava
 * 
 */
public class UserForm extends BaseForm<User> {

	private long userId;

	private String loginName;

	private String firstName;

	private String lastName;

	private String email;

	private boolean global;

	private String roleId;

	private String roleName;

	private String address1;

	private String address2;

	private String city;

	private String state;

	private String zip;

	private String telephone;

	private String mobile;

	private String faxNo;

	private String userRolesAsString = ConstantStrings.EMPTY_STRING;

	private long[] selectedSalesCatList;

	private boolean displayOnWarRoom = false;

	public String getStatusString() {
		if (this.isActive()) {
			return "Active";
		}
		return "Inactive";
	}

	public String getGlobalUserString() {
		if (global) {
			return "Yes";
		}
		return "No";
	}

	public String getUserRolesAsString() {
		return userRolesAsString;
	}

	/**
	 * Only one role will be assigned to user from the application.
	 * @param setRoles
	 * @return
	 */
	public String constructRolesAsString(final Set<Role> setRoles) {
		if (!setRoles.isEmpty()) {
			for (Role role : setRoles) {
				userRolesAsString += role.getDisplayName();
				roleId = String.valueOf(role.getRoleId());
			}
		}
		return userRolesAsString;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(final String roleId) {
		this.roleId = roleId;
	}

	@JsonIgnore
	public Set<Role> getSetRoles() {
		return setRoles;
	}

	public void setSetRoles(final Set<Role> setRoles) {
		this.setRoles = setRoles;
	}

	public void setUserRolesAsString(final String userRolesAsString) {
		this.userRolesAsString = userRolesAsString;
	}

	@JsonIgnore
	private Set<Role> setRoles;

	public long getUserId() {
		return userId;
	}

	public void setUserId(final long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(final String loginName) {
		this.loginName = loginName != null ? loginName.trim() : loginName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName != null ? firstName.trim() : firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName != null ? lastName.trim() : lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email != null ? email.trim() : email;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(final boolean global) {
		this.global = global;
	}

	@Override
	public User populate(final User userVO) {
		userVO.setActive(isActive());
		userVO.setVersion(getVersion());

		userVO.setUserId(this.userId);
		userVO.setLoginName(this.loginName);
		userVO.setFirstName(this.firstName);
		userVO.setLastName(this.lastName);
		userVO.setEmail(this.email);
		userVO.setGlobal(this.global);
		userVO.setAddress1(this.address1);
		userVO.setAddress2(this.address2);
		userVO.setState(this.state);
		userVO.setCity(this.city);
		userVO.setZip(this.zip);
		userVO.setMobile(this.mobile);
		userVO.setTelephone(this.telephone);
		userVO.setFaxNo(this.faxNo);
		userVO.setDisplayOnWarRoom(this.isDisplayOnWarRoom());

		final Set<SalesCategory> salesCategories = new HashSet<SalesCategory>();
		if (this.selectedSalesCatList != null) {
			for (Long salesCatId : this.selectedSalesCatList) {
				final SalesCategory salesCategory = new SalesCategory();
				salesCategory.setSosSalesCategoryId(salesCatId);
				salesCategories.add(salesCategory);
			}
		}

		userVO.setSalesCategories(salesCategories);
		final Role role = new Role();
		role.setRoleId(Long.valueOf(this.roleId));
		userVO.addRole(role);
		return userVO;
	}

	@Override
	public void populateForm(final User bean) {
		this.setActive(bean.isActive());
		this.setVersion(bean.getVersion());

		this.userId = bean.getUserId();
		this.firstName = bean.getFirstName();
		this.lastName = bean.getLastName();
		this.loginName = bean.getLoginName();
		this.email = bean.getEmail();
		this.global = bean.isGlobal();
		this.setRoles = bean.getUserRoles();
		this.address1 = bean.getAddress1();
		this.address2 = bean.getAddress2();
		this.state = bean.getState();
		this.zip = bean.getZip();
		this.city = bean.getCity();
		this.mobile = bean.getMobile();
		this.telephone = bean.getTelephone();
		this.faxNo = bean.getFaxNo();
		this.displayOnWarRoom = bean.isDisplayOnWarRoom();

		this.userRolesAsString = constructRolesAsString(bean.getUserRoles());
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(final String roleName) {
		this.roleName = roleName != null ? roleName.trim() : roleName;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(final String faxNo) {
		this.faxNo = faxNo != null ? faxNo.trim() : faxNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(final String address1) {
		this.address1 = address1 != null ? address1.trim() : address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(final String address2) {
		this.address2 = address2 != null ? address2.trim() : address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city != null ? city.trim() : city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state != null ? state.trim() : state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(final String zip) {
		this.zip = zip != null ? zip.trim() : zip;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(final String telephone) {
		this.telephone = telephone != null ? telephone.trim() : telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile != null ? mobile.trim() : mobile;
	}

	public boolean isActive() {
		return super.isActive();
	}

	public void setActive(final boolean active) {
		super.setActive(active);
	}

	public long[] getSelectedSalesCatList() {
		return selectedSalesCatList;
	}

	public void setSelectedSalesCatList(final long[] selectedSalesCatList) {
		this.selectedSalesCatList = selectedSalesCatList;
	}

	public int getVersion() {
		return super.getVersion();
	}

	public void setVersion(final int version) {
		super.setVersion(version);
	}

	@Override
	public boolean isForceUpdate() {
		return super.isForceUpdate();
	}

	@Override
	public void setForceUpdate(final boolean forceUpdate) {
		super.setForceUpdate(forceUpdate);
	}

	public boolean isDisplayOnWarRoom() {
		return displayOnWarRoom;
	}

	public void setDisplayOnWarRoom(final boolean displayOnWarRoom) {
		this.displayOnWarRoom = displayOnWarRoom;
	}
}
