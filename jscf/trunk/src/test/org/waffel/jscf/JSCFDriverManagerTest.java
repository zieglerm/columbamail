/*
 * The contents of this file are subject to the Mozilla Public License 
 * Version 1.1 (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
 * for the specific language governing rights and
 * limitations under the License.
 *
 * The Original Code is "Java Security Component Framework"
 *
 * The Initial Developer of the Original Code are Thomas Wabner, alias waffel.
 * Portions created by Thomas Wabner are Copyright (C) 2004. 
 * 
 * All Rights Reserved.
 * Created on 06.02.2004
 *
 */
package org.waffel.jscf;

import java.util.Properties;

import org.waffel.jscf.gpg.GPGDriver;

import junit.framework.TestCase;

/**
 * @author waffel
 * 
 */
public final class JSCFDriverManagerTest extends TestCase {

	/**
	 * Tests, if the included example GPGDriver can be registered with the
	 * JSCFDriverManager without exception. If the driver is then available
	 * would'nt be testet.
	 * 
	 * @throws JSCFException
	 *             If the exception are thrown, the test fails.
	 */
	public void testRegisterJSCFDriver() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
	}

	/**
	 * Tests, if the getConnection method with one String argument works.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionString() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		JSCFDriverManager.getConnection("jscf:gpg:/usr/bin/gpg");
	}

	/**
	 * Tests, if the getConnection method with one String and Properties
	 * argument works.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionStringProperties() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		Properties props = new Properties();
		props.put("test", "test");
		JSCFDriverManager.getConnection("jscf:gpg:", props);

	}

	/**
	 * Tests, if the getConnection method with url.-string, userid and password
	 * works.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionStringStringString() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		JSCFDriverManager.getConnection("jscf:gpg:", "testid", "test");
	}

	/**
	 * Tests, if initialize works.
	 */
	public void testInitialize() {
		JSCFDriverManager.initialize();
	}

	/**
	 * Test getConnection with wrong URL.
	 */
	public void testGetConnectionStringFails1() {
		try {
			JSCFDriverManager.registerJSCFDriver(new GPGDriver());
			JSCFDriverManager.getConnection("jscf:fail:/usr/bin/gpg");
		} catch (JSCFException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/**
	 * Test getConnection with null parameter.
	 */
	public void testGetConnectionStringFails2() {
		try {
			JSCFDriverManager.registerJSCFDriver(new GPGDriver());
			JSCFDriverManager.getConnection(null);
		} catch (JSCFException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/**
	 * Tests, if getConnection with null Properties works.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionStringPropertiesNull() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		JSCFConnection con = JSCFDriverManager.getConnection(
				"jscf:gpg::/usr/bin/gpg", null);
		assertEquals(true, (con != null));
		Properties props = con.getProperties();
		assertEquals("/usr/bin/gpg", (String) props.get("PATH"));
	}

	/**
	 * Tests, if getConnection with three String works correct, if the first
	 * string is null.
	 */
	public void testGetConnectionNullStringString() {
		try {
			JSCFDriverManager.registerJSCFDriver(new GPGDriver());
			JSCFDriverManager.getConnection(null, "testid", "test");
		} catch (JSCFException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/**
	 * Tests, if getConnection with three String works correct, if the second
	 * string is null.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionStringNullString() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		JSCFConnection con = JSCFDriverManager.getConnection("jscf:gpg:", null,
				"test");
		assertEquals(true, (con != null));
		Properties props = con.getProperties();
		assertEquals("test", (String) props.get("PASSWORD"));
	}

	/**
	 * Tests, if getConnection with three String works correct, if the third
	 * string is null.
	 * 
	 * @throws JSCFException
	 *             if the test fails
	 */
	public void testGetConnectionStringStringNull() throws JSCFException {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		JSCFConnection con = JSCFDriverManager.getConnection("jscf:gpg:",
				"testid", null);
		assertEquals(true, (con != null));
		Properties props = con.getProperties();
		assertEquals("testid", (String) props.get("USERID"));
	}

	/**
	 * Tests, if the register method works correect, if the given Driver is
	 * null.
	 */
	public void testRegisterDriverNull() {
		try {
			JSCFDriverManager.registerJSCFDriver(null);
		} catch (JSCFException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

}
