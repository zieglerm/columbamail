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
package org.macchiato.maps.filter;

import java.util.Iterator;
import java.util.Vector;

import org.macchiato.maps.OccurencesMap;
import org.macchiato.tokenizer.Token;

/**
 * Filters a given number of tokens, with the highest
 * occurences rate.
 *
 * @author fdietz
 */
public class TopOccurencesFilter extends AbstractOccurencesFilter {

	private int number;
	private Vector tokens;

	/**
	 * @param map
	 */
	public TopOccurencesFilter(OccurencesMap map, int number) {
		super(map);
		this.number= number;
		tokens= new Vector();
	}

	private float max() {
		Iterator it= getMap().iterator();
		float max= 0.0f;

		while (it.hasNext()) {
			Token token= (Token) it.next();
			float value= getMap().getOccurences(token);
			if (max < value)
				max= value;
		}

		return max;
	}

	private void insert(Token newToken, double value) {
		boolean bigger= false;

		if (tokens.size() < number) {
			// buffer is not full
			tokens.add(newToken);

		} else {

			Token min= null;
			for (int i= 0; i < tokens.size(); i++) {
				Token token= (Token) tokens.get(i);
				double d= getMap().getOccurences(token);
				if (value > d) {
					// found a new value which is bigger

					min= token;
					bigger= true;
					break;
				}
			}

			if (bigger) {

				//-> remove old value
				tokens.remove(min);

				// add new value
				tokens.add(newToken);
			}
		}
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#iterator()
	 */
	public Iterator iterator() {
		Iterator it= getMap().iterator();

		while (it.hasNext()) {
			Token token= (Token) it.next();
			double value= getMap().getOccurences(token);

			insert(token, value);
		}

		return tokens.iterator();
	}

}
