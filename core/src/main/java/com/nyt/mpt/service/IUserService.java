package com.nyt.mpt.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare User Service level  methods
 * @author Shishir.Srivastava
 *
 */
public interface IUserService {

	/**
	 * Return list of users in system.
	 * @return List<User>
	 */
	List<User> getUserList();
	
	/**
	 * Return list of users from filter and paging criteria 
	 * @param filterCriteria
	 * @param paginationCriteria
	 * @param sortingCriteria
	 * @param showAllUser 
	 * @return List<User>
	 */
	List<User> getFilteredUserList(FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria, boolean showAllUser);
	
	/**
	 * Update user in system
	 * @param user
	 * @return long
	 */
	long updateUser(User user, boolean forceUpdate);

	/**
	 * Create a new user in the system.
	 * @param user
	 * @return long
	 */
	long createUser(User user);
	
	/**
	 * Get all sales categories
	 * @return List<SalesCategory>
	 */
	List<SalesCategory> getUserSalesCategories(long userId);
	
	/**
	 * Return list of all roles in the system.
	 * @return List<Role>
	 */
	List<Role> getAllAssignableRole();
	
	/**
	 * Return true if user is exist in the system.
	 * @param loginName
	 * @return boolean
	 */
	boolean isUserExists(String loginName);

	/**
	 * Return total no of users matching given filter criteria
	 * @param filterCriteria
	 * @param showAllUser 
	 * @return int
	 */
	int getUserFilteredListCount(FilterCriteria filterCriteria, boolean showAllUser);
	
	/**
	 * Return list of users matching given userName in LDAP. 
	 * @param userName
	 * @return List<User>
	 */
	List<User> searchUserFromLdap(String userName);

	/**
	 * Return list of users having particular roles
	 * @return List<User>
	 */
	List<User> getUserBasedOnRoleList(String [] roles);
	
	/**
	 * Return user for given user id.
	 * @param userId
	 * @return User
	 */
	User getUserById(long userId);
	
	/**
	 * This method is part of UserDetails
	 * @param username
	 * @return
	 */
	UserDetails loadUserByUsername(String username);// We want to use this method by userService
}
