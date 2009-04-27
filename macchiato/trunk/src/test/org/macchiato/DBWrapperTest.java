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
package org.macchiato;

import org.junit.Assert;
import org.junit.Test;
import org.macchiato.db.DBToken;
import org.macchiato.db.DBTokenImpl;
import org.macchiato.db.FrequencyDB;
import org.macchiato.db.FrequencyDBImpl;

/**
 * @author fdietz
 *  
 */
public class DBWrapperTest {

    @Test
	public void testCleanupDB() {
		FrequencyDB db = new FrequencyDBImpl();
		DBWrapper wrapper = new DBWrapper(db);

		add(wrapper, "frog", 1, 1);
		sleep();
		add(wrapper, "frog", 1, 1);
		sleep();
		add(wrapper, "Joe", 1, 1);
		sleep();
		add(wrapper, "peter", 2, 5);
		sleep();
		add(wrapper, "frog2", 5, 3);
		sleep();
		add(wrapper, "frog2", 5, 0);
		add(wrapper, "frog3", 5, 3);
		add(wrapper, "frog4", 5, 0);
		add(wrapper, "frog5", 5, 2);
		add(wrapper, "frog6", 5, 0);
		add(wrapper, "frog7", 5, 1);
		add(wrapper, "frog8", 5, 0);

		// cleanup DB
		wrapper.cleanupDB(9);
		
		// oldest token should be removed
		Assert.assertEquals("db size", 9, wrapper.tokenCount());

		// "frog" token should be removed
		Assert.assertEquals("shouldn't contain", false, wrapper.contains("frog"));
	}

	/**
	 * Sleep for 2 seconds
	 *  
	 */
	private static void sleep() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void add(FrequencyDB db, String word, int bad, int good) {
		DBToken token = new DBTokenImpl(word);
		token.setBadCount(bad);
		token.setGoodCount(good);
		db.addToken(token);
	}

}

