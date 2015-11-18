/**
 * 
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.UserFilter;

/**
 * This <code>IMultipleCalendarDao</code> interface includes all declarations for the methods to get the Multiple Calendar's related data
 * 
 * @author shipra.bansal
 */

public interface IMultipleCalendarDao extends IGenericDAO {

	/**
	 * Returns the List of {@link UserFilter} by <code>filterType</code> and <code>userIds</code>
	 * @param filterType
	 * @param userIds
	 * @return
	 */
	List<UserFilter> getUserFiltersByType(final String filterType, final Set<Long> userIds);

	/**
	 * Saves the {@link UserFilter} and returns the saved {@link UserFilter}
	 * @param 	filter
	 * 			{@link UserFilter} to be saved in the database
	 * @return
	 * 			Returns the saved {@link UserFilter}
	 */
	UserFilter saveFilter(final UserFilter filter);

	/**
	 * Deletes the {@link UserFilter} by <code>filterId</code>
	 * @param 	filterId
	 * 			{@link UserFilter}'s Id to be deleted from the AMPT db
	 */
	void deleteFilter(final long filterId);

	/**
	 * Returns the {@link UserFilter} by <code>filterId</code>
	 * @param filterId
	 * @return
	 */
	UserFilter getFilterById(final long filterId);

	/**
	 * Checks if duplicate name already exists in the databse or not 
	 * @param filterName
	 * @param filterId
	 * @param userId
	 * @return
	 */
	boolean isDuplicateName(final String filterName, final Long filterId,final long userId);

}
