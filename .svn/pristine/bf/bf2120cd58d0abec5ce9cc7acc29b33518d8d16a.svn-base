/**
 * 
 */
package com.nyt.mpt.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Utility class for storing pagination status.
 * 
 * @author shishir.srivastava
 * 
 */
public class PaginationCriteria {

	/**
	 * @see HibernateTemplate#findByCriteria(org.hibernate.criterion.DetachedCriteria, int, int)
	 */
	private int firstResultPosition = -1; 

	private int maxNoOfRecordsToBeFetch = -1;

	public PaginationCriteria(int startIndex, int maxRecord){
		this.firstResultPosition = startIndex;
		this.maxNoOfRecordsToBeFetch = maxRecord;
	}
	
	public int getFirstResultPosition() {
		return firstResultPosition;
	}

	public void setFirstResultPosition(int firstResultPosition) {
		this.firstResultPosition = firstResultPosition;
	}

	public int getMaxNoOfRecordsToBeFetch() {
		return maxNoOfRecordsToBeFetch;
	}

	public void setMaxNoOfRecordsToBeFetch(int maxNoOfRecordsToBeFetch) {
		this.maxNoOfRecordsToBeFetch = maxNoOfRecordsToBeFetch;
	}
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new CacheSupportToStringStyle() ).toString();	
	}
}
