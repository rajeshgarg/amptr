/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.CreativeForm;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class is used to validate creative
 * @author amandeep.singh
 */
public class CreativeValidator extends AbstractValidator implements Validator {

	private static final Logger LOGGER = Logger.getLogger(CreativeValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return CreativeForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final CreativeForm form = (CreativeForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isNotBlank(form.getName()) && form.getName().length() > 60) {
			customErrors.rejectValue("name", ErrorCodes.ExceedMaxAllowedCharacter, "name", new Object[] { "creative name", "60" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getName())) {
			customErrors.rejectValue("name", ErrorCodes.MandatoryInputMissing, "name", new Object[] { "Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getName()) && performsXSS(form.getName())) {
			customErrors.rejectValue("name", ErrorCodes.containsXSSCharacters, "name", new Object[] { "Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getType())) {
			customErrors.rejectValue("type", ErrorCodes.MandatoryInputMissing, "type", new Object[] { "Type" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getDescription()) && form.getDescription().length() > 500) {
			customErrors.rejectValue("description", ErrorCodes.ExceedMaxAllowedCharacter, "description", new Object[] { "description", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getDescription()) && containsXSSAttacks(form.getDescription())) {
			customErrors.rejectValue("description", ErrorCodes.ContainsXSSContent, "description", new Object[] { "description" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		// Height and width validation
		validateWidthHieght(form.getHeight(), "height", Constants.CREATIVE_HEIGHT, customErrors);
		validateWidthHieght(form.getWidth(), "width", Constants.CREATIVE_WIDTH, customErrors);

		if (StringUtils.isNotBlank(form.getHeight2()) && StringUtils.isBlank(form.getWidth2())) {
			customErrors.rejectValue("height2", ErrorCodes.creativeWidthHeight, "height2", new Object[] { Constants.CREATIVE_HEIGHT2 }, UserHelpCodes.creativeWidthHeightHelp);
			customErrors.rejectValue("width2", ErrorCodes.creativeWidthHeight, "width2", new Object[] { Constants.CREATIVE_WIDTH2 }, UserHelpCodes.creativeWidthHeightHelp);
		}
		if (StringUtils.isNotBlank(form.getWidth2()) && StringUtils.isBlank(form.getHeight2())) {
			customErrors.rejectValue("height2", ErrorCodes.creativeWidthHeight, "height2", new Object[] { Constants.CREATIVE_HEIGHT2 }, UserHelpCodes.creativeWidthHeightHelp);
			customErrors.rejectValue("width2", ErrorCodes.creativeWidthHeight, "width2", new Object[] { Constants.CREATIVE_WIDTH2 }, UserHelpCodes.creativeWidthHeightHelp);
		}
		if (StringUtils.isNotBlank(form.getHeight2())) {
			validateWidthHieght(form.getHeight2(), "height2", Constants.CREATIVE_HEIGHT2, customErrors);
		}
		if (StringUtils.isNotBlank(form.getWidth2())) {
			validateWidthHieght(form.getWidth2(), "width2", Constants.CREATIVE_WIDTH2, customErrors);
		}
	}

	private void validateWidthHieght(final String inputValue, final String inputField, final String arguments, final CustomBindingResult customErrors) {
		if (StringUtils.isBlank(inputValue)) {
			customErrors.rejectValue(inputField, ErrorCodes.MandatoryInputMissing, inputField, new Object[] { arguments }, UserHelpCodes.HelpMandatoryInputMissing);
		} else if ("0".equals(inputValue)) {
			customErrors.rejectValue(inputField, ErrorCodes.NumericDigitMinVal, inputField, new Object[] { arguments }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateLongValues(inputValue, 99999L);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Height}.
				customErrors.rejectValue(inputField, ErrorCodes.NumericDigit, inputField, new Object[] { arguments }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Height} is {99999}.
				customErrors.rejectValue(inputField, ErrorCodes.NumericDigitMaxVal, inputField, new Object[] { arguments, "99,999" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}
}