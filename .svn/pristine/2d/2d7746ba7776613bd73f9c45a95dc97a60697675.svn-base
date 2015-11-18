package com.nyt.mpt.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.nyt.mpt.dao.IRoleDAO;
import com.nyt.mpt.dao.IUserDAO;
import com.nyt.mpt.domain.Role;
import com.nyt.mpt.domain.SalesCategory;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.annotation.Validate;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.ldap.ILdapSearchProvider;

/**
 * 
 * This service class is used to provide ldap user operation.
 * @author surendra.singh
 *
 */
public class UserService implements IUserService, UserDetailsService {
	
	private static final Logger logger = Logger.getLogger(UserService.class);

	private IUserDAO userDao;
	
	private IRoleDAO roleDAO;
	
	private ILdapSearchProvider ldapSearchProvider;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		if(logger.isDebugEnabled()){
			logger.debug("Loading user by username: "+username);
		}
		return userDao.getUserWithAllInfo(username);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#searchUserFromLdap(java.lang.String)
	 */
	public List<User> searchUserFromLdap(String userName){
		if(logger.isDebugEnabled()){
			logger.debug("Searching user from LDAP: "+userName);
		}
		return ldapSearchProvider.searchUserFromLdap(userName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#createUser(com.nyt.mpt.domain.User)
	 */
	@Override
	public long createUser(User user) {
		if(logger.isDebugEnabled()){
			logger.debug("Creating user with name: "+user.getLoginName());
		}
		for (SalesCategory salesCategory : user.getSalesCategories()) {
			salesCategory.setUser(user);
		}
		userDao.saveUser(user);
		return user.getUserId();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#updateUser(com.nyt.mpt.domain.User, boolean)
	 */
	@Validate
	@Override
	public long updateUser(User user, boolean forceUpdate) {
		if(logger.isDebugEnabled()){
			logger.debug("Updating user with userid: "+user.getUserId());
		}
		User userDb = getUserById(user.getUserId());
		userDb.setGlobal(user.isGlobal());
		userDb.setActive(user.isActive());		
		userDb.setAddress1(user.getAddress1());
		userDb.setAddress2(user.getAddress2());
		userDb.setState(user.getState());
		userDb.setCity(user.getCity());
		userDb.setZip(user.getZip());
		userDb.setMobile(user.getMobile());
		userDb.setTelephone(user.getTelephone());
		userDb.setFaxNo(user.getFaxNo());
		userDb.setUserRoles(user.getUserRoles());
		userDb.setVersion(user.getVersion());
		userDb.setDisplayOnWarRoom(user.isDisplayOnWarRoom());
		
		removeSalesCategories(userDb.getSalesCategories());		
		for (SalesCategory salesCategory : user.getSalesCategories()) {
			salesCategory.setUser(userDb);
		}
		userDb.setSalesCategories(user.getSalesCategories());
		userDao.saveUser(userDb);
		return user.getUserId();
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getUserList()
	 */
	@Override
	public List<User> getUserList() {
		return userDao.getUserList();
	}

	/**
	 * Remove all given sales category
	 * @param salesCategories
	 */
	private void removeSalesCategories(Set<SalesCategory> salesCategories) {
		if(logger.isDebugEnabled()){
			logger.debug("Removing sales category from user."+salesCategories);
		}
		userDao.removeSalesCategories(salesCategories);		
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getUserById(long)
	 */
	public User getUserById(long userId) {
		return userDao.getUserById(userId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getAllAssignableRole()
	 */
	@Override
	public List<Role> getAllAssignableRole() {
		return roleDAO.getAllAssignableRole();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getUserSalesCategories(long)
	 */
	@Override
	public List<SalesCategory> getUserSalesCategories(long userId) {
		return userDao.getUserSalesCategories(userId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#isUserExists(java.lang.String)
	 */
	@Override
	public boolean isUserExists(String loginName) {
		if(logger.isDebugEnabled()){
			logger.debug("Check for whether username exists for loginName: "+loginName);
		}
		return userDao.findByUsername(loginName) != null ;
	}
	
	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getFilteredUserList(com.nyt.mpt.util.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<User> getFilteredUserList(FilterCriteria filterCriteria,
			PaginationCriteria paginationCriteria, SortingCriteria sortingCriteria, final boolean showAllUser) {
		return userDao.getFilteredUserList(filterCriteria, paginationCriteria, sortingCriteria, showAllUser);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getUserFilteredListCount(com.nyt.mpt.util.FilterCriteria)
	 */
	@Override
	public int getUserFilteredListCount(FilterCriteria filterCriteria, final boolean showAllUser) {
		return userDao.getUserFilteredListCount(filterCriteria, showAllUser);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.IUserService#getUserBasedOnRoleList()
	 */
	public List<User> getUserBasedOnRoleList(final String [] roles) {
		return userDao.getUserBasedOnRoleList(roles);
	}
	
	public IRoleDAO getRoleDAO() {
		return roleDAO;
	}

	public void setRoleDAO(IRoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	public void setUserDao(IUserDAO userDao) {
		this.userDao = userDao;
	}
	
	public IUserDAO getUserDao() {
		return userDao;
	}
	
	public void setLdapSearchProvider(ILdapSearchProvider ldapSearchProvider) {
		this.ldapSearchProvider = ldapSearchProvider;
	}
}
