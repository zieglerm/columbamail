package org.columba.mail.folder.command;

import java.net.URI;
import java.net.URISyntaxException;

import org.columba.api.command.IWorkerStatusController;
import org.columba.core.association.AssociationStore;
import org.columba.core.command.Command;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.IMailbox;

public class TagMessageCommand extends Command {

	public final static int ADD_TAG = 0;

	public final static int REMOVE_TAG = 1;

	private int type;

	private String id;

	public TagMessageCommand(IMailFolderCommandReference theReference,
			int type, String id) {
		super(theReference);
		this.type = type;
		this.id = id;
	}

	@Override
	public void execute(IWorkerStatusController worker) throws Exception {
		// get array of source references
		IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();

		// does not work: r.getFolderName()
		// does not work: r.getMessage().getUID()

		// get source folder
		IMailbox srcFolder = (IMailbox) r.getSourceFolder();

		if (type == ADD_TAG) {
			for (Object currentItem : r.getUids()) {
				AssociationStore.getInstance().addAssociation("tagging", id,
						createURI(srcFolder.getId(), currentItem).toString());
			}
		} else if (type == REMOVE_TAG) {
			for (Object currentItem : r.getUids()) {
				AssociationStore.getInstance().removeAssociation("tagging", id,
						createURI(srcFolder.getId(), currentItem).toString());
			}
		}
	}

	// TODO @author hubms: copied from frederiks search builder
	public static URI createURI(String folderId, Object messageId) {
		URI uri = null;
		try {
			uri = new URI("columba://org.columba.mail/" + folderId + "/"
					+ messageId);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
	}

}
