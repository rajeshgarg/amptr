/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.PlanBudgetForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

public class PlanBudgetValidator extends AbstractValidator implements Validator {

	private static final String AV_PERCENTAGE = "avPercentage";
	private static final String TOTAL_INVESTMENT2 = "Total Investment";
	private static final String TOTAL_INVESTMENT = "totalInvestment";
	private static final Logger LOGGER = Logger.getLogger(PlanBudgetValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return PlanBudgetForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final PlanBudgetForm form = (PlanBudgetForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		// Notes Validation

		if (StringUtils.isNotBlank(form.getAvNotes()) && containsXSSAttacks(form.getAvNotes())) {
			customErrors.rejectValue("avNotes", ErrorCodes.ContainsXSSContent, "avNotes", new Object[] { "Notes" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getAvNotes()) && form.getAvNotes().length() > 500) {
			customErrors.rejectValue("avNotes", ErrorCodes.ExceedMaxAllowedCharacter, "avNotes", new Object[] { "Notes", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		// Total Investment validation

		if (StringUtils.isNotBlank(String.valueOf(form.getTotalInvestment()))) {
			if (String.valueOf(form.getTotalInvestment()).length() > 100) {
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.ExceedMaxAllowedCharacter, TOTAL_INVESTMENT, new Object[] { TOTAL_INVESTMENT2, "100" },
						UserHelpCodes.HelpMandatoryInputMissing);
			}
			try {
				super.validateDecimalValues(form.getTotalInvestment(), 9999989999990.00, 2);
				if (Double.valueOf(form.getTotalInvestment().replaceAll(",", "")) <= 0.00) {
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigitMinVal, TOTAL_INVESTMENT, new Object[] { TOTAL_INVESTMENT2, "100" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Total Investment}.
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDecimalValue, TOTAL_INVESTMENT, new Object[] { "2", TOTAL_INVESTMENT2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigit, TOTAL_INVESTMENT, new Object[] { TOTAL_INVESTMENT2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Total Investment} is {9999999999.99}.
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigitMaxVal, TOTAL_INVESTMENT, new Object[] { TOTAL_INVESTMENT2, "9,999,989,999,990.00" },
						UserHelpCodes.HelpMandatoryInputMissing);
			}
		} else {
			customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.MandatoryInputMissing, TOTAL_INVESTMENT, new Object[] { TOTAL_INVESTMENT2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		// Percentage validation
		if (StringUtils.isNotBlank(String.valueOf(form.getAvPercentage()))) {
			try {
				super.validateDecimalValues(String.valueOf(form.getAvPercentage()), 100, 2);
				if (Double.valueOf(form.getAvPercentage().replaceAll(",", "")) <= 0.00 || Double.valueOf(form.getAvPercentage().replaceAll(",", "")) >= 100.00) {
					customErrors.rejectValue(AV_PERCENTAGE, ErrorCodes.InvalidAvPercentage, AV_PERCENTAGE, new Object[] { "Percentage", "100", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Percentage}.
				customErrors.rejectValue(AV_PERCENTAGE, ErrorCodes.NumericDecimalValue, AV_PERCENTAGE, new Object[] { "2", "Percentage" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Percentage}.
				customErrors.rejectValue(AV_PERCENTAGE, ErrorCodes.NumericDigit, AV_PERCENTAGE, new Object[] { "Percantage" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Allowed limit for {Percentage} is less than {100} and greater than {0}.
				customErrors.rejectValue(AV_PERCENTAGE, ErrorCodes.InvalidAvPercentage, AV_PERCENTAGE, new Object[] { "Percentage", "100", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		} else {
			customErrors.rejectValue(AV_PERCENTAGE, ErrorCodes.MandatoryInputMissing, AV_PERCENTAGE, new Object[] { "Percantage" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
	}
}
