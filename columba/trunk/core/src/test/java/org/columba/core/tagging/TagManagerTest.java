package org.columba.core.tagging;

import java.io.File;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.columba.core.config.Config;
import org.columba.core.tagging.api.ITag;

public class TagManagerTest extends TestCase {

	TagManager tm;

	protected void setUp() throws Exception {
		super.setUp();
		Config c = new Config(new File(
				"C:\\Dokumente und Einstellungen\\Matthias\\.columba"));
		tm = new TagManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.addTag(String)'
	 */
	public void testAddTag() {
		tm.addTag("NormalTag");
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.getAllTags()'
	 */
	public void testGetAllTags() {
		boolean found = false;
		for (Iterator<ITag> iter = tm.getAllTags(); iter.hasNext();) {
			ITag tag = iter.next();
			if (tag.getProperty("name").equals("NormalTag")) {
				found = true;
				System.out.println("Found NormalTag");
			}
		}
	}

	/*
	 * Test method for 'org.columba.core.tagging.TagManager.getTag(String)'
	 */
	public void testGetTag() {
		if (tm.getTag("NormalTag") == null) // getByName
			fail("NormalTag not found");
		else
			System.out.println("NormalTag found");
	}
	/*
	 * Test method for 'org.columba.core.tagging.TagManager.removeTag(String)'
	 */
	public void testRemoveTag() {
		tm.removeTag("NormalTag");
		if (tm.getTag("NormalTag") != null)
			fail("NormalTag not deleted!");
	}

}
