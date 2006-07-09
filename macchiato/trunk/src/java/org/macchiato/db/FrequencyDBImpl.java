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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * FrequencyDB implementation using a HashMap as backend.
 * 
 * @author fdietz
 */
public class FrequencyDBImpl implements FrequencyDB {

	
	/**
	 * Map for storing tokens.
	 * <p>
	 * Map key is the token word, value the {@link DBToken}
	 */
	private Map tokenMap;

	/**
	 * Map for storing MD5 sums of message.
	 * <p>
	 * Map key is the md5 sum (byte[]), value the {@link MD5Sum}
	 */
	private Map messageMap;

	/**
	 * total count of bad messages
	 */
	private int badMessageCount;

	/**
	 * total count of good messages
	 */
	private int goodMessageCount;

	/**
	 * Default Constructor
	 */
	public FrequencyDBImpl() {
		super();

		// init token map
		tokenMap = new HashMap();

		// init md5 sum message map
		messageMap = new HashMap();

		badMessageCount = 0;

		goodMessageCount = 0;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getBadMessageCount()
	 */
	public int getBadMessageCount() {
		return badMessageCount;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getGoodMessageCount()
	 */
	public int getGoodMessageCount() {
		return goodMessageCount;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageCount()
	 */
	public int getMessageCount() {

		return goodMessageCount + badMessageCount;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getToken(java.lang.String)
	 */
	public DBToken getToken(String s) {

		return (DBToken) tokenMap.get(s);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#removeToken(java.lang.String)
	 */
	public void removeToken(String s) {
		tokenMap.remove(s);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setBadMessageCount(int)
	 */
	public void setBadMessageCount(int count) {
		badMessageCount = count;

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setGoodMessageCount(int)
	 */
	public void setGoodMessageCount(int count) {
		goodMessageCount = count;

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addToken(org.macchiato.db.DBToken)
	 */
	public void addToken(DBToken token) {

		// add new token
		tokenMap.put(token.getWord(), token);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#iterator()
	 */
	public Iterator tokenKeyIterator() {
		return tokenMap.keySet().iterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#count()
	 */
	public int tokenCount() {
		return tokenMap.size();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#contains(java.lang.String)
	 */
	public boolean contains(String word) {
		if (tokenMap.containsKey(word))
			return true;

		return false;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#printDebug()
	 */
	public void printDebug() {
		System.out.println("bad message count=" + getBadMessageCount());
		System.out.println("good message count=" + getGoodMessageCount());
		System.out.println("token count=" + tokenCount());

		Iterator it = tokenKeyIterator();
		while (it.hasNext()) {
			String word = (String) it.next();
			DBToken token = (DBToken) getToken(word);
			int bad = token.getBadCount();
			int good = token.getGoodCount();
				token.printDebug();

			/*
			 * float score= new GrahamFilter().scoreTerm( token.getGoodCount(),
			 * token.getBadCount(), getGoodMessageCount(),
			 * getBadMessageCount()); System.out.println(" -score=" + score);
			 */
		}

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrBadMessageCount(int)
	 */
	public void incrBadMessageCount(int incr) {
		badMessageCount += incr;

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrGoodMessageCount(int)
	 */
	public void incrGoodMessageCount(int incr) {
		goodMessageCount += incr;

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addMessageMD5Sum(byte[])
	 */
	public void addMessageMD5Sum(MD5Sum md5sum) {
		messageMap.put(md5sum.getMd5sum(), md5sum);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#MD5SumExists(byte[])
	 */
	public boolean MD5SumExists(byte[] md5sum) {
		if (messageMap.containsKey(md5sum))
			return true;

		return false;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#cleanupDB(int)
	 */
	public void cleanupDB(int threshold) {
		// DBWrapper implements this instead
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumIterator()
	 */
	public Iterator md5SumIterator() {
		return messageMap.keySet().iterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumCount()
	 */
	public int md5SumCount() {
		return messageMap.size();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageMD5Sum(byte[])
	 */
	public MD5Sum getMessageMD5Sum(byte[] md5sum) {
		return (MD5Sum) messageMap.get(md5sum);
	}
	
	/**
	 * @see org.macchiato.db.FrequencyDB#tokenIterator()
	 */
	public Iterator tokenIterator() {
		return tokenMap.entrySet().iterator();
	}
	
	/**
	 * @see org.macchiato.db.FrequencyDB#entrySet()
	 */
	public Set entrySet() {
		return tokenMap.entrySet();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#close()
	 */
	public void close() {
		// don't do anything here
		
	}
}