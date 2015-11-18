/**
 *
 */
package com.nyt.mpt.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.nyt.mpt.dao.ITemplateDAO;
import com.nyt.mpt.domain.Advertiser;
import com.nyt.mpt.domain.Agency;
import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemSalesTargetAssoc;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.ProposalVersion;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.TemplateJson;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateMetaDataAttributes;
import com.nyt.mpt.domain.TemplateSheetMetaData;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ISOSService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.template.ReferenceDataMap;
import com.nyt.mpt.template.TemplateAttributeVO;
import com.nyt.mpt.template.TemplateSheetVO;
import com.nyt.mpt.template.TemplateVO;
import com.nyt.mpt.template.TemplateValueFormateType;
import com.nyt.mpt.util.AttributeIdComparator;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.PaginationCriteria;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.enums.CreativeType;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.filter.FilterCriteria;

/**
 * This <code>TemplateService</code> includes those methods which are required
 * to configure a custom <tt>Template</tt> and to export a <tt>Template</tt>.
 * 
 * @author Manish.Kesarwani
 */
public class TemplateService implements ITemplateService {

	private static final Logger LOGGER = Logger.getLogger(TemplateService.class);

	private ITemplateDAO templateDao;

	private IProductService productService;

	private ICreativeService creativeService;

	private IProposalService proposalService;

	private ISOSService sosService;

	private IProposalSOSService proposalSOSService;

	private ITargetingService targetingService;

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getActiveMediaPlanTemplates()
	 */
	@Override
	public List<TemplateMetaData> getActiveMediaPlanTemplates() {
		return templateDao.getActiveMediaPlanTemplates();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getActiveMediaPlanTemplateById(long)
	 */
	@Override
	public TemplateMetaData getActiveMediaPlanTemplateById(final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Get mediaPlanTemplate with id: " + templateId);
		}
		return templateDao.getActiveMediaPlanTemplateById(templateId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getMediaPlanTemplateByName(java.lang.String)
	 */
	@Override
	public TemplateMetaData getMediaPlanTemplateByName(final String templateName) {
		return templateDao.getMediaPlanTemplateByName(templateName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#generateMediaTemplateObject(long, com.nyt.mpt.domain.ProposalVersion, long)
	 */
	@Override
	public TemplateVO generateMediaTemplateObject(final long optionID, final ProposalVersion proposalVersion, final long templateId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Generating mediaTemplateObject with TemplateId: " + templateId + "ProposalVersion: " + proposalVersion);
		}
		final TemplateVO templateVO = new TemplateVO();
		/*
		 * Get list of all the line item of an option based on option Id and option version.
		 */
		final List<LineItem> lineItemLst = proposalService.getProposalLineItems(optionID, proposalVersion.getProposalVersion(), null, null, null);
		LOGGER.info("Populating reference data from SOS");
		/*
		 * Populate reference Data from SOS.
		 * From the list of line item system creates map, in ReferenceDataMap, for product 
		 * and sales target from SOS. System also creates map for audience target type, in ReferenceDataMap.
		 * */
		populateReferenceDataMap(templateVO, lineItemLst);
		TemplateSheetVO templateSheetVO = null;
		List<TemplateSheetMetaData> sheetMetaDataList = null;
		LOGGER.info("Fetching media template attributes form database");
		/*Get all data of {@link TemplateMetaData} by Template Id*/
		final TemplateMetaData mediaTemplate = templateDao.getActiveMediaPlanTemplateById(templateId);
		if (mediaTemplate != null) {
			// Getting Media Template Sheet Meta Data
			sheetMetaDataList = mediaTemplate.getTemplateSheetList();
		}
		//Getting Proposal attribute map to Categorize proposal head attribute
		Map<String, String> attributeHeadMap = getProHeadAttrMapByName(ConstantStrings.PROPOSAL);
		LOGGER.info("Iterate media template sheet and populate basic attribute and line item header list");
		populateTemplateSheet(templateVO, sheetMetaDataList, templateSheetVO, attributeHeadMap, proposalVersion);
		return templateVO;
	}

	/**
	 * Populate {@link TemplateMetaDataAttributes} and {@link TemplateSheetMetaData}
	 * data in {@link TemplateSheetVO}. After populating the data in 
	 * {@link TemplateSheetVO} update templateSheetMap map of {@link TemplateVO}.
	 * 
	 * @param templateVO
	 *            {@link TemplateVO}.
	 * @param sheetMetaDataList
	 *            List of {@link TemplateSheetMetaData}.
	 * @param templateSheetVO
	 *            {@link TemplateSheetVO}.
	 * @param attributeHeadMap
	 *            A map in which attribute name of {@link ProposalHeadAttributes}
	 *            is key and lookUpHead as a value.
	 * @param proposalVersion
	 *            {@link ProposalVersion}
	 */
	private void populateTemplateSheet(TemplateVO templateVO, List<TemplateSheetMetaData> sheetMetaDataList, TemplateSheetVO templateSheetVO, Map<String, String> attributeHeadMap,
			ProposalVersion proposalVersion) {
		for (TemplateSheetMetaData templateSheetMetaData : sheetMetaDataList) {
			// Fetching media Plan attributes from media template sheet
			List<TemplateMetaDataAttributes> attributes = templateSheetMetaData.getMediaPlanAttributes();
			templateSheetVO = new TemplateSheetVO();
			templateSheetVO.setName(templateSheetMetaData.getSheetName());
			for (TemplateMetaDataAttributes mediaPlanAttributes : attributes) {
				populateTemplateAttributes(mediaPlanAttributes, templateSheetVO, attributeHeadMap, proposalVersion);
			}
			templateVO.getTemplateSheetMap().put(templateSheetVO.getName(), templateSheetVO);
		}
	}

	/**
	 * Populate proposal, option and option version data data in
	 * {@link TemplateSheetVO}.
	 * 
	 * @param mediaPlanAttributes
	 *            {@link TemplateMetaDataAttributes}.
	 * @param templateSheetVO
	 *            {@link TemplateSheetVO}
	 * @param attributeHeadMap
	 *            A map in which attribute name of
	 *            {@link ProposalHeadAttributes} is key and lookUpHead as a
	 *            value.
	 * @param proposalVersion
	 *            {@link ProposalVersion}
	 */
	private void populateTemplateAttributes(TemplateMetaDataAttributes mediaPlanAttributes, TemplateSheetVO templateSheetVO, Map<String, String> attributeHeadMap, ProposalVersion proposalVersion) {
		TemplateAttributeVO attributeVO = new TemplateAttributeVO();
		/*
		 * Populate {@link TemplateMetaDataAttributes} in {@link
		 * TemplateAttributeVO}
		 */
		attributeVO.populate(attributeVO, mediaPlanAttributes);
		if (mediaPlanAttributes.getProposalHeadAttributes() != null && ConstantStrings.PROPOSAL.equalsIgnoreCase(attributeVO.getAttributeType())) {
			if (ConstantStrings.PROPOSAL.equalsIgnoreCase(attributeHeadMap.get(mediaPlanAttributes.getProposalHeadAttributes().getAttributeName()))) {
				/*
				 * Set advertiser name, agency name, sales category name etc. in
				 * TemplateAttributeVO)
				 */
				attributeVO.setAttributeValue(getProposalAttributeValue(attributeVO, mediaPlanAttributes, proposalVersion.getProposalOption().getProposal()));
				templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
			} else if (ConstantStrings.PROPOSALVERSION.equalsIgnoreCase(attributeHeadMap.get(mediaPlanAttributes.getProposalHeadAttributes().getAttributeName()))) {
				/*
				 * Set option version data in TemplateAttributeVO based on
				 * current attribute name and these values are 'Net Impressions', 'Total Investment', 'Effective CPM' 
				 */
				attributeVO.setAttributeValue(attributeVO.populateKeyValueFromObject(mediaPlanAttributes.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSALVERSION_CLASS,
						proposalVersion));
				templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
			} else if (ConstantStrings.PROPOSAL_OPTION.equalsIgnoreCase(attributeHeadMap.get(mediaPlanAttributes.getProposalHeadAttributes().getAttributeName()))) {
				/* Set name of the option */
				attributeVO.setAttributeValue(getOptinAttributeVelue(attributeVO, mediaPlanAttributes, proposalVersion.getProposalOption()));
				templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
			}
		} else if (mediaPlanAttributes.getProposalHeadAttributes() != null && ConstantStrings.USER.equalsIgnoreCase(mediaPlanAttributes.getProposalHeadAttributes().getProposalHead().getHeadName())) {
			/*
			 * proposal owner details based on the value i.e user full name,
			 * first name, last name etc.
			 */
			attributeVO.setAttributeValue(getAssignedUserAttributeVelue(attributeVO, mediaPlanAttributes, proposalVersion.getProposalOption().getProposal().getAssignedUser()));
			templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
		} else {
			templateSheetVO.getMediaPlanLineItemsHeaderList().add(attributeVO);
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#generateCreativeSpecObject(long, com.nyt.mpt.domain.ProposalVersion, long)
	 */
	@Override
	public TemplateVO generateCreativeSpecObject(final long optionID, final ProposalVersion proposalVersion, final long templateId) {
		List<TemplateSheetMetaData> sheetMetaDataList = null;
		final TemplateVO templateVO = new TemplateVO();
		final List<LineItem> lineItemLst = proposalService.getProposalLineItems(optionID, proposalVersion.getProposalVersion(), null, null, null);

		LOGGER.info("Populating reference data from SOS");
		populateReferenceDataMap(templateVO, lineItemLst);

		LOGGER.info("Fetching media template attributes form database");
		final TemplateMetaData mediaTemplate = templateDao.getMediaPlanTemplateById(templateId);
		if (mediaTemplate != null) {
			sheetMetaDataList = mediaTemplate.getTemplateSheetList();
		}
		// Adding creative specification header
		final List<Attribute> attributeList = new ArrayList<Attribute>();
		final List<Attribute> creativeAttributeList = new ArrayList<Attribute>();
		// Populating attribute list
		populateCreativeAttrList(lineItemLst, attributeList, creativeAttributeList);
		final List<TemplateMetaDataAttributes> creativeAttrMetaData = updatedCreativeAttr(sheetMetaDataList, creativeAttributeList);
		// Updating attribute value
		final List<TemplateMetaDataAttributes> attrMetaData = updatedAttributeMetaData(sheetMetaDataList, attributeList, creativeAttributeList);
		LOGGER.info("Iterate media template sheet and populate basic attribute and line item header list");
		for (TemplateSheetMetaData templateSheetMetaData : sheetMetaDataList) {
			// Fetching media Plan attributes from media template sheet
			final List<TemplateMetaDataAttributes> attributes = templateSheetMetaData.getMediaPlanAttributes();
			// Adding attribute height2 and width2
			attributes.addAll(creativeAttrMetaData);
			// Adding other attribute
			attributes.addAll(attrMetaData);
			final TemplateSheetVO templateSheetVO = new TemplateSheetVO();
			templateSheetVO.setName(templateSheetMetaData.getSheetName());
			// Populate template sheet VO from attribute list
			populateTemplateSheetVO(attributes, templateSheetVO, proposalVersion);
			templateVO.getTemplateSheetMap().put(templateSheetVO.getName(), templateSheetVO);
		}
		return templateVO;
	}

	/**
	 * <p>
	 * Set {@link Attribute} value (i.e Width2, Height2) in @param
	 * creativeAttributeList which are associated with selected product of line
	 * item.
	 * <p>
	 * Set {@link Attribute} in @param attributeList which are associated with
	 * selected product of line item.
	 * 
	 * @param lineItemLst
	 *            List of {@link LineItem} whose {@link Attribute} data want to populate.
	 * @param attributeList
	 *            List of {@link Attribute}
	 * @param creativeAttributeList
	 *            List of {@link Attribute}
	 */
	private void populateCreativeAttrList(final List<LineItem> lineItemLst, final List<Attribute> attributeList, final List<Attribute> creativeAttributeList) {
		final Set<Attribute> uniqueHeaderList = new HashSet<Attribute>();
		boolean heightWidthFlag = false;
		for (LineItem lineItem : lineItemLst) {
			/*Get all the creative which are associated with selected product of line item.*/
			final List<ProductCreativeAssoc> prodCreativeAssocList = productService.getProductCreativesForTemplates(lineItem);
			/*Populating height2 and width2 of the creative*/
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
				if (productCreativeAssoc.getCreative().getHeight2() != null && productCreativeAssoc.getCreative().getWidth2() != null) {
					if (!heightWidthFlag) {
						final Attribute width2 = new Attribute();
						width2.setAttributeName("Width2");
						width2.setAttributeKey("width2");
						width2.setAttributeType(ConstantStrings.CREATIVE);
						creativeAttributeList.add(width2);

						final Attribute heigth2 = new Attribute();
						heigth2.setAttributeName("Height2");
						heigth2.setAttributeKey("height2");
						heigth2.setAttributeType(ConstantStrings.CREATIVE);
						creativeAttributeList.add(heigth2);
						heightWidthFlag = true;
					}
					break;
				}
			}
			/*Set those {@link Attribute} which are associated with {@link ProductCreativeAssoc}*/
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
				final Set<CreativeAttributeValue> creativeAttrValueSet = creativeService.getCreativeAttribute(productCreativeAssoc.getCreative().getCreativeId());
				for (CreativeAttributeValue creativeAttributeValue : creativeAttrValueSet) {
					if (ConstantStrings.CREATIVE.equalsIgnoreCase(creativeAttributeValue.getAttribute().getAttributeType())) {
						uniqueHeaderList.add(creativeAttributeValue.getAttribute());
					}
				}
			}
		}
		attributeList.addAll(uniqueHeaderList);
		Collections.sort(attributeList, new AttributeIdComparator());
	}

	/**
	 * <p>
	 * Returns list of {@link TemplateMetaDataAttributes}. This method populate
	 * {@link Attribute} data and set cell details.
	 * <p>
	 * Set <tt>CREATIVE ATTRIBUTE</tt> data.
	 * 
	 * @param sheetMetaDataList
	 *            List of {@link TemplateSheetMetaData}
	 * @param attributeList
	 *            List of {@link Attribute} and these {@link Attribute} are
	 *            associated with {@link CreativeAttributeValue}
	 * @param creativeAttriList
	 *            List of {@link Attribute} and these {@link Attribute} contains
	 *            value as Width2, Height2 of a {@link Creative} which are
	 *            associated with {@link ProductCreativeAssoc}.
	 * @return List of {@link TemplateSheetMetaData}.
	 */
	private List<TemplateMetaDataAttributes> updatedAttributeMetaData(final List<TemplateSheetMetaData> sheetMetaDataList, final List<Attribute> attributeList, final List<Attribute> creativeAttriList) {
		final List<TemplateMetaDataAttributes> headerAttributes = new ArrayList<TemplateMetaDataAttributes>();
		TemplateMetaDataAttributes templateMetaDataAttributes = null;
		/*From rowAttribute get row number*/
		final TemplateMetaDataAttributes rowAttribute = sheetMetaDataList.get(0).getMediaPlanAttributes().get(0);
		final int incrCount = creativeAttriList.size() + 1;
		int i = sheetMetaDataList.get(0).getMediaPlanAttributes().size() + incrCount;
		/*Set template cell data*/
		for (Attribute attribute : attributeList) {
			templateMetaDataAttributes = new TemplateMetaDataAttributes();
			templateMetaDataAttributes.setAttributeName(attribute.getAttributeName());
			ProposalHeadAttributes proposalHeadAttributes = new ProposalHeadAttributes();
			proposalHeadAttributes.setAttributeName(attribute.getAttributeKey());
			ProposalHead proposalHead = new ProposalHead();
			proposalHead.setHeadName("CREATIVE_ATTRIBUTE");
			proposalHeadAttributes.setProposalHead(proposalHead);
			templateMetaDataAttributes.setProposalHeadAttributes(proposalHeadAttributes);
			templateMetaDataAttributes.setColNum(getColName(i));
			templateMetaDataAttributes.setRowNum(rowAttribute.getRowNum());
			templateMetaDataAttributes.setFormat("TEXT");
			headerAttributes.add(templateMetaDataAttributes);
			i++;
		}
		return headerAttributes;
	}

	/**
	 * <p>
	 * Returns list of {@link TemplateMetaDataAttributes}. This method populate
	 * {@link Attribute} data and set cell details.
	 * <p>
	 * Set <tt>CREATIVE</tt> data.
	 * 
	 * @param sheetMetaDataList
	 *            List of {@link TemplateSheetMetaData}
	 * @param creativeAttrList
	 *            List of {@link Attribute} and these {@link Attribute} contains
	 *            value as Width2, Height2 of a {@link Creative} which are
	 *            associated with {@link ProductCreativeAssoc}.
	 * @return List of {@link TemplateMetaDataAttributes}.
	 */
	private List<TemplateMetaDataAttributes> updatedCreativeAttr(final List<TemplateSheetMetaData> sheetMetaDataList, final List<Attribute> creativeAttrList) {
		final List<TemplateMetaDataAttributes> headerAttributes = new ArrayList<TemplateMetaDataAttributes>();
		TemplateMetaDataAttributes templateMetaDataAttributes = null;
		final TemplateMetaDataAttributes rowAttribute = sheetMetaDataList.get(0).getMediaPlanAttributes().get(0);
		int i = sheetMetaDataList.get(0).getMediaPlanAttributes().size() + 1;
		/*Set template cell data*/
		for (Attribute attribute : creativeAttrList) {
			templateMetaDataAttributes = new TemplateMetaDataAttributes();
			templateMetaDataAttributes.setAttributeName(attribute.getAttributeName());
			ProposalHeadAttributes proposalHeadAttributes = new ProposalHeadAttributes();
			proposalHeadAttributes.setAttributeName(attribute.getAttributeKey());
			ProposalHead proposalHead = new ProposalHead();
			proposalHead.setHeadName("CREATIVE");
			proposalHeadAttributes.setProposalHead(proposalHead);
			templateMetaDataAttributes.setProposalHeadAttributes(proposalHeadAttributes);
			templateMetaDataAttributes.setColNum(getColName(i));
			templateMetaDataAttributes.setRowNum(rowAttribute.getRowNum());
			templateMetaDataAttributes.setFormat("TEXT");
			headerAttributes.add(templateMetaDataAttributes);
			i++;
		}
		return headerAttributes;
	}

	/**
	 * Populate {@link Proposal}, {@link ProposalVersion} and proposal owner
	 * data in {@link TemplateSheetVO} from {@link TemplateMetaDataAttributes}
	 * 
	 * @param attributes
	 *            List of {@link TemplateMetaDataAttributes}
	 * @param templateSheetVO
	 *            {@link TemplateSheetVO}
	 * @param proposalVersion
	 *            {@link ProposalVersion}
	 */
	public void populateTemplateSheetVO(final List<TemplateMetaDataAttributes> attributes, final TemplateSheetVO templateSheetVO, final ProposalVersion proposalVersion) {
		for (TemplateMetaDataAttributes mediaPlanAttributes : attributes) {
			final TemplateAttributeVO attributeVO = new TemplateAttributeVO();
			attributeVO.populate(attributeVO, mediaPlanAttributes);
			if (mediaPlanAttributes.getProposalHeadAttributes() != null) {
				if (ConstantStrings.PROPOSAL.equalsIgnoreCase(mediaPlanAttributes.getProposalHeadAttributes().getProposalHead().getHeadName())) {
					/*
					 * Set advertiser name, agency name, sales category name etc. in
					 * TemplateAttributeVO)
					 */
					attributeVO.setAttributeValue(getProposalAttributeValue(attributeVO, mediaPlanAttributes, proposalVersion.getProposalOption().getProposal()));
					templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);

				} else if (ConstantStrings.PROPOSALVERSION.equalsIgnoreCase(mediaPlanAttributes.getProposalHeadAttributes().getProposalHead().getHeadName())) {
					/*
					 * Set option version data in TemplateAttributeVO based on
					 * current attribute name and these values are 'Net Impressions', 'Total Investment', 'Effective CPM' 
					 */
					attributeVO.setAttributeValue(attributeVO.populateKeyValueFromObject(mediaPlanAttributes.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSALVERSION_CLASS,
							proposalVersion));
					templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
				} else if (ConstantStrings.USER.equalsIgnoreCase(mediaPlanAttributes.getProposalHeadAttributes().getProposalHead().getHeadName())) {
					/*
					 * proposal owner details based on the value i.e user full name,
					 * first name, last name etc.
					 */
					attributeVO.setAttributeValue(getAssignedUserAttributeVelue(attributeVO, mediaPlanAttributes, proposalVersion.getProposalOption().getProposal().getAssignedUser()));
					templateSheetVO.getMediaPlanProposalAttributesList().add(attributeVO);
				} else {
					templateSheetVO.getMediaPlanLineItemsHeaderList().add(attributeVO);
				}
			}
		}
	}

	/**
	 * Return proposal owner details based on {@link ProposalHeadAttributes}
	 * value.
	 * 
	 * @param attributeVO
	 *            {@link TemplateAttributeVO}
	 * @param attributes
	 *            {@link TemplateMetaDataAttributes}
	 * @param user
	 *            {@link User}
	 * @return proposal owner details based on {@link ProposalHeadAttributes}
	 *         value.
	 */
	private String getAssignedUserAttributeVelue(final TemplateAttributeVO attributeVO, final TemplateMetaDataAttributes attributes, final User user) {
		String attributeValue = ConstantStrings.EMPTY_STRING;
		if (attributes.getProposalHeadAttributes().getAttributeName().equalsIgnoreCase(ConstantStrings.NAME)) {
			attributeValue = user.getFullName();
		} else {
			/*
			 * proposal owner details based on the value i.e user full name,
			 * first name, last name etc.
			 */
			attributeValue = attributeVO.populateKeyValueFromObject(attributes.getProposalHeadAttributes().getAttributeName(), ConstantStrings.USER_CLASS, user);
		}
		return attributeValue;
	}

	/**
	 * Populate {@link SalesTarget}, {@link Product} and
	 * {@link AudienceTargetType} reference data in {@link TemplateVO}.
	 * 
	 * @param mediaTemplateVO
	 *            {@link TemplateVO}
	 * @param propLineItemLst
	 *            List of {@link LineItem}
	 */
	private void populateReferenceDataMap(final TemplateVO mediaTemplateVO, final List<LineItem> propLineItemLst) {
		final ReferenceDataMap referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemAssocList(propLineItemLst);
		referenceDataMap.setTargetType(targetingService.getTargetTypeCriteria());
		mediaTemplateVO.setReferenceDataMap(referenceDataMap);
	}

	/**
	 * Returns {@link Proposal} data (i.e advertiser name, agency name, sales
	 * category name etc.)
	 * 
	 * @param attributeVO
	 *            From {@link TemplateAttributeVO} call
	 *            populateKeyValueFromObject method.
	 * @param metaDataAttr
	 *            From {@link TemplateMetaDataAttributes} get the attribute name
	 *            whose data want to return.
	 * @param proposal
	 *            {@link Proposal}
	 * @return {@link Proposal} data (i.e advertiser name, agency name, sales
	 *         category name etc.)
	 */
	private String getProposalAttributeValue(final TemplateAttributeVO attributeVO, final TemplateMetaDataAttributes metaDataAttr, final Proposal proposal) {
		String attributeValue = ConstantStrings.EMPTY_STRING;
		if (metaDataAttr.getProposalHeadAttributes() != null) {
			/*
			 * Set advertiser name, agency name, sales category name etc. in
			 * TemplateAttributeVO)
			 */
			if (ConstantStrings.SOS_ADVERTISER_NAME.equalsIgnoreCase(metaDataAttr.getProposalHeadAttributes().getAttributeName())) {
				attributeValue = attributeVO.populateKeyValueFromObject(metaDataAttr.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSAL_CLASS, proposal);
				/*Before set the advertiser name check either it is available or not in  Proposal*/
				if ((attributeValue == null || attributeValue.length() == 0) && proposal.getSosAdvertiserId() != null) {
					final Advertiser advertiser = proposalSOSService.getAdvertiserById(proposal.getSosAdvertiserId());
					if (advertiser != null) {
						attributeValue = advertiser.getName();
					}
				}
			} else if (ConstantStrings.SOS_AGENCY_NAME.equalsIgnoreCase(metaDataAttr.getProposalHeadAttributes().getAttributeName())) {
				attributeValue = attributeVO.populateKeyValueFromObject(metaDataAttr.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSAL_CLASS, proposal);
				/*Before set the agency name check either it is available or not in  Proposal*/
				if ((attributeValue == null || attributeValue.length() == 0) && proposal.getSosAgencyId() != null) {
					final Agency agency = proposalSOSService.getAgencyById(proposal.getSosAgencyId());
					if (agency != null) {
						attributeValue = agency.getName();
					}
				}
			} else if (ConstantStrings.SOS_SALES_CATEGORY_NAME.equalsIgnoreCase(metaDataAttr.getProposalHeadAttributes().getAttributeName())) {
				if (proposal.getSosSalesCategoryId() != null) {
					/*set sales category name of proposal*/
					final Map<Long, String> salesCategories = sosService.getSalesCategoryById(proposal.getSosSalesCategoryId());
					if (salesCategories.get(proposal.getSosSalesCategoryId()) != null) {
						attributeValue = salesCategories.get(proposal.getSosSalesCategoryId());
					}
				}
			} else {
				attributeValue = attributeVO.populateKeyValueFromObject(metaDataAttr.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSAL_CLASS, proposal);
			}
		}
		return attributeValue;
	}

	/**
	 * Return name of the option from {@link ProposalOption}
	 * 
	 * @param attributeVO
	 *            From {@link TemplateAttributeVO} call
	 *            populateKeyValueFromObject method.
	 * @param metaDataAttr
	 *            From {@link TemplateMetaDataAttributes} check
	 *            {@link ProposalHeadAttributes} name.
	 * @param proposalOption
	 *            From {@link ProposalOption} get option name.
	 * @return name of the option from {@link ProposalOption}.
	 */
	private String getOptinAttributeVelue(final TemplateAttributeVO attributeVO, final TemplateMetaDataAttributes metaDataAttr, final ProposalOption proposalOption) {
		String attributeValue = ConstantStrings.EMPTY_STRING;
		if (metaDataAttr.getProposalHeadAttributes() != null && ConstantStrings.NAME.equalsIgnoreCase(metaDataAttr.getProposalHeadAttributes().getAttributeName())) {
			attributeValue = attributeVO.populateKeyValueFromObject(metaDataAttr.getProposalHeadAttributes().getAttributeName(), ConstantStrings.PROPOSAL_OPTION_CLASS, proposalOption);
		}
		return attributeValue;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#populateLineItemAttributesList(com.nyt.mpt.template.TemplateVO, com.nyt.mpt.domain.LineItem, int)
	 */
	@Override
	public TemplateVO populateLineItemAttributesList(final TemplateVO templateVO, final LineItem lineitem, final int counter) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Populating LineItem Attributes List from TemplateVO for LineItem Id: " + lineitem.getLineItemID());
		}
		TemplateAttributeVO attributeVO = null;
		// Fetching SOS Product
		final Product product = templateVO.getReferenceDataMap().getProductMap().get(lineitem.getSosProductId());
		// Validating Product
		if (templateVO.isValidate()) {
			templateVO.validateProduct(product, lineitem.getSosProductId());
		}
		// Fetching SOS Sales Target
		final List<LineItemSalesTargetAssoc> lineItemSTAssocs = lineitem.getLineItemSalesTargetAssocs();
		for (LineItemSalesTargetAssoc lineItemSalesTargetAssoc : lineItemSTAssocs) {
			final SalesTarget salesTarget = templateVO.getReferenceDataMap().getSalesTargetMap().get(lineItemSalesTargetAssoc.getSosSalesTargetId());
			// Validating Sales Target
			if (templateVO.isValidate()) {
				templateVO.validateSalesTarget(salesTarget, lineItemSalesTargetAssoc.getSosSalesTargetId());
			}
		}
		// Fetching Creative Attributes List
		final Set<CreativeAttributeValue> creativeAttrValueList = new HashSet<CreativeAttributeValue>();
		final List<ProductCreativeAssoc> prodCreativeAssocList = productService.getProductCreativesForTemplates(lineitem);
		for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
			creativeAttrValueList.addAll(creativeService.getCreativeAttribute(productCreativeAssoc.getCreative().getCreativeId()));
		}
		// Iterate Template Sheet Map and Populate line item attribute for each sheet
		final Map<String, TemplateSheetVO> templateSheetMap = templateVO.getTemplateSheetMap();
		final Iterator<String> templateSheetIterator = templateSheetMap.keySet().iterator();
		while (templateSheetIterator.hasNext()) {
			final List<TemplateAttributeVO> lineItemAttributesList = new ArrayList<TemplateAttributeVO>();
			final TemplateSheetVO templateSheetVO = templateSheetMap.get(templateSheetIterator.next());
			final List<TemplateAttributeVO> lineItemsHeaderList = templateSheetVO.getMediaPlanLineItemsHeaderList();
			// Populating Line Item Attributes
			for (TemplateAttributeVO mediaPlanHeaderAttribute : lineItemsHeaderList) {
				attributeVO = populatingLineItemAttributes(mediaPlanHeaderAttribute, counter);
				// Populate LineItem attribute from Database
				// Populate Product data
				if (ConstantStrings.PRODUCT.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(attributeVO.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), ConstantStrings.PRODUCT_CLASS, product));
				} else // Populating creative data
				if (ConstantStrings.CREATIVE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getCreativeValue(mediaPlanHeaderAttribute, prodCreativeAssocList));
				} else // Populating Creative attribute data
				if (ConstantStrings.CREATIVE_ATRRIBUTE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getCreativeAttributeValue(mediaPlanHeaderAttribute, creativeAttrValueList));
				} else // Populating line item data
				if (ConstantStrings.LINEITEM.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getLineItemValue(attributeVO, mediaPlanHeaderAttribute, ConstantStrings.LINEITEM_CLASS, lineitem));
				} else // Populating Product sales target attribute data
				if (ConstantStrings.PRODUCT_SALESTARGET.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getProdSalesTargetAttrValue(mediaPlanHeaderAttribute, lineitem));
				} else if (ConstantStrings.TARGET.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getLineItemTargetAttrValue(templateVO, mediaPlanHeaderAttribute, lineitem));
				} else if (ConstantStrings.PACKAGE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					if (lineitem.getPackageObj() != null) {
						attributeVO.setAttributeValue(attributeVO.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(),
								ConstantStrings.PACKAGE_CLASS, lineitem.getPackageObj()));
					}
				}
				lineItemAttributesList.add(attributeVO);
			}
			if (!lineItemAttributesList.isEmpty()) {
				templateSheetVO.getMediaPlanLineItemsAttributesList().put(new Long(counter), lineItemAttributesList);
			}
		}
		return templateVO;
	}

	/**
	 * Populate value in {@link TemplateAttributeVO} and set row number of
	 * template.
	 * 
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO} contains value from data base.
	 * @param counter
	 *            Row number of template.
	 * @return Updated {@link TemplateAttributeVO}.
	 */
	private TemplateAttributeVO populatingLineItemAttributes(TemplateAttributeVO mediaPlanHeaderAttribute, int counter) {
		final TemplateAttributeVO attributeVO = new TemplateAttributeVO();
		// setting attribute values
		attributeVO.setAttributeName(mediaPlanHeaderAttribute.getAttributeName());
		attributeVO.setAttributeType(mediaPlanHeaderAttribute.getAttributeType());
		attributeVO.setAttributeTypeKey(mediaPlanHeaderAttribute.getAttributeTypeKey());
		attributeVO.setRowNum(mediaPlanHeaderAttribute.getRowNum() + counter);
		attributeVO.setColNum(mediaPlanHeaderAttribute.getColNum());
		attributeVO.setFormat(mediaPlanHeaderAttribute.getFormat());
		attributeVO.setFontSize(mediaPlanHeaderAttribute.getFontSize());
		return attributeVO;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#populateCreativeSpecList(com.nyt.mpt.template.TemplateVO, com.nyt.mpt.domain.LineItem, int, com.nyt.mpt.domain.ProductCreativeAssoc)
	 */
	@Override
	public TemplateVO populateCreativeSpecList(final TemplateVO templateVO, final LineItem lineitem, final int counter, final ProductCreativeAssoc productCreativeAssoc) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Populating LineItem Attributes List from TemplateVO for LineItem Id: " + lineitem.getLineItemID());
		}
		TemplateAttributeVO attributeVO = null;
		// Fetching Creative Attributes List
		final Set<CreativeAttributeValue> creativeAttrValueList = creativeService.getCreativeAttribute(productCreativeAssoc.getCreative().getCreativeId());
		// Iterate Template Sheet Map and Populate line item attribute for each sheet
		final Map<String, TemplateSheetVO> templateSheetMap = templateVO.getTemplateSheetMap();
		final Iterator<String> templateSheetIterator = templateSheetMap.keySet().iterator();
		while (templateSheetIterator.hasNext()) {
			final List<TemplateAttributeVO> lineItemAttributesList = new ArrayList<TemplateAttributeVO>();
			final TemplateSheetVO templateSheetVO = templateSheetMap.get(templateSheetIterator.next());
			final List<TemplateAttributeVO> lineItemsHeaderList = templateSheetVO.getMediaPlanLineItemsHeaderList();
			// Populating Line Item Attributes
			for (TemplateAttributeVO mediaPlanHeaderAttribute : lineItemsHeaderList) {
				// Populate LineItem attribute from Database
				attributeVO = populatingLineItemAttributes(mediaPlanHeaderAttribute, counter);
				// Populate Product data
				// Populating creative data
				if (ConstantStrings.CREATIVE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), ConstantStrings.CREATIVE_CLASS,
							productCreativeAssoc.getCreative()));
				} else // Populating Creative attribute data
				if (ConstantStrings.CREATIVE_ATRRIBUTE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getCreativeAttributeValue(mediaPlanHeaderAttribute, creativeAttrValueList));
				} else // Populating line item data
				if (ConstantStrings.LINEITEM.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeType())) {
					attributeVO.setAttributeValue(getLineItemValue(attributeVO, mediaPlanHeaderAttribute, ConstantStrings.LINEITEM_CLASS, lineitem));
				}
				lineItemAttributesList.add(attributeVO);
			}

			if (!lineItemAttributesList.isEmpty()) {
				templateSheetVO.getMediaPlanLineItemsAttributesList().put(Long.valueOf(counter), lineItemAttributesList);
			}
		}
		return templateVO;
	}

	/**
	 * Return a string which contains details of adSize or creative type or name
	 * of {@link Creative}.
	 * 
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO}
	 * @param prodCreativeAssocList
	 *            List of {@link ProductCreativeAssoc}
	 * @return Return a string which contains details of adSize or creative type
	 *         or name of {@link Creative}.
	 */
	private String getCreativeValue(final TemplateAttributeVO mediaPlanHeaderAttribute, final List<ProductCreativeAssoc> prodCreativeAssocList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting creative value.");
		}
		String attributeValue = ConstantStrings.EMPTY_STRING;
		// Code for Populating adSize from creative
		if (ConstantStrings.ADDSIZE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeTypeKey())) {
			/* Create string for adSize type */
			final StringBuffer adSizeBuffer = new StringBuffer();
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
				final Creative creative = productCreativeAssoc.getCreative();
				final int width = creative.getWidth();
				final int height = creative.getHeight();
				if (adSizeBuffer.length() > 0) {
					adSizeBuffer.append(ConstantStrings.COMMA);
					adSizeBuffer.append(ConstantStrings.SPACE);
				}
				adSizeBuffer.append(width);
				adSizeBuffer.append(ConstantStrings.CROSS_SIGN);
				adSizeBuffer.append(height);
				final Integer width2 = creative.getWidth2();
				final Integer height2 = creative.getHeight2();
				if (null != width2 && null != height2) {
					adSizeBuffer.append(" or ");
					adSizeBuffer.append(width2);
					adSizeBuffer.append(ConstantStrings.CROSS_SIGN);
					adSizeBuffer.append(height2);
				}
			}
			attributeValue = adSizeBuffer.toString();
		} else if (ConstantStrings.TYPE.equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeTypeKey())) {
			/* Create string for creative type */
			final StringBuffer creativeTypeBuffer = new StringBuffer();
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
				final Creative creative = productCreativeAssoc.getCreative();
				final String type = creative.getType();
				final CreativeType creativeType = CreativeType.findByName(type);
				if (creativeTypeBuffer.length() > 0) {
					creativeTypeBuffer.append(ConstantStrings.COMMA);
					creativeTypeBuffer.append(ConstantStrings.SPACE);
				}
				if (creativeType != null) {
					creativeTypeBuffer.append(creativeType.getDisplayValue());
				}
			}
			attributeValue = creativeTypeBuffer.toString();
		} else {
			// Code for populating other attribute from creative
			/* Create string for creative name or other values*/
			final StringBuffer creativeAttributeBuffer = new StringBuffer();
			for (ProductCreativeAssoc productCreativeAssoc : prodCreativeAssocList) {
				final Creative creative = productCreativeAssoc.getCreative();
				if (creativeAttributeBuffer.length() > 0) {
					creativeAttributeBuffer.append(ConstantStrings.COMMA);
					creativeAttributeBuffer.append(ConstantStrings.SPACE);
				}
				creativeAttributeBuffer.append(mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), ConstantStrings.CREATIVE_CLASS, creative));
			}
			attributeValue = creativeAttributeBuffer.toString();
		}
		return attributeValue;
	}

	/**
	 * Returns comma separated string of attribute value from
	 * {@link CreativeAttributeValue} whose type is
	 * {@link ConstantStrings.CREATIVE}.
	 * 
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO}
	 * @param creativeAttributeValueList
	 *            Set of {@link CreativeAttributeValue}
	 * @return Comma separated string of attribute value from
	 *         {@link CreativeAttributeValue} whose type is
	 *         {@link ConstantStrings.CREATIVE}.
	 */
	private String getCreativeAttributeValue(final TemplateAttributeVO mediaPlanHeaderAttribute, final Set<CreativeAttributeValue> creativeAttributeValueList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting creative attribute value.");
		}
		String attributeValue = StringUtils.EMPTY;
		for (CreativeAttributeValue creativeAttributeValue : creativeAttributeValueList) {
			if (mediaPlanHeaderAttribute.getAttributeTypeKey().equals(creativeAttributeValue.getAttribute().getAttributeKey())
					&& ConstantStrings.CREATIVE.equalsIgnoreCase(creativeAttributeValue.getAttribute().getAttributeType())) {
				if (attributeValue.equals(StringUtils.EMPTY)) {
					attributeValue = creativeAttributeValue.getAttributeValue();
				} else {
					attributeValue = attributeValue + ConstantStrings.COMMA + creativeAttributeValue.getAttributeValue();
				}
			}
		}
		return attributeValue;
	}

	/**
	 * Returns string of comma separated name from {@link LineItemTarget}.
	 * 
	 * @param mediaPlanTemplateVO
	 *            {@link TemplateVO}
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO}
	 * @param lineItem
	 *            {@link LineItem}
	 * @return string of comma separated name from {@link LineItemTarget}.
	 */
	private String getLineItemTargetAttrValue(final TemplateVO mediaPlanTemplateVO, final TemplateAttributeVO mediaPlanHeaderAttribute, final LineItem lineItem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting Line item Target attributes value.");
		}
		String attributeValue = StringUtils.EMPTY;
		if ("targetType".equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeTypeKey())) {
			final Set<LineItemTarget> geoTargetSet = lineItem.getGeoTargetSet();

			for (LineItemTarget geoTarget : geoTargetSet) {
				if (attributeValue.equals(StringUtils.EMPTY)) {
					attributeValue = mediaPlanTemplateVO.getReferenceDataMap().getTargetType().get(geoTarget.getSosTarTypeId());
				} else {
					attributeValue = attributeValue + ConstantStrings.COMMA + mediaPlanTemplateVO.getReferenceDataMap().getTargetType().get(geoTarget.getSosTarTypeId());
				}
			}
		} else {
			final Set<LineItemTarget> geoTargetSet = lineItem.getGeoTargetSet();
			for (LineItemTarget geoTarget : geoTargetSet) {
				if (attributeValue.equals(StringUtils.EMPTY)) {
					attributeValue = mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), ConstantStrings.TARGET_CLASS, geoTarget);
				} else {
					attributeValue = attributeValue + ConstantStrings.COMMA
							+ mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), ConstantStrings.TARGET_CLASS, geoTarget);
				}
			}

		}
		return attributeValue;
	}

	/**
	 * Returns comma separated string of line item attribute name from
	 * {@link ProductAttributeAssoc}
	 * 
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO}
	 * @param lineitem
	 *            {@link LineItem}
	 * @return Comma separated string of line item attribute
	 */
	private String getProdSalesTargetAttrValue(final TemplateAttributeVO mediaPlanHeaderAttribute, final LineItem lineitem) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Setting prod sales target attribute value.");
		}
		String attributeValue = StringUtils.EMPTY;
		final List<ProductAttributeAssoc> prodSalesTargetAttrList = productService.getLineItemAttrByAttrKey(lineitem.getSosProductId(), lineitem.getLineItemSalesTargetAssocs().get(0)
				.getSosSalesTargetId(), mediaPlanHeaderAttribute.getAttributeTypeKey());
		for (ProductAttributeAssoc prodSalesTargetAttrAssoc : prodSalesTargetAttrList) {
			if (attributeValue.equals(StringUtils.EMPTY)) {
				attributeValue = prodSalesTargetAttrAssoc.getAttributeValue();
			} else {
				attributeValue = attributeValue + ConstantStrings.COMMA + prodSalesTargetAttrAssoc.getAttributeValue();
			}
		}
		// If Product Sales Target Attribute is not available then fetch data
		// from Product Attribute List
		if (attributeValue == null || attributeValue.length() == 0) {
			final List<ProductAttributeAssoc> productAttributeList = productService.getLineItemAttrByAttrKey(lineitem.getSosProductId(), null, mediaPlanHeaderAttribute.getAttributeTypeKey());
			for (ProductAttributeAssoc productAttributeAssoc : productAttributeList) {
				if (attributeValue.equals(StringUtils.EMPTY)) {
					attributeValue = productAttributeAssoc.getAttributeValue();
				} else {
					attributeValue = attributeValue + ConstantStrings.COMMA + productAttributeAssoc.getAttributeValue();
				}
			}
		}
		return attributeValue;
	}

	/**
	 * Returns {@link LineItem} level data e.g Impressions Capacity, Placement Name,
	 * Start Date, End Date, Price Type ,CPM ,Offered Impressions ,Investment
	 * ,SOV ,Default In Excel ,Rate Card Price ,Avails, Comments etc.
	 * 
	 * @param mediaPlanAttributeVO
	 *            {@link TemplateAttributeVO}
	 * @param mediaPlanHeaderAttribute
	 *            {@link TemplateAttributeVO}
	 * @param className
	 *            Name of the class i.e com.nyt.mpt.domain.LineItem
	 * @param lineitem
	 *            {@link LineItem}
	 * @return {@link LineItem} level data.
	 */
	private String getLineItemValue(final TemplateAttributeVO mediaPlanAttributeVO, final TemplateAttributeVO mediaPlanHeaderAttribute, final String className, final LineItem lineitem) {
		String attributeValue = ConstantStrings.EMPTY_STRING;
		if ("startDate".equalsIgnoreCase(mediaPlanHeaderAttribute.getAttributeTypeKey())) {
			attributeValue = mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), className, lineitem);
			if (attributeValue == null || attributeValue.length() == 0) {
				attributeValue = lineitem.getFlight();
				mediaPlanAttributeVO.setFormat(TemplateValueFormateType.TEXT.name());
			}
		} else {
			if ("sov".equals(mediaPlanHeaderAttribute.getAttributeTypeKey()) && LineItemProductTypeEnum.RESERVABLE.name().equals(lineitem.getProductType().name())) {
				attributeValue = mediaPlanHeaderAttribute.populateKeyValueFromObject("sor", className, lineitem);
			} else {
				attributeValue = mediaPlanHeaderAttribute.populateKeyValueFromObject(mediaPlanHeaderAttribute.getAttributeTypeKey(), className, lineitem);
			}
		}
		return attributeValue;
	}

	/**
	 * Calculate cell name based on cell number of .xlsx file.
	 * 
	 * @param colNum
	 *            Number of the cell in .xlsx file.
	 * @return Cell name of .xlsx file.
	 */
	public String getColName(final int colNum) {
		String res = ConstantStrings.EMPTY_STRING;
		int quot = colNum;
		int rem;

		while (quot > 0) {
			quot = quot - 1;
			rem = quot % 26;
			quot = quot / 26;
			res = (char) (rem + 97) + res;
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getProposalHeadAttributesMap(java.lang.Long)
	 */
	@Override
	public Map<Long, String> getProposalHeadAttributesMap(final Long attributeID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head Attributes");
		}
		return templateDao.getProposalHeadAttributes(attributeID);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getProposalHeadAttributes()
	 */
	@Override
	public List<ProposalHeadAttributes> getProposalHeadAttributes() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching proposal head attributes list");
		}
		return templateDao.getProposalHeadAttributes();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getProposalHeadList()
	 */
	@Override
	public List<ProposalHead> getProposalHeadList() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching Proposal Head List");
		}
		return templateDao.getProposalHeadList();
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#deleteCustomTemplate(com.nyt.mpt.domain.TemplateMetaData)
	 */
	@Override
	public void deleteCustomTemplate(final TemplateMetaData customTemplate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Delete custom template, id:" + customTemplate.getTemplateId());
		}
		templateDao.deleteCustomTemplate(customTemplate);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#saveCustomTemplateMetaData(java.util.List, com.nyt.mpt.domain.TemplateMetaData, com.nyt.mpt.domain.TemplateSheetMetaData, boolean)
	 */
	@Override
	public void saveCustomTemplateMetaData(final List<TemplateJson> templateSheetData, TemplateMetaData customTemplateDB, final TemplateSheetMetaData templateSheetMetaData, boolean editAction) {
		LOGGER.info("Saving template meta data with id: " + customTemplateDB.getTemplateId() + " editAction :" + editAction);
		if (customTemplateDB.getTemplateId() != null && editAction) {
			deleteTemplateAttribute(customTemplateDB);
		}
		setTemplateSheetVO(customTemplateDB, templateSheetMetaData);
		final List<TemplateMetaDataAttributes> templateAttributesLst = getCustomTemplateMetaDataAttributesVO(templateSheetMetaData, templateSheetData, customTemplateDB);
		templateSheetMetaData.setMediaPlanAttributes(templateAttributesLst);
		customTemplateDB.getTemplateSheetList().add(templateSheetMetaData);
		templateDao.saveCustomTemplate(customTemplateDB);
		LOGGER.info("Template meta data saved with id: " + customTemplateDB.getTemplateId() + " editAction :" + editAction);
	}

	/**
	 * Delete {@link TemplateMetaDataAttributes} from data base
	 * 
	 * @param customTemplateDB
	 *            {@link TemplateMetaData}
	 */
	private void deleteTemplateAttribute(TemplateMetaData customTemplateDB) {
		List<TemplateSheetMetaData> templateSheetLst = customTemplateDB.getTemplateSheetList();
		for (TemplateSheetMetaData templateSheet : templateSheetLst) {
			for (TemplateMetaDataAttributes templateAttributesDB : templateSheet.getMediaPlanAttributes()) {
				templateDao.deleteTemplateAttribute(templateAttributesDB);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getProposalHeadDisplayName()
	 */
	@Override
	public Map<Long, String> getProposalHeadDisplayName() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching map of proposal head name ");
		}
		return templateDao.getProposalHeadDisplayName();
	}

	/**
	 * Set template meta data value
	 * 
	 * @param customTemplateMetaData
	 * @param templateSheetMetaData
	 */
	private void setTemplateSheetVO(final TemplateMetaData customTemplateMetaData, final TemplateSheetMetaData templateSheetMetaData) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Return template sheet meta data object ");
		}
		templateSheetMetaData.setMediaTemplate(customTemplateMetaData);
	}

	/**
	 * Returns List of {@link TemplateMetaDataAttributes}.
	 * 
	 * @param customTemplateSheetMetaData
	 * @param templateSheetData
	 * @param customTemplateVO
	 * @return List of {@link TemplateMetaDataAttributes}.
	 */
	private List<TemplateMetaDataAttributes> getCustomTemplateMetaDataAttributesVO(final TemplateSheetMetaData customTemplateSheetMetaData, final List<TemplateJson> templateSheetData,
			final TemplateMetaData customTemplateVO) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Return list of template sheet attribute data object ");
		}
		final List<ProposalHeadAttributes> proposalHeadAttLst = templateDao.getProposalHeadAttributes();
		final List<TemplateMetaDataAttributes> templateMetaDataAttributesLst = new ArrayList<TemplateMetaDataAttributes>();
		for (TemplateJson templateAttributes : templateSheetData) {
			final TemplateMetaDataAttributes sheetAttribute = new TemplateMetaDataAttributes();
			sheetAttribute.setId(templateAttributes.getAttributesId());
			sheetAttribute.setMediaTemplateSheet(customTemplateSheetMetaData);
			sheetAttribute.setAttributeName(templateAttributes.getTokenName());
			getProposalHeadAttributesID(sheetAttribute, proposalHeadAttLst, templateAttributes.getProposalHead(), Long.valueOf(templateAttributes.getProposalAttribute()));
			sheetAttribute.setRowNum(Integer.parseInt(templateAttributes.getRowNum()));
			sheetAttribute.setColNum(TemplateAttributeVO.getCellNameFromCellNumber(Integer.parseInt(templateAttributes.getColNum())));
			sheetAttribute.setFontSize(templateAttributes.getFontSize());
			templateMetaDataAttributesLst.add(sheetAttribute);
		}
		return templateMetaDataAttributesLst;
	}

	/**
	 * Based on the lookUpHead value and id of {@link ProposalHeadAttributes},
	 * method get data type of attribute (i.e. attributeName). According to the
	 * data type system set .xlsx cell format type for this attribute.
	 * 
	 * @param sheetAttribute
	 *            Object of {@link TemplateMetaDataAttributes}
	 * @param proposalHeadAttLst
	 *            List of {@link ProposalHeadAttributes}.
	 * @param proposalHead
	 *            lookUpHead name of {@link ProposalHeadAttributes}
	 * @param proposalAttributeId
	 *            {@link ProposalHeadAttributes} id.
	 */
	private void getProposalHeadAttributesID(TemplateMetaDataAttributes sheetAttribute, final List<ProposalHeadAttributes> proposalHeadAttLst, final String proposalHead, final Long proposalAttributeId) {
		final TemplateAttributeVO attributeVO = new TemplateAttributeVO();
		String format = ConstantStrings.EMPTY_STRING;
		for (ProposalHeadAttributes proposalHeadAttributes : proposalHeadAttLst) {
			if (proposalHeadAttributes.getLookUpHead().equals(proposalHead) && proposalHeadAttributes.getId().equals(proposalAttributeId)) {
				sheetAttribute.setProposalHeadAttributes(proposalHeadAttributes);
				if (ConstantStrings.PRODUCT.equalsIgnoreCase(proposalHeadAttributes.getLookUpHead())) {
					format = attributeVO.populateKeyType(proposalHeadAttributes.getAttributeName(), ConstantStrings.PRODUCT_CLASS);
				} else if (ConstantStrings.LINEITEM.equalsIgnoreCase(proposalHeadAttributes.getLookUpHead())) {
					format = attributeVO.populateKeyType(proposalHeadAttributes.getAttributeName(), ConstantStrings.LINEITEM_CLASS);
				} else if (ConstantStrings.PROPOSAL.equalsIgnoreCase(proposalHeadAttributes.getLookUpHead())) {
					format = attributeVO.populateKeyType(proposalHeadAttributes.getAttributeName(), ConstantStrings.PROPOSAL_CLASS);
				} else if (ConstantStrings.PROPOSALVERSION.equalsIgnoreCase(proposalHeadAttributes.getLookUpHead())) {
					format = attributeVO.populateKeyType(proposalHeadAttributes.getAttributeName(), ConstantStrings.PROPOSALVERSION_CLASS);
				} else if (ConstantStrings.USER.equalsIgnoreCase(proposalHeadAttributes.getLookUpHead())) {
					format = attributeVO.populateKeyType(proposalHeadAttributes.getAttributeName(), ConstantStrings.USER_CLASS);
				} else {
					format = ConstantStrings.TEXT;
				}
				sheetAttribute.setFormat(attributeVO.getFormat(format));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getMediaPlanTemplates(com.nyt.mpt.util.filter.FilterCriteria, com.nyt.mpt.util.PaginationCriteria, com.nyt.mpt.util.SortingCriteria)
	 */
	@Override
	public List<TemplateMetaData> getMediaPlanTemplates(final FilterCriteria filterCriteria, final PaginationCriteria paginationCriteria, final SortingCriteria sortingCriteria) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching list of template meta data ");
		}
		return templateDao.getMediaPlanTemplates(filterCriteria, paginationCriteria, sortingCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getMediaPlanTemplatesCount(com.nyt.mpt.util.filter.FilterCriteria)
	 */
	@Override
	public int getMediaPlanTemplatesCount(FilterCriteria filterCriteria){
		return templateDao.getMediaPlanTemplatesCount(filterCriteria);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#saveHeadAttributes(com.nyt.mpt.domain.ProposalHeadAttributes)
	 */
	@Override
	public ProposalHeadAttributes saveHeadAttributes(final ProposalHeadAttributes headAttributes) {
		return templateDao.saveHeadAttributes(headAttributes);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getHeadAttributesByParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ProposalHeadAttributes> getHeadAttributesByParameter(final String attributeName, final String headName) {
		return templateDao.getHeadAttributesByParameter(attributeName, headName);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getMediaPlanTemplateById(long)
	 */
	@Override
	public TemplateMetaData getMediaPlanTemplateById(long templateId) {
		return templateDao.getMediaPlanTemplateById(templateId);
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getProposalHeadAttributes(java.util.Set)
	 */
	@Override
	public List<ProposalHeadAttributes> getProposalHeadAttributes(Set<String> tokenSet) {
		return templateDao.getProposalHeadAttributes(tokenSet);
	}

	/**
	 * Returns a map in which attribute name of {@link ProposalHeadAttributes}
	 * is key and lookUpHead as a value.
	 * 
	 * @param headName
	 *            Name of {@link ProposalHead}.
	 * @return a map in which attribute name of {@link ProposalHeadAttributes}
	 *         is key and lookUpHead as a key.
	 */
	private Map<String, String> getProHeadAttrMapByName(String headName) {
		Map<String, String> proAttributeMap = new HashMap<String, String>();
		ProposalHead proposalHead = templateDao.getProHeadListByName(headName).get(0);
		List<ProposalHeadAttributes> headAttributes = proposalHead.getProposalHeadAttributes();
		for (ProposalHeadAttributes proposalHeadAttributes : headAttributes) {
			proAttributeMap.put(proposalHeadAttributes.getAttributeName(), proposalHeadAttributes.getLookUpHead());
		}
		return proAttributeMap;
	}

	/* (non-Javadoc)
	 * @see com.nyt.mpt.service.ITemplateService#getTemplateIdByName(java.lang.String)
	 */
	@Override
	public Long getTemplateIdByName(final String templateName) {
		return templateDao.getTemplateIdByName(templateName);
	}
		
	public void setProductService(final IProductService productService) {
		this.productService = productService;
	}

	public void setCreativeService(final ICreativeService creativeService) {
		this.creativeService = creativeService;
	}

	public void setTemplateDao(final ITemplateDAO templateDao) {
		this.templateDao = templateDao;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setSosService(final ISOSService sosService) {
		this.sosService = sosService;
	}

	public void setProposalSOSService(final IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}

	public void setTargetingService(final ITargetingService targetingService) {
		this.targetingService = targetingService;
	}
}
