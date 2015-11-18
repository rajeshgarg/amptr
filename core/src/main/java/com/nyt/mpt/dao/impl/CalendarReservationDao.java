/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;

import com.nyt.mpt.dao.ICalendarReservationDao;
import com.nyt.mpt.dao.ISalesTargetDAO;
import com.nyt.mpt.domain.sos.OrderLineItem;
import com.nyt.mpt.domain.sos.Region;
import com.nyt.mpt.domain.sos.SalesOrder;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.EntityFormPropertyMap;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.reservation.ReservationTO;
import com.nyt.mpt.util.reservation.SalesCalendarReservationDTO;

/**
 * This <code>CalendarReservationDao</code> class includes all the methods to get the reservation data for calendar
 * 
 * @author surendra.singh
 */
public class CalendarReservationDao extends GenericDAOImpl implements ICalendarReservationDao {

	private static final Logger LOGGER = Logger.getLogger(CalendarReservationDao.class);
	private ISalesTargetDAO salesTargetDAO;
	private static final String SALESORDER_STATUS_ID = "1,2,3,5,7,9,10,12,15";
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getSalesOrderForCalendar()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesOrder> getSalesOrderForCalendar(final ReservationTO calendarVO) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales order for calander: ");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class).addOrder(Order.desc("salesOrderId")).addOrder(Order.desc("lineItem.lineItemId"));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("lineItem", "lineItem", Criteria.LEFT_JOIN);
		criteria.createAlias("lineItem.lineItemTargeting", "lineItemTargeting", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.le("lineItem.startDate", calendarVO.getEndDate()));
		criteria.add(Restrictions.ge("lineItem.endDate", calendarVO.getStartDate()));
		criteria.add(Restrictions.gt("lineItem.shareOfReservation", 0D));
		criteria.add(Restrictions.gt("lineItem.totalQtyExpected", 0L));
		final List<Long> salesTargetList = new ArrayList<Long>();
		salesTargetList.add(calendarVO.getSalesTargetId());
		criteria.add(Restrictions.in("lineItem.salesTargetId", salesTargetDAO.getSalesTargetParentOrChild(salesTargetList)));
		final List<Long> productIdList = new ArrayList<Long>();
		productIdList.add(calendarVO.getProductId());
		criteria.add(Restrictions.in("lineItem.productId", getConflictingProductIdList(productIdList)));
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getConflictingProductIdList(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Long> getConflictingProductIdList(final List<Long> productIds) {
		final List<Long> productIdList = new ArrayList<Long>();
		productIdList.addAll(productIds);
		
		final String conflictingProduct = "SELECT PC.CONFLICT_PRODUCT_ID AS PRODUCT_ID FROM PRODUCT_BASE P, PRODUCT_CONFLICT PC " +
				"WHERE P.IS_RESERVABLE = 'Y' AND P.PRODUCT_ID = PC.BASE_PRODUCT_ID AND P.PRODUCT_ID in (:PRODUCT_IDS)";
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(conflictingProduct).addScalar("PRODUCT_ID", LongType.INSTANCE);
		query.setParameterList("PRODUCT_IDS", productIds);
		productIdList.addAll(query.list());
		return productIdList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getSalesOrderForCalendar()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<OrderLineItem> getSalesOrderForReservationSearch(final List<RangeFilterCriteria> criteriaLst, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales order for reservation search: ");
		}
		
		final DetachedCriteria criteria = getCriteriaForSearchReservation();
		try {
			getSOSFilterCriteriaForSearchProposal(criteria, criteriaLst);
		} catch (NumberFormatException ne) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(EntityFormPropertyMap.SALES_ORDER_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		
		final List<OrderLineItem> lineItemsLst = findByCriteria(criteria,pgCriteria);
		if(lineItemsLst != null && lineItemsLst.size() > 0){
			for (OrderLineItem orderLineItem : lineItemsLst) {
				Hibernate.initialize(orderLineItem.getLineItemTargeting());
			}
		}
		return lineItemsLst;
	}
	
	/**
	 * Sets Filter Criteria for search Reservation screen
	 * @param criteria
	 * @param filterCriterias
	 */
	private void getSOSFilterCriteriaForSearchProposal(final DetachedCriteria criteria, final List<RangeFilterCriteria> filterCriterias) {
		if (filterCriterias != null) {
			for (RangeFilterCriteria filterCriteria : filterCriterias) {
				if ("advertiserName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if("productId".equalsIgnoreCase(filterCriteria.getSearchField())){
						criteria.add(Restrictions.in(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							StringUtil.convertStringToLongList(filterCriteria.getSearchString())));
				}
				if("salesTargetId".equalsIgnoreCase(filterCriteria.getSearchField())){
						criteria.add(Restrictions.in(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							StringUtil.convertStringToLongList(filterCriteria.getSearchString())));
				}
				if("sosOrderId".equalsIgnoreCase(filterCriteria.getSearchField())){
					criteria.add(Restrictions.eq(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if("lineItemId".equalsIgnoreCase(filterCriteria.getSearchField())){
					criteria.add(Restrictions.eq(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if ("date".equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringFrom())) {
						criteria.add(Restrictions.ge(EntityFormPropertyMap.SALES_ORDER_MAP.get("endDate"),
								DateUtil.parseToDate(filterCriteria.getSearchStringFrom())));
					}
					if (StringUtils.isNotBlank(filterCriteria.getSearchStringTo())) {
						criteria.add(Restrictions.le(EntityFormPropertyMap.SALES_ORDER_MAP.get("startDate"),
								DateUtil.parseToDate(filterCriteria.getSearchStringTo())));
					}
				}
				if ("salesCategoryId".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.eq(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
				if("sosLineItemId".equalsIgnoreCase(filterCriteria.getSearchField())){
					criteria.add(Restrictions.eq(EntityFormPropertyMap.SALES_ORDER_MAP.get(filterCriteria.getSearchField()),
							NumberUtil.longValue(filterCriteria.getSearchString())));
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getSalesOrderForCalendar()
	 */
	@Override
	public int getSalesOrderForReservationSearchCount(final List<RangeFilterCriteria> criteriaLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales order for reservation search: ");
		}
		
		final DetachedCriteria criteria = getCriteriaForSearchReservation();
		try{
			getSOSFilterCriteriaForSearchProposal(criteria, criteriaLst);
		} catch(NumberFormatException ne){
			return 0;
		}
		
		return getCount(criteria);
	}
	
	/**
	 * Return {@link DetachedCriteria} for Searching of Reservation
	 * @return
	 */
	private DetachedCriteria getCriteriaForSearchReservation() {
		final DetachedCriteria criteria = DetachedCriteria.forClass(OrderLineItem.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("salesOrder", "salesOrder");
		criteria.createAlias("salesOrder.advertiser", "advertiser", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.in("salesOrder.statusId", StringUtil.convertStringArrayToLongArray(SALESORDER_STATUS_ID.split(ConstantStrings.COMMA))));
		criteria.add(Restrictions.gt("shareOfReservation", 0D));
		criteria.add(Restrictions.gt("totalQtyExpected", 0L));	
		return criteria;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getRegionList()
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Region> getRegionList() {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Region.class).addOrder(Order.asc(ConstantStrings.NAME));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.getExecutableCriteria(getHibernateTemplate().getSessionFactory().getCurrentSession()).setCacheable(true);
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ICalendarReservationDao#getSalesOrderForSalesCalendar(com.nyt.mpt.util.reservation.SalesCalendarReservationDTO)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<SalesOrder> getSalesOrderForSalesCalendar(SalesCalendarReservationDTO calendarVO) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching sales order for sales calander: ");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesOrder.class).addOrder(Order.desc("salesOrderId")).addOrder(Order.desc("lineItem.lineItemId"));
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("lineItem", "lineItem", Criteria.LEFT_JOIN);
		criteria.createAlias("owner", "owner", Criteria.LEFT_JOIN);
		criteria.createAlias("lineItem.lineItemTargeting", "lineItemTargeting", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.le("lineItem.startDate", calendarVO.getEndDate()));
		criteria.add(Restrictions.ge("lineItem.endDate", calendarVO.getStartDate()));
		criteria.add(Restrictions.gt("lineItem.shareOfReservation", 0D));
		criteria.add(Restrictions.gt("lineItem.totalQtyExpected", 0L));
		if(calendarVO.getSalesTargetIds() != null && !calendarVO.getSalesTargetIds().isEmpty()){
			criteria.add(Restrictions.in("lineItem.salesTargetId", salesTargetDAO.getSalesTargetParentOrChild(calendarVO.getSalesTargetIds())));
		}
		criteria.add(Restrictions.in("lineItem.productId", getConflictingProductIdList(calendarVO.getProductIds())));
		return findByCriteria(criteria);
	}
	
	public ISalesTargetDAO getSalesTargetDAO() {
		return salesTargetDAO;
	}

	public void setSalesTargetDAO(ISalesTargetDAO salesTargetDAO) {
		this.salesTargetDAO = salesTargetDAO;
	}

}
