/**
 * 
 */
package com.nyt.mpt.validator;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class is used to validate required parameter for avails
 * @author manish.kesarwani
 */
public class AvailsValidator extends AbstractValidator implements Validator {
	private static final Logger LOGGER = Logger.getLogger(AvailsValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return LineItemForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}

		final LineItemForm form = (LineItemForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		final Calendar proposalStartCalDate = Calendar.getInstance();
		final Calendar proposalEndCalDate = Calendar.getInstance();
		if (null != form.getManageAvailsOption()) {
			if (form.getSosSalesTargetId() == null || form.getSosSalesTargetId().length == 0) {
				customErrors.rejectValue("fetchAvailsSOSSalesTargetId_custom", ErrorCodes.MandatoryInputMissing, "_fetchAvailsSOSSalesTargetId_custom", new Object[] { Constants.SALES_TARGET },
						UserHelpCodes.HelpMandatorySelectMissing);
			}

			if (StringUtils.isBlank(form.getSosProductId())) {
				customErrors.rejectValue("fetchAvailsSOSProductId_select2", ErrorCodes.MandatoryInputMissing, "fetchAvailsSOSProductId_select2", new Object[] { Constants.PRODUCT },
						UserHelpCodes.HelpMandatorySelectMissing);
			}

			if (StringUtils.isBlank(form.getStartDate().trim())) {
				customErrors.rejectValue("fetchAvailsStartDate", ErrorCodes.MandatoryInputMissing, "fetchAvailsStartDate", new Object[] { Constants.START_DATE }, UserHelpCodes.LineItemStartDateHelp);
			}

			if (StringUtils.isBlank(form.getEndDate().trim())) {
				customErrors.rejectValue("fetchAvailsEndDate", ErrorCodes.MandatoryInputMissing, "fetchAvailsEndDate", new Object[] { Constants.END_DATE }, UserHelpCodes.LineItemEndDateHelp);
			}

			if (!StringUtils.isBlank(form.getStartDate()) && !StringUtils.isBlank(form.getEndDate())) {
				final Date proposalStartDate = DateUtil.parseToDate(form.getStartDate());
				proposalStartCalDate.setTime(proposalStartDate);
				final Date proposalEndDate = DateUtil.parseToDate(form.getEndDate());
				proposalEndCalDate.setTime(proposalEndDate);
				if (proposalEndCalDate.before(proposalStartCalDate)) {
					customErrors.rejectValue("fetchAvailsStartDate", ErrorCodes.ProposalStartDateError, "fetchAvailsStartDate", new Object[] { Constants.START_DATE }, UserHelpCodes.ProposalStartDateHelp);
					customErrors.rejectValue("fetchAvailsEndDate", ErrorCodes.ProposalEndDateError, "fetchAvailsEndDate", new Object[] { Constants.END_DATE }, UserHelpCodes.ProposalEndDateHelp);
				}
			}
			
			if("D".equals(form.getType()) ||  "B".equals(form.getType())){
				if(StringUtils.isBlank(form.getPriceType())){
					customErrors.rejectValue("fetchAvailspriceType", ErrorCodes.MandatoryInputMissing, "fetchAvailspriceType", new Object[] { Constants.PRICE_TYPE }, UserHelpCodes.HelpMandatoryInputMissing);
				}
				final Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(DateUtil.convertToMidNightDate(DateUtil.getCurrentDate()));
				if (proposalEndCalDate.before(currentDate)) {
					customErrors.rejectValue("fetchAvailsEndDate", ErrorCodes.fetchAvailsEndDateCannotBefore, "fetchAvailsEndDate", new Object[] { Constants.END_DATE }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		} else {
			if (form.getSosSalesTargetId() == null || form.getSosSalesTargetId().length == 0) {
				customErrors.rejectValue("sosSalesTargetId_custom", ErrorCodes.MandatoryInputMissing, "_sosSalesTargetId_custom", new Object[] { Constants.SALES_TARGET },
						UserHelpCodes.HelpMandatorySelectMissing);
			}

			if (StringUtils.isBlank(form.getSosProductId())) {
				customErrors
						.rejectValue("sosProductId_select2", ErrorCodes.MandatoryInputMissing, "sosProductId_select2", new Object[] { Constants.PRODUCT }, UserHelpCodes.HelpMandatorySelectMissing);
			}

			if (StringUtils.isBlank(form.getStartDate().trim())) {
				customErrors.rejectValue("startDate", ErrorCodes.MandatoryInputMissing, "startDate", new Object[] { Constants.START_DATE }, UserHelpCodes.LineItemStartDateHelp);
			}

			if (StringUtils.isBlank(form.getEndDate().trim())) {
				customErrors.rejectValue("endDate", ErrorCodes.MandatoryInputMissing, "endDate", new Object[] { Constants.END_DATE }, UserHelpCodes.LineItemEndDateHelp);
			}

			if (!StringUtils.isBlank(form.getStartDate()) && !StringUtils.isBlank(form.getEndDate())) {
				final Date proposalStartDate = DateUtil.parseToDate(form.getStartDate());
				proposalStartCalDate.setTime(proposalStartDate);
				final Date proposalEndDate = DateUtil.parseToDate(form.getEndDate());
				proposalEndCalDate.setTime(proposalEndDate);
				if (proposalEndCalDate.before(proposalStartCalDate)) {
					customErrors.rejectValue("startDate", ErrorCodes.ProposalStartDateError, "startDate", new Object[] { Constants.START_DATE }, UserHelpCodes.ProposalStartDateHelp);
					customErrors.rejectValue("endDate", ErrorCodes.ProposalEndDateError, "endDate", new Object[] { Constants.END_DATE }, UserHelpCodes.ProposalEndDateHelp);
				}
				final Calendar currentDate = Calendar.getInstance();
				currentDate.setTime(DateUtil.convertToMidNightDate(DateUtil.getCurrentDate()));
				if (proposalEndCalDate.before(currentDate)) {
					customErrors.rejectValue("endDate", ErrorCodes.fetchAvailsEndDateCannotBefore, "endDate", new Object[] { Constants.END_DATE }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		}
	}
}
