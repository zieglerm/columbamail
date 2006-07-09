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
 * Created on 13.02.2004
 *
 */
package org.waffel.jscf.gpg;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

/**
 * @author waffel
 *  
 */
public class GPGResultSetTest extends TestCase {

    /**
     * Tests, if the getResultStream works as expected
     * 
     * @throws Exception
     *                      if the test fails
     */
    public void testSetGetResultStream() throws Exception {
        GPGResultSet res = new GPGResultSet();
        res.setResultStream(new ByteArrayInputStream("testmessage".getBytes()));

        assertEquals(true, (res.getResultStream() != null));
        assertEquals("testmessage", StreamUtils.readInString(
                res.getResultStream()).toString());
    }

    /**
     * Tests, if the getErrorStream works as expected
     * 
     * @throws Exception
     *                      if the test fails
     */
    public void testSetGetErrorStream() throws Exception {
        GPGResultSet res = new GPGResultSet();
        res.setErrorStream((new ByteArrayInputStream(new String("testmessage")
                .getBytes())));

        assertEquals(true, (res.getErrorStream() != null));
        assertEquals("testmessage", StreamUtils.readInString(
                res.getErrorStream()).toString());
    }

    /**
     * Tests, if the isError method works as expected
     */
    public void testIsError() {
        GPGResultSet res = new GPGResultSet();
        res.setReturnValue(-1);
        assertEquals(true, res.isError());

        res.setReturnValue(0);
        assertEquals(false, res.isError());
    }

    /**
     * Tests, if the getReturnValue method works as expected.
     */
    public void testSetGetReturnValue() {
        GPGResultSet res = new GPGResultSet();
        res.setReturnValue(0);

        assertEquals(0, res.getReturnValue());
    }

}
