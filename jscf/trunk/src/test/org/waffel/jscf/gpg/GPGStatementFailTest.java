/*The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the 
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
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
 * All Rights reserved.
 * Created on 10.03.2004
 *
 */
package org.waffel.jscf.gpg;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;

/**
 * This class should test for failings.
 * 
 * @author waffel
 *  
 */
public class GPGStatementFailTest extends TestCase {

    /**
     * Tests, if ProgramNotFound fails as expected.
     * 
     * @throws Exception
     */
    public void testProgramNotFound() throws Exception {
        try {
            JSCFConnection pgpCon = null;
            JSCFResultSet res = null;
            JSCFDriverManager.registerJSCFDriver(new GPGDriver());
            pgpCon = JSCFDriverManager.getConnection("jscf:gpg::/fail/gpg",
                    "testid", "test");
        } catch (ProgramNotFoundException e) {
            assertTrue(true);
        }
    }
    
}
