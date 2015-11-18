/**
 * 
 */
package com.nyt.mpt.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.nyt.mpt.form.RateProfileDiscountsForm;
import com.nyt.mpt.form.RateProfileForm;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.CustomBindingResult;
import com.nyt.mpt.util.DateUtil;
import com.nyt.mpt.util.NumberUtil;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.UserHelpCodes;
import com.nyt.mpt.util.exception.MaxAllowedDecimalExceedExecption;

/**
 * This class is used to validate List Rate Profile
 * @author sachin.ahuja
 */
public class RateProfileValidator extends AbstractValidator implements Validator {

	private static final String _SEASONAL_DISCOUNT = "_seasonalDiscount";

	private static final String _START_DATE = "_startDate";

	private static final String SHEEP_IT_FORM = "sheepItForm_";

	private static final String BASE_PRICE = "basePrice";

	private static final Logger LOGGER = Logger.getLogger(RateProfileValidator.class);

	private static final String DISCOUNT = "Discount";

	@Override
	public boolean supports(final Class<?> clazz) {
		return RateProfileForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + target + "Error:" + errors);
		}
		final RateProfileForm form = (RateProfileForm) target;
		final CustomBindingResult customErrors = (CustomBindingResult) errors;

		if (StringUtils.isBlank(form.getProductId())) {
			customErrors.rejectValue("productId_select2", ErrorCodes.MandatoryInputMissing, "productId_select2", new Object[] { "Product Name" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (form.getSalesTargetId() == null || form.getSalesTargetId().length == 0) {
			customErrors.rejectValue("salesTargetId_custom", ErrorCodes.MandatoryInputMissing, "salesTargetId_custom", new Object[] { "Sales Target" }, UserHelpCodes.HelpMandatoryInputMissing);
		}

		if (StringUtils.isBlank(form.getBasePrice())) {
			customErrors.rejectValue(BASE_PRICE, ErrorCodes.MandatoryInputMissing, BASE_PRICE, new Object[] { "Base price" }, UserHelpCodes.HelpMandatoryInputMissing);
		} else {
			try {
				super.validateDecimalValues(form.getBasePrice(), 999999.99, 2);
				if (NumberUtil.doubleValue(form.getBasePrice()) <= 0.0) {
					customErrors.rejectValue(BASE_PRICE, ErrorCodes.NumericDigitMinVal, BASE_PRICE, new Object[] { "Base price" }, UserHelpCodes.HelpMandatoryInputMissing);
				}
			} catch (MaxAllowedDecimalExceedExecption e) {
				// Only {2} decimal digits allowed in {Base price}.
				customErrors.rejectValue(BASE_PRICE, ErrorCodes.NumericDecimalValue, BASE_PRICE, new Object[] { "2", "base price" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (NumberFormatException e) {
				// Only numeric digits allowed in {Base price}.
				customErrors.rejectValue(BASE_PRICE, ErrorCodes.NumericDigit, BASE_PRICE, new Object[] { "base price" }, UserHelpCodes.HelpMandatoryInputMissing);
			} catch (IllegalArgumentException e) {
				// Maximum allowed limit for {Base price} is {999999.99}.
				customErrors.rejectValue(BASE_PRICE, ErrorCodes.NumericDigitMaxVal, BASE_PRICE, new Object[] { "base price", "999,999.99" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}

		if (StringUtils.isNotBlank(form.getNotes())) {
			if (form.getNotes().length() > 500) {
				customErrors.rejectValue("notes", ErrorCodes.ExceedMaxAllowedCharacter, "notes", new Object[] { "Notes", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
			} else if (containsXSSAttacks(form.getNotes())) {
				customErrors.rejectValue("notes", ErrorCodes.ContainsXSSContent, "notes", new Object[] { "Notes", "500" }, UserHelpCodes.HelpMandatoryInputMissing);
			}
		}
	}

	public void validateDiscounts(final Set<RateProfileDiscountsForm> discountSet, final Errors errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validating. Object:" + discountSet + "Error:" + errors);
		}
		boolean isNot = false;
		if (!discountSet.isEmpty()) {
			final CustomBindingResult customErrors = (CustomBindingResult) errors;
			final List<RateProfileDiscountsForm> rangeCheckDiscountLst = new ArrayList<RateProfileDiscountsForm>();
			boolean isBlank = false;
			for (RateProfileDiscountsForm rateProfileDiscountsForm : discountSet) {
				if (StringUtils.isBlank(rateProfileDiscountsForm.getStartDate())) {
					customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _START_DATE, ErrorCodes.RateProfileDiscountStartDateMandatory, SHEEP_IT_FORM
							+ rateProfileDiscountsForm.getRowIndex() + _START_DATE, new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
					isBlank = true;
				}
				if (StringUtils.isBlank(rateProfileDiscountsForm.getEndDate())) {
					customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + "_endDate", ErrorCodes.RateProfileDiscountEndDateMandatory, SHEEP_IT_FORM
							+ rateProfileDiscountsForm.getRowIndex() + "_endDate", new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
					isBlank = true;
				}
				if (StringUtils.isBlank(rateProfileDiscountsForm.getDiscount())) {
					customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, ErrorCodes.RateProfileDiscountPercentMandatory, SHEEP_IT_FORM
							+ rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
				} else {
					try {
						super.validateDecimalValues(rateProfileDiscountsForm.getDiscount(), 99.99D, 2);
						if (Double.valueOf(rateProfileDiscountsForm.getDiscount().replaceAll(",", "")) <= 0.0D) {
							customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, ErrorCodes.DiscountLessThanEqualZero, SHEEP_IT_FORM
									+ rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
						}
					} catch (NumberFormatException e) {
						customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, ErrorCodes.PositiveDecimalDigit, SHEEP_IT_FORM
								+ rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, new Object[] { DISCOUNT }, UserHelpCodes.HelpMandatoryInputMissing);
					} catch (IllegalArgumentException e) {
						// Maximum allowed limit for {Discount} is {100}.
						customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, ErrorCodes.SeasonalDiscountMaxVal, SHEEP_IT_FORM
								+ rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, new Object[] {}, UserHelpCodes.HelpMandatoryInputMissing);
					} catch (MaxAllowedDecimalExceedExecption e) {
						customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, ErrorCodes.NumericDecimalValue, SHEEP_IT_FORM
								+ rateProfileDiscountsForm.getRowIndex() + _SEASONAL_DISCOUNT, new Object[] { "2", DISCOUNT }, UserHelpCodes.HelpMandatoryInputMissing);
					}
				}

				if (!(rangeCheckDiscountLst.isEmpty()) && !isBlank) {
					boolean isError = false;
					String conflictingDisSeqNo = ConstantStrings.EMPTY_STRING;
					for (RateProfileDiscountsForm discount : rangeCheckDiscountLst) {
						final long startDate = DateUtil.parseToDate(discount.getStartDate()).getTime();
						final long endDate = DateUtil.parseToDate(discount.getEndDate()).getTime();
						final long startDateNew = DateUtil.parseToDate(rateProfileDiscountsForm.getStartDate()).getTime();
						final long endDateNew = DateUtil.parseToDate(rateProfileDiscountsForm.getEndDate()).getTime();
						if (rateProfileDiscountsForm.getNot() && isNot) {
							isError = true;
							conflictingDisSeqNo = conflictingDisSeqNo + discount.getDiscountSeqNo() + ", ";
						} else if (!discount.getNot() && !rateProfileDiscountsForm.getNot() && !(startDateNew > endDate || endDateNew < startDate)) {
							isError = true;
							conflictingDisSeqNo = conflictingDisSeqNo + discount.getDiscountSeqNo() + ", ";
						} else if (discount.getNot() && !rateProfileDiscountsForm.getNot() && !(startDateNew >= startDate && endDateNew <= endDate)) {
							isError = true;
							conflictingDisSeqNo = conflictingDisSeqNo + discount.getDiscountSeqNo() + ", ";
						} else if (!discount.getNot() && rateProfileDiscountsForm.getNot() && !(startDate >= startDateNew && endDateNew >= endDate)) {
							isError = true;
							conflictingDisSeqNo = conflictingDisSeqNo + discount.getDiscountSeqNo() + ", ";
						}
					}
					if (isError) {
						conflictingDisSeqNo = conflictingDisSeqNo.trim().substring(0, conflictingDisSeqNo.trim().length() - 1);
						customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + _START_DATE, ErrorCodes.RateProfileDiscountDateRangeConflict, SHEEP_IT_FORM
								+ rateProfileDiscountsForm.getRowIndex() + _START_DATE, new Object[] { conflictingDisSeqNo }, UserHelpCodes.HelpMandatoryInputMissing);
						customErrors.rejectValue(SHEEP_IT_FORM + rateProfileDiscountsForm.getRowIndex() + "_endDate", ErrorCodes.RateProfileDiscountDateRangeConflict, SHEEP_IT_FORM
								+ rateProfileDiscountsForm.getRowIndex() + _START_DATE, new Object[] { conflictingDisSeqNo }, UserHelpCodes.HelpMandatoryInputMissing);
					}
				}
				if (StringUtils.isNotBlank(rateProfileDiscountsForm.getStartDate()) && StringUtils.isNotBlank(rateProfileDiscountsForm.getEndDate())) {
					rangeCheckDiscountLst.add(rateProfileDiscountsForm);
					if (rateProfileDiscountsForm.getNot()) {
						isNot = true;
					}
				}
			}
		}
	}
}
