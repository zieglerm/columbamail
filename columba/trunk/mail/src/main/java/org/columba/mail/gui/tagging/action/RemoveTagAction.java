package org.columba.mail.gui.tagging.action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.mail.gui.tagging.TagList;

public class RemoveTagAction extends AbstractColumbaAction {

	final static String REMOVE_TAG = "Remove Tag";
	
	private TagList tagList;

	public RemoveTagAction(IFrameMediator frameMediator, TagList tagList) {
		super(frameMediator, REMOVE_TAG);
		this.tagList = tagList;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		String stringlist = "";
		for (Object o : tagList.getSelectedValues()) {
			stringlist += (stringlist.length() > 0 ? ", " : "") + ((ITag) o).getProperty("name");
		}
		JPanel panel = new JPanel(new BorderLayout(0, 10));

		panel.add(new MultiLineLabel("Remove tag" + (tagList.getSelectedIndices().length > 0 ? "s" : "") + " '"
				+ stringlist + "'?"), BorderLayout.NORTH);

		int n = JOptionPane.showConfirmDialog(FrameManager.getInstance()
				.getActiveFrame(), panel, "Removing tag" + (tagList.getSelectedIndices().length > 0 ? "s" : ""),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null);

		if (n == JOptionPane.YES_OPTION) {
			for (Object o : tagList.getSelectedValues()) {
				// remove tags
				TagManager.getInstance().removeTagById(((ITag) o).getId());
			}
			tagList.update(null, null);
		}
	}

}
