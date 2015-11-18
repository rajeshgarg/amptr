/**
 *
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.nyt.mpt.domain.Attribute;
import com.nyt.mpt.domain.AttributeType;
import com.nyt.mpt.domain.Creative;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.domain.Product;
import com.nyt.mpt.domain.ProductAttributeAssoc;
import com.nyt.mpt.domain.ProductCreativeAssoc;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.form.AttributeAssocForm;
import com.nyt.mpt.form.AttributeForm;
import com.nyt.mpt.form.ProductCreativeAssocForm;
import com.nyt.mpt.form.ProductForm;
import com.nyt.mpt.form.SalesTargetForm;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.ProposalHelper;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.validator.AttributeValidator;

/**
 * This <code>ManageProductController</code> contains all the methods for managing Products and its Creative and Attributes
 * 
 * @author amandeep.singh
 */
@Controller
@RequestMapping("/manageProduct/*")
public class ManageProductController extends AbstractBaseController {
	private static final String CREATIVES_CUSTOM = "creatives_custom";
	private static final Logger LOGGER = Logger.getLogger(ManageProductController.class);
	private static final String VIEW_DISPLAY_PAGE = "manageProductDisplayPage";
	private static final String GRID_DATA = "manageProductGridData";

	private static final String PRODUCT_FORM = "productDetailForm";

	private IProductService adProductService;
	private ICreativeService adCreativeService;
	private IAttributeService attributeService;
	private IDocumentService documentService;
	private ProposalHelper proposalHelper;

	/**
	 * Return Model and view for Manage Product
	 * @param productForm
	 * @return
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage(@ModelAttribute(PRODUCT_FORM) final ProductForm productForm) {
		final ModelAndView view = new ModelAndView(VIEW_DISPLAY_PAGE);
		view.addObject(PRODUCT_FORM, productForm);
		view.addObject("allCreatives", loadCreativeNames());
		return view;
	}

	/**
	 * Loads manage product master grid data
	 * @param tblGrid
	 * @return
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadProduct(@ModelAttribute final TableGrid<ProductForm> tblGrid) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setGridData(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Set product data to the table grid
	 * @param tblGrid
	 */
	private void setGridData(final TableGrid<ProductForm> tblGrid) {
		final FilterCriteria filterCriteria = tblGrid.getFilterCriteria();
		final List<Product> productLst = adProductService.getProductFilteredList(
							filterCriteria, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!productLst.isEmpty()) {
			final List<ProductForm> productFormList = convertDtoToForm(productLst);
			final List<Long> productIDLst = new ArrayList<Long>(productFormList.size());
			for (ProductForm productForm : productFormList) {
				productIDLst.add(productForm.getProductId());
			}
			final Map<Long, String> productCreatives = adProductService.getProductCreativesName(productIDLst);
			if (productCreatives != null && !productCreatives.isEmpty()) {
				for (ProductForm productForm : productFormList) {
					if (productCreatives.containsKey(productForm.getProductId())) {
						productForm.setCreativeName(productCreatives.get(productForm.getProductId()));
					}
				}
			}
			tblGrid.setGridData(productFormList, adProductService.getProductFilteredListcount(filterCriteria));
			setProductDocuments(tblGrid.getGridModel());
		}
	}

	/**
	 * Sets status of product document to the product form
	 * @param formList
	 */
	private void setProductDocuments(final List<ProductForm> formList) {
		if (formList == null || formList.isEmpty()) {
			return;
		}
		final List<Long> componentIds = new ArrayList<Long>(formList.size());
		for (ProductForm productForm : formList) {
			componentIds.add(productForm.getProductId());
		}

		final Map<Long, List<Document>> docMap = documentService.getDocumentsForComponents(componentIds, DocumentForEnum.PRODUCT.name());
		for (ProductForm productForm : formList) {
			final List<Document> docList = docMap.get(productForm.getProductId());
			if (docList != null && !docList.isEmpty()) {
				productForm.setHasDocument(true);
			}
		}
	}

	/**
	 * Converts List of {@link Product} to List of {@link ProductForm}
	 * @param productLst
	 * @return
	 */
	private List<ProductForm> convertDtoToForm(final List<Product> productLst) {
		final List<ProductForm> adProductFormList = new ArrayList<ProductForm>(productLst.size());
		for (Product adProduct : productLst) {
			final ProductForm adProductForm = new ProductForm();
			adProductForm.populateForm(adProduct);
			adProductFormList.add(adProductForm);
		}
		return adProductFormList;
	}

	/**
	 * Returns list of {@link SalesTargetForm} on the basis of {@link ProductForm}
	 * @param adProductForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSalesTarget")
	public List<SalesTargetForm> getSalesTarget(final ProductForm adProductForm) {
		final List<SalesTargetForm> formList = new ArrayList<SalesTargetForm>();
		final List<SalesTarget> salesTargetLst = adProductService.getSalesTarget(adProductForm.getProductId());
		for (SalesTarget salesTarget : salesTargetLst) {
			final SalesTargetForm salesTargetForm = new SalesTargetForm();
			salesTargetForm.populateForm(salesTarget);
			formList.add(salesTargetForm);
		}
		return formList;
	}

	/**
	 * Returns list of SalesTarget on the basis of <code>productId</code>
	 * @param productId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProductDetails")
	public Map<String, String> getProductDetails(@RequestParam final Long productId) {
		final Map<String, String> productData = new HashMap<String, String>();
		final StringBuilder creativeIds = new StringBuilder();
		final List<ProductCreativeAssoc> assoclist = adProductService.getProductCreatives(productId);
		for (ProductCreativeAssoc assoc : assoclist) {
			creativeIds.append(assoc.getCreative().getCreativeId()).append(ConstantStrings.COMMA);
		}
		productData.put("creativeIds", creativeIds.toString());
		return productData;
	}

	/**
	 * Saves/Updates product in the SOS database
	 * @param response
	 * @param productForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveProductDetails")
	public AjaxFormSubmitResponse saveProductDetails(final HttpServletResponse response, @ModelAttribute(PRODUCT_FORM) final ProductForm productForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Product Details. Product Form:" + productForm);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(PRODUCT_FORM, productForm);

		final List<Creative> creativeLst = adCreativeService.getCreativeList(true);
		String[] creativesId = null;
		if (StringUtils.isNotBlank(productForm.getCreatives())) {
			creativesId = productForm.getCreatives().split(ConstantStrings.COMMA);
		}
		boolean isError = false;
		int creativeLength = 0;
		if (creativeLst != null && creativesId != null) {
			for (Creative creatives : creativeLst) {
				for (int i = 0; i < creativesId.length; i++) {
					if (creatives.getCreativeId() == Long.valueOf(creativesId[i])) {
						creativeLength++;
					}
				}
			}
			if (creativeLength != creativesId.length) {
				isError = true;
			}
			if (isError) {
				results.rejectValue(CREATIVES_CUSTOM, ErrorCodes.InvalidCreatives, CREATIVES_CUSTOM, new Object[] { "Creatives" }, UserHelpCodes.InvalidCreatives);
			}
		} else if (creativesId != null && creativesId.length != 0 && creativeLst == null) {
			results.rejectValue(CREATIVES_CUSTOM, ErrorCodes.InvalidCreatives, CREATIVES_CUSTOM, new Object[] { "Creatives" }, UserHelpCodes.InvalidCreatives);
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			if (StringUtils.isNotBlank(productForm.getCreatives())) {
				adProductService.updateProductCreative(productForm.getProductId(), StringUtil.convertStringToLongList(productForm.getCreatives()));
			} else {
				adProductService.updateProductCreative(productForm.getProductId(), null);
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, 1);
		}
		return ajaxResponse;
	}

	/**
	 * Returns product placement for given product and sales target
	 * @param adProductForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProductPlacement")
	public List<String> getProductPlacement(final ProductForm adProductForm) {
		final List<String> placementList = new ArrayList<String>(1);
		String productplacement = adProductService.getProductPlacement(adProductForm.getProductId(), adProductForm.getSalestargetId());

		if (productplacement == null) {
			productplacement = messageSource.getMessage("label.generic.NotApplicable", null, null);
		}
		placementList.add(productplacement);
		return placementList;
	}

	/**
	 * Returns product placement for given product and sales target
	 * @param adProductForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getLineItemProductPlacement")
	public List<String> getLineItemProductPlacement(final ProductForm adProductForm) {
		final List<String> placementList = new ArrayList<String>(1);
		final String[] salesTargetIds = adProductForm.getSosSalesTargetId();
		if (salesTargetIds != null && salesTargetIds.length > 0) {
			final String productPlacement = proposalHelper.getProductPlacement(adProductForm.getProductId(),
					StringUtil.convertStringArrayToLongArray(salesTargetIds), adProductForm.getLineItemID());
			placementList.add(productPlacement);
		}
		return placementList;
	}

	/**
	 * Returns list of all creative linked to given product
	 * @param response
	 * @param tblGrid
	 * @param adProductForm
	 * @return
	 */
	@RequestMapping("/creativesgriddata")
	public ModelAndView loadProductCreatives(final HttpServletResponse response,
			final TableGrid<ProductCreativeAssocForm> tblGrid, @RequestParam final long productId) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		setProductCreativeData(tblGrid, productId);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Sets product creative data to the table grid
	 * @param tblGrid
	 * @param productId
	 */
	private void setProductCreativeData(final TableGrid<ProductCreativeAssocForm> tblGrid, final Long productId) {
		final List<ProductCreativeAssoc> prodCreativeAssocList = adProductService.getFilteredProductCreatives(productId, 
				tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!prodCreativeAssocList.isEmpty()) {
			final int productCreatives = adProductService.getFilteredProductCreativesCount(productId, tblGrid.getFilterCriteria());
			final List<ProductCreativeAssocForm> assocFormList = convertProductCreativeDtoToForm(prodCreativeAssocList);
			tblGrid.setGridData(assocFormList, productCreatives);
		}
	}

	/**
	 * Converts List of {@link ProductCreativeAssoc} to List of {@link ProductCreativeAssocForm}
	 * @param assocList
	 * @return
	 */
	private List<ProductCreativeAssocForm> convertProductCreativeDtoToForm(final List<ProductCreativeAssoc> assocList) {
		final List<ProductCreativeAssocForm> formList = new ArrayList<ProductCreativeAssocForm>(assocList.size());
		for (ProductCreativeAssoc assoc : assocList) {
			final ProductCreativeAssocForm assocForm = new ProductCreativeAssocForm();
			assocForm.populateForm(assoc);
			formList.add(assocForm);
		}
		return formList;
	}

	/**
	 * Returns the Map of creative by Id and Name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/creativenames")
	public Map<Long, String> loadCreativeNames() {
		final List<Creative> creativeLst = adCreativeService.getCreativeList(true);
		final Map<Long, String> creativeMap = new LinkedHashMap<Long, String>();
		for (Creative bean : creativeLst) {
			creativeMap.put(bean.getCreativeId(), bean.getName());
		}
		return creativeMap;
	}

	/**
	 * Load all attributes for the product
	 * @param tblGrid
	 * @param adProductForm
	 * @return
	 */
	@RequestMapping("/attributesgriddata")
	public ModelAndView loadProductAttributes(final TableGrid<AttributeAssocForm> tblGrid, final ProductForm adProductForm) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setAttributeDataToGrid(tblGrid, adProductForm);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Set product attributes to the grid
	 * @param tblGrid
	 * @param adProductForm
	 */
	private void setAttributeDataToGrid(final TableGrid<AttributeAssocForm> tblGrid, final ProductForm adProductForm) {
		final FilterCriteria criteria = new FilterCriteria("salesTargetId", adProductForm.getSalestargetId() == -1 ? null
				: String.valueOf(adProductForm.getSalestargetId()), SearchOption.EQUAL.name());
		final List<ProductAttributeAssoc> prodAttributeAssocLst = adProductService.getFilteredProductAttributes(
				adProductForm.getProductId(), criteria, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!prodAttributeAssocLst.isEmpty()) {
			final int attributeCount = adProductService.getFilteredProductAttributesCount(adProductForm.getProductId(), criteria);
			final List<AttributeAssocForm> adAttributeFormLst = convertAttributeDtoToForm(prodAttributeAssocLst);
			tblGrid.setGridData(adAttributeFormLst, attributeCount);
		}
	}

	/**
	 * Converts List of {@link ProductAttributeAssoc} to List of {@link AttributeAssocForm}
	 * @param prodAttrAssocList
	 * @return
	 */
	private List<AttributeAssocForm> convertAttributeDtoToForm(final List<ProductAttributeAssoc> prodAttrAssocList) {
		final List<AttributeAssocForm> attributeFormList = new ArrayList<AttributeAssocForm>(prodAttrAssocList.size());
		for (ProductAttributeAssoc productAttributeAssoc : prodAttrAssocList) {
			final AttributeAssocForm attributeForm = new AttributeAssocForm();
			final Attribute bean = productAttributeAssoc.getAttribute();
			attributeForm.populateForm(bean);
			attributeForm.setAttributeValue(productAttributeAssoc.getAttributeValue());
			attributeForm.setAssocId(String.valueOf(productAttributeAssoc.getAssociationId()));
			attributeFormList.add(attributeForm);
		}
		return attributeFormList;
	}

	/**
	 * Removes attribute association for product
	 * @param response
	 * @param attributeForm
	 * @param productForm
	 */
	@RequestMapping("/deleteproducattribute")
	public void deleteProductAttributes(final HttpServletResponse response, final AttributeForm attributeForm, final ProductForm productForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting Product Attribute. attributeId: " + attributeForm.getAttributeId() + " productId: " + productForm.getProductId());
		}
		final Attribute attribute = new Attribute();
		attribute.setAttributeId(Long.valueOf(attributeForm.getAttributeId()));
		final ProductAttributeAssoc assoc = new ProductAttributeAssoc();
		assoc.setAttribute(attribute);
		assoc.setProductId(productForm.getProductId());
		assoc.setSalesTargetId(getSalesTargetId(productForm.getSalestargetId()));
		adProductService.deleteLineItemAttribute(assoc);
		response.setHeader("Content-Type", "application/json");
	}

	private Long getSalesTargetId(final long salesTargetId) {
		return salesTargetId == -1 ? null : Long.valueOf(salesTargetId);
	}

	/**
	 * Returns all linked attribute to the product
	 * @param productForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAttributenames")
	public List<Attribute> getAttributenames(final ProductForm productForm) {
		final List<Attribute> attributeLst = attributeService.getAttributeList(true, AttributeType.PRODUCT);
		final List<ProductAttributeAssoc> productAttributeAssocLst = adProductService.getLineItemAttributes(productForm.getProductId(),
				getSalesTargetId(productForm.getSalestargetId()));
		if (!productAttributeAssocLst.isEmpty()) {
			for (ProductAttributeAssoc productAttributeAssoc : productAttributeAssocLst) {
				for (int i = 0; i < attributeLst.size(); i++) {
					if (attributeLst.get(i).getAttributeId() == productAttributeAssoc.getAttribute().getAttributeId()) {
						attributeLst.remove(i);
					}
				}
			}
		}
		return attributeLst;
	}

	/**
	 * Update product attribute association value
	 * @param response
	 * @param adProductForm
	 * @param attributeForm
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateattributesgriddata")
	public AjaxFormSubmitResponse updateProductAttributes(final HttpServletResponse response, final ProductForm adProductForm, final AttributeForm attributeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating Product Attributes. ProductId: " + adProductForm.getProductId() + " attributeId: " + attributeForm.getAttributeId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("attributeForm", attributeForm);
		new AttributeValidator().validateForAttributeValue(attributeForm, results);

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			final ProductAttributeAssoc assoc = new ProductAttributeAssoc();
			assoc.setProductId(adProductForm.getProductId());
			assoc.setSalesTargetId(getSalesTargetId(adProductForm.getSalestargetId()));
			assoc.setAttributeValue(attributeForm.getAttributeValue());

			final Attribute attribute = new Attribute();
			attribute.setAttributeId(Long.valueOf(attributeForm.getAttributeId()));

			assoc.setAttribute(attribute);
			if (attribute.getAttributeId() == 0) {
				attribute.setAttributeId(Long.valueOf(attributeForm.getAttributeName()));
				adProductService.addLineItemAttribute(assoc);
			} else {
				adProductService.updateLineItemAttribute(assoc);
			}
		}
		return ajaxResponse;
	}

	/**
	 * Returns Product details for the given product Id
	 * @param productId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getProductDetail")
	public ProductForm getProductDetail(@RequestParam final long productId) {
		final Product product = adProductService.getProductById(productId);
		final ProductForm propForm = new ProductForm();
		propForm.setProductName(product.getName());
		propForm.setTypeName(product.getTypeName().getProductTypeName());
		return propForm;
	}

	/**
	 * Return Model and view to display product, sales Target default values
	 * @param productForm
	 * @return
	 */
	@RequestMapping("/getProductSalesTargetDefaultValue")
	public ModelAndView viewProductSalesDefaultValue(final ProductForm productForm) {
		final ModelAndView view = new ModelAndView("productSalesDefaultValue");
		final FilterCriteria criteria = new FilterCriteria("salesTargetId", String.valueOf(productForm.getSalestargetId()), SearchOption.EQUAL.name());
		final List<ProductAttributeAssoc> prodAttributeAssocLst = adProductService.getFilteredProductAttributes(productForm.getProductId(), criteria, null, null);
		final Map<String, String> returnMap = new LinkedHashMap<String, String>(6);
		returnMap.put("SOR", ConstantStrings.EMPTY_STRING);
		returnMap.put("FLIGHT INFO", ConstantStrings.EMPTY_STRING);
		returnMap.put("GEO TARGET INFO", ConstantStrings.EMPTY_STRING);
		returnMap.put("US IMPRESSION GUARANTEE", ConstantStrings.EMPTY_STRING);
		returnMap.put("FREQUENCY CAP", ConstantStrings.EMPTY_STRING);
		returnMap.put("ADDITIONAL DEFAULTS", ConstantStrings.EMPTY_STRING);
		if (!prodAttributeAssocLst.isEmpty()) {
			for (ProductAttributeAssoc productAttributeAssoc : prodAttributeAssocLst) {
				if (returnMap.containsKey(StringUtils.upperCase(productAttributeAssoc.getAttribute().getAttributeName().trim()))) {
					returnMap.put(StringUtils.upperCase(productAttributeAssoc.getAttribute().getAttributeName().trim()), productAttributeAssoc.getAttributeValue());
				}
			}
		}
		view.addObject("attributesMap", returnMap);
		return view;
	}

	public void setDocumentService(final IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setAdCreativeService(final ICreativeService adCreativeService) {
		this.adCreativeService = adCreativeService;
	}

	public void setAttributeService(final IAttributeService attributeService) {
		this.attributeService = attributeService;
	}

	public void setAdProductService(final IProductService adProductService) {
		this.adProductService = adProductService;
	}

	public void setProposalHelper(final ProposalHelper proposalHelper) {
		this.proposalHelper = proposalHelper;
	}
}
