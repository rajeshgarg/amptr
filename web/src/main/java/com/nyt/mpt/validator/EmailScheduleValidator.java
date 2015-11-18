/**
 * 
 */
package com.nyt.mpt.validator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.CreativeForm;
import com.nyt.mpt.form.EmailSendScheduleForm;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.StringUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.enums.Weekdays;

/**
 * This <code>EmailScheduleValidator</code> contains all the validations to be applied while saving/updating the {@link EmailSendScheduleForm} from the UI
 * 
 * @author Gurditta.Garg
 */

public class EmailScheduleValidator extends AbstractValidator implements Validator {

	private static final Logger LOGGER = Logger.getLogger(EmailScheduleValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return CreativeForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object: " + target + "Error:" + errors);
		}
		final EmailSendScheduleForm form = (EmailSendScheduleForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(form.getEmailStartDate())) {
			customErrors.rejectValue("emailStartDate", ErrorCodes.MandatoryInputMissing, "emailStartDate", new Object[] { Constants.START_DATE },
					UserHelpCodes.HelpMandatoryDateMissing);
		}

		if (StringUtils.isBlank(form.getEmailEndDate()) && "endOn".equals(form.getEnds())) {
			customErrors.rejectValue("emailEndDate", ErrorCodes.MandatoryInputMissing, "emailEndDate", new Object[] { Constants.END_DATE },
					UserHelpCodes.HelpMandatoryDateMissing);
		}

		if (form.isRecurrence() && StringUtils.isBlank(form.getWeekDays())) {
			customErrors.rejectValue("recurrenceFieldset", ErrorCodes.MandatoryInputMissing, "recurrenceFieldset", new Object[] { "Weekday(s)" },
					UserHelpCodes.HelpMandatoryWeekdaysMissing);
		}

		if (form.isRecurrence() && StringUtils.isBlank(form.getFrequency())) {
			customErrors.rejectValue("recurrenceFieldset", ErrorCodes.MandatoryInputMissing, "recurrenceFieldset", new Object[] { "Frequency" },
					UserHelpCodes.HelpMandatoryFrequencyMissing);
		}
		
		if ((StringUtils.isNotBlank(form.getEmailStartDate()) && StringUtils.isNotBlank(form.getEmailEndDate()))) {
			if (DateUtil.parseToDate(form.getEmailStartDate()).after(DateUtil.parseToDate(form.getEmailEndDate()))) {
				customErrors.rejectValue("emailEndDate", ErrorCodes.LineItemFlightIncorrectEndDate, "emailEndDate", new Object[] { Constants.END_DATE,
						Constants.START_DATE }, UserHelpCodes.LineItemEndDateHelp);
				customErrors.rejectValue("emailStartDate", ErrorCodes.LineItemFlightIncorrectStartDate, "emailStartDate", new Object[] { Constants.START_DATE,
						Constants.END_DATE }, UserHelpCodes.LineItemStartDateHelp);
			}
		}

		if ("endOn".equals(form.getEnds()) && StringUtils.isNotBlank(form.getEmailEndDate()) && StringUtils.isNotBlank(form.getWeekDays())) {
			if (isWeekDaysOutSideDateRange(form)) {
				customErrors.rejectValue("recurrenceFieldset", ErrorCodes.EmailScheduleDaysOutsideDateRange, "recurrenceFieldset",
						new Object[] { "Weekday(s)" }, UserHelpCodes.HelpEmailScheduleDaysOutsideDateRange);
			}
		}
	}

	private boolean isWeekDaysOutSideDateRange(final EmailSendScheduleForm form) {
		final Calendar startDate = Calendar.getInstance(); startDate.setTime(DateUtil.parseToDate(form.getEmailStartDate()));
		final Calendar endDate = Calendar.getInstance(); endDate.setTime(DateUtil.parseToDate(form.getEmailEndDate()));
		final List<String> daysList = StringUtil.getListFromArray(form.getWeekDays().split(ConstantStrings.COMMA));
		final Map<String, Integer> dayMap = initializeMap();
		while(startDate.before(endDate) || startDate.equals(endDate)) {
			for (String day : daysList) {
				if (startDate.get(Calendar.DAY_OF_WEEK) == dayMap.get(day)) {
					daysList.remove(day);
					break;
				}
			}
			if (daysList.isEmpty()) {
				break;
			}
			startDate.add(Calendar.DATE, 1);
		}
		return !daysList.isEmpty();
	}
	
	private Map<String, Integer> initializeMap() {
		final Map<String, Integer> returnMap = new HashMap<String, Integer>();
		returnMap.put(Weekdays.SUNDAY.name(), Calendar.SUNDAY);
		returnMap.put(Weekdays.MONDAY.name(), Calendar.MONDAY);
		returnMap.put(Weekdays.TUESDAY.name(), Calendar.TUESDAY);
		returnMap.put(Weekdays.WEDNESDAY.name(), Calendar.WEDNESDAY);
		returnMap.put(Weekdays.THURSDAY.name(), Calendar.THURSDAY);
		returnMap.put(Weekdays.FRIDAY.name(), Calendar.FRIDAY);
		returnMap.put(Weekdays.SATURDAY.name(), Calendar.SATURDAY);
		return returnMap;
	}
}
