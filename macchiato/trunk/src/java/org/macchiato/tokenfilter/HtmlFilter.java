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
package org.macchiato.tokenfilter;

import java.util.regex.Pattern;

import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.Tokenizer;

/**
 * Remove html tags like <BR>, or <table>. 
 *
 * @author fdietz
 */
public class HtmlFilter extends AbstractTokenizerFilter {

	// opening tags: <html	
	private static String openingTags= "<[\\w\\d]*";

	// closing tags: /html>
	private static String closingTags= "/[\\w\\d]*>";

	// complete tags: <html>
	private static String tags= "<[\\w\\d]*>";

	//	complete tags </html>
	private static String closedTags= "</[\\w\\d]*>";
	
	// &nbsp; tag
	private static String space = "&nbsp;";

	private Pattern[] p=
		{
			Pattern.compile(tags),
			Pattern.compile(closedTags),
			Pattern.compile(openingTags),
			Pattern.compile(closingTags),
			Pattern.compile(space)};

	/**
	 * @param tokenizer
	 */
	public HtmlFilter(Tokenizer tokenizer) {
		super(tokenizer);

	}

	/**
	 * @see org.macchiato.tokenizer.Tokenizer#nextToken()
	 */
	public Token nextToken() {
		Token token= getTokenizer().nextToken();
		if (token.equals(Token.EOF))
			return token;
		if (token.equals(Token.SKIP))
			return token;

		for (int i= 0; i < p.length; i++) {

			// replace all found tags, with ""
			token.setString(p[i].matcher(token.getString()).replaceAll(""));
		}

		return token;
	}

}
