package org.columba.core.associations;

import junit.framework.TestCase;

import org.columba.core.association.AssociationStore;

public class AssociationStoreCases extends TestCase {

	AssociationStore store;

	protected void setUp() throws Exception {
		store = AssociationStore.getInstance();
	}

	protected void tearDown() throws Exception {
		store.shutdown();
	}

	public void testAddAssociation() {
		store.addAssociation("serviceId1", "metaDataId1", "itemId1");
		if ((store.getAssociatedItems("serviceId1", "metaDataId1") != null)
				&& (store.getAssociatedItems("serviceId1", "metaDataId1")
						.size() == 1)
				&& ((String) (store.getAssociatedItems("serviceId1",
						"metaDataId1").toArray()[0])).equals("itemId1"))
			System.out.println("Correct value 'itemId1' found");
		else
			fail("Associated item not found "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size());
		/*
		 * for (IAssociation association: store.getAllAssociations("itemId1")) {
		 * System.out.println(association.getItemId() +
		 * association.getMetaDataId() + association.getServiceId()); }
		 */
		store.removeAssociation("serviceId1", "metaDataId1", "itemId1");
	}

	public void testGetAllAssociations() {
		store.addAssociation("serviceId1", "metaDataId1", "itemId1");
		if ((store.getAllAssociations("itemId1") != null)
				&& (store.getAllAssociations("itemId1").size() == 1))
			System.out.println("'itemId1' found 1 time");
		else
			fail("Associated item not found "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size());
		store.removeAssociation("serviceId1", "metaDataId1", "itemId1");
	}

	public void testGetAssociatedItems() {
		store.addAssociation("serviceId1", "metaDataId1", "itemId1");
		if ((store.getAssociatedItems("serviceId1", "metaDataId1") != null)
				&& (store.getAssociatedItems("serviceId1", "metaDataId1")
						.size() == 1)
				&& ((String) (store.getAssociatedItems("serviceId1",
						"metaDataId1").toArray()[0])).equals("itemId1"))
			System.out.println("Correct value 'itemId1' found");
		else
			fail("Associated item not found "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size());
		store.removeAssociation("serviceId1", "metaDataId1", "itemId1");
	}

	public void testInit() {
		store.init();
		if (store.isReady()) {
			System.out.println("Store is ready");
		} else {
			fail("Store is not ready");
		}
		System.out.println(store.getAllAssociations("itemId1").size());
		System.out.println(store.getAllAssociations("itemId2").size());

	}

	public void testIsReady() {
		/*
		 * does not work after the singleton instantiation! if (store.isReady() ==
		 * false) System.out.println("Store is not ready (thats correct)!");
		 * else fail("Store is ready, should not be before init!");
		 */
		store.init();
		if (store.isReady())
			System.out.println("Store is ready!");
		else
			fail("Store is not ready, should be after init!");
	}

	public void testRemoveAssociation() {
		store.addAssociation("serviceId1", "metaDataId1", "itemId1");
		store.addAssociation("serviceId1", "metaDataId1", "itemId2");
		if ((store.getAssociatedItems("serviceId1", "metaDataId1") != null)
				&& (store.getAssociatedItems("serviceId1", "metaDataId1")
						.size() == 2))
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 2");
		store.removeAssociation("serviceId1", "metaDataId1", "itemId1");
		if (store.getAssociatedItems("serviceId1", "metaDataId1").size() == 1)
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 1");
		store.removeAssociation("serviceId1", "metaDataId1", "itemId2");
		if (store.getAssociatedItems("serviceId1", "metaDataId1").size() == 0)
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 0");
	}

	public void testRemoveItem() {
		store.addAssociation("serviceId1", "metaDataId1", "itemId1");
		store.addAssociation("serviceId1", "metaDataId2", "itemId2");
		store.addAssociation("serviceId1", "metaDataId1", "itemId2");
		if ((store.getAssociatedItems("serviceId1", "metaDataId1") != null)
				&& (store.getAssociatedItems("serviceId1", "metaDataId1")
						.size() == 2))
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 2");
		store.removeItem("itemId1");
		if (store.getAssociatedItems("serviceId1", "metaDataId1").size() == 1)
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 1");
		if (store.getAllAssociations("itemId2").size() == 2)
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAllAssociations("itemId2").size()
					+ " should be 2");
		store.removeItem("itemId2");
		if (store.getAssociatedItems("serviceId1", "metaDataId1").size() == 0)
			System.out.println("Associated item size correct!");
		else
			fail("Associated item size not correct, "
					+ store.getAssociatedItems("serviceId1", "metaDataId1")
							.size() + " should be 0");
	}

	public void testShutdown() {
		if (store.isReady() == false)
			store.init();
		System.out.println(store.isReady());
		store.shutdown();
	}

	public void testGetInstance() {
		if (store == null)
			fail("store instance is null");
	}

}
