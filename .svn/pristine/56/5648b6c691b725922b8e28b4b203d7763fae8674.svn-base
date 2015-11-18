/**
 * 
 */
package com.nyt.mpt.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.domain.SosIntegrationPojo;
import com.nyt.mpt.form.ProposalForm;
import com.nyt.mpt.util.AjaxFormSubmitResponse;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * This class is used to validate Proposal
 * @author amandeep.singh
 */
public class ProposalValidator extends AbstractValidator implements Validator {

	private static final String REQUESTED_ON = "requestedOn";
	private static final String DUE_ON = "dueOn";
	private static final String SOS_ORDER_ID2 = "SOS Order Id";
	private static final String SOS_ORDER_ID = "sosOrderId";
	private static final String NET_CPM = "netCpm";
	private static final String BUDGET = "budget";
	private static final String NEW_ADVERTISER_NAME = "newAdvertiserName";
	private static final String CAMPAIGN_NAME2 = "Campaign Name";
	private static final String AGENCY_MARGIN2 = "Agency Margin";
	private static final String ACCOUNT_MANAGER = "accountManager";
	private static final String CAMPAIGN_NAME = "campaignName";
	private static final String AGENCY_MARGIN = "agencyMargin";
	private static final Logger LOGGER = Logger.getLogger(ProposalValidator.class);
	private static final String THRESHOLD_VALUE = "Threshold Value";

	@Override
	public boolean supports(final Class<?> clazz) {
		return ProposalForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final ProposalForm form = (ProposalForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isNotBlank(form.getAgencyMargin())) {
			try {
				super.validateDecimalValues(form.getAgencyMargin(), 99, 2);
				if (Double.valueOf(form.getAgencyMargin()) <= 0.00) {
					customErrors.rejectValue(AGENCY_MARGIN, ErrorCodes.SorLimit, AGENCY_MARGIN, new Object[] { AGENCY_MARGIN2, "99", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Agency Margin}.
				customErrors.rejectValue(AGENCY_MARGIN, ErrorCodes.NumericDecimalValue, AGENCY_MARGIN, new Object[] { "2", AGENCY_MARGIN2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Agency Margin}.
				customErrors.rejectValue(AGENCY_MARGIN, ErrorCodes.NumericDigit, AGENCY_MARGIN, new Object[] { AGENCY_MARGIN2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Agency Margin} is {100}.
				customErrors.rejectValue(AGENCY_MARGIN, ErrorCodes.SorLimit, AGENCY_MARGIN, new Object[] { AGENCY_MARGIN2, "99", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		} else {
			customErrors.rejectValue(AGENCY_MARGIN, ErrorCodes.MandatoryInputMissing, AGENCY_MARGIN, new Object[] { AGENCY_MARGIN2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getCampaignName()) && form.getCampaignName().length() > 60) {
			customErrors.rejectValue(CAMPAIGN_NAME, ErrorCodes.ExceedMaxAllowedCharacter, CAMPAIGN_NAME, new Object[] { CAMPAIGN_NAME2, "60" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getCampaignName()) && containsXSSAttacks(form.getCampaignName())) {
			customErrors.rejectValue(CAMPAIGN_NAME, ErrorCodes.containsXSSAttackCharacters, CAMPAIGN_NAME, new Object[] { CAMPAIGN_NAME2 }, UserHelpCodes.HelpXSSAttackCharacters);
		}

		if (StringUtils.isNotBlank(form.getAccountManager()) && form.getAccountManager().length() > 60) {
			customErrors.rejectValue(ACCOUNT_MANAGER, ErrorCodes.ExceedMaxAllowedCharacter, ACCOUNT_MANAGER, new Object[] { ACCOUNT_MANAGER, "60" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getAccountManager()) && performsXSS(form.getAccountManager())) {
			customErrors.rejectValue(ACCOUNT_MANAGER, ErrorCodes.containsXSSCharacters, ACCOUNT_MANAGER, new Object[] { "Account Manager" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getCampaignName()) && performsXSSForCampaignName(form.getCampaignName())) {
			customErrors.rejectValue(CAMPAIGN_NAME, ErrorCodes.containsXSSCharactersCampaignName, CAMPAIGN_NAME, new Object[] { CAMPAIGN_NAME2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getNewAdvertiserName()) && form.getNewAdvertiserName().length() > 50) {
			customErrors.rejectValue(NEW_ADVERTISER_NAME, ErrorCodes.ExceedMaxAllowedCharacter, NEW_ADVERTISER_NAME, new Object[] { "new AdvertiserName", "50" },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getNewAdvertiserName()) && containsXSSAttacks(form.getNewAdvertiserName())) {
			customErrors.rejectValue(NEW_ADVERTISER_NAME, ErrorCodes.ContainsXSSContent, NEW_ADVERTISER_NAME, new Object[] { "New Advertiser Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isBlank(form.getCampaignName())) {
			customErrors.rejectValue(CAMPAIGN_NAME, ErrorCodes.MandatoryInputMissing, CAMPAIGN_NAME, new Object[] { CAMPAIGN_NAME2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getSalescategory())) {
			customErrors.rejectValue("salescategory_select2", ErrorCodes.MandatoryInputMissing, "salescategory_select2", new Object[] { "Sales Category" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getDueOn())) {
			customErrors.rejectValue(DUE_ON, ErrorCodes.MandatoryInputMissing, DUE_ON, new Object[] { "Due On" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getRequestedOn())) {
			customErrors.rejectValue(REQUESTED_ON, ErrorCodes.MandatoryInputMissing, REQUESTED_ON, new Object[] { "Requested On" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(form.getCriticality())) {
			customErrors.rejectValue("criticality", ErrorCodes.MandatoryInputMissing, "criticality", new Object[] { "Priority" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getSosOrderId()) && performsXSS(form.getSosOrderId())) {
			customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.containsXSSCharacters, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2 }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isNotBlank(form.getSosOrderId()) && form.getSosOrderId().length() > 100) {
			customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.ExceedMaxAllowedCharacter, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2, "100" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (!StringUtils.isBlank(form.getRequestedOn()) && !StringUtils.isBlank(form.getDueOn())) {
			final Date proposalRequestedDate = DateUtil.parseToDateTime(form.getRequestedOn());
			final Calendar proposalRequestedCalDate = Calendar.getInstance();
			proposalRequestedCalDate.setTime(proposalRequestedDate);
			final Date proposalDueDate = DateUtil.parseToDateTime(form.getDueOn());
			final Calendar proposalDueCalDate = Calendar.getInstance();
			proposalDueCalDate.setTime(proposalDueDate);
			if (proposalDueCalDate.before(proposalRequestedCalDate)) {
				customErrors.rejectValue(DUE_ON, ErrorCodes.ProposalDueDateError, DUE_ON, new Object[] { "Due On" }, UserHelpCodes.ProposalDueDateHelp);
				customErrors.rejectValue(REQUESTED_ON, ErrorCodes.ProposalReqDateError, REQUESTED_ON, new Object[] { "Requested On" }, UserHelpCodes.ProposalReqDateHelp);
			}
		}

		if (!StringUtils.isBlank(form.getStartDate()) && !StringUtils.isBlank(form.getEndDate())) {
			final Date proposalStartDate = DateUtil.parseToDate(form.getStartDate());
			final Calendar proposalStartCalDate = Calendar.getInstance();
			proposalStartCalDate.setTime(proposalStartDate);
			final Date proposalEndDate = DateUtil.parseToDate(form.getEndDate());
			final Calendar proposalEndCalDate = Calendar.getInstance();
			proposalEndCalDate.setTime(proposalEndDate);
			if (proposalEndCalDate.before(proposalStartCalDate)) {
				customErrors.rejectValue("startDate", ErrorCodes.ProposalStartDateError, "startDate", new Object[] { "Start Date" }, UserHelpCodes.ProposalStartDateHelp);
				customErrors.rejectValue("endDate", ErrorCodes.ProposalEndDateError, "endDate", new Object[] { "End Date" }, UserHelpCodes.ProposalEndDateHelp);
			}
		}

		if (StringUtils.isNotBlank(form.getBudget())) {
			try {
				super.validateDecimalValues(form.getBudget(), 9999999999.99, 2);
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Budget}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDecimalValue, BUDGET, new Object[] { "2", "Budget" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Budget}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDigit, BUDGET, new Object[] { "Budget" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Budget} is {9999999999.99}.
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDigitMaxVal, BUDGET, new Object[] { "Budget", "9,999,999,999.99" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getNetCpm())) {
			try {
				super.validateDecimalValues(form.getNetCpm(), 9999999999999.99, 2);
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Net CPM}.
				customErrors.rejectValue(NET_CPM, ErrorCodes.NumericDecimalValue, NET_CPM, new Object[] { "2", "Effective CPM" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Net CPM}.
				customErrors.rejectValue(NET_CPM, ErrorCodes.NumericDigit, NET_CPM, new Object[] { "Effective CPM" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Net CPM} is {9999999999.99}.
				customErrors.rejectValue(NET_CPM, ErrorCodes.NumericDigitMaxVal, NET_CPM, new Object[] { "Effective CPM", "9,999,999,999,999.99" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getNetImpressions())) {
			try {
				super.validateLongValues(form.getNetImpressions(), 9999999999999l);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Net Impression}.
				customErrors.rejectValue("netImpressions", ErrorCodes.NumericDigit, "netImpressions", new Object[] { "Net Impressions" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Net Impression} is
				// {9999999999.99}.
				customErrors.rejectValue("netImpressions", ErrorCodes.NumericDigitMaxVal, "netImpressions", new Object[] { "Net Impressions", "9,999,999,999,999" },
						UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getReservationEmails())) {
			if (isValidEmailData(form.getReservationEmails())) {
				customErrors.rejectValue("reservationEmails", ErrorCodes.InvalidEmailString, "reservationEmails", new Object[] { "Reservation Emails" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (form.getReservationEmails().length() >= 500) {
				customErrors.rejectValue("reservationEmails", ErrorCodes.ExceedMaxAllowedCharacter, "reservationEmails", new Object[] { "Reservation Emails", "500" },
						UserHelpCodes.HelpMandatoryInputMissing);
			} else {
				final String[] emailArray = form.getReservationEmails().split(ConstantStrings.COMMA);
				final List<String> invalidEmailIds = new ArrayList<String>();
				for (int i = 0; i < emailArray.length; i++) {
					if (!isValidEmail(emailArray[i].trim())) {
						invalidEmailIds.add(emailArray[i]);
					}
				}
				if (invalidEmailIds.size() > 0 || emailArray.length == 0) {
					final String invalidEmailStr = invalidEmailIds.toString();
					customErrors.rejectValue("reservationEmails", ErrorCodes.InvalidEmail, "reservationEmails", new Object[] { invalidEmailStr }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		}

	}

	/**
	 * pattern to match consecutive commas
	 * @param str
	 * @return
	 */
	private boolean isValidEmailData(final String str) {
		final Pattern p = Pattern.compile(".*,\\s*,.*");
		final Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * pattern to match email for validation
	 * @param emailid
	 * @return
	 */
	private boolean isValidEmail(final String emailid) {
		final Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		final Matcher m = p.matcher(emailid);
		return m.matches();
	}

	public void validateBudget(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final ProposalForm form = (ProposalForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isNotBlank(form.getBudget())) {
			try {
				super.validateDecimalValues(form.getBudget(), 9999999999.99, 2);
			} catch (NumberFormatException e) {
				customErrors.rejectValue(BUDGET, ErrorCodes.PositiveDecimalDigit, BUDGET, new Object[] { BUDGET }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (MaxAllowedDecimalExceedExecption e) {
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDecimalValue, BUDGET, new Object[] { "2", BUDGET }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				customErrors.rejectValue(BUDGET, ErrorCodes.NumericDigitMaxVal, BUDGET, new Object[] { BUDGET, "9,999,999,999.00" }, UserHelpCodes.HelpMandatoryInputMissing);
			}

		} else {
			customErrors.rejectValue(BUDGET, ErrorCodes.MandatoryInputMissing, BUDGET, new Object[] { BUDGET }, UserHelpCodes.HelpMandatoryInputMissing);

		}
	}

	/**
	 * Validation check for SOS Order Id
	 * @param target
	 * @param errors
	 */
	public void validateSosOrderId(final Object target, final Errors errors) {
		final ProposalForm proposalForm = (ProposalForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(proposalForm.getSosOrderId())) {
			customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.MandatoryInputMissing, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2 }, UserHelpCodes.HelpMandatoryInputMissing);
		} else if (proposalForm.getSosOrderId().length() > 12) {
			customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.ExceedMaxAllowedCharacter, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2, "12" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateLongValues(proposalForm.getSosOrderId(), 999999999999L);
				final long value = Long.valueOf(proposalForm.getSosOrderId());
				if (value < 0) {
					// Only positive integers allowed in {SosOrder Id}.
					customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.PositiveNumber, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2 }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {SosOrder Id}.
				customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.IntegerNumber, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2 }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {SosOrder Id} is {999999999999}.
				customErrors.rejectValue(SOS_ORDER_ID, ErrorCodes.NumericDigitMaxVal, SOS_ORDER_ID, new Object[] { SOS_ORDER_ID2, "9,999,999,999,99" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}

	public boolean validateProposalNotes(final String description, final AjaxFormSubmitResponse ajaxResponse) {
		boolean isError = false;
		if (StringUtils.isBlank(description)) {
			ajaxResponse.getObjectMap().put("descriptionIsNull", true);
			isError = true;
		} else if (description.length() > 3072) {
			ajaxResponse.getObjectMap().put("descriptionLengthExceed3072", true);
			isError = true;
		} else if (AbstractValidator.containsXSSAttacks(description)) {
			ajaxResponse.getObjectMap().put("descriptionContainsXSSContent", true);
			isError = true;
		}
		return isError;
	}

	public void validateSosOrderDetails(final Object target, final Errors errors) {
		final SosIntegrationPojo sosIntegrationPojo = (SosIntegrationPojo) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(sosIntegrationPojo.getAdvertiser())) {
			customErrors.rejectValue("advertiser", ErrorCodes.MandatoryInputMissing, "advertiser", new Object[] { "Advertiser Name" }, UserHelpCodes.AdvertiserNameHelp);
		}
		if (StringUtils.isBlank(sosIntegrationPojo.getBillingAddress())) {
			customErrors.rejectValue("billingAddress_select2", ErrorCodes.MandatoryInputMissing, "billingAddress_select2", new Object[] { "Billing Address" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (sosIntegrationPojo.getBillingContact().length == 0) {
			customErrors.rejectValue("billingContact_custom", ErrorCodes.MandatoryInputMissing, "billingContact_custom", new Object[] { "Billing Contact" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(sosIntegrationPojo.getBillingMethod())) {
			customErrors.rejectValue("billingMethod_select2", ErrorCodes.MandatoryInputMissing, "billingMethod_select2", new Object[] { "Billing Method" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(sosIntegrationPojo.getPaymentMethod())) {
			customErrors.rejectValue("paymentMethod_select2", ErrorCodes.MandatoryInputMissing, "paymentMethod_select2", new Object[] { "Payment Method" }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		if (StringUtils.isBlank(sosIntegrationPojo.getBillto())) {
			customErrors.rejectValue("billto", ErrorCodes.MandatoryInputMissing, "billto", new Object[] { "Bill To" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

	}

	/**
	 * Validates the threshold value of all the options of a proposal.
	 * @param proposalOptionMap
	 * @param errors
	 */
	public void validateOptionsThresholdValue(final Map<Long, ProposalForm> proposalOptionMap, final Errors errors) {
		if (!proposalOptionMap.isEmpty()) {
			final CustomBindingResult customErrors = (CustomBindingResult) errors;
			for (Long optionId : proposalOptionMap.keySet()) {
				final ProposalForm proposalForm = proposalOptionMap.get(optionId);
				if (StringUtils.isBlank(proposalForm.getThresholdLimit())) {
					customErrors.rejectValue("Threshold_" + proposalForm.getOptionId(), ErrorCodes.OptionThresholdValueMandatory, "Threshold_" + proposalForm.getOptionId(), new Object[] {},
							UserHelpCodes.HelpMandatoryInputMissing);
				} else {
					try {
						super.validateDecimalValues(proposalForm.getThresholdLimit().replaceAll(",", ""), Double.valueOf(proposalForm.getQualifingLineItemInvestment().replaceAll(",", "")), 2);
						if (Double.valueOf(proposalForm.getThresholdLimit().replaceAll(",", "")) <= 0.0D) {
							customErrors.rejectValue("Threshold_" + proposalForm.getOptionId(), ErrorCodes.ThresholdLessThanEqualZero, "Threshold_" + proposalForm.getOptionId(), new Object[] {},
									UserHelpCodes.HelpMandatoryInputMissing);
						}
					} catch (NumberFormatException e) {
						customErrors.rejectValue("Threshold_" + proposalForm.getOptionId(), ErrorCodes.PositiveDecimalDigit, "Threshold_" + proposalForm.getOptionId(),
								new Object[] { THRESHOLD_VALUE }, UserHelpCodes.HelpMandatoryInputMissing);
					} catch (MaxAllowedDecimalExceedExecption e) {
						customErrors.rejectValue("Threshold_" + proposalForm.getOptionId(), ErrorCodes.NumericDecimalValue, "Threshold_" + proposalForm.getOptionId(), new Object[] { "2",
								THRESHOLD_VALUE }, UserHelpCodes.HelpMandatoryInputMissing);
					} catch (IllegalArgumentException e) {
						customErrors.rejectValue("Threshold_" + proposalForm.getOptionId(), ErrorCodes.OptionThresholdValueGreaterThanInvestment, "Threshold_" + proposalForm.getOptionId(),
								new Object[] { THRESHOLD_VALUE }, UserHelpCodes.HelpMandatoryInputMissing);
					}
				}
			}
		}
	}
	
	/**
	 * Validates the new saleforce id
	 * @param newSalesforceId
	 * @param ajaxResponse
	 * @return
	 */
	public boolean validateNewSalesforceMapping(final String newSalesforceId, final AjaxFormSubmitResponse ajaxResponse) {
		boolean isError = false;
		if (StringUtils.isBlank(newSalesforceId)) {
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "sfid.is.mandatory");
			isError = true;
		} else if (AbstractValidator.containsXSSAttacks(newSalesforceId)) {
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "error.contains.xssAttackCharacters");
			isError = true;
		} else if (newSalesforceId.length() != 15) {
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "sfid.length.not.valid");
			isError = true;
		} else if (sfAlphanumericPattern(newSalesforceId)) {
			ajaxResponse.getObjectMap().put(Constants.GRID_KEY_COLUMN_IDENTIFIER_VALUE, "error.contains.alphanumeric.salesforceId");
			isError = true;
		}
		return isError;
	}
}