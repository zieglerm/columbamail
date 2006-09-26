package org.columba.mail.gui.tagging.action;

import java.awt.event.ActionEvent;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.tagging.AddTagDialog;
import org.columba.mail.gui.tagging.TagList;

public class AddTagAction extends AbstractColumbaAction {

	final static String ADD_TAG = "Add Tag...";
	
	private TagList tagList;
	
	public AddTagAction(IFrameMediator frameMediator, TagList tagList) {
		super(frameMediator, ADD_TAG);
		this.tagList = tagList;
	}

	public void actionPerformed(ActionEvent arg0) {
		AddTagDialog dialog = new AddTagDialog();
		// will notify the TagList if a tag was added
		dialog.addObserver(tagList);
	}

}
