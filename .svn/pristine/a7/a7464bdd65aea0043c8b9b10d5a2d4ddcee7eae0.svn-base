/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.TierForm;
import com.nyt.mpt.form.TierPremiumForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * @author garima.garg
 */
public class TierValidator extends AbstractValidator implements Validator {

	private static final String PREMIUM2 = "premium";

	private static final String LEVEL2 = "level";

	private static final String TIER_NAME = "tierName";

	private static final String NAME = "Name";

	private static final String LEVEL = "Level";

	private static final String PREMIUM = "Premium";

	private static final Logger LOGGER = Logger.getLogger(TierValidator.class);

	private static final String MAX_LONG = "999";

	private static final String PREMIUM_MAX_LONG = "100.00";

	@Override
	public boolean supports(final Class<?> clazz) {
		return TierForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}

		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (target instanceof TierForm) {
			final TierForm tierForm = (TierForm) target;
			if (StringUtils.isBlank(tierForm.getTierName())) {
				customErrors.rejectValue(TIER_NAME, ErrorCodes.MandatoryInputMissing, TIER_NAME, new Object[] { NAME }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (performsXSS(tierForm.getTierName())) {
				customErrors.rejectValue(TIER_NAME, ErrorCodes.containsXSSCharacters, TIER_NAME, new Object[] { NAME }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (tierForm.getTierName().length() > 60) {
				customErrors.rejectValue(TIER_NAME, ErrorCodes.ExceedMaxAllowedCharacter, TIER_NAME, new Object[] { NAME, "60" }, UserHelpCodes.HelpMandatoryInputMissing);
			}

			if (StringUtils.isBlank(tierForm.getLevel())) {
				customErrors.rejectValue(LEVEL2, ErrorCodes.MandatoryInputMissing, LEVEL2, new Object[] { LEVEL }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (StringUtils.isNumeric(tierForm.getLevel())) {
				try {
					super.validateLongValues(tierForm.getLevel(), 999L);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {Tier Level} is {999}.
					customErrors.rejectValue(LEVEL2, ErrorCodes.NumericDigitMaxVal, LEVEL2, new Object[] { LEVEL, MAX_LONG }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} else {
				customErrors.rejectValue(LEVEL2, ErrorCodes.PositiveNumericDigit, LEVEL2, new Object[] { LEVEL }, UserHelpCodes.HelpMandatoryInputMissing);
			}

			if (tierForm.getSelectedSectionIds().length == 0) {
				customErrors.rejectValue("tierSections", ErrorCodes.MandatoryInputMissing, "tierSections", new Object[] { "Sections" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		} else {
			final TierPremiumForm tierPremiumForm = (TierPremiumForm) target;

			if (StringUtils.isBlank(tierPremiumForm.getHidTargetTypeId())) {
				customErrors.rejectValue("targetTypeId", ErrorCodes.MandatoryInputMissing, "targetTypeId", new Object[] { "Target Type" }, UserHelpCodes.HelpMandatoryInputMissing);
			}

			if (StringUtils.isBlank(tierPremiumForm.getPremium())) {
				customErrors.rejectValue(PREMIUM2, ErrorCodes.MandatoryInputMissing, PREMIUM2, new Object[] { PREMIUM }, UserHelpCodes.HelpMandatoryInputMissing);
			} else {
				try {
					super.validateDecimalValues(tierPremiumForm.getPremium(), 100D, 2);
					if (Double.valueOf(tierPremiumForm.getPremium().replaceAll(",", "")) <= 0.0D) {
						customErrors.rejectValue(PREMIUM2, ErrorCodes.PremiumLessThanEqualZero, PREMIUM2, new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
					}
				} catch (NumberFormatException e) {
					customErrors.rejectValue(PREMIUM2, ErrorCodes.PositiveDecimalDigit, PREMIUM2, new Object[] { PREMIUM }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {Tier Premium} is {100}.
					customErrors.rejectValue(PREMIUM2, ErrorCodes.NumericDigitMaxVal, PREMIUM2, new Object[] { PREMIUM, PREMIUM_MAX_LONG }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (MaxAllowedDecimalExceedExecption e) {
					customErrors.rejectValue(PREMIUM2, ErrorCodes.NumericDecimalValue, PREMIUM2, new Object[] { "2", PREMIUM }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		}
	}
}
