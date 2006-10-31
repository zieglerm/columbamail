package org.columba.contact.gui.box;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.columba.addressbook.folder.IContactFolder;
import org.columba.addressbook.gui.base.FolderComboBox;
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
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class ContactBox extends JPanel implements IComponentBox {

	private ContactList list;

	private JTextField textField;

	private JLabel label;

	private IContactFolder selectedFolder;

	private FolderComboBox folderComboBox;
	
	public ContactBox() {

		setLayout(new BorderLayout());

		//setBackground(UIManager.getColor("TextField.background"));
		
		label = new JLabel("Quick Find:");
		label.setDisplayedMnemonic('F');
		//label.setBackground(UIManager.getColor("TextField.background"));
		textField = new JTextField(10);
		label.setLabelFor(textField);
			

		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		selectedFolder = (IContactFolder) model.getFolder("101");

		list = new ContactList();
		list.setModel(new FilteringModel());
		list.addAll(selectedFolder.getHeaderItemList());

		
		folderComboBox = new FolderComboBox(false);
		folderComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				list.uninstallJTextField(textField);
				selectedFolder = (IContactFolder) folderComboBox.getSelectedItem();
				
				List<IContactModelPartial> contactList = selectedFolder.getHeaderItemList();
				list.setModel(new FilteringModel());
				list.addAll(contactList);
				list.installJTextField(textField);
				textField.setText(textField.getText());
			}
		});
		
		
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

		
		JPanel p = new JPanel();
		//p.setBackground(UIManager.getColor("TextField.background"));
		p.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		FormLayout layout = new FormLayout("pref, 2dlu, pref, 2dlu, fill:default:grow",
		// 2 columns
				"fill:default:grow");

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, p);

		builder.append(folderComboBox);
		builder.append(label);
		builder.append(textField);

		add(p, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);

	}

	/**
	 * *************** IComponentBox implementation
	 * *******************************
	 */

	public String getDescription() {
		return "Contacts";
	}

	public ImageIcon getIcon() {
		return ImageLoader.getSmallIcon(IconKeys.ADDRESSBOOK);
	}

	public String getName() {
		return "Contacts";
	}

	public String getTechnicalName() {
		return "contact_list";
	}

	public JComponent getView() {
		return this;
	}

}
