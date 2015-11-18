/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.CreativeForm;
import com.nyt.mpt.form.MultipleCalendarForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This <code>MultipleCalendarValidator</code> has all the validations to be applied for the the Multiple Calendar screen while saving the {@link MultipleCalendarForm}
 * 
 * @author shipra.bansal
 */

public class MultipleCalendarValidator extends AbstractValidator implements Validator {

	private static final String FILTER_TEXT = "filterText";
	private static final Logger LOGGER = Logger.getLogger(MultipleCalendarValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return CreativeForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final MultipleCalendarForm form = (MultipleCalendarForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(form.getFilterName())) {
			customErrors.rejectValue(FILTER_TEXT, ErrorCodes.MandatoryInputMissing, FILTER_TEXT, new Object[] { "Filter Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getFilterName())) {
			if (form.getFilterName().length() > 60) {
				customErrors.rejectValue(FILTER_TEXT, ErrorCodes.ExceedMaxAllowedCharacter, FILTER_TEXT, new Object[] { "Filter Name", "60" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (performsXSS(form.getFilterName())) {
				customErrors.rejectValue(FILTER_TEXT, ErrorCodes.containsXSSCharacters, FILTER_TEXT, new Object[] { "Filter Name" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}
}
