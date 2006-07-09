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

import org.waffel.jscf.JSCFException;

/**
 * @author waffel
 * 
 */
public class ProgramNotFoundException extends JSCFException {

	/**
	 * Creates a new exception with the given reason.
	 * 
	 * @param reason
	 *            The reason of the exception.
	 */
	public ProgramNotFoundException(final String reason) {
		super(reason);
	}

}
