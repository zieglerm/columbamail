package org.columba.calendar.parser;

import junit.framework.TestCase;

public class XCSParserTest extends TestCase {

	public void testGetRValue() {
		String rule = "RRULE:FREQ=YEARLY;COUNT=5;INTERVAL=1";
		assertEquals("YEARLY", XCSDocumentParser.getRValue(rule, "FREQ"));
		assertEquals("5", XCSDocumentParser.getRValue(rule, "COUNT"));
		assertEquals("1", XCSDocumentParser.getRValue(rule, "INTERVAL"));
		
		rule = "RRULE:FREQ=YEARLY;INTERVAL=1";
		assertEquals("YEARLY", XCSDocumentParser.getRValue(rule, "FREQ"));
		assertEquals("", XCSDocumentParser.getRValue(rule, "COUNT"));
		assertEquals("1", XCSDocumentParser.getRValue(rule, "INTERVAL"));
		assertEquals("", XCSDocumentParser.getRValue(rule, "UNTIL"));

		rule = "RRULE:FREQ=MONTHLY;UNTIL=20070627T215959;INTERVAL=2;BYDAY=4WE";
		assertEquals("MONTHLY", XCSDocumentParser.getRValue(rule, "FREQ"));
		assertEquals("", XCSDocumentParser.getRValue(rule, "COUNT"));
		assertEquals("2", XCSDocumentParser.getRValue(rule, "INTERVAL"));
		assertEquals("20070627T215959", XCSDocumentParser.getRValue(rule, "UNTIL"));
		assertEquals("4WE", XCSDocumentParser.getRValue(rule, "BYDAY"));

	}

}
