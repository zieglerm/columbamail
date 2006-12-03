package org.columba.calendar.facade;

import java.net.URI;

import javax.swing.JOptionPane;

import org.columba.calendar.model.api.IEvent;
import org.columba.calendar.store.CalendarStoreFactory;
import org.columba.calendar.store.api.ICalendarStore;
import org.columba.calendar.store.api.StoreException;
import org.columba.calendar.ui.dialog.EditEventDialog;

public class DialogFacade {

	public void openEventEditorDialog(URI location) {
		String s = location.toString();
		// TODO: @author fdietz replace with regular expression
		int activityIndex = s.lastIndexOf('/');
		String activityId = s.substring(activityIndex + 1, s.length());
		int folderIndex = s.lastIndexOf('/', activityIndex - 1);
		String folderId = s.substring(folderIndex + 1, activityIndex);
		int componentIndex = s.lastIndexOf('/', folderIndex - 1);
		String componentId = s.substring(componentIndex + 1,
				folderIndex);

		ICalendarStore store = CalendarStoreFactory.getInstance()
				.getLocaleStore();

		// retrieve event from store
		try {
			IEvent model = (IEvent) store.get(activityId);

			EditEventDialog dialog = new EditEventDialog(null, model);
			if (dialog.success()) {
				IEvent updatedModel = dialog.getModel();

				// update store
				store.modify(activityId, updatedModel);
			}

		} catch (StoreException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
			e1.printStackTrace();
		}
	}
}
