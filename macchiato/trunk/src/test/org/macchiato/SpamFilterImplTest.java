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
package org.macchiato;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.macchiato.io.CSVHelper;
import org.macchiato.log.MacchiatoLogger;

/**
 * 
 *
 * @author fdietz
 */
public class SpamFilterImplTest extends TestCase {

	private final static int FRAME_SIZE= 40;

	/**
	 * Constructor for SpamFilterImplTest.
	 * @param arg0
	 */
	public SpamFilterImplTest(String arg0) {
		super(arg0);
	}

	public void test() {
		List list= new ArrayList();
		list.add(new Float(0.9));
		list.add(new Float(0.9999));
		list.add(new Float(0.9998));
		list.add(new Float(0.01));
		list.add(new Float(0.0001));
		list.add(new Float(0.345632));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.54));
		list.add(new Float(0.9999));
		list.add(new Float(0.9998));
		list.add(new Float(0.01));
		list.add(new Float(0.0001));
		list.add(new Float(0.345632));

		list= new SpamFilterImpl().filterProbabilities(list);
		Iterator it= list.iterator();
		while (it.hasNext()) {
			System.out.println("it=" + it.next());
		}

	}
	
	public void testSingleMessage() throws Exception {
		File[] spamFileList=
			filterDirectories(new File("res/spam").listFiles());
		File[] hamFileList= filterDirectories(new File("res/ham").listFiles());

		SpamFilter filter= new SpamFilterImpl();

		learnMessage(spamFileList, filter, 0, 1, true);

		//((SpamFilterImpl) filter).getDB().printDebug();

		CSVHelper.save(((SpamFilterImpl) filter).getDB(), new File("single.csv"));
		
		System.out.println("-----> bad message count="+((SpamFilterImpl) filter).getDB().getBadMessageCount());
		System.out.println("-----> good message count="+((SpamFilterImpl) filter).getDB().getGoodMessageCount());
		System.out.println("-----> token count="+((SpamFilterImpl) filter).getDB().tokenCount());

	}

	public void test2() throws Exception {
		File[] spamFileList=
			filterDirectories(new File("res/spam").listFiles());
		File[] hamFileList= filterDirectories(new File("res/ham").listFiles());

		SpamFilter filter= new SpamFilterImpl();

		learnMessage(spamFileList, filter, 0, 400, true);

		learnMessage(hamFileList, filter, 0, 300, false);

		((SpamFilterImpl) filter).getDB().printDebug();

		System.out.println("-----> bad message count="+((SpamFilterImpl) filter).getDB().getBadMessageCount());
		System.out.println("-----> good message count="+((SpamFilterImpl) filter).getDB().getGoodMessageCount());
		System.out.println("-----> token count="+((SpamFilterImpl) filter).getDB().tokenCount());
				
		MacchiatoLogger.log.info("---> score ham message");

		scoreMessageSet(hamFileList, filter, 301, 50);

		MacchiatoLogger.log.info("---> score spam message");

		scoreMessageSet(spamFileList, filter, 401, 50);

	}

	private void learnMessage(
		File[] list,
		SpamFilter filter,
		int startIndex,
		int count,
		boolean spam)
		throws Exception {

		for (int i= startIndex; i < startIndex + count; i++) {
			if (spam)
				MacchiatoLogger.log.info("training ham=" + list[i]);
			else
				MacchiatoLogger.log.info("training spam=" + list[i]);

			Message message= new Message(new FileInputStream(list[i]));

			if (spam)
				filter.trainMessageAsSpam(message);
			else
				filter.trainMessageAsHam(message);
		}

	}

	private void scoreMessageSet(
		File[] list,
		SpamFilter filter,
		int startIndex,
		int count)
		throws Exception {
		float[] results= new float[list.length];

		for (int i= startIndex; i < startIndex + count; i++) {
			Message message= new Message(new FileInputStream(list[i]));
			float score= filter.scoreMessage(message,null);
			System.out.println("->score=" + score + "\n");
		}
	}

	private File[] filterDirectories(File[] list) {
		ArrayList l= new ArrayList();

		for (int i= 0; i < list.length; i++) {
			if (list[i].isDirectory())
				continue;

			l.add(list[i]);
		}

		File[] results= new File[l.size()];
		for (int i= 0; i < l.size(); i++) {
			results[i]= (File) l.get(i);
		}

		return results;
	}

}
