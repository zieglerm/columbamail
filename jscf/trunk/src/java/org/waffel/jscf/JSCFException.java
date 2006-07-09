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

/**
 * An exception that provides information on a JSCF access error or other
 * errors.
 * <p>
 * Each JSCFException provides a string describing the error. This is used as
 * the Java Exception message, available via the method getMesage
 * 
 * @author waffel (Thomas Wabner)
 * @version $Id: JSCFException.java,v 1.2 2006/02/06 17:32:32 waffel Exp $
 */
public class JSCFException extends Exception {

    /**
     * @{inheritDoc}
     */
    private static final long serialVersionUID = 7843083231659883061L;

    /**
     * Constructs an SQLException object with a reason.
     * 
     * @param reason
     *            a description of the exception
     */
    public JSCFException(final String reason) {
        super(reason);
    }

}
