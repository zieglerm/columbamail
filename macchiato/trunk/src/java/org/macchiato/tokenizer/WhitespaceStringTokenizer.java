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

import java.util.StringTokenizer;


/**
 * Tokenizer for a string containing whitespace separated words.
 *
 * @author fdietz
 */
public class WhitespaceStringTokenizer implements Tokenizer {
    private String source;
    private StringTokenizer tokenizer;

    public WhitespaceStringTokenizer(String s) {
        this.source = s;

        tokenizer = new StringTokenizer(s, " ");
    }

    /**
     * @see org.macchiato.tokenizer.Tokenizer#count()
     */
    public int count() {
        return tokenizer.countTokens();
    }

    /**
     * @see org.macchiato.tokenizer.Tokenizer#nextToken()
     */
    public Token nextToken() {
        // if no more tokens available
        // -> return EOF
        if (!tokenizer.hasMoreTokens()) {
            return Token.EOF;
        }

        return new Token(tokenizer.nextToken());
    }
}
