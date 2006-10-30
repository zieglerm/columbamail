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

import org.columba.core.tagging.api.ITag;

public class EditTagDialog extends JDialog implements ActionListener {
	
	private JTextField field;
	
	private ITag tag;
	
	private boolean success = false;
	
	public EditTagDialog(JFrame frame, ITag tag) {
		super(frame, true);
		
		this.tag = tag;
		
		setTitle("Edit Tag");
		
		JPanel panel = new JPanel(new BorderLayout(0, 10));

		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		
		JLabel lCurrentName = new JLabel("Current name: " + tag.getProperty("name"));
		

		JLabel label = new JLabel("New tag name: ");
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

		field = new JTextField("");
		field.setColumns(10);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));

		JButton bAdd = new JButton("Edit");
		// bAdd.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		bAdd.addActionListener(this);

		JButton bCancel = new JButton("Cancel");
		bCancel.addActionListener(this);

		panel.add(lCurrentName, BorderLayout.NORTH);
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
	
	public boolean getSuccess() {
		return success;
	}

	public String getTagName() {
		return field.getText();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Edit")) {
			success = true;
			dispose();
		} else if (arg0.getActionCommand().equals("Cancel")) {
			dispose();
			success = false;
		}
	}
}
