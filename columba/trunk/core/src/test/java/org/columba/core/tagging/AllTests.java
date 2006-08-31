package org.columba.core.tagging;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.columba.core.tagging");
		//$JUnit-BEGIN$
		suite.addTestSuite(TagManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
