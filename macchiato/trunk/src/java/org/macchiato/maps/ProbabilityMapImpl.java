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
public class ProbabilityMapImpl implements ProbabilityMap {
	private Map map;

	public ProbabilityMapImpl() {
		map= new HashMap();
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#addToken(java.lang.String, double);
	 */
	public void addToken(Token token, float probability) {
		map.put(token, new Float(probability));
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#getOccurences(java.lang.String)
	 */
	public float getProbability(Token token) {
		if (map.containsKey(token)) {
			Float oc= (Float) map.get(token);

			return oc.floatValue();
		}

		return 0;
	}

	public Iterator iterator() {
		return map.keySet().iterator();
	}

	/**
	    * Print results.
	    *
	    */
	public void printResults() {
		Iterator it= map.keySet().iterator();

		while (it.hasNext()) {
			Token key= (Token) it.next();
			Float p= (Float) map.get(key);
			System.out.println(
				"Token[" + key.getString() + "]=" + p.toString());
		}
	}
	
	/**
	 * @see org.macchiato.maps.ProbabilityMap#count()
	 */
	public int count() {
		return map.size();
	}

}
