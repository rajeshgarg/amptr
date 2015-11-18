/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.nyt.mpt.dao.IRateProfileDAO;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This DAO level class is used for List Rate Profile related operation
 *
 * @author sachin.ahuja
 *
 */
@SuppressWarnings("unchecked")
public class RateProfileDAO extends GenericDAOImpl implements IRateProfileDAO {

	private static final String SALES_CATEGORY_ID = "salesCategoryId";

	private static final String PROFILE_ID = "profileId";

	private static final String PRODUCT_NAME = "productName";

	private static final String PRODUCT_ID = "productId";

	private static final Logger LOGGER = Logger.getLogger(RateProfileDAO.class);

	/**
	 * Map for list rate profile field form and entity map
	 */
	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_COLUMN_MAP.put(PROFILE_ID, PROFILE_ID);
		FIELD_COLUMN_MAP.put(SALES_CATEGORY_ID, SALES_CATEGORY_ID);
		FIELD_COLUMN_MAP.put("salesTargetNamesStr", "listRateConfig.salesTargetName");
		FIELD_COLUMN_MAP.put(PRODUCT_ID, PRODUCT_ID);
		FIELD_COLUMN_MAP.put(PRODUCT_NAME, PRODUCT_NAME);
		FIELD_COLUMN_MAP.put("basePrice", "basePrice");
		FIELD_COLUMN_MAP.put("sectionNames", "sectionNames");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#saveListRateProfile(com.nyt.mpt.domain.ListRateProfile)
	 */
	@Override
	public RateProfile saveRateProfile(final RateProfile rateProfile) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving list rate profile with Id:" + rateProfile.getProfileId());
		}
		saveOrUpdate(rateProfile);
		return rateProfile;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#getListRateProfile(long)
	 */
	@Override
	public RateProfile getRateProfile(final long profileId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching rate list with Id: " + profileId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);
		criteria.add(Restrictions.eq(PROFILE_ID, profileId));
		return (RateProfile) findByCriteria(criteria).get(0);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IRateProfileDAO#getRateProfilesBySalesCategory(long)
	 */
	@Override
	public List<RateProfile> getRateProfilesBySalesCategory(final Long salesCategoryId){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching rate list with Sales Category Id: " + salesCategoryId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		
		if (salesCategoryId == null){
			criteria.add(Restrictions.isNull(SALES_CATEGORY_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_CATEGORY_ID, salesCategoryId));
		}
		
		final List<RateProfile> rateProfiles = findByCriteria(criteria);
		for (RateProfile rateProfile : rateProfiles) {
			Hibernate.initialize(rateProfile.getRateConfigSet());
			Hibernate.initialize(rateProfile.getSeasonalDiscountsLst());
		}
		return rateProfiles;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#deleteListRateConfigAssoc(com.nyt.mpt.domain.ListRateConfig)
	 */
	@Override
	public void deleteRateConfig(RateConfig listRateConfig) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Sales Target for: " + listRateConfig.getConfigId());
		}
		delete(listRateConfig);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IRateProfileDAO#isDuplicateRateProfile(long, long, java.util.List)
	 */
	@Override
	public boolean isDuplicateRateProfile(final RateProfile rateProfile, final Long[] salesTargetIds) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq(PRODUCT_ID, rateProfile.getProductId()));
		if (rateProfile.getSalesCategoryId() == null) {
			criteria.add(Restrictions.isNull(SALES_CATEGORY_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_CATEGORY_ID, rateProfile.getSalesCategoryId()));
		}
		final DetachedCriteria subQuery = DetachedCriteria.forClass(RateConfig.class);
		subQuery.add(Restrictions.in("salesTargetId", salesTargetIds));
		subQuery.setProjection(Projections.property("rateProfile.profileId"));
		criteria.add(Subqueries.propertyIn("id", subQuery));

		if (rateProfile.getProfileId() != 0) {
			criteria.add(Restrictions.ne(PROFILE_ID, rateProfile.getProfileId()));
		}		
		
		final List<RateProfile> list = findByCriteria(criteria);
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#getFilteredListRateProfileList(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<RateProfile> getFilteredRateProfileList(final List<FilterCriteria> filterCriteria,
			final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered list rate profile list with filterCriteria: " + filterCriteria);
		}
		DetachedCriteria criteria = null;
		try {
			 criteria = constructFilterCriteriaForListRateProfile(filterCriteria);
		} catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		final List<RateProfile> rateProfiles = findByCriteria(criteria, pageCriteria);
		for (RateProfile rateProfile : rateProfiles) {
			Hibernate.initialize(rateProfile.getRateConfigSet());
			Hibernate.initialize(rateProfile.getSeasonalDiscountsLst());
		}
		return rateProfiles;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#getFilteredListRateProfileCount(java.util.List)
	 */
	@Override
	public int getFilteredRateProfileCount(final List<FilterCriteria> filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered list rate profile list count with filterCriteria: " + filterCriteria);
		}
		try {
			return getCount(constructFilterCriteriaForListRateProfile(filterCriteria));
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
	
	/**
	 * @param criteriaList
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForListRateProfile(final List<FilterCriteria> criteriaList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for List rate profile. Filter Criteria: " + criteriaList);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);

		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if(criteriaList != null){
		for (FilterCriteria filterCriteria : criteriaList) {			
			if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
				if ("salesTargetNamesStr".equalsIgnoreCase(filterCriteria.getSearchField())) {
					// Sub Query for List Rate Configuration to fetch based on sales target
					final DetachedCriteria subQuery = DetachedCriteria.forClass(RateConfig.class);
					subQuery.add(Restrictions.ilike("salesTargetName", filterCriteria.getSearchString(), MatchMode.ANYWHERE));
					subQuery.setProjection(Projections.property("rateProfile.profileId"));
					criteria.add(Subqueries.propertyIn("id", subQuery));
				} else if("productName".equalsIgnoreCase(filterCriteria.getSearchField())) {
					criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), 
							filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				} else if("sectionNames".equalsIgnoreCase(filterCriteria.getSearchField()) && StringUtils.isNotBlank(filterCriteria.getSearchString())) {
					criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), 
							filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				} else if ("basePrice".equalsIgnoreCase(filterCriteria.getSearchField())) {
					try {
						if(StringUtils.isNotBlank(filterCriteria.getSearchString())) {
							criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), 
									Double.valueOf(filterCriteria.getSearchString())));
						}
					} catch (NumberFormatException ex) {
						LOGGER.info("Invalid search input for " + FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()) + 
								" - " + filterCriteria.getSearchString());
						throw ex;
					}
				}
				if (SALES_CATEGORY_ID.equalsIgnoreCase(filterCriteria.getSearchField())) {
					if ("null".equalsIgnoreCase(filterCriteria.getSearchString())) {
						criteria.add(Restrictions.isNull(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField())));
					} else {
						criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), NumberUtil.longValue(filterCriteria.getSearchString())));
					}
				}
			}
		}
	}
		return criteria;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IRateProfileDAO#getProfileSeasonalDiscountLst(long)
	 */
	@Override
	public List<RateProfileSeasonalDiscounts> getProfileSeasonalDiscountLst(long rateProfileId ){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching discounts of the Rate Profile with Id: " + rateProfileId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfileSeasonalDiscounts.class);
		criteria.createAlias("rateProfile", "rateProfile");
		criteria.add(Restrictions.eq("rateProfile.profileId", rateProfileId));
		List<RateProfileSeasonalDiscounts> discountList = findByCriteria(criteria);
		return discountList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#deleteListRateConfigAssoc(com.nyt.mpt.domain.ListRateConfig)
	 */
	@Override
	public void deleteSeasonalDiscount(RateProfileSeasonalDiscounts listRateDiscount) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Discounts for the List Rate Profile with Id: " + listRateDiscount.getRateProfile().getProfileId());
		}
		delete(listRateDiscount);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IRateProfileDAO#getRateProfilesBySalesCategoryAndProduct(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<RateProfile> getRateProfilesBySalesCategoryAndProduct(final Long salesCategoryId , final Long productId){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching rate list with Sales Category Id: " + salesCategoryId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		
		if (salesCategoryId == null){
			criteria.add(Restrictions.isNull(SALES_CATEGORY_ID));
		} else {
			criteria.add(Restrictions.eq(SALES_CATEGORY_ID, salesCategoryId));
		}
		
		if(productId != null){
			criteria.add(Restrictions.eq(PRODUCT_ID, productId));
		}
		
		final List<RateProfile> rateProfiles = findByCriteria(criteria);
		for (RateProfile rateProfile : rateProfiles) {
			Hibernate.initialize(rateProfile.getRateConfigSet());
			Hibernate.initialize(rateProfile.getSeasonalDiscountsLst());
		}
		return rateProfiles;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.nyt.mpt.dao.IRateProfileDAO#getProfileSeasonalDiscountLst(long)
	 */
	@Override
	public List<RateProfileSeasonalDiscounts> getSeasonalDiscountByIds(Long[] discountIdLst ){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching discounts having Ids as : " + discountIdLst);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfileSeasonalDiscounts.class);
		criteria.add(Restrictions.in("discountId", discountIdLst));
		criteria.addOrder(Order.asc("discountId"));
		List<RateProfileSeasonalDiscounts> discountList = findByCriteria(criteria);
		return discountList;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IListRateProfileDAO#getListRateProfile(long)
	 */
	@Override
	public List<RateProfile> getRateProfileLstByIds(final Long[] profileIdLst) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching rate profile list with Ids: " + profileIdLst);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(RateProfile.class);
		criteria.add(Restrictions.in(PROFILE_ID, profileIdLst));
		final List<RateProfile> rateProfiles = findByCriteria(criteria);
		
		for (RateProfile rateProfile : rateProfiles) {
			Hibernate.initialize(rateProfile.getRateConfigSet());
			Hibernate.initialize(rateProfile.getSeasonalDiscountsLst());
		}
		return rateProfiles;
	}
}
