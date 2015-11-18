/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.PackageForm;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * This class is used to validate package
 * 
 * @author surendra.singh
 * 
 */
public class PackageFormValidator extends AbstractValidator implements Validator {

	private static final String BUDGET2 = "Budget";
	private static final String BUDGET = "budget";
	private static final String COMMENTS = "comments";
	private static final String PACKAGE_NAME = "packageName";
	private static final Logger LOGGER = Logger.getLogger(PackageFormValidator.class);

	@Override
	public boolean supports(final Class<?> candidate) {
		return PackageForm.class.isAssignableFrom(candidate);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final PackageForm form = (PackageForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		if (StringUtils.isBlank(form.getPackageName().trim())) {
			customErrors.rejectValue(PACKAGE_NAME, ErrorCodes.MandatoryInputMissing, PACKAGE_NAME, new Object[] { "Package Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getPackageName().trim()) && performsXSS(form.getPackageName().trim())) {

			customErrors.rejectValue(PACKAGE_NAME, ErrorCodes.containsXSSCharacters, PACKAGE_NAME, new Object[] { "Package Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getPackageName().trim()) && form.getPackageName().length() > 150) {
			customErrors.rejectValue(PACKAGE_NAME, ErrorCodes.ExceedMaxAllowedCharacter, PACKAGE_NAME, new Object[] { "package name", "150" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getOwnerId())) {
			customErrors.rejectValue("ownerId", ErrorCodes.MandatoryInputMissing, "ownerId", new Object[] { "Package Owner" }, UserHelpCodes.HelpMandatorySelectMissing);
		}
		if (StringUtils.isBlank(form.getBreakable())) {
			customErrors.rejectValue("breakable", ErrorCodes.MandatoryInputMissing, "breakable", new Object[] { "Breakable" }, UserHelpCodes.HelpMandatorySelectMissing);
		}
		if (StringUtils.isNotBlank(form.getComments()) && form.getComments().length() > 500) {
			customErrors.rejectValue(COMMENTS, ErrorCodes.ExceedMaxAllowedCharacter, COMMENTS, new Object[] { "comment", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getComments()) && containsXSSAttacks(form.getComments())) {
			customErrors.rejectValue(COMMENTS, ErrorCodes.ContainsXSSContent, COMMENTS, new Object[] { "Comments" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		
		if ((StringUtils.isNotBlank(form.getValidFrom()) && StringUtils.isNotBlank(form.getValidTo()))) {
			if(DateUtil.parseToDate(form.getValidFrom()).after(DateUtil.parseToDate(form.getValidTo()))){
				customErrors.rejectValue("validTo", ErrorCodes.ProposalEndDateError, "validTo", new Object[] { Constants.END_DATE }, UserHelpCodes.ProposalEndDateHelp);
				customErrors.rejectValue("validFrom", ErrorCodes.ProposalStartDateError, "validFrom", new Object[] { Constants.START_DATE }, UserHelpCodes.ProposalStartDateHelp);
			}
		}
		
		if (StringUtils.isBlank(form.getBudget())) {
			customErrors.rejectValue(BUDGET, ErrorCodes.MandatoryInputMissing, BUDGET, new Object[] { BUDGET2 }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateDecimalValues(form.getBudget(), 99999999.99, 2);
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Budget}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDecimalValue, BUDGET, new Object[] { "2", BUDGET2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Budget}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDigit, BUDGET, new Object[] { BUDGET2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Budget} is {9999999999.99}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDigitMaxVal, BUDGET, new Object[] { BUDGET2, "99,999,999.99" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}
}
