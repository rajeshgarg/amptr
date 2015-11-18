package com.nyt.mpt.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nyt.mpt.common.AbstractTest;
import com.nyt.mpt.domain.EmailSchedule;
import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.service.IEmailScheduleService;

public class DateUtilTest extends AbstractTest {
	
	@Autowired
	private IEmailScheduleService emailScheduleService;
	
	private Date date = new Date();

	@Before
	public void setup() {
		super.setAuthenticationInfo();
	}

	@Test
	public void testGetGuiDateString() {
		Assert.assertNotNull(DateUtil.getGuiDateString(date.getTime()));
	}

	@Test
	public void testGetGuiDateString2() {
		Assert.assertNotNull(DateUtil.getGuiDateString(date));
	}

	@Test
	public void testGetGuiDateTimeString() {
		Assert.assertNotNull(DateUtil.getGuiDateTimeString(date.getTime()));
	}

	@Test
	public void testGetGuiTimeString() {
		Assert.assertNotNull(DateUtil.getGuiTimeString(date.getTime()));
	}

	@Test
	public void testGetYieldexDateString() {
		Assert.assertNotNull(DateUtil.getYieldexDateString(date));
	}

	@Test
	public void testParseToDateTime() {
		Assert.assertNotNull(DateUtil.parseToDateTime(new SimpleDateFormat("MM/dd/yyyy HH:mm").format(date)));
	}

	@Test
	public void testParseToDate() {
		Assert.assertNotNull(DateUtil.parseToDate(DateUtil.getGuiDateString(date)));
	}

	@Test
	public void testParseToDateForRemote() {
		Assert.assertNotNull(DateUtil.parseToDateForRemote(new SimpleDateFormat("yyyy-MM-dd").format(date)));
	}

	@Test
	public void getCurrentDate() {
		Assert.assertNotNull(DateUtil.getCurrentDate());
	}

	@Test
	public void testDateTypeToCalendar() {
		Assert.assertNotNull(DateUtil.dateTypeToCalendar(date));
	}

	@Test
	public void testFormatDateForListView() {
		Assert.assertNotNull(DateUtil.formatDateForListView(date));
	}

	@Test
	public void testGetSosDateString() {
		Assert.assertNotNull(DateUtil.getSosDateString(date));
	}

	@Test
	public void testGetDayDateString() {
		Assert.assertNotNull(DateUtil.getDayDateString(date.getTime()));
	}

	@Test
	public void testgetGuiDateTimeString() {
		Assert.assertNotNull(DateUtil.getGuiDateTimeString(date));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testparseToDateTime() {
		DateUtil.parseToDateTime("ParseException");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testparseToDate() {
		DateUtil.parseToDate("ParseException");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testparseToDateForRemote() {
		DateUtil.parseToDateForRemote("ParseException");
	}

	@Test
	public void testgetCurrentDateAndUserTime() {
		Assert.assertNotNull(DateUtil.getCurrentDateAndUserTime("23:59"));
	}

	@Test
	public void testgetCurrentMidnightDate() {
		Assert.assertNotNull(DateUtil.getCurrentMidnightDate());
	}

	@Test
	public void testgetTomorrowMidnightDate() {
		Assert.assertNotNull(DateUtil.getTomorrowMidnightDate());
	}

	@Test
	public void testisFirstDayOfQuarter() {
		Assert.assertNotNull(DateUtil.isFirstDayOfQuarter(date));
	}

	@Test
	public void testgetPriorDateFromCurrentDate() {
		Assert.assertNotNull(DateUtil.getPriorDateFromCurrentDate(2));
	}

	@Test
	public void testconvertToMidNightDate() {
		Assert.assertNotNull(DateUtil.convertToMidNightDate(date));
	}

	@Test
	public void testaddDaysToDate() {
		Assert.assertNotNull(DateUtil.addDaysToDate(date, 3));
	}

	@Test
	public void tesconvertToSFDateFormatForFetching() {
		Assert.assertNotNull(DateUtil.convertToSFDateFormatForFetching(30));
	}

	@Test
	public void testmergeDateAndTimeString() {
		Assert.assertNotNull(DateUtil.mergeDateAndTimeString(DateUtil.getCurrentDate(), "12:59 PM"));
	}

	@Test
	public void testconvertDatesToStringFormat() {
		@SuppressWarnings("serial")
		List<Date> datesList = new ArrayList<Date>() {
			{
				add(DateUtil.getCurrentDate());
				add(DateUtil.getCurrentMidnightDate());
				add(DateUtil.getMidnightDateAfterThreeDay());
			}
		};
		Assert.assertNotNull(DateUtil.convertDatesToStringFormat(datesList));
	}

	@Test
	public void testdaysBetween() {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(DateUtil.getCurrentDate());
		Assert.assertTrue(DateUtil.daysBetween(startDate, DateUtil.dateTypeToCalendar(DateUtil.addDaysToDate(DateUtil.getCurrentDate(), 60))) > 0);
	}

	@Test
	public void testgetCurrentDateTimeString() {
		Assert.assertNotNull(DateUtil.getCurrentDateTimeString("HH:mm"));
	}
	
	@Test
	public void testgetDaysBetweenDates() {
		Assert.assertNotNull(DateUtil.getDaysBetweenDates(DateUtil.getCurrentDate(), DateUtil.addDaysToDate(DateUtil.getCurrentDate(), 60)));
	}
	
	@Test
	public void testgetAvailableDates() {
		final List<Date> datesList = new ArrayList<Date>();
		final List<EmailSchedule> emailScheduleLst = emailScheduleService.getFilteredEmailDetailList(null, null, null);
		for (EmailSchedule emailSchedule : emailScheduleLst) {
			for (EmailScheduleDetails scheduleDetails : emailSchedule.getEmailSchedules()) {
				datesList.addAll(DateUtil.getAvailableDates(scheduleDetails));
			}
		}
	}
}
