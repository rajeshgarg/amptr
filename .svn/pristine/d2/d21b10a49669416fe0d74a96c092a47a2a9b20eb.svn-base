/**
 * 
 */
package com.nyt.mpt.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class to provide numeric operations
 *
 * @author surendra.singh
 *
 */
public final class NumberUtil {

	private NumberUtil() {
		super();
	}

	/**
	 * Format a decimal number in two decimal digits and produce a String
	 *
	 * @param number				The number to format
	 * @param useGrouping			Set whether or not grouping will be used in this format
	 * @return
	 */
	public static String formatDouble(final double number, final boolean useGrouping) {
		return formatNumber(number, useGrouping, 2, 2);
	}

	/**
	 * Format a long number and produce a String
	 *
	 * @param number			The number to format
	 * @param useGrouping		Set whether or not grouping will be used in this format
	 * @return
	 */
	public static String formatLong(final long number, final boolean useGrouping) {
		return formatNumber(number, useGrouping, 0, 0);
	}

	/**
	 * Returns the value of the specified number as an integer. This may involve rounding or truncation.
	 *
	 * @param number
	 * @return
	 */
	public static int intValue(final String number) {
		return parseNumber(number).intValue();
	}

	/**
	 * Returns the value of the specified number as an long. This may involve rounding or truncation.
	 *
	 * @param number
	 * @return
	 */
	public static long longValue(final String number) {
		return parseNumber(number).longValue();
	}

	/**
	 * Returns the value of the specified number as a double. This may involve rounding.
 	 *
	 * @param number
	 * @return
	 */
	public static double doubleValue(final String number) {
		return parseNumber(number).doubleValue();
	}

	/**
	 * Parses text from the beginning of the given string to produce a number
	 *
	 * @param number
	 * @return
	 */
	private static Number parseNumber(final String number) {
		if (StringUtils.isNotBlank(number)) {
			final NumberFormat formatter = DecimalFormat.getNumberInstance();
			try {
				return formatter.parse(number);
			} catch (ParseException e) {
				throw new NumberFormatException("Error occured while parsing number - " + number);
			}
		}
		return 0;
	}

	/**
	 * Format a number and return string representation
	 *
	 * @param number				The number to format
	 * @param useGrouping			Set whether or not grouping will be used in this format
	 * @param minFractionDigits		Sets the minimum number of digits allowed in the fraction portion of number
	 * @param maxFractionDigits		Sets the maximum number of digits allowed in the fraction portion of number
	 * @return
	 */
	private static String formatNumber(final Number number, final boolean useGrouping, final int minFractionDigits, final int maxFractionDigits) {
		final NumberFormat formatter = DecimalFormat.getNumberInstance();
		formatter.setGroupingUsed(useGrouping);
		formatter.setMinimumFractionDigits(minFractionDigits);
		formatter.setMaximumFractionDigits(maxFractionDigits);
		return formatter.format(number);
	}

	/**
	 * Roundoff given value to some decimal place
	 *
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(final double value, final int places) {
	    if (places < 0) {
	    	throw new IllegalArgumentException("Round off place cannot be less than zero");
	    }
	    final long factor = (long) Math.pow(10, places);
	    final long tmp = Math.round(value * factor);
	    return (double) tmp / factor;
	}
	
	/**
	 * Round off pricing value to .05 cents.
	 * E.g - $4.23 -> $4.25
	 * 		 $4.22 -> $4.20 
	 * @param value
	 * @return
	 */
	public static double getHalfCentFormatedValue(final double value) {
		BigDecimal amount = new BigDecimal(value);
		// To round to the nearest .05, multiply by 20, round to the nearest integer, then divide by 20 
		BigDecimal result =  new BigDecimal(Math.round(amount.doubleValue() * 20.00) / 20.00); 
		return result.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	/**
	 * generates random numbers in given range
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static int genRandom(int minValue, int maxValue) {
		return (int) (minValue + ((new Random()).nextDouble() * (maxValue - minValue)));
	}
}
