/**
 *
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.form.AttributeAssocForm;
import com.nyt.mpt.form.AttributeForm;
import com.nyt.mpt.form.InfoTipForm;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.AttributeValidator;

/**
 * This <code>ManageAttributeController</code> is used for managing Attributes of PRODUCT and CREATIVE
 * 
 * @author amandeep.singh
 */
@Controller
@RequestMapping("/manageAttribute/*")
public class ManageAttributeController extends AbstractBaseController {

	private static final String ATTRIBUTE_NAME = "attributeName";

	private static final String ADD = "add";

	private static final String ATTRIBUTE_FORM = "attributeForm";

	private static final String VIEW_ATTRIBUTE = "manageAttributeAssocDisplayPage";

	private static final String VIEW_INFOTIP = "infoTipDisplayPage";

	private static final String PRODUCT = "PRODUCT";

	private static final String CREATIVE = "CREATIVE";

	private static final String PRODUCT_SALESTARGET = "PRODUCT_SALESTARGET";

	private static final String CREATIVE_ATTRIBUTE = "CREATIVE_ATTRIBUTE";

	private static final Logger LOGGER = Logger.getLogger(ManageAttributeController.class);

	private IAttributeService attributeService;

	private ICreativeService creativeService;

	private IProductService adProductService;

	private ProposalHelper proposalHelper;

	private ITemplateService templateService;

	/**
	 * Returns Model and view for Manage Attribute screen
	 * @param attributeForm
	 * @return
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage(@ModelAttribute(ATTRIBUTE_FORM) final AttributeForm attributeForm) {
		final ModelAndView view = new ModelAndView("manageAttributeDisplayPage");
		view.addObject(ATTRIBUTE_FORM, attributeForm);
		return view;
	}

	/**
	 * Loads master grid data for manage attribute
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadGridData(@ModelAttribute final TableGrid<AttributeForm> tblGrid) {
		final ModelAndView view = new ModelAndView("manageAttributeGridData");
		this.setAttributeDataToGrid(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Sets attribute data to the table grid
	 * @param tblGrid
	 */
	private void setAttributeDataToGrid(final TableGrid<AttributeForm> tblGrid) {
		final List<Attribute> attributeLst = attributeService.getFilteredAttributeList(
				tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!attributeLst.isEmpty()) {
			final int count = attributeService.getFilteredAttributeListCount(tblGrid.getFilterCriteria());
			tblGrid.setGridData(convertAttributeDtoToForm(attributeLst), count);
		}
	}

	/**
	 * Converts List of {@link Attribute} to List of {@link AttributeForm}
	 * @param attributeList
	 * @return
	 */
	private List<AttributeForm> convertAttributeDtoToForm(final List<Attribute> attributeList) {
		final List<AttributeForm> attributeFormList = new ArrayList<AttributeForm>();
		for (Attribute attribute : attributeList) {
			final AttributeForm attributeForm = new AttributeForm();
			attributeForm.populateForm(attribute);
			attributeFormList.add(attributeForm);
		}
		return attributeFormList;
	}

	/**
	 * Checks attribute association with creative or product
	 * @param attributeForm
	 * @param results
	 * @return
	 */
	private CustomBindingResult attributeAssocValidationResult(final AttributeAssocForm attributeForm, final CustomBindingResult results) {
		final AttributeValidator validator = new AttributeValidator();
		if (StringUtils.equals(attributeForm.getOperation(), ADD)) {
			validator.validateForAttributeAssoc(attributeForm, results);
			final boolean duplicateName = attributeService.isDuplicateAttributeName(
					attributeForm.getAttributeName(), Long.valueOf(attributeForm.getAttributeId()), attributeForm.getAttributeType());
			if (duplicateName) {
				results.rejectValue(ATTRIBUTE_NAME, ErrorCodes.DuplicateAttribute, ATTRIBUTE_NAME,
						new Object[] {"Attribute Name"}, UserHelpCodes.DuplicateName);
			}
		}
		validator.validateForAttributeAssocValue(attributeForm, results);
		return results;
	}

	/**
	 * Saves attribute association with creative
	 * @param response
	 * @param attributeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveCreativeAttributeAssoc")
	public AjaxFormSubmitResponse saveCreativeAttributeAssoc(final HttpServletResponse response, @ModelAttribute("attributeAssocForm") final AttributeAssocForm attributeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Creative Attribute Assoc. Attribute Form: " + attributeForm);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		CustomBindingResult results = new CustomBindingResult(ATTRIBUTE_FORM, attributeForm);
		attributeForm.setAttributeOptionalValue(attributeForm.getAttributeValue());
		results = attributeAssocValidationResult(attributeForm, results);
		if (StringUtils.equalsIgnoreCase(attributeForm.getAttributeType(), AttributeType.CREATIVE.toString())
				&& StringUtils.equals(attributeForm.getOperation(), ADD) && StringUtils.equals(attributeForm.getAction(), "AssociateAttribute")) {
			final boolean duplicateAssoc = creativeService.isDuplicateCreativeAttributeAssocExist(attributeForm.getId(),
					Long.valueOf(attributeForm.getAttributeId()));
			if (duplicateAssoc) {
				results.rejectValue(ATTRIBUTE_NAME, ErrorCodes.DuplicateAssoc, ATTRIBUTE_NAME, new Object[] {"Attribute Name"}, UserHelpCodes.DuplicateName);
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			long attrId = 0;
			Attribute bean = attributeForm.populate(new Attribute());
			if ("AddNew".equals(attributeForm.getAction())) {
				bean = attributeService.createAttribute(bean);
				attrId = bean.getAttributeId();
				// Update the Attribute details in Proposal Head Attribute
				saveHeadAttributes(bean, bean.getAttributeName());
			}
			if (ADD.equals(attributeForm.getOperation())) {
				creativeService.addCreativeAttribute(attributeForm.getId(), bean, attributeForm.getAttributeValue());
			} else {
				bean.setAttributeId(Long.parseLong(attributeForm.getHidAttributeId()));
				creativeService.updateCreativeAttribute(attributeForm.getId(), bean, attributeForm.getAttributeValue());
				attrId = bean.getAttributeId();
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, attrId);
		}
		return ajaxResponse;
	}

	/**
	 * Saves attribute association with product
	 * @param response
	 * @param attributeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveProductAttributeAssoc")
	public AjaxFormSubmitResponse saveProductAttributeAssoc(final HttpServletResponse response, @ModelAttribute("attributeAssocForm") final AttributeAssocForm attributeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("saving ProductAttributeAssoc. AttributeForm:" + attributeForm);
		}
		final AjaxFormSubmitResponse ajaxFormResponse = new AjaxFormSubmitResponse(getMessageSource());
		CustomBindingResult results = new CustomBindingResult(ATTRIBUTE_FORM, attributeForm);
		attributeForm.setAttributeOptionalValue(attributeForm.getAttributeValue());
		results = attributeAssocValidationResult(attributeForm, results);
		if (StringUtils.equalsIgnoreCase(attributeForm.getAttributeType(), AttributeType.PRODUCT.toString())
				&& StringUtils.equals(attributeForm.getOperation(), ADD) && StringUtils.equals(attributeForm.getAction(), "AssociateAttribute")) {
			final boolean duplicateAssoc = adProductService.isDuplicateProductAttributeAssocExist(attributeForm.getId(),
					Long.valueOf(attributeForm.getAttributeId()), getSalesTargetId(attributeForm.getSalestargetId()));
			if (duplicateAssoc) {
				results.rejectValue(ATTRIBUTE_NAME, ErrorCodes.DuplicateAssoc, ATTRIBUTE_NAME, new Object[] {"Attribute Name"},
						UserHelpCodes.DuplicateName);
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxFormResponse, results);
		} else {
			long attrId = 0;
			Attribute bean = attributeForm.populate(new Attribute());
			if ("AddNew".equals(attributeForm.getAction())) {
				bean = attributeService.createAttribute(bean);
				attrId = bean.getAttributeId();
				// Update the Attribute details in Proposal Head Attribute
				saveHeadAttributes(bean, bean.getAttributeName());
			}
			final ProductAttributeAssoc assoc = new ProductAttributeAssoc();
			assoc.setProductId(attributeForm.getId());
			assoc.setSalesTargetId(getSalesTargetId(attributeForm.getSalestargetId()));
			assoc.setAttributeValue(attributeForm.getAttributeValue());
			assoc.setAttribute(bean);
			if (ADD.equals(attributeForm.getOperation())) {
				adProductService.addLineItemAttribute(assoc);
			} else {
				bean.setAttributeId(Long.valueOf(attributeForm.getHidAttributeId()));
				adProductService.updateLineItemAttribute(assoc);
				attrId = bean.getAttributeId();
			}
			ajaxFormResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, attrId);
		}
		return ajaxFormResponse;
	}

	/**
	 * Returns attribute detail by <code>attributeForm</code>
	 * @param 	attributeForm
	 * 			{@link AttributeForm} has all the information related to get {@link Attribute} Object
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAttributeDetails")
	public AjaxFormSubmitResponse getAttributeDetails(final AttributeAssocForm attributeForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Attribute attribute = attributeService.getAttribute(Long.valueOf(attributeForm.getAttributeId()));
		final AttributeForm adAttributeForm = new AttributeForm();
		adAttributeForm.populateForm(attribute);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, adAttributeForm);
		return ajaxResponse;
	}

	/**
	 * Saves/Updates attribute to the AMPT database
	 * @param response
	 * @param attributeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveAttribute")
	public AjaxFormSubmitResponse saveAttribute(final HttpServletResponse response, @ModelAttribute(ATTRIBUTE_FORM) final AttributeForm attributeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Attribute. Attribute Form:" + attributeForm);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(ATTRIBUTE_FORM, attributeForm);
		new AttributeValidator().validate(attributeForm, results);

		if (Long.valueOf(attributeForm.getAttributeId()) > 0) {
			if ("Creative".equalsIgnoreCase(attributeForm.getAttributeTypeStr())) {
				attributeForm.setAttributeType(CREATIVE);
			} else {
				attributeForm.setAttributeType(PRODUCT);
			}
		}
		final boolean duplicateAttr = attributeService.isDuplicateAttributeName(attributeForm.getAttributeName(), 
				Long.valueOf(attributeForm.getAttributeId()), attributeForm.getAttributeType());
		if (duplicateAttr) {
			results.rejectValue(ATTRIBUTE_NAME, ErrorCodes.DuplicateAttribute, ATTRIBUTE_NAME, new Object[] { "Attribute Name" }, UserHelpCodes.DuplicateName);
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			String oldName = null;
			Attribute bean = attributeForm.populate(new Attribute());
			if (bean.getAttributeId() == 0) {
				oldName = bean.getAttributeName();
				bean = attributeService.createAttribute(bean);
			} else {
				oldName = attributeService.getAttribute(bean.getAttributeId()).getAttributeName();
				bean = attributeService.updateAttribute(bean, attributeForm.isForceUpdate());
			}
			// Update the Attribute details in Proposal Head Attribute
			saveHeadAttributes(bean, oldName);
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, bean.getAttributeId());
		}
		return ajaxResponse;
	}

	/**
	 * Deletes attribute from the AMPT database
	 * @param attributeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAttribute")
	public long deleteAttribute(@RequestParam final long attributeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Attribute. AttributeId:" + attributeId);
		}
		final Attribute attribute = new Attribute();
		attribute.setAttributeId(attributeId);
		return attributeService.deleteAttribute(attribute).getAttributeId();
	}

	/**
	 * Returns Attribute details including description.
	 * @param attributeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAttributeDesc")
	public AttributeForm getAttributeDescription(final AttributeForm attributeForm) {
		final Attribute attribute = attributeService.getAttribute(Long.valueOf(attributeForm.getAttributeId()));
		final AttributeForm adAttributeForm = new AttributeForm();
		adAttributeForm.populateForm(attribute);
		return adAttributeForm;
	}

	/**
	 * Returns model and view for creative attribute and their association
	 * @param attributeForm
	 * @return
	 */
	@RequestMapping("/getCreativeAttributes")
	public ModelAndView getCreativeAttributes(final AttributeAssocForm attributeForm) {
		final List<Attribute> attributeLst = attributeService.getAttributeList(true, AttributeType.CREATIVE);
		if (ADD.equals(attributeForm.getOperation())) {
			final Set<CreativeAttributeValue> creativeAttrSet = creativeService.getCreativeAttribute(attributeForm.getId());
			if (!creativeAttrSet.isEmpty()) {
				for (CreativeAttributeValue creativeAttribute : creativeAttrSet) {
					for (int i = 0; i < attributeLst.size(); i++) {
						if (attributeLst.get(i).getAttributeId() == creativeAttribute.getAttribute().getAttributeId()) {
							attributeLst.remove(i);
						}
					}
				}
			}
		}
		return getAssociationView(attributeForm, attributeLst);
	}

	/**
	 * Return model and view for product attribute and their association
	 * @param attributeForm
	 * @return
	 */
	@RequestMapping("/getProductAttributes")
	public ModelAndView getProductAttributes(final AttributeAssocForm attributeForm) {
		final List<Attribute> attributeLst = attributeService.getAttributeList(true, AttributeType.PRODUCT);
		if (ADD.equals(attributeForm.getOperation())) {
			final Long salesTargetId = getSalesTargetId(attributeForm.getSalestargetId());
			final List<ProductAttributeAssoc> productAttrList = adProductService.getLineItemAttributes(attributeForm.getId(), salesTargetId);
			if (!productAttrList.isEmpty()) {
				for (ProductAttributeAssoc productAttribute : productAttrList) {
					for (int i = 0; i < attributeLst.size(); i++) {
						if (attributeLst.get(i).getAttributeId() == productAttribute.getAttribute().getAttributeId()) {
							attributeLst.remove(i);
						}
					}
				}
			}
		}
		return getAssociationView(attributeForm, attributeLst);
	}

	/**
	 * Returns the association details of a attribute
	 * @return
	 */
	@RequestMapping("/getAssocDetails")
	public ModelAndView getAssocDetails(final InfoTipForm infoTipForm) {
		final ModelAndView view = new ModelAndView(VIEW_INFOTIP);
		final List<InfoTipForm> infoTipFormList = new ArrayList<InfoTipForm>();
		if (CREATIVE.equals(infoTipForm.getType())) {
			LOGGER.info("Search Associated Creatives and Values for AttributeId: " + infoTipForm.getId());
			view.addObject("type", "Creative");

			final List<CreativeAttributeValue> creativeAttrList = creativeService.getCreativeAttrAssocListByAttributeId(infoTipForm.getId());
			if (creativeAttrList != null && !creativeAttrList.isEmpty()) {
				for (CreativeAttributeValue creativeAttrValue : creativeAttrList) {
					final InfoTipForm infoTipFormDB = new InfoTipForm();
					infoTipFormDB.setName(creativeAttrValue.getCreative().getName());
					infoTipFormDB.setId(creativeAttrValue.getCreative().getCreativeId());
					infoTipFormDB.setType(infoTipForm.getType());
					infoTipFormDB.setValue(creativeAttrValue.getAttributeValue());
					infoTipFormList.add(infoTipFormDB);
				}
			}
		} else if (PRODUCT.equals(infoTipForm.getType())) {
			LOGGER.info("Search Associated Product and Values for AttributeId: " + infoTipForm.getId());
			view.addObject("type", "Product");

			final List<ProductAttributeAssoc> productAttrList = adProductService.getProductAttrAssocListByAttributeId(infoTipForm.getId());
			if (productAttrList != null && !productAttrList.isEmpty()) {
				final Map<Long, String> productMap = proposalHelper.getAllProducts();
				final Map<Long, String> salesTargetMap = proposalHelper.getSalesTarget();
				for (ProductAttributeAssoc productAttrValue : productAttrList) {
					final InfoTipForm infoTipFormDB = new InfoTipForm();
					infoTipFormDB.setName(productMap.get(productAttrValue.getProductId()));
					infoTipFormDB.setId(productAttrValue.getProductId());
					infoTipFormDB.setType(infoTipForm.getType());
					infoTipFormDB.setValue(productAttrValue.getAttributeValue());
					if (productAttrValue.getSalesTargetId() == null) {
						infoTipFormDB.setSalesTargetName("All");
					} else {
						infoTipFormDB.setSalesTargetName(salesTargetMap.get(productAttrValue.getSalesTargetId()));
					}
					infoTipFormList.add(infoTipFormDB);
				}
			}
		}
		view.addObject("assocList", infoTipFormList);
		return view;
	}

	private Long getSalesTargetId(final String salesTargetId) {
		if (StringUtils.isNotBlank(salesTargetId)) {
			return "-1".equals(salesTargetId) ? null : Long.valueOf(salesTargetId);
		}
		return null;
	}

	/**
	 * Returns the view for attribute association
	 * @param attributeForm
	 * @param attributeLst
	 * @return
	 */
	private ModelAndView getAssociationView(final AttributeAssocForm attributeForm, final List<Attribute> attributeLst) {
		final Map<Long, String> attributeMap = new LinkedHashMap<Long, String>();
		for (Attribute attribute : attributeLst) {
			attributeMap.put(attribute.getAttributeId(), attribute.getAttributeName());
		}
		final ModelAndView view = new ModelAndView(VIEW_ATTRIBUTE);
		view.addObject("attributeAssocForm", attributeForm);
		view.addObject("allAttributes", attributeMap);
		view.addObject("attributeType", attributeForm.getAttributeType().toUpperCase());
		return view;
	}

	/**
	 * Updates the Attribute details in Proposal Head Attribute
	 * @param bean
	 * @param oldName
	 */
	private void saveHeadAttributes(final Attribute bean, final String oldName) {
		String headName = ConstantStrings.EMPTY_STRING;
		ProposalHeadAttributes headAttributes = null;
		if (StringUtils.equalsIgnoreCase(CREATIVE, bean.getAttributeType())) {
			headName = CREATIVE_ATTRIBUTE;
		} else {
			headName = PRODUCT_SALESTARGET;
		}
		final List<ProposalHeadAttributes> getHeadAttrLst = templateService.getHeadAttributesByParameter(oldName, headName);
		if (getHeadAttrLst == null) {
			headAttributes = createHeadAttributes(bean, headName);
		} else {
			headAttributes = getHeadAttrLst.get(0);
			headAttributes.setDisplayAttributeName(bean.getAttributeName());
			headAttributes.setAttributeName(bean.getAttributeKey());
		}
		headAttributes.setLookUpHead(headName);
		headAttributes.setAutoConfigKey(headName + ConstantStrings.UNDER_SCORE + bean.getAttributeKey());
		templateService.saveHeadAttributes(headAttributes);
	}

	private ProposalHeadAttributes createHeadAttributes(final Attribute bean, final String headName) {
		final ProposalHeadAttributes headAttributes = new ProposalHeadAttributes();
		ProposalHead proposalHead = null;
		headAttributes.setAttributeName(bean.getAttributeKey());
		headAttributes.setDisplayAttributeName(bean.getAttributeName());
		headAttributes.setId(null);
		final List<ProposalHead> proposalHeadlst = templateService.getProposalHeadList();
		for (ProposalHead head : proposalHeadlst) {
			if (StringUtils.equalsIgnoreCase(headName, head.getHeadName())) {
				proposalHead = head;
			}
		}
		final List<ProposalHeadAttributes> headAttributesLst = new ArrayList<ProposalHeadAttributes>();
		headAttributesLst.add(headAttributes);
		proposalHead.setProposalHeadAttributes(headAttributesLst);
		headAttributes.setProposalHead(proposalHead);
		return headAttributes;
	}

	public void setAttributeService(final IAttributeService attributeService) {
		this.attributeService = attributeService;
	}

	public void setCreativeService(final ICreativeService creativeService) {
		this.creativeService = creativeService;
	}

	public void setAdProductService(final IProductService adProductService) {
		this.adProductService = adProductService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}
}
