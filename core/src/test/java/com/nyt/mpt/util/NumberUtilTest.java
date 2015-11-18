package com.nyt.mpt.util;

import junit.framework.Assert;

import org.junit.Test;

import com.nyt.mpt.common.AbstractTest;

public class NumberUtilTest extends AbstractTest {

	@Test
	public void testFormatDouble() {
		Assert.assertNotNull(NumberUtil.formatDouble(12389.26, true));
	}

	@Test
	public void testFormatLong() {
		Assert.assertNotNull(NumberUtil.formatLong(12389, true));
	}

	@Test
	public void testStringToDoubleValue() {
		Assert.assertNotNull(NumberUtil.doubleValue("6564.59"));
	}

	@Test
	public void testStringToIntValue() {
		Assert.assertNotNull(NumberUtil.intValue("6564"));
	}

	@Test
	public void testStringToLongValue() {
		Assert.assertNotNull(NumberUtil.longValue("6564569"));
	}

	@Test
	public void testGetHalfCentFormatedValue() {
		Assert.assertNotNull(NumberUtil.getHalfCentFormatedValue(4.22));
	}

	@Test
	public void testRoundDoubleValue() {
		Assert.assertNotNull(NumberUtil.round(4895.2664, 2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRoundDoubleValueException() {
		NumberUtil.round(4895.2664, -2);
	}

	@Test(expected = NumberFormatException.class)
	public void testParseNumberException() {
		NumberUtil.longValue("ParseException");
	}
	
	@Test
	public void testgenRandom() {
		Assert.assertNotNull(NumberUtil.genRandom(123,143));
	}
	
}
