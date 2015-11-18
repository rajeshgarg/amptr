/**
 * 
 */
package com.nyt.mpt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.ProposalHead;
import com.nyt.mpt.domain.ProposalHeadAttributes;
import com.nyt.mpt.domain.TemplateJson;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.domain.TemplateMetaDataAttributes;
import com.nyt.mpt.domain.TemplateSheetMetaData;
import com.nyt.mpt.form.TemplateHelpPojo;
import com.nyt.mpt.form.TemplateManagementForm;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.template.TemplateAttributeVO;
import com.nyt.mpt.template.TemplateSheetVO;
import com.nyt.mpt.template.TemplateVO;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.TemplateAttributeComparator;
import com.nyt.mpt.util.TemplateGenerator;
import com.nyt.mpt.util.XMLResponseForAjaxCall;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.validator.TemplateManagementValidator;

/**
 * This <code>ManageTemplateController</code> is used to manage all the Template related operations
 * 
 * @author rakesh.tewari
 */
@Controller
@RequestMapping("/templateManagement/*")
public class ManageTemplateController extends AbstractBaseController {

	private static final String TEMPLATE_FORM = "templateManagementForm";
	private TemplateGenerator templateGenerator;
	private ITemplateService templateService;

	private static final String MANAGE_TEMPLATE = "manageTemplate";
	private static final String TEMPLATE_CONFIG = "templateConfigPage";
	private static final String GRID_DATA = "templateGridData";

	private static final String SHEET_NAME = "templateSheetName";
	private static final String SHEET_ID = "templateSheetId";
	private static final String PROPOSAL_HEAD = "ProposalHead";
	private static final String LINEITEM_HEAD = "ProposalLineItemHead";
	private static final String FILE_NAME = "templateFileName";
	private static final String TEMPLATE_NAME = "templateName";
	private static final String TEMPLATE_HELP = "templateHelp";
	private static final String TMP_FILE = "tmpFile";
	private static final String FONT_SIZE = "fontsize";
	private static final String USE_EXISTING_ROW = "useExistingRow";

	private static final int MAX_FINT_SIZE = 20;
	private static final int MIN_FINT_SIZE = 8;

	/**
	 * This method render <tt>Manage Template</tt> view.
	 * And this view is used to configure template.
	 * 
	 * @param templteForm
	 *            {@link TemplateManagementForm}
	 * @return <tt>Manage Template</tt> view.
	 */
	@RequestMapping("/viewTemplateManagement")
	public ModelAndView viewTemplateManagement(@ModelAttribute(TEMPLATE_FORM) final TemplateManagementForm templteForm) {
		final ModelAndView view = new ModelAndView(MANAGE_TEMPLATE);
		view.addObject(TEMPLATE_FORM, templteForm);
		return view;
	}

	/**
	 * This method is used for loading the data in the <tt>Template</tt> grid 
	 * based on <tt>Filter Criteria</tt>, <tt>Pagination Criteria</tt>
	 * and <tt>Sorting Criteria</tt>.
	 * 
	 * @param tblGrid
	 *            {@link TableGrid} of {@link TemplateManagementForm}
	 * @return {@link ModelAndView} and <tt>Template</tt> grid data based on <tt>Filter Criteria</tt>,
	 *         <tt>Pagination Criteria</tt> and <tt>Sorting Criteria</tt>.
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadGridData(@ModelAttribute final TableGrid<TemplateManagementForm> tblGrid) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setTemplateDataToGrid(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Validates the data of the uploaded <tt>Template</tt> and the template itself before configuration.
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param templateForm
	 *            {@link TemplateManagementForm}
	 * @return Validation message if any validation error occurs.
	 * @throws IOException
	 *             in case of access errors (if the temporary store fails)
	 */
	@ResponseBody
	@RequestMapping("/validateNewDocument")
	public XMLResponseForAjaxCall validatorNewDocument(final HttpServletResponse response, @ModelAttribute(TEMPLATE_FORM) final TemplateManagementForm templateForm) throws IOException {
		final XMLResponseForAjaxCall ajaxResponse = new XMLResponseForAjaxCall(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(TEMPLATE_FORM, templateForm);
		if (templateForm.getCustomTemplateFile() == null) {
			/*
			 * When select a file with browse button and then remove or change file name
			 */
			results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.FileNotFound, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { ConstantStrings.SPACE },
					UserHelpCodes.CorruptTemplateFile);
		}
		if (templateForm != null && templateForm.getCustomTemplateFile() != null) {
			final String templateName = templateForm.getCustomTemplateFile().getOriginalFilename();
			new TemplateManagementValidator().validate(templateForm, results);
			if (!results.hasErrors() && StringUtils.isNotBlank(templateName)) {
				final Workbook customTemplate = getWorkBook(templateName, templateForm.getCustomTemplateFile().getInputStream());
				if (customTemplate == null) {
					/* When template is corrupt or not .xlsx type */
					results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.CorruptTemplateFile, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName },
							UserHelpCodes.CorruptTemplateFile);
				} else if (customTemplate != null) {
					final FilterCriteria filterCriteria = new FilterCriteria("templateName", templateName, "eq");
					final List<TemplateMetaData> metaDataList = templateService.getMediaPlanTemplates(filterCriteria, null, null);
					if (metaDataList == null || metaDataList.isEmpty()) {
						final Sheet sheet = customTemplate.getSheetAt(0);
						if (StringUtils.isBlank(sheet.getSheetName()) || templateGenerator.performsXSS(sheet.getSheetName())) {
							results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.containsXSSAttackCharactersInSheetName, ConstantStrings.CUSTOM_TEMPLATE_FILE,
									new Object[] { templateName }, UserHelpCodes.HelpXSSAttackCharactersInSheet);
						}
						// In case of edit template, uploading different template file
						if (templateForm.getTemplateId() > 0) {
							results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.TemplateFileNameNotMatch, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName },
									UserHelpCodes.HelpNeedSameFileName);
						} else if (!templateGenerator.isTemplateHashToken(customTemplate, templateName)) {// In case of add a new template , with no token
							results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.TokenNotFoundInTemplateFile, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName },
									UserHelpCodes.HelpPrefixedTokenMmissing);
						}
					} else {// When a template is already configure
						if (templateForm.getTemplateId() == 0) {// In case of add a new template, but this is already configure
							results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.DuplicateTemplateFileName, 
									ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName }, UserHelpCodes.DuplicateTemplateNameFileName);
						} else {/* for Template Edit */
							if (templateForm.getTemplateId() != metaDataList.get(0).getTemplateId()) {// When upload a template which is already configure but not same which we want edit
								results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.TemplateFileNameNotMatch, 
										ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName }, UserHelpCodes.HelpNeedSameFileName);
							} else if (!templateGenerator.isTemplateHashToken(customTemplate, templateName)) {//In case of edit template, there is no token in template
								results.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.TokenNotFoundInTemplateFile, 
										ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { templateName }, UserHelpCodes.HelpPrefixedTokenMmissing);
							}
						}
					}
				}
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}
		return ajaxResponse;
	}

	/**
	 * Fetches all the attributes of the template headers based on the id of the
	 * given {@link ProposalHead}.Returns the map of the proposal header
	 * attributes fetched with its key value pairs representing the id and
	 * display name of {@link ProposalHeadAttributes} respectively.
	 * 
	 * @param headId
	 *            {@link ProposalHead} id
	 * @return Returns the map of the proposal header attributes fetched with
	 *         its key value pairs representing the id and display name of
	 *         {@link ProposalHeadAttributes} respectively.
	 */
	@ResponseBody
	@RequestMapping("/getProposalAttribute")
	public Map<Long, String> getProposalAttribute(@RequestParam("headId") final Long headId) {
		return templateService.getProposalHeadAttributesMap(headId);
	}

	/**
	 * After validating the uploaded <tt>Template</tt> this method renders 
	 * uploaded custom <tt>Template</tt> data for save and edit <tt>Template</tt>.
	 * 
	 * @param templateForm
	 *            {@link TemplateManagementForm}
	 * @return Validation message if any validation error occurs and render the view.
	 *         uploaded custom <tt>Template</tt> data.
	 * @throws IOException
	 *             in case of access errors (if the temporary store fails)
	 */
	@RequestMapping("/uploadCustomTemplate")
	public ModelAndView uploadCustomTemplate(@ModelAttribute(TEMPLATE_FORM) final TemplateManagementForm templateForm) throws IOException {
		final ModelAndView view = new ModelAndView(TEMPLATE_CONFIG);
		final String fileName = templateForm.getCustomTemplateFile().getOriginalFilename();
		final long templateId = templateForm.getTemplateId();
		final Workbook customTemplate = getWorkBook(fileName, templateForm.getCustomTemplateFile().getInputStream());
		final TemplateVO templateVO = templateGenerator.getTemplateVOFromCustomTemplate(customTemplate, fileName);
		TreeMap<String, List<TemplateAttributeVO>> attributesMap = new TreeMap<String, List<TemplateAttributeVO>>();
		if (templateVO != null && templateVO.getTemplateSheetMap() != null && !templateVO.getTemplateSheetMap().isEmpty()) {
			final Map<String, TemplateSheetVO> templateSheetMap = templateVO.getTemplateSheetMap();
			for (String templateSheetKey : templateSheetMap.keySet()) {
				final TemplateSheetVO templateSheetVO = templateSheetMap.get(templateSheetKey);
				if (templateSheetVO != null) {
					if (templateId == 0) {// Add a new template
						attributesMap = templateSheetVO.getTemplateAttributesMap();
					} else {// Edit a template
						if (templateSheetVO.getTemplateAttributesMap() != null) {
							if (templateSheetVO.getTemplateAttributesMap().containsKey(ConstantStrings.PROPOSAL_HEADER)) {// Check proposal level data
								if (templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_HEADER) != null
										&& !templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_HEADER).isEmpty()) {
									attributesMap.put(ConstantStrings.PROPOSAL_HEADER,
											getUpdatedAttributeList(templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_HEADER), templateId, true));
								}
							}
							if (templateSheetVO.getTemplateAttributesMap().containsKey(ConstantStrings.PROPOSAL_LINEITEM)) {//Check line item level data
								if (templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_LINEITEM) != null
										&& !templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_LINEITEM).isEmpty()) {
									attributesMap.put(ConstantStrings.PROPOSAL_LINEITEM,
											getUpdatedAttributeList(templateSheetVO.getTemplateAttributesMap().get(ConstantStrings.PROPOSAL_LINEITEM), templateId, false));
								}
							}
						}
					}
					view.addObject(SHEET_NAME, templateSheetVO.getName());
				}
			}
		}
		final String tmpFile = templateGenerator.saveFileToTempDir(templateForm);
		view.addObject(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, tokenAutoConfig(attributesMap, templateVO));
		view.addObject(FILE_NAME, fileName);
		view.addObject(PROPOSAL_HEAD, getProposalHead(ConstantStrings.PROPOSAL));
		view.addObject(LINEITEM_HEAD, getProposalHead(ConstantStrings.LINEITEM));
		view.addObject(TMP_FILE, tmpFile);
		view.addObject(FONT_SIZE, getFontSize());
		view.addObject(USE_EXISTING_ROW, templateForm.isUseExistingRow());
		return view;
	}

	/**
	 * Returns a map of {@link ProposalHead} id as <tt>key</tt> and map of
	 * {@link ProposalHeadAttributes} with its id and display name representing its key value pairs respectively as <tt>value</tt>.
	 * 
	 * @param headAttributeLst
	 *            List of {@link ProposalHeadAttributes}
	 * @return Return a map of {@link ProposalHead} id as <tt>key</tt> and map
	 *         of {@link ProposalHeadAttributes} id and display name as
	 *         <tt>value</tt>.
	 */
	private Map<Long, Map<Long, String>> getAttributeMap(final List<ProposalHeadAttributes> headAttributeLst) {
		final Map<Long, Map<Long, String>> attributeMap = new HashMap<Long, Map<Long, String>>();
		for (ProposalHeadAttributes attribute : headAttributeLst) {
			if (attributeMap.containsKey(attribute.getProposalHead().getId())) {
				attributeMap.get(attribute.getProposalHead().getId()).put(attribute.getId(), attribute.getDisplayAttributeName());
			} else {
				final Map<Long, String> displayAttributMap = new HashMap<Long, String>();
				displayAttributMap.put(attribute.getId(), attribute.getDisplayAttributeName());
				attributeMap.put(attribute.getProposalHead().getId(), displayAttributMap);
			}
		}
		return attributeMap;
	}
	
	/**
	 * Returns a map containing proposal and line item level data, based on
	 * configured token in the uploaded <tt>Template</tt>. This method sets value
	 * of {@link ProposalHead} and {@link ProposalHeadAttributes} in
	 * {@link TemplateAttributeVO} based on token.
	 * 
	 * @param attributesMap
	 *            TreeMap of {@link TemplateAttributeVO}
	 * @param templateVO
	 *            {@link TemplateVO}
	 * @return map containing proposal and line item level configuration data,
	 *         based on token configured in the uploaded <tt>Template</tt>
	 */
	private TreeMap<String, List<TemplateAttributeVO>> tokenAutoConfig(final TreeMap<String, List<TemplateAttributeVO>> attributesMap, final TemplateVO templateVO) {
		boolean hasProposalAttribute = true;
		boolean hasLineItemAttribute = true;
		final List<ProposalHeadAttributes> headAttributeLst = templateService.getProposalHeadAttributes(templateVO.getTokenSet());
		final Map<Long, Map<Long, String>> attributeMap = getAttributeMap(templateService.getProposalHeadAttributes());
		if (headAttributeLst != null && !headAttributeLst.isEmpty()) {
			for (ProposalHeadAttributes attributeDB : headAttributeLst) {
				if (attributeDB.getProposalHead().isProposalType() && attributesMap.get(ConstantStrings.PROPOSAL_HEADER) != null) {
					for (TemplateAttributeVO attributeVO : attributesMap.get(ConstantStrings.PROPOSAL_HEADER)) {
						configureTokenAttribute(attributeVO, attributeDB, attributeMap);
						hasProposalAttribute = false;
					}
				} else {
					if (attributesMap.get(ConstantStrings.PROPOSAL_LINEITEM) != null) {
						for (TemplateAttributeVO attributeVO : attributesMap.get(ConstantStrings.PROPOSAL_LINEITEM)) {
							configureTokenAttribute(attributeVO, attributeDB, attributeMap);
							hasLineItemAttribute = false;
						}
					}
				}
			}
		} else {
			setAttributeForNonTokenTem(attributesMap, attributeMap);
		}
		//This condition is used when template has auto configuration token for only one i.e. PROPOSAL_HEADER , PROPOSAL_LINEITEM
		if (hasLineItemAttribute || hasProposalAttribute) {
			setAttributeForNonTokenTem(attributesMap, attributeMap);
		}
		return attributesMap;
	}

	/**
	 * Configures the proposal and line item as the {@link ProposalHead} and
	 * their respective {@link ProposalHeadAttributes} in case the uploaded
	 * <tt>Template</tt> does not contain the auto configured token.
	 * 
	 * @param attributesMap
	 * @param attributeMap
	 */
	private void setAttributeForNonTokenTem(final TreeMap<String, List<TemplateAttributeVO>> attributesMap, final Map<Long, Map<Long, String>> attributeMap) {
		if ((attributesMap != null && !attributesMap.isEmpty())) {
			if (attributesMap.get(ConstantStrings.PROPOSAL_HEADER) != null) {
				configureNonTokenAttribute(attributesMap.get(ConstantStrings.PROPOSAL_HEADER), attributeMap);
			}
			if (attributesMap.get(ConstantStrings.PROPOSAL_LINEITEM) != null) {
				configureNonTokenAttribute(attributesMap.get(ConstantStrings.PROPOSAL_LINEITEM), attributeMap);
			}
		}
	}

	/**
	 * Set display name for proposal and line item's {@link ProposalHead} and
	 * {@link ProposalHeadAttributes} attributes when uploaded <tt>Template</tt>
	 * not contain auto configure token.
	 * 
	 * @param attributeVOLst
	 *            List of {@link TemplateAttributeVO}
	 * @param attributeMap
	 *            map of attribute display name.
	 */
	private void configureNonTokenAttribute(final List<TemplateAttributeVO> attributeVOLst, final Map<Long, Map<Long, String>> attributeMap) {
		for (TemplateAttributeVO attributeVO : attributeVOLst) {
			if (StringUtils.isNotBlank(attributeVO.getAttributeType())) {
				attributeVO.setDisplayAttributMap(attributeMap.get(Long.valueOf(attributeVO.getAttributeType())));
			}
		}
	}

	/**
	 * Configures proposal and line item's as {@link ProposalHead} and their
	 * {@link ProposalHeadAttributes} attributes in case the uploaded <tt>Template</tt>
	 * contains auto configured token.
	 * 
	 * @param attributeVO
	 *            {@link TemplateAttributeVO}
	 * @param attributeDB
	 *            {@link ProposalHeadAttributes}
	 * @param attributeMap
	 *            map of attribute display name.
	 */
	private void configureTokenAttribute(final TemplateAttributeVO attributeVO, final ProposalHeadAttributes attributeDB, final Map<Long, Map<Long, String>> attributeMap) {
		if (attributeVO.getAttributeName().equalsIgnoreCase(attributeDB.getAutoConfigKey())) {
			if (StringUtils.isBlank(attributeVO.getAttributeType())) {
				attributeVO.setAttributeType(String.valueOf(attributeDB.getProposalHead().getId()));
			}
			if (attributeVO.getAttributeTypeKeyId() == null && StringUtils.isBlank(attributeVO.getAttributeTypeKey())) {
				attributeVO.setAttributeTypeKey(attributeDB.getDisplayAttributeName());
				attributeVO.setAttributeTypeKeyId(attributeDB.getId());
			}
		}
		if (StringUtils.isNotBlank(attributeVO.getAttributeType())) {
			attributeVO.setDisplayAttributMap(attributeMap.get(Long.valueOf(attributeVO.getAttributeType())));
		}
	}

	/**
	 * Returns the details of a given <tt>Template</tt>.
	 * 
	 * @param templateForm
	 *            {@link TemplateManagementForm} object containing the data of the template whose details has to be fetched.
	 * @return selected <tt>Template</tt> name, file name, proposal level data,
	 *         line item level data font size and use existing row of
	 *         <tt>Template</tt>.
	 */
	@RequestMapping("/getSelectedTemplateData")
	public ModelAndView getSelectedTemplateData(@ModelAttribute(TEMPLATE_FORM) final TemplateManagementForm templateForm) {
		final ModelAndView view = new ModelAndView(TEMPLATE_CONFIG);
		final Map<String, List<TemplateAttributeVO>> attributesMap = new TreeMap<String, List<TemplateAttributeVO>>();
		final TemplateMetaData templateMetaData = templateService.getActiveMediaPlanTemplateById(Long.valueOf(templateForm.getTemplateId()));
		setTemplateSheetData(view, templateMetaData, attributesMap);
		view.addObject(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, attributesMap);
		view.addObject(FILE_NAME, templateMetaData.getTemplateName());
		view.addObject(TEMPLATE_NAME, templateMetaData.getTemplateFileName());
		view.addObject(PROPOSAL_HEAD, getProposalHead(ConstantStrings.PROPOSAL));
		view.addObject(LINEITEM_HEAD, getProposalHead(ConstantStrings.LINEITEM));
		view.addObject(FONT_SIZE, getFontSize());
		view.addObject(USE_EXISTING_ROW, templateMetaData.isUseExistingRow());
		return view;
	}

	/**
	 * Set <tt>Template</tt> sheet name, template sheet id and cell data in
	 * {@link TemplateAttributeVO} object when user select a row from
	 * <tt>Template</tt> grid.
	 * 
	 * @param view
	 *            {@link ModelAndView}
	 * @param templateMetaData
	 *            {@link TemplateMetaData}
	 * @param attributesMap
	 *            map of attribute display name.
	 */
	private void setTemplateSheetData(final ModelAndView view, final TemplateMetaData templateMetaData, final Map<String, List<TemplateAttributeVO>> attributesMap) {
		if (templateMetaData != null) {
			final List<TemplateSheetMetaData> metaDataLst = templateMetaData.getTemplateSheetList();
			final List<ProposalHeadAttributes> headAttributesLst = templateService.getProposalHeadAttributes();
			for (TemplateSheetMetaData templateSheetMetaData : metaDataLst) {
				final List<TemplateMetaDataAttributes> metaDataList = templateSheetMetaData.getMediaPlanAttributes();
				getTemplateAttributeMap(metaDataList, headAttributesLst, attributesMap);
				view.addObject(SHEET_NAME, templateSheetMetaData.getSheetName());
				view.addObject(SHEET_ID, templateSheetMetaData.getSheetId());
			}
		}
	}

	/**
	 * Returns a map which contain {@link ConstantStrings.PROPOSAL_HEADER} and
	 * {@link ConstantStrings.PROPOSAL_LINEITEM} as a key and list of
	 * {@link TemplateAttributeVO} as a value and this list contain proposal and
	 * line item level data, based on token which starts with the specified
	 * prefix.
	 * 
	 * @param metaDataList
	 *            List of {@link TemplateMetaDataAttributes}
	 * @param headAttributesList
	 *            List of {@link ProposalHeadAttributes}
	 * @param attributesMap
	 *            map of attribute display name.
	 * @return map which contain {@link ConstantStrings.PROPOSAL_HEADER} and
	 *         {@link ConstantStrings.PROPOSAL_LINEITEM} as a key and list of
	 *         {@link TemplateAttributeVO} as a value.
	 */
	private Map<String, List<TemplateAttributeVO>> getTemplateAttributeMap(final List<TemplateMetaDataAttributes> metaDataList, final List<ProposalHeadAttributes> headAttributesList,
			final Map<String, List<TemplateAttributeVO>> attributesMap) {
		final Map<Long, Map<Long, String>> attributeMap = getAttributeMap(headAttributesList);
		final List<TemplateAttributeVO> proposalAttributeList = new ArrayList<TemplateAttributeVO>(metaDataList.size());
		final List<TemplateAttributeVO> lineItemAttributeList = new ArrayList<TemplateAttributeVO>(metaDataList.size());
		for (TemplateMetaDataAttributes metaDataAttribute : metaDataList) {
			if (metaDataAttribute.getProposalHeadAttributes() != null && metaDataAttribute.getProposalHeadAttributes().getProposalHead().isProposalType()) {
				proposalAttributeList.add(createTemplateAttributesVO(metaDataAttribute, headAttributesList, attributeMap));
			} else {
				lineItemAttributeList.add(createTemplateAttributesVO(metaDataAttribute, headAttributesList, attributeMap));
			}
		}

		if (proposalAttributeList != null && !proposalAttributeList.isEmpty()) {
			Collections.sort(proposalAttributeList, new TemplateAttributeComparator());
			attributesMap.put(ConstantStrings.PROPOSAL_HEADER, proposalAttributeList);
		}
		if (lineItemAttributeList != null && !lineItemAttributeList.isEmpty()) {
			Collections.sort(lineItemAttributeList, new TemplateAttributeComparator());
			attributesMap.put(ConstantStrings.PROPOSAL_LINEITEM, lineItemAttributeList);
		}
		return attributesMap;
	}

	/**
	 * Returns {@link TemplateAttributeVO} which contain proposal and line item
	 * level data, based on token which starts with the specified prefix.
	 * 
	 * @param metaDataAttribute
	 *            {@link TemplateMetaDataAttributes}
	 * @param proposalHeadAttributesLst
	 *            List of {@link ProposalHeadAttributes}
	 * @return {@link TemplateAttributeVO} which contain proposal and line item
	 *         level data.
	 */
	private TemplateAttributeVO createTemplateAttributesVO(final TemplateMetaDataAttributes metaDataAttribute, final List<ProposalHeadAttributes> proposalHeadAttributesLst,
		final Map<Long, Map<Long, String>> attributeMap) {
		final TemplateAttributeVO templateAttributeVO = new TemplateAttributeVO();
		templateAttributeVO.setTemplateMetaDataAttributesID(metaDataAttribute.getId());
		if (metaDataAttribute.getAttributeName().startsWith(ConstantStrings.TOKEN)) {
			if (metaDataAttribute.getAttributeName().startsWith(ConstantStrings.AMPT_PRO)) {
				templateAttributeVO.setAttributeName(metaDataAttribute.getAttributeName().replace(ConstantStrings.AMPT_PRO, ConstantStrings.EMPTY_STRING));
			} else if (metaDataAttribute.getAttributeName().startsWith(ConstantStrings.AMPT)) {
				templateAttributeVO.setAttributeName(metaDataAttribute.getAttributeName().replace(ConstantStrings.AMPT, ConstantStrings.EMPTY_STRING));
			}
		} else {
			templateAttributeVO.setAttributeName(metaDataAttribute.getAttributeName());
		}
		templateAttributeVO.setColNum(templateAttributeVO.getCellNumberBasedOnCellName(metaDataAttribute.getColNum()));
		templateAttributeVO.setFormat(metaDataAttribute.getFormat());
		templateAttributeVO.setRowNum(metaDataAttribute.getRowNum());
		templateAttributeVO.setFontSize(metaDataAttribute.getFontSize());
		templateAttributeVO.setAttributeType(metaDataAttribute.getProposalHeadAttributes() == null ? ConstantStrings.EMPTY_STRING : 
			String.valueOf(metaDataAttribute.getProposalHeadAttributes().getProposalHead().getId()));
		if (metaDataAttribute.getProposalHeadAttributes() != null && metaDataAttribute.getProposalHeadAttributes().getProposalHead().isProposalType()) {
			templateAttributeVO.setProposalType(true);
		} else {
			templateAttributeVO.setProposalType(false);
		}

		if (metaDataAttribute.getProposalHeadAttributes() != null && StringUtils.isNotBlank(templateAttributeVO.getAttributeType())) {
			templateAttributeVO.setDisplayAttributMap(attributeMap.get(metaDataAttribute.getProposalHeadAttributes().getProposalHead().getId()));
		}

		if (metaDataAttribute.getProposalHeadAttributes() != null && StringUtils.isNotBlank(templateAttributeVO.getAttributeType())) {
			final TemplateJson templateSelectedAttributeTypeData = getAttributeTypeKeyDisplayName(proposalHeadAttributesLst, 
					metaDataAttribute.getProposalHeadAttributes().getAttributeName(), Long.valueOf(templateAttributeVO.getAttributeType()));
			if (templateSelectedAttributeTypeData != null) {
				templateAttributeVO.setAttributeTypeKeyId(templateSelectedAttributeTypeData.getSelectedKey());
				templateAttributeVO.setAttributeTypeKey(templateSelectedAttributeTypeData.getProposalAttribute());
			}
		}
		return templateAttributeVO;
	}

	/**
	 * This method get a <tt>Template</tt> by template id and delete this
	 * <tt>Template</tt> from data base and template directory.
	 * 
	 * @param templateId
	 *            <tt>Template</tt> id.
	 * @return {@link AjaxFormSubmitResponse}
	 */
	@ResponseBody
	@RequestMapping("/deleteCustomTemplate")
	public AjaxFormSubmitResponse deleteCustomTemplate(@RequestParam("templateId") final Long templateId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final TemplateMetaData customTemplateDb = templateService.getActiveMediaPlanTemplateById(templateId);
		if (customTemplateDb != null) {
			templateService.deleteCustomTemplate(customTemplateDb);
			templateGenerator.deleteTemplate(customTemplateDb.getTemplateName());
		}
		return ajaxResponse;
	}

	/**
	 * After validate duplicate <tt>Template</tt> name from data base, this
	 * method save or update a custom <tt>Template</tt> in data base. This
	 * method move custom <tt>Template</tt> file from temporary directory to
	 * Template directory.
	 * 
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param templteForm
	 *            {@link TemplateManagementForm}
	 * @return validation message.
	 * @throws IOException
	 *             If source or destination is invalid
	 * @throws IOException
	 *             If an IO error occurs during copying
	 */
	@ResponseBody
	@RequestMapping("/saveCustomTemplate")
	public XMLResponseForAjaxCall saveCustomTemplate(final HttpServletResponse response, @ModelAttribute(TEMPLATE_FORM) final TemplateManagementForm templteForm) throws IOException {
		final XMLResponseForAjaxCall ajaxResponse = new XMLResponseForAjaxCall(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(TEMPLATE_FORM, templteForm);
		validateTemplateInfoWithDB(templteForm, results);
		new TemplateManagementValidator().validateTemplate(templteForm, results);
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			final TemplateMetaData templateMetaData = templateService.getMediaPlanTemplateByName(templteForm.getFileName());
			List<TemplateJson> templateSheetData = null;
			if (templteForm != null && StringUtils.isNotEmpty(templteForm.getTemplateJsonData())) {
				templateSheetData = convertJsonTOList(templteForm);
				final TemplateMetaData customTemplateVO = getCustomTemplateVO(templteForm, templateMetaData);
				if (customTemplateVO.getTemplateId() == null || templteForm.isEditAction()) {
					templateGenerator.copyFileInTemplateDir(templteForm);
				}
				templateService.saveCustomTemplateMetaData(templateSheetData, customTemplateVO,
						getTemplateSheetMetaData(templateMetaData, templteForm), templteForm.isEditAction());
			}
		}
		return ajaxResponse;
	}

	/**
	 * This method validate duplicate <tt>Template</tt> name from data base when
	 * save or update a custom <tt>Template</tt>.
	 * 
	 * @param templteManagementForm
	 *            {@link TemplateManagementForm}
	 * @param results
	 *            {@link CustomBindingResult}
	 */
	private void validateTemplateInfoWithDB(final TemplateManagementForm templteManagementForm, final CustomBindingResult results) {
		final FilterCriteria filterCriteria = new FilterCriteria("templateFileName", templteManagementForm.getTemplateName(), ConstantStrings.EMPTY_STRING);
		final List<TemplateMetaData> templateMetaDataLst = templateService.getMediaPlanTemplates(filterCriteria, null, null);
		if (templateMetaDataLst != null && !templateMetaDataLst.isEmpty()) {
			final TemplateMetaData templateMetaData = templateMetaDataLst.get(0);
			if (templteManagementForm.getTemplateId() == 0 && StringUtils.equalsIgnoreCase(templteManagementForm.getTemplateName(), templateMetaData.getTemplateFileName())) {
				results.rejectValue(ConstantStrings.TEMPLATE_NAME, ErrorCodes.DuplicateTemplateEntry, ConstantStrings.TEMPLATE_NAME,
						new Object[] { templteManagementForm.getTemplateName() }, UserHelpCodes.DuplicateTemplateName);
			} else {
				if (StringUtils.equalsIgnoreCase(templteManagementForm.getTemplateName(), templateMetaData.getTemplateFileName())
						&& (templteManagementForm.getTemplateId() != templateMetaData.getTemplateId())) {
					results.rejectValue(ConstantStrings.TEMPLATE_NAME, ErrorCodes.DuplicateTemplateEntry, ConstantStrings.TEMPLATE_NAME,
							new Object[] { templteManagementForm.getTemplateName() }, UserHelpCodes.DuplicateTemplateName);
				}
			}
		}
	}

	/**
	 * Returns list of {@link TemplateJson} after deserialize JSON content. This
	 * JSON contains token name, row number, column number, font size, head and
	 * attribute id of a custom <tt>Template</tt>.
	 * 
	 * @param templteManagementForm
	 * @return Return list of {@link TemplateJson} after deserialize JSON content.
	 * @throws IOException in case of access errors
	 */
	private List<TemplateJson> convertJsonTOList(final TemplateManagementForm templteManagementForm) throws IOException {
		final List<TemplateJson> templateSheetData = new ArrayList<TemplateJson>();
		final ObjectMapper objectMapper = new ObjectMapper();
		final JsonNode parentNode = objectMapper.readTree(templteManagementForm.getTemplateJsonData());
		final List<ProposalHeadAttributes> headAttributeList = templateService.getProposalHeadAttributes();
		final Map<Long, String> headAttributeMap = new HashMap<Long, String>();
		for (ProposalHeadAttributes proposalHeadAttributes : headAttributeList) {
			headAttributeMap.put(proposalHeadAttributes.getId(), proposalHeadAttributes.getLookUpHead());
		}
		for (JsonNode jsonNode : parentNode) {
			if (StringUtils.isNotBlank(jsonNode.findValue(ConstantStrings.TOKENNAME).getTextValue())) {
				final TemplateJson templateRowData = new TemplateJson();
				templateRowData.setAttributesId(Long.valueOf(jsonNode.findValue(ConstantStrings.META_DATA_ATTRIBUTES_ID).getTextValue()));
				templateRowData.setTokenName(jsonNode.findValue(ConstantStrings.TOKENNAME).getTextValue());
				templateRowData.setProposalHead(headAttributeMap.get(Long.valueOf(jsonNode.findValue(ConstantStrings.PROPOSAL_ATTRIBUTE).getTextValue())));
				templateRowData.setProposalAttribute(jsonNode.findValue(ConstantStrings.PROPOSAL_ATTRIBUTE).getTextValue());
				templateRowData.setRowNum(jsonNode.findValue(ConstantStrings.ROWNUM).getTextValue());
				templateRowData.setColNum(jsonNode.findValue(ConstantStrings.COLNUM).getTextValue());
				if (StringUtils.isBlank(jsonNode.findValue(ConstantStrings.FONT_SIZE).getTextValue())) {
					templateRowData.setFontSize(null);
				} else {
					templateRowData.setFontSize(Long.valueOf(jsonNode.findValue(ConstantStrings.FONT_SIZE).getTextValue()));
				}
				templateSheetData.add(templateRowData);
			}
		}
		return templateSheetData;
	}

	/**
	 * Returns {@link TemplateJson} which contain display name of attribute and
	 * attribute id.
	 * 
	 * @param headAttributesLst
	 *            List of {@link ProposalHeadAttributes}
	 * @param attributeTypeKey
	 *            Name of {@link ProposalHeadAttributes} attribute
	 * @param attributeType
	 *            Id of {@link ProposalHead}
	 * @return {@link TemplateJson} which contain display name of attribute and
	 *         attribute id.
	 */
	private TemplateJson getAttributeTypeKeyDisplayName(final List<ProposalHeadAttributes> headAttributesLst, final String attributeTypeKey, final Long attributeType) {
		final TemplateJson templateAttributeTypeKeyDisplayName = new TemplateJson();
		for (ProposalHeadAttributes proposalHeadAttributes : headAttributesLst) {
			if (StringUtils.equalsIgnoreCase(proposalHeadAttributes.getAttributeName(), attributeTypeKey)
					&& (attributeType.equals(proposalHeadAttributes.getProposalHead().getId()))) {
				templateAttributeTypeKeyDisplayName.setProposalAttribute(proposalHeadAttributes.getDisplayAttributeName());
				templateAttributeTypeKeyDisplayName.setSelectedKey(proposalHeadAttributes.getId());
			}
		}
		return templateAttributeTypeKeyDisplayName;
	}

	/**
	 * Returns {@link TemplateSheetMetaData} which contain <tt>Template</tt>
	 * sheet name and sheet id. This action performed when system save or edit a
	 * <tt>Template</tt>.
	 * 
	 * @param templateMetaData
	 *            {@link TemplateMetaData}
	 * @return {@link TemplateSheetMetaData} which contain <tt>Template</tt>
	 *         sheet name and sheet id.
	 */
	private TemplateSheetMetaData getTemplateSheetMetaData(final TemplateMetaData templateMetaData, final TemplateManagementForm templteForm) {
		TemplateSheetMetaData sheetMetaData = new TemplateSheetMetaData();
		if (templateMetaData == null) {
			if (StringUtils.isNotBlank(templteForm.getSheetID())) {
				sheetMetaData.setSheetId(Long.valueOf(templteForm.getSheetID()));
			}
			sheetMetaData.setSheetName(templteForm.getSheetName());
		} else {
			final List<TemplateSheetMetaData> templateSheetList = templateMetaData.getTemplateSheetList();
			for (TemplateSheetMetaData templateSheetMetaData : templateSheetList) {
				templateSheetMetaData.setSheetName(templteForm.getSheetName());
				sheetMetaData = templateSheetMetaData;
			}
		}
		return sheetMetaData;
	}

	/**
	 * Returns {@link TemplateMetaData} which contain <tt>Template</tt> name,
	 * file name, template id and list of template sheet's meta data.
	 * 
	 * @param templteForm
	 *            {@link TemplateManagementForm}
	 * @param templateMetaData
	 *            {@link TemplateMetaData}
	 * @return {@link TemplateMetaData} which contain <tt>Template</tt> meta
	 *         data details.
	 */
	private TemplateMetaData getCustomTemplateVO(final TemplateManagementForm templteForm, final TemplateMetaData templateMetaData) {
		final TemplateMetaData customTemplateMetaData = new TemplateMetaData();
		customTemplateMetaData.setTemplateName(templteForm.getFileName());
		customTemplateMetaData.setTemplateFileName(templteForm.getTemplateName());
		if (templteForm.getTemplateId() == 0) {
			customTemplateMetaData.setTemplateId(null);
		} else {
			customTemplateMetaData.setTemplateId(templteForm.getTemplateId());
			customTemplateMetaData.setTemplateSheetList(templateMetaData.getTemplateSheetList());
		}
		customTemplateMetaData.setUseExistingRow(templteForm.isUseExistingRow());
		return customTemplateMetaData;
	}

	/**
	 * Returns {@link Workbook} if uploaded file type is <tt>.xls</tt> or
	 * <tt>.xlsx</tt> otherwise system return {@code null}. This is a high level
	 * representation of a Excel workbook. This is the first object where system
	 * will construct whether they are reading or writing a workbook. It is also
	 * the top level object for creating new sheets/etc.
	 * 
	 * @param fileName
	 *            Name of the <tt>.xlsx</tt> file.
	 * @param inputStream
	 *            An input stream of bytes.
	 * @return {@link Workbook} if uploaded file type is <tt>.xls</tt> or
	 *         <tt>.xlsx</tt> otherwise system return {@code null}.
	 */
	public Workbook getWorkBook(final String fileName, final InputStream inputStream) {
		Workbook uploadedFile = null;
		try {
			if (ConstantStrings.XLS.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.') + 1))) {
				uploadedFile = new HSSFWorkbook(inputStream);
			} else if (ConstantStrings.XLSX.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.') + 1))) {
				uploadedFile = new XSSFWorkbook(inputStream);
			} else {
				return null;
			}
		} catch (IOException ex) {
			return null;
		}
		return uploadedFile;
	}

	/**
	 * Set all the <tt>Template</tt> data in the {@link TableGrid} based on
	 * <tt>Filter Criteria</tt>, <tt>Pagination Criteria</tt> and
	 * <tt>Sorting Criteria</tt>.
	 * 
	 * @param tblGrid
	 *            {@link TableGrid} of {@link TemplateManagementForm}
	 */
	private void setTemplateDataToGrid(final TableGrid<TemplateManagementForm> tblGrid) {
		final List<TemplateMetaData> templateLst = templateService.getMediaPlanTemplates(tblGrid.getFilterCriteria(),
				tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		
		if (templateLst != null && !templateLst.isEmpty()) {
			final List<TemplateManagementForm> templateManagementFormsFormList = convertTemplateDtoToForm(templateLst);
			tblGrid.setGridData(templateManagementFormsFormList, templateService.getMediaPlanTemplatesCount(tblGrid.getFilterCriteria()));
		}
	}

	/**
	 * Returns list of {@link TemplateManagementForm} which contain template
	 * details and this details are displayed in <tt>Template</tt> grid.
	 * 
	 * @param metaDatasLst
	 *            List of {@link TemplateMetaData}
	 * @return list of {@link TemplateManagementForm} which contain
	 *         <tt>Template</tt> details.
	 */
	private List<TemplateManagementForm> convertTemplateDtoToForm(final List<TemplateMetaData> metaDatasLst) {
		final List<TemplateManagementForm> formsList = new ArrayList<TemplateManagementForm>(metaDatasLst.size());
		for (TemplateMetaData templateMetaData : metaDatasLst) {
			final TemplateManagementForm templateForm = new TemplateManagementForm();
			templateForm.populateForm(templateMetaData);
			templateForm.setUseExistingRow(templateMetaData.isUseExistingRow());
			templateForm.setDownloadString("<a href=../templateManagement/downloadtemplatefile.action?templateName=" + templateMetaData.getTemplateName() + ">Download</a>");
			formsList.add(templateForm);
		}
		return formsList;
	}

	/**
	 * Returns a configured custom <tt>Template</tt> from <tt>Template</tt>
	 * directory based on file name.
	 * 
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param templateName
	 *            Name of the <tt>Template</tt>
	 * @throws IOException
	 *             in case of I/O errors.
	 */
	@RequestMapping("/downloadtemplatefile")
	public void downloadFile(final HttpServletResponse response, @RequestParam final String templateName) throws IOException {
		final File file = templateGenerator.getTemplateFileByName(templateName);
		response.setContentType(new MimetypesFileTypeMap().getContentType(file));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	/**
	 * Returns map of head id and display name based on parameter. Based on
	 * parameter this map can contain proposal or line item level data.
	 * 
	 * @param proposalType
	 *            Type of proposal
	 * @return map of head id and display name based on parameter.
	 */
	private Map<Long, String> getProposalHead(final String proposalType) {
		final List<ProposalHead> proposalHeadLst = templateService.getProposalHeadList();
		final Map<Long, String> returnMap = new LinkedHashMap<Long, String>();
		if (proposalHeadLst != null && !proposalHeadLst.isEmpty()) {
			for (ProposalHead proposalHead : proposalHeadLst) {
				if (proposalHead.isProposalType() && StringUtils.equalsIgnoreCase(proposalType, ConstantStrings.PROPOSAL)) {
					returnMap.put(proposalHead.getId(), proposalHead.getDisplayName());
				}
				if (!proposalHead.isProposalType() && StringUtils.equalsIgnoreCase(proposalType, ConstantStrings.LINEITEM)) {
					returnMap.put(proposalHead.getId(), proposalHead.getDisplayName());
				}
			}
		}
		return returnMap;
	}

	/**
	 * Returns a list of font size and the range of font size is stared from
	 * {@link MIN_FINT_SIZE} and goes to {@link MAX_FINT_SIZE}.
	 * 
	 * @return List of font size.
	 */
	private List<Long> getFontSize() {
		final List<Long> returnLst = new ArrayList<Long>();
		for (int font = MIN_FINT_SIZE; font <= MAX_FINT_SIZE; font++) {
			returnLst.add(Long.valueOf(font));
		}
		return returnLst;
	}

	/**
	 * Returns list of {@link TemplateAttributeVO} which contains all attributes
	 * details of a <tt>Template</tt> which are configured in uploaded file.
	 * 
	 * @param templateAttributeList
	 *            List of {@link TemplateAttributeVO}
	 * @param templateId
	 *            <tt>Template</tt> id.
	 * @param headType
	 *            If headType is true then {@link TemplateMetaData} contain proposal data
	 * @return A List of {@link TemplateAttributeVO}.
	 *         {@link TemplateAttributeVO} contains all the attribut's details
	 *         of a <tt>Template</tt>.
	 */
	private List<TemplateAttributeVO> getUpdatedAttributeList(final List<TemplateAttributeVO> templateAttributeList, final long templateId, final boolean headType) {
		final TemplateMetaData templateMetaData = templateService.getActiveMediaPlanTemplateById(Long.valueOf(templateId));
		final List<ProposalHeadAttributes> proposalHeadAttributesLst = templateService.getProposalHeadAttributes();
		final Map<String, TemplateMetaDataAttributes> templateAttributeMap = getTemplateAttributeMap(templateMetaData, headType);
		final List<TemplateAttributeVO> updatedTemplateAttributeList = new ArrayList<TemplateAttributeVO>();
		for (TemplateAttributeVO templateAttributeVO : templateAttributeList) {
			if (templateAttributeMap.containsKey(templateAttributeVO.getAttributeName().toLowerCase())) {
				/*
				 * update token if available in template otherwise delete from table when save data
				 */
				final TemplateMetaDataAttributes metaDataAttributes = templateAttributeMap.get(templateAttributeVO.getAttributeName().toLowerCase());
				updatedTemplateAttributeList.add(setUpdatedTemplateAttribute(templateAttributeVO, metaDataAttributes, proposalHeadAttributesLst));
			} else {
				// add new token case
				updatedTemplateAttributeList.add(templateAttributeVO);
			}
		}
		return updatedTemplateAttributeList;
	}

	/**
	 * Returns {@link TemplateAttributeVO} in this object method sets display
	 * name of attribute name, id and font size.
	 * 
	 * @param templateAttributeVO
	 *            {@link TemplateAttributeVO}
	 * @param metaDataAttributes
	 *            {@link TemplateMetaDataAttributes}
	 * @param headAttributesLst
	 *            List of {@link ProposalHeadAttributes}
	 * @return {@link TemplateAttributeVO} and this contains display name of
	 *         attribute name, id and font size.
	 */
	private TemplateAttributeVO setUpdatedTemplateAttribute(final TemplateAttributeVO templateAttributeVO, final TemplateMetaDataAttributes metaDataAttributes, 
			final List<ProposalHeadAttributes> headAttributesLst) {
		templateAttributeVO.setAttributeType(metaDataAttributes.getProposalHeadAttributes() == null ? ConstantStrings.EMPTY_STRING : 
			String.valueOf(metaDataAttributes.getProposalHeadAttributes().getProposalHead().getId()));
		templateAttributeVO.setFontSize(metaDataAttributes.getFontSize());
		if (metaDataAttributes.getProposalHeadAttributes() != null && StringUtils.isNotBlank(templateAttributeVO.getAttributeType())) {
			final TemplateJson templateSelectedAttributeTypeData = getAttributeTypeKeyDisplayName(headAttributesLst, 
					metaDataAttributes.getProposalHeadAttributes().getAttributeName(), Long.valueOf(templateAttributeVO.getAttributeType()));
			if (templateSelectedAttributeTypeData != null) {
				templateAttributeVO.setAttributeTypeKeyId(templateSelectedAttributeTypeData.getSelectedKey());
				templateAttributeVO.setAttributeTypeKey(templateSelectedAttributeTypeData.getProposalAttribute());
			}
		}
		return templateAttributeVO;
	}

	/**
	 * Based on head type this map contains proposal or line item data and
	 * returns a Map which contains attribute name as key and
	 * {@link TemplateMetaDataAttributes} as value.
	 * 
	 * @param templateMetaData
	 *            {@link TemplateMetaData}
	 * @param headType
	 *            true if {@link TemplateMetaData} contain proposal data
	 * @return A Map which contains attribute name as key and
	 *         {@link TemplateMetaDataAttributes} as value.
	 */
	private Map<String, TemplateMetaDataAttributes> getTemplateAttributeMap(final TemplateMetaData templateMetaData, final boolean headType) {
		final Map<String, TemplateMetaDataAttributes> templateAttributeMap = new HashMap<String, TemplateMetaDataAttributes>();
		final List<TemplateSheetMetaData> templateSheetMetaDataLst = templateMetaData.getTemplateSheetList();
		final List<TemplateMetaDataAttributes> duplicateAttributesLst = new ArrayList<TemplateMetaDataAttributes>();
		final List<String> duplicateAttributesKey = new ArrayList<String>();
		for (TemplateSheetMetaData templateSheetMetaData : templateSheetMetaDataLst) {
			final List<TemplateMetaDataAttributes> templateMetaDataAttributesLst = templateSheetMetaData.getMediaPlanAttributes();
			for (TemplateMetaDataAttributes templateMetaDataAttributes : templateMetaDataAttributesLst) {
				if (templateMetaDataAttributes.getProposalHeadAttributes().getProposalHead().isProposalType() == headType) {
					if (templateAttributeMap.containsKey(templateMetaDataAttributes.getAttributeName().toLowerCase())) {
						duplicateAttributesLst.add(templateMetaDataAttributes);
						duplicateAttributesKey.add(templateMetaDataAttributes.getAttributeName().toLowerCase());
					} else {
						templateAttributeMap.put(templateMetaDataAttributes.getAttributeName().toLowerCase(), templateMetaDataAttributes);
					}
				}
			}
		}
		for (String duplicateAttributeName : duplicateAttributesKey) {
			for (TemplateMetaDataAttributes duplicateAttribute : duplicateAttributesLst) {
				final TemplateMetaDataAttributes attributes = templateAttributeMap.get(duplicateAttributeName);
				if (attributes != null && attributes.getAttributeName().equalsIgnoreCase(duplicateAttribute.getAttributeName())
						&& attributes.getProposalHeadAttributes() != null && duplicateAttribute.getProposalHeadAttributes() != null
						&& attributes.getProposalHeadAttributes().getProposalHead().getId() != duplicateAttribute.getProposalHeadAttributes().getProposalHead().getId()) {
					templateAttributeMap.remove(duplicateAttributeName);
				}
			}
		}
		return templateAttributeMap;
	}

	/**
	 * This method renders <tt>Template Help</tt> view. which contains
	 * <tt>Template</tt> help detail.
	 * 
	 * @return <tt>Template Help</tt> view.
	 */
	@RequestMapping("/getTemplateHelp")
	public ModelAndView getTemplateHelp() {
		final ModelAndView view = new ModelAndView(TEMPLATE_HELP);
		final List<ProposalHead> proposalHeadList = templateService.getProposalHeadList();
		view.addObject("allTemplateHeads", getTemplateHelpPojoLst(proposalHeadList));
		return view;
	}

	/**
	 * Returns list of {@link TemplateHelpPojo}. This contains <tt>Template</tt>
	 * help detail. In {@link TemplateHelpPojo} system sets display name of head
	 * and attribute and auto configuration key.
	 * 
	 * @param proposalHeadList
	 *            List of {@link ProposalHead}
	 * @return list of {@link TemplateHelpPojo} which contains <tt>Template</tt>
	 *         help detail.
	 */
	private List<TemplateHelpPojo> getTemplateHelpPojoLst(final List<ProposalHead> proposalHeadList) {
		final List<TemplateHelpPojo> pojoLst = new ArrayList<TemplateHelpPojo>();
		for (ProposalHead proposalHead : proposalHeadList) {
			for (ProposalHeadAttributes headAttr : proposalHead.getProposalHeadAttributes()) {
				final TemplateHelpPojo pojo = new TemplateHelpPojo();
				pojo.setHead(proposalHead.getDisplayName());
				pojo.setAttribute(headAttr.getDisplayAttributeName());
				if (ConstantStrings.PROPOSAL.equals(proposalHead.getHeadName()) || ConstantStrings.USER.equals(proposalHead.getHeadName())) {
					pojo.setKey(ConstantStrings.AMPT_PRO + headAttr.getAutoConfigKey());
				} else {
					pojo.setKey(ConstantStrings.AMPT + headAttr.getAutoConfigKey());
				}
				pojoLst.add(pojo);
			}
		}
		return pojoLst;
	}

	public void setTemplateGenerator(final TemplateGenerator templateGenerator) {
		this.templateGenerator = templateGenerator;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}
}
