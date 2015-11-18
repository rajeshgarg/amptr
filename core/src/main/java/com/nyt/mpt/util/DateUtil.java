/**
 * 
 */
package com.nyt.mpt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.nyt.mpt.domain.EmailScheduleDetails;
import com.nyt.mpt.util.enums.EmailScheduleFrequency;
import com.nyt.mpt.util.enums.Weekdays;
import com.nyt.mpt.util.exception.BusinessException;

/**
 * Utility class to provide date operations
 *
 * @author surendra.singh
 *
 */
public final class DateUtil {

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private static final String MM_DD_YYYY = "MM/dd/yyyy";
	
	private static final String DAY_MM_DD_YYYY = "EEEE, " + MM_DD_YYYY;

	private static final String MM_DD_YYYY_HH_MM = "MM/dd/yyyy HH:mm";
	
	private static final String HH_MM_SS = "HH:mm:ss:SSS";
	
	private static final String HH_MM = "HH:mm";
	
	private static final String HH_MM_AMORPM = "hh:mm a";

	private static final String SALESFORCE_TIMEZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	private static final String MM_DD = "MM/dd"; 
	
	private static final SimpleDateFormat SALES_CALENDAR_DATE_FORMAT = new SimpleDateFormat(MM_DD);

	private static final SimpleDateFormat GUI_DATE_FORMAT = new SimpleDateFormat(MM_DD_YYYY);

	private static final SimpleDateFormat GUI_DATE_TIME_FORMAT = new SimpleDateFormat(MM_DD_YYYY_HH_MM);

	private static final SimpleDateFormat YIELDEX_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
	
	private static final SimpleDateFormat SOS_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
	
	private static final SimpleDateFormat DAY_MM_DD_YYYY_FORMAT = new SimpleDateFormat(DAY_MM_DD_YYYY);
	
	private static final SimpleDateFormat GUI_TIME_FORMATTER = new SimpleDateFormat(HH_MM_SS);
	
	private static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	private static final String[] WEEKS = {"Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

	private DateUtil() {
		super();
	}

	/**
	 * Formats time into a string format - MM/dd/yyyy
	 *
	 * @param time
	 * @return
	 */
	public static String getGuiDateString(final long time) {
		synchronized (GUI_DATE_FORMAT) {
			return GUI_DATE_FORMAT.format(new Date(time));
		}
	}

	/**
	 * Formats date into a string format - MM/dd/yyyy
	 *
	 * @param date
	 * @return
	 */
	public static String getGuiDateString(final Date date) {
		synchronized (GUI_DATE_FORMAT) {
			return GUI_DATE_FORMAT.format(date);
		}
	}
	
	public static String getSalesCalendarDateString(final Date date) {
		synchronized (SALES_CALENDAR_DATE_FORMAT) {
			return SALES_CALENDAR_DATE_FORMAT.format(date);
		}
	}
	
	

	/**
	 * Formats date into a string format - MM/dd/yyyy
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateForListView(final Date date) {
		synchronized (GUI_DATE_FORMAT) {
			final Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTime(date);
			final int year = calendarDate.get(Calendar.YEAR);
			final int month = calendarDate.get(Calendar.MONTH);
			final int dayDate = calendarDate.get(Calendar.DAY_OF_MONTH);
			final int dayOfWeek = calendarDate.get(Calendar.DAY_OF_WEEK);
			
			final StringBuffer dateString = new StringBuffer();
			dateString.append(WEEKS[dayOfWeek-1]);
			dateString.append(", ");
			dateString.append(MONTHS[month]);
			dateString.append(ConstantStrings.SPACE);
			dateString.append(dayDate);
			dateString.append(", ");
			dateString.append(year);
			return dateString.toString();
		}
	}
	
	/**
	 * Formats date into a yieldex format - yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String getYieldexDateString(final Date date) {
		synchronized (YIELDEX_DATE_FORMAT) {
			return YIELDEX_DATE_FORMAT.format(date);
		}
	}
	
	/**
	 * Formats date into a Sos date format - yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String getSosDateString(final Date date) {
		synchronized (SOS_DATE_FORMAT) {
			return SOS_DATE_FORMAT.format(date);
		}
	}
	
	
	
	/**
	 * Formats date into a Day, MM/dd/yyyy formate. E.g - Friday, 05/10/2013
	 *
	 * @param date
	 * @return
	 */
	public static String getDayDateString(final long date) {
		synchronized (DAY_MM_DD_YYYY_FORMAT) {
			return DAY_MM_DD_YYYY_FORMAT.format(date);
		}
	}

	/**
	 * Formats time into a string format - MM/dd/yyyy HH:mm
	 *
	 * @param time
	 * @return
	 */
	public static String getGuiDateTimeString(final long time) {
		synchronized (GUI_DATE_TIME_FORMAT) {
			return GUI_DATE_TIME_FORMAT.format(new Date(time));
		}
	}
	
	/**
	 * formats date to MM/dd/yyyy HH:mm
	 * 
	 * @param date
	 * @return
	 */
	public static String getGuiDateTimeString(final Date date) {
		synchronized (GUI_DATE_TIME_FORMAT) {
			return GUI_DATE_TIME_FORMAT.format(date);
		}
	}
	public static String getGuiTimeString(final long time) {
        return GUI_TIME_FORMATTER.format(new Date(time));
    }

	/**
	 * Parses a string representing a date in 'MM/dd/yyyy HH:mm' format to date object
	 *
	 * @param date
	 * @return
	 */
	public static Date parseToDateTime(final String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				return DateUtils.parseDate(date, new String[] { MM_DD_YYYY_HH_MM });
			} catch (ParseException e) {
				throw new IllegalArgumentException("Error occured while parsing date - " + date, e);
			}
		}
		return null;
	}

	/**
	 * Parses a string representing a date in 'MM/dd/yyyy' format to date object
	 *
	 * @param date
	 * @return
	 */
	public static Date parseToDate(final String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				return DateUtils.parseDate(date, new String[] { MM_DD_YYYY });
			} catch (ParseException e) {
				throw new IllegalArgumentException("Error occured while parsing date - " + date, e);
			}
		}
		return null;
	}

	/**
	 * Parses a string representing a date in 'yyyy-MM-dd' format to date object
	 *
	 * @param date
	 * @return
	 */
	public static Date parseToDateForRemote(final String date) {
		if (StringUtils.isNotBlank(date)) {
			try {
				return DateUtils.parseDate(date, new String[] { YYYY_MM_DD });
			} catch (ParseException e) {
				throw new IllegalArgumentException("Error occured while parsing date - " + date, e);
			}
		}
		return null;
	}

	/**
	 * Return current date
	 *
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * returns current date embedded with user entered String Time in hh:mm format
	 * @param timeString
	 * @return
	 */
	public static Date getCurrentDateAndUserTime(final String timeString) {
		Date timeOnly = new Date();
		if (StringUtils.isNotBlank(timeString)) {
			try {
				timeOnly = DateUtils.parseDate(timeString, new String[] { HH_MM });
			} catch (ParseException e) {
				throw new IllegalArgumentException("Error occured while parsing date - " + timeString, e);
			}
		}
		final Calendar calendarTime = Calendar.getInstance();
		calendarTime.setTime(timeOnly);
		final Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(getCurrentDate());
		calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
		calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
		return calendarDate.getTime();
	}

	/**
	 * Return Current Date with MidNight Time
	 * @return
	 */
	public static Date getCurrentMidnightDate() {
		final Calendar calendar = Calendar.getInstance();
		setMidNightDate(calendar);
		return calendar.getTime();
	}
	
	/**
	 * @return
	 */
	public static Date getMidnightDateAfterThreeDay(){
		final Calendar calendar = Calendar.getInstance();
		setMidNightDate(calendar);
		calendar.add(Calendar.DAY_OF_MONTH, 3);
		return calendar.getTime();
	}

	/**
	 * Return Current Date with MidNight Time
	 * @return
	 */
	public static Date getTomorrowMidnightDate() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		setMidNightDate(calendar);
		return calendar.getTime();
	}
	
	private static void setMidNightDate(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Check whether given date is first day of quarter 
	 * @param cal
	 * @return
	 */
	public static boolean isFirstDayOfQuarter(final Date date) {
		final Calendar cal = Calendar.getInstance(); cal.setTime(date);
		final int month = cal.get(Calendar.MONTH);
		if (cal.get(Calendar.DAY_OF_MONTH) == 1 
				&& (month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JULY || month == Calendar.OCTOBER)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the date prior to current date by specified number of days.
	 * @param priorDaysCount
	 * @return
	 */
	public static Date getPriorDateFromCurrentDate(final int priorDaysCount){
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -priorDaysCount);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date convertToMidNightDate(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); setMidNightDate(calendar);
		return calendar.getTime();
	}
	
	/**
	 * @param date
	 * @param daysToAdd
	 * @return
	 */
	public static Date addDaysToDate(final Date date, final int daysToAdd) {
		final Calendar calendar = Calendar.getInstance();  calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + daysToAdd);
		return calendar.getTime();
	}
	
	/**
	 * conversion of date data-type to calendar
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateTypeToCalendar(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * converting system date-time to sales force date-time format
	 * 
	 * @param fetchDurationInMinutes
	 * @return
	 */
	public static String convertToSFDateFormatForFetching(final int fetchDurationInMinutes) {
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -fetchDurationInMinutes);
		final Date date = calendar.getTime();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(SALESFORCE_TIMEZONE_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone(ConstantStrings.SALESFORCE_GMT_TIMEZONE));
		return dateFormat.format(date);
	}

	/**
	 * merge a date object and time string into one date object
	 * 
	 * @param dueOnDate
	 * @param parseDate
	 * @return
	 */
	public static Date mergeDateAndTimeString(final Date dueOnDate, final String dueTime) {
		final SimpleDateFormat formatter = new SimpleDateFormat(HH_MM_AMORPM);
		Date parseDate = null;
		try {
			if(StringUtils.isNotBlank(dueTime))
				parseDate = formatter.parse(dueTime);
		} catch (ParseException exception) {
			throw new BusinessException(ConstantStrings.SALESFORCE_DATE_PARSE_EXCEPTION, new CustomBusinessError(), exception);
		}
		final Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(dueOnDate);
		final Calendar calendarTime = Calendar.getInstance();
		calendarTime.setTime(parseDate);
		calendarDate.set(Calendar.HOUR, calendarTime.get(Calendar.HOUR));
		calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));
		calendarDate.set(Calendar.AM_PM, calendarTime.get(Calendar.AM_PM));
		return calendarDate.getTime();
	}
	
	/**
	 * @param datesList
	 * @return
	 */
	public static List<String> convertDatesToStringFormat(final List<Date> datesList) {
		Collections.sort(datesList);
		final List<String> dateString = new ArrayList<String>(datesList.size());
		for (Date date : datesList) {
			dateString.add(DateUtil.getGuiDateString(date));
		}
		return dateString;
	}
	
	public static String[] getWeekdaysArr(final String weekdays) {
		if (StringUtils.isNotBlank(weekdays)) {
			return weekdays.split(ConstantStrings.COMMA);
		} else {
			final String returnArr[] = new String[7];
			returnArr[0] = Weekdays.SUNDAY.name();
			returnArr[1] = Weekdays.MONDAY.name();
			returnArr[2] = Weekdays.TUESDAY.name();
			returnArr[3] = Weekdays.WEDNESDAY.name();
			returnArr[4] = Weekdays.THURSDAY.name();
			returnArr[5] = Weekdays.FRIDAY.name();
			returnArr[6] = Weekdays.SATURDAY.name();
			return returnArr;
		}
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Map<String, List<Date>> getDaysBetweenDates(final Date startDate, final Date endDate) {
		final Map<String, List<Date>> returnMap = new HashMap<String, List<Date>>(7);
		initializeMap(returnMap);
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(endDate);

		final Calendar c2 = Calendar.getInstance();
		c2.setTime(startDate);

		while (c1.after(c2) || c1.equals(c2)) {
			if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				returnMap.get(Weekdays.SUNDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				returnMap.get(Weekdays.MONDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
				returnMap.get(Weekdays.TUESDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
				returnMap.get(Weekdays.WEDNESDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
				returnMap.get(Weekdays.THURSDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				returnMap.get(Weekdays.FRIDAY.name()).add(c2.getTime());
			} else if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				returnMap.get(Weekdays.SATURDAY.name()).add(c2.getTime());
			}
			c2.add(Calendar.DATE, 1);
		}
		return returnMap;

	}
	
	/**
	 * @param returnMap
	 */
	private static void initializeMap(final Map<String, List<Date>> returnMap) {
		returnMap.put(Weekdays.MONDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.TUESDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.WEDNESDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.THURSDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.FRIDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.SATURDAY.name(), new ArrayList<Date>());
		returnMap.put(Weekdays.SUNDAY.name(), new ArrayList<Date>());
	}
	
	public static List<Date> getAvailableDates(final EmailScheduleDetails scheduleDetails) {
		final List<Date> datesList = new ArrayList<Date>();
		final Map<String, List<Date>> daysMap = DateUtil.getDaysBetweenDates(scheduleDetails.getStartDate(), scheduleDetails.getEndDate());
		final String daysArr[] = DateUtil.getWeekdaysArr(scheduleDetails.getWeekdays());
		for (int i = 0; i < daysArr.length; i++) {
			if (StringUtils.isNotBlank(scheduleDetails.getFrequency())
					&& EmailScheduleFrequency.BIWEEKLY.name().equals(scheduleDetails.getFrequency())) {
				final List<Date> tempList = daysMap.get(Weekdays.findByName(daysArr[i]).name());
				for (int j = 0; j < tempList.size(); j = j + 2) {
					datesList.add(tempList.get(j));
				}
			} else {
				datesList.addAll(daysMap.get(Weekdays.findByName(daysArr[i]).name()));
			}
		}
		return datesList;
	}
	
	/**
	 * This method return current date time string on the basis of pattern to give as an argument
	 * @param pattern
	 * @return
	 */
	public static String getCurrentDateTimeString(final String pattern) {
    	return new SimpleDateFormat(pattern).format(Calendar.getInstance().getTime()).toString();
	}
	
	/**
	 * Returns the total number of days between start date and end date.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long daysBetween(Calendar startDate, Calendar endDate) {  
		long daysBetween = 0;  
		long noOfFlightMonths = monthsBetween(startDate , endDate);
		Calendar stDate = (Calendar) startDate.clone();
		if(noOfFlightMonths > 1){
			for(int i = 0 ; i < noOfFlightMonths ; i ++){
				if(i != (noOfFlightMonths - 1)){
					daysBetween += (stDate.getActualMaximum(Calendar.DAY_OF_MONTH) - stDate.get(Calendar.DAY_OF_MONTH) + 1);
				}else{
					daysBetween += endDate.get(Calendar.DAY_OF_MONTH);
				}
				stDate.add(Calendar.MONTH, 1);
				stDate.set(Calendar.DATE, 1);
			}
		}else{
			daysBetween += (endDate.get(Calendar.DAY_OF_MONTH) - stDate.get(Calendar.DAY_OF_MONTH) + 1);
		}
		return daysBetween;  
	}
	
	public static long monthsBetween(Calendar startDate, Calendar endDate){
		long noOfFlightMonths = 0;
		int noOfFlightYears = (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
		if(noOfFlightYears == 0){
			noOfFlightMonths = (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH)) + 1;
		}else if(noOfFlightYears == 1){
			noOfFlightMonths =  ((12 - startDate.get(Calendar.MONTH)) + endDate.get(Calendar.MONTH)) + 1;
		}else if(noOfFlightYears > 1){
			noOfFlightMonths = (12 * (noOfFlightYears - 1)) + ((12 - startDate.get(Calendar.MONTH)) + endDate.get(Calendar.MONTH)) + 1;
		}
		return noOfFlightMonths;
	}
}
