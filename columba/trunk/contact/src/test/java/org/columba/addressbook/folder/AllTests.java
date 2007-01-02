package org.columba.addressbook.folder;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.columba.addressbook.folder");
		//$JUnit-BEGIN$
		suite.addTestSuite(VirtualFolderContactTest.class);
		suite.addTestSuite(AddContactTest.class);
		suite.addTestSuite(ModifyContactTest.class);
		suite.addTestSuite(GetContactTest.class);
		suite.addTestSuite(RemoveContactTest.class);
		suite.addTestSuite(GetHeaderItemListTest.class);
		//$JUnit-END$
		return suite;
	}

}
