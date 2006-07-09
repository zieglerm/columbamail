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

import org.macchiato.tokenizer.Token;


/**
 * @author fdietz
 */
public interface OccurencesMap {
    
    /**
    * Add token to map.
    * <p>
    * Increase number of occurence if token was added
    * before.
    *
    * @param token
    */
    void addToken(Token token);

	void addToken(String data);
	
    /**
     * Get occurences of token.
     *
     * @param token     token
     * @return  occurences
     */
    int getOccurences(Token token);
    
    int getOccurences(String tokenString);

    /**
     * Get total count of occurences of complete map.
     *
     * @return
     */
    int getTotalOccurencesCount();
    
    Iterator iterator();
    
    void printResults();
    
    void remove(Token token);
}
