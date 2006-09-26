package org.columba.mail.gui.table.action;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.swing.JMenuItem;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.association.AssociationStore;
import org.columba.core.association.api.IAssociation;
import org.columba.core.command.CommandProcessor;
import org.columba.core.folder.api.IFolder;
import org.columba.core.gui.menu.IMenu;
import org.columba.core.main.Bootstrap;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.command.TagMessageCommand;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.folder.virtual.VirtualHeader;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.selection.TableSelectionChangedEvent;
import org.columba.mail.message.IHeaderList;
import org.columba.mail.util.MailResourceLoader;

public class TagMessageMenu extends IMenu implements ActionListener,
		ISelectionListener {

	final static private String SERVICE_ID = "tagging";

	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.table.action.TagMessageMenu");

	/**
	 * @param controller
	 */
	public TagMessageMenu(IFrameMediator controller) {
		super(controller, MailResourceLoader.getString("dialog", "tagging",
				"menu_tag_message"), "menu_tag_message");

		createSubMenu();

		((MailFrameMediator) controller).registerTableSelectionListener(this);
	}

	protected void createSubMenu() {

		if (!Bootstrap.ENABLE_TAGS)
			return;

		IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator())
				.getTableSelection();

		// TODO (@author hubms): implement custom menuitem renderer
		JMenuItem item = new JMenuItem(MailResourceLoader.getString("dialog",
				"tagging", "none"));
		item.setActionCommand("NONE");
		item.addActionListener(this);
		add(item);
		addSeparator();

		// don't want to have two separators
		boolean tags = false;

		// add all existing tags to the menu
		for (Iterator<ITag> iter = TagManager.getInstance().getAllTags(); iter
				.hasNext();) {
			ITag tag = iter.next();
			item = new JMenuItem(tag.getProperty("name"));

			// mark tag, if the current selection is tagged with it
			if (isTagged(r, tag))
				item.setIcon(ImageLoader.getSmallIcon(IconKeys.INTERNET));

			item.setActionCommand(tag.getProperty("name"));
			item.addActionListener(this);
			add(item);

			tags = true;
		}
		if (tags)
			addSeparator();

		// static entries add and edit
		JMenuItem addTagItem = new JMenuItem(MailResourceLoader.getString(
				"dialog", "tagging", "add"));
		addTagItem.setActionCommand("ADD");
		addTagItem.addActionListener(this);
		add(addTagItem);

		JMenuItem editTagItem = new JMenuItem(MailResourceLoader.getString(
				"dialog", "tagging", "edit"));
		editTagItem.setActionCommand("EDIT");
		editTagItem.addActionListener(this);
		add(editTagItem);

	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		// get current message list selection
		IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator())
				.getTableSelection();

		if (action.equals("NONE")) {

			for (Iterator<ITag> iter = TagManager.getInstance().getAllTags(); iter
					.hasNext();) {

				ITag tag = iter.next();
				// TODO @author hubms: bad: if there is a icon it is tagged or
				// not

				// pass command to scheduler
				CommandProcessor.getInstance().addOp(
						new TagMessageCommand(r, TagMessageCommand.REMOVE_TAG,
								tag.getId()));

			}

		} else if (action.equals("ADD")) {
			// TODO @author hubms: solve the observer problem
			// AddTagDialog addDialog = new AddTagDialog();
			// addDialog.addObserver(getFrameMediator(),
			// getFrameMediator().getTagList());
		} else if (action.equals("EDIT")) {
			// TODO @author hubms: solve the observer problem
			// give the focus to the new taglist
		} else {
			// which menuitem was selected?
			ITag result = null;
			int no = 2; // start with one because None is the first menu entry

			for (Iterator<ITag> iter = TagManager.getInstance().getAllTags(); iter
					.hasNext();) {
				ITag tag = iter.next();
				if (action.equals(tag.getProperty("name"))) {
					result = tag;
					break;
				}
				no++;
			}

			if (result != null) {

				// TODO @author hubms: bad: if there is a icon it is tagged or
				// not
				if (getItem(no).getIcon() == null)
					// pass command to scheduler
					CommandProcessor.getInstance().addOp(
							new TagMessageCommand(r, TagMessageCommand.ADD_TAG,
									result.getId()));
				else
					// pass command to scheduler
					CommandProcessor.getInstance().addOp(
							new TagMessageCommand(r,
									TagMessageCommand.REMOVE_TAG, result
											.getId()));
			}

		}

	}

	public void selectionChanged(SelectionChangedEvent e) {
		if (((TableSelectionChangedEvent) e).getUids().length > 0) {
			setEnabled(true);
		} else {
			setEnabled(false);
		}

	}

	public void paint(Graphics arg0) {
		// because tags can be added to the list at runtime
		// recreate the submenu
		this.removeAll();
		createSubMenu();
		super.paint(arg0);
	}

	private boolean checkOneUid(Object uid, IFolder srcFolder, ITag tag) {
		for (IAssociation as : AssociationStore.getInstance()
				.getAllAssociations(
						TagMessageCommand.createURI(srcFolder.getId(), uid)
								.toString())) {
			if (as.getServiceId().equals(SERVICE_ID)
					&& (as.getMetaDataId() != null)
					&& (as.getMetaDataId().equals(tag.getId()))) {
				return true;
			}
		}
		return false;
	}

	private boolean isTagged(IMailFolderCommandReference r, ITag tag) {
		if ((r == null) || (r.getUids().length <= 0) || (tag == null))
			return false;

		Hashtable<Object, IFolder> mails = new Hashtable<Object, IFolder>();

		// check if virtual folder, if yes, do not use these uids, use the
		// real uids instead
		if (r.getSourceFolder() instanceof VirtualFolder) {
			// get original folder
			try {

				IHeaderList hl = ((IMailbox) r.getSourceFolder())
						.getHeaderList();
				for (Object uid : r.getUids()) {
					// should be virtual
					mails.put(((VirtualHeader) hl.get(uid)).getSrcUid(),
							((VirtualHeader) hl.get(uid)).getSrcFolder());
				}
			} catch (Exception e) {
				LOG.severe("Error getting header list from virtual folder");
				e.printStackTrace();
				return false;
			}
		} else {
			for (Object uid : r.getUids()) {
				mails.put(uid, r.getSourceFolder());
			}
		}

		// if all messages are tagged with tag, then return true, else false
		boolean result = true;
		for (Entry<Object, IFolder> entry : mails.entrySet()) {
			boolean current = checkOneUid(entry.getKey(), entry.getValue(), tag);
			if (current == false)
				return false;
		}
		return result;
	}

}
