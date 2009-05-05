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
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fdietz
 *  
 */
public class TextParserTest {

    @Test
	public void testIndex() {
		String str = "hallo, ";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		Assert.assertEquals(6, index);
	}

    @Test
	public void testIndex2() {
		String str = "hallo, a";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		Assert.assertEquals(6, index);
	}

    @Test
	public void testIndex3() {
		String str = "hallo, peter,  ";

		int index = TextParser.getLeftIndex(str, ',', str.length() - 1);

		Assert.assertEquals(13, index);
	}

    @Test
	public void testIndex4() {
		String str = "hallo, peter,  ";

		int index = TextParser.getLeftIndex(str, ',', 8);

		Assert.assertEquals(6, index);
	}

    @Test
	public void testIndexRight() {
		String str = "hallo, ";

		int index = TextParser.getRightIndex(str, ',', 1);

		Assert.assertEquals(5, index);
	}

    @Test
	public void testIndexRight2() {
		String str = "hallo, peter, ";

		int index = TextParser.getRightIndex(str, ',', 6);

		Assert.assertEquals(12, index);
	}

    @Test
	public void testGetTextRegion() {
		String str = "hallo, ";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		Assert.assertEquals(" ", result);
	}

    @Test
	public void testGetTextRegion2() {
		String str = "hallo, a";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		Assert.assertEquals(" a", result);
	}

    @Test
	public void testGetTextRegion3() {
		String str = "hallo, peter";

		String result = TextParser.getTextRegion(str, ',', str.length() - 1);

		Assert.assertEquals(" peter", result);
	}

    @Test
	public void testGetTextRegion4() {
		String str = "hallo, peter";

		String result = TextParser.getTextRegion(str, ',', str.length() - 3);

		Assert.assertEquals(" pet", result);
	}

    @Test
	public void testReplace() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", 6, str.length());

		Assert.assertEquals("hallo,klaus", result);
	}

    @Test
	public void testReplace2() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", TextParser.getLeftIndex(
				str, ',', 8)+1, str.length());

		Assert.assertEquals("hallo, klaus", result);
	}

    @Test
	public void testReplace3() {
		String str = "hallo, peter";

		String result = TextParser.replace(str, "klaus", ',' , str.length());

		Assert.assertEquals("hallo, klaus", result);
	}

    @Test
	public void testGetItemAt() {
		String str = "hallo, peter, ";

		String result = TextParser.getItemAt(str, ',', 6);

		Assert.assertEquals(" peter", result);
	}
	
	/**
	 * Left boundary check
	 *
	 */
    @Test
	public void testGetItemAt2() {
		String str = "hallo, peter, ";

		String result = TextParser.getItemAt(str, ',', 3);

		Assert.assertEquals("hallo", result);
	}
	
	/**
	 * right boundary check
	 *
	 */
    @Test
	public void testGetItemAt3() {
		String str = "hallo, peter, a";

		String result = TextParser.getItemAt(str, ',', str.length()-1);

		Assert.assertEquals(" a", result);
	}

    @Test
	public void testIndexPattern3() {
		Pattern separatorPattern=Pattern.compile("[,;:]");
		String str = "hallo; peter,  ";

		int index = TextParser.getLeftIndex(str, separatorPattern, str.length() - 1);

		Assert.assertEquals(13, index);
	}

    @Test
	public void testGetItemAtPattern3() {
		Pattern separatorPattern=Pattern.compile("[,;:]");
		String str = "hallo, peter: a";

		String result = TextParser.getItemAt(str, separatorPattern, str.length()-1);

		Assert.assertEquals(" a", result);
	}
	
	
}