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
package org.macchiato;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.macchiato.db.DBToken;
import org.macchiato.db.DBTokenComparator;
import org.macchiato.db.FrequencyDB;
import org.macchiato.db.MD5Sum;

/**
 * Wrapper class adding spam filter specific logic to the database.
 * <p>
 * Following the composite pattern, it wraps a frequency database instance.
 * <p>
 * 
 * @author fdietz
 */
public class DBWrapper implements FrequencyDB {

	static final long OneDay = 24 * 60 * 60 * 1000;

	static TimeZone localTimeZone = TimeZone.getDefault();

	private FrequencyDB db;

	/**
	 *  
	 */
	public DBWrapper(FrequencyDB db) {
		super();

		this.db = db;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addMessageMD5Sum(byte[])
	 */
	public void addMessageMD5Sum(MD5Sum md5sum) {

		db.addMessageMD5Sum(md5sum);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addToken(org.macchiato.db.DBToken)
	 */
	public void addToken(DBToken token) {

		if (db.contains(token.getWord())) {
			// token already exists

			// increase the bad/good count
			DBToken t = getToken(token.getWord());
			t.incrBadCount(token.getBadCount());
			t.incrGoodCount(token.getGoodCount());

			// update timestamp
			t.setTimeStamp(new Date().getTime());
		} else {

			// create timestamp
			token.setTimeStamp(new Date().getTime());

			// add new token
			db.addToken(token);

		}

	}

	/**
	 * Get difference in days between today and t.
	 * 
	 * @param t
	 *            date in long format
	 * @return days difference
	 */
	private int getLocalDaysDiff(long t) {
		return (int) (((System.currentTimeMillis() + localTimeZone
				.getRawOffset()) - (((t + localTimeZone.getRawOffset()) / OneDay) * OneDay)) / OneDay);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#cleanupDB(int)
	 */
	public void cleanupDB(int threshold) {

		// only cleanup DB if size > threshold
		if (tokenCount() < threshold)
			return;

		// get all DB entries
		Set set = db.entrySet();
		// create new list of all entries
		List l = new ArrayList(set.size());
		Iterator it = set.iterator();
		while (it.hasNext()) {
			DBToken t = (DBToken) ((Map.Entry) it.next()).getValue();
			l.add(t);
		}

		// sort all entries based on age, using the timestamp value
		Collections.sort(l, new DBTokenComparator());

		// DB-size - threshold
		int partition = tokenCount() - threshold;

		// remove obsolete partition of DB
		for (int i = 0; i < partition; i++) {
			// get token with index i
			DBToken token = (DBToken) l.get(i);
			// remove token from DB
			db.removeToken(token.getWord());
		}

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#contains(java.lang.String)
	 */
	public boolean contains(String word) {

		return db.contains(word);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getBadMessageCount()
	 */
	public int getBadMessageCount() {

		return db.getBadMessageCount();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getGoodMessageCount()
	 */
	public int getGoodMessageCount() {

		return db.getGoodMessageCount();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageCount()
	 */
	public int getMessageCount() {

		return db.getMessageCount();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getToken(java.lang.String)
	 */
	public DBToken getToken(String s) {

		return db.getToken(s);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrBadMessageCount(int)
	 */
	public void incrBadMessageCount(int incr) {

		db.incrBadMessageCount(incr);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrGoodMessageCount(int)
	 */
	public void incrGoodMessageCount(int incr) {

		db.incrGoodMessageCount(incr);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumCount()
	 */
	public int md5SumCount() {

		return db.md5SumCount();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#MD5SumExists(byte[])
	 */
	public boolean MD5SumExists(byte[] md5sum) {

		return db.MD5SumExists(md5sum);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumIterator()
	 */
	public Iterator md5SumIterator() {

		return db.md5SumIterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#printDebug()
	 */
	public void printDebug() {

		db.printDebug();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#removeToken(java.lang.String)
	 */
	public void removeToken(String s) {

		db.removeToken(s);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setBadMessageCount(int)
	 */
	public void setBadMessageCount(int count) {

		db.setBadMessageCount(count);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setGoodMessageCount(int)
	 */
	public void setGoodMessageCount(int count) {

		db.setGoodMessageCount(count);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#tokenCount()
	 */
	public int tokenCount() {

		return db.tokenCount();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#tokenKeyIterator()
	 */
	public Iterator tokenKeyIterator() {

		return db.tokenKeyIterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageMD5Sum(byte[])
	 */
	public MD5Sum getMessageMD5Sum(byte[] md5sum) {
		return db.getMessageMD5Sum(md5sum);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#tokenIterator()
	 */
	public Iterator tokenIterator() {
		return db.tokenIterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#entrySet()
	 */
	public Set entrySet() {
		return db.entrySet();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#close()
	 */
	public void close() {
		db.close();

	}
}