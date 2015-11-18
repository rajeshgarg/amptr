/**
 * 
 */
package com.nyt.mpt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.domain.SalesTarget;
import com.nyt.mpt.form.EmailSendScheduleForm;
import com.nyt.mpt.service.IProductService;
import com.nyt.mpt.service.impl.EmailScheduleService;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.TableGrid;
import com.nyt.mpt.util.enums.EmailScheduleFrequency;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.SearchOption;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.enums.Weekdays;
import com.nyt.mpt.util.filter.FilterCriteria;
import com.nyt.mpt.validator.EmailScheduleValidator;

/**
 * This <code>ManageEmailScheduleController</code> class includes all the methods related to Managing Email
 * 
 * @author sachin.ahuja
 */

@Controller
@RequestMapping("/emailSchedule/*")
public class ManageEmailScheduleController extends AbstractBaseController {
	
	private final static String EMAIL_SCHEDULE_FORM = "emailSendScheduleForm";
	
	private final static Logger LOGGER = Logger.getLogger(ManageEmailScheduleController.class);

	private IProductService productService;
	
	private EmailScheduleService emailScheduleService;
	
	/**
	 * Returns the {@link ModelAndView}
	 * @param form
	 * @return
	 */
	@RequestMapping("/viewDetail")
	public ModelAndView displayPage(@ModelAttribute(EMAIL_SCHEDULE_FORM) final EmailSendScheduleForm form) {
		final ModelAndView view = new ModelAndView("manageEmailSchedule");
		view.addObject("productMap", productService.getProductsByAvailsId(13, true));
		view.addObject("weekDays", Weekdays.getWeekDayMap());
		view.addObject("frequency", EmailScheduleFrequency.getFrequencyMap());
		view.addObject(EMAIL_SCHEDULE_FORM, form);
		return view;
	}

	/**
	 * @param tblGrid
	 * @param salesTargetId
	 * @param productId
	 * @return
	 */
	@RequestMapping("/loadGridData")
	public ModelAndView loadGridData(@ModelAttribute final TableGrid<EmailSendScheduleForm> tblGrid, 
			@RequestParam final long salesTargetId, @RequestParam final long productId) {
		final ModelAndView view = new ModelAndView("emailSendScheduleGridData");
		setEmailSendScheduleDataToGrid(tblGrid, productId, salesTargetId);
		view.addObject(tblGrid);
		return view;
	}

	/**
	 * Returns all the Sales Target
	 * @param productId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSalesTargetList")
	public Map<Long, String> getSalesTargetList(@RequestParam final Long productId){
		final List<SalesTarget> salesTargets = productService.getSalesTarget(productId);
		final Map<Long, String> salesTargetMap = new HashMap<Long, String>(salesTargets.size());
		for (SalesTarget salesTarget : salesTargets) {
			salesTargetMap.put(salesTarget.getSalesTargetId(), salesTarget.getSalesTargeDisplayName());
		}
		return salesTargetMap;
	}
	
	/**
	 * Saves/Updates Email Schedule
	 * @param 	response
	 * 			{@link AjaxFormSubmitResponse}
	 * @param 	scheduleForm
	 * 			{@link EmailSendScheduleForm} to be saved/updated in the AMPT db
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveEmailSendSchedule")
	public AjaxFormSubmitResponse saveEmailSendSchedule(final HttpServletResponse response, 
			@ModelAttribute(EMAIL_SCHEDULE_FORM) final EmailSendScheduleForm scheduleForm) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Saving Email send schedule for schedule id - " + scheduleForm.getScheduleDetailId());
		}
		final AjaxFormSubmitResponse ajaxResponse = new AjaxFormSubmitResponse(getMessageSource());
		
		final CustomBindingResult results = new CustomBindingResult(EMAIL_SCHEDULE_FORM, scheduleForm);
		new EmailScheduleValidator().validate(scheduleForm, results);
		
		EmailSchedule bean = scheduleForm.populate(new EmailSchedule());
		//checking valid Date Range
		final boolean duplicateEmailDate = results.hasErrors() ? false : emailScheduleService.findDuplicateDataExists(bean);
		if (duplicateEmailDate) {
			results.rejectValue("emailStartDate", ErrorCodes.DuplicateEmailDetailsEntry, "emailStartDate", 
					new Object[] { "Start Date" }, UserHelpCodes.DuplicateEmailDetailsName);
			if (!"forever".equals(scheduleForm.getEnds())) {
				results.rejectValue("emailEndDate", ErrorCodes.DuplicateEmailDetailsEntry, "emailEndDate", 
						new Object[] { "End Date" }, UserHelpCodes.DuplicateEmailDetailsName);
			} else {
				results.rejectValue("forever", ErrorCodes.DuplicateEmailDetailsEntry, "forever", 
						new Object[] { "Forever" }, UserHelpCodes.DuplicateEmailDetailsName);
			}
		}
		if (results.hasErrors()) {
			return constructResponse(response, ajaxResponse, results);
		} else {
			//check for update or save Email Schedule
			if (Long.valueOf(scheduleForm.getScheduleId()) == 0) {
				bean = emailScheduleService.saveEmailSchedule(bean);
			} else {
				bean = emailScheduleService.updateEmailSchedule(bean);
			}
		}
		final EmailScheduleDetails details = bean.getEmailSchedules().get(0);
		scheduleForm.populateScheduleDetails(details);
		ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, scheduleForm);
		return ajaxResponse;
	}

	/**
	 * Deletes {@link EmailSchedule} from the AMPT database
	 * @param 	scheduleDetailId
	 * 			{@link EmailSchedule}'s Id to be delted from the AMPT db
	 */
	@ResponseBody
	@RequestMapping("/deleteEmailScheduleDetail")
	public void deleteScheduleDetail(@RequestParam final long scheduleDetailId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Deleting email send schedule detail for scheduleDetailId: " + scheduleDetailId);
		}
		final  EmailScheduleDetails detail = new EmailScheduleDetails();
		detail.setEmailScheduleDetailsId(scheduleDetailId);
		emailScheduleService.deleteEmailSchedule(scheduleDetailId);
	}
	
	/**
	 * Returns the {@link EmailSchedule}'s Id
	 * @param productId
	 * @param salesTargetId
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping("/getEmailScheduleId")
	public Long getEmailScheduleId(@RequestParam final Long productId, @RequestParam final Long salesTargetId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Searching Email Schedule for productId: " + productId + "& salesTargetId: " + salesTargetId);
		}
		List<EmailSchedule> emailScheduleList = Collections.EMPTY_LIST;
		final List<FilterCriteria> filterCriteriaList = new ArrayList<FilterCriteria>();
		if (productId != null && salesTargetId != null) {
			filterCriteriaList.add(new FilterCriteria("productId", String.valueOf(productId), SearchOption.EQUAL.toString()));
			filterCriteriaList.add(new FilterCriteria("salesTargetId", String.valueOf(salesTargetId), SearchOption.EQUAL.toString()));
			emailScheduleList = emailScheduleService.getFilteredEmailDetailList(filterCriteriaList, null, null);
		}
		return emailScheduleList.size() > 0 ? emailScheduleList.get(0).getEmailScheduleId() : 0L;
	}
	
	/**
	 * Sets the {@link TableGrid}
	 * @param tblGrid
	 * @param productId
	 * @param salesTargetId
	 */
	private void setEmailSendScheduleDataToGrid(final TableGrid<EmailSendScheduleForm> tblGrid, final long productId, final long salesTargetId) {
		final List<FilterCriteria> filterCriteriaList = new ArrayList<FilterCriteria>();
		filterCriteriaList.add(tblGrid.getFilterCriteria());
		filterCriteriaList.add(new FilterCriteria("productId", String.valueOf(productId), SearchOption.EQUAL.toString()));
		filterCriteriaList.add(new FilterCriteria("salesTargetId", String.valueOf(salesTargetId), SearchOption.EQUAL.toString()));

		final List<EmailSchedule> emailScheduleList = emailScheduleService.getFilteredEmailDetailList(filterCriteriaList, tblGrid.getPaginationCriteria(), tblGrid.getSortingCriteria());
		if (!emailScheduleList.isEmpty()) {
			final int count = emailScheduleService.getFilteredEmailDetailListSize(filterCriteriaList);
			tblGrid.setGridData(convertEmailScheduletoToForm(emailScheduleList), count);
		}
	}

	/**
	 * Returns the List of {@link EmailSendScheduleForm} to be shown in the UI from the List of {@link EmailSchedule} from the db
	 * @param 	scheduleList
	 * 			List of {@link EmailSchedule} from the db
	 * @return
	 * 			Returns the List of {@link EmailSendScheduleForm}
	 */
	private List<EmailSendScheduleForm> convertEmailScheduletoToForm(final List<EmailSchedule> scheduleList) {
		final List<EmailSendScheduleForm> emailScheduleList = new ArrayList<EmailSendScheduleForm>();
		for (EmailSchedule emailSchedule : scheduleList) {
			for(EmailScheduleDetails emailScheduleDetails : emailSchedule.getEmailSchedules() ){
				final EmailSendScheduleForm emailSendScheduleForm = new EmailSendScheduleForm();
				emailSendScheduleForm.populateScheduleDetails(emailScheduleDetails);
				emailScheduleList.add(emailSendScheduleForm);
			}
		}
		return emailScheduleList;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public void setEmailScheduleService(EmailScheduleService emailScheduleService) {
		this.emailScheduleService = emailScheduleService;
	}	
}
