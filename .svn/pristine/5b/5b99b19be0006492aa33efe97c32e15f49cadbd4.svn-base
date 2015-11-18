/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare User info related methods
 *
 * @author surendra.singh
 *
 */
public interface IUserDAO extends IGenericDAO {

	/**
	 * Return user info not associated objects
	 *
	 * @param loginName
	 * @return User
	 */
	User findByUsername(String loginName);

	/**
	 * Load user with all its info(Roles, sales categories, Personalisation info etc)
	 *
	 * @param loginName
	 * @return User
	 */
	User getUserWithAllInfo(String loginName);

	/**
	 * Return list of all users.
	 *
	 * @return List<User>
	 */
	List<User> getUserList();

	/**
	 * Save user info to database
	 *
	 * @param user
	 * @return long
	 */
	long saveUser(User user);

	/**
	 * Return list of users based on pagination and filter criteria.
	 *
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @param showAllUser 
	 * @return List<User>
	 */
	List<User> getFilteredUserList(FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortCriteria, boolean showAllUser);

	/**
	 * Return total no of users matching given filter criteria
	 *
	 * @param filterCriteria
	 * @param showAllUser 
	 * @return int
	 */
	int getUserFilteredListCount(FilterCriteria filterCriteria, boolean showAllUser);

	/**
	 * Return list of all users having given role.
	 *
	 * @param roles
	 * @return List<User>
	 */
	List<User> getUserBasedOnRoleList(String[] roles);

	/**
	 * Return list of sales categories of given userId
	 *
	 * @param userId
	 * @return List<SalesCategory>
	 */
	List<SalesCategory> getUserSalesCategories(long userId);

	/**
	 * Return user for given user Id.
	 *
	 * @param userId
	 * @return User
	 */
	User getUserById(long userId);

	/**
	 * Remove given sales categories, It will only remove sales category for users.
	 *
	 * @param salesCategories
	 */
	void removeSalesCategories(Set<SalesCategory> salesCategories);
}
