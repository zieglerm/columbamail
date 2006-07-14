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

import java.security.AccessController;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import sun.security.action.GetPropertyAction;

/**
 * This is the basic service to manage a set of JSCF drivers. JSCF Drivers
 * provides functions to sign, verify, encrypt and decrypt Streams.
 * <p>
 * When <b>JSCFDriverManager</b> starts the initialisation, it attempts to load
 * the driver classes referenced in the "jscf.drivers" system property. A user
 * can customize his JSCF Drivers in his application using the property. For
 * example in your ~/.hotjava/properties file you can specify following:
 * 
 * <pre>
 * <code>
 *          jscf.drivers=org.waffel.jscf.gpgDriver:org.kapott.jscfDriver:foo.yourDriver
 * </code>
 * </pre>
 * 
 * A program can also load a JSCF Driver at any time. For example the GnuPG
 * commandline JSCF Driver is loaded with following statement:
 * 
 * <pre>
 * <code>
 * Class.forName(&quot;org.waffel.jscf.gpgDriver&quot;)
 * </code>
 * </pre>
 * 
 * When the method <code>getConnection</code> is called, the
 * <b>JSCFDriverManager</b> will attempt to locate a suitable driver from
 * amongst those loaded at initialization and those loaded explicitly using the
 * same classloader as the current applet or application. The same classloader
 * is used with <code>JSCFDriverManager.class.getClassloader</code>. The
 * implementation to use the classloader don't need a native routine as the JDBC
 * DriverManager does.
 * 
 * @see org.waffel.jscf.JSCFDriver
 * @see org.waffel.jscf.JSCFConnection
 * @author waffel (Thomas Wabner)
 * @version $Id: JSCFDriverManager.java,v 1.4 2006/02/06 17:32:32 waffel Exp $
 */
public final class JSCFDriverManager {

	/**
	 * Registers the given driver with the <code>JSCFDriverManager</code>. A
	 * newly loaded driver should call the method
	 * <code>registerJSCFDriver</code> to make itself known to
	 * <code>JSCFDriverManager</code>
	 * 
	 * @param driver
	 *            a new JSCF Driver, that is to be registered to the
	 *            <code>JSCFDriverManager</code>
	 * @throws JSCFException
	 *             if as JSCF access error occurs
	 */
	public static synchronized void registerJSCFDriver(final JSCFDriver driver)
			throws JSCFException {
		if (!initialized) {
			initialize();
		}
		if (driver == null) {
			throw new JSCFException("driver cannot be null");
		}
		JSCFDriverInfo jscfDI = new JSCFDriverInfo();
		jscfDI.jscfDriver = driver;
		jscfDI.jscfDriverClass = jscfDI.jscfDriver.getClass();
		jscfDI.jscfDriverClassName = jscfDI.jscfDriverClass.getName();

		jscfDrivers.add(jscfDI);
	}

	/**
	 * Attempts to create a new connection to the given JSCF URL. The
	 * <code>JSCFDriverManager</code> attempts to select an appropriate
	 * <code>JSCFDriver</code> from the set of registered drivers. Each
	 * registered driver is asked if it understands the given url. The first
	 * driver which understands the URL is used to return the connection.
	 * 
	 * @param url
	 *            a JSCF URL in from of
	 *            <code>jfc:<em>subprotocol</em>:<em>paramters</em></code>
	 * @return a connection to the URL
	 * @throws JSCFException
	 *             if a JSCF access error occurs
	 */
	public static synchronized JSCFConnection getConnection(final String url)
			throws JSCFException {
		Properties info = new Properties();
		// TODO this won't work. Test it without eclipse
		// ClassLoader callerCL = JSCFDriverManager.getCallerClassLoader();
		ClassLoader callerCL = JSCFDriverManager.class.getClassLoader();
		return getConnection(url, info, callerCL);
	}

	/**
	 * Attempts to create a new connection to the given JSCF URL.
	 * <code>JSCFDriverManager</code> attempts to select an appropriate
	 * <code>JSCFDriver</code> from the set of registered drivers. Each
	 * registered driver is asked if it understands the given url. The first
	 * driver which understands the URL is used to return the connection.
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
	public static synchronized JSCFConnection getConnection(final String url,
			final Properties info) throws JSCFException {
		// TODO this won't work. Test it without eclipse
		// ClassLoader callerCL = JSCFDriverManager.getCallerClassLoader();
		ClassLoader callerCL = JSCFDriverManager.class.getClassLoader();
		return getConnection(url, info, callerCL);
	}

	/**
	 * Attempts to create a new connection to the given JSCF URL. The
	 * <code>JSCFDriverManager</code> attempts to select an appropriate
	 * <code>JSCFDriver</code> from the set of registered drivers. Each
	 * registered driver is asked if it understands the given url. The first
	 * driver which understands the URL is used to return the connection.
	 * 
	 * @param url
	 *            a JSCF URL in from of
	 *            <code>jfc:<em>subprotocol</em>:<em>paramters</em></code>
	 * @param user
	 *            the user which is used for JSCF operations
	 * @param password
	 *            the user's password
	 * @return a connection to the URL
	 * @throws JSCFException
	 *             if a JSCF access error occurs
	 */
	public static synchronized JSCFConnection getConnection(final String url,
			final String user, final String password) throws JSCFException {
		Properties info = new Properties();
		if (user != null) {
			info.put(JSCFDefinitions.USERID, user);
		}
		if (password != null) {
			info.put(JSCFDefinitions.PASSWORD, password);
		}
		// TODO this won't work. Test it without eclipse
		// ClassLoader callerCL = JSCFDriverManager.getCallerClassLoader();
		ClassLoader callerCL = JSCFDriverManager.class.getClassLoader();
		return getConnection(url, info, callerCL);
	}

	// ------------------------------------------------------------------------

	private static Class getCallerClass(final ClassLoader callerCL,
			final String className) {
		Class callerClass = null;
		try {
			callerClass = Class.forName(className, true, callerCL);
		} catch (Exception e) {
			callerClass = null;
		}
		return callerClass;
	}

	private static synchronized JSCFConnection getConnection(final String url,
			final Properties info, final ClassLoader callerCL)
			throws JSCFException {
		if (url == null) {
			throw new JSCFException("The url cannot be null!");
		}
		if (!initialized) {
			initialize();
		}

		JSCFException reason = null;

		for (JSCFDriverInfo jscfDI : jscfDrivers) {
			if (getCallerClass(callerCL, jscfDI.jscfDriverClassName) != jscfDI.jscfDriverClass) {
				continue;
			}
			try {
				JSCFConnection result = jscfDI.jscfDriver.connect(url, info);
				if (result != null) {
					return result;
				}
			} catch (JSCFException exception) {
				if (reason == null) {
					reason = exception;
				}
			}
		}

		if (reason != null) {
			throw reason;
		}
		throw new JSCFException("No suitable JSCFDriver available!");
	}

	private static List<JSCFDriverInfo> jscfDrivers = new Vector<JSCFDriverInfo>();

	private static boolean initialized = false;

	private JSCFDriverManager() {
		// private constructor ... don't use it
	}

	// private static native ClassLoader getCallerClassLoader();

	// initialises the Class and loads all initial Drivers
	static void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;
		loadInitialJSCFDrivers();
	}

	private static void loadInitialJSCFDrivers() {
		String drivers;
		try {
			drivers = (String) AccessController
					.doPrivileged(new GetPropertyAction("jscf.drivers"));
		} catch (Exception e) {
			// abort loading
			return;
		}
		if (drivers == null) {
			return;
		}
		StringTokenizer strToken = new StringTokenizer(drivers, ":");
		while (strToken.hasMoreTokens()) {
			try {
				Class.forName(strToken.nextToken(), true, ClassLoader
						.getSystemClassLoader());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
}

// helperclass fro this package

class JSCFDriverInfo {

	/**
	 * <code>jscfDriver</code> stored in this object.
	 */
	public JSCFDriver jscfDriver;

	/**
	 * <code>jscfDriverClass</code> stored in this object.
	 */
	public Class jscfDriverClass;

	/**
	 * <code>jscfDriverClassName</code> stored in this object.
	 */
	public String jscfDriverClassName;

	/**
	 * Returns a string in form of
	 * <em>jscfDriver[className=my.package.mydriver]</em>.
	 * 
	 * @return A string in form of
	 *         <em>jscfDriver[className=my.package.mydriver]</em>.
	 */
	@Override
	public String toString() {
		return ("jscfDriver[className=" + this.jscfDriverClassName + ","
				+ this.jscfDriver + "]");
	}
}
