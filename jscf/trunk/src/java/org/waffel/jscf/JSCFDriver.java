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
 * The interface that every JSCF driver must implement.
 * <p>
 * The Java Security Component Framework supports multiple jscf drivers.
 * <p>
 * Each driver must supply a class that implements the <code>JSCFDriver</code>
 * interface.
 * <p>
 * The <code>JSCFDriverManager</code> will try to load as many drivers as it
 * can find, and then for any given connection request, it will ask each driver
 * in turn to try to connect to the target URL.
 * <p>
 * It is strongly recommended that each <code>JSCFDriver</code> class should
 * be small and standalone so that the Driver class can be loaded and queried
 * without bringing in vast quantities of supporting code.
 * <p>
 * When a Driver class is loaded, it should create an instance of itself, and
 * register it with the <code>JSCFDriverManager</code>. This means that a
 * user can load and register a driver by calling
 * 
 * <pre>
 * <code>
 * Class.forName(&quot;org.waffel.GPGDriver&quot;)
 * </code>
 * </pre>
 * 
 * @author waffel (Thomas Wabner)
 * @version $Id: JSCFDriver.java,v 1.3 2006/02/06 17:32:32 waffel Exp $
 * @see org.waffel.jscf.JSCFDriverManager
 * @see org.waffel.jscf.JSCFConnection
 * 
 */
public interface JSCFDriver {

	/**
	 * Attempts to make a connection to the given URL. The driver should return
	 * "null" if it realizes it is the wrong kind of driver to connect to the
	 * given URL. This will be common, as when the JSCF driver manager is asked
	 * to connect to a given URL it passes the URL to each loaded driver in
	 * turn.
	 * <p>
	 * Making a connection means JSCF connects to a driver which can be an
	 * external program, or something else. Whilst this package is created after
	 * JDBC driver framework, we also use the same words to express other
	 * things. In JDBC, you can connect to a database, or file system, and in
	 * JSCF to a program or security device.
	 * <p>
	 * The driver should throw an JSCFException if it is the right driver to
	 * connect to the given URL but has trouble connecting to the whole jscf
	 * implementation (e.g. gpg is not available).
	 * <p>
	 * The java.util.Properties argument can be used to pass arbitrary string
	 * tag/value pairs as connection arguments. Normally at least "user" and
	 * "password" properties should be included in the Properties object.
	 * 
	 * @param url
	 *            a JSCF URL in from of
	 *            <code>jfc:<em>subprotocol</em>:<em>paramters</em></code>
	 * @param info
	 *            a list of arbitrary string tag/value pairs as connection
	 *            arguments; normally at least a "user" and "password" property
	 *            should be included
	 * @return a connection to the URL
	 * @throws JSCFException
	 *             if a JSCF access error occurs
	 */
	JSCFConnection connect(String url, Properties info) throws JSCFException;
}
