/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.Role;

/**
 * This interface declare user Role related methods
 *
 * @author surendra.singh
 *
 */
public interface IRoleDAO {

	/**
	 * Return list of all assignable role
	 *
	 * @return
	 */
	List<Role> getAllAssignableRole();
}
