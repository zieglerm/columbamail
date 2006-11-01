package org.columba.mail.gui.tagging;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.MutableTreeNode;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.PluginLoadingFailedException;
import org.columba.core.association.AssociationStore;
import org.columba.core.base.UUIDGenerator;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.gui.tagging.TagList;
import org.columba.core.tagging.api.ITag;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.message.ColumbaHeader;
import org.columba.ristretto.message.Header;

public class MailTagList extends TagList {

	private IFrameMediator frameMediator;

	public MailTagList(IFrameMediator frameMediator) {
		super();

		this.frameMediator = frameMediator;

		addListSelectionListener(new MyListSelectionListener());
	}

	private IMailFolderCommandReference getMessageFromURI(String uri) {
		// example: "columba://org.columba.mail/<folder-id>/<message-id>"
		String s = uri;

		// TODO: @author fdietz replace with regular expression
		int index = s.lastIndexOf('/');
		String messageId = s.substring(index + 1, s.length());
		String folderId = s.substring(s.lastIndexOf('/', index - 1) + 1, index);

		IMailbox folder = (IMailbox) FolderTreeModel.getInstance().getFolder(
				folderId);
		IMailFolderCommandReference r = new MailFolderCommandReference(folder,
				new Object[] { Integer.parseInt(messageId) });

		return r;
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
			Collection<String> uriList = AssociationStore.getInstance()
					.getAssociatedItems("tagging", result.getId());

			// TODO @author hubms show if there is already a virtual folder for
			// this tag
			String uuid = new UUIDGenerator().newUUID();

			// create a virtual folder
			VirtualFolder taggedMessageFolder = new VirtualFolder(
					"Tag Search Result", "VirtualFolder", uuid);

			// should be a MutableTreeNode
			Object root = ((TreeViewOwner) frameMediator).getTreeController()
					.getModel().getRoot();
			if (root instanceof MutableTreeNode)
				taggedMessageFolder.setParent((MutableTreeNode) root);

			for (Iterator<String> it = uriList.iterator(); it.hasNext();) {
				String uri = it.next();
				// skip all non-mail component items
				if ( !uri.startsWith("columba://org.columbam.mail")) continue;
				
				IMailFolderCommandReference r = getMessageFromURI((String) it
						.next());
				try {
					Header header = ((IMailbox) r.getSourceFolder())
							.getHeaderFields(r.getUids()[0], CachedHeaderfields
									.getDefaultHeaderfields());
					ColumbaHeader pHeader = new ColumbaHeader(header);
					taggedMessageFolder.add(pHeader, (IMailbox) r
							.getSourceFolder(), r.getUids()[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// try {
			// taggedMessageFolder.activate();
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			// ensure that we are currently in the mail component
			IFrameMediator newMediator = null;
			try {
				newMediator = FrameManager.getInstance().switchView(
						frameMediator.getContainer(), "ThreePaneMail");
			} catch (PluginLoadingFailedException e) {
				e.printStackTrace();
			}

			// select invisible virtual folder
			((TreeViewOwner) newMediator).getTreeController().setSelected(
					taggedMessageFolder);

			// update message list
			CommandProcessor.getInstance()
					.addOp(
							new ViewHeaderListCommand(newMediator,
									new MailFolderCommandReference(
											taggedMessageFolder)));
		}
	}
}
