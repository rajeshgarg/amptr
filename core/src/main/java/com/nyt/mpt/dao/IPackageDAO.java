/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.domain.PackageSalesCategoryAssoc;

/**
 * This interface declare Package related methods
 *
 * @author surendra.singh
 *
 */
public interface IPackageDAO {

	/**
	 * Return list of all active package.
	 *
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortingCriteria
	 * @return
	 */
	List<Package> getFilteredPackageList(FilterCriteria filterCriteria, PaginationCriteria pageCriteria, SortingCriteria sortingCriteria);

	/**
	 * Return a count of package in system as per filter criteria.
	 *
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredPackageListCount(FilterCriteria filterCriteria);

	/**
	 * Save a new package if package is not already exist else update package
	 *
	 * @param adPackage
	 * @return long
	 */
	long savePackage(Package adPackage);

	/**
	 * Return a package for given package Id.
	 *
	 * @param packageId
	 * @return Package
	 */
	Package getPackageById(long packageId);

	/**
	 * Checks whether Package with same name Exist.
	 *
	 * @param packageName
	 * @return Package
	 */
	boolean isDuplicatePackageName(String packageName, long packageID);

	/**
	 * Save LineItem in Database
	 *
	 * @param lineItem
	 * @return Long
	 */
	Long saveLineItems(LineItem lineItem);

	/**
	 * Return list of all package lineItem in system as per package Id, filter, paging and sorting criteria.
	 *
	 * @param packageId
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortingCriteria
	 * @return List
	 */
	List<LineItem> getFilteredPackageLineItems(long packageId, FilterCriteria filterCriteria, PaginationCriteria pageCriteria,
			SortingCriteria sortingCriteria);

	/**
	 * Return a count of Package LineItem in system as per package Id and filter criteria.
	 *
	 * @param packageId
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredPackageLineItemsCount(long packageId, FilterCriteria filterCriteria);

	/**
	 * Return a LineItem for given LineItem Id.
	 *
	 * @param lineItemId
	 * @return LineItem
	 */
	LineItem getLineItemById(Long lineItemId);

	/**
	 * Update LineItem in Database
	 *
	 * @param lineItem
	 * @return Long
	 */
	Long updateLineItems(LineItem lineItem);

	/**
	 * Return list of all package in system as per filter, paging and sorting criteria.
	 *
	 * @param filterCriteriaLst
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List
	 */
	List<Package> getFilteredPackageList(List<RangeFilterCriteria> filterCriteriaLst, PaginationCriteria pageCriteria,
			SortingCriteria sortCriteria);

	/**
	 * Return count of all package having lineItem in system as per filter criteria.
	 *
	 * @param filterCriteriaLst
	 * @return Integer
	 */
	Integer getFilteredPackageListCountHavingLineItems(List<RangeFilterCriteria> filterCriteriaLst);

	/**
	 * Return list of all package having lineItem in system as per filter,
	 * paging and sorting criteria.
	 *
	 * @param filterCriteriaLst
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List
	 */
	List<Package> getFilteredPackageListHavingLineItems(List<RangeFilterCriteria> filterCriteriaLst, PaginationCriteria pageCriteria,
			SortingCriteria sortCriteria);

	/**
	 * Method will return the max sequence no for the package Line Item
	 *
	 * @param packageId
	 * @return
	 */
	int getMaxSequenceNoForLineItem(long packageId);

	/**
	 * Method will return the package with Line Item and targeting information initialised
	 * @param packageId
	 * @return
	 */
	Package getCompletePackageByID(long packageId);
	/**
	 * Method will delete the sales category assoc.
	 * @param PackageSalesCategoryAssoc
	 * @return
	 */
	Long deleteSalesCategoryAssoc(PackageSalesCategoryAssoc salesCategoryAssoc);
	
}
