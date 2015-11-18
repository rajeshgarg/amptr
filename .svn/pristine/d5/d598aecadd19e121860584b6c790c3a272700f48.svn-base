/**
 * 
 */
package com.nyt.mpt.dao.impl;

import static com.nyt.mpt.util.ConstantStrings.UNCHECKED;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.nyt.mpt.dao.IDashboardDAO;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.UserFilter;
import com.nyt.mpt.util.EntityFormPropertyMap;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.SfProposalRevisionTypeEnum;
import com.nyt.mpt.util.enums.UserFilterTypeEnum;
import com.nyt.mpt.util.filter.DateRangeFilterCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.InFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>DashboardDAO</code> includes all the methods to show the dash-board and all its related {@link Proposal} in the grid. 
 * This includes methods for dash-board for various views i.e. bar view, graph view and calendar view. This also include how the {@link UserFilter} will be saved in database and fetched when required
 * @author surendra.singh
 */

public class DashboardDAO extends GenericDAOImpl implements IDashboardDAO {
	
	private static final Logger LOGGER = Logger.getLogger(DashboardDAO.class);
	
	private static final String USER_ROLE = "userRole";
	
	private static final String ASSIGNED_USER = "assignedUser";

	private static final String PROPOSAL_VERSION = "proposalVersion";
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDashboardDAO#saveDashboardFilter(com.nyt.mpt.domain.DashboardFilter)
	 */
	@Override
	public UserFilter saveDashboardFilter(final UserFilter filter) {
		saveOrUpdate(filter);
		return filter;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDashboardDAO#deleteDashboardFilter(long)
	 */
	@Override
	public void deleteDashboardFilter(final long filterId) {
		final UserFilter filter = load(UserFilter.class, filterId);
		delete(filter);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDashboardDAO#getUserSaveFilterList(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<UserFilter> getUserDashboardFilterList(final long userId) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(UserFilter.class).addOrder(Order.asc("filterName"));
		criteria.add(Restrictions.eq("filterType", UserFilterTypeEnum.DASHBOARD.name()));
		criteria.add(Restrictions.eq("userId", userId));
		return findByCriteria(criteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IDashboardDAO#getDashboardFilterById(long)
	 */
	@Override
	public UserFilter getDashboardFilterById(final long filterId) {
		return load(UserFilter.class, filterId);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalDashboardGridList(java.util.List, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getProposalList(final List<FilterCriteria> filterList, final PaginationCriteria pgCriteria, final SortingCriteria sortCriteria) {
		final DetachedCriteria criteria = getCriteriaForProposalData(filterList);
		if (sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(EntityFormPropertyMap.DB_COLUMN_MAP.get(sortCriteria.getSortingField()));
			super.addSortingCriteria(criteria, sortCriteria);

			sortCriteria.setSortingField(EntityFormPropertyMap.DB_COLUMN_MAP.get("id"));
			super.addSortingCriteria(criteria, sortCriteria);
		}
		return findByCriteria(criteria, pgCriteria);
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalDashboardGridListCount(java.util.List)
	 */
	@Override
	public int getProposalCount(final List<FilterCriteria> criteriaList) {
		return findByCriteria(getCriteriaForProposalData(criteriaList)).size();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IProposalDAO#getProposalDashboardData(java.util.List)
	 */
	@Override
	@SuppressWarnings(UNCHECKED)
	public List<Proposal> getProposalDashboardData(final List<FilterCriteria> filterList) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.createAlias(ASSIGNED_USER, ASSIGNED_USER, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("assignedUser.userRoles", USER_ROLE, CriteriaSpecification.LEFT_JOIN);

		/**
		 * To avoid multiple use criteria 
		 */
		boolean userCriteriaExist = false;
		if (filterList != null && !filterList.isEmpty()) {
			for (FilterCriteria filterCriteria : filterList) {
				if (ASSIGNED_USER.equalsIgnoreCase(filterCriteria.getSearchField())) {
					userCriteriaExist = true;
				}
			}
		}
		if (userCriteriaExist) {
			if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
				criteria.add(Restrictions.eq("withPricing", true));
			}
		} else {
			if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
				criteria.add(Restrictions.eq("withPricing", true));
				criteria.add(Restrictions.disjunction().add(Restrictions.ne("userRole.roleName", SecurityUtil.PRICING_ADMIN)).add(Restrictions.eq("assignedUser.userId", SecurityUtil.getUser().getUserId())));
			} else if (SecurityUtil.isUserProposalPlanner(SecurityUtil.getUser())) {
				final Criterion conjunction = Restrictions.conjunction().add(Restrictions.eq("assignedUser.displayOnWarRoom", true)).
						add(Restrictions.eq("userRole.roleName", SecurityUtil.MEDIA_PLANNER)).add(Restrictions.eq("assignedUser.userId", SecurityUtil.getUser().getUserId()));
				criteria.add(Restrictions.disjunction().add(conjunction).add(Restrictions.isNull(ASSIGNED_USER)));
			} else {
				criteria.add(Restrictions.isNull(ASSIGNED_USER));
			}
		}
		setFilterCriteria(filterList, criteria);
		return findByCriteria(criteria);
	}

	/**
	 * Returns {@link DetachedCriteria} for the List of {@link FilterCriteria}
	 * @param 	filterList
	 * 			List of {@link FilterCriteria}
	 * @return
	 * 			Returns {@link DetachedCriteria}
	 */
	private DetachedCriteria getCriteriaForProposalData(final List<FilterCriteria> filterList) {
		// This code is used to create inner join criteria with max proposal version
		final DetachedCriteria innerJoinCriteria = DetachedCriteria.forClass(ProposalVersion.class, "proposaVersion");
		innerJoinCriteria.setProjection(Property.forName("proposaVersion.proposalVersion").max());
		innerJoinCriteria.add(Property.forName("proposaVersion.proposalOption.id").eqProperty("propOption.id"));

		final DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class, "propo");
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())){
			criteria.createAlias(ASSIGNED_USER, ASSIGNED_USER, CriteriaSpecification.INNER_JOIN);
			criteria.createAlias("assignedUser.userRoles", USER_ROLE, CriteriaSpecification.INNER_JOIN);
		} else {
			criteria.createAlias(ASSIGNED_USER, ASSIGNED_USER, CriteriaSpecification.LEFT_JOIN);
			criteria.createAlias("assignedUser.userRoles", USER_ROLE, CriteriaSpecification.LEFT_JOIN);
		}
		
		/**
		 * To avoid multiple use criteria
		 */
		boolean userCriteriaExist = false;
		if (filterList != null && !filterList.isEmpty()) {
			for (FilterCriteria filterCriteria : filterList) {
				if (ASSIGNED_USER.equalsIgnoreCase(filterCriteria.getSearchField())) {
					userCriteriaExist = true;
				}
			}
		}
		if (userCriteriaExist) {
			if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
				criteria.add(Restrictions.eq("withPricing", true));
			}
		} else {
			if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
				criteria.add(Restrictions.eq("withPricing", true));
				criteria.add(Restrictions.disjunction().add(Restrictions.ne("userRole.roleName", SecurityUtil.PRICING_ADMIN)).add(Restrictions.eq("assignedUser.userId", SecurityUtil.getUser().getUserId())));
			} else if (SecurityUtil.isUserProposalPlanner(SecurityUtil.getUser())) {
				final Criterion conjunction = Restrictions.conjunction().add(Restrictions.eq("assignedUser.displayOnWarRoom", true)).
						add(Restrictions.eq("userRole.roleName", SecurityUtil.MEDIA_PLANNER)).add(Restrictions.eq("assignedUser.userId", SecurityUtil.getUser().getUserId()));
				criteria.add(Restrictions.disjunction().add(conjunction).add(Restrictions.isNull(ASSIGNED_USER)));
			} else {
				criteria.add(Restrictions.isNull(ASSIGNED_USER));
			}
		}
		
		criteria.createAlias("proposalOptions", "propOption");
		criteria.add(Restrictions.eq("propOption.defaultOption",true));
		criteria.createAlias("propOption.proposalVersions", PROPOSAL_VERSION, CriteriaSpecification.INNER_JOIN);

		// Applying inner join for proposal version
		criteria.add(Property.forName("proposalVersion.proposalVersion").eq(innerJoinCriteria));

		setFilterCriteria(filterList, criteria);
		return criteria;
	}

	/**
	 * Sets List of {@link FilterCriteria} to {@link DetachedCriteria}
	 * @param filterList
	 * @param criteria
	 */
	@SuppressWarnings("rawtypes")
	private void setFilterCriteria(final List<FilterCriteria> filterList, final DetachedCriteria criteria) {
		LOGGER.info("Adding filter criteria for dashbord");
		if (filterList != null && !filterList.isEmpty()) {
			for (FilterCriteria filterCriteria : filterList) {
				if ("proposalStatus".equalsIgnoreCase(filterCriteria.getSearchField())
						|| "salescategory".equalsIgnoreCase(filterCriteria.getSearchField())
						|| "criticality".equalsIgnoreCase(filterCriteria.getSearchField())) {
					final InFilterCriteria inFilterCriteria = (InFilterCriteria) filterCriteria;
					criteria.add(Restrictions.in(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), inFilterCriteria.getSearchvalues()));
					if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
						criteria.add(Restrictions.eq("withPricing", true));
					} else {
						criteria.add(Restrictions.eq("withPricing", false));
					}
				} else if ("dueOn".equalsIgnoreCase(filterCriteria.getSearchField())) {
					final DateRangeFilterCriteria dateRangeFc = (DateRangeFilterCriteria) filterCriteria;
					if (dateRangeFc.getSearchOper().equals(SearchOption.BETWEEN.name())) {
						criteria.add(Restrictions.between(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), 
								dateRangeFc.getSearchStringFrom(), dateRangeFc.getSearchStringTo()));
					} else if (dateRangeFc.getSearchOper().equals(SearchOption.LESS.name())) {
						criteria.add(Restrictions.lt(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), 
								dateRangeFc.getSearchStringTo()));
					} else if (dateRangeFc.getSearchOper().equals(SearchOption.GREATER.name())) {
						criteria.add(Restrictions.gt(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), 
								dateRangeFc.getSearchStringFrom()));
					}
					if(SecurityUtil.isUserPricingAdmin(SecurityUtil.getUser())) {
						criteria.add(Restrictions.eq("withPricing", true));
					} else {
						criteria.add(Restrictions.eq("withPricing", false));
					}
				} else if (ASSIGNED_USER.equalsIgnoreCase(filterCriteria.getSearchField())) {
					if (filterCriteria instanceof InFilterCriteria) {
						final InFilterCriteria inFilterCriteria = (InFilterCriteria) filterCriteria;
						criteria.add(Restrictions.in(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
								inFilterCriteria.getSearchvalues()));
					} else {
						if (StringUtils.isNotBlank(filterCriteria.getSearchString()) && "0".equals(filterCriteria.getSearchString())) {
							criteria.add(Restrictions.isNull(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField())));
							criteria.add(Restrictions.isNull("sfRevisionType"));
							criteria.add(Restrictions.eq("withPricing", false));
						} else if(StringUtils.isNotBlank(filterCriteria.getSearchString()) && "-1".equals(filterCriteria.getSearchString())){
							criteria.add(Restrictions.isNull(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField())));
							criteria.add(Restrictions.eq("sfRevisionType", SfProposalRevisionTypeEnum.REVISION.name()));
							criteria.add(Restrictions.eq("withPricing", false));
						} else if(StringUtils.isNotBlank(filterCriteria.getSearchString()) && "-2".equals(filterCriteria.getSearchString())) {
							criteria.add(Restrictions.isNull(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField())));
							criteria.add(Restrictions.eq("sfRevisionType", SfProposalRevisionTypeEnum.SALESORDER.name()));
							criteria.add(Restrictions.eq("withPricing", false));
						} else if(StringUtils.isNotBlank(filterCriteria.getSearchString()) && "-3".equals(filterCriteria.getSearchString())) {
							criteria.add(Restrictions.ne("userRole.roleName", SecurityUtil.PRICING_ADMIN));
							criteria.add(Restrictions.eq("withPricing", true));
						} else {
							criteria.add(Restrictions.eq(EntityFormPropertyMap.DB_COLUMN_MAP.get(filterCriteria.getSearchField()), Long.valueOf(filterCriteria.getSearchString())));
						}
					}
				}
			}
		}
	}
}
