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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * Tokenizer which uses an underlying inputstream.
 *
 * @author fdietz
 */
public class InputStreamTokenizer implements Tokenizer {
    /**
     * use these characters to separate tokens
     */
    private static int[] delimiter = { ' ', '\n' };

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
     * Constructor
     *
     * @param istream       inputstream which should be tokenized
     */
    public InputStreamTokenizer(InputStream istream) {
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
                if (isSeparator(ch)) {
                    // found separator character
                    // -> only finish if buffer contains some characters
                    if (buf.length() != 0) {
                        return new Token(buf.toString());
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
     * Check if int-value is a separator.
     *
     * @param ch        int value
     * @return          true, if its a separator. False, otherwise.
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
