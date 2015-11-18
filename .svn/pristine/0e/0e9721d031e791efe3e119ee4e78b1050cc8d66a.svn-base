/**
 *
 */
package com.nyt.mpt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.User;
import com.nyt.mpt.form.AutoCompletePojo;
import com.nyt.mpt.form.AvailData;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.PackageForm;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.service.IPackageService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.IUserService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.ProposalWorkflowHandler;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.CreativeType;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemViewableCriteriaEnum;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.PackageFormValidator;

/**
 * This <code>ManagePackageController</code> has all the methods to manage package and all operations related to package
 * 
 * @author surendra.singh
 */

@Controller
@RequestMapping("/managepackage/*")
public class ManagePackageController extends AbstractBaseController {

	private static final String PACKAGE_FORM = "packageForm";

	private IPackageService packageService;

	private IUserService userService;

	private IDocumentService documentService;

	private IProductService adProductService;

	private ProposalHelper proposalHelper;
	
	private ProposalWorkflowHandler proposalWorkFlowHandler;

	private static final Logger LOGGER = Logger.getLogger(ManagePackageController.class);

	/**
	 * Returns the {@link ModelAndView} when the Manage Package is click from the left navigation menu
	 * @param 	packageForm
	 * 			Load default {@link PackageForm} 
	 * @param 	lineItemForm
	 * 			{@link LineItemForm} has all the information related to Line Item
	 * @return
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage(@ModelAttribute(PACKAGE_FORM) final PackageForm packageForm, @ModelAttribute("lineItemForm") final LineItemForm lineItemForm) {
		final ModelAndView view = new ModelAndView("managePackageDisplayPage");
		view.addObject(PACKAGE_FORM, packageForm);
		view.addObject("allProducts", proposalHelper.getAllProductsByClassMap());
		view.addObject("allTargetType", proposalHelper.getAllSalesTargetType());
		view.addObject("allUsersList", getUsersList());
		view.addObject("allPriceType", LineItemPriceTypeEnum.getAllPriceType());
		view.addObject("targetTypeCriteria", proposalHelper.getTargetTypeCriteria());
		view.addObject("allSalesCategories", proposalWorkFlowHandler.getUserSpecificSortedList(proposalHelper.getSalesCategory()));
		view.addObject("lineItemSpecType",  CreativeType.getSpecTypeMap());
		view.addObject("isViewableOptions", LineItemViewableCriteriaEnum.getAllValuesMap());
		return view;
	}

	/**
	 * Loads the Package Grid
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/loadmastergridData")
	public ModelAndView loadMasterGridData(@ModelAttribute final TableGrid<PackageForm> tblGrid) {
		final ModelAndView view = new ModelAndView("managePackageGridData");
		setGridData(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Save/Update a Package
	 * @param response
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/savepackage")
	public AjaxFormSubmitResponse savePackage(final HttpServletResponse response, @ModelAttribute(PACKAGE_FORM) final PackageForm form) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Package with id: " + form.getPackageId());
		}
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		long packageId;
		final CustomBindingResult results = new CustomBindingResult(PACKAGE_FORM, form);
		new PackageFormValidator().validate(form, results);

		if (StringUtils.isNotBlank(form.getPackageName().trim())) {
			final boolean duplicatePackage = packageService.isDuplicatePackageName(form.getPackageName().trim(), form.getPackageId());
			if (duplicatePackage) {
				results.rejectValue("packageName", ErrorCodes.DuplicatePackageName, "packageName", new Object[] {form.getPackageName()},
						UserHelpCodes.DuplicatePackageName);
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajxFormSubmtResp, results);
		} else {
			Package adPackage = new Package();
			adPackage = form.populate(adPackage);
			if (adPackage.getId() == 0) {
				packageId = packageService.savePackage(adPackage);
				form.setPackageId(packageId);
			} else {
				packageId = packageService.savePackage(adPackage);
			}
			ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, packageId);
		}
		return ajxFormSubmtResp;
	}

	/**
	 * Delete a Package
	 * @param response
	 * @param form
	 */
	@RequestMapping("/deletepackage")
	public void deletePackage(final HttpServletResponse response, final PackageForm form) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Package with id: " + form.getPackageId());
		}
		final Package adPackage = new Package();
		adPackage.setId(form.getPackageId());
		packageService.deletePackage(adPackage);
	}

	/**
	 * Set Data from server to the Grid
	 * @param tblGrid
	 */
	private void setGridData(final TableGrid<PackageForm> tblGrid) {
		final List<Package> packageList = packageService.getFilteredPackageList(
				tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!packageList.isEmpty()) {
			final List<PackageForm> listPackageForm = convertDToListToFormLsit(packageList);
			setPackageDocuments(listPackageForm);
			tblGrid.setGridData(listPackageForm, packageService.getFilteredPackageListCount(tblGrid.getFilterCriteria()));
		}
	}

	/**
	 * Set the document List attached to a Package
	 * @param formList
	 */
	private void setPackageDocuments(final List<PackageForm> formList) {
		if (formList == null || formList.isEmpty()) {
			return;
		}
		final List<Long> componentIds = new ArrayList<Long>(formList.size());
		for (PackageForm packageForm : formList) {
			componentIds.add(packageForm.getPackageId());
		}

		final Map<Long, List<Document>> docMap = documentService.getDocumentsForComponents(componentIds, DocumentForEnum.PACKAGE.name());
		for (PackageForm packageForm : formList) {
			final List<Document> docList = docMap.get(packageForm.getPackageId());
			if (docList != null && !docList.isEmpty()) {
				packageForm.setHasDocument(true);
			}
		}
	}

	/**
	 * Converts List of {@link Package} to List of {@link PackageForm}
	 * @param dtoList
	 * @return
	 */
	private List<PackageForm> convertDToListToFormLsit(final List<Package> dtoList) {
		final List<PackageForm> formList = new ArrayList<PackageForm>();
		final Map<Long, String> allSalesCategory = proposalWorkFlowHandler.getUserSpecificSortedList(proposalHelper.getSalesCategory());
		for (Package dto : dtoList) {
			final PackageForm form = new PackageForm();
			form.populateForm(dto);
			if (StringUtils.isNotBlank(form.getPackageSalescategory())) {
				final String[] salesCategoryIds = form.getPackageSalescategory().split(ConstantStrings.COMMA);
				final StringBuffer salesCategorynames = new StringBuffer();
				for (String salesCategoryId : salesCategoryIds) {
					salesCategorynames.append(allSalesCategory.get(NumberUtil.longValue(salesCategoryId))).append(ConstantStrings.COMMA);
				}
				form.setPackageSalescategoryName(salesCategorynames.substring(0, salesCategorynames.lastIndexOf(ConstantStrings.COMMA)).toString());
			}
			formList.add(form);
		}
		return formList;
	}

	/**
	 * Returns the Map of all Users
	 * @return
	 */
	private Map<Long, String> getUsersList() {
		final List<User> allUserList = userService.getUserList();
		final Map<Long, String> userMap = new LinkedHashMap<Long, String>();
		for (User user : allUserList) {
			userMap.put(user.getUserId(), user.getFullName());
		}
		return userMap;
	}

	/**
	 * @param salesTargetID
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAllProductsForSalesTarget")
	public Map<Long, String> getAllProductsForSalesTarget(@RequestParam("salesTargetID") final long salesTargetID) {
		return proposalHelper.getAllProductsForSalesTarget(salesTargetID);
	}

	/**
	 * @param salesTargetID
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAllProductsForMultiSalesTarget")
	public AjaxFormSubmitResponse getAllProductsForMultiSalesTarget(@RequestParam("salesTargetID") final String salesTargetID) {
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		final String[] saleTargetTypeId = salesTargetID.split(ConstantStrings.COMMA);
		final List<Long> saleTargetTypeLst = new ArrayList<Long>();
		for (String targetTypeId : saleTargetTypeId) {
			saleTargetTypeLst.add(Long.valueOf(targetTypeId));
		}
		final Map<Long, String> allProductMap = proposalHelper.getAllProductsForMultiSalesTarget(saleTargetTypeLst);
		ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, allProductMap);

		return ajxFormSubmtResp;
	}


	/**
	 * @param saleTargetType
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAllSalesTargetForTargetType")
	public Map<Long, String> getAllSalesTargetForTargetType(@RequestParam("saleTargetType") final long saleTargetType) {
		return proposalHelper.getAllSalesTargetForTargetType(saleTargetType);
	}


	/**
	 * @param saleTargetType
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAllSalesTargetForMultiTargetType")
	public AjaxFormSubmitResponse getAllSalesTargetForMultiTargetType(@RequestParam("saleTargetType") final String saleTargetType) {
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		int count = 0;
		final String[] saleTargetTypeId = saleTargetType.split(ConstantStrings.COMMA);
		Long[] saleTarTypArr = new Long[saleTargetTypeId.length];
		for (String targetTypeId : saleTargetTypeId) {
			saleTarTypArr[count++] = Long.valueOf(targetTypeId);
		}
		final Map<Long, String> saleTarTypMap = proposalHelper.getAllSalesTargetForMultiTargetType(saleTarTypArr);
		ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, saleTarTypMap);

		final Set<Long> sosSalesTarIdKey = saleTarTypMap.keySet();

		final List<Long> saleTargetTypeLst = new ArrayList<Long>();
		for (Long targetTypeId : sosSalesTarIdKey) {
			saleTargetTypeLst.add(Long.valueOf(targetTypeId));
		}

		final Map<Long, Long> childSaleTargtLst = proposalHelper.getParentSalesTargetId(saleTargetTypeLst);
		ajxFormSubmtResp.getObjectMap().put("childSaleTargetIds", childSaleTargtLst);

		return ajxFormSubmtResp;
	}
	
	/**
	 * @param productName
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getProductList")
	public List<AutoCompletePojo>  getProductList(@RequestParam("term") final String productName) {
		final List<AutoCompletePojo> campAutoCompltLst = new ArrayList<AutoCompletePojo>();
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(productName))) {
			final List<Product>  productList  = adProductService.getProductsBySearchString(productName);
			for (Product productObj : productList) {
				final AutoCompletePojo campAutoComplt = new AutoCompletePojo();
				campAutoComplt.setId(productObj.getId());
				campAutoComplt.setValue(productObj.getName());
				campAutoComplt.setLabel(productObj.getDisplayName());
				campAutoCompltLst.add(campAutoComplt);
			}
		}
		return campAutoCompltLst;
	}

	/**
	 * @param productID
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getSalesTargetForProduct")
	public AjaxFormSubmitResponse getSalesTargetFromProductID(@RequestParam("productID") final Long productID) {
		final AjaxFormSubmitResponse ajxFormSubmtResp = new AjaxFormSubmitResponse(getMessageSource());
		final Map<Long, String> allSalesTarTypMap = proposalHelper.getAllSalesTargetForProductID(productID);
		ajxFormSubmtResp.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, allSalesTarTypMap);
		final Set<Long> sosSaleTarIdKey = allSalesTarTypMap.keySet();
		final List<Long> saleTargetTypeLst = new ArrayList<Long>();
		for (Long targetTypeId : sosSaleTarIdKey) {
			saleTargetTypeLst.add(Long.valueOf(targetTypeId));
		}
		if (allSalesTarTypMap != null && !allSalesTarTypMap.isEmpty()) {
			final Map<Long, Long> childSaleTargtLst = proposalHelper.getParentSalesTargetId(saleTargetTypeLst);
			ajxFormSubmtResp.getObjectMap().put("childSaleTargetIds", childSaleTargtLst);
		}
		return ajxFormSubmtResp;
	}

	/**
	 * @param packageID
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/copyPackage")
	public AjaxFormSubmitResponse copyPackage(@RequestParam("packageID") final long packageID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Cloning Package with id: " + packageID);
		}
		final AjaxFormSubmitResponse ajxResponse = new AjaxFormSubmitResponse(getMessageSource());
		ajxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, packageService.createPackageCloneByID(packageID));
		return ajxResponse;
	}

	/**
	 * @return
	 * 		Returns spec type Map
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getSpecType")
	public Map<String, String> getSpecType() {
		return CreativeType.getSpecTypeMap();
	}

	/**
	 * Used to update avails of all package line item
	 * @param packageId
	 * @param availsJsonData
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateAvails")
	@SuppressWarnings("unchecked")
	public AjaxFormSubmitResponse updateAvails(@RequestParam("packageId") final long packageId, @RequestParam("availsJsonData") final String availsJsonData) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		Package adPackage = new Package();
		final ObjectMapper objectMapper = new ObjectMapper();
		List<LinkedHashMap<String, String>> resultLst = new ArrayList<LinkedHashMap<String, String>>();
		adPackage = packageService.getPackageById(packageId);
		try {
			resultLst = objectMapper.readValue(availsJsonData, resultLst.getClass());
		} catch (JsonParseException e) {
			LOGGER.error("Error during Parsing JSON - " + e);
		} catch (JsonMappingException e) {
			LOGGER.error("Error during Mapping JSON - " + e);
		} catch (IOException e) {
			LOGGER.error("Error during Converting JSON to CampaignPacingObject - " + e);
		}
		final Map<Long, AvailData> lineItemAvailsMap = convertAvailsLstToMap(resultLst);
		if (adPackage != null) {
			Long capacity = 0L;
			Double sov = 0D;
			for (LineItem lineItem : adPackage.getPackagelineItemSet()) {
				if (lineItemAvailsMap.containsKey(lineItem.getLineItemID())) {
					if (!StringUtils.equals(ConstantStrings.NA, lineItemAvailsMap.get(lineItem.getLineItemID()).getAvail())) {
						lineItem.setAvails(NumberUtil.doubleValue(lineItemAvailsMap.get(lineItem.getLineItemID()).getAvail()));
					}
					if (!StringUtils.equals(ConstantStrings.NA, lineItemAvailsMap.get(lineItem.getLineItemID()).getCapacity())) {
						capacity = NumberUtil.longValue(lineItemAvailsMap.get(lineItem.getLineItemID()).getCapacity());
						lineItem.setTotalPossibleImpressions(capacity);
					}
					if (capacity > 0) {
						sov = (double)((lineItem.getImpressionTotal() * 100) / capacity);
						lineItem.setSov(NumberUtil.round(sov, 2));
					}
					lineItem.setAvailsPopulatedDate(DateUtil.getCurrentDate());
				}
			}
		}
		final long returnId = packageService.updatedPackage(adPackage);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, returnId);
		return ajaxResponse;
	}

	/**
	 * Convert JSON Object 'availLst' to Map
	 * @param availLst
	 * @return
	 */
	private Map<Long, AvailData> convertAvailsLstToMap(final List<LinkedHashMap<String, String>> availLst) {
		final Map<Long, AvailData> lineItemMap = new HashMap<Long, AvailData>(availLst.size());
		AvailData availData = null;
		for (LinkedHashMap<String, String> linkedHashMap : availLst) {
			availData = new AvailData();
			availData.setAvail(linkedHashMap.get(ConstantStrings.AVAILS));
			availData.setCapacity(linkedHashMap.get(ConstantStrings.CAPACITY));
			availData.setLineItemID(NumberUtil.longValue(linkedHashMap.get(ConstantStrings.LINITEMID)));
			lineItemMap.put(availData.getLineItemID(), availData);
		}
		return lineItemMap;
	}
	
	/**
	 * Update base price of given line items
	 * @param lineItemIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateMultiLineItemPrice")
	public AjaxFormSubmitResponse updateAllLineItemPrice(@RequestParam(required = true) final String lineItemIds) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final String[] lineItems = lineItemIds.split(ConstantStrings.COMMA);
		packageService.updateAllLineItemsPrice(StringUtil.convertStringArrayToLongArray(lineItems));
		return ajaxResponse;
	}

	/**
	 * @param packageId
	 * @param positionToMove
	 * @param lineItemsToMove
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/arrangeLineItemSequence")
	public AjaxFormSubmitResponse arrangeLineItemSequence(@RequestParam(required = true) final long packageId, 
			@RequestParam(required = true) final int positionToMove, @RequestParam(required = true) String lineItemsToMove) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final Package packge = packageService.getPackageById(packageId);
		proposalHelper.arrangeLineItemSequence(new ArrayList<LineItem>(packge.getPackagelineItemSet()), 
				StringUtil.convertStringToLongList(lineItemsToMove), positionToMove);
		packageService.updatedPackage(packge);
		return ajaxResponse;
	}
	
	public void setPackageService(final IPackageService packageService) {
		this.packageService = packageService;
	}

	public void setUserService(final IUserService userService) {
		this.userService = userService;
	}

	public void setDocumentService(final IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setAdProductService(final IProductService adProductService) {
		this.adProductService = adProductService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public IProductService getAdProductService() {
		return adProductService;
	}
	
	public ProposalWorkflowHandler getProposalWorkFlowHandler() {
		return proposalWorkFlowHandler;
	}

	public void setProposalWorkFlowHandler(
			ProposalWorkflowHandler proposalWorkFlowHandler) {
		this.proposalWorkFlowHandler = proposalWorkFlowHandler;
	}
}
