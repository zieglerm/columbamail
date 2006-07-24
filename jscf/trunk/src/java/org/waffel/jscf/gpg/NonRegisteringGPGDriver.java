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
import java.util.StringTokenizer;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDefinitions;
import org.waffel.jscf.JSCFDriver;
import org.waffel.jscf.JSCFException;

/**
 * The implementation for GPGDriver.
 * 
 * @author waffel (Thomas Wabner)
 * 
 */
public class NonRegisteringGPGDriver implements JSCFDriver {

	/**
	 * Required for Class.forName().newInstance().
	 * 
	 * @throws JSCFException
	 *             Doe's nothing.
	 */
	public NonRegisteringGPGDriver() throws JSCFException {
		// required for Class.forName().newInstance()
	}

	/**
	 * Here we provide some hints on how the gpg driver works with connection
	 * urls, and how the url as a string are parsed and accepted.
	 * <p>
	 * The url is structured in some components which are separated by a default
	 * and custom seperator. There are some required components which cannot be
	 * left empty. These are the
	 * <ul>
	 * <li>protocol</li>
	 * <li>subprotocol</li>
	 * </ul>
	 * The protocol must have the name <code>jscf</code>. The subprotocol to
	 * use this gpg driver implementation must have the name <code>gpg</code>.
	 * <p>
	 * The required components are separated with the standard seperator "<code>:</code>".
	 * There are also some parameters accepted to etablish a gpg connection to
	 * the gpg program. While the standard separator "<code>:</code>" is not
	 * useful in a windows environmment, this separator is, for example, used to
	 * specify the path to the gpg program like
	 * <code>C:/program/gpg/gpg.exe</code>. We have added one extra parameter -
	 * this parameter is called <code>seperator</code> and must given after
	 * the required components. Then this separator is used to parse the next
	 * optional parameters.
	 * <p>
	 * The following optional parameters are allowed:
	 * <ul>
	 * <li>seperator</li>
	 * <li>path to gpg program</li>
	 * <li>gpg username</li>
	 * <li>gpg user password</li>
	 * </ul>
	 * A valid url with all required parameters are for example:
	 * <p>
	 * <code>jscf:gpg:$C:/programs/gpg/gpg.exe$username$password</code>.
	 * <p>
	 * It is also allowed to specify the url like
	 * <p>
	 * <code>jscf:gpg::/usr/bin/gpg:username</code>
	 * <p>
	 * The password can then set via a property. Following is forbidden and
	 * won't work:
	 * <p>
	 * <code>jscf:gpg:C:/programme/gpg/gpg.exe</code>
	 * <p>
	 * The separator is the the "<code>C</code>" character and the resulting
	 * path is wrong.
	 * <p>
	 * In short: If you would give optional parameters, then you must provide as
	 * the first parameter a separator to be used to seperate all next
	 * parameters.
	 * 
	 * @see org.waffel.jscf.JSCFDriver#connect(java.lang.String,
	 *      java.util.Properties)
	 */
	public JSCFConnection connect(final String url, final Properties info)
			throws JSCFException {
		Properties props = null;
		if ((props = parseURL(url, info)) == null) {
			return null;
		}
		GPGConnection connection = new GPGConnection(props, this);
		return connection;
	}

	private Properties parseURL(final String url, final Properties info) {
		Properties urlProps = new Properties();
		int posCounter = 0;
		if (info != null) {
			urlProps.putAll(info);
		}
		if (url == null) {
			return null;
		}
		StringTokenizer strToken = new StringTokenizer(url, ":", true);

		// look for pgp
		if (!lookForStringInStrToken(strToken, "jscf")) {
			return null;
		}
		posCounter = posCounter + 4;
		// look for colon
		if (!lookForStringInStrToken(strToken, ":")) {
			return null;
		}
		posCounter++;
		// look for subprotocol. should be gpg
		if (!lookForStringInStrToken(strToken, "gpg")) {
			return null;
		}
		posCounter = posCounter + 3;
		// look for colon
		if (!lookForStringInStrToken(strToken, ":")) {
			return null;
		}
		posCounter++;

		// the next is for extra parameters like path, id, password
		if (strToken.hasMoreTokens()) {
			String addonToken = url.substring(posCounter, url.length());
			// get the separator which should be the next character in this
			// token.
			// For example jscf:gpg:$C:/programs/gpg/gpg.exe$user$pass
			// should then can be parsed.
			String seperator = addonToken.substring(0, 1);
			// remove the separator sign from the addonToken
			addonToken = addonToken.substring(1, addonToken.length());
			StringTokenizer addonTokenizer = new StringTokenizer(addonToken,
					seperator, true);
			while (addonTokenizer.hasMoreTokens()) {
				String addonNextToken = addonTokenizer.nextToken();
				// next must be path to gpg
				urlProps.put(GPGDefinitions.PATH, addonNextToken);

				// check for id
				if (addonTokenizer.hasMoreTokens()) {
					addonNextToken = addonTokenizer.nextToken();
					if (addonNextToken.equals(seperator)) {
						if (addonTokenizer.hasMoreTokens()) {
							addonNextToken = addonTokenizer.nextToken();
							// next must be the id
							urlProps
									.put(JSCFDefinitions.USERID, addonNextToken);
						}
					}
				} else {
					return urlProps;
				}
				// check for password
				if (addonTokenizer.hasMoreTokens()) {
					addonNextToken = addonTokenizer.nextToken();
					if (addonNextToken.equals(seperator)) {
						if (addonTokenizer.hasMoreTokens()) {
							addonNextToken = addonTokenizer.nextToken();
							urlProps.put(JSCFDefinitions.PASSWORD,
									addonNextToken);
						}
					}
				} else {
					return urlProps;
				}
			}
		}
		return urlProps;
	}

	private boolean lookForStringInStrToken(final StringTokenizer strToken,
			final String lookStr) {
		if (strToken.hasMoreTokens()) {
			String ifStr = strToken.nextToken().toLowerCase();
			if (ifStr != null) {
				if (!lookStr.equals(ifStr)) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
