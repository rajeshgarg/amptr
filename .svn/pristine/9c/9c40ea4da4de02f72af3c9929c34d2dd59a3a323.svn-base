/**
 * 
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.AudienceTargetType;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.Tier;
import com.nyt.mpt.domain.TierPremium;
import com.nyt.mpt.domain.TierSectionAssoc;
import com.nyt.mpt.form.TierForm;
import com.nyt.mpt.form.TierPremiumForm;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.service.ITargetingService;
import com.nyt.mpt.service.ITierService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.BehavioralTargetTypeEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.TierValidator;

/**
 * This <code>ManageTierController</code> contains all the methods for managing the Tier and Premium and their related information
 * 
 * @author garima.garg
 */
@Controller
@RequestMapping("/manageTier/*")
public class ManageTierController extends AbstractBaseController {

	private static final String BEHAVIORAL = "Behavioral";

	private static final long ROS_TARGET_TYPEID = 16L;

	private static final String DEFAULT = "Default";

	private static final long SECTION_TARGET_TYPEID = 17L;

	private static final Logger LOGGER = Logger.getLogger(ManageTierController.class);

	private static final String MANAGE_TIER_FORM = "tierForm";

	private static final String MANAGE_TIER_PREMIUM_FORM = "tierPremiumForm";

	private static final String GRID_DATA = "manageTierGridData";

	private static final String PREMIUM_GRID_DATA = "manageTierPremiumGridData";

	private ISalesTargetService salesTargetService;

	private ITierService tierService;

	private ITargetingService targetingService;

	/**
	 * Return model and view for tier detail page
	 * @param manageTierForm
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute(MANAGE_TIER_FORM) final TierForm manageTierForm) {
		final ModelAndView view = new ModelAndView("manageTierPage");
		view.addObject(MANAGE_TIER_FORM, manageTierForm);
		return view;
	}

	/**
	 * Load Tier master grid data
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadTierGridData(@ModelAttribute final TableGrid<TierForm> tblGrid) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setTierDataToGrid(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Set tier data to the table grid
	 * @param tblGrid
	 */
	private void setTierDataToGrid(final TableGrid<TierForm> tblGrid) {
		final List<Tier> tierLst = tierService.getFilteredTierList(tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!tierLst.isEmpty()) {
			final int count = tierService.getFilteredTierCount(tblGrid.getFilterCriteria());
			tblGrid.setGridData(convertTierDtoToForm(tierLst), count);
		}
	}

	/**
	 * Returns the list of all the sales target not mapped to any tier.
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTierSections")
	public AjaxFormSubmitResponse getSectionMap() {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final List<Long> salesTargetTypeIdLst = new ArrayList<Long>();
		salesTargetTypeIdLst.add(SECTION_TARGET_TYPEID);
		salesTargetTypeIdLst.add(ROS_TARGET_TYPEID);
		final List<SalesTarget> salesTargetList = salesTargetService.getActiveSalesTargetBySTTypeId(salesTargetTypeIdLst);
		final List<Tier> tierList = tierService.getFilteredTierList(null, null, null);
		final Map<Long, String> sectionMap = new HashMap<Long, String>(salesTargetList.size());
		final Map<Long, String> tierSectionMap = getTierSectionMap(tierList);

		if (salesTargetList != null && !salesTargetList.isEmpty()) {
			for (SalesTarget salesTarget : salesTargetList) {
				if (!tierSectionMap.containsKey(salesTarget.getSalesTargetId())) {
					sectionMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
				}
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, sectionMap);
		return ajaxResponse;
	}

	/**
	 * Returns the list of sections for a particular tier.
	 * @param tierId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSelectedTierSections")
	public AjaxFormSubmitResponse getSelectedTierSections(@RequestParam(required = true) final long tierId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Tier tier = tierService.getTierById(tierId);
		final List<Tier> tierList = new ArrayList<Tier>();
		tierList.add(tier);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, getTierSectionMap(tierList));
		return ajaxResponse;
	}

	/**
	 * Save or update a tier
	 * @param response
	 * @param tierForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveTier")
	public AjaxFormSubmitResponse saveTier(final HttpServletResponse response, @ModelAttribute(MANAGE_TIER_FORM) final TierForm tierForm) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(MANAGE_TIER_FORM, tierForm);
		new TierValidator().validate(tierForm, results);
		if (StringUtils.isNotBlank(tierForm.getTierName())) {
			final boolean duplicateTierName = tierService.isDuplicateTierName(tierForm.getTierName(), tierForm.getTierId());
			if (duplicateTierName) {
				results.rejectValue("tierName", ErrorCodes.DuplicateTierName, "tierName", new Object[] { tierForm.getTierName() }, UserHelpCodes.HelpDuplicateTierName);
			}
		}
		if (StringUtils.isNotBlank(tierForm.getLevel()) && StringUtils.isNumeric(tierForm.getLevel())) {
			final boolean duplicateTierLevel = tierService.isDuplicateTierLevel(Long.valueOf(tierForm.getLevel()), tierForm.getTierId());
			if (duplicateTierLevel) {
				results.rejectValue("level", ErrorCodes.DuplicateTierLevel, "level", new Object[] { Long.valueOf(tierForm.getLevel()) }, UserHelpCodes.HelpDuplicateTierLevel);
			}
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}

		final List<TierSectionAssoc> tierSecAssocList = new ArrayList<TierSectionAssoc>();
		final List<SalesTarget> salesTargetList = salesTargetService.getSalesTargetListByIDs(StringUtil.convertStringArrayToLongArray(tierForm.getSelectedSectionIds()));

		final Tier tierBean = tierForm.populate(new Tier());
		if (salesTargetList != null && !salesTargetList.isEmpty()) {
			for (SalesTarget salesTarget : salesTargetList) {
				final TierSectionAssoc tierSec = new TierSectionAssoc();
				tierSec.setSectionId(salesTarget.getSalesTargetId());
				tierSec.setSectionName(salesTarget.getSalesTargeDisplayName());
				tierSec.setTier(tierBean);
				tierSecAssocList.add(tierSec);
			}
		}
		tierBean.setTierSectionAssocLst(tierSecAssocList);
		final Tier tierCreated = tierService.saveTier(tierBean);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, tierCreated.getTierId());
		return ajaxResponse;
	}

	/**
	 * Delete the selected tier from the data base
	 * @param tierId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTier")
	public long deleteTier(@RequestParam(required = true) final long tierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Tier. Id: " + tierId);
		}
		return tierService.deleteTier(tierId);
	}

	/**
	 * Return model and view for Premium detail page
	 * @param tierPremiumForm
	 * @return
	 */
	@RequestMapping("/viewPremiumDetail")
	public ModelAndView displayPremiumPage(@ModelAttribute(MANAGE_TIER_PREMIUM_FORM) final TierPremiumForm tierPremiumForm) {
		final ModelAndView view = new ModelAndView("manageTierPremiumPage");
		view.addObject(MANAGE_TIER_PREMIUM_FORM, tierPremiumForm);
		view.addObject("targetTypes", targetingService.getTargetTypeCriteria());
		return view;
	}

	/**
	 * Load Premium master grid data
	 * @param tblGrid
	 * @param tierId
	 * @return
	 */
	@RequestMapping("/loadPremiumGridData")
	public ModelAndView loadPremiumGridData(@ModelAttribute final TableGrid<TierPremiumForm> tblGrid, @RequestParam final long tierId) {
		final ModelAndView view = new ModelAndView(PREMIUM_GRID_DATA);
		this.setPremiumDataToGrid(tblGrid, tierId);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Saves/Updates a Premium in the AMPT database
	 * @param response
	 * @param tierPremiumForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveTierPremium")
	public AjaxFormSubmitResponse saveTierPremium(final HttpServletResponse response, @ModelAttribute(MANAGE_TIER_PREMIUM_FORM) final TierPremiumForm tierPremiumForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Tier. Tier Form: " + tierPremiumForm);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(MANAGE_TIER_PREMIUM_FORM, tierPremiumForm);
		new TierValidator().validate(tierPremiumForm, results);

		if (StringUtils.isNotBlank(tierPremiumForm.getHidTargetTypeId())) {
			final TierPremium tierPremium = new TierPremium();
			final AudienceTargetType targetType = new AudienceTargetType();
			targetType.setSosAudienceTargetTypeId(Long.valueOf(tierPremiumForm.getHidTargetTypeId()));
			tierPremium.setTargetType(targetType);

			final Tier tierObj = new Tier();
			tierObj.setTierId(tierPremiumForm.getPremiumTierId());

			tierPremium.setTierObj(tierObj);
			tierPremium.setId(tierPremiumForm.getId());
			tierPremium.setTarTypeElementId(StringUtils.join(tierPremiumForm.getTargetTypeElements(), ConstantStrings.COMMA));
			final StringBuilder duplicateElementsStr = new StringBuilder();
			if (tierService.isDuplicatePremiumElement(tierPremium, duplicateElementsStr)) {
				final Map<Long, String> targetElementsNames = targetingService.getTargetTypeElement(tierPremium.getTargetType().getSosAudienceTargetTypeId());
				final Map<String, String> behavioralTargetElementsNames = new HashMap<String, String>();
				if (BEHAVIORAL.equalsIgnoreCase(tierPremiumForm.getTargetName())) {
					for (BehavioralTargetTypeEnum elements : BehavioralTargetTypeEnum.values()) {
						behavioralTargetElementsNames.put(elements.getResourceName().toUpperCase(), elements.getResourceName());
					}
				}
				String duplicateEle = ConstantStrings.EMPTY_STRING;
				if (!DEFAULT.equalsIgnoreCase(duplicateElementsStr.toString())) {
					final String duplicateEleStr = duplicateElementsStr.toString().trim().substring(0, duplicateElementsStr.toString().trim().length() - 1);
					final String[] duplicateElements = duplicateEleStr.split(ConstantStrings.COMMA);
					for (String element : duplicateElements) {
						if (BEHAVIORAL.equalsIgnoreCase(tierPremiumForm.getTargetName())) {
							duplicateEle += behavioralTargetElementsNames.get(element.trim()) + ", ";
						} else {
							duplicateEle += targetElementsNames.get(Long.valueOf(element.trim())) + ", ";
						}
					}
					duplicateEle = duplicateEle.trim().substring(0, duplicateEle.trim().length() - 1);
				} else {
					duplicateEle = duplicateElementsStr.toString();
				}

				results.rejectValue("targetTypeElements_custom", ErrorCodes.DuplicatePremiumElement, "targetTypeElements_custom", new Object[] { duplicateEle },
						UserHelpCodes.HelpDuplicatePremiumElement);
			}
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}

		final TierPremium tierPremiumBean = tierPremiumForm.populate(new TierPremium());
		final Tier tierCreated = tierService.saveTierPremium(tierPremiumBean, tierPremiumForm.getPremiumTierId());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, tierCreated.getTierId());
		return ajaxResponse;
	}

	/**
	 * Delete the selected tier from the data base
	 * @param premiumRowId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTierPremium")
	public long deleteTierPremium(@RequestParam(required = true) final long premiumRowId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Tier Premium. Id: " + premiumRowId);
		}
		return tierService.deleteTierPremium(premiumRowId);
	}

	/**
	 * Returns the list of sections for a particular tier.
	 * @param targetTypeId
	 * @param targetName
	 * @param tierId
	 * @param premiumId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSelectedTargetTypeElements")
	public AjaxFormSubmitResponse getSelectedTargetTypeElements(@RequestParam(required = true) final long targetTypeId, @RequestParam(required = true) final String targetName,
			@RequestParam(required = true) final long tierId, @RequestParam(required = true) final String premiumId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final List<TierPremium> premiumList = tierService.getPremiumListByTierId(tierId);
		String selectedElements = ConstantStrings.EMPTY_STRING;
		for (TierPremium tierPremium : premiumList) {
			if (tierPremium.getTargetType().getSosAudienceTargetTypeId() == targetTypeId && (StringUtils.isBlank(premiumId) || tierPremium.getId() != Long.valueOf(premiumId))
					&& (StringUtils.isNotBlank(tierPremium.getTarTypeElementId()))) {
				selectedElements = selectedElements + tierPremium.getTarTypeElementId();
				selectedElements = selectedElements + ConstantStrings.COMMA;
			}
		}

		if (BEHAVIORAL.equalsIgnoreCase(targetName.trim())) {
			final Map<String, String> targetElementsMap = new HashMap<String, String>();

			for (BehavioralTargetTypeEnum elements : BehavioralTargetTypeEnum.values()) {
				targetElementsMap.put(elements.name(), elements.getResourceName());
			}
			if (StringUtils.isNotBlank(selectedElements)) {
				for (String elementId : selectedElements.split(ConstantStrings.COMMA)) {
					if (StringUtils.isNotBlank(elementId) && targetElementsMap.containsKey(elementId)) {
						targetElementsMap.remove(elementId);
					}
				}
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, targetElementsMap);
		} else {
			final Map<Long, String> returnMap = targetingService.getTargetTypeElement(targetTypeId);
			if (StringUtils.isNotBlank(selectedElements)) {
				for (String elementId : selectedElements.split(ConstantStrings.COMMA)) {
					if (StringUtils.isNotBlank(elementId) && returnMap.containsKey(Long.valueOf(elementId))) {
						returnMap.remove(Long.valueOf(elementId));
					}
				}
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnMap);
		}
		return ajaxResponse;
	}

	/**
	 * Returns the Map containing the tier Id and tier name of all the tiers created
	 * @param tierId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTierNames")
	public AjaxFormSubmitResponse getTierNames(@RequestParam final long tierId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final List<Tier> tierLst = tierService.getFilteredTierList(null, null, null);
		if (!tierLst.isEmpty()) {
			final Map<Long, String> tierNames = new HashMap<Long, String>(tierLst.size());
			for (Tier tier : tierLst) {
				if (!tier.getTierId().equals(Long.valueOf(tierId))) {
					tierNames.put(tier.getTierId(), tier.getTierName());
				}
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, tierNames);
		}
		return ajaxResponse;
	}

	/**
	 * Check if Premium exist for given Tier Id or not
	 * @param targetTierId
	 * @param fromTierId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkPremiumAvailabilityForTier")
	public AjaxFormSubmitResponse checkPremiumAvailabilityForTier(final long targetTierId, final long fromTierId) {
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		List<TierPremium> premiumList = tierService.getPremiumListByTierId(fromTierId);
		if (premiumList.isEmpty()) {
			ajxResponse.getObjectMap().put("premiumDataNotExistInSrcTier", premiumList.isEmpty());
		} else {
			premiumList = tierService.getPremiumListByTierId(targetTierId);
			ajxResponse.getObjectMap().put("premiumDataExistInTargetTier", !premiumList.isEmpty());
		}
		return ajxResponse;
	}

	/**
	 * Copy Rate Profile(s) from source Sales Category to target Sales Category
	 * @param sourceTierId
	 * @param targetTierId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyPremiums")
	public AjaxFormSubmitResponse copyPremiums(final long sourceTierId, final long targetTierId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Cloning Premiums with tier id: " + sourceTierId);
		}
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		tierService.createClonePremiumForTier(sourceTierId, targetTierId);
		return ajxResponse;
	}

	/**
	 * Set tier data to the table grid
	 * @param tblGrid
	 * @param tierId
	 */
	private void setPremiumDataToGrid(final TableGrid<TierPremiumForm> tblGrid, final long tierId) {
		final List<TierPremium> premiumList = tierService.getFilteredTierPremiumList(tierId, tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!premiumList.isEmpty()) {
			tblGrid.setGridData(convertPremiumDtoToForm(premiumList), tierService.getFilteredTierPremiumCount(tierId, null));
		}
	}

	/**
	 * Convert tier entity to tier form
	 * @param premiumLst
	 * @return
	 */
	private List<TierPremiumForm> convertPremiumDtoToForm(final List<TierPremium> premiumLst) {
		final List<TierPremiumForm> tierFormList = new ArrayList<TierPremiumForm>(premiumLst.size());
		for (TierPremium tierPremium : premiumLst) {
			final TierPremiumForm tierForm = new TierPremiumForm();
			tierForm.populateForm(tierPremium);

			if (tierPremium.getTarTypeElementId() != null) {

				if (BEHAVIORAL.equalsIgnoreCase(tierPremium.getTargetType().getName())) {
					final Map<String, String> tarElementsMap = new HashMap<String, String>();
					for (BehavioralTargetTypeEnum elements : BehavioralTargetTypeEnum.values()) {
						final String elementName = elements.toString();
						tarElementsMap.put(elementName, elements.getResourceName());
					}
					final String[] elementIds = tierPremium.getTarTypeElementId().split(ConstantStrings.COMMA);
					final StringBuilder elementNames = new StringBuilder();
					for (String elementId : elementIds) {
						elementId = elementId.trim();
						if (tarElementsMap.containsKey(elementId)) {
							elementNames.append(tarElementsMap.get(elementId) + ConstantStrings.COMMA);
						}
					}
					tierForm.setTargetElements(elementNames.substring(0, elementNames.length() - 1).toString());
				} else {
					final Map<Long, String> targetElementsMap = targetingService.getTargetTypeElement(tierPremium.getTargetType().getSosAudienceTargetTypeId());
					final String[] elementIds = tierPremium.getTarTypeElementId().split(ConstantStrings.COMMA);
					final StringBuilder elementNames = new StringBuilder();
					for (String elementId : elementIds) {
						elementId = elementId.trim();
						if (targetElementsMap.containsKey(Long.valueOf(elementId))) {
							elementNames.append(targetElementsMap.get(Long.valueOf(elementId)) + ConstantStrings.COMMA);
						}
					}
					if (elementNames != null && elementNames.length() > 0) {
						tierForm.setTargetElements(elementNames.substring(0, elementNames.length() - 1).toString());
					}
				}
			} else {
				tierForm.setTargetElements(DEFAULT);
			}
			tierFormList.add(tierForm);
		}
		return tierFormList;
	}

	/**
	 * Returns the map containing the sections Id as key and section name as
	 * value corresponding to the list of tiers passed as an argument.
	 * @param tierList : The list of tier beans.
	 * @return
	 */
	private Map<Long, String> getTierSectionMap(final List<Tier> tierList) {
		final Map<Long, String> tierSectionMap = new HashMap<Long, String>();
		if (tierList != null && !tierList.isEmpty()) {
			for (Tier tier : tierList) {
				final List<TierSectionAssoc> tierSectionList = tier.getTierSectionAssocLst();
				if (tierSectionList != null && !tierSectionList.isEmpty()) {
					for (TierSectionAssoc tierSectionAssoc : tierSectionList) {
						tierSectionMap.put(tierSectionAssoc.getSectionId(), tierSectionAssoc.getSectionName());
					}
				}
			}
		}
		return tierSectionMap;
	}

	/**
	 * Convert tier entity to tier form
	 * @param tierLst
	 * @return
	 */
	private List<TierForm> convertTierDtoToForm(final List<Tier> tierLst) {
		final List<TierForm> tierFormList = new ArrayList<TierForm>();
		for (Tier tier : tierLst) {
			final TierForm tierForm = new TierForm();
			tierForm.populateForm(tier);
			tierFormList.add(tierForm);
		}
		return tierFormList;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setTierService(final ITierService tierService) {
		this.tierService = tierService;
	}

	public void setTargetingService(final ITargetingService targetingService) {
		this.targetingService = targetingService;
	}
}
