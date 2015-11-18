/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;

import com.nyt.mpt.dao.IProductDAO;
import com.nyt.mpt.dao.IProductDAOAMPT;
import com.nyt.mpt.domain.ClusterSalesTarget;
import com.nyt.mpt.domain.PrimaryPageGroup;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductClass;
import com.nyt.mpt.domain.ProductPosition;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.SalesTargetType;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.EntityFormPropertyMap;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for product related operation
 *
 * @author surendra.singh
 *
 */
public class ProductDAO extends GenericDAOImpl implements IProductDAO {

	private static final String ID = "id";

	private static final String SALES_TARGETS = "salesTargets";

	private static final String DISPLAY_NAME = "displayName";

	private static final Logger LOGGER = Logger.getLogger(ProductDAO.class);

	private IProductDAOAMPT productDaoAMPT;


	/**
	 * Product form and entity map
	 */
	private static final Map<String, String> DB_COLUMN_MAP = new HashMap<String, String>();

	static {
		DB_COLUMN_MAP.put("productId", ID);
		DB_COLUMN_MAP.put("productName", "name");
		DB_COLUMN_MAP.put("productDescription", "description");
		DB_COLUMN_MAP.put("typeName", "typeName.productTypeName");
		DB_COLUMN_MAP.put("note", "note");
		DB_COLUMN_MAP.put("className", "className.productClassName");
		DB_COLUMN_MAP.put(DISPLAY_NAME, DISPLAY_NAME);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductFilteredList(com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getProductFilteredList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria,
			final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Product List. FilterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = constructFilterCriteria(filterCriteria);

		if (sortCriteria != null) {
			// update the sorting field name in sorting criteria with DB column name
			sortCriteria.setSortingField(DB_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pageCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductFilteredListcount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public Integer getProductFilteredListcount(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Product List count. FilterCriteria: " + filterCriteria);
		}
		return getCount(constructFilterCriteria(filterCriteria));
	}

	/**
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteria(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("constructing FilterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.createAlias("typeName", "typeName");
		criteria.createAlias("className", "className");

		if (filterCriteria != null && "creativeName".equalsIgnoreCase(filterCriteria.getSearchField())) {
			final Set<Long> productIdsList = productDaoAMPT.getProdIdsByCreativeFilter(filterCriteria);
			if (productIdsList.isEmpty()) {
				criteria.add(Restrictions.eq("id", Long.valueOf(-1)));
			} else {
				criteria.add(Restrictions.in("id", productIdsList));
			}

		} else {
			if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
				if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
					criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(),
							MatchMode.ANYWHERE));
				} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
					criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(),
							MatchMode.START));
				} else {
					criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(),
							MatchMode.EXACT));
				}

			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.product.dao.IAdProductDAO#getSalesTarget(java.lang.Long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTarget(final Long productId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching SalesTarget for product id: " + productId);
		}
		final String hqlQuery = "SELECT ST from SalesTarget ST, ProductPlacement PP WHERE ST.salesTargetId = PP.salesTarget.salesTargetId "
				+ "AND PP.status = 'Active' AND ST.status = 'Active' AND PP.product.id = "
				+ productId + " ORDER BY LOWER(ST.salesTargeDisplayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setCacheable(true);
		return (List<SalesTarget>) hibernateQuery.list();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getSalesTargetByType(java.lang.Long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTargetByType(final Long targetTypeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching SalesTarget By Type targetTypeId: " + targetTypeId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq("salesTargetType.salestargetTypeId", targetTypeId));
		criteria.addOrder(Order.asc("salesTargeDisplayName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getSalesTargetTypeForPackage()
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTargetType> getAllSalesTargetType() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching SalesTarget Type");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTargetType.class);
		criteria.add(Restrictions.not(Restrictions.in("salesTargetTypeName", EntityFormPropertyMap.CLUSTER_SALEST_TARGET_LIST)));
		criteria.addOrder(Order.asc("salesTargetTypeName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getClusterSalesTargetsBySalesTargetId(long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ClusterSalesTarget> getClusterSalesTargetsBySalesTargetId(final long salesTargetId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(ClusterSalesTarget.class);
		criteria.add(Restrictions.eq("clusterSalesTargetId.sosClusterSalesTargetId", salesTargetId));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductById(java.lang.Long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Product getProductById(final Long productId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Product by id: " + productId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq(ID, productId));
		final List<Product> productList = findByCriteria(criteria);
		if (productList.isEmpty()) {
			return null;
		} else {
			final Product product = productList.get(0);
			Hibernate.initialize(product.getClassName());
			Hibernate.initialize(product.getTypeName());
			return product;
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getSalesTargetById(java.lang.Long)
	 */
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public SalesTarget getSalesTargetById(final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales target by id: " + salesTargetId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq("salesTargetId", salesTargetId));
		final List<SalesTarget> salesTargetList = findByCriteria(criteria);
		if (salesTargetList.isEmpty()) {
			return null;
		} else {
			final SalesTarget salesTarget = salesTargetList.get(0);
			Hibernate.initialize(salesTarget.getSalesTargetType());
			return salesTarget;
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductPositionsByProductId(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<ProductPosition> getProductPositionsByProductId(final long productId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductPosition.class);
		criteria.createAlias("product", "product");
		criteria.add(Restrictions.eq("product.id", productId));
		criteria.add(Restrictions.isNotNull("position"));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getPrimaryPageGroupBySalesTargetId(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public PrimaryPageGroup getPrimaryPageGroupBySalesTargetId(final long salesTargetId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(PrimaryPageGroup.class);
		criteria.createAlias(SALES_TARGETS, SALES_TARGETS);
		criteria.add(Restrictions.eq("salesTargets.salesTargetId", salesTargetId));
		final List<PrimaryPageGroup> pageGroups = findByCriteria(criteria);
		if (pageGroups.isEmpty()) {
			return null;
		}
		return pageGroups.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getPrimaryPageGroupBySalesTargetIds(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<PrimaryPageGroup> getPrimaryPageGroupBySalesTargetIds(final Long[] salesTargetIds) {
		final List<PrimaryPageGroup> pageGroupList = new LinkedList<PrimaryPageGroup>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(PrimaryPageGroup.class);
		criteria.createAlias(SALES_TARGETS, SALES_TARGETS);
		if (salesTargetIds != null && salesTargetIds.length >= 1000) {
			int fromIndex, toIndex = 0;
			for (int count = salesTargetIds.length; count >= 0; count = count - 999) {
				fromIndex = toIndex;
				if (count / 1000 == 0) {
					toIndex = toIndex + count % 1000;
				} else {
					toIndex = toIndex + 999;
				}
				final Long[] subArray = Arrays.copyOfRange(salesTargetIds, fromIndex, toIndex);
				criteria.add(Restrictions.in("salesTargets.salesTargetId", subArray));
				pageGroupList.addAll(findByCriteria(criteria));
			}
		} else {
			criteria.add(Restrictions.in("salesTargets.salesTargetId", salesTargetIds));
			pageGroupList.addAll(findByCriteria(criteria));
		}
		return pageGroupList;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllProducts()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getAllProducts() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all products");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.addOrder(Order.asc(DISPLAY_NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Product> productLst = findByCriteria(criteria);
		for (Product product : productLst) {
			Hibernate.initialize(product.getClassName());
		}
		return productLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getActiveInActiveProducts()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getActiveInActiveProductsByProductIds(final List<Long> productIdList) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.in(ID, productIdList));
		criteria.addOrder(Order.asc(DISPLAY_NAME).ignoreCase());
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductClass()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getProductClass() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching product class");
		}
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductClass.class);
		criteria.addOrder(Order.asc("productClassName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<ProductClass> productClasses = (List<ProductClass>) findByCriteria(criteria);
		if (productClasses != null) {
			for (ProductClass productClass : productClasses) {
				returnMap.put(productClass.getProductClassID(), productClass.getProductClassName());
			}
		}
		return returnMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductsClassMap()
	 */
	@Override
	public Map<String, Long> getProductsClassMap() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching product class");
		}
		final Map<String, Long> returnMap = new LinkedHashMap<String, Long>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductClass.class);
		criteria.addOrder(Order.asc("productClassName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<ProductClass> productClasses = (List<ProductClass>) findByCriteria(criteria);
		if (productClasses != null) {
			for (ProductClass productClass : productClasses) {
				returnMap.put( productClass.getProductClassName(), productClass.getProductClassID());
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllReservableProductsByProdutId(long,boolean)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getProductsByAvailsId(final long availsSystemId, final boolean reservable) {
		final Map<Long, String> productMap = new LinkedHashMap<Long, String>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.eq("availsSytemId", availsSystemId));
		criteria.addOrder(Order.asc(DISPLAY_NAME).ignoreCase());
		if (reservable) {
			criteria.add(Restrictions.eq("reservable", "Y"));
		} else {
			criteria.add(Restrictions.isNull("reservable"));
		}
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Product> products = (List<Product>) findByCriteria(criteria);
		if (!products .isEmpty()) {
			for (Product product : products) {
				productMap.put(product.getId(), product.getDisplayName());
			}
		}
		return productMap;
	}
    /* (non-Javadoc)
     * @see com.nyt.mpt.dao.IProductDAO#getAllProductsForSalesTarget(long)
     */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getAllProductsForSalesTarget(final long salesTargetId) {
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		final String hqlQuery = "SELECT PRD from SalesTarget ST, ProductPlacement PP, Product PRD"
				+ " WHERE ST.salesTargetId = PP.salesTarget.salesTargetId AND PP.product.id = PRD.id "
				+ " AND PP.status = 'Active' AND ST.status = 'Active' AND PRD.status = 'Active' AND ST.salesTargetId = :salesTargetId "
				+ " ORDER BY LOWER(PRD.displayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setLong("salesTargetId", salesTargetId);
		hibernateQuery.setCacheable(true);
		final List<Product> products = (List<Product>) hibernateQuery.list();
		if (products != null) {
			for (Product product : products) {
				returnMap.put(product.getId(), product.getDisplayName());
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllSalesTargetForMultiTargetType(java.lang.Long[])
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getAllSalesTargetForMultiTargetType(final Long[] saleTargetTypeIds) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.in("salesTargetType.salestargetTypeId", saleTargetTypeIds));
		criteria.addOrder(Order.asc("salesTargeDisplayName").ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllProductsForMultiSalesTarget(java.util.List)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, String> getAllProductsForMultiSalesTarget(final List<Long> saleTargetTypeLst) {
		List<Product> products = new LinkedList<Product>();
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		final String hqlQuery = "SELECT PRD from SalesTarget ST, ProductPlacement PP, Product PRD"
				+ " WHERE ST.salesTargetId = PP.salesTarget.salesTargetId AND PP.product.id = PRD.id "
				+ " AND PP.status = 'Active' AND ST.status = 'Active' AND PRD.status = 'Active' AND ST.salesTargetId in (:salesTargetIdList) "
				+ " ORDER BY LOWER(PRD.displayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		if (saleTargetTypeLst != null && saleTargetTypeLst.size() >= 1000) {
			int fromIndex, toIndex = 0;
			for (int count = saleTargetTypeLst.size(); count >= 0; count = count - 999) {
				if (count / 1000 == 0) {
					fromIndex = toIndex;
					toIndex = toIndex + count % 1000;
				} else {
					fromIndex = toIndex;
					toIndex = toIndex + 999;
				}
				hibernateQuery.setParameterList("salesTargetIdList", saleTargetTypeLst.subList(fromIndex, toIndex));
				hibernateQuery.setCacheable(true);
				products.addAll((List<Product>) hibernateQuery.list());
			}
		} else {
			hibernateQuery.setParameterList("salesTargetIdList", saleTargetTypeLst);
			hibernateQuery.setCacheable(true);
			products = (List<Product>) hibernateQuery.list();
		}

		if (products != null) {
			for (Product product : products) {
				returnMap.put(product.getId(), product.getDisplayName());
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getParentSalesTargetId(java.util.List)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public Map<Long, Long> getParentSalesTargetId(final List<Long> targetTypeList) {
		final List<SalesTarget> salesTarget = new LinkedList<SalesTarget>();
		final Map<Long, Long> returnMap = new LinkedHashMap<Long, Long>();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching SalesTarget By Type targetTypeId: " + targetTypeList);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesTarget.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		criteria.add(Restrictions.in("parentSalesTargetId", targetTypeList));
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		salesTarget.addAll(findByCriteria(criteria));
		if (salesTarget != null) {
			for (SalesTarget salesTargetObj : salesTarget) {
				returnMap.put(salesTargetObj.getSalesTargetId(), salesTargetObj.getParentSalesTargetId());
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductsBySearchString(java.lang.String)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getProductsBySearchString(final String productName) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.ilike(DISPLAY_NAME, "%" + productName + "%"));
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getSalesTargetFromProductID(java.lang.Long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesTarget> getSalesTargetFromProductID(final Long productID) {
		final String hqlQuery = "SELECT ST from SalesTarget ST, ProductPlacement PP, Product PRD"
				+ " WHERE ST.salesTargetId = PP.salesTarget.salesTargetId AND PP.product.id = PRD.id "
				+ " AND PP.status = 'Active' AND ST.status = 'Active' AND PRD.status = 'Active' AND PRD.id = :productId "
				+ " ORDER BY LOWER(ST.salesTargeDisplayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setLong("productId", productID);
		hibernateQuery.setCacheable(true);
		return (List<SalesTarget>) hibernateQuery.list();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllReservableProducts()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Product> getAllReservableProducts(final boolean isReservable) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all Reservable products");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Product.class);
		criteria.add(Restrictions.eq(ConstantStrings.STATUS, ConstantStrings.ACTIVE_STATUS));
		if (isReservable) {
			criteria.add(Restrictions.eq("reservable", "Y"));
		} else {
			criteria.add(Restrictions.isNull("reservable"));
		}
		criteria.addOrder(Order.asc(DISPLAY_NAME).ignoreCase());
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<Product> productLst = findByCriteria(criteria);
		for (Product product : productLst) {
			Hibernate.initialize(product.getClassName());
		}
		return productLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getInactiveProduct()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getInactiveProducts() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all inactive or deleted products from sos");
		}
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query query = session.createSQLQuery("select SOS_PRODUCT_ID from {h-schema}PRODUCT where DELETE_DATE is not null or STATUS = 'Inactive'").addScalar("SOS_PRODUCT_ID", LongType.INSTANCE);
		return query.list();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllSalesTargetsAndProducts()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Map<Long, String>> getAllSalesTargetsAndProducts() {
		final String hqlQuery = "SELECT PRD.id, ST from SalesTarget ST, ProductPlacement PP, Product PRD"
				+ " WHERE ST.salesTargetId = PP.salesTarget.salesTargetId AND PP.product.id = PRD.id "
				+ " AND PP.status = 'Active' AND ST.status = 'Active' AND PRD.status = 'Active' "
				+ " ORDER BY LOWER(ST.salesTargeDisplayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setCacheable(true);
		
		final Map<Long, Map<Long, String>> productSalesTarget = new LinkedHashMap<Long, Map<Long, String>>();
		for (Iterator iterator = hibernateQuery.list().iterator(); iterator.hasNext();) {
			final Object[] row = (Object[]) (iterator.next());
			final SalesTarget salesTarget = (SalesTarget) row[1];
			if (productSalesTarget.containsKey(row[0])) {
				final Map<Long, String> salesTargetsMap = productSalesTarget.get(row[0]);
				salesTargetsMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
				productSalesTarget.put((Long) row[0], salesTargetsMap);
			} else {
				final Map<Long, String> salesTargetsMap = new LinkedHashMap<Long, String>();
				salesTargetsMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
				productSalesTarget.put((Long) row[0], salesTargetsMap);
			}			
		}
		return productSalesTarget;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getInactiveProductById(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Product getInactiveProductById(Long productId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching all inactive or deleted products from sos");
		}
		final Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query query = session.createSQLQuery("select * from {h-schema}PRODUCT  where DELETE_DATE is not null or STATUS = 'Inactive' and SOS_PRODUCT_ID = " + productId).addEntity(Product.class);
		final List<Product> productlist = query.list();
		if(productlist != null && !productlist.isEmpty()){
			return (Product) query.list().get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getAllSalesTargetForMultiProduct(java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SalesTarget> getAllSalesTargetForMultiProduct(final List<Long> productIdLst) {
		final String hqlQuery = "SELECT ST from SalesTarget ST, ProductPlacement PP, Product PRD"
				+ " WHERE ST.salesTargetId = PP.salesTarget.salesTargetId AND PP.product.id = PRD.id "
				+ " AND PP.status = 'Active' AND ST.status = 'Active' AND PRD.status = 'Active' AND PRD.id IN (:productIdLst) "
				+ " ORDER BY LOWER(ST.salesTargeDisplayName)";
		final Session hibernateSession = getHibernateTemplate().getSessionFactory().getCurrentSession();
		final Query hibernateQuery = hibernateSession.createQuery(hqlQuery);
		hibernateQuery.setParameterList("productIdLst", productIdLst);
		hibernateQuery.setCacheable(true);
		return (List<SalesTarget>) hibernateQuery.list();
	}
	
	public void setProductDaoAMPT(final IProductDAOAMPT productDaoAMPT) {
		this.productDaoAMPT = productDaoAMPT;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProductDAO#getProductClassIdLstByDisplayName(java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getProductClassIdLstByDisplayName(List<String> prdClassDisplayNameLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching product class");
		}
		final List<Long> productClassIdLst = new ArrayList<Long>();
		final DetachedCriteria criteria = DetachedCriteria.forClass(ProductClass.class);
		criteria.add(Restrictions.in("productClassName", prdClassDisplayNameLst));
		final Criteria hibernateCriteria = criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession());
		hibernateCriteria.setCacheable(true);
		final List<ProductClass> productClasses = (List<ProductClass>) findByCriteria(criteria);
		for(ProductClass productClass : productClasses){
			productClassIdLst.add(productClass.getProductClassID());
		}
		return productClassIdLst;
	}
}
