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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.macchiato.io.CSVHelper;
import org.macchiato.log.MacchiatoLogger;

/**
 * @author fdietz
 *  
 */
public class CrossValidationTest extends TestCase {

	private final static int FRAME_SIZE = 40;

	private int hamCount;

	private int falseHam;

	private int spamCount;

	private int falseSpam;

	/**
	 *  
	 */
	public CrossValidationTest() {
		super();
	}

	/**
	 * @param arg0
	 */
	public CrossValidationTest(String arg0) {
		super(arg0);
	}

	public void test() throws Exception {
		File[] spamFileList = filterDirectories(new File("res/spam")
				.listFiles());
		File[] hamFileList = filterDirectories(new File("res/ham").listFiles());

		SpamFilter filter = new SpamFilterImpl();

		learnMessage(spamFileList, filter, 0, 10, true);

		learnMessage(hamFileList, filter, 0, 10, false);

		//((SpamFilterImpl) filter).getDB().printDebug();

		System.out.println("-----> bad message count="
				+ ((SpamFilterImpl) filter).getDB().getBadMessageCount());
		System.out.println("-----> good message count="
				+ ((SpamFilterImpl) filter).getDB().getGoodMessageCount());
		System.out.println("-----> token count="
				+ ((SpamFilterImpl) filter).getDB().tokenCount());

		CSVHelper.save(((SpamFilterImpl) filter).getDB(),
				new File("output.csv"));

		spamCount = 0;
		hamCount = 0;

		MacchiatoLogger.log.info("---> score ham message");

		scoreMessageSet(hamFileList, filter, 101, 300, false);

		MacchiatoLogger.log.info("---> score spam message");

		scoreMessageSet(spamFileList, filter, 101, 300, true);

		System.out.println("ham total count=" + hamCount);
		System.out.println("spam total count=" + spamCount);

		System.out.println("ham false positive=" + falseHam);
		System.out.println("spam false positive=" + falseSpam);

		double hamFalsePositivePercentage = (double) falseHam / hamCount;

		double spamFalsePositivePercentage = (double) falseSpam / spamCount;

		System.out.println("ham false-positive percentage="
				+ hamFalsePositivePercentage);
		System.out.println("spam false-positive percentage="
				+ spamFalsePositivePercentage);
	}

	private void learnMessage(File[] list, SpamFilter filter, int startIndex,
			int count, boolean spam) throws Exception {

		for (int i = startIndex; i < startIndex + count; i++) {
			if (spam)
				MacchiatoLogger.log.info("training ham=" + list[i]);
			else
				MacchiatoLogger.log.info("training spam=" + list[i]);

			Message message = new Message(new FileInputStream(list[i]));

			if (spam)
				filter.trainMessageAsSpam(message);
			else
				filter.trainMessageAsHam(message);
		}

	}

	private void scoreMessageSet(File[] list, SpamFilter filter,
			int startIndex, int count, boolean spam) throws Exception {
		float[] results = new float[list.length];

		for (int i = startIndex; i < startIndex + count; i++) {
			Message message = new Message(new FileInputStream(list[i]));
			float score = filter.scoreMessage(message, null);
			if (score > 0.99) {
				if (!spam)
					falseSpam++;

				spamCount++;
			} else {
				if (spam)
					falseHam++;

				hamCount++;
			}

			System.out.println("->score=" + score + "\n");
		}
	}

	private File[] filterDirectories(File[] list) {
		ArrayList l = new ArrayList();

		for (int i = 0; i < list.length; i++) {
			if (list[i].isDirectory())
				continue;

			l.add(list[i]);
		}

		File[] results = new File[l.size()];
		for (int i = 0; i < l.size(); i++) {
			results[i] = (File) l.get(i);
		}

		return results;
	}

}