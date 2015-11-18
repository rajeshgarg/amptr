/**
 * 
 */
package com.nyt.mpt.yieldex;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.service.IYieldexService;
import com.nyt.mpt.util.YieldexHelper;
import com.nyt.mpt.util.exception.YieldexAvailsException;

/**
 * @author Gurditta.Garg
 *
 */
public class YieldExTest extends AbstractTest{
	
	@Autowired
	private YieldexHelper yieldexHelper;
	@Autowired
	private IYieldexService yieldexService;
	
	@Test
	public void testGetAllTargetTypeElement() {
		Assert.notNull(yieldexHelper.getAllTargetTypeElement());
	}
	
	@Test (expected = YieldexAvailsException.class)
	public void testGetInventoryDetail() {
		Assert.notNull(yieldexService.getInventoryDetail("www.xyz.com"));
	}	
}
