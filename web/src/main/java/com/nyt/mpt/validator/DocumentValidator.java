/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.DocumentForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.DocumentTypeEnum;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This <code>DocumentValidator</code> contains all the validations to be applied on the Document while uploading the document
 * 
 * @author amandeep.singh
 */

public class DocumentValidator extends AbstractValidator implements Validator {

	private static final String FILE_SELECTION = "File Selection ";

	private static final String ATTACHEMENT = "attachement";

	private static final String FILE = "file";

	private static final Logger LOGGER = Logger.getLogger(DocumentValidator.class);

	private static final long MEGABYTE = 1024L * 1024L;

	@Override
	public boolean supports(final Class<?> clazz) {
		return DocumentForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final DocumentForm form = (DocumentForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (form.getFile() == null || StringUtils.isBlank(form.getFile().getOriginalFilename())) {
			customErrors.rejectValue(FILE, ErrorCodes.MandatoryInputMissing, ATTACHEMENT, new Object[] { FILE_SELECTION }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			if (!StringUtils.isBlank(form.getFileName())) {
				if (!form.getFile().getOriginalFilename().equals(form.getFileName())) {
					customErrors.rejectValue(FILE, ErrorCodes.FileNameChanged, ATTACHEMENT, new Object[] { FILE_SELECTION }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}

			if (form.getFile().getOriginalFilename().length() > 100) {
				customErrors.rejectValue(FILE, ErrorCodes.FileLengthExceeds, ATTACHEMENT, new Object[] { FILE_SELECTION }, UserHelpCodes.HelpMandatoryInputMissing);
			}
			if (byteToMegaByte(form.getFile().getSize())) {
				customErrors.rejectValue(FILE, ErrorCodes.FileSizeExceeds, ATTACHEMENT, new Object[] { "1 MB " }, UserHelpCodes.HelpFileSize);
			}
			if (!DocumentTypeEnum.isAllowedDocumentType(form.getFile().getOriginalFilename())) {
				customErrors.rejectValue(FILE, ErrorCodes.FileTypeMisMatch, ATTACHEMENT, new Object[] { FILE_SELECTION }, UserHelpCodes.HelpMandatoryInputMissing);
			}
			if (StringUtils.isNotBlank(form.getDescription()) && containsXSSAttacks(form.getDescription())) {
				customErrors.rejectValue("description", ErrorCodes.ContainsXSSContent, "description", new Object[] { "description" }, UserHelpCodes.HelpMandatoryInputMissing);
			}

			if (StringUtils.isNotBlank(form.getDescription()) && form.getDescription().length() > 500) {
				customErrors.rejectValue("description", ErrorCodes.ExceedMaxAllowedCharacter, form.getDescription() + "description", new Object[] { "description", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}

	private boolean byteToMegaByte(final Long bytesize) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("byteToMegaByte. Bytesize: " + bytesize);
		}
		if (bytesize.compareTo(MEGABYTE * 1L) <= 0) {
			return false;
		} else {
			return true;
		}
	}
}
