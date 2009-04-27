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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;
import org.macchiato.maps.OccurencesMapImpl;

/**
 * Output all tokens read from a file, using the
 * {@link InputStreamTokenizer}.
 *
 * @author fdietz
 */
public class InputStreamTokenizerTest{
	/**
	 * Test outputs all tokens.
	 *
	 */
    @Test
	public void testInputStream() {
		try {
			File file= new File("res/spam/test1.txt");
			InputStreamTokenizer tokenizer=
				new InputStreamTokenizer(new FileInputStream(file));

			Token token= new Token();

			while (!token.equals(Token.EOF)) {
				token= tokenizer.nextToken();

				System.out.println(token.getString());
			}

			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Count occurence of every token
	 *
	 */
    @Test
	public void testInputStream2() {
		try {
			File file=
				new File("res/spam/test1.txt");

			InputStreamTokenizer tokenizer=
				new InputStreamTokenizer(new FileInputStream(file));

			OccurencesMapImpl stats= new OccurencesMapImpl();

			Token token= new Token();

			while (!token.equals(Token.EOF)) {
				token= tokenizer.nextToken();
				//				add to stats
				stats.addToken(token);

			}

			// print stats 
			stats.printResults();

			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
