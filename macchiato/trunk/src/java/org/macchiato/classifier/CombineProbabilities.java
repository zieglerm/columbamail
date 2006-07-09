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

import java.util.List;
import java.util.logging.Logger;


/**
 * @author fdietz
 */
public class CombineProbabilities {
    static final Logger log = Logger.getLogger("org.macchiato.classifier");
    
    /**
     * Combine probabilites.
     * <p>
     * For the maths, visit:<br>
     * <a href="http://www.mathpages.com/home/kmath267.htm">http://www.mathpages.com/home/kmath267.htm</a>
     * <p>
     * In short:<br>
     * x1*x2*x3/(x1*x2*x3 + z)
     * z = (1-x1)*(1-x2)*(1-x3)
     *
     * @param list      list containing probabilities as float values
     * @return          resulting probability
     */
    public static float combine(List list) {
		float result = -1;

		float z = 1.0f;
		float x = 1.0f;

		
        for (int i = 0; i < list.size(); i++) {
            Float d = (Float) list.get(i);
            
            
            float p = d.floatValue();
            
			log.info("probability="+p);
						
            z = z * (1.0f - p);			
            x = x * p;
       }

        result = x / (x + z);
        
		log.info("combined message score="+result);

        return result;
    }
}
