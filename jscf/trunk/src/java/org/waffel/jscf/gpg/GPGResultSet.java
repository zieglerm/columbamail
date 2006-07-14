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

import java.io.InputStream;

import org.waffel.jscf.JSCFException;
import org.waffel.jscf.JSCFResultSet;

/**
 * Don't use methods from this class.
 * 
 * Use the JSCFramwork ({@link org.waffel.jscf.JSCFResultSet})
 * 
 * @author waffel (Thomas Wabner)
 * 
 */
public final class GPGResultSet implements JSCFResultSet {

	/**
	 * The result stream used for internal operations. Default is
	 * <code>null</code>.
	 */
	private InputStream resultStream = null;

	/**
	 * The error stream used for internal operations. Default is
	 * <code>null</code>.
	 */
	private InputStream errorStream = null;

	/**
	 * The return value used for internal operations.
	 * 
	 * Default is <code>-1</code>.
	 */
	private int returnValue = -1;

	/**
	 * {@inheritDoc}
	 */
	public InputStream getResultStream() throws JSCFException {
		return this.resultStream;
	}

	/**
	 * {@inheritDoc}
	 */
	public InputStream getErrorStream() throws JSCFException {
		return this.errorStream;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isError() {
		boolean ret = false;
		if (this.returnValue != 0) {
			ret = true;
		}
		return ret;
	}

	// ---------------------- support routines for gpg -------------------------

	/**
	 * @param theErrorStream
	 *            The errorStream to set.
	 */
	public void setErrorStream(final InputStream theErrorStream) {
		this.errorStream = theErrorStream;
	}

	/**
	 * @param theResultStream
	 *            The resultStream to set.
	 */
	public void setResultStream(final InputStream theResultStream) {
		this.resultStream = theResultStream;
	}

	/**
	 * @return Returns the returnValue.
	 */
	public int getReturnValue() {
		return returnValue;
	}

	/**
	 * @param theReturnValue
	 *            The returnValue to set.
	 */
	public void setReturnValue(final int theReturnValue) {
		this.returnValue = theReturnValue;
	}
}
