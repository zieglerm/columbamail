package org.columba.calendar.ui.calendar;

import junit.framework.TestCase;

import com.miginfocom.util.dates.DateRange;

public class MainCalendarControllerTest extends TestCase {
	
	private DateRange resetClock(DateRange in) {
		DateRange range = in;
		range.setStartField(java.util.Calendar.HOUR_OF_DAY, 0);
		range.setStartField(java.util.Calendar.MINUTE, 0);
		range.setStartField(java.util.Calendar.SECOND, 0);
		range.setStartField(java.util.Calendar.MILLISECOND, 0);
		range.setEndField(java.util.Calendar.HOUR_OF_DAY, 23);
		range.setEndField(java.util.Calendar.MINUTE, 59);
		range.setEndField(java.util.Calendar.SECOND, 59);
		range.setEndField(java.util.Calendar.MILLISECOND, 999);
		return range;
	}
	
	// FIXME missing full tests for leap year

	// ************
	// normal cases
	// ************
	
	public void testRollWithYearsNormalCaseDay() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 1);
		range = resetClock(range);

		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, 1);
		
		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(2, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(2, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsNormalCaseWeek() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 7);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);
		
		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(8, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(14, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsNormalCaseDayNegative() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 2);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 2);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, -1);
		
		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(1, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(1, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsNormalCaseWeekNegative() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 8);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 14);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, -1);
		
		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(1, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(7, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	// ***********
	// edge values
	// ***********

	public void testRollWithYearsFirstDayCaseDayNegative() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, -1);
		
		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(365, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2006, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(11, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(365, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseDay() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		range.setEndField(java.util.Calendar.YEAR, 2006);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, 1);
		
		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(1, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(1, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsFirstDayCaseWeekNegative() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 7);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, -1);

		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(359, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2006, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(11, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(365, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseWeek() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 359);
		
		range.setEndField(java.util.Calendar.YEAR, 2006);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(1, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(7, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	// ******************
	// more than one year
	// ******************

	public void testRollWithYearsFirstDayCaseDayNegativeMore() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, -366);
		
		assertEquals(2005, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(365, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2005, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(11, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(365, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseDayMore() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		range.setEndField(java.util.Calendar.YEAR, 2006);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, 366);
		
		assertEquals(2008, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(1, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2008, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(1, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsFirstDayCaseWeekNegativeMore() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 1);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 7);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, -54);

		assertEquals(2005, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(353, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2005, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(11, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(359, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseWeekMore() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 359);
		
		range.setEndField(java.util.Calendar.YEAR, 2006);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 53);

		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(365, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2008, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(6, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	public void testROllWithYearsViewToday() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2011);
		range.setStartField(java.util.Calendar.MONTH, 1);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 37);
		
		range.setEndField(java.util.Calendar.YEAR, 2011);
		range.setEndField(java.util.Calendar.MONTH, 2);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 85);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, -1510);

		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(352, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(1, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(35, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	public void testPlusYears() {
		
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2005);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 351);
		
		range.setEndField(java.util.Calendar.YEAR, 2005);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 361);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.DAY_OF_YEAR, 379);

		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(365, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(10, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	// ***************
	// MONTH VIEW TEST
	// ***************
	
	public void testRollWithYearsLastDayCaseMonth() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 10);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 329);
		
		range.setEndField(java.util.Calendar.YEAR, 2006);
		range.setEndField(java.util.Calendar.MONTH, 11);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(336, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(0, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(7, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseBack() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2006);
		range.setStartField(java.util.Calendar.MONTH, 10);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 334);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 7);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, -1);

		assertEquals(2006, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(10, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(327, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2006, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(11, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(365, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseForward() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 358);
		
		range.setEndField(java.util.Calendar.YEAR, 2008);
		range.setEndField(java.util.Calendar.MONTH, 1);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 33);
		
		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(365, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2008, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(1, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(40, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	public void testRollWithYearsLastDayCaseForwardTwo() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2004);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 361);
		
		range.setEndField(java.util.Calendar.YEAR, 2005);
		range.setEndField(java.util.Calendar.MONTH, 0);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 29);
		
		range = resetClock(range);

		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2005, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(0, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(2, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2005, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(1, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(36, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}
	
	public void testRollWithYearsLastDayCaseBackTwo() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2005);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 2);
		
		range.setEndField(java.util.Calendar.YEAR, 2005);
		range.setEndField(java.util.Calendar.MONTH, 1);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 43);
		
		range = resetClock(range);

		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, -1);

		assertEquals(2004, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(361, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2005, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(1, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(36, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	public void testRollWithYearsLastDayCaseForwardThree() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 357);
		
		range.setEndField(java.util.Calendar.YEAR, 2008);
		range.setEndField(java.util.Calendar.MONTH, 1);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 33);
		
		range = resetClock(range);

		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(11, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(364, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2008, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(1, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(40, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	// new vis range = Start: Sep 23, 2007 12:00:00 AM (RAW:1.0, DST:-1.0)   End: Nov 3, 2007 11:59:59 PM (RAW:1.0, DST:0.0)  Time zone: null  Locale: null
	// new vis range = Start: Oct 1, 2007 12:00:00 AM (RAW:1.0, DST:-1.0)   End: Nov 11, 2007 11:59:59 PM (RAW:1.0, DST:0.0)  Time zone: null  Locale: null
	// should be
	// new vis range = Start: Oct 1, 2007 12:00:00 AM (RAW:1.0, DST:-1.0)   End: Nov 10, 2007 11:59:59 PM (RAW:1.0, DST:0.0)  Time zone: null  Locale: null
	public void testRollWithYearsLastDayCaseForwardFour() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2007);
		range.setStartField(java.util.Calendar.MONTH, 8);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 266);
		
		range.setEndField(java.util.Calendar.YEAR, 2007);
		range.setEndField(java.util.Calendar.MONTH, 10);
		range.setEndField(java.util.Calendar.DAY_OF_YEAR, 307);
		
		range = resetClock(range);

		MainCalendarController.rollWithYears(range, java.util.Calendar.WEEK_OF_YEAR, 1);

		assertEquals(2007, range.getStartField(java.util.Calendar.YEAR));
		assertEquals(8, range.getStartField(java.util.Calendar.MONTH));
		assertEquals(273, range.getStartField(java.util.Calendar.DAY_OF_YEAR));
		
		assertEquals(2007, range.getEndField(java.util.Calendar.YEAR));
		assertEquals(10, range.getEndField(java.util.Calendar.MONTH));
		assertEquals(314, range.getEndField(java.util.Calendar.DAY_OF_YEAR));
		
	}

	// java bugs
	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4197699
	//
	// For JDK1.2.1_003, WEEK_OF_YEAR returns 1 instead of 53 for 12/31/1999 and also
	// 1 instead of 54 for 12/31/2000, (It returns 1 for 1/1/1999 and 1/1/2000).

	public void testWeekFiftyThree() {

		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2004);
		range.setStartField(java.util.Calendar.MONTH, 11);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 365);
		
		assertEquals(53, MainCalendarController.getWeekOfYear(range.getStart()));

	}
	
	public void testWeekFiftyFour() {
		
		int[] fiftythree = new int[] { 1970, 1976, 1987, 1992, 1998 };
		
		for (int i = 0; i < fiftythree.length; i++) {

			DateRange range = new DateRange();
			range.setStartField(java.util.Calendar.YEAR, fiftythree[i]);
			range.setStartField(java.util.Calendar.MONTH, 11);
			range.setStartField(java.util.Calendar.DAY_OF_YEAR, 365);
			
			assertEquals(53, MainCalendarController.getWeekOfYear(range.getStart()));

		}
		
	}
	
	public void testNonFiftyThree() {
		DateRange range = new DateRange();
		range.setStartField(java.util.Calendar.YEAR, 2005);
		range.setStartField(java.util.Calendar.MONTH, 0);
		range.setStartField(java.util.Calendar.DAY_OF_YEAR, 2);
		
		assertEquals(1, MainCalendarController.getWeekOfYear(range.getStart()));
	}

}
