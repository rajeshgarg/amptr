package com.nyt.mpt.emailSchedule;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.service.impl.EmailScheduleService;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.EmailScheduleFrequency;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.Weekdays;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * The Class EmailScheduleTest.
 */
public class EmailScheduleTest extends AbstractTest {
	
	/** The email schedule service. */
	@Autowired
	@Qualifier("emailScheduleService")
	private EmailScheduleService emailScheduleService;
	
	private EmailSchedule emailSchedule;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		super.setAuthenticationInfo();
		List<EmailSchedule> emailScheduleLst = emailScheduleService.getFilteredEmailDetailList(null,new PaginationCriteria(1, 1), null);
		emailSchedule = emailScheduleLst.get(0);
	}
	
	/**
	 * Test save email schedule.
	 */
	@Test
	public void testSaveEmailSchedule() {
		EmailSchedule entity = new EmailSchedule();
		entity.setProdcutName("Product-AMPT");
		entity.setProductId(100l);
		entity.setSalesTargetId(100l);
		entity.setSalesTargetName("Sales-AMPT");
		entity.setActive(true);
		emailScheduleService.saveEmailSchedule(entity);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		//adding filter for the data saved
		List<FilterCriteria> filter = new ArrayList<FilterCriteria>();
		filter.add(new FilterCriteria("productId","100",SearchOption.EQUAL.toString()));
		filter.add(new FilterCriteria("salesTargetId","100",SearchOption.EQUAL.toString()));		
		int size = emailScheduleService.getFilteredEmailDetailList(filter,new PaginationCriteria(1, 1), null).size();
		Assert.assertTrue(size==1);
	}
	
	/**
	 * Test get filtered emai detail list.
	 */
	@Test
	public void testGetFilteredEmaiDetailList(){
		SortingCriteria sortingCriteria = new SortingCriteria("emailScheduleId", "desc");
		List<FilterCriteria> filter = new ArrayList<FilterCriteria>();
		
		FilterCriteria filterCriteriaProduct = new FilterCriteria();
		filterCriteriaProduct.setSearchField("prodcutName");
		filterCriteriaProduct.setSearchString("AMPT");
		
		FilterCriteria filterCriteriaSales = new FilterCriteria();
		filterCriteriaSales.setSearchField("salesTargetName");
		filterCriteriaSales.setSearchString("AMPT");
		
		FilterCriteria filterCriteriaProductId = new FilterCriteria();
		filterCriteriaProductId.setSearchField("productId");
		filterCriteriaProductId.setSearchString("100");
		
		FilterCriteria filterCriteriaforSalesId = new FilterCriteria();
		filterCriteriaforSalesId.setSearchField("salesTargetId");
		filterCriteriaforSalesId.setSearchString("100");
		
		FilterCriteria filterCriteriaforStartDate = new FilterCriteria();
		filterCriteriaforStartDate.setSearchField("startDate");
		filterCriteriaforStartDate.setSearchString("2014-01-22");
		
		filter.add(filterCriteriaforSalesId);
		filter.add(filterCriteriaProductId);
		filter.add(filterCriteriaSales);
		filter.add(filterCriteriaProduct);
		//filter.add(filterCriteriaforStartDate);
		
		List<EmailSchedule> emailSchedule =  emailScheduleService.getFilteredEmailDetailList(filter,new PaginationCriteria(1, 4), sortingCriteria);
		Assert.assertNotNull(emailSchedule.get(0).getEmailSchedules().size());
	}
	
	
	/**
	 * Test delete email schedule.
	 */
	@Test
	public void testDeleteEmailSchedule() {	
		boolean flag = true;
		EmailScheduleDetails emailScheduleDetails = emailSchedule.getEmailSchedules().get(0);
		emailSchedule.getEmailSchedules().remove(emailScheduleDetails);
		emailScheduleService.deleteEmailSchedule(emailScheduleDetails.getEmailScheduleDetailsId());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		List<FilterCriteria> filter = new ArrayList<FilterCriteria>();
		filter.add(new FilterCriteria("productId",emailSchedule.getProductId().toString(),SearchOption.EQUAL.toString()));
		filter.add(new FilterCriteria("salesTargetId",emailSchedule.getSalesTargetId().toString(),SearchOption.EQUAL.toString()));
		List<EmailSchedule> emailScheduleLst =  emailScheduleService.getFilteredEmailDetailList(filter,null, null);
		for (EmailSchedule emailScheduleDB : emailScheduleLst) {
			if(emailScheduleDB.getEmailScheduleId() == emailSchedule.getEmailScheduleId()){
				for(EmailScheduleDetails emailScheduleDetailsNew : emailSchedule.getEmailSchedules()){
					if(emailScheduleDetailsNew.getEmailScheduleDetailsId() == emailScheduleDetails.getEmailScheduleDetailsId()){
						flag = false;
						break;
					}
				}
				break;
			}
		}
		Assert.assertTrue(flag);
	}
	
	/**
	 * Test filtered email detail list size.
	 */
	@Test
	public void testFilteredEmailDetailListSize() {
		List<FilterCriteria> filter = new ArrayList<FilterCriteria>();
		filter.add(new FilterCriteria("productId",emailSchedule.getProductId().toString(),SearchOption.EQUAL.toString()));
		filter.add(new FilterCriteria("salesTargetId",emailSchedule.getSalesTargetId().toString(),SearchOption.EQUAL.toString()));
		int emailDetailsSize = emailScheduleService.getFilteredEmailDetailListSize(filter);
		Assert.assertTrue(emailDetailsSize >= 1);
	}
	
	@Test
	public void testDuplicateEmailSchedule() throws Exception {
		EmailSchedule entity = new EmailSchedule();
		entity.setProdcutName(emailSchedule.getProdcutName());
		entity.setProductId(emailSchedule.getProductId());
		entity.setSalesTargetId(emailSchedule.getSalesTargetId());
		entity.setSalesTargetName(emailSchedule.getSalesTargetName());
		entity.setActive(true);
		entity.setEmailScheduleId(emailSchedule.getEmailScheduleId());
		List<EmailScheduleDetails> emailSchedules = new ArrayList<EmailScheduleDetails>();
		
		for(EmailScheduleDetails emailScheduleDetails : emailSchedule.getEmailSchedules()){
			EmailScheduleDetails emailScheduleNew = new EmailScheduleDetails();
			emailScheduleNew.setStartDate(emailScheduleDetails.getStartDate());
			emailScheduleNew.setEndDate(emailScheduleDetails.getEndDate());
			emailScheduleNew.setEmailScheduleDetailsId(0);
			emailSchedules.add(emailScheduleNew);
			break;
		}
		
		entity.setEmailSchedules(emailSchedules);
		boolean flag = emailScheduleService.findDuplicateDataExists(entity);
		Assert.assertTrue(flag);
		
	}
	
	@Test
	public void testGetEmailScheduleByDate(){		
		EmailSchedule emailScheduleDB = emailScheduleService.getEmailScheduleByDate(emailSchedule.getProductId(), emailSchedule.getSalesTargetId(), emailSchedule.getEmailSchedules().get(0).getStartDate());		
		Assert.assertTrue(emailScheduleDB.getEmailScheduleId() == emailSchedule.getEmailScheduleId());
	}
	
	@Test
	public void testAddScheduleToemailScheduleDetails(){
		EmailSchedule emailScheduleNew = new EmailSchedule();
		emailScheduleNew.setEmailScheduleId(emailSchedule.getEmailScheduleId());
		List <EmailScheduleDetails> scheduleDetailLst = new ArrayList<EmailScheduleDetails>(1);
		EmailScheduleDetails emailScheduleDetails = new EmailScheduleDetails();
		emailScheduleDetails.setEmailSchedule(emailScheduleNew);
		emailScheduleDetails.setStartDate(DateUtil.getCurrentDate());
		emailScheduleDetails.setEndDate(DateUtil.getPriorDateFromCurrentDate(-30));
		emailScheduleDetails.setForever(false);
		emailScheduleDetails.setWeekdays(Weekdays.findByCode(6).name());
		emailScheduleDetails.setFrequency(EmailScheduleFrequency.findByName(EmailScheduleFrequency.WEEKLY.name()).name());
		scheduleDetailLst.add(emailScheduleDetails);
		emailScheduleNew.setEmailSchedules(scheduleDetailLst);
		EmailSchedule emailScheduleDB = emailScheduleService.updateEmailSchedule(emailScheduleNew);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertTrue(emailScheduleDB.getEmailSchedules().get(0).getEmailScheduleDetailsId() > 0);
	}
	
	@Test
	public void testUpdateScheduleToemailScheduleDetails(){
		long scheduleDetailsId = 0;
		for (EmailScheduleDetails emailScheduleDetails : emailSchedule.getEmailSchedules()){
			scheduleDetailsId = emailScheduleDetails.getEmailScheduleDetailsId();
			emailScheduleDetails.setFrequency(EmailScheduleFrequency.findByName(EmailScheduleFrequency.WEEKLY.name()).name());
			break;
		}
		EmailSchedule emailScheduleDB = emailScheduleService.updateEmailSchedule(emailSchedule);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		for (EmailScheduleDetails emailScheduleDetails : emailScheduleDB.getEmailSchedules()){
			if(emailScheduleDetails.getEmailScheduleDetailsId() == scheduleDetailsId){
				Assert.assertTrue(EmailScheduleFrequency.findByName(EmailScheduleFrequency.WEEKLY.name()).name().equals(emailScheduleDetails.getFrequency()));
				break;
			}
		}
	}
	
}
