package org.columba.mail.folder.command;

import java.net.URI;
import java.net.URISyntaxException;

import org.columba.api.command.IWorkerStatusController;
import org.columba.core.association.AssociationStore;
import org.columba.core.command.Command;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.IMailbox;

public class TagMessageCommand extends Command {

	public TagMessageCommand(IMailFolderCommandReference theReference) {
		super(theReference);
	}

	@Override
	public void execute(IWorkerStatusController worker) throws Exception {
		// get array of source references
		IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();

		// does not work: r.getFolderName()
		// does not work: r.getMessage().getUID()

		// get source folder
		IMailbox srcFolder = (IMailbox) r.getSourceFolder();

		if (r.getAddTag() != null) {
			for (Object currentItem : r.getUids()) {
				AssociationStore.getInstance().addAssociation("tagging",
						r.getAddTag(),
						createURI(srcFolder.getName(), currentItem).toString());
			}
			r.addTag(null);
		} else {
			for (Object currentItem : r.getUids()) {
				AssociationStore.getInstance().removeAssociation("tagging",
						r.getRemoveTag(),
						createURI(srcFolder.getName(), currentItem).toString());
			}
			r.removeTag(null);
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
