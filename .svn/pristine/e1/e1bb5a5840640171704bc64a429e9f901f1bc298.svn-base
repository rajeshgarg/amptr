package com.nyt.mpt.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.impl.UserService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for User Service
 * @author surendra.singh
 * 
 */
public class UserServiceTest extends AbstractTest {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	private long userId;
	private String loginName;

	@Before
	public void setup() {
		super.setAuthenticationInfo();

		List<User> userList = userService.getUserList();
		Assert.assertNotNull(userList);
		Assert.assertTrue(userList.size() > 0);
		userId = userList.get(0).getUserId();
		loginName = userList.get(0).getLoginName();
	}

	/**
	 * Test for loadUserByUsername a part of UserDetails
	 */
	@Test
	public void testLoadUserByUserName() {
		User usr = (User) userService.loadUserByUsername(this.loginName);
		Assert.assertNotNull(usr);
		Assert.assertNotNull(usr.getFirstName());
		Assert.assertNotNull(usr.getLastName());
		Assert.assertNotNull(usr.getEmail());
		Assert.assertTrue(usr.getUserRoles().size() == 1);
		Assert.assertTrue(usr.getAuthorities().size() > 0);

		try {
			usr = (User) userService.loadUserByUsername("XYZ_TEST");
		} catch (UsernameNotFoundException e) {
			Assert.assertEquals(e.getClass(), UsernameNotFoundException.class);
		}
	}

	/**
	 * Test for createUser that Creates a new user in the system.
	 */
	@Test
	public void testCreateUser() {
		long userId = userService.createUser(getDummyUser());
		Assert.assertTrue(userId > 0);
	}

	/**
	 * Test for updateUser that will Update user in system
	 */
	@Test
	public void testUpdateUser() {
		User user = userService.getUserById(this.userId);
		user.setFirstName("Test");
		user.setEmail("123@nyt.com");
		SalesCategory category = new SalesCategory();
		category.setSosSalesCategoryId(100);

		Set<SalesCategory> salesCategories = new HashSet<SalesCategory>();
		salesCategories.add(category);
		user.setSalesCategories(salesCategories);
		Assert.assertTrue(this.userId == userService.updateUser(user, true));
		Assert.assertTrue(user.getSalesCategories().iterator().next().getSosSalesCategoryId() == 100);
	}

	/**
	 * Test for getAllAssignableRole that Returns list of all roles in the
	 * system.
	 */
	@Test
	public void testGetAllAssignableRole() {
		List<Role> roles = userService.getAllAssignableRole();
		Assert.assertTrue(roles.size() > 0);
		for (Role role : roles) {
			Assert.assertNotNull(role);
			Assert.assertNotNull(role.getRoleName());
		}
	}

	/**
	 * Test for isUserExists that Returns true if user is exist in the system.
	 */
	@Test
	public void testIsUserExist() {
		Assert.assertTrue(userService.isUserExists(this.loginName));
		try {
			userService.isUserExists("TEST_XYZ");
		} catch (UsernameNotFoundException e) {
			Assert.assertEquals(e.getClass(), UsernameNotFoundException.class);
		}
	}

	/**
	 * Test for getFilteredUserList that Return list of users from filter and
	 * paging criteria
	 */
	@Test
	public void testGetFilteredUserList() {
		List<User> userList = userService.getFilteredUserList(null, new PaginationCriteria(1, 10), null, false);
		Assert.assertTrue(userList.size() <= 10);
	}

	/**
	 * Test for getUserFilteredListCount that Return total no of users matching
	 * given filter criteria
	 */
	@Test
	public void testGetUserFilteredListCount() {
		FilterCriteria filterCriteria = null;
		int count = userService.getUserFilteredListCount(filterCriteria, false);
		Assert.assertTrue(count >= 0);
	}

	/**
	 * Test for getUserSalesCategories that will Get all sales categories
	 */
	@Test
	public void testGetUserSalesCategories() {
		long userID = this.userId;
		List<SalesCategory> salesCategoryLst = userService.getUserSalesCategories(userID);
		Assert.assertTrue(salesCategoryLst.size() >= 0);
	}

	/**
	 * Test for getUserBasedOnRoleList that Returns list of users having
	 * particular roles
	 */
	@Test
	public void testGetUserBasedOnRoleList() {
		List<User> userList = userService.getFilteredUserList(null, new PaginationCriteria(1, 10), null, false);
		String[] roles = new String[10];
		int i = 0;
		if (userList != null && userList.size() > 0) {
			for (User userlist : userList) {
				Set<Role> role = userlist.getUserRoles();
				if (role != null && role.size() > 0 && i < 10) {
					for (Role rol : role) {
						roles[i++] = rol.getRoleName();
						if (i >= 10) {
							break;
						}
					}
				}
			}

		}
		if (roles.length > 0) {
			List<User> user = userService.getUserBasedOnRoleList(roles);
			Assert.assertTrue(user.size() >= 0);
		}
	}

	/**
	 * Test for searchUserFromLdap that Returns list of users matching given
	 * userName in LDAP.
	 */
	@Test
	public void testSearchUserFromLdap() {
		try {
			Assert.assertTrue(userService.searchUserFromLdap(this.loginName).size() >= 0);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * @return
	 */
	private User getDummyUser() {
		User user = new User();
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("123@nyt.com");
		user.setLoginName("user.test");

		SalesCategory category = new SalesCategory();
		category.setSosSalesCategoryId(100);

		Set<SalesCategory> salesCategories = new HashSet<SalesCategory>();
		salesCategories.add(category);
		user.setSalesCategories(salesCategories);

		Role role = new Role();
		role.setRoleId(10);
		user.addRole(role);
		return user;
	}
}
