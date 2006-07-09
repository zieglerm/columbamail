package org.frapuccino.dynamicitemlistpanel;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.frapuccino.DemoComponent;
import org.frapuccino.dynamicitemlistpanel.DynamicItemListPanel;

public class DynamicItemListDemo implements DemoComponent {

	public DynamicItemListDemo() {
		super();

	}

	public JComponent getComponent() {

		// add "more.." label
		JLabel label = new JLabel("more...");
		label.setForeground(Color.blue);
		
		DynamicItemListPanel p = new DynamicItemListPanel(2, label, false);
		p.addItem(new JLabel("test1"));
		p.addItem(new JLabel("test2"));
		p.addItem(new JLabel("test3"));
		p.addItem(new JLabel("test4"));
		p.addItem(new JLabel("test5"));
		p.addItem(new JLabel("test6"));
		p.addItem(new JLabel("tes7"));
		p.addItem(new JLabel("test8"));
		p.addItem(new JLabel("test9"));
		p.addItem(new JLabel("test10"));

		return p;
	}

	public String getDescription() {
		return "Item list resizes automatically when the parent container size changes.\nA maximum allowed row number is used for line wrapping. Additionally, if there's not enough space a delimter shows that there's more information is available.";
	}

	public String getDemoName() {
		return "Dynamic Item List";
	}

}
