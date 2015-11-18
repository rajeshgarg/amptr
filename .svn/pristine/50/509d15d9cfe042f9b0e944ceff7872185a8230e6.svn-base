/**
 * 
 */
package com.nyt.mpt.util.filter;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.nyt.mpt.util.CacheSupportToStringStyle;
import com.nyt.mpt.util.StringUtil;

/**
 * This class is used to create Filter Criteria
 *
 * @author amandeep.singh
 * 
 */
public class FilterCriteria {

	private String searchField;
	private String searchString;
	private String searchOper;

	public FilterCriteria () { 
		super();
	}

	/**
	 * Construct a FilterCriteria object using the search field, search value and search operator  
	 * @param searchField
	 * @param searchString
	 * @param searchOper
	 */
	public FilterCriteria(String searchField, String searchString, String searchOper) {
		this.searchField = searchField;
		this.searchString = searchString;
		this.searchOper = searchOper;
	}
	
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = StringUtil.escapeSqlCharacters(searchString);
	}
	
	public void setSearchStringWtSQLChar(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchOper() {
		return searchOper;
	}

	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new CacheSupportToStringStyle() ).toString();	
	}
}
