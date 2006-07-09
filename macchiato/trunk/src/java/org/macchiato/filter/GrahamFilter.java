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

import java.util.logging.Logger;

/**
 * Filter using a naive bayesian filtering approach. Read Paul Graham's 
 * <a href="http://www.paulgraham.com/spam.html">introduction</a>.
 * <p>
 * In Scheme this would look like:
 * <verbatim>
 * (max .01 (min .99 (float 
 *                          (/ (min 1 (/ b nbad)) 
 *                          (+ (min 1 (/ g ngood)) 
 *                             (min 1 (/ b nbad)))
 *                           )
 *                   )
 *           )
 * )
 * </verbatim>
 *
 * @author fdietz
 */
public class GrahamFilter implements Filter {

	static final Logger log = Logger.getLogger("org.macchiato.filter");
	
	/**
	 * new words added to database get this default score
	 */
	private static float NEW_WORD_SCORE= 0.4f;

	/**
	 * minimum number of word occurences
	 */
	private static int MIN_WORD_COUNT= 4;

	/**
	 * minimum possible probability
	 */
	private static float MIN_PROBABILITY= 0.01f;

	/**
	 * maximium possible probability
	 */
	private static float MAX_PROBABILITY= 0.99f;

	/**
	 * maths-safe zero
	 */
	private static float NEARLY_ZERO= 0.0000001f;

	private float newWordScore;
	private int minWordCount;

	/**
	 * Default constructor
	 *
	 */
	public GrahamFilter() {

		newWordScore= NEW_WORD_SCORE;
		minWordCount= MIN_WORD_COUNT;
	}

	public GrahamFilter(float newWordScore, int minWordCount) {
		this.newWordScore= newWordScore;
		this.minWordCount= minWordCount;
	}

	/**
	 * @see org.macchiato.filter.Filter#scoreTerm(int, int, int, int)
	 */
	public float scoreTerm(
		int goodCount,
		int badCount,
		int goodMessageCount,
		int badMessageCount) {

		int totalMessageCount= goodMessageCount + badMessageCount;

		/*
		int halfMessageCount = totalMessageCount / 2;
		
		float goodMultiplier = (float) goodMessageCount / (float) totalMessageCount;
		float badMultiplier = (float) badMessageCount / (float) totalMessageCount;
		*/

		// double the count good -> avoid false-positives
		goodCount *= 2;

		int totalCount= goodCount + badCount;

		float score= newWordScore;

		if (totalCount >= minWordCount) {
			//System.out.println("totalCount>minWordCount...");
			if (goodCount == 0) {
				if (badCount > 10) {
					return 0.9999f;
				} else {
					return 0.9998f;
				}
			}

			float goodRatio= computeRatio(goodCount, goodMessageCount);
			log.finest("good-ratio="+goodRatio);
			float badRatio= computeRatio(badCount, badMessageCount);
			log.finest("bad-ratio="+badRatio);
			float denominator= goodRatio + badRatio;
			//System.out.println("denom="+denominator);

			if (denominator > 0.0f) {
				score= badRatio / denominator;

				//System.out.println("score="+score);
			}
		}

		/*
		if ( score > MAX_PROBABILITY) {
			System.out.println("------> border case at score="+score);
		}
		*/

		return Math.max(MIN_PROBABILITY, Math.min(MAX_PROBABILITY, score));
	}

	/**
	 * Compute ratio
	 * 
	 * @param count				count of occurences
	 * @param totalCount		total count of messages
	 * 
	 * @return					resulting ratio
	 */
	public float computeRatio(int count, int totalCount) {
		if ((count == 0) || (totalCount == 0))
			return NEARLY_ZERO;

		if (count >= totalCount) {
			return MAX_PROBABILITY;
		}

		return (float) count / (float) totalCount;
	}

}
