/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.AttributeAssocForm;
import com.nyt.mpt.form.AttributeForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This class is used to validate attribute
 * @author amandeep.singh
 */
public class AttributeValidator extends AbstractValidator implements Validator {

	private static final Logger LOGGER = Logger.getLogger(AttributeValidator.class);

	@Override
	public boolean supports(final Class<?> clazz) {
		return AttributeForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final AttributeForm form = (AttributeForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(form.getAttributeName())) {
			customErrors.rejectValue("attributeName", ErrorCodes.MandatoryInputMissing, "attributeName", new Object[] { "Attribute Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (performsXSS(form.getAttributeName())) {
				customErrors.rejectValue("attributeName", ErrorCodes.containsXSSCharacters, "attributeName", new Object[] { "attributeName" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (form.getAttributeName().length() > 60) {
				customErrors.rejectValue("attributeName", ErrorCodes.ExceedMaxAllowedCharacter, "attributeName", new Object[] { "attribute name", "60" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
		if (NumberUtil.longValue(form.getAttributeId()) == 0 && StringUtils.isBlank(form.getAttributeType())) {
			customErrors.rejectValue("attributeType", ErrorCodes.MandatoryInputMissing, "attributeName", new Object[] { "Attribute Type" }, UserHelpCodes.HelpMandatorySelectMissing);
		}
		if (StringUtils.isNotBlank(form.getAttributeOptionalValue()) && performsXSS(form.getAttributeOptionalValue())) {

			customErrors.rejectValue("attributeOptionalValue", ErrorCodes.containsXSSCharacters, "attributeOptionalValue", new Object[] { "attribute optional value" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAttributeOptionalValue()) && form.getAttributeOptionalValue().length() > 200) {
			customErrors.rejectValue("attributeOptionalValue", ErrorCodes.ExceedMaxAllowedCharacter, "attributeOptionalValue", new Object[] { "attribute value", "200" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAttributeDescription()) && containsXSSAttacks(form.getAttributeDescription())) {
			customErrors.rejectValue("attributeDescription", ErrorCodes.ContainsXSSContent, "attributeDescription", new Object[] { "attribute description" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getAttributeDescription()) && form.getAttributeDescription().length() > 500) {
			customErrors.rejectValue("attributeDescription", ErrorCodes.ExceedMaxAllowedCharacter, "attributeDescription", new Object[] { "attribute description", "500" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
	}

	public void validateForAttributeValue(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating AttributeValue. Object:" + target + "Error:" + errors);
		}
		final AttributeForm attributeForm = (AttributeForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(attributeForm.getAttributeValue())) {
			customErrors.rejectValue("attributeValue", ErrorCodes.MandatoryInputMissing, attributeForm.getAttributeId() + "_attributeValue", new Object[] { "Attribute Value" },
					UserHelpCodes.HelpMandatoryInputMissing);
		} else if (attributeForm.getAttributeValue().length() > 50) {
			customErrors.rejectValue("attributeValue", ErrorCodes.ExceedMaxAllowedCharacter, attributeForm.getAttributeId() + "_attributeValue", new Object[] { "attribute value", "50" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.equals(attributeForm.getAttributeId(), "0") && StringUtils.isBlank(attributeForm.getAttributeName())) {
			customErrors.rejectValue("attributeName", ErrorCodes.MandatoryInputMissing, attributeForm.getAttributeId() + "_attributeName", new Object[] { "Attribute Name" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
	}

	public void validateForAttributeAssoc(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating For Attribute Assoc. Object:" + target + "Error:" + errors);
		}
		final AttributeAssocForm form = (AttributeAssocForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(form.getAction())) {
			customErrors.rejectValue("addaction", ErrorCodes.MandatoryInputMissing, "addaction", new Object[] { "Action" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (StringUtils.equals("0", form.getAttributeId()) && StringUtils.equals("AssociateAttribute", form.getAction())) {
				customErrors.rejectValue("attributeId", ErrorCodes.MandatoryInputMissing, "attributeId", new Object[] { "Name" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getAction()) && StringUtils.isBlank(form.getAttributeName()) && StringUtils.equals("AddNew", form.getAction())) {
			customErrors.rejectValue("attributeName", ErrorCodes.MandatoryInputMissing, "attributeName", new Object[] { "Attribute Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (performsXSS(form.getAttributeName())) {
				customErrors.rejectValue("attributeName", ErrorCodes.containsXSSCharacters, "attributeName", new Object[] { "attributeName" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (form.getAttributeName().length() > 60) {
				customErrors.rejectValue("attributeName", ErrorCodes.ExceedMaxAllowedCharacter, "attributeName", new Object[] { "attribute name", "60" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
		if (form.getAttributeValue().length() > 200) {
			customErrors.rejectValue("attributeValue", ErrorCodes.ExceedMaxAllowedCharacter, form.getAttributeId() + "_attributeValue", new Object[] { "attribute value", "200" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAttributeDescription()) && form.getAttributeDescription().length() > 500) {
			customErrors.rejectValue("attributeDescription", ErrorCodes.ExceedMaxAllowedCharacter, "attributeDescription", new Object[] { "attribute description", "500" },
					UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (StringUtils.isNotBlank(form.getAttributeDescription()) && containsXSSAttacks(form.getAttributeDescription())) {
				customErrors.rejectValue("attributeDescription", ErrorCodes.ContainsXSSContent, "attributeDescription", new Object[] { "attributeDescription" },
						UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}

	public void validateForAttributeAssocValue(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating For Attribute Association . Object: " + target + "Error:" + errors);
		}
		final AttributeAssocForm attributeForm = (AttributeAssocForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(attributeForm.getAttributeValue())) {
			customErrors.rejectValue("attributeValue", ErrorCodes.MandatoryInputMissing, attributeForm.getAttributeId() + "_attributeValue", new Object[] { "Attribute Value" },
					UserHelpCodes.HelpMandatoryInputMissing);
		} else if (attributeForm.getAttributeValue().length() > 200) {
			customErrors.rejectValue("attributeValue", ErrorCodes.ExceedMaxAllowedCharacter, attributeForm.getAttributeId() + "_attributeValue", new Object[] { "attribute value", "200" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(attributeForm.getAttributeValue()) && performsXSS(attributeForm.getAttributeValue())) {
			customErrors.rejectValue("attributeValue", ErrorCodes.containsXSSCharacters, "attributeValue", new Object[] { "attributeValue" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.equals(attributeForm.getAttributeId(), "0") && StringUtils.isBlank(attributeForm.getAttributeName())) {
			customErrors.rejectValue("attributeName", ErrorCodes.MandatoryInputMissing, attributeForm.getAttributeId() + "_attributeName", new Object[] { "Attribute Name" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
	}
}
