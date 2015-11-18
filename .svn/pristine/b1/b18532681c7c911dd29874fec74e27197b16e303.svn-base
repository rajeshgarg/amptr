package com.nyt.mpt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.nyt.mpt.domain.DailyDetail;
import com.nyt.mpt.domain.InventoryDetail;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Summary;
import com.nyt.mpt.form.DFPLineItemPojo;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.service.IPackageService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.IYieldexService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.DfpGateway;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.YieldexHelper;
import com.nyt.mpt.util.dto.LineItemDTO;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.exception.DFPWrapperExcepion;
import com.nyt.mpt.util.exception.YieldexAvailsException;
import com.nyt.mpt.validator.AvailsValidator;

/**
 * This <code>AvailsController</code> is used to provide facility related with Avails
 * 
 * @author manish.kesarwani
 */
@Controller
@RequestMapping("/avails/*")
public class AvailsController extends AbstractBaseController {

	private ProposalHelper proposalHelper;
	private IYieldexService yieldexService;
	private YieldexHelper yieldexHelper;
	private IPackageService packageService;
	private IProposalService proposalService;
	private DfpGateway dfpGateway;

	private static final String YIELDEX_ERROR_MSG = "yieldexError";
	private static final String BAD_GATEWAY = "502 Bad Gateway";
	private static final String FETCH_AVAILS_FORM = "fetchAvailsForm";
	private static final String PROPOSAL_LINE_ITEM_FORM = "proposalLineItemForm";

	private static final Logger LOGGER = Logger.getLogger(AvailsController.class);

	/**
	 * Returns the ModelAndView when the tab is created
	 * @param form
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute(FETCH_AVAILS_FORM) final LineItemForm form) {
		final ModelAndView view = new ModelAndView("fetchAvailsPage");
		view.addObject("allProducts", proposalHelper.getAllProductsMapByAvailsId());
		view.addObject("allPriceType", LineItemPriceTypeEnum.getAllPriceType());
		view.addObject("targetTypeCriteria", proposalHelper.getTargetTypeCriteria());
		return view;
	}

	/**
	 * Returns the SalesTarget for the specific <code>productID</code>
	 * @param productID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSalesTargetForProduct")
	public AjaxFormSubmitResponse getSalesTargetFromProductID(@RequestParam final Long productID) {
		final AjaxFormSubmitResponse ajxSubmitResp = new AjaxFormSubmitResponse(getMessageSource());
		final Map<Long, String> salesTargetMap = proposalHelper.getAllSalesTargetForProductID(productID);
		ajxSubmitResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, salesTargetMap);
		return ajxSubmitResp;
	}

	/**
	 * This method is used to populate avails from yield-ex
	 * @param response
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/populateAvailsFromYieldex")
	public AjaxFormSubmitResponse populateAvailsFromYieldex(final HttpServletResponse response, @ModelAttribute(PROPOSAL_LINE_ITEM_FORM) final LineItemForm form) {
		LOGGER.info("Populating Avails from Yieldex");
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("lineItemForm", form);
		new AvailsValidator().validate(form, results);
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			Summary dfpSummary = null;
			Summary yieldexSummary = null;
			String errorMsg = ConstantStrings.EMPTY_STRING;
			final Product prod = yieldexHelper.getProductById(form.getSosProductId());			
			if(prod.getAvailsSytemId() != 15) { // if only yieldex is being called
				final String url = yieldexHelper.getYieldexURLForAvails(getLineItemDTO(form));
				if (url.length() > 7500) {
					errorMsg = getMessageSource().getMessage(ErrorCodes.yieldexUrlLengthException.getResourceName(), null, null);
				} else {
					try {
						final InventoryDetail inventoryDetail = yieldexService.getInventoryDetail(url);
						yieldexSummary = inventoryDetail.getSummary();
						// change summary start and end date formating
						yieldexSummary.setStartDate(DateUtil.getGuiDateString(DateUtil.parseToDateForRemote(yieldexSummary.getStartDate())));
						yieldexSummary.setEndDate(DateUtil.getGuiDateString(DateUtil.parseToDateForRemote(yieldexSummary.getEndDate())));
						yieldexSummary.setAvailable(NumberUtil.formatLong(NumberUtil.longValue(yieldexSummary.getAvailable()), true));
						yieldexSummary.setCapacity(NumberUtil.formatLong(NumberUtil.longValue(yieldexSummary.getCapacity()), true));
					} catch (YieldexAvailsException e) {
						try {
							errorMsg = errorYieldexMsg(UriUtils.encodeHttpUrl(url, "UTF-8"));
						} catch (UnsupportedEncodingException ex) {
							// should not happen, UTF-8 is always supported
							throw new IllegalStateException(ex);
						}
					}
				}
			}
			if(StringUtils.isBlank(errorMsg) && (prod.getAvailsSytemId() == 15 || prod.getAvailsSytemId() == 16)){ //checking avails id for DFP or DFP & yieldex
				final DFPLineItemPojo dfpLineItemPojo = populateLineItemFormToDfpPojo(form, null);
				dfpLineItemPojo.setSyncAds(prod.isSyncAds());
				dfpSummary = dfpGateway.getInventoryForeCast(dfpLineItemPojo);
			}
			if(StringUtils.isBlank(errorMsg)){
				ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, getConsolidatedSummary(dfpSummary, yieldexSummary));
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_ADDITIONAL_VALUE, DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
			ajaxResponse.getObjectMap().put(YIELDEX_ERROR_MSG, errorMsg);
		}
		return ajaxResponse;
	}

	/**
	 * This method is used to fetch avails from Yield-ex
	 * @param response
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fetchAvailsFromYieldex")
	public AjaxFormSubmitResponse fetchAvailsFromYieldex(final HttpServletResponse response, @ModelAttribute(FETCH_AVAILS_FORM) final LineItemForm form) {
		LOGGER.info("Populating Avails from Yieldex");
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("lineItemForm", form);
		new AvailsValidator().validate(form, results);
		String errorMsg = ConstantStrings.EMPTY_STRING;
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			if(!"D".equals(form.getType())){
				final String url = yieldexHelper.getYieldexURLForAvails(getLineItemDTO(form));
				if (url.length() > 7500) {
					errorMsg = getMessageSource().getMessage(ErrorCodes.yieldexUrlLengthException.getResourceName(), null, null);
				} else {
					try {
						final InventoryDetail inventoryDetail = yieldexService.getInvWithDailyDetails(url);
	
						// Change daily details start and end date formating
						final List<DailyDetail> dailyDetailsList = inventoryDetail.getDailyDetail();
						for (DailyDetail dailyDetail : dailyDetailsList) {
							dailyDetail.setStartDate(DateUtil.getGuiDateString(DateUtil.parseToDateForRemote(dailyDetail.getStartDate())));
							dailyDetail.setEndDate(DateUtil.getGuiDateString(DateUtil.parseToDateForRemote(dailyDetail.getEndDate())));
							dailyDetail.setAvailable(NumberUtil.formatLong(NumberUtil.longValue(dailyDetail.getAvailable()), false));
							dailyDetail.setCapacity(NumberUtil.formatLong(NumberUtil.longValue(dailyDetail.getCapacity()), false));
						}
	
						ajaxResponse.getObjectMap().put("dailyDetails", dailyDetailsList);
						ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_ADDITIONAL_VALUE, DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
					} catch (YieldexAvailsException e) {
						try {
							errorMsg = errorYieldexMsg(UriUtils.encodeHttpUrl(url, "UTF-8"));
						} catch (UnsupportedEncodingException ex) {
							// should not happen, UTF-8 is always supported
							throw new IllegalStateException(ex);
						}
					}
				}
				ajaxResponse.getObjectMap().put("yieldexURL", url);
			}
			if(StringUtils.isBlank(errorMsg) && ("D".equals(form.getType()) || "B".equals(form.getType()))){
				DFPLineItemPojo dfpPojo =  populateLineItemFormToDfpPojo(form, null);
				dfpPojo.setSyncAds(yieldexHelper.getProductById(form.getSosProductId()).isSyncAds());
				Summary dfpSummary = dfpGateway.getInventoryForeCast(dfpPojo);
				dfpSummary.setStartDate(form.getStartDate());
				dfpSummary.setEndDate(form.getEndDate());
				ajaxResponse.getObjectMap().put("dfpResponse", dfpSummary);
			}
			ajaxResponse.getObjectMap().put(YIELDEX_ERROR_MSG, errorMsg);
		}
		return ajaxResponse;
	}

	/**
	 * Additional validation check for bulk fetch on Proposal and Package screen
	 * @param date
	 */
	private void bulkFetchAvailsValidation(Date date) {
		final Calendar lineItemEndCalDate = Calendar.getInstance();
		lineItemEndCalDate.setTime(date);
		final Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(DateUtil.convertToMidNightDate(DateUtil.getCurrentDate()));
		if (lineItemEndCalDate.before(currentDate)) {
			throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.bulkFetchAvailsEndDateCannotBefore, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
		}
	}

	/**
	 * Get avails for all the LineItems in a Package by <code>packageId</code>
	 * @param packageId
	 * @return
	 */
	@RequestMapping("/getPackageAvailStatusSummary")
	public ModelAndView getPackageAvailStatusSummary(@RequestParam("packageId") final Long packageId) {
		final ModelAndView view = new ModelAndView("availsStatusSummary");
		final List<LineItemForm> formList = setAvailStatusSummary(packageService.getFilteredPackageLineItems(packageId, null, null, new SortingCriteria("lineItemSequence", CustomDbOrder.ASCENDING)));
		view.addObject("availsSummaryList", formList);
		view.addObject("sovExceedFlag", isSovExceed(formList));
		return view;
	}

	/**
	 * Returns the List of {@link LineItemForm} to show the Avail Status Summary for the List of LineItems
	 * @param lineItemLst
	 * @return
	 */
	private List<LineItemForm> setAvailStatusSummary(final List<LineItem> lineItemLst) {
		final List<LineItemForm> lineItemFormLst = new ArrayList<LineItemForm>(lineItemLst.size());
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			for (LineItem lineItemDB : lineItemLst) {
				final LineItemForm lineItemForm = new LineItemForm();
				lineItemForm.setProductType(lineItemDB.getProductType().getShortName());
				lineItemForm.populateForm(lineItemDB);
				InventoryDetail inventoryDetail = null;
				Summary dfpSummary = null;
				if (StringUtils.isBlank(lineItemDB.getFlight())) {
					final Product prod =  yieldexHelper.getProductById(lineItemForm.getSosProductId());
					if (prod != null) {
						if(prod.getAvailsSytemId() == 15 || prod.getAvailsSytemId() == 16){//checking avails id for DFP
							bulkFetchAvailsValidation(lineItemDB.getEndDate());
							DFPLineItemPojo dfpPojo =  populateLineItemFormToDfpPojo(lineItemForm, lineItemDB.getGeoTargetSet());
							dfpPojo.setSyncAds(prod.isSyncAds());
							dfpSummary = dfpGateway.getInventoryForeCast(dfpPojo);
						}
						if(prod.getAvailsSytemId() != 15){
							final LineItemDTO lineItemDTO = getLineItemDTO(lineItemForm);
							lineItemDTO.setGeoTargetSet(lineItemDB.getGeoTargetSet());
							inventoryDetail = yieldexService.getInventoryDetail(yieldexHelper.getYieldexURLForAvails(lineItemDTO));
						}
					}
				}
				if (inventoryDetail == null && dfpSummary == null) {
					lineItemForm.setCurrentAvails(ConstantStrings.NA);
					lineItemForm.setCurrentTotalPossibleImpressions(ConstantStrings.NA);
				} else {
					Summary yieldexSummary = null;
					if (inventoryDetail != null) {
						yieldexSummary = inventoryDetail.getSummary();
					}
					
					Summary consolidatedSummary = getConsolidatedSummary(dfpSummary,yieldexSummary);
					lineItemForm.setCurrentAvails((consolidatedSummary.getAvailable() == null) ? ConstantStrings.NA : NumberUtil.formatLong(NumberUtil.longValue(consolidatedSummary.getAvailable()), true));
					lineItemForm.setCurrentTotalPossibleImpressions((consolidatedSummary.getCapacity() == null) ? 
							ConstantStrings.NA : NumberUtil.formatLong(NumberUtil.longValue(consolidatedSummary.getCapacity()), true));
					if (!ConstantStrings.NA.equalsIgnoreCase(lineItemForm.getCurrentAvails()) && (lineItemDB.getImpressionTotal().longValue() > 
					NumberUtil.longValue(consolidatedSummary.getAvailable()))) {
						lineItemForm.setOfferedImpLessThanCurAvails(true);
					}
					lineItemForm.setCurrentAvailsUICamparision((consolidatedSummary.getAvailable() == null) ? ConstantStrings.NA : String.valueOf(NumberUtil.longValue(consolidatedSummary.getAvailable())));
					lineItemForm.setCurrentTotalUICamparision((consolidatedSummary.getCapacity() == null) ? ConstantStrings.NA : String.valueOf(NumberUtil.longValue(consolidatedSummary.getCapacity())));
					if(LineItemProductTypeEnum.STANDARD.getShortName().equals(lineItemDB.getProductType().getShortName())) {
						final Long capacity = NumberUtil.longValue(consolidatedSummary.getCapacity());
						if (capacity > 0) {
							lineItemForm.setSov(String.valueOf(Math.round(NumberUtil.round((NumberUtil.doubleValue(lineItemForm.getImpressionTotal()) * 100 / capacity), 2))));
						} else {
							lineItemForm.setSov(null);
						}
					} else {
						lineItemForm.setSov(null);
					}
				}
				lineItemForm.setAvails((lineItemDB.getAvails() == null) ? ConstantStrings.NA : NumberUtil.formatLong(lineItemDB.getAvails().longValue(), true));
				lineItemForm.setTotalPossibleImpressions((lineItemDB.getTotalPossibleImpressions() == null) ? ConstantStrings.NA : 
					NumberUtil.formatLong(lineItemDB.getTotalPossibleImpressions().longValue(), true));
				lineItemForm.setAvailsPopulatedDate(DateUtil.getGuiDateString(DateUtil.getCurrentDate()));
				lineItemFormLst.add(lineItemForm);
			}
		}
		return lineItemFormLst;
	}

	/**
	 * Get avails for all the LineItems in a Proposal by <code>proposalVersionID</code> and <code>optionId</code>
	 * @param proposalVersionID
	 * @param optionId
	 * @return
	 */
	@RequestMapping("/getAvailStatusSummary")
	public ModelAndView getAvailStatusSummary(@RequestParam("proposalVersionID") final long proposalVersionID, @RequestParam("optionId") final long optionId) {
		final ModelAndView view = new ModelAndView("availsStatusSummary");
		final List<LineItemForm> formList = setAvailStatusSummary(proposalService.getProposalLineItems(optionId, proposalVersionID, null, null, new SortingCriteria("lineItemSequence", CustomDbOrder.ASCENDING)));
		view.addObject("availsSummaryList", formList);
		view.addObject("sovExceedFlag", isSovExceed(formList));
		return view;
	}
	
	/**
	 * Check if any one of line items has more than 100 sov
	 */
	private boolean isSovExceed(List<LineItemForm> formList) {
		boolean sovExceed = false;
		for (LineItemForm lineItemForm : formList) {
			if (StringUtils.isNotBlank(lineItemForm.getSov()) && Long.valueOf(lineItemForm.getSov()) > 100) {
				sovExceed = true;
				break;
			}
		}
		return sovExceed;
	}

	/**
	 * Used to convert {@link LineItemForm} data into {@link LineItemDTO} mainly targeting string
	 * @param form
	 * @return
	 */
	private LineItemDTO getLineItemDTO(final LineItemForm form) {
		final LineItemDTO lineItemDTO = new LineItemDTO();
		lineItemDTO.setLineItemID(form.getLineItemID());
		lineItemDTO.setSosProductId(form.getSosProductId());
		lineItemDTO.setSosSalesTargetId(form.getSosSalesTargetId());
		if (StringUtils.isNotBlank(form.getLineItemTargetingData())) {
			lineItemDTO.setGeoTargetSet(TargetJsonConverter.convertJsonToObject(form.getLineItemTargetingData(), null));
		}
		lineItemDTO.setStartDate(form.getStartDate());
		lineItemDTO.setEndDate(form.getEndDate());
		return lineItemDTO;
	}
	
	/**
	 * Used to populate the AMPT Lineitem form to DFP Lineitem POJO
	 * @param lineItemForm
	 * @param set 
	 */
	private DFPLineItemPojo populateLineItemFormToDfpPojo(final LineItemForm lineItemForm, Set<LineItemTarget> geoTargetSet) {
		final DFPLineItemPojo dfpLineItemPojo = new DFPLineItemPojo();
		dfpLineItemPojo.setStartDate(DateUtil.parseToDate(lineItemForm.getStartDate()));
		dfpLineItemPojo.setEndDate(DateUtil.parseToDate(lineItemForm.getEndDate()));
		dfpLineItemPojo.setProductId(Long.valueOf(lineItemForm.getSosProductId()));
		final List<Long> saleTargetIds = new ArrayList<Long>();
		for (String salesTargetId : lineItemForm.getSosSalesTargetId()) {
			saleTargetIds.add(Long.valueOf(salesTargetId));
		}
		dfpLineItemPojo.setSalesTargetIds(saleTargetIds);
		if (LineItemProductTypeEnum.RESERVABLE.getShortName().equals(lineItemForm.getProductType())) {
			dfpLineItemPojo.setShareOfReservation(50);// hard-coded SOR value in case of DFP
			dfpLineItemPojo.setReservable(true);
		} else {
			dfpLineItemPojo.setImpressions(10000); //// hard-coded impressions value in case of DFP
			dfpLineItemPojo.setReservable(false);
		}
		dfpLineItemPojo.setCostType(lineItemForm.getPriceType());
		settingTargetting(dfpLineItemPojo, lineItemForm.getLineItemTargetingData(), geoTargetSet);
		return dfpLineItemPojo;
	}

	/**
	 * Used to set the Targeting info from AMPT Lineitem to DFP Lineitem Pojo
	 * @param dfpLineItemPojo
	 * @param lineItemTargetingData
	 * @param set 
	 */
	private void settingTargetting(final DFPLineItemPojo dfpLineItemPojo, final String lineItemTargetingData, Set<LineItemTarget> geoTargetSet) {
		final Set<String> allDeviceIds = new HashSet<String>();
		final Set<String> allCountryIds = new HashSet<String>();
		final Set<String> allRegionIds = new HashSet<String>();
		final Set<String> allOsIds = new HashSet<String>();
		final Set<String> allStateIds = new HashSet<String>();
		final Set<String> allBrowsersIds = new HashSet<String>();
		final Set<String> allDMAIds = new HashSet<String>();
		boolean isCountriesExcluded = false, isDmaExcluded = false, isStateExcluded = false, isDeviceExcluded = false, isOsExcluded = false, isBrowserExcluded = false;
		final Set<LineItemTarget> lineItemTargets = (geoTargetSet == null) ? TargetJsonConverter.convertJsonToObject(lineItemTargetingData, null) : geoTargetSet;
		validationDfpForecasting(lineItemTargets);
		/**
		 * Ids of the Target Type has been Hard Coded any mismatch will not be added for Forecasting
		 */
		for (LineItemTarget lineItemTarget : lineItemTargets) {
			int targetType = lineItemTarget.getSosTarTypeId().intValue();
			switch (targetType) {
			case 5: // DMA
				isDmaExcluded = setUniqueIdsAndNegation(lineItemTarget, allDMAIds, isDmaExcluded);
				break;
			case 6: // States
				isStateExcluded = setUniqueIdsAndNegation(lineItemTarget, allStateIds, isStateExcluded);
				break;
			case 8: // Countries
				isCountriesExcluded = setUniqueIdsAndNegation(lineItemTarget, allCountryIds, isCountriesExcluded);
				break;
			case 35: // Region
				isCountriesExcluded = setUniqueIdsAndNegation(lineItemTarget, allRegionIds, isCountriesExcluded);
				break;
			case 37: // Browser
				isBrowserExcluded = setUniqueIdsAndNegation(lineItemTarget, allBrowsersIds, isBrowserExcluded);
				break;
			case 38: // OS
				isOsExcluded = setUniqueIdsAndNegation(lineItemTarget, allOsIds, isOsExcluded);
				break;
			case 40: // Frequency Cap
				dfpLineItemPojo.setFrequencyCap(yieldexHelper.getTargetElementNameById(lineItemTarget.getSosTarTypeElementId()));
				break;
			case 42: // Device
				isDeviceExcluded = setUniqueIdsAndNegation(lineItemTarget, allDeviceIds, isDeviceExcluded);
				break;
			}
		}
		
		if(lineItemTargets != null && lineItemTargets.size() > 0) {
			
			/**
			 *  get map of all the targeting element data one time and using it for all elements
			 */
			final Map<Long, String> targetElementIdsAndNamesMap = yieldexHelper.getAllTargetTypeElement();
			if(!targetElementIdsAndNamesMap.isEmpty()) {
				// setting the name (ADX_LOOKUP) of the targeting elements e.g US (in case of countries), Apple_iPad_2 (in case of devices) etc.
				dfpLineItemPojo.setDeviceLookup(getTargetedElementNames(allDeviceIds, targetElementIdsAndNamesMap));
				dfpLineItemPojo.setCountriesLookup(getTargetedElementNames(allCountryIds, targetElementIdsAndNamesMap));
				if(!allRegionIds.isEmpty()){
					dfpLineItemPojo.setCountriesLookup(yieldexHelper.getAllCoutriesByRegions(allRegionIds));
				}
				dfpLineItemPojo.setOsLookup(getTargetedElementNames(allOsIds, targetElementIdsAndNamesMap));
				dfpLineItemPojo.setStateLookup(getTargetedElementNames(allStateIds, targetElementIdsAndNamesMap));
				dfpLineItemPojo.setBrowserLookup(getTargetedElementNames(allBrowsersIds, targetElementIdsAndNamesMap));
				dfpLineItemPojo.setDmaLookup(getTargetedElementNames(allDMAIds, targetElementIdsAndNamesMap));

				// setting the "Not" of targeting elements
				dfpLineItemPojo.setDeviceExcluded(isDeviceExcluded);
				dfpLineItemPojo.setCountriesExcluded(isCountriesExcluded);
				dfpLineItemPojo.setOsExcluded(isOsExcluded);
				dfpLineItemPojo.setStateExcluded(isStateExcluded);
				dfpLineItemPojo.setBrowserExcluded(isBrowserExcluded);
				dfpLineItemPojo.setDmaExcluded(isDmaExcluded);
			}
		}
	}
	
	/**
	 * Validating the DFP validation
	 * @param lineItemTargets
	 */
	private void validationDfpForecasting(Set<LineItemTarget> lineItemTargets) {
		final Map<Long, Boolean> validateTargetMap = new HashMap<Long, Boolean>();
		Long targetTypeId = 0L;
		for (LineItemTarget lineItemTarget : lineItemTargets) {
			if (ConstantStrings.OR.equals(lineItemTarget.getOperation())) {
				throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.dfpOrCantnotComeInTargeting, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
			}
			// this ensures that region and country will be treated as same 
			if (lineItemTarget.getSosTarTypeId() == 35L) {
				targetTypeId = 8L;
			}else{
				targetTypeId = lineItemTarget.getSosTarTypeId();
			}
			if (validateTargetMap.containsKey(targetTypeId)) {
				if (getNegationBoolean(lineItemTarget.getNegation()) || validateTargetMap.get(targetTypeId)) {
					if(!(getNegationBoolean(lineItemTarget.getNegation()) && validateTargetMap.get(targetTypeId))){
						throw new DFPWrapperExcepion(DFPWrapperExcepion.getCustomeBusinessError(ErrorCodes.noTwoTargetingTogetherWithNot, ErrorMessageType.DFP_WRAPPER_EXCEPTION, null));
					}
				}
			} else {
				validateTargetMap.put(targetTypeId, getNegationBoolean(lineItemTarget.getNegation()));
			}
			
		}
	}
	
	Boolean getNegationBoolean(String negation) {
		return ("Not".equals(negation) ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Setting and overriding (if Targeting has been added multiple times with OR operation 
	 * as AND will give error on UI side itself) the uniqueIds and negation
	 * @param lineItemTarget
	 * @param uniqueIds
	 * @param negation
	 */
	private boolean setUniqueIdsAndNegation(final LineItemTarget lineItemTarget, final Set<String> uniqueIds, boolean negation) {
		for (String string : lineItemTarget.getSosTarTypeElementId().split(ConstantStrings.COMMA)) {
			uniqueIds.add(string);
		}
		if("Not".equals(lineItemTarget.getNegation())) {
			negation = true;
		}
		return negation;
	}

	/**
	 * Used to get the Targeted elements from the the Set of Targeted element Ids
	 * @param 	targetElementsIds
	 * @param 	targetElementIdsAndNamesMap
	 * 			Map of All targeting elements just to save the database hits
	 * @return
	 */
	private List<String> getTargetedElementNames(final Set<String> targetElementsIds, final Map<Long, String> targetElementIdsAndNamesMap) {
		final List<String> targetElementNames = new ArrayList<String>();
		if(!targetElementsIds.isEmpty()) {
			for (String string : targetElementsIds) {
				targetElementNames.add(targetElementIdsAndNamesMap.get(Long.valueOf(string)));
			}
		}
		return targetElementNames;
	}

	/**
	 * Returns Yield-Ex error message
	 * @param url
	 * @return
	 */
	private String errorYieldexMsg(final String url) {
		final StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			String readMsg;
			final HttpClient httpClient = new DefaultHttpClient();
			final HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
			final HttpEntity entity = httpResponse.getEntity();
			reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			while ((readMsg = reader.readLine()) != null) {
				buffer.append(readMsg);
			}
		} catch (IOException exp) {
			LOGGER.debug(exp.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.debug(e.getMessage());
				}
			}
		}

		final String errMessage = buffer.toString();
		if (ConstantStrings.EMPTY_STRING.equals(errMessage) || errMessage.contains(ConstantStrings.INVENTORY_DETAIL)) {
			return getMessageSource().getMessage(ErrorCodes.yieldexException.getResourceName(), null, null);
		} else if (errMessage.contains(BAD_GATEWAY)) {
			return getMessageSource().getMessage(ErrorCodes.yieldexBadGateWayException.getResourceName(), null, null);
		}
		return errMessage;
	}
	
	/**
	 * Method returns the consolidated summary of dfp and yieldex
	 * @param dfpSummary
	 * @param yieldexSummary
	 * @return
	 */
	private Summary getConsolidatedSummary(Summary dfpSummary, Summary yieldexSummary) {
		Summary returnObj = null;
		if(dfpSummary != null && yieldexSummary != null){
			returnObj = yieldexSummary;
			Long totalAvailable = NumberUtil.longValue(yieldexSummary.getAvailable()) + NumberUtil.longValue(dfpSummary.getAvailable());
			returnObj.setAvailable(NumberUtil.formatLong(totalAvailable, true));
			Long totalCapacity = NumberUtil.longValue(yieldexSummary.getCapacity()) + NumberUtil.longValue(dfpSummary.getCapacity());
			returnObj.setCapacity(NumberUtil.formatLong(totalCapacity, true));
		}else{
			returnObj = (dfpSummary == null) ? yieldexSummary : dfpSummary;
		}
		return returnObj;
	}

	public ProposalHelper getProposalHelper() {
		return proposalHelper;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public IYieldexService getYieldexService() {
		return yieldexService;
	}

	public void setYieldexService(final IYieldexService yieldexService) {
		this.yieldexService = yieldexService;
	}

	public YieldexHelper getYieldexHelper() {
		return yieldexHelper;
	}

	public void setYieldexHelper(final YieldexHelper yieldexHelper) {
		this.yieldexHelper = yieldexHelper;
	}

	public IPackageService getPackageService() {
		return packageService;
	}

	public void setPackageService(final IPackageService packageService) {
		this.packageService = packageService;
	}

	public IProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setDfpGateway(DfpGateway dfpGateway) {
		this.dfpGateway = dfpGateway;
	}
	
}