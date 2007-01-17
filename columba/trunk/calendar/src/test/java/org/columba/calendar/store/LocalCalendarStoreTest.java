package org.columba.calendar.store;

import java.io.File;

import junit.framework.TestCase;

import org.columba.calendar.base.UUIDGenerator;
import org.columba.calendar.model.Event;
import org.columba.calendar.model.EventInfo;
import org.columba.calendar.model.api.IComponentInfo;
import org.columba.calendar.store.api.StoreException;

public class LocalCalendarStoreTest extends TestCase {

	private File file;

	private LocalCalendarStore storage;

	protected void setUp() throws Exception {
		file = new File("test_calendar");

		storage = new LocalCalendarStore(file);
	}

	public void testAddGet() throws Exception {
		String uuid = new UUIDGenerator().newUUID();

		Event model = new Event(uuid);
		model.setSummary("summary");
		model.setDescription("description");
		
		EventInfo eventInfo = new EventInfo(uuid, "calendar1", model);
		storage.add(eventInfo);

		boolean exists = storage.exists(uuid);
		assertTrue(exists);

		IComponentInfo result = storage.get(uuid);
		assertNotNull(result);

	}

	public void testRemove() throws Exception {
		String uuid = new UUIDGenerator().newUUID();

		Event model = new Event(uuid);
		model.setSummary("summary");
		model.setDescription("description");
		
		EventInfo eventInfo = new EventInfo(uuid, "calendar1", model);

		storage.add(eventInfo);

		storage.remove(uuid);

		try {
			IComponentInfo result = storage.get(uuid);
		} catch (StoreException e) {
			// that is the expected case
			return;
		} catch (Exception e) {
			fail("Expected a StoreException, not " + e.getMessage());
		}
		
		fail("Expected a StoreException");

	}

	protected void tearDown() throws Exception {
//		// delete all data in directory
//		File[] list = file.listFiles();
//
//		for (int i = 0; i < list.length; i++) {
//			list[i].delete();
//		}
//
//		// delete folder
//		file.delete();
	}

}
