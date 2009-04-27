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
package org.macchiato.maps;

import org.junit.Assert;
import org.junit.Test;
import org.macchiato.tokenizer.Token;

/**
 * @author fdietz
 */
public class ProbabilityMapFactoryTest {

    @Test
    public void test() {
        OccurencesMap spam = new OccurencesMapImpl();
        spam.addToken("snake");
        spam.addToken("snake");
        spam.addToken("snake");
        spam.addToken("snake");
        spam.addToken("snake");

        spam.addToken("tiger");
        spam.addToken("dragon");
        spam.addToken("camel");
        spam.addToken("bird");
        spam.addToken("fly");

        OccurencesMap ham = new OccurencesMapImpl();
        ham.addToken("Peter");
        ham.addToken("Peter");
        ham.addToken("Peter");
        ham.addToken("Peter");
        ham.addToken("Peter");

        ham.addToken("snake");
        ham.addToken("snake");

        ham.addToken("tiger");
        spam.addToken("bird");

        ProbabilityMapImpl resultMap = (ProbabilityMapImpl) ProbabilityMapFactory.createProbabilityMap(spam,
                ham);
        resultMap.printResults();

        // note that "snake" has 5 occurences as spam, 2 occurences as ham
        // -> 5 / 2+5 = 5 / 7 = 0.71
        float result = resultMap.getProbability(new Token("snake"));
        Assert.assertEquals(0.71, result, 0.01);

        // "fly" has only 1 occurences as spam, 0 as ham
        Assert.assertEquals(1.0, resultMap.getProbability(new Token("fly")), 0.01);

        // "Peter" has multiple occurences as ham, 0 as spam
        Assert.assertEquals(0.0, resultMap.getProbability(new Token("Peter")), 0.01);
    }
}
