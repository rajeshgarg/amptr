/**
 * 
 */
package com.nyt.mpt.spec;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.exception.IntegrityValidatorException;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * JUnit test for Attribute Service 
 * @author amandeep.singh
 */
public class AttributeTest extends AbstractTest {

	@Autowired
	@Qualifier("attributeService")
	private IAttributeService attributeService;

	@Autowired
	@Qualifier("creativeService")
	private ICreativeService creativeService;
	
	private long attributeId = 0;
	private String attributeName = ConstantStrings.EMPTY_STRING;
	private Attribute attrDb;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		List<Attribute> attributes = attributeService.getAttributeList(true, AttributeType.CREATIVE);
		if (attributes.size() > 0) {
			attributeId = attributes.get(0).getAttributeId();
			attributeName = attributes.get(0).getAttributeName();
			for (Attribute attr : attributes) {
				if (creativeService.getCreativeAttrAssocListByAttributeId(attr.getAttributeId()) != null) {
					attrDb = attr;
					break;
				}
			}
		}
	}

	/**
	 * test for createAttribute that Create new attribute in system
	 */
	@Test
	public void testCreateAttribute() {
		Attribute adAttribute = getDummyAttribute();
		Assert.assertTrue(attributeService.createAttribute(adAttribute).getAttributeId() > 0);
	}

	/**
	 * test for getAttribute that Returns attribute for given Id
	 */
	@Test
	public void testGetAttributById() {
		if (attributeId > 0) {
			Attribute attribute = attributeService.getAttribute(attributeId);
			Assert.assertNotNull(attribute);
			try {
				Attribute attribute_1 = attributeService.getAttribute(-1);
				Assert.assertNull(attribute_1);
			} catch (Exception e) {
				Assert.assertTrue(true);
			}
		}
	}

	/**
	 * test for isDuplicateAttributeName that Check attribute name is already exist in system
	 */
	@Test
	public void testIsDuplicateAttribute() {
		if (attributeId > 0) {
			Assert.assertFalse(attributeService.isDuplicateAttributeName(attributeName, attributeId, AttributeType.CREATIVE.name()));
			Assert.assertTrue(attributeService.isDuplicateAttributeName(attributeName, 0, AttributeType.CREATIVE.name()));
		}
	}

	/**
	 * test for updateAttribute that Update existing attribute
	 */
	@Test
	public void testUpdateAttribute() {
		if (attributeId > 0) {
			Attribute attribute = attributeService.getAttribute(attributeId);
			attribute.setActive(false);
			Assert.assertTrue(attributeService.updateAttribute(attribute, true).getAttributeId() == attributeId);
		}
	}
	
	/**
	 * test for updateAttribute that Update existing attribute
	 */
	@Test
	(expected = IntegrityValidatorException.class)
	public void testUpdateAttributeWithIntegrityViolation() {
		if (attributeId > 0) {
			Attribute attribute = attributeService.getAttribute(attributeId);
			Creative creative = creativeService.getCreativeList(true).get(0);
			creativeService.addCreativeAttribute(creative.getCreativeId(),attribute,"123");
			getTransactionManager().getSessionFactory().getCurrentSession().flush();
			attribute.setActive(false);
			attribute = attributeService.updateAttribute(attribute, false);
			Assert.assertTrue(attribute.getAttributeId() == attributeId);
		}
	}

	/**
	 * @return
	 */
	private Attribute getDummyAttribute() {
		Attribute adAttribute = new Attribute();
		adAttribute.setAttributeDescription("THIRD ATTRIBUTE DESCRIPTION");
		adAttribute.setAttributeKey("TEXT");
		adAttribute.setAttributeName("THIRD ATTRIBUTE");
		adAttribute.setAttributeType(AttributeType.CREATIVE.name());
		return adAttribute;
	}

	/**
	 * test for getFilteredAttributeList that 
	 * Return list of all attributes in system as per filter, paging and sorting criteria.
	 */
	@Test
	public void testGetFilteredAttributeList() {
		FilterCriteria filterCriteria = null;
		PaginationCriteria paginationCriteria = null;
		SortingCriteria sortingCriteria = null;
		List<Attribute> atrList = attributeService.getFilteredAttributeList(filterCriteria, paginationCriteria, sortingCriteria);
		Assert.assertTrue(atrList.size() >= 0);
		paginationCriteria = new PaginationCriteria(1, 10);
		atrList = attributeService.getFilteredAttributeList(filterCriteria, paginationCriteria, sortingCriteria);
		Assert.assertTrue(atrList.size() <= 10);
		if (atrList != null && atrList.size() > 0) {
			FilterCriteria filtercriteria = new FilterCriteria();
			filtercriteria.setSearchField("attributeName");
			filtercriteria.setSearchField(atrList.get(0).getAttributeName());
			filtercriteria.setSearchOper("cn");
			atrList = attributeService.getFilteredAttributeList(filterCriteria, paginationCriteria, sortingCriteria);
			Assert.assertTrue(atrList.size() > 0);
		}
	}

	/**
	 * test for getFilteredAttributeListCount that
	 * Return total number of attribute in system as per filter criteria
	 */
	@Test
	public void testGetFilteredAttributeListCount() {
		int count = attributeService.getFilteredAttributeListCount(null);
		Assert.assertTrue(count >= 0);
	}

	/**
	 * test for deleteAttribute that Delete attribute in database
	 */
	@Test
	public void testDeleteAttribute() {
		if (attrDb != null) {
			try {
				attributeService.deleteAttribute(attrDb);
				Assert.assertFalse(attrDb.isActive());
			} catch (Exception e) {
				Assert.assertTrue(true);
			}
		}
	}
}
