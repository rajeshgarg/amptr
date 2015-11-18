/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.TemplateManagementForm;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * @author rakesh.tewari
 */
public class TemplateManagementValidator extends AbstractValidator implements Validator {

	private static final long MEGA_BYTE = 1024L * 1024L;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return TemplateManagementForm.class.isAssignableFrom(clazz);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object target, final Errors errors) {
		final TemplateManagementForm form = (TemplateManagementForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		if (form.getCustomTemplateFile() != null && StringUtils.isNotBlank(form.getCustomTemplateFile().getOriginalFilename())) {
			final String fileName = form.getCustomTemplateFile().getOriginalFilename();

			if (ConstantStrings.CREATIVE_SPEC_TEMPALTE_NAME.equalsIgnoreCase(fileName.substring(0, fileName.lastIndexOf('.')))) {
				customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.ReserveForSystem, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "CreativeSpecTemplate" },
						UserHelpCodes.HelpReserveForSystem);
			} else if ((!ConstantStrings.XLS.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.') + 1)))
					&& (!ConstantStrings.XLSX.equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.') + 1)))) {
				customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.FileTypeMisMatch, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "File Selection" },
						UserHelpCodes.HelpMandatoryFileTypeMissing);
			} else if (alphanumericPattern(fileName.substring(0, fileName.lastIndexOf('.')))) {
				customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.containsAlphanumericCharacters, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "File Selection" },
						UserHelpCodes.HelpMandatoryInputMissing);
			} else if (form.getCustomTemplateFile().getSize() >= MEGA_BYTE * 2L) {
				customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.FileSizeExceeds, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "2 MB" }, UserHelpCodes.HelpFileSize);
			} else if (form.getCustomTemplateFile().getOriginalFilename().length() > 100) {
				customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.FileLengthExceeds, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "File Selection" },
						UserHelpCodes.HelpMandatoryInputMissing);
			}

		} else {
			customErrors.rejectValue(ConstantStrings.CUSTOM_TEMPLATE_FILE, ErrorCodes.MandatoryInputMissing, ConstantStrings.CUSTOM_TEMPLATE_FILE, new Object[] { "File Selection" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}
	}

	/**
	 * @param target
	 * @param errors
	 */
	public void validateTemplate(final Object target, final Errors errors) {
		final TemplateManagementForm form = (TemplateManagementForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		if (form != null && StringUtils.isBlank(form.getTemplateName())) {
			customErrors.rejectValue(ConstantStrings.TEMPLATE_NAME, ErrorCodes.MandatoryInputMissing, ConstantStrings.TEMPLATE_NAME,
					new Object[] { "Template name" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (performsXSS(form.getTemplateName())) {
				customErrors.rejectValue(ConstantStrings.TEMPLATE_NAME, ErrorCodes.containsXSSCharacters, ConstantStrings.TEMPLATE_NAME,
						new Object[] { "Template name" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (form.getTemplateName().length() > 60) {
				customErrors.rejectValue(ConstantStrings.TEMPLATE_NAME, ErrorCodes.ExceedMaxAllowedCharacter, ConstantStrings.TEMPLATE_NAME,
						new Object[] { "Template name ", "60" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}
}
