/*
 * The contents of this file are subject to the Mozilla Public License Version 1.1 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 * 
 * The Original Code is "Java Security Component Framework"
 * 
 * The Initial Developer of the Original Code are Thomas Wabner, alias waffel. Portions created by Thomas Wabner are
 * Copyright (C) 2004.
 * 
 * All Rights reserved. Created on 28.01.2004
 *  
 */
package org.waffel.jscf.gpg;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import junit.framework.TestCase;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;

/**
 * Test class to check if the gpg statements with the gpg command line process
 * are working.
 * 
 * @author waffel
 * 
 */
public final class GPGStatementTest extends TestCase {

	/**
	 * Register the gpg driver to the JSCF driver manager.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		JSCFDriverManager.registerJSCFDriver(new GPGDriver());
	}

	/**
	 * Tests, if ExecuteSignInputStreamStringString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testExecuteSignInputStreamStringString() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeSign(message, "testid", "test");
		assertEquals(true, (res.getResultStream() != null));
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
	}

	/**
	 * Tests, if xecuteSignInputStreamString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testExecuteSignInputStreamString() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeSign(message, "test");
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertEquals(true, (res.getResultStream() != null));
	}

	/**
	 * Tests, if ExecuteSignInputStream works as expected.
	 * 
	 * @throws Exception
	 */
	public void testExecuteSignInputStream() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg",
				"testid", "test");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeSign(message);
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertEquals(true, (res.getResultStream() != null));
	}

	/**
	 * Tests, if ExecuteVerifyStreamStreamString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testExecuteVerifyStreamStreamString() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg",
				"testid", "test");
		JSCFStatement stmt = pgpCon.createStatement();
		// dont forget that the testmessage Stream is after one use empty!
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeSign(message);

		// dont forget that the testmessage Stream is after one use empty!
		message = new ByteArrayInputStream("testmessage".getBytes());
		res = stmt.executeVerify(message, res.getResultStream(), "sha1");
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
	}

	/**
	 * Tests, if ExecuteVerifyStreamStream works as expected.
	 * 
	 * @throws Exception
	 */
	public void testExecuteVerifyStreamStream() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		// dont forget that the testmessage Stream is after one use empty!
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeSign(message);

		// dont forget that the testmessage Stream is after one use empty!
		message = new ByteArrayInputStream("testmessage".getBytes());
		res = stmt.executeVerify(message, res.getResultStream());
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
	}

	/**
	 * Test if the verify process runs without an id and password and
	 * digestAlgorithm in the connection
	 * 
	 * @throws Exception
	 */
	public void testExecuteVerifyStreamStreamFailure1() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		// dont forget that the testmessage Stream is after one use empty!
		ByteArrayInputStream message = new ByteArrayInputStream(new String(
				"testmessage").getBytes());
		res = stmt.executeSign(message);

		// dont forget that the testmessage Stream is after one use empty!
		message = new ByteArrayInputStream("testmessage".getBytes());
		pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/usr/bin/gpg");
		stmt = pgpCon.createStatement();
		res = stmt.executeVerify(message, res.getResultStream());
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
	}

	/**
	 * Tests, if EncryptStreamString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testEncryptStreamString() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeEncrypt(message, "testid");

		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertEquals(true, (res.getResultStream() != null));
	}

	/**
	 * Tests, if EncryptStream works as expected.
	 * 
	 * @throws Exception
	 */
	public void testEncryptStream() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		Properties props = new Properties();
		props.put("RECIPIENTS", "testid");
		pgpCon = JSCFDriverManager.getConnection(
				"jscf:gpg::/usr/bin/gpg:testid:test", props);
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeEncrypt(message);

		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertEquals(true, (res.getResultStream() != null));
	}

	/**
	 * Tests, if encryption also works without a passphrase
	 * 
	 * @throws Exception
	 */
	public void testEncryptStream2() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		Properties props = new Properties();
		props.put("RECIPIENTS", "testid");
		pgpCon = JSCFDriverManager.getConnection(
				"jscf:gpg::/usr/bin/gpg:testid", props);
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeEncrypt(message);

		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertEquals(true, (res.getResultStream() != null));
	}

	/**
	 * Tests, if DecryptStreamString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testDecryptStreamString() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeEncrypt(message, "testid");

		JSCFResultSet resDecrypt = stmt.executeDecrypt(res.getResultStream(),
				"test");

		assertEquals(false, resDecrypt.isError());
		assertEquals(0, ((GPGResultSet) resDecrypt).getReturnValue());
		assertEquals(true, (resDecrypt.getResultStream() != null));
		assertEquals("testmessage", StreamUtils.readInString(
				resDecrypt.getResultStream()).toString());
	}

	/**
	 * Tests, if DecryptStream works as expected.
	 * 
	 * @throws Exception
	 */
	public void testDecryptStream() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		JSCFResultSet resDecrypt = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		ByteArrayInputStream message = new ByteArrayInputStream("testmessage"
				.getBytes());
		res = stmt.executeEncrypt(message, "testid");

		resDecrypt = stmt.executeDecrypt(res.getResultStream());

		assertEquals(false, resDecrypt.isError());
		assertEquals(0, ((GPGResultSet) resDecrypt).getReturnValue());
		assertEquals(true, (resDecrypt.getResultStream() != null));
		String resString = StreamUtils.readInString(
				resDecrypt.getResultStream()).toString();
		System.out.println(resString);
		assertEquals("testmessage", resString);
	}

	/**
	 * Tests, if CheckPassphraeString works as expected.
	 * 
	 * @throws Exception
	 */
	public void testCheckPassphraeString() throws Exception {
		JSCFConnection pgpCon = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid");
		JSCFStatement stmt = pgpCon.createStatement();

		assertEquals(true, stmt.checkPassphrase("test"));
		assertEquals(false, stmt.checkPassphrase("wrong"));
	}

	/**
	 * Tests, if CheckPassphrase works as expected.
	 * 
	 * @throws Exception
	 */
	public void testCheckPassphrase() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();

		assertEquals(true, stmt.checkPassphrase());
	}
	
	/**
	 * Test, if the {@link JSCFStatement#getKeys(Collection)} method works as
	 * expected with one name.
	 * 
	 * @throws Exception
	 */
	public void testGetKeysOne() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		Collection<String> names = new LinkedList<String>();
		names.add("test"); //$NON-NLS-1$
		res = stmt.getKeys(names);

		assertNotNull(res);
		String resString = StreamUtils.readInString(
				res.getErrorStream()).toString();
		System.out.println(resString);
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertNotNull(res.getResultStream());
	}

	/**
	 * Test, if the {@link JSCFStatement#getKeys(Collection)} method works as
	 * expected with two names.
	 * 
	 * @throws Exception
	 */
	public void testGetKeysTwo() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		Collection<String> names = new LinkedList<String>();
		names.add("test"); //$NON-NLS-1$
		names.add("waffel"); //$NON-NLS-1$
		res = stmt.getKeys(names);

		assertNotNull(res);
		String resString = StreamUtils.readInString(
				res.getErrorStream()).toString();
		System.out.println(resString);
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertNotNull(res.getResultStream());
	}

	/**
	 * Test, if the {@link JSCFStatement#getKeys(Collection)} method returns all
	 * stored keys without failure if null is given as paramter.
	 * 
	 * @throws Exception
	 */
	public void testGetKeysNull() throws Exception {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		pgpCon = JSCFDriverManager
				.getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
		JSCFStatement stmt = pgpCon.createStatement();
		res = stmt.getKeys(null);

		assertNotNull(res);
		assertEquals(false, res.isError());
		assertEquals(0, ((GPGResultSet) res).getReturnValue());
		assertNotNull(res.getResultStream());
	}

}