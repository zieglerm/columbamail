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

import java.util.Iterator;

import org.macchiato.probability.Probability;
import org.macchiato.tokenizer.Token;

/**
 * @author fdietz
 */
public class ProbabilityMapFactory {

    /**
     * Create probability map, mapping a token to the probability that its
     * message is actually spam.
     *
     * @param token         token
     * @param ham           ham occurences map
     * @param spam          spam occurences map
     * @param hamCount      total count of ham messages
     * @param spamCount     total count of spam messages
     * @return
     */
    public static ProbabilityMap createProbabilityMap(
        OccurencesMap spam,
        OccurencesMap ham) {
        ProbabilityMap map = new ProbabilityMapImpl();

		// for each spam token
        Iterator it = spam.iterator();
        while (it.hasNext()) {
            Token token = (Token) it.next();
            
			float p = Probability.calc(spam.getOccurences(token), ham.getOccurences(token));
            map.addToken(token, p);
        }
        
        // for each ham token
        it = ham.iterator();
        while (it.hasNext()) {
        	Token token = (Token) it.next();
        	
        	// check if this token was already found in the spam map
        	if ( spam.getOccurences(token) == 0 )
        	{
        		// new token
				float p = Probability.calc(0, ham.getOccurences(token));
        		map.addToken(token, p);
        	}
        }

        return map;
    }
}
