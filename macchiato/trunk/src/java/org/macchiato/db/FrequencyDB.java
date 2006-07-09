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

import java.util.Iterator;
import java.util.Set;

/**
 * Database abstraction layer for a token db.
 *
 * @author fdietz
 */
public interface FrequencyDB {

	/**
	 * Get token from database.
	 * 
	 * @param s		token word
	 * 
	 * @return		db token
	 */
	DBToken getToken(String s);

	/**
	 * Add token to database.
	 * 
	 * @param token		new db token
	 */
	void addToken(DBToken token);

	/**
	 * Remove token of database.
	 * 
	 * @param s		token word
	 */
	void removeToken(String s);

	/**
	 * Get total count of all messages.
	 * 
	 * @return		message count
	 */
	int getMessageCount();

	/**
	 * Get total count of bad messages.
	 * 
	 * @return		bad message count
	 */
	int getBadMessageCount();

	/**
	 * Set total count of bad messages.
	 * 
	 * @param count		bad message count
	 */
	void setBadMessageCount(int count);

	void incrBadMessageCount(int incr);

	/**
	 * Get total count of good messages.
	 * 
	 * @return		good message count
	 */
	int getGoodMessageCount();

	void incrGoodMessageCount(int incr);

	/**
	 * Set total count of good messages.
	 * 
	 * @param count		good message count
	 */
	void setGoodMessageCount(int count);

	/**
	 * Get iterator of all keys.
	 * 
	 * @return		key iterator
	 */
	Iterator tokenKeyIterator();

	/**
	 * Get iterator of all tokens.
	 * 
	 * @return		token value iterator.
	 */
	Iterator tokenIterator();
	
	/**
	 * Get Set of all tokens.
	 * 
	 * @return		set of tokens
	 */
	Set entrySet();
	
	/**
	 * Get count of elements.
	 * @return
	 */
	int tokenCount();

	/**
	 * Add md5 sum of message to database.
	 * 
	 * @param md5sum		md5sum
	 */
	void addMessageMD5Sum(MD5Sum md5sum);
	
	/**
	 * Get MD5sum of message.
	 * 
	 * @param md5sum		MD5 sum identifying message
	 * @return				MD5Sum object
	 */
	MD5Sum getMessageMD5Sum(byte[] md5sum);

	/**
	 * Check if MD5 exists in database.
	 * 
	 * @return	true, if already in database. False, otherwise.
	 */
	boolean MD5SumExists(byte[] md5sum);

	/**
		 * Get iterator of all md5 sums.
		 * 
		 * @return		key iterator
		 */
	Iterator md5SumIterator();

	/**
	 * Check if tokens exists in database.
	 * 
	 * @param word		word to identify the token
	 * @return			true, if token exists. False, otherwise.
	 */
	boolean contains(String word);
	
	/**
	 * Get total count of message md5 sums.
	 * 
	 * @return		total count
	 */
	int md5SumCount();

	/**
	 * Cleanup database, removing all tokens with occurences
	 * less than the given threshold, which are older than the
	 * age.
	 * @param threshold 
	 * @param date			age of token
	 */
	void cleanupDB(int threshold);

	public void printDebug();
	
	public void close();
}
