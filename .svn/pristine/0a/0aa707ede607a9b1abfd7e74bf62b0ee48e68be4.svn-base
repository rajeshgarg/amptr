/**
 * 
 */
package com.nyt.mpt.service;

import java.util.List;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;

/**
 * This interface declare Package Service  level methods
 * @author surendra.singh
 *
 */
public interface IPackageService {

	/**
	 * Return list of all active package.
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sorCriteria
	 * @return List<Package>
	 */
	List<Package> getFilteredPackageList(FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sorCriteria);

	/**
	 * Return a count of package in system as per filter criteria.
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredPackageListCount(FilterCriteria filterCriteria);

	/**
	 * Delete package from database.
	 * @param adPackage
	 * @return long
	 */
	long deletePackage(Package adPackage);

	/**
	 * Save a new package if package is not already exist else update package
	 * @param adPackage
	 * @return long
	 */
	long savePackage(Package adPackage);

	/**
	 * Checks whether Package with same name Exist.
	 * @param packageName
	 * @param packageID
	 * @return boolean
	 */
	boolean isDuplicatePackageName(String packageName, long packageID);

	/**
	 * Return a package for given package Id.
	 * @param packageId
	 * @return Package
	 */
	Package getPackageById(long packageId);

	/**
	 * Deleting Line Item based on  lineItemId.
	 * @param lineItemId
	 */
	void deleteLineItems(String lineItemId);

	/**
	 * Saving Line Item with lineItem.
	 * @param lineItem
	 * @return Long
	 */
	Long saveLineItems(LineItem lineItem);

	/**
	 * Return list of all package lineItem in system as per package Id, filter, paging and sorting criteria.
	 * @param packageId
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<LineItem>
	 */
	List<LineItem> getFilteredPackageLineItems(long packageId, FilterCriteria filterCriteria, PaginationCriteria pageCriteria,
			SortingCriteria sortCriteria);

	/**
	 * Return a count of Package LineItem in system as per package Id and filter criteria.
	 * @param packageId
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredPackageLineItemsCount(long packageId, FilterCriteria filterCriteria);

	/**
	 * Return a LineItem for given LineItem Id.
	 * @param lineItemId
	 * @return LineItem
	 */
	LineItem getLineItemById(Long lineItemId);

	/**
	 * Return list of all package in system as per filter, paging and sorting criteria.
	 * @param filterCriteriaLst
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<Package>
	 */
	List<Package> getFilteredPackageList(List<RangeFilterCriteria> filterCriteriaLst,
			PaginationCriteria pageCriteria, SortingCriteria sortCriteria);

	/**
	 * Return count of all package having lineItem in system as per filter criteria.
	 * @param filterCriteriaLst
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<Package>
	 */
	List<Package> getFilteredPackageListHavingLineItems(List<RangeFilterCriteria> filterCriteriaLst,
			PaginationCriteria pageCriteria, SortingCriteria sortCriteria);

	/**
	 * Return count of all package having lineItem in system as per filter criteria.
	 * @param filterCriteriaLst
	 * @return Integer
	 */
	Integer getFilteredPackageListCountHavingLineItems(List<RangeFilterCriteria> filterCriteriaLst);

	/**
	 * Method will return the next sequence no for the Package Line Item
	 * @param packageId
	 * @return
	 */
	int getNextSequenceNoForLineItem(long packageId);

	/**
	 * Method will Clone the package
	 * @param packageId
	 * @return
	 */
	long createPackageCloneByID(long packageId);

	/**
	 * Update all line item avails
	 * @param adPackage
	 * @return
	 */
	long updatedPackage(final Package adPackage);

	/**
	 * Update base price of given line item
	 * @param lineItems
	 * @return
	 */
	void updateAllLineItemsPrice(final Long[] lineItems);
	
}
