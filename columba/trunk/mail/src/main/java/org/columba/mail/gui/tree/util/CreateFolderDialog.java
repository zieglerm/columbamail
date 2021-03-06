// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.tree.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.SingleSideEtchedBorder;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.util.MailResourceLoader;
import org.frapuccino.swing.SortedJTree;
import org.frapuccino.swing.SortedTreeModelDecorator;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;


public class CreateFolderDialog extends JDialog implements ActionListener {
	protected boolean bool = false;

	protected JTextField textField;

	protected JButton okButton;

	protected JButton cancelButton;

	protected JButton helpButton;

	protected JComboBox typeBox;

	protected JTree tree;

	protected String name;

	protected TreePath selected;

	protected IFrameMediator mediator;

	public CreateFolderDialog(IFrameMediator mediator, TreePath selected) {
		super(mediator.getView().getFrame(), MailResourceLoader.getString(
				"dialog", "folder", "edit_name"), true);

		this.mediator = mediator;

		name = MailResourceLoader.getString("dialog", "folder",
				"new_folder_name");

		this.selected = selected;

		initComponents();
		layoutComponents();
		// try to set selection
		if (selected != null) {
			tree.setSelectionPath(selected);
		}

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	protected void layoutComponents() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// layout center panel
		FormLayout layout = new FormLayout(
				"left:max(20dlu;pref), 3dlu, 80dlu:grow", "");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		// create EmptyBorder between components and dialog-frame
		builder.setDefaultDialogBorder();

		// skip the first column
		// builder.setLeadingColumnOffset(1);
		// Add components to the panel:
		builder.append(new JLabel(MailResourceLoader.getString("dialog",
				"folder", "name")));
		builder.append(textField);
		builder.nextLine();

		builder.append(new JLabel("Type:"));
		builder.append(typeBox);

		builder.appendRow("3dlu");
		builder.appendRow("fill:d:grow");
		builder.nextLine(2);

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(200, 300));
		builder.append(scrollPane, 3);

		contentPane.add(builder.getPanel(), BorderLayout.CENTER);

		// init bottom panel with OK, Cancel buttons
		JPanel bottomPanel = new JPanel(new BorderLayout(0, 0));
		bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		buttonPanel.add(okButton);

		buttonPanel.add(cancelButton);
		bottomPanel.add(buttonPanel, BorderLayout.EAST);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
		getRootPane().registerKeyboardAction(this, "CANCEL",
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	protected void initComponents() {
		// Init components
		textField = new JTextField(name, 15);
		textField.setSelectionStart(0);
		textField.setSelectionEnd(name.length());

		typeBox = new JComboBox();

		// get global sorting state
		TreeModel t = ((TreeViewOwner) mediator).getTreeController().getModel();

		if (t instanceof SortedTreeModelDecorator) {
			// sorting is enabled
			SortedTreeModelDecorator treemodel = (SortedTreeModelDecorator) t;
			Comparator c = treemodel.getSortingComparator();

			tree = new SortedJTree(FolderTreeModel.getInstance());
			// apply sorting state
			SortedTreeModelDecorator m = (SortedTreeModelDecorator) tree
					.getModel();
			m.setSortingComparator(c);
		} else {
			// sorting is disabled
			tree = new SortedJTree(FolderTreeModel.getInstance());
		}

		tree.setCellRenderer(new FolderTreeCellRenderer());
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.expandRow(0);
		tree.expandRow(1);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				selectionChanged();
			}
		});

		// button panel
		okButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "ok"));
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		cancelButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "cancel"));
		cancelButton.setActionCommand("CANCEL");
		cancelButton.addActionListener(this);
		helpButton = new ButtonWithMnemonic(MailResourceLoader.getString(
				"global", "help"));
		helpButton.setActionCommand("HELP");
		helpButton.addActionListener(this);

	}

	protected void selectionChanged() {
		IMailFolder selected = getSelected();
		if (selected != null) {
			List childs = FolderFactory.getInstance().getPossibleChilds(
					selected);
			Iterator it = childs.iterator();
			while (it.hasNext()) {
				String type = (String) it.next();
				if (!selected.supportsAddFolder(type)) {
					it.remove();
				}

				// We have a special Command for VFolders
				if (type.equals("VirtualFolder")) {
					it.remove();
				}
			}

			if (childs.size() > 0) {
				typeBox.setModel(new DefaultComboBoxModel(childs.toArray()));

				okButton.setEnabled(true);
				typeBox.setEnabled(true);
			} else {
				okButton.setEnabled(false);
				typeBox.setEnabled(false);
			}
		} else {
			okButton.setEnabled(false);
			typeBox.setEnabled(false);
		}
	}

	public String getName() {
		return name;
	}

	public IMailFolder getSelected() {
		if ( tree.getSelectionPath() == null ) return null;

		return (IMailFolder) tree.getSelectionPath().getLastPathComponent();
	}

	public boolean success() {
		return bool;
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals("OK")) {
			name = textField.getText().trim();

			// fixing bug with id 553176
			if (name.indexOf('/') != -1) {
				// if the character / is found shows the user a error message
				JOptionPane.showMessageDialog(this, MailResourceLoader
						.getString("dialog", "folder", "error_char_text"),
						MailResourceLoader.getString("dialog", "folder",
								"error_title"), JOptionPane.ERROR_MESSAGE);

				return;
			}

			bool = true;
			setVisible(false);
		} else if (action.equals("CANCEL")) {
			bool = false;

			dispose();
		}
	}

	/**
	 * @return
	 */
	public String getFolderType() {
		return (String) typeBox.getSelectedItem();
	}
}