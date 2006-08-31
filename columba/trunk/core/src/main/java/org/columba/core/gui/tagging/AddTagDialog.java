package org.columba.core.gui.tagging;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.tagging.TagManager;

public class AddTagDialog extends JDialog implements ActionListener {

	private JTextField field;

	public AddTagDialog() {
		setTitle("Add new Tag...");

		JPanel panel = new JPanel(new BorderLayout(0, 10));

		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel label = new JLabel("New Tag Name: ");
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

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
			// TODO @author hubms: error message if not created
			if (TagManager.getInstance().addTag(field.getText()) != null) {
				JPanel panel = new JPanel(new BorderLayout(0, 10));

				panel.add(new MultiLineLabel("Successfully added tag '"
						+ field.getText() + "'!"), BorderLayout.NORTH);

				// Some error in the client/server communication
				// --> fall back to default login process
				JOptionPane.showMessageDialog(FrameManager.getInstance()
						.getActiveFrame(), panel, "Successfully added Tag",
						JOptionPane.INFORMATION_MESSAGE, null);
			} else {
				JPanel panel = new JPanel(new BorderLayout(0, 10));

				panel.add(new MultiLineLabel("Could not add tag '"
						+ field.getText() + "' to tag file tags.xml!"),
						BorderLayout.NORTH);

				// Some error in the client/server communication
				// --> fall back to default login process
				JOptionPane.showMessageDialog(FrameManager.getInstance()
						.getActiveFrame(), panel, "Error adding tag",
						JOptionPane.ERROR_MESSAGE, null);
			}
			dispose();
		} else if (e.getActionCommand().equals("Cancel")) {
			dispose();
		}
	}

}
