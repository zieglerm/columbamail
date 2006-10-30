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

public class EditTagAction extends AbstractColumbaAction {

	final static String EDIT_TAG = "Edit Tag...";

	private TagList list;
	
	public EditTagAction(IFrameMediator frameMediator, TagList list) {
		super(frameMediator, EDIT_TAG);
		this.list = list;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
		JFrame frame = getFrameMediator().getContainer().getFrame();
		
		ITag tag = list.getSelectedTag();
		
		EditTagDialog editTagDialog = new EditTagDialog(frame, tag);
		if ( editTagDialog.getSuccess()) {
			
			String name = editTagDialog.getTagName();
			if ( name != null && name.length() > 0) {
				
				try {
					TagManager.getInstance().setProperty(tag, "name", name);
				} catch (StoreException e) {
					if ( Logging.DEBUG)
						e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error editing Tag");
				}
			}
		}
	}

}
