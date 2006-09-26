package org.columba.mail.gui.tagging.action;

import java.awt.event.ActionEvent;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.mail.gui.tagging.EditTagDialog;
import org.columba.mail.gui.tagging.TagList;

public class EditTagAction extends AbstractColumbaAction {

	final static String EDIT_TAG = "Edit Tag...";
	
	private TagList tagList;
	
	public EditTagAction(IFrameMediator frameMediator, TagList tagList) {
		super(frameMediator, EDIT_TAG);
		this.tagList = tagList;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		EditTagDialog editTagDialog = new EditTagDialog(tagList.getCurrentClickedTag());
		editTagDialog.addObserver(tagList);
		// tagList.update(editTagDialog.getObservable(), null);
	}

}
