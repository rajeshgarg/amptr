/**
 * 
 */
package com.nyt.mpt.util;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This class is used for table grid
 * @author surendra.singh
 */
public class TableGrid<T> {

	private List<T> dataList;

	/**
	 * Number of rows per page
	 */
	private Integer rows = 0;

	/**
	 * Requested page index
	 */
	private Integer page = new Integer(1);

	/**
	 * Total number of pages: (records / page)
	 */
	private Integer total = new Integer(1);

	/**
	 * Total number of records in data base
	 */
	private Integer records = 0;

	/**
	 * Sorting order (asc or desc)
	 */
	private String sord;

	/**
	 * Sorting field name
	 */
	private String sidx;

	/**
	 * Search field name
	 */
	private String searchField;

	/**
	 * Search value
	 */
	private String searchString;

	/**
	 * Search operator (like - eg, lt, gt etc)
	 */
	private String searchOper;

	/**
	 * @param data List of filtered data after applying the pagination criteria
	 * @param totalCount Total no of records for the given FiterCriteria
	 */
	public void setGridData(final List<T> data, final int totalCount) {
		this.records = totalCount;
		if (data == null || data.isEmpty()) {
			return;
		}
		if (this.records > 0 && this.rows > 0) {
			this.total = (int) Math.ceil((double) this.records / (double) this.rows);
		} else {
			this.total = 0;
		}

		if (getTotal().intValue() < getPage().intValue()) {
			setPage(getTotal());
		}
		this.dataList = data;
	}

	/**
	 * Return new filter criteria
	 */
	@JsonIgnore
	public FilterCriteria getFilterCriteria() {
		return new FilterCriteria(this.searchField, this.searchString, this.searchOper);
	}

	/**
	 * Calculate start position of record to fetch and return pagination criteria.
	 */
	@JsonIgnore
	public PaginationCriteria getPaginationCriteria() {
		return new PaginationCriteria(calculateStartIndex(), rows);
	}

	/**
	 * Return Sorting Criteria
	 */
	@JsonIgnore
	public SortingCriteria getSortingCriteria() {
		return new SortingCriteria(this.sidx, this.sord);
	}

	private int calculateStartIndex() {
		if (page != null && page.intValue() > 0) {
			return ((page - 1) * rows) + 1;
		}
		return -1;
	}

	public Integer getRecords() {
		return records;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(final Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(final Integer page) {
		this.page = page;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(final Integer total) {
		this.total = total;
	}

	public List<T> getGridModel() {
		return this.dataList;
	}

	public void setSord(final String sord) {
		this.sord = sord;
	}

	public void setSidx(final String sidx) {
		this.sidx = sidx;
	}

	public void setSearchField(final String searchField) {
		this.searchField = searchField;
	}

	public void setSearchString(final String searchString) {
		this.searchString = searchString;
	}

	public void setSearchOper(final String searchOper) {
		this.searchOper = searchOper;
	}

	/**
	 * Use setGridData(List<T> data, int totalCount) instead if user wants to
	 * load only limited data for performance benefit.
	 */
	@Deprecated
	public void setGridData(List<T> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		setRecords(data.size());
		final SortingUtil<T> util = new SortingUtil<T>();
		util.sortComponent(data, this.sidx, this.sord);

		if (getTotal().intValue() < getPage().intValue()) {
			setPage(getTotal());
		}

		int to = (getRows() * getPage());
		final int from = to - getRows();

		if (to > getRecords()) {
			to = getRecords();
		}
		this.dataList = getSubList(data, from, to);
	}

	public void setRecords(final Integer records) {
		this.records = records;
		if (this.records > 0 && this.rows > 0) {
			this.total = (int) Math.ceil((double) this.records / (double) this.rows);
		} else {
			this.total = 0;
		}
	}

	private List<T> getSubList(final List<T> data, final int from, final int to) {
		final List<T> list = new ArrayList<T>();
		for (int i = from; i < to; i++) {
			list.add(data.get(i));
		}
		return list;
	}
}
