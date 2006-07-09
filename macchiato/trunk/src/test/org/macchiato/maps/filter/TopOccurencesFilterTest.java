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

import junit.framework.TestCase;

import org.macchiato.log.MacchiatoLogger;
import org.macchiato.maps.OccurencesMap;
import org.macchiato.maps.OccurencesMapImpl;
import org.macchiato.tokenizer.Token;

/**
 * 
 *
 * @author fdietz
 */
public class TopOccurencesFilterTest extends TestCase {

	/**
	 * Constructor for TopOccurencesFilterTest.
	 * @param arg0
	 */
	public TopOccurencesFilterTest(String arg0) {
		super(arg0);
	}
	
	public void test() {
		OccurencesMap map = new OccurencesMapImpl();
		map.addToken("snake");
		map.addToken("snake");
		map.addToken("snake");
		map.addToken("snake");
		map.addToken("snake");
		
		map.addToken("tiger");
		map.addToken("tiger");
		map.addToken("tiger");
		map.addToken("dragon");
		map.addToken("dragon");
		map.addToken("camel");
		map.addToken("bird");
		map.addToken("fly");
		
		// find the TOP 3 elements, with the highest occurence
		TopOccurencesFilter filter = new TopOccurencesFilter(map, 3);
		
		Iterator it = filter.iterator();
		Token snake = (Token) it.next();
		MacchiatoLogger.log.info("snake="+snake.getString());
		Token tiger = (Token) it.next();
		MacchiatoLogger.log.info("tiger="+tiger.getString());
		Token dragon = (Token) it.next();
		MacchiatoLogger.log.info("dragon="+dragon.getString());
		
		assertEquals(5, map.getOccurences(snake));
		assertEquals(3, map.getOccurences(tiger));
		assertEquals(2, map.getOccurences(dragon));
	}

}
