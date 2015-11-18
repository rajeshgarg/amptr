/**
 *
 */
package com.nyt.mpt.dao;

import java.io.Serializable;
import java.util.List;

/**
 * This interface declare Generic methods
 * 
 * @author surendra.singh
 */
public interface IGenericDAO {

	/**
	 * Tries to get an instance of the object
	 * @param entityId the id to search for
	 * @return the requested instance, or <code>null</code> if not found
	 */
	<T> T load(Class<T> entityClass, Serializable entityId);

	/**
	 * Adds a new instance of the object
	 * @param entity the instance to save
	 */
	void save(Object entity);

	/**
	 * Create a new persistent or save the existing instance of the Object The
	 * mapping for the class should exists
	 * @param object
	 */
	void saveOrUpdate(Object object);

	/**
	 * Deletes the object
	 * @param entity the object to delete
	 */
	void delete(Object entity);

	/**
	 * Delete all persistent entities
	 * @param objectList
	 */
	@SuppressWarnings("rawtypes")
	void deleteAll(List objectList);

	/**
	 * Updates the information of an existing object
	 * @param entity the instance to update
	 */
	void update(Object entity);
}
