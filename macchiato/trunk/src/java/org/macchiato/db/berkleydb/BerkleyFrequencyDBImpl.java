// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.macchiato.db.berkleydb;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.macchiato.db.DBToken;
import org.macchiato.db.DBTokenImpl;
import org.macchiato.db.FrequencyDB;
import org.macchiato.db.MD5Sum;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;

/**
 * Database using BerkleyDB Java Edition as backend.
 * <p>
 * Using three databases currently. One for collecting tokens, one for collecting
 * messages and one for counting the total number of messages. The last one will most 
 * probably be removed in the future.
 * <p>
 * Making use of the collection classes of BerkleyDB Java Edition. This makes 
 * everything as simple as accessing a Map interface.
 * 
 * @author fdietz
 */
public class BerkleyFrequencyDBImpl implements FrequencyDB {

	private static final String GOOD = "GOOD";
	private static final String BAD = "BAD";
	
	private File path;

	// Encapsulates the environment and databases.
	private static MyDBEnv env = new MyDBEnv();

	//	Need a serial binding for the token data
	EntryBinding tokenDataBinding;

	// Need a serial binding for the token key
	EntryBinding tokenKeyBinding;

	// token map
	private StoredMap tokenMap;

	//	Need a serial binding for the message data
	EntryBinding messageDataBinding;

	// Need a serial binding for the message key
	EntryBinding messageKeyBinding;

	// message map
	private StoredMap messageMap;

	//	Need a serial binding for the total count key
	EntryBinding countDataBinding;

	// Need a serial binding for the total count data
	EntryBinding countKeyBinding;

	// total count map
	private StoredMap countMap;

	/**
	 *  
	 */
	public BerkleyFrequencyDBImpl() {
		this(new File("spamdb"));

	}

	/**
	 *  
	 */
	public BerkleyFrequencyDBImpl(File path) {
		super();

		this.path = path;

		try {
			// create directory
			if (!path.exists())
				path.mkdir();

			// path to the environment home
			// is this environment read-only?
			env.setup(path, false);

			// create token key binding
			tokenKeyBinding = new SerialBinding(env.getClassCatalog(),
					String.class);

			// create token data binding
			tokenDataBinding = new SerialBinding(env.getClassCatalog(),
					DBTokenImpl.class);

			// create message map
			tokenMap = new StoredMap(env.getTokenDB(), tokenKeyBinding,
					tokenDataBinding, true);

			// create message key binding
			messageKeyBinding = new SerialBinding(env.getClassCatalog(),
					String.class);

			// create message data binding
			messageDataBinding = new SerialBinding(env.getClassCatalog(),
					MD5Sum.class);

			// create message map
			messageMap = new StoredMap(env.getTokenDB(), messageKeyBinding,
					messageDataBinding, true);

			//			 create total count key binding
			countKeyBinding = new SerialBinding(env.getClassCatalog(),
					String.class);

			// create total count data binding
			countDataBinding = new SerialBinding(env.getClassCatalog(),
					Integer.class);

			// create total count map
			countMap = new StoredMap(env.getTokenDB(), countKeyBinding,
					countDataBinding, true);

			// init default values
			if ( !countMap.containsKey(BerkleyFrequencyDBImpl.GOOD)) 
				countMap.put(BerkleyFrequencyDBImpl.GOOD,new Integer(0));
			if ( !countMap.containsKey(BerkleyFrequencyDBImpl.BAD)) 
				countMap.put(BerkleyFrequencyDBImpl.BAD,new Integer(0));
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

	}

	public void close() {
		env.close();
	}

	public MyDBEnv getDBEnvironment() {
		return env;
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getToken(java.lang.String)
	 */
	public DBToken getToken(String s) {
		return (DBToken) tokenMap.get(s);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addToken(org.macchiato.db.DBToken)
	 */
	public void addToken(DBToken token) {
		tokenMap.put(token.getWord(), token);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#removeToken(java.lang.String)
	 */
	public void removeToken(String word) {
		tokenMap.remove(word);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageCount()
	 */
	public int getMessageCount() {
		return messageMap.size();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getBadMessageCount()
	 */
	public int getBadMessageCount() {
		return ((Integer)countMap.get(BerkleyFrequencyDBImpl.BAD)).intValue();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setBadMessageCount(int)
	 */
	public void setBadMessageCount(int count) {
		countMap.put(BerkleyFrequencyDBImpl.BAD, new Integer(count));

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrBadMessageCount(int)
	 */
	public void incrBadMessageCount(int incr) {
		setBadMessageCount(getBadMessageCount()+incr);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getGoodMessageCount()
	 */
	public int getGoodMessageCount() {
		return ((Integer)countMap.get(BerkleyFrequencyDBImpl.GOOD)).intValue();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#incrGoodMessageCount(int)
	 */
	public void incrGoodMessageCount(int incr) {
		setGoodMessageCount(getGoodMessageCount()+incr);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#setGoodMessageCount(int)
	 */
	public void setGoodMessageCount(int count) {
		countMap.put(BerkleyFrequencyDBImpl.GOOD, new Integer(count));

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#tokenKeyIterator()
	 */
	public Iterator tokenKeyIterator() {
		return tokenMap.keySet().iterator();
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
	 * @see org.macchiato.db.FrequencyDB#tokenCount()
	 */
	public int tokenCount() {
		return tokenMap.size();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#addMessageMD5Sum(org.macchiato.db.MD5Sum)
	 */
	public void addMessageMD5Sum(MD5Sum md5sum) {
		messageMap.put(new String(md5sum.getMd5sum()), md5sum);

	}

	/**
	 * @see org.macchiato.db.FrequencyDB#getMessageMD5Sum(byte[])
	 */
	public MD5Sum getMessageMD5Sum(byte[] md5sum) {
		return (MD5Sum) messageMap.get(new String(md5sum));
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#MD5SumExists(byte[])
	 */
	public boolean MD5SumExists(byte[] md5sum) {
		return messageMap.containsKey(new String(md5sum));
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumIterator()
	 */
	public Iterator md5SumIterator() {
		return messageMap.keySet().iterator();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#contains(java.lang.String)
	 */
	public boolean contains(String word) {
		return tokenMap.containsKey(word);
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#md5SumCount()
	 */
	public int md5SumCount() {
		return messageMap.size();
	}

	/**
	 * @see org.macchiato.db.FrequencyDB#cleanupDB(int)
	 */
	public void cleanupDB(int threshold) {
		//		 DBWrapper implements this instead

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

}