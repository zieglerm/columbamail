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

import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFException;

/**
 * GPGDriver which self-registers to the JSCFDriverManager (
 * {@link org.waffel.jscf.JSCFDriverManager}).
 * 
 * @author waffel (Thomas Wabner)
 * @see org.waffel.jscf.JSCFDriver
 */
public class GPGDriver extends NonRegisteringGPGDriver {

	static {
		try {
			JSCFDriverManager.registerJSCFDriver(new GPGDriver());
		} catch (JSCFException e) {
			throw new RuntimeException("Can't register driver!");
		}
	}

	/**
	 * Only for Class.forName.newInstance.
	 * 
	 * @throws JSCFException
	 *             This is only for <em>class for name</em> stuff.
	 */
	public GPGDriver() throws JSCFException {
		// only for Class.forName.newInstance
	}
}
