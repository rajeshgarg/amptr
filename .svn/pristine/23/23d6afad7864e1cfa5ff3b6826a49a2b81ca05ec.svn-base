/**
 *
 */
package com.nyt.mpt.dao.impl;

import static com.nyt.mpt.util.ConstantStrings.UNCHECKED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IProductDAOAMPT;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * AMPT DAO implementation for product
 *
 * @author amandeep.singh
 *
 */
public class ProductDAOAMPT extends GenericDAOImpl implements IProductDAOAMPT {

	private static final String CREATIVE = "creative";

	private static final String ATTRIBUTE = "attribute";

	private static final String PRODUCT_ID = "productId";

	private static final String SALES_TARGET_ID = "salesTargetId";

	private static final Logger LOGGER = Logger.getLogger(ProductDAOAMPT.class);

	private static final String PLACEMENT_NAME = "Placement_Name";

	/**
	 * Product creative form and entity map
	 */
	private static final Map<String, String> PRODUCT_CREATIVE_MAP = new HashMap<String, String>();

	static {
		PRODUCT_CREATIVE_MAP.put("creativeId", "creative.creativeId");
		PRODUCT_CREATIVE_MAP.put("associationId", "associationId");
		PRODUCT_CREATIVE_MAP.put("creativeName", "creative.name");
		PRODUCT_CREATIVE_MAP.put("creativeDescription", "creative.description");
	}

	/**
	 * Product attribute form and entity map
	 */
	private static final Map<String, String> PRODUCT_ATTRIBUTE_MAP = new HashMap<String, String>();

	static {
		PRODUCT_ATTRIBUTE_MAP.put("attributeId", "attribute.attributeId");
		PRODUCT_ATTRIBUTE_MAP.put("attributeName", "attribute.attributeName");
		PRODUCT_ATTRIBUTE_MAP.put("attributeDescription", "attribute.attributeDescription");
		PRODUCT_ATTRIBUTE_MAP.put("attributeKey", "attribute.attributeKey");
		PRODUCT_ATTRIBUTE_MAP.put("attributeValue", "attributeValue");
		PRODUCT_ATTRIBUTE_MAP.put(SALES_TARGET_ID, SALES_TARGET_ID);
		PRODUCT_ATTRIBUTE_MAP.put("assocId", "associationId");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProductCreatives(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductCreativeAssoc> getProductCreatives(final Long productId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product creatives. ProductId: " + productId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductCreativeAssoc.class);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		final List<ProductCreativeAssoc> assocList = findByCriteria(criteria);
		for (ProductCreativeAssoc productCreativeAssoc : assocList) {
			Hibernate.initialize(productCreativeAssoc.getCreative());
		}
		return assocList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProductCreativesForTemplates(com.nyt.mpt.domain.LineItem)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductCreativeAssoc> getProductCreativesForTemplates(final LineItem lineItem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product creatives. ProductId: " + lineItem.getSosProductId());
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductCreativeAssoc.class);
		criteria.createAlias("creative", "prodCreative");
		criteria.add(Restrictions.eq(PRODUCT_ID, lineItem.getSosProductId()));
		criteria.add(Restrictions.in("prodCreative.type", lineItem.getSpecType().split(ConstantStrings.COMMA)));
		criteria.addOrder(Order.asc("prodCreative.name").ignoreCase());
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#addProductCreative(com.nyt.mpt.domain.ProductCreativeAssoc)
	 */
	@Override
	public Long addProductCreative(final ProductCreativeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding product creative association for product id: " + assoc.getProductId());
		}
		save(assoc);
		return assoc.getProductId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#deleteProductCreative(com.nyt.mpt.domain.ProductCreativeAssoc)
	 */
	@Override
	public Long deleteProductCreative(final ProductCreativeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Product Creative Association. Association id: " + assoc.getAssociationId());
		}
		delete(assoc);
		return assoc.getAssociationId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getLineItemAttributes(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductAttributeAssoc> getLineItemAttributes(final Long productId, final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching line item attributes. ProductId: " + productId + "Sales TargetId: " + salesTargetId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		if (salesTargetId == null) {
			criteria.add(Restrictions.isNull(SALES_TARGET_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_TARGET_ID, salesTargetId));
		}
		final List<ProductAttributeAssoc> assocList = findByCriteria(criteria);
		for (ProductAttributeAssoc productAttributeAssoc : assocList) {
			Hibernate.initialize(productAttributeAssoc.getAttribute());
		}
		return assocList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getLineItemAttrByAttrKey(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductAttributeAssoc> getLineItemAttrByAttrKey(final Long productId, final Long salesTargetId, final String attributeKey) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching line item attributes By attribute Key, Attribute Key: " + attributeKey);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		if (salesTargetId == null) {
			criteria.add(Restrictions.isNull(SALES_TARGET_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_TARGET_ID, salesTargetId));
		}
		criteria.add(Restrictions.eq("attribute.attributeKey", attributeKey));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#addLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	@Override
	public Long addLineItemAttribute(final ProductAttributeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding LineItem attribute for Product attribute association with product id: " + assoc.getProductId());
		}
		save(assoc);
		return assoc.getProductId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#updateLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	@Override
	public Long updateLineItemAttribute(final ProductAttributeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("updating LineItem attribute  for Product Attribute Association for product Id: " + assoc.getProductId());
		}
		update(assoc);
		return assoc.getProductId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getFilteredProductCreatives(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductCreativeAssoc> getFilteredProductCreatives(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered product creatives. ProductId: " + productId + " FilterCriteria:" + filterCriteria);
		}
		final DetachedCriteria criteria = createCriteriaForProductCreative(productId);
		addSortCriteria(criteria, sortCriteria, PRODUCT_CREATIVE_MAP);
		return findByCriteria(criteria, pageCriteria);
	}

	/**
	 * @param criteria
	 * @param sortingCriteria
	 * @param map
	 */
	private void addSortCriteria(final DetachedCriteria criteria, final SortingCriteria sortingCriteria, final Map<String, String> map) {
		if (sortingCriteria == null) {
			return;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding sorting criteria .Criteria: " + criteria);
		}
		final CustomDbOrder order = new CustomDbOrder();
		order.setAscending(sortingCriteria.getSortingOrder().equals(CustomDbOrder.ASCENDING));
		order.setFieldName(map.get(sortingCriteria.getSortingField()));
		criteria.addOrder(order.getOrder());
	}

	/**
	 * @param productId
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria createCriteriaForProductCreative(final Long productId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creating criteria for Product Creative for productId:" + productId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductCreativeAssoc.class);
		criteria.createAlias(CREATIVE, CREATIVE);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getFilteredProductCreativesCount(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredProductCreativesCount(final Long productId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product creatives count. ProductId: " + productId + "FilterCriteria " + filterCriteria);
		}
		return getCount(createCriteriaForProductCreative(productId));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getFilteredProductAttributes(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductAttributeAssoc> getFilteredProductAttributes(final Long productId, final FilterCriteria filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Product attributes. ProductId: " + productId + "FilterCriteria " + filterCriteria);
		}
		final DetachedCriteria criteria = createCriteriaForProductAttribute(productId, filterCriteria);
		addSortCriteria(criteria, sortCriteria, PRODUCT_ATTRIBUTE_MAP);
		return findByCriteria(criteria, pageCriteria);
	}

	/**
	 * @param productId
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria createCriteriaForProductAttribute(final Long productId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("creating Criteria For ProductAttribute for productId: " + productId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (SALES_TARGET_ID.equals(PRODUCT_ATTRIBUTE_MAP.get(filterCriteria.getSearchField()))) {
				if (filterCriteria.getSearchString() == null) {
					criteria.add(Restrictions.isNull(PRODUCT_ATTRIBUTE_MAP.get(filterCriteria.getSearchField())));
				} else {
					criteria.add(Restrictions.eq(PRODUCT_ATTRIBUTE_MAP.get(filterCriteria.getSearchField()), NumberUtil.longValue(filterCriteria.getSearchString())));
				}
			} else {
				criteria.add(Restrictions.eq(PRODUCT_ATTRIBUTE_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString()));
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getFilteredProductAttributesCount(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredProductAttributesCount(final Long productId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Product attributes count. ProductId: " + productId + "FilterCriteria " + filterCriteria);
		}
		return getCount(createCriteriaForProductAttribute(productId, filterCriteria));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#deleteLineItemAttribute(com.nyt.mpt.domain.ProductAttributeAssoc)
	 */
	@Override
	public void deleteLineItemAttribute(final ProductAttributeAssoc assoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting LineItem Attribute for Product Attribute Association with id:" + assoc.getAssociationId());
		}
		delete(assoc);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#isDuplicateProductAttributeAssocExist(long, long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public boolean isDuplicateProductAttributeAssocExist(final long productId, final long attributeId, final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for duplicate Product Attribute Association for productId: " + productId + " and attributeId: " + attributeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		if (salesTargetId == null) {
			criteria.add(Restrictions.isNull(SALES_TARGET_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_TARGET_ID, salesTargetId));
		}
		criteria.add(Restrictions.eq("attribute.attributeId", attributeId));
		final List<ProductAttributeAssoc> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("count of duplicate Product Attribute Association. Count" + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProductPlacement(java.lang.Long, java.lang.Long)
	 */
	public String getProductPlacement(final Long productId, final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product Placement. ProductId: " + productId + " and SalesTargetId " + salesTargetId);
		}
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hqlQuery = session.createQuery("SELECT PAA.attributeValue from ProductAttributeAssoc PAA"
				+ " where PAA.productId = :productId and PAA.salesTargetId = :salesTargetId" + " and PAA.attribute.attributeKey = :placementName");

		hqlQuery.setLong(PRODUCT_ID, productId.longValue());
		hqlQuery.setLong(SALES_TARGET_ID, salesTargetId.longValue());
		hqlQuery.setString("placementName", PLACEMENT_NAME);
		return (String) hqlQuery.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getFilteredProductCreativesAssocList(java.util.List)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductCreativeAssoc> getFilteredProductCreativesAssocList(final List<Long> productIdList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered product creatives. ProductId: " + productIdList);
		}
		List<ProductCreativeAssoc> creativeAssocLst = new ArrayList<ProductCreativeAssoc>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductCreativeAssoc.class);
		criteria.createAlias(CREATIVE, CREATIVE);
		if (productIdList != null && productIdList.size() >= 1000) {
			int fromIndex, toIndex = 0;
			for (int count = productIdList.size(); count >= 0; count = count - 999) {
				fromIndex = toIndex;
				if (count / 1000 == 0) {
					toIndex = toIndex + count % 1000;
				} else {
					toIndex = toIndex + 999;
				}
				criteria.add(Restrictions.in(PRODUCT_ID, productIdList.subList(fromIndex, toIndex)));
				List<ProductCreativeAssoc> creativeAssocSubLst = findByCriteria(criteria);
				if (creativeAssocSubLst != null && !creativeAssocSubLst.isEmpty()) {
					creativeAssocLst.addAll(creativeAssocSubLst);
				}
			}
		} else {
			criteria.add(Restrictions.in(PRODUCT_ID, productIdList));
			creativeAssocLst.addAll(findByCriteria(criteria));
		}
		return creativeAssocLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProductAttrAssocListByAttributeId(long)
	 */
	@SuppressWarnings(UNCHECKED)
	public List<ProductAttributeAssoc> getProductAttrAssocListByAttributeId(final long attributeId) {
		LOGGER.info("Search Product which Associated with AttributeId: " + attributeId);
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		criteria.add(Restrictions.eq("attribute.attributeId", attributeId));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProdIdsByCreativeFilter(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public Set<Long> getProdIdsByCreativeFilter(final FilterCriteria filterCriteria) {
		final Set<Long> prodIdsList = new HashSet<Long>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductCreativeAssoc.class);
		criteria.createAlias("creative", "creative");

		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(PRODUCT_CREATIVE_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.ANYWHERE));
			} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(PRODUCT_CREATIVE_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.START));
			} else {
				criteria.add(Restrictions.ilike(PRODUCT_CREATIVE_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.EXACT));
			}
		}
		final List<ProductCreativeAssoc> creativeAssocList =  findByCriteria(criteria);
		if (creativeAssocList != null) {
			for (ProductCreativeAssoc productCreativeAssoc : creativeAssocList) {
				prodIdsList.add(productCreativeAssoc.getProductId());
			}
		}
		return prodIdsList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getSosProductPlacement(java.lang.Long, java.util.List)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public String getSosProductPlacement(final Long productId, final List<Long> salesTargetId) {
		final Set<String> placementNameList = new HashSet<String>();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product Placement. ProductId: " + productId + " and SalesTargetId " + salesTargetId);
		}
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hqlQuery = session.createQuery("SELECT PAA.attributeValue from ProductAttributeAssoc PAA"
				+ " where PAA.productId = :productId and PAA.salesTargetId in (:salesTargetId)" + " and PAA.attribute.attributeKey = :placementName");

		hqlQuery.setLong(PRODUCT_ID, productId.longValue());
		hqlQuery.setString("placementName", PLACEMENT_NAME);
		if (salesTargetId != null && salesTargetId.size() >= 1000) {
			int fromIndex, toIndex = 0;
			for (int count = salesTargetId.size(); count >= 0; count = count - 999) {
				fromIndex = toIndex;
				if (count / 1000 == 0) {
					toIndex = toIndex + count % 1000;
				} else {
					toIndex = toIndex + 999;
				}
				hqlQuery.setParameterList(SALES_TARGET_ID, salesTargetId.subList(fromIndex, toIndex));
				final List<String> list = hqlQuery.list();
				if (list != null && !list.isEmpty()) {
					placementNameList.addAll(list);
				}
			}

		} else {
			hqlQuery.setParameterList(SALES_TARGET_ID, salesTargetId);
			final List<String> list = hqlQuery.list();
			if (list != null && !list.isEmpty()) {
				placementNameList.addAll(list);
			}
		}
		final StringBuffer placementName = new StringBuffer();
		if (placementNameList != null) {
			for (String objects : placementNameList) {
				if (placementName.length() > 0) {
					placementName.append(',');
				}
				placementName.append(objects);
			}
		}
		return placementName.toString();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#deleteProductCreativeAssoc(java.util.List)
	 */
	@Override
	public void deleteProductCreativeAssoc(final List<ProductCreativeAssoc> productCreativeAssocLst) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.debug("Delete all creatives which are associated with inactive products");
		}
		deleteAll(productCreativeAssocLst);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#deleteProductAttributeAssoc(java.util.List)
	 */
	@Override
	public void deleteProductAttributeAssoc(final List<ProductAttributeAssoc> productAttributeAssocLst) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.debug("Delete all attribute which are associated with inactive products");
		}
		deleteAll(productAttributeAssocLst);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAOAMPT#getProductAttributeAssocLst(java.util.List)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<ProductAttributeAssoc> getProductAttributeAssocLst(final List<Long> productIdList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching product attribute assoc by list of product ids.");
		}
		final List<ProductAttributeAssoc> attributeAssocLst = new ArrayList<ProductAttributeAssoc>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductAttributeAssoc.class);
		criteria.createAlias(ATTRIBUTE, ATTRIBUTE);
		if (productIdList != null && productIdList.size() >= 1000) {
			int fromIndex, toIndex = 0;
			for (int count = productIdList.size(); count >= 0; count = count - 999) {
				fromIndex = toIndex;
				if (count / 1000 == 0) {
					toIndex = toIndex + count % 1000;
				} else {
					toIndex = toIndex + 999;
				}
				criteria.add(Restrictions.in(PRODUCT_ID, productIdList.subList(fromIndex, toIndex)));
				final List<ProductAttributeAssoc> attributeAssocSubLst = findByCriteria(criteria);
				if (attributeAssocSubLst != null && !attributeAssocSubLst.isEmpty()) {
					attributeAssocLst.addAll(attributeAssocSubLst);
				}
			}
		} else {
			criteria.add(Restrictions.in(PRODUCT_ID, productIdList));
			attributeAssocLst.addAll(findByCriteria(criteria));
		}
		return attributeAssocLst;
	}
}
