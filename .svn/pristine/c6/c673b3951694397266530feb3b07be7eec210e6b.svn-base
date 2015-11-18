package com.nyt.mpt.mediaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.impl.ProposalService;
import com.nyt.mpt.service.impl.TemplateService;
import com.nyt.mpt.template.TemplateVO;
import com.nyt.mpt.util.ConstantStrings;

/**
 * JUnit test for Template Service
 * @author surendra.singh
 */

public class TemplateTest extends AbstractTest {

	@Autowired
	@Qualifier("proposalService")
	private ProposalService proposalService;

	@Autowired
	@Qualifier("templateService")
	private TemplateService templateService;

	@Autowired
	@Qualifier("attributeService")
	private IAttributeService attributeService;

	private Proposal proposal = null;

	private List<LineItem> lineItemLst = null;
	private ProposalOption proposalOption = null;
	private ProposalVersion proposalVersion = null;
	private User userobj = null;
	private List<TemplateMetaData> templateList = null;
	private List<Attribute> attributes = null;

	@Before
	public void setup() {
		super.setAuthenticationInfo();
		if(attributes == null || lineItemLst == null){
			setInitialParams();
		}
		
	}
	
	private void setInitialParams(){
		templateList = templateService.getActiveMediaPlanTemplates();
		List<Proposal> proposalLst = proposalService.getProposalListhavingLineItems(null, null, null);
		if (proposalLst.size() > 0) {
			this.proposal = proposalLst.get(0);
			userobj = proposal.getAssignedUser();
			SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userobj, "Test"));
			for (Proposal proposal : proposalLst) {
				proposalOption = proposal.getDefaultOption();
				proposalVersion = proposalOption.getLatestVersion();
				lineItemLst = new ArrayList<LineItem>(proposalVersion.getProposalLineItemSet());
				if (lineItemLst != null && !lineItemLst.isEmpty()) {
					return;
				}
			}
		}
		attributes = attributeService.getAttributeList(true, AttributeType.CREATIVE);
	}

	/**
	 * Test for generateMediaTemplateObject that return media template based on templateId
	 */
	@Test
	public void testGetMediaPlanTemplates() {
		templateList = templateService.getActiveMediaPlanTemplates();
		if (templateList != null && templateList.size() > 0) {
			final TemplateMetaData templateMetaData = templateService.getActiveMediaPlanTemplateById(templateList.get(0).getTemplateId());
			Assert.assertNotNull(templateMetaData);
		}

	}

	/**
	 * Test for generateMediaTemplateObject that return media plan template based on template name
	 */
	@Test
	public void testGetMediaPlanTemplateByName() {
		final TemplateMetaData mediaPlanTemplate = templateService.getMediaPlanTemplateByName("NYT_TIMES.xlsx");
		if (mediaPlanTemplate != null) {
			Assert.assertEquals(mediaPlanTemplate.getTemplateName(), "NYT_TIMES.xlsx");
		}
	}

	/**
	 * Test for generateMediaTemplateObject that return MediaPlanTemplateVO, which contains proposal data and line items header list
	 */
	@Test
	public void testGenerateMediaTemplateObject() {
		if (proposalOption != null) {
			final TemplateVO templateVO = templateService.generateMediaTemplateObject(proposalOption.getId(), proposalOption.getLatestVersion(), templateList.get(0).getTemplateId());
			Assert.assertNotNull(templateVO);
		}
	}

	/**
	 * Test for generateCreativeSpecObject that use to create creative specification object
	 */
	@Test
	public void testGenerateCreativeSpecObject() {
		final TemplateMetaData mediaPlanTemplate = templateService.getMediaPlanTemplateByName("CreativeSpecTemplate.xlsx");
		if (proposalOption != null) {
			final TemplateVO templateVO = templateService.generateCreativeSpecObject(proposalOption.getId(), proposalOption.getLatestVersion(), mediaPlanTemplate.getTemplateId());
			Assert.assertNotNull(templateVO);
		}
	}

	/**
	 * Test for populateLineItemAttributesList that populate MediaPlanTemplateVO with line items data
	 */
	@Test
	public void testPopulateLineItemAttributesList() {
		final TemplateMetaData mediaPlanTemplate = templateService.getMediaPlanTemplateByName("CreativeSpecTemplate.xlsx");
		if (proposalOption != null && lineItemLst != null && lineItemLst.size() > 0) {
			TemplateVO templateVO = templateService.generateCreativeSpecObject(proposalOption.getId(), proposalOption.getLatestVersion(), mediaPlanTemplate.getTemplateId());
			templateVO = templateService.populateLineItemAttributesList(templateVO, lineItemLst.get(0), 0);
			Assert.assertNotNull(templateVO);
		}
	}

	/**
	 * Test for getProposalHeadList that return list of proposal head
	 */
	@Test
	public void testGetProposalHeadList() {
		final List<ProposalHead> proposalHeadLst = templateService.getProposalHeadList();
		Assert.assertTrue(proposalHeadLst != null);
	}

	/**
	 * Test for getProposalHeadAttributes that return list of proposal head attributes
	 */
	@Test
	public void testGetProposalHeadAttributes() {
		final List<ProposalHeadAttributes> proposalHeadAttributesLst = templateService.getProposalHeadAttributes();
		Assert.assertTrue(proposalHeadAttributesLst != null);
	}

	/**
	 * Test for getProposalHeadAttributes that return list of proposal head attributes for given token set
	 */
	@Test
	public void testGetProposalHeadAttributesByTokenSet() {
		if (proposalOption != null) {
			final TemplateVO templateVO = templateService.generateMediaTemplateObject(proposalOption.getId(), proposalOption.getLatestVersion(), templateList.get(0).getTemplateId());
			if (templateVO != null && templateVO.getTokenSet() != null && templateVO.getTokenSet().size() > 0) {
				List<ProposalHeadAttributes> proposalHeadAttributes = templateService.getProposalHeadAttributes(templateVO.getTokenSet());
				Assert.assertNotNull(proposalHeadAttributes != null);
			}
		}
	}

	/**
	 * Test for getMediaPlanTemplateById that return template meta data for given template id
	 */
	@Test
	public void testGetMediaPlanTemplateById() {
		final TemplateMetaData mediaPlanTemplate = templateService.getMediaPlanTemplateByName("NYT_TIMES.xlsx");
		if (mediaPlanTemplate != null) {
			final TemplateMetaData templateMetaData = templateService.getMediaPlanTemplateById(mediaPlanTemplate.getTemplateId());
			Assert.assertNotNull(templateMetaData);
		}
	}

	/**
	 * Test for getMediaPlanTemplatesCount that return count of templates
	 */
	@Test
	public void testGetMediaPlanTemplatesWithCriteria() {
		final List<TemplateMetaData> templateMetaDataLst = templateService.getMediaPlanTemplates(null, null, null);
		Assert.assertTrue(templateMetaDataLst != null);
		final int count = templateService.getMediaPlanTemplatesCount(null);
		Assert.assertTrue(templateMetaDataLst.size() == count);
	}

	/**
	 * Test for getHeadAttributesByParameter that return list of proposal head attributes
	 */
	@Test
	public void testGetHeadAttributesByParameter() {
		String attributeName = ConstantStrings.EMPTY_STRING;
		String headName = ConstantStrings.EMPTY_STRING;
		if (attributes != null && !attributes.isEmpty()) {
			attributeName = attributes.get(0).getAttributeName();
			if (StringUtils.equalsIgnoreCase("CREATIVE", attributes.get(0).getAttributeType())) {
				headName = "CREATIVE_ATTRIBUTE";
			} else {
				headName = "PRODUCT_SALESTARGET";
			}
			final List<ProposalHeadAttributes> headAttributesLst = templateService.getHeadAttributesByParameter(attributeName, headName);
			Assert.assertNotNull(headAttributesLst);
		}
	}

	/**
	 * Test for saveHeadAttributes that return proposal head attributes
	 */
	@Test
	public void testSaveHeadAttributes() {
		if (attributes != null && !attributes.isEmpty()) {
			ProposalHeadAttributes proposalHeadAttributes = getHeadAttributes(attributes.get(0));
			if (proposalHeadAttributes != null) {
				proposalHeadAttributes = templateService.saveHeadAttributes(proposalHeadAttributes);
				Assert.assertNotNull(proposalHeadAttributes);
			}
		}
	}

	/**
	 * Test for getProposalHeadAttributesMap that return map of head attributes
	 */
	@Test
	public void testGetProposalHeadAttributesMap() {
		if (attributes != null && !attributes.isEmpty()) {
			Map<Long, String> allHeadAttributesMap = templateService.getProposalHeadDisplayName();
			if (allHeadAttributesMap != null && !allHeadAttributesMap.isEmpty()) {
				final Map<Long, String> headAttributesMap = templateService.getProposalHeadAttributesMap(allHeadAttributesMap.keySet().iterator().next());
				Assert.assertTrue(headAttributesMap != null);
			}
		}
	}

	/**
	 * Test for deleteCustomTemplate that delete template
	 */
	@Test
	public void testDeleteCustomTemplate() {
		if (templateList != null && !templateList.isEmpty()) {
			final TemplateMetaData customTemplate = templateList.get(0);
			templateService.deleteCustomTemplate(customTemplate);
		}

	}

	/**
	 * Return proposal head attributes
	 * @param attribute
	 * @return
	 */
	private ProposalHeadAttributes getHeadAttributes(final Attribute attribute) {
		String headName = ConstantStrings.EMPTY_STRING;
		ProposalHeadAttributes headAttributes = null;
		if (StringUtils.equalsIgnoreCase("CREATIVE", attribute.getAttributeType())) {
			headName = "CREATIVE_ATTRIBUTE";
		} else {
			headName = "PRODUCT_SALESTARGET";
		}
		final List<ProposalHeadAttributes> headAttrLst = templateService.getHeadAttributesByParameter(attribute.getAttributeName(), headName);
		if (headAttrLst != null && !headAttrLst.isEmpty()) {
			headAttributes = headAttrLst.get(0);
		}
		return headAttributes;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}
}
