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

import java.io.Serializable;



/**
 * Light-weight token object storing token properties. 
 *
 * @author fdietz
 */
public class DBTokenImpl implements DBToken, Serializable {

	private String word;
	private int goodCount;
	private int badCount;
	private float score;
	private long timeStamp;

	/**
	 * @param s
	 */
	public DBTokenImpl(String s) {
		this.word= s;

		goodCount= 0;
		badCount= 0;
		score= 0;
	}

	/**
	 * @see org.macchiato.db.DBToken#getBadCount()
	 */
	public int getBadCount() {
		return badCount;
	}

	/**
	 * @see org.macchiato.db.DBToken#getGoodCount()
	 */
	public int getGoodCount() {
		return goodCount;
	}

	/**
	 * @see org.macchiato.db.DBToken#getScore()
	 */
	public float getScore() {
		return score;
	}

	/**
	 * @see org.macchiato.db.DBToken#setBadCount(int)
	 */
	public void setBadCount(int count) {
		badCount= count;

	}

	/**
	 * @see org.macchiato.db.DBToken#setGoodCount(int)
	 */
	public void setGoodCount(int count) {
		goodCount= count;

	}

	/**
	 * @see org.macchiato.db.DBToken#setScore(float)
	 */
	public void setScore(float score) {
		this.score= score;

	}

	/**
	 * @see org.macchiato.db.DBToken#getWord()
	 */
	public String getWord() {
		return word;
	}

	/**
	 * A token is equal another token if both contain the same string.
	 */
	public boolean equals(Object arg0) {
		if (getWord().equals(((DBToken) arg0).getWord()))
			return true;

		return false;
	}

	/**
	 * Token shares its hashCode with its underlying String.
	 * 
	 */
	public int hashCode() {

		return getWord().hashCode();
	}
	
	public void printDebug() {
		System.out.println("word="+getWord());
		System.out.println(" -bad Count="+getBadCount());
		System.out.println(" -good Count="+getGoodCount());
		//System.out.println("score="+getScore());
	}


	/**
	 * @see org.macchiato.db.DBToken#incrBadCount(int)
	 */
	public void incrBadCount(int incr) {
		badCount+=incr;

	}

	/**
	 * @see org.macchiato.db.DBToken#incrGoodCount(int)
	 */
	public void incrGoodCount(int incr) {
		goodCount+=incr;

	}

	/**
	 * @see org.macchiato.db.DBToken#getTimeStamp()
	 */
	public long getTimeStamp() {
	
		return timeStamp;
	}

	/**
	 * @see org.macchiato.db.DBToken#setTimeStamp(long)
	 */
	public void setTimeStamp(long date) {
		timeStamp = date;

	}

}
