/**
 * 
 */
package com.nyt.mpt.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
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
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.exception.YieldexAvailsException;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for Product Service
 * 
 * @author amandeep.singh
 * 
 */
public class ProductTest extends AbstractTest {

	@Autowired
	@Qualifier("productService")
	private IProductService productService;

	@Autowired
	@Qualifier("creativeService")
	private ICreativeService creativeService;

	@Autowired
	@Qualifier("attributeService")
	private IAttributeService attributeService;

	List<SalesTarget> saleTargetLst = null;
	private Product productDB = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		List<Product> productLst = productService.getAllProducts();
		for (Product product : productLst) {
			saleTargetLst = productService.getSalesTarget(product.getId());
			if (saleTargetLst != null && saleTargetLst.size() > 0) {
				productDB = product;
				break;
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProduct() {
		List<Product> productList = productService.getAllProducts();
		if (productList != null && productList.size() > 0) {
			Assert.assertTrue(productList.size() > 0);
			Assert.assertTrue(productList.get(0) != null);
			Assert.assertTrue(productList.get(0).getName() != null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetSalesTarget() {
		if (productDB != null) {
			List<SalesTarget> saleTargetDBLst = productService.getSalesTarget(productDB.getId());
			if (saleTargetDBLst != null && saleTargetLst != null) {
				Assert.assertTrue(saleTargetDBLst.size() == saleTargetLst.size());
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductFilteredList() {
		List<Product> products = productService.getProductFilteredList(null, new PaginationCriteria(1, 10), null);
		Assert.assertTrue(products.size() <= 10);

		products = productService.getProductFilteredList(null, new PaginationCriteria(1, 10), null);
		int count = productService.getProductFilteredListcount(null);
		Assert.assertTrue(products.size() <= count);

		products = productService.getProductFilteredList(null, null, null);
		count = productService.getProductFilteredListcount(null);
		Assert.assertTrue(products.size() == count);
	}

	/**
	 * 
	 */
	@Test
	public void testGetSalesTargetForType() {
		if (saleTargetLst != null & saleTargetLst.size() > 0) {
			List<SalesTarget> type = productService.getSalesTargetByType(saleTargetLst.get(0).getSalesTargetType().getSalestargetTypeId());
			if (type != null && type.size() > 0) {
				Assert.assertTrue(type.size() > 0);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductById() {
		if (productDB != null) {
			Product product = productService.getProductById(productDB.getId());
			Assert.assertTrue(product != null);
			Assert.assertTrue(product.getId() == productDB.getId());
			Assert.assertTrue(product.getName() != null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetFilteredProductCreative() {
		if (productDB != null) {
			List<ProductCreativeAssoc> proAssocs = productService.getFilteredProductCreatives(productDB.getId(), null, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(proAssocs.size() <= 10);

			int count = productService.getFilteredProductCreativesCount(productDB.getId(), null);
			Assert.assertTrue(proAssocs.size() <= count);

			proAssocs = productService.getFilteredProductCreatives(productDB.getId(), null, null, null);
			count = productService.getFilteredProductCreativesCount(productDB.getId(), null);
			Assert.assertTrue(proAssocs.size() == count);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductAttribute() {
		if (productDB != null) {
			List<ProductAttributeAssoc> proAssocs = productService.getFilteredProductAttributes(productDB.getId(), null, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(proAssocs.size() <= 10);

			int count = productService.getFilteredProductAttributesCount(productDB.getId(), null);
			Assert.assertTrue(proAssocs.size() <= count);

			proAssocs = productService.getFilteredProductAttributes(productDB.getId(), null, null, null);
			count = productService.getFilteredProductAttributesCount(productDB.getId(), null);
			Assert.assertTrue(proAssocs.size() == count);

			List<Product> productList = productService.getAllProducts();
			if (productList != null && productList.size() > 0) {
				for (Product productlst : productList) {
					proAssocs = productService.getFilteredProductAttributes(productlst.getId(), null, null, null);
					if (proAssocs != null && proAssocs.size() > 0) {
						for (ProductAttributeAssoc proassocs : proAssocs) {
							if (proassocs.getSalesTargetId() != null) {
								FilterCriteria filterCriteria = new FilterCriteria();
								filterCriteria.setSearchField("salesTargetId");
								filterCriteria.setSearchString(String.valueOf(proassocs.getSalesTargetId()));
								count = productService.getFilteredProductAttributesCount(productlst.getId(), filterCriteria);
								Assert.assertTrue(count >= 0);
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testUpdateLineItemAttribute() {
		List<Product> productList = productService.getAllProducts();
		if (productList != null && productList.size() > 0) {
			for (Product productlst : productList) {
				FilterCriteria filterCriteria = null;
				PaginationCriteria paginationCriteria = null;
				SortingCriteria sortingCriteria = null;
				List<ProductAttributeAssoc> proAssocsLst = productService.getFilteredProductAttributes(productlst.getId(), filterCriteria, paginationCriteria, sortingCriteria);
				if (proAssocsLst != null && proAssocsLst.size() > 0) {
					for (ProductAttributeAssoc proAssocslst : proAssocsLst) {
						ProductAttributeAssoc assoc = proAssocslst;
						long count = productService.updateLineItemAttribute(assoc);
						Assert.assertTrue(count > 0);
						break;
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductCreativesName() {
		List<Product> productListAll = productService.getAllProducts();
		List<Long> productIDList = new ArrayList<Long>();
		if (productListAll != null && productListAll.size() > 0) {
			for (Product productlstAll : productListAll) {
				productIDList.add(productlstAll.getId());
			}
		}
		if (productIDList != null && productIDList.size() > 0) {
			Map<Long, String> productMap = productService.getProductCreativesName(productIDList);
			Assert.assertTrue(productMap.size() >= 0);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductPlacement() {
		if (productDB != null && saleTargetLst != null && saleTargetLst.size() > 0) {
			Long salesTargetId = saleTargetLst.iterator().next().getSalesTargetId();
			String productPlacement = productService.getProductPlacement(productDB.getId(), salesTargetId);
			if (StringUtils.isNotBlank(productPlacement)) {
				Assert.assertNotNull(productPlacement);
			} else {
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductAttrAssocListByAttributeId() {
		List<Attribute> attributes = attributeService.getAttributeList(true, AttributeType.CREATIVE);
		if (attributes != null && attributes.size() > 0) {
			long attributeId = attributes.get(0).getAttributeId();
			List<ProductAttributeAssoc> productAttributeAssocLst = productService.getProductAttrAssocListByAttributeId(attributeId);
			Assert.assertTrue(productAttributeAssocLst.size() >= 0);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductCreatives() {
		List<Product> productLst = productService.getAllProducts();
		List<Creative> creativeList = creativeService.getCreativeList(true);
		if (productLst != null && productLst.size() > 0) {
			for (Product product : productLst) {
				List<ProductCreativeAssoc> productCreativesLst = productService.getProductCreatives(product.getId());
				Assert.assertTrue(productCreativesLst.size() >= 0);
			}
		}
		Assert.assertTrue(creativeList.size() >= 0);
	}

	/**
	 * 
	 */
	@Test
	public void testGetLineItemAttributes() {
		if (productDB != null && saleTargetLst != null && saleTargetLst.size() > 0) {
			Long salesTargetId = saleTargetLst.get(0).getSalesTargetId();
			List<ProductAttributeAssoc> lineItemAttributesLst = productService.getLineItemAttributes(productDB.getId(), salesTargetId);
			if (lineItemAttributesLst != null && lineItemAttributesLst.size() > 0) {
				Assert.assertEquals(lineItemAttributesLst.get(0).getProductId(), productDB.getId());
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetLineItemAttrByAttrKey() {
		if (productDB != null) {
			List<Attribute> attributes = attributeService.getAttributeList(true, AttributeType.CREATIVE);
			if (attributes != null && attributes.size() > 0 && saleTargetLst != null && saleTargetLst.size() > 0) {
				Long salesTargetId = saleTargetLst.get(0).getSalesTargetId();
				String attributeKey = attributes.get(0).getAttributeKey();
				List<ProductAttributeAssoc> LineItemAttr = productService.getLineItemAttrByAttrKey(productDB.getId(), salesTargetId, attributeKey);
				Assert.assertTrue(LineItemAttr.size() >= 0);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetAllProductsForSalesTarget() {
		if (saleTargetLst != null && saleTargetLst.size() > 0) {
			long salesTargetID = saleTargetLst.get(0).getSalesTargetId();
			Map<Long, String> productsMap = productService.getAllProductsForSalesTarget(salesTargetID);
			if (productsMap != null && productsMap.size() > 0) {
				Assert.assertTrue(productsMap.size() >= 0);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testIsDuplicateProductAttributeAssocExist() {
		if (productDB != null) {
			List<Attribute> attributes = attributeService.getAttributeList(true, AttributeType.CREATIVE);
			List<SalesTarget> saleTargetLst = productService.getSalesTarget(productDB.getId());
			if (attributes != null && attributes.size() > 0 && saleTargetLst != null && saleTargetLst.size() > 0) {
				long attributeId = attributes.get(0).getAttributeId();
				Long salesTargetId = saleTargetLst.get(0).getSalesTargetId();
				boolean isDuplicateProductAttribute = productService.isDuplicateProductAttributeAssocExist(productDB.getId(), attributeId, salesTargetId);
				if (isDuplicateProductAttribute) {
					Assert.assertTrue(isDuplicateProductAttribute);
				} else {
					Assert.assertFalse(isDuplicateProductAttribute);
				}
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testAddLineItemAttribute() {
		List<Product> productList = productService.getAllProducts();
		if (productList != null && productList.size() > 0) {
			for (Product productlst : productList) {
				List<SalesTarget> saleTargetLst = productService.getSalesTarget(productlst.getId());
				if (saleTargetLst != null && saleTargetLst.size() > 0) {
					for (SalesTarget saletargetlst : saleTargetLst) {
						Long salesTargetId = saletargetlst.getSalesTargetId();
						List<ProductAttributeAssoc> lineItemAttributesLst = productService.getLineItemAttributes(productlst.getId(), salesTargetId);
						if (lineItemAttributesLst != null && lineItemAttributesLst.size() > 0) {
							for (ProductAttributeAssoc lineItemattributesLst : lineItemAttributesLst) {
								ProductAttributeAssoc productAttributeAssoc = lineItemattributesLst;
								Long productId = productService.addLineItemAttribute(productAttributeAssoc);
								Assert.assertTrue(productId >= 0);
								break;
							}
						}

					}
				}
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetPrimaryPageGroupBySalesTargetId() {
		if (saleTargetLst != null && saleTargetLst.size() > 0) {
			long salesTargetId = saleTargetLst.get(0).getSalesTargetId();
			try {
				PrimaryPageGroup primaryPageGroup = productService.getPrimaryPageGroupBySalesTargetId(salesTargetId);
				Assert.assertTrue(primaryPageGroup != null);
			} catch (YieldexAvailsException e) {
				Assert.assertTrue(true);
			}
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetReservableProducts() {
		List<Product> productList = productService.getAllReservableProducts(false);
		if (productList != null && productList.size() > 0) {
			Assert.assertTrue(productList.size() > 0);
			Assert.assertTrue(productList.get(0) != null);
			Assert.assertTrue(productList.get(0).getName() != null);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetClusterSalesTargetsBySalesTargetId() {
		if (saleTargetLst != null && saleTargetLst.size() > 0) {
			SalesTarget salesTarget = saleTargetLst.get(0);
			try {
				List<ClusterSalesTarget> clusterSalesTarget = productService.getClusterSalesTargetsBySalesTargetId(salesTarget.getSalesTargetId());
				if (clusterSalesTarget != null && !clusterSalesTarget.isEmpty()) {
					Assert.assertTrue(true);
				}
			} catch (YieldexAvailsException e) {
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductsBySearchString() {
		if (productDB != null && StringUtils.isNotBlank(productDB.getName())) {
			List<Product> productLst = productService.getProductsBySearchString(productDB.getName());
			Assert.assertTrue(productLst.size() >= 1);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductPositionsByProductId() {
		if (productDB != null) {
			try {
				List<ProductPosition> productPositionLst = productService.getProductPositionsByProductId(productDB.getId());
				if (productPositionLst != null && !productPositionLst.isEmpty()) {
					Assert.assertTrue(true);
				}
			} catch (YieldexAvailsException e) {
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetLineItemProductPlacement() {
		List<Long> sosSalesTargetId = new ArrayList<Long>();
		if (productDB != null && saleTargetLst != null && saleTargetLst.size() > 0) {
			for (SalesTarget saleTarget : saleTargetLst) {
				sosSalesTargetId.add(saleTarget.getSalesTargetId());
			}
			String productPlacementName = productService.getLineItemProductPlacement(productDB.getId(), sosSalesTargetId);
			Assert.assertTrue(StringUtils.isNotEmpty(productPlacementName));
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetSalesTargetFromProductID() {
		if (productDB != null) {
			List<SalesTarget> salTargetLst = productService.getSalesTargetFromProductID(productDB.getId());
			Assert.assertTrue(salTargetLst.size() >= saleTargetLst.size());
		}
	}

	/**
	 * 
	 */
	@Test
	public void testGetProductCreativesForTemplates() {
		List<ProductCreativeAssoc> productCreativeAssocLst = productService.getProductCreativesForTemplates(getDummyLineItem());
		Assert.assertTrue(productCreativeAssocLst.size() >= 0);
	}

	/**
	 * 
	 */
	@Test
	public void testGetAllSalesTargetType() {
		List<SalesTargetType> allSalesTarget = productService.getAllSalesTargetType();
		Assert.assertTrue(allSalesTarget.size() >= 0);
		if (allSalesTarget != null && !allSalesTarget.isEmpty()) {
			List<Long> salestargetTypeIdLst = new ArrayList<Long>();
			salestargetTypeIdLst.add(allSalesTarget.get(0).getSalestargetTypeId());
			Map<Long, String> productsMap = productService.getAllProductsForMultiSalesTarget(salestargetTypeIdLst);
			Assert.assertTrue(productsMap.size() >= 0);
			List<SalesTarget> allSalesTargetLst = productService.getAllSalesTargetForMultiTargetType(salestargetTypeIdLst.toArray(new Long[salestargetTypeIdLst.size()]));
			Assert.assertTrue(allSalesTargetLst.size() >= 0);
		}
	}

	/**
	 * 
	 */
	@Test
	public void testUpdateProductCreative() {
		if (productDB != null) {
			List<Creative> creativeLst = creativeService.getCreativeList(true);
			if (creativeLst != null && !creativeLst.isEmpty()) {
				List<Long> creativeIds = new ArrayList<Long>();
				creativeIds.add(creativeLst.get(0).getCreativeId());
				productService.updateProductCreative(productDB.getId(), creativeIds);
			}
		}
	}
	
	@Test
	public void testProductService() {
		List<Long> productIDLst = productService.getAllInactiveProducts();
		Assert.assertTrue(productIDLst.size() >= 0);
	}

	/**
	 * @return
	 */
	private LineItem getDummyLineItem() {
		LineItem returnLineItem = new LineItem();
		returnLineItem.setSosProductId(838L);
		returnLineItem.setSpecType("STANDARD,VIDEO,TEXT,RICH_MEDIA");
		return returnLineItem;
	}
	@Test
	public void testGetAllReservableProductsByProductId() {
		Map<Long, String> productList = productService.getProductsByAvailsId(13, true);
		if (productList != null && productList.size() > 0) {
			Assert.assertTrue(productList.size() > 0);
		}
	}
	
	@Test
	public void testGetAllSalesTargetsAndProducts(){
		Assert.assertNotNull(productService.getAllSalesTargetsAndProducts());
	}
	
	@Test
	public void testGetInactiveProductById(){
		Product prod = productService.getInactiveProductById(productDB.getId());
		if(prod != null){
			Assert.assertTrue(prod.getStatus().equalsIgnoreCase("Inactive"));
		}
	}
	
	@Test
	public void testDeleteProductAttributeAssoc(){
		List<Product> productLst = productService.getAllProducts();
		for (Product product : productLst) {
			List<ProductAttributeAssoc> proAssocsLst = productService.getFilteredProductAttributes(product.getId(), null, new PaginationCriteria(1, 5), null);
			if (proAssocsLst != null && !proAssocsLst.isEmpty()) {
				List<Long> prodIds = new ArrayList<Long>(1);
				prodIds.add(product.getId());
				productService.deleteProductAttributeAssoc(prodIds);
				Assert.assertTrue(true);
				break;
			}
		}
	}
	
	@Test
	public void testDeleteProductCreativeAssoc(){
		List<Product> productLst = productService.getAllProducts();
		for (Product product : productLst) {
			List<ProductCreativeAssoc> proAssocsLst = productService.getFilteredProductCreatives(product.getId(), null, new PaginationCriteria(1, 5), null);
			if (proAssocsLst != null && !proAssocsLst.isEmpty()) {
				List<Long> prodIds = new ArrayList<Long>(1);
				prodIds.add(product.getId());
				productService.deleteProductCreativeAssoc(prodIds);
				Assert.assertTrue(true);
				break;
			}
		}
	}
	
	@Test
	public void testDeleteLineItemAttribute(){
		List<Product> productLst = productService.getAllProducts();
		for (Product product : productLst) {
			List<ProductAttributeAssoc> proAssocsLst = productService.getFilteredProductAttributes(product.getId(), null, new PaginationCriteria(1, 5), null);
			if (proAssocsLst != null && !proAssocsLst.isEmpty()) {
				Long id = productService.deleteLineItemAttribute(proAssocsLst.get(0));
				Assert.assertTrue(id == 0);
				break;
			}
		}
	}
	
	@Test
	public void testGetProductClass(){
		Assert.assertFalse(productService.getProductClass().isEmpty());
	}
	
	@Test
	public void testGetParentSalesTarget(){
		if (productDB != null) {
			List<SalesTarget> salTargetLst = productService.getSalesTargetFromProductID(productDB.getId());
			final List<Long> saleTargetTypeLst = new ArrayList<Long>();
			for (SalesTarget salesTarget : salTargetLst) {
				saleTargetTypeLst.add(salesTarget.getSalesTargetId());
			}
			Map<Long, Long> parentTargetId = productService.getParentSalesTargetId(saleTargetTypeLst);
			if(parentTargetId.isEmpty()){
				Assert.assertTrue(parentTargetId.isEmpty());
			}else{
				Assert.assertFalse(parentTargetId.isEmpty());
			}
		}
	}

}
