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
package org.columba.ristretto.parser;

import junit.framework.TestCase;

import org.columba.ristretto.message.Address;

public class AddressParserTest extends TestCase {

	public void testSingle1() {
		String testString = "Peter ?lafton <xyt@zpt.de>";
		
		
		Address address;
		try {
			address = AddressParser.parseAddress(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		Address testAddress = new Address( "Peter ?lafton", "xyt@zpt.de");
		
		assertTrue( address.equals( testAddress ));
		assertTrue( address.getDisplayName().equals("Peter ?lafton"));
		assertTrue( address.toString().equals("\"Peter ?lafton\" <xyt@zpt.de>"));
	}

	public void testSingle8() {
		String testString = "mail@timostich.de, ";
		
		
		Address[] address;
		try {
			address = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		assertEquals(1, address.length);
		assertEquals("mail@timostich.de", address[0].getMailAddress());
	}

	/*	
	public void testGroup() {
	    String testString = "\"GMX Kundennummer #7743037\": ;";
	    
	    
	    Address[] addressList;
	    try {
	        addressList = AddressParser.parseMailboxList(testString);
	    } catch (ParserException e) {
	        assertTrue(false);
	        return;
	    }
	    
	    assertTrue( addressList[0].getDisplayName().equals("\"GMX Kundennummer #7743037\""));
	}
*/	
	
	
	public void testSingle2() {
		String testString = "<xyt@zpt.de>";
		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		Address testAddress = new Address( "xyt@zpt.de");
		
		assertTrue( addressList.length == 1);
		assertTrue( ((Address) addressList[0]).equals( testAddress ));
		assertTrue( ((Address) addressList[0]).getDisplayName().equals(""));
		assertEquals( addressList[0].getMailAddress(), "xyt@zpt.de");
	}

	public void testSingle3() {
		String testString = "xyt@zpt.de";
		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		Address testAddress = new Address( "xyt@zpt.de");
		
		assertTrue( addressList.length == 1);
		assertTrue( ((Address) addressList[0]).equals( testAddress ));
		assertTrue( ((Address) addressList[0]).getDisplayName().equals(""));			
	}

	public void testSingle4() {
	    String testString = "\"Peter ?lafton\" <xyt@zpt.de>";
	    
	    
	    Address[] addressList;
	    try {
	        addressList = AddressParser.parseMailboxList(testString);
	    } catch (ParserException e) {
	        assertTrue(false);
	        return;
	    }
	    
	    Address testAddress = new Address( "Peter ?lafton", "xyt@zpt.de");
	    
	    assertTrue( addressList.length == 1);
	    assertTrue( ((Address) addressList[0]).equals( testAddress ));
	    assertTrue( ((Address) addressList[0]).getDisplayName().equals("Peter ?lafton"));	
	}
	
	
	public void testMultiple1() {
		String testString = "Peter ?lafton <xyt@zpt.de>, xyt@zpt.de, <xyt@zpt.de>";		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		assertTrue( addressList.length == 3);
		assertTrue( ((Address) addressList[0]).equals( new Address( "Peter ?lafton", "xyt@zpt.de") ));
		assertTrue( ((Address) addressList[0]).getDisplayName().equals("Peter ?lafton"));	
		assertTrue( ((Address) addressList[1]).equals( new Address( "xyt@zpt.de") ));
		assertTrue( ((Address) addressList[2]).equals( new Address( "xyt@zpt.de") ));
	}
	
	public void testMultiple2() {
		String testString = "hans, peter, lukas";		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		assertTrue( addressList.length == 3);
		assertTrue( ((Address) addressList[0]).equals( new Address( "hans") ));
		assertTrue( ((Address) addressList[1]).equals( new Address( "peter") ));
		assertTrue( ((Address) addressList[2]).equals( new Address( "lukas") ));
	}

	
	public void testMultipleQuoted1() {
		String testString = "\"Rmazam, Peter\" <xyt@zpt.de>, \"Bkalbal, Olaf\" <zkn@opb.com>";
		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		assertTrue( addressList.length == 2);
		assertTrue( ((Address) addressList[0]).equals( new Address( "Rmazam, Peter", "xyt@zpt.de") ));
		assertTrue( ((Address) addressList[0]).getDisplayName().equals("Rmazam, Peter"));			
		assertTrue( ((Address) addressList[1]).equals( new Address( "Bkalbal, Olaf", "zkn@opb.com") ));
		assertTrue( ((Address) addressList[1]).getDisplayName().equals("Bkalbal, Olaf"));			
	}
	
	public void testSingle5() {
	    String testString = "columba-devel-admin@lists.sourceforge.net";
	    
	    
	    Address[] addressList;
	    try {
		        addressList = AddressParser.parseMailboxList(testString);
	    } catch (ParserException e) {
	        assertTrue(false);
	        return;
	    }
	    
	    Address testAddress = new Address( "columba-devel-admin@lists.sourceforge.net");
	    
	    assertTrue( addressList.length == 1);
	    assertTrue( ((Address) addressList[0]).equals( testAddress ));
	}
	
	
	
	
	public void testAddressToString1() {
		Address testAddress = new Address( "Timo Stich", "tstich@users.sourceforge.net");
		String addressString = testAddress.toString();
		assertTrue( addressString.equals("\"Timo Stich\" <tstich@users.sourceforge.net>"));
	}

	public void testAddressToString2() {
		Address testAddress = new Address( "tstich@users.sourceforge.net");
		String addressString = testAddress.toString();
		assertTrue( addressString.equals("tstich@users.sourceforge.net"));
	}
	
	public void testSingle6() {
		String testString = "=?ISO-8859-1?Q?J=F6rg_Tester?= <tester@test.de>";
		
		Address address;
		try {
			address = AddressParser.parseAddress(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		Address testAddress = new Address( "=?ISO-8859-1?Q?J=F6rg_Tester?=", "tester@test.de");
		
		assertTrue( address.equals( testAddress ));
	}

	public void testSingle7() {
		String testString = "\"info@spreadshirt.de\"<info@spreadshirt.de>";
		
		
		Address[] addressList;
		try {
			addressList = AddressParser.parseMailboxList(testString);
		} catch (ParserException e) {
			assertTrue(false);
			return;
		}
		
		Address testAddress = new Address( "info@spreadshirt.de", "info@spreadshirt.de");
		
		assertTrue( addressList.length == 1);
		assertTrue( ((Address) addressList[0]).equals( testAddress ));
		assertTrue( ((Address) addressList[0]).getDisplayName().equals("info@spreadshirt.de"));
		assertEquals( addressList[0].getMailAddress(), "info@spreadshirt.de");
	}

}
