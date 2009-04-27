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
package org.macchiato.filter;

import org.junit.Assert;
import org.junit.Test;
import org.macchiato.log.MacchiatoLogger;

/**
 * 
 *
 * @author fdietz
 */
public class GrahamFilterTest {

    @Test
	public void test() {
		float score = new GrahamFilter().scoreTerm(0, 1, 382, 587);
		
		MacchiatoLogger.log.info("score="+score);
		
		Assert.assertEquals(0.4f,score, 0.1);
		
		float score2 = new GrahamFilter().scoreTerm(3, 0, 382, 587);
		
		MacchiatoLogger.log.info("score2="+score2);
	}

}
