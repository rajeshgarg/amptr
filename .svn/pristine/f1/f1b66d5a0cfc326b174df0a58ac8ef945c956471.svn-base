/**
 * 
 */
package com.nyt.mpt.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.UserForm;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;

/**
 * This Validator class will be used for Validating User Form.
 * @author shishir.srivastava
 */

public class UserFormValidator extends AbstractValidator implements Validator {

	private static final String MOBILE = "mobile";
	private static final String TELEPHONE = "telephone";
	private static final String FAX_NO = "faxNo";
	private static final String ZIP = "zip";
	private static final String STATE = "state";
	private static final String ADDRESS2 = "address2";
	private static final String ADDRESS1 = "address1";
	private static final String CITY = "city";
	private static final String ROLE_ID = "roleId";
	private static final String FIRST_NAME = "firstName";
	private static final String LOGIN_NAME = "loginName";

	private static final Logger LOGGER = Logger.getLogger(UserFormValidator.class);

	@Override
	public boolean supports(final Class<?> candidate) {
		return UserForm.class.isAssignableFrom(candidate);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final UserForm form = (UserForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isNotBlank(form.getAddress1()) && form.getAddress1().length() > 50) {
			customErrors.rejectValue(ADDRESS1, ErrorCodes.ExceedMaxAllowedCharacter, ADDRESS1, new Object[] { ADDRESS1, "50" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAddress2()) && form.getAddress2().length() > 50) {
			customErrors.rejectValue(ADDRESS2, ErrorCodes.ExceedMaxAllowedCharacter, ADDRESS2, new Object[] { ADDRESS2, "50" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getState()) && form.getState().length() > 30) {
			customErrors.rejectValue(STATE, ErrorCodes.ExceedMaxAllowedCharacter, STATE, new Object[] { STATE, "30" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getState()) && form.getState().length() > 30) {
			customErrors.rejectValue(STATE, ErrorCodes.ExceedMaxAllowedCharacter, STATE, new Object[] { STATE, "30" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getCity()) && form.getCity().length() > 30) {
			customErrors.rejectValue(CITY, ErrorCodes.ExceedMaxAllowedCharacter, CITY, new Object[] { CITY, "30" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getZip()) && form.getZip().length() > 10) {
			customErrors.rejectValue(ZIP, ErrorCodes.ExceedMaxAllowedCharacter, ZIP, new Object[] { ZIP, "10" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getFaxNo()) && form.getFaxNo().length() > 15) {
			customErrors.rejectValue(FAX_NO, ErrorCodes.ExceedMaxAllowedCharacter, FAX_NO, new Object[] { FAX_NO, "15" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getTelephone()) && form.getTelephone().length() > 15) {
			customErrors.rejectValue(TELEPHONE, ErrorCodes.ExceedMaxAllowedCharacter, TELEPHONE, new Object[] { TELEPHONE, "15" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getMobile()) && form.getMobile().length() > 15) {
			customErrors.rejectValue(MOBILE, ErrorCodes.ExceedMaxAllowedCharacter, MOBILE, new Object[] { MOBILE, "15" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAddress1()) && performsXSS(form.getAddress1())) {
			customErrors.rejectValue(ADDRESS1, ErrorCodes.containsXSSCharacters, ADDRESS1, new Object[] { ADDRESS1 }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getAddress2()) && performsXSS(form.getAddress2())) {
			customErrors.rejectValue(ADDRESS2, ErrorCodes.containsXSSCharacters, ADDRESS2, new Object[] { ADDRESS2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getState()) && performsXSS(form.getState())) {
			customErrors.rejectValue(STATE, ErrorCodes.containsXSSCharacters, STATE, new Object[] { STATE }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getCity()) && performsXSS(form.getCity())) {
			customErrors.rejectValue(CITY, ErrorCodes.containsXSSCharacters, CITY, new Object[] { CITY }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getZip()) && performsXSS(form.getZip())) {
			customErrors.rejectValue(ZIP, ErrorCodes.containsXSSCharacters, ZIP, new Object[] { ZIP }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getFaxNo()) && performsXSS(form.getFaxNo())) {
			customErrors.rejectValue(FAX_NO, ErrorCodes.containsXSSCharacters, FAX_NO, new Object[] { FAX_NO }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getTelephone()) && performsXSS(form.getTelephone())) {
			customErrors.rejectValue(TELEPHONE, ErrorCodes.containsXSSCharacters, TELEPHONE, new Object[] { TELEPHONE }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getMobile()) && performsXSS(form.getMobile())) {
			customErrors.rejectValue(MOBILE, ErrorCodes.containsXSSCharacters, MOBILE, new Object[] { MOBILE }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getLoginName())) {
			customErrors.rejectValue(LOGIN_NAME, ErrorCodes.MandatoryInputMissing, LOGIN_NAME, new Object[] { "Login Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getFirstName())) {
			customErrors.rejectValue(FIRST_NAME, ErrorCodes.MandatoryInputMissing, FIRST_NAME, new Object[] { "First Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getRoleId())) {
			customErrors.rejectValue(ROLE_ID, ErrorCodes.MandatoryInputMissing, ROLE_ID, new Object[] { "Role" }, UserHelpCodes.HelpMandatorySelectMissing);
		}
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString() {
		return "UserFormValidator ( " + super.toString() + " )";
	}
}
