package org.columba.core.gui.tagging;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.columba.api.exception.StoreException;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.logging.Logging;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;

public class AddTagAction extends AbstractColumbaAction {

	final static String ADD_TAG = "Add Tag...";

	public AddTagAction(IFrameMediator frameMediator) {
		super(frameMediator, ADD_TAG);
	}

	public void actionPerformed(ActionEvent arg0) {
		JFrame frame = getFrameMediator().getContainer().getFrame();
		AddTagDialog dialog = new AddTagDialog(frame);

		if (dialog.getSuccess()) {
			String tagName = dialog.getTagName();

			if (tagName != null && tagName.length() > 0) {

				try {
					ITag tag = TagManager.getInstance().addTag(tagName);

					if (tag == null)
						JOptionPane.showMessageDialog(null, "Error adding Tag");

				} catch (StoreException e) {
					if (Logging.DEBUG)
						e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error adding Tag");
				}

			}
		}

	}

}
