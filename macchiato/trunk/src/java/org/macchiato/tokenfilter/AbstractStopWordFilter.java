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

import org.macchiato.tokenfilter.stopwords.StopWordProvider;
import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.Tokenizer;


/**
 * Removes stopwords from tokens.
 *
 * @author fdietz
 */
public abstract class AbstractStopWordFilter extends AbstractTokenizerFilter {
    private StopWordProvider provider;

    /**
     * Default Constructor
     *
     */
    public AbstractStopWordFilter(Tokenizer tokenizer) {
        super(tokenizer);
    }

    /**
     * @see org.macchiato.tokenizer.AbstractTokenizer#nextToken()
     */
    public Token nextToken() {
        Token token = getTokenizer().nextToken();

        if (isStopword(token)) {
            return Token.SKIP;
        }

        return token;
    }

    /**
     * Check if String-value is a stopword.
     *
     * @param ch        int value
     * @return          true, if its a stopword. False, otherwise.
     */
    private boolean isStopword(Token word) {
        // for each stopword
        for (int i = 0; i < getStopWordProviderInstance().length(); i++) {
            // is this word a stopword as specified
            // by stopword String[] array ?
            if (word.getString().equalsIgnoreCase(getStopWordProviderInstance().getStopWords()[i])) {
                return true;
            }
        }

        return false;
    }

    public abstract StopWordProvider getStopWordProviderInstance();
}
