package org.columba.core.associations;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.columba.core.associations");
		//$JUnit-BEGIN$
		suite.addTestSuite(AssociationStoreCases.class);
		//$JUnit-END$
		return suite;
	}

}
