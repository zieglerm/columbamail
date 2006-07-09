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
package org.macchiato;

import java.util.Iterator;
import java.util.List;

import org.macchiato.maps.OccurencesMap;
import org.macchiato.maps.OccurencesMapFactory;
import org.macchiato.maps.OccurencesMapImpl;
import org.macchiato.tokenfilter.MaxTokenLengthFilter;
import org.macchiato.tokenfilter.MinTokenLengthFilter;
import org.macchiato.tokenfilter.stopwords.EnglishStopWordFilter;
import org.macchiato.tokenfilter.stopwords.RFC822StopWordFilter;
import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.Tokenizer;
import org.macchiato.tokenizer.TokenizerFactory;

/**
 * 
 *
 * @author fdietz
 */
public final class SpamFilterHelper {

	/**
	 * Create occurences map of message.
	 * 
	 * @param message		message 
	 * @return				occurences map
	 */
	public static OccurencesMap createOccurencesMap(Message message) {

		OccurencesMap map= new OccurencesMapImpl();

		// create tokenizer from inputstream
		Tokenizer tokenizer=
			TokenizerFactory.createHTMLInputStreamTokenizer(
				message.getInputstream());

		// filter tokens
		Tokenizer filter= filterTokens(tokenizer);

		// create occurences map
		OccurencesMapFactory.appendOccurencesMap(map, filter);

		// get map containing headerfields
		List headerfields= message.getList();

		if (headerfields != null) {

			// for each headerfield
			Iterator it= headerfields.iterator();
			while (it.hasNext()) {
				// create token
				Token token= new Token((String) it.next());

				// add to occurences map
				map.addToken(token);
			}
		}
		return map;

	}

	/**
		 * Filter for Tokenizer
		 * 
		 * @param inputTokenizer		input tokenizer
		 * @return						filtered output tokenizer
		 */
	public static Tokenizer filterTokens(Tokenizer inputTokenizer) {
			
		// filter too-long tokens (>30 characters)
		Tokenizer tokenLengthFilter= new MaxTokenLengthFilter(inputTokenizer, 30);
				
		// filter too-short tokens (<3 characters)
		Tokenizer tokenMinLengthFilter=
			new MinTokenLengthFilter(tokenLengthFilter, 3);

		// filter email-header keys
		Tokenizer rfc822Filter= new RFC822StopWordFilter(tokenMinLengthFilter);

		// filter english stop words
		Tokenizer filter= new EnglishStopWordFilter(rfc822Filter);

		return filter;
	}
}
