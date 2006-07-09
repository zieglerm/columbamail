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
package org.macchiato.maps;

import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.Tokenizer;

/**
 * 
 *
 * @author fdietz
 */
public class OccurencesMapFactory {

	public static OccurencesMap createOccurencesMap(Tokenizer tokenizer) {
		OccurencesMap map= new OccurencesMapImpl();

		Token token= new Token();

		while (!token.equals(Token.EOF)) {
			token= tokenizer.nextToken();
			map.addToken(token);

		}

		return map;
	}

	public static void appendOccurencesMap(
		OccurencesMap map,
		Tokenizer tokenizer) {
		Token token= new Token();

		// remove trailing EOF
		map.remove(Token.EOF);

		while (!token.isEOF()) {
			token= tokenizer.nextToken();
			if (!token.isSKIP()) {
				map.addToken(token);
			}

		}
	}

}
