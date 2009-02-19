/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Ristretto Mail API.
 *
 * The Initial Developers of the Original Code are
 * Timo Stich and Frederik Dietz.
 * Portions created by the Initial Developers are Copyright (C) 2004
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.columba.ristretto.imap.parser;

import junit.framework.TestCase;

import org.columba.ristretto.imap.IMAPResponse;
import org.columba.ristretto.imap.ResponseTextCode;
import org.columba.ristretto.parser.ParserException;

public class IMAPResponseParserTest extends TestCase {
	
	public void testUntaggedStatus1() {
		String responseString = "* OK Alles klar!\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getResponseSubType().equals("OK"));
			assertTrue(response.getResponseMessage().equals("Alles klar!"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testUntaggedStatus2() {
		String responseString = "* OK [UNSEEN 12] Alles klar!\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getResponseSubType().equals("OK"));
			assertEquals(response.getResponseTextCode().getType(), ResponseTextCode.UNSEEN);
			assertEquals(response.getResponseTextCode().getIntValue(), 12);
			assertTrue(response.getResponseMessage().equals("Alles klar!"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testUntaggedMailbox1() {
		String responseString = "* LIST mailboxlist\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getResponseSubType().equals("LIST"));
			assertTrue(response.getResponseMessage().equals("mailboxlist"));
			assertTrue(response.getResponseType() == IMAPResponse.RESPONSE_MAILBOX_DATA);
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testUntaggedMailbox2() {
		String responseString = "* 512 EXISTS\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getResponseSubType().equals("EXISTS"));
			assertTrue(response.getPreNumber() == 512);
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testUntaggedMessage1() {
		String responseString = "* 12 FETCH fetchblabla\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getPreNumber() == 12);
			assertTrue(response.getResponseSubType().equals("FETCH"));
			assertTrue(response.getResponseMessage().equals("fetchblabla"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}
	
	public void testUntaggedMessage2() {
		String responseString = "* 124 EXPUNGE\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue(response.getPreNumber() == 124);
			assertTrue(response.getResponseSubType().equals("EXPUNGE"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testTaggedStatus1() {
		String responseString = "A01 OK Alles klar!\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(response.isTagged());
			assertTrue(response.getTag().equals("A01"));
			assertTrue(response.getResponseSubType().equals("OK"));
			assertTrue(response.getResponseMessage().equals("Alles klar!"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}
	
	public void testTaggedStatus2() {
		String responseString = "A142 OK [READ-WRITE] SELECT completed";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(response.isTagged());
			assertTrue(response.getTag().equals("A142"));
			assertTrue(response.getResponseSubType().equals("OK"));
			assertEquals(response.getResponseTextCode().getType(), ResponseTextCode.READ_WRITE);
			assertTrue(response.getResponseMessage().equals("SELECT completed"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testContinuation1() {
		String responseString = "+ give me more\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(response.getResponseType() == IMAPResponse.RESPONSE_CONTINUATION);
			assertTrue(response.getResponseMessage().equals("give me more"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testContinuation2() {
		String responseString = "+ [UIDVALIDITY 385752904] give me more\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(response.getResponseType() == IMAPResponse.RESPONSE_CONTINUATION);
			assertEquals(response.getResponseTextCode().getType(), ResponseTextCode.UIDVALIDITY );
			assertEquals(response.getResponseTextCode().getIntValue(), 385752904);
			assertTrue(response.getResponseMessage().equals("give me more"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}

	public void testCapabilities() {
		String responseString = "* CAPABILITY IMAP4REV1 IDLE NAMESPACE MAILBOX-REFERRALS SCAN SORT THREAD=REFERENCES THREAD=ORDEREDSUBJECT MULTIAPPEND\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertTrue( response.getResponseSubType().equals("CAPABILITY"));
		} catch (ParserException e) {
			e.printStackTrace();
		} 
	}
	
	public void testResponseTextCode1() {
		String responseString = "* OK [PERMANENTFLAGS (\\Answered \\Flagged \\Draft \\Deleted \\Seen)]\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(!response.isTagged());
			assertEquals( ResponseTextCode.PERMANENTFLAGS, response.getResponseTextCode().getType());
		} catch (ParserException e) {
			e.printStackTrace();
		} 		
	}

	public void testResponseTextCode2() {
		String responseString = "3 OK [READ_WRITE] completed\r\n";
		try {
			IMAPResponse response = IMAPResponseParser.parse(responseString);
			
			assertTrue(response.isTagged());
			assertEquals( ResponseTextCode.READ_WRITE, response.getResponseTextCode().getType());
		} catch (ParserException e) {
			e.printStackTrace();
		} 		
	}

}
