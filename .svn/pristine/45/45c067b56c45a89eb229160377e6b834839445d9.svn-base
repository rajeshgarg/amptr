/**
 * 
 */
package com.nyt.mpt.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nyt.mpt.dao.IProductDAO;
import com.nyt.mpt.dao.IProductDAOAMPT;
import com.nyt.mpt.dao.IProductDAOSOS;
import com.nyt.mpt.domain.ClusterSalesTarget;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.PrimaryPageGroup;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.ProductPosition;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetType;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.YieldexAvailsException;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * Service class for all product related operations
 *
 * @author surendra.singh
 *
 */
public class ProductService implements IProductService {

	private static final Logger LOGGER = Logger.getLogger(ProductService.class);

	private IProductDAO productDao;

	private IProductDAOAMPT productDaoAMPT;
	
	private IProductDAOSOS productDaoSOS;
	
	private String premiumProductClassNames;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getProductFilteredList(com.nyt.mpt.util.FilterCriteria)
	 */
	public List<Product> getProductFilteredList(final FilterCriteria searchGridParams, final PaginationCriteria pCriteria,
			final SortingCriteria sortingCriteria) {
		return productDao.getProductFilteredList(searchGridParams, pCriteria, sortingCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductFilteredListcount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public Integer getProductFilteredListcount(final FilterCriteria searchGridParams) {
		return productDao.getProductFilteredListcount(searchGridParams);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getSalesTarget(java.lang.Long)
	 */
	public List<SalesTarget> getSalesTarget(final Long productId) {
		return productDao.getSalesTarget(productId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getProductPlacement(java.lang.Long, java.lang.Long)
	 */
	public String getProductPlacement(final Long productId, final Long salesTargetId) {
		return productDaoAMPT.getProductPlacement(productId, salesTargetId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductCreatives(java.lang.Long)
	 */
	public List<ProductCreativeAssoc> getProductCreatives(final Long productId) {
		return productDaoAMPT.getProductCreatives(productId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductCreativesForTemplates(com.nyt.mpt.domain.LineItem)
	 */
	@Override
	public List<ProductCreativeAssoc> getProductCreativesForTemplates(final LineItem lineItem) {
		return productDaoAMPT.getProductCreativesForTemplates(lineItem);
	}


	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getLineItemAttributes(java.lang.Long, java.lang.Long)
	 */
	public List<ProductAttributeAssoc> getLineItemAttributes(final Long productId, final Long salesTargetId) {
		return productDaoAMPT.getLineItemAttributes(productId, salesTargetId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getLineItemAttrByAttrKey(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public List<ProductAttributeAssoc> getLineItemAttrByAttrKey(final Long productId, final Long salesTargetId, final String attributeKey) {
		return productDaoAMPT.getLineItemAttrByAttrKey(productId, salesTargetId, attributeKey);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#addLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	public Long addLineItemAttribute(final ProductAttributeAssoc prodAttrAssoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding LineItem Attribute for id: " + prodAttrAssoc.getAssociationId());
		}
		return productDaoAMPT.addLineItemAttribute(prodAttrAssoc);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#updateLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	public Long updateLineItemAttribute(final ProductAttributeAssoc assoc) {
		Long returnType = 0L;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating LineItem Attribute for id: " + assoc.getAssociationId());
		}
		final List<ProductAttributeAssoc> prodAttrAssocLst =
					productDaoAMPT.getLineItemAttributes(assoc.getProductId(), assoc.getSalesTargetId());
		if (!prodAttrAssocLst.isEmpty()) {
			for (ProductAttributeAssoc productAttributeAssoc : prodAttrAssocLst) {
				if (productAttributeAssoc.getAttribute().getAttributeId() == assoc.getAttribute().getAttributeId()) {
					productAttributeAssoc.setAttributeValue(assoc.getAttributeValue());
					returnType = productDaoAMPT.updateLineItemAttribute(productAttributeAssoc);
				}
			}
		}
		return returnType;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#deleteLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	public Long deleteLineItemAttribute(final ProductAttributeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting LineItem Attribute for id: " + assoc.getAssociationId());
		}
		final List<ProductAttributeAssoc> lineItemAttrLst =
							productDaoAMPT.getLineItemAttributes(assoc.getProductId(), assoc.getSalesTargetId());
		if (!lineItemAttrLst.isEmpty()) {
			for (ProductAttributeAssoc lineItemAssoc : lineItemAttrLst) {
				if (lineItemAssoc.getAttribute().getAttributeId() == assoc.getAttribute().getAttributeId()) {
					productDaoAMPT.deleteLineItemAttribute(lineItemAssoc);
				}
			}
		}
		return 0L;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getSOSProductListByIDs(java.lang.Long[])
	 */
	@Override
	public List<com.nyt.mpt.domain.sos.Product> getSOSProductListByIDs(final Long[] ids){
		return productDaoSOS.getProductListByIDs(ids);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getSalesTargetByType(java.lang.Long)
	 */
	public List<SalesTarget> getSalesTargetByType(final Long targetType) {
		return productDao.getSalesTargetByType(targetType);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getSalesTargetTypeForPackage()
	 */
	@Override
	public List<SalesTargetType> getAllSalesTargetType() {
		return productDao.getAllSalesTargetType();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getClusterSalesTargetsBySalesTargetId(long)
	 */
	@Override
	public List<ClusterSalesTarget> getClusterSalesTargetsBySalesTargetId(final long salesTargetId) {
		final List<ClusterSalesTarget> clusterSTList = productDao.getClusterSalesTargetsBySalesTargetId(salesTargetId);
		if (clusterSTList == null || clusterSTList.isEmpty()) {
			final SalesTarget salesTarget = productDao.getSalesTargetById(salesTargetId);
			final String[] messageArguments = {salesTarget.getSalesTargeDisplayName() };
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.adxSalesTargetDataNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
		}
		return clusterSTList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.service.IAdProductService#getProductById(java.lang.Long)
	 */
	@Override
	public Product getProductById(final Long productId) {
		return productDao.getProductById(productId);
	}
	
	
	/**
	 * @param productId
	 * @return
	 */
	@Override
	public Product getInactiveProductById(final Long productId) {
		return productDao.getInactiveProductById(productId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getFilteredProductCreatives(java.lang.Long, com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<ProductCreativeAssoc> getFilteredProductCreatives(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		return productDaoAMPT.getFilteredProductCreatives(productId, filterCriteria, pgCriteria, sortingCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getPrimaryPageGroupBySalesTargetId(long)
	 */
	@Override
	public PrimaryPageGroup getPrimaryPageGroupBySalesTargetId(final long salesTargetId) {
		final PrimaryPageGroup primaryPageGroup = productDao.getPrimaryPageGroupBySalesTargetId(salesTargetId);
		if (primaryPageGroup == null) {
			final SalesTarget salesTarget = productDao.getSalesTargetById(salesTargetId);
			final String[] messageArguments = {salesTarget.getSalesTargeDisplayName()};
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.adxSalesTargetDataNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
		}

		return primaryPageGroup;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getPrimaryPageGroupBySalesTargetIds(long, java.lang.Long[])
	 */
	@Override
	public List<PrimaryPageGroup> getPrimaryPageGroupBySalesTargetIds(final long clusterSTId, final Long[] salesTargetIds) {
		final List<PrimaryPageGroup> primaryPGList = productDao.getPrimaryPageGroupBySalesTargetIds(salesTargetIds);
		if (primaryPGList == null || primaryPGList.isEmpty()) {
			final SalesTarget salesTarget = productDao.getSalesTargetById(clusterSTId);
			final String[] messageArguments = {salesTarget.getSalesTargeDisplayName()};
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.adxSalesTargetDataNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
		}

		return primaryPGList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductPositionsByProductId(long)
	 */
	@Override
	public List<ProductPosition> getProductPositionsByProductId(final long productId) {
		final List<ProductPosition> prodPositionList = productDao.getProductPositionsByProductId(productId);
		if (prodPositionList == null || prodPositionList.isEmpty()) {
			final Product product = productDao.getProductById(productId);
			final String[] messageArguments = {product.getDisplayName()};
			throw new YieldexAvailsException(YieldexAvailsException.getCustomeBusinessError(ErrorCodes.adxProductDataNotAvailable,
					ErrorMessageType.YIELDEX_AVAILS_ERROR, messageArguments));
		}

		return prodPositionList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getFilteredProductCreativesCount(java.lang.Long, com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredProductCreativesCount(final Long productId, final FilterCriteria filterCriteria) {
		return productDaoAMPT.getFilteredProductCreativesCount(productId, filterCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getFilteredProductAttributes(java.lang.Long, com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<ProductAttributeAssoc> getFilteredProductAttributes(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pgCriteria, final SortingCriteria sortingCriteria) {
		return productDaoAMPT.getFilteredProductAttributes(productId, filterCriteria, pgCriteria, sortingCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getFilteredProductAttributesCount(java.lang.Long, com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getFilteredProductAttributesCount(final Long productId, final FilterCriteria filterCriteria) {
		return productDaoAMPT.getFilteredProductAttributesCount(productId, filterCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllProducts()
	 */
	@Override
	public List<Product> getAllProducts() {
		return productDao.getAllProducts();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductClass()
	 */
	@Override
	public Map<Long, String> getProductClass() {
		return productDao.getProductClass();
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductsClassMap()
	 */
	@Override
	public Map<String, Long> getProductsClassMap() {
		return productDao.getProductsClassMap();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#isDuplicateProductAttributeAssocExist(long, long, long)
	 */
	@Override
	public boolean isDuplicateProductAttributeAssocExist(final long productId, final long attributeId, final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for duplicate Product Attribute Association. ProductId: " + productId + " attributeId: " + attributeId);
		}
		return productDaoAMPT.isDuplicateProductAttributeAssocExist(productId, attributeId, salesTargetId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllProductsForSalesTarget(long)
	 */
	@Override
	public Map<Long, String> getAllProductsForSalesTarget(final long salesTargetID) {
		return productDao.getAllProductsForSalesTarget(salesTargetID);
	}
	@Override
	public Map<Long, String> getAllProductsForMultiSalesTarget(final List<Long> saleTargetTypeLst) {
		 return productDao.getAllProductsForMultiSalesTarget(saleTargetTypeLst);
	 }

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllSalesTargetForMultiTargetType(java.lang.Long[])
	 */
	@Override
	public List<SalesTarget> getAllSalesTargetForMultiTargetType(final Long[] saleTargetTypeIds) {
		return productDao.getAllSalesTargetForMultiTargetType(saleTargetTypeIds);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductCreativesName(java.util.List)
	 */
	@Override
	public Map<Long, String> getProductCreativesName(final List<Long> productIdList) {
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		final List<ProductCreativeAssoc> prodCreativeList = productDaoAMPT.getFilteredProductCreativesAssocList(productIdList);
		if (!prodCreativeList.isEmpty()) {
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeList) {
				if (returnMap.containsKey(productCreativeAssoc.getProductId())) {
					returnMap.put(productCreativeAssoc.getProductId(), returnMap.get(productCreativeAssoc.getProductId()) + ", "
							+ productCreativeAssoc.getCreative().getName());
				} else {
					returnMap.put(productCreativeAssoc.getProductId(), productCreativeAssoc.getCreative().getName());
				}
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#updateProductCreative(java.lang.Long, java.lang.String)
	 */
	@Override
	public void updateProductCreative(final long productId, final List<Long> creativeIds) {
		final List<ProductCreativeAssoc> assoc = productDaoAMPT.getProductCreatives(productId);
		for (ProductCreativeAssoc values : assoc) {
			productDaoAMPT.deleteProductCreative(values);
		}

		if (creativeIds != null) {
			for (Long creativeId : creativeIds) {
				final Creative bean = new Creative();
				bean.setCreativeId(creativeId);
				final ProductCreativeAssoc prodCreativeAssoc = new ProductCreativeAssoc();
				prodCreativeAssoc.setProductId(productId);
				prodCreativeAssoc.setCreative(bean);
				productDaoAMPT.addProductCreative(prodCreativeAssoc);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductAttrAssocListByAttributeId(long)
	 */
	public List<ProductAttributeAssoc> getProductAttrAssocListByAttributeId(final long attributeId) {
		LOGGER.info("Search Product Associated with AttributeId:" + attributeId);
		return productDaoAMPT.getProductAttrAssocListByAttributeId(attributeId);
	 }

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getParentSalesTargetId(java.util.List)
	 */
	@Override
	public Map<Long, Long> getParentSalesTargetId(final List<Long> saleTargetTypeLst) {
		return productDao.getParentSalesTargetId(saleTargetTypeLst);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getLineItemProductPlacement(java.lang.Long, java.util.List)
	 */
	@Override
	public String getLineItemProductPlacement(final Long productId, final List<Long> sosSalesTargetId) {
		return productDaoAMPT.getSosProductPlacement(productId, sosSalesTargetId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductsBySearchString(java.lang.String)
	 */
	@Override
	public List<Product> getProductsBySearchString(final String productName) {
		return productDao.getProductsBySearchString(productName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getSalesTargetFromProductID(java.lang.Long)
	 */
	@Override
	public List<SalesTarget> getSalesTargetFromProductID(final Long productID) {
		return productDao.getSalesTargetFromProductID(productID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllReservableProducts()
	 */
	@Override
	public List<Product> getAllReservableProducts(final boolean isReservable) {
		return productDao.getAllReservableProducts(isReservable);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllInactiveProduct()
	 */
	@Override
	public List<Long> getAllInactiveProducts() {
		return productDao.getInactiveProducts();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#deleteProductCreativeAssoc(java.util.List)
	 */
	@Override
	public void deleteProductCreativeAssoc(final List<Long> productIDLst) {
		final List<ProductCreativeAssoc> creativeAssocLst = productDaoAMPT.getFilteredProductCreativesAssocList(productIDLst);
		if (creativeAssocLst != null && !creativeAssocLst.isEmpty()) {
			productDaoAMPT.deleteProductCreativeAssoc(creativeAssocLst);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#deleteProductAttributeAssoc(java.util.List)
	 */
	@Override
	public void deleteProductAttributeAssoc(final List<Long> productIDLst) {
		final List<ProductAttributeAssoc> attributeAssocLst = productDaoAMPT.getProductAttributeAssocLst(productIDLst);
		if (attributeAssocLst != null && !attributeAssocLst.isEmpty()) {
			productDaoAMPT.deleteProductAttributeAssoc(attributeAssocLst);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllSalesTargetForMultiProduct(java.util.List)
	 */
	@Override
	public List<SalesTarget> getAllSalesTargetForMultiProduct(final List<Long> productIdLst){
		return productDao.getAllSalesTargetForMultiProduct(productIdLst);
	}
	/**
	 * Returns all products and sales target combinations.
	 * @return
	 */
	public Map<Long, Map<Long, String>> getAllSalesTargetsAndProducts() {
		return productDao.getAllSalesTargetsAndProducts();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getAllReservableProductsForProductClass(long, boolean)
	 */
	@Override
	public Map<Long, String> getProductsByAvailsId(
			final long availsSystemId, final boolean reservable) {
		return 	productDao.getProductsByAvailsId(availsSystemId, reservable);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.service.IProductService#getProductClassIdLstByDisplayName(java.util.List)
	 */
	@Override
	public List<Long> getProductClassIdLstByDisplayName(){
		String[] productClassNameArr =  premiumProductClassNames.split(",");
		List<String> productClassDisplayNameLst = StringUtil.getListFromArray(productClassNameArr);
		return productDao.getProductClassIdLstByDisplayName(productClassDisplayNameLst);
	}
	
	public void setProductDao(final IProductDAO productDao) {
		this.productDao = productDao;
	}

	public void setProductDaoAMPT(final IProductDAOAMPT productDaoAMPT) {
		this.productDaoAMPT = productDaoAMPT;
	}

	public void setProductDaoSOS(IProductDAOSOS productDaoSOS) {
		this.productDaoSOS = productDaoSOS;
	}

	public void setPremiumProductClassNames(String premiumProductClassNames) {
		this.premiumProductClassNames = premiumProductClassNames;
	}
}
