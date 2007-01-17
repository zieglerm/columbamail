package org.columba.calendar.parser;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import org.columba.calendar.base.CalendarItem;
import org.columba.calendar.base.api.ICalendarItem;
import org.columba.calendar.model.api.IEventInfo;
import org.columba.calendar.model.api.IRecurrence;

import com.miginfocom.calendar.activity.recurrence.RecurrenceRule;

public class CalendarImporterTest extends TestCase {

	public void testImportCalendar() {
		File example1 = new File("calendar/src/test/resources/SunbirdEvents.ics");
		CalendarItem calendarItem = new CalendarItem("example", ICalendarItem.TYPE.LOCAL, "example", null);
		CalendarImporter importer = new CalendarImporter();
		Iterator<IEventInfo> i = null;
		try {
			i = importer.importCalendar(calendarItem, example1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got following exception:" + e.getMessage());
		}
		
		// check three entries
		for (;i.hasNext();) {
			IEventInfo entry = i.next();
			// Event: Anniversary
			if ("Anniversary".equals(entry.getEvent().getSummary())) {
				assertEquals("Anniversary", entry.getEvent().getCategories());
				assertEquals("Anniversary every year", entry.getEvent().getDescription());
				assertEquals(15, entry.getEvent().getDtStamp().get(java.util.Calendar.DAY_OF_MONTH));
				assertEquals(0, entry.getEvent().getDtStamp().get(java.util.Calendar.MONTH));
				assertEquals(2007, entry.getEvent().getDtStamp().get(java.util.Calendar.YEAR));
				// 204524
				assertEquals(21, entry.getEvent().getDtStamp().get(java.util.Calendar.HOUR_OF_DAY));
				assertEquals(45, entry.getEvent().getDtStamp().get(java.util.Calendar.MINUTE));
				assertEquals(24, entry.getEvent().getDtStamp().get(java.util.Calendar.SECOND));
				assertEquals("cb5f77cd-655d-45f9-9b78-04bb668abccc", entry.getEvent().getId());
				assertEquals(IRecurrence.RECURRENCE_ANNUALLY, entry.getEvent().getRecurrence().getType());
				assertEquals(IRecurrence.RECURRENCE_END_FOREVER, entry.getEvent().getRecurrence().getEndType());
				assertEquals("20070125", entry.getEvent().getDtStart().get(java.util.Calendar.YEAR) + "0" + (entry.getEvent().getDtStart().get(java.util.Calendar.MONTH)+1) + "" + entry.getEvent().getDtStart().get(java.util.Calendar.DAY_OF_MONTH) + "");
				assertEquals("20070126", entry.getEvent().getDtEnd().get(java.util.Calendar.YEAR) + "0" + (entry.getEvent().getDtEnd().get(java.util.Calendar.MONTH)+1) + "" + entry.getEvent().getDtEnd().get(java.util.Calendar.DAY_OF_MONTH) + "");
			// Event: All Day Event
			} else if ("AllDayEvent".equals(entry.getEvent().getSummary())) {
				assertEquals("Business", entry.getEvent().getCategories());
				assertEquals("I will the whole day do some work", entry.getEvent().getDescription());
				assertEquals("PUBLIC", entry.getEvent().getEventClass());
				assertEquals("5", entry.getEvent().getPriority());
				assertEquals(15, entry.getEvent().getDtStamp().get(java.util.Calendar.DAY_OF_MONTH));
				assertEquals(0, entry.getEvent().getDtStamp().get(java.util.Calendar.MONTH));
				assertEquals(2007, entry.getEvent().getDtStamp().get(java.util.Calendar.YEAR));
				assertEquals("CONFIRMED", entry.getEvent().getStatus());
				assertEquals(23, entry.getEvent().getDtStart().get(java.util.Calendar.DAY_OF_MONTH));
				assertEquals(0, entry.getEvent().getDtStart().get(java.util.Calendar.MONTH));
				assertEquals(2007, entry.getEvent().getDtStart().get(java.util.Calendar.YEAR));
				assertEquals(0, entry.getEvent().getDtStart().get(java.util.Calendar.HOUR_OF_DAY));
				assertEquals(0, entry.getEvent().getDtStart().get(java.util.Calendar.MINUTE));
				assertEquals(0, entry.getEvent().getDtStart().get(java.util.Calendar.SECOND));
				assertEquals(24, entry.getEvent().getDtEnd().get(java.util.Calendar.DAY_OF_MONTH));
				assertEquals(0, entry.getEvent().getDtEnd().get(java.util.Calendar.MONTH));
				assertEquals(2007, entry.getEvent().getDtEnd().get(java.util.Calendar.YEAR));
				assertEquals(0, entry.getEvent().getDtEnd().get(java.util.Calendar.HOUR_OF_DAY));
				assertEquals(0, entry.getEvent().getDtEnd().get(java.util.Calendar.MINUTE));
				assertEquals(0, entry.getEvent().getDtEnd().get(java.util.Calendar.SECOND));
				assertEquals("at home", entry.getEvent().getLocation());
				assertEquals("8197de12-8a20-4dd0-a13a-4e932745914e", entry.getEvent().getId());
			// Event: Normal Event
			} else if ("NormalEvent".equals(entry.getEvent().getSummary())) {
				System.out.println("Normal Event");
			}
		}
	}

	public void testImportCalendarRecurrence() {
		File example1 = new File("calendar/src/test/resources/SunbirdRecurrendEvents.ics");
		CalendarItem calendarItem = new CalendarItem("example", ICalendarItem.TYPE.LOCAL, "example", null);
		CalendarImporter importer = new CalendarImporter();
		Iterator<IEventInfo> i = null;
		try {
			i = importer.importCalendar(calendarItem, example1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got following exception:" + e.getMessage());
		}
		
		// check three entries
		for (;i.hasNext();) {
			IEventInfo entry = i.next();

		}
	}
	
	public void testCorrectUid() {
		String uid = "";
		assertEquals(true, CalendarImporter.correctUid(uid).length() > 0);
		uid = "uuid:197601031$§%&§$%&&-_";
		assertEquals("uuid-197601031----------_", CalendarImporter.correctUid(uid));
	}
	
}
