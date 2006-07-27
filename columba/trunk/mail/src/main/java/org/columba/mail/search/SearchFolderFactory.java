package org.columba.mail.search;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.columba.core.base.UUIDGenerator;
import org.columba.core.filter.FilterCriteria;
import org.columba.core.filter.FilterRule;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.tree.FolderTreeModel;

public class SearchFolderFactory {

	/**
	 * Create search folder for simple search with a single criteria
	 */
	public static VirtualFolder createSearchFolder(FilterCriteria c,
			IMailFolder folder) throws Exception {
		// get search folder

		String uuid = new UUIDGenerator().newUUID();
		VirtualFolder searchFolder = new VirtualFolder("Search Result",
				"VirtualFolder", uuid, folder);

		// remove old filters
		searchFolder.getFilter().getFilterRule().removeAll();

		// add filter criteria
		searchFolder.getFilter().getFilterRule().add(c);

		// search in subfolders recursively
		searchFolder.getConfiguration().setString("property",
				"include_subfolders", "true");

		String uid = folder.getId();

		// set source folder UID
		searchFolder.getConfiguration()
				.setString("property", "source_uid", uid);

		return searchFolder;
	}

	/**
	 * Create new search folder but use existing virtual folder for an "search
	 * inside results" search
	 */
	public static VirtualFolder prepareSearchFolder(FilterCriteria c,
			IMailFolder folder) throws Exception {

		String uuid = new UUIDGenerator().newUUID();
		VirtualFolder searchFolder = new VirtualFolder("Search Result",
				"VirtualFolder", uuid, folder);

		// remove old filters
		searchFolder.getFilter().getFilterRule().removeAll();

		// add filter criteria
		searchFolder.getFilter().getFilterRule().add(c);

		// don't search in subfolders recursively
		searchFolder.getConfiguration().setString("property",
				"include_subfolders", "false");

		String uid = folder.getId();

		// set source folder UID
		searchFolder.getConfiguration()
				.setString("property", "source_uid", uid);

		return searchFolder;
	}

	/**
	 * Create search for folder complex search with multiple criteria
	 */
	public static VirtualFolder createSearchFolder(FilterRule rule,
			IMailFolder folder) throws Exception {
		// get search folder

		String uuid = new UUIDGenerator().newUUID();
		VirtualFolder searchFolder = new VirtualFolder("Search Result",
				"VirtualFolder", uuid, folder);

		// remove old filters
		searchFolder.getFilter().getFilterRule().removeAll();
		searchFolder.getFilter().getFilterRule().setCondition(
				rule.getConditionInt());

		for (int i = 0; i < rule.getChildCount(); i++) {
			// add filter criteria
			searchFolder.getFilter().getFilterRule().add(rule.get(i));
		}

		// search in subfolders recursively
		searchFolder.getConfiguration().setString("property",
				"include_subfolders", "true");

		String uid = folder.getId();

		// set source folder UID
		searchFolder.getConfiguration()
				.setString("property", "source_uid", uid);

		return searchFolder;
	}

	/**
	 * Update already existing virtual folder for an "search inside results"
	 * search
	 */
	public static VirtualFolder prepareSearchFolder(FilterRule rule,
			IMailFolder folder) throws Exception {

		String uuid = new UUIDGenerator().newUUID();
		VirtualFolder searchFolder = new VirtualFolder("Search Result",
				"VirtualFolder", uuid, folder);

		// remove old filters
		searchFolder.getFilter().getFilterRule().removeAll();

		searchFolder.getFilter().getFilterRule().setCondition(
				rule.getConditionInt());

		for (int i = 0; i < rule.getChildCount(); i++) {
			// add filter criteria
			searchFolder.getFilter().getFilterRule().add(rule.get(i));
		}
		// don't search in subfolders recursively
		searchFolder.getConfiguration().setString("property",
				"include_subfolders", "false");

		String uid = folder.getId();

		// set source folder UID
		searchFolder.getConfiguration()
				.setString("property", "source_uid", uid);

		return searchFolder;
	}

	/**
	 * Return list of all source folders we going to query.
	 * 
	 * @return list of source folders
	 */
	public static List<IMailbox> getAllSourceFolders() {
		IMailFolder rootFolder = (IMailFolder) FolderTreeModel.getInstance()
				.getRoot();

		List<IMailbox> v = new Vector<IMailbox>();
		getChildren(rootFolder, v);

		return v;
	}

	private static void getChildren(IMailFolder parentFolder,
			List<IMailbox> list) {
		IMailFolder child;

		for (Enumeration e = parentFolder.children(); e.hasMoreElements();) {
			child = (IMailFolder) e.nextElement();

			if (child instanceof IMailbox) {
				if (child.getName().equalsIgnoreCase("Inbox")) {
					list.add((IMailbox) child);
				}
			}

			getChildren(child, list);
		}
	}
}
