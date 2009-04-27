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
package org.macchiato.classifier;

import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.macchiato.log.MacchiatoLogger;

/**
 * @author fdietz
 */
public class CombineProbabilitiesTest{

    @Test
	public void test() {
		List list= new Vector();
		list.add(new Float("0.6"));
		list.add(new Float("0.72"));

		float result= CombineProbabilities.combine(list);
		Assert.assertEquals(0.794f, result, 0.01f);
	}

    @Test
	public void test2() {
		List list= new Vector();
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));
		list.add(new Float("0.9"));

		float result= CombineProbabilities.combine(list);
		System.out.println("result=" + result);

		Assert.assertEquals(1.0f, result, 0.01f);
	}

    @Test
	public void test3() {
		List list= new Vector();
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));
		list.add(new Float("0.01"));

		float result= CombineProbabilities.combine(list);
		System.out.println("result=" + result);

		Assert.assertEquals(0.0f, result, 0.01f);
	}


    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        new MacchiatoLogger();
    }
}
