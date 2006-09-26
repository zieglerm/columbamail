package org.columba.mail.gui.tagging;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.mail.gui.tagging.action.AddTagAction;
import org.columba.mail.gui.tagging.action.EditTagAction;
import org.columba.mail.gui.tagging.action.RemoveTagAction;

public class TagPopupMenu extends JPopupMenu implements ActionListener {
	
	TagList tagList;
	
	public TagPopupMenu(IFrameMediator controller, TagList tagList) {
		this.tagList = tagList;
		add(new AddTagAction(controller, tagList));
		add(new EditTagAction(controller, tagList));
		add(new RemoveTagAction(controller, tagList));
	}

	public void actionPerformed(ActionEvent arg0) {
	}

}
