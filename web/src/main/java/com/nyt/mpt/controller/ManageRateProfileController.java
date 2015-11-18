/**
 * 
 */
package com.nyt.mpt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.CronJobSchedule;
import com.nyt.mpt.domain.RateConfig;
import com.nyt.mpt.domain.RateProfile;
import com.nyt.mpt.domain.RateProfileSeasonalDiscounts;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.form.RateProfileDiscountsForm;
import com.nyt.mpt.form.RateProfileForm;
import com.nyt.mpt.service.ICronJobService;
import com.nyt.mpt.service.IRateProfileService;
import com.nyt.mpt.service.ISalesTargetService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.CronJobNameEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;
import com.nyt.mpt.validator.RateProfileValidator;

/**
 * This <code>ManageRateProfileController</code> contains all the methods used to Manage the Rate Profiles
 * 
 * @author sachin.ahuja
 */
@Controller
@RequestMapping("/manageRateProfile/*")
public class ManageRateProfileController extends AbstractBaseController {

	private static final Logger LOGGER = Logger.getLogger(ManageRateProfileController.class);

	private static final String RATE_PROFILE_FORM = "rateProfileForm";

	private static final long ROS_TARGET_TYPEID = 16L;

	private static final long SECTION_TARGET_TYPEID = 17L;

	private IRateProfileService rateProfileService;

	private ISalesTargetService salesTargetService;

	private ProposalHelper proposalHelper;

	private ICronJobService cronJobService;

	/**
	 * Returns model and view for Manage the Rate Profiles
	 * @param rateProfileForm
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute(RATE_PROFILE_FORM) final RateProfileForm rateProfileForm) {
		final ModelAndView view = new ModelAndView("rateProfileDetailPage");
		view.addObject(RATE_PROFILE_FORM, rateProfileForm);
		view.addObject("allSalesCategory", proposalHelper.getSalesCategory());
		view.addObject("allProducts", proposalHelper.getAllProductsMap());
		return view;
	}

	/**
	 * Loads master grid data for manage list rate profile
	 * @param tblGrid
	 * @param salesCategoryId
	 * @return
	 */
	@RequestMapping("/loadGridData")
	public ModelAndView loadGridData(@ModelAttribute final TableGrid<RateProfileForm> tblGrid, @RequestParam(required = true) final Long salesCategoryId) {
		final ModelAndView view = new ModelAndView("rateProfileGridData");
		setRateProfileDataToGrid(tblGrid, salesCategoryId);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Returns list of all active sales target for given product id
	 * @param productID
	 * @param salesCategoryId
	 * @param rateProfileId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSalesTargetForProduct")
	public AjaxFormSubmitResponse getSalesTargetFromProductID(@RequestParam(required = true) final Long productID, @RequestParam(required = true) final Long salesCategoryId,
			@RequestParam(required = true) final Long rateProfileId) {
		final AjaxFormSubmitResponse ajxSubmitResp = new AjaxFormSubmitResponse(getMessageSource());
		final Map<Long, String> salesTargetMap = proposalHelper.getAllSalesTargetForProductID(productID);
		ajxSubmitResp.getObjectMap().put("productSalesTargets", salesTargetMap);
		/**
		 * Following code is used to remove already selected sales Targets -- Start
		 */
		final List<RateProfile> rateProfileLst = rateProfileService.getRateProfilesBySalesCategory(salesCategoryId);
		for (RateProfile rateProfile : rateProfileLst) {
			if (rateProfile.getProfileId() != rateProfileId && rateProfile.getProductId() == productID) {
				for (RateConfig rateConfig : rateProfile.getRateConfigSet()) {
					if (salesTargetMap.containsKey(rateConfig.getSalesTargetId())) {
						salesTargetMap.remove(rateConfig.getSalesTargetId());
					}
				}
			}
		}
		/**
		 * Code to remove already selected sales Targets -- Ends
		 */
		final Set<Long> saleTargetIds = salesTargetMap.keySet();
		if (salesTargetMap != null && !salesTargetMap.isEmpty()) {
			final Map<Long, Long> childSaleTargetList = proposalHelper.getParentSalesTargetId(new ArrayList<Long>(saleTargetIds));
			ajxSubmitResp.getObjectMap().put("childSaleTarget", childSaleTargetList);
		}
		return ajxSubmitResp;
	}

	/**
	 * Sets list rate data to the table grid
	 * @param tblGrid
	 * @param salesCategoryId
	 */
	private void setRateProfileDataToGrid(final TableGrid<RateProfileForm> tblGrid, final Long salesCategoryId) {
		final List<FilterCriteria> filterCriteria = new ArrayList<FilterCriteria>();
		filterCriteria.add(tblGrid.getFilterCriteria());

		final FilterCriteria criteria = new FilterCriteria();
		criteria.setSearchField("salesCategoryId");
		criteria.setSearchOper(SearchOption.EQUAL.toString());
		criteria.setSearchString(String.valueOf(salesCategoryId));
		filterCriteria.add(criteria);
		
		final List<RateProfile> rateProfileList = rateProfileService.getFilteredRateProfileList(
				filterCriteria, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!rateProfileList.isEmpty()) {
			final int count = rateProfileService.getFilteredRateProfileListCount(filterCriteria);
			tblGrid.setGridData(convertRateProfileDtoToForm(rateProfileList), count);
		}
	}

	/**
	 * Converts rate profile entity to rate profile form
	 * @param rateList
	 * @return
	 */
	private List<RateProfileForm> convertRateProfileDtoToForm(final List<RateProfile> rateList) {
		final List<RateProfileForm> rateFormList = new ArrayList<RateProfileForm>();
		for (RateProfile listRateProfile : rateList) {
			final RateProfileForm rateProfileForm = new RateProfileForm();
			rateProfileForm.populateForm(listRateProfile);
			rateFormList.add(rateProfileForm);
		}
		return rateFormList;
	}

	/**
	 * Saves/Updates Rate Profile
	 * @param response
	 * @param rateProfileForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveRateProfile")
	public AjaxFormSubmitResponse saveRateProfile(final HttpServletResponse response, @ModelAttribute(RATE_PROFILE_FORM) final RateProfileForm rateProfileForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Rate Proflie for profile Id - " + rateProfileForm.getProfileId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(RATE_PROFILE_FORM, rateProfileForm);
		Set<RateProfileDiscountsForm> discountSet = null;
		new RateProfileValidator().validate(rateProfileForm, results);
		if (StringUtils.isNotBlank(rateProfileForm.getRateProfileDiscountData())) {
			discountSet = convertJsonToObject(rateProfileForm.getRateProfileDiscountData());
			new RateProfileValidator().validateDiscounts(discountSet, results);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		}

		RateProfile rateProfile = rateProfileForm.populate(new RateProfile());
		if (discountSet != null) {
			final List<RateProfileSeasonalDiscounts> discountLst = new ArrayList<RateProfileSeasonalDiscounts>();
			for (RateProfileDiscountsForm discountForm : discountSet) {
				final RateProfileSeasonalDiscounts discount = discountForm.populate(new RateProfileSeasonalDiscounts());
				discount.setRateProfile(rateProfile);
				discountLst.add(discount);
			}
			rateProfile.setSeasonalDiscountsLst(discountLst);
		}
		final boolean duplicateProfile = rateProfileService.isDuplicateRateProfile(rateProfile, StringUtil.convertStringArrayToLongArray(rateProfileForm.getSalesTargetId()));
		if (duplicateProfile) {
			results.rejectValue("productId_select2", ErrorCodes.DuplicateRateProfile, "productId_select2", new Object[] {}, UserHelpCodes.DuplicateRateProfile);
			results.rejectValue("salesTargetId_custom", ErrorCodes.DuplicateRateProfile, "salesTargetId_custom", new Object[] {}, UserHelpCodes.DuplicateRateProfile);
		}

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			populateListConfigData(salesTargetService.getSalesTargetListByIDs(StringUtil.convertStringArrayToLongArray(rateProfileForm.getSalesTargetId())), rateProfile);

			if (rateProfile.getProfileId() == 0) {
				rateProfile = rateProfileService.createRateProfile(rateProfile);
			} else {
				rateProfile = rateProfileService.updateRateProfile(rateProfile, rateProfileForm.isForceUpdate());
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, rateProfile.getSectionNames());
		}
		return ajaxResponse;
	}

	/**
	 * Check if Rate Profile exist for given Sales Category Id or not
	 * @param salesCategoryId
	 * @param fromSalesCategoryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getRateProfilesForSalesCategory")
	public AjaxFormSubmitResponse getRateProfilesForSalesCategory(final Long salesCategoryId, final Long fromSalesCategoryId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finding Rate Profile with Sales Category id: " + salesCategoryId);
		}
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		List<RateProfile> rateProfileList = rateProfileService.getRateProfilesBySalesCategory(fromSalesCategoryId);
		if (rateProfileList.isEmpty()) {
			ajxResponse.getObjectMap().put("fromRateProfileDataNotExist", Boolean.valueOf(rateProfileList.isEmpty()));
		} else {
			rateProfileList = rateProfileService.getRateProfilesBySalesCategory(salesCategoryId);
			ajxResponse.getObjectMap().put("rateProfileDataExist", Boolean.valueOf(!rateProfileList.isEmpty()));
		}
		return ajxResponse;
	}

	/**
	 * Copy Rate Profile(s) from source Sales Category to target Sales Category
	 * @param sourceSalesCategoryId
	 * @param targetSalesCategoryId
	 * @param targetSalesCategoryName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyRateProfiles")
	public AjaxFormSubmitResponse copyRateProfiles(final Long sourceSalesCategoryId, final Long targetSalesCategoryId, final String targetSalesCategoryName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Cloning Rate Profile with Sales Category id: " + sourceSalesCategoryId);
		}
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		rateProfileService.createCloneRateProfileForSalesCategory(sourceSalesCategoryId, targetSalesCategoryId, targetSalesCategoryName);
		return ajxResponse;
	}

	/**
	 * Deletes Rate Profile from AMPT database
	 * @param profileId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteRateProfile")
	public long deleteRateProfile(@RequestParam(required = true) final long profileId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Rate Profile. Id:" + profileId);
		}
		final RateProfile rateProfile = new RateProfile();
		rateProfile.setProfileId(profileId);
		return rateProfileService.deleteRateProfile(rateProfile);
	}

	/**
	 * This is to Trigger Section weight calculation job
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/scheduleSectionWeightCalJob")
	public AjaxFormSubmitResponse scheduleSectionWeightCalJob(final HttpServletRequest request) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Date date = DateUtil.getTomorrowMidnightDate();

		final CronJobSchedule jobSchedule = new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date);
		jobSchedule.setScheduledBy(SecurityUtil.getUser().getFullName());
		cronJobService.addNewJobSchedule(jobSchedule);
		ajaxResponse.getObjectMap().put("jobScheduledOn", DateUtil.getGuiDateTimeString(date.getTime()));
		return ajaxResponse;
	}

	/**
	 * Checks for the Scheduled Section Weight Job and Prompt for the user action in 'Yes' or 'No'
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkScheduledSectionWeightCalJob")
	public AjaxFormSubmitResponse checkScheduledSectionWeightCalJob(final HttpServletRequest request) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Date date = DateUtil.getTomorrowMidnightDate();

		/**
		 * Validate tomorrow date is 1st day of quarter, because weight
		 * calculation job is already scheduled for the day
		 */
		if (DateUtil.isFirstDayOfQuarter(date)) {
			ajaxResponse.getObjectMap().put("jobScheduled", true);
			ajaxResponse.getObjectMap().put("nextFireTime", DateUtil.getGuiDateTimeString(date.getTime()));
			return ajaxResponse;
		}

		/**
		 * Fetch manual scheduled weight calculation job from database
		 */
		final CronJobSchedule jobScheduleDB = cronJobService.getScheduledJob(new CronJobSchedule(CronJobNameEnum.SECTION_WEIGHT_CALCULATION.name(), date));
		if (jobScheduleDB != null) {
			ajaxResponse.getObjectMap().put("jobScheduled", true);
			ajaxResponse.getObjectMap().put("jobScheduledBy", jobScheduleDB.getScheduledBy());
			ajaxResponse.getObjectMap().put("nextFireTime", DateUtil.getGuiDateTimeString(date.getTime()));
			return ajaxResponse;
		}

		/**
		 * Weight calculation job is not scheduled yet
		 */
		ajaxResponse.getObjectMap().put("jobScheduled", false);
		ajaxResponse.getObjectMap().put("nextFireTime", DateUtil.getGuiDateTimeString(date.getTime()));
		return ajaxResponse;
	}

	/**
	 * Populates Rate Config data
	 * @param salestargetLst
	 * @param bean
	 */
	private void populateListConfigData(final List<SalesTarget> salestargetLst, final RateProfile bean) {
		final List<String> sectionNames = new ArrayList<String>();
		boolean isRosSelectected = false;
		for (SalesTarget salesTarget : salestargetLst) {
			final RateConfig rateConfig = new RateConfig();
			rateConfig.setSalesTargetId(salesTarget.getSalesTargetId());
			rateConfig.setSalesTargetName(salesTarget.getSalesTargeDisplayName());
			bean.addListRateConfig(rateConfig);
			if (salesTarget.getSalesTargetType().getSalestargetTypeId() == SECTION_TARGET_TYPEID && !isRosSelectected) {
				sectionNames.add(salesTarget.getSalesTargeDisplayName());
			} else if (salesTarget.getSalesTargetType().getSalestargetTypeId() == ROS_TARGET_TYPEID) {
				sectionNames.clear();
				sectionNames.add(salesTarget.getSalesTargeDisplayName());
				isRosSelectected = true;
			}
		}
		if (!sectionNames.isEmpty()) {
			Collections.sort(sectionNames);
			bean.setSectionNames(StringUtils.join(sectionNames, ", "));
		} else {
			bean.setSectionNames(ConstantStrings.EMPTY_STRING);
		}
	}

	/**
	 * Fetches all the seasonal discounts of a given profile.
	 * @param rateProfileId - The profile Id whose discounts has to be fetched.
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProfileDiscounts")
	public AjaxFormSubmitResponse getProfileDiscounts(@RequestParam(required = true) final long rateProfileId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final List<RateProfileSeasonalDiscounts> discountFormLst = rateProfileService.getProfileSeasonalDiscountLst(rateProfileId);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, convertObjectToJson(discountFormLst));
		return ajaxResponse;
	}

	/**
	 * Converts the JSON String of the discount into the discount form object.
	 * @param jsonString
	 * @return
	 */
	public static Set<RateProfileDiscountsForm> convertJsonToObject(final String jsonString) {
		final Set<RateProfileDiscountsForm> discountSet = new LinkedHashSet<RateProfileDiscountsForm>();
		final ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		try {
			node = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing discounts of a List Rate Profile");
			}
		} catch (IOException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while parsing Targeting of a List Rate Profile");
			}
		}
		for (JsonNode discount : node) {
			final RateProfileDiscountsForm seasonalDiscount = new RateProfileDiscountsForm();
			seasonalDiscount.setStartDate(discount.findValue("startDate").getTextValue());
			seasonalDiscount.setEndDate(discount.findValue("endDate").getTextValue());
			seasonalDiscount.setDiscount(discount.findValue("discount").getTextValue());
			seasonalDiscount.setNot(discount.findValue("not").getValueAsBoolean());
			seasonalDiscount.setRowIndex(Integer.valueOf(discount.findValue("rowIndex").getTextValue()));
			seasonalDiscount.setDiscountSeqNo(discount.findValue("discountSeqNo").getTextValue());
			discountSet.add(seasonalDiscount);
		}
		return discountSet;
	}

	/**
	 * Converts the discount form object into the JSON object.
	 * @param discountLst
	 * @return
	 */
	public String convertObjectToJson(final List<RateProfileSeasonalDiscounts> discountLst) {
		List<RateProfileDiscountsForm> discountFormLst = null;
		String returnString = ConstantStrings.EMPTY_STRING;
		final ObjectMapper objectMapper = new ObjectMapper();
		if (discountLst != null && !discountLst.isEmpty()) {
			discountFormLst = getDiscountFormList(discountLst);
			try {
				returnString = objectMapper.writeValueAsString(discountFormLst);
			} catch (JsonGenerationException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			} catch (JsonMappingException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			} catch (IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Error while creating Json for ADGroups for UI");
				}
			}
		}
		return returnString;
	}

	/**
	 * Return list of populated RateProfileDiscountsForm
	 * @param discountLst
	 * @return
	 */
	private List<RateProfileDiscountsForm> getDiscountFormList(final List<RateProfileSeasonalDiscounts> discountLst) {
		final List<RateProfileDiscountsForm> discountFormLst = new ArrayList<RateProfileDiscountsForm>();
		for (RateProfileSeasonalDiscounts discount : discountLst) {
			final RateProfileDiscountsForm discountForm = new RateProfileDiscountsForm();
			discountForm.populateForm(discount);
			discountFormLst.add(discountForm);
		}
		return discountFormLst;
	}

	/**
	 * Convert rate profile entity to rate profile form
	 * @param rateList
	 * @param profileId
	 * @return
	 */
	private List<RateProfileForm> convertRateProfileDtoToForm(final List<RateProfile> rateList, final long profileId) {
		final List<RateProfileForm> rateFormList = new ArrayList<RateProfileForm>();
		for (RateProfile listRateProfile : rateList) {
			if (listRateProfile.getProfileId() != profileId) {
				final RateProfileForm rateProfileForm = new RateProfileForm();
				rateProfileForm.populateForm(listRateProfile);
				rateFormList.add(rateProfileForm);
			}
		}
		return rateFormList;
	}

	/**
	 * Return model and view for list rate profile clone discount page
	 * @param rateProfileForm
	 * @param salesCategoryId
	 * @param profileId
	 * @param productId
	 * @return
	 */
	@RequestMapping("/getRateProfileData")
	public ModelAndView getRateProfileData(@ModelAttribute(RATE_PROFILE_FORM) final RateProfileForm rateProfileForm, @RequestParam(required = true) final Long salesCategoryId,
			@RequestParam(required = true) final Long profileId, @RequestParam(required = true) final Long productId) {
		final ModelAndView view = new ModelAndView("cloneDiscountDetailPage");
		view.addObject(RATE_PROFILE_FORM, rateProfileForm);
		view.addObject("allSalesCategory", proposalHelper.getSalesCategory());
		view.addObject("allProducts", proposalHelper.getAllProductsMap());
		view.addObject("rateProfileList", convertRateProfileDtoToForm(rateProfileService.getRateProfilesBySalesCategoryAndProduct(salesCategoryId, productId), profileId));
		return view;
	}

	/**
	 * Return model and view for clone discount page loop up discount link
	 * @param profileId
	 * @return
	 */
	@RequestMapping("/getRateProfileDiscountData")
	public ModelAndView getRateProfileDiscountData(@RequestParam(required = true) final Long profileId) {
		final ModelAndView view = new ModelAndView("rateProfileDiscountDetailPage");
		view.addObject("discountList", getDiscountFormList(rateProfileService.getProfileSeasonalDiscountLst(profileId)));
		return view;
	}

	/**
	 * Copy seasonal Discount(s) from source rate profile to target rate profiles(s)
	 * @param discountIds
	 * @param targetProfileIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copySeasonalDiscounts")
	public AjaxFormSubmitResponse copySeasonalDiscounts(final String discountIds, final String targetProfileIds) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Cloning Discounts: " + discountIds);
		}
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final String[] discountIdArr = discountIds.split(ConstantStrings.COMMA);
		final String[] targetProfileIdArr = targetProfileIds.split(ConstantStrings.COMMA);
		rateProfileService.createCopySeasonalDiscounts(StringUtil.convertStringArrayToLongArray(discountIdArr), StringUtil.convertStringArrayToLongArray(targetProfileIdArr));
		return ajxResponse;
	}

	public void setRateProfileService(final IRateProfileService rateProfileService) {
		this.rateProfileService = rateProfileService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setSalesTargetService(final ISalesTargetService salesTargetService) {
		this.salesTargetService = salesTargetService;
	}

	public void setCronJobService(final ICronJobService cronJobService) {
		this.cronJobService = cronJobService;
	}
}
