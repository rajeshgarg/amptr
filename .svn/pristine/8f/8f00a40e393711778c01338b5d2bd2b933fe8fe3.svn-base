/**
 *
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

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
import com.nyt.mpt.domain.CreativeAttributeValue;
import com.nyt.mpt.domain.Document;
import com.nyt.mpt.form.AttributeAssocForm;
import com.nyt.mpt.form.AttributeForm;
import com.nyt.mpt.form.CreativeForm;
import com.nyt.mpt.service.IAttributeService;
import com.nyt.mpt.service.ICreativeService;
import com.nyt.mpt.service.IDocumentService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.DocumentForEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.validator.AttributeValidator;
import com.nyt.mpt.validator.CreativeValidator;

/**
 * This <code>ManageCreativeController</code> class includes all the methods for Managing Creative and its Attributes
 * 
 * @author surendra.singh
 */
@Controller
@RequestMapping("/manageCreative/*")
public class ManageCreativeController extends AbstractBaseController {

	private static final String CREATIVE_FORM = "creativeForm";

	private static final String DISPLAY_PAGE = "manageCreativeDisplayPage";

	private static final String GRID_DATA = "manageCreativeGridData";

	private ICreativeService creativeService;

	private IAttributeService attributeService;

	private IDocumentService documentService;

	private static final Logger LOGGER = Logger.getLogger(ManageCreativeController.class);

	/**
	 * This method render <tt>Manage Creative<tt> view.
	 * 
	 * @param creativeForm
	 * @param attrAssocForm
	 * @return <tt>Manage Creative<tt> view.
	 */
	@RequestMapping("/viewdetail")
	public ModelAndView displayPage(@ModelAttribute(CREATIVE_FORM) final CreativeForm creativeForm,
			@ModelAttribute("attributeAssocForm") final AttributeAssocForm attrAssocForm) {
		final ModelAndView view = new ModelAndView(DISPLAY_PAGE);
		view.addObject(CREATIVE_FORM, creativeForm);
		return view;
	}

	/**
	 * This method is used for loading the data in the <tt>Creative<tt> grid 
	 * based on <tt>Filter Criteria<tt>, <tt>Pagination Criteria<tt>
	 * and <tt>Sorting Criteria<tt>.
	 * 
	 * @param tblGrid
	 * @return <tt>Creative<tt> grid data based on <tt>Filter Criteria<tt>,
	 *         <tt>Pagination Criteria<tt> and <tt>Sorting Criteria<tt>.
	 */
	@RequestMapping("/loadgriddata")
	public ModelAndView loadCreativeGridData(@ModelAttribute final TableGrid<CreativeForm> tblGrid) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setCreativeDataToGrid(tblGrid);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Set all the <tt>Creative<tt> data in the {@link TableGrid} based on 
	 * <tt>Filter Criteria<tt>, <tt>Pagination Criteria<tt> and <tt>Sorting Criteria<tt>.
	 * 
	 * @param tblGrid
	 */
	private void setCreativeDataToGrid(final TableGrid<CreativeForm> tblGrid) {
		final List<Creative> creativeList = creativeService.getFilteredCreativeList(
				tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!creativeList.isEmpty()) {
			final List<CreativeForm> creativeFormList = convertCreativeDtoToForm(creativeList);
			setCreativeDocuments(creativeFormList);
			tblGrid.setGridData(creativeFormList, creativeService.getFilteredCreativeListCount(tblGrid.getFilterCriteria()));
		}
	}

	/**
	 * Set <tt>Creative<tt> document status if any document is associated.
	 * This method collect all the creative id in a list and based on these values
	 * this method get all the associated {@link Document}.
	 * 
	 * @param formList
	 */
	private void setCreativeDocuments(final List<CreativeForm> formList) {
		if (formList == null || formList.isEmpty()) {
			return;
		}
		final List<Long> componentIds = new ArrayList<Long>();
		for (CreativeForm creativeForm : formList) {
			componentIds.add(creativeForm.getCreativeId());
		}
		final Map<Long, List<Document>> docMap = documentService.getDocumentsForComponents(componentIds, DocumentForEnum.CREATIVE.name());
		for (CreativeForm creativeForm : formList) {
			final List<Document> docList = docMap.get(creativeForm.getCreativeId());
			if (docList != null && !docList.isEmpty()) {
				creativeForm.setHasDocument(true);
			}
		}
	}

	/**
	 * Return list of {@link CreativeForm}. This method populate
	 * {@link Creative} data into {@link CreativeForm}.
	 * 
	 * @param creativeLst
	 * @return list of {@link CreativeForm}. This method populate
	 *         {@link Creative} data into {@link CreativeForm}.
	 */
	private List<CreativeForm> convertCreativeDtoToForm(final List<Creative> creativeLst) {
		final List<CreativeForm> creativeFormList = new ArrayList<CreativeForm>();
		for (Creative adCreative : creativeLst) {
			final CreativeForm creativeForm = new CreativeForm();
			creativeForm.populateForm(adCreative);
			creativeFormList.add(creativeForm);
		}
		return creativeFormList;
	}

	/**
	 * This method return validation message either any validation fail or name
	 * of the <tt>Creative<tt> is duplicate. After that this method save or update
	 * <tt>Creative<tt>.
	 * 
	 * @param response
	 * @param creativeForm
	 * @return validation message either any validation fail or name of the
	 *         <tt>Creative<tt> is duplicate. After save or update
	 *         <tt>Creative<tt> return {@link Creative} id.
	 */
	@ResponseBody
	@RequestMapping("/savecreative")
	public AjaxFormSubmitResponse saveCreative(final HttpServletResponse response, @ModelAttribute(CREATIVE_FORM) final CreativeForm creativeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving creative form for creativeId: " + creativeForm.getCreativeId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult(CREATIVE_FORM, creativeForm);
		new CreativeValidator().validate(creativeForm, results);

		final boolean duplicateCreative = creativeService.isDuplicateCreativeName(creativeForm.getName(), creativeForm.getCreativeId());
		if (duplicateCreative) {
			results.rejectValue(ConstantStrings.NAME, ErrorCodes.DuplicateCreativeEntry, ConstantStrings.NAME, new Object[] { creativeForm.getName() }, UserHelpCodes.DuplicateCreativeName);
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			long crativeId = 0;
			final Creative creative = creativeForm.populate(new Creative());
			if (creative.getCreativeId() == 0) {
				crativeId = creativeService.createCreative(creative);
			} else {
				crativeId = creativeService.updateCreative(creative, creativeForm.isForceUpdate());
			}
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, crativeId);
		}
		return ajaxResponse;
	}

	/**
	 * This method delete {@link Creative} based on <tt>Creative<tt> id 
	 * and return this deleted creative's id.
	 * 
	 * @param creativeForm
	 * @return deleted {@link Creative} id.
	 */
	@ResponseBody
	@RequestMapping("/deletecreative")
	public long deleteCreative(final CreativeForm creativeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting creative with id:" + creativeForm.getCreativeId());
		}
		final Creative creative = new Creative();
		creative.setCreativeId(creativeForm.getCreativeId());
		return creativeService.deleteCreative(creative);
	}
	
	/**
	 * This method is used for loading the data in the <tt>Creative Attribute<tt> grid 
	 * based on <tt>Filter Criteria<tt>, <tt>Pagination Criteria<tt> and <tt>Sorting Criteria<tt>.
	 * 
	 * @param response
	 * @param tblGrid
	 * @param creativeForm
	 * @return <tt>Creative Attribute<tt> grid data based on <tt>Filter Criteria<tt>,
	 *         <tt>Pagination Criteria<tt> and <tt>Sorting Criteria<tt>.
	 */
	@RequestMapping("/attributesgriddata")
	public ModelAndView loadCreativeAttributes(final HttpServletResponse response, final TableGrid<AttributeAssocForm> tblGrid, final CreativeForm creativeForm) {
		final ModelAndView view = new ModelAndView(GRID_DATA);
		this.setAttributeDataToGrid(tblGrid, creativeForm.getCreativeId());
		view.addObject(tblGrid);
		return view;
	}
	
	/**
	 * Set all the {@link CreativeAttributeValue} data in the {@link TableGrid} based on 
	 * <tt>Filter Criteria<tt>, <tt>Pagination Criteria<tt> and <tt>Sorting Criteria<tt>.
	 * 
	 * @param tblGrid
	 * @param creativeId
	 */
	public void setAttributeDataToGrid(final TableGrid<AttributeAssocForm> tblGrid, final long creativeId) {
		final List<CreativeAttributeValue> creativeAttrSet = creativeService.getFilteredCreativeAttributeList(creativeId,
				tblGrid.getFilterCriteria(), tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());

		if (!creativeAttrSet.isEmpty()) {
			final List<AttributeAssocForm> attributeFormList = convertAttributeDtoToForm(creativeAttrSet, creativeId);

			final int count = creativeService.getFilteredCreativeAttributeListCount(creativeId, tblGrid.getFilterCriteria());
			tblGrid.setGridData(attributeFormList, count);
		}
	}

	/**
	 * Return list of {@link AttributeAssocForm}. This method populate data from
	 * {@link CreativeAttributeValue} to {@link AttributeAssocForm}.
	 * 
	 * @param creativeAttrSet
	 * @param creativeId
	 * @return list of {@link AttributeAssocForm}.
	 */
	private List<AttributeAssocForm> convertAttributeDtoToForm(final List<CreativeAttributeValue> creativeAttrSet, final long creativeId) {
		final List<AttributeAssocForm> attributeFormList = new ArrayList<AttributeAssocForm>();
		for (CreativeAttributeValue creativeAttrMap : creativeAttrSet) {
			final AttributeAssocForm attributeForm = new AttributeAssocForm();
			final Attribute bean = creativeAttrMap.getAttribute();
			attributeForm.populateForm(bean);
			attributeForm.setId(creativeId);
			attributeForm.setVersion(creativeAttrMap.getVersion());
			attributeForm.setAttributeValue(creativeAttrMap.getAttributeValue());
			attributeForm.setCreatedDate(DateUtil.getGuiDateString(creativeAttrMap.getCreatedDate()));
			attributeFormList.add(attributeForm);
		}
		return attributeFormList;
	}

	/**
	 * Return list of active {@link Attribute} whose
	 * <tt>Attribute Type<tt> is {@link AttributeType.CREATIVE}. 
	 * This method removes all those {@link Attribute} who have any association with 
	 * given <tt>Creative<tt> id.
	 * 
	 * @param response
	 * @param adCreativeForm
	 * @return list of active {@link Attribute} whose <tt>Attribute Type<tt> is
	 *         {@link AttributeType.CREATIVE}.
	 */
	@ResponseBody
	@RequestMapping("/attributeNames")
	public List<Attribute> loadAttributeNames(final HttpServletResponse response, final CreativeForm adCreativeForm) {
		final List<Attribute> attributeLst = attributeService.getAttributeList(true, AttributeType.CREATIVE);
		final Set<CreativeAttributeValue> creativeAttrSet = creativeService.getCreativeAttribute(adCreativeForm.getCreativeId());
		if (!creativeAttrSet.isEmpty()) {
			for (CreativeAttributeValue creativeAttr : creativeAttrSet) {
				for (int i = 0; i < attributeLst.size(); i++) {
					if (attributeLst.get(i).getAttributeId() == creativeAttr.getAttribute().getAttributeId()) {
						attributeLst.remove(i);
					}
				}
			}
		}
		return attributeLst;
	}

	/**
	 * This method return validation message if any validation fail 
	 * After that this method save or update both {@link Attribute} and 
	 * it's association in {@link CreativeAttributeValue}. 
	 * 
	 * @param response
	 * @param creativeForm
	 * @param attributeForm
	 */
	@ResponseBody
	@RequestMapping("/updateCreativeAttributesValue")
	public AjaxFormSubmitResponse updateCreativeAttributes(final HttpServletResponse response,
			@RequestParam final long creativeId, final AttributeForm attributeForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Updating creative attributes. creativeId: " + creativeId + " attributeForm: " + attributeForm);
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		final CustomBindingResult results = new CustomBindingResult("attributeForm", attributeForm);
		new AttributeValidator().validateForAttributeValue(attributeForm, results);

		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			final Attribute bean = attributeForm.populate(new Attribute());
			if (bean.getAttributeId() == 0) {
				bean.setAttributeId(Long.valueOf(attributeForm.getAttributeName()));
				creativeService.addCreativeAttribute(creativeId, bean, attributeForm.getAttributeValue());
			} else {
				creativeService.updateCreativeAttribute(creativeId, bean, attributeForm.getAttributeValue());
			}
		}
		return ajaxResponse;
	}

	/**
	 * Remove Attribute from creative
	 * @param response
	 * @param creativeForm
	 * @param attributeForm
	 */
	@ResponseBody
	@RequestMapping("/deleteCreativeAttribute")
	public int deleteCreativeAttribute(@RequestParam final long creativeId, @RequestParam final long attributeId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting creative attribute . creativeId: " + creativeId + " attributeId: " + attributeId);
		}
		final Attribute bean = new Attribute();
		bean.setAttributeId(attributeId);
		return creativeService.deleteCreativeAttribute(creativeId, bean);
	}

	/**
	 * return creative details
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCreativeDesc")
	public CreativeForm getAttributeDescription(final CreativeForm form) {
		final Creative creative = creativeService.getCreative(form.getCreativeId());
		final CreativeForm creativeForm = new CreativeForm();
		creativeForm.populateForm(creative);
		return creativeForm;
	}

	public void setDocumentService(final IDocumentService documentService) {
		this.documentService = documentService;
	}

	public void setCreativeService(final ICreativeService creativeService) {
		this.creativeService = creativeService;
	}

	public void setAttributeService(final IAttributeService attributeService) {
		this.attributeService = attributeService;
	}
}
