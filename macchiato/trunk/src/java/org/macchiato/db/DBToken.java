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
package org.macchiato.db;



/**
 * A token data item used in the database.
 *
 * @author fdietz
 */
public interface DBToken {

	/**
	 * Get string identifying this token.
	 * 
	 * @return		token word
	 */
	String getWord();
	
	/**
	 * Get total count of token in good message set.
	 * 
	 * @return		total good count
	 */
	int getGoodCount();
	
	/**
	 * Set total count of token in good message set.
	 * 
	 * @param count		total good count
	 */
	void setGoodCount( int count);
	
	void incrGoodCount(int incr);
	
	/**
	 * Get total count of token in bad message set.
	 * 
	 * @return		total bad count
	 */
	int getBadCount();
	
	/**
	 * Set total count of token in bad message set.
	 * 
	 * @param count		total bad count
	 */
	void setBadCount(int count);
	
	void incrBadCount( int incr);
	
	/**
	 * Get score of token.
	 * 
	 * @return		token score in percentage
	 */
	float getScore();
	
	/**
	 * Set score of token.
	 * 
	 * @param score		token score in percentage
	 */
	void setScore(float score);
	
	long getTimeStamp();
	
	void setTimeStamp(long date);
	
	void printDebug();
}
