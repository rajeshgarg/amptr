package com.nyt.mpt.Tier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.AudienceTargetType;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierSectionAssoc;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.ITierService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for  Tier Service
 * @author rakesh.tewari
 * 
 */
public class TierTest extends AbstractTest {

	@Autowired
	@Qualifier("tierService")
	private ITierService tierService;

	@Autowired
	@Qualifier("salesTargetService")
	private ISalesTargetService salesTargetService;
	
	@Autowired
	@Qualifier("targetingService")
	private ITargetingService targetingService;

	private String searchField;
	private String searchString;
	private Long tierIdHavePremiumID = null;
	private Tier tierDB = null;
	List<Tier> tierList;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		tierList = tierService.getFilteredTierList(null, null, null);
		if (tierList != null && !tierList.isEmpty()) {
			searchField = "tierId";
			searchString = String.valueOf(tierList.get(0).getTierId());
			for (Tier tier : tierList) {
				if (tierIdHavePremiumID == null && tier.getTierPremiumLst() != null && !tier.getTierPremiumLst().isEmpty()) {
					tierIdHavePremiumID = tier.getTierId();
				}
				if (!tier.getTierId().equals(tierIdHavePremiumID)) {
					tierDB = tier;
				}
			}
		}
	}

	/**
	 * Test for Get SalesTarget list based on SalesTarget Type id
	 */
	@Test
	public void testActiveSalesTargetBySTTypeId() {
		List<Long> salesTargetTypeIdLst = new ArrayList<Long>();
		salesTargetTypeIdLst.add(17l);
		List<SalesTarget> salesTargetLst = salesTargetService.getActiveSalesTargetBySTTypeId(salesTargetTypeIdLst);
		Assert.assertNotNull(salesTargetLst);
	}

	/**
	 * Test SaveTier that will Save tier information
	 */
	@Test
	public void testSaveTier() {
		if (tierDB != null) {
			boolean duplicateTierLevel = tierService.isDuplicateTierLevel(tierDB.getTierLevel(), tierDB.getTierId());
			boolean duplicateTierName = tierService.isDuplicateTierName(tierDB.getTierName(), tierDB.getTierId());

			if (duplicateTierLevel && duplicateTierName) {
				Assert.assertTrue(false);
			} else {
				tierService.deleteTier(tierDB.getTierId());
				tierDB = tierService.saveTier(tierDB);
				if (tierDB != null) {
					Assert.assertNotNull(tierDB.getTierId());
				}
			}
		}
	}

	/**
	 * Test for FilteredTierList() that Return list of tier based on filter,
	 * pagination, sorting criteria
	 */
	@Test
	public void testFilteredTierList() {
		if (tierList != null && !tierList.isEmpty()) {
			FilterCriteria filterCriteria = new FilterCriteria(searchField, searchString, ConstantStrings.EMPTY_STRING);
			List<Tier> tierDBList = tierService.getFilteredTierList(filterCriteria, null, null);
			Assert.assertTrue(tierDBList.size() >= 0);
		}
	}

	/**
	 * test for FilterredTierCount that Return count of tier
	 */
	@Test
	public void testFilteredTierCount() {
		int count;
		if (tierList != null && !tierList.isEmpty()) {
			count = tierService.getFilteredTierCount(null);
			Assert.assertTrue(count >= 0);
		}
		FilterCriteria filterCriteria = new FilterCriteria(searchField, searchString, ConstantStrings.EMPTY_STRING);
		count = tierService.getFilteredTierCount(filterCriteria);
		Assert.assertTrue(count >= 0);
	}

	/**
	 * Test getTierById that Returns the tier object with the given tier Id
	 */
	@Test
	public void testGetTierById() {
		if (tierDB != null) {
			Tier tier = tierService.getTierById(tierDB.getTierId());
			Assert.assertEquals(tierDB, tier);
		}
	}

	/**
	 * Test isDuplicateTierName that Checks duplicate active tier Name Returns
	 * true if Name is duplicate
	 */
	@Test
	public void testIsDuplicateTierName() {
		if (tierDB != null) {
			boolean value = tierService.isDuplicateTierName(tierDB.getTierName(), tierDB.getTierId());
			Assert.assertTrue(!value);
		}
	}

	/**
	 * Test isDupLicateTierLevel that Checks duplicate active tier level Returns
	 * true if Level is duplicate
	 */
	@Test
	public void testIsDuplicateTierLevel() {
		if (tierDB != null) {
			boolean value = tierService.isDuplicateTierLevel(tierDB.getTierLevel(), tierDB.getTierId());
			Assert.assertTrue(!value);
		}
	}

	/**
	 * test getFilteredtierPremiumCount that Returns count of premium
	 */
	@Test
	public void testGetFilteredTierPremiumCount() {
		if (tierDB != null) {
			Assert.assertTrue(tierService.getFilteredTierPremiumCount(tierDB.getTierId(), null) > 0);
		}
	}

	/**
	 * Test getFilteredTierPremiumList that Returns list of premium based on
	 * filter, pagination, sorting criteria
	 */
	@Test
	public void testGetFilteredTierPremiumList() {
		if (tierDB != null) {
			Assert.assertTrue(tierService.getFilteredTierPremiumList(tierDB.getTierId(), null, null, null).size() > 0);
		}
	}

	/**
	 * Test saveTierPremium that will Save tier information
	 */
	@Test
	public void testSaveTierPremium() {
		Map<Long, String> targetTypesMap = targetingService.getTargetTypeCriteria();
		if (targetTypesMap != null && !targetTypesMap.isEmpty()) {

			long targetTypeId = targetTypesMap.keySet().iterator().next();
			targetingService.getTargetTypeElement(targetTypeId);

			if (tierDB != null && (tierDB.getTierPremiumLst() != null && !tierDB.getTierPremiumLst().isEmpty())) {
				Tier newTier = tierService.saveTierPremium(tierDB.getTierPremiumLst().get(0), tierDB.getTierId());
				Assert.assertTrue(newTier.getTierPremiumLst().size() > 0);
			}
		}
	}
	
	/**
	 * Test saveTierPremium that will Save tier information
	 */
	@Test
	public void testAddTierPremium() {
		if (tierDB != null) {
			TierPremium premium = new TierPremium();
			premium.setPremium(500.23);
			premium.setTierObj(tierDB);
			final AudienceTargetType targetType = new AudienceTargetType();
			Map<Long, String> targetTypesMap = targetingService.getTargetTypeCriteria();
			if (targetTypesMap != null && !targetTypesMap.isEmpty()) {
				for (Iterator<Long> iterator = targetTypesMap.keySet().iterator(); iterator.hasNext();) {
					Long targetTypeId = (Long) iterator.next();
					Map<Long, String> targetElementsMap = targetingService.getTargetTypeElement(targetTypeId);
					if(!targetElementsMap.isEmpty()){
						targetType.setSosAudienceTargetTypeId(targetTypeId);
						Long elementId = (Long) targetElementsMap.keySet().iterator().next();
						premium.setTargetType(targetType);
						premium.setTarTypeElementId(elementId.toString());
						break;
					}
				}
			}
			tierService.saveTierPremium(premium, tierDB.getTierId());
			getTransactionManager().getSessionFactory().getCurrentSession().flush();
			Tier tierObj  = tierService.getTierById(tierDB.getTierId());
			Assert.assertTrue(tierObj.getTierPremiumLst().size() > 0);
		}
	}

	/**
	 * Test isDuplicatePremiumElement that will Check the Duplicacy of Target
	 * Type elements in a premium Object
	 */
	@Test
	public void testIsDuplicatePremiumElement() {
		TierPremium tierPremiumDb = null;
		List<TierPremium> tierPrList = tierService.getFilteredTierPremiumList(tierIdHavePremiumID, null, null, null);
		if (tierPrList != null && !tierPrList.isEmpty()) {
			for (TierPremium tierPremium : tierPrList) {
				tierPremiumDb = tierPremium;
				break;
			}
		}
		if (tierPremiumDb != null) {
			StringBuilder duplicateElementsStr = new StringBuilder();
			Assert.assertTrue(!(tierService.isDuplicatePremiumElement(tierPremiumDb, duplicateElementsStr)));
		}
	}

	/**
	 * Test createClonePremiumForTier that To clone the premiums from the source
	 * tier to the target tier
	 */
	@Test
	public void testCreateClonePremiumForTier() {
		if (tierDB != null) {
			tierIdHavePremiumID = tierDB.getTierId();
			tierService.createClonePremiumForTier(tierIdHavePremiumID, tierDB.getTierId());
			Assert.assertTrue(true);
		}
	}

	/**
	 * Test getPremiumById that Returns the premium object with the given
	 * premium Id
	 */
	@Test
	public void testGetPremiumById() {
		TierPremium tierPremiumDb = null;
		for (Tier tier : tierList) {
			if (tier.getTierPremiumLst() != null && !tier.getTierPremiumLst().isEmpty()) {
				for (TierPremium tierPremium : tier.getTierPremiumLst()) {
					tierPremiumDb = tierPremium;
					break;
				}
			}
		}
		if (tierPremiumDb != null) {
			Assert.assertEquals(tierPremiumDb, tierService.getPremiumById(tierPremiumDb.getId()));
		}
	}

	/**
	 * Test getTierSectionAssocList that will Return list of Tier based on
	 * section IDs
	 */
	@Test
	public void testGetTierSectionAssocList() {
		Set<Long> sectionIDSet = new HashSet<Long>();
		if (tierDB != null) {
			List<TierSectionAssoc> sectionAssocList = tierDB.getTierSectionAssocLst();
			for (TierSectionAssoc tierSectionAssoc : sectionAssocList) {
				sectionIDSet.add(tierSectionAssoc.getSectionId());
			}
			List<Tier> tierList = tierService.getTierSectionAssocList(sectionIDSet);
			Assert.assertEquals(sectionAssocList, tierList.get(0).getTierSectionAssocLst());
		}
	}

	/**
	 * Test getPremiumByTierId that Returns the list of premiums of a tier with
	 * the given tierId
	 */
	@Test
	public void testGetPremiumListByTierId() {
		if (tierDB != null) {
			List<TierPremium> premiumList = tierService.getPremiumListByTierId(tierDB.getTierId());
			if (premiumList != null && !premiumList.isEmpty()) {
				Assert.assertTrue(premiumList.size() >= 0);
			}
		}
	}

	/**
	 * Test DeleteTierPremium that will delete premium of a tier.
	 */
	@Test
	public void testDeleteTierPremium() {
		TierPremium tierPremiumDb = null;
		for (Tier tier : tierList) {
			if (tier.getTierPremiumLst() != null && !tier.getTierPremiumLst().isEmpty()) {
				for (TierPremium tierPremium : tier.getTierPremiumLst()) {
					tierPremiumDb = tierPremium;
					break;
				}
			}
		}
		if (tierPremiumDb != null) {
			tierService.deleteTierPremium(tierPremiumDb.getId());
			Assert.assertTrue(true);
		}
	}
	
	/**
	 * Test deleteTier that will delete tier
	 */
	@Test
	public void testdeleteTier() {
		tierService.deleteTier(tierDB.getTierId());
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Tier tierObj  = tierService.getTierById(tierDB.getTierId());
		Assert.assertNull(tierObj);
	}

}
