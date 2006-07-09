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

import junit.framework.TestCase;

/**
 * @author waffel
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public final class JSCFExceptionTest extends TestCase {

	/**
	 * Tests if the exception works as aspected.
	 */
	public void testJSCFException() {
		String refString = "testString";
		String retString = null;
		try {
			throw new JSCFException(refString);
		} catch (JSCFException e) {
			retString = e.getMessage();
		}
		assertEquals(refString, retString);

	}

}
