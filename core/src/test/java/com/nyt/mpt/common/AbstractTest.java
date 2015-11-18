package com.nyt.mpt.common;

import javax.sql.DataSource;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nyt.mpt.domain.User;

/**
 * Base test class for all the Spring enabled Test cases
 * @author surendra.singh
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional (propagation = Propagation.REQUIRES_NEW)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration (transactionManager = "amptTransactionManager", defaultRollback = true)
public abstract class AbstractTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private HibernateTransactionManager amptTransactionManager;
	
	@Autowired
	public void setDataSource(@Qualifier("amptBasicDataSource") final DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
	@Autowired
	public void setTransactionManager(@Qualifier("amptTransactionManager") final HibernateTransactionManager amptTransactionManager) {
		this.amptTransactionManager = amptTransactionManager;
	}
	
	public HibernateTransactionManager getTransactionManager() {
		return amptTransactionManager;
	}
	
	/**
	 * Set AuthenticationInfo for test case.
	 */
	protected void setAuthenticationInfo() {
		User userobj = new User();
		userobj.setLoginName("Test");
		userobj.setUserId(1);
		SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
	}
}
