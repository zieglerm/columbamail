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
 * Created on 21.01.2004
 *
 */
package org.waffel.jscf;

import java.util.Properties;

/**
 * A connection (session) with a specific JSCF implementation. A JSCF
 * implementation can be a commandline wrapper to the popular GnuPG tool, or
 * other implementations of security components which supporting the functions
 * defined in <code>JSCFStatement</code>.
 * 
 * @author waffel (Thomas Wabner)
 * @version $Id: JSCFConnection.java,v 1.3 2006/02/06 17:32:32 waffel Exp $
 * @see org.waffel.jscf.JSCFDriverManager#getConnection(String, Properties)
 * @see org.waffel.jscf.JSCFStatement
 * @see org.waffel.jscf.JSCFResultSet
 */
public interface JSCFConnection {

	/**
	 * Creates a statement object for sending commmands to the JSCF
	 * implementation (for example GnuPG).
	 * 
	 * @return a new default <code>JSCFStatement</code>
	 * @throws JSCFException
	 *             if a JSCF access error occurs
	 */
	JSCFStatement createStatement() throws JSCFException;

	/**
	 * Sets the given properties for this <code>JSCFConnection</code>.
	 * <p>
	 * Normally at least "user" and "password" properties should be included in
	 * the Properties object.
	 * 
	 * @param info
	 *            a list of abitary string tag/value pairs. Normally at least
	 *            "user" and "password" properties should be included in the
	 *            Properties object.
	 */
	void setProperties(Properties info);

	/**
	 * Gives all properties of this <code>JSCFConnection</code> as a list of
	 * arbitrary string tag/value pairs back. Normally, at least the "user" and
	 * "password" properties should be included in the property object.
	 * 
	 * @return a list of arbitrary string tag/value pairs. Normally, at least
	 *         the "user" and "password" properties should be included in the
	 *         property object.
	 */
	Properties getProperties();

	/**
	 * All stored parameters in the connection are set to null. If the
	 * connection is closed, no new Statement can be created and the setting of
	 * properties should have no effect.
	 */
	void close();
}
