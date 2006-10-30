package org.columba.calendar.ui.tagging;

import java.util.Collection;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.calendar.ui.frame.CalendarFrameMediator;
import org.columba.core.association.AssociationStore;
import org.columba.core.gui.tagging.TagList;
import org.columba.core.tagging.api.ITag;

import com.miginfocom.calendar.category.Category;
import com.miginfocom.calendar.category.CategoryDepository;

public class CalendarTagList extends TagList {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger
			.getLogger("org.columba.calendar.ui.tagging"); //$NON-NLS-1$

	public static final String PROP_FILTERED = "filterRow";

	private IFrameMediator frameMediator;

	public CalendarTagList(IFrameMediator frameMediator) {
		super();

		this.frameMediator = frameMediator;

		addListSelectionListener(new MyListSelectionListener());
	}

	class MyListSelectionListener implements ListSelectionListener {
		MyListSelectionListener() {
		}

		public void valueChanged(ListSelectionEvent event) {
			// return if selection change is in flux
			if (event.getValueIsAdjusting()) {
				return;
			}

			ITag result = (ITag) getSelectedValue();
			// create a virtual folder with all messages holding this tag
			Collection<String> messageList = AssociationStore.getInstance()
					.getAssociatedItems("tagging", result.getId());
			for (String id : messageList) {

				// example:
				// "columba://org.columba.contact/<folder-id>/<contact-id>"
				String s = id.toString();

				// TODO: @author fdietz replace with regular expression
				int activityIndex = s.lastIndexOf('/');
				String contactId = s.substring(activityIndex + 1, s.length());
				int folderIndex = s.lastIndexOf('/', activityIndex - 1);
				String folderId = s.substring(folderIndex + 1, activityIndex);
				int componentIndex = s.lastIndexOf('/', folderIndex - 1);
				String componentId = s.substring(componentIndex + 1,
						folderIndex);

				// check if its a contact component
				if (componentId.equals("org.columba.calendar")) {
					boolean success = false;
					Collection<Category> c = CategoryDepository.getRoot()
							.getChildrenDeep();
					for (Category category : c) {
						String categoryId = (String) category.getId();
						if ( !categoryId.startsWith("tag_")) continue;
						
						if (categoryId.equals(folderId)) {
							// found category -> mark it as selected
							category.setPropertyDeep(Category.PROP_IS_HIDDEN,
									Boolean.valueOf(false), Boolean.TRUE);
							success = true;
						} else {
							// non-matching found -> mark it as *not* selected
							category.setPropertyDeep(Category.PROP_IS_HIDDEN,
									Boolean.valueOf(true), Boolean.TRUE);
						}
					}
					
					if ( !success) {
						// there was no category -> create a new one
						Category category = CategoryDepository.getRoot().addSubCategory("tag_"+result.getId(), result.getProperty("name"));
						// mark it as selected
						category.setPropertyDeep(Category.PROP_IS_HIDDEN,
								Boolean.valueOf(false), Boolean.TRUE);
					}
				}
				
				// update calendar viewer
				((CalendarFrameMediator) frameMediator).getCalendarView().recreateFilterRows();
			}
		}
	}
}