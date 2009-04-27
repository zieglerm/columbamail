//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.macchiato.tokenizer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author fdietz
 */
public class WhitespaceStringTokenizerTest{

    @Test
	public void testString() {
		String source= "Dies ist ein Test";

		WhitespaceStringTokenizer tokenizer=
			new WhitespaceStringTokenizer(source);
		Token[] tokens= new Token[4];
		for (int i= 0; i < 4; i++) {
			tokens[i]= tokenizer.nextToken();
		}

		Assert.assertEquals((String) tokens[0].getString(), "Dies");
		Assert.assertEquals((String) tokens[1].getString(), "ist");
		Assert.assertEquals((String) tokens[2].getString(), "ein");
		Assert.assertEquals((String) tokens[3].getString(), "Test");
	}
}
