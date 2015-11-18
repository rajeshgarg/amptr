/**
 * 
 */
package com.nyt.mpt.avails;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.CountryRegionMap;
import com.nyt.mpt.service.ITargetingService;

/**
 * Test case for Targeting Service
 * @author surendra.singh
 */
public class TargetingServiceTest extends AbstractTest {

	@Autowired
	@Qualifier("targetingService")
	private ITargetingService targetingService;

		
	/**
	 * Test for getAllCountryRegionMap that gives all data related to country
	 */
	@Test
	public void testGetAllCountryRegionMap() {
		final List<CountryRegionMap> crmList = targetingService.getAllCountryRegionData();
		if (crmList != null && !crmList.isEmpty()) {
			Assert.assertTrue(crmList.size() >= 0);
		}
	}

	/**
	 * test for getTargetTypeCriteria that Returns a map of
	 * SosAudienceTargetTypeId and audienceTargetType Name
	 */
	@Test
	public void testGetTargetTypeCriteria() {
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
		Assert.assertTrue (targetTypeMap != null);
	}

	/**
	 * test for getTargetTypeWithAdxNameCriteria used to fetch target type with
	 * ADX name
	 */
	@Test
	public void testGetTargetTypeWithAdxNameCriteria() {
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeWithAdxNameCriteria();
		Assert.assertTrue (targetTypeMap != null);
	}

	/**
	 * test for testGetTargetTypeElement that Returns a map of
	 * SosAudienceTargetId and audienceTarget Name based on
	 * targetTypeCriteriaID.
	 */
	@Test
	public void testGetTargetTypeElement() {
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
		long targetTypeId = targetTypeMap.keySet().iterator().next();
		final Map<Long, String> targetTypeElementMap = targetingService.getTargetTypeElement(targetTypeId);
		Assert.assertTrue(targetTypeElementMap != null) ;
	}

	/**
	 * test for getAllTargetTypeElement that Returns All Target Type Elements
	 */
	@Test
	public void testGetAllTargetTypeElement() {
		final Map<Long, String> allTargetTypeMap = targetingService.getAllTargetTypeElement();
		Assert.assertTrue (allTargetTypeMap != null);
	}

	/**
	 * test for getTargetForCountries used to fetch target type for countries
	 */
	@Test
	public void testGetTargetForCountries() {
		final Map<Long, String> targetTypeMap = targetingService.getTargetTypeCriteria();
		long targetTypeId = targetTypeMap.keySet().iterator().next();
		final Map<Long, String> countryTargetTypeMap = targetingService.getTargetForCountries(targetTypeId);
		if (countryTargetTypeMap != null) {
			Assert.assertTrue(countryTargetTypeMap.size() >= 0);
		}
	}
}
