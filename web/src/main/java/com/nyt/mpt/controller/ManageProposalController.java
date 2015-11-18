/**
 *
 */
package com.nyt.mpt.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.LineItem;
import com.nyt.mpt.domain.Package;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.Proposal;
import com.nyt.mpt.domain.ProposalOption;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.domain.TemplateMetaData;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.form.PackageForm;
import com.nyt.mpt.form.ProposalForm;
import com.nyt.mpt.form.SearchProposalForm;
import com.nyt.mpt.service.IPackageService;
import com.nyt.mpt.service.IProposalSOSService;
import com.nyt.mpt.service.IProposalService;
import com.nyt.mpt.service.ITemplateService;
import com.nyt.mpt.template.ReferenceDataMap;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBusinessError;
import com.nyt.mpt.util.CustomDbOrder;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.SortingCriteria;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.TemplateGenerator;
import com.nyt.mpt.util.TemplateWorkBook;
import com.nyt.mpt.util.enums.ProposalStatus;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.exception.BusinessException;
import com.nyt.mpt.util.exception.CustomCheckedException;
import com.nyt.mpt.util.filter.RangeFilterCriteria;
import com.nyt.mpt.util.security.SecurityUtil;

/**
 * This <code>ManageProposalController</code> class contains all the methods to manage Proposal and related data and it's screen
 * 
 * @author amandeep.singh
 */
@Controller
@RequestMapping("/manageProposal/*")
public class ManageProposalController extends AbstractBaseController {

	private static final Logger LOGGER = Logger.getLogger(ManageProposalController.class);

	private static final String PRO_GRID_DATA = "manageProposalGridData";

	private static final String PRO_DISPLAY_PAGE = "manageProposalDisplayPage";

	private static final String LINEITEM_PAGE = "manageProposalLineItemDisplayPage";

	private IProposalService proposalService;

	private ProposalHelper proposalHelper;

	private IPackageService packageService;

	private ITemplateService templateService;

	private TemplateGenerator templateGenerator;

	private IProposalSOSService proposalSOSService;

	/**
	 * Returns the ModelAndView for the first time when the Manage Proposal screen loads
	 * @return
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage(@ModelAttribute("proposalForm") final ProposalForm proposalForm) {
		final ModelAndView view = new ModelAndView(PRO_DISPLAY_PAGE);
		view.addObject("proposalForm", proposalForm);
		return view;
	}

	/**
	 * Search the proposal and set to the grid
	 * @param searchForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/searchProposal")
	public ModelAndView reloadGridData(final SearchProposalForm searchForm, @ModelAttribute final TableGrid<ProposalForm> tblGrid) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("searching proposal with id: " + searchForm.getProposalID());
		}
		final List<RangeFilterCriteria> filterCriteriaLst = getFilterCriteriaList(searchForm);
		RangeFilterCriteria filterCriteria = null;
		if (StringUtils.isNotBlank(searchForm.getProposalID())) {
			filterCriteria = getFilterCriteriaFor("proposalID", SearchOption.EQUAL);
			filterCriteria.setSearchString(searchForm.getProposalID().trim());
			filterCriteriaLst.add(filterCriteria);
		}
		final List<Proposal> proposalLst = proposalService.getProposalList(filterCriteriaLst, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (proposalLst != null && !proposalLst.isEmpty()) {
			final List<ProposalForm> proposalFormList = new ArrayList<ProposalForm>();
			final Map<Long, String> advertiserMap = getAdvertiser();
			final Map<Long, String> agencyMap = getAgency();
			final Map<Long, String> salescategoryMap = getSalesCategory();
			for (Proposal proposal : proposalLst) {
				final ProposalForm proposalForm = new ProposalForm();
				proposalForm.populateForm(proposal);
				proposalForm.setAgencyName(agencyMap.get(proposal.getSosAgencyId()));
				proposalForm.setSalescategory(salescategoryMap.get(proposal.getSosSalesCategoryId()));
				if (StringUtils.isNotBlank(proposalForm.getAdvertiserName()) && "0".equals(proposalForm.getAdvertiserName())) {
					proposalForm.setAdvertiserName(proposalForm.getNewAdvertiserName());
				} else {
					proposalForm.setAdvertiserName(advertiserMap.get(proposal.getSosAdvertiserId()));
				}
				final ProposalOption option = proposal.getDefaultOption();
				proposalForm.setBudget((option.getBudget() == null) ? ConstantStrings.EMPTY_STRING : NumberUtil.formatDouble(option.getBudget(), true));
				proposalFormList.add(proposalForm);
			}
			tblGrid.setGridData(proposalFormList, proposalService.getProposalListCount(filterCriteriaLst));
		}
		final ModelAndView view = new ModelAndView(PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * @param searchForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/searchPackageForBuild")
	public ModelAndView searchPackageForBuild(final SearchProposalForm searchForm, @ModelAttribute final TableGrid<PackageForm> tblGrid) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Searching Package For Build with id: " + searchForm.getProposalID());
		}
		if ("package".equals(searchForm.getSearchType())) {
			final List<RangeFilterCriteria> filterCriteriaLst = getFilterCriteriaList(searchForm);
			tblGrid.setSidx("packageId");
			final List<Package> packageList = packageService.getFilteredPackageListHavingLineItems(filterCriteriaLst, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
			if (!packageList.isEmpty()) {
				final List<PackageForm> listPackageForm = convertDToListToFormLsit(packageList);
				final Integer totalCount = packageService.getFilteredPackageListCountHavingLineItems(filterCriteriaLst);
				tblGrid.setGridData(listPackageForm, totalCount);
			}
		}
		final ModelAndView view = new ModelAndView(PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Converts List of {@link Package} to List of {@link PackageForm}
	 * @param dtoList
	 * @return
	 */
	private List<PackageForm> convertDToListToFormLsit(final List<Package> dtoList) {
		final List<PackageForm> formList = new ArrayList<PackageForm>();
		for (Package dto : dtoList) {
			final PackageForm form = new PackageForm();
			form.populateForm(dto);
			formList.add(form);
		}
		return formList;
	}
	
	/**
	 * Displays the Line Items of a Proposal
	 * @param proposalForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/getLineItemsForProposal")
	public ModelAndView getLineItemsForProposal(final ProposalForm proposalForm, @ModelAttribute final TableGrid<LineItemForm> tblGrid) {
		final List<LineItemForm> lineItemFormLst = new ArrayList<LineItemForm>();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching LineItems for Option with Id: " + proposalForm.getOptionId());
		}
		final List<LineItem> lineItemLst = proposalService.getProposalLineItems(proposalForm.getOptionId(),
				Long.valueOf(proposalForm.getProposalVersion()), tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			final ReferenceDataMap referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemAssocList(lineItemLst);
			final Map<Long, Product> productMap = referenceDataMap.getProductMap();
			final Map<Long, SalesTarget> salesTargetMap = referenceDataMap.getSalesTargetMap();
			for (LineItem proposalLineItem : lineItemLst) {
				final LineItemForm lineItemForm = new LineItemForm();
				lineItemForm.populateForm(proposalLineItem);
				if (proposalLineItem.getLineItemExceptions() != null && proposalLineItem.getLineItemExceptions().size() > 0) {
					lineItemForm.setLineItemExceptions(Constants.YES);
				} else {
					lineItemForm.setLineItemExceptions(Constants.NO);
				}
				lineItemForm.setType("LineItem");
				if (!productMap.containsKey(proposalLineItem.getSosProductId()) || !Constants.ACTIVE_STATUS.equalsIgnoreCase(productMap.get(proposalLineItem.getSosProductId()).getStatus())
						|| productMap.get(proposalLineItem.getSosProductId()).getDeleteDate() != null) {
					lineItemForm.setProduct_active(Constants.NO);
				}
				// Setting sales target status
				if (!proposalHelper.getSalesTargetStatusFromSalesTargetAssocList(salesTargetMap, proposalLineItem)) {
					lineItemForm.setSalesTarget_active(Constants.NO);
				}
				lineItemFormLst.add(lineItemForm);
			}
			final int lineItemCount = proposalService.getFilteredPackageLineItemsCount(proposalForm.getOptionId(),
					Long.valueOf(proposalForm.getProposalVersion()), tblGrid.getFilterCriteria());
			tblGrid.setGridData(lineItemFormLst, lineItemCount);
		}

		final ModelAndView view = new ModelAndView(PRO_GRID_DATA);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Returns the ModelAndView when a Line Item is created by searching the Line Item from Package grid by clicking search icon 
	 * @param proposalForm
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/getLineItemsForProposalSearch")
	public ModelAndView getLineItemsForProposalSearch(final SearchProposalForm proposalForm, @ModelAttribute final TableGrid<LineItemForm> tblGrid) {
		final List<LineItemForm> lineItemFormLst = new ArrayList<LineItemForm>();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Fetching LineItems for Package with Id: " + proposalForm.getPackageId());
		}
		final SortingCriteria sortingCriteria = new SortingCriteria("lineItemID", CustomDbOrder.DESCENDING);
		final List<LineItem> lineItemLst = packageService.getFilteredPackageLineItems(Long.valueOf(proposalForm.getPackageId()), null, null, sortingCriteria);

		final ReferenceDataMap referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemList(lineItemLst);
		final Map<Long, Product> productMap = referenceDataMap.getProductMap();
		final Map<Long, SalesTarget> salesTargetMap = referenceDataMap.getSalesTargetMap();
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			for (LineItem lineItem : lineItemLst) {
				final LineItemForm lineItemForm = new LineItemForm();
				lineItemForm.populateForm(lineItem);
				lineItemForm.setType("LineItem");

				// Setting sales target name
				lineItemForm.setSosSalesTargetName(proposalHelper.getSalesTargetNameFromSalesTargetAssocList(salesTargetMap, lineItem));

				if (!productMap.containsKey(lineItem.getSosProductId()) || !Constants.ACTIVE_STATUS.equalsIgnoreCase(productMap.get(lineItem.getSosProductId()).getStatus())
						|| productMap.get(lineItem.getSosProductId()).getDeleteDate() != null) {
					lineItemForm.setProduct_active(Constants.NO);
				}
				// Setting sales target status
				if (!proposalHelper.getSalesTargetStatusFromSalesTargetAssocList(salesTargetMap, lineItem)) {
					lineItemForm.setSalesTarget_active(Constants.NO);
				}
				if ((Constants.YES).equals(lineItemForm.getProduct_active()) && (Constants.YES).equals(lineItemForm.getSalesTarget_active())) {
					LOGGER.info("Checking product/sales target cobination is active for Product with Id:" + lineItem.getSosProductId() + " Sales target with id: "
							+ proposalHelper.convertSalesTargetAssocsToIds(lineItem.getLineItemSalesTargetAssocs()));
					if (!proposalSOSService.isProductPlacementActive(lineItem.getSosProductId(), proposalHelper.convertSalesTargetAssocsToIds(lineItem.getLineItemSalesTargetAssocs()))) {
						lineItemForm.setProductSalesTargetCombo_active(Constants.NO);
					}
				}

				lineItemFormLst.add(lineItemForm);
			}
		}
		final ModelAndView view = new ModelAndView(LINEITEM_PAGE);
		view.addObject("allLineItems", lineItemFormLst);
		return view;
	}

	/**
	 * This method is used to Validate Template
	 * @param proposalForm
	 * @param templateType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validateMediaTemplate")
	public AjaxFormSubmitResponse validateMediaTemplate(final ProposalForm proposalForm, @RequestParam final String templateType) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final long optionID = proposalForm.getOptionId();
		long templateId = 0;
		long creativeId = 0;
		if (!("C".equalsIgnoreCase(templateType) || "AC".equalsIgnoreCase(templateType))) {
			final TemplateMetaData template = templateService.getMediaPlanTemplateById(Long.valueOf(proposalForm.getProposalTemplate()));
			if (template == null) {
				ajaxResponse.getObjectMap().put("Valid", false);
				return ajaxResponse;
			}
		}
		if ("T".equalsIgnoreCase(templateType) || "B".equalsIgnoreCase(templateType)) {
			templateId = Long.valueOf(proposalForm.getProposalTemplate());
			LOGGER.info("Validating Proposal data for export template having proposal ID :" + optionID + " and version ID: " + proposalForm.getProposalVersion());
			templateGenerator.getExportTemplateVO(optionID, Long.valueOf(proposalForm.getProposalVersion()), templateId);
		}
		if ("C".equalsIgnoreCase(templateType) || "B".equalsIgnoreCase(templateType)) {
			final TemplateMetaData templateMetaData = templateService.getMediaPlanTemplateByName(ConstantStrings.CREATIVE_SPEC_TEMPALTE);
			templateId = templateMetaData.getTemplateId();
			LOGGER.info("Validating creative spec for optionID: " + optionID + " and templateId: " + templateId);
			templateGenerator.getCreativeTemplateVO(optionID, Long.valueOf(proposalForm.getProposalVersion()), templateId);
		}

		if ("AT".equalsIgnoreCase(templateType) || "A".equalsIgnoreCase(templateType) || "AC".equalsIgnoreCase(templateType)) {
			final Proposal proposal = proposalService.getProposalbyId(Long.valueOf(proposalForm.getId()));
			final Set<ProposalOption> proposalOptions = proposal.getProposalOptions();
			if (proposalOptions != null && !proposalOptions.isEmpty()) {
				if ("AC".equalsIgnoreCase(templateType) || "A".equalsIgnoreCase(templateType)) {
					final TemplateMetaData templateMetaData = templateService.getMediaPlanTemplateByName(ConstantStrings.CREATIVE_SPEC_TEMPALTE);
					creativeId = templateMetaData.getTemplateId();
				}
				for (ProposalOption proposalOption : proposalOptions) {
					if ("AT".equalsIgnoreCase(templateType) || "A".equalsIgnoreCase(templateType)) {
						templateGenerator.getExportTemplateVO(proposalOption.getId(), proposalOption.getLatestVersion().getProposalVersion(), Long.valueOf(proposalForm.getProposalTemplate()));
					}
					if ("AC".equalsIgnoreCase(templateType) || "A".equalsIgnoreCase(templateType)) {
						templateGenerator.getCreativeTemplateVO(proposalOption.getId(), proposalOption.getLatestVersion().getProposalVersion(), creativeId);
					}
				}
			}
		}

		ajaxResponse.getObjectMap().put("Valid", true);
		return ajaxResponse;
	}

	/**
	 * This method is used to Generate Media Template
	 * @param response
	 * @param proposalForm
	 * @param templateType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/generateMediaTemplate")
	public AjaxFormSubmitResponse generateMediaTemplate(final HttpServletResponse response, final ProposalForm proposalForm, @RequestParam final String templateType) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("generating Media Template for proposalId : " + proposalForm.getId());
		}
		final long proposalId = proposalForm.getId();
		final long optionID = proposalForm.getOptionId();
		final long proposalVersionId = Long.valueOf(proposalForm.getProposalVersion());
		if ("T".equalsIgnoreCase(templateType)) {
			final TemplateWorkBook templateWorkBook = getTemplateWorkBook(proposalForm, optionID, proposalVersionId);
			LOGGER.info("Downloading Media Template for proposalId : " + proposalId);
			downloadTemplate(templateWorkBook, response);
		} else if ("C".equalsIgnoreCase(templateType)) {
			final TemplateWorkBook templateWorkBook = getCreativeWorkBook(optionID, proposalVersionId);
			LOGGER.info("Downloading creative spec for proposalId : " + proposalId);
			downloadTemplate(templateWorkBook, response);
		} else if ("B".equalsIgnoreCase(templateType)) {
			LOGGER.info("Downloading Proposal Template and Creative spec for proposalId : " + proposalId);
			final List<TemplateWorkBook> workBookList = new ArrayList<TemplateWorkBook>();
			/**
			 * Generate Template, creative spec and add to the workbook list for
			 * download
			 */
			workBookList.add(getCreativeWorkBook(optionID, proposalVersionId));
			workBookList.add(getTemplateWorkBook(proposalForm, optionID, proposalVersionId));
			downloadTemplatesInZIP(workBookList, response);
		} else if ("AT".equalsIgnoreCase(templateType)) {
			LOGGER.info("Downloading template for all proposal option for proposalId : " + proposalId);
			downloadTemplatesInZIP(getAllTemplateWorkBookLst(proposalId, templateType, proposalForm), response);
		} else if ("AC".equalsIgnoreCase(templateType)) {
			LOGGER.info("Downloading creative spec for all proposal option for proposalId : " + proposalId);
			downloadTemplatesInZIP(getAllTemplateWorkBookLst(proposalId, templateType, proposalForm), response);
		} else if ("A".equalsIgnoreCase(templateType)) {
			LOGGER.info("Downloading Proposal Template and Creative spec for all proposal option proposalId : " + proposalId);
			final List<TemplateWorkBook> workBookList = new ArrayList<TemplateWorkBook>();
			/**
			 * Generate Template, creative spec and add to the workbook list for
			 * download
			 */
			workBookList.addAll(getAllTemplateWorkBookLst(proposalId, "AC", proposalForm));
			workBookList.addAll(getAllTemplateWorkBookLst(proposalId, "AT", proposalForm));
			downloadTemplatesInZIP(workBookList, response);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, proposalId);
		return ajaxResponse;
	}

	/**
	 * Get template work book list for all option of proposal
	 * @param proposalID
	 * @param templateType
	 * @param proposalForm
	 * @return
	 */
	private List<TemplateWorkBook> getAllTemplateWorkBookLst(final Long proposalID, final String templateType, final ProposalForm proposalForm) {
		final List<TemplateWorkBook> templateWorkBookLst = new ArrayList<TemplateWorkBook>();
		final Proposal proposal = proposalService.getProposalbyId(proposalID);
		if (proposal != null) {
			final Set<ProposalOption> proposalOptions = proposal.getProposalOptions();
			if (proposalOptions != null && !proposalOptions.isEmpty()) {
				for (ProposalOption proposalOption : proposalOptions) {
					if ("AC".equalsIgnoreCase(templateType)) {
						final TemplateWorkBook templateWorkBook = getCreativeWorkBook(proposalOption.getId(), proposalOption.getLatestVersion().getProposalVersion());
						final String outputFileName = proposalOption.getProposal().getName() + ConstantStrings.UNDER_SCORE + proposalOption.getName() + ConstantStrings.UNDER_SCORE
								+ ConstantStrings.CREATIVE_SPEC + templateWorkBook.getTemplateFileName().substring(templateWorkBook.getTemplateFileName().indexOf('.'));
						templateWorkBook.setTemplateOutputFileName(outputFileName);
						templateWorkBookLst.add(templateWorkBook);
					} else if ("AT".equalsIgnoreCase(templateType)) {
						final TemplateWorkBook templateWorkBook = getTemplateWorkBook(proposalForm, proposalOption.getId(), proposalOption.getLatestVersion().getProposalVersion());
						final String outputFileName = proposalOption.getProposal().getName() + ConstantStrings.UNDER_SCORE + proposalOption.getName()
								+ templateWorkBook.getTemplateFileName().substring(templateWorkBook.getTemplateFileName().indexOf('.'));
						templateWorkBook.setTemplateOutputFileName(outputFileName);
						templateWorkBookLst.add(templateWorkBook);
					}
				}
			}
		}
		return templateWorkBookLst;
	}

	/**
	 * Use to get template workbook
	 * @param proposalForm
	 * @param optionId
	 * @param proposalVersionId
	 * @return
	 */
	private TemplateWorkBook getTemplateWorkBook(final ProposalForm proposalForm, final Long optionId, final Long proposalVersionId) {
		final long templateId = Long.valueOf(proposalForm.getProposalTemplate());
		LOGGER.info("Generating Media Template for proposalId: " + optionId + " and templateId: " + templateId);
		return templateGenerator.generateTemplate(optionId, proposalVersionId, templateId);
	}

	/**
	 * Use to get creative workbook
	 * @param optionID
	 * @param proposalVersionId
	 * @return
	 */
	private TemplateWorkBook getCreativeWorkBook(final Long optionID, final Long proposalVersionId) {
		final TemplateMetaData templateMetaData = templateService.getMediaPlanTemplateByName(ConstantStrings.CREATIVE_SPEC_TEMPALTE);
		final long templateId = templateMetaData.getTemplateId();
		LOGGER.info("Generating creative spec for proposalId: " + optionID + " and templateId: " + templateId);
		return templateGenerator.generateCreativeSpec(optionID, proposalVersionId, templateId);
	}

	/**
	 * Used to download Template WorkBook
	 * @param templateWorkBook
	 * @param response
	 */
	private void downloadTemplate(final TemplateWorkBook templateWorkBook, final HttpServletResponse response) {
		final Workbook templateWorkbook = templateWorkBook.getTemplateWorkBook();
		response.setContentType(new MimetypesFileTypeMap().getContentType(templateWorkBook.getTemplateFileName()));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + templateWorkBook.getTemplateOutputFileName() + "\"");
		try {
			templateWorkbook.write(response.getOutputStream());
		} catch (IOException e) {
			throw new BusinessException(new CustomBusinessError(), e);
		}
	}

	/**
	 * This method is used to download template in zip file
	 * @param workBookList
	 * @param response
	 */
	private void downloadTemplatesInZIP(final List<TemplateWorkBook> workBookList, final HttpServletResponse response) {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "inline; filename=exportedTemplates.zip;");
		try {
			final ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			if (workBookList != null && !workBookList.isEmpty()) {
				for (TemplateWorkBook templateWorkBook : workBookList) {
					final Workbook templatebook = templateWorkBook.getTemplateWorkBook();
					// Adding templates, it should be converted into byte array and then put into zip
					zip.putNextEntry(new ZipEntry(templateWorkBook.getTemplateOutputFileName().replaceAll("/", "_")));

					final ByteArrayOutputStream bstream = new ByteArrayOutputStream();
					templatebook.write(bstream);
					zip.write(bstream.toByteArray());
				}
			}
			zip.flush();
			zip.close();
		} catch (IOException e) {
			throw new BusinessException(new CustomBusinessError(), e);
		}
	}

	/**
	 * Fetch all the sales categories
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getSalesCategoriesList")
	public Map<Long, String> getSalesCategory() {
		return proposalHelper.getSalesCategory();
	}

	/**
	 * Fetch all the advertiser info
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAdvertiserList")
	public Map<Long, String> getAdvertiser() {
		return proposalHelper.getAdvertiser();
	}

	/**
	 * Fetch all the Proposal statuses
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getProposalStatus")
	public Map<String, String> getProposalStatus() {
		return ProposalStatus.getProposalStatusMap();
	}

	/**
	 * Fetch all Agency info
	 * @return
	 */
	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getAgencyList")
	public Map<Long, String> getAgency() {
		return proposalHelper.getAgency();
	}

	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getUsersBasedOnRole")
	public Map<Long, String> getUsersBasedOnRole() {
		return proposalHelper.getUsersBasedOnRole(SecurityUtil.getProposalAssignToList());
	}

	@JsonIgnore
	@ResponseBody
	@RequestMapping("/getCampaignObjectives")
	public Map<Long, String> getCampaignObjectives() {
		return proposalHelper.getCampainObjectives();
	}

	/**
	 * Method Changes the Status of Proposal to Complete It returns an empty map
	 * just to avoid json Parsing error
	 * @param proposalId
	 * @return
	 * @throws CustomCheckedException
	 */
	@ResponseBody
	@RequestMapping("/proposalStatusComplete")
	public String proposalStatusComplete(@RequestParam("proposalId") final long proposalId) throws CustomCheckedException {
		final Proposal proposal = proposalService.getProposalbyId(proposalId);
		proposal.setProposalStatus(ProposalStatus.PROPOSED);
		proposalService.saveProposal(proposal.getId(), proposal);
		return "SUCCESS";
	}

	/**
	 * Create list of search criteria for searching the proposal.
	 * @param searchForm
	 * @return
	 */
	private List<RangeFilterCriteria> getFilterCriteriaList(final SearchProposalForm searchForm) {
		final List<RangeFilterCriteria> filterCriteriaLst = new ArrayList<RangeFilterCriteria>();
		RangeFilterCriteria filterCriteria;
		if ("package".equals(searchForm.getSearchType())) {
			// Adding default sales category while searching package
			final Proposal proposalData = proposalService.getProposalbyId(Long.valueOf(searchForm.getProposalID()));
			filterCriteria = getFilterCriteriaFor("packageSalescategory", SearchOption.EQUAL);
			filterCriteria.setSearchString(proposalData.getSosSalesCategoryId().toString());
			filterCriteriaLst.add(filterCriteria);

			if (StringUtils.isNotBlank(searchForm.getPackageName())) {
				filterCriteria = getFilterCriteriaFor("packageName", SearchOption.CONTAIN);
				filterCriteria.setSearchString(searchForm.getPackageName().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getBudgetFrom()) || StringUtils.isNotBlank(searchForm.getBudgetTo())) {
				filterCriteria = getFilterCriteriaFor("budget", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getBudgetFrom().trim());
				filterCriteria.setSearchStringTo(searchForm.getBudgetTo().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getValidFrom())) {
				filterCriteria = getFilterCriteriaFor("validFrom", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getValidFrom().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getValidTo())) {
				filterCriteria = getFilterCriteriaFor("validTo", SearchOption.BETWEEN);
				filterCriteria.setSearchStringTo(searchForm.getValidTo().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getExpiredPackages()) && StringUtils.equalsIgnoreCase("true", searchForm.getExpiredPackages())) {
				filterCriteria = getFilterCriteriaFor("validTo", SearchOption.LESS);
				filterCriteria.setSearchString(searchForm.getCurrentDate());
				filterCriteriaLst.add(filterCriteria);
			}
		} else {
			if (StringUtils.isNotBlank(searchForm.getProposalname())) {
				filterCriteria = getFilterCriteriaFor("proposalName", SearchOption.CONTAIN);
				filterCriteria.setSearchStringWtSQLChar(searchForm.getProposalname().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getProposalStatus())) {
				filterCriteria = getFilterCriteriaFor("status", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getProposalStatus());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getAgencyName())) {
				filterCriteria = getFilterCriteriaFor("agencyName", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getAgencyName());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getSalescategory())) {
				filterCriteria = getFilterCriteriaFor("salescategory", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getSalescategory());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getAdvertiserName())) {
				filterCriteria = getFilterCriteriaFor("advertiserName", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getAdvertiserName());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getCpmFrom()) || StringUtils.isNotBlank(searchForm.getCpmTo())) {
				filterCriteria = getFilterCriteriaFor("cpm", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getCpmFrom().trim());
				filterCriteria.setSearchStringTo(searchForm.getCpmTo().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getBudgetFrom()) || StringUtils.isNotBlank(searchForm.getBudgetTo())) {
				filterCriteria = getFilterCriteriaFor("budget", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getBudgetFrom().trim());
				filterCriteria.setSearchStringTo(searchForm.getBudgetTo().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			if (StringUtils.isNotBlank(searchForm.getImpressionFrom()) || StringUtils.isNotBlank(searchForm.getImpressionTo())) {
				filterCriteria = getFilterCriteriaFor("impressions", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getImpressionFrom().trim());
				filterCriteria.setSearchStringTo(searchForm.getImpressionTo().trim());
				filterCriteriaLst.add(filterCriteria);
			}

			// REQ:001, Search Criteria for my Proposal
			if (StringUtils.isNotBlank(searchForm.getMyProposal()) && "true".equalsIgnoreCase(searchForm.getMyProposal())) {
				final String userId = String.valueOf(SecurityUtil.getUser().getUserId());
				filterCriteria = getFilterCriteriaFor("assignedUser", SearchOption.EQUAL);
				filterCriteria.setSearchString(userId);
				filterCriteriaLst.add(filterCriteria);
				// now check for proposal status as "In Progress" or "Revised"
				filterCriteria = getFilterCriteriaFor("proposalStatus", SearchOption.EQUAL);
				// Create a search string for proposal status as IN clause
				filterCriteria.setSearchString(createSearchStringforProposalStatus());
				filterCriteriaLst.add(filterCriteria);
			}

			if (StringUtils.isNotBlank(searchForm.getDueDateFrom()) || StringUtils.isNotBlank(searchForm.getDueDateTo())) {
				filterCriteria = getFilterCriteriaFor("dueDateFromTo", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getDueDateFrom());
				filterCriteria.setSearchStringTo(searchForm.getDueDateTo());
				filterCriteriaLst.add(filterCriteria);
			}
			// Get filter criteria for Requested date
			if (StringUtils.isNotBlank(searchForm.getRequestedDateFrom()) || StringUtils.isNotBlank(searchForm.getRequestedDateTo())) {
				filterCriteria = getFilterCriteriaFor("requestedDateFromTo", SearchOption.BETWEEN);
				filterCriteria.setSearchStringFrom(searchForm.getRequestedDateFrom());
				filterCriteria.setSearchStringTo(searchForm.getRequestedDateTo());
				filterCriteriaLst.add(filterCriteria);
			}

			// REQ:006
			if (StringUtils.isNotBlank(searchForm.getAssignedUserId())) {
				filterCriteria = getFilterCriteriaFor("assignedUser", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getAssignedUserId());
				filterCriteriaLst.add(filterCriteria);
			}

			// REQ:006
			if (StringUtils.isNotBlank(searchForm.getCmpObjectiveId())) {
				filterCriteria = getFilterCriteriaFor("cmpObjective", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getCmpObjectiveId());
				filterCriteriaLst.add(filterCriteria);
			}

			if (StringUtils.isNotBlank(searchForm.getSosOrderId())) {
				filterCriteria = getFilterCriteriaFor("sosOrderId", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getSosOrderId().trim());
				filterCriteriaLst.add(filterCriteria);
			}
			
			if (StringUtils.isNotBlank(searchForm.getSosLineItemId())) {
				filterCriteria = getFilterCriteriaFor("sosLineItemId", SearchOption.EQUAL);
				filterCriteria.setSearchString(searchForm.getSosLineItemId().trim());
				filterCriteriaLst.add(filterCriteria);
			}
		}
		return filterCriteriaLst;
	}

	/**
	 * Returns {@link RangeFilterCriteria} by <code>field</code> and <code>operator</code>
	 * @param field
	 * @param operator
	 * @return
	 */
	private RangeFilterCriteria getFilterCriteriaFor(final String field, final SearchOption operator) {
		final RangeFilterCriteria filterCriteria = new RangeFilterCriteria();
		filterCriteria.setSearchField(field);
		filterCriteria.setSearchOper(operator.toString());
		return filterCriteria;
	}

	private String createSearchStringforProposalStatus() {
		final StringBuffer proposalStatus = new StringBuffer();
		proposalStatus.append(ConstantStrings.OPENING_BRACKET);
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ProposalStatus.INPROGRESS.name());
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ConstantStrings.COMMA);
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ProposalStatus.PROPOSED.name());
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ConstantStrings.COMMA);
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ProposalStatus.SOLD.name());
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ConstantStrings.COMMA);
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ProposalStatus.REVIEW.name());
		proposalStatus.append(ConstantStrings.SINGLE_QUOTE);
		proposalStatus.append(ConstantStrings.CLOSING_BRACKET);
		return proposalStatus.toString();
	}

	/**
	 * Copies all the line items from a Package to Proposal
	 * @param proposalForm
	 * @param lineitemNumbers
	 * @param version
	 * @param proposalValue
	 * @param optionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLineItems")
	public AjaxFormSubmitResponse getLineItems(final SearchProposalForm proposalForm, @RequestParam("lineitemNumbers") final int lineitemNumbers, 
			@RequestParam("version") final int version, @RequestParam("proposalValue") final Long proposalValue, @RequestParam("parentOptionId") final long optionId) {
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final StringBuffer lineItemIdStr = new StringBuffer();
		boolean flag = false;
		ReferenceDataMap referenceDataMap = null;
		List<LineItem> lineItemLst = new ArrayList<LineItem>();
		String priceType = ConstantStrings.EMPTY_STRING;
		double propAgencyMargin = 0.0;
		if ("proposal".equals(proposalForm.getSearchType())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Fetching LineItems for Proposal with Id: " + proposalForm.getProposalID());
			}
			final SortingCriteria sortingCriteria = new SortingCriteria("lineItemID", CustomDbOrder.ASCENDING);
			lineItemLst = proposalService.getProposalLineItems(proposalForm.getOptionId(), Long.valueOf(proposalForm.getProposalversion()), null, null, sortingCriteria);
			if (lineItemLst != null && !lineItemLst.isEmpty()) {
				referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemAssocList(lineItemLst);
			}
			final Proposal proposalSourceDBValue = proposalService.getProposalbyId(Long.valueOf(proposalForm.getProposalID()));
			priceType = proposalSourceDBValue.getPriceType();
			propAgencyMargin = proposalSourceDBValue.getAgencyMargin();
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Fetching LineItems for Package with Id: " + proposalForm.getPackageId());
			}
			priceType = "Net";
			propAgencyMargin = 0.0;
			final SortingCriteria sortingCriteria = new SortingCriteria("lineItemID", CustomDbOrder.DESCENDING);
			lineItemLst = packageService.getFilteredPackageLineItems(Long.valueOf(proposalForm.getPackageId()), null, null, sortingCriteria);
			referenceDataMap = proposalSOSService.getReferenceDataMapFromLineItemList(lineItemLst);
		}
		
		if (lineItemLst.size() > 300) {
			ajaxResponse.getObjectMap().put("isError", true);
			return ajaxResponse;
		}

		boolean isLineItemsPartiallyCopied = getLineItemIdsStr(lineItemLst, referenceDataMap, lineItemIdStr);
		if(proposalForm.isBreakablePkg() && isLineItemsPartiallyCopied){
			isLineItemsPartiallyCopied = false;
		}
		
		if (StringUtils.isNotBlank(lineItemIdStr.toString())) {
			final String[] lineItem = lineItemIdStr.toString().split(ConstantStrings.COMMA);
			Long[] lineItems = new Long[lineItem.length];
			for (int i = 0; i < lineItem.length; i++) {
				lineItems[i] = Long.valueOf(lineItem[i]);
			}

			flag = proposalService.saveCopiedLineItemsToProposal(proposalValue, optionId, Long.valueOf(version), proposalForm.getSearchType(), lineItems, isLineItemsPartiallyCopied,
					proposalForm.isCopiedFromExpired(), lineitemNumbers, priceType, false, propAgencyMargin);

			if (!flag) {
				proposalService.updateAllLineItemPricingStatus(optionId);
				proposalService.updateProposalVersionNetImpressionAndCPM(optionId, Long.valueOf(version));
			}
		}
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, flag);

		return ajaxResponse;
	}

	/**
	 * Constructs the comma separated list of line item Id's to be copied and
	 * returns true if line item list is greater than 300 else false.
	 * 
	 * @param lineItemLst
	 * @param referenceDataMap
	 * @param lineItemIdStr
	 * @return
	 */
	public boolean getLineItemIdsStr(final List<LineItem> lineItemLst, final ReferenceDataMap referenceDataMap, final StringBuffer lineItemIdStr) {
		boolean isLineItemsPartiallyCopied = false;
		if (lineItemLst != null && !lineItemLst.isEmpty()) {
			final Map<Long, Product> productMap = referenceDataMap.getProductMap();
			final Map<Long, SalesTarget> salesTargetMap = referenceDataMap.getSalesTargetMap();
			if (lineItemLst.size() > 300) {
				isLineItemsPartiallyCopied = true;
			} else {
				for (LineItem lineItem : lineItemLst) {
					boolean isProduct_active = true;
					boolean isSalestarget_active = true;
					boolean isProductSalesTargetCombo_active = true;
					// Setting product status
					if (!productMap.containsKey(lineItem.getSosProductId()) || !Constants.ACTIVE_STATUS.equalsIgnoreCase(productMap.get(lineItem.getSosProductId()).getStatus())
							|| productMap.get(lineItem.getSosProductId()).getDeleteDate() != null) {
						isProduct_active = false;
					}
					// Setting sales target status
					if (!proposalHelper.getSalesTargetStatusFromSalesTargetAssocList(salesTargetMap, lineItem)) {
						isSalestarget_active = false;
					}
					// // Setting product and sales target combo status
					if (isProduct_active && isSalestarget_active) {
						if (!proposalSOSService.isProductPlacementActive(lineItem.getSosProductId(), proposalHelper.convertSalesTargetAssocsToIds(lineItem.getLineItemSalesTargetAssocs()))) {
							isProductSalesTargetCombo_active = false;
						}
					}
					if (isProduct_active && isSalestarget_active && isProductSalesTargetCombo_active) {
						lineItemIdStr.append(lineItem.getLineItemID()).append(ConstantStrings.COMMA);
					}else{
						isLineItemsPartiallyCopied = true;
					}
				}
			}
		}

		if (StringUtils.isNotBlank(lineItemIdStr.toString())) {
			lineItemIdStr.replace(0, lineItemIdStr.length(), lineItemIdStr.substring(0, lineItemIdStr.lastIndexOf(ConstantStrings.COMMA)));
		}
		return isLineItemsPartiallyCopied;
	}

	public void setProposalService(final IProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}

	public void setPackageService(final IPackageService packageService) {
		this.packageService = packageService;
	}

	public ITemplateService getTemplateService() {
		return templateService;
	}

	public void setTemplateService(final ITemplateService templateService) {
		this.templateService = templateService;
	}

	public TemplateGenerator getTemplateGenerator() {
		return templateGenerator;
	}

	public void setTemplateGenerator(final TemplateGenerator templateGenerator) {
		this.templateGenerator = templateGenerator;
	}

	public void setProposalSOSService(final IProposalSOSService proposalSOSService) {
		this.proposalSOSService = proposalSOSService;
	}
}
