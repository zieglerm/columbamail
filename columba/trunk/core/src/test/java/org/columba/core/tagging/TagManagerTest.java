package org.columba.core.tagging;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import org.columba.core.config.Config;
import org.columba.core.tagging.api.ITag;

public class TagManagerTest extends TestCase {

	TagManager tm;
	static String normalTagId = "";

	protected void setUp() throws Exception {
		super.setUp();
		tm = new TagManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.addTag(String)'
	 */
	public void testAddTag() {
		ITag normalTag = tm.addTag("NormalTag");
		normalTagId = normalTag.getId();
		System.out.println("normalTagId = " + normalTagId);
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.getAllTags()'
	 */
	public void testGetAllTags() {
		boolean found = false;
		for (Iterator<ITag> iter = tm.getAllTags(); iter.hasNext();) {
			ITag tag = iter.next();
			System.out.println("tag = " + tag.getId() + " | " + tag.getName());
			if (tag.getName().equals("NormalTag")) {
				found = true;
				System.out.println("Found NormalTag");
			}
		}
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.getTag(String)'
	 */
	public void testGetTag() {
		if (tm.getTag(normalTagId) == null) // getByName
			fail("NormalTag not found");
		else
			System.out.println("NormalTag found");
	}
	/*
	 * Test method for 'org.columba.core.tagging.TagManager.removeTag(String)'
	 */
	public void testRemoveTag() {
		if (tm.getTag(normalTagId) == null)
			fail("NormalTag should be there!");
		tm.removeTag(normalTagId);
		if (tm.getTag(normalTagId) != null)
			fail("NormalTag not deleted!");
	}

}
