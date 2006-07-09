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
 * Only allows tokens with an occurence of at least <b>bound</b>.
 *
 * @author fdietz
 */
public class LowerBoundOccurencesFilter extends AbstractOccurencesFilter {

	private int bound;
	
	/**
	 * @param map
	 */
	public LowerBoundOccurencesFilter(OccurencesMap map, int bound) {
		super(map);
		
		this.bound = bound;
		
	}

	/**
	 * @see org.macchiato.statistics.AbstractOccurencesFilter#iterator()
	 */
	public Iterator iterator() {
		Vector v = new Vector();
		
		Iterator it = getMap().iterator();
		while ( it.hasNext()) {
			Token token = (Token) it.next();
			int occurence = getMap().getOccurences(token);
			if ( occurence > bound ) v.add(token);
		}
		
		return v.iterator();
	}

}
