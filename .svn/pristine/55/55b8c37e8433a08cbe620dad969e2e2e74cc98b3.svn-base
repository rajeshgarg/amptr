/**
 * 
 */
package com.nyt.mpt.addedValue;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.AddedValueBudget;
import com.nyt.mpt.service.IAddedValueService;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * @author amandeep.singh
 *
 */
public class AddedValueServiceTest extends AbstractTest {
	
	@Autowired
	@Qualifier("addedValueService")
	private IAddedValueService addedValueService;
	
	@Before
	public void setup(){
		super.setAuthenticationInfo();
	}
	
	@Test
	public void testGetFilteredPlanBudgetList(){
		List<AddedValueBudget> addedValueLst = addedValueService.getFilteredPlanBudgetList(null,new PaginationCriteria(1, 5),null);
		Assert.assertNotNull(addedValueLst);
		FilterCriteria criteria = new FilterCriteria("totalInvestment",String.valueOf(addedValueLst.get(0).getTotalInvestment()), SearchOption.CONTAIN.toString());
		addedValueLst = addedValueService.getFilteredPlanBudgetList(criteria,new PaginationCriteria(1, 5),null);
		Assert.assertFalse(addedValueLst.isEmpty());
		criteria = new FilterCriteria("avPercentage",String.valueOf(addedValueLst.get(0).getAvPercentage()), SearchOption.CONTAIN.toString());
		addedValueLst = addedValueService.getFilteredPlanBudgetList(criteria,new PaginationCriteria(1, 5),null);
		Assert.assertFalse(addedValueLst.isEmpty());
	}
	
	@Test
	public void testGetFilteredPlanBudgetListCount(){
		Assert.assertTrue(addedValueService.getFilteredPlanBudgetCount(null) > 0);
	}
	
	@Test
	public void testCreatePlanBudget(){
		AddedValueBudget addedValueBudget = addedValueService.createPlanBudget(getDummyObject());
		Assert.assertTrue(addedValueBudget.getId() > 0);
	}
	
	@Test
	public void testUpdatePlanBudget(){
		AddedValueBudget addedValueBudget = addedValueService.createPlanBudget(getDummyObject());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		addedValueBudget.setTotalInvestment(9999.99);
		addedValueService.updatePlanBudget(addedValueBudget, true);
		Assert.assertTrue(addedValueBudget.getTotalInvestment() == 9999.99);
	}
	
	@Test
	public void testDeletePlanBudget(){
		AddedValueBudget addedValueBudget = addedValueService.createPlanBudget(getDummyObject());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		addedValueService.deleteAddedValuePlanBudget(addedValueBudget.getId());
		Assert.assertTrue(true);
	}
	
	@Test
	public void testIsDuplicateInvestment(){
		AddedValueBudget addedValueBudget = addedValueService.createPlanBudget(getDummyObject());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Assert.assertTrue(addedValueService.isDuplicateInvestment(addedValueBudget.getTotalInvestment(), 0L));
		Assert.assertFalse(addedValueService.isDuplicateInvestment(addedValueBudget.getTotalInvestment(), addedValueBudget.getId()));
	}
	
	@Test
	public void testGetAvPercentValidForInvestment(){
		List<AddedValueBudget> addedValueLst = addedValueService.getFilteredPlanBudgetList(null,new PaginationCriteria(1, 2),null);
		for (AddedValueBudget addedValueBudget : addedValueLst) {
			Assert.assertFalse(addedValueService.getAvPercentValidForInvestment(addedValueBudget.getTotalInvestment()+5d, addedValueBudget.getAvPercentage()-0.5d, 0));
			break;
		}
	}
	
	private AddedValueBudget getDummyObject(){
		AddedValueBudget addedValueBudget = new AddedValueBudget();
		addedValueBudget.setAvPercentage(66.50);
		addedValueBudget.setId(0);
		addedValueBudget.setTotalInvestment(5000.58);
		addedValueBudget.setNotes("Testing");
		return addedValueBudget;
	}
	
}
