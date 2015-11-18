package com.nyt.mpt.spec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.CreativeType;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for Creative Service
 * @author amandeep.singh
 * 
 */
 
public class CreativeTest extends AbstractTest {

	@Autowired
	@Qualifier("creativeService")
	private ICreativeService creativeService;

	@Autowired
	@Qualifier("attributeService")
	private IAttributeService attributeService;

	private long creativeId = 0;
	private String creativeName;
	private List<Creative> creativeList = null;
	List<Attribute> attributesList = null;

	@Before
	public void setAuthintication() {
		super.setAuthenticationInfo();
		creativeList = creativeService.getCreativeList(true);
		if (creativeList != null && creativeList.size() > 0) {
			Assert.assertTrue(creativeList.get(0) != null);
			creativeId = creativeList.get(0).getCreativeId();
			creativeName = creativeList.get(0).getName();
		}
		attributesList = attributeService.getAttributeList(true, AttributeType.CREATIVE);
	}

	/**
	 * test for createCreative that Adds a new creative to the database
	 */
	@Test
	public void testCreateCreative() {
		Creative creative = getDummyCreative();
		long creativeId = creativeService.createCreative(creative);
		Assert.assertTrue(creativeId > 0);
	}

	/**
	 * test for updateCreative that Update a existing creative.
	 */
	@Test
	public void testUpdateCreative() {
		if (creativeId > 0) {
			Creative creative = creativeService.getCreative(creativeId);
			creative.setName("FIRST CREATIVE");
			creative.setDescription("FIRST CREATIVE DESCRIPTION");
			long tempId = creativeService.updateCreative(creative, true);
			Assert.assertTrue(tempId != 0);
			Assert.assertTrue(tempId == creativeId);
			Creative creativeDB = creativeService.getCreative(creativeId);
			Assert.assertEquals(creativeDB.getName(), "FIRST CREATIVE");
		}
	}

	/**
	 * test for getCreativeList that Returns list of creative based on status
	 * (active/inactive)
	 */
	@Test
	public void testGetCreativeList() {
		List<Creative> creativeList = creativeService.getCreativeList(true);
		if (creativeList != null && !creativeList.isEmpty()) {
			Assert.assertTrue(creativeList.size() > 0);
			Assert.assertTrue(creativeList.get(0) != null);
		}
	}

	/**
	 * test for getCreativeAttribute that Return the list of attribute for given
	 * creative
	 */
	@Test
	public void testGetCreativeAttribute() {
		if (creativeId > 0) {
			Set<CreativeAttributeValue> creativeAttributeSet = creativeService.getCreativeAttribute(creativeId);
			if (creativeAttributeSet.size() > 0) {
				Assert.assertTrue(creativeAttributeSet.iterator().next().getAttributeValue() != null);
			}else{
				creativeService.addCreativeAttribute(creativeId,attributesList.get(0),"123");
				getTransactionManager().getSessionFactory().getCurrentSession().flush();
				Creative creativeDB = creativeService.getCreative(creativeId);
				Assert.assertTrue(creativeDB.getAttributeValues().iterator().next().getAttributeValue() != null);				
			}
		}
	}

	/**
	 * test for getFilteredCreativeList that Return list of creative based on
	 * search parameters
	 */
	@Test
	public void testGetFilteredCreativeList() {
		FilterCriteria criteria = new FilterCriteria("typeStr", "html5", SearchOption.CONTAIN.toString());
		List<Creative> creativeList = creativeService.getFilteredCreativeList(criteria, new PaginationCriteria(1, 10), new SortingCriteria("creativeId", CustomDbOrder.ASCENDING));
		if (creativeList != null && !creativeList.isEmpty()) {
			Assert.assertTrue(creativeList.size() <= 10);
		}
		creativeList = creativeService.getFilteredCreativeList(null, null, null);
		if (creativeList != null && !creativeList.isEmpty()) {
			int count = creativeService.getFilteredCreativeListCount(null);
			Assert.assertTrue(creativeList.size() == count);
		}
	}

	/**
	 * test for isDuplicateCreativeName that check duplicate creative name
	 */
	@Test
	public void testIsDuplicateCreativeName() {
		if (creativeId > 0) {
			Assert.assertFalse(creativeService.isDuplicateCreativeName(creativeName, creativeId));
		}
		Assert.assertTrue(creativeService.isDuplicateCreativeName(creativeName, 0));
	}

	/**
	 * test for addCreativeAttribute that Add a new attribute to the creative
	 * with given attribute value.
	 */
	@Test
	public void testAddCreativeAttribute() {
		if (attributesList != null && attributesList.size() > 0) {
			Attribute adAttribute = attributesList.get(0);
			String attributeValue = adAttribute.getAttributeName();
			long creativeID = creativeService.addCreativeAttribute(creativeId, adAttribute, attributeValue);
			getTransactionManager().getSessionFactory().getCurrentSession().flush();
			Creative creative = creativeService.getCreative(creativeID);
			Assert.assertTrue(creative.getAttributeValues().size() > 0);
		}
	}
	
	/**
	 * test for isDuplicateCreativeAttributeAssocExist that Check for creative
	 * attribute association
	 */
	@Test
	public void testIsDuplicateCreativeAttributeAssocExist() {
		Attribute attrbute = attributesList.get(0);
		creativeService.addCreativeAttribute(creativeId,attrbute,"123");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		boolean duplicateCreaAttriAssExi = creativeService.isDuplicateCreativeAttributeAssocExist(creativeId, attrbute.getAttributeId());
		Assert.assertTrue(duplicateCreaAttriAssExi);
	}
	
	/**
	 * test for updateCreativeAttribute that Update attribute value for given
	 * creative
	 */
	@Test
	public void testUpdateCreativeAttribute() {
		Attribute attrbute = attributesList.get(0);
		creativeService.addCreativeAttribute(creativeId,attrbute,"123");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		creativeService.updateCreativeAttribute(creativeId, attrbute, "456");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Creative creativeDB = creativeService.getCreative(creativeId);
		for(CreativeAttributeValue creativeAttr : creativeDB.getAttributeValues()){
			if(creativeAttr.getAttribute().getAttributeId() == attrbute.getAttributeId()){
				Assert.assertTrue(creativeAttr.getAttributeValue().equals("456"));
				break;
			}
		}
	}

	/**
	 * test for getFilteredCreativeAttributeList that get filtered creative
	 * attribute list
	 */
	@Test
	public void testGetFilteredCreativeAttributeList() {
		Attribute attrbute = attributesList.get(0);
		creativeService.addCreativeAttribute(creativeId,attrbute,"123");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		List<CreativeAttributeValue> filCreativeAttributeList = creativeService.getFilteredCreativeAttributeList(creativeId, null, null, null);
		if (filCreativeAttributeList != null && filCreativeAttributeList.size() > 0) {
			Assert.assertTrue(filCreativeAttributeList.size() >= 0);
		}
	}

	/**
	 * test for getFilteredCreativeAttributeListCount that return creative
	 * attribute count for filter criteria
	 */
	@Test
	public void testGetFilteredCreativeAttributeListCount() {
		Attribute attrbute = attributesList.get(0);
		creativeService.addCreativeAttribute(creativeId,attrbute,"123");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		if (creativeId > 0) {
			int count = creativeService.getFilteredCreativeAttributeListCount(creativeId, null);
			Assert.assertTrue(count >= 0);
		}
	}

	/**
	 * test for getCreativeAttrAssocListByAttributeId that Find creative name
	 * value and ID associated with attributeId
	 */
	@Test
	public void testGetCreativeAttrAssocListByAttributeId() {
		Attribute attrbute = attributesList.get(0);
		creativeService.addCreativeAttribute(creativeId,attrbute,"123");
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		long attributeId = attrbute.getAttributeId();
		List<CreativeAttributeValue> creativeAttrAssocList = creativeService.getCreativeAttrAssocListByAttributeId(attributeId);
		if (creativeAttrAssocList != null && !creativeAttrAssocList.isEmpty()) {
			Assert.assertTrue(creativeAttrAssocList.size() >= 0);
		}
	}

	/**
	 * test for getFilteredAttributeListCount that Return total number of
	 * attribute in system as per filter criteria
	 */
	@Test
	public void testGetFilteredCreativeListCount() {
		FilterCriteria criteria = new FilterCriteria("attributeTypeStr", "creative", SearchOption.CONTAIN.toString());
		Assert.assertTrue(attributeService.getFilteredAttributeListCount(criteria) >= 0);
	}

	/**
	 * test for deleteCreative that Delete creative from database
	 */
	@Test
	public void testDeleteCreative() {
		Creative creative = getDummyCreative();
		long creativeId = creativeService.createCreative(creative);
		getTransactionManager().getSessionFactory().getCurrentSession().flush();
		Creative creativeDB = creativeService.getCreative(creativeId);
		creativeService.deleteCreative(creativeDB);
		creativeDB = creativeService.getCreative(creativeId);
		Assert.assertFalse(creativeDB.isActive());
	}


	/**
	 * test for deleteCreativeAttribute that Delete attribute for given creative
	 */
	@Test
	public void testDeleteCreativeAttribute() {
		Set<CreativeAttributeValue> attributeValuesSet = new HashSet<CreativeAttributeValue>();
		long creativeId = 0;
		if (creativeList != null && creativeList.size() > 0) {
			for (int i = 0; i < creativeList.size(); i++) {
				if (creativeList.get(i).getAttributeValues() != null && creativeList.get(i).getAttributeValues().size() > 0) {
					attributeValuesSet = creativeList.get(i).getAttributeValues();
					creativeId = creativeList.get(i).getCreativeId();
				}
			}
			if (attributeValuesSet != null && attributeValuesSet.size() > 0 && creativeId > 0) {
				Attribute attribute = attributeValuesSet.iterator().next().getAttribute();
				int delCreativeAttribute = creativeService.deleteCreativeAttribute(creativeId, attribute);
				Assert.assertTrue(delCreativeAttribute == creativeId);
			}
		}
	}
	
	/**
	 * Create a dummy creative object
	 * @return Creative
	 */
	private Creative getDummyCreative() {
		Creative creative = new Creative();
		creative.setName("Test Creative");
		creative.setType(CreativeType.RICH_MEDIA.name());
		creative.setWidth(100);
		creative.setHeight(200);
		creative.setDescription("Test Creative");
		return creative;
	}
}
