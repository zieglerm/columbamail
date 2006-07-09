/*
 * The contents of this file are subject to the Mozilla Public License Version 1.1 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 * 
 * The Original Code is "Java Security Component Framework"
 * 
 * The Initial Developer of the Original Code are Thomas Wabner, alias waffel. Portions created by Thomas Wabner are
 * Copyright (C) 2004.
 * 
 * All Rights reserved. Created on 10.02.2004
 *  
 */
package org.waffel.jscf.gpg;

import java.util.Properties;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;

import junit.framework.TestCase;

/**
 * @author waffel
 *  
 */
public class GPGDriverTest extends TestCase {

  /**
   * Tests, if the gpg-connection can etablished, if the driver is instantiated
   * via Class.forName
   * 
   * @throws Exception
   *           if the tests fails
   */
  public void testConnectString1() throws Exception {
    Class.forName("org.waffel.jscf.gpg.GPGDriver");
    JSCFConnection con = JSCFDriverManager
        .getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
    boolean instance = (con instanceof GPGConnection);
    assertEquals(true, instance);
    assertEquals("testid", ((GPGConnection) con).getId());
    assertEquals("test", ((GPGConnection) con).getPassword());
    Properties props = ((GPGConnection) con).getProperties();
    assertEquals("testid", props.get("USERID"));
    assertEquals("test", props.get("PASSWORD"));
  }

  /**
   * Tests, if the gpg-connection can etablished, if the driver is given direct
   * as new Class
   * 
   * @throws Exception
   *           if the tests fails
   */
  public void testConnectString2() throws Exception {
    JSCFDriverManager.registerJSCFDriver(new GPGDriver());
    JSCFConnection con = JSCFDriverManager
        .getConnection("jscf:gpg::/usr/bin/gpg:testid:test");
    boolean instance = (con instanceof GPGConnection);
    assertEquals(true, instance);
    assertEquals("testid", ((GPGConnection) con).getId());
    assertEquals("test", ((GPGConnection) con).getPassword());
    Properties props = ((GPGConnection) con).getProperties();
    assertEquals("testid", props.get("USERID"));
    assertEquals("test", props.get("PASSWORD"));
  }

  /**
   * Tests if all given properties to a gpg-connection are used correct.
   * 
   * @throws Exception
   *           if the tests fails
   */
  public void testConnectStringProperties1() throws Exception {
    Properties props = new Properties();
    props.put("USERID", "to-override");
    props.put("PASSWORD", "test");
    props.put("other", "other-prop");
    Class.forName("org.waffel.jscf.gpg.GPGDriver");
    JSCFConnection con = JSCFDriverManager.getConnection(
        "jscf:gpg::/usr/bin/gpg:testid", props);
    boolean instance = (con instanceof GPGConnection);
    assertEquals(true, instance);
    assertEquals("testid", ((GPGConnection) con).getId());
    assertEquals("test", ((GPGConnection) con).getPassword());
    Properties propRet = ((GPGConnection) con).getProperties();
    assertEquals("testid", propRet.get("USERID"));
    assertEquals("test", propRet.get("PASSWORD"));
    assertEquals("other-prop", propRet.get("other"));
    Properties newProps = new Properties();
    newProps.put("USERID", "to-override");
    newProps.put("PASSWORD", "test");
    newProps.put("other", "other-prop");
    con.setProperties(newProps);
    assertEquals("to-override", ((GPGConnection) con).getId());
    assertEquals("test", ((GPGConnection) con).getPassword());
    assertEquals("other-prop", newProps.get("other"));
  }

  /**
   * Test if the seperator is used and parsed correct
   * 
   * @throws Exception
   *           if the test fails
   */
  public void testConnectStringVariants() throws Exception {
    Class.forName("org.waffel.jscf.gpg.GPGDriver");
    JSCFConnection con = JSCFDriverManager
        .getConnection("jscf:gpg:$C:/test$user$pass");
    boolean instance = (con instanceof GPGConnection);
    assertEquals(true, instance);
    assertEquals("C:/test", ((GPGConnection)con).getPath());
    assertEquals("user", ((GPGConnection)con).getId());
    assertEquals("pass", ((GPGConnection)con).getPassword());
    con.close();
    // test next combination
    con = JSCFDriverManager.getConnection("jscf:gpg:%/usr/bin/gpg%user1%pass1");
    instance = (con instanceof GPGConnection);
    assertEquals(true, instance);
    assertEquals("/usr/bin/gpg", ((GPGConnection)con).getPath());
    assertEquals("user1", ((GPGConnection)con).getId());
    assertEquals("pass1", ((GPGConnection)con).getPassword());
    con.close();

  }

}