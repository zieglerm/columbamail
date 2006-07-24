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
 * Examples which showing some variants how to use the JSCF framework to sign an
 * input stream.
 * 
 * @author waffel (Thomas Wabner)
 */
public class JSCFSignExample {

	/**
	 * This example shows, how to:
	 * <ul>
	 * <li>Register a JSCF driver,</li>
	 * <li>creating a connection to the driver,</li>
	 * <li>creating a statement,</li>
	 * <li>running the sign process,</li>
	 * <li>and using the result.</li>
	 * </ul>
	 * 
	 * @param message
	 *            The message stream which should be signed.
	 * @param userID
	 *            The user id wich should be used to sign the message stream.
	 * @param password
	 *            The password to be used for the given user id. *
	 * @author waffel
	 */
	public void signExample1(final InputStream message, final String userID,
			final String password) {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		try {
			// Initialize the concrete implementation of the driver that should
			// be used here, i.e. the gpg-driver.
			Class.forName("org.waffel.jscf.gpg.GPGDriver"); //$NON-NLS-1$
		} catch (ClassNotFoundException classNotFoundExcecption) {
			// log or print the stack trace
			classNotFoundExcecption.printStackTrace();
		}
		try {
			// Creates a new connection over the JSCFDriverManager with the
			// given connection url. In this example, we create a connection
			// to the gpg-tool without extra parameters. The path to the
			// gpg-tool is set as the third parameter.
			pgpCon = JSCFDriverManager.getConnection("pgp:gpg:/usr/bin/gpg"); //$NON-NLS-1$
			// create a new statement from the created connection. With this
			// statement we can execute something.
			JSCFStatement stmt = pgpCon.createStatement();
			// executes the sign Statement with the given message, userID and
			// password.
			res = stmt.executeSign(message, userID, password);
			// catching PGPExceptions, if the creating of connection failed, or
			// the statement cannot be executed, or the execution process
			// fails
		} catch (JSCFException jscfException) {
			// log or print the stack trace
			jscfException.printStackTrace();
		} finally {
			// in every case, after processing, we release the connection to the
			// gpg-tool
			if (pgpCon != null)
				pgpCon.close();
		}
	}

	/**
	 * This example shows, how to:
	 * <ul>
	 * <li>Register a JSCF driver,</li>
	 * <li>creating a connection to the driver,</li>
	 * <li>creating a statement,</li>
	 * <li>running the sign process,</li>
	 * <li>and using the result.</li>
	 * </ul>
	 * This example differs to example one, because the user name is hard coded
	 * in the connection string. You may use this, if you have a static variable
	 * with a user id or if you have the user id stored somewhere else and want
	 * to give is in the connection.
	 * 
	 * @param message
	 *            The message stream which should be signed.
	 * @param password
	 *            The password to be used for the given user id. *
	 * @author waffel
	 */
	public void signExample2(final InputStream message, final String password) {
		JSCFConnection pgpCon = null;
		JSCFResultSet res = null;
		try {
			// Initialize the concrete implementation of the driver that should
			// be used here, i.e. the gpg-driver.
			Class.forName("org.waffel.jscf.gpg.GPGDriver"); //$NON-NLS-1$
		} catch (ClassNotFoundException classNotFoundException) {
			// log or print the stack trace
			classNotFoundException.printStackTrace();
		}
		try {
			// Creates a new Connection with the given url. This url has
			// currently the userID as parameter. If we want use the connection
			// again, we should not create a new connection or set the userID
			// again.
			// The userID for this connection is stored in the
			// connection object. The same can be done with the password. Thus,
			// an application can create one JSCFConnection with userID and
			// password and then calling any statements without extra
			// parameters.
			pgpCon = JSCFDriverManager
					.getConnection("pgp:gpg:/usr/bin/gpg:twabner@imn.htwk.leipzig.de"); //$NON-NLS-1$
			// create a new statement from the created connection. With this
			// statement we can execute something.
			JSCFStatement stmt = pgpCon.createStatement();
			// executes the sign Statement with the given message and
			// password.
			res = stmt.executeSign(message, password);
		} catch (JSCFException jscfException) {
			// log or print the stack trace
			jscfException.printStackTrace();
		} finally {
			if (pgpCon != null)
				pgpCon.close();

		}
	}
}