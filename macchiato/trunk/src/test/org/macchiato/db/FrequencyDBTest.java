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

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 *
 * @author fdietz
 */
public class FrequencyDBTest{

    @Test
	public void test() {
		FrequencyDBImpl db= new FrequencyDBImpl();

		DBToken token= new DBTokenImpl("frog");
		token.setBadCount(5);
		token.setGoodCount(0);
		db.addToken(token);
		
		token= db.getToken("frog");
		Assert.assertEquals(5, token.getBadCount());
		Assert.assertEquals(0, token.getGoodCount());
		
		
		token= new DBTokenImpl("frog");
		token.setBadCount(1);
		token.setGoodCount(2);
		db.addToken(token);
		
		token= new DBTokenImpl("peter");
		token.setBadCount(2);
		token.setGoodCount(5);
		db.addToken(token);

		token= db.getToken("frog");
		Assert.assertEquals(1, token.getBadCount());
		Assert.assertEquals(2, token.getGoodCount());
		

		token= db.getToken("peter");
		Assert.assertEquals(2, token.getBadCount());
		Assert.assertEquals(5, token.getGoodCount());
	}

}
