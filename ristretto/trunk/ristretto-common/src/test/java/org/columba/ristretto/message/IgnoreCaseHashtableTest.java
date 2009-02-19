package org.columba.ristretto.message;

import junit.framework.TestCase;

public class IgnoreCaseHashtableTest extends TestCase {

	public void test1() {
		IgnoreCaseHashtable table = new IgnoreCaseHashtable();
		table.put("my-Test", "value1");
		table.put("dummy", "value3");
		assertEquals("value1", table.get("my-test"));
		assertEquals("value3", table.get("dummy"));
		
		table.put("my-test", "value2");
		
		assertEquals("value2", table.get("my-Test"));
		assertEquals("value2", table.get("my-test"));
		assertEquals("value2", table.get("MY-TeST"));
		assertEquals("value3", table.get("duMMy"));

		assertTrue(table.containsKey("MY-TEST"));
		
		assertEquals("my-Test", table.keys().nextElement());
		
		System.out.println(table.toString());
	}
	
}
