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

import org.macchiato.maps.OccurencesMap;
import org.macchiato.tokenizer.Token;

/**
 * 
 *
 * @author fdietz
 */
public abstract class AbstractOccurencesFilter implements OccurencesMap {

	private OccurencesMap map;

	public AbstractOccurencesFilter(OccurencesMap map) {
		this.map= map;
	}

	public abstract Iterator iterator();

	/**
	 * @return
	 */
	public OccurencesMap getMap() {
		return map;
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#addToken(java.lang.String)
	 */
	public void addToken(String data) {
		getMap().addToken(data);

	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#addToken(org.macchiato.tokenizer.Token)
	 */
	public void addToken(Token token) {
		getMap().addToken(token);

	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#getOccurences(java.lang.String)
	 */
	public int getOccurences(String tokenString) {
		return getMap().getOccurences(tokenString);
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#getOccurences(org.macchiato.tokenizer.Token)
	 */
	public int getOccurences(Token token) {
		return getMap().getOccurences(token);
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#getTotalOccurencesCount()
	 */
	public int getTotalOccurencesCount() {
		return getMap().getTotalOccurencesCount();
	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#printResults()
	 */
	public void printResults() {
		Iterator it= iterator();

		while (it.hasNext()) {
			Token key= (Token) it.next();
			int count= getOccurences(key);

			System.out.println("Token[" + key.getString() + "]=" + count);
		}

	}

	/**
	 * @see org.macchiato.maps.OccurencesMap#remove(org.macchiato.tokenizer.Token)
	 */
	public void remove(Token token) {
		getMap().remove(token);

	}

}
