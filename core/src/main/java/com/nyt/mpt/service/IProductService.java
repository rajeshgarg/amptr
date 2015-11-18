/**
 *
 */
package com.nyt.mpt.service;

import java.util.List;
import java.util.Map;

import com.nyt.mpt.domain.ClusterSalesTarget;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.PrimaryPageGroup;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.ProductPosition;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetType;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare Product Service  level methods.
 *
 * @author surendra.singh
 */
public interface IProductService {

	/**
	 * Return list of product for given product ID's from SOS.
	 *
	 * @param id the id
	 * @return List<Product>
	 */
	List<com.nyt.mpt.domain.sos.Product> getSOSProductListByIDs(final Long[] id);


	/**
	 * Get filtered product list.
	 *
	 * @param searchGridParams the search grid params
	 * @param pgCriteria the pg criteria
	 * @param sortingCriteria the sorting criteria
	 * @return List<Product>
	 */
	List<Product> getProductFilteredList(final FilterCriteria searchGridParams, final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria);

	/**
	 * Get total number of product for given filterCriteria.
	 *
	 * @param filterCriteria the filter criteria
	 * @return Integer
	 */
	Integer getProductFilteredListcount(final FilterCriteria filterCriteria);

	/**
	 * Function to retrieve sales target for the selected product.
	 *
	 * @param productId the product id
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTarget(final Long productId);

	/**
	 * Get product placement info for product.
	 *
	 * @param productId the product id
	 * @param salesTargetId the sales target id
	 * @return String
	 */
	String getProductPlacement(final Long productId, final Long salesTargetId);

	/**
	 * Return list of all creative for particular product.
	 *
	 * @param productId the product id
	 * @return List<ProductCreativeAssoc>
	 */
	List<ProductCreativeAssoc> getProductCreatives(final Long productId);

	/**
	 * Get list of product attribute.
	 *
	 * @param productId the product id
	 * @param salesTargetId the sales target id
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getLineItemAttributes(final Long productId, final Long salesTargetId);

	/**
	 * Return list of attributes associated with a line item with attribute name.
	 *
	 * @param productId the product id
	 * @param salesTargetId the sales target id
	 * @param attributeName the attribute name
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getLineItemAttrByAttrKey(final Long productId, final Long salesTargetId, final String attributeName);

	/**
	 * Add attribute to the line item.
	 *
	 * @param attributeAssoc the attribute assoc
	 * @return Long
	 */
	Long addLineItemAttribute(final ProductAttributeAssoc attributeAssoc);

	/**
	 * Update value of line item attribute.
	 *
	 * @param attributeAssoc the attribute assoc
	 * @return Long
	 */
	Long updateLineItemAttribute(final ProductAttributeAssoc attributeAssoc);

	/**
	 * Remove line item attribute.
	 *
	 * @param assoc the assoc
	 * @return Long
	 */
	Long deleteLineItemAttribute(final ProductAttributeAssoc assoc);

	/**
	 * Get list of sales target for given type.
	 *
	 * @param targetType the target type
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTargetByType(final Long targetType);

	/**
	 * Return list of sales target type.
	 *
	 * @return the all sales target type
	 * @returnList<SalesTargetType> 
	 */
	List<SalesTargetType> getAllSalesTargetType();

	/**
	 * Return Cluster Sales Target based on sales target id.
	 *
	 * @param salesTargetId the sales target id
	 * @return List<ClusterSalesTarget>
	 */
	List<ClusterSalesTarget> getClusterSalesTargetsBySalesTargetId(final long salesTargetId);

	/**
	 * Return product for product id.
	 *
	 * @param productId the product id
	 * @return Product
	 */
	Product getProductById(final Long productId);
	
	/**
	 * Return Inactive product for product id.
	 *
	 * @param productId the product id
	 * @return Product
	 */
	Product getInactiveProductById(final Long productId);

	/**
	 * Get filtered list of product creative.
	 *
	 * @param productId the product id
	 * @param filterCriteria the filter criteria
	 * @param pgCriteria the pg criteria
	 * @param sortingCriteria the sorting criteria
	 * @return List<ProductCreativeAssoc>
	 */
	List<ProductCreativeAssoc> getFilteredProductCreatives(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria);

	/**
	 * Get total number of product creative for given product.
	 *
	 * @param productId the product id
	 * @param filterCriteria the filter criteria
	 * @return int
	 */
	int getFilteredProductCreativesCount(final Long productId, final FilterCriteria filterCriteria);

	/**
	 * Get filtered list of product attribute.
	 *
	 * @param productId the product id
	 * @param filterCriteria the filter criteria
	 * @param pgCriteria the pg criteria
	 * @param sortingCriteria the sorting criteria
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getFilteredProductAttributes(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria);

	/**
	 * Get total number of attributes for given products.
	 *
	 * @param productId the product id
	 * @param filterCriteria the filter criteria
	 * @return int
	 */
	int getFilteredProductAttributesCount(final Long productId, final FilterCriteria filterCriteria);

	/**
	 * Return list of all active products.
	 *
	 * @return List<Product>
	 */
	List<Product> getAllProducts();

	/**
	 * Return list of all product class.
	 *
	 * @return Map<Long,String>
	 */
	Map<Long, String> getProductClass();
	
	/**
	 * Return list of all product class.
	 * 
	 * @return Map<String, Long>
	 */
	Map<String, Long> getProductsClassMap();

	/**
	 * Check whether attribute exist for the product or not.
	 *
	 * @param productId the product id
	 * @param attributeId the attribute id
	 * @param salesTargetId the sales target id
	 * @return boolean
	 */
	boolean isDuplicateProductAttributeAssocExist(final long productId, final long attributeId, final Long salesTargetId);

	/**
	 * Return Map of ProductName on basis of salestarget.
	 *
	 * @param salesTargetID the sales target id
	 * @return Map<Long, String>
	 */
	Map<Long, String> getAllProductsForSalesTarget(final long salesTargetID);

	/**
	 * Return Map of ProductID and CreativesName.
	 *
	 * @param productIdList the product id list
	 * @return Map<Long,String>
	 */
	Map<Long, String> getProductCreativesName(final List<Long> productIdList);

	/**
	 * Find product name value and ID associated with attributeId.
	 *
	 * @param attributeId the attribute id
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getProductAttrAssocListByAttributeId(final long attributeId);

	/**
	 * Return PrimaryPageGroup based on sales Target Id.
	 *
	 * @param salesTargetId the sales target id
	 * @return PrimaryPageGroup
	 */
	PrimaryPageGroup getPrimaryPageGroupBySalesTargetId(final long salesTargetId);

	/**
	 * Return List of Primary Page Group based on Sales Target Ids.
	 *
	 * @param clusterSalesTargetId the cluster sales target id
	 * @param salesTargetIds the sales target ids
	 * @return List<PrimaryPageGroup>
	 */
	List<PrimaryPageGroup> getPrimaryPageGroupBySalesTargetIds(final long clusterSalesTargetId, final Long[] salesTargetIds);

	/**
	 * Return ProductPosition List based on Product Id.
	 *
	 * @param productId the product id
	 * @return List<ProductPosition>
	 */
	List<ProductPosition> getProductPositionsByProductId(final long productId);

	/**
	 * Gets the all sales target for multi target type.
	 *
	 * @param saleTargetTypeIds the sale target type ids
	 * @return the all sales target for multi target type
	 */
	List<SalesTarget> getAllSalesTargetForMultiTargetType(final Long[] saleTargetTypeIds);

	/**
	 * Gets the all products for multi sales target.
	 *
	 * @param saleTargetTypeLst the sale target type lst
	 * @return the all products for multi sales target
	 */
	Map<Long, String>  getAllProductsForMultiSalesTarget(final List<Long> saleTargetTypeLst);

	/**
	 * This method is used to get parent sales target Id based on sales target
	 * type id's.
	 *
	 * @param saleTargetTypeLst the sale target type lst
	 * @return the parent sales target id
	 */
	Map<Long, Long> getParentSalesTargetId(final List<Long> saleTargetTypeLst);

	/**
	 * Get product placement info for line item.
	 *
	 * @param productId the product id
	 * @param sosSalesTargetId the sos sales target id
	 * @return the line item product placement
	 */
	String getLineItemProductPlacement(final Long productId, final List<Long> sosSalesTargetId);

	/**
	 * return list of sales target based on searched product.
	 *
	 * @param productID the product id
	 * @return List<SalesTarget>
	 */
	List<SalesTarget> getSalesTargetFromProductID(final Long productID);

	/**
	 * return list of product based on search string.
	 *
	 * @param productName the product name
	 * @return List<Product>
	 */
	List<Product> getProductsBySearchString(final String productName);

	/**
	 * Used to get the product creative for export templates.
	 *
	 * @param lineItem the line item
	 * @return the product creatives for templates
	 */
	List<ProductCreativeAssoc> getProductCreativesForTemplates(final LineItem lineItem);

	/**
	 * Update the creative to a product.
	 *
	 * @param productId the product id
	 * @param creativeIds the creative ids
	 */
	void updateProductCreative(final long productId, final List<Long> creativeIds);

	/**
	 * Return list of all active Reservable products.
	 *
	 * @param isReservable the is reservable
	 * @return List<Product>
	 */
	List<Product> getAllReservableProducts(final boolean isReservable);

	/**
	 * Return list of inactive product ids.
	 *
	 * @return the all inactive products
	 */
	List<Long> getAllInactiveProducts();

	/**
	 * Delete product creative assoc for given product ids.
	 *
	 * @param productIDLst the product id lst
	 */
	void deleteProductCreativeAssoc(final List<Long> productIDLst);

	/**
	 * Delete product attribute assoc for given product ids.
	 *
	 * @param productIDLst the product id lst
	 */
	void deleteProductAttributeAssoc(final List<Long> productIDLst);
	
	/**
	 * Returns all products and sales target combinations.
	 *
	 * @return the all sales targets and products
	 */
	Map<Long, Map<Long, String>> getAllSalesTargetsAndProducts();
	

	/**
	 * Gets the all reservable products by produt id.
	 *
	 * @param availsSystemId 
	 * @param reservable the reservable
	 * @return the all reservable products by produt id
	 */
	Map<Long, String> getProductsByAvailsId(final long availsSystemId, final boolean reservable);
	
	/**
	 * Get all Sales Target for multiple products.
	 * @param productIdLst - List of product Id's whose sales target has to be fetched.
	 * @return The list of Sales Target
	 */
	List<SalesTarget> getAllSalesTargetForMultiProduct(final List<Long> productIdLst);
	
	/**
	 * Fetches the list of the product class Ids based  on the list of the product display names.
	 * 
	 * @return
	 */
	List<Long> getProductClassIdLstByDisplayName();
}
