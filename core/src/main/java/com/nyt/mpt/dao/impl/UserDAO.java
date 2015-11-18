/**
 *
 */
package com.nyt.mpt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nyt.mpt.dao.IUserDAO;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 *
 * This DAO level class is used for user related operation
 *
 * @author surendra.singh
 *
 */
public class UserDAO extends GenericDAOImpl implements IUserDAO {

	private static final String USER_ROLES = "userRoles";

	private static final String FIRST_NAME = "firstName";

	private static final String LOGIN_NAME = "loginName";

	private static final Logger LOGGER = Logger.getLogger(UserDAO.class);

	/**
	 * Map contains form and entity mapping for User
	 */
	private static final Map<String, String> DB_COLUMN_MAP = new HashMap<String, String>();

	static {
		DB_COLUMN_MAP.put("userId", "userId");
		DB_COLUMN_MAP.put(LOGIN_NAME, LOGIN_NAME);
		DB_COLUMN_MAP.put(FIRST_NAME, FIRST_NAME);
		DB_COLUMN_MAP.put("lastName", "lastName");
		DB_COLUMN_MAP.put("email", "email");
		DB_COLUMN_MAP.put("globalUserString", "global");
		DB_COLUMN_MAP.put("statusString", ConstantStrings.ACTIVE);
		DB_COLUMN_MAP.put("userRolesAsString", "userRoles.displayName");
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.security.dao.IUsertDAO#findByUsername(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User findByUsername(final String loginName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Find by username. LoginName: " + loginName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq(LOGIN_NAME, loginName));
		final List<User> list = findByCriteria(criteria);
		if (list.isEmpty()) {
			LOGGER.info("No user exist for given name : " + loginName);
			throw new UsernameNotFoundException("No user exist for given name : " + loginName);
		}
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.security.dao.IUsertDAO#findByUsername(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User getUserWithAllInfo(final String loginName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching user with all info. LoginName: " + loginName);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq(LOGIN_NAME, loginName));
		criteria.createAlias("salesCategories", "salesCategories", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias(USER_ROLES, USER_ROLES, CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("userRoles.authorities", "userRoles.authorities", CriteriaSpecification.LEFT_JOIN);
		final List<User> list = findByCriteria(criteria);
		if (list.isEmpty()) {
			LOGGER.error("No user exist for given name : " + loginName);
			throw new UsernameNotFoundException("No user exist for given name : " + loginName);
		}
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.security.dao.IUsertDAO#getUserList()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserList() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching userList");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(User.class).addOrder(Order.asc(FIRST_NAME).ignoreCase());
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.security.dao.IUsertDAO#updateUser(com.nyt.mpt.security.User)
	 */
	@Override
	public long saveUser(final User user) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving User: " + user.getUserId());
		}
		saveOrUpdate(user);
		return user.getUserId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#getFilteredUserList(com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getFilteredUserList(final FilterCriteria filterCriteria, final PaginationCriteria pgCriteria,
			final SortingCriteria sortCriteria, final boolean showAllUser) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Filtered User List. FilterCriteria - " + filterCriteria);
		}
		final DetachedCriteria criteria = constructFilterCriteria(filterCriteria, showAllUser);
		if(sortCriteria != null) {
			//update the sorting field name in sorting criteria with DB column name 
			sortCriteria.setSortingField(DB_COLUMN_MAP.get(sortCriteria.getSortingField()));
			addSortingCriteria(criteria, sortCriteria);

			sortCriteria.setSortingField(DB_COLUMN_MAP.get("userId"));
			addSortingCriteria(criteria, sortCriteria);
		}
		List<User> userLst = findByCriteria(criteria, pgCriteria);
		if (userLst != null && !userLst.isEmpty()) {
			for (User user : userLst) {
				Set<SalesCategory> salesCategories = user.getSalesCategories();
				for (SalesCategory salesCategory : salesCategories) {
					Hibernate.initialize(salesCategory);
				}
				Set<Role> userRoles = user.getUserRoles();
				for (Role role : userRoles) {
					Hibernate.initialize(role);
				}
			}
		}
		return userLst;
	}

	/**
	 * @param filterCriteria
	 * @return
	 */
	private DetachedCriteria constructFilterCriteria(final FilterCriteria filterCriteria, final boolean showAllUser) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Constructing FilterCriteria. filterCriteria: " + filterCriteria);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias(USER_ROLES, USER_ROLES, CriteriaSpecification.LEFT_JOIN);
		if (filterCriteria != null && StringUtils.isNotBlank(filterCriteria.getSearchField())) {
			if (SearchOption.CONTAIN.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.ANYWHERE));
			} else if (SearchOption.BEGINS_WITH.toString().equals(filterCriteria.getSearchOper())) {
				criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.START));
			} else {
				criteria.add(Restrictions.ilike(DB_COLUMN_MAP.get(filterCriteria.getSearchField()),
						filterCriteria.getSearchString(), MatchMode.EXACT));
			}
		}
		if (!showAllUser) {
			criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, Boolean.TRUE));
		}
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#getUserFilteredListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getUserFilteredListCount(final FilterCriteria filterCriteria, final boolean showAllUser) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching count of filtered user list. FilterCriteria:" + filterCriteria);
		}
		return getCount(constructFilterCriteria(filterCriteria, showAllUser));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#getListOfAllUserMediaPlanners(java.lang.String[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserBasedOnRoleList(final String[] roles) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching List of all media planners");
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(User.class).addOrder(Order.asc("firstName").ignoreCase());
		criteria.createAlias(USER_ROLES, "roles", Criteria.LEFT_JOIN);
		criteria.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);
		
		criteria.add(Restrictions.eq(ConstantStrings.ACTIVE, true));
		criteria.add(Restrictions.in("roles.roleName", roles));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#getUserSalesCategories(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SalesCategory> getUserSalesCategories(final long userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching user sales categories. UserId:" + userId);
		}
		final DetachedCriteria criteria = DetachedCriteria.forClass(SalesCategory.class);
		criteria.createAlias("user", "user");
		criteria.add(Restrictions.eq("user.userId", userId));
		return findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#getUserById(long)
	 */
	@Override
	public User getUserById(final long userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching user by id. UserId: " + userId);
		}
		return getHibernateTemplate().load(User.class, Long.valueOf(userId));
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.dao.IUserDAO#removeSalesCategories(java.util.Set)
	 */
	@Override
	public void removeSalesCategories(final Set<SalesCategory> salesCategories) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Removing SalesCategories salesCategories: " + salesCategories);
		}
		getHibernateTemplate().deleteAll(salesCategories);
	}
}
