// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.frapuccino.addresscombobox;

import java.util.regex.Pattern;

import junit.framework.TestCase;

/**
 * @author fdietz
 *  
 */
public class TextParserTest extends TestCase {

	public void testIndex() {
		String str = "hallo, ";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		assertEquals(6, index);
	}

	public void testIndex2() {
		String str = "hallo, a";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		assertEquals(6, index);
	}

	public void testIndex3() {
		String str = "hallo, peter,  ";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		assertEquals(13, index);
	}

	public void testIndex4() {
		String str = "hallo, peter,  ";

		int index = TextParser.getLeftIndex(str, ',', 8);

		assertEquals(6, index);
	}
	
	public void testIndexRight() {
		String str = "hallo, ";

		int index = TextParser.getRightIndex(str, ',', 1);

		assertEquals(5, index);
	}
	
	public void testIndexRight2() {
		String str = "hallo, peter, ";

		int index = TextParser.getRightIndex(str, ',', 6);

		assertEquals(12, index);
	}

	public void testGetTextRegion() {
		String str = "hallo, ";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		assertEquals(" ", result);
	}

	public void testGetTextRegion2() {
		String str = "hallo, a";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		assertEquals(" a", result);
	}

	public void testGetTextRegion3() {
		String str = "hallo, peter";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		assertEquals(" peter", result);
	}

	public void testGetTextRegion4() {
		String str = "hallo, peter";

		String result = TextParser.getTextRegion(str, ',', str.length() - 3);

		assertEquals(" pet", result);
	}

	public void testReplace() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", 6, str.length());

		assertEquals("hallo,klaus", result);
	}

	public void testReplace2() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", TextParser.getLeftIndex(
				str, ',', 8)+1, str.length());

		assertEquals("hallo, klaus", result);
	}
	
	public void testReplace3() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", ',' , str.length());

		assertEquals("hallo, klaus", result);
	}
	
	public void testGetItemAt() {
		String str = "hallo, peter, ";

		String result = TextParser.getItemAt(str, ',', 6);

		assertEquals(" peter", result);
	}
	
	/**
	 * Left boundary check
	 *
	 */
	public void testGetItemAt2() {
		String str = "hallo, peter, ";

		String result = TextParser.getItemAt(str, ',', 3);

		assertEquals("hallo", result);
	}
	
	/**
	 * right boundary check
	 *
	 */
	public void testGetItemAt3() {
		String str = "hallo, peter, a";

		String result = TextParser.getItemAt(str, ',', str.length()-1);

		assertEquals(" a", result);
	}

	public void testIndexPattern3() {
		Pattern separatorPattern=Pattern.compile("[,;:]");
		String str = "hallo; peter,  ";

		int index = TextParser.getLeftIndex(str, separatorPattern, str.length() - 1);

		assertEquals(13, index);
	}
	
	public void testGetItemAtPattern3() {
		Pattern separatorPattern=Pattern.compile("[,;:]");
		String str = "hallo, peter: a";

		String result = TextParser.getItemAt(str, separatorPattern, str.length()-1);

		assertEquals(" a", result);
	}
	
	
}