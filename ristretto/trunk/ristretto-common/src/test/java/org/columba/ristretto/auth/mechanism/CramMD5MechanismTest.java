package org.columba.ristretto.auth.mechanism;

import java.io.IOException;

import junit.framework.TestCase;

import org.columba.ristretto.auth.AuthenticationException;
import org.columba.ristretto.auth.AuthenticationMechanism;

public class CramMD5MechanismTest extends TestCase {

	   public void testOK() throws IOException {
	        TestAuthServer server = new TestAuthServer();
	        server.addResponse("<1896.697170952@postoffice.reston.mci.net>".getBytes());
	        server.addCall("tim b913a602c7eda7a495b4e6e7334d3890".getBytes());
	        AuthenticationMechanism mechanism = new CramMD5Mechanism();
	        
	        try {
	            mechanism.authenticate(server,"tim","tanstaaftanstaaf".toCharArray());
	        } catch (AuthenticationException e) {
	        	e.printStackTrace();
	        }
	                
	        assertTrue( server.isOk());
	    }

	    public void testFailure() throws IOException {
	        TestAuthServer server = new TestAuthServer();
	        server.addCall("tim".getBytes());
	        server.addCall("oo".getBytes());
	        AuthenticationMechanism mechanism = new CramMD5Mechanism();
	        
	        try {
	            mechanism.authenticate(server,"tim", "foo".toCharArray());
	        } catch (AuthenticationException e) {
	            e.printStackTrace();
	        }
	        
	        assertFalse( server.isOk());
	    }
	   
	
}
