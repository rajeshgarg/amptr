/**
 * 
 */
package com.nyt.mpt.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.domain.LineItemTarget;
import com.nyt.mpt.form.LineItemForm;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.Constants;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.TargetJsonConverter;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.LineItemPriceTypeEnum;
import com.nyt.mpt.util.enums.LineItemProductTypeEnum;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * This class is used to validate Package Line Items
 * @author ankit.phanda
 */
public class PackageLineItemsFormValidator extends AbstractValidator implements Validator {

	private static final String SOS_PRODUCT_ID_SELECT2 = "sosProductId_select2";
	private static final String MAX_LONG = "999,999,999,999";
	private static final String TOTAL_POSSIBLE_IMPRESSIONS = "totalPossibleImpressions";
	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String AVAILS = "avails";
	private static final String SOV = "sov";
	private static final String IMPRESSION_TOTAL = "impressionTotal";
	private static final String RATE = "rate";
	private static final String TOTAL_INVESTMENT = "totalInvestment";
	private static final String LINE_ITEM_SEQUENCE = "lineItemSequence";
	private static final String COMMENTS = "comments";
	private static final String FLIGHT = "flight";
	private static final String PLACEMENT_NAME = "placementName";
	private static final Logger LOGGER = Logger.getLogger(PackageLineItemsFormValidator.class);
	private static final String TARGETING_STRING = "targetingString";
	private static final String RATE_CARD_PRICE = "rateCardPrice";
	private static final String RATE_CARD_PRICE_STRING = "Rate Card Price";
	private static final String SOR = "sor";

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return LineItemForm.class.isAssignableFrom(clazz);
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final LineItemForm form = (LineItemForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isNotBlank(form.getPlacementName()) && form.getPlacementName().length() > 4000) {
			customErrors.rejectValue(PLACEMENT_NAME, ErrorCodes.ExceedMaxAllowedCharacter, PLACEMENT_NAME, new Object[] { "placement name", "4000" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else if (StringUtils.isNotBlank(form.getPlacementName()) && containsXSSAttacks(form.getPlacementName())) {
			customErrors.rejectValue(PLACEMENT_NAME, ErrorCodes.ContainsXSSContent, PLACEMENT_NAME, new Object[] { "PlacementName" }, UserHelpCodes.HelpXSSAttackCharacters);
		}
		if (StringUtils.isNotBlank(form.getTargetingString()) && form.getTargetingString().length() > 4000) {
			customErrors.rejectValue(TARGETING_STRING, ErrorCodes.ExceedMaxAllowedCharacter, TARGETING_STRING, new Object[] { "targeting string", "4000" }, UserHelpCodes.HelpTargetingString);
		}

		if (StringUtils.isNotBlank(form.getFlight()) && form.getFlight().length() > 30) {
			customErrors.rejectValue(FLIGHT, ErrorCodes.ExceedMaxAllowedCharacter, FLIGHT, new Object[] { FLIGHT, "30" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getComments()) && form.getComments().length() > 500) {
			customErrors.rejectValue(COMMENTS, ErrorCodes.ExceedMaxAllowedCharacter, COMMENTS, new Object[] { COMMENTS, "500" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getFlight()) && performsXSS(form.getFlight())) {
			customErrors.rejectValue(FLIGHT, ErrorCodes.containsXSSCharacters, FLIGHT, new Object[] { FLIGHT }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isNotBlank(form.getComments()) && containsXSSAttacks(form.getComments())) {
			customErrors.rejectValue(COMMENTS, ErrorCodes.ContainsXSSContent, COMMENTS, new Object[] { COMMENTS }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isBlank(form.getSosProductId())) {
			customErrors.rejectValue(SOS_PRODUCT_ID_SELECT2, ErrorCodes.MandatoryInputMissing, SOS_PRODUCT_ID_SELECT2, new Object[] { Constants.PRODUCT }, UserHelpCodes.HelpMandatorySelectMissing);
		}

		if (form.getSosSalesTargetId() == null || form.getSosSalesTargetId().length == 0) {
			customErrors.rejectValue("sosSalesTargetId_custom", ErrorCodes.MandatoryInputMissing, "sosSalesTargetId_custom", new Object[] { Constants.SALES_TARGET },
					UserHelpCodes.HelpMandatorySelectMissing);
		}

		if (form.getSpecType() == null || form.getSpecType().length == 0) {
			customErrors.rejectValue("specType_custom", ErrorCodes.MandatoryInputMissing, "specType_custom", new Object[] { ConstantStrings.SPEC_TYPE }, UserHelpCodes.HelpMandatorySelectMissing);
		}

		if (StringUtils.isBlank(form.getPlacementName())) {
			customErrors.rejectValue(PLACEMENT_NAME, ErrorCodes.MandatoryInputMissing, PLACEMENT_NAME, new Object[] { Constants.PLACEMENT }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (form.getPriceType() == null || StringUtils.isBlank(form.getPriceType())) {
			customErrors.rejectValue("cpm", ErrorCodes.MandatoryInputMissing, "cpm", new Object[] { Constants.PRICE_TYPE }, UserHelpCodes.HelpMandatoryInputMissing);

			customErrors.rejectValue(ConstantStrings.FLAT_RATE, ErrorCodes.MandatoryInputMissing, ConstantStrings.FLAT_RATE, new Object[] { Constants.PRICE_TYPE },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		/*
		 * Validating Line Item Sequence -Start
		 */
		if (StringUtils.isBlank(form.getLineItemSequence())) {
			customErrors.rejectValue(LINE_ITEM_SEQUENCE, ErrorCodes.MandatoryInputMissing, LINE_ITEM_SEQUENCE, new Object[] { Constants.LINEITEM_SEQUENCE }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateLongValues(form.getLineItemSequence().trim(), 999L);
				if (NumberUtil.longValue(form.getLineItemSequence().trim()) <= 0) {
					customErrors.rejectValue(LINE_ITEM_SEQUENCE, ErrorCodes.LineItemSequenceError, LINE_ITEM_SEQUENCE, new Object[] { "1", "999" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Sequence No.}.
				customErrors.rejectValue(LINE_ITEM_SEQUENCE, ErrorCodes.NumericDigit, LINE_ITEM_SEQUENCE, new Object[] { Constants.LINEITEM_SEQUENCE }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Sequence No.} is {999}.
				customErrors.rejectValue(LINE_ITEM_SEQUENCE, ErrorCodes.LineItemSequenceError, LINE_ITEM_SEQUENCE, new Object[] { "1", "999" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		// Total Investment
		if (ConstantStrings.FLAT_RATE.equalsIgnoreCase(form.getPriceType())) {
			if (StringUtils.isBlank(form.getTotalInvestment())) {
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.MandatoryInputMissing, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT }, UserHelpCodes.HelpMandatoryInputMissing);
			} else {
				try {
					super.validateDecimalValues(form.getTotalInvestment(), 9999999.99, 2);
				} catch (MaxAllowedDecimalExceedExecption e) {
					// Only {2} decimal digits allowed in {Total Investment}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDecimalValue, TOTAL_INVESTMENT, new Object[] { "2", Constants.TOTAL_INVESTMENT },
							UserHelpCodes.HelpMandatoryInputMissing);
				} catch (NumberFormatException e) {
					// Only numeric digits allowed in {Total Investment}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigit, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {Total Investment} is
					// {9,999,999.99}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigitMaxVal, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT, "9,999,999.99" },
							UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		} else if (ConstantStrings.LINE_ITEM_PRICE_TYPE_CPM.equalsIgnoreCase(form.getPriceType()) || ConstantStrings.LINE_ITEM_PRICE_TYPE_CUSTOM_UNIT.equals(form.getPriceType())
				|| ConstantStrings.LINE_ITEM_PRICE_TYPE_PRE_EMPTIBLE.equals(form.getPriceType())) {
			if (StringUtils.isBlank(form.getTotalInvestment())) {
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.MandatoryInputMissing, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT }, UserHelpCodes.HelpMandatoryInputMissing);
			} else {
				try {
					super.validateDecimalValues(form.getTotalInvestment(), 9999989999990.00, 2);
				} catch (MaxAllowedDecimalExceedExecption e) {
					// Only {2} decimal digits allowed in {Total Investment}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDecimalValue, TOTAL_INVESTMENT, new Object[] { "2", Constants.TOTAL_INVESTMENT },
							UserHelpCodes.HelpMandatoryInputMissing);
				} catch (NumberFormatException e) {
					// Only numeric digits allowed in {Total Investment}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigit, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {Total Investment} is
					// {9,999,989,999,990.00}.
					customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.NumericDigitMaxVal, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT, "9,999,989,999,990.00" },
							UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		} else {
			if (StringUtils.isBlank(form.getTotalInvestment())) {
				customErrors.rejectValue(TOTAL_INVESTMENT, ErrorCodes.MandatoryInputMissing, TOTAL_INVESTMENT, new Object[] { Constants.TOTAL_INVESTMENT }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		// Rate for CPM
		if (StringUtils.isNotBlank(form.getPriceType())
				&& (StringUtils.equals("CPM", form.getPriceType()) || StringUtils.equals("PRE EMPTIBLE", form.getPriceType()) || StringUtils.equals("CUSTOM UNIT", form.getPriceType()))) {
			if (StringUtils.isBlank(form.getRate())) {
				customErrors.rejectValue(RATE, ErrorCodes.MandatoryInputMissing, RATE, new Object[] { Constants.CPM }, UserHelpCodes.HelpMandatoryInputMissing);
			} else {
				try {
					super.validateDecimalValues(form.getRate(), 9999.99, 2);
				} catch (MaxAllowedDecimalExceedExecption e) {
					// Only {2} decimal digits allowed in {CPM}.
					customErrors.rejectValue(RATE, ErrorCodes.NumericDecimalValue, RATE, new Object[] { "2", Constants.CPM }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (NumberFormatException e) {
					// Only numeric digits allowed in {CPM}.
					customErrors.rejectValue(RATE, ErrorCodes.NumericDigit, RATE, new Object[] { Constants.CPM }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {CPM} is {9999999.99}.
					customErrors.rejectValue(RATE, ErrorCodes.NumericDigitMaxVal, RATE, new Object[] { Constants.CPM, "9,999.99" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			}
		}
		// Offered Impression
		String impressionsName = (LineItemProductTypeEnum.EMAIL.getShortName().equals(form.getProductType())) ? Constants.SUBSCRIBERS : Constants.IMPRESSIONS ; 
		if (StringUtils.isBlank(form.getImpressionTotal())) {
			customErrors.rejectValue(IMPRESSION_TOTAL, ErrorCodes.MandatoryInputMissing, IMPRESSION_TOTAL, new Object[] { impressionsName }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateLongValues(form.getImpressionTotal(), 999999999999L);
				if (NumberUtil.longValue(form.getImpressionTotal()) == 0) {
					customErrors.rejectValue(IMPRESSION_TOTAL, ErrorCodes.MandatoryInputMissing, IMPRESSION_TOTAL, new Object[] { impressionsName }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Offered Impression}.
				customErrors.rejectValue(IMPRESSION_TOTAL, ErrorCodes.NumericDigit, IMPRESSION_TOTAL, new Object[] { impressionsName }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Offered Impression} is
				// {999999999999}.
				customErrors.rejectValue(IMPRESSION_TOTAL, ErrorCodes.NumericDigitMaxVal, IMPRESSION_TOTAL, new Object[] { impressionsName, MAX_LONG }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getPriceType()) && !(StringUtils.equals(LineItemPriceTypeEnum.FLATRATE.getOptionValue(), form.getPriceType()))) {
			if (StringUtils.isBlank(form.getRateCardPrice())) {
				customErrors.rejectValue(RATE_CARD_PRICE, ErrorCodes.MandatoryInputMissing, RATE_CARD_PRICE, new Object[] { RATE_CARD_PRICE_STRING },
						UserHelpCodes.HelpMandatoryToCalculateBasePriceMissing);
			}
		}

		// SOV
		if (StringUtils.isNotBlank(form.getSov()) && "S".equals(form.getProductType())) {
			try {
				super.validateDecimalValues(form.getSov(), 100, 2);
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {SOV}.
				customErrors.rejectValue(SOV, ErrorCodes.NumericDecimalValue, SOV, new Object[] { "2", Constants.SOV }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {SOV}.
				customErrors.rejectValue(SOV, ErrorCodes.NumericDigit, SOV, new Object[] { Constants.SOV }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {SOV} is {100}.
				customErrors.rejectValue(SOV, ErrorCodes.NumericDigitMaxVal, SOV, new Object[] { Constants.SOV, "100" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		// Avails validation
		String startDateName = (LineItemProductTypeEnum.EMAIL.getShortName().equals(form.getProductType())) ? Constants.SEND_DATE : Constants.START_DATE ; 
		String dateName = (LineItemProductTypeEnum.EMAIL.getShortName().equals(form.getProductType())) ? Constants.SEND_DATE : Constants.START_DATE + " with " + Constants.END_DATE;
		try {
			super.validateLongValues(form.getAvails(), 999999999999L);
		} catch (NumberFormatException e) {
			// Only numeric digits allowed in {Budget}.
			customErrors.rejectValue(AVAILS, ErrorCodes.NumericDigit, AVAILS, new Object[] { Constants.AVAILS }, UserHelpCodes.HelpMandatoryInputMissing);
		} catch (IllegalArgumentException e) {
			// Maximum allowed limit for {Budget} is {9999999999.99}.
			customErrors.rejectValue(AVAILS, ErrorCodes.NumericDigitMaxVal, AVAILS, new Object[] { Constants.AVAILS, MAX_LONG }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isBlank(form.getFlight()) && (StringUtils.isBlank(form.getStartDate()) && StringUtils.isBlank(form.getEndDate()))) {
			customErrors.rejectValue(END_DATE, ErrorCodes.LineItemFlightError, END_DATE, new Object[] { dateName }, UserHelpCodes.LineItemFlightHelp);
			customErrors.rejectValue(START_DATE, ErrorCodes.LineItemFlightError, START_DATE, new Object[] { dateName }, UserHelpCodes.LineItemFlightHelp);
			customErrors.rejectValue(FLIGHT, ErrorCodes.LineItemFlightError, FLIGHT, new Object[] { dateName}, UserHelpCodes.LineItemFlightHelp);
		}

		if ((StringUtils.isNotBlank(form.getStartDate()) && StringUtils.isBlank(form.getEndDate())) || StringUtils.isNotBlank(form.getEndDate()) && StringUtils.isBlank(form.getStartDate())) {
			customErrors.rejectValue(END_DATE, ErrorCodes.LineItemFlightRedundantError, END_DATE, new Object[] { Constants.END_DATE }, UserHelpCodes.LineItemFlightRedundanthelp);
			customErrors.rejectValue(START_DATE, ErrorCodes.LineItemFlightRedundantError, START_DATE, new Object[] { startDateName }, UserHelpCodes.LineItemFlightRedundanthelp);
		}
		
		if ((StringUtils.isNotBlank(form.getStartDate()) && StringUtils.isNotBlank(form.getEndDate()))) {
			if(DateUtil.parseToDate(form.getStartDate()).after(DateUtil.parseToDate(form.getEndDate()))){
				customErrors.rejectValue(END_DATE, ErrorCodes.LineItemFlightIncorrectEndDate, END_DATE, new Object[] { Constants.END_DATE , startDateName}, UserHelpCodes.LineItemEndDateHelp);
				customErrors.rejectValue(START_DATE, ErrorCodes.LineItemFlightIncorrectStartDate, START_DATE, new Object[] { startDateName , Constants.END_DATE }, UserHelpCodes.LineItemStartDateHelp);
			}
		}
		// Impression capacity
		try {
			super.validateLongValues(form.getTotalPossibleImpressions(), 999999999999L);
		} catch (NumberFormatException e) {
			// Only numeric digits allowed in {Budget}.
			customErrors.rejectValue(TOTAL_POSSIBLE_IMPRESSIONS, ErrorCodes.NumericDigit, TOTAL_POSSIBLE_IMPRESSIONS, new Object[] { Constants.TOTAL_IMPRESSIONS },
					UserHelpCodes.HelpMandatoryInputMissing);
		} catch (IllegalArgumentException e) {
			// Maximum allowed limit for {Budget} is {9999999999.99}.
			customErrors.rejectValue(TOTAL_POSSIBLE_IMPRESSIONS, ErrorCodes.NumericDigitMaxVal, TOTAL_POSSIBLE_IMPRESSIONS, new Object[] { Constants.TOTAL_IMPRESSIONS, MAX_LONG },
					UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (LineItemProductTypeEnum.EMAIL.getShortName().equals(form.getProductType()) && form.isReserved() && (StringUtils.isBlank(form.getStartDate()) && StringUtils.isBlank(form.getEndDate()))) {
			customErrors.rejectValue(START_DATE, ErrorCodes.EmailDatesMandatory, START_DATE, new Object[] { startDateName }, UserHelpCodes.HelpMandatoryInputMissing);
		}
		
		// SOR
		if (LineItemProductTypeEnum.RESERVABLE.getShortName().equals(form.getProductType())) {
			if (form.isReserved() && (StringUtils.isBlank(form.getStartDate()) && StringUtils.isBlank(form.getEndDate()))) {
				customErrors.rejectValue(END_DATE, ErrorCodes.DatesMandatory, END_DATE, new Object[] { Constants.END_DATE }, UserHelpCodes.HelpMandatoryInputMissing);
				customErrors.rejectValue(START_DATE, ErrorCodes.DatesMandatory, START_DATE, new Object[] { startDateName }, UserHelpCodes.HelpMandatoryInputMissing);
			}

			if (StringUtils.isNotBlank(form.getSor())) {
				try {
					super.validateDecimalValues(form.getSor(), 100, 2);
					if (Double.valueOf(form.getSor()) <= 0.00) {
						customErrors.rejectValue(SOR, ErrorCodes.SorLimit, SOR, new Object[] { Constants.SOR, "100", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
					}
				} catch (MaxAllowedDecimalExceedExecption e) {
					// Only {2} decimal digits allowed in {SOR}.
					customErrors.rejectValue(SOR, ErrorCodes.NumericDecimalValue, SOR, new Object[] { "2", Constants.SOR }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (NumberFormatException e) {
					// Only numeric digits allowed in {SOR}.
					customErrors.rejectValue(SOR, ErrorCodes.NumericDigit, SOR, new Object[] { Constants.SOR }, UserHelpCodes.HelpMandatoryInputMissing);
				} catch (IllegalArgumentException e) {
					// Maximum allowed limit for {SOR} is {100}.
					customErrors.rejectValue(SOR, ErrorCodes.SorLimit, SOR, new Object[] { Constants.SOR, "100", "0" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} else {
				customErrors.rejectValue(SOR, ErrorCodes.MandatoryInputMissing, SOR, new Object[] { Constants.SOR }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
		if(form.getAvailSystemId() != null && (form.getAvailSystemId() == 15 || form.getAvailSystemId() == 16) && validateTargetingForDFP(form)){
			customErrors.rejectValue("geoTargeting", ErrorCodes.DFPTargeting, "geoTargeting", new Object[] { }, UserHelpCodes.HelpMandatoryInputMissing);
		}
	}

	/**
	 * @param form
	 */
	private boolean validateTargetingForDFP(LineItemForm form) {
		boolean returnFlag = false;
		final Set<LineItemTarget> lineItemTargets =  TargetJsonConverter.convertJsonToObject(form.getLineItemTargetingData(), null);
		final Map<Long, Boolean> validateTargetMap = new HashMap<Long, Boolean>();
		Long targetTypeId = 0L;
		for (LineItemTarget lineItemTarget : lineItemTargets) {
			if (ConstantStrings.OR.equals(lineItemTarget.getOperation())) {
				returnFlag = true;
				break;
			}
			// this ensures that region and country will be treated as same 
			if (lineItemTarget.getSosTarTypeId() == 35L) {
				targetTypeId = 8L;
			}else{
				targetTypeId = lineItemTarget.getSosTarTypeId();
			}
			if (!(targetTypeId == 5L || targetTypeId == 6L ||targetTypeId == 8L ||targetTypeId == 35L ||targetTypeId == 37L || targetTypeId == 38L ||targetTypeId == 40L || targetTypeId == 42L)) {
				returnFlag = true;
				break;
			}
			if (validateTargetMap.containsKey(targetTypeId)) {
				if (getNegationBoolean(lineItemTarget.getNegation()) || validateTargetMap.get(targetTypeId)) {
					if(!(getNegationBoolean(lineItemTarget.getNegation()) && validateTargetMap.get(targetTypeId))){
						returnFlag = true;
						break;
					}
				}
			} else {
				validateTargetMap.put(targetTypeId, getNegationBoolean(lineItemTarget.getNegation()));
			}
		}
		return returnFlag;
	}
	
	private Boolean getNegationBoolean(String negation) {
		return ("Not".equals(negation) ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * This is used to validate Line Items product and Sales Target while calculate base price
	 * 
	 * @param target
	 * @param errors
	 */
	public void pricingCalculatorValidate(final Object target, final Errors errors) {
		final LineItemForm form = (LineItemForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (form.getSosSalesTargetId() == null || form.getSosSalesTargetId().length == 0) {
			customErrors.rejectValue("sosSalesTargetId_custom", ErrorCodes.MandatoryInputMissing, "_sosSalesTargetId_custom", new Object[] { Constants.SALES_TARGET },
					UserHelpCodes.HelpMandatorySelectMissing);
		}
		if (StringUtils.isBlank(form.getSosProductId())) {
			customErrors.rejectValue(SOS_PRODUCT_ID_SELECT2, ErrorCodes.MandatoryInputMissing, SOS_PRODUCT_ID_SELECT2, new Object[] { Constants.PRODUCT }, UserHelpCodes.HelpMandatorySelectMissing);
		}
	}
	
	public void validateReservations(final Object target, final Errors errors){
		final LineItemForm lineItemForm = (LineItemForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		if (lineItemForm.getReservationExpiryDate() != null && !ConstantStrings.EMPTY_STRING.equals(lineItemForm.getReservationExpiryDate())) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 12);
			String startDateName = (LineItemProductTypeEnum.EMAIL.getShortName().equals(lineItemForm.getProductType())) ? Constants.SEND_DATE : Constants.START_DATE ;
			if (StringUtils.isNotBlank(lineItemForm.getStartDate())
					&& DateUtil.parseToDate(lineItemForm.getStartDate()).before(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))) {
				customErrors.rejectValue("reservedChkBoxSpan", ErrorCodes.LineItemReservationPastEndDateError, "reservedChkBoxSpan", new Object[] {startDateName},
						startDateName.equals(Constants.START_DATE) ? UserHelpCodes.LineItemStartDateHelp : UserHelpCodes.LineItemSendDateHelp);
			}else if(DateUtil.parseToDate(lineItemForm.getReservationExpiryDate()).before(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))){
				customErrors.rejectValue("reservedChkBoxSpan", ErrorCodes.LineItemReservationPastEndDateError, "reservedChkBoxSpan", new Object[] {"expiry date"},
						UserHelpCodes.HelpExpiryDateBeforeCurrent);
			}else if(DateUtil.parseToDate(lineItemForm.getReservationExpiryDate()).after(DateUtil.parseToDate(DateUtil.getGuiDateString(cal.getTime())))){
				customErrors.rejectValue("reservedChkBoxSpan", ErrorCodes.LineItemExpiryBeyondOeYear, "reservedChkBoxSpan", new Object[] {},
						UserHelpCodes.HelpExpiryDateBeyondOneYear);
			}
		}
	}
	
	
	
	public void validateReservationsRenewDate(String startDate, String expiryDate, String productType, final Errors errors){
		final CustomBindingResult customErrors = (CustomBindingResult) errors;
		if (expiryDate != null && !ConstantStrings.EMPTY_STRING.equals(expiryDate)) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 12);
			String startDateName = (LineItemProductTypeEnum.EMAIL.getShortName().equals(productType)) ? Constants.SEND_DATE : Constants.START_DATE ;
			if (StringUtils.isNotBlank(startDate)
					&& DateUtil.parseToDate(startDate).before(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))) {
				customErrors.rejectValue("renewDate", ErrorCodes.LineItemReservationPastEndDateError, "renewDate", new Object[] {startDateName},
						startDateName.equals(Constants.START_DATE) ? UserHelpCodes.LineItemStartDateHelp : UserHelpCodes.LineItemSendDateHelp);
			}else if(DateUtil.parseToDate(expiryDate).before(DateUtil.parseToDate(DateUtil.getGuiDateString(new Date())))){
				customErrors.rejectValue("renewDate", ErrorCodes.LineItemReservationPastEndDateError, "renewDate", new Object[] {"expiry date"},
						UserHelpCodes.HelpExpiryDateBeforeCurrent);
			}else if(DateUtil.parseToDate(expiryDate).after(DateUtil.parseToDate(DateUtil.getGuiDateString(cal.getTime())))){
				customErrors.rejectValue("renewDate", ErrorCodes.LineItemExpiryBeyondOeYear, "renewDate", new Object[] {},
						UserHelpCodes.HelpExpiryDateBeyondOneYear);
			}
		}
	}
}
