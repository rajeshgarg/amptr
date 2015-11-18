/**
 * 
 */
package com.nyt.mpt.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.nyt.mpt.dao.ITierDAO;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.TierSectionAssoc;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * @author rakesh.tewari
 *
 */
public class TierDAO extends GenericDAOImpl implements ITierDAO {

	private static final String TIER_OBJECT = "tierObj";

	private static final Logger LOGGER = Logger.getLogger(TierDAO.class);

	private static final String TIER_ID = "tierId";
	
	private static final String TIER_NAME = "tierName";
	
	private static final String TARGET_TYPE = "targetType";
	
	private static final String TARGET_TYPE_ID = "targetTypeId";

	private static final Map<String, String> FIELD_COLUMN_MAP = new HashMap<String, String>();

	static {
		FIELD_COLUMN_MAP.put(TIER_NAME, TIER_NAME);
		FIELD_COLUMN_MAP.put(TIER_ID, TIER_ID);
		FIELD_COLUMN_MAP.put("level", "tierLevel");
		FIELD_COLUMN_MAP.put("premiumId", "premiumId");
		FIELD_COLUMN_MAP.put(TARGET_TYPE_ID, TARGET_TYPE);
		FIELD_COLUMN_MAP.put("tarTypeElementId", "tarTypeElementId");
		FIELD_COLUMN_MAP.put("premium", "premium");
		FIELD_COLUMN_MAP.put("premiumID", "id");
		FIELD_COLUMN_MAP.put("targetName", "targetType.name");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#saveAttribute(com.nyt.mpt.domain.Tier)
	 */
	@Override
	public Tier saveTier(final Tier tier) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving tier with Id:" + tier.getTierId());
		}
		saveOrUpdate(tier);
		return tier;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getFilteredTierList(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<Tier> getFilteredTierList(final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered attribute list with filterCriteria: " + filterCriteria);
		}
		DetachedCriteria criteria = null;		
		try {
			 criteria = constructFilterCriteriaForTier(filterCriteria);
		} catch (NumberFormatException ex) {
			return Collections.EMPTY_LIST;
		}
		if(sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		final List<Tier> tierLst = findByCriteria(criteria, pageCriteria);
        for (Tier tier : tierLst) {
               Hibernate.initialize(tier.getTierSectionAssocLst());
               Hibernate.initialize(tier.getTierPremiumLst());
        }
        return tierLst;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getFilteredTierCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredTierCount(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered attribute list count with filterCriteria: " + filterCriteria);
		}
		try {
			return getCount(constructFilterCriteriaForTier(filterCriteria));
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * Construct FilterCriteria for Tier
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteriaForTier(final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing filter criteria for tier. Filter Criteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Tier.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (TIER_ID.equals(filterCriteria.getSearchField())) {
				criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
			} else if( StringUtils.isNotBlank(filterCriteria.getSearchString())&& "level".equals(filterCriteria.getSearchField())){
				try{
					criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
				}catch (NumberFormatException ex) {
					LOGGER.info("Invalid search input for " + FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()) + " - "
							+ filterCriteria.getSearchString());
					throw ex;
				}
			} else if("sectionNames".equals(filterCriteria.getSearchField().trim())){
				// Sub Query for tier sections to be fetched based on the section names.
				final DetachedCriteria subQuery = DetachedCriteria.forClass(TierSectionAssoc.class);
				subQuery.add(Restrictions.ilike("sectionName", filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				subQuery.setProjection(Projections.property("tier.tierId"));
				criteria.add(Subqueries.propertyIn(TIER_ID, subQuery));
			} else if(TIER_NAME.equals(filterCriteria.getSearchField().trim())){
				if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
					criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(), MatchMode.ANYWHERE));
				} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
					criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(), MatchMode.START));
				} else {
					criteria.add(Restrictions.ilike(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), filterCriteria.getSearchString(), MatchMode.EXACT));
				}
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#isDuplicateTierName(java.lang.String, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean isDuplicateTierName(final String tierName, final Long tierID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for Duplicate Tier Name: " + tierName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Tier.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (tierID == null) {
			criteria.add(Restrictions.eq(TIER_NAME, tierName).ignoreCase());
		} else {
			criteria.add(Restrictions.ne(TIER_ID, tierID));
			criteria.add(Restrictions.eq(TIER_NAME, tierName).ignoreCase());
		}
		final List<Tier> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Count for Tier name '" + tierName + "' is: " + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#isDuplicateTierLevel(java.lang.Long, java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean isDuplicateTierLevel(final long tierLevel, final Long tierID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Check for Duplicate Tier Name: " + tierLevel);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Tier.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		if (tierID == null) {
			criteria.add(Restrictions.eq("tierLevel", tierLevel));
		} else {
			criteria.add(Restrictions.ne(TIER_ID, tierID));
			criteria.add(Restrictions.eq("tierLevel", tierLevel));
		}
		final List<Tier> list = findByCriteria(criteria);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Count for Tier Level '" + tierLevel + "' is: " + list.size());
		}
		if (list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getFilteredPremiumCount(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getFilteredTierPremiumCount(final long tierId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered attribute list count with filterCriteria: " + filterCriteria);
		}
		return getCount(constructCriteriaForPremium(tierId, filterCriteria));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getFilteredPremiumList(java.lang.Long, com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TierPremium> getFilteredTierPremiumList(final Long tierId, final FilterCriteria filterCriteria, final PaginationCriteria pageCriteria, final SortingCriteria sortCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching filtered Premium list with filterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = constructCriteriaForPremium(tierId, filterCriteria);
		if(sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(FIELD_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pageCriteria);
	}

	/**
	 * Construct criteria for Premium
	 * @param tierId
	 * @return
	 */
	private DetachedCriteria constructCriteriaForPremium(final Long tierId, final FilterCriteria filterCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing Criteria for premium. based on tierId : " + tierId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TierPremium.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias(TIER_OBJECT, TIER_OBJECT);
		criteria.createAlias(TARGET_TYPE, TARGET_TYPE);
		criteria.add(Restrictions.eq("tierObj.tierId", tierId));
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if ("premiumID".equals(filterCriteria.getSearchField())) {
				criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
			}
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#saveTierPremium(com.nyt.mpt.domain.TierPremium)
	 */
	@Override
	public TierPremium saveTierPremium(final TierPremium premium) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving premium with Id:" + premium.getId());
		}
		saveOrUpdate(premium);
		return premium;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#deleteTierSection(java.util.List)
	 */
	@Override
	public void deleteTierSection(TierSectionAssoc tierSection) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting tierSectionAssoc objects");
		}
		delete(tierSection);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.spec.dao.IAdAttributeDAO#getAttribute(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Tier getTierById(long tierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Tier with TierId: " + tierId);
		}
		Tier tierObj = null;
		final DetachedCriteria criteria = DetachedCriteria.forClass(Tier.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq(TIER_ID, tierId));
		List<Tier> tierLst = findByCriteria(criteria);
		if(!tierLst.isEmpty()){
			tierObj = (Tier) findByCriteria(criteria).get(0);
			Hibernate.initialize(tierObj.getTierSectionAssocLst());
			Hibernate.initialize(tierObj.getTierPremiumLst());
		}
		return tierObj;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getPremiumById(long)
	 */
	@Override
	public TierPremium getPremiumById(final long premiumId){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Premium with premiumId: " + premiumId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(TierPremium.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.eq(FIELD_COLUMN_MAP.get("premiumID"), premiumId));
		return (TierPremium)findByCriteria(criteria).get(0);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getPremiumListByTierId(long)
	 */
	@Override
	@SuppressWarnings(ConstantStrings.UNCHECKED)
	public List<TierPremium> getPremiumListByTierId(long tierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching premium list of a tier, with tier Id : " + tierId);
		}
		
		final DetachedCriteria criteria = DetachedCriteria.forClass(TierPremium.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias(TIER_OBJECT, TIER_OBJECT);
		criteria.createAlias(TARGET_TYPE, TARGET_TYPE);
		criteria.add(Restrictions.eq("tierObj.tierId", tierId));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.ITierDAO#getTierSectionAssocList(java.util.Set)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Tier> getTierSectionAssocList(final Set<Long> sectionIDSet) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Return list of Tier based on section Ids");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(Tier.class);
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.createAlias("tierSectionAssocLst", "tierSectionAssocLst");
		criteria.add(Restrictions.in("tierSectionAssocLst.sectionId", sectionIDSet));
		final List<Tier> tierLst = findByCriteria(criteria); 
		for (Tier tier : tierLst) {
			Hibernate.initialize(tier.getTierSectionAssocLst());
            Hibernate.initialize(tier.getTierPremiumLst());
		}
		return tierLst;
	}
}
