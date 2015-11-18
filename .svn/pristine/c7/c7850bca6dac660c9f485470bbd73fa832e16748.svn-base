/**
 *
 */
package com.nyt.mpt.dao;

import java.util.List;
import java.util.Set;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This interface declare AMPT Product related methods
 *
 * @author amandeep.singh
 *
 */
public interface IProductDAOAMPT {

	/**
	 * Return list of product creative
	 *
	 * @param productId
	 * @return List<ProductCreativeAssoc>
	 */
	List<ProductCreativeAssoc> getProductCreatives(final Long productId);

	/**
	 * Add creative to the product
	 *
	 * @param assoc
	 * @return Long
	 */
	Long addProductCreative(final ProductCreativeAssoc assoc);

	/**
	 * Remove creative from the product list
	 *
	 * @param assoc
	 * @return Long
	 */
	Long deleteProductCreative(final ProductCreativeAssoc assoc);

	/**
	 * Return list of attributes for line item
	 *
	 * @param productId
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getLineItemAttributes(final Long productId, final Long salesTargetId);

	/**
	 * Return list of attributes associated with a line item with attribute name
	 *
	 * @param productId
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getLineItemAttrByAttrKey(final Long productId, final Long salesTargetId, final String attributeName);

	/**
	 * Add attribute to the line item.
	 *
	 * @param assoc
	 * @return Long
	 */
	Long addLineItemAttribute(final ProductAttributeAssoc assoc);

	/**
	 * Update value of given line item
	 *
	 * @param assoc
	 * @return Long
	 */
	Long updateLineItemAttribute(final ProductAttributeAssoc assoc);

	/**
	 * Get filtered list of product creative
	 *
	 * @param productId
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<ProductCreativeAssoc>
	 */
	List<ProductCreativeAssoc> getFilteredProductCreatives(final Long productId, final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria,
			final SortingCriteria sortCriteria);

	/**
	 * Get total number of product creative for given product Return total
	 *
	 * @param productId
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredProductCreativesCount(final Long productId, final FilterCriteria filterCriteria);

	/**
	 * Get filtered list of product attribute
	 *
	 * @param productId
	 * @param filterCriteria
	 * @param pageCriteria
	 * @param sortCriteria
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getFilteredProductAttributes(final Long productId, final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria,
			final SortingCriteria sortCriteria);

	/**
	 * Get total number of attributes for given products
	 *
	 * @param productId
	 * @param filterCriteria
	 * @return int
	 */
	int getFilteredProductAttributesCount(final Long productId, final FilterCriteria filterCriteria);

	/**
	 * Delete attribute from the line item.
	 *
	 * @param assoc
	 */
	void deleteLineItemAttribute(final ProductAttributeAssoc assoc);

	/**
	 * Check whether attribute exist for the product or not.
	 *
	 * @param productId
	 * @param attributeId
	 * @param salesTargetId
	 * @return boolean
	 */
	boolean isDuplicateProductAttributeAssocExist(final long productId, final long attributeId, final Long salesTargetId);

	/**
	 * Get product placement for the product
	 *
	 * @param productId
	 * @param salesTargetId
	 * @return String
	 */
	String getProductPlacement(final Long productId, final Long salesTargetId);

	/**
	 * Get Creatives attached to products
	 * @param productIdList
	 * @return
	 */
	List<ProductCreativeAssoc> getFilteredProductCreativesAssocList(final List<Long> productIdList);

	/**
	 * Find product name value and ID associated with attributeId
	 *
	 * @param attributeId
	 * @return List<ProductAttributeAssoc>
	 */
	List<ProductAttributeAssoc> getProductAttrAssocListByAttributeId(final long attributeId);

	/**
	 * Get product placement info for line item
	 *
	 * @param productId
	 * @param sosSalesTargetId
	 * @return
	 */
	String getSosProductPlacement(final Long productId, final List<Long> sosSalesTargetId);

	/**
	 * Used to get product creative for export templates
	 * @param lineItem
	 * @return
	 */
	List<ProductCreativeAssoc> getProductCreativesForTemplates(final LineItem lineItem);

	/**
	 * Get product Id's from creative filter list
	 * @param filterCriteria
	 * @return
	 */
	 Set<Long> getProdIdsByCreativeFilter(final FilterCriteria filterCriteria);

	/**
	 * Delete product creative assoc for given product ids
	 *
	 * @param creativeAssocLst
	 */
	void deleteProductCreativeAssoc(final List<ProductCreativeAssoc> creativeAssocLst);

	/**
	 * Delete product attribute assoc for given product ids
	 *
	 * @param attributeAssocLst
	 */
	void deleteProductAttributeAssoc(final List<ProductAttributeAssoc> attributeAssocLst);

	/**
	 * Return list of productAttributeAssoc for given product ids
	 * @param productIdList
	 * @return
	 */
	List<ProductAttributeAssoc> getProductAttributeAssocLst(final List<Long> productIdList);
}
