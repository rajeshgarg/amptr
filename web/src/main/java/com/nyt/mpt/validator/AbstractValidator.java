/**
 *
 */
package com.nyt.mpt.validator;

import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * This class is used to prevent cross-side scripting
 * @author shishir.srivastava
 */
public abstract class AbstractValidator {

	private static final Logger LOGGER = Logger.getLogger(AbstractValidator.class);
	private static AntiSamy antiSamy;

	/**
	 * Load AntySamy policy file and return instance of AntiSamy
	 * @return
	 * @throws PolicyException
	 */
	private static AntiSamy getAntiSamyInstance() throws PolicyException {
		if (antiSamy == null) {
			final URL policyFileUrl = Thread.currentThread().getContextClassLoader().getResource("antisamy_config.xml");
			antiSamy = new AntiSamy(Policy.getInstance(policyFileUrl));
		}
		return antiSamy;
	}

	/**
	 * This method is used to check cross-side scripting
	 * @param content
	 * @return
	 */
	protected static boolean containsXSSAttacks(final String content) {
		try {
			final CleanResults result = getAntiSamyInstance().scan(content);
			return result.getErrorMessages().isEmpty() ? false : true;
		} catch (ScanException e) {
			LOGGER.warn(e.getMessage());
		} catch (PolicyException e) {
			LOGGER.warn(e.getMessage());
		}
		return true;
	}

	/**
	 * Method to prevent cross-side scripting
	 * @param string
	 * @return
	 */
	public boolean performsXSS(final String string) {
		return Pattern.compile("[^A-Za-z0-9.$@;,/ _()-]+").matcher(string).find();
	}
	
	public boolean performsXSSForCampaignName(final String string) {
		return Pattern.compile("[^A-Za-z0-9.$'&@;,/ _()-]+").matcher(string).find();
	}

	/**
	 * check pattern for A to Z, a to z, 0 to 9 and _
	 * @param string
	 * @return
	 */
	public boolean alphanumericPattern(final String string) {
		return Pattern.compile("[^A-Za-z0-9_]").matcher(string).find();
	}
	
	/**
	 * check pattern for A to Z, a to z, 0 to 9
	 * @param string
	 * @return
	 */
	public boolean sfAlphanumericPattern(final String string) {
		return Pattern.compile("[^A-Za-z0-9]").matcher(string).find();
	}

	/**
	 * @param decimalNumber
	 */
	protected void validateDecimalValues(final String decimalNumber, final double maxValue, final int decimalPlace) {
		if (StringUtils.isNotBlank(decimalNumber)) {
			if (decimalNumber.lastIndexOf('.') != -1 && decimalNumber.length() > decimalNumber.lastIndexOf('.') + (decimalPlace + 1)) {
				throw new MaxAllowedDecimalExceedExecption();
			}
			try {
				if (Double.valueOf(replaceComma(decimalNumber)) > maxValue) {
					throw new IllegalArgumentException();
				}
			} catch (NumberFormatException e) {
				throw e;
			}
		}
	}

	/**
	 * @param longNumber
	 */
	protected void validateLongValues(final String longNumber, final long maxValue) {
		if (StringUtils.isNotBlank(longNumber)) {
			try {
				final long value = Long.valueOf(replaceComma(longNumber));
				if (value > maxValue) {
					throw new IllegalArgumentException();
				}
			} catch (NumberFormatException e) {
				throw e;
			}
		}
	}

	/**
	 * Replace all occurrences of comma with blank String
	 * @param longNumber
	 * @return
	 */
	private String replaceComma(final String longNumber) {
		return StringUtils.replace(longNumber, ConstantStrings.COMMA, ConstantStrings.EMPTY_STRING);
	}
}