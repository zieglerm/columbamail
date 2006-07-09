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
package org.macchiato.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * HTML-aware inputstream tokenizer
 * <p>
 * You can find a more detailed description of this initial tokenizer at <a
 * href="http://www.paulgraham.com/better.html">A better spam filter" </a>.
 * <p>
 * Definition of a token: <br>
 * <ul>
 * <li>case is preserved (most spam message use imperative words)</li>
 * <li>Periods and commas are constituents if they occur between two digits.
 * This lets me get ip addresses and prices intact.</li>
 * <li>A price range like $20-25 yields two tokens, $20 and $25.</li>
 * <li>color attributes of html tags</li>
 * </ul>
 * 
 * @author fdietz
 */
public class HTMLInputStreamTokenizer implements Tokenizer {

	/**
	 * use these characters to separate tokens
	 */
	private static int[] delimiter = { ' ', '\n' };

	/**
	 * trailing/leading characters which are going to be trimmed
	 */
	private static int[] trimCharacters = { '.', ')', '(', ']', '[', ',', '!',
			':', ';', '?', '*', '-', '_', '\'', '}', '{', '=', '?', '`', ',' };

	/**
	 * html attributes we extract tokens from
	 */
	private static String[] attributes = { "src", "href", "color",
			"background-color", "face" };

	/**
	 * Reader stream
	 */
	private Reader r;

	/**
	 * StringBuffer for buffering bytes read from the stream
	 */
	private StringBuffer buf;

	/**
	 * inputstream which delivers the data
	 */
	InputStream istream;

	/**
	 * true, if parser is currently in a html-tag. False, otherwise
	 */
	boolean htmlTag = false;

	/**
	 * Constructor
	 * 
	 * @param istream
	 *            inputstream which should be tokenized
	 */
	public HTMLInputStreamTokenizer(InputStream istream) {
		this.istream = istream;

		r = new BufferedReader(new InputStreamReader(istream));
	}

	/**
	 * @see org.macchiato.tokenizer.Tokenizer#nextToken()
	 */
	public Token nextToken() {
		buf = new StringBuffer();

		try {
			int ch;

			ch = r.read();

			// while stream has characters
			while (ch != -1) {
				if (ch == '"') {
					// opening " attribute
					// -> read all characters until we find a closing "
					// character

					// skip the already found " character
					ch = r.read();
					while ((ch != -1) && (ch != '"')) {
						buf.append((char) ch);
						ch = r.read();
					}

					// return results
					if (buf.length() != 0) {
						return extractAttribute(buf);
					}
				}

				if (ch == '<') {
					// opening html tag
					htmlTag = true;

					// skipping tag characters
					// example: <html
					while ((ch != -1) && (!isSeparator(ch))) {
						ch = r.read();
					}
				} else if (ch == '>') {
					// closing html tag
					htmlTag = false;

					// only return the already buffered characters
					// -> skip the trailing ">" character
					if (buf.length() != 0) {
						// extract interesting attribute values
						return extractAttribute(buf);
					} else {
						ch = r.read();
						// start from the begging to skip the "<P" opening tag,
						// too
						// -> example: IMG><P
						continue;
					}
				}

				if (isSeparator(ch)) {
					// found separator character
					// -> only finish if buffer contains some characters

					if (buf.length() != 0) {
						return extractAttribute(buf);
					}
				} else {
					// found token data
					// -> put it in buffer
					buf.append((char) ch);
				}

				// read more characters from stream
				ch = r.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// stream has no more tokens
		// -> return EOF
		return Token.EOF;
	}

	/**
	 * Check if the buffer contains a HTML attribute.
	 * <p>
	 * If its an color attribute return the attribute value;
	 * 
	 * @param buf
	 *            buffer containing the token data
	 * @return token
	 */
	private Token extractAttribute(StringBuffer buf) {
		int equalSign = buf.indexOf("=");

		// html attributes are key/value pairs
		// example: "color=#ffffff"
		if (equalSign != -1) {
			// found html attribute

			// string before equal sign character "="
			String beforeEqualSign = buf.substring(0, equalSign);

			boolean matchFound = false;
			for (int i = 0; i < attributes.length; i++) {
				if (beforeEqualSign.equalsIgnoreCase(attributes[i])) {
					// found "SRC" attribute
					// example: src="http://www.web.de"
					// remove "src=" from buffer
					buf.delete(0, attributes[i].length() + 1);
					return new Token(trim(buf));
				}
			}

			return Token.SKIP;

		}

		return new Token(trim(buf));
	}

	private String trim(StringBuffer buf) {
		String result = buf.toString();

		boolean modified = false;
		// for each trailing character
		while (!modified) {
			if (result.length() <= 1)
				return result;

			modified = true;
			for (int i = 0; i < trimCharacters.length; i++) {
				if (result.length() <= 1)
					return result;

				if (result.charAt(result.length() - 1) == trimCharacters[i]) {
					result = result.substring(0, result.length() - 1);
					modified = false;
				}

			}

		}

		modified = false;
		while (!modified) {
			if (result.length() <= 1)
				return result;

			modified = true;
			// for each leading character
			for (int i = 0; i < trimCharacters.length; i++) {
				if (result.length() <= 1)
					return result;

				if (result.charAt(0) == trimCharacters[i]) {
					result = result.substring(1);
					modified = false;
				}
			}
		}

		return result;
	}

	/**
	 * Check if int-value is a separator.
	 * 
	 * @param ch
	 *            int value
	 * @return true, if its a separator. False, otherwise.
	 */
	private boolean isSeparator(int ch) {
		// for each delimiter character
		for (int i = 0; i < delimiter.length; i++) {
			// is this character a delimiter as specified
			// by delimiter int[] array
			if (ch == delimiter[i]) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Close stream.
	 *  
	 */
	public void close() {
		try {
			r.close();
			istream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}