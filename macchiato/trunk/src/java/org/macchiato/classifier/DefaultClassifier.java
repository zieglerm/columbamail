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
package org.macchiato.classifier;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.macchiato.maps.ProbabilityMap;
import org.macchiato.tokenizer.Token;

/**
 * @author fdietz
 */
public class DefaultClassifier{

      /**
     * Get probability if this is a spam message.
     * <p>
     * Expects hash map which contain the probability of
     * every token, if its spam or not.
     * 
     * @param map       hash map
     * @param list      list of tokens
     * @return          probability
     */
    public static float classify(ProbabilityMap map, List list) {
        
        List probabilityList = new Vector();
        Iterator it = list.iterator();
        while (it.hasNext()) {
			Token token = (Token) it.next();
            float d = map.getProbability(token);
            probabilityList.add( new Float(d) );
        }
        
        float result = CombineProbabilities.combine(probabilityList);
        return result;
        
    }
    
    /**
     * Calculate
     * @param map
     * @param token
     * @return
     */
    public static double classify(ProbabilityMap map, Token token) {
        List probabilityList = new Vector();
        double d = map.getProbability(token);
       
        return  d;
    }

}
