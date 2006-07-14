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
 * Created on 13.01.2004
 *
 */
package org.waffel.jscf.examples;

import java.io.InputStream;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFException;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;

/**
 * SimpleJSCFExample class
 * @author waffel (Thomas Wabner)
 */
public class SimpleJSCFExample {

	/**
	 * signExample1 method
	 * @author waffel
	 * @param message
	 * @param userID
	 * @param password
	 * @return res 
	 */
	public JSCFResultSet signExample1(final InputStream message,
			final String userID, final String password) {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		try {
			// Initialise the concrete implementation of the driver that should
			// be used here, i.e. the gpg-driver.
			Class.forName("org.waffel.jscf.gpg.GPGDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// Creates a new connection over the JSCFDriverManager with the
			// given connection url. In this example, we create a connection
			// to the gpg-tool without extra parameters. The path to the
			// gpg-tool is set as the third parameter.
			pgpCon = JSCFDriverManager.getConnection("pgp:gpg:/usr/bin/gpg");
			// creating a new Statement over the connection
			JSCFStatement stmt = pgpCon.createStatement();
			// executes the sign Statement with the given message, userID and
			// password. This method call returns the result as an
			// InputStream which can be read or given back
			res = stmt.executeSign(message, userID, password);
			// catching PGPExceptions, if the creating of connection failed, or
			// the statement cannot be executed, or the execution process
			// fails
		} catch (JSCFException e) {
			e.printStackTrace();
		} finally {
			// in every case, after processing, we release the connection to the
			// gpg-tool
			pgpCon.close();
		}
		// returning the result
		return res;
	}

	/**
	 * signExample2 method
	 * @author waffel
	 * @param message
	 * @param password
	 * @return res
	 */
	public JSCFResultSet signExample2(final InputStream message,
			final String password) {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		try {
			Class.forName("org.waffel.jscf.gpg.GPGDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// Creates a new Connection with the given url. This url has
			// currently the userID as parameter. If we want use the connection
			// again, we shouldnt create a new connection or set the userID
			// again.
			// The userID for this connection is stored in the
			// connection object. The same can be done with the password. Thus,
			// an application can create one JSCFConnection with userID and
			// password and then calling any statements without extra
			// parameters.
			pgpCon = JSCFDriverManager
					.getConnection("pgp:gpg:/usr/bin/gpg:twabner@imn.htwk.leipzig.de");
			JSCFStatement stmt = pgpCon.createStatement();
			res = stmt.executeSign(message, password);
		} catch (JSCFException e) {
			e.printStackTrace();
		} finally {
			pgpCon.close();
		}
		return res;
	}
}