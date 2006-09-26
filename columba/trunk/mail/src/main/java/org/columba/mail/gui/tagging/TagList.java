package org.columba.mail.gui.tagging;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.MutableTreeNode;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.PluginLoadingFailedException;
import org.columba.core.association.AssociationStore;
import org.columba.core.base.UUIDGenerator;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.message.viewer.HeaderSeparatorBorder;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.message.ColumbaHeader;
import org.columba.ristretto.message.Header;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;

/**
 * Show all tags in a docking view, you can create listeners with implementing
 * the ITagListListener
 * 
 * @author hubms
 * 
 */

public class TagList extends JXList implements Observer {

	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.gui.tagging.TagList");

	protected EventListenerList listeners = new EventListenerList();

	private DefaultListModel listModel;

	private int[] oldSelections;

	private IFrameMediator controller;

	private ITag currentClickedTag;

	public TagList(IFrameMediator controller) {
		this.controller = controller;
		listModel = new DefaultListModel();
		setModel(listModel);

		update(null, null);

		setCellRenderer(new MyListCellRenderer());

		setBorder(null);
		setHighlighters(new HighlighterPipeline(
				new Highlighter[] { new RolloverHighlighter(new Color(248, 248,
						248), Color.white) }));
		setRolloverEnabled(true);

		addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent arg0) {
				// return if selection change is in flux
				if (arg0.getValueIsAdjusting()) {
					return;
				}

				if (arg0.getSource() instanceof TagList) {
					TagList tl = (TagList) arg0.getSource();
					// compare selections
					for (int i = 0; i < tl.getSelectedIndices().length; i++) {
						boolean contains = false;
						for (int j = 0; j < oldSelections.length; j++) {
							if (oldSelections[j] == tl.getSelectedIndices()[i])
								contains = true;
						}
						if (!contains) {
							// fire listeners
							fireSelectionChanged((ITag) tl.getElementAt(tl
									.getSelectedIndices()[i]));
						}
					}
					for (int i = 0; i < oldSelections.length; i++) {
						boolean contains = false;
						for (int j = 0; j < tl.getSelectedIndices().length; j++) {
							if (oldSelections[i] == tl.getSelectedIndices()[j])
								contains = true;
						}
						if (!contains) {
							// fire listeners
							fireSelectionChanged((ITag) tl
									.getElementAt(oldSelections[i]));
						}
					}

					oldSelections = tl.getSelectedIndices();
				}

			}

		});

		addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				
				// if there is nothing, set the element to null 
				if (getModel().getSize() == 0) {
					currentClickedTag = null;
					return;
				}
				
				// select row, if right mouse button was pressed
				if (arg0.getButton() == MouseEvent.BUTTON3) {
					// only select, if current item is not selected
					if (!isSelectedIndex(locationToIndex(arg0.getPoint())))
						setSelectedIndex(locationToIndex(arg0.getPoint()));
					currentClickedTag = (ITag) getElementAt(locationToIndex(arg0
							.getPoint()));
				} else if (arg0.getButton() == MouseEvent.BUTTON1) {
					currentClickedTag = (ITag) getElementAt(locationToIndex(arg0
							.getPoint()));
				}
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

		});

		final IFrameMediator mediator = controller;

		addMouseListener(new DoubleClickListener() {

			@Override
			public void doubleClick(MouseEvent event) {

				ITag result = (ITag) getSelectedValue();
				// create a virtual folder with all messages holding this tag
				Collection messageList = AssociationStore.getInstance()
						.getAssociatedItems("tagging", result.getId());

				// TODO @author hubms show if there is already a virtual folder for this tag
				String uuid = new UUIDGenerator().newUUID();

				// create a virtual folder
				VirtualFolder taggedMessageFolder = new VirtualFolder(
						"Tag Search Result", "VirtualFolder",
						uuid);

				// should be a MutableTreeNode
				Object root = ((TreeViewOwner) mediator).getTreeController()
						.getModel().getRoot();
				if (root instanceof MutableTreeNode)
					taggedMessageFolder.setParent((MutableTreeNode) root);

				for (Iterator it = messageList.iterator(); it.hasNext();) {
					IMailFolderCommandReference r = getMessageFromURI((String) it
							.next());
					try {
						Header header = ((IMailbox) r.getSourceFolder())
								.getHeaderFields(r.getUids()[0],
										CachedHeaderfields
												.getDefaultHeaderfields());
						ColumbaHeader pHeader = new ColumbaHeader(header);
						taggedMessageFolder.add(pHeader, (IMailbox) r
								.getSourceFolder(), r.getUids()[0]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				try {
					taggedMessageFolder.activate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// ensure that we are currently in the mail component
				IFrameMediator newMediator = null;
				try {
					newMediator = FrameManager.getInstance().switchView(
							mediator.getContainer(), "ThreePaneMail");
				} catch (PluginLoadingFailedException e) {
					e.printStackTrace();
				}

				// select invisible virtual folder
				((TreeViewOwner) newMediator).getTreeController().setSelected(
						taggedMessageFolder);

				// update message list
				CommandProcessor.getInstance().addOp(
						new ViewHeaderListCommand(newMediator,
								new MailFolderCommandReference(
										taggedMessageFolder)));

			}
		});

	}

	public ITag getCurrentClickedTag() {
		// returns only one tag, the mouse has currently clicked
		return currentClickedTag;
	}

	public void addAll(Iterator<ITag> it) {
		while (it.hasNext()) {
			listModel.addElement(it.next());
		}
	}

	public void addAll(List<ITag> result) {
		Iterator<ITag> it = result.iterator();
		addAll(it);
	}

	public void add(ITag result) {
		listModel.addElement(result);
	}

	public void clear() {
		listModel.clear();
	}

	class MyListCellRenderer extends JPanel implements ListCellRenderer {

		private Border lineBorder = new HeaderSeparatorBorder(new Color(230,
				230, 230));

		private JLabel nameLabel = new JLabel();

		private JLabel descriptionLabel = new JLabel();

		private JLabel idLabel = new JLabel();

		MyListCellRenderer() {
			setLayout(new BorderLayout());

			add(nameLabel, BorderLayout.WEST);
			add(descriptionLabel, BorderLayout.CENTER);
			add(idLabel, BorderLayout.EAST);

			setBorder(BorderFactory.createCompoundBorder(lineBorder,
					BorderFactory.createEmptyBorder(2, 2, 2, 2)));

			setOpaque(true);

		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			ITag result = (ITag) value;

			idLabel.setText("(id = " + result.getId() + ")");
			idLabel.setForeground(new Color(100, 100, 100));
			nameLabel.setText(result.getProperty("name"));
			descriptionLabel.setText(result.getProperty("description"));

			return this;
		}

	}

	public void fireSelectionChanged(ITag tag) {
		// Guaranteed to return a non-null array
		Object[] ls = listeners.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = ls.length - 2; i >= 0; i -= 2) {
			if (ls[i] == ITagListListener.class) {
				((ITagListListener) ls[i + 1]).selectionChanged(tag);
			}
		}
	}

	public void addListener(ITagListListener l) {
		this.listeners.add(ITagListListener.class, l);
	}

	public void removeListener(ITagListListener l) {
		this.listeners.remove(ITagListListener.class, l);
	}

	public void update(Observable arg0, Object arg1) {
		this.oldSelections = new int[0];
		clear();
		addAll(TagManager.getInstance().getAllTags());
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

}
