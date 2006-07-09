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
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 
 *
 * @author fdietz
 */
public class TokenizerFactory {

	public static Tokenizer createHTMLInputStreamTokenizer(InputStream istream) {
		return new HTMLInputStreamTokenizer(istream);
	}

	public static Tokenizer createInputStreamTokenizer(InputStream istream) {
		return new InputStreamTokenizer(istream);
	}

	public static Tokenizer createFileTokenizer(File file)
		throws FileNotFoundException {
		return new InputStreamTokenizer(new FileInputStream(file));
	}

	public static Tokenizer createStringTokenizer(String string) {
		return new WhitespaceStringTokenizer(string);
	}

}
