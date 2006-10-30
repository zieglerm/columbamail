package org.columba.core.gui.tagging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Simple Add Tag Dialogs which adds a given String as Tag to the tagging
 * component
 * 
 * @author hubms
 * 
 */

public class AddTagDialog extends JDialog implements ActionListener {

	private JTextField field;

	private boolean success = false;

	public AddTagDialog(JFrame parent) {
		super(parent, true);

		setTitle("Add new Tag...");

		JPanel panel = new JPanel(new BorderLayout(0, 10));

		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel label = new JLabel("Tag Name: ");
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

		field = new JTextField("");
		field.setColumns(10);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));

		JButton bAdd = new JButton("Add");
		// bAdd.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		bAdd.addActionListener(this);

		JButton bCancel = new JButton("Cancel");
		bCancel.addActionListener(this);

		panel.add(label, BorderLayout.WEST);
		panel.add(field, BorderLayout.EAST);
		buttonPanel.add(bAdd, BorderLayout.WEST);
		buttonPanel.add(bCancel, BorderLayout.EAST);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(panel, BorderLayout.SOUTH);

		pack();

		setLocationRelativeTo(null);
		setVisible(true);

	}

	public String getTagName() {
		return field.getText();
	}

	public boolean getSuccess() {
		return success;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
			success = true;

			setVisible(false);
		} else if (e.getActionCommand().equals("Cancel")) {
			success = false;

			setVisible(false);
		}
	}

}
