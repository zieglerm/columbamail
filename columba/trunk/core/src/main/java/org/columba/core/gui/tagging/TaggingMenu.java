package org.columba.core.gui.tagging;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.menu.IMenu;
import org.columba.core.main.Bootstrap;
import org.columba.core.resourceloader.GlobalResourceLoader;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.core.tagging.api.ITagEvent;
import org.columba.core.tagging.api.ITagListener;

/**
 * Abstract base class for a tagging menu.
 * <p>
 * 
 * @author fdietz
 * 
 * @author frd
 */
public abstract class TaggingMenu extends IMenu {

	public TaggingMenu(IFrameMediator controller, String name, String id) {
		super(controller, name, id);

		createSubMenu();

		// update menu if tags are changed
		TagManager.getInstance().addTagListener(new MyTagListener());
	}

	protected void createSubMenu() {

		if (!Bootstrap.ENABLE_TAGS)
			return;

		// TODO (@author hubms): implement custom menuitem renderer
		JMenuItem item = new JMenuItem(GlobalResourceLoader.getString("dialog",
				"tagging", "none"));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// fire callback to remove all tags from selected items
				removeAllTags();
			}
		});
		add(item);
		addSeparator();

		// don't want to have two separators
		boolean tags = false;

		// add all existing tags to the menu
		for (Iterator<ITag> iter = TagManager.getInstance().getAllTags(); iter
				.hasNext();) {
			final ITag tag = iter.next();
			final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(tag
					.getProperty("name"));

			// mark tag, if the current selection is tagged with it
			final boolean tagged = isTagged(tag);

			// we can add icons or colors later on
			// item.setIcon(ImageLoader.getSmallIcon(IconKeys.INTERNET));
			menuItem.setSelected(tagged);

			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (menuItem.isSelected())
						assignTag(tag.getId());
					else
						removeTag(tag.getId());
				}
			});
			add(menuItem);

			tags = true;
		}

		if (tags)
			addSeparator();

		JMenuItem editTagItem = new JMenuItem(GlobalResourceLoader.getString(
				"dialog", "tagging", "edit"));
		editTagItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// not implemented yet
			}
		});
		add(editTagItem);

	}

	// listener updates menu in case the tags where changed
	class MyTagListener implements ITagListener {
		MyTagListener() {
		}

		public void tagChanged(ITagEvent event) {
			String tagId = event.getId();
			updateMenu();
		}

		public void tagAdded(ITagEvent event) {
			String tagId = event.getId();
			updateMenu();
		}

		public void tagDeleted(ITagEvent event) {
			String tagId = event.getId();
			updateMenu();
		}

		// real stupid recreation of whole menu model
		private void updateMenu() {
			removeAll();
			createSubMenu();
		}
	}

	/** **************** overwrite callback methods ************************ */

	/**
	 * Check if the current selection has the specific tag assigned to it.
	 * 
	 * @param tag
	 * @return
	 */
	protected abstract boolean isTagged(ITag tag);

	/**
	 * Method is called if tag should be added to selected elements
	 * 
	 * @param tagId
	 */
	protected abstract void assignTag(String tagId);

	/**
	 * Method is called if tag should be removed from selected elements
	 * 
	 * @param tagId
	 */
	protected abstract void removeTag(String tagId);

	/**
	 * Methid is called if all tags should be removed from the selected elements
	 * 
	 */
	protected abstract void removeAllTags();

}
