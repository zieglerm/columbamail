package org.columba.mail.gui.table.action;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.association.AssociationStore;
import org.columba.core.association.api.IAssociation;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.menu.IMenu;
import org.columba.core.gui.tagging.AddTagDialog;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.command.TagMessageCommand;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.selection.TableSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

public class TagMessageMenu extends IMenu implements ActionListener,
		ISelectionListener {

	final static private String SERVICE_ID = "tagging";

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

		// TODO @author hubms, remove that trick, when the new
		// entity manager is released
		
		// here is the magic flag!
		boolean enableTags = false;

		if (!enableTags)
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
			item = new JMenuItem(tag.getName());
			
			// mark tag, if the current selection is tagged with it 
			if (isTagged(r, tag))
				item.setIcon(ImageLoader.getSmallIcon(IconKeys.INTERNET));
			
			item.setActionCommand(tag.getName());
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
			// TODO @author hubms: not the COLOR!

			// r.removeAllTags(true);

			// pass command to scheduler
			// CommandProcessor.getInstance().addOp(new ColorMessageCommand(r));
		} else if (action.equals("ADD")) {
			AddTagDialog addDialog = new AddTagDialog();
		} else if (action.equals("EDIT")) {
			// TODO @author hubms: popup a dialog
		} else {
			// which menuitem was selected?
			ITag result = null;
			int no = 2; // start with one because None is the first menu entry

			for (Iterator<ITag> iter = TagManager.getInstance().getAllTags(); iter
					.hasNext();) {
				ITag tag = iter.next();
				if (action.equals(tag.getName())) {
					result = tag;
					break;
				}
				no++;
			}

			if (result != null) {

				// TODO @author hubms: bad: if there is a icon it is tagged or
				// not
				if (getItem(no).getIcon() == null)
					r.addTag(result.getName());
				else
					r.removeTag(result.getName());

				// pass command to scheduler
				CommandProcessor.getInstance().addOp(new TagMessageCommand(r));
			}

			// tricks from the ColorChooserDialog:
			//
			// for (int i = 0; i < items.length; i++) {
			// if (action.equals(items[i])) {
			// result = i;
			// break;
			// }
			// }
			//
			// // add color selection to reference
			//
			// r.setColorValue(colors[result].getRGB());
			//
			// // pass command to scheduler
			// CommandProcessor.getInstance().addOp(new ColorMessageCommand(r));

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

	private boolean isTagged(IMailFolderCommandReference r, ITag tag) {
		if ((r == null) || (r.getUids().length <= 0) || (tag == null))
			return false;

		// if all messages are tagged with tag, then return true, else false
		boolean result = true;
		for (Object currentUid : r.getUids()) {
			boolean current = false;
			for (IAssociation as : AssociationStore.getInstance()
					.getAllAssociations(
							TagMessageCommand.createURI(
									r.getSourceFolder().getName(), currentUid)
									.toString())) {
				if (as.getServiceId().equals(SERVICE_ID)
						&& (as.getMetaDataId() != null)
						&& (as.getMetaDataId().equals(tag.getName()))) {
					current = true;
				}
			}
			if (current == false)
				return false;
		}
		return result;
	}

}
