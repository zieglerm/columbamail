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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.macchiato.tokenizer.Token;


/**
 * @author fdietz
 */
public class OccurencesMapImpl extends AbstractOccurencesMap{
    private Map map;

    /**
     * Default constructor
     *
     */
    public OccurencesMapImpl() {
        map = new HashMap();
    }

	public void addToken(String data) {
		addToken( new Token(data));
	}
    /**
     * Add token to map.
     * <p>
     * Increase number of occurence if token was added
     * before.
     *
     * @param token
     */
    public void addToken(Token token) {
        if (map.containsKey(token)) {
            // token was added before
            Integer count = (Integer) map.get(token);
            int c = count.intValue();
            c++;

            // add increased count to map
            map.put(token, new Integer(c));
        } else {
            // this is the first this token is added
            map.put(token, new Integer(1));
        }
    }

    /**
     * Get count of token.
     *
     * @param token     token
     * @return          count of token
     */
    public int getOccurences(Token token) {
        if (map.containsKey(token)) {
            Integer count = (Integer) map.get(token);

            return count.intValue();
        }

        return 0;
    }


    /**
     * Get total count of tokens
     *
     * @return      total count of tokens
     */
    private int getTotalTokenCount() {
        return map.size();
    }

    /**
     * Get total count of occurences.
     *
     * @return      total count of occurences
     */
    public int getTotalOccurencesCount() {
        int totalTokenCount = getTotalTokenCount();
        Iterator it = map.keySet().iterator();
        int count = 0;

        while (it.hasNext()) {
            Token key = (Token) it.next();
            Integer countInt = (Integer) map.get(key);
            count += countInt.intValue();
        }

        return count;
    }

    /**
     * Get occurence of token in percentage.
     * 
     * @param token     token
     * @return          occurence in percentage
     */
    public float getTokenCountInPercentage(Token token) {
        int tokenCount = getOccurences(token);

        int occurencesCount = getTotalOccurencesCount();

        // this does only work when casting to double
        float probability = (float) tokenCount / (float) occurencesCount;

        return probability;
    }
    
    /**
     * @see org.macchiato.maps.OccurencesMap#iterator()
     */
    public Iterator iterator() {
      return map.keySet().iterator();
    }

	/**
	 * @see org.macchiato.maps.OccurencesMap#getOccurences(java.lang.String)
	 */
	public int getOccurences(String tokenString) {
		return getOccurences(new Token(tokenString));
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#remove(org.macchiato.tokenizer.Token)
	 */
	public void remove(Token token) {
		map.remove(token);
	}

}
