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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.macchiato;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.macchiato.classifier.CombineProbabilities;
import org.macchiato.db.DBToken;
import org.macchiato.db.DBTokenImpl;
import org.macchiato.db.FrequencyDB;
import org.macchiato.db.FrequencyDBImpl;
import org.macchiato.filter.Filter;
import org.macchiato.filter.GrahamFilter;
import org.macchiato.maps.OccurencesMap;
import org.macchiato.maps.ProbabilityMap;
import org.macchiato.tokenizer.Token;

/**
 * Implementation of a spam filter using Paul Grahams's approach for email
 * classification.
 * <p>
 * TODO: document all modifications and improvements here
 * 
 * @author fdietz
 */
public class SpamFilterImpl implements SpamFilter {

	static final Logger log = Logger.getLogger("org.macchiato");
	
    private FrequencyDB db;

    private Filter filter;

    public SpamFilterImpl() {

        db = new DBWrapper(new FrequencyDBImpl());

        filter = new GrahamFilter();
    }

    public SpamFilterImpl(FrequencyDB db) {
        this.db = db;

        filter = new GrahamFilter();
    }

    /**
     * @see org.macchiato.SpamFilter#scoreMessage(org.macchiato.Message)
     */
    public float scoreMessage(Message message, ProbabilityMap additional) {

        // for each token
        OccurencesMap map = SpamFilterHelper.createOccurencesMap(message);

        // create list of probabilities
        List p = new Vector();
        Iterator it = map.iterator();
        while (it.hasNext()) {
            Token token = (Token) it.next();
            int occurence = map.getOccurences(token);

            DBToken dbToken = db.getToken(token.getString());
            if (dbToken == null) {
                p.add(new Float(0.5));

                continue;
            }

            float score = filter.scoreTerm(dbToken.getGoodCount(), dbToken
                    .getBadCount(), db.getGoodMessageCount(), db
                    .getBadMessageCount());

            
            log.finest("token="+token.getString());
            log.finest("good="+dbToken.getGoodCount());
            log.finest("bad="+dbToken.getBadCount());
              
              log.fine("->score="+score);
             

            p.add(new Float(score));
        }

        // copy all handcrafted additional rules probabilities into vector
        if (additional != null) {
            it = additional.iterator();
            while (it.hasNext()) {
                Token token = (Token) it.next();
                p.add(new Float(additional.getProbability(token)));
            }
        }

        // filter list of probabilities
        List filteredList = filterProbabilities(p);

        // combine all resulting probabilities
        float score = CombineProbabilities.combine(filteredList);

        return score;
    }

    /**
     * Filter out the 15 most interesting tokens. Where interesting means
     * measured by how far their spam probability is from a neutral <b>0.5 </b>
     * percentage.
     * 
     * @param p
     *            list of probabilities
     * @return filtered list
     */
    public List filterProbabilities(List p) {
        if (p.size() <= 16) return p;

        float[] f = new float[p.size()];

        for (int i = 0; i < f.length; i++) {
            Float helper = (Float) p.get(i);
            f[i] = helper.floatValue();
        }

        Arrays.sort(f);

        List list = new ArrayList();
        for (int i = 0; i < 8; i++) {
            float j1 = f[f.length - 1 - i];
            if (j1 != 0.5) list.add(new Float(j1));

            float j2 = f[i];
            if (j2 != 0.5) list.add(new Float(j2));
        }

        // filter out 0.0 values
        for (int i = 0; i < list.size(); i++) {
            Float value = (Float) list.get(i);
            if (value.floatValue() == 0.0f) list.remove(i);
        }

        return list;

    }

    /**
     * @see org.macchiato.SpamFilter#trainMessageAsHam(org.macchiato.Message)
     */
    public void trainMessageAsHam(Message message) {
        OccurencesMap map = SpamFilterHelper.createOccurencesMap(message);

        // for each token
        Iterator it = map.iterator();
        while (it.hasNext()) {
            Token key = (Token) it.next();
            int occurences = map.getOccurences(key);

            DBToken token = new DBTokenImpl(key.getString());
            token.setGoodCount(occurences);

            db.addToken(token);
        }

        db.incrGoodMessageCount(1);

    }

    /**
     * @see org.macchiato.SpamFilter#trainMessageAsSpam(org.macchiato.Message)
     */
    public void trainMessageAsSpam(Message message) {
        OccurencesMap map = SpamFilterHelper.createOccurencesMap(message);

        // for each token
        Iterator it = map.iterator();
        while (it.hasNext()) {
            Token key = (Token) it.next();
            int occurences = map.getOccurences(key);

            DBToken token = new DBTokenImpl(key.getString());
            token.setBadCount(occurences);

            db.addToken(token);
        }

        db.incrBadMessageCount(1);
    }

    /**
     * Set new frequency db.
     * 
     * @param frequencyDB
     *            frequency db
     */
    public void setDb(FrequencyDB frequencyDB) {
        this.db = frequencyDB;
    }

    /**
     * @return
     */
    public FrequencyDB getDB() {
        return db;
    }

    /**
     * @see org.macchiato.SpamFilter#correctMessageAsHam(org.macchiato.Message)
     */
    public void correctMessageAsHam(Message message) {
        OccurencesMap map = SpamFilterHelper.createOccurencesMap(message);

        // for each token
        Iterator it = map.iterator();
        while (it.hasNext()) {
            Token key = (Token) it.next();
            int occurences = map.getOccurences(key);

            // double occurences, because of correction of wrong value
            occurences = occurences * 2;

            DBToken token = new DBTokenImpl(key.getString());
            token.setGoodCount(occurences);

            db.addToken(token);
        }

    }

    /**
     * @see org.macchiato.SpamFilter#correctMessageAsSpam(org.macchiato.Message)
     */
    public void correctMessageAsSpam(Message message) {
        OccurencesMap map = SpamFilterHelper.createOccurencesMap(message);

        // for each token
        Iterator it = map.iterator();
        while (it.hasNext()) {
            Token key = (Token) it.next();
            int occurences = map.getOccurences(key);

            // double occurences, because of correction of wrong value
            occurences = occurences * 2;

            DBToken token = new DBTokenImpl(key.getString());
            token.setBadCount(occurences);

            db.addToken(token);
        }

    }
}
