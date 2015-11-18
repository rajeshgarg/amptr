/**
 * 
 */
package com.nyt.mpt.packge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.service.IPackageService;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.filter.RangeFilterCriteria;

/**
 * JUnit test for Package Service
 * @author surendra.singh
 */
public class PackageTest extends AbstractTest {

	@Autowired
	@Qualifier("packageService")
	private IPackageService packageService;

	private List<Package> packageList = null;
	private Package packageDb = null;
	private LineItem lineItemDB = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		FilterCriteria filterCriteria = null;
		packageList = packageService.getFilteredPackageList(filterCriteria, null, new SortingCriteria("packageName", CustomDbOrder.ASCENDING));
		if (packageList != null && !packageList.isEmpty()) {
			for (Package packageObj : packageList) {
				packageDb = packageObj;
				Set<LineItem> packagelineItemSet = packageDb.getPackagelineItemSet();
				for (LineItem lineItem : packagelineItemSet) {
					if (lineItemDB == null) {
						lineItemDB = lineItem;
					} else {
						if (lineItemDB.getLineItemSequence() < lineItem.getLineItemSequence()) {
							lineItemDB = lineItem;
						}
					}
				}
				if (lineItemDB != null) {
					break;
				}
			}
		}
	}

	/**
	 * test for getFilteredPackageList that Return list of all active package.
	 */
	@Test
	public void testPackageService() {
		final FilterCriteria filterCriteria = null;
		List<Package> packageList = packageService.getFilteredPackageList(filterCriteria, null, null);
		if (packageList != null && packageList.size() > 0) {
			final Package packageDB = packageService.getPackageById(packageList.get(0).getId());
			Assert.assertNotNull(packageDB);
		}
		if (packageList != null && packageList.size() > 0) {
			final FilterCriteria filterCriteri = new FilterCriteria();
			filterCriteri.setSearchField("packageName");
			filterCriteri.setSearchOper("eq");
			filterCriteri.setSearchString(packageList.get(0).getName());
			packageList = packageService.getFilteredPackageList(filterCriteri, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(packageList.size() < 11);
			filterCriteri.setSearchField("validTo");
			packageList = packageService.getFilteredPackageList(filterCriteri, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(packageList.size() < 11);
			filterCriteri.setSearchField("budget");
			filterCriteri.setSearchString("10");
			filterCriteri.setSearchOper("ge");
			packageList = packageService.getFilteredPackageList(filterCriteri, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(packageList.size() < 11);
			filterCriteri.setSearchField("expired");
			packageList = packageService.getFilteredPackageList(filterCriteri, new PaginationCriteria(1, 10), null);
			Assert.assertTrue(packageList.size() < 11);
		}
	}

	/**
	 * test for savePackage that Saves a new package if package is not already
	 * exist else update package
	 */
	@Test
	public void testSavePackage() {
		if (packageDb != null) {
			final long packageID = packageService.savePackage(packageDb);
			packageDb.setComments("test");
			Assert.assertTrue(packageID >= 0);
			final Package updatedPackage = packageService.getPackageById(packageDb.getId());
			Assert.assertEquals(updatedPackage.getComments(), "test");
		}
	}

	/**
	 * test for saveLineItems Saving Line Item with lineItem.
	 */
	@Test
	public void testSaveLineItems() {
		if (lineItemDB != null) {
			long savLinItem = packageService.saveLineItems(lineItemDB);
			Assert.assertEquals(lineItemDB.getLineItemID(), (Long) savLinItem);
			final LineItem lineItemNew = getDummyLineItem(lineItemDB);
			lineItemNew.setPackageObj(lineItemDB.getPackageObj());
			lineItemNew.setLineItemID(0L);
			savLinItem = packageService.saveLineItems(lineItemNew);
			Assert.assertTrue(savLinItem > 0);
		}
	}

	/**
	 * test for getFilteredPackageLineItems that Return list
	 * of all package lineItem in system as per package Id, filter, paging and sorting criteria.
	 */
	@Test
	public void testGetFilteredPackageLineItems() {
		final FilterCriteria filterCriteria = null;
		final List<Package> packageList = packageService.getFilteredPackageList(filterCriteria, null, null);
		final List<Package> sortedPackageList = packageService.getFilteredPackageList(filterCriteria, null, new SortingCriteria("packageId", "desc"));
		if (packageList != null && packageList.size() > 0) {
			long packageId = packageList.get(0).getId();
			List<LineItem> packageLineItemsLst = packageService.getFilteredPackageLineItems(packageId, null, null, new SortingCriteria("lineItemSequence", CustomDbOrder.ASCENDING));
			Assert.assertTrue(packageLineItemsLst.size() >= 0);
		}
		if (packageList != null && packageList.size() > 0 && sortedPackageList != null) {
			Assert.assertTrue(packageList.size() >= sortedPackageList.size());
		}
	}

	/**
	 * test for getFilteredPackageLineItemsCount that Returns list of all active package.
	 */
	@Test
	public void testGetFilteredPackageLineItemsCount() {
		FilterCriteria filterCriteria = null;
		List<Package> packageList = packageService.getFilteredPackageList(filterCriteria, null, null);
		if (packageList != null && packageList.size() > 0) {
			long packageId = packageList.get(0).getId();
			int count = packageService.getFilteredPackageLineItemsCount(packageId, null);
			Assert.assertTrue(count >= 0);
		}
	}

	/**
	 * test for getFilteredPackageListCountHavingLineItems that Returns count of
	 * all package having lineItem in system as per filter criteria.
	 */
	@Test
	public void testGetFilteredPackageListCountHavingLineItems() {
		Integer count = packageService.getFilteredPackageListCountHavingLineItems(null);
		Assert.assertTrue(count >= 0);
	}

	/**
	 * test for getFilteredPackageListHavingLineItems that Returns count of all
	 * package having lineItem in system as per filter criteria.
	 */
	@Test
	public void testGetFilteredPackageListHavingLineItems() {
		List<Package> packageListHavingLineItemsLst = packageService.getFilteredPackageListHavingLineItems(null, null, null);
		int count = packageService.getFilteredPackageListCountHavingLineItems(null);
		if (packageListHavingLineItemsLst != null) {
			Assert.assertEquals(count, packageListHavingLineItemsLst.size());
		}
		List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria rangefiltercriteria = new RangeFilterCriteria();
		if (packageListHavingLineItemsLst != null && packageListHavingLineItemsLst.size() > 0) {
			rangefiltercriteria.setSearchField("packageName");
			rangefiltercriteria.setSearchString(packageListHavingLineItemsLst.get(0).getName());
			rangefiltercriteria.setSearchOper("cn");
			filterCriteriaLst.add(0, rangefiltercriteria);
			count = packageService.getFilteredPackageListCountHavingLineItems(filterCriteriaLst);
			List<Package> packag = packageService.getFilteredPackageList(filterCriteriaLst, new PaginationCriteria(1, 10), null);
			if (packag != null && packag.size() > 0 && count > 0) {
				Assert.assertTrue(count >= packag.size());
			}

			rangefiltercriteria.setSearchField("packageOwner");
			rangefiltercriteria.setSearchString(String.valueOf(packageListHavingLineItemsLst.get(0).getOwner()));
			rangefiltercriteria.setSearchOper("cn");
			filterCriteriaLst.add(0, rangefiltercriteria);
			count = packageService.getFilteredPackageListCountHavingLineItems(filterCriteriaLst);
			packag = packageService.getFilteredPackageList(filterCriteriaLst, new PaginationCriteria(1, 10), null);
			if (packag != null && packag.size() > 0 && count > 0) {
				Assert.assertTrue(count >= packag.size());
			}

			rangefiltercriteria.setSearchField("budget");
			rangefiltercriteria.setSearchOper("btw");
			rangefiltercriteria.setSearchString("100");
			filterCriteriaLst.add(0, rangefiltercriteria);
			count = packageService.getFilteredPackageListCountHavingLineItems(filterCriteriaLst);
			packag = packageService.getFilteredPackageList(filterCriteriaLst, new PaginationCriteria(1, 10), null);
			if (packag != null && packag.size() > 0 && count > 0) {
				Assert.assertTrue(count >= packag.size());
			}

			rangefiltercriteria.setSearchField("ValidTo");
			rangefiltercriteria.setSearchOper("btw");
			rangefiltercriteria.setSearchString(packageListHavingLineItemsLst.get(0).getValidTo().toString());
			filterCriteriaLst.add(0, rangefiltercriteria);
			count = packageService.getFilteredPackageListCountHavingLineItems(filterCriteriaLst);
			packag = packageService.getFilteredPackageList(filterCriteriaLst, new PaginationCriteria(1, 10), null);
			if (packag != null && packag.size() > 0 && count > 0) {
				Assert.assertTrue(count >= packag.size());
			}

		}

	}

	/**
	 * test for getFilteredPackageList that Return list of all active package.
	 */
	@Test
	public void testGetFilteredPackageList() {
		FilterCriteria filterCriteria = null;
		List<Package> packagePagingLst = packageService.getFilteredPackageList(filterCriteria, new PaginationCriteria(1, 10), null);
		List<Package> packageLst = packageService.getFilteredPackageList(filterCriteria, null, null);
		if (packagePagingLst != null && packageLst != null && packageLst.size() > 0 && packagePagingLst.size() > 0) {
			Assert.assertTrue(packageLst.size() >= packagePagingLst.size());
		}
		int count = packageService.getFilteredPackageListCount(filterCriteria);
		if (count > 0) {
			Assert.assertTrue(count >= packageLst.size());
		}
	}

	/**
	 * test for isDuplicatePackageName that Checks whether Package with same
	 * name Exist.
	 */
	@Test
	public void testGetPackageByName() {
		FilterCriteria filterCriteria = null;
		List<Package> packageList = packageService.getFilteredPackageList(filterCriteria, null, null);
		if (packageList != null && packageList.size() > 0) {
			String packageName = packageList.get(0).getName();
			long packageID = packageList.get(0).getId();
			boolean packageObj = packageService.isDuplicatePackageName(packageName, packageID);
			if (packageObj) {
				Assert.assertTrue(packageObj);
			}
		}

	}

	/**
	 * test for getLineItemID
	 */
	@Test
	public void testGetLineItemById() {
		if (lineItemDB != null) {
			LineItem lineItem = packageService.getLineItemById(lineItemDB.getLineItemID());
			if (lineItem != null) {
				Assert.assertEquals(lineItem.getLineItemID(), lineItemDB.getLineItemID());
			}
		}
	}

	/**
	 * test for createPackageCloneByID that will Clone the package
	 */
	@Test
	public void testClonePackageByID() {
		FilterCriteria filterCriteria = null;
		List<Package> packageList = packageService.getFilteredPackageList(filterCriteria, null, null);
		if (packageList != null && packageList.size() > 0) {
			Package adPackage = packageList.get(0);
			long packageId = packageService.createPackageCloneByID(adPackage.getId());
			Assert.assertTrue(packageId > 0);
		}
	}

	/**
	 * @return Package
	 */
	@SuppressWarnings("unused")
	private Package getDummyPackage() {
		Package adPackage = new Package();
		try {
			adPackage.setName("Package Name");

			adPackage.setBreakable(true);
			adPackage.setBudget(500);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
			adPackage.setValidFrom(dateFormat.parse("12/12/2013"));
			adPackage.setValidTo(dateFormat.parse("12/12/2021"));
			adPackage.setComments("test");
		} catch (Exception e) {

		}
		return adPackage;
	}

	/**
	 * test for getNextSequenceNoForLineItem that Method will return the next
	 * sequence no for the Package Line Item
	 */
	@Test
	public void testGetNextSequenceNoForLineItem() {
		if (packageDb != null && lineItemDB != null) {
			int sqno = packageService.getNextSequenceNoForLineItem(packageDb.getId());
			Assert.assertTrue(sqno > lineItemDB.getLineItemSequence());
		}
	}

	/**
	 * test for udatedPackage that Update all line item avails
	 */
	@Test
	public void testUdatedPackage() {
		if (packageDb != null) {
			packageDb.setBudget(100);
			long packageID = packageService.updatedPackage(packageDb);
			getTransactionManager().getSessionFactory().getCurrentSession().flush();
			Package updatedPackage = packageService.getPackageById(packageID);
			Assert.assertTrue(updatedPackage.getBudget()==100);
		}
	}

	/**
	 * test for getFilteredPackageListCount that Returns a count of package in
	 * system as per filter criteria.
	 */
	@Test
	public void testGetFilteredPackageListCount() {
		final FilterCriteria filterCriteria = null;
		final int count = packageService.getFilteredPackageListCount(filterCriteria);
		final List<Package> packageLst = packageService.getFilteredPackageList(filterCriteria, null, null);
		Assert.assertTrue(count == packageLst.size());
	}

	/**
	 * test for updateAllLineItemsPrice that Update base price of given line
	 * item
	 */
	@Test
	public void testUpdateAllLineItemsPrice() {
		if (lineItemDB != null) {
			Long[] lineItemArr = new Long[10];
			lineItemArr[0] = lineItemDB.getLineItemID();
			packageService.updateAllLineItemsPrice(lineItemArr);
			Assert.assertTrue(true);
		}
	}

	/**
	 * test for deleteLineItems that will Delete Line Item based on lineItemId.
	 */
	@Test
	public void testDeleteLineItems() {
		if (lineItemDB != null) {
			packageService.deleteLineItems(String.valueOf(lineItemDB.getLineItemID()));
			Assert.assertTrue(true);

			final LineItem packageLineItems = packageService.getLineItemById(lineItemDB.getLineItemID());
			Assert.assertEquals(packageLineItems.isActive(), false);
		}
	}
	
	/**
	 * test for deletePackage that will Delete package from database.
	 */
	@Test
	public void testDeletePackage() {
		if (packageDb != null) {
			long delPackageID = packageService.deletePackage(packageDb);
			Assert.assertTrue(delPackageID == packageDb.getId());
			Package delPackage = packageService.getPackageById(delPackageID);
			Assert.assertEquals(delPackage.isActive(), false);
		}
	}
	
	/**
	 * test for deletePackage that will Delete package from database.
	 */
	@Test
	(expected = ObjectNotFoundException.class)
	public void testDeleteNonExistingPackagePackage() {
		Package pkg =new Package();
		pkg.setId(-56456);
		packageService.deletePackage(pkg);
			
	}
	
	private LineItem getDummyLineItem(LineItem lineItem) {
		LineItem returnLineItem = new LineItem();

		returnLineItem.setComments(lineItem.getComments());
		returnLineItem.setFlight(lineItem.getFlight());
		returnLineItem.setSosProductClass(lineItem.getSosProductClass());
		returnLineItem.setSosProductId(lineItem.getSosProductId());
		returnLineItem.setProductName(lineItem.getProductName());
		returnLineItem.setTargetingString(lineItem.getTargetingString());
		// Clear the line items sales target association and put new list

		returnLineItem.setLineItemSalesTargetAssocs(lineItem.getLineItemSalesTargetAssocs());
		returnLineItem.setImpressionTotal(lineItem.getImpressionTotal());
		returnLineItem.setPriceType(lineItem.getPriceType());
		returnLineItem.setRate(lineItem.getRate());
		returnLineItem.setTotalInvestment(lineItem.getTotalInvestment());
		returnLineItem.setAvailsPopulatedDate(lineItem.getAvailsPopulatedDate());
		returnLineItem.setPlacementName(lineItem.getPlacementName());
		returnLineItem.setAvails(lineItem.getAvails());
		returnLineItem.setSov(lineItem.getSov());
		returnLineItem.setStartDate(lineItem.getStartDate());
		returnLineItem.setEndDate(lineItem.getEndDate());
		returnLineItem.setTotalPossibleImpressions(lineItem.getTotalPossibleImpressions());
		returnLineItem.setSpecType(lineItem.getSpecType());
		returnLineItem.setLineItemSequence(lineItem.getLineItemSequence());
		returnLineItem.setRateCardPrice(lineItem.getRateCardPrice());
		returnLineItem.setPriceCalSummary(lineItem.getPriceCalSummary());
		returnLineItem.setSor(lineItem.getSor());
		returnLineItem.setProductType(lineItem.getProductType());
		return returnLineItem;

	}

}
