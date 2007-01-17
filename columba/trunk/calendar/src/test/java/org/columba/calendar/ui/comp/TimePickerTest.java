package org.columba.calendar.ui.comp;

import junit.framework.TestCase;

public class TimePickerTest extends TestCase {

	public void testValidateInput() {
		// valid inputs
		String value = "11:20";
		assertEquals(value, TimePicker.validateInput(value));
		value = "23:59";
		assertEquals(value, TimePicker.validateInput(value));
		value = "00:00";
		assertEquals(value, TimePicker.validateInput(value));
		value = "10:09";
		assertEquals(value, TimePicker.validateInput(value));
		value = "20:50";
		assertEquals(value, TimePicker.validateInput(value));
		
		// recoverable inputs
		value = "00:000";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "23:000";
		assertEquals("23:00", TimePicker.validateInput(value));
		value = "2:000";
		assertEquals("02:00", TimePicker.validateInput(value));
		value = "000:0";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "2";
		assertEquals("02:00", TimePicker.validateInput(value));
		value = "11";
		assertEquals("11:00", TimePicker.validateInput(value));
		value = "23";
		assertEquals("23:00", TimePicker.validateInput(value));
		value = "0:1";
		assertEquals("00:01", TimePicker.validateInput(value));
		value = "0:9";
		assertEquals("00:09", TimePicker.validateInput(value));
		value = "14:9";
		assertEquals("14:09", TimePicker.validateInput(value));
		
		// unrecoverable inputs
		value = "235:9";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "asdasdl_jk";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "123";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "-3:2";
		assertEquals("00:00", TimePicker.validateInput(value));
		value = "-3";
		assertEquals("00:00", TimePicker.validateInput(value));
	}

}
