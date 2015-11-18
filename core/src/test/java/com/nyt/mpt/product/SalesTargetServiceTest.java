/**
 *
 */
package com.nyt.mpt.product;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.SalesTargetAmpt;
import com.nyt.mpt.service.ISalesTargetService;

/**
 * @author surendra.singh
 *
 */
public class SalesTargetServiceTest extends AbstractTest {
	
	@Autowired
	@Qualifier("salesTargetService")
	private ISalesTargetService salesTargetService;

	@Before
	public  void setAuthentication(){
		super.setAuthenticationInfo();
	}
	
	@Test
	public void testSaveSalesTarget() {
		salesTargetService.saveSalesTargets(getSalesTargetList());
	}
	
	@Test
	public void testGetSalestarget(){
		Assert.assertFalse(salesTargetService.getSalesTarget().isEmpty());
	}

	private List<SalesTargetAmpt> getSalesTargetList() {
		final List<SalesTargetAmpt> amptSalesTargets = new ArrayList<SalesTargetAmpt>();
		final SalesTargetAmpt amptSalesTarget = new SalesTargetAmpt();
		amptSalesTarget.setSalesTargetId(1L);
		amptSalesTarget.setSalesTargetName("w4wer");
		amptSalesTarget.setSalesTargeDisplayName("sfddsf");
		amptSalesTarget.setCapacity(38484848L);
		amptSalesTarget.setWeight(23.23D);

		final SalesTargetAmpt amptSalesTarget1 = new SalesTargetAmpt();
		amptSalesTarget1.setSalesTargetId(2L);
		amptSalesTarget1.setSalesTargetName("efsdfdsfsd");
		amptSalesTarget1.setSalesTargeDisplayName("sdsfdsfdsfsdffddsf");
		amptSalesTarget1.setCapacity(3848454544848L);
		amptSalesTarget1.setWeight(0.23D);
		
		amptSalesTargets.add(amptSalesTarget);
		amptSalesTargets.add(amptSalesTarget1);
		return amptSalesTargets;
	}
}
