package org.columba.contact.gui.box;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.columba.addressbook.folder.IContactFolder;
import org.columba.addressbook.gui.dialog.contact.ContactEditorDialog;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.model.IContactModel;
import org.columba.addressbook.model.IContactModelPartial;
import org.columba.api.gui.frame.IContainer;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.gui.frame.api.IComponentBox;
import org.columba.core.logging.Logging;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class ContactBox extends JPanel implements IComponentBox {

	private ContactList list;

	private JTextField textField;

	private JLabel label;

	private IContactFolder selectedFolder;

	public ContactBox() {

		setLayout(new BorderLayout());

		setBackground(UIManager.getColor("TextField.background"));
		
		label = new JLabel("Quick Find:");
		label.setDisplayedMnemonic('F');
		label.setBackground(UIManager.getColor("TextField.background"));
		textField = new JTextField(10);
		label.setLabelFor(textField);
		
		JPanel p = new JPanel();
		p.setBackground(UIManager.getColor("TextField.background"));
		p.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		FormLayout layout = new FormLayout("pref, 2dlu, fill:default:grow",
		// 2 columns
				"fill:default:grow");

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, p);

		builder.append(label);
		builder.append(textField);

		add(p, BorderLayout.NORTH);

		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		selectedFolder = (IContactFolder) model.getFolder("101");

		list = new ContactList();
		list.setModel(new FilteringModel());
		list.addAll(selectedFolder.getHeaderItemList());

		list.installJTextField(textField);

		list.addMouseListener(new DoubleClickListener() {

			@Override
			public void doubleClick(MouseEvent event) {
				IContactModelPartial selected = (IContactModelPartial) list
						.getSelectedValue();

				IContactModel card = null;
				try {
					card = (IContactModel) selectedFolder.get(selected.getId());
				} catch (Exception e) {
					if (Logging.DEBUG)
						e.printStackTrace();
					ErrorDialog.createDialog(e.getMessage(), e);
				}

				IContainer[] container = FrameManager.getInstance()
						.getOpenFrames();
				if (container == null || container.length == 0)
					throw new RuntimeException("No frames available");

				IFrameMediator frameMediator = container[0].getFrameMediator();

				ContactEditorDialog dialog = new ContactEditorDialog(
						frameMediator.getView().getFrame(), card);

				if (dialog.getResult()) {

					try {
						// modify card properties in folder
						selectedFolder.modify(selected.getId(), dialog
								.getDestModel());
					} catch (Exception e1) {
						if (Logging.DEBUG)
							e1.printStackTrace();

						ErrorDialog.createDialog(e1.getMessage(), e1);
					}

				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);

	}

	/**
	 * *************** IComponentBox implementation
	 * *******************************
	 */

	public String getDescription() {
		return "Contact List";
	}

	public ImageIcon getIcon() {
		return null;
	}

	public String getName() {
		return "Contact List";
	}

	public String getTechnicalName() {
		return "contact_list";
	}

	public JComponent getView() {
		return this;
	}

}
