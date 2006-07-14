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
package org.waffel.jscf.gpg;

import java.util.Properties;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDefinitions;
import org.waffel.jscf.JSCFException;
import org.waffel.jscf.JSCFStatement;

/**
 * Don't use methods from this class. Use the JSCFramwork (
 * {@link org.waffel.jscf.JSCFConnection})..
 * 
 * @author waffel (Thomas Wabner)
 * 
 */
public final class GPGConnection implements JSCFConnection {

	/**
	 * Driver that created me.
	 */
	private NonRegisteringGPGDriver myDriver;

	/**
	 * Properties for this connection.
	 */
	private Properties props;

	/**
	 * Constructor for the gpg connection with the given properties and the gpg
	 * driver.
	 * 
	 * @param info
	 *            The properties to be used for this gpg connection.
	 * @param driver
	 *            The gpg driver used for this connection.
	 */
	public GPGConnection(final Properties info,
			final NonRegisteringGPGDriver driver) {
		this.props = info;
		// TODO: adding defaults for windows, unix, linux ect.
		this.myDriver = driver;
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFStatement createStatement() throws JSCFException {
		return new GPGStatement(this);
	}

	/**
	 * @see org.waffel.jscf.JSCFConnection#close()
	 */
	public void close() {
		// we don't need to close a connection. Setting only all variables to
		// null
		this.myDriver = null;
		this.props = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProperties(final Properties info) {
		// do not use putAll. We should override the complete hashtable
		this.props = info;
	}

	/**
	 * {@inheritDoc}
	 */
	public Properties getProperties() {
		return this.props;
	}

	// --------------------- support Routines for GPG -------------------------
	/**
	 * Returns the id, stored for this connection. The id is the user name used
	 * for gpg to access the keys. This can be a normal username or in many
	 * cases a complete email adress.
	 * 
	 * @return Returns the id, stored for this connection.
	 */
	public String getId() {
		return this.props.getProperty(JSCFDefinitions.USERID);
	}

	/**
	 * Returns the myDriver, stored for this connection.
	 * 
	 * @return Returns the myDriver, stored for this connection.
	 */
	public NonRegisteringGPGDriver getMyDriver() {
		return myDriver;
	}

	/**
	 * Returns the password, stored for this connection.
	 * 
	 * @return Returns the password, stored for this connection.
	 */
	public String getPassword() {
		return this.props.getProperty(JSCFDefinitions.PASSWORD);
	}

	/**
	 * Returns the path, stored for this connection. The path is used to call
	 * the gpg commandline tool. Under linux it lives often under /usr/bin and
	 * path is then /usr/bin/gpg
	 * 
	 * @return Returns the path, stored for this connection.
	 */
	public String getPath() {
		return this.props.getProperty(GPGDefinitions.PATH, "/usr/bin/gpg");
	}
}
